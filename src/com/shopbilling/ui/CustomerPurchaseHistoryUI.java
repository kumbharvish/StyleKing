package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.dto.BillDetails;
import com.shopbilling.dto.Customer;
import com.shopbilling.services.AutoSuggestTable;
import com.shopbilling.services.ButtonColumn;
import com.shopbilling.services.CustomerHistoryServices;
import com.shopbilling.services.UserServices;
import com.shopbilling.utils.PDFUtils;

public class CustomerPurchaseHistoryUI extends JInternalFrame {

	private JPanel contentPane;
	private JTextField tf_HSearchCust;
	private JTextField tf_HCustName;
	private JTextField tf_HCustMob;
	private JTextField tf_HBalance;
	private JTextField tf_HEntryDt;
	private HashMap<String,Customer> customerMap;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
	private JTable purchaseHistoryTable;
	private Map<Integer,BillDetails> billsMap;
	private DefaultTableModel purchaseHistModel;
	private JFrame parentFrame;
	/**
	 * Create the frame.
	 */
	public CustomerPurchaseHistoryUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Customer Purchase History");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setResizable(false);
		contentPane.setLayout(null);
		//fix the frame location
		//setUndecorated(true);
		JPanel panelCustHistory = new JPanel();
		panelCustHistory.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelCustHistory.setLayout(null);
		panelCustHistory.setBounds(10, 11, 1150, 579);
		contentPane.add(panelCustHistory);
		
		//Customer History Started
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Customer Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(79, 11, 991, 148);
		panelCustHistory.add(panel);
		panel.setLayout(null);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		JLabel lblHSelectCustomer = new JLabel("Select Customer :");
		lblHSelectCustomer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHSelectCustomer.setBounds(312, 21, 103, 25);
		panel.add(lblHSelectCustomer);
		
		tf_HSearchCust = new AutoSuggestTable<String>(getCustomerNameList());
		tf_HSearchCust.setBounds(422, 21, 229, 25);
		panel.add(tf_HSearchCust);
		tf_HSearchCust.setBorder(border);
		tf_HSearchCust.setFont(new Font("Dialog", Font.BOLD, 13));
		tf_HSearchCust.setColumns(10);
		
		JLabel lblHCustName = new JLabel("Customer Name  ");
		lblHCustName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHCustName.setForeground(Color.GRAY);
		lblHCustName.setBorder(border);
		lblHCustName.setBounds(66, 83, 120, 25);
		panel.add(lblHCustName);
		
		tf_HCustName = new JTextField();
		tf_HCustName.setText("");
		tf_HCustName.setFont(new Font("Dialog", Font.BOLD, 13));
		tf_HCustName.setEnabled(false);
		tf_HCustName.setEditable(false);
		tf_HCustName.setDisabledTextColor(Color.DARK_GRAY);
		tf_HCustName.setColumns(10);
		tf_HCustName.setBounds(185, 83, 207, 25);
		tf_HCustName.setBorder(border);
		panel.add(tf_HCustName);
		
		JLabel lblHCustMob = new JLabel("Customer Mobile  ");
		lblHCustMob.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHCustMob.setForeground(Color.GRAY);
		lblHCustMob.setBounds(66, 107, 120, 25);
		lblHCustMob.setBorder(border);
		panel.add(lblHCustMob);
		
		tf_HCustMob = new JTextField();
		tf_HCustMob.setText("");
		tf_HCustMob.setFont(new Font("Dialog", Font.BOLD, 13));
		tf_HCustMob.setEnabled(false);
		tf_HCustMob.setEditable(false);
		tf_HCustMob.setDisabledTextColor(Color.DARK_GRAY);
		tf_HCustMob.setColumns(10);
		tf_HCustMob.setBounds(185, 107, 207, 25);
		tf_HCustMob.setBorder(border);
		panel.add(tf_HCustMob);
		
		JLabel lblHBalance = new JLabel("Pending Amount  ");
		lblHBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHBalance.setForeground(Color.GRAY);
		lblHBalance.setBounds(628, 83, 120, 25);
		lblHBalance.setBorder(border);
		panel.add(lblHBalance);
		
		tf_HBalance = new JTextField();
		tf_HBalance.setText("");
		tf_HBalance.setFont(new Font("Dialog", Font.BOLD, 20));
		tf_HBalance.setEnabled(false);
		tf_HBalance.setEditable(false);
		tf_HBalance.setDisabledTextColor(Color.white);
		tf_HBalance.setColumns(10);
		tf_HBalance.setBounds(747, 83, 207, 25);
		tf_HBalance.setBorder(border);
		tf_HBalance.setBackground(Color.GRAY);
		panel.add(tf_HBalance);
		
		JLabel lblHEntryDate = new JLabel("Entry Date  ");
		lblHEntryDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHEntryDate.setForeground(Color.GRAY);
		lblHEntryDate.setBounds(628, 107, 120, 25);
		lblHEntryDate.setBorder(border);
		panel.add(lblHEntryDate);
		
		tf_HEntryDt = new JTextField();
		tf_HEntryDt.setText("");
		tf_HEntryDt.setFont(new Font("Dialog", Font.BOLD, 13));
		tf_HEntryDt.setEnabled(false);
		tf_HEntryDt.setEditable(false);
		tf_HEntryDt.setDisabledTextColor(Color.DARK_GRAY);
		tf_HEntryDt.setColumns(10);
		tf_HEntryDt.setBounds(747, 107, 207, 25);
		tf_HEntryDt.setBorder(border);
		panel.add(tf_HEntryDt);
		
		JPanel panelPurHistory = new JPanel();
		panelPurHistory.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Purchase History", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelPurHistory.setBounds(79, 170, 991, 398);
		panelCustHistory.add(panelPurHistory);
		panelPurHistory.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 27, 971, 360);
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
		//Table Row Height 
		 PDFUtils.setTableRowHeight(purchaseHistoryTable);
		 
		scrollPane.setViewportView(purchaseHistoryTable);
		
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
		 
		 ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
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
			tf_HCustMob.setText("  "+String.valueOf(cust.getCustMobileNumber()));
			tf_HCustName.setText("  "+cust.getCustName());
			tf_HBalance.setText("  "+PDFUtils.getDecimalFormat(cust.getBalanceAmt()));
			tf_HEntryDt.setText("  "+sdf.format(cust.getEntryDate()));
		}
	}
	protected void setHistoryDetails(String customer) {
		Customer cust = customerMap.get(customer);
		billsMap = new HashMap<Integer, BillDetails>();
		purchaseHistModel.setRowCount(0);
		if(cust!=null){
			for(BillDetails bill : CustomerHistoryServices.getBillDetails(cust.getCustMobileNumber())){
				billsMap.put(bill.getBillNumber(), bill);
				purchaseHistModel.addRow(new Object[]{bill.getBillNumber(),sdf.format(bill.getTimestamp()),bill.getNoOfItems(),bill.getTotalQuanity(),PDFUtils.getDecimalFormat(bill.getNetSalesAmt()),"View Bill"});
			 }
		}
	}
}
