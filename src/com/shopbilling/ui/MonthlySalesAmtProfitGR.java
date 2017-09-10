package com.shopbilling.ui;

import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;

import com.shopbilling.dto.GraphDTO;
import com.shopbilling.services.GraphServices;

public class MonthlySalesAmtProfitGR extends JInternalFrame {
	/**
	 * Create the frame.
	 */
	public MonthlySalesAmtProfitGR() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Monthly Sales Amount & Profit Report");
		setBorder(null);
		getContentPane().setLayout(null);

		//Create Dataset
		//Create list of Last 12 Months
		List<String> dateList = getLast12Months();
		
		List<GraphDTO> graphList = GraphServices.getMonthlySalesReport();
		
		List<GraphDTO> graphFinalList = new ArrayList<GraphDTO>();
		
		HashMap<String,GraphDTO> dateMap = new HashMap<String, GraphDTO>();
		for (GraphDTO gr :graphList){
			dateMap.put(gr.getDate(), gr);
		}
		
		for(String dt :dateList){
			GraphDTO grp = new GraphDTO();
			if(dateMap.containsKey(dt)){
				grp.setDate(dt);
				grp.setTotalCollection(dateMap.get(dt).getTotalCollection());
				grp.setTotalPurchaseAmt(dateMap.get(dt).getTotalPurchaseAmt());
			}else{
				grp.setDate(dt);
				grp.setTotalCollection(0);
				grp.setTotalPurchaseAmt(0);
			}
			graphFinalList.add(grp);
			
		}
		DefaultCategoryDataset objDataset = new DefaultCategoryDataset();

		for(GraphDTO grph : graphFinalList){
			objDataset.setValue(grph.getTotalCollection(),"Total Sales Amount",grph.getDate());
			objDataset.setValue(grph.getTotalProfit(),"Total Profit",grph.getDate());
		}
		
		JFreeChart chart = ChartFactory.createBarChart(
			    "Monthly Sales Amount & Profit Amount",     //Chart title
			    "Last 12 Month's Sales Report",     //Domain axis label
			    "Amount",         //Range axis label
			    objDataset,         //Chart Data 
			    PlotOrientation.VERTICAL, // orientation
			    true,             // include legend?
			    true,             // include tooltips?
			    false             // include URLs?
			);
		
		BufferedImage image = chart.createBufferedImage(1000,600);
		JLabel lblChart = new JLabel();
		lblChart.setIcon(new ImageIcon(image));
		lblChart.setBounds(73,30,1050,610);
		getContentPane().add(lblChart);
	}
	
	private List<String> getLast12Months() {
	    List<String> monthList = new ArrayList<String>();
	    SimpleDateFormat sdf = new SimpleDateFormat("MMM yy");
	    DateTime currentMonth = new DateTime();
	    monthList.add(sdf.format(currentMonth.toDate()));
	    
	    int i =0;
	    while(i<12){
	    	DateTime month = currentMonth.minusMonths(1);
	    	monthList.add(sdf.format(month.toDate()));
	    	currentMonth = month;
	    	i++;
	    }
	    Collections.reverse(monthList);
	    return monthList;
	}
}
