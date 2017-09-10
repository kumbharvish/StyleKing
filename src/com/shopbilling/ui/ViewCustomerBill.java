package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.BillDetails;
import com.shopbilling.dto.Customer;
import com.shopbilling.dto.ItemDetails;
import com.shopbilling.dto.Product;
import com.shopbilling.dto.ReturnDetails;
import com.shopbilling.dto.StatusDTO;
import com.shopbilling.services.BillingServices;
import com.shopbilling.services.JasperServices;
import com.shopbilling.services.ProductHistoryServices;
import com.shopbilling.services.ProductServices;
import com.shopbilling.services.SalesReturnServices;
import com.shopbilling.services.UserServices;
import com.shopbilling.utils.JasperUtils;
import com.shopbilling.utils.PDFUtils;

public class ViewCustomerBill extends JDialog {

	private JPanel contentPane;
	private JTextField tf_BillNumber;
	private JTextField tf_BillDate;
	private JTextField tf_CustName;
	private JTextField tf_CustMob;
	private JTextField tf_NoOfItems;
	private JTextField tf_TotalQty;
	private JTextField tf_SubTotal;
	private JTextField tf_Discount;
	private JTextField tf_Tax;
	private JTextField tf_PaymentMode;
	private JTextField tf_GrandTotal;
	private JTextField tf_DiscAmt;
	private JTextField tf_NetSalesAmt;
	private JTable table;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
	private DefaultTableModel itemsModel;
	private static BillDetails billDetails = new BillDetails();
	private JButton btnModify;
	private JButton btnPrintBill;
	private JButton btnDelete;
	private JDialog mainFrame;
	private JLabel lblReturnNoValue;
	private JLabel lblRDateValue; 
	private JLabel lblTotalReturnAmtValue;
	private JButton btnViewSalesReturn;
	private ReturnDetails returnDetails;
	/**
	 * Create the frame.
	 */
	public ViewCustomerBill(BillDetails bill,JFrame frame) {
		super(frame,"View Bill",true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("shop32X32.png"));
		setBounds(350, 100, 950, 534);
		setResizable(false);
		 mainFrame = this;
		Color lablColor = Color.gray;
		Color valueColor = Color.DARK_GRAY;
		Font valueFont = new Font("Dialog",Font.BOLD,13);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Bill Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 924, 490);
		contentPane.add(panel);
		panel.setLayout(null);
		billDetails = bill;
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Payment Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 25, 300, 380);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblBillNo = new JLabel("Bill Number ");
		lblBillNo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBillNo.setBounds(10, 25, 120, 25);
		lblBillNo.setBorder(border);
		lblBillNo.setForeground(lablColor);
		panel_1.add(lblBillNo);
		
		tf_BillNumber = new JTextField();
		tf_BillNumber.setEnabled(false);
		tf_BillNumber.setEditable(false);
		tf_BillNumber.setBounds(129, 25, 161, 25);
		tf_BillNumber.setBorder(border);
		tf_BillNumber.setText(" "+String.valueOf(bill.getBillNumber()));
		tf_BillNumber.setFont(valueFont);
		tf_BillNumber.setDisabledTextColor(valueColor);
		panel_1.add(tf_BillNumber);
		tf_BillNumber.setColumns(10);
		
		JLabel lblBillDate = new JLabel("Bill Date ");
		lblBillDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBillDate.setBorder(border);
		lblBillDate.setBounds(10, 49, 120, 25);
		lblBillDate.setForeground(lablColor);

		panel_1.add(lblBillDate);
		
		tf_BillDate = new JTextField();
		tf_BillDate.setEditable(false);
		tf_BillDate.setEnabled(false);
		tf_BillDate.setColumns(10);
		tf_BillDate.setBounds(129, 49, 161, 25);
		tf_BillDate.setBorder(border);
		tf_BillDate.setText(" "+sdf.format(bill.getTimestamp()));
		tf_BillDate.setFont(valueFont);
		tf_BillDate.setDisabledTextColor(valueColor);
		panel_1.add(tf_BillDate);
		
		JLabel lblCustomerName = new JLabel("Customer Name ");
		lblCustomerName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCustomerName.setBounds(10, 73, 120, 25);
		lblCustomerName.setBorder(border);
		lblCustomerName.setForeground(lablColor);
		panel_1.add(lblCustomerName);
		
		tf_CustName = new JTextField();
		tf_CustName.setEnabled(false);
		tf_CustName.setEditable(false);
		tf_CustName.setColumns(10);
		tf_CustName.setBounds(129, 73, 161, 25);
		tf_CustName.setBorder(border);
		tf_CustName.setText(" "+bill.getCustomerName());
		tf_CustName.setFont(valueFont);
		tf_CustName.setDisabledTextColor(valueColor);
		panel_1.add(tf_CustName);
		
		JLabel lblCustomerMobile = new JLabel("Customer Mobile ");
		lblCustomerMobile.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCustomerMobile.setBounds(10, 97, 120, 25);
		lblCustomerMobile.setBorder(border);
		lblCustomerMobile.setForeground(lablColor);
		panel_1.add(lblCustomerMobile);
		
		tf_CustMob = new JTextField();
		tf_CustMob.setEnabled(false);
		tf_CustMob.setEditable(false);
		tf_CustMob.setColumns(10);
		tf_CustMob.setBounds(129, 97, 161, 25);
		tf_CustMob.setBorder(border);
		tf_CustMob.setText(" "+String.valueOf(bill.getCustomerMobileNo()));
		tf_CustMob.setFont(valueFont);
		tf_CustMob.setDisabledTextColor(valueColor);
		panel_1.add(tf_CustMob);
		
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
		
		JLabel lblSubTotal = new JLabel("Sub Total ");
		lblSubTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSubTotal.setBounds(10, 169, 120, 25);
		lblSubTotal.setBorder(border);
		lblSubTotal.setForeground(lablColor);
		panel_1.add(lblSubTotal);
		
		JLabel lblDiscount = new JLabel("Discount(%) ");
		lblDiscount.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDiscount.setBounds(10, 193, 120, 25);
		lblDiscount.setBorder(border);
		lblDiscount.setForeground(lablColor);
		panel_1.add(lblDiscount);
		
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
		
		tf_SubTotal = new JTextField();
		tf_SubTotal.setEnabled(false);
		tf_SubTotal.setEditable(false);
		tf_SubTotal.setColumns(10);
		tf_SubTotal.setBounds(129, 169, 161, 25);
		tf_SubTotal.setBorder(border);
		tf_SubTotal.setText(" "+PDFUtils.getDecimalFormat(bill.getTotalAmount()));
		tf_SubTotal.setFont(valueFont);
		tf_SubTotal.setDisabledTextColor(valueColor);
		panel_1.add(tf_SubTotal);
		
		tf_Discount = new JTextField();
		tf_Discount.setEnabled(false);
		tf_Discount.setEditable(false);
		tf_Discount.setColumns(10);
		tf_Discount.setBounds(129, 193, 161, 25);
		tf_Discount.setBorder(border);
		tf_Discount.setText(" "+PDFUtils.getDecimalFormat(bill.getDiscount()));
		tf_Discount.setDisabledTextColor(valueColor);
		tf_Discount.setFont(valueFont);
		
		panel_1.add(tf_Discount);
		
		JLabel lblDiscountAmount = new JLabel("Discount Amount ");
		lblDiscountAmount.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDiscountAmount.setBounds(10, 217, 120, 25);
		lblDiscountAmount.setBorder(border);
		lblDiscountAmount.setForeground(lablColor);
		panel_1.add(lblDiscountAmount);
		
		JLabel lblGrandTotal = new JLabel("Grand Total ");
		lblGrandTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGrandTotal.setBounds(10, 241, 120, 25);
		lblGrandTotal.setBorder(border);
		lblGrandTotal.setForeground(lablColor);
		panel_1.add(lblGrandTotal);
		
		JLabel lblPay = new JLabel("Payment Mode ");
		lblPay.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPay.setBounds(10, 265, 120, 25);
		lblPay.setBorder(border);
		lblPay.setForeground(lablColor);
		panel_1.add(lblPay);
		
		JLabel lblTax = new JLabel("Tax(%) ");
		lblTax.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTax.setBounds(10, 289, 120, 25);
		lblTax.setBorder(border);
		lblTax.setForeground(lablColor);
		panel_1.add(lblTax);
		
		JLabel lblNetSalesAmount = new JLabel("Net Sales Amount ");
		lblNetSalesAmount.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNetSalesAmount.setHorizontalAlignment(SwingConstants.CENTER);
		lblNetSalesAmount.setBounds(10, 313, 134, 25);
		
		panel_1.add(lblNetSalesAmount);
		
		tf_Tax = new JTextField();
		tf_Tax.setEnabled(false);
		tf_Tax.setEditable(false);
		tf_Tax.setColumns(10);
		tf_Tax.setBounds(129, 289, 161, 25);
		tf_Tax.setBorder(border);
		tf_Tax.setText(" "+PDFUtils.getDecimalFormat(bill.getTax()));
		tf_Tax.setFont(valueFont);
		tf_Tax.setDisabledTextColor(valueColor);
		panel_1.add(tf_Tax);
		
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
		
		tf_GrandTotal = new JTextField();
		tf_GrandTotal.setEnabled(false);
		tf_GrandTotal.setEditable(false);
		tf_GrandTotal.setColumns(10);
		tf_GrandTotal.setBounds(129, 241, 161, 25);
		tf_GrandTotal.setBorder(border);
		tf_GrandTotal.setText(" "+PDFUtils.getDecimalFormat(bill.getGrandTotal()));
		tf_GrandTotal.setFont(valueFont);
		tf_GrandTotal.setDisabledTextColor(valueColor);
		
		panel_1.add(tf_GrandTotal);
		
		tf_DiscAmt = new JTextField();
		tf_DiscAmt.setEnabled(false);
		tf_DiscAmt.setEditable(false);
		tf_DiscAmt.setColumns(10);
		tf_DiscAmt.setBounds(129, 217, 161, 25);
		tf_DiscAmt.setBorder(border);
		tf_DiscAmt.setText(" "+PDFUtils.getDecimalFormat(bill.getDiscountAmt()));
		tf_DiscAmt.setFont(valueFont);
		tf_DiscAmt.setDisabledTextColor(valueColor);
		panel_1.add(tf_DiscAmt);
		
		tf_NetSalesAmt = new JTextField();
		tf_NetSalesAmt.setText("0.00");
		tf_NetSalesAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_NetSalesAmt.setForeground(Color.WHITE);
		tf_NetSalesAmt.setFont(new Font("Dialog", Font.BOLD, 30));
		tf_NetSalesAmt.setEditable(false);
		tf_NetSalesAmt.setColumns(10);
		tf_NetSalesAmt.setBackground(Color.GRAY);
		tf_NetSalesAmt.setBounds(59, 338, 231, 35);
		tf_NetSalesAmt.setText(PDFUtils.getDecimalFormat(bill.getNetSalesAmt()));
		panel_1.add(tf_NetSalesAmt);
		
		JLabel label = new JLabel("New label");
		label.setIcon(new ImageIcon(ViewCustomerBill.class.getResource("/images/currency_sign_rupee.png")));
		label.setBounds(30, 338, 29, 35);
		panel_1.add(label);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Item Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(320, 25, 594, 342);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 25, 574, 307);
		panel_2.add(scrollPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Item No", "Item Name", "MRP", "Rate", "Qty", "Amount"
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
		
		btnPrintBill = new JButton("Print");
		btnPrintBill.setIcon(new ImageIcon(ViewCustomerBill.class.getResource("/images/printer_off16.png")));
		btnPrintBill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printBill(billDetails);
			}
		});
		
		btnPrintBill.setBounds(397, 378, 110, 23);
		panel.add(btnPrintBill);
		
		btnModify = new JButton("Modify");
		btnModify.setIcon(new ImageIcon(ViewCustomerBill.class.getResource("/images/edit.png")));
		btnModify.setBounds(598, 378, 110, 23);
		panel.add(btnModify);
		
		btnDelete = new JButton("Delete");
		btnDelete.setIcon(new ImageIcon(ViewCustomerBill.class.getResource("/images/list_remove.png")));
		btnDelete.setBounds(775, 378, 110, 23);
		panel.add(btnDelete);
		
		//Modify Bill
		btnModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ModifyBill(billDetails);
			}
		});
		//Delete Bill
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteBill(billDetails);
			}
		});
		
		JPanel p_salesReturnDtls = new JPanel();
		p_salesReturnDtls.setBorder(new TitledBorder(null, "Sales Return Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		p_salesReturnDtls.setBounds(10, 407, 904, 71);
		panel.add(p_salesReturnDtls);
		p_salesReturnDtls.setLayout(null);
		
		btnViewSalesReturn = new JButton("View");
		btnViewSalesReturn.setIcon(new ImageIcon(ViewCustomerBill.class.getResource("/images/cart_delete.png")));
		btnViewSalesReturn.setEnabled(false);
		btnViewSalesReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewSalesReturn();
			}
		});
		btnViewSalesReturn.setBounds(766, 24, 110, 23);
		p_salesReturnDtls.add(btnViewSalesReturn);
		
		JLabel lblReturnNumber = new JLabel("Return Number : ");
		lblReturnNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblReturnNumber.setBounds(22, 24, 110, 25);
		lblReturnNumber.setForeground(lablColor);
		
		p_salesReturnDtls.add(lblReturnNumber);
		
		lblReturnNoValue = new JLabel("NA");
		lblReturnNoValue.setHorizontalAlignment(SwingConstants.LEFT);
		lblReturnNoValue.setBounds(133, 24, 92, 25);
		p_salesReturnDtls.add(lblReturnNoValue);
		
		lblRDateValue = new JLabel("NA");
		lblRDateValue.setHorizontalAlignment(SwingConstants.LEFT);
		lblRDateValue.setBounds(301, 24, 162, 25);
		p_salesReturnDtls.add(lblRDateValue);
		
		JLabel lblDate = new JLabel("Date : ");
		lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDate.setBounds(235, 24, 65, 25);
		lblDate.setForeground(lablColor);
		p_salesReturnDtls.add(lblDate);
		
		JLabel lblTotalAmt = new JLabel("Total Return Amount : ");
		lblTotalAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalAmt.setBounds(481, 24, 137, 25);
		lblTotalAmt.setForeground(lablColor);
		p_salesReturnDtls.add(lblTotalAmt);
		
		lblTotalReturnAmtValue = new JLabel("NA");
		lblTotalReturnAmtValue.setHorizontalAlignment(SwingConstants.LEFT);
		lblTotalReturnAmtValue.setBounds(618, 24, 123, 25);
		p_salesReturnDtls.add(lblTotalReturnAmtValue);
		 table.getColumnModel().getColumn(0).setPreferredWidth(100);
		 table.getColumnModel().getColumn(1).setPreferredWidth(290);
		 table.getColumnModel().getColumn(2).setPreferredWidth(100);
		 table.getColumnModel().getColumn(3).setPreferredWidth(100);
		 table.getColumnModel().getColumn(4).setPreferredWidth(80);
		 table.getColumnModel().getColumn(5).setPreferredWidth(100);
		 List<ItemDetails> itemList = ProductServices.getItemDetails(bill.getBillNumber());
		 billDetails.setItemDetails(itemList);
		 for(ItemDetails item : itemList){
			 itemsModel = (DefaultTableModel)table.getModel();
			 itemsModel.addRow(new Object[]{item.getItemNo(),item.getItemName(),PDFUtils.getDecimalFormat(item.getMRP()),PDFUtils.getDecimalFormat(item.getRate()),item.getQuantity(),PDFUtils.getDecimalFormat(item.getAmount())});
		 }
		 
		 //Set Sales Return Details
		 setSalesReturnDetails();
	}
	
	private void setSalesReturnDetails() {
		returnDetails = SalesReturnServices.getReturnDetails(billDetails.getBillNumber());
		if(returnDetails!=null){
			lblReturnNoValue.setText(String.valueOf(returnDetails.getReturnNumber()));
			lblRDateValue.setText(PDFUtils.getFormattedDateWithTime(returnDetails.getTimestamp()));
			lblTotalReturnAmtValue.setText(PDFUtils.getDecimalFormat(returnDetails.getTotalAmount()));
			btnModify.setEnabled(false);
			btnDelete.setEnabled(false);
			btnViewSalesReturn.setEnabled(true);
		}
	}

	protected void deleteBill(BillDetails billDetails2) {
		StatusDTO	statusCustBlanceUpdate=new StatusDTO(-1);
		StatusDTO	statusDeleteBill=new StatusDTO(-1);
		StatusDTO 	statusUpdatePayHistory= new StatusDTO(-1);
		StatusDTO 	statusUpdateStock= new StatusDTO(-1);
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure to Delete Bill No. : "+billDetails2.getBillNumber()+" ?","Delete Bill",dialogButton);
		if(dialogResult == JOptionPane.YES_OPTION){
			StatusDTO isSalesReturned = SalesReturnServices.isSalesReturned(billDetails2.getBillNumber());
			if(isSalesReturned.getStatusCode()!=0){
				Customer customer = UserServices.getCustomerDetails(billDetails2.getCustomerMobileNo());
				//Added condition to check customer pending balance is greater than or equal to current return amount
				if("PENDING".equals(billDetails2.getPaymentMode()) && customer.getBalanceAmt()>=billDetails2.getNetSalesAmt()){
					statusDeleteBill = BillingServices.deleteBillDetails(billDetails2.getBillNumber());
					if(statusDeleteBill.getStatusCode()==0){
						//Stock Correction
							statusUpdateStock = BillingServices.updateDeletedBillProductStock(billDetails2.getItemDetails());
							
							statusUpdatePayHistory = UserServices.addCustomerPaymentHistory(billDetails2.getCustomerMobileNo(), 0,billDetails2.getNetSalesAmt(), AppConstants.DEBIT, "DELETE BILL Adjustment Based on B.No : "+billDetails2.getBillNumber());
							statusCustBlanceUpdate = UserServices.settleUpCustomerBalance(billDetails2.getCustomerMobileNo(), billDetails2.getNetSalesAmt());
	
							if(statusCustBlanceUpdate.getStatusCode()==0 
									&& statusUpdatePayHistory.getStatusCode()==0 
									&& statusUpdateStock.getStatusCode()==0){
								ProductHistoryServices.addProductStockLedger(getProductList(billDetails2.getItemDetails(),billDetails2.getBillNumber()), AppConstants.STOCK_IN, AppConstants.DELETE_BILL);
								JOptionPane.showMessageDialog(getContentPane(), "Bill Deleted Sucessfully !");
								dispose();
							}
						}else{
							JOptionPane.showMessageDialog(getContentPane(),"Error Occured while deleting the bill !","Error",JOptionPane.WARNING_MESSAGE);
						}
					}else{
						if("PENDING".equals(billDetails2.getPaymentMode())){
							JOptionPane.showMessageDialog(getContentPane(), "Customer Balance is less than bill amount. Please check customer payment history !");
						}
					}
				//Cash Bill
					if(!"PENDING".equals(billDetails2.getPaymentMode())){
						statusDeleteBill = BillingServices.deleteBillDetails(billDetails2.getBillNumber());
						if(statusDeleteBill.getStatusCode()==0){
							//Stock Correction
								statusUpdateStock = BillingServices.updateDeletedBillProductStock(billDetails2.getItemDetails());
								if(statusUpdateStock.getStatusCode()==0 ){
									ProductHistoryServices.addProductStockLedger(getProductList(billDetails2.getItemDetails(),billDetails2.getBillNumber()), AppConstants.STOCK_IN, AppConstants.DELETE_BILL);
									JOptionPane.showMessageDialog(getContentPane(), "Bill Deleted Sucessfully !");
									dispose();
								}
						}else{
							JOptionPane.showMessageDialog(getContentPane(),"Error Occured while deleting the bill !","Error",JOptionPane.WARNING_MESSAGE);
						}
					}
			}else{
				JOptionPane.showMessageDialog(getContentPane(), "Sales Return available for this bill. Delete bill operation is not allowed.", "Delete Bill", JOptionPane.WARNING_MESSAGE);
			}
			
		}
		
	}

	//Modify Bill
	protected void ModifyBill(BillDetails bill) {
		ModifyBillUI  modifyBill = new ModifyBillUI(bill,mainFrame);
		modifyBill.setVisible(true);
	}
	//Modify Bill
	protected void viewSalesReturn() {
		ViewSalesReturnUI  viewSalesReturnUI = new ViewSalesReturnUI(returnDetails.getReturnNumber(),mainFrame);
		viewSalesReturnUI.setVisible(true);
	}
	//Print Bill
	protected void printBill(BillDetails bill) {
		JOptionPane.showMessageDialog(getContentPane(), "Printing !");
		JasperUtils.createPDFWithJasper(JasperServices.createDataForBill(bill),AppConstants.BILL_PRINT_JASPER);
	}
	
	private List<Product> getProductList(List<ItemDetails> itemList, int billNo){
		List<Product> productList = new ArrayList<Product>();
		for(ItemDetails item : itemList){
			Product p = new Product();
			p.setProductCode(item.getItemNo());
			p.setQuanity(item.getQuantity());
			p.setDescription("Delete Bill Based on Bill No.: "+billNo);
			productList.add(p);
		}
		return productList;
		
	}
}
