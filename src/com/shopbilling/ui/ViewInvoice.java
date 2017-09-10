package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.shopbilling.dto.StockItemDetails;
import com.shopbilling.dto.SupplierInvoiceDetails;
import com.shopbilling.services.SupplierServices;
import com.shopbilling.utils.PDFUtils;

public class ViewInvoice extends JDialog {

	private JPanel contentPane;
	private JTextField tf_SupplierName;
	private JTextField tf_InvoiceDate;
	private JTextField tf_InvoiceNumber;
	private JTextField tf_Comments;
	private JTextField tf_NoOfItems;
	private JTextField tf_TotalQty;
	private JTextField tf_StockEntryDate;
	private JTextField tf_TotalAmtWOTax;
	private JTextField tf_ExtraCharges;
	private JTextField tf_PaymentMode;
	private JTextField tf_TotalMRPAmt;
	private JTextField tf_TotalTax;
	private JTextField tf_InvoiceAmt;
	private JTable table;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
	private SimpleDateFormat sdfInvoiceDate = new SimpleDateFormat("dd-MM-yyyy");
	private DefaultTableModel itemsModel;

	/**
	 * Create the frame.
	 */
	public ViewInvoice(SupplierInvoiceDetails bill,JFrame frame) {
		super(frame,"View Invoice",true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(300, 100, 1045, 477);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage("shop32X32.png"));
		Color lablColor = Color.gray;
		Color valueColor = Color.DARK_GRAY;
		Font valueFont = new Font("Dialog",Font.BOLD,13);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Invoice Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 1019, 416);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Payment Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 25, 300, 380);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblSupplierName = new JLabel("Supplier Name ");
		lblSupplierName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSupplierName.setBounds(10, 25, 120, 25);
		lblSupplierName.setBorder(border);
		lblSupplierName.setForeground(lablColor);
		panel_1.add(lblSupplierName);
		
		tf_SupplierName = new JTextField();
		tf_SupplierName.setEnabled(false);
		tf_SupplierName.setEditable(false);
		tf_SupplierName.setBounds(129, 25, 161, 25);
		tf_SupplierName.setBorder(border);
		tf_SupplierName.setText(" "+bill.getSupplierName());
		tf_SupplierName.setFont(new Font("Dialog", Font.BOLD, 11));
		tf_SupplierName.setDisabledTextColor(valueColor);
		panel_1.add(tf_SupplierName);
		tf_SupplierName.setColumns(10);
		
		JLabel lblBillDate = new JLabel("Invoice Date ");
		lblBillDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBillDate.setBorder(border);
		lblBillDate.setBounds(10, 49, 120, 25);
		lblBillDate.setForeground(lablColor);

		panel_1.add(lblBillDate);
		
		tf_InvoiceDate = new JTextField();
		tf_InvoiceDate.setEditable(false);
		tf_InvoiceDate.setEnabled(false);
		tf_InvoiceDate.setColumns(10);
		tf_InvoiceDate.setBounds(129, 49, 161, 25);
		tf_InvoiceDate.setBorder(border);
		tf_InvoiceDate.setText(" "+sdfInvoiceDate.format(bill.getInvoiceDate()));
		tf_InvoiceDate.setFont(valueFont);
		tf_InvoiceDate.setDisabledTextColor(valueColor);
		panel_1.add(tf_InvoiceDate);
		
		JLabel lblCustomerName = new JLabel("Invoice Number ");
		lblCustomerName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCustomerName.setBounds(10, 73, 120, 25);
		lblCustomerName.setBorder(border);
		lblCustomerName.setForeground(lablColor);
		panel_1.add(lblCustomerName);
		
		tf_InvoiceNumber = new JTextField();
		tf_InvoiceNumber.setEnabled(false);
		tf_InvoiceNumber.setEditable(false);
		tf_InvoiceNumber.setColumns(10);
		tf_InvoiceNumber.setBounds(129, 73, 161, 25);
		tf_InvoiceNumber.setBorder(border);
		tf_InvoiceNumber.setText(" "+String.valueOf(bill.getInvoiceNumber()));
		tf_InvoiceNumber.setFont(valueFont);
		tf_InvoiceNumber.setDisabledTextColor(valueColor);
		panel_1.add(tf_InvoiceNumber);
		
		JLabel lblCustomerMobile = new JLabel("Comments ");
		lblCustomerMobile.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCustomerMobile.setBounds(10, 97, 120, 25);
		lblCustomerMobile.setBorder(border);
		lblCustomerMobile.setForeground(lablColor);
		panel_1.add(lblCustomerMobile);
		
		tf_Comments = new JTextField();
		tf_Comments.setEnabled(false);
		tf_Comments.setEditable(false);
		tf_Comments.setColumns(10);
		tf_Comments.setBounds(129, 97, 161, 25);
		tf_Comments.setBorder(border);
		tf_Comments.setText(" "+String.valueOf(bill.getComments()));
		tf_Comments.setFont(valueFont);
		tf_Comments.setDisabledTextColor(valueColor);
		panel_1.add(tf_Comments);
		
		JLabel lblNoOfItems = new JLabel("No Of Items ");
		lblNoOfItems.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNoOfItems.setBounds(10, 121, 120, 25);
		lblNoOfItems.setBorder(border);
		lblNoOfItems.setForeground(lablColor);
		panel_1.add(lblNoOfItems);
		
		JLabel lblTotalQuantity = new JLabel("Total Quantity ");
		lblTotalQuantity.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalQuantity.setBounds(10, 145, 120, 25);
		lblTotalQuantity.setBorder(border);
		lblTotalQuantity.setForeground(lablColor);
		panel_1.add(lblTotalQuantity);
		
		JLabel lblStockEntryDate = new JLabel("Stock Entry Date ");
		lblStockEntryDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStockEntryDate.setBounds(10, 169, 120, 25);
		lblStockEntryDate.setBorder(border);
		lblStockEntryDate.setForeground(lablColor);
		panel_1.add(lblStockEntryDate);
		
		JLabel lblTotalAmtWOTax = new JLabel("Total Amt WO Tax ");
		lblTotalAmtWOTax.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalAmtWOTax.setBounds(10, 193, 120, 25);
		lblTotalAmtWOTax.setBorder(border);
		lblTotalAmtWOTax.setForeground(lablColor);
		panel_1.add(lblTotalAmtWOTax);
		
		tf_NoOfItems = new JTextField();
		tf_NoOfItems.setEnabled(false);
		tf_NoOfItems.setEditable(false);
		tf_NoOfItems.setColumns(10);
		tf_NoOfItems.setBounds(129, 121, 161, 25);
		tf_NoOfItems.setBorder(border);
		tf_NoOfItems.setText(" "+String.valueOf(bill.getNoOfItems()));
		tf_NoOfItems.setFont(valueFont);
		tf_NoOfItems.setDisabledTextColor(valueColor);
		panel_1.add(tf_NoOfItems);
		
		tf_TotalQty = new JTextField();
		tf_TotalQty.setEnabled(false);
		tf_TotalQty.setEditable(false);
		tf_TotalQty.setColumns(10);
		tf_TotalQty.setBounds(129, 145, 161, 25);
		tf_TotalQty.setBorder(border);
		tf_TotalQty.setText(" "+String.valueOf(bill.getTotalQuanity()));
		tf_TotalQty.setFont(valueFont);
		tf_TotalQty.setDisabledTextColor(valueColor);
		panel_1.add(tf_TotalQty);
		
		tf_StockEntryDate = new JTextField();
		tf_StockEntryDate.setEnabled(false);
		tf_StockEntryDate.setEditable(false);
		tf_StockEntryDate.setColumns(10);
		tf_StockEntryDate.setBounds(129, 169, 161, 25);
		tf_StockEntryDate.setBorder(border);
		tf_StockEntryDate.setText(" "+sdf.format(bill.getTimeStamp()));
		tf_StockEntryDate.setFont(valueFont);
		tf_StockEntryDate.setDisabledTextColor(valueColor);
		panel_1.add(tf_StockEntryDate);
		
		tf_TotalAmtWOTax = new JTextField();
		tf_TotalAmtWOTax.setEnabled(false);
		tf_TotalAmtWOTax.setEditable(false);
		tf_TotalAmtWOTax.setColumns(10);
		tf_TotalAmtWOTax.setBounds(129, 193, 161, 25);
		tf_TotalAmtWOTax.setBorder(border);
		tf_TotalAmtWOTax.setText(" "+PDFUtils.getDecimalFormat(bill.getTotalAmtWOTax()));
		tf_TotalAmtWOTax.setDisabledTextColor(valueColor);
		tf_TotalAmtWOTax.setFont(valueFont);
		
		panel_1.add(tf_TotalAmtWOTax);
		
		JLabel lblTotalTax = new JLabel("Total Tax ");
		lblTotalTax.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalTax.setBounds(10, 217, 120, 25);
		lblTotalTax.setBorder(border);
		lblTotalTax.setForeground(lablColor);
		panel_1.add(lblTotalTax);
		
		JLabel lblTotalMRPAmt = new JLabel("Total MRP Amt");
		lblTotalMRPAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalMRPAmt.setBounds(10, 241, 120, 25);
		lblTotalMRPAmt.setBorder(border);
		lblTotalMRPAmt.setForeground(lablColor);
		panel_1.add(lblTotalMRPAmt);
		
		JLabel lblPay = new JLabel("Payment Mode ");
		lblPay.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPay.setBounds(10, 265, 120, 25);
		lblPay.setBorder(border);
		lblPay.setForeground(lablColor);
		panel_1.add(lblPay);
		
		JLabel lblExtraCharges = new JLabel("Extra Charges");
		lblExtraCharges.setHorizontalAlignment(SwingConstants.RIGHT);
		lblExtraCharges.setBounds(10, 289, 120, 25);
		lblExtraCharges.setBorder(border);
		lblExtraCharges.setForeground(lablColor);
		panel_1.add(lblExtraCharges);
		
		JLabel lblNetSalesAmount = new JLabel("Invoice Amount ");
		lblNetSalesAmount.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNetSalesAmount.setHorizontalAlignment(SwingConstants.CENTER);
		lblNetSalesAmount.setBounds(10, 313, 134, 25);
		
		panel_1.add(lblNetSalesAmount);
		
		tf_ExtraCharges = new JTextField();
		tf_ExtraCharges.setEnabled(false);
		tf_ExtraCharges.setEditable(false);
		tf_ExtraCharges.setColumns(10);
		tf_ExtraCharges.setBounds(129, 289, 161, 25);
		tf_ExtraCharges.setBorder(border);
		tf_ExtraCharges.setText(" "+PDFUtils.getDecimalFormat(bill.getExtraCharges()));
		tf_ExtraCharges.setFont(valueFont);
		tf_ExtraCharges.setDisabledTextColor(valueColor);
		panel_1.add(tf_ExtraCharges);
		
		tf_PaymentMode = new JTextField();
		tf_PaymentMode.setEnabled(false);
		tf_PaymentMode.setEditable(false);
		tf_PaymentMode.setColumns(10);
		tf_PaymentMode.setBounds(129, 265, 161, 25);
		tf_PaymentMode.setBorder(border);
		tf_PaymentMode.setText(" "+bill.getPaymentMode());
		tf_PaymentMode.setFont(valueFont);
		tf_PaymentMode.setDisabledTextColor(valueColor);
		panel_1.add(tf_PaymentMode);
		
		tf_TotalMRPAmt = new JTextField();
		tf_TotalMRPAmt.setEnabled(false);
		tf_TotalMRPAmt.setEditable(false);
		tf_TotalMRPAmt.setColumns(10);
		tf_TotalMRPAmt.setBounds(129, 241, 161, 25);
		tf_TotalMRPAmt.setBorder(border);
		tf_TotalMRPAmt.setText(" "+PDFUtils.getDecimalFormat(bill.getTotalMRPAmt()));
		tf_TotalMRPAmt.setFont(valueFont);
		tf_TotalMRPAmt.setDisabledTextColor(valueColor);
		
		panel_1.add(tf_TotalMRPAmt);
		
		tf_TotalTax = new JTextField();
		tf_TotalTax.setEnabled(false);
		tf_TotalTax.setEditable(false);
		tf_TotalTax.setColumns(10);
		tf_TotalTax.setBounds(129, 217, 161, 25);
		tf_TotalTax.setBorder(border);
		tf_TotalTax.setText(" "+PDFUtils.getDecimalFormat(bill.getTotalTax()));
		tf_TotalTax.setFont(valueFont);
		tf_TotalTax.setDisabledTextColor(valueColor);
		panel_1.add(tf_TotalTax);
		
		tf_InvoiceAmt = new JTextField();
		tf_InvoiceAmt.setText("0.00");
		tf_InvoiceAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_InvoiceAmt.setForeground(Color.WHITE);
		tf_InvoiceAmt.setFont(new Font("Dialog", Font.BOLD, 30));
		tf_InvoiceAmt.setEditable(false);
		tf_InvoiceAmt.setColumns(10);
		tf_InvoiceAmt.setBackground(Color.GRAY);
		tf_InvoiceAmt.setBounds(59, 338, 231, 35);
		tf_InvoiceAmt.setText(PDFUtils.getDecimalFormat(bill.getSupplierInvoiceAmt()));
		panel_1.add(tf_InvoiceAmt);
		
		JLabel label = new JLabel("New label");
		label.setIcon(new ImageIcon(ViewInvoice.class.getResource("/images/currency_sign_rupee.png")));
		label.setBounds(30, 338, 29, 35);
		panel_1.add(label);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Item Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(320, 25, 689, 380);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 25, 669, 344);
		panel_2.add(scrollPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Item Name", "MRP", "TAX(%)","Rate","Purchase Price", "Qty", "Amount"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false,false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		//table.getColumnModel().getColumn(0).setResizable(false);
		table.setEnabled(false);
		PDFUtils.setTableRowHeight(table);
		
		scrollPane.setViewportView(table);
		 table.getColumnModel().getColumn(0).setPreferredWidth(250);
		 table.getColumnModel().getColumn(1).setPreferredWidth(80);
		 table.getColumnModel().getColumn(2).setPreferredWidth(100);
		 table.getColumnModel().getColumn(3).setPreferredWidth(100);
		 table.getColumnModel().getColumn(4).setPreferredWidth(130);
		 table.getColumnModel().getColumn(5).setPreferredWidth(80);
		 table.getColumnModel().getColumn(6).setPreferredWidth(120);
		 for(StockItemDetails item : SupplierServices.getItemDetails(bill.getStockNumber())){
			 itemsModel = (DefaultTableModel)table.getModel();
			 itemsModel.addRow(new Object[]{item.getItemName(),PDFUtils.getDecimalFormat(item.getMRP()),PDFUtils.getDecimalFormat(item.getTax()),PDFUtils.getDecimalFormat(item.getRate()),PDFUtils.getDecimalFormat(item.getPurchasePrice()),item.getQuantity(),PDFUtils.getDecimalFormat(item.getAmount())});
		 }
	}
}
