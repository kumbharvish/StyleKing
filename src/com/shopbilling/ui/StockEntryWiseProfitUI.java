package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

import com.shopbilling.dto.Supplier;
import com.shopbilling.dto.SupplierInvoiceDetails;
import com.shopbilling.services.SupplierServices;
import com.shopbilling.utils.PDFUtils;
import com.toedter.calendar.JDateChooser;

public class StockEntryWiseProfitUI extends JInternalFrame {
	private JTable table;
	private DefaultTableModel reportModel;
	private JDateChooser toDateChooser;
	private JDateChooser fromDateChooser;
	private double totalProfit=0;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
	private Map<Integer,SupplierInvoiceDetails> billsMap;
	private  JLabel lblTotalProfitValue;
	private JComboBox cb_Suppliers;
	private HashMap<String,Integer> supplierIdMap;
	
	/**
	 * Create the frame.
	 */
	public StockEntryWiseProfitUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Stock Entry Wise Profit");
		getContentPane().setLayout(null);
		Border border = new LineBorder(Color.black, 2);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Date Range", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 1150, 60);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblFromDate = new JLabel("From Date :");
		lblFromDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFromDate.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblFromDate.setBounds(382, 16, 99, 28);
		panel.add(lblFromDate);
		
		JLabel lblToDate = new JLabel("To Date :");
		lblToDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblToDate.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblToDate.setBounds(664, 16, 79, 28);
		panel.add(lblToDate);
		
		JButton btnGetReport = new JButton("Get Report");
		btnGetReport.setIcon(new ImageIcon(SellReport.class.getResource("/images/clipboard_report_bar_24_ns.png")));
		btnGetReport.setBackground(UIManager.getColor("Button.background"));
		btnGetReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(PDFUtils.isMandatorySelected(cb_Suppliers)){
					fillReportTable();
					setReportValues();
				}else{
					JOptionPane.showMessageDialog(getContentPane(), "Please Select Supplier !");
				}
				
			}
		});
		btnGetReport.setBounds(937, 13, 127, 34);
		panel.add(btnGetReport);
		
		fromDateChooser = new JDateChooser();
		fromDateChooser.setBounds(491, 16, 133, 28);
		fromDateChooser.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(fromDateChooser);
		
		toDateChooser = new JDateChooser();
		toDateChooser.setBounds(753, 16, 133, 28);
		toDateChooser.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(toDateChooser);
		
		JLabel lblSupplier = new JLabel("Supplier *:");
		lblSupplier.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSupplier.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblSupplier.setBounds(39, 16, 99, 28);
		panel.add(lblSupplier);
		
		cb_Suppliers = new JComboBox();
		cb_Suppliers.setBounds(148, 16, 200, 28);
		populateSupplier(cb_Suppliers);
		panel.add(cb_Suppliers);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Invoice Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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
				 "Supplier Name", "Invoice Number","Invoice Date","Total Qty","Invoice Amount","Profit Amount"}
	       );
		table.setModel(reportModel);
		scrollPane.setViewportView(table);
		PDFUtils.setTableRowHeight(table);
		 JPanel panel_2 = new JPanel();
		 panel_2.setBorder(new TitledBorder(null, "Consolidated Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		 panel_2.setBounds(10, 449, 1150, 78);
		 getContentPane().add(panel_2);
		 panel_2.setLayout(null);
		 
		 lblTotalProfitValue = new JLabel("");
		 lblTotalProfitValue.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalProfitValue.setFont(new Font("Tahoma", Font.BOLD, 20));
		 lblTotalProfitValue.setBounds(872, 40, 241, 31);
		 lblTotalProfitValue.setBorder(border);
		 panel_2.add(lblTotalProfitValue);
		 
		 JLabel lblTotalProfit = new JLabel("Total Profit");
		 lblTotalProfit.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalProfit.setBounds(872, 11, 241, 31);
		 lblTotalProfit.setBorder(border);
		 lblTotalProfit.setFont(new Font("Tahoma", Font.BOLD, 15));
		 panel_2.add(lblTotalProfit);
		 
		 ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
	
	//Fill Report Table
	private void fillReportTable(){
		List<SupplierInvoiceDetails> billList= SupplierServices.getStockEntryDetails(supplierIdMap.get((String)cb_Suppliers.getSelectedItem()),null,fromDateChooser.getDate()==null?null:new java.sql.Date(fromDateChooser.getDate().getTime()),toDateChooser.getDate()==null?null:new java.sql.Date(toDateChooser.getDate().getTime()));
		calculateConsolidateValues(billList);
		reportModel.setRowCount(0);
		if(billList.isEmpty()){
			JOptionPane.showMessageDialog(getContentPane(), "No Stock Entry found for the given criteria !");
		}else{
			for(SupplierInvoiceDetails b : billList){
				
				reportModel.addRow(new Object[]{b.getSupplierName(),b.getInvoiceNumber(),sdf.format(b.getInvoiceDate()),b.getTotalQuanity(),PDFUtils.getDecimalFormat(b.getSupplierInvoiceAmt()),PDFUtils.getDecimalFormat(b.getTotalMRPAmt() - b.getSupplierInvoiceAmt())});
			}
		}
	}

	private void calculateConsolidateValues(List<SupplierInvoiceDetails> billList) {
		totalProfit = 0;
		for(SupplierInvoiceDetails bill: billList){
			totalProfit+=bill.getTotalMRPAmt() - bill.getSupplierInvoiceAmt();
		}
	}
	
	private void setReportValues() {
		lblTotalProfitValue.setText("Rs. "+PDFUtils.getDecimalFormat(totalProfit)+" /-");
	}
	
	public void populateSupplier(JComboBox<String> combobox){
		combobox.addItem("----- SELECT SUPPLIER-----");
		supplierIdMap = new HashMap<String, Integer>();
		for(Supplier supplier :SupplierServices.getAllSuppliers()){
			combobox.addItem(supplier.getSupplierName());
			supplierIdMap.put(supplier.getSupplierName(), supplier.getSupplierID());
		}
	}
}
