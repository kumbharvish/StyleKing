package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.dto.Supplier;
import com.shopbilling.dto.SupplierInvoiceDetails;
import com.shopbilling.services.ButtonColumn;
import com.shopbilling.services.SupplierServices;
import com.shopbilling.utils.PDFUtils;
import com.toedter.calendar.JDateChooser;

public class StockPurchaseHistoryUI extends JInternalFrame {
	
	private JPanel contentPane;
	//History Fields
	private JButton btnGetHistory;
	private JComboBox<String> cb_SuppHistory;
	private JTextField tf_HistoryInvoiceNo;
	private JTable stockHistoryTable;
	private DefaultTableModel stockHistoryModel;
	private JDateChooser dc_HistoryToDate;
	private JDateChooser dc_HistoryFromDate;
	private HashMap<Integer, SupplierInvoiceDetails> stocksMap;
	private JFrame parentFrame;
	private HashMap<String,Integer> supplierIdMap; 

	
	/**
	 * Create the frame.
	 */
	public StockPurchaseHistoryUI() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Stock Purchase History");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		parentFrame = (JFrame)this.getTopLevelAncestor();
		setResizable(false);
		//fix the frame location
		//setUndecorated(true);
		JPanel stockHistory = new JPanel();
		stockHistory.setBorder(new LineBorder(new Color(0, 0, 0)));
		stockHistory.setBounds(10, 11, 1150, 579);
		contentPane.add(stockHistory);
		stockHistory.setLayout(null);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(10, 11, 1115, 108);
		panel_4.setBorder(new TitledBorder(null, "Supplier Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		stockHistory.add(panel_4);
		panel_4.setLayout(null);
		
		JLabel lblSupplier = new JLabel("Supplier *:");
		lblSupplier.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSupplier.setBounds(60, 20, 94, 25);
		panel_4.add(lblSupplier);
		
		cb_SuppHistory = new JComboBox();
		cb_SuppHistory.setBounds(160, 20, 200, 25);
		panel_4.add(cb_SuppHistory);
		populateSupplier(cb_SuppHistory);
		JLabel lblInvoiceNumber = new JLabel("Invoice Number :");
		lblInvoiceNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblInvoiceNumber.setBounds(60, 55, 94, 25);
		panel_4.add(lblInvoiceNumber);
		
		tf_HistoryInvoiceNo = new JTextField();
		tf_HistoryInvoiceNo.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_HistoryInvoiceNo.setHorizontalAlignment(SwingConstants.LEFT);
		tf_HistoryInvoiceNo.setBounds(160, 55, 200, 25);
		panel_4.add(tf_HistoryInvoiceNo);
		tf_HistoryInvoiceNo.setColumns(10);
		
		JLabel lblFromDate = new JLabel("From Date **:");
		lblFromDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFromDate.setBounds(510, 20, 104, 25);
		panel_4.add(lblFromDate);
		
		dc_HistoryFromDate = new JDateChooser();
		dc_HistoryFromDate.setFont(new Font("Tahoma", Font.BOLD, 12));
		dc_HistoryFromDate.setBounds(620, 20, 168, 25);
		panel_4.add(dc_HistoryFromDate);
		
		JLabel lblToDate = new JLabel("To Date **:");
		lblToDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblToDate.setBounds(510, 55, 104, 25);
		panel_4.add(lblToDate);
		
		dc_HistoryToDate = new JDateChooser();
		dc_HistoryToDate.setFont(new Font("Tahoma", Font.BOLD, 12));
		dc_HistoryToDate.setBounds(620, 55, 168, 25);
		panel_4.add(dc_HistoryToDate);
		
		btnGetHistory = new JButton("Get History");
		btnGetHistory.setBounds(890, 29, 143, 50);
		panel_4.add(btnGetHistory);
		
		JLabel lblMandatory = new JLabel("* Mandatory Field  ** Conditional Mandatory Fields");
		lblMandatory.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblMandatory.setBounds(799, 88, 234, 14);
		panel_4.add(lblMandatory);
		
		btnGetHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(PDFUtils.isMandatorySelected(cb_SuppHistory)){
					if(dc_HistoryFromDate.getDate()!=null && dc_HistoryToDate.getDate()==null){
						JOptionPane.showMessageDialog(getContentPane(), "Please Select To Date");
					}else if(dc_HistoryFromDate.getDate()==null && dc_HistoryToDate.getDate()!=null){
						JOptionPane.showMessageDialog(getContentPane(), "Please Select From Date");
					}else{
						fillStockhistoryTable();
					}
					
				}else{
					JOptionPane.showMessageDialog(getContentPane(), "Please Select supplier");
				}
				
			}
		});
		
		JPanel panel_7 = new JPanel();
		panel_7.setBounds(10, 135, 1115, 433);
		panel_7.setBorder(new TitledBorder(null, "Stock Entry History Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		stockHistory.add(panel_7);
		panel_7.setLayout(null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 23, 1090, 399);
		panel_7.add(scrollPane_2);
		
		stockHistoryTable = new JTable();
		
		stockHistoryModel = new DefaultTableModel(){
			 boolean[] columnEditables = new boolean[] {
					 false, false, false, false,false,false,false,true,false
					 };
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
		 stockHistoryModel.setColumnIdentifiers(new String[] {
				 "Supplier", "Invoice Number", "Invoice Date","No Of Items", "Total Qty","Invoice Amount", "Comments","Invoice Details","Stock No"}
	       );
		 stockHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		 PDFUtils.setTableRowHeight(stockHistoryTable);
		 stockHistoryTable.setModel(stockHistoryModel);
		 //stockHistoryTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 scrollPane_2.setViewportView(stockHistoryTable);
		
		stockHistoryTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		stockHistoryTable.getColumnModel().getColumn(1).setPreferredWidth(140);
		stockHistoryTable.getColumnModel().getColumn(2).setPreferredWidth(140);
		stockHistoryTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		stockHistoryTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		stockHistoryTable.getColumnModel().getColumn(5).setPreferredWidth(145);
		stockHistoryTable.getColumnModel().getColumn(6).setPreferredWidth(140);
		stockHistoryTable.getColumnModel().getColumn(7).setPreferredWidth(120);
		stockHistoryTable.getColumnModel().getColumn(8).setWidth(0);
		stockHistoryTable.getColumnModel().getColumn(8).setMinWidth(0);
		stockHistoryTable.getColumnModel().getColumn(8).setMaxWidth(0);
		
		 Action viewBillAction = new AbstractAction()
		 {
		     public void actionPerformed(ActionEvent e)
		     {
		    	 JTable table = (JTable)e.getSource();
        		int modelRow = Integer.valueOf( e.getActionCommand() );
        		int stockNo = Integer.valueOf(table.getModel().getValueAt(modelRow, 8).toString());
		    	 //Open View Bill Screen
        		if(stocksMap.containsKey(stockNo)){
        			 ViewInvoice vs = new ViewInvoice(stocksMap.get(stockNo),parentFrame);
        			 vs.setVisible(true);
        		}
		    	
		     }
		 };
		 contentPane.setLayout(null);

		 ButtonColumn buttonColumn = new ButtonColumn(stockHistoryTable, viewBillAction, 7);
		 buttonColumn.setMnemonic(KeyEvent.VK_D);
		 
		 ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
	
	//Fill Report Table "Supplier", "Invoice Number", "Invoice Date","No Of Items", "Total Qty","Invoice Amount", "Comments","Invoice Details"
		private void fillStockhistoryTable(){
			List<SupplierInvoiceDetails> stockList= SupplierServices.getStockEntryDetails(supplierIdMap.get((String)cb_SuppHistory.getSelectedItem()), tf_HistoryInvoiceNo.getText().equals("")?null:Integer.valueOf(tf_HistoryInvoiceNo.getText()), dc_HistoryFromDate.getDate()==null?null:new java.sql.Date(dc_HistoryFromDate.getDate().getTime()),dc_HistoryToDate.getDate()==null?null:new java.sql.Date(dc_HistoryToDate.getDate().getTime()));
			crateStockHashMap(stockList);
			stockHistoryModel.setRowCount(0);
			if(stockList.isEmpty()){
				JOptionPane.showMessageDialog(getContentPane(), "No records found for the given criteria !");
			}else{
				for(SupplierInvoiceDetails b : stockList){
					
					stockHistoryModel.addRow(new Object[]{b.getSupplierName(),b.getInvoiceNumber(),b.getInvoiceDate(),b.getNoOfItems(),b.getTotalQuanity(),PDFUtils.getDecimalFormat(b.getSupplierInvoiceAmt()),b.getComments(),"View Invoice",b.getStockNumber()});
				}
			}
		}
		
		private void crateStockHashMap(List<SupplierInvoiceDetails> supplierInvoiceDetailsList) {
			stocksMap = new HashMap<Integer, SupplierInvoiceDetails>();
			for(SupplierInvoiceDetails invoice: supplierInvoiceDetailsList){
				stocksMap.put(invoice.getStockNumber(), invoice);
			}
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
