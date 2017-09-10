package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.LabelAlignment;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.BillDetails;
import com.shopbilling.dto.Customer;
import com.shopbilling.dto.ItemDetails;
import com.shopbilling.dto.Product;
import com.shopbilling.dto.StatusDTO;
import com.shopbilling.dto.UserDetails;
import com.shopbilling.services.AutoSuggestTable;
import com.shopbilling.services.ProductServices;
import com.shopbilling.services.UserServices;
import com.shopbilling.utils.PDFUtils;

public class POS extends JInternalFrame {

	private UserDetails userDetails;
	private JTextField billNumber;
	private JTextField customerName;
	private JTextField customerMobileNo;
	private JTextField billDate;
	private JLabel customerExistingBalance;
	private JButton addCustomer;
	private JTable table;
	private JTextField itemNo;
	private JTextField itemName;
	private JTextField MRP;
	private JTextField rate;
	private JTextField quantity;
	private JTextField amount;
	private JPanel itemDetailsPanel;
	private List<ItemDetails> itemList;
	private List<Integer> productsInTable = new ArrayList<Integer>();
	private HashMap<String,Product> productMap; 
	private HashMap<Long,Product> productMapWithBarcode;
	private DefaultTableModel productModel;
	private JTextField tf_NoOfItems;
	private JTextField tf_TotalQty;
	private JTextField tf_TotalAmount;
	private JTextField tf_TAX;
	private JTextField tf_GrossAmt;
	private JTextField tf_Discount;
	private JTextField tf_NetSalesAmt;
	JComboBox cb_PaymentMode;
	//JButton btnPrint;
	JButton btnSaveBill;
	JButton btnReset;
	JButton btnCashHelp;
	//
	private int noOfItems = 0;
	private int totalQty = 0;
	private double totalAmt = 0;
	private double grossAmt=0;
	//private double discountAmt=0;
	private double netSalesAmt=0;
	private double billPurchaseAmt=0;
	private double discountAmt=0;
	//Bill Customer 
	private Customer customer;
	private JTextField tf_DiscountAmt;
	private JLabel rupeeLabel;
	private boolean mouseListnerFlag=true;
	private BillDetails bill;
	private JTextField tf_barCode;
	private JComboBox cb_newFocustTo;
	private JPanel panelBarCode;
	private JButton btn_refresh;
	private HashMap<String,Customer> customerMap;
	private Long customerMobileNumber;
	private String custName;
	private JLabel custNamelbl;

	/**
	 * Create the frame.
	 */
	public POS() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Point Of Sale");
		Font font = new Font("Dialog", Font.BOLD, 13);
		
		JPanel billDetailsPanel = new JPanel();
		billDetailsPanel.setBounds(10, 11, 930, 131);
		itemDetailsPanel = new JPanel();
		itemDetailsPanel.setBounds(10, 148, 930, 450);
		billDetailsPanel.requestFocus();
		billDetailsPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Bill Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		Border borderBlue = new LineBorder(SystemColor.inactiveCaption);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		//Bill Details Form
		DesignGridLayout layout = new DesignGridLayout(billDetailsPanel);
		layout.labelAlignment(LabelAlignment.RIGHT);
		billNumber = new JTextField();
		billNumber.setEditable(false);
		billNumber.setText(String.valueOf(PDFUtils.getBillNumber()));
		billNumber.setFont(font);
		customerName = new JTextField(20);
		customerName.setDisabledTextColor(Color.BLACK);
		//customerMobileNo = new JTextField(20);
		customerMobileNo = new AutoSuggestTable<String>(getCustomerNameList());
		customerMobileNo.setBorder(borderBlue);
		itemName = new AutoSuggestTable<String>(getProductName());
		getProductMapWithBarCode();
		customerMobileNo.setFont(font);
		customerMobileNo.setDisabledTextColor(Color.BLACK);
		customerName.setFont(font);
		customerName.setEditable(false);
		customerName.setVisible(false);
		billDate = new JTextField(20);
		billDate.setFont(font);
		billDate.setEditable(false);
		customerExistingBalance = new JLabel();
		customerExistingBalance.setFont(new Font("Tahoma", Font.BOLD, 16));
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		billDate.setText(sdf.format(new Date()));
		addCustomer = new JButton("Add Customer");
		addCustomer.setEnabled(false);
		addCustomer.setVisible(false);
		custNamelbl = new JLabel("Customer Name *:");
		custNamelbl.setVisible(false);
		layout.row().grid(new JLabel("Bill Number :"))	.add(billNumber)	.grid( new JLabel("Select Customer *:"))	.add(customerMobileNo);
		layout.row().grid(new JLabel("Bill Date :"))	.add(billDate).grid(custNamelbl)	.add(customerName);
		layout.emptyRow();
		JLabel icon = new  JLabel();
		icon.setIcon(new ImageIcon(NewBill.class.getResource("/images/currency_sign_rupee_lock_open.png")));
		layout.row().grid(new JLabel("Customer Existing Balance :")).addMulti(icon,customerExistingBalance).empty().add(addCustomer);
		//Add Customer button action
		addCustomer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	if(customerName.getText().equals("") || customerMobileNo.getText().equals("")){
        			JOptionPane.showMessageDialog(getContentPane(), "Please Select/Enter Customer Details!");
        		}else{
        			Customer cust = new Customer();
        			cust.setCustMobileNumber(Long.parseLong(customerMobileNo.getText()));
        			cust.setCustName(customerName.getText());
        			StatusDTO status = UserServices.addCustomer(cust);
        			if(status.getStatusCode()==0){
        				JOptionPane.showMessageDialog(getContentPane(), "Customer Added Successfully");
        				addCustomer.setEnabled(false);
        				addCustomer.setVisible(false);
            			customerName.setEditable(false);
            			customerMobileNo.setEditable(false);
            			customerMobileNumber = Long.parseLong(customerMobileNo.getText());
	            		custName = customerName.getText();
            			setNewFoucus();
        			}else{
        				if(status.getStatusCode()==-1 && status.getException().contains("Duplicate entry")){
        					JOptionPane.showMessageDialog(getContentPane(),"Customer already exists for Mobile number : "+customerMobileNo.getText(),"Error",JOptionPane.WARNING_MESSAGE);
        				}else{
        					JOptionPane.showMessageDialog(getContentPane(),"Error occured ","Error",JOptionPane.WARNING_MESSAGE);
        				}
        			}
        		}
            }});
		
		/*customerMobileNo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	//customer =UserServices.getCustomerDetails(Long.parseLong(customerMobileNo.getText()));
            	customer = customerMap.get(customerMobileNo.getText());
            	if(customer==null){
            		addCustomer.setEnabled(true);
            		customerName.setText("");
            		customerExistingBalance.setText("0.00");
            		JOptionPane.showMessageDialog(getContentPane(), "Customer Details Not Found!");
            		customerName.requestFocus();
            	}else{
            		customerName.setText(customer.getCustName());
            		customerExistingBalance.setText(AppUtils.getDecimalFormat(customer.getBalanceAmt()));
            		addCustomer.setEnabled(false);
            		setNewFoucus();
            	}
            }});*/
		
		customerMobileNo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					customer = customerMap.get(customerMobileNo.getText());
	            	if(customer==null){
	            		addCustomer.setEnabled(true);
	            		customerName.setEditable(true);
	            		customerName.setText("");
	            		customerName.setVisible(true);
	            		custNamelbl.setVisible(true);
	            		addCustomer.setVisible(true);
	            		customerExistingBalance.setText("0.00");
	            		JOptionPane.showMessageDialog(getContentPane(), "Customer Details Not Found!");
	            		customerName.requestFocus();
	            	}else{
	            		customerName.setText(customer.getCustName());
	            		customerName.setEditable(false);
	            		customerExistingBalance.setText(PDFUtils.getDecimalFormat(customer.getBalanceAmt()));
	            		addCustomer.setEnabled(false);
	            		customerMobileNumber = customer.getCustMobileNumber();
	            		custName = customer.getCustName();
	            		setNewFoucus();
	            	}
				}
			}
		});
		
		
		itemDetailsPanel.setBorder(new TitledBorder(null, "Item Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel paymentDetails = new JPanel();
		paymentDetails.setBounds(950, 148, 330, 450);
		paymentDetails.setBorder(new TitledBorder(null, "Payment Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(17, 53, 884, 324);
		 Font amtFont = new Font("Dialog", Font.BOLD, 16);
		itemNo = new JTextField();
		itemNo.setEnabled(false);
		itemNo.setBounds(17, 27, 102, 27);
		itemNo.setColumns(10);
		itemNo.setFont(font);
		itemNo.setDisabledTextColor(Color.BLUE);
		itemNo.setBorder(border);
		itemName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					if(!itemName.getText().equals("")){
						quantity.requestFocus();
						setProductDetails(itemName.getText());
					}
					
				}
			}
		});
		panelBarCode = new JPanel();
		panelBarCode.setBorder(new TitledBorder(null, "Barcode Entry", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBarCode.setLayout(null);
		panelBarCode.setBounds(950, 73, 330, 64);
		getContentPane().add(panelBarCode);
		tf_barCode = new JTextField();
		tf_barCode.setHorizontalAlignment(SwingConstants.CENTER);
		tf_barCode.setFont(new Font("Tahoma", Font.BOLD, 15));
		tf_barCode.setBounds(37, 20, 256, 29);
		panelBarCode.add(tf_barCode);
		tf_barCode.setColumns(10);
		//BarCode Event
		tf_barCode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					if(!tf_barCode.getText().equals("")){
						setProductDetailsWithBarCode(Long.valueOf(tf_barCode.getText().trim()));
						tf_barCode.setText("");
						setNewFoucus();
						
					}
					
				}
			}
		});
		itemName.setBounds(118, 27, 329, 27);
		itemName.setColumns(10);
		itemName.setFont(font);
		itemName.setForeground(Color.BLUE);
		itemName.setBorder(border);
		MRP = new JTextField();
		MRP.setEnabled(false);
		MRP.setBounds(447, 27, 102, 27);
		MRP.setColumns(10);
		MRP.setFont(font);
		MRP.setDisabledTextColor(Color.BLUE);
		MRP.setBorder(border);
		rate = new JTextField();
		rate.setEnabled(false);
		rate.setBounds(548, 27, 102, 27);
		rate.setColumns(10);
		rate.setFont(font);
		rate.setDisabledTextColor(Color.BLUE);
		rate.setBorder(border);
		table = new JTable();
		
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
		table.setFont(new Font("Tahoma", Font.BOLD, 15));
		table.setRowHeight(20);
		scrollPane.setViewportView(table);
		 JTableHeader header = table.getTableHeader();
		 header.setFont(new Font("Dialog", Font.BOLD, 15));
		 
		 table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 table.getColumnModel().getColumn(0).setPreferredWidth(100);
		 table.getColumnModel().getColumn(1).setPreferredWidth(330);
		 table.getColumnModel().getColumn(2).setPreferredWidth(100);
		 table.getColumnModel().getColumn(3).setPreferredWidth(100);
		 table.getColumnModel().getColumn(4).setPreferredWidth(100);
		 table.getColumnModel().getColumn(5).setPreferredWidth(150);
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
		quantity.setBounds(649, 27, 100, 27);
		quantity.setForeground(Color.BLUE);
		quantity.setBorder(border);
		itemDetailsPanel.add(quantity);
		quantity.setColumns(10);
		quantity.setFont(font);
		amount = new JTextField();
		amount.setEnabled(false);
		amount.setBounds(748, 27, 150, 27);
		itemDetailsPanel.add(amount);
		amount.setColumns(10);
		amount.setFont(font);
		amount.setDisabledTextColor(Color.BLUE);
		amount.setBorder(border);
		getContentPane().setLayout(null);
		getContentPane().add(billDetailsPanel);
		getContentPane().add(itemDetailsPanel);
		//Font buttonFont = new Font("Dialog", Font.BOLD, 20);
		btnSaveBill = new JButton("Save & Print");
		btnSaveBill.setIcon(new ImageIcon(NewBill.class.getResource("/images/save.png")));
		btnSaveBill.setFont(new Font("Dialog", Font.BOLD, 12));
		btnSaveBill.setBounds(100, 388, 145, 51);
		itemDetailsPanel.add(btnSaveBill);
		
		/*btnPrint = new JButton("Print");
		btnPrint.setIcon(new ImageIcon(NewBill.class.getResource("/images/printer_picture.png")));
		btnPrint.setBounds(239, 388, 102, 51);
		btnPrint.setEnabled(false);
		btnPrint.setFont(new Font("Dialog", Font.BOLD, 12));
		itemDetailsPanel.add(btnPrint);*/
		
		btnReset = new JButton("New Bill");
		btnReset.setIcon(new ImageIcon(NewBill.class.getResource("/images/document_new.png")));
		btnReset.setBounds(390, 388, 145, 51);
		btnReset.setFont(new Font("Dialog", Font.BOLD, 12));
		itemDetailsPanel.add(btnReset);
		
		btnCashHelp = new JButton("Cash Help");
		btnCashHelp.setIcon(new ImageIcon(NewBill.class.getResource("/images/dollars (1).png")));
		btnCashHelp.setBounds(675, 388, 145, 51);
		btnCashHelp.setFont(new Font("Dialog", Font.BOLD, 12));
		itemDetailsPanel.add(btnCashHelp);
		getContentPane().add(paymentDetails);
		paymentDetails.setLayout(null);
		
		JLabel lblNoOfItems = new JLabel("No. Of Items");
		lblNoOfItems.setBounds(20, 30, 91, 35);
		paymentDetails.add(lblNoOfItems);
		
		tf_NoOfItems = new JTextField();
		tf_NoOfItems.setBounds(120, 30, 180, 38);
		paymentDetails.add(tf_NoOfItems);
		tf_NoOfItems.setColumns(10);
		tf_NoOfItems.setEditable(false);
		tf_NoOfItems.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_NoOfItems.setFont(amtFont);
		tf_NoOfItems.setText("0");
		
		JLabel lblTotalQuantity = new JLabel("Total Quantity");
		lblTotalQuantity.setBounds(20, 69, 91, 35);
		paymentDetails.add(lblTotalQuantity);
		
		tf_TotalQty = new JTextField();
		tf_TotalQty.setColumns(10);
		tf_TotalQty.setBounds(120, 69, 180, 38);
		tf_TotalQty.setEditable(false);
		paymentDetails.add(tf_TotalQty);
		tf_TotalQty.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_TotalQty.setFont(amtFont);
		tf_TotalQty.setText("0");
		
		JLabel label = new JLabel("Sub Total");
		label.setBounds(20, 108, 91, 35);
		paymentDetails.add(label);
		
		tf_TotalAmount = new JTextField();
		tf_TotalAmount.setColumns(10);
		tf_TotalAmount.setBounds(120, 108, 180, 38);
		tf_TotalAmount.setEditable(false);
		paymentDetails.add(tf_TotalAmount);
		tf_TotalAmount.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_TotalAmount.setFont(amtFont);
		tf_TotalAmount.setText("0.00");
		
		JLabel lblTax = new JLabel("TAX (%)");
		lblTax.setBounds(20, 303, 91, 35);
		paymentDetails.add(lblTax);
		
		tf_TAX = new JTextField();
		tf_TAX.setColumns(10);
		tf_TAX.setBounds(120, 303, 180, 38);
		tf_TAX.setText("0.00");
		paymentDetails.add(tf_TAX);
		tf_TAX.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_TAX.setFont(amtFont);
		tf_TAX.setDisabledTextColor(Color.BLACK);
		if("Y".equals(PDFUtils.getAppDataValues("TAX_ENABLED").get(0))){
			tf_TAX.setEditable(true);
		}else{
			tf_TAX.setEditable(false);
		}
			
		
		JLabel lblGrossAmount = new JLabel("Grand Total");
		lblGrossAmount.setBounds(20, 225, 91, 35);
		paymentDetails.add(lblGrossAmount);
		
		tf_GrossAmt = new JTextField();
		tf_GrossAmt.setColumns(10);
		tf_GrossAmt.setBounds(120, 225, 180, 38);
		tf_GrossAmt.setEditable(false);
		paymentDetails.add(tf_GrossAmt);
		tf_GrossAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_GrossAmt.setFont(amtFont);
		tf_GrossAmt.setText("0.00");
		
		JLabel lblPaymentMode = new JLabel("Payment Mode");
		lblPaymentMode.setBounds(20, 264, 91, 35);
		paymentDetails.add(lblPaymentMode);
		
		cb_PaymentMode = new JComboBox();
		cb_PaymentMode.setBounds(120, 264, 180, 38);
		PDFUtils.populateDropdown(cb_PaymentMode,"PAYMENT_MODE");
		paymentDetails.add(cb_PaymentMode);
		
		JLabel lblDiscount = new JLabel("Discount (%)");
		lblDiscount.setBounds(20, 147, 91, 35);
		paymentDetails.add(lblDiscount);
		
		tf_Discount = new JTextField();
		tf_Discount.setColumns(10);
		tf_Discount.setBounds(120, 147, 180, 38);
		paymentDetails.add(tf_Discount);
		tf_Discount.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_Discount.setFont(amtFont);
		tf_Discount.setText("0.00");
		tf_Discount.setDisabledTextColor(Color.BLACK);
		
		JLabel lblNetSa = new JLabel("Net Sales Amount");
		lblNetSa.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNetSa.setBounds(20, 345, 212, 20);
		paymentDetails.add(lblNetSa);
		
		tf_NetSalesAmt = new JTextField();
		tf_NetSalesAmt.setEditable(false);
		tf_NetSalesAmt.setForeground(Color.WHITE);
		tf_NetSalesAmt.setBackground(Color.GRAY);
		tf_NetSalesAmt.setColumns(10);
		tf_NetSalesAmt.setBounds(69, 376, 231, 38);
		paymentDetails.add(tf_NetSalesAmt);
		tf_NetSalesAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_NetSalesAmt.setFont(new Font("Dialog", Font.BOLD, 30));
		tf_NetSalesAmt.setText("0.00");
		
		JLabel lblDiscountAmount = new JLabel("Discount Amount");
		lblDiscountAmount.setBounds(20, 186, 103, 35);
		paymentDetails.add(lblDiscountAmount);
		
		tf_DiscountAmt = new JTextField();
		tf_DiscountAmt.setText("0.00");
		tf_DiscountAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_DiscountAmt.setFont(new Font("Dialog", Font.BOLD, 16));
		tf_DiscountAmt.setEditable(false);
		tf_DiscountAmt.setColumns(10);
		tf_DiscountAmt.setBounds(120, 186, 180, 38);
		paymentDetails.add(tf_DiscountAmt);
		
		rupeeLabel = new JLabel("New label");
		rupeeLabel.setBounds(10, 376, 49, 47);
		paymentDetails.add(rupeeLabel);
		rupeeLabel.setIcon(new ImageIcon(NewBill.class.getResource("/images/Rupee-64.png")));
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(SystemColor.inactiveCaption));
		panel.setBounds(950, 15, 330, 51);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewFocusTo = new JLabel("New Focus To ");
		lblNewFocusTo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewFocusTo.setBounds(10, 11, 102, 25);
		panel.add(lblNewFocusTo);
		
		cb_newFocustTo = new JComboBox();
		cb_newFocustTo.setBounds(117, 11, 179, 25);
		panel.add(cb_newFocustTo);
		//cb_newFocustTo.addItem("Item Name");
		//cb_newFocustTo.addItem("Barcode");
		PDFUtils.populateDropdown(cb_newFocustTo, AppConstants.NEW_FOCUS_TO);
		
		btn_refresh = new JButton("");
		btn_refresh.setToolTipText("Refresh Products");
		btn_refresh.setIcon(new ImageIcon(ProductProfitReportUI.class.getResource("/images/view_refresh.png")));
		btn_refresh.setBorder(BorderFactory.createEmptyBorder());
		btn_refresh.setBackground(SystemColor.menu);
		btn_refresh.setBounds(1290, 15, 24, 24);
		getContentPane().add(btn_refresh);
		
		btn_refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				productMap = getProductMap();
				productMapWithBarcode= getProductMapWithBarCode();
			}
		});
		
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
						if(row==0){
							tf_Discount.setText("0.00");
							tf_TAX.setText("0.00");
						}	
					}
			}
			}
		});
		
		/*customerMobileNo.addKeyListener(new KeyAdapter() {
			   public void keyTyped(KeyEvent e) {
			      char c = e.getKeyChar();
			      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
			         e.consume();  // ignore event
			      }
			   }
			});*/
		
		quantity.addKeyListener(new KeyAdapter() {
			   public void keyTyped(KeyEvent e) {
			      char c = e.getKeyChar();
			      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
			         e.consume();  // ignore event
			      }
			      if(c=='\n'){
			    	  if(!itemName.getText().equals(""))
			    	  addRecordToTable(productMap.get(itemName.getText()));
			    	  resetItemFields();
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
		
		tf_Discount.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				setGrossAmt();
				setNetSaleAmount();
			}
			});
		tf_TAX.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				setNetSaleAmount();
			}
			});
		//Save Button Action
		btnSaveBill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean billSaveStatus=false;
				createItemList();
				bill = new BillDetails();
				if(customerMobileNo.getText().equals("") || customerName.getText().equals("")
						|| custName==null || customerMobileNumber==null){
					JOptionPane.showMessageDialog(getContentPane(), "Please Select/Enter Customer Details");
				}else if(productModel.getRowCount()>0){
					if(addCustomer.isEnabled()){
						JOptionPane.showMessageDialog(getContentPane(), "Please Add Customer Details !");
					}else{
						bill.setBillNumber(Integer.valueOf(billNumber.getText()));
						bill.setCustomerMobileNo(customerMobileNumber);
						bill.setCustomerName(custName);
						bill.setItemDetails(itemList);
						if(tf_Discount.getText().equals("")){
							bill.setDiscount(0.0);
							bill.setDiscountAmt(0.0);
						}else{
							bill.setDiscount(Double.valueOf(tf_Discount.getText()));
							bill.setDiscountAmt(Double.valueOf(tf_DiscountAmt.getText()));
						}
						
						bill.setGrandTotal(Double.valueOf(tf_GrossAmt.getText()));
						bill.setNetSalesAmt(Double.valueOf(tf_NetSalesAmt.getText()));
						bill.setNoOfItems(Integer.valueOf(tf_NoOfItems.getText()));
						bill.setPaymentMode((String)cb_PaymentMode.getSelectedItem());
						bill.setPurchaseAmt(billPurchaseAmt);
						bill.setTotalAmount(Double.valueOf(tf_TotalAmount.getText()));
						bill.setTotalQuanity(Integer.valueOf(tf_TotalQty.getText()));
						if(!tf_TAX.getText().equals(""))
							bill.setTax(Double.valueOf(tf_TAX.getText()));
						else
							bill.setTax(0);
						bill.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
						System.out.println("Bill Details : "+bill);
						//Save Bill Details
						StatusDTO status  = ProductServices.saveBillDetails(bill);
						StatusDTO	statusAddPendingAmt=new StatusDTO(-1);
						StatusDTO 	statusUpdatePayHistory= new StatusDTO(-1);
						if(status.getStatusCode()==0){
							if("PENDING".equals(bill.getPaymentMode())){
								statusUpdatePayHistory = UserServices.addCustomerPaymentHistory(bill.getCustomerMobileNo(), bill.getNetSalesAmt(),0, AppConstants.CREDIT, "BILL AMOUNT Based on B.No : "+bill.getBillNumber());
								statusAddPendingAmt = UserServices.addPendingPaymentToCustomer(bill.getCustomerMobileNo(), bill.getNetSalesAmt());
							}
						}
						if("PENDING".equals(bill.getPaymentMode())){
							if(status.getStatusCode() ==0 && statusAddPendingAmt.getStatusCode() ==0 
									&&statusUpdatePayHistory.getStatusCode() ==0 ){
								JOptionPane.showMessageDialog(getContentPane(), "Bill Saved Sucessfully !");
								//btnPrint.setEnabled(true);
								btnSaveBill.setEnabled(false);
								disableAfterSave();
							}
							else
								JOptionPane.showMessageDialog(getContentPane(), "Error Occured while saving Bill !");
						}else{
						if(status.getStatusCode() ==0){
							JOptionPane.showMessageDialog(getContentPane(), "Bill Saved Sucessfully !");
							//btnPrint.setEnabled(true);
							btnSaveBill.setEnabled(false);
							disableAfterSave();
						}
						else
							JOptionPane.showMessageDialog(getContentPane(), "Error Occured while saving Bill !");
						}
				}
					}
				else{
					JOptionPane.showMessageDialog(getContentPane(), "Add Items first to Save this Bill !");
				}
			}
		});
		//Reset Action
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetBillDetails();
			}
		});
		//Print Action
		/*btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//PrintServices.createPDF(bill);
				JOptionPane.showMessageDialog(getContentPane(), "Printing !");
				JasperUtils.createPDFWithJasper(JasperServices.createDataForBill(bill),AppConstants.BILL_PRINT_JASPER);
			}
		});*/
		//Cash Action
		btnCashHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*CashHelp cash = new CashHelp(tf_NetSalesAmt.getText());
				cash.setVisible(true);*/
			}
		});
	}


	//Set Product details for BarCode
	protected void setProductDetailsWithBarCode(Long productBarCode) {
		Product product = productMapWithBarcode.get(productBarCode);
		if(product==null){
			JOptionPane.showMessageDialog(getContentPane(), "Product not preset for Bar Code : "+productBarCode);
		}
		if(product!=null){
			itemNo.setText(String.valueOf(product.getProductCode()));
			//itemName.setText(product.getProductName());
			MRP.setText(PDFUtils.getDecimalFormat(product.getProductMRP()));
			rate.setText(PDFUtils.getDecimalFormat(product.getSellPrice()));
			quantity.setText("1");
			amount.setText(PDFUtils.getDecimalFormat(product.getSellPrice()));
			addRecordToTableWithBarCode(product);
			resetItemFields();
		}
		
	}

	private void addRecordToTableWithBarCode(Product product) {
		if(product!=null){
				if(product.getQuanity()>=Integer.valueOf(quantity.getText())){
					productsInTable.add(product.getProductCode());
					billPurchaseAmt+=product.getPurcasePrice()*Integer.valueOf(quantity.getText());
					//Add row to table
					productModel.addRow(new Object[]{product.getProductCode(), product.getProductName(), PDFUtils.getDecimalFormat(product.getProductMRP()), PDFUtils.getDecimalFormat(product.getSellPrice()),quantity.getText(),amount.getText(),product.getPurcasePrice()});
					setPaymentFields(Integer.valueOf(quantity.getText()),Double.valueOf(amount.getText()),productModel.getRowCount());
					setGrossAmt();
					setNetSaleAmount();
				}else{
					//JOptionPane.showMessageDialog(getContentPane(), "Product :  "+product.getProductName()+"  Available Stock is : "+product.getQuanity());
					JOptionPane.showMessageDialog(getContentPane(),"Product :  "+product.getProductName()+"  Available Stock is : "+product.getQuanity(),"Product Not Available",JOptionPane.WARNING_MESSAGE);
				}
		}
	}

	protected void setNewFoucus() {
		if(cb_newFocustTo.getSelectedItem().equals(AppConstants.ITEM_NAME)){
			itemDetailsPanel.requestFocus();
			itemName.requestFocus();
		}
		if(cb_newFocustTo.getSelectedItem().equals(AppConstants.BARCODE)){
			panelBarCode.requestFocus();
			tf_barCode.requestFocus();
		}
	}

	protected void setNetSaleAmount() {
		if(!tf_TAX.getText().equals("")){
			Double tax = Double.parseDouble(tf_TAX.getText());
			double tempDisc = grossAmt;
			tempDisc= tempDisc+(grossAmt/100)*tax;
			netSalesAmt = tempDisc;
			tf_NetSalesAmt.setText(PDFUtils.getDecimalFormat((PDFUtils.getDecimalRoundUp(netSalesAmt))));
		}else{
			netSalesAmt = grossAmt;
			tf_NetSalesAmt.setText(PDFUtils.getDecimalFormat((PDFUtils.getDecimalRoundUp(netSalesAmt))));
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
				if(product.getQuanity()>=Integer.valueOf(quantity.getText())){
					productsInTable.add(product.getProductCode());
					billPurchaseAmt+=product.getPurcasePrice()*Integer.valueOf(quantity.getText());
					System.out.println("Add Bill Purchase Amt : "+billPurchaseAmt);
					//Add row to table
					productModel.addRow(new Object[]{product.getProductCode(), product.getProductName(), PDFUtils.getDecimalFormat(product.getProductMRP()), PDFUtils.getDecimalFormat(product.getSellPrice()),quantity.getText(),amount.getText(),product.getPurcasePrice()});
					setPaymentFields(Integer.valueOf(quantity.getText()),Double.valueOf(amount.getText()),productModel.getRowCount());
					setGrossAmt();
					setNetSaleAmount();
				}else{
					//JOptionPane.showMessageDialog(getContentPane(), "Selected quantity for product is not available. Available Stock is: "+product.getQuanity());
					JOptionPane.showMessageDialog(getContentPane(),"Product :  "+product.getProductName()+"  Available Stock is : "+product.getQuanity(),"Product Not Available",JOptionPane.WARNING_MESSAGE);
				}
			
			}else{
				JOptionPane.showMessageDialog(getContentPane(), "Item already present!");
			}
		}
	}
	
	private void setPaymentFields(int newQuantity, Double amount, int rowCount) {

		noOfItems=getNoOfItems();
		totalQty+=newQuantity;
		totalAmt+=amount;
		netSalesAmt+=amount;
		grossAmt+=amount;
		setAmountQty();
	}

	private void setAmountQty() {
		tf_NoOfItems.setText(String.valueOf(noOfItems));
		tf_TotalQty.setText(String.valueOf(totalQty));
		tf_TotalAmount.setText(PDFUtils.getDecimalFormat(totalAmt));
		tf_NetSalesAmt.setText(PDFUtils.getDecimalFormat(netSalesAmt));
		if(!tf_Discount.getText().equals("")){
			setGrossAmt();
		}else{
			tf_GrossAmt.setText(PDFUtils.getDecimalFormat(grossAmt));	
		}	
		if(!tf_Discount.getText().equals("")){
			setGrossAmt();
		}else{
			tf_DiscountAmt.setText("0.00");	
		}
	}

	private void setGrossAmt() {
		if(!tf_Discount.getText().equals("")){
			Double disc = Double.parseDouble(tf_Discount.getText());
			double tempDisc = totalAmt;
			tempDisc= tempDisc-(totalAmt/100)*disc;
			discountAmt=(totalAmt/100)*disc;
			netSalesAmt = tempDisc;
			grossAmt = tempDisc;
			System.out.println(grossAmt);
			System.out.println("disocunt "+discountAmt);
			tf_GrossAmt.setText(PDFUtils.getDecimalFormat(grossAmt));
			tf_DiscountAmt.setText(PDFUtils.getDecimalFormat(discountAmt));
			tf_NetSalesAmt.setText(PDFUtils.getDecimalFormat((PDFUtils.getDecimalRoundUp(netSalesAmt))));
		}else{
			netSalesAmt = totalAmt;
			grossAmt = totalAmt;
			discountAmt=0.0;
			tf_GrossAmt.setText(PDFUtils.getDecimalFormat(grossAmt));
			tf_DiscountAmt.setText(PDFUtils.getDecimalFormat(discountAmt));
			tf_NetSalesAmt.setText(PDFUtils.getDecimalFormat((PDFUtils.getDecimalRoundUp(netSalesAmt))));
		}
		
	}

	private void resetItemFields() {
		itemNo.setText("");
		itemName.setText("");
		setNewFoucus();
		MRP.setText("");
		rate.setText("");
		quantity.setText("");
		amount.setText("");		
		
	}
	
	private void createItemList(){
		ItemDetails item = null;
		HashMap <Integer,ItemDetails> itemsMap = new HashMap<Integer, ItemDetails>();
		itemList = new ArrayList<ItemDetails>();
		for(int i=0;i<table.getRowCount();i++){
			item = new ItemDetails();
			if(!itemsMap.containsKey(Integer.valueOf(table.getModel().getValueAt(i, 0).toString()))){
				
				item.setItemNo(Integer.valueOf(table.getModel().getValueAt(i, 0).toString()));
				item.setItemName(table.getModel().getValueAt(i, 1).toString());
				item.setMRP(Double.valueOf(table.getModel().getValueAt(i, 2).toString()));
				item.setRate(Double.valueOf(table.getModel().getValueAt(i, 3).toString()));
				item.setQuantity(Integer.valueOf(table.getModel().getValueAt(i, 4).toString()));
				item.setPurchasePrice(Double.valueOf(table.getModel().getValueAt(i, 6).toString()));
				item.setBillNumber(Integer.valueOf(billNumber.getText()));
				
				itemsMap.put(Integer.valueOf(table.getModel().getValueAt(i, 0).toString()),item);
			}else{
				ItemDetails tempItem = itemsMap.get(Integer.valueOf(table.getModel().getValueAt(i, 0).toString()));
				int qty = tempItem.getQuantity()+1;
				tempItem.setQuantity(qty);
				itemsMap.put(Integer.valueOf(table.getModel().getValueAt(i, 0).toString()),tempItem);
			}
		}
		 for(Entry<Integer,ItemDetails> entry: itemsMap.entrySet()) {
			 itemList.add(entry.getValue());
		    }
	}
	
	private int getNoOfItems(){
		HashSet<Integer> uniqueProducts = new HashSet<Integer>();	
		for(int i=0;i<table.getRowCount();i++){
			uniqueProducts.add(Integer.valueOf(table.getModel().getValueAt(i, 0).toString()));
		}
		return uniqueProducts.size();
	}
	
	private void resetBillDetails(){
		productModel.setRowCount(0);
		billNumber.setText(String.valueOf(PDFUtils.getBillNumber()));
		billDate.setText(PDFUtils.getFormattedDate(new Date()));
		customerMobileNo.setText("");
		custName=null;
		customerMobileNumber = null;
		custNamelbl.setVisible(false);
		customerName.setVisible(false);
		addCustomer.setVisible(false);
		customerName.setText("");
		customerExistingBalance.setText("");
		resetItemFields();
		tf_NoOfItems.setText("0");
		tf_TotalQty.setText("0");
		tf_NetSalesAmt.setText("0.00");
		tf_GrossAmt.setText("0.00");
		tf_TAX.setText("0.00");
		tf_Discount.setText("0.00");
		tf_DiscountAmt.setText("0.00");
		cb_PaymentMode.setSelectedIndex(0);
		tf_TotalAmount.setText("0.00");
		noOfItems = 0;
		totalQty = 0;
		totalAmt = 0;
		grossAmt=0;
		discountAmt=0;
		netSalesAmt=0;
		billPurchaseAmt=0;
		productsInTable.clear();
		if(itemList!=null)
		itemList.clear();
		btnSaveBill.setEnabled(true);
		//btnPrint.setEnabled(false);
		productMap = getProductMap();
		productMapWithBarcode = getProductMapWithBarCode();
		customerMap = getCustomerMap();
		enableForNewBill();
	}
	
	private void disableAfterSave(){
		customerMobileNo.setEditable(false);
		customerName.setEditable(false);
		tf_Discount.setEnabled(false);
		tf_TAX.setEnabled(false);
		itemName.setEnabled(false);
		quantity.setEnabled(false);
		cb_PaymentMode.setEnabled(false);
		mouseListnerFlag = false;
		//billDate.setEnabled(false);
		tf_barCode.setEnabled(false);
	}
	private void enableForNewBill(){
		customerMobileNo.setEnabled(true);
		customerMobileNo.setEditable(true);
		customerName.setEnabled(true);
		tf_Discount.setEnabled(true);
		tf_TAX.setEnabled(true);
		itemName.setEnabled(true);
		quantity.setEnabled(true);
		cb_PaymentMode.setEnabled(true);
		customerMobileNo.requestFocus();
		mouseListnerFlag=true;
		//billDate.setEnabled(true);
		tf_barCode.setEnabled(true);
	}
	
	public List<String> getCustomerNameList(){
		
		List<String> customerNameList = new ArrayList<String>();
		customerMap = new HashMap<String, Customer>();
		for(Customer cust :UserServices.getAllCustomers()){
			customerNameList.add(cust.getCustName()+" : "+cust.getCustMobileNumber());
			customerMap.put(cust.getCustName()+" : "+cust.getCustMobileNumber(),cust);
		}
		return customerNameList;
	}
	
	public HashMap<String,Customer> getCustomerMap(){
		customerMap = new HashMap<String, Customer>();
		for(Customer cust :UserServices.getAllCustomers()){
			customerMap.put(cust.getCustName()+" : "+cust.getCustMobileNumber(),cust);
		}
		return customerMap;
	}
}
