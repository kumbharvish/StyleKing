package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.BillDetails;
import com.shopbilling.dto.Product;
import com.shopbilling.services.AutoSuggestTable;
import com.shopbilling.services.ProductHistoryServices;
import com.shopbilling.services.ProductServices;
import com.shopbilling.utils.PDFUtils;

public class QuickStockCorrection extends JDialog {

	private JPanel contentPane;
	private JTextField tf_ItemNumber;
	private JTextField tf_itemName;
	private JTextField tf_MRP;
	private JTextField tf_Rate;
	private JTextField tf_CStock;
	private JTextField tf_Barcode;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
	private DefaultTableModel itemsModel;
	private static BillDetails billDetails = new BillDetails();
	private JTextField barcodeEntry;
	private JTextField itemNameEntry;
	private HashMap<String,Product> productMap;
	private HashMap<Long,Product> productMapWithBarcode;
	private JTextField tf_newQty;


	/**
	 * Create the frame.
	 */
	public QuickStockCorrection(JFrame frame) {
		super(frame,"Quick Stock Correction",true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(350, 100, 815, 477);
		setIconImage(Toolkit.getDefaultToolkit().getImage("shop32X32.png"));
		setResizable(false);
		Color lablColor = Color.gray;
		Color valueColor = Color.DARK_GRAY;
		Font valueFont = new Font("Dialog",Font.BOLD,13);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(31, 11, 422, 416);
		contentPane.add(panel);
		panel.setLayout(null);
		//billDetails = bill;
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Product Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(16, 204, 390, 201);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblBillNo = new JLabel("Item Number ");
		lblBillNo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBillNo.setBounds(10, 34, 120, 25);
		lblBillNo.setBorder(border);
		lblBillNo.setForeground(lablColor);
		panel_1.add(lblBillNo);
		
		tf_ItemNumber = new JTextField();
		tf_ItemNumber.setEnabled(false);
		tf_ItemNumber.setEditable(false);
		tf_ItemNumber.setBounds(129, 34, 251, 25);
		tf_ItemNumber.setBorder(border);
		//tf_BillNumber.setText(" "+String.valueOf(bill.getBillNumber()));
		tf_ItemNumber.setFont(valueFont);
		tf_ItemNumber.setDisabledTextColor(valueColor);
		panel_1.add(tf_ItemNumber);
		tf_ItemNumber.setColumns(10);
		
		JLabel lblBillDate = new JLabel("Item Name ");
		lblBillDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBillDate.setBorder(border);
		lblBillDate.setBounds(10, 58, 120, 25);
		lblBillDate.setForeground(lablColor);

		panel_1.add(lblBillDate);
		
		tf_itemName = new JTextField();
		tf_itemName.setEditable(false);
		tf_itemName.setEnabled(false);
		tf_itemName.setColumns(10);
		tf_itemName.setBounds(129, 58, 251, 25);
		tf_itemName.setBorder(border);
		//tf_BillDate.setText(" "+sdf.format(bill.getTimestamp()));
		tf_itemName.setFont(valueFont);
		tf_itemName.setDisabledTextColor(valueColor);
		panel_1.add(tf_itemName);
		
		JLabel lblCustomerName = new JLabel("MRP ");
		lblCustomerName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCustomerName.setBounds(10, 82, 120, 25);
		lblCustomerName.setBorder(border);
		lblCustomerName.setForeground(lablColor);
		panel_1.add(lblCustomerName);
		
		tf_MRP = new JTextField();
		tf_MRP.setEnabled(false);
		tf_MRP.setEditable(false);
		tf_MRP.setColumns(10);
		tf_MRP.setBounds(129, 82, 251, 25);
		tf_MRP.setBorder(border);
		//tf_CustName.setText(" "+bill.getCustomerName());
		tf_MRP.setFont(valueFont);
		tf_MRP.setDisabledTextColor(valueColor);
		panel_1.add(tf_MRP);
		
		JLabel lblCustomerMobile = new JLabel("Rate ");
		lblCustomerMobile.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCustomerMobile.setBounds(10, 106, 120, 25);
		lblCustomerMobile.setBorder(border);
		lblCustomerMobile.setForeground(lablColor);
		panel_1.add(lblCustomerMobile);
		
		tf_Rate = new JTextField();
		tf_Rate.setEnabled(false);
		tf_Rate.setEditable(false);
		tf_Rate.setColumns(10);
		tf_Rate.setBounds(129, 106, 251, 25);
		tf_Rate.setBorder(border);
		//tf_CustMob.setText(" "+String.valueOf(bill.getCustomerMobileNo()));
		tf_Rate.setFont(valueFont);
		tf_Rate.setDisabledTextColor(valueColor);
		panel_1.add(tf_Rate);
		
		JLabel lblNoOfItems = new JLabel("Current Stock ");
		lblNoOfItems.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNoOfItems.setBounds(10, 130, 120, 25);
		lblNoOfItems.setBorder(border);
		lblNoOfItems.setForeground(lablColor);
		panel_1.add(lblNoOfItems);
		
		JLabel lblTotalQuantity = new JLabel("Barcode ");
		lblTotalQuantity.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalQuantity.setBounds(10, 154, 120, 25);
		lblTotalQuantity.setBorder(border);
		lblTotalQuantity.setForeground(lablColor);
		panel_1.add(lblTotalQuantity);
		
		tf_CStock = new JTextField();
		tf_CStock.setEnabled(false);
		tf_CStock.setEditable(false);
		tf_CStock.setColumns(10);
		tf_CStock.setBounds(129, 130, 251, 25);
		tf_CStock.setBorder(border);
		//tf_NoOfItems.setText(" "+String.valueOf(bill.getNoOfItems()));
		tf_CStock.setFont(valueFont);
		tf_CStock.setDisabledTextColor(valueColor);
		panel_1.add(tf_CStock);
		
		tf_Barcode = new JTextField();
		tf_Barcode.setEnabled(false);
		tf_Barcode.setEditable(false);
		tf_Barcode.setColumns(10);
		tf_Barcode.setBounds(129, 154, 251, 25);
		tf_Barcode.setBorder(border);
		//tf_TotalQty.setText(" "+String.valueOf(bill.getTotalQuanity()));
		tf_Barcode.setFont(valueFont);
		tf_Barcode.setDisabledTextColor(valueColor);
		panel_1.add(tf_Barcode);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new TitledBorder(null, "Barcode Entry", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(46, 23, 330, 64);
		panel.add(panel_2);
		
		barcodeEntry = new JTextField();
		barcodeEntry.setHorizontalAlignment(SwingConstants.CENTER);
		barcodeEntry.setFont(new Font("Tahoma", Font.BOLD, 15));
		barcodeEntry.setColumns(10);
		barcodeEntry.setBounds(37, 20, 256, 29);
		panel_2.add(barcodeEntry);
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Item Name Entry", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(46, 117, 330, 64);
		panel.add(panel_3);
		
		itemNameEntry = new AutoSuggestTable<String>(getProductName());
		itemNameEntry.setHorizontalAlignment(SwingConstants.LEFT);
		itemNameEntry.setFont(new Font("Tahoma", Font.BOLD, 15));
		itemNameEntry.setColumns(10);
		itemNameEntry.setBounds(37, 20, 256, 29);
		itemNameEntry.setBorder(border);
		panel_3.add(itemNameEntry);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Update Stock", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setBounds(497, 82, 289, 192);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		
		JLabel lblNewQuantity = new JLabel("New Quantity : ");
		lblNewQuantity.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewQuantity.setBounds(10, 60, 89, 25);
		panel_4.add(lblNewQuantity);
		
		tf_newQty = new JTextField();
		tf_newQty.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_newQty.setBounds(109, 62, 128, 25);
		tf_newQty.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel_4.add(tf_newQty);
		tf_newQty.setColumns(10);
		
		JButton btnUpdateStock = new JButton("Update Stock");
		btnUpdateStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateStock();
			}
		});
		btnUpdateStock.setBounds(88, 127, 112, 32);
		panel_4.add(btnUpdateStock);
		
		//BarCode Entry
		barcodeEntry.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					if(!barcodeEntry.getText().equals("")){
						setProductDetailsWithBarCode(Long.valueOf(barcodeEntry.getText().trim()));
						barcodeEntry.setText("");
						itemNameEntry.setText("");
						tf_newQty.requestFocus();
					}
					
				}
			}
		});
		//Item Name Entry
		itemNameEntry.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					if(!itemNameEntry.getText().equals("")){
						setProductDetails(itemNameEntry.getText());
						itemNameEntry.setText("");
						tf_newQty.requestFocus();
					}
					
				}
			}
		});
		// Allow only numbers for new qty
		tf_newQty.addKeyListener(new KeyAdapter() {
			   public void keyTyped(KeyEvent e) {
			      char c = e.getKeyChar();
			      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
			         e.consume();  // ignore event
			      }
			   }
		});
	}
	
	protected void updateStock() {
		if(PDFUtils.isMandatoryEntered(tf_ItemNumber)){
			if(PDFUtils.isMandatoryEntered(tf_newQty)){
				Product product = new Product();
				product.setProductCode(Integer.valueOf(tf_ItemNumber.getText().trim()));
				product.setQuanity(Integer.valueOf(tf_newQty.getText().trim()));
				List<Product> productList = new ArrayList<Product>();
				Product p = ProductServices.getProduct(product.getProductCode());
				product.setDescription("Existing Stock: "+p.getQuanity()+" Correction Qty: "+product.getQuanity());
				if(product.getQuanity()<p.getQuanity()){
					product.setQuanity(p.getQuanity()-product.getQuanity());
					productList.add(product);
					ProductHistoryServices.addProductStockLedger(productList, AppConstants.STOCK_OUT, AppConstants.QUICK_STOCK_CORR);
				}else{
					product.setQuanity(product.getQuanity()-p.getQuanity());
					productList.add(product);
					ProductHistoryServices.addProductStockLedger(productList, AppConstants.STOCK_IN, AppConstants.QUICK_STOCK_CORR)	;
				}
				product.setQuanity(Integer.valueOf(tf_newQty.getText().trim()));
				boolean flag = ProductServices.quickStockCorrection(product);
				if(flag){
					resetFields();
					JOptionPane.showMessageDialog(getContentPane(), "Product Stock Update Successfully !");
					this.dispose();
				}
				else{
					JOptionPane.showMessageDialog(getContentPane(), "Error Occured ");
				}
			}else{
				JOptionPane.showMessageDialog(getContentPane(), "Please Enter New Quantity !");
			}
		}else{
			JOptionPane.showMessageDialog(getContentPane(), "Please Select Product !");
		}
	}

	public List<String> getProductName(){
		
		List<String> productNameList = new ArrayList<String>();
		productMap = new HashMap<String, Product>();
		productMapWithBarcode = new HashMap<Long, Product>();
		for(Product product :ProductServices.getAllProducts()){
			productNameList.add(product.getProductName());
			productMap.put(product.getProductName(),product);
			productMapWithBarcode.put(product.getProductBarCode(),product);
		}
		return productNameList;
	}
	
	protected void setProductDetailsWithBarCode(Long productBarCode) {
		Product product = productMapWithBarcode.get(productBarCode);
		if(product==null){
			JOptionPane.showMessageDialog(getContentPane(), "Product not preset for Bar Code : "+productBarCode);
		}
		if(product!=null){
			tf_ItemNumber.setText(" "+String.valueOf(product.getProductCode()));
			tf_itemName.setText(" "+product.getProductName());
			tf_MRP.setText(" "+PDFUtils.getDecimalFormat(product.getProductMRP()));
			tf_Rate.setText(" "+PDFUtils.getDecimalFormat(product.getSellPrice()));
			tf_CStock.setText(" "+String.valueOf(product.getQuanity()));
			tf_Barcode.setText(" "+String.valueOf(product.getProductBarCode()));
		}
		
	}
	
	private void setProductDetails(String productName) {
		Product product = productMap.get(productName);
		if(product==null){
			JOptionPane.showMessageDialog(getContentPane(), "Product not preset !");
		}
		if(product!=null){
			tf_ItemNumber.setText(" "+String.valueOf(product.getProductCode()));
			tf_itemName.setText(" "+product.getProductName());
			tf_MRP.setText(" "+PDFUtils.getDecimalFormat(product.getProductMRP()));
			tf_Rate.setText(" "+PDFUtils.getDecimalFormat(product.getSellPrice()));
			tf_CStock.setText(" "+String.valueOf(product.getQuanity()));
			tf_Barcode.setText(" "+String.valueOf(product.getProductBarCode()));
		}
		
	}
	
	private void resetFields(){
		tf_ItemNumber.setText("");
		tf_itemName.setText("");
		tf_MRP.setText("");
		tf_Rate.setText("");
		tf_CStock.setText("");
		tf_Barcode.setText("");
		tf_newQty.setText("");
	}
}
