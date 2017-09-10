package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.LabelAlignment;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.BillDetails;
import com.shopbilling.dto.Customer;
import com.shopbilling.dto.StatusDTO;
import com.shopbilling.dto.UserDetails;
import com.shopbilling.services.CustomerHistoryServices;
import com.shopbilling.services.UserServices;
import com.shopbilling.utils.PDFUtils;

public class ManageCustomersUI extends JInternalFrame {

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
	/**
	 * Create the frame.
	 */
	public ManageCustomersUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Manage Customer Details");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setResizable(false);
		//fix the frame location
		//setUndecorated(true);
		JPanel panelManage = new JPanel();
		panelManage.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelManage.setBounds(10, 11, 1150, 579);
		contentPane.add(panelManage);
		panelManage.setLayout(null);
		JPanel panelCustDetails = new JPanel();
		panelCustDetails.setBorder(new TitledBorder(null, "Customer Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelCustDetails.setBounds(37, 11, 507, 249);
		panelManage.add(panelCustDetails);
		panelCustDetails.setLayout(null);
		
		panelDisgnGrid = new JPanel();
		DesignGridLayout layout = new DesignGridLayout(panelDisgnGrid);
		layout.labelAlignment(LabelAlignment.RIGHT);
		tf_CustMob = new JTextField();
		tf_CustMob.setDisabledTextColor(Color.BLACK);
		tf_CustMob.setFont(new Font("Tahoma", Font.BOLD, 12));
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
		tf_CustName.setDisabledTextColor(Color.BLACK);
		tf_CustName.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_CustCity = new JTextField();
		tf_CustCity.setDisabledTextColor(Color.BLACK);
		tf_CustCity.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_CustEmail = new JTextField();
		tf_CustEmail.setDisabledTextColor(Color.BLACK);
		tf_CustEmail.setFont(new Font("Tahoma", Font.BOLD, 12));
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
		panelCustBalance.setBounds(37, 271, 507, 284);
		panelManage.add(panelCustBalance);
		panelCustBalance.setLayout(null);
		
		panelBalance = new JPanel();
		panelBalance.setBounds(71, 35, 360, 214);
		panelCustBalance.add(panelBalance);
		panelBalance.setLayout(null);
		
		JLabel lblCustomerBalance = new JLabel("Pending Amount : ");
		lblCustomerBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCustomerBalance.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblCustomerBalance.setBounds(10, 30, 134, 25);
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
				updatePaymentAction();
			}
		});
		btnUpdatePayment.setBounds(138, 180, 148, 23);
		panelBalance.add(btnUpdatePayment);
		
		JPanel panelCustomers = new JPanel();
		panelCustomers.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Customers", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelCustomers.setBounds(560, 11, 570, 544);
		panelManage.add(panelCustomers);
		panelCustomers.setLayout(null);
		
		JScrollPane CustomerScrollPane = new JScrollPane();
		CustomerScrollPane.setBounds(18, 62, 534, 471);
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
		 //Table Row Height 
		 PDFUtils.setTableRowHeight(customerTable);
		 
		 customerModel.setColumnIdentifiers(new String[] {
				 "Mobile Number", "Name", "Pending Amount","City","Email"}
	       );
		 customerTable.setModel(customerModel);
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
		 
		//customerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		customerTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		customerTable.getColumnModel().getColumn(1).setPreferredWidth(235);
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
		lblSearchCustomer.setBounds(193, 17, 150, 40);
		panelCustomers.add(lblSearchCustomer);
		
		tf_SearchCust = new JTextField();
		tf_SearchCust.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_SearchCust.setBounds(353, 26, 193, 23);
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
				saveCustomerAction();
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
				updateCustomerAction();
			}
		});
		//Delete Button Action
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteCustomerAction();
			}
		});
		
		ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
	
	protected void setUpdatedCustBalance(Long custMobileNumber) {
		Customer customer = UserServices.getCustomerDetails(custMobileNumber);
		tf_CustBalance.setText(PDFUtils.getDecimalFormat((customer.getBalanceAmt())));
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

	private void updatePaymentAction() {
		if(!rb_AddPendingAmt.isSelected()&& !rb_SettleUpAmt.isSelected()){
			JOptionPane.showMessageDialog(contentPane, "Please select the one of the option to proceed !");
		}else{
			if(!tf_CustMob.isEnabled()){
				if(rb_AddPendingAmt.isSelected()){
					if(PDFUtils.isMandatoryEntered(tf_AddPendingAmt)&& Double.valueOf(tf_AddPendingAmt.getText())>0){
						Customer customer = new Customer();
						customer.setCustMobileNumber(Long.valueOf(tf_CustMob.getText()));
						customer.setCustName(tf_CustName.getText());
						customer.setAmount(Double.valueOf(tf_AddPendingAmt.getText()));
						StatusDTO statusPayHist = UserServices.addCustomerPaymentHistory(customer.getCustMobileNumber(),customer.getAmount(),0,AppConstants.CREDIT,"Added by : "+userDetails.getFirstName()+" "+userDetails.getLastName());
						StatusDTO statusAddAmt = UserServices.addPendingPaymentToCustomer(Long.valueOf(tf_CustMob.getText()), Double.valueOf(tf_AddPendingAmt.getText()));
						
						if(statusPayHist.getStatusCode()==0 && statusAddAmt.getStatusCode()==0){
							JOptionPane.showMessageDialog(contentPane, "Add Payment Succussful !");
							fillCustomerTableData();
							setUpdatedCustBalance(Long.valueOf(tf_CustMob.getText()));
							tf_AddPendingAmt.setText("0.00");
						}else{
							JOptionPane.showMessageDialog(contentPane,"Error occured while adding payment","Error",JOptionPane.WARNING_MESSAGE);
						}
					}else{
						JOptionPane.showMessageDialog(contentPane, "Amount should be greater than Zero (0)");
					}
					
				}else if(rb_SettleUpAmt.isSelected()){
					if(PDFUtils.isMandatoryEntered(tf_SettleAmt)&& Double.valueOf(tf_SettleAmt.getText())>0 && Double.valueOf(tf_SettleAmt.getText())<= Double.valueOf(tf_CustBalance.getText())){
							Customer customerSt = new Customer();
							customerSt.setCustMobileNumber(Long.valueOf(tf_CustMob.getText()));
							customerSt.setCustName(tf_CustName.getText());
							customerSt.setAmount(Double.valueOf(tf_SettleAmt.getText()));
							StatusDTO statusPayHist = UserServices.addCustomerPaymentHistory(customerSt.getCustMobileNumber(),0,customerSt.getAmount(),AppConstants.DEBIT,"Settled up by : "+userDetails.getFirstName()+" "+userDetails.getLastName());
							StatusDTO statusAddAmt =UserServices.settleUpCustomerBalance(Long.valueOf(tf_CustMob.getText()), Double.valueOf(tf_SettleAmt.getText()));
							
							if(statusPayHist.getStatusCode()==0 && statusAddAmt.getStatusCode()==0){
								JOptionPane.showMessageDialog(contentPane, "Settle Payment Succussful !");
								fillCustomerTableData();
								setUpdatedCustBalance(Long.valueOf(tf_CustMob.getText()));
								tf_SettleAmt.setText("0.00");
							}else{
								JOptionPane.showMessageDialog(contentPane,"Error occured while settle up payment","Error",JOptionPane.WARNING_MESSAGE);	
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

	private void saveCustomerAction() {
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
					JOptionPane.showMessageDialog(contentPane,"Customer already exists for Mobile number : "+tf_CustMob.getText(),"Error",JOptionPane.WARNING_MESSAGE);
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

	private void updateCustomerAction() {
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

	private void deleteCustomerAction() {
		if(tf_CustMob.getText().equals("")){
			JOptionPane.showMessageDialog(contentPane, "Please select Customer!");
		}else{
			List<BillDetails> billList = CustomerHistoryServices.getBillDetails(Long.parseLong(tf_CustMob.getText()));
			if(billList.size()>0){
				JOptionPane.showMessageDialog(contentPane,"The selected customer can not be deleted,since there is other data related to this customer in system ","Error",JOptionPane.WARNING_MESSAGE);
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

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		System.out.println(userDetails.getFirstName());
		System.out.println(userDetails.getLastName());
		this.userDetails = userDetails;
	}
	
}
