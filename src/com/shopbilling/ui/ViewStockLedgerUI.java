package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
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
import com.shopbilling.dto.StockLedger;
import com.shopbilling.services.ProductHistoryServices;
import com.shopbilling.utils.PDFUtils;
import com.toedter.calendar.JDateChooser;

public class ViewStockLedgerUI extends JDialog {

	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel itemsModel;
	private JLabel lblProductCodeValue;
	private JLabel lblProductNameValue; 
	private JLabel lblCurrentStockValue;
	private JDateChooser toDateChooser;
	private JDateChooser fromDateChooser;
	private Product p;
	/**
	 * Create the frame.
	 */
	public ViewStockLedgerUI(Product pr,JFrame frame) {
		super(frame,"View Bill",true);
		setTitle("View Stock Ledger");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("shop32X32.png"));
		setBounds(350, 100, 857, 534);
		setResizable(false);
		p=pr;
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
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Product Stock Transactions", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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
				"Date", "Transaction Type", "Stock In", "Stock Out", "Narration"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false
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
		lblProductNameValue.setBounds(140, 60, 226, 25);
		lblProductNameValue.setText(p.getProductName());
		p_productDetails.add(lblProductNameValue);
		
		JLabel lblProductName = new JLabel("Product Name : ");
		lblProductName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblProductName.setBounds(10, 60, 122, 25);
		lblProductName.setForeground(lablColor);
		p_productDetails.add(lblProductName);
		
		JLabel lblCurrentStock = new JLabel("Current Stock : ");
		lblCurrentStock.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCurrentStock.setForeground(Color.GRAY);
		lblCurrentStock.setBounds(332, 24, 110, 25);
		p_productDetails.add(lblCurrentStock);
		
		lblCurrentStockValue = new JLabel();
		lblCurrentStockValue.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCurrentStockValue.setHorizontalAlignment(SwingConstants.LEFT);
		lblCurrentStockValue.setBounds(450, 24, 134, 25);
		lblCurrentStockValue.setText(String.valueOf(p.getQuanity()));
		p_productDetails.add(lblCurrentStockValue);
		
		JLabel labelFromDate = new JLabel("From Date :");
		labelFromDate.setHorizontalAlignment(SwingConstants.RIGHT);
		labelFromDate.setBounds(354, 61, 87, 28);
		labelFromDate.setForeground(Color.GRAY);
		p_productDetails.add(labelFromDate);
		
		fromDateChooser = new JDateChooser();
		fromDateChooser.setBounds(451, 61, 133, 28);
		fromDateChooser.setDate(new Date());
		fromDateChooser.setFont(new Font("Tahoma", Font.BOLD, 11));
		p_productDetails.add(fromDateChooser);
		
		JLabel lblToDate = new JLabel("To Date :");
		lblToDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblToDate.setBounds(572, 60, 79, 28);
		lblToDate.setForeground(Color.GRAY);
		p_productDetails.add(lblToDate);
		
		toDateChooser = new JDateChooser();
		toDateChooser.setFont(new Font("Tahoma", Font.BOLD, 11));
		toDateChooser.setBounds(661, 60, 133, 28);
		toDateChooser.setDate(new Date());
		p_productDetails.add(toDateChooser);
		
		JButton btnGetDetails = new JButton("Get Details");
		btnGetDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadTableData();
			}
		});
		btnGetDetails.setBounds(669, 24, 106, 23);
		p_productDetails.add(btnGetDetails);
		 table.getColumnModel().getColumn(0).setPreferredWidth(80);
		 table.getColumnModel().getColumn(1).setPreferredWidth(80);
		 table.getColumnModel().getColumn(2).setPreferredWidth(30);
		 table.getColumnModel().getColumn(3).setPreferredWidth(30);
		 table.getColumnModel().getColumn(4).setPreferredWidth(170);
		 loadTableData();
	}
	
	private void loadTableData() {
		List<StockLedger> stockLedgerList = ProductHistoryServices.getProductStockLedger(p.getProductCode(), fromDateChooser.getDate()==null?null:new java.sql.Date(fromDateChooser.getDate().getTime()),toDateChooser.getDate()==null?null:new java.sql.Date(toDateChooser.getDate().getTime()));
		itemsModel = (DefaultTableModel)table.getModel();
		 itemsModel.setRowCount(0);
		 for(StockLedger item : stockLedgerList){
			 itemsModel.addRow(new Object[]{PDFUtils.getFormattedDateWithTime(item.getTimeStamp()),item.getTransactionType(),item.getStockIn(),item.getStockOut(),item.getNarration()});
		 }
	}
}
