package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.dto.MyStoreDetails;
import com.shopbilling.dto.ProfitLossData;
import com.shopbilling.dto.ProfitLossDetails;
import com.shopbilling.services.MyStoreServices;
import com.shopbilling.services.ReportServices;
import com.shopbilling.utils.PDFUtils;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class ProfitLossStatementUI extends JInternalFrame {
	private JTable debitTable;
	private JTable creditTable;
	private DefaultTableModel debitModel;
	private DefaultTableModel creditModel;
	private JDateChooser fromDateChooser;
	private JDateChooser toDateChooser;
	private JLabel lblTotalDebitValue;
	private JLabel lblNetProfitValue;
	private JLabel lblNetLossValue;
	private JLabel lblTotalCreditValue;
	

	public ProfitLossStatementUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Profit Loss Report");
		getContentPane().setLayout(null);
		MyStoreDetails myStore = MyStoreServices.getMyStoreDetails();
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		mainPanel.setBounds(174, 22, 878, 568);
		getContentPane().add(mainPanel);
		mainPanel.setLayout(null);
		Border border = new LineBorder(Color.black, 1);
		
		JLabel lblStoreName = new JLabel(myStore.getStoreName());
		lblStoreName.setHorizontalAlignment(SwingConstants.CENTER);
		lblStoreName.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblStoreName.setBounds(197, 11, 444, 24);
		mainPanel.add(lblStoreName);
		
		JLabel lblStoreAddress = new JLabel(myStore.getAddress()+", "+myStore.getAddress2()+", "+myStore.getCity());
		lblStoreAddress.setHorizontalAlignment(SwingConstants.CENTER);
		lblStoreAddress.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblStoreAddress.setBounds(197, 36, 444, 24);
		mainPanel.add(lblStoreAddress);
		
		JButton btnNewButton = new JButton("Get Report");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getReport();
			}
		});
		btnNewButton.setBounds(638, 90, 105, 25);
		mainPanel.add(btnNewButton);
		
		JLabel lblFromDate = new JLabel("From Date:");
		lblFromDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFromDate.setBounds(109, 90, 92, 25);
		mainPanel.add(lblFromDate);
		
		JLabel lblToDate = new JLabel("To Date:");
		lblToDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblToDate.setBounds(348, 90, 92, 25);
		mainPanel.add(lblToDate);
		
		fromDateChooser = new JDateChooser();
		fromDateChooser.setBounds(205, 90, 133, 25);
		fromDateChooser.setFont(new Font("Tahoma", Font.BOLD, 12));
		fromDateChooser.setMaxSelectableDate(new Date());
		JTextFieldDateEditor editor = (JTextFieldDateEditor) fromDateChooser.getDateEditor();
		editor.setEditable(false);
		fromDateChooser.setDate(new Date());
		mainPanel.add(fromDateChooser);
		
		toDateChooser = new JDateChooser();
		toDateChooser.setBounds(450, 90, 133, 25);
		toDateChooser.setFont(new Font("Tahoma", Font.BOLD, 12));
		toDateChooser.setMaxSelectableDate(new Date());
		toDateChooser.setDate(new Date());
		JTextFieldDateEditor toEditor = (JTextFieldDateEditor) toDateChooser.getDateEditor();
		toEditor.setEditable(false);
		mainPanel.add(toDateChooser);
		
		JPanel debitPanel = new JPanel();
		debitPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		debitPanel.setBounds(45, 468, 388, 75);
		mainPanel.add(debitPanel);
		debitPanel.setLayout(null);
		
		JLabel lblNetProfit = new JLabel("Net Profit : ");
		lblNetProfit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNetProfit.setBounds(10, 11, 113, 25);
		debitPanel.add(lblNetProfit);
		
		lblNetProfitValue = new JLabel("");
		lblNetProfitValue.setForeground(new Color(34, 139, 34));
		lblNetProfitValue.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNetProfitValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNetProfitValue.setBounds(200, 11, 178, 25);
		debitPanel.add(lblNetProfitValue);
		
		JLabel lblTotalDebit = new JLabel("Total : ");
		lblTotalDebit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalDebit.setBounds(10, 47, 113, 25);
		debitPanel.add(lblTotalDebit);
		
		lblTotalDebitValue = new JLabel("");
		lblTotalDebitValue.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotalDebitValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalDebitValue.setBounds(200, 47, 178, 25);
		debitPanel.add(lblTotalDebitValue);
		
		JPanel creditPanel = new JPanel();
		creditPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		creditPanel.setBounds(443, 468, 386, 75);
		mainPanel.add(creditPanel);
		creditPanel.setLayout(null);
		
		JLabel lblNetLoss = new JLabel("Net Loss : ");
		lblNetLoss.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNetLoss.setBounds(10, 11, 115, 25);
		creditPanel.add(lblNetLoss);
		
		JLabel lblTotalCredit = new JLabel("Total : ");
		lblTotalCredit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalCredit.setBounds(10, 47, 115, 25);
		creditPanel.add(lblTotalCredit);
		
		lblTotalCreditValue = new JLabel("");
		lblTotalCreditValue.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotalCreditValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalCreditValue.setBounds(200, 47, 178, 25);
		creditPanel.add(lblTotalCreditValue);
		
		lblNetLossValue = new JLabel("");
		lblNetLossValue.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNetLossValue.setForeground(Color.RED);
		lblNetLossValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNetLossValue.setBounds(200, 11, 178, 25);
		creditPanel.add(lblNetLossValue);
		
		JScrollPane debitScrollPane = new JScrollPane();
		debitScrollPane.setBounds(45, 135, 386, 322);
		mainPanel.add(debitScrollPane);
		
		debitTable = new JTable();
		debitModel = new DefaultTableModel(){
			 boolean[] columnEditables = new boolean[] {
					 false, false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
		 debitModel.setColumnIdentifiers(new String[] {
				 "Debit Details", "Amount",}
	       );
		 debitTable.setModel(debitModel);
		//Table Row Height 
		 debitTable.setFont(new Font("Tahoma", Font.PLAIN, 14));
		 debitTable.setRowHeight(20);
		 //Header
		 JTableHeader header = debitTable.getTableHeader();
		 header.setFont(new Font("Tahoma", Font.BOLD, 14));
		 header.setBackground(Color.GRAY);
		 header.setForeground(Color.WHITE);
		 
		debitScrollPane.setViewportView(debitTable);
		
		JScrollPane creditScrollPane = new JScrollPane();
		creditScrollPane.setBounds(443, 135, 386, 322);
		mainPanel.add(creditScrollPane);
		
		creditTable = new JTable();
		creditScrollPane.setViewportView(creditTable);
		creditModel = new DefaultTableModel(){
			 boolean[] columnEditables = new boolean[] {
					 false, false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
		 creditModel.setColumnIdentifiers(new String[] {
				 "Credit Details", "Amount",}
	       );
		//Table Row Height 
		 creditTable.setFont(new Font("Tahoma", Font.PLAIN, 14));
		 creditTable.setRowHeight(20);
		 //Header
		 JTableHeader header2 = creditTable.getTableHeader();
		 header2.setFont(new Font("Tahoma", Font.BOLD, 14));
		 header2.setBackground(Color.GRAY);
		 header2.setForeground(Color.WHITE);
		 creditTable.setModel(creditModel);
		 DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		 rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		 creditTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		 debitTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		 getReport();
		 
		 ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}

	/*protected void getReport() {
		if(fromDateChooser.getDate()==null || toDateChooser.getDate()==null){
			JOptionPane.showMessageDialog(getContentPane(), "Please select Date Range !");
		}else if(fromDateChooser.getDate().compareTo(toDateChooser.getDate())>0){
			JOptionPane.showMessageDialog(getContentPane(), "From Date should be less than To Date !");
		}else{
			ProfitLossDetails report = ReportServices.getProfitLossStatment(fromDateChooser.getDate()==null?null:new java.sql.Date(fromDateChooser.getDate().getTime()),toDateChooser.getDate()==null?null:new java.sql.Date(toDateChooser.getDate().getTime()));
			if(report==null){
				JOptionPane.showMessageDialog(getContentPane(), "Opening Stock Amount not available for selected date range !");
			}else{
				debitModel.setRowCount(0);
				creditModel.setRowCount(0);
				for(ProfitLossData p: report.getDebit()){
					debitModel.addRow(new Object[]{p.getDescription(),PDFUtils.getAmountFormat(p.getAmount())});
				}
				for(ProfitLossData p: report.getCredit()){
					creditModel.addRow(new Object[]{p.getDescription(),PDFUtils.getAmountFormat(p.getAmount())});
				}
				lblNetLossValue.setText(PDFUtils.getAmountFormat(report.getNetLoss()));
				lblNetProfitValue.setText(PDFUtils.getAmountFormat(report.getNetProfit()));
				lblTotalCreditValue.setText(PDFUtils.getAmountFormat(report.getTotalCredit()));
				lblTotalDebitValue.setText(PDFUtils.getAmountFormat(report.getTotalDebit()));
			}
		}
	}*/
	
	protected void getReport() {
		if(fromDateChooser.getDate()==null || toDateChooser.getDate()==null){
			JOptionPane.showMessageDialog(getContentPane(), "Please select Date Range !");
		}else if(fromDateChooser.getDate().compareTo(toDateChooser.getDate())>0){
			JOptionPane.showMessageDialog(getContentPane(), "From Date should be less than To Date !");
		}else{
			ProfitLossDetails report = ReportServices.getProfitLossReport(fromDateChooser.getDate()==null?null:new java.sql.Date(fromDateChooser.getDate().getTime()),toDateChooser.getDate()==null?null:new java.sql.Date(toDateChooser.getDate().getTime()));
				debitModel.setRowCount(0);
				creditModel.setRowCount(0);
				debitModel.addRow(new Object[]{"Expense Details",""});
				for(ProfitLossData p: report.getDebit()){
					debitModel.addRow(new Object[]{"  - "+p.getDescription(),PDFUtils.getAmountFormat(p.getAmount())});
				}
				for(ProfitLossData p: report.getCredit()){
					creditModel.addRow(new Object[]{p.getDescription(),PDFUtils.getAmountFormat(p.getAmount())});
				}
				lblNetLossValue.setText(PDFUtils.getAmountFormat(report.getNetLoss()));
				lblNetProfitValue.setText(PDFUtils.getAmountFormat(report.getNetProfit()));
				lblTotalCreditValue.setText(PDFUtils.getAmountFormat(report.getTotalCredit()));
				lblTotalDebitValue.setText(PDFUtils.getAmountFormat(report.getTotalDebit()));
		}
	}
}
