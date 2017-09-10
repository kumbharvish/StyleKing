package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.Expense;
import com.shopbilling.dto.StatusDTO;
import com.shopbilling.services.ExpensesServices;
import com.shopbilling.utils.PDFUtils;
import com.toedter.calendar.JDateChooser;

public class AddExpenseUI extends JInternalFrame {
	private JTextField tf_Amount;
	private JTextField tf_Description;
	private JPanel panel_1;
	private JComboBox expCategory;
	private JDateChooser tf_Date;

	public AddExpenseUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Add Expense");
		getContentPane().setLayout(null);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(313, 70, 406, 413);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblExpenseCategory = new JLabel("Expense Category *: ");
		lblExpenseCategory.setHorizontalAlignment(SwingConstants.RIGHT);
		lblExpenseCategory.setBounds(10, 32, 121, 25);
		panel_1.add(lblExpenseCategory);
		
		JLabel lblAmount = new JLabel("Amount *: ");
		lblAmount.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAmount.setBounds(10, 104, 121, 25);
		panel_1.add(lblAmount);
		
		tf_Amount = new JTextField();
		tf_Amount.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_Amount.setColumns(10);
		tf_Amount.setBounds(141, 104, 225, 25);
		tf_Amount.setDisabledTextColor(Color.black);
		panel_1.add(tf_Amount);
		
		JLabel lblDescription = new JLabel("Description : ");
		lblDescription.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDescription.setBounds(10, 176, 121, 25);
		panel_1.add(lblDescription);
		
		tf_Description = new JTextField();
		tf_Description.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_Description.setColumns(10);
		tf_Description.setBounds(141, 176, 225, 25);
		tf_Description.setDisabledTextColor(Color.black);
		panel_1.add(tf_Description);
		
		JLabel lblDate = new JLabel("Date *: ");
		lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDate.setBounds(10, 248, 121, 25);
		panel_1.add(lblDate);
		
		expCategory = new JComboBox();
		expCategory.setBounds(141, 32, 225, 25);
		expCategory.addItem("-- Select Category --");
		ExpensesServices.populateDropdown(expCategory);
		panel_1.add(expCategory);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(PDFUtils.isMandatorySelected(expCategory)&&
						PDFUtils.isMandatoryEntered(tf_Amount) && tf_Date.getDate()!=null){
					Expense expense = new Expense();
					expense.setCategory((String)expCategory.getSelectedItem());
					expense.setDate(new java.sql.Date(tf_Date.getDate().getTime()));
					expense.setAmount(Double.parseDouble(tf_Amount.getText()));
					expense.setDescription(tf_Description.getText());
					
					StatusDTO status = ExpensesServices.addExpense(expense);
					if(status.getStatusCode()==0){
						JOptionPane.showMessageDialog(getContentPane(), "Expense Added Successfully !");
						resetFields();
					}else{
						JOptionPane.showMessageDialog(getContentPane(), "Error occured ", "Error", JOptionPane.WARNING_MESSAGE);
					}
					
				}else{
					JOptionPane.showMessageDialog(getContentPane(), "Please enter mandatory fields !");
				}
			}
		});
		btnSave.setBounds(158, 339, 89, 23);
		panel_1.add(btnSave);
		
		tf_Date = new JDateChooser(new Date());
		tf_Date.setBounds(141, 248, 225, 25);
		tf_Date.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_1.add(tf_Date);
		
		ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}

	protected void resetFields() {
		expCategory.setSelectedIndex(0);
		tf_Amount.setText("");
		tf_Date.setDate(new Date());
		tf_Description.setText("");
	}
}
