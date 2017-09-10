package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.Expense;
import com.shopbilling.services.ButtonColumn;
import com.shopbilling.services.ExpensesServices;
import com.shopbilling.utils.PDFUtils;
import com.toedter.calendar.JDateChooser;

public class ViewExpensesUI extends JInternalFrame {
	private JTable table;
	private DefaultTableModel reportModel;
	private JDateChooser toDateChooser;
	private JDateChooser fromDateChooser;
	private double totalAmount=0;
	private JLabel lblTotalAmountValue;
	private Map<Integer,Expense> expenseMap;
	private JFrame parentFrame;
	private JComboBox cb_expenseCategory;
	/**
	 * Create the frame.
	 */
	public ViewExpensesUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("View Expenses");
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
		
		JLabel lblExpenseCategory = new JLabel("Category :");
		lblExpenseCategory.setHorizontalAlignment(SwingConstants.RIGHT);
		lblExpenseCategory.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblExpenseCategory.setBounds(559, 19, 146, 28);
		panel.add(lblExpenseCategory);
		
		cb_expenseCategory = new JComboBox();
		cb_expenseCategory.setBounds(715, 19, 199, 28);
		cb_expenseCategory.addItem("-- Select Category --");
		ExpensesServices.populateDropdown(cb_expenseCategory);
		panel.add(cb_expenseCategory);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 82, 1150, 391);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 26, 1120, 354);
		panel_1.add(scrollPane);
		
		table = new JTable();
		reportModel = new DefaultTableModel(){
			 boolean[] columnEditables = new boolean[] {
					 false, false, false, false,true,true,false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
		 reportModel.setColumnIdentifiers(new String[] {
				 "Expense Category", "Amount", "Description", "Date", "Delete","Update","Id"}
	       );
		table.setModel(reportModel);
		
		//Table Row Height 
		 PDFUtils.setTableRowHeight(table);
		 
		scrollPane.setViewportView(table);
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 //View Action
		 Action deleteAction = new AbstractAction()
		 {
		     public void actionPerformed(ActionEvent e)
		     {
		    	 JTable table = (JTable)e.getSource();
        		int modelRow = Integer.valueOf( e.getActionCommand() );
        		int id = Integer.valueOf(table.getModel().getValueAt(modelRow, 6).toString());
        		int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure to delete ?","Delete Expense",dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					boolean flag = ExpensesServices.deleteExpense(id);
					if(flag){
						JOptionPane.showMessageDialog(getContentPane(), "Expense Deleted Successfully!");
						fillReportTable();
					}
				}
		     }
		 };

		 ButtonColumn buttonColumn = new ButtonColumn(table, deleteAction, 4);
		 buttonColumn.setMnemonic(KeyEvent.VK_D);
		 //Update Action
		 Action updateAction = new AbstractAction()
		 {
		     public void actionPerformed(ActionEvent e)
		     {
		    	 JTable table = (JTable)e.getSource();
        		int modelRow = Integer.valueOf( e.getActionCommand() );
        		Integer id = Integer.valueOf(table.getModel().getValueAt(modelRow, 6).toString());
		    	 //Open View Bill Screen
        		ModifyExpenseUI modify = new ModifyExpenseUI(id, parentFrame);
        		modify.setVisible(true);
		     }
		 };

		 ButtonColumn buttonColumn2 = new ButtonColumn(table, updateAction, 5);
		 buttonColumn2.setMnemonic(KeyEvent.VK_U);
		 
		 JPanel panel_2 = new JPanel();
		 panel_2.setBorder(new TitledBorder(null, "Consolidated Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		 panel_2.setBounds(10, 484, 1150, 89);
		 getContentPane().add(panel_2);
		 panel_2.setLayout(null);
		 Border br = new LineBorder(Color.black, 2);
		 
		 JLabel lblTotalAmount = new JLabel("Total Expenses");
		 lblTotalAmount.setFont(new Font("Tahoma", Font.BOLD, 13));
		 lblTotalAmount.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalAmount.setBounds(688, 29, 195, 31);
		 lblTotalAmount.setBorder(br);
		 panel_2.add(lblTotalAmount);
		 
		 lblTotalAmountValue = new JLabel("");
		 lblTotalAmountValue.setFont(new Font("Tahoma", Font.BOLD, 20));
		 lblTotalAmountValue.setForeground(Color.BLACK);
		 lblTotalAmountValue.setHorizontalAlignment(SwingConstants.CENTER);
		 lblTotalAmountValue.setBounds(881, 29, 245, 31);
		 lblTotalAmountValue.setBorder(br);
		 panel_2.add(lblTotalAmountValue);
		 
		 table.getColumnModel().getColumn(0).setPreferredWidth(200);
		 table.getColumnModel().getColumn(1).setPreferredWidth(150);
		 table.getColumnModel().getColumn(2).setPreferredWidth(397);
		 table.getColumnModel().getColumn(3).setPreferredWidth(150);
		 table.getColumnModel().getColumn(4).setPreferredWidth(110);
		 table.getColumnModel().getColumn(5).setPreferredWidth(110);
		 table.getColumnModel().getColumn(6).setMinWidth(0);
		 table.getColumnModel().getColumn(6).setMaxWidth(0);
		 table.getColumnModel().getColumn(6).setWidth(0);
		 ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
	
	//Fill Report Table
	private void fillReportTable(){
		List<Expense> expenseList= ExpensesServices.getExpenses(fromDateChooser.getDate()==null?null:new java.sql.Date(fromDateChooser.getDate().getTime()),toDateChooser.getDate()==null?null:new java.sql.Date(toDateChooser.getDate().getTime()),cb_expenseCategory.getSelectedIndex()==0?null:(String)cb_expenseCategory.getSelectedItem());
		calculateConsolidateValues(expenseList);
		reportModel.setRowCount(0);
		if(expenseList.isEmpty()){
			JOptionPane.showMessageDialog(getContentPane(), "No Expenses found for the given period !");
		}else{
			for(Expense b : expenseList){
				
				reportModel.addRow(new Object[]{b.getCategory(),PDFUtils.getDecimalFormat(b.getAmount()),b.getDescription(),b.getDate(),"Delete","Update",b.getId()});
			}
		}
	}

	private void calculateConsolidateValues(List<Expense> expenseList) {
		expenseMap = new HashMap<Integer, Expense>();
		totalAmount=0;
		
		for(Expense expense: expenseList){
			totalAmount+=expense.getAmount();
			expenseMap.put(expense.getId(), expense);
		}
		lblTotalAmountValue.setText("Rs. "+PDFUtils.getDecimalFormat(totalAmount)+" /-");
	}
}
