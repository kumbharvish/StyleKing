package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.dto.BillDetails;
import com.shopbilling.dto.Customer;
import com.shopbilling.dto.CustomerProfit;
import com.shopbilling.services.CustomerHistoryServices;
import com.shopbilling.utils.PDFUtils;
import com.toedter.calendar.JDateChooser;

public class CustomerWiseProfitUI extends JInternalFrame {
	private JTable table;
	private DefaultTableModel reportModel;
	private JDateChooser toDateChooser;
	private JDateChooser fromDateChooser;
	private double totalProfit=0;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
	private Map<Integer,BillDetails> billsMap;
	private  JLabel lblTotalProfitValue;
	private JLabel lblCustomerNameValue;
	private Map<Long,Customer> customerMap = CustomerHistoryServices.getCustomerMap();
	/**
	 * Create the frame.
	 */
	public CustomerWiseProfitUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Customer Wise Profit");
		getContentPane().setLayout(null);
		Border border = new LineBorder(Color.black, 2);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Date Range", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 1150, 60);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblFromDate = new JLabel("From Date :");
		lblFromDate.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblFromDate.setBounds(212, 16, 99, 28);
		panel.add(lblFromDate);
		
		JLabel lblToDate = new JLabel("To Date :");
		lblToDate.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblToDate.setBounds(545, 16, 79, 28);
		panel.add(lblToDate);
		
		JButton btnGetReport = new JButton("Get Report");
		btnGetReport.setIcon(new ImageIcon(SellReport.class.getResource("/images/clipboard_report_bar_24_ns.png")));
		btnGetReport.setBackground(UIManager.getColor("Button.background"));
		btnGetReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillReportTable();
			}
		});
		btnGetReport.setBounds(852, 13, 127, 34);
		panel.add(btnGetReport);
		
		fromDateChooser = new JDateChooser();
		fromDateChooser.setBounds(321, 16, 133, 28);
		fromDateChooser.setDate(new Date());
		fromDateChooser.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(fromDateChooser);
		
		toDateChooser = new JDateChooser();
		toDateChooser.setBounds(634, 16, 133, 28);
		toDateChooser.setDate(new Date());
		toDateChooser.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(toDateChooser);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Customer Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 82, 1150, 356);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 26, 1120, 319);
		panel_1.add(scrollPane);
		
		table = new JTable();
		reportModel = new DefaultTableModel(){
			 boolean[] columnEditables = new boolean[] {
					 false, false, false, false,false,false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
		 reportModel.setColumnIdentifiers(new String[] {
				 "Mobile Number", "Name","Total No of Items","Total Qty","Bill Amount","Profit Amount"}
	       );
		table.setModel(reportModel);
		scrollPane.setViewportView(table);
		 PDFUtils.setTableRowHeight(table);
		 
		 JPanel panel_2 = new JPanel();
		 panel_2.setBorder(new TitledBorder(null, "Top Customer", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		 panel_2.setBounds(10, 449, 1150, 78);
		 getContentPane().add(panel_2);
		 panel_2.setLayout(null);
		 
		 lblTotalProfitValue = new JLabel("");
		 lblTotalProfitValue.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalProfitValue.setFont(new Font("Tahoma", Font.BOLD, 20));
		 lblTotalProfitValue.setBounds(872, 40, 241, 31);
		 lblTotalProfitValue.setBorder(border);
		 panel_2.add(lblTotalProfitValue);
		 
		 JLabel lblTotalProfit = new JLabel("Profit");
		 lblTotalProfit.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalProfit.setBounds(872, 11, 241, 31);
		 lblTotalProfit.setBorder(border);
		 lblTotalProfit.setFont(new Font("Tahoma", Font.BOLD, 15));
		 panel_2.add(lblTotalProfit);
		 
		 lblCustomerNameValue = new JLabel("");
		 lblCustomerNameValue.setHorizontalAlignment(SwingConstants.CENTER);
		 lblCustomerNameValue.setFont(new Font("Tahoma", Font.BOLD, 20));
		 lblCustomerNameValue.setBounds(633, 40, 241, 31);
		 lblCustomerNameValue.setBorder(border);
		 panel_2.add(lblCustomerNameValue);
		 
		 JLabel lblCustomerName = new JLabel("Customer Name");
		 lblCustomerName.setHorizontalAlignment(SwingConstants.CENTER);
		 lblCustomerName.setFont(new Font("Tahoma", Font.BOLD, 15));
		 lblCustomerName.setBounds(633, 11, 241, 31);
		 lblCustomerName.setBorder(border);
		 panel_2.add(lblCustomerName);
		 
		 ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
	
	//Fill Report Table
	private void fillReportTable(){
		List<CustomerProfit> list= CustomerHistoryServices.getCustomerWiseProfit(fromDateChooser.getDate()==null?null:new java.sql.Date(fromDateChooser.getDate().getTime()),toDateChooser.getDate()==null?null:new java.sql.Date(toDateChooser.getDate().getTime()));
		reportModel.setRowCount(0);
		if(list.isEmpty()){
			JOptionPane.showMessageDialog(getContentPane(), "No Records found for the given period !");
			lblTotalProfitValue.setText("");
			lblCustomerNameValue.setText("");
			
		}else{
			for(CustomerProfit b : list){
				
				reportModel.addRow(new Object[]{b.getCustMobileNumber(),customerMap.get(b.getCustMobileNumber())!=null?customerMap.get(b.getCustMobileNumber()).getCustName():"<Deleted Customer>",b.getTotalNoOfItems(),b.getTotalQty(),PDFUtils.getDecimalFormat(b.getSumOfBillAmt()),PDFUtils.getDecimalFormat(b.getProfit())});
			}
			setReportValues();
		}
	}

	private void setReportValues() {
		String name = (String)reportModel.getValueAt(0, 1);
		double profit = Double.valueOf((String)reportModel.getValueAt(0, 5));
		lblTotalProfitValue.setText("Rs. "+PDFUtils.getDecimalFormat(profit)+" /-");
		lblCustomerNameValue.setText(" "+name);
	}
}
