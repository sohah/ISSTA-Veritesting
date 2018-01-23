seconds = [ 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 3 8 16 27 52 100 194 387 777 1537 3066 6149];
%           1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 21 22 23 24  25  26  27  28   29   30

x_vals=1:30;

hold on;

plot(x_vals,seconds,'r-','Linewidth',2);



set(gca,'FontSize',15); 
h_legend=0; %legend('SPF','Location','northwest');
xlabel('value of \it{bound}');
ylabel('Time (seconds) to complete exploration');
xlim([18 30]);
%ylim([0 20]);
set(gca,'FontSize',15);

%toplot('increasing \it{bound}','Time (seconds) to complete exploration','');  

hold off;

print -depsc '../figures/sharing_time'
