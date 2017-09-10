package com.shopbilling.ui;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

public class PaymentModeWiseCollectionGR extends JInternalFrame {
	
	private PaymentModePieChartUI paymentModePieChart;
	private JPanel containerPanel;
	private JDateChooser fromDateChooser;
	private JDateChooser toDateChooser;
	/**
	 * Create the frame.
	 */
	public PaymentModeWiseCollectionGR() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Payment Mode Wise Report");
		getContentPane().setLayout(null);
		
		
		containerPanel = new JPanel();
		containerPanel.setBounds(10, 129, 1150, 483);
		getContentPane().add(containerPanel);
		containerPanel.setLayout(new CardLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Report Period", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 1150, 103);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("From Date :");
		label.setFont(new Font("Tahoma", Font.BOLD, 14));
		label.setBounds(195, 32, 99, 28);
		panel.add(label);
		
		fromDateChooser = new JDateChooser(new Date());
		fromDateChooser.setFont(new Font("Tahoma", Font.BOLD, 12));
		fromDateChooser.setBounds(304, 32, 133, 28);
		panel.add(fromDateChooser);
		
		JLabel label_1 = new JLabel("To Date :");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		label_1.setBounds(528, 32, 79, 28);
		panel.add(label_1);
		
		toDateChooser = new JDateChooser(new Date());
		toDateChooser.setFont(new Font("Tahoma", Font.BOLD, 12));
		toDateChooser.setBounds(617, 32, 133, 28);
		panel.add(toDateChooser);
		
		JButton btnGetReport = new JButton("Get Report");
		btnGetReport.setIcon(new ImageIcon(PaymentModeWiseCollectionGR.class.getResource("/images/pie_chart_red (1).png")));
		btnGetReport.setBackground(UIManager.getColor("Button.background"));
		btnGetReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(paymentModePieChart!=null)containerPanel.remove(paymentModePieChart);
				paymentModePieChart = new PaymentModePieChartUI(fromDateChooser.getDate()==null?null:new java.sql.Date(fromDateChooser.getDate().getTime()),toDateChooser.getDate()==null?null:new java.sql.Date(toDateChooser.getDate().getTime()));
				containerPanel.add(paymentModePieChart);
				//Code To Hide Title Bar of Internal Frame
				((javax.swing.plaf.basic.BasicInternalFrameUI)   
						paymentModePieChart.getUI()).setNorthPane(null);
			}
		});
		btnGetReport.setBounds(854, 32, 161, 34);
		panel.add(btnGetReport);
		

	}
}
