package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.BillDetails;
import com.shopbilling.dto.Customer;
import com.shopbilling.dto.ItemDetails;
import com.shopbilling.dto.Product;
import com.shopbilling.dto.ReturnDetails;
import com.shopbilling.dto.StatusDTO;
import com.shopbilling.dto.UserDetails;
import com.shopbilling.services.ProductHistoryServices;
import com.shopbilling.services.ProductServices;
import com.shopbilling.services.SalesReturnServices;
import com.shopbilling.services.UserServices;
import com.shopbilling.utils.PDFUtils;

public class SalesReturnUI extends JInternalFrame {

	private UserDetails userDetails;
	private JTextField billNumber;
	private JTextField customerName;
	private JTextField customerMobileNo;
	private JTextField billDate;
	private JTextField billPayMode;
	private JTextField billNetSalesAmt;
	private JTable table;
	private JTextField itemNo;
	private JTextField itemName;
	private JTextField MRP;
	private JTextField rate;
	private JTextField quantity;
	private JTextField amount;
	private JPanel itemDetailsPanel;
	private List<ItemDetails> itemList;
	private HashMap<Integer,Integer> itemQtyMap;
	private List<Integer> productsInTable = new ArrayList<Integer>();
	private HashMap<String,Product> productMap; 
	private HashMap<Long,Product> productMapWithBarcode;
	private DefaultTableModel productModel;
	private JTextField tf_NoOfItems;
	private JTextField tf_TotalQty;
	private JTextField tf_ReturnTotalAmt;
	JButton btnSaveBill;
	JButton btnReset;
	private JTextField tf_ReturnNumber;
	private JTextField tf_ReturnDate;
	private JTextField tf_Comments;
	//
	private int noOfItems = 0;
	private int totalQty = 0;
	//private double discountAmt=0;
	private double netSalesAmt=0;
	private double subTotalAmt=0;
	private double billPurchaseAmt=0;
	private double grossTotal = 0;
	private double tax;
	private double discount;
	//Bill Customer 
	private JLabel rupeeLabel;
	private boolean mouseListnerFlag=true;
	private ReturnDetails bill;
	private JButton btnByBillNumber;
	private JButton btnByCustomer;
	private JButton btnChangeQty; 
	private SalesReturnCustomerDialogUI salesReturnCustomerDialogUI;
	private SalesReturnBillNoDialogUI salesReturnBillNoDialogUI;
	private JFrame mainFrame;
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
	public SalesReturnUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Sales Return");
		Font font = new Font("Dialog", Font.BOLD, 13);
		mainFrame = (JFrame)this.getTopLevelAncestor();
		JPanel billDetailsPanel = new JPanel();
		billDetailsPanel.setBounds(10, 11, 806, 123);
		itemDetailsPanel = new JPanel();
		itemDetailsPanel.setBounds(10, 145, 806, 453);
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
		itemName = new JTextField(20);
		itemName.setEnabled(false);
		getProductMapWithBarCode();
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
		paymentDetails.setBounds(826, 245, 330, 353);
		paymentDetails.setBorder(new TitledBorder(null, "Payment Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(17, 53, 773, 324);
		 Font amtFont = new Font("Dialog", Font.BOLD, 16);
		itemNo = new JTextField();
		itemNo.setEnabled(false);
		itemNo.setBounds(17, 27, 102, 27);
		itemNo.setColumns(10);
		itemNo.setFont(font);
		itemNo.setDisabledTextColor(Color.BLUE);
		itemNo.setBorder(border);
		/*itemName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					if(!itemName.getText().equals("")){
						quantity.requestFocus();
						setProductDetails(itemName.getText());
					}
					
				}
			}
		});*/
		itemName.setBounds(119, 27, 288, 27);
		itemName.setColumns(10);
		itemName.setFont(font);
		itemName.setDisabledTextColor(Color.BLUE);
		itemName.setBorder(border);
		MRP = new JTextField();
		MRP.setEnabled(false);
		MRP.setBounds(407, 27, 102, 27);
		MRP.setColumns(10);
		MRP.setFont(font);
		MRP.setDisabledTextColor(Color.BLUE);
		MRP.setBorder(border);
		rate = new JTextField();
		rate.setEnabled(false);
		rate.setBounds(507, 27, 102, 27);
		rate.setColumns(10);
		rate.setFont(font);
		rate.setDisabledTextColor(Color.BLUE);
		rate.setBorder(border);
		table = new JTable();
		table.setFont(new Font("Tahoma", Font.BOLD, 13));
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
		scrollPane.setViewportView(table);
		 JTableHeader header = table.getTableHeader();
		 header.setFont(new Font("Dialog", Font.BOLD, 15));
		 
		 //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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
		itemDetailsPanel.add(itemNo);
		itemDetailsPanel.add(itemName);
		itemDetailsPanel.add(MRP);
		itemDetailsPanel.add(rate);
		
		quantity = new JTextField();
		quantity.setBounds(608, 27, 82, 27);
		quantity.setForeground(Color.BLUE);
		quantity.setBorder(border);
		itemDetailsPanel.add(quantity);
		quantity.setColumns(10);
		quantity.setFont(font);
		amount = new JTextField();
		amount.setEnabled(false);
		amount.setBounds(688, 27, 100, 27);
		itemDetailsPanel.add(amount);
		amount.setColumns(10);
		amount.setFont(font);
		amount.setDisabledTextColor(Color.BLUE);
		amount.setBorder(border);
		getContentPane().setLayout(null);
		getContentPane().add(billDetailsPanel);
		getContentPane().add(itemDetailsPanel);
		//Font buttonFont = new Font("Dialog", Font.BOLD, 20);
		btnSaveBill = new JButton("Save");
		btnSaveBill.setIcon(new ImageIcon(NewBill.class.getResource("/images/save.png")));
		btnSaveBill.setFont(new Font("Dialog", Font.BOLD, 12));
		btnSaveBill.setBounds(49, 388, 122, 51);
		itemDetailsPanel.add(btnSaveBill);
		
		btnReset = new JButton("New Return");
		btnReset.setIcon(new ImageIcon(NewBill.class.getResource("/images/document_new.png")));
		btnReset.setBounds(288, 388, 150, 51);
		btnReset.setFont(new Font("Dialog", Font.BOLD, 12));
		itemDetailsPanel.add(btnReset);
		
		btnChangeQty = new JButton("Change Qty");
		btnChangeQty.setIcon(new ImageIcon(SalesReturnUI.class.getResource("/images/ic_arrow_round_change.png")));
		btnChangeQty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if(row==-1){
					JOptionPane.showMessageDialog(getContentPane(), "Please select item to change quantity");
				}else{
					String itemNotmp = table.getModel().getValueAt(row, 0).toString();
					String itemNametmp = table.getModel().getValueAt(row, 1).toString();
					String itemMRPtmp = table.getModel().getValueAt(row, 2).toString();
					String itemRatetmp = table.getModel().getValueAt(row, 3).toString();
					String itemQtytmp = table.getModel().getValueAt(row, 4).toString();
					String itemAmounttmp = table.getModel().getValueAt(row, 5).toString();
					itemNo.setText(itemNotmp);
					itemName.setText(itemNametmp);
					MRP.setText(itemMRPtmp);
					quantity.setText(itemQtytmp);
					amount.setText(itemAmounttmp);
					rate.setText(itemRatetmp);
					int removeProduct = Integer.valueOf(table.getModel().getValueAt(row, 0).toString());
					double removeAmt = Double.valueOf(table.getModel().getValueAt(row, 5).toString());
					int removeQty = Integer.valueOf(table.getModel().getValueAt(row, 4).toString());
					
					PDFUtils.removeItemFromList(productsInTable,removeProduct);
					billPurchaseAmt-=(Double.valueOf(table.getModel().getValueAt(row, 6).toString()))*removeQty;
					productModel.removeRow(row);
					setPaymentFields(-removeQty, -removeAmt, productModel.getRowCount());
					quantity.requestFocus();
					
				}
			}
		});
		btnChangeQty.setFont(new Font("Dialog", Font.BOLD, 12));
		btnChangeQty.setBounds(568, 388, 155, 51);
		itemDetailsPanel.add(btnChangeQty);
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
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Select Bill by", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(826, 154, 330, 80);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		btnByCustomer = new JButton("Customer");
		btnByCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salesReturnCustomerDialogUI = new SalesReturnCustomerDialogUI(mainFrame);
				salesReturnCustomerDialogUI.setVisible(true);
				setBillDetails(salesReturnCustomerDialogUI.getBillNumber());
			}
		});
		btnByCustomer.setBounds(37, 21, 113, 38);
		panel.add(btnByCustomer);
		
		btnByBillNumber = new JButton("Bill Number");
		btnByBillNumber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salesReturnBillNoDialogUI = new SalesReturnBillNoDialogUI(mainFrame);
				salesReturnBillNoDialogUI.setVisible(true);
				setBillDetails(salesReturnBillNoDialogUI.getBillNumber());
			}
		});
		btnByBillNumber.setBounds(196, 21, 105, 38);
		panel.add(btnByBillNumber);
		
		JPanel panel_SalesReturnDetails = new JPanel();
		panel_SalesReturnDetails.setBorder(new TitledBorder(null, "Sales Return Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_SalesReturnDetails.setBounds(826, 11, 330, 132);
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
		tf_Comments.setDisabledTextColor(Color.black);
		
		DesignGridLayout layout2 = new DesignGridLayout(panel_SalesReturnDetails);
		layout2.labelAlignment(LabelAlignment.RIGHT);
		
		layout2.row().grid(new JLabel("Return Number :"))	.add(tf_ReturnNumber);
		layout2.emptyRow();
		layout2.row().grid(new JLabel("Return Date :"))	.add(tf_ReturnDate);
		layout2.emptyRow();
		layout2.row().grid(new JLabel("Comments :"))	.add(tf_Comments);
		layout2.emptyRow();
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(mouseListnerFlag){
					int row = table.getSelectedRow();
					String productName = table.getModel().getValueAt(row, 1).toString();
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure to remove "+productName+" ?","Remove Product",dialogButton);
					if(dialogResult == JOptionPane.YES_OPTION){
						int removeProduct = Integer.valueOf(table.getModel().getValueAt(row, 0).toString());
						double removeAmt = Double.valueOf(table.getModel().getValueAt(row, 5).toString());
						int removeQty = Integer.valueOf(table.getModel().getValueAt(row, 4).toString());
						
						PDFUtils.removeItemFromList(productsInTable,removeProduct);
						billPurchaseAmt-=(Double.valueOf(table.getModel().getValueAt(row, 6).toString()))*removeQty;
						productModel.removeRow(row);
						setPaymentFields(-removeQty, -removeAmt, productModel.getRowCount());
					}
			}
			}
		});
		
		quantity.addKeyListener(new KeyAdapter() {
			   public void keyTyped(KeyEvent e) {
			      char c = e.getKeyChar();
			      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
			         e.consume();  // ignore event
			      }
			      if(c=='\n'){
			    	  if(PDFUtils.isMandatoryEntered(itemName) && PDFUtils.isMandatoryEntered(itemNo)){
			    		  if(!PDFUtils.isMandatoryEntered(quantity) || quantity.getText().equals("0")){
			    			  JOptionPane.showMessageDialog(getContentPane(), "Please Enter valid quantity");
			    		  }else{
			    			  if(Integer.valueOf(quantity.getText())>itemQtyMap.get(Integer.valueOf(itemNo.getText()))){
			    				  JOptionPane.showMessageDialog(getContentPane(), "Item Quantity should be equal to or less than purchased quantity");
				    		  }else{
				    			  addRecordToTable(productMap.get(itemName.getText()));
						    	  resetItemFields();
				    		  }
			    		  }
			    	  }else{
			    		  JOptionPane.showMessageDialog(getContentPane(), "Please select item");
			    	  }
			      }
			   }
			

			@Override
			public void keyReleased(KeyEvent e) {
				amount.setText("");
				if(!quantity.getText().equals("")){
					Double pRate = Double.parseDouble(rate.getText());
					int pQty = Integer.valueOf(quantity.getText());
					Double pAmount= pQty*pRate;
					amount.setText(PDFUtils.getDecimalFormat(pAmount));
				}
				
			}
			});
		//Save Button Action
		btnSaveBill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveReturnAction();
		}
	});
		//Reset Action
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetBillDetails(true);
			}
		});
		
		ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}


	protected void setBillDetails(Integer billNo) {
		resetBillDetails(false);
		if(billNo!=null){
			BillDetails bill = ProductServices.getBillDetailsOfBillNumber(billNo);
			List<ItemDetails> itemList = ProductServices.getItemDetails(billNo);
			createItemQuantityMap(itemList);
			billNumber.setText(String.valueOf(bill.getBillNumber()));
			billDate.setText(PDFUtils.getFormattedDate((bill.getTimestamp())));
			billPayMode.setText(bill.getPaymentMode());
			billNetSalesAmt.setText(PDFUtils.getDecimalFormat(bill.getNetSalesAmt()));
			customerMobileNo.setText(String.valueOf(bill.getCustomerMobileNo()));
			customerName.setText(bill.getCustomerName());
			tf_NoOfItems.setText(String.valueOf(bill.getNoOfItems()));
			tf_TotalQty.setText(String.valueOf(bill.getTotalQuanity()));
			tf_ReturnTotalAmt.setText(PDFUtils.getDecimalFormat(bill.getNetSalesAmt()));
			tf_Discount.setText(PDFUtils.getDecimalFormat(bill.getDiscount())+"%");
			tf_DiscountAmt.setText(PDFUtils.getDecimalFormat(bill.getDiscountAmt()));
			tf_tax.setText(PDFUtils.getDecimalFormat(bill.getTax())+"%");
			tax = bill.getTax();
			discount = bill.getDiscount();
			double taxAmt = bill.getGrandTotal()*(bill.getTax()/100);
			tf_TaxAmt.setText(PDFUtils.getDecimalFormat(taxAmt));
			totalQty =bill.getTotalQuanity();
			netSalesAmt = bill.getNetSalesAmt();
			subTotalAmt = bill.getTotalAmount();
			grossTotal = bill.getGrandTotal();
			tf_subTotal.setText(PDFUtils.getDecimalFormat(subTotalAmt));
			tf_grandTotal.setText(PDFUtils.getDecimalFormat(grossTotal));
			for(ItemDetails item : itemList){
				addBillItemDetails(item);
			}
		}
	}

	//Create Item Quantity Map
	private void createItemQuantityMap(List<ItemDetails> itemList2) {
		itemQtyMap = new HashMap<Integer, Integer>();
		for(ItemDetails item : itemList2){
			itemQtyMap.put(item.getItemNo(), item.getQuantity());
		}
	}


	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}
	
	public List<String> getProductName(){
		
		List<String> productNameList = new ArrayList<String>();
		productMap = new HashMap<String, Product>();
		for(Product product :ProductServices.getAllProducts()){
			productNameList.add(product.getProductName());
			productMap.put(product.getProductName(),product);
		}
		return productNameList;
	}
	
	public HashMap<String, Product> getProductMap(){
		productMap = new HashMap<String, Product>();
		for(Product product :ProductServices.getAllProducts()){
			productMap.put(product.getProductName(),product);
		}
		return productMap;
	}
	public HashMap<Long, Product> getProductMapWithBarCode(){
		productMapWithBarcode = new HashMap<Long, Product>();
		for(Product product :ProductServices.getAllProducts()){
			productMapWithBarcode.put(product.getProductBarCode(),product);
		}
		return productMapWithBarcode;
	}
	
	private void setProductDetails(String productName) {
		Product product = productMap.get(productName);
		quantity.setText("");
		if(product!=null){
			itemNo.setText(String.valueOf(product.getProductCode()));
			MRP.setText(PDFUtils.getDecimalFormat(product.getProductMRP()));
			rate.setText(PDFUtils.getDecimalFormat(product.getSellPrice()));
		}
		
	}
	
	private void addRecordToTable(Product product) {
		if(product!=null){
			if(!productsInTable.contains(product.getProductCode())){
					productsInTable.add(product.getProductCode());
					billPurchaseAmt+=product.getPurcasePrice()*Integer.valueOf(quantity.getText());
					System.out.println("Add Bill Purchase Amt : "+billPurchaseAmt);
					//Add row to table
					productModel.addRow(new Object[]{product.getProductCode(), product.getProductName(), PDFUtils.getDecimalFormat(product.getProductMRP()), PDFUtils.getDecimalFormat(product.getSellPrice()),quantity.getText(),amount.getText(),product.getPurcasePrice()});
					setPaymentFields(Integer.valueOf(quantity.getText()),Double.valueOf(amount.getText()),productModel.getRowCount());
			}else{
				JOptionPane.showMessageDialog(getContentPane(), "Item already present!");
			}
		}
	}
	
	private void addBillItemDetails(ItemDetails item) {
		if(item!=null){
		productModel.addRow(new Object[]{item.getItemNo(), item.getItemName(), PDFUtils.getDecimalFormat(item.getMRP()), PDFUtils.getDecimalFormat(item.getRate()),item.getQuantity(),PDFUtils.getDecimalFormat(item.getAmount()),item.getPurchasePrice()});
		}
	}
	
	private void setPaymentFields(int newQuantity, Double amount, int rowCount) {

		noOfItems=getNoOfItems();
		totalQty+=newQuantity;
		netSalesAmt+=amount;
		subTotalAmt+=amount;
		setAmountQty();
	}

	private void setAmountQty() {
		tf_NoOfItems.setText(String.valueOf(noOfItems));
		tf_TotalQty.setText(String.valueOf(totalQty));
		tf_subTotal.setText(PDFUtils.getDecimalFormat(subTotalAmt));
		setTotalReturnAmt();
	}


	private void setTotalReturnAmt() {
			double tempDisc = subTotalAmt;
			tempDisc= tempDisc-(subTotalAmt/100)*discount;
			double discountAmt=(subTotalAmt/100)*discount;
			netSalesAmt = tempDisc;
			grossTotal = tempDisc;
			System.out.println("discountAmt :"+discountAmt);
			System.out.println("grossTotal :"+grossTotal);
			tf_DiscountAmt.setText(PDFUtils.getDecimalFormat(discountAmt));
			tf_grandTotal.setText(PDFUtils.getDecimalFormat(grossTotal));
			double tempAmt = grossTotal;
			tempAmt= tempAmt+(grossTotal/100)*tax;
			double taxAmt = (grossTotal/100)*tax;
			tf_TaxAmt.setText(PDFUtils.getDecimalFormat(taxAmt));
			netSalesAmt = tempAmt;
			
			tf_ReturnTotalAmt.setText(PDFUtils.getDecimalFormat((PDFUtils.getDecimalRoundUp(netSalesAmt))));
	}

	private void resetItemFields() {
		itemNo.setText("");
		itemName.setText("");
		MRP.setText("");
		rate.setText("");
		quantity.setText("");
		amount.setText("");		
		
	}
	
	private void createItemList(){
		ItemDetails item = null;
		itemList = new ArrayList<ItemDetails>();
		for(int i=0;i<table.getRowCount();i++){
			item = new ItemDetails();
				item.setItemNo(Integer.valueOf(table.getModel().getValueAt(i, 0).toString()));
				item.setItemName(table.getModel().getValueAt(i, 1).toString());
				item.setMRP(Double.valueOf(table.getModel().getValueAt(i, 2).toString()));
				item.setRate(Double.valueOf(table.getModel().getValueAt(i, 3).toString()));
				item.setQuantity(Integer.valueOf(table.getModel().getValueAt(i, 4).toString()));
				item.setPurchasePrice(Double.valueOf(table.getModel().getValueAt(i, 6).toString()));
				item.setBillNumber(Integer.valueOf(tf_ReturnNumber.getText()));
				
				itemList.add(item);
		}
	}
	
	private int getNoOfItems(){
		HashSet<Integer> uniqueProducts = new HashSet<Integer>();	
		for(int i=0;i<table.getRowCount();i++){
			uniqueProducts.add(Integer.valueOf(table.getModel().getValueAt(i, 0).toString()));
		}
		return uniqueProducts.size();
	}
	
	private void resetBillDetails(Boolean flag){
		productModel.setRowCount(0);
		if(flag){
			tf_ReturnNumber.setText(String.valueOf(PDFUtils.getBillNumber()));
			tf_ReturnDate.setText(PDFUtils.getFormattedDate(new Date()));
		}
		billNumber.setText("");
		billDate.setText("");
		billPayMode.setText("");
		billNetSalesAmt.setText("");
		customerMobileNo.setText("");
		customerName.setText("");
		tf_Comments.setText("");
		tf_DiscountAmt.setText("0.00");
		tf_TaxAmt.setText("0.00");
		tf_subTotal.setText("0.00");
		tf_grandTotal.setText("0.00");
		resetItemFields();
		tf_NoOfItems.setText("0");
		tf_TotalQty.setText("0");
		tf_ReturnTotalAmt.setText("0.00");
		//cb_PaymentMode.setSelectedIndex(0);
		tf_Discount.setText("0.00%");
		tf_tax.setText("0.00%");
		noOfItems = 0;
		totalQty = 0;
		grossTotal = 0;
		netSalesAmt=0;
		billPurchaseAmt=0;
		subTotalAmt=0;
		tax=0;
		discount=0;
		productsInTable.clear();
		itemQtyMap = new HashMap<Integer, Integer>();
		if(itemList!=null)
		itemList.clear();
		btnSaveBill.setEnabled(true);
		productMap = getProductMap();
		productMapWithBarcode = getProductMapWithBarCode();
		enableForNewBill();
	}
	
	private void disableAfterSave(){
		//itemName.setEnabled(false);
		quantity.setEnabled(false);
		//cb_PaymentMode.setEnabled(false);
		btnByCustomer.setEnabled(false);
		btnByBillNumber.setEnabled(false);
		btnChangeQty.setEnabled(false);
		tf_Comments.setEnabled(false);
		mouseListnerFlag = false;
	}
	private void enableForNewBill(){
		quantity.setEnabled(true);
		//cb_PaymentMode.setEnabled(true);
		btnByCustomer.setEnabled(true);
		btnByBillNumber.setEnabled(true);
		btnByCustomer.requestFocus();
		btnChangeQty.setEnabled(true);
		tf_Comments.setEnabled(true);
		mouseListnerFlag=true;
	}


	private void saveReturnAction() {
		if(!PDFUtils.isMandatoryEntered(billNumber)){
			JOptionPane.showMessageDialog(getContentPane(), "Please Select Bill to return items!");
		}else {
			ReturnDetails rd = SalesReturnServices.getReturnDetails(Integer.valueOf(billNumber.getText()));
			if(rd!=null){
				JOptionPane.showMessageDialog(null, "Return No. : "+rd.getReturnNumber()+" already exists for Bill No. : "+rd.getBillNumber(), "Error", JOptionPane.WARNING_MESSAGE);
			}else{
				if(productModel.getRowCount()>0){
					//create item list
					createItemList();
					bill = new ReturnDetails();
				
					bill.setReturnNumber(Integer.valueOf(tf_ReturnNumber.getText()));
					bill.setBillNumber(Integer.valueOf(billNumber.getText()));
					bill.setCustomerMobileNo(Long.parseLong(customerMobileNo.getText()));
					bill.setItemDetails(itemList);
					bill.setBillNetSalesAmt(Double.valueOf(billNetSalesAmt.getText()));
					bill.setNoOfItems(Integer.valueOf(tf_NoOfItems.getText()));
					bill.setReturnpaymentMode(billPayMode.getText());
					bill.setTotalQuanity(Integer.valueOf(tf_TotalQty.getText()));
					bill.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
					bill.setComments(tf_Comments.getText());
					bill.setBillDate(new java.sql.Date(PDFUtils.getFormattedDate(billDate.getText()).getTime()));
					bill.setBillPaymentMode(billPayMode.getText());
					bill.setTotalAmount(Double.valueOf(tf_ReturnTotalAmt.getText()));
					bill.setNewBillnetSalesAmt(Double.valueOf(billNetSalesAmt.getText())-Double.valueOf(tf_ReturnTotalAmt.getText()));
					bill.setTax(tax);
					bill.setDiscount(discount);
					bill.setTaxAmount(Double.valueOf(tf_TaxAmt.getText()));
					bill.setDiscountAmount(Double.valueOf(tf_DiscountAmt.getText()));
					bill.setSubTotal(Double.valueOf(tf_subTotal.getText()));
					bill.setGrandTotal(Double.valueOf(tf_grandTotal.getText()));
					bill.setReturnPurchaseAmt(billPurchaseAmt);
					//Save Bill Details
					Customer customer = UserServices.getCustomerDetails(bill.getCustomerMobileNo());
					//Added condition to check customer pending balance is greater than or equal to current return amount
					if("PENDING".equals(bill.getBillPaymentMode()) && customer.getBalanceAmt()>=bill.getTotalAmount()){
						StatusDTO status = SalesReturnServices.saveReturnDetails(bill);
						StatusDTO	statusAddPendingAmt=new StatusDTO(-1);
						StatusDTO 	statusUpdatePayHistory= new StatusDTO(-1);
						if(status.getStatusCode()==0){
							//Update the customer balance for pending payment
								statusUpdatePayHistory = UserServices.addCustomerPaymentHistory(bill.getCustomerMobileNo(),0,bill.getTotalAmount(), AppConstants.DEBIT, "SALES RETURN Based on R.No : "+bill.getReturnNumber());
								statusAddPendingAmt = UserServices.settleUpCustomerBalance(bill.getCustomerMobileNo(), bill.getTotalAmount());
						}
						if(status.getStatusCode() ==0 && statusAddPendingAmt.getStatusCode() ==0 
									&&statusUpdatePayHistory.getStatusCode() ==0 ){
							ProductHistoryServices.addProductStockLedger(getProductList(itemList), AppConstants.STOCK_IN, AppConstants.SALES_RETURN);
								JOptionPane.showMessageDialog(getContentPane(), "Return Details Saved Sucessfully !");
								btnSaveBill.setEnabled(false);
								//Show Bill Amount Adjustment Window
								disableAfterSave();
								new SalesReturnAdjustmentUI(bill, mainFrame).setVisible(true);
							}
							else{
								JOptionPane.showMessageDialog(getContentPane(),"Error Occured while saving Bill !","Error",JOptionPane.WARNING_MESSAGE);
							}
					}else{
						if("PENDING".equals(bill.getBillPaymentMode())){
							JOptionPane.showMessageDialog(getContentPane(), "Customer Balance is less than return amount. Please check customer payment history !");
						}
					}
					//Cash Bill return
					if(!"PENDING".equals(bill.getBillPaymentMode())){
						StatusDTO status = SalesReturnServices.saveReturnDetails(bill);
						if(status.getStatusCode()==0){
							ProductHistoryServices.addProductStockLedger(getProductList(itemList), AppConstants.STOCK_IN, AppConstants.SALES_RETURN);
							JOptionPane.showMessageDialog(getContentPane(), "Return Details Saved Sucessfully !");
							btnSaveBill.setEnabled(false);
							//Show Bill Amount Adjustment Window
							disableAfterSave();
							new SalesReturnAdjustmentUI(bill, mainFrame).setVisible(true);
						}else{
							JOptionPane.showMessageDialog(getContentPane(),"Error Occured while saving Bill !","Error",JOptionPane.WARNING_MESSAGE);
						}
					}
					
				}
			else{
				JOptionPane.showMessageDialog(getContentPane(), "No Items to return !");
			}	
		}
}
	}
	
	/*private double getReturnPurchaseAmount(List<ItemDetails> itemList) {
		double purchaseAmount =0;
		for(ItemDetails item : itemList){
			purchaseAmount+=item.getPurchasePrice()*item.getQuantity();
		}
		System.out.println("Sales Return Purchase Amt : "+purchaseAmount);
		return purchaseAmount;
	}*/

	private List<Product> getProductList(List<ItemDetails> itemList){
		List<Product> productList = new ArrayList<Product>();
		for(ItemDetails item : itemList){
			Product p = new Product();
			p.setProductCode(item.getItemNo());
			p.setQuanity(item.getQuantity());
			p.setDescription("Sales Return Based on Return No.: "+item.getBillNumber());
			productList.add(p);
		}
		return productList;
		
	}
}
