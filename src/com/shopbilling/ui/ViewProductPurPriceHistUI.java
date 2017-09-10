package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.shopbilling.dto.Product;
import com.shopbilling.services.ProductHistoryServices;
import com.shopbilling.utils.PDFUtils;

public class ViewProductPurPriceHistUI extends JDialog {

	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel itemsModel;
	private JLabel lblProductCodeValue;
	private JLabel lblProductNameValue; 
	/**
	 * Create the frame.
	 */
	public ViewProductPurPriceHistUI(Product p,JFrame frame) {
		super(frame,"View Bill",true);
		setTitle("Product Purchase Price History");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("shop32X32.png"));
		setBounds(350, 100, 857, 534);
		setResizable(false);
		Color lablColor = Color.gray;
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 831, 490);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Purchase Price History", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(13, 122, 804, 357);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 25, 784, 321);
		panel_2.add(scrollPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Product Recived Date", "Tax", "Rate", "Purchase Price", "Narration", "Supplier Name"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, true, true, true, true, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		//table.getColumnModel().getColumn(0).setResizable(false);
		table.setEnabled(false);
		PDFUtils.setTableRowHeight(table);
		
		scrollPane.setViewportView(table);
		
		JPanel p_productDetails = new JPanel();
		p_productDetails.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Product Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		p_productDetails.setBounds(13, 11, 804, 100);
		panel.add(p_productDetails);
		p_productDetails.setLayout(null);
		
		JLabel lblProductCode = new JLabel("Product Code : ");
		lblProductCode.setHorizontalAlignment(SwingConstants.RIGHT);
		lblProductCode.setBounds(22, 24, 110, 25);
		lblProductCode.setForeground(lablColor);
		
		p_productDetails.add(lblProductCode);
		
		lblProductCodeValue = new JLabel();
		lblProductCodeValue.setHorizontalAlignment(SwingConstants.LEFT);
		lblProductCodeValue.setBounds(140, 24, 134, 25);
		lblProductCodeValue.setText(String.valueOf(p.getProductCode()));
		p_productDetails.add(lblProductCodeValue);
		
		lblProductNameValue = new JLabel();
		lblProductNameValue.setHorizontalAlignment(SwingConstants.LEFT);
		lblProductNameValue.setBounds(140, 60, 382, 25);
		lblProductNameValue.setText(p.getProductName());
		p_productDetails.add(lblProductNameValue);
		
		JLabel lblProductName = new JLabel("Product Name : ");
		lblProductName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblProductName.setBounds(10, 60, 122, 25);
		lblProductName.setForeground(lablColor);
		p_productDetails.add(lblProductName);
		 table.getColumnModel().getColumn(0).setPreferredWidth(120);
		 table.getColumnModel().getColumn(1).setPreferredWidth(40);
		 table.getColumnModel().getColumn(2).setPreferredWidth(50);
		 table.getColumnModel().getColumn(3).setPreferredWidth(80);
		 table.getColumnModel().getColumn(4).setPreferredWidth(150);
		 table.getColumnModel().getColumn(5).setPreferredWidth(150);
		 List<Product> productList = ProductHistoryServices.getProductPurchasePriceHist(p.getProductCode());
		 for(Product item : productList){
			 itemsModel = (DefaultTableModel)table.getModel();
			 itemsModel.addRow(new Object[]{PDFUtils.getFormattedDateWithTime(item.getTimeStamp()),PDFUtils.getDecimalFormat(item.getProductTax()),PDFUtils.getDecimalFormat(item.getPurcaseRate()),PDFUtils.getDecimalFormat(item.getPurcasePrice()),item.getDescription(),item.getSupplierName()});
		 }
		 
	}
	
}
