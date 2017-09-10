package com.shopbilling.ui;

import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;

import com.shopbilling.dto.GraphDTO;
import com.shopbilling.services.GraphServices;

public class DailySalesAmtProfitGR extends JInternalFrame {
	/**
	 * Create the frame.
	 */
	public DailySalesAmtProfitGR() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Daily Sales Amount & Profit Report");
		setBorder(null);
		getContentPane().setLayout(null);

		//Create Dataset
		Date todaysDate = new Date();
		DateTime dateTime = new DateTime();
		Date backDate = dateTime.minusDays(7).toDate();
		//Create list of Last 7 Days Date Range
		List<String> dateList = getListOfDaysBetweenTwoDates(backDate,todaysDate);
		
		List<GraphDTO> graphList = GraphServices.getDailySalesReport();
		
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
			    "Daily Sales Amount & Profit Amount",     //Chart title
			    "Last 7 Daily Sales Report",     //Domain axis label
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
		ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
	
	private List<String> getListOfDaysBetweenTwoDates(Date startDate, Date endDate) {
	    List<String> dates = new ArrayList<String>();
	    Calendar start = Calendar.getInstance();
	    start.setTime(startDate);
	    Calendar end = Calendar.getInstance();
	    SimpleDateFormat sdf= new SimpleDateFormat("dd MMM yy");
	    end.setTime(endDate);
	    end.add(Calendar.DAY_OF_YEAR, 1);
	    while (start.before(end)) {
	        dates.add(sdf.format(start.getTime()));
	        start.add(Calendar.DAY_OF_YEAR, 1);
	    }
	    return dates;
	}
}
