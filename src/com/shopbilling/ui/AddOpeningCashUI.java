package com.shopbilling.ui;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.shopbilling.dto.StatusDTO;
import com.shopbilling.services.BillingServices;
import com.shopbilling.services.ReportServices;
import com.shopbilling.utils.PDFUtils;

public class AddOpeningCashUI extends JDialog {

	private JPanel contentPane;
	private JTextField tf_openingCash;
	private JTextField tf_newOpeningCash;

	/**
	 * Create the frame.
	 */
	public AddOpeningCashUI(JFrame frame,final CashCounterUI cash) {
		super(frame,"Add Opening Cash",true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("shop32X32.png"));
		setBounds(400, 200, 391, 249);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblOpeningCash = new JLabel("Opening Cash Amount: ");
		lblOpeningCash.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOpeningCash.setBounds(10, 52, 163, 25);
		contentPane.add(lblOpeningCash);
		
		tf_openingCash = new JTextField();
		tf_openingCash.setEditable(false);
		tf_openingCash.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_openingCash.setFont(new Font("Tahoma", Font.BOLD, 15));
		tf_openingCash.setBounds(183, 52, 138, 25);
		contentPane.add(tf_openingCash);
		tf_openingCash.setColumns(10);
		
		JLabel lblNewOpeningCash = new JLabel("New Opening Cash Amount: ");
		lblNewOpeningCash.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewOpeningCash.setBounds(10, 99, 163, 25);
		contentPane.add(lblNewOpeningCash);
		
		tf_newOpeningCash = new JTextField();
		tf_newOpeningCash.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_newOpeningCash.setFont(new Font("Tahoma", Font.BOLD, 15));
		tf_newOpeningCash.setColumns(10);
		tf_newOpeningCash.setBounds(183, 99, 138, 25);
		contentPane.add(tf_newOpeningCash);
		
		Double openCash = ReportServices.getOpeningCash(new Date(System.currentTimeMillis()));
		if(openCash!=null){
			tf_openingCash.setText(PDFUtils.getDecimalFormat(openCash)); 
		}else{
			tf_openingCash.setText("0.00");
			BillingServices.addOpeningCash(0);
		}
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(PDFUtils.isMandatoryEntered(tf_newOpeningCash)){
					double newopenCash = Double.valueOf(tf_newOpeningCash.getText());
					StatusDTO status = BillingServices.updateOpeningCash(newopenCash, new Date(System.currentTimeMillis()));
					if(status.getStatusCode()==0){
						dispose();
						cash.setCashCounterDetails();
						
					}
				}else{
					JOptionPane.showMessageDialog(contentPane, "Please enter new opening cash amount !");
				}
			}
		});
		btnUpdate.setBounds(162, 169, 89, 30);
		contentPane.add(btnUpdate);
		
	}
}
