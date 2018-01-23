#!/usr/bin/perl

use strict;


die "Usage: count-insns.pl <path-to-toplevel-classes-dir>"
  unless @ARGV == 1;
my ($class_files_list) = @ARGV;

my @class_files;

open(F, "<$class_files_list") or die;
while (<F>) {
    push @class_files, $_;
}
close F;

printf("#class_files = %d\n", scalar(@class_files));

my ($g_if_ret_d, $g_if_iv_d, $g_if_throw_d) = (0,0,0);
my ($g_if_ret_cnt, $g_if_iv_cnt, $g_if_throw_cnt) = (0,0,0);

for(my $i=0; $i<=$#class_files; $i++) {
    
    my $class_file = $class_files[$i];
    $class_file =~ s/\$/\\\$/;
    $class_file =~ s/\n//;
    my @args = ("javap","-c", $class_file);
    my @printable;
    for my $a (@args) {
	if ($a =~ /[\s|<>]/) {
	    push @printable, "'$a'";
	} else {
	    push @printable, $a;
	}
    }
    print "@printable\n";

    open(LOG, "-|", @args);

    my @iv_offsets = ();
    my @ret_offsets = ();
    my @if_offsets = ();
    my @throw_offsets = ();
    my $index = 0;

    my $total_ret = 0;
    my $total_if = 0;
    my $total_iv = 0;
    my $total_throw = 0;
    my $num_methods = 0;

    my $total_if_ret_d = 0;
    my $total_if_iv_d = 0;
    my $total_if_throw_d = 0;

    my $if_ret_d = 0;
    my $if_throw_d = 0;
    my $if_iv_d = 0;

    my $if_ret_cnt = 0;
    my $if_throw_cnt = 0;
    my $if_iv_cnt = 0;

    my $last_if = -1;

    while (<LOG>) {
	if(/^$/) {
	    # printf("Method body ended\n");;
	    # printf("iv = @iv_offsets\n");;
	    # printf("if = @if_offsets\n");;
	    # printf("ret = @ret_offsets\n");;
	    # printf("throw = @throw_offsets\n");;
	    if($index > 0) { $num_methods++; }
	    $index=0;
	    $total_ret += scalar(@ret_offsets);
	    $total_if += scalar(@if_offsets);
	    $total_iv += scalar(@iv_offsets);
	    $total_throw += scalar(@throw_offsets);
	    
	    $total_if_ret_d += $if_ret_d;
	    if($if_ret_d != 0) { $if_ret_cnt++; }
	    $total_if_iv_d += $if_iv_d;
	    if($if_iv_d != 0) { $if_iv_cnt++; }
	    $total_if_throw_d += $if_throw_d;
	    if($if_throw_d != 0) { $if_throw_cnt++; }
	    # printf("total-if-ret-dist = $total_if_ret_d\n");
	    # printf("total-if-throw-dist = $total_if_throw_d\n");
	    # printf("total-if-iv-dist = $total_if_iv_d\n");
	    # printf("Method body started\n");;

	    @iv_offsets = ();
	    @if_offsets = ();
	    @ret_offsets = ();
	    @throw_offsets = ();
	    $if_ret_d = 0;
	    $if_iv_d = 0;
	    $if_throw_d = 0;
	    $last_if = -1;
	} elsif(/^.*invokevirtual.*$/ || /^.*invokeinterface.*$/) {
	    # printf("found iv\n");
	    push @iv_offsets, $index;
	    if($last_if != -1 && $if_iv_d == 0) {
		$if_iv_d = $index - $last_if;
	    }
	} elsif(/^.*if.*$/) {
	    push @if_offsets, $index;
	    $last_if = $index;
	    if($if_ret_d != 0) {
		$total_if_ret_d += $if_ret_d;
		$if_ret_cnt++;
		$if_ret_d = 0;
	    }
	    if($if_throw_d != 0) {
		$total_if_throw_d += $if_throw_d;
		$if_throw_cnt++;
		$if_throw_d = 0;
	    }
	    if($if_iv_d != 0) {
		$total_if_iv_d += $if_iv_d;
		$if_iv_cnt++;
		$if_iv_d = 0;
	    }
	} elsif(/^.*return.*$/) {
	    push @ret_offsets, $index;
	    if($last_if != -1 && $if_ret_d == 0) {
		$if_ret_d = $index - $last_if;
	    }
	} elsif(/^.*throw.*$/ && $index > 0) {
	    push @throw_offsets, $index;
	    if($last_if != -1 && $if_throw_d == 0) {
		$if_throw_d = $index - $last_if;
	    }
	}
	# print "$_";
	if(/^.*[0-9]+: .*$/) {
	    $index++;
	}
    }
    printf("classfile = $class_file\n");
    printf("number of methods = $num_methods\n");
    printf("total #ret = $total_ret\n");
    printf("total #if = $total_if\n");
    printf("total #iv = $total_iv\n");
    printf("total #throw = $total_throw\n");
    printf("total #if-ret-dist = $total_if_ret_d ($if_ret_cnt)\n");
    printf("total #if-iv-dist = $total_if_iv_d ($if_iv_cnt)\n");
    printf("total #if-throw-dist = $total_if_throw_d ($if_throw_cnt)\n");
    printf("\n");
    $g_if_ret_d += $total_if_ret_d;
    $g_if_ret_cnt += $if_ret_cnt;
    $g_if_iv_d += $total_if_iv_d;
    $g_if_iv_cnt += $if_iv_cnt;
    $g_if_throw_d += $total_if_throw_d;
    $g_if_throw_cnt += $if_throw_cnt;
}

printf("global #if-ret-dist = $g_if_ret_d ($g_if_ret_cnt)"); 
if($g_if_ret_cnt != 0) { printf(", avg = %f\n", $g_if_ret_d/$g_if_ret_cnt); } 
else { printf("\n"); }
printf("global #if-iv-dist = $g_if_iv_d ($g_if_iv_cnt)"); 
if($g_if_iv_cnt != 0) { printf(", avg = %f\n", $g_if_iv_d/$g_if_iv_cnt); } 
else { printf("\n"); }
printf("global #if-throw-dist = $g_if_throw_d ($g_if_throw_cnt)");
if($g_if_throw_cnt != 0) { printf(", avg = %f\n", $g_if_throw_d/$g_if_throw_cnt); }
printf("\n");
