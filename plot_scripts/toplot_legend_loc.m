function toplot_legend_loc(xaxis,yaxis,plotname,h_legend,new_location)
  %,legend1,legend2)
  fontsize_val=15;
  
  %horrible hack placed just to get the XTickLabels to start showing up with the 
  %right size in the err_box_plot.m output
  set(findobj(gca,'Type','text'),'FontSize',fontsize_val-4);
  
  %end horrible hack

  xlabel(xaxis,'FontName','Times New Roman','FontSize',fontsize_val,'Fontweight','Normal')
  ylabel(yaxis,'FontName','Times New Roman','FontSize',fontsize_val,'Fontweight','Normal')
  title(strcat(plotname),'FontSize',fontsize_val+2,'Fontweight','Normal','FontName','Times New Roman')
  set(gcf, 'PaperUnits', 'inches');
  set(gcf, 'PaperSize', [7.5 7.5]);
  set(gcf, 'PaperPositionMode', 'auto');
  set(gcf, 'PaperPosition', [0 0 7.5 7.5  ]);
  set(gca,'Fontsize',fontsize_val,'Fontname','Times New Roman','Fontweight','Normal');
  
  
  %h_legend = legend(legend1,legend2);
  set(h_legend,'Fontsize',fontsize_val,'Fontname','Times New Roman','Location',new_location);
  %rect = [0.62, 0.12, .25, .25];
  %set(h_legend, 'Position', rect)
  legend boxoff;
end
