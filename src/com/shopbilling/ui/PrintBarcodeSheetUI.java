package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.Product;
import com.shopbilling.services.AutoSuggestTable;
import com.shopbilling.services.BarCodeServices;
import com.shopbilling.services.ProductServices;
import com.shopbilling.utils.PDFUtils;

public class PrintBarcodeSheetUI extends JInternalFrame {
	
	private HashMap<String,Product> productMap; 
	private JTextField tf_ProductName;
	private JTextField tf_StockQty;
	private JTextField tf_CategoryName;
	private JTextField tf_Barcode;
	private AutoSuggestTable<String> autoSuggestTable;
	private JComboBox comboBox;
	private String fileName;
	private String fileLocation;
	private final static Logger logger = Logger.getLogger(PrintBarcodeSheetUI.class);
	/**
	 * Create the frame.
	 */
	public PrintBarcodeSheetUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Print Barcode Label");
		getContentPane().setLayout(null);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Products", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(347, 26, 519, 556);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		autoSuggestTable = new AutoSuggestTable<String>(getProductName());
		autoSuggestTable.setForeground(Color.BLUE);
		autoSuggestTable.setFont(new Font("Dialog", Font.BOLD, 13));
		autoSuggestTable.setColumns(10);
		autoSuggestTable.setBounds(175, 34, 288, 27);
		autoSuggestTable.setBorder(border);
		panel_1.add(autoSuggestTable);
		
		JLabel lblSelectProduct = new JLabel("Select Product :");
		lblSelectProduct.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSelectProduct.setBounds(45, 34, 120, 27);
		panel_1.add(lblSelectProduct);
		
		JLabel label = new JLabel("Barcode  ");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setForeground(Color.GRAY);
		label.setBounds(77, 173, 120, 25);
		label.setBorder(border);
		panel_1.add(label);
		
		JLabel label_1 = new JLabel("Category Name  ");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setForeground(Color.GRAY);
		label_1.setBounds(77, 149, 120, 25);
		label_1.setBorder(border);
		panel_1.add(label_1);
		
		JLabel label_2 = new JLabel("Stock Quantity  ");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setForeground(Color.GRAY);
		label_2.setBounds(77, 125, 120, 25);
		label_2.setBorder(border);
		panel_1.add(label_2);
		
		JLabel label_3 = new JLabel("Product Name  ");
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setForeground(Color.GRAY);
		label_3.setBounds(77, 101, 120, 25);
		label_3.setBorder(border);
		panel_1.add(label_3);
		
		tf_ProductName = new JTextField();
		tf_ProductName.setFont(new Font("Dialog", Font.BOLD, 13));
		tf_ProductName.setEnabled(false);
		tf_ProductName.setEditable(false);
		tf_ProductName.setDisabledTextColor(Color.DARK_GRAY);
		tf_ProductName.setColumns(10);
		tf_ProductName.setBounds(196, 101, 279, 25);
		tf_ProductName.setBorder(border);
		
		panel_1.add(tf_ProductName);
		
		tf_StockQty = new JTextField();
		tf_StockQty.setFont(new Font("Dialog", Font.BOLD, 13));
		tf_StockQty.setEnabled(false);
		tf_StockQty.setEditable(false);
		tf_StockQty.setDisabledTextColor(Color.DARK_GRAY);
		tf_StockQty.setColumns(10);
		tf_StockQty.setBounds(196, 125, 279, 25);
		tf_StockQty.setBorder(border);
		panel_1.add(tf_StockQty);
		
		tf_CategoryName = new JTextField();
		tf_CategoryName.setFont(new Font("Dialog", Font.BOLD, 13));
		tf_CategoryName.setEnabled(false);
		tf_CategoryName.setEditable(false);
		tf_CategoryName.setDisabledTextColor(Color.DARK_GRAY);
		tf_CategoryName.setColumns(10);
		tf_CategoryName.setBorder(border);
		tf_CategoryName.setBounds(196, 149, 279, 25);
		panel_1.add(tf_CategoryName);
		
		tf_Barcode = new JTextField();
		tf_Barcode.setFont(new Font("Dialog", Font.BOLD, 13));
		tf_Barcode.setEnabled(false);
		tf_Barcode.setEditable(false);
		tf_Barcode.setDisabledTextColor(Color.DARK_GRAY);
		tf_Barcode.setColumns(10);
		tf_Barcode.setBorder(border);
		tf_Barcode.setBounds(196, 173, 279, 25);
		panel_1.add(tf_Barcode);
		
		JLabel lblNewLabel = new JLabel("Note : One A4 Size Page holds 52 Barcode labels");
		lblNewLabel.setBounds(155, 228, 294, 14);
		panel_1.add(lblNewLabel);
		
		JLabel lblSelectNoOf = new JLabel("Select No of Pages :");
		lblSelectNoOf.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSelectNoOf.setBounds(77, 290, 120, 27);
		panel_1.add(lblSelectNoOf);
		
		comboBox = new JComboBox();
		comboBox.setBounds(207, 290, 217, 27);
		comboBox.addItem("Page 1 : 52 Barcode Labels");
		comboBox.addItem("Page 2 : 104 Barcode Labels");
		comboBox.addItem("Page 3 : 156 Barcode Labels");
		comboBox.addItem("Page 4 : 208 Barcode Labels");
		comboBox.addItem("Page 5 : 260 Barcode Labels");
		
		panel_1.add(comboBox);
		fileLocation = PDFUtils.getAppDataValues(AppConstants.MYSTORE_HOME).get(0)+AppConstants.BARCODE_SHEET_FOLER+"\\";
		
		JButton btnCreateBarcodeSheet = new JButton("Create Barcode Sheet");
		btnCreateBarcodeSheet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(PDFUtils.isMandatoryEntered(tf_Barcode) && PDFUtils.isMandatoryEntered(tf_ProductName)){
					BarCodeServices barcode = new BarCodeServices();
					try {
						fileName = fileLocation+tf_ProductName.getText()+".pdf";
						int noOfPages = comboBox.getSelectedIndex()+1;
						barcode.createPdf(fileName, tf_Barcode.getText().trim(), tf_ProductName.getText(),noOfPages);
						JOptionPane.showMessageDialog(getContentPane(), "Barcode Sheet Ready at "+fileName);
						PDFUtils.openWindowsDocument(fileName);
					} catch (Exception e1) {
						e1.printStackTrace();
						logger.error("Print Barcode Sheet Exception : ",e1);
					} 
				}else{
					JOptionPane.showMessageDialog(getContentPane(), "Please Select the Product !");
				}
				
			}
		});
		btnCreateBarcodeSheet.setBounds(175, 380, 211, 23);
		panel_1.add(btnCreateBarcodeSheet);
		
		autoSuggestTable.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					if(!autoSuggestTable.getText().equals("")){
						resetFields();
						setProductDetails(autoSuggestTable.getText());
					}
					
				}
			}
		});
		ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
	
	protected void setProductDetails(String productName) {
		Product product = productMap.get(productName);
		tf_ProductName.setText(" "+product.getProductName());
		tf_CategoryName.setText(" "+product.getProductCategory());
		tf_Barcode.setText(" "+String.valueOf(product.getProductBarCode()));
		tf_StockQty.setText(" "+String.valueOf(product.getQuanity()));
	}
	
	private void resetFields(){
		tf_ProductName.setText("");
		tf_CategoryName.setText("");
		tf_Barcode.setText("");
		tf_StockQty.setText("");
	}

	public List<String> getProductName(){
		
		List<String> productNameList = new ArrayList<String>();
		productMap = new HashMap<String, Product>();
		for(Product product :ProductServices.getAllProducts()){
			productNameList.add(product.getProductName()+" : "+PDFUtils.getDecimalFormat(product.getProductMRP()));
			productMap.put(product.getProductName()+" : "+PDFUtils.getDecimalFormat(product.getProductMRP()),product);
		}
		return productNameList;
	}
}
