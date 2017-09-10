package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.dto.CashCounter;
import com.shopbilling.dto.MyStoreDetails;
import com.shopbilling.services.MyStoreServices;
import com.shopbilling.services.ReportServices;
import com.shopbilling.utils.PDFUtils;
import com.toedter.calendar.JDateChooser;

public class CashCounterUI extends JInternalFrame {
	
	private JLabel totalSalesAmtCbal;
	private JLabel totalSalesReturnAmtDr;
	private JLabel totalSalesReturnAmtCbal;
	private JLabel totalCustSettlementAmtCr;
	private JLabel totalCustSettlementAmtCbal;
	private JLabel totalCredit;
	private JLabel totalDebit;
	private JLabel closingBalance;
	private JLabel totalSalesAmtCr;
	private JLabel lblOpeningCashAmountCr;
	private JLabel lblOpeningCashAmountCbl;
	private JFrame mainFrame;
	CashCounterUI cash;
	private JLabel totalExpensesAmtDr;
	private JLabel totalExpensesAmtCbal;
	public JDateChooser dateChooser;
	private JButton btnUpdate;
	private JButton btnViewCustomers;

	public CashCounterUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Cash Counter Collection");
		getContentPane().setLayout(null);
		Border border = new LineBorder(Color.black, 1);
		MyStoreDetails myStore = MyStoreServices.getMyStoreDetails();
		JLabel lblStoreName = new JLabel(myStore.getStoreName());
		lblStoreName.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblStoreName.setHorizontalAlignment(SwingConstants.CENTER);
		lblStoreName.setBounds(327, 11, 444, 24);
		getContentPane().add(lblStoreName);
		mainFrame = (JFrame)this.getTopLevelAncestor();
		cash=this;
		
		JLabel lblAddress = new JLabel(myStore.getAddress()+", "+myStore.getAddress2()+", "+myStore.getCity());
		lblAddress.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddress.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblAddress.setBounds(327, 36, 444, 24);
		getContentPane().add(lblAddress);
		
		JLabel lblDate = new JLabel("Date : ");
		lblDate.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDate.setBounds(68, 42, 46, 25);
		getContentPane().add(lblDate);
		
		JLabel lblNewLabel = new JLabel("Description");
		lblNewLabel.setBackground(Color.LIGHT_GRAY);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel.setOpaque(true);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(120, 137, 292, 35);
		lblNewLabel.setBorder(border);
		getContentPane().add(lblNewLabel);
		
		JLabel lblCredit = new JLabel("Credit");
		lblCredit.setBackground(Color.LIGHT_GRAY);
		lblCredit.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblCredit.setOpaque(true);
		lblCredit.setHorizontalAlignment(SwingConstants.CENTER);
		lblCredit.setBounds(411, 137, 206, 35);
		lblCredit.setBorder(border);		
		getContentPane().add(lblCredit);
		
		JLabel lblDebit = new JLabel("Debit");
		lblDebit.setBackground(Color.LIGHT_GRAY);
		lblDebit.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblDebit.setOpaque(true);
		lblDebit.setHorizontalAlignment(SwingConstants.CENTER);
		lblDebit.setBounds(616, 137, 206, 35);
		lblDebit.setBorder(border);
		getContentPane().add(lblDebit);
		
		JLabel lblClosingBalance = new JLabel("Closing Balance");
		lblClosingBalance.setBackground(Color.LIGHT_GRAY);
		lblClosingBalance.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblClosingBalance.setOpaque(true);
		lblClosingBalance.setHorizontalAlignment(SwingConstants.CENTER);
		lblClosingBalance.setBounds(821, 137, 206, 35);
		lblClosingBalance.setBorder(border);
		getContentPane().add(lblClosingBalance);
		
		JLabel lblTotalSalesAmount = new JLabel("Total Sales Amount");
		lblTotalSalesAmount.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotalSalesAmount.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalSalesAmount.setBounds(120, 231, 292, 60);
		lblTotalSalesAmount.setBorder(border);
		getContentPane().add(lblTotalSalesAmount);
		
		totalSalesAmtCr = new JLabel("");
		totalSalesAmtCr.setFont(new Font("Tahoma", Font.PLAIN, 14));
		totalSalesAmtCr.setHorizontalAlignment(SwingConstants.CENTER);
		totalSalesAmtCr.setBounds(411, 231, 206, 60);
		totalSalesAmtCr.setBorder(border);
		
		getContentPane().add(totalSalesAmtCr);
		
		JLabel totalSalesAmtDr = new JLabel("");
		totalSalesAmtDr.setHorizontalAlignment(SwingConstants.CENTER);
		totalSalesAmtDr.setBounds(616, 231, 206, 60);
		totalSalesAmtDr.setBorder(border);
		getContentPane().add(totalSalesAmtDr);
		
		totalSalesAmtCbal = new JLabel("");
		totalSalesAmtCbal.setFont(new Font("Tahoma", Font.PLAIN, 15));
		totalSalesAmtCbal.setHorizontalAlignment(SwingConstants.CENTER);
		totalSalesAmtCbal.setBounds(821, 231, 206, 60);
		totalSalesAmtCbal.setBorder(border);
		
		getContentPane().add(totalSalesAmtCbal);
		
		JLabel lblTotalSalesReturn = new JLabel("Total Sales Return Amount");
		lblTotalSalesReturn.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotalSalesReturn.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalSalesReturn.setBounds(120, 290, 292, 60);
		lblTotalSalesReturn.setBorder(border);
		getContentPane().add(lblTotalSalesReturn);
		
		JLabel totalSalesReturnAmtCr = new JLabel("");
		totalSalesReturnAmtCr.setHorizontalAlignment(SwingConstants.CENTER);
		totalSalesReturnAmtCr.setBounds(411, 290, 206, 60);
		totalSalesReturnAmtCr.setBorder(border);
		getContentPane().add(totalSalesReturnAmtCr);
		
		totalSalesReturnAmtDr = new JLabel("");
		totalSalesReturnAmtDr.setFont(new Font("Tahoma", Font.PLAIN, 15));
		totalSalesReturnAmtDr.setHorizontalAlignment(SwingConstants.CENTER);
		totalSalesReturnAmtDr.setBounds(616, 290, 206, 60);
		totalSalesReturnAmtDr.setBorder(border);
		
		getContentPane().add(totalSalesReturnAmtDr);
		
		totalSalesReturnAmtCbal = new JLabel("");
		totalSalesReturnAmtCbal.setFont(new Font("Tahoma", Font.PLAIN, 15));
		totalSalesReturnAmtCbal.setHorizontalAlignment(SwingConstants.CENTER);
		totalSalesReturnAmtCbal.setBounds(821, 290, 206, 60);
		totalSalesReturnAmtCbal.setBorder(border);
		
		getContentPane().add(totalSalesReturnAmtCbal);
		
		totalExpensesAmtCbal = new JLabel("");
		totalExpensesAmtCbal.setFont(new Font("Tahoma", Font.PLAIN, 15));
		totalExpensesAmtCbal.setHorizontalAlignment(SwingConstants.CENTER);
		totalExpensesAmtCbal.setBounds(821, 349, 206, 60);
		totalExpensesAmtCbal.setBorder(border);
		getContentPane().add(totalExpensesAmtCbal);
		
		JLabel lblTotalExpensesAmount = new JLabel("Total Expenses Amount");
		lblTotalExpensesAmount.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotalExpensesAmount.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalExpensesAmount.setBounds(120, 349, 292, 60);
		getContentPane().add(lblTotalExpensesAmount);
		lblTotalExpensesAmount.setBorder(border);
		
		JLabel totalExpensesAmtCr = new JLabel("");
		totalExpensesAmtCr.setHorizontalAlignment(SwingConstants.CENTER);
		totalExpensesAmtCr.setBounds(411, 349, 206, 60);
		totalExpensesAmtCr.setBorder(border);
		getContentPane().add(totalExpensesAmtCr);
		
		totalExpensesAmtDr = new JLabel("");
		totalExpensesAmtDr.setFont(new Font("Tahoma", Font.PLAIN, 15));
		totalExpensesAmtDr.setHorizontalAlignment(SwingConstants.CENTER);
		totalExpensesAmtDr.setBounds(616, 349, 206, 60);
		totalExpensesAmtDr.setBorder(border);
		getContentPane().add(totalExpensesAmtDr);
		
		JLabel lblTotalCustomerSettlement = new JLabel("Total Customer Settlement Amount");
		lblTotalCustomerSettlement.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotalCustomerSettlement.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalCustomerSettlement.setBounds(120, 408, 292, 60);
		lblTotalCustomerSettlement.setBorder(border);
		getContentPane().add(lblTotalCustomerSettlement);
		
		totalCustSettlementAmtCr = new JLabel("");
		totalCustSettlementAmtCr.setFont(new Font("Tahoma", Font.PLAIN, 15));
		totalCustSettlementAmtCr.setHorizontalAlignment(SwingConstants.CENTER);
		totalCustSettlementAmtCr.setBounds(411, 408, 206, 60);
		totalCustSettlementAmtCr.setBorder(border);
		
		getContentPane().add(totalCustSettlementAmtCr);
		
		JLabel totalCustSettlementAmtDr = new JLabel("");
		totalCustSettlementAmtDr.setHorizontalAlignment(SwingConstants.CENTER);
		totalCustSettlementAmtDr.setBounds(616, 408, 206, 60);
		totalCustSettlementAmtDr.setBorder(border);
		getContentPane().add(totalCustSettlementAmtDr);
		
		totalCustSettlementAmtCbal = new JLabel("");
		totalCustSettlementAmtCbal.setFont(new Font("Tahoma", Font.PLAIN, 15));
		totalCustSettlementAmtCbal.setHorizontalAlignment(SwingConstants.CENTER);
		totalCustSettlementAmtCbal.setBounds(821, 408, 206, 60);
		totalCustSettlementAmtCbal.setBorder(border);
		
		getContentPane().add(totalCustSettlementAmtCbal);
		
		JLabel lblTotal = new JLabel("Total");
		lblTotal.setForeground(Color.WHITE);
		lblTotal.setBackground(Color.GRAY);
		lblTotal.setOpaque(true);
		lblTotal.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotal.setBounds(120, 467, 292, 60);
		lblTotal.setBorder(border);
		getContentPane().add(lblTotal);
		
		totalCredit = new JLabel("");
		totalCredit.setForeground(Color.WHITE);
		totalCredit.setBackground(Color.GRAY);
		totalCredit.setFont(new Font("Tahoma", Font.BOLD, 15));
		totalCredit.setHorizontalAlignment(SwingConstants.CENTER);
		totalCredit.setOpaque(true);
		totalCredit.setBounds(411, 467, 206, 60);
		totalCredit.setBorder(border);
		
		getContentPane().add(totalCredit);
		
		totalDebit = new JLabel("");
		totalDebit.setForeground(Color.WHITE);
		totalDebit.setBackground(Color.GRAY);
		totalDebit.setFont(new Font("Tahoma", Font.BOLD, 15));
		totalDebit.setOpaque(true);
		totalDebit.setHorizontalAlignment(SwingConstants.CENTER);
		totalDebit.setBounds(616, 467, 206, 60);
		totalDebit.setBorder(border);
		
		getContentPane().add(totalDebit);
		
		closingBalance = new JLabel("");
		closingBalance.setForeground(Color.WHITE);
		closingBalance.setBackground(Color.GRAY);
		closingBalance.setOpaque(true);
		closingBalance.setFont(new Font("Tahoma", Font.BOLD, 18));
		closingBalance.setHorizontalAlignment(SwingConstants.CENTER);
		closingBalance.setBounds(821, 467, 206, 60);
		closingBalance.setBorder(border);
		
		getContentPane().add(closingBalance);
		
		btnUpdate = new JButton("Update Opening Cash");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddOpeningCashUI  viewDetails= new AddOpeningCashUI(mainFrame,cash);
				viewDetails.setVisible(true);
			}
		});
		btnUpdate.setBounds(886, 41, 171, 23);
		getContentPane().add(btnUpdate);
		
		JLabel lblOpeningCashAmount = new JLabel("Opening Cash Amount");
		lblOpeningCashAmount.setHorizontalAlignment(SwingConstants.CENTER);
		lblOpeningCashAmount.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblOpeningCashAmount.setBounds(120, 172, 292, 60);
		lblOpeningCashAmount.setBorder(border);
		getContentPane().add(lblOpeningCashAmount);
		
		lblOpeningCashAmountCr = new JLabel("0.00");
		lblOpeningCashAmountCr.setHorizontalAlignment(SwingConstants.CENTER);
		lblOpeningCashAmountCr.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblOpeningCashAmountCr.setBounds(411, 172, 206, 60);
		lblOpeningCashAmountCr.setBorder(border);
		getContentPane().add(lblOpeningCashAmountCr);
		
		JLabel lblOpeningCashAmountDr = new JLabel("");
		lblOpeningCashAmountDr.setHorizontalAlignment(SwingConstants.CENTER);
		lblOpeningCashAmountDr.setBounds(616, 172, 206, 60);
		lblOpeningCashAmountDr.setBorder(border);
		getContentPane().add(lblOpeningCashAmountDr);
		
		lblOpeningCashAmountCbl = new JLabel("0.00");
		lblOpeningCashAmountCbl.setHorizontalAlignment(SwingConstants.CENTER);
		lblOpeningCashAmountCbl.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblOpeningCashAmountCbl.setBounds(821, 172, 206, 60);
		lblOpeningCashAmountCbl.setBorder(border);
		getContentPane().add(lblOpeningCashAmountCbl);
		
		dateChooser = new JDateChooser();
		dateChooser.setBounds(124, 42, 137, 25);
		getContentPane().add(dateChooser);
		dateChooser.setDate(new Date());
		dateChooser.setFont(new Font("Tahoma", Font.BOLD, 13));
		
		btnViewCustomers = new JButton("View Settlement Details");
		btnViewCustomers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettlementCustListUI  viewDetails= new SettlementCustListUI(mainFrame,cash);
				viewDetails.setVisible(true);
			}
		});
		btnViewCustomers.setBounds(886, 91, 171, 23);
		getContentPane().add(btnViewCustomers);
		
		dateChooser.getDateEditor().addPropertyChangeListener(
			    new PropertyChangeListener() {
			        @Override
			        public void propertyChange(PropertyChangeEvent e) {
			            if ("date".equals(e.getPropertyName())) {
			            	setCashCounterDetails();
			            	if(!PDFUtils.getFormattedDate(dateChooser.getDate()).equals(PDFUtils.getFormattedDate(new Date()))){
			            		btnUpdate.setEnabled(false);
			            	}else{
			            		btnUpdate.setEnabled(true);
			            	}
			            }
			        }
			    });
		
		setCashCounterDetails();
		
		ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}

	public void setCashCounterDetails() {
		CashCounter totalSalesAmount = new CashCounter();
		CashCounter totalSalesReturnAmount = new CashCounter();
		CashCounter totalCustSettleAmount = new CashCounter();
		CashCounter openingCashBalance = new CashCounter();
		CashCounter totalAmount = new CashCounter();
		CashCounter totalExpense = new CashCounter();
		
		for(CashCounter cash : ReportServices.getCashCounterDetails(dateChooser.getDate()==null?null:new java.sql.Date(dateChooser.getDate().getTime()),dateChooser.getDate()==null?null:new java.sql.Date(dateChooser.getDate().getTime()))){
			if(cash.getDescription().equals("TOTAL_SALES_AMOUNT")){
				totalSalesAmount = cash;
			}
			if(cash.getDescription().equals("TOTAL_SALES_RETURN_AMOUNT")){
				totalSalesReturnAmount = cash;
			}
			if(cash.getDescription().equals("TOTAL_CUST_SETTLEMENT_AMOUNT")){
				totalCustSettleAmount = cash;
			}
			if(cash.getDescription().equals("TOTAL")){
				totalAmount = cash;
			}
			if(cash.getDescription().equals("OPENING_CASH")){
				openingCashBalance = cash;
			}
			if(cash.getDescription().equals("TOTAL_EXPENSE_AMOUNT")){
				totalExpense = cash;
			}
			
		}
		totalSalesAmtCr.setText(PDFUtils.getAmountFormat(totalSalesAmount.getCreditAmount()));
		totalSalesAmtCbal.setText(PDFUtils.getAmountFormat(totalSalesAmount.getClosingBalance()));
		totalSalesReturnAmtDr.setText(PDFUtils.getAmountFormat(totalSalesReturnAmount.getDebitAmount()));
		totalSalesReturnAmtCbal.setText(PDFUtils.getAmountFormat(totalSalesReturnAmount.getClosingBalance()));
		totalCustSettlementAmtCr.setText(PDFUtils.getAmountFormat(totalCustSettleAmount.getCreditAmount()));
		totalCustSettlementAmtCbal.setText(PDFUtils.getAmountFormat(totalCustSettleAmount.getClosingBalance()));
		totalCredit.setText(PDFUtils.getAmountFormat(totalAmount.getCreditAmount()));
		totalDebit.setText(PDFUtils.getAmountFormat(totalAmount.getDebitAmount()));
		closingBalance.setText(PDFUtils.getAmountFormat(totalAmount.getClosingBalance()));
		lblOpeningCashAmountCr.setText(PDFUtils.getAmountFormat(openingCashBalance.getCreditAmount()));
		lblOpeningCashAmountCbl.setText(PDFUtils.getAmountFormat(openingCashBalance.getClosingBalance()));
		totalExpensesAmtDr.setText(PDFUtils.getAmountFormat(totalExpense.getDebitAmount()));
		totalExpensesAmtCbal.setText(PDFUtils.getAmountFormat(totalExpense.getClosingBalance()));
	}
}
