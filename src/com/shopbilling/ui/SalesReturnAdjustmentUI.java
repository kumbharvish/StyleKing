package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.shopbilling.dto.ReturnDetails;
import com.shopbilling.utils.PDFUtils;

public class SalesReturnAdjustmentUI extends JDialog {

	private JPanel contentPane;
	private JTextField tf_BillNumber;
	private JTextField tf_BillDate;
	private JTextField tf_oldSalesAmt;
	private JTextField tf_TotalReturnAmt;
	private JTextField tf_newBillAmt;

	/**
	 * Create the frame.
	 */
	public SalesReturnAdjustmentUI(ReturnDetails bill,JFrame frame) {
		super(frame,"Sales Return Bill Amount Adjustment",true);
		setTitle("Sales Return Bill Amount Adjustment");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("shop32X32.png"));
		setBounds(450, 200, 419, 297);
		setResizable(false);
		Color lablColor = Color.gray;
		Color valueColor = Color.DARK_GRAY;
		Font valueFont = new Font("Dialog",Font.BOLD,13);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBounds(10, 14, 393, 245);
		contentPane.add(panel);
		panel.setLayout(null);
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Payment Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(17, 11, 359, 218);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblBillNo = new JLabel("Bill Number ");
		lblBillNo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBillNo.setBounds(10, 25, 173, 25);
		lblBillNo.setBorder(border);
		lblBillNo.setForeground(lablColor);
		panel_1.add(lblBillNo);
		
		tf_BillNumber = new JTextField();
		tf_BillNumber.setEnabled(false);
		tf_BillNumber.setEditable(false);
		tf_BillNumber.setBounds(182, 25, 167, 25);
		tf_BillNumber.setBorder(border);
		tf_BillNumber.setText(" "+String.valueOf(bill.getBillNumber()));
		tf_BillNumber.setFont(valueFont);
		tf_BillNumber.setDisabledTextColor(valueColor);
		panel_1.add(tf_BillNumber);
		tf_BillNumber.setColumns(10);
		
		JLabel lblBillDate = new JLabel("Bill Date ");
		lblBillDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBillDate.setBorder(border);
		lblBillDate.setBounds(10, 49, 173, 25);
		lblBillDate.setForeground(lablColor);

		panel_1.add(lblBillDate);
		
		tf_BillDate = new JTextField();
		tf_BillDate.setEditable(false);
		tf_BillDate.setEnabled(false);
		tf_BillDate.setColumns(10);
		tf_BillDate.setBounds(182, 49, 167, 25);
		tf_BillDate.setBorder(border);
		tf_BillDate.setText(" "+ PDFUtils.getFormattedDate(bill.getBillDate()));
		tf_BillDate.setFont(valueFont);
		tf_BillDate.setDisabledTextColor(valueColor);
		panel_1.add(tf_BillDate);
		
		JLabel lblNoOfItems = new JLabel("Old Bill Amount ");
		lblNoOfItems.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNoOfItems.setBounds(10, 85, 173, 25);
		lblNoOfItems.setBorder(border);
		lblNoOfItems.setForeground(lablColor);
		panel_1.add(lblNoOfItems);
		
		JLabel lblTotalQuantity = new JLabel("Total Return Amount ");
		lblTotalQuantity.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalQuantity.setBounds(10, 109, 173, 25);
		lblTotalQuantity.setBorder(border);
		lblTotalQuantity.setForeground(lablColor);
		panel_1.add(lblTotalQuantity);
		
		JLabel lblSubTotal = new JLabel("New Bill Amount ");
		lblSubTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSubTotal.setBounds(10, 133, 173, 25);
		lblSubTotal.setBorder(border);
		lblSubTotal.setForeground(lablColor);
		panel_1.add(lblSubTotal);
		
		tf_oldSalesAmt = new JTextField();
		tf_oldSalesAmt.setEnabled(false);
		tf_oldSalesAmt.setEditable(false);
		tf_oldSalesAmt.setColumns(10);
		tf_oldSalesAmt.setBounds(182, 85, 167, 25);
		tf_oldSalesAmt.setBorder(border);
		tf_oldSalesAmt.setText(" "+PDFUtils.getDecimalFormat(bill.getBillNetSalesAmt()));
		tf_oldSalesAmt.setFont(valueFont);
		tf_oldSalesAmt.setDisabledTextColor(valueColor);
		panel_1.add(tf_oldSalesAmt);
		
		tf_TotalReturnAmt = new JTextField();
		tf_TotalReturnAmt.setEnabled(false);
		tf_TotalReturnAmt.setEditable(false);
		tf_TotalReturnAmt.setColumns(10);
		tf_TotalReturnAmt.setBounds(182, 109, 167, 25);
		tf_TotalReturnAmt.setBorder(border);
		tf_TotalReturnAmt.setText(" "+PDFUtils.getDecimalFormat(bill.getTotalAmount()));
		tf_TotalReturnAmt.setFont(valueFont);
		tf_TotalReturnAmt.setDisabledTextColor(valueColor);
		panel_1.add(tf_TotalReturnAmt);
		
		tf_newBillAmt = new JTextField();
		tf_newBillAmt.setEnabled(false);
		tf_newBillAmt.setEditable(false);
		tf_newBillAmt.setColumns(10);
		tf_newBillAmt.setBounds(182, 133, 167, 25);
		tf_newBillAmt.setBorder(border);
		tf_newBillAmt.setText(" "+PDFUtils.getDecimalFormat(bill.getNewBillnetSalesAmt()));
		tf_newBillAmt.setFont(valueFont);
		tf_newBillAmt.setDisabledTextColor(valueColor);
		panel_1.add(tf_newBillAmt);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnOk.setBounds(132, 169, 69, 35);
		panel_1.add(btnOk);
	}
}
