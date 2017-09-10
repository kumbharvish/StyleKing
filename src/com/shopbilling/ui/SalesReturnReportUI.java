package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.List;

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

import com.shopbilling.dto.ReturnDetails;
import com.shopbilling.services.ButtonColumn;
import com.shopbilling.services.SalesReturnServices;
import com.shopbilling.utils.PDFUtils;
import com.toedter.calendar.JDateChooser;

public class SalesReturnReportUI extends JInternalFrame {
	private JTable table;
	private DefaultTableModel reportModel;
	private JDateChooser toDateChooser;
	private JDateChooser fromDateChooser;
	private int totalReturnCount=0;
	private int totalNoOfItems=0;
	private int totalQty=0;
	private double totalAmount=0;
	private double totalCashPayment=0;
	private double totalPendingPayment=0;
	//
	private JLabel lblTotalCustomerCountvalue;
	private JLabel lblTotalNoOfValue;
	private JLabel lblTotalQuantityValue;
	private JLabel lblTotalPaymentValue;
	private JFrame parentFrame;
	private JTextField tf_CustMobile;
	private  JLabel lblCashPaymentValue;
	private  JLabel lblpendingPaymentValue ;
	/**
	 * Create the frame.
	 */
	public SalesReturnReportUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Sales Return Report");
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
		tf_CustMobile.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_CustMobile.setBounds(720, 19, 193, 28);
		panel.add(tf_CustMobile);
		tf_CustMobile.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Return Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 82, 1150, 356);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 26, 1120, 319);
		panel_1.add(scrollPane);
		
		table = new JTable();
		reportModel = new DefaultTableModel(){
			 boolean[] columnEditables = new boolean[] {
					 false, false, false, false,false,false,false,false,false,true
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
		 reportModel.setColumnIdentifiers(new String[] {
				 "Return Number", "Customer Mobile","Customer Name", "No of Items", "Quantity", "Bill Date","Return Date","Bill Net Sales Amount","Return Amount","Return Details"}
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
        		int returnNumber = Integer.valueOf(table.getModel().getValueAt(modelRow, 0).toString());
		    	 //Open View Bill Screen
        			ViewSalesReturnUI vb = new ViewSalesReturnUI(returnNumber,parentFrame);
    		    	 vb.setVisible(true);
		     }
		 };

		 ButtonColumn buttonColumn = new ButtonColumn(table, viewBillAction, 9);
		 buttonColumn.setMnemonic(KeyEvent.VK_D);
		 
		 JPanel panel_2 = new JPanel();
		 panel_2.setBorder(new TitledBorder(null, "Consolidated Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		 panel_2.setBounds(10, 449, 1150, 130);
		 getContentPane().add(panel_2);
		 panel_2.setLayout(null);
		 Border br = new LineBorder(Color.black, 2);
		 JLabel lblTotalCustomerCount = new JLabel("Total Return Count");
		 lblTotalCustomerCount.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalCustomerCount.setBounds(10, 22, 190, 31);
		 lblTotalCustomerCount.setBorder(br);
		 panel_2.add(lblTotalCustomerCount);
		 
		 lblTotalCustomerCountvalue = new JLabel("");
		 lblTotalCustomerCountvalue.setFont(new Font("Tahoma", Font.BOLD, 17));
		 lblTotalCustomerCountvalue.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalCustomerCountvalue.setBounds(10, 51, 190, 31);
		 lblTotalCustomerCountvalue.setBorder(br);
		 panel_2.add(lblTotalCustomerCountvalue);
		 
		 JLabel lblTotalNoOf = new JLabel("Total No Of Items");
		 lblTotalNoOf.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalNoOf.setBounds(198, 22, 218, 31);
		 lblTotalNoOf.setBorder(br);
		 panel_2.add(lblTotalNoOf);
		 
		 lblTotalNoOfValue = new JLabel("");
		 lblTotalNoOfValue.setFont(new Font("Tahoma", Font.BOLD, 17));
		 lblTotalNoOfValue.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalNoOfValue.setBounds(198, 51, 218, 31);
		 lblTotalNoOfValue.setBorder(br);
		 panel_2.add(lblTotalNoOfValue);
		 
		 JLabel lblTotalQuantity = new JLabel("Total Quantity");
		 lblTotalQuantity.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalQuantity.setBounds(414, 22, 226, 31);
		 lblTotalQuantity.setBorder(br);
		 panel_2.add(lblTotalQuantity);
		 
		 lblTotalQuantityValue = new JLabel("");
		 lblTotalQuantityValue.setFont(new Font("Tahoma", Font.BOLD, 17));
		 lblTotalQuantityValue.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalQuantityValue.setBounds(414, 51, 226, 31);
		 lblTotalQuantityValue.setBorder(br);
		 panel_2.add(lblTotalQuantityValue);
		 
		 JLabel lblCashPayment = new JLabel("Cash Payment");
		 lblCashPayment.setHorizontalAlignment(SwingConstants.CENTER);
		 lblCashPayment.setBounds(878, 22, 248, 31);
		 lblCashPayment.setBorder(br);
		 panel_2.add(lblCashPayment);
		 
		 lblTotalPaymentValue = new JLabel("");
		 lblTotalPaymentValue.setFont(new Font("Tahoma", Font.BOLD, 17));
		 lblTotalPaymentValue.setForeground(Color.BLACK);
		 lblTotalPaymentValue.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalPaymentValue.setBounds(638, 80, 488, 31);
		 lblTotalPaymentValue.setBorder(br);
		 panel_2.add(lblTotalPaymentValue);
		 
		 JLabel lblPending = new JLabel("Pending Payment");
		 lblPending.setHorizontalAlignment(SwingConstants.CENTER);
		 lblPending.setBounds(638, 22, 242, 31);
		 lblPending.setBorder(br);
		 panel_2.add(lblPending);
		 
		 lblpendingPaymentValue = new JLabel("");
		 lblpendingPaymentValue.setHorizontalAlignment(SwingConstants.CENTER);
		 lblpendingPaymentValue.setFont(new Font("Tahoma", Font.BOLD, 17));
		 lblpendingPaymentValue.setBounds(638, 51, 242, 31);
		 lblpendingPaymentValue.setBorder(br);
		 panel_2.add(lblpendingPaymentValue);
		 
		 lblCashPaymentValue = new JLabel("");
		 lblCashPaymentValue.setHorizontalAlignment(SwingConstants.CENTER);
		 lblCashPaymentValue.setFont(new Font("Tahoma", Font.BOLD, 17));
		 lblCashPaymentValue.setBounds(878, 51, 248, 31);
		 lblCashPaymentValue.setBorder(br);
		 panel_2.add(lblCashPaymentValue);
		 
		 JLabel lblTotal = new JLabel("Total Amount");
		 lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotal.setBounds(414, 80, 226, 31);
		 lblTotal.setBorder(br);
		 panel_2.add(lblTotal);
		 
		 table.getColumnModel().getColumn(0).setPreferredWidth(110);
		 table.getColumnModel().getColumn(1).setPreferredWidth(130);
		 table.getColumnModel().getColumn(2).setPreferredWidth(120);
		 table.getColumnModel().getColumn(3).setPreferredWidth(100);
		 table.getColumnModel().getColumn(4).setPreferredWidth(100);
		 table.getColumnModel().getColumn(5).setPreferredWidth(100);
		 table.getColumnModel().getColumn(6).setPreferredWidth(170);
		 table.getColumnModel().getColumn(7).setPreferredWidth(150);
		 table.getColumnModel().getColumn(8).setPreferredWidth(120);
		 table.getColumnModel().getColumn(9).setPreferredWidth(120);

		 ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
	
	//Fill Report Table
	private void fillReportTable(){
		List<ReturnDetails> returnList= SalesReturnServices.getReturnDetails(fromDateChooser.getDate()==null?null:new java.sql.Date(fromDateChooser.getDate().getTime()),toDateChooser.getDate()==null?null:new java.sql.Date(toDateChooser.getDate().getTime()),tf_CustMobile.getText().equals("")?null:Long.valueOf(tf_CustMobile.getText()));
		calculateConsolidateValues(returnList);
		reportModel.setRowCount(0);
		if(returnList.isEmpty()){
			JOptionPane.showMessageDialog(getContentPane(), "No Returns found for the given period !");
		}else{
			for(ReturnDetails b : returnList){
				reportModel.addRow(new Object[]{b.getReturnNumber(),b.getCustomerMobileNo(),b.getCustomerName(),b.getNoOfItems(),b.getTotalQuanity(),PDFUtils.getFormattedDate(b.getTimestamp()),PDFUtils.getFormattedDateWithTime(b.getTimestamp()),PDFUtils.getDecimalFormat(b.getBillNetSalesAmt()),PDFUtils.getDecimalFormat(b.getTotalAmount()),"View Return"});
			}
		}
	}

	private void calculateConsolidateValues(List<ReturnDetails> returnList) {
		totalReturnCount =0;
		totalNoOfItems=0;
		totalQty=0;
		totalAmount=0;
		totalCashPayment=0;
		totalPendingPayment=0;
		
		for(ReturnDetails bill: returnList){
			totalReturnCount=returnList.size();
			totalNoOfItems+=bill.getNoOfItems();
			totalQty+=bill.getTotalQuanity();
			totalAmount+=bill.getTotalAmount();
			if("CASH".equals(bill.getReturnpaymentMode())){
				totalCashPayment+=bill.getTotalAmount();
			}else if("PENDING".equals(bill.getReturnpaymentMode())){
				totalPendingPayment+=bill.getTotalAmount();
			}
		}
	}
	
	private void setReportValues() {
		lblTotalCustomerCountvalue.setText(String.valueOf(totalReturnCount));
		lblTotalNoOfValue.setText(String.valueOf(totalNoOfItems));
		lblTotalQuantityValue.setText(String.valueOf(totalQty));
		lblTotalPaymentValue.setText("Rs. "+PDFUtils.getDecimalFormat(totalAmount)+" /-");
		lblCashPaymentValue.setText(PDFUtils.getDecimalFormat(totalCashPayment));
		lblpendingPaymentValue.setText(PDFUtils.getDecimalFormat(totalPendingPayment));
	}
}
