package com.shopbilling.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.ReturnDetails;
import com.shopbilling.services.JasperServices;
import com.shopbilling.services.SalesReturnServices;
import com.shopbilling.utils.JasperUtils;
import com.shopbilling.utils.PDFUtils;
import com.toedter.calendar.JDateChooser;

public class SalesReturnReportExportUI extends JInternalFrame {
	private DefaultTableModel reportModel;
	private JDateChooser toDateChooser;
	private JDateChooser fromDateChooser;
	private int totalCustomerCount=0;
	private int totalNoOfItems=0;
	private int totalQty=0;
	private double totalDiscAmt=0;
	private double pendingPayment=0;
	private double cashPayment=0;
	private double totalAmount=0;
	private JFrame parentFrame;
	/**
	 * Create the frame.
	 */
	public SalesReturnReportExportUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Sales Return Report");
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Report Date", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 64, 1150, 107);
		getContentPane().add(panel);
		panel.setLayout(null);
		parentFrame = (JFrame)this.getTopLevelAncestor();
		JLabel lblFromDate = new JLabel("From Date :");
		lblFromDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFromDate.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblFromDate.setBounds(133, 39, 99, 28);
		panel.add(lblFromDate);
		
		JLabel lblToDate = new JLabel("To Date :");
		lblToDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblToDate.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblToDate.setBounds(573, 39, 79, 28);
		panel.add(lblToDate);
		
		JButton btnGetReport = new JButton("Export");
		btnGetReport.setIcon(new ImageIcon(SalesReportExportUI.class.getResource("/images/pdf.png")));
		btnGetReport.setBackground(UIManager.getColor("Button.background"));
		btnGetReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillReportTable();
			}
		});
		btnGetReport.setBounds(931, 36, 158, 45);
		panel.add(btnGetReport);
		
		fromDateChooser = new JDateChooser();
		fromDateChooser.setFont(new Font("Tahoma", Font.BOLD, 12));
		fromDateChooser.setBounds(242, 39, 133, 28);
		fromDateChooser.setDate(new Date());
		
		panel.add(fromDateChooser);
		
		toDateChooser = new JDateChooser();
		toDateChooser.setFont(new Font("Tahoma", Font.BOLD, 12));
		toDateChooser.setBounds(662, 39, 133, 28);
		toDateChooser.setDate(new Date());
		panel.add(toDateChooser);

		ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
	
	//Fill Report Table
	private void fillReportTable(){
		List<ReturnDetails> returnList= SalesReturnServices.getReturnDetails(fromDateChooser.getDate()==null?null:new java.sql.Date(fromDateChooser.getDate().getTime()),toDateChooser.getDate()==null?null:new java.sql.Date(toDateChooser.getDate().getTime()),null);
		calculateConsolidateValues(returnList);
		if(returnList.isEmpty()){
			JOptionPane.showMessageDialog(getContentPane(), "No Records found for the given period !");
		}else{
			List<Map<String,?>> dataSourceMap = JasperServices.createDateForSalesReturnReport(returnList,fromDateChooser.getDate()!=null?PDFUtils.getFormattedDate(fromDateChooser.getDate()):"___",toDateChooser.getDate()!=null?PDFUtils.getFormattedDate(toDateChooser.getDate()):"___",pendingPayment,cashPayment,totalAmount,totalQty,totalNoOfItems);
			boolean isSucess=JasperUtils.createPDF(dataSourceMap,AppConstants.SALES_RETURN_REPORT_JASPER,"Sales_Return_Report");
			if(isSucess){
				JOptionPane.showMessageDialog(getContentPane(), "Report Exported Successfully!");
			}
		}
	}

	private void calculateConsolidateValues(List<ReturnDetails> returnList) {
		totalCustomerCount =0;
		totalDiscAmt=0;
		totalNoOfItems=0;
		totalQty=0;
		cashPayment=0;
		pendingPayment=0;
		totalAmount=0;
		
		for(ReturnDetails bill: returnList){
			totalCustomerCount=returnList.size();
			totalDiscAmt+=bill.getDiscountAmount();
			totalNoOfItems+=bill.getNoOfItems();
			totalQty+=bill.getTotalQuanity();
			if("CASH".equals(bill.getReturnpaymentMode())){
				cashPayment+=bill.getTotalAmount();
			}else if("PENDING".equals(bill.getReturnpaymentMode())){
				pendingPayment+=bill.getTotalAmount();
			}
			totalAmount+=bill.getTotalAmount();
		}
	}
	
}
