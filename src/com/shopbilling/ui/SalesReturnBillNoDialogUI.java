package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.swing.AbstractAction;
import javax.swing.Action;
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
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.joda.time.DateTime;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.BillDetails;
import com.shopbilling.services.ButtonColumn;
import com.shopbilling.services.ProductServices;
import com.shopbilling.utils.PDFUtils;

public class SalesReturnBillNoDialogUI extends JDialog {

	private JPanel contentPane;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
	private JTextField tf_BillNo;
	private JTable table;
	private DefaultTableModel reportModel;
	private Integer billNumber;

	/**
	 * Create the frame.
	 */
	public SalesReturnBillNoDialogUI(JFrame frame) {
		super(frame,"Select Bill by Bill Number",true);
		setTitle("Select Bill by Bill Number");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("shop32X32.png"));
		setBounds(150, 100, 1110, 477);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Search Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 1084, 60);
		contentPane.add(panel);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillReportTable();
			}
		});
		btnSearch.setIcon(new ImageIcon(SalesReturnCustomerDialogUI.class.getResource("/images/search.png")));
		btnSearch.setBounds(693, 14, 127, 34);
		panel.add(btnSearch);
		
		JLabel lblBillNumber = new JLabel("Bill Number *:");
		lblBillNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBillNumber.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblBillNumber.setBounds(251, 16, 119, 25);
		panel.add(lblBillNumber);
		
		tf_BillNo = new JTextField();
		tf_BillNo.setBounds(375, 15, 225, 25);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		tf_BillNo.setBorder(border);
		tf_BillNo.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(tf_BillNo);
		tf_BillNo.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 82, 1084, 355);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 31, 1064, 313);
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
				 "Bill Number", "Customer Mobile", "No of Items", "Quantity", "Pay Mode","Bill Date","Discount Amt","Net Sales Amount","Select Bill"}
	       );
		table.setModel(reportModel);
		PDFUtils.setTableRowHeight(table);
		scrollPane.setViewportView(table);
		 table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 
		 Action viewBillAction = new AbstractAction()
		 {
		     public void actionPerformed(ActionEvent e)
		     {
		    	 JTable table = (JTable)e.getSource();
       		int modelRow = Integer.valueOf( e.getActionCommand() );
       		int billNo = Integer.valueOf(table.getModel().getValueAt(modelRow, 0).toString());
		    	setBillNumber(billNo);
		    	dispose();
		    }
		 };

		 ButtonColumn buttonColumn = new ButtonColumn(table, viewBillAction, 8);
		 buttonColumn.setMnemonic(KeyEvent.VK_D);
		 
		 
		 table.getColumnModel().getColumn(0).setPreferredWidth(100);
		 table.getColumnModel().getColumn(1).setPreferredWidth(130);
		 table.getColumnModel().getColumn(2).setPreferredWidth(110);
		 table.getColumnModel().getColumn(3).setPreferredWidth(100);
		 table.getColumnModel().getColumn(4).setPreferredWidth(100);
		 table.getColumnModel().getColumn(5).setPreferredWidth(170);
		 table.getColumnModel().getColumn(6).setPreferredWidth(100);
		 table.getColumnModel().getColumn(7).setPreferredWidth(130);
		 table.getColumnModel().getColumn(8).setPreferredWidth(120);
		
	}
	
	//Fill Report Table
	private void fillReportTable(){
		if(PDFUtils.isMandatoryEntered(tf_BillNo)){
			//Get Allowed minimum days for Sales Return
			String allowedDays = PDFUtils.getAppDataValues(AppConstants.SALES_RETURN_ALLOWED_DAYS).get(0);
			DateTime dateTime = new DateTime();
			java.util.Date backDate = dateTime.minusDays(Integer.valueOf(allowedDays)).toDate();
			
			Date toDate = new Date(System.currentTimeMillis());
			Date fromDate = new Date(backDate.getTime());
			System.out.println("toDate "+toDate);
			System.out.println("fromDate "+fromDate);
			
			BillDetails bill= ProductServices.getBillDetailsOfBillNumberWithinDateRange(Integer.valueOf(tf_BillNo.getText()),fromDate,toDate);
			reportModel.setRowCount(0);
			if(bill==null){
				JOptionPane.showMessageDialog(getContentPane(), "Invalid Bill number / Given Bill Number not within sales return days policy");
			}else{
					reportModel.addRow(new Object[]{bill.getBillNumber(),bill.getCustomerMobileNo(),bill.getNoOfItems(),bill.getTotalQuanity(),bill.getPaymentMode(),sdf.format(bill.getTimestamp()),PDFUtils.getDecimalFormat(bill.getDiscountAmt()),PDFUtils.getDecimalFormat(bill.getNetSalesAmt()),"Select Bill"});
			}	
		}else{
			JOptionPane.showMessageDialog(getContentPane(), "Please enter Bill Number");
		}
		
	}

	public Integer getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(Integer billNumber) {
		this.billNumber = billNumber;
	}
}
