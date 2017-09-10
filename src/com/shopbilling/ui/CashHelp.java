package com.shopbilling.ui;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.shopbilling.utils.PDFUtils;


public class CashHelp extends JDialog {

	private JPanel contentPane;
	private JTextField tf_billAmt;
	private JTextField tf_TenderAmt;
	private JTextField tf_balance;
	private JButton btnOk;

	public CashHelp(JFrame frame,final String netSaleAmt) {
		super(frame,"Cash Help",true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("shop32X32.png"));
		setBounds(450, 100, 416, 356);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.requestFocus();
		JLabel lblBillAmount = new JLabel("Bill Amount");
		lblBillAmount.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblBillAmount.setBounds(149, 11, 101, 28);
		contentPane.add(lblBillAmount);
		
		tf_billAmt = new JTextField();
		tf_billAmt.setEditable(false);
		tf_billAmt.setBounds(114, 50, 171, 33);
		contentPane.add(tf_billAmt);
		tf_billAmt.setColumns(10);
		tf_billAmt.setText(netSaleAmt);
		tf_billAmt.setFont(new Font("Dialog", Font.BOLD, 30));
		tf_billAmt.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblTenderAmount = new JLabel("Tender Amount");
		lblTenderAmount.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTenderAmount.setBounds(132, 94, 136, 28);
		contentPane.add(lblTenderAmount);
		
		tf_TenderAmt = new JTextField();
		
		tf_TenderAmt.setColumns(10);
		tf_TenderAmt.setBounds(114, 129, 171, 33);
		tf_TenderAmt.setFont(new Font("Dialog", Font.BOLD, 30));
		tf_TenderAmt.setHorizontalAlignment(SwingConstants.CENTER);
		tf_TenderAmt.requestFocus();
		this.addWindowListener( new WindowAdapter() {
		    public void windowOpened( WindowEvent e ){
		    	tf_TenderAmt.requestFocus();
		    }
		});
		contentPane.add(tf_TenderAmt);
		
		JLabel lblBalance = new JLabel("Balance");
		lblBalance.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblBalance.setBounds(164, 178, 71, 28);
		contentPane.add(lblBalance);
		
		tf_balance = new JTextField();
		tf_balance.setEditable(false);
		tf_balance.setColumns(10);
		tf_balance.setBounds(114, 217, 171, 33);
		tf_balance.setFont(new Font("Dialog", Font.BOLD, 30));
		tf_balance.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(tf_balance);
		
		btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		tf_TenderAmt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(!tf_TenderAmt.getText().equals("")){
					double balance = Double.valueOf(tf_TenderAmt.getText())- Double.valueOf(netSaleAmt);
					tf_balance.setText(PDFUtils.getDecimalFormat(balance));
				}else{
					tf_balance.setText("0.00");
				}
				
			}
			public void keyTyped(KeyEvent e) {
			      char c = e.getKeyChar();
			      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
			         e.consume();  // ignore event
			      }
			   }
		});
		btnOk.setBounds(149, 268, 89, 38);
		contentPane.add(btnOk);
	}
}
