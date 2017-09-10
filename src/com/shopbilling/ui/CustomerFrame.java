package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.LabelAlignment;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.BillDetails;
import com.shopbilling.dto.Customer;
import com.shopbilling.dto.StatusDTO;
import com.shopbilling.dto.UserDetails;
import com.shopbilling.services.AutoSuggestTable;
import com.shopbilling.services.ButtonColumn;
import com.shopbilling.services.CustomerHistoryServices;
import com.shopbilling.services.UserServices;
import com.shopbilling.utils.PDFUtils;

public class CustomerFrame extends JInternalFrame {

	private JPanel contentPane;
	private UserDetails userDetails;
	private JTable customerTable;
	private JTextField tf_SearchCust;
	private JTextField tf_CustMob;
	private JTextField tf_CustName;
	private JTextField tf_CustCity;
	private JTextField tf_CustEmail;
	private JTextField tf_CustBalance;
	private JTextField tf_AddPendingAmt;
	private JTextField tf_SettleAmt;
	private JRadioButton rb_AddPendingAmt;
	private JRadioButton rb_SettleUpAmt;
	private JRadioButton rb_CustPayment;
	private JRadioButton rb_CustDetails;
	private JPanel panelDisgnGrid;
	private JPanel panelBalance;
	private DefaultTableModel customerModel;
	private JTextField tf_HSearchCust;
	private JTextField tf_HCustName;
	private JTextField tf_HCustMob;
	private JTextField tf_HBalance;
	private JTextField tf_HEntryDt;
	private HashMap<String,Customer> customerMap;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
	private JTable purchaseHistoryTable;
	private JTable paymentHistoryTable;
	private Map<Integer,BillDetails> billsMap;
	private DefaultTableModel purchaseHistModel;
	private DefaultTableModel paymentHistModel;
	private JFrame parentFrame;
	/**
	 * Create the frame.
	 */
	public CustomerFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Manage Customer");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setResizable(false);
		//fix the frame location
		//setUndecorated(true);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(15, 21, 1150, 564);
		
		JPanel panelManage = new JPanel();
		tabbedPane.addTab("Manage Customers", null, panelManage, null);
		panelManage.setLayout(null);
		parentFrame = (JFrame)this.getTopLevelAncestor();
		JPanel panelCustDetails = new JPanel();
		panelCustDetails.setBorder(new TitledBorder(null, "Customer Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelCustDetails.setBounds(37, 11, 507, 249);
		panelManage.add(panelCustDetails);
		panelCustDetails.setLayout(null);
		
		panelDisgnGrid = new JPanel();
		DesignGridLayout layout = new DesignGridLayout(panelDisgnGrid);
		layout.labelAlignment(LabelAlignment.RIGHT);
		tf_CustMob = new JTextField();
		//Only Numbers allowed to enter
		tf_CustMob.addKeyListener(new KeyAdapter() {
			   public void keyTyped(KeyEvent e) {
			      char c = e.getKeyChar();
			      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
			         e.consume();  // ignore event
			      }
			   }
			});
		tf_CustName = new JTextField();
		tf_CustCity = new JTextField();
		tf_CustEmail = new JTextField();
		JButton deleteButton = new JButton("Delete");
 		JButton updateButton = new JButton("Update");
 		JButton saveButton = new JButton("Add");
 		JButton resetButton = new JButton("Reset");
 		Font noteFont = new Font("Dialog", Font.PLAIN, 12);
 		layout.row().grid(new JLabel("Mobile *:"))	.add(tf_CustMob);
		layout.row().grid(new JLabel("Name *:"))	.add(tf_CustName);
		layout.row().grid(new JLabel("City :"))	.add(tf_CustCity);
		layout.row().grid(new JLabel("Email :"))	.add(tf_CustEmail);
		layout.row().grid(new JLabel(" "));
		layout.emptyRow();
 		layout.row().right().add(saveButton).add(updateButton).add(deleteButton).add(resetButton);
 		layout.emptyRow();
 		JLabel note = new JLabel("Note :");
 		JLabel noteHeading = new JLabel("Customer Mobile update not allowed");
 		note.setFont(noteFont);
 		noteHeading.setFont(noteFont);
 		layout.row().grid(note).add(noteHeading);
		panelDisgnGrid.setBounds(73, 25, 360, 213);
		panelCustDetails.add(panelDisgnGrid);
		
		JPanel panelCustBalance = new JPanel();
		panelCustBalance.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Payment Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelCustBalance.setBounds(37, 271, 507, 254);
		panelManage.add(panelCustBalance);
		panelCustBalance.setLayout(null);
		
		panelBalance = new JPanel();
		panelBalance.setBounds(71, 29, 360, 214);
		panelCustBalance.add(panelBalance);
		panelBalance.setLayout(null);
		
		JLabel lblCustomerBalance = new JLabel("Balance : ");
		lblCustomerBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCustomerBalance.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblCustomerBalance.setBounds(42, 30, 102, 25);
		panelBalance.add(lblCustomerBalance);
		
		tf_CustBalance = new JTextField();
		tf_CustBalance.setBounds(180, 31, 139, 25);
		panelBalance.add(tf_CustBalance);
		tf_CustBalance.setColumns(10);
		tf_CustBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_CustBalance.setFont(new Font("Dialog", Font.BOLD, 20));
		tf_CustBalance.setEditable(false);
		tf_CustBalance.setForeground(Color.WHITE);
		tf_CustBalance.setBackground(Color.GRAY);
		tf_CustBalance.setText("0.00");
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(CustomerFrame.class.getResource("/images/currency_sign_rupee.png")));
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setFont(new Font("Tahoma", Font.BOLD, 13));
		label.setBounds(138, 30, 36, 25);
		panelBalance.add(label);
		
		rb_AddPendingAmt = new JRadioButton("Add Pending Amount");
		rb_AddPendingAmt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enableDisableTF();
			}
		});
		rb_AddPendingAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		rb_AddPendingAmt.setBounds(26, 90, 148, 23);
		panelBalance.add(rb_AddPendingAmt);
		
		rb_SettleUpAmt = new JRadioButton("Settle Up Amount");
		rb_SettleUpAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		rb_SettleUpAmt.setBounds(26, 131, 148, 23);
		rb_SettleUpAmt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enableDisableTF();
			}
		});
		panelBalance.add(rb_SettleUpAmt);
		ButtonGroup bg=new ButtonGroup();
		bg.add(rb_AddPendingAmt);
		bg.add(rb_SettleUpAmt);
		tf_AddPendingAmt = new JTextField();
		tf_AddPendingAmt.setText("0.00");
		tf_AddPendingAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_AddPendingAmt.setForeground(Color.WHITE);
		tf_AddPendingAmt.setFont(new Font("Dialog", Font.BOLD, 20));
		tf_AddPendingAmt.setColumns(10);
		tf_AddPendingAmt.setBackground(Color.GRAY);
		tf_AddPendingAmt.setBounds(180, 91, 139, 25);
		
		tf_AddPendingAmt.addKeyListener(new KeyAdapter() {
			   public void keyTyped(KeyEvent e) {
			      char c = e.getKeyChar();
			      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
			         e.consume();  // ignore event
			      }
			   }
			});
		
		panelBalance.add(tf_AddPendingAmt);
		
		tf_SettleAmt = new JTextField();
		tf_SettleAmt.setText("0.00");
		tf_SettleAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_SettleAmt.setForeground(Color.WHITE);
		tf_SettleAmt.setFont(new Font("Dialog", Font.BOLD, 20));
		tf_SettleAmt.setColumns(10);
		tf_SettleAmt.setBackground(Color.GRAY);
		tf_SettleAmt.setBounds(180, 132, 139, 25);
		
		tf_SettleAmt.addKeyListener(new KeyAdapter() {
			   public void keyTyped(KeyEvent e) {
			      char c = e.getKeyChar();
			      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
			         e.consume();  // ignore event
			      }
			   }
			});
		panelBalance.add(tf_SettleAmt);
		JButton btnUpdatePayment = new JButton("Update Payment");
		//Update Payment Button Action
		btnUpdatePayment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!rb_AddPendingAmt.isSelected()&& !rb_SettleUpAmt.isSelected()){
					JOptionPane.showMessageDialog(contentPane, "Please select the one of the option to proceed !");
				}else{
					if(!tf_CustMob.isEnabled()){
						if(rb_AddPendingAmt.isSelected()){
							if(PDFUtils.isMandatoryEntered(tf_AddPendingAmt)&& Double.valueOf(tf_AddPendingAmt.getText())>0){
								StatusDTO status = 	UserServices.addPendingPaymentToCustomer(Long.valueOf(tf_CustMob.getText()), Double.valueOf(tf_AddPendingAmt.getText()));
								if(status.getStatusCode()==0){
								JOptionPane.showMessageDialog(contentPane, "Add Payment Succussful !");
								fillCustomerTableData();
								setUpdatedCustBalance(Long.valueOf(tf_CustMob.getText()));
								Customer customer = new Customer();
								customer.setCustMobileNumber(Long.valueOf(tf_CustMob.getText()));
								customer.setCustName(tf_CustName.getText());
								customer.setAmount(Double.valueOf(tf_AddPendingAmt.getText()));
								customer.setHistoryFlag("ADD");
								UserServices.addCustomerPaymentHistory(customer.getCustMobileNumber(),customer.getAmount(),0,AppConstants.CREDIT,"Added Pending Payment by : "+userDetails.getFirstName()+" "+userDetails.getLastName());
								tf_AddPendingAmt.setText("0.00");
							}else{
								JOptionPane.showMessageDialog(contentPane,"Error occured while adding payment","Error",JOptionPane.WARNING_MESSAGE);
							}
							}else{
								JOptionPane.showMessageDialog(contentPane, "Amount should be greater than Zero (0)");
							}
							
						}else if(rb_SettleUpAmt.isSelected()){
							if(PDFUtils.isMandatoryEntered(tf_SettleAmt)&& Double.valueOf(tf_SettleAmt.getText())>0 && Double.valueOf(tf_SettleAmt.getText())<= Double.valueOf(tf_CustBalance.getText())){
								StatusDTO status = 	UserServices.settleUpCustomerBalance(Long.valueOf(tf_CustMob.getText()), Double.valueOf(tf_SettleAmt.getText()));
								if(status.getStatusCode()==0){
									JOptionPane.showMessageDialog(contentPane, "Settle Payment Succussful !");
									fillCustomerTableData();
									setUpdatedCustBalance(Long.valueOf(tf_CustMob.getText()));
									Customer customerSt = new Customer();
									customerSt.setCustMobileNumber(Long.valueOf(tf_CustMob.getText()));
									customerSt.setCustName(tf_CustName.getText());
									customerSt.setAmount(Double.valueOf(tf_SettleAmt.getText()));
									customerSt.setHistoryFlag("SETTLE");
									UserServices.addCustomerPaymentHistory(customerSt.getCustMobileNumber(),0,customerSt.getAmount(),AppConstants.DEBIT,"Added Pending Payment by : "+userDetails.getFirstName()+" "+userDetails.getLastName());
									tf_SettleAmt.setText("0.00");
								}else{
									JOptionPane.showMessageDialog(contentPane,"Error occured while adding payment","Error",JOptionPane.WARNING_MESSAGE);
								}
								}else{
									JOptionPane.showMessageDialog(contentPane, "Amount should be greater than Zero (0) and Less than current Balance");
								}
						}
					}else{
						JOptionPane.showMessageDialog(contentPane, "Please select customer !");
					}
				}
				
			}
		});
		btnUpdatePayment.setBounds(138, 180, 148, 23);
		panelBalance.add(btnUpdatePayment);
		
		JPanel panelCustomers = new JPanel();
		panelCustomers.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Customers", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelCustomers.setBounds(560, 11, 570, 514);
		panelManage.add(panelCustomers);
		panelCustomers.setLayout(null);
		
		JScrollPane CustomerScrollPane = new JScrollPane();
		CustomerScrollPane.setBounds(18, 62, 534, 441);
		panelCustomers.add(CustomerScrollPane);
		
		customerTable = new JTable();
		customerModel = new DefaultTableModel(){
			 boolean[] columnEditables = new boolean[] {
					 false, false, false,false,false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
		 
		 customerModel.setColumnIdentifiers(new String[] {
				 "Mobile Number", "Name", "Balance Amount","City","Email"}
	       );
		 customerTable.setModel(customerModel);
		 JTableHeader header3 = customerTable.getTableHeader();
		 header3.setFont(new Font("Dialog", Font.BOLD, 12));
		 customerTable.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int row = customerTable.getSelectedRow();
		        	tf_CustMob.setText(customerTable.getModel().getValueAt(row, 0).toString());
					tf_CustName.setText(customerTable.getModel().getValueAt(row, 1).toString());
					tf_CustBalance.setText(customerTable.getModel().getValueAt(row, 2).toString());
					tf_CustCity.setText(customerTable.getModel().getValueAt(row, 3)!=null ?customerTable.getModel().getValueAt(row, 3).toString():"");
					tf_CustEmail.setText(customerTable.getModel().getValueAt(row, 4)!=null?customerTable.getModel().getValueAt(row, 4).toString():"");
				}
			});
		 
		customerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		customerTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		customerTable.getColumnModel().getColumn(1).setPreferredWidth(250);
		customerTable.getColumnModel().getColumn(2).setPreferredWidth(130);
		//Hide City and Email Cols
		customerTable.getColumnModel().getColumn(3).setWidth(0);
		customerTable.getColumnModel().getColumn(3).setMinWidth(0);
		customerTable.getColumnModel().getColumn(3).setMaxWidth(0);
		customerTable.getColumnModel().getColumn(4).setWidth(0);
		customerTable.getColumnModel().getColumn(4).setMinWidth(0);
		customerTable.getColumnModel().getColumn(4).setMaxWidth(0);
		
		CustomerScrollPane.setViewportView(customerTable);
		//Fill Table Data on Load
		fillCustomerTableData();
		
		JLabel lblSearchCustomer = new JLabel("Search Customer :");
		lblSearchCustomer.setIcon(new ImageIcon(CustomerFrame.class.getResource("/images/search.png")));
		lblSearchCustomer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSearchCustomer.setBounds(250, 20, 150, 40);
		panelCustomers.add(lblSearchCustomer);
		
		tf_SearchCust = new JTextField();
		tf_SearchCust.setBounds(410, 26, 150, 25);
		panelCustomers.add(tf_SearchCust);
		tf_SearchCust.setColumns(10);
		tf_SearchCust.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				searchCustomerTableData(tf_SearchCust.getText());
			}
		});
		rb_CustPayment = new JRadioButton("");
		rb_CustPayment.setBounds(6, 264, 21, 23);
		panelManage.add(rb_CustPayment);
		
		rb_CustDetails = new JRadioButton("");
		rb_CustDetails.setBounds(6, 6, 21, 23);
		panelManage.add(rb_CustDetails);
		contentPane.setLayout(null);
		JPanel panelCustHistory = new JPanel();
		tabbedPane.addTab("Customer Hisotry", null, panelCustHistory, null);
		panelCustHistory.setLayout(null);
		
		ButtonGroup topBg = new ButtonGroup();
		topBg.add(rb_CustDetails);
		topBg.add(rb_CustPayment);
		
		rb_CustDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enableDisablePanel();	
			}
		});
		rb_CustPayment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enableDisablePanel();
			}
		});
		
		//Disable balance panel by default
			rb_CustDetails.setSelected(true);
			enableDisablePanel();
				
		//Add Customer Action
		
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tf_CustMob.isEnabled()){
					if(PDFUtils.isMandatoryEntered(tf_CustName) && PDFUtils.isMandatoryEntered(tf_CustMob))
					{
					Customer customer = new Customer();
					customer.setCustMobileNumber(Long.valueOf(tf_CustMob.getText()));
					customer.setCustName(tf_CustName.getText());
					customer.setCustCity(tf_CustCity.getText());
					customer.setCustEmail(tf_CustEmail.getText());
					StatusDTO status = UserServices.addCustomer(customer);
					if(status.getStatusCode()==0){
						JOptionPane.showMessageDialog(contentPane, "Customer Added Successfully");
						resetCustomerFields();
						fillCustomerTableData();
					}else{
						if(status.getStatusCode()==-1 && status.getException().contains("Duplicate entry")){
							JOptionPane.showMessageDialog(contentPane,"Customer already exists for Mobile number: "+tf_CustMob.getText(),"Warning",JOptionPane.WARNING_MESSAGE);
						}else{
							JOptionPane.showMessageDialog(contentPane,"Error occured ","Error",JOptionPane.WARNING_MESSAGE);
						}
					}
					
				}else{
					JOptionPane.showMessageDialog(contentPane, "Please Enter Mandatory fields!");
				}
				}else{
					JOptionPane.showMessageDialog(contentPane, "Please Reset fields!");
				}
					
			}
				
		});
		
		//Reset Customer Fields
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetCustomerFields();
				}
				
		});
				
		//Update Button Action
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tf_CustMob.getText().equals("")){
					JOptionPane.showMessageDialog(contentPane, "Please select Customer!");
				}else{
					if(PDFUtils.isMandatoryEntered(tf_CustName))
					{
						Customer customer = new Customer();
						customer.setCustMobileNumber(Long.valueOf(tf_CustMob.getText()));
						customer.setCustName(tf_CustName.getText());
						customer.setCustCity(tf_CustCity.getText());
						customer.setCustEmail(tf_CustEmail.getText());
					
					boolean isCustUpdated = UserServices.updateCustomer(customer);
					if(isCustUpdated){
						JOptionPane.showMessageDialog(contentPane, "Customer Updated Successfully");
						resetCustomerFields();
						fillCustomerTableData();
					}else{
						JOptionPane.showMessageDialog(contentPane,"Customer Mobile does not exists : " +tf_CustMob.getText()+" ","Warning",JOptionPane.WARNING_MESSAGE);
					}
					}else{
						JOptionPane.showMessageDialog(contentPane, "Please Enter Mandatory fields!");
					}
				}
				
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tf_CustMob.getText().equals("")){
					JOptionPane.showMessageDialog(contentPane, "Please select Customer!");
				}else{
					if(Double.valueOf(tf_CustBalance.getText())!=0){
						JOptionPane.showMessageDialog(contentPane,"Please settle up customer balance before delete!","Warning",JOptionPane.WARNING_MESSAGE);
					}else{
						int dialogButton = JOptionPane.YES_NO_OPTION;
						int dialogResult = JOptionPane.showConfirmDialog (contentPane, "Are you sure?","Warning",dialogButton);
						if(dialogResult == JOptionPane.YES_OPTION){
							boolean isCustDeleted = UserServices.deleteCustomer(Long.parseLong(tf_CustMob.getText()));
							if(isCustDeleted){
								JOptionPane.showMessageDialog(contentPane, "Customer Deleted Successfully!");
								resetCustomerFields();
								fillCustomerTableData();
							}else{
								JOptionPane.showMessageDialog(contentPane,"Customer Mobile does not exists : " +tf_CustMob.getText()+" ","Warning",JOptionPane.WARNING_MESSAGE);
							}
						}else{
							resetCustomerFields();
						}
					}
					
				}
				
			}
		});
		
		//Customer History Started
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Customer Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 646, 148);
		panelCustHistory.add(panel);
		panel.setLayout(null);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		JLabel lblHSelectCustomer = new JLabel("Select Customer :");
		lblHSelectCustomer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHSelectCustomer.setBounds(140, 23, 103, 25);
		panel.add(lblHSelectCustomer);
		
		tf_HSearchCust = new AutoSuggestTable<String>(getCustomerNameList());
		tf_HSearchCust.setBounds(250, 23, 229, 25);
		panel.add(tf_HSearchCust);
		tf_HSearchCust.setBorder(border);
		tf_HSearchCust.setFont(new Font("Dialog", Font.BOLD, 13));
		tf_HSearchCust.setColumns(10);
		
		JLabel lblHCustName = new JLabel("Customer Name ");
		lblHCustName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHCustName.setForeground(Color.GRAY);
		lblHCustName.setBorder(border);
		lblHCustName.setBounds(10, 83, 120, 25);
		panel.add(lblHCustName);
		
		tf_HCustName = new JTextField();
		tf_HCustName.setText("");
		tf_HCustName.setFont(new Font("Dialog", Font.BOLD, 13));
		tf_HCustName.setEnabled(false);
		tf_HCustName.setEditable(false);
		tf_HCustName.setDisabledTextColor(Color.DARK_GRAY);
		tf_HCustName.setColumns(10);
		tf_HCustName.setBounds(129, 83, 161, 25);
		tf_HCustName.setBorder(border);
		panel.add(tf_HCustName);
		
		JLabel lblHCustMob = new JLabel("Customer Mobile ");
		lblHCustMob.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHCustMob.setForeground(Color.GRAY);
		lblHCustMob.setBounds(10, 107, 120, 25);
		lblHCustMob.setBorder(border);
		panel.add(lblHCustMob);
		
		tf_HCustMob = new JTextField();
		tf_HCustMob.setText("");
		tf_HCustMob.setFont(new Font("Dialog", Font.BOLD, 13));
		tf_HCustMob.setEnabled(false);
		tf_HCustMob.setEditable(false);
		tf_HCustMob.setDisabledTextColor(Color.DARK_GRAY);
		tf_HCustMob.setColumns(10);
		tf_HCustMob.setBounds(129, 107, 161, 25);
		tf_HCustMob.setBorder(border);
		panel.add(tf_HCustMob);
		
		JLabel lblHBalance = new JLabel("Balance ");
		lblHBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHBalance.setForeground(Color.GRAY);
		lblHBalance.setBounds(356, 83, 120, 25);
		lblHBalance.setBorder(border);
		panel.add(lblHBalance);
		
		tf_HBalance = new JTextField();
		tf_HBalance.setText("");
		tf_HBalance.setFont(new Font("Dialog", Font.BOLD, 20));
		tf_HBalance.setEnabled(false);
		tf_HBalance.setEditable(false);
		tf_HBalance.setDisabledTextColor(Color.white);
		tf_HBalance.setColumns(10);
		tf_HBalance.setBounds(475, 83, 161, 25);
		tf_HBalance.setBorder(border);
		tf_HBalance.setBackground(Color.GRAY);
		panel.add(tf_HBalance);
		
		JLabel lblHEntryDate = new JLabel("Entry Date ");
		lblHEntryDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHEntryDate.setForeground(Color.GRAY);
		lblHEntryDate.setBounds(356, 107, 120, 25);
		lblHEntryDate.setBorder(border);
		panel.add(lblHEntryDate);
		
		tf_HEntryDt = new JTextField();
		tf_HEntryDt.setText("");
		tf_HEntryDt.setFont(new Font("Dialog", Font.BOLD, 13));
		tf_HEntryDt.setEnabled(false);
		tf_HEntryDt.setEditable(false);
		tf_HEntryDt.setDisabledTextColor(Color.DARK_GRAY);
		tf_HEntryDt.setColumns(10);
		tf_HEntryDt.setBounds(475, 107, 161, 25);
		tf_HEntryDt.setBorder(border);
		panel.add(tf_HEntryDt);
		
		JPanel panelPurHistory = new JPanel();
		panelPurHistory.setBorder(new TitledBorder(null, "Customer Purchase History", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelPurHistory.setBounds(10, 170, 646, 355);
		panelCustHistory.add(panelPurHistory);
		panelPurHistory.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 27, 626, 317);
		panelPurHistory.add(scrollPane);
		
		purchaseHistoryTable = new JTable();

		purchaseHistModel= new DefaultTableModel(){
			boolean[] columnEditables = new boolean[] {
					 false, false, false,false, false, true
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		 };
		 purchaseHistModel.setColumnIdentifiers(new String[] {
				 "Bill Number", "Bill Date", "No Of Items", "Quantity", "Bill Amount", "Bill Details"}
			       );
		 
		 purchaseHistoryTable.setModel(purchaseHistModel);
		
		scrollPane.setViewportView(purchaseHistoryTable);
		JTableHeader header = purchaseHistoryTable.getTableHeader();
		 header.setFont(new Font("Dialog", Font.BOLD, 12));
		 
		JPanel panelCustPayHistory = new JPanel();
		panelCustPayHistory.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Customer Payment History", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelCustPayHistory.setBounds(666, 11, 469, 514);
		panelCustHistory.add(panelCustPayHistory);
		panelCustPayHistory.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(17, 32, 434, 471);
		panelCustPayHistory.add(scrollPane_1);
		
		paymentHistoryTable = new JTable();
		
		paymentHistModel= new DefaultTableModel(){
			boolean[] columnEditables = new boolean[] {
					 false, false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		 };
		 paymentHistModel.setColumnIdentifiers(new String[] {
				 "Payment Date", "Amount", "Status"}
			       );
		 
		paymentHistoryTable.setModel(paymentHistModel);
		 JTableHeader header2 = paymentHistoryTable.getTableHeader();
		 header2.setFont(new Font("Dialog", Font.BOLD, 12));
		 
		scrollPane_1.setViewportView(paymentHistoryTable);
		contentPane.add(tabbedPane);
		
		//Actions
		tf_HSearchCust.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					if(!tf_HSearchCust.getText().equals("")){
						setCustomerDetails(tf_HSearchCust.getText());
						setHistoryDetails(tf_HSearchCust.getText());
					}
					
				}
			}
		});
		
		//purchaseHistoryTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 
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

		 ButtonColumn buttonColumn = new ButtonColumn(purchaseHistoryTable, viewBillAction, 5);
		 buttonColumn.setMnemonic(KeyEvent.VK_D);
	}
	
	protected void setUpdatedCustBalance(Long custMobileNumber) {
		Customer customer = UserServices.getCustomerDetails(custMobileNumber);
		tf_CustBalance.setText(String.valueOf(customer.getBalanceAmt()));
	}
	protected void enableDisableTF() {
		if(rb_AddPendingAmt.isSelected()){
			tf_AddPendingAmt.setEnabled(true);
			tf_SettleAmt.setEnabled(false);
			tf_SettleAmt.setText("0.00");
		}
		if(rb_SettleUpAmt.isSelected()){
			tf_AddPendingAmt.setEnabled(false);
			tf_SettleAmt.setEnabled(true);
			tf_AddPendingAmt.setText("0.00");
		}
	}
	
	protected void enableDisablePanel() {
		if(rb_CustDetails.isSelected()){
			PDFUtils.setEnableRec(panelDisgnGrid, true);
			PDFUtils.setEnableRec(panelBalance, false);
		}
		if(rb_CustPayment.isSelected()){
			PDFUtils.setEnableRec(panelDisgnGrid, false);
			PDFUtils.setEnableRec(panelBalance, true);
			tf_AddPendingAmt.setEnabled(false);
			tf_SettleAmt.setEnabled(false);
		}
	}
	
	protected void resetCustomerFields() {
		tf_CustMob.setText("");
		tf_CustName.setText("");
		tf_CustCity.setText("");
		tf_CustEmail.setText("");
		tf_CustBalance.setText("0.00");
		tf_CustMob.setEnabled(true);
	}
	public void fillCustomerTableData(){
		List<Customer> customerList= UserServices.getAllCustomers();
		customerModel.setRowCount(0);
		if(customerList.isEmpty()){
			JOptionPane.showMessageDialog(contentPane, "No Customer found!");
		}else{
			for(Customer c : customerList){
				customerModel.addRow(new Object[]{c.getCustMobileNumber(),c.getCustName(),PDFUtils.getDecimalFormat(c.getBalanceAmt()),c.getCustCity(),c.getCustEmail()});
			}
		}
	}
	
	public void searchCustomerTableData(String searchString){
		List<Customer> customerList = null;
		if(searchString.isEmpty()){
			customerList= UserServices.getAllCustomers();
		}else{
			customerList= UserServices.searchCustomer(searchString);
		}
		
		customerModel.setRowCount(0);
		if(customerList.isEmpty()){
			//JOptionPane.showMessageDialog(contentPane, "No Product with matching criteria!");
		}else{
			for(Customer c : customerList){
				customerModel.addRow(new Object[]{c.getCustMobileNumber(),c.getCustName(),PDFUtils.getDecimalFormat(c.getBalanceAmt()),c.getCustCity(),c.getCustEmail()});
			}
		}
	}
	
	//Customer History
	public List<String> getCustomerNameList(){
		
		List<String> customerNameList = new ArrayList<String>();
		customerMap = new HashMap<String, Customer>();
		for(Customer cust :UserServices.getAllCustomers()){
			customerNameList.add(cust.getCustName()+" : "+cust.getCustMobileNumber());
			customerMap.put(cust.getCustName()+" : "+cust.getCustMobileNumber(),cust);
		}
		return customerNameList;
	}
	
	protected void setCustomerDetails(String customer) {
		String custMobile;
		Customer cust = null;
		if(!customer.isEmpty()){
			custMobile = customer.split(" : ")[1];
			cust = UserServices.getCustomerDetails(Long.valueOf(custMobile));
		}
		
		if(cust!=null){
			tf_HCustMob.setText(" "+String.valueOf(cust.getCustMobileNumber()));
			tf_HCustName.setText(" "+cust.getCustName());
			tf_HBalance.setText(" "+PDFUtils.getDecimalFormat(cust.getBalanceAmt()));
			tf_HEntryDt.setText(" "+sdf.format(cust.getEntryDate()));
		}
	}
	protected void setHistoryDetails(String customer) {
		Customer cust = customerMap.get(customer);
		billsMap = new HashMap<Integer, BillDetails>();
		purchaseHistModel.setRowCount(0);
		paymentHistModel.setRowCount(0);
		if(cust!=null){
			for(BillDetails bill : CustomerHistoryServices.getBillDetails(cust.getCustMobileNumber())){
				billsMap.put(bill.getBillNumber(), bill);
				purchaseHistModel.addRow(new Object[]{bill.getBillNumber(),sdf.format(bill.getTimestamp()),bill.getNoOfItems(),bill.getTotalQuanity(),PDFUtils.getDecimalFormat(bill.getNetSalesAmt()),"View Bill"});
			 }
			/*for(Customer custpay : CustomerHistoryServices.getAllCustomersPayHistory(cust.getCustMobileNumber())){
				paymentHistModel.addRow(new Object[]{sdf.format(custpay.getEntryDate()),AppUtils.getDecimalFormat(custpay.getAmount()),custpay.getHistoryFlag()});
			 }*/
			
		}
	}
}
