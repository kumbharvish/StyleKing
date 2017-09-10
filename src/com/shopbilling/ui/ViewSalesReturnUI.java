package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.LabelAlignment;

import com.shopbilling.dto.ItemDetails;
import com.shopbilling.dto.ReturnDetails;
import com.shopbilling.services.SalesReturnServices;
import com.shopbilling.utils.PDFUtils;

public class ViewSalesReturnUI extends JDialog {

	private JTextField billNumber;
	private JTextField customerName;
	private JTextField customerMobileNo;
	private JTextField billDate;
	private JTextField billPayMode;
	private JTextField billNetSalesAmt;
	private JTable table;
	private JPanel itemDetailsPanel;
	private DefaultTableModel productModel;
	private JTextField tf_NoOfItems;
	private JTextField tf_TotalQty;
	private JTextField tf_ReturnTotalAmt;
	private JTextField tf_ReturnNumber;
	private JTextField tf_ReturnDate;
	private JTextField tf_Comments;
	//Bill Customer 
	private JLabel rupeeLabel;
	private JTextField tf_DiscountAmt;
	private JTextField tf_TaxAmt;
	JLabel lblDiscount;
	JLabel lblTax;
	private JTextField tf_subTotal;
	private JTextField tf_Discount;
	private JTextField tf_tax;
	private JTextField tf_grandTotal;
	/**
	 * Create the frame.
	 */
	
	//This constructor is called from Sales Report view Return button
	public ViewSalesReturnUI(Integer returnNumber,JFrame frame) {
		super(frame,"View Sales Return",true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("shop32X32.png"));
		setBounds(150, 100, 1158, 557);
		setResizable(false);
		setTitle("View Sales Return");
		Font font = new Font("Dialog", Font.BOLD, 13);
		JPanel billDetailsPanel = new JPanel();
		billDetailsPanel.setBounds(10, 11, 806, 123);
		itemDetailsPanel = new JPanel();
		itemDetailsPanel.setBounds(10, 145, 806, 362);
		billDetailsPanel.requestFocus();
		billDetailsPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Bill Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		//Bill Details Form
		DesignGridLayout layout = new DesignGridLayout(billDetailsPanel);
		layout.labelAlignment(LabelAlignment.RIGHT);
		billNumber = new JTextField();
		billNumber.setEditable(false);
		billNumber.setFont(font);
		customerName = new JTextField(20);
		customerName.setEditable(false);
		customerMobileNo = new JTextField(20);
		customerMobileNo.setEditable(false);
		customerMobileNo.setFont(font);
		customerName.setFont(font);
		billDate = new JTextField(20);
		billDate.setFont(font);
		billDate.setEditable(false);
		billPayMode = new JTextField(20);
		billPayMode.setFont(font);
		billPayMode.setEditable(false);
		billNetSalesAmt = new JTextField(20);
		billNetSalesAmt.setFont(font);
		billNetSalesAmt.setEditable(false);
		
		layout.row().grid(new JLabel("Bill Number :"))	.add(billNumber)	.grid(new JLabel("Customer Mobile No. :"))	.add(customerMobileNo);
		layout.row().grid(new JLabel("Bill Date :"))	.add(billDate).grid(new JLabel("Customer Name :"))	.add(customerName);
		layout.row().grid(new JLabel("Bill Payment Mode :"))	.add(billPayMode).grid(new JLabel("Bill Net Sales Amt :")).add(billNetSalesAmt);
		
		itemDetailsPanel.setBorder(new TitledBorder(null, "Item Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel paymentDetails = new JPanel();
		paymentDetails.setBounds(826, 154, 313, 353);
		paymentDetails.setBorder(new TitledBorder(null, "Payment Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(17, 29, 773, 322);
		 Font amtFont = new Font("Dialog", Font.BOLD, 16);
		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 13));
		productModel = new DefaultTableModel(){
			 boolean[] columnEditables = new boolean[] {
					 false, false, false, false,false,false,false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
		 productModel.setColumnIdentifiers(new String[] {
				 "Item No", "Item Name", "MRP", "Rate", "Qty", "Amount","Purchase Price"}
	       );
		table.setModel(productModel);
		table.setRowHeight(20);
		table.setEnabled(false);
		scrollPane.setViewportView(table);
		 JTableHeader header = table.getTableHeader();
		 header.setFont(new Font("Dialog", Font.BOLD, 13));
		 header.setBackground(Color.GRAY);
		 header.setForeground(Color.WHITE);
		 
		 table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 table.getColumnModel().getColumn(0).setPreferredWidth(100);
		 table.getColumnModel().getColumn(1).setPreferredWidth(290);
		 table.getColumnModel().getColumn(2).setPreferredWidth(100);
		 table.getColumnModel().getColumn(3).setPreferredWidth(100);
		 table.getColumnModel().getColumn(4).setPreferredWidth(80);
		 table.getColumnModel().getColumn(5).setPreferredWidth(100);
		 //hide purchase price column
		 table.getColumnModel().getColumn(6).setMinWidth(0);
		 table.getColumnModel().getColumn(6).setMaxWidth(0);
		 table.getColumnModel().getColumn(6).setWidth(0);
		itemDetailsPanel.setLayout(null);
		itemDetailsPanel.add(scrollPane);
		getContentPane().setLayout(null);
		getContentPane().add(billDetailsPanel);
		getContentPane().add(itemDetailsPanel);
		getContentPane().add(paymentDetails);
		paymentDetails.setLayout(null);
		
		JLabel lblNoOfItems = new JLabel("No. Of Items");
		lblNoOfItems.setBounds(20, 20, 91, 35);
		paymentDetails.add(lblNoOfItems);
		
		tf_NoOfItems = new JTextField();
		tf_NoOfItems.setBounds(120, 20, 180, 38);
		paymentDetails.add(tf_NoOfItems);
		tf_NoOfItems.setColumns(10);
		tf_NoOfItems.setEditable(false);
		tf_NoOfItems.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_NoOfItems.setFont(amtFont);
		tf_NoOfItems.setText("0");
		
		JLabel lblTotalQuantity = new JLabel("Total Quantity");
		lblTotalQuantity.setBounds(20, 59, 91, 35);
		paymentDetails.add(lblTotalQuantity);
		
		tf_TotalQty = new JTextField();
		tf_TotalQty.setColumns(10);
		tf_TotalQty.setBounds(120, 59, 180, 38);
		tf_TotalQty.setEditable(false);
		paymentDetails.add(tf_TotalQty);
		tf_TotalQty.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_TotalQty.setFont(amtFont);
		tf_TotalQty.setText("0");
		
		JLabel lblNetSa = new JLabel("Total Amount");
		lblNetSa.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNetSa.setBounds(20, 264, 212, 20);
		paymentDetails.add(lblNetSa);
		
		tf_ReturnTotalAmt = new JTextField();
		tf_ReturnTotalAmt.setEditable(false);
		tf_ReturnTotalAmt.setForeground(Color.WHITE);
		tf_ReturnTotalAmt.setBackground(Color.GRAY);
		tf_ReturnTotalAmt.setColumns(10);
		tf_ReturnTotalAmt.setBounds(69, 295, 231, 38);
		paymentDetails.add(tf_ReturnTotalAmt);
		tf_ReturnTotalAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_ReturnTotalAmt.setFont(new Font("Dialog", Font.BOLD, 30));
		tf_ReturnTotalAmt.setText("0.00");
		
		rupeeLabel = new JLabel("New label");
		rupeeLabel.setBounds(10, 295, 49, 47);
		paymentDetails.add(rupeeLabel);
		rupeeLabel.setIcon(new ImageIcon(NewBill.class.getResource("/images/Rupee-64.png")));
		
		lblTax = new JLabel("TAX");
		lblTax.setBounds(20, 215, 91, 35);
		paymentDetails.add(lblTax);
		
		lblDiscount = new JLabel("Discount");
		lblDiscount.setBounds(20, 137, 91, 35);
		paymentDetails.add(lblDiscount);
		
		tf_DiscountAmt = new JTextField();
		tf_DiscountAmt.setText("0.00");
		tf_DiscountAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_DiscountAmt.setFont(new Font("Dialog", Font.BOLD, 16));
		tf_DiscountAmt.setEditable(false);
		tf_DiscountAmt.setColumns(10);
		tf_DiscountAmt.setBounds(203, 137, 97, 38);
		paymentDetails.add(tf_DiscountAmt);
		
		tf_TaxAmt = new JTextField();
		tf_TaxAmt.setText("0.00");
		tf_TaxAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_TaxAmt.setFont(new Font("Dialog", Font.BOLD, 16));
		tf_TaxAmt.setEditable(false);
		tf_TaxAmt.setColumns(10);
		tf_TaxAmt.setBounds(203, 215, 97, 38);
		paymentDetails.add(tf_TaxAmt);
		
		JLabel lblSubTotal = new JLabel("Sub Total");
		lblSubTotal.setBounds(20, 98, 91, 35);
		paymentDetails.add(lblSubTotal);
		
		tf_subTotal = new JTextField();
		tf_subTotal.setText("0.00");
		tf_subTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_subTotal.setFont(new Font("Dialog", Font.BOLD, 16));
		tf_subTotal.setEditable(false);
		tf_subTotal.setColumns(10);
		tf_subTotal.setBounds(120, 98, 180, 38);
		paymentDetails.add(tf_subTotal);
		
		tf_Discount = new JTextField();
		tf_Discount.setText("0.00%");
		tf_Discount.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_Discount.setFont(new Font("Dialog", Font.BOLD, 12));
		tf_Discount.setEditable(false);
		tf_Discount.setColumns(10);
		tf_Discount.setBounds(120, 137, 58, 38);
		paymentDetails.add(tf_Discount);
		
		JLabel lblAmt = new JLabel("Amt");
		lblAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAmt.setBounds(170, 137, 32, 35);
		paymentDetails.add(lblAmt);
		
		JLabel label = new JLabel("Amt");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(170, 215, 32, 35);
		paymentDetails.add(label);
		
		tf_tax = new JTextField();
		tf_tax.setText("0.00%");
		tf_tax.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_tax.setFont(new Font("Dialog", Font.BOLD, 12));
		tf_tax.setEditable(false);
		tf_tax.setColumns(10);
		tf_tax.setBounds(120, 215, 58, 38);
		paymentDetails.add(tf_tax);
		
		JLabel lblGrandTotal = new JLabel("Grand Total");
		lblGrandTotal.setBounds(20, 176, 91, 35);
		paymentDetails.add(lblGrandTotal);
		
		tf_grandTotal = new JTextField();
		tf_grandTotal.setText("0.00");
		tf_grandTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_grandTotal.setFont(new Font("Dialog", Font.BOLD, 16));
		tf_grandTotal.setEditable(false);
		tf_grandTotal.setColumns(10);
		tf_grandTotal.setBounds(120, 176, 180, 38);
		paymentDetails.add(tf_grandTotal);
		
		JPanel panel_SalesReturnDetails = new JPanel();
		panel_SalesReturnDetails.setBorder(new TitledBorder(null, "Sales Return Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_SalesReturnDetails.setBounds(826, 11, 313, 132);
		getContentPane().add(panel_SalesReturnDetails);
		panel_SalesReturnDetails.setLayout(null);
		
		tf_ReturnDate = new JTextField(PDFUtils.getFormattedDate(new Date()));
		tf_ReturnDate.setFont(font);
		tf_ReturnDate.setEditable(false);
		tf_ReturnNumber = new JTextField();
		tf_ReturnNumber.setEditable(false);
		tf_ReturnNumber.setText(String.valueOf(PDFUtils.getBillNumber()));
		tf_ReturnNumber.setFont(font);
		tf_Comments = new JTextField();
		tf_Comments.setFont(font);
		tf_Comments.setEditable(false);
		
		DesignGridLayout layout2 = new DesignGridLayout(panel_SalesReturnDetails);
		layout2.labelAlignment(LabelAlignment.RIGHT);
		
		layout2.row().grid(new JLabel("Return Number :"))	.add(tf_ReturnNumber);
		layout2.emptyRow();
		layout2.row().grid(new JLabel("Return Date :"))	.add(tf_ReturnDate);
		layout2.emptyRow();
		layout2.row().grid(new JLabel("Comments :"))	.add(tf_Comments);
		layout2.emptyRow();
		setBillDetails(returnNumber);
	}
	// This constructor is called from viewCustomerBill view button
	public ViewSalesReturnUI(Integer returnNumber,JDialog frame) {
		super(frame,"View Sales Return",true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("shop32X32.png"));
		setBounds(150, 100, 1158, 557);
		setResizable(false);
		setTitle("View Sales Return");
		Font font = new Font("Dialog", Font.BOLD, 13);
		JPanel billDetailsPanel = new JPanel();
		billDetailsPanel.setBounds(10, 11, 806, 123);
		itemDetailsPanel = new JPanel();
		itemDetailsPanel.setBounds(10, 145, 806, 362);
		billDetailsPanel.requestFocus();
		billDetailsPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Bill Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		//Bill Details Form
		DesignGridLayout layout = new DesignGridLayout(billDetailsPanel);
		layout.labelAlignment(LabelAlignment.RIGHT);
		billNumber = new JTextField();
		billNumber.setEditable(false);
		billNumber.setFont(font);
		customerName = new JTextField(20);
		customerName.setEditable(false);
		customerMobileNo = new JTextField(20);
		customerMobileNo.setEditable(false);
		customerMobileNo.setFont(font);
		customerName.setFont(font);
		billDate = new JTextField(20);
		billDate.setFont(font);
		billDate.setEditable(false);
		billPayMode = new JTextField(20);
		billPayMode.setFont(font);
		billPayMode.setEditable(false);
		billNetSalesAmt = new JTextField(20);
		billNetSalesAmt.setFont(font);
		billNetSalesAmt.setEditable(false);
		
		layout.row().grid(new JLabel("Bill Number :"))	.add(billNumber)	.grid(new JLabel("Customer Mobile No. :"))	.add(customerMobileNo);
		layout.row().grid(new JLabel("Bill Date :"))	.add(billDate).grid(new JLabel("Customer Name :"))	.add(customerName);
		layout.row().grid(new JLabel("Bill Payment Mode :"))	.add(billPayMode).grid(new JLabel("Bill Net Sales Amt :")).add(billNetSalesAmt);
		
		itemDetailsPanel.setBorder(new TitledBorder(null, "Item Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel paymentDetails = new JPanel();
		paymentDetails.setBounds(826, 154, 313, 353);
		paymentDetails.setBorder(new TitledBorder(null, "Payment Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(17, 29, 773, 322);
		 Font amtFont = new Font("Dialog", Font.BOLD, 16);
		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 13));
		productModel = new DefaultTableModel(){
			 boolean[] columnEditables = new boolean[] {
					 false, false, false, false,false,false,false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
		 productModel.setColumnIdentifiers(new String[] {
				 "Item No", "Item Name", "MRP", "Rate", "Qty", "Amount","Purchase Price"}
	       );
		table.setModel(productModel);
		table.setEnabled(false);
		scrollPane.setViewportView(table);
		 JTableHeader header = table.getTableHeader();
		 header.setFont(new Font("Dialog", Font.BOLD, 13));
		 header.setBackground(Color.GRAY);
		 header.setForeground(Color.WHITE);
		 
		 table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 table.getColumnModel().getColumn(0).setPreferredWidth(100);
		 table.getColumnModel().getColumn(1).setPreferredWidth(290);
		 table.getColumnModel().getColumn(2).setPreferredWidth(100);
		 table.getColumnModel().getColumn(3).setPreferredWidth(100);
		 table.getColumnModel().getColumn(4).setPreferredWidth(80);
		 table.getColumnModel().getColumn(5).setPreferredWidth(100);
		 //hide purchase price column
		 table.getColumnModel().getColumn(6).setMinWidth(0);
		 table.getColumnModel().getColumn(6).setMaxWidth(0);
		 table.getColumnModel().getColumn(6).setWidth(0);
		itemDetailsPanel.setLayout(null);
		itemDetailsPanel.add(scrollPane);
		getContentPane().setLayout(null);
		getContentPane().add(billDetailsPanel);
		getContentPane().add(itemDetailsPanel);
		getContentPane().add(paymentDetails);
		paymentDetails.setLayout(null);
		
		JLabel lblNoOfItems = new JLabel("No. Of Items");
		lblNoOfItems.setBounds(20, 20, 91, 35);
		paymentDetails.add(lblNoOfItems);
		
		tf_NoOfItems = new JTextField();
		tf_NoOfItems.setBounds(120, 20, 180, 38);
		paymentDetails.add(tf_NoOfItems);
		tf_NoOfItems.setColumns(10);
		tf_NoOfItems.setEditable(false);
		tf_NoOfItems.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_NoOfItems.setFont(amtFont);
		tf_NoOfItems.setText("0");
		
		JLabel lblTotalQuantity = new JLabel("Total Quantity");
		lblTotalQuantity.setBounds(20, 59, 91, 35);
		paymentDetails.add(lblTotalQuantity);
		
		tf_TotalQty = new JTextField();
		tf_TotalQty.setColumns(10);
		tf_TotalQty.setBounds(120, 59, 180, 38);
		tf_TotalQty.setEditable(false);
		paymentDetails.add(tf_TotalQty);
		tf_TotalQty.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_TotalQty.setFont(amtFont);
		tf_TotalQty.setText("0");
		
		JLabel lblNetSa = new JLabel("Total Amount");
		lblNetSa.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNetSa.setBounds(20, 264, 212, 20);
		paymentDetails.add(lblNetSa);
		
		tf_ReturnTotalAmt = new JTextField();
		tf_ReturnTotalAmt.setEditable(false);
		tf_ReturnTotalAmt.setForeground(Color.WHITE);
		tf_ReturnTotalAmt.setBackground(Color.GRAY);
		tf_ReturnTotalAmt.setColumns(10);
		tf_ReturnTotalAmt.setBounds(69, 295, 231, 38);
		paymentDetails.add(tf_ReturnTotalAmt);
		tf_ReturnTotalAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_ReturnTotalAmt.setFont(new Font("Dialog", Font.BOLD, 30));
		tf_ReturnTotalAmt.setText("0.00");
		
		rupeeLabel = new JLabel("New label");
		rupeeLabel.setBounds(10, 295, 49, 47);
		paymentDetails.add(rupeeLabel);
		rupeeLabel.setIcon(new ImageIcon(NewBill.class.getResource("/images/Rupee-64.png")));
		
		lblTax = new JLabel("TAX");
		lblTax.setBounds(20, 215, 91, 35);
		paymentDetails.add(lblTax);
		
		lblDiscount = new JLabel("Discount");
		lblDiscount.setBounds(20, 137, 91, 35);
		paymentDetails.add(lblDiscount);
		
		tf_DiscountAmt = new JTextField();
		tf_DiscountAmt.setText("0.00");
		tf_DiscountAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_DiscountAmt.setFont(new Font("Dialog", Font.BOLD, 16));
		tf_DiscountAmt.setEditable(false);
		tf_DiscountAmt.setColumns(10);
		tf_DiscountAmt.setBounds(203, 137, 97, 38);
		paymentDetails.add(tf_DiscountAmt);
		
		tf_TaxAmt = new JTextField();
		tf_TaxAmt.setText("0.00");
		tf_TaxAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_TaxAmt.setFont(new Font("Dialog", Font.BOLD, 16));
		tf_TaxAmt.setEditable(false);
		tf_TaxAmt.setColumns(10);
		tf_TaxAmt.setBounds(203, 215, 97, 38);
		paymentDetails.add(tf_TaxAmt);
		
		JLabel lblSubTotal = new JLabel("Sub Total");
		lblSubTotal.setBounds(20, 98, 91, 35);
		paymentDetails.add(lblSubTotal);
		
		tf_subTotal = new JTextField();
		tf_subTotal.setText("0.00");
		tf_subTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_subTotal.setFont(new Font("Dialog", Font.BOLD, 16));
		tf_subTotal.setEditable(false);
		tf_subTotal.setColumns(10);
		tf_subTotal.setBounds(120, 98, 180, 38);
		paymentDetails.add(tf_subTotal);
		
		tf_Discount = new JTextField();
		tf_Discount.setText("0.00%");
		tf_Discount.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_Discount.setFont(new Font("Dialog", Font.BOLD, 12));
		tf_Discount.setEditable(false);
		tf_Discount.setColumns(10);
		tf_Discount.setBounds(120, 137, 58, 38);
		paymentDetails.add(tf_Discount);
		
		JLabel lblAmt = new JLabel("Amt");
		lblAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAmt.setBounds(170, 137, 32, 35);
		paymentDetails.add(lblAmt);
		
		JLabel label = new JLabel("Amt");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(170, 215, 32, 35);
		paymentDetails.add(label);
		
		tf_tax = new JTextField();
		tf_tax.setText("0.00%");
		tf_tax.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_tax.setFont(new Font("Dialog", Font.BOLD, 12));
		tf_tax.setEditable(false);
		tf_tax.setColumns(10);
		tf_tax.setBounds(120, 215, 58, 38);
		paymentDetails.add(tf_tax);
		
		JLabel lblGrandTotal = new JLabel("Grand Total");
		lblGrandTotal.setBounds(20, 176, 91, 35);
		paymentDetails.add(lblGrandTotal);
		
		tf_grandTotal = new JTextField();
		tf_grandTotal.setText("0.00");
		tf_grandTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_grandTotal.setFont(new Font("Dialog", Font.BOLD, 16));
		tf_grandTotal.setEditable(false);
		tf_grandTotal.setColumns(10);
		tf_grandTotal.setBounds(120, 176, 180, 38);
		paymentDetails.add(tf_grandTotal);
		
		JPanel panel_SalesReturnDetails = new JPanel();
		panel_SalesReturnDetails.setBorder(new TitledBorder(null, "Sales Return Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_SalesReturnDetails.setBounds(826, 11, 313, 132);
		getContentPane().add(panel_SalesReturnDetails);
		panel_SalesReturnDetails.setLayout(null);
		
		tf_ReturnDate = new JTextField(PDFUtils.getFormattedDate(new Date()));
		tf_ReturnDate.setFont(font);
		tf_ReturnDate.setEditable(false);
		tf_ReturnNumber = new JTextField();
		tf_ReturnNumber.setEditable(false);
		tf_ReturnNumber.setText(String.valueOf(PDFUtils.getBillNumber()));
		tf_ReturnNumber.setFont(font);
		tf_Comments = new JTextField();
		tf_Comments.setFont(font);
		tf_Comments.setEditable(false);
		
		DesignGridLayout layout2 = new DesignGridLayout(panel_SalesReturnDetails);
		layout2.labelAlignment(LabelAlignment.RIGHT);
		
		layout2.row().grid(new JLabel("Return Number :"))	.add(tf_ReturnNumber);
		layout2.emptyRow();
		layout2.row().grid(new JLabel("Return Date :"))	.add(tf_ReturnDate);
		layout2.emptyRow();
		layout2.row().grid(new JLabel("Comments :"))	.add(tf_Comments);
		layout2.emptyRow();
		setBillDetails(returnNumber);
	}


	protected void setBillDetails(Integer returnNumber) {
		if(returnNumber!=null){
			ReturnDetails bill = SalesReturnServices.getReturnDetailsForReturnNo(returnNumber);
			List<ItemDetails> itemList = SalesReturnServices.getReturnedItemDetails(returnNumber);
			
			billNumber.setText(String.valueOf(bill.getBillNumber()));
			billDate.setText(PDFUtils.getFormattedDate((bill.getBillDate())));
			billPayMode.setText(bill.getBillPaymentMode());
			billNetSalesAmt.setText(PDFUtils.getDecimalFormat(bill.getBillNetSalesAmt()));
			customerMobileNo.setText(String.valueOf(bill.getCustomerMobileNo()));
			customerName.setText(bill.getCustomerName());
			tf_NoOfItems.setText(String.valueOf(bill.getNoOfItems()));
			tf_TotalQty.setText(String.valueOf(bill.getTotalQuanity()));
			tf_ReturnTotalAmt.setText(PDFUtils.getDecimalFormat(bill.getTotalAmount()));
			tf_Discount.setText(PDFUtils.getDecimalFormat(bill.getDiscount())+"%");
			tf_DiscountAmt.setText(PDFUtils.getDecimalFormat(bill.getDiscountAmount()));
			tf_tax.setText(PDFUtils.getDecimalFormat(bill.getTax())+"%");
			tf_TaxAmt.setText(PDFUtils.getDecimalFormat(bill.getTaxAmount()));
			tf_subTotal.setText(PDFUtils.getDecimalFormat(bill.getSubTotal()));
			tf_grandTotal.setText(PDFUtils.getDecimalFormat(bill.getGrandTotal()));
			tf_Comments.setText(bill.getComments());
			for(ItemDetails item : itemList){
				addBillItemDetails(item);
			}
		}
	}


	private void addBillItemDetails(ItemDetails item) {
		if(item!=null){
		productModel.addRow(new Object[]{item.getItemNo(), item.getItemName(), PDFUtils.getDecimalFormat(item.getMRP()), PDFUtils.getDecimalFormat(item.getRate()),item.getQuantity(),PDFUtils.getDecimalFormat(item.getAmount()),item.getPurchasePrice()});
		}
	}
}
