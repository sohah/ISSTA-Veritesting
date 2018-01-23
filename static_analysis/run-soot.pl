#!/usr/bin/perl

use strict;


die "Usage: run-soot.pl <path-to-toplevel-classes-dir> <path-to-classes-file>"
  unless @ARGV == 2;
my ($class_files_dir, $class_files_list) = @ARGV;

my @class_files;

open(F, "<$class_files_list") or die;
while (<F>) {
    push @class_files, $_;
}
close F;

printf("#class_files = %d\n", scalar(@class_files));

my ($g_if_ret_d, $g_if_iv_d, $g_if_throw_d) = (0,0,0);
my ($g_if_ret_count, $g_if_iv_count, $g_if_throw_count) = (0,0,0);
my ($g_num_methods, $g_v_region_size, $g_v_region_count) = (0,0,0);

for(my $i=0; $i<=$#class_files; $i++) {
    
    my $class_file = $class_files[$i];
    # $class_file =~ s/\$/\\\$/;
    $class_file =~ s/\n//;
    $class_file =~ s/\//./g;
    $class_file =~ s/\.class//g;
    $class_file = substr($class_file, 2);
    my @args = ("java","-Xms4096m", "SimpleMain", "-keep-offset", 
		"-pp", "-cp" ,$class_files_dir, "-src-prec", "c", 
		"-f", "S", $class_file);
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

    while (<LOG>) {
	if(/^if-iv-distance = ([0-9]+) \(([0-9]+)\)$/) {
	    $g_if_iv_d += $1;
	    $g_if_iv_count += $2;
	} elsif(/^if-ret-distance = ([0-9]+) \(([0-9]+)\)$/) {
	    $g_if_ret_d += $1;
	    $g_if_ret_count += $2;
	} elsif(/^if-throw-distance = ([0-9]+) \(([0-9]+)\)$/) {
	    $g_if_throw_d += $1;
	    $g_if_throw_count += $2;
	} elsif(/^pure-Veritesting-regions-size = ([0-9]+) \(([0-9]+)\)$/) {
	    $g_v_region_size += $1;
	    $g_v_region_count += $2;
	} elsif(/^number-of-methods = ([0-9]+)$/) {
	    $g_num_methods += $1;
	}
	print "$_";
    }
    printf("classfile = $class_file\n");
    printf("\n");
}

printf("global #if-ret-dist = $g_if_ret_d ($g_if_ret_count)"); 
if($g_if_ret_count != 0) { printf(", avg = %f\n", $g_if_ret_d/$g_if_ret_count); } 
else { printf("\n"); }
printf("global #if-iv-dist = $g_if_iv_d ($g_if_iv_count)"); 
if($g_if_iv_count != 0) { printf(", avg = %f\n", $g_if_iv_d/$g_if_iv_count); } 
else { printf("\n"); }
printf("global #if-throw-dist = $g_if_throw_d ($g_if_throw_count)");
if($g_if_throw_count != 0) { printf(", avg = %f\n", $g_if_throw_d/$g_if_throw_count); }
printf("global v_region_size = $g_v_region_size ($g_v_region_count)");
if($g_v_region_count != 0) { printf(", avg = %f\n", $g_v_region_size/$g_v_region_count); }
printf("\n");
