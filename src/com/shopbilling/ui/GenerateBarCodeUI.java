package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
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

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.dto.Product;
import com.shopbilling.dto.StatusDTO;
import com.shopbilling.services.ProductServices;
import com.shopbilling.utils.PDFUtils;

public class GenerateBarCodeUI extends JInternalFrame {
	private JTable table;
	private DefaultTableModel reportModel;
	private JTextField tf_ProductName;
	private JTextField tf_StockQty;
	private JTextField tf_CategoryName;
	private JTextField tf_Barcode;
	private int productCode;
	/**
	 * Create the frame.
	 */
	public GenerateBarCodeUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Generate Barcode For Product");
		getContentPane().setLayout(null);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Products", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 23, 1150, 556);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(524, 26, 579, 491);
		panel_1.add(scrollPane);
		
		table = new JTable();
		reportModel = new DefaultTableModel(){
			 boolean[] columnEditables = new boolean[] {
					 false, false, false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
		 reportModel.setColumnIdentifiers(new String[] {
				 "Product Name","Stock Quantity","Category","Product Code"}
	       );
		table.setModel(reportModel);
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				tf_ProductName.setText(" "+table.getModel().getValueAt(row, 0).toString());
				tf_StockQty.setText(" "+table.getModel().getValueAt(row, 1).toString());
				tf_CategoryName.setText(" "+table.getModel().getValueAt(row, 2).toString());
				tf_Barcode.setText("");
				productCode = Integer.valueOf(table.getModel().getValueAt(row, 3).toString());
				
			}
		});
		//Table Row Height 
		PDFUtils.setTableRowHeight(table);
		
		 //table.getColumnModel().getColumn(2).setMinWidth(0);
		 //table.getColumnModel().getColumn(2).setMaxWidth(0);
		 //table.getColumnModel().getColumn(2).setWidth(0);
		 table.getColumnModel().getColumn(3).setMinWidth(0);
		 table.getColumnModel().getColumn(3).setMaxWidth(0);
		 table.getColumnModel().getColumn(3).setWidth(0);
		 
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(45, 162, 429, 231);
		panel_1.add(panel);
		panel.setLayout(null);
		
		JLabel lblProductName = new JLabel("Product Name  ");
		lblProductName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblProductName.setForeground(Color.GRAY);
		lblProductName.setBounds(21, 31, 120, 25);
		lblProductName.setBorder(border);
		panel.add(lblProductName);
		
		tf_ProductName = new JTextField();
		tf_ProductName.setFont(new Font("Dialog", Font.BOLD, 13));
		tf_ProductName.setEnabled(false);
		tf_ProductName.setEditable(false);
		tf_ProductName.setDisabledTextColor(Color.DARK_GRAY);
		tf_ProductName.setColumns(10);
		tf_ProductName.setBorder(border);
		tf_ProductName.setBounds(140, 31, 279, 25);
		panel.add(tf_ProductName);
		
		JLabel lblStockQuantity = new JLabel("Stock Quantity  ");
		lblStockQuantity.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStockQuantity.setForeground(Color.GRAY);
		lblStockQuantity.setBounds(21, 55, 120, 25);
		lblStockQuantity.setBorder(border);
		panel.add(lblStockQuantity);
		
		tf_StockQty = new JTextField();
		tf_StockQty.setFont(new Font("Dialog", Font.BOLD, 13));
		tf_StockQty.setEnabled(false);
		tf_StockQty.setEditable(false);
		tf_StockQty.setDisabledTextColor(Color.DARK_GRAY);
		tf_StockQty.setColumns(10);
		tf_StockQty.setBounds(140, 55, 279, 25);
		tf_StockQty.setBorder(border);
		panel.add(tf_StockQty);
		
		JLabel lblCategoryName = new JLabel("Category Name  ");
		lblCategoryName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCategoryName.setForeground(Color.GRAY);
		lblCategoryName.setBounds(21, 79, 120, 25);
		lblCategoryName.setBorder(border);
		panel.add(lblCategoryName);
		
		tf_CategoryName = new JTextField();
		tf_CategoryName.setFont(new Font("Dialog", Font.BOLD, 13));
		tf_CategoryName.setEnabled(false);
		tf_CategoryName.setEditable(false);
		tf_CategoryName.setDisabledTextColor(Color.DARK_GRAY);
		tf_CategoryName.setColumns(10);
		tf_CategoryName.setBounds(140, 79, 279, 25);
		tf_CategoryName.setBorder(border);
		panel.add(tf_CategoryName);
		
		tf_Barcode = new JTextField();
		tf_Barcode.setFont(new Font("Dialog", Font.BOLD, 13));
		tf_Barcode.setEnabled(false);
		tf_Barcode.setEditable(false);
		tf_Barcode.setDisabledTextColor(Color.DARK_GRAY);
		tf_Barcode.setColumns(10);
		tf_Barcode.setBounds(140, 103, 279, 25);
		tf_Barcode.setBorder(border);
		panel.add(tf_Barcode);
		
		JLabel lblBarcode = new JLabel("Barcode  ");
		lblBarcode.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBarcode.setForeground(Color.GRAY);
		lblBarcode.setBounds(21, 103, 120, 25);
		lblBarcode.setBorder(border);
		panel.add(lblBarcode);
		
		JButton btnGenerate = new JButton("Generate Barcode");
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!tf_ProductName.getText().equals("")){
					generateBarcode();
				}else{
					JOptionPane.showMessageDialog(getContentPane(), "Please Select product !");
				}
				
			}
		});
		btnGenerate.setBounds(21, 165, 168, 23);
		panel.add(btnGenerate);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveBarcode();
			}
		});
		btnSave.setBounds(315, 165, 89, 23);
		panel.add(btnSave);
		
		 fillReportTable();
		 
		 ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
	
	private void fillReportTable() {
		List<Product> productList= ProductServices.getProductsWithNoBarcode();
			reportModel.setRowCount(0);
			if(productList.isEmpty()){
				JOptionPane.showMessageDialog(getContentPane(), "No Product found!");
			}else{
				for(Product p : productList){
					reportModel.addRow(new Object[]{p.getProductName(), p.getQuanity(),p.getProductCategory(),p.getProductCode()});
				}
			}
	}
	
	private void generateBarcode(){
		tf_Barcode.setText(" "+String.valueOf(PDFUtils.getBarcode()));
	}
	
	private void resetFields(){
		tf_Barcode.setText("");
		tf_CategoryName.setText("");
		tf_ProductName.setText("");
		tf_StockQty.setText("");
		productCode = 0;
	}

	private void saveBarcode() {
		if(!tf_ProductName.getText().equals("") && !tf_Barcode.getText().equals("")){
			Product product = new Product();
			product.setProductCode(productCode);
			product.setProductBarCode(Long.valueOf(tf_Barcode.getText().trim()));
			HashMap<Long,Product> productMap = ProductServices.getProductBarCodeMap();
			
			if(productMap.containsKey(product.getProductBarCode())){
				JOptionPane.showMessageDialog(null, "Please regenerate barcode ", "Error", JOptionPane.WARNING_MESSAGE);
			}else{
				StatusDTO status = ProductServices.saveBarcode(product);
				if(status.getStatusCode()==0){
					JOptionPane.showMessageDialog(getContentPane(), "Product Barcode Saved Successfully!");
					fillReportTable();
					resetFields();
				}else{
					JOptionPane.showMessageDialog(null, "Exception occured ", "Error", JOptionPane.WARNING_MESSAGE);
				}
			}
		}else{
			JOptionPane.showMessageDialog(getContentPane(), "Please Select product / Generate Barcode!");
		}
	}
}
