%normal_execution_paths = [ 19 42 76 123 185 264 362 481 623 790 984 1207 1461 1748 2070 2429 2827 3266 3748 4275 4849 5472 6146 6873 7655];
%veritesting_execution_paths = 3:27;

%x_vals=3:2:51;

len = 20;
x_vals=1:1:len;
normal_execution_paths = 3.^(x_vals+1);
veritesting_execution_paths = repmat(3, len, 1);

figure
semilogy(x_vals,normal_execution_paths,'r--','Linewidth',4);

hold on;
semilogy(x_vals,veritesting_execution_paths,'b-.','Linewidth',4);

set(gca,'yscale','log');
set(gca,'FontSize',15); 
h_legend=legend('Vanilla SPF','SPF with Veritesting','Location','northwest');
xlabel('values of \it{len}');
ylabel('# of execution paths');
xlim([1 len]);
%ylim([1 10000]);
set(gca,'FontSize',15);

%toplot_legend_loc('allowed range of symbolic integers','number of execution paths','',h_legend,'northwest');  


print -depsc '../figures/veritesting_example_semilogy'

x = 0:0.1:10;
y = exp(x);
