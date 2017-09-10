package com.shopbilling.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
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
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.dto.BillDetails;
import com.shopbilling.services.ButtonColumn;
import com.shopbilling.services.ProductServices;
import com.shopbilling.utils.PDFUtils;
import com.toedter.calendar.JDateChooser;

public class SellReport extends JInternalFrame {
	private JTable table;
	private DefaultTableModel reportModel;
	private JDateChooser toDateChooser;
	private JDateChooser fromDateChooser;
	private int totalCustomerCount=0;
	private int totalNoOfItems=0;
	private int totalQty=0;
	private double totalDiscAmt=0;
	private double pendingPayment=0;
	private double cashPayment=0;
	private double totalAmount=0;
	//
	private JLabel lblTotalCustomerCountvalue;
	private JLabel lblTotalNoOfValue;
	private JLabel lblTotalQuantityValue;
	private JLabel lblTotalDiscountAmountValue;
	private JLabel lblPendingPaymentValue;
	private JLabel lblCashPaymentValue;
	private JLabel lblTotalAmountValue;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
	private Map<Integer,BillDetails> billsMap;
	private JFrame parentFrame;
	private JTextField tf_CustMobile;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SellReport frame = new SellReport();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the frame.
	 */
	public SellReport() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Sale Report");
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Report Date", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 1150, 60);
		getContentPane().add(panel);
		panel.setLayout(null);
		parentFrame = (JFrame)this.getTopLevelAncestor();
		JLabel lblFromDate = new JLabel("From Date :");
		lblFromDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFromDate.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblFromDate.setBounds(10, 19, 99, 28);
		panel.add(lblFromDate);
		
		JLabel lblToDate = new JLabel("To Date :");
		lblToDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblToDate.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblToDate.setBounds(293, 19, 79, 28);
		panel.add(lblToDate);
		
		JButton btnGetReport = new JButton("Get Report");
		btnGetReport.setIcon(new ImageIcon(SellReport.class.getResource("/images/clipboard_report_bar_24_ns.png")));
		btnGetReport.setBackground(UIManager.getColor("Button.background"));
		btnGetReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillReportTable();
				setReportValues();
			}
		});
		btnGetReport.setBounds(962, 15, 127, 34);
		panel.add(btnGetReport);
		
		fromDateChooser = new JDateChooser();
		fromDateChooser.setFont(new Font("Tahoma", Font.BOLD, 12));
		fromDateChooser.setBounds(119, 19, 133, 28);
		fromDateChooser.setDate(new Date());
		
		panel.add(fromDateChooser);
		
		toDateChooser = new JDateChooser();
		toDateChooser.setFont(new Font("Tahoma", Font.BOLD, 12));
		toDateChooser.setBounds(382, 19, 133, 28);
		toDateChooser.setDate(new Date());
		panel.add(toDateChooser);
		
		JLabel lblCustomerMobile = new JLabel("Customer Mobile :");
		lblCustomerMobile.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCustomerMobile.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCustomerMobile.setBounds(559, 19, 146, 28);
		panel.add(lblCustomerMobile);
		
		tf_CustMobile = new JTextField();
		tf_CustMobile.setBounds(720, 19, 193, 28);
		panel.add(tf_CustMobile);
		tf_CustMobile.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Bill Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 82, 1150, 356);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 26, 1120, 319);
		panel_1.add(scrollPane);
		
		table = new JTable();
		reportModel = new DefaultTableModel(){
			 boolean[] columnEditables = new boolean[] {
					 false, false, false, false,false,false,false,false,true
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
		 reportModel.setColumnIdentifiers(new String[] {
				 "Bill Number", "Customer Mobile", "No of Items", "Quantity", "Pay Mode","Bill Date","Discount Amt","Net Sales Amount","Bill Details"}
	       );
		table.setModel(reportModel);
		
		//Table Row Height 
		 PDFUtils.setTableRowHeight(table);
		 
		scrollPane.setViewportView(table);
		 //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 
		 Action viewBillAction = new AbstractAction()
		 {
		     public void actionPerformed(ActionEvent e)
		     {
		    	 JTable table = (JTable)e.getSource();
        		int modelRow = Integer.valueOf( e.getActionCommand() );
        		int billNo = Integer.valueOf(table.getModel().getValueAt(modelRow, 0).toString());
		    	 //Open View Bill Screen
        		if(billsMap.containsKey(billNo)){
        			 ViewCustomerBill vb = new ViewCustomerBill(billsMap.get(billNo),parentFrame);
    		    	 vb.setVisible(true);
        		}
		    	
		     }
		 };

		 ButtonColumn buttonColumn = new ButtonColumn(table, viewBillAction, 8);
		 buttonColumn.setMnemonic(KeyEvent.VK_D);
		 
		 JPanel panel_2 = new JPanel();
		 panel_2.setBorder(new TitledBorder(null, "Consolidated Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		 panel_2.setBounds(10, 449, 1150, 131);
		 getContentPane().add(panel_2);
		 panel_2.setLayout(null);
		 Border br = new LineBorder(Color.black, 2);
		 JLabel lblTotalCustomerCount = new JLabel("Total Customer Count");
		 lblTotalCustomerCount.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalCustomerCount.setBounds(10, 22, 176, 31);
		 lblTotalCustomerCount.setBorder(br);
		 panel_2.add(lblTotalCustomerCount);
		 
		 lblTotalCustomerCountvalue = new JLabel("");
		 lblTotalCustomerCountvalue.setFont(new Font("Tahoma", Font.BOLD, 17));
		 lblTotalCustomerCountvalue.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalCustomerCountvalue.setBounds(10, 51, 176, 31);
		 lblTotalCustomerCountvalue.setBorder(br);
		 panel_2.add(lblTotalCustomerCountvalue);
		 
		 JLabel lblTotalNoOf = new JLabel("Total No Of Items");
		 lblTotalNoOf.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalNoOf.setBounds(184, 22, 176, 31);
		 lblTotalNoOf.setBorder(br);
		 panel_2.add(lblTotalNoOf);
		 
		 lblTotalNoOfValue = new JLabel("");
		 lblTotalNoOfValue.setFont(new Font("Tahoma", Font.BOLD, 17));
		 lblTotalNoOfValue.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalNoOfValue.setBounds(184, 51, 176, 31);
		 lblTotalNoOfValue.setBorder(br);
		 panel_2.add(lblTotalNoOfValue);
		 
		 JLabel lblTotalQuantity = new JLabel("Total Quantity");
		 lblTotalQuantity.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalQuantity.setBounds(358, 22, 176, 31);
		 lblTotalQuantity.setBorder(br);
		 panel_2.add(lblTotalQuantity);
		 
		 lblTotalQuantityValue = new JLabel("");
		 lblTotalQuantityValue.setFont(new Font("Tahoma", Font.BOLD, 17));
		 lblTotalQuantityValue.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalQuantityValue.setBounds(358, 51, 176, 31);
		 lblTotalQuantityValue.setBorder(br);
		 panel_2.add(lblTotalQuantityValue);
		 
		 JLabel lblTotalDiscountAmount = new JLabel("Total Discount Amount");
		 lblTotalDiscountAmount.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalDiscountAmount.setBounds(532, 22, 176, 31);
		 lblTotalDiscountAmount.setBorder(br);
		 panel_2.add(lblTotalDiscountAmount);
		 
		 lblTotalDiscountAmountValue = new JLabel("");
		 lblTotalDiscountAmountValue.setFont(new Font("Tahoma", Font.BOLD, 17));
		 lblTotalDiscountAmountValue.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalDiscountAmountValue.setBounds(532, 51, 176, 31);
		 lblTotalDiscountAmountValue.setBorder(br);
		 panel_2.add(lblTotalDiscountAmountValue);
		 
		 JLabel lblPendingPayment = new JLabel("Pending Payment");
		 lblPendingPayment.setHorizontalAlignment(SwingConstants.CENTER);
		 lblPendingPayment.setBounds(706, 22, 216, 31);
		 lblPendingPayment.setBorder(br);
		 panel_2.add(lblPendingPayment);
		 
		 lblPendingPaymentValue = new JLabel("");
		 lblPendingPaymentValue.setFont(new Font("Tahoma", Font.BOLD, 17));
		 lblPendingPaymentValue.setForeground(Color.RED);
		 lblPendingPaymentValue.setHorizontalAlignment(SwingConstants.CENTER);
		 lblPendingPaymentValue.setBounds(706, 51, 216, 31);
		 lblPendingPaymentValue.setBorder(br);
		 panel_2.add(lblPendingPaymentValue);
		 
		 JLabel lblCashPayment = new JLabel("Cash Payment");
		 lblCashPayment.setHorizontalAlignment(SwingConstants.CENTER);
		 lblCashPayment.setBounds(920, 22, 206, 31);
		 lblCashPayment.setBorder(br);
		 panel_2.add(lblCashPayment);
		 
		 lblCashPaymentValue = new JLabel("");
		 lblCashPaymentValue.setFont(new Font("Tahoma", Font.BOLD, 17));
		 lblCashPaymentValue.setForeground(Color.BLUE);
		 lblCashPaymentValue.setHorizontalAlignment(SwingConstants.CENTER);
		 lblCashPaymentValue.setBounds(920, 51, 206, 31);
		 lblCashPaymentValue.setBorder(br);
		 panel_2.add(lblCashPaymentValue);
		 
		 JLabel lblTotalAmount = new JLabel("Total Amount");
		 lblTotalAmount.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalAmount.setBounds(532, 80, 176, 31);
		 lblTotalAmount.setBorder(br);
		 panel_2.add(lblTotalAmount);
		 
		 lblTotalAmountValue = new JLabel("");
		 lblTotalAmountValue.setFont(new Font("Tahoma", Font.BOLD, 20));
		 lblTotalAmountValue.setForeground(Color.BLACK);
		 lblTotalAmountValue.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalAmountValue.setBounds(706, 80, 420, 31);
		 lblTotalAmountValue.setBorder(br);
		 panel_2.add(lblTotalAmountValue);
		 
		 //table.getColumnModel().getColumn(0).setPreferredWidth(110);
		 table.getColumnModel().getColumn(1).setPreferredWidth(130);
		// table.getColumnModel().getColumn(2).setPreferredWidth(120);
		 //table.getColumnModel().getColumn(3).setPreferredWidth(100);
		// table.getColumnModel().getColumn(4).setPreferredWidth(100);
		 table.getColumnModel().getColumn(5).setPreferredWidth(170);
		 table.getColumnModel().getColumn(6).setPreferredWidth(100);
		 table.getColumnModel().getColumn(7).setPreferredWidth(150);
		 //table.getColumnModel().getColumn(8).setPreferredWidth(120);

		 ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
	
	//Fill Report Table
	private void fillReportTable(){
		List<BillDetails> billList= ProductServices.getBillDetails(fromDateChooser.getDate()==null?null:new java.sql.Date(fromDateChooser.getDate().getTime()),toDateChooser.getDate()==null?null:new java.sql.Date(toDateChooser.getDate().getTime()),tf_CustMobile.getText().equals("")?null:Long.valueOf(tf_CustMobile.getText()));
		calculateConsolidateValues(billList);
		reportModel.setRowCount(0);
		if(billList.isEmpty()){
			JOptionPane.showMessageDialog(getContentPane(), "No Bills found for the given period !");
		}else{
			for(BillDetails b : billList){
				
				reportModel.addRow(new Object[]{b.getBillNumber(),b.getCustomerMobileNo(),b.getNoOfItems(),b.getTotalQuanity(),b.getPaymentMode(),sdf.format(b.getTimestamp()),PDFUtils.getDecimalFormat(b.getDiscountAmt()),PDFUtils.getDecimalFormat(b.getNetSalesAmt()),"View Bill"});
			}
		}
	}

	private void calculateConsolidateValues(List<BillDetails> billList) {
		billsMap = new HashMap<Integer, BillDetails>();
		totalCustomerCount =0;
		totalDiscAmt=0;
		totalNoOfItems=0;
		totalQty=0;
		cashPayment=0;
		pendingPayment=0;
		totalAmount=0;
		
		for(BillDetails bill: billList){
			totalCustomerCount=billList.size();
			totalDiscAmt+=bill.getDiscountAmt();
			totalNoOfItems+=bill.getNoOfItems();
			totalQty+=bill.getTotalQuanity();
			if("CASH".equals(bill.getPaymentMode())){
				cashPayment+=bill.getNetSalesAmt();
			}else if("PENDING".equals(bill.getPaymentMode())){
				pendingPayment+=bill.getNetSalesAmt();
			}
			totalAmount+=bill.getNetSalesAmt();
			billsMap.put(bill.getBillNumber(), bill);
		}
	}
	
	private void setReportValues() {
		lblTotalCustomerCountvalue.setText(String.valueOf(totalCustomerCount));
		lblTotalNoOfValue.setText(String.valueOf(totalNoOfItems));
		lblTotalQuantityValue.setText(String.valueOf(totalQty));
		lblTotalDiscountAmountValue.setText(PDFUtils.getDecimalFormat(totalDiscAmt));
		lblPendingPaymentValue.setText(PDFUtils.getDecimalFormat(pendingPayment));
		lblCashPaymentValue.setText(PDFUtils.getDecimalFormat(cashPayment));
		lblTotalAmountValue.setText("Rs. "+PDFUtils.getDecimalFormat(totalAmount)+" /-");
	}
}
