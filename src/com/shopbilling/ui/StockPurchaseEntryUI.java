package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
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
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.Product;
import com.shopbilling.dto.StatusDTO;
import com.shopbilling.dto.StockItemDetails;
import com.shopbilling.dto.Supplier;
import com.shopbilling.dto.SupplierInvoiceDetails;
import com.shopbilling.services.AutoSuggestTable;
import com.shopbilling.services.ProductHistoryServices;
import com.shopbilling.services.ProductServices;
import com.shopbilling.services.SupplierServices;
import com.shopbilling.utils.PDFUtils;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class StockPurchaseEntryUI extends JInternalFrame {
	
	private JPanel contentPane;
	DefaultTableModel model;
	private JTextField tf_invoiceNumber;
	private JTextField tf_Comments;
	private JTable stockItemTable;
	DefaultTableModel stockItemModel;
	private JTextField tf_ItemName;
	private JTextField tf_ItemStock;
	private JTextField tf_PurPrice;
	private JTextField tf_TAX;
	private JTextField tf_ItemRate;
	private JTextField tf_ItemQty;
	private JTextField tf_NoOfItems;
	private JTextField tf_TotalQty;
	private JTextField tf_TotalAmtWOTax;
	private JTextField tf_TotalTax;
	private JTextField tf_TotalMRPAmt;
	private JTextField tf_ExtraCharges;
	private JTextField tf_InvoiceAmtPaid;
	private HashMap<String,Product> productMap; 
	private HashMap<String,Integer> supplierIdMap; 
	private List<String> productsInTable = new ArrayList<String>();
	private List<StockItemDetails> stockItemList;
	private JButton btn_SaveStock;
	private JButton btn_ResetStock;
	private SupplierInvoiceDetails supplierInvoiceDetails;
	private JDateChooser dc_invoiceDate;
	private JComboBox<String> cb_Suppliers;
	private JComboBox cb_PayMode;
	private int noOfItems = 0;
	private int totalQty = 0;
	private double totalAmtWOTax = 0;
	private double totalMRPAmount=0;
	private double totalTaxAmount=0;
	private double invoiceAmount=0;
	private double var_amount=0;
	private int stockNumber=0;
	private boolean mouseListnerFlag=true;
	private HashMap<Integer, SupplierInvoiceDetails> stocksMap;
	
	/**
	 * Create the frame.
	 */
	public StockPurchaseEntryUI() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Stock Purchase Entry");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setResizable(false);
		//fix the frame location
		//setUndecorated(true);
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_3.setBounds(10, 11, 1150, 579);
		contentPane.add(panel_3);
		panel_3.setLayout(null);
		//Product Form Start
		JPanel invoiceDetailsPanel = new JPanel();
		invoiceDetailsPanel.setBounds(7, 11, 837, 124);
		invoiceDetailsPanel.setBorder(new TitledBorder(null, "Invoice Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.add(invoiceDetailsPanel);
		invoiceDetailsPanel.setLayout(null);
		stockNumber = PDFUtils.getBillNumber();
		JLabel lblNewLabel = new JLabel("Invoice Date *:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(10, 21, 124, 25);
		invoiceDetailsPanel.add(lblNewLabel);
		
		dc_invoiceDate = new JDateChooser();
		dc_invoiceDate.setBounds(144, 21, 180, 25);
		dc_invoiceDate.setFont(new Font("Tahoma", Font.BOLD, 12));
		((JTextFieldDateEditor)dc_invoiceDate.getDateEditor()).setDisabledTextColor(Color.darkGray);
		invoiceDetailsPanel.add(dc_invoiceDate);
		
		JLabel lblInvoiceNo = new JLabel("Invoice Number *:");
		lblInvoiceNo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblInvoiceNo.setBounds(10, 52, 124, 25);
		invoiceDetailsPanel.add(lblInvoiceNo);
		
		JLabel lblComments = new JLabel("Comments :");
		lblComments.setHorizontalAlignment(SwingConstants.RIGHT);
		lblComments.setBounds(10, 85, 124, 25);
		invoiceDetailsPanel.add(lblComments);
		
		tf_invoiceNumber = new JTextField();
		tf_invoiceNumber.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_invoiceNumber.setBounds(144, 52, 180, 25);
		invoiceDetailsPanel.add(tf_invoiceNumber);
		tf_invoiceNumber.setDisabledTextColor(Color.black);
		tf_invoiceNumber.setColumns(10);
		
		tf_Comments = new JTextField();
		tf_Comments.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_Comments.setColumns(10);
		tf_Comments.setBounds(144, 85, 301, 25);
		tf_Comments.setDisabledTextColor(Color.black);
		invoiceDetailsPanel.add(tf_Comments);
		
		JLabel lblSupplierDetails = new JLabel("Supplier Details *:");
		lblSupplierDetails.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSupplierDetails.setBounds(408, 21, 124, 25);
		invoiceDetailsPanel.add(lblSupplierDetails);
		
		cb_Suppliers = new JComboBox<String>();
		cb_Suppliers.setBounds(542, 21, 237, 25);
		populateSupplier(cb_Suppliers);
		//to set black color when disabled
		cb_Suppliers.setRenderer(new DefaultListCellRenderer() {
		        @Override
		        public void paint(Graphics g) {
		            setForeground(Color.BLACK);
		            super.paint(g);
		        }
		    });
		invoiceDetailsPanel.add(cb_Suppliers);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Enter Stock Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_5.setBounds(7, 143, 837, 426);
		panel_3.add(panel_5);
		panel_5.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 58, 817, 294);
		panel_5.add(scrollPane_1);
		
		stockItemTable = new JTable();
		scrollPane_1.setViewportView(stockItemTable);
		
		stockItemModel = new DefaultTableModel(){
			 boolean[] columnEditables = new boolean[] {
					 false, false, false, false,false,false,false,false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
		 stockItemModel.setColumnIdentifiers(new String[] {
				"Item Name", "MRP", "TAX(%)", "Rate","Qty", "Amount","New Purchase Price","Item No"}
	       );
		 stockItemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		 JTableHeader stockTableHeader = stockItemTable.getTableHeader();
		 stockTableHeader.setFont(new Font("Dialog", Font.BOLD, 15));
		 stockItemTable.setModel(stockItemModel);
		// stockItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 stockItemTable.setFont(new Font("Tahoma", Font.PLAIN, 14));
		 stockItemTable.setRowHeight(20);
		 Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		 Font font = new Font("Dialog", Font.BOLD, 13);
		 tf_ItemName = new AutoSuggestTable<String>(getProductName());;
		 tf_ItemName.setBounds(10, 34, 301, 25);
		 tf_ItemName.setFont(font);
		 tf_ItemName.setForeground(Color.BLUE);
		 panel_5.add(tf_ItemName);
		 tf_ItemName.setColumns(10);
		 tf_ItemName.setBorder(border);
		 tf_ItemStock = new JTextField();
		 tf_ItemStock.setEnabled(false);
		 tf_ItemStock.setBounds(310, 34, 101, 25);
		 tf_ItemStock.setBorder(border);
		 tf_ItemStock.setFont(font);
		 tf_ItemStock.setDisabledTextColor(Color.BLUE);
		 panel_5.add(tf_ItemStock);
		 tf_ItemStock.setColumns(10);
		 
		 tf_PurPrice = new JTextField();
		 tf_PurPrice.setEnabled(false);
		 tf_PurPrice.setColumns(10);
		 tf_PurPrice.setBounds(410, 34, 101, 25);
		 tf_PurPrice.setBorder(border);
		 tf_PurPrice.setDisabledTextColor(Color.BLUE);
		 tf_PurPrice.setFont(font);
		 panel_5.add(tf_PurPrice);
		 
		 tf_TAX = new JTextField();
		 tf_TAX.setColumns(10);
		 tf_TAX.setBounds(510, 34, 101, 25);
		 tf_TAX.setBorder(border);
		 tf_TAX.setForeground(Color.BLUE);
		 tf_TAX.setFont(font);
		 panel_5.add(tf_TAX);
		 
		 tf_ItemRate = new JTextField();
		 tf_ItemRate.setColumns(10);
		 tf_ItemRate.setBounds(610, 34, 101, 25);
		 tf_ItemRate.setBorder(border);
		 tf_ItemRate.setForeground(Color.BLUE);
		 tf_ItemRate.setFont(font);
		 panel_5.add(tf_ItemRate);
		 
		 tf_ItemQty = new JTextField();
		 tf_ItemQty.setColumns(10);
		 tf_ItemQty.setBounds(710, 34, 115, 25);
		 tf_ItemQty.setBorder(border);
		 tf_ItemQty.setForeground(Color.BLUE);
		 tf_ItemQty.setFont(font);
		 panel_5.add(tf_ItemQty);
		 
		 JLabel lblNewLabel_1 = new JLabel("Item Name");
		 lblNewLabel_1.setBounds(13, 18, 81, 14);
		 panel_5.add(lblNewLabel_1);
		 
		 JLabel lblStock = new JLabel("Stock");
		 lblStock.setBounds(315, 18, 81, 14);
		 panel_5.add(lblStock);
		 
		 JLabel lblPurRate = new JLabel("Pur Price");
		 lblPurRate.setBounds(413, 18, 81, 14);
		 panel_5.add(lblPurRate);
		 
		 JLabel lblRate = new JLabel("TAX(%)");
		 lblRate.setBounds(512, 18, 81, 14);
		 panel_5.add(lblRate);
		 
		 JLabel lblQuantity = new JLabel("Rate");
		 lblQuantity.setBounds(613, 18, 81, 14);
		 panel_5.add(lblQuantity);
		 
		 JLabel lblAmount = new JLabel("Quantity");
		 lblAmount.setBounds(712, 18, 81, 14);
		 panel_5.add(lblAmount);
		 
		 btn_SaveStock = new JButton("Save Stock");
		 btn_SaveStock.setIcon(new ImageIcon(StockPurchaseEntryUI.class.getResource("/images/save.png")));
		 btn_SaveStock.setBounds(152, 363, 140, 51);
		 panel_5.add(btn_SaveStock);
		 
		 btn_ResetStock = new JButton("New Entry");
		 btn_ResetStock.setIcon(new ImageIcon(StockPurchaseEntryUI.class.getResource("/images/document_new.png")));
		 btn_ResetStock.setBounds(561, 363, 140, 51);
		 panel_5.add(btn_ResetStock);
		 //stockItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 stockItemTable.getColumnModel().getColumn(0).setPreferredWidth(300);
		 stockItemTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		 stockItemTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		 stockItemTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		 stockItemTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		 stockItemTable.getColumnModel().getColumn(5).setPreferredWidth(113);
		 //hide new purchase price column
		 stockItemTable.getColumnModel().getColumn(6).setMinWidth(0);
		 stockItemTable.getColumnModel().getColumn(6).setMaxWidth(0);
		 stockItemTable.getColumnModel().getColumn(6).setWidth(0);
		 //hide Item No column
		 stockItemTable.getColumnModel().getColumn(7).setMinWidth(0);
		 stockItemTable.getColumnModel().getColumn(7).setMaxWidth(0);
		 stockItemTable.getColumnModel().getColumn(7).setWidth(0);
		contentPane.setLayout(null);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(null, "Payment Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_6.setBounds(855, 143, 290, 426);
		panel_3.add(panel_6);
		panel_6.setLayout(null);
		
		JLabel label = new JLabel("No. Of Items");
		label.setBounds(10, 22, 115, 35);
		panel_6.add(label);
		
		tf_NoOfItems = new JTextField();
		tf_NoOfItems.setText("0");
		tf_NoOfItems.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_NoOfItems.setFont(new Font("Dialog", Font.BOLD, 16));
		tf_NoOfItems.setEditable(false);
		tf_NoOfItems.setColumns(10);
		tf_NoOfItems.setBounds(125, 22, 155, 38);
		panel_6.add(tf_NoOfItems);
		
		JLabel label_1 = new JLabel("Total Quantity");
		label_1.setBounds(10, 61, 115, 35);
		panel_6.add(label_1);
		
		tf_TotalQty = new JTextField();
		tf_TotalQty.setText("0");
		tf_TotalQty.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_TotalQty.setFont(new Font("Dialog", Font.BOLD, 16));
		tf_TotalQty.setEditable(false);
		tf_TotalQty.setColumns(10);
		tf_TotalQty.setBounds(125, 61, 155, 38);
		panel_6.add(tf_TotalQty);
		
		JLabel lblTotalAmtWo = new JLabel("Total Amt W/O Tax");
		lblTotalAmtWo.setBounds(10, 100, 115, 35);
		panel_6.add(lblTotalAmtWo);
		
		tf_TotalAmtWOTax = new JTextField();
		tf_TotalAmtWOTax.setText("0.00");
		tf_TotalAmtWOTax.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_TotalAmtWOTax.setFont(new Font("Dialog", Font.BOLD, 16));
		tf_TotalAmtWOTax.setEditable(false);
		tf_TotalAmtWOTax.setColumns(10);
		tf_TotalAmtWOTax.setBounds(125, 100, 155, 38);
		panel_6.add(tf_TotalAmtWOTax);
		
		JLabel lblTotalAmtW = new JLabel("Total Tax");
		lblTotalAmtW.setBounds(10, 139, 115, 35);
		panel_6.add(lblTotalAmtW);
		
		tf_TotalTax = new JTextField();
		tf_TotalTax.setText("0.00");
		tf_TotalTax.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_TotalTax.setFont(new Font("Dialog", Font.BOLD, 16));
		tf_TotalTax.setEditable(false);
		tf_TotalTax.setColumns(10);
		tf_TotalTax.setBounds(125, 139, 155, 38);
		panel_6.add(tf_TotalTax);
		
		JLabel lblTotalMrpAmt = new JLabel("Total MRP Amt");
		lblTotalMrpAmt.setBounds(10, 178, 115, 35);
		panel_6.add(lblTotalMrpAmt);
		
		tf_TotalMRPAmt = new JTextField();
		tf_TotalMRPAmt.setText("0.00");
		tf_TotalMRPAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_TotalMRPAmt.setFont(new Font("Dialog", Font.BOLD, 16));
		tf_TotalMRPAmt.setEditable(false);
		tf_TotalMRPAmt.setColumns(10);
		tf_TotalMRPAmt.setBounds(125, 178, 155, 38);
		panel_6.add(tf_TotalMRPAmt);
		
		JLabel lblExtraCharges = new JLabel("Extra Charges Amt");
		lblExtraCharges.setBounds(10, 217, 115, 35);
		panel_6.add(lblExtraCharges);
		
		tf_ExtraCharges = new JTextField();
		tf_ExtraCharges.setToolTipText("Transportion Charges etc");
		tf_ExtraCharges.setText("0.00");
		tf_ExtraCharges.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_ExtraCharges.setFont(new Font("Dialog", Font.BOLD, 16));
		tf_ExtraCharges.setColumns(10);
		tf_ExtraCharges.setBounds(125, 217, 155, 38);
		tf_ExtraCharges.setDisabledTextColor(Color.BLACK);
		panel_6.add(tf_ExtraCharges);
		
		JLabel lblPayM = new JLabel("Payment Mode");
		lblPayM.setBounds(10, 256, 115, 35);
		panel_6.add(lblPayM);
		
		cb_PayMode = new JComboBox();
		cb_PayMode.setBounds(125, 256, 155, 38);
		PDFUtils.populateDropdown(cb_PayMode,"STOCK_PAYMENT_MODE");
		cb_PayMode.setRenderer(new DefaultListCellRenderer() {
	        @Override
	        public void paint(Graphics g) {
	            setForeground(Color.BLACK);
	            super.paint(g);
	        }
	    });
		panel_6.add(cb_PayMode);
		
		JLabel lblInvoiceAmountPaid = new JLabel("Invoice Amount");
		lblInvoiceAmountPaid.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblInvoiceAmountPaid.setBounds(10, 318, 191, 27);
		panel_6.add(lblInvoiceAmountPaid);
		
		tf_InvoiceAmtPaid = new JTextField();
		tf_InvoiceAmtPaid.setText("0.00");
		tf_InvoiceAmtPaid.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_InvoiceAmtPaid.setForeground(Color.WHITE);
		tf_InvoiceAmtPaid.setFont(new Font("Dialog", Font.BOLD, 30));
		tf_InvoiceAmtPaid.setEditable(false);
		tf_InvoiceAmtPaid.setColumns(10);
		tf_InvoiceAmtPaid.setBackground(Color.GRAY);
		tf_InvoiceAmtPaid.setBounds(66, 356, 214, 38);
		panel_6.add(tf_InvoiceAmtPaid);
		
		JLabel label_2 = new JLabel("");
		label_2.setIcon(new ImageIcon(StockEntry.class.getResource("/images/Rupee-64.png")));
		label_2.setBounds(10, 352, 48, 47);
		panel_6.add(label_2);
		
		tf_ItemName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					if(!tf_ItemName.getText().equals("")){
						tf_TAX.requestFocus();
						//Disabled tax field already added in product screen
						//tf_TAX.requestFocus();
						setProductDetails(tf_ItemName.getText());
					}
					
				}
			}
		});
		tf_invoiceNumber.addKeyListener(new KeyAdapter() {
			   public void keyTyped(KeyEvent e) {
			      char c = e.getKeyChar();
			      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
			         e.consume();  // ignore event
			      }
			   }
			});
		
		tf_TAX.addKeyListener(new KeyAdapter() {
			   public void keyTyped(KeyEvent e) {
			      char c = e.getKeyChar();
			      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) && (c != KeyEvent.VK_PERIOD)) {
			         e.consume();  // ignore event
			      }
			      if(c=='\n'){
			    	  tf_ItemRate.requestFocus();
			      }
			   }
			});
		tf_ItemRate.addKeyListener(new KeyAdapter() {
			   public void keyTyped(KeyEvent e) {
			      char c = e.getKeyChar();
			      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) && (c != KeyEvent.VK_PERIOD)) {
			         e.consume();  // ignore event
			      }
			      if(c=='\n'){
			    	  tf_ItemQty.requestFocus();
			      }
			   }
			});
		tf_ItemQty.addKeyListener(new KeyAdapter() {
			   public void keyTyped(KeyEvent e) {
			      char c = e.getKeyChar();
			      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
			         e.consume();  // ignore event
			      }
			    	  if(c=='\n' && !tf_ItemName.getText().equals("")&& !tf_ItemRate.getText().equals("") && !tf_ItemQty.getText().equals("")){
			    		  /*if(getPurchasePrice(!tf_TAX.getText().equals("")?Double.valueOf(tf_TAX.getText()):0,Double.valueOf(tf_ItemRate.getText())) != productMap.get(tf_ItemName.getText()).getPurcasePrice()){
			    			  JOptionPane.showMessageDialog(getContentPane(), "Existing purchase price not matching with new price!");
			    			  resetItemFields();
					    	  tf_ItemName.requestFocus();
			    		  }else{*/
			    		  calculateAndSetAmount(!tf_TAX.getText().equals("")?Double.valueOf(tf_TAX.getText()):0,Double.valueOf(tf_ItemRate.getText()),Double.valueOf(tf_ItemQty.getText()));
			    		  addRecordToTable(productMap.get(tf_ItemName.getText()));
				    	  resetItemFields();
				    	  tf_ItemName.requestFocus();
			    		 // }
			    	  }
			      }
			});
		stockItemTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				stockItemTableMouseAction();
		}
		});
		
		tf_ExtraCharges.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				setInvoiceAmt();
			}
			});
		//Save Stock Action
		 btn_SaveStock.addActionListener(new ActionListener() {
			 	public void actionPerformed(ActionEvent e) {
			 		saveStockAction();
			 	}
			 });
		 //Reset Button Action
		 btn_ResetStock.addActionListener(new ActionListener() {
			 	public void actionPerformed(ActionEvent e) {
			 		resetStockDetails();
			 		enableAfterReset();
			 		btn_SaveStock.setEnabled(true);
			 	}
			 });
		 
		 tf_invoiceNumber.addKeyListener(new KeyAdapter() {
			   public void keyTyped(KeyEvent e) {
			      char c = e.getKeyChar();
			      if(c=='\n'){
			    	  tf_Comments.requestFocus();
			      }
			   }
			});
		 tf_Comments.addKeyListener(new KeyAdapter() {
			   public void keyTyped(KeyEvent e) {
			      char c = e.getKeyChar();
			      if(c=='\n'){
			    	  tf_ItemName.requestFocus();
			      }
			   }
			});
		 
		 ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);

	}
	
	protected void disableAfterSave() {
		dc_invoiceDate.setEnabled(false);
		tf_invoiceNumber.setEnabled(false);
		tf_Comments.setEnabled(false);
		cb_Suppliers.setEnabled(false);
		tf_ItemName.setEnabled(false);
		tf_ItemQty.setEnabled(false);
		tf_ExtraCharges.setEnabled(false);
		cb_PayMode.setEnabled(false);
		mouseListnerFlag=false;
		
	}
	
	protected void enableAfterReset() {
		dc_invoiceDate.setEnabled(true);
		tf_invoiceNumber.setEnabled(true);
		tf_Comments.setEnabled(true);
		cb_Suppliers.setEnabled(true);
		tf_ItemName.setEnabled(true);
		tf_ItemQty.setEnabled(true);
		tf_ExtraCharges.setEnabled(true);
		cb_PayMode.setEnabled(true);
		mouseListnerFlag=true;
		
	}

	private void createItemList(){
		StockItemDetails item = null;
		stockItemList = new ArrayList<StockItemDetails>();
		for(int i=0;i<stockItemTable.getRowCount();i++){
			item = new StockItemDetails();
			item.setItemNo(Integer.valueOf(stockItemTable.getModel().getValueAt(i, 7).toString()));
			item.setItemName(stockItemTable.getModel().getValueAt(i, 0).toString());
			item.setMRP(Double.valueOf(stockItemTable.getModel().getValueAt(i, 1).toString()));
			item.setRate(Double.valueOf(stockItemTable.getModel().getValueAt(i, 3).toString()));
			item.setQuantity(Integer.valueOf(stockItemTable.getModel().getValueAt(i, 4).toString()));
			item.setPurchasePrice(Double.valueOf(stockItemTable.getModel().getValueAt(i, 6).toString()));
			item.setAmount(Double.valueOf(stockItemTable.getModel().getValueAt(i, 5).toString()));
			item.setTax(Double.valueOf(stockItemTable.getModel().getValueAt(i, 2).toString()));
			item.setStockNumber(stockNumber);
			stockItemList.add(item);
		}
		
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
	
	public void populateSupplier(JComboBox<String> combobox){
		supplierIdMap = new HashMap<String, Integer>();
		combobox.addItem("----- SELECT SUPPLIER-----");
		for(Supplier supplier :SupplierServices.getAllSuppliers()){
			combobox.addItem(supplier.getSupplierName());
			supplierIdMap.put(supplier.getSupplierName(), supplier.getSupplierID());
		}
	}
	
	private void setProductDetails(String productName) {
		Product product = productMap.get(productName);
		tf_TAX.setText("");
		if(product!=null){
			tf_ItemStock.setText(String.valueOf(product.getQuanity()));
			tf_PurPrice.setText(PDFUtils.getDecimalFormat(product.getPurcasePrice()));
			tf_TAX.setText(PDFUtils.getDecimalFormat(product.getProductTax()));
			tf_ItemRate.setText(PDFUtils.getDecimalFormat(product.getPurcaseRate()));
		}
		
	}
	private void resetItemFields() {
		tf_ItemName.setText("");
		tf_ItemStock.setText("");
		tf_TAX.setText("");
		tf_PurPrice.setText("");
		tf_ItemRate.setText("");
		tf_ItemQty.setText("");	
		var_amount = 0;
		
	}
	
	private void addRecordToTable(Product product) {
		if(product!=null){
			if(!productsInTable.contains(product.getProductName())){
					productsInTable.add(product.getProductName());
					//Add row to table
					stockItemModel.addRow(new Object[]{product.getProductName(), PDFUtils.getDecimalFormat(product.getProductMRP()), !tf_TAX.getText().equals("")?PDFUtils.getDecimalFormat(Double.valueOf(tf_TAX.getText())):0,PDFUtils.getDecimalFormat(Double.valueOf(tf_ItemRate.getText())),tf_ItemQty.getText(),PDFUtils.getDecimalFormat(var_amount),getPurchasePrice(!tf_TAX.getText().equals("")?Double.valueOf(tf_TAX.getText()):0,Double.valueOf(tf_ItemRate.getText())),product.getProductCode()});
					setPaymentFields(Integer.valueOf(tf_ItemQty.getText()),var_amount,Double.valueOf(tf_ItemRate.getText()),product.getProductMRP(),stockItemModel.getRowCount());
			
			}else{
				JOptionPane.showMessageDialog(getContentPane(), "Item already present!");
			}
		}
	}
	private void setPaymentFields(int newQuantity, Double amount,Double itemRate,Double itemMRP, int rowCount) {

		noOfItems=rowCount;
		totalQty+=newQuantity;
		invoiceAmount+=amount;
		totalAmtWOTax+=itemRate*newQuantity;
		totalMRPAmount+=itemMRP*newQuantity;
		setAmountQty();
	}
	private void setPaymentFieldsDelete(int newQuantity, Double amount,Double itemRate,Double itemMRP,Double tax, int rowCount) {

		noOfItems=rowCount;
		totalQty-=newQuantity;
		double tempInvoiceAmt = Double.valueOf(PDFUtils.getDecimalFormat(invoiceAmount));
		tempInvoiceAmt+=amount;
		invoiceAmount = tempInvoiceAmt;
		double tmpTotalAmtWOTax = Double.valueOf(PDFUtils.getDecimalFormat(totalAmtWOTax));
		tmpTotalAmtWOTax+=itemRate*newQuantity;
		totalAmtWOTax = tmpTotalAmtWOTax;
		totalMRPAmount+=itemMRP*newQuantity;
		//
		double tempAmount = itemRate*newQuantity;
		double tempTax = (tempAmount/100)*tax;
		System.out.println("Temp Tax 2: " +tempTax);
		double amountLocal= tempAmount + tempTax;
		System.out.println("amountLocal " +amountLocal);
		var_amount = amountLocal;
		double tmpTotalTaxAmount = Double.valueOf(PDFUtils.getDecimalFormat(totalTaxAmount));
		System.out.println("tmpTotalTaxAmount : "+tmpTotalTaxAmount);
		double tempTax2 = Double.valueOf(PDFUtils.getDecimalFormat(tempTax));
		System.out.println("tempTax2 :"+tempTax2);
		tmpTotalTaxAmount+=tempTax2;
		System.out.println("totalTaxAmount 1: "+totalTaxAmount);
		totalTaxAmount=tmpTotalTaxAmount;
		System.out.println("totalTaxAmount - tmpTotalTaxAmount : "+totalTaxAmount);
		//
		setAmountQty();
	}
	private void setAmountQty() {
		tf_NoOfItems.setText(String.valueOf(noOfItems));
		tf_TotalQty.setText(String.valueOf(totalQty));
		tf_TotalAmtWOTax.setText(PDFUtils.getDecimalFormat(totalAmtWOTax));
		tf_InvoiceAmtPaid.setText(PDFUtils.getDecimalFormat(PDFUtils.getDecimalRoundUp(invoiceAmount)));
		tf_TotalMRPAmt.setText(PDFUtils.getDecimalFormat(totalMRPAmount));
		tf_TotalTax.setText(PDFUtils.getDecimalFormat(totalTaxAmount));
	}
	
	private void setInvoiceAmt(){
		double tempInvoiceAmt = invoiceAmount;
		if(!tf_ExtraCharges.getText().equals("")){
			double extraCharges= Double.valueOf(tf_ExtraCharges.getText());
			tempInvoiceAmt+=extraCharges;
			tf_InvoiceAmtPaid.setText(PDFUtils.getDecimalFormat((PDFUtils.getDecimalRoundUp(tempInvoiceAmt))));
		}else{
			tempInvoiceAmt = invoiceAmount;
			tf_InvoiceAmtPaid.setText(PDFUtils.getDecimalFormat((PDFUtils.getDecimalRoundUp(tempInvoiceAmt))));
		}
	}
	
	private void calculateAndSetAmount(Double tax, Double rate,
			Double quantity) {
		
		double tempAmount = rate*quantity;
		double tempTax = (tempAmount/100)*tax;
		double amount = tempAmount + tempTax;
		var_amount = amount;
		totalTaxAmount+=tempTax;
	}
	
	private double getPurchasePrice(Double tax,Double rate){
		
		double newPurchasePrice = 0;
		newPurchasePrice = rate+(rate/100)*tax;
		System.out.println("New Purchase Price :"+ newPurchasePrice);
		System.out.println("Round up Value : " +Double.valueOf(PDFUtils.getDecimalFormat(newPurchasePrice)));
		return Double.valueOf(PDFUtils.getDecimalFormat(newPurchasePrice));
	}
	
	private void resetStockDetails(){
		
		stockItemModel.setRowCount(0);
		stockNumber = PDFUtils.getBillNumber();
		dc_invoiceDate.setDate(null);
		cb_PayMode.setSelectedIndex(0);
		cb_Suppliers.setSelectedIndex(0);
		tf_invoiceNumber.setText("");
		tf_Comments.setText("");
		tf_NoOfItems.setText("0");
		tf_TotalQty.setText("0");
		tf_TotalAmtWOTax.setText("0.00");
		tf_TotalTax.setText("0.00");
		tf_TotalMRPAmt.setText("0.00");
		tf_ExtraCharges.setText("0.00");
		tf_InvoiceAmtPaid.setText("0.00");
		
		tf_ItemName.setText("");
		tf_ItemStock.setText("");
		tf_PurPrice.setText("");
		tf_TAX.setText("");
		tf_ItemRate.setText("");
		tf_ItemQty.setText("");
		
		
		  noOfItems = 0;
		  totalQty = 0;
		  totalAmtWOTax = 0;
		  totalMRPAmount=0;
		  totalTaxAmount=0;
		  invoiceAmount=0;
		  var_amount=0;
		  
		  productsInTable.clear();
	}

	private void saveStockAction() {
		boolean stockSaveStatus=false;
		createItemList();
		supplierInvoiceDetails = new SupplierInvoiceDetails();
		if(dc_invoiceDate.getDate()==null || !PDFUtils.isMandatoryEntered(tf_invoiceNumber)|| !PDFUtils.isMandatorySelected(cb_Suppliers)){
			JOptionPane.showMessageDialog(getContentPane(), "Please Enter Invoice Details");
		}else if(stockItemModel.getRowCount()>0){
			supplierInvoiceDetails.setStockNumber(stockNumber);
			supplierInvoiceDetails.setInvoiceDate(new java.sql.Date(dc_invoiceDate.getDate().getTime()));
			supplierInvoiceDetails.setComments(tf_Comments.getText());
			supplierInvoiceDetails.setItemDetails(stockItemList);
			supplierInvoiceDetails.setInvoiceNumber(Integer.valueOf(tf_invoiceNumber.getText()));
			supplierInvoiceDetails.setSupplierName((String)cb_Suppliers.getSelectedItem());
			supplierInvoiceDetails.setSupplierId(supplierIdMap.get(supplierInvoiceDetails.getSupplierName()));
			if(!tf_ExtraCharges.getText().equals("")){
				supplierInvoiceDetails.setExtraCharges(Double.valueOf(tf_ExtraCharges.getText()));
			}else{
				supplierInvoiceDetails.setExtraCharges(0);
			}
			supplierInvoiceDetails.setPaymentMode((String)cb_PayMode.getSelectedItem());
			supplierInvoiceDetails.setNoOfItems(Integer.valueOf(tf_NoOfItems.getText()));
			supplierInvoiceDetails.setTotalQuanity(Integer.valueOf(tf_TotalQty.getText()));
			supplierInvoiceDetails.setTotalAmtWOTax(Double.valueOf(tf_TotalAmtWOTax.getText()));
			supplierInvoiceDetails.setTotalTax(Double.valueOf(tf_TotalTax.getText()));
			supplierInvoiceDetails.setTotalMRPAmt(Double.valueOf(tf_TotalMRPAmt.getText()));
			supplierInvoiceDetails.setSupplierInvoiceAmt(Double.valueOf(tf_InvoiceAmtPaid.getText()));
			supplierInvoiceDetails.setTimeStamp(new java.sql.Timestamp(System.currentTimeMillis()));
			
			stockSaveStatus = SupplierServices.saveStockDetails(supplierInvoiceDetails);
			//Check existing purchase price and new purchase price to update product details and purchase price history
			StatusDTO statusUpdateInfo = SupplierServices.updateStockInformation(supplierInvoiceDetails.getItemDetails(), supplierInvoiceDetails.getInvoiceNumber(), supplierIdMap.get(supplierInvoiceDetails.getSupplierName()));
			
			if(stockSaveStatus && statusUpdateInfo.getStatusCode()==0){
				ProductHistoryServices.addProductStockLedger(getProductList(stockItemList,supplierInvoiceDetails.getInvoiceNumber()), AppConstants.STOCK_IN, AppConstants.PURCHASE);
				JOptionPane.showMessageDialog(getContentPane(), "Stock Saved Sucessfully !");
				btn_SaveStock.setEnabled(false);
				disableAfterSave();
			}
		}else{
			JOptionPane.showMessageDialog(getContentPane(), "Add Items first to Save this Stock !");
		}
	}

	private void stockItemTableMouseAction() {
		if(mouseListnerFlag){
			int row = stockItemTable.getSelectedRow();
			String productName = stockItemTable.getModel().getValueAt(row, 0).toString();
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure to remove "+productName+" ?","Remove Product",dialogButton);
			if(dialogResult == JOptionPane.YES_OPTION){
				String removeProduct = stockItemTable.getModel().getValueAt(row, 0).toString();
				double removeAmt = Double.valueOf(stockItemTable.getModel().getValueAt(row, 5).toString());
				int removeQty = Integer.valueOf(stockItemTable.getModel().getValueAt(row, 4).toString());
				double itemRate = Double.valueOf(stockItemTable.getModel().getValueAt(row, 3).toString());
				double itemMRP = Double.valueOf(stockItemTable.getModel().getValueAt(row, 1).toString());
				double tax = Double.valueOf(stockItemTable.getModel().getValueAt(row, 2).toString());
				PDFUtils.removeItemFromList(productsInTable,removeProduct);
				stockItemModel.removeRow(row);
				setPaymentFieldsDelete(removeQty, -removeAmt,-itemRate,-itemMRP,tax,stockItemTable.getRowCount());
				if(row==0){
					//All records removed reset fields
					tf_ExtraCharges.setText("0.00");
				}
			}
		}
	}
	
	private List<Product> getProductList(List<StockItemDetails> itemList,int invoiceNo){
		List<Product> productList = new ArrayList<Product>();
		for(StockItemDetails item : itemList){
			Product p = new Product();
			p.setProductCode(item.getItemNo());
			p.setQuanity(item.getQuantity());
			p.setDescription("Purchase Based on Invoice No.: "+invoiceNo);
			productList.add(p);
		}
		return productList;
		
	}
}
