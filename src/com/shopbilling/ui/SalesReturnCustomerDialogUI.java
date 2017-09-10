package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import com.shopbilling.dto.Customer;
import com.shopbilling.services.AutoSuggestTable;
import com.shopbilling.services.ButtonColumn;
import com.shopbilling.services.ProductServices;
import com.shopbilling.services.UserServices;
import com.shopbilling.utils.PDFUtils;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class SalesReturnCustomerDialogUI extends JDialog {

	private JPanel contentPane;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
	private JTextField tf_CustMobile;
	private JTable table;
	private DefaultTableModel reportModel;
	private JDateChooser fromDateChooser;
	private JDateChooser toDateChooser;
	private HashMap<String, Long> customerMap;
	private Integer billNumber;

	/**
	 * Create the frame.
	 */
	public SalesReturnCustomerDialogUI(JFrame frame) {
		super(frame,"Select Bill by Customer",true);
		setTitle("Select Bill by Customer");
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
		
		JLabel label = new JLabel("From Date :");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setFont(new Font("Tahoma", Font.BOLD, 12));
		label.setBounds(10, 19, 99, 28);
		panel.add(label);
		
		JLabel label_1 = new JLabel("To Date :");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_1.setBounds(262, 19, 79, 28);
		panel.add(label_1);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillReportTable();
			}
		});
		btnSearch.setIcon(new ImageIcon(SalesReturnCustomerDialogUI.class.getResource("/images/search.png")));
		btnSearch.setBounds(936, 15, 127, 34);
		panel.add(btnSearch);
		//Get Allowed minimum days for Sales Return
		String allowedDays = PDFUtils.getAppDataValues(AppConstants.SALES_RETURN_ALLOWED_DAYS).get(0);
		DateTime dateTime = new DateTime();
		Date backDate = dateTime.minusDays(Integer.valueOf(allowedDays)).toDate();
		
		fromDateChooser = new JDateChooser();
		fromDateChooser.setFont(new Font("Tahoma", Font.BOLD, 12));
		fromDateChooser.setBounds(119, 19, 133, 28);
		fromDateChooser.setDate(new Date());
		fromDateChooser.getJCalendar().setMinSelectableDate(backDate);
		fromDateChooser.getJCalendar().setMaxSelectableDate(new Date());
		JTextFieldDateEditor editor = (JTextFieldDateEditor) fromDateChooser.getDateEditor();
		editor.setEditable(false);
		panel.add(fromDateChooser);
		
		toDateChooser = new JDateChooser();
		toDateChooser.setFont(new Font("Tahoma", Font.BOLD, 12));
		toDateChooser.setBounds(351, 19, 133, 28);
		toDateChooser.setDate(new Date());
		toDateChooser.getJCalendar().setMaxSelectableDate(new Date());	
		toDateChooser.getJCalendar().setMinSelectableDate(backDate);
		JTextFieldDateEditor toEditor = (JTextFieldDateEditor) toDateChooser.getDateEditor();
		toEditor.setEditable(false);
		panel.add(toDateChooser);
		
		JLabel label_2 = new JLabel("Select Customer :");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_2.setBounds(521, 19, 119, 28);
		panel.add(label_2);
		
		tf_CustMobile = new AutoSuggestTable(getCustomerNameList());
		tf_CustMobile.setBounds(645, 18, 268, 25);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		tf_CustMobile.setBorder(border);
		tf_CustMobile.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(tf_CustMobile);
		tf_CustMobile.setColumns(10);
		
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
			List<BillDetails> billList= ProductServices.getBillDetails(fromDateChooser.getDate()==null?null:new java.sql.Date(fromDateChooser.getDate().getTime()),toDateChooser.getDate()==null?null:new java.sql.Date(toDateChooser.getDate().getTime()),tf_CustMobile.getText().equals("")?null:Long.valueOf(customerMap.get(tf_CustMobile.getText())));
			reportModel.setRowCount(0);
			if(billList.isEmpty()){
				JOptionPane.showMessageDialog(getContentPane(), "No Bills found for the given criteria !");
			}else{
				for(BillDetails b : billList){
					
					reportModel.addRow(new Object[]{b.getBillNumber(),b.getCustomerMobileNo(),b.getNoOfItems(),b.getTotalQuanity(),b.getPaymentMode(),sdf.format(b.getTimestamp()),PDFUtils.getDecimalFormat(b.getDiscountAmt()),PDFUtils.getDecimalFormat(b.getNetSalesAmt()),"Select Bill"});
				}
			}
		}

		//Customer List
		public List<String> getCustomerNameList(){
			
			List<String> customerNameList = new ArrayList<String>();
			customerMap = new HashMap<String, Long>();
			for(Customer cust :UserServices.getAllCustomers()){
				customerNameList.add(cust.getCustName()+" : "+cust.getCustMobileNumber());
				customerMap.put(cust.getCustName()+" : "+cust.getCustMobileNumber(),cust.getCustMobileNumber());
			}
			return customerNameList;
		}

		public Integer getBillNumber() {
			return billNumber;
		}

		public void setBillNumber(Integer billNumber) {
			this.billNumber = billNumber;
		}
}
