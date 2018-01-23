function toplot(xaxis,yaxis,plotname)
  %,legend1,legend2)
  fontsize_val=17;
  
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
  %set(h_legend,'Fontsize',fontsize_val,'Fontname','Times New Roman','location','best'); 
end
