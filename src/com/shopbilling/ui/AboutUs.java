package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.utils.PDFUtils;

public class AboutUs extends JInternalFrame {
	private JLabel textField;
	private JLabel textField_1;
	private JLabel textField_2;
	private JLabel txtVishalKumbhar;
	private JLabel txtKumbharvishgmailcom;
	private JLabel textField_3;
	private JLabel textField_4;

	public AboutUs() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("About Us");
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(195, 110, 705, 317);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblMyStoreSoftware = new JLabel("My Store Software");
		lblMyStoreSoftware.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblMyStoreSoftware.setHorizontalAlignment(SwingConstants.CENTER);
		lblMyStoreSoftware.setBounds(204, 27, 296, 25);
		panel.add(lblMyStoreSoftware);
		
		JLabel lblLicensed = new JLabel("Licensed : ");
		lblLicensed.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblLicensed.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLicensed.setForeground(Color.GRAY);
		lblLicensed.setBounds(167, 76, 120, 30);
		panel.add(lblLicensed);
		
		textField = new JLabel();
		textField.setText(" "+PDFUtils.getAppDataValues("LICENSED_TO").get(0));
		textField.setFont(new Font("Tahoma", Font.BOLD, 17));
		textField.setEnabled(false);
		textField.setBounds(286, 76, 357, 30);
		panel.add(textField);
		
		JLabel lblValidUpto = new JLabel("Valid Upto : ");
		lblValidUpto.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblValidUpto.setHorizontalAlignment(SwingConstants.RIGHT);
		lblValidUpto.setForeground(Color.GRAY);
		lblValidUpto.setBounds(167, 105, 120, 30);
		panel.add(lblValidUpto);
		
		JLabel lblVersion = new JLabel("Version : ");
		lblVersion.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblVersion.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVersion.setForeground(Color.GRAY);
		lblVersion.setBounds(167, 129, 120, 30);
		panel.add(lblVersion);
		
		textField_1 = new JLabel();
		textField_1.setText(" 1.14.2");
		textField_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		textField_1.setEnabled(false);
		textField_1.setBounds(286, 129, 161, 30);
		panel.add(textField_1);
		
		JLabel lblCopyright = new JLabel("Copyright : ");
		lblCopyright.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblCopyright.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCopyright.setForeground(Color.GRAY);
		lblCopyright.setBounds(167, 153, 120, 30);
		panel.add(lblCopyright);
		
		textField_2 = new JLabel();
		textField_2.setIcon(new ImageIcon(AboutUs.class.getResource("/images/copyright.png")));
		textField_2.setText(" "+"2017");
		textField_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		textField_2.setEnabled(false);
		textField_2.setBounds(286, 153, 161, 30);
		panel.add(textField_2);
		
		JLabel lblDevelopedBy = new JLabel("Developed by : ");
		lblDevelopedBy.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblDevelopedBy.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDevelopedBy.setForeground(Color.GRAY);
		lblDevelopedBy.setBounds(167, 177, 120, 30);
		panel.add(lblDevelopedBy);
		
		txtVishalKumbhar = new JLabel();
		txtVishalKumbhar.setText(" Vishal Kumbhar / Adhir Shishupal");
		txtVishalKumbhar.setFont(new Font("Tahoma", Font.BOLD, 15));
		txtVishalKumbhar.setEnabled(false);
		txtVishalKumbhar.setBounds(286, 177, 342, 30);
		panel.add(txtVishalKumbhar);
		
		JLabel lblEmail = new JLabel("Email : ");
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEmail.setForeground(Color.GRAY);
		lblEmail.setBounds(167, 201, 120, 30);
		panel.add(lblEmail);
		
		txtKumbharvishgmailcom = new JLabel();
		txtKumbharvishgmailcom.setText(" "+"kumbharvish@gmail.com");
		txtKumbharvishgmailcom.setFont(new Font("Tahoma", Font.BOLD, 15));
		txtKumbharvishgmailcom.setEnabled(false);
		txtKumbharvishgmailcom.setBounds(286, 201, 267, 30);
		panel.add(txtKumbharvishgmailcom);
		String validUpto="";
		try {
			validUpto = PDFUtils.dec(PDFUtils.getAppDataValues("APP_SECURE_KEY").get(0));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		textField_3 = new JLabel();
		textField_3.setText(" "+validUpto);
		textField_3.setFont(new Font("Tahoma", Font.BOLD, 15));
		textField_3.setEnabled(false);
		textField_3.setBounds(286, 105, 161, 30);
		panel.add(textField_3);
		
		JLabel lblMobileNo = new JLabel("Mobile No. : ");
		lblMobileNo.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblMobileNo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMobileNo.setForeground(Color.GRAY);
		lblMobileNo.setBounds(167, 227, 120, 30);
		panel.add(lblMobileNo);
		
		textField_4 = new JLabel();
		textField_4.setText(" +91 8149880299 / +91 9579616107");
		textField_4.setFont(new Font("Tahoma", Font.BOLD, 15));
		textField_4.setEnabled(false);
		textField_4.setBounds(286, 227, 373, 30);
		panel.add(textField_4);
		
		JLabel lblbg = new JLabel("");
		lblbg.setIcon(new ImageIcon(AboutUs.class.getResource("/images/AboutUsBG.png")));
		lblbg.setBounds(10, 24, 685, 282);
		panel.add(lblbg);
		
		ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
}
