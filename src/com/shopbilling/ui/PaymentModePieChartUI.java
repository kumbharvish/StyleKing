package com.shopbilling.ui;

import java.awt.image.BufferedImage;
import java.sql.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import com.shopbilling.dto.GraphDTO;
import com.shopbilling.services.GraphServices;
import com.shopbilling.utils.PDFUtils;

public class PaymentModePieChartUI extends JInternalFrame {
	/**
	 * Create the frame.
	 */
	public PaymentModePieChartUI(Date fromDate,Date toDate) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setBorder(null);
		getContentPane().setLayout(null);

		DefaultPieDataset pieDataset = new DefaultPieDataset();
		List<GraphDTO> list = GraphServices.getPaymentModeAmounts(fromDate,toDate);
			if(list.isEmpty()){
				JOptionPane.showMessageDialog(getContentPane(), "No Records found !");
			}else{
				for(GraphDTO graph :list){
					pieDataset.setValue(graph.getPaymentMode()+" : "+PDFUtils.getDecimalFormat(graph.getTotalAmount()), graph.getTotalAmount());
					pieDataset.setValue(graph.getPaymentMode()+" : "+PDFUtils.getDecimalFormat(graph.getTotalAmount()), graph.getTotalAmount());
				}
				JFreeChart chart = ChartFactory.createPieChart("Payment Mode Wise Sales Amount",pieDataset);
				BufferedImage image = chart.createBufferedImage(1000,300);
				JLabel lblChart = new JLabel();
				lblChart.setIcon(new ImageIcon(image));
				lblChart.setBounds(73,11,1050,366);
				getContentPane().add(lblChart);
			}
			ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
}
