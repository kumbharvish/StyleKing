package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.Customer;
import com.shopbilling.services.ExcelServices;
import com.shopbilling.services.JasperServices;
import com.shopbilling.services.UserServices;
import com.shopbilling.utils.JasperUtils;
import com.shopbilling.utils.PDFUtils;

public class CustomersReportUI extends JInternalFrame {
	private JTable table;
	private DefaultTableModel reportModel;
	private JRadioButton rdbtnCustomerName;
	private JRadioButton rdbtnBalanceAmt;
	private JTextField tf_totalCustomers;
	private JTextField tf_totalCustBalanceAmt;
	private int totalCustomers=0;
	private double totalCustBalanceAmt=0;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
	/**
	 * Create the frame.
	 */
	public CustomersReportUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Customers Report");
		getContentPane().setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Report", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(21, 62, 937, 529);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 26, 909, 492);
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
				 "Mobile Number", "Name","City","Email","Entry Date","Pending Amount"}
	       );
		table.setModel(reportModel);
		scrollPane.setViewportView(table);
		table.getColumnModel().getColumn(1).setPreferredWidth(170);
		JLabel lblExportAs = new JLabel("Export As :");
		lblExportAs.setBounds(842, 22, 65, 21);
		getContentPane().add(lblExportAs);
		
		JButton btnPDF = new JButton("");
		btnPDF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportPDF();
			}
		});
		btnPDF.setBackground(SystemColor.menu);
		btnPDF.setBorder(BorderFactory.createEmptyBorder());
		btnPDF.setToolTipText("PDF");
		btnPDF.setIcon(new ImageIcon(ProductProfitReportUI.class.getResource("/images/pdf.png")));
		btnPDF.setBounds(915, 10, 48, 48);
		getContentPane().add(btnPDF);
		
		JButton btnXLS = new JButton("");
		btnXLS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportExcel();
			}
		});
		btnXLS.setBackground(SystemColor.menu);
		btnXLS.setToolTipText("Excel");
		btnXLS.setBorder(BorderFactory.createEmptyBorder());
		btnXLS.setIcon(new ImageIcon(ProductProfitReportUI.class.getResource("/images/excel.png")));
		btnXLS.setBounds(988, 10, 48, 48);
		getContentPane().add(btnXLS);
		
		JLabel lblSortBy = new JLabel("Sort by :");
		lblSortBy.setBounds(233, 22, 53, 21);
		getContentPane().add(lblSortBy);
		
		rdbtnCustomerName = new JRadioButton("Customer Name");
		rdbtnCustomerName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chageSortParam();
			}
		});
		rdbtnCustomerName.setBounds(292, 21, 160, 23);
		getContentPane().add(rdbtnCustomerName);
		
		rdbtnBalanceAmt = new JRadioButton("Pending Amount");
		rdbtnBalanceAmt.setBounds(454, 21, 146, 23);
		
		rdbtnBalanceAmt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chageSortParam();
			}
		});
		
		
		getContentPane().add(rdbtnBalanceAmt);
		PDFUtils.setTableRowHeight(table);
		 ButtonGroup bg = new ButtonGroup();
			bg.add(rdbtnBalanceAmt);
			bg.add(rdbtnCustomerName);
			rdbtnCustomerName.setSelected(true);
			 rdbtnBalanceAmt.setSelected(false);
		 
		 JPanel panel = new JPanel();
		 panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Total Customers", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		 panel.setBounds(968, 437, 202, 154);
		 getContentPane().add(panel);
		 panel.setLayout(null);
		 
		 JLabel lblTotal = new JLabel("Total Customers");
		 lblTotal.setBounds(10, 26, 134, 25);
		 panel.add(lblTotal);
		 
		 tf_totalCustomers = new JTextField();
		 tf_totalCustomers.setColumns(10);
		 tf_totalCustomers.setBounds(10, 49, 185, 25);
		 tf_totalCustomers.setFont(new Font("Dialog", Font.BOLD, 20));
		 tf_totalCustomers.setHorizontalAlignment(SwingConstants.RIGHT);
		 tf_totalCustomers.setEditable(false);
		 panel.add(tf_totalCustomers);
		 
		 JLabel lblTotalStockValue = new JLabel("Total Pending Amount");
		 lblTotalStockValue.setBounds(10, 85, 150, 25);
		 panel.add(lblTotalStockValue);
		 
		 tf_totalCustBalanceAmt = new JTextField();
		 tf_totalCustBalanceAmt.setColumns(10);
		 tf_totalCustBalanceAmt.setBounds(45, 112, 150, 30);
		 tf_totalCustBalanceAmt.setBackground(Color.GRAY);
		 tf_totalCustBalanceAmt.setForeground(Color.WHITE);
		 tf_totalCustBalanceAmt.setEditable(false);
		 tf_totalCustBalanceAmt.setFont(new Font("Dialog", Font.BOLD, 20));
		 tf_totalCustBalanceAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		 panel.add(tf_totalCustBalanceAmt);
		 
		 JLabel label = new JLabel("");
		 label.setIcon(new ImageIcon(SalesStockReportUI.class.getResource("/images/currency_sign_rupee.png")));
		 label.setBounds(10, 112, 29, 30);
		 panel.add(label);
		 fillReportTable(Customer.SortParameter.CUSTOMER_NAME_ASCENDING);
		 
		 ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
	private void fillReportTable(Customer.SortParameter VALUE) {
		 totalCustBalanceAmt=0;
		 totalCustomers=0;
		List<Customer> customerList= UserServices.getAllCustomers();
		 Comparator<Customer> cp = Customer.getComparator(VALUE); 
			Collections.sort(customerList,cp);
			reportModel.setRowCount(0);
			totalCustomers = customerList.size();
			if(customerList.isEmpty()){
				JOptionPane.showMessageDialog(getContentPane(), "No Customer found!");
			}else{
				for(Customer p : customerList){
					reportModel.addRow(new Object[]{p.getCustMobileNumber(), p.getCustName(),p.getCustCity(),p.getCustEmail() ,sdf.format(p.getEntryDate()),PDFUtils.getDecimalFormat(p.getBalanceAmt())});
					totalCustBalanceAmt += p.getBalanceAmt();
				}
				setTotalFieldValues();
			}
	}
	
	private void chageSortParam(){
		if(rdbtnCustomerName.isSelected()){
			fillReportTable(Customer.SortParameter.CUSTOMER_NAME_ASCENDING);
		}
		if(rdbtnBalanceAmt.isSelected()){
			fillReportTable(Customer.SortParameter.CUST_BALANCE_ASCENDING);
		}
	}
	
	private void setTotalFieldValues(){
		tf_totalCustomers.setText(String.valueOf(totalCustomers));
		tf_totalCustBalanceAmt.setText(PDFUtils.getDecimalFormat(totalCustBalanceAmt));
	}
	
	protected void exportExcel() {
		List<Customer> customerList= UserServices.getAllCustomers();
			
		if(rdbtnCustomerName.isSelected()){
			Comparator<Customer> cp = Customer.getComparator(Customer.SortParameter.CUSTOMER_NAME_ASCENDING); 
			Collections.sort(customerList,cp);
		}
		if(rdbtnBalanceAmt.isSelected()){
			Comparator<Customer> cp = Customer.getComparator(Customer.SortParameter.CUST_BALANCE_ASCENDING); 
			Collections.sort(customerList,cp);
		}
		boolean isSucess=ExcelServices.writeCustomersExcel(customerList);
		if(isSucess){
			JOptionPane.showMessageDialog(getContentPane(), "Report Exported Sucessfully! ");
		}else{
			JOptionPane.showMessageDialog(getContentPane(),"Error Occured! ","Error",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	protected void exportPDF() {
		List<Customer> customerList= UserServices.getAllCustomers();
		
		if(rdbtnCustomerName.isSelected()){
			Comparator<Customer> cp = Customer.getComparator(Customer.SortParameter.CUSTOMER_NAME_ASCENDING); 
			Collections.sort(customerList,cp);
		}
		if(rdbtnBalanceAmt.isSelected()){
			Comparator<Customer> cp = Customer.getComparator(Customer.SortParameter.CUST_BALANCE_ASCENDING); 
			Collections.sort(customerList,cp);
		}
		List<Map<String,?>> dataSourceMap = JasperServices.createDataForCustomersReport(customerList);
		boolean isSucess=JasperUtils.createPDF(dataSourceMap,AppConstants.CUSTOMERS_REPORT_JRXML,"Customers_Report");
		if(isSucess){
			JOptionPane.showMessageDialog(getContentPane(), "Report Exported Sucessfully! ");
		}else{
			JOptionPane.showMessageDialog(getContentPane(),"Error Occured! ","Error",JOptionPane.WARNING_MESSAGE);
		}
	}
}
