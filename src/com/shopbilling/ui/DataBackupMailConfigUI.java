package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.dto.MailConfigDTO;
import com.shopbilling.dto.StatusDTO;
import com.shopbilling.utils.MailConfigurationServices;
import com.shopbilling.utils.PDFUtils;

public class DataBackupMailConfigUI extends JInternalFrame {

	private JPanel contentPane;
	private JTextField tf_mailFrom;
	private JTextField tf_dataBkpMailID;
	private JTextField tf_subject;
	private JTextField tf_message;
	private JPasswordField tf_password;
	private int configId;
	private JRadioButton rdbtnNo;
	private JRadioButton rdbtnYes;

	public DataBackupMailConfigUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Data Backup Mail Configuration");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(294, 41, 529, 402);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblMailFromId = new JLabel("Mail From ID *: ");
		lblMailFromId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMailFromId.setBounds(10, 40, 149, 24);
		panel_1.add(lblMailFromId);
		
		tf_mailFrom = new JTextField();
		tf_mailFrom.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_mailFrom.setBounds(182, 40, 310, 25);
		panel_1.add(tf_mailFrom);
		tf_mailFrom.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password *: ");
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setBounds(10, 88, 149, 24);
		panel_1.add(lblPassword);
		
		JLabel lblDataBackupMail = new JLabel("Data Backup Mail ID *: ");
		lblDataBackupMail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDataBackupMail.setBounds(10, 137, 149, 24);
		panel_1.add(lblDataBackupMail);
		
		tf_dataBkpMailID = new JTextField();
		tf_dataBkpMailID.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_dataBkpMailID.setColumns(10);
		tf_dataBkpMailID.setBounds(182, 137, 310, 25);
		panel_1.add(tf_dataBkpMailID);
		
		JLabel lblMailSubject = new JLabel("Subject *: ");
		lblMailSubject.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMailSubject.setBounds(10, 186, 149, 24);
		panel_1.add(lblMailSubject);
		
		tf_subject = new JTextField();
		tf_subject.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_subject.setColumns(10);
		tf_subject.setBounds(182, 186, 310, 25);
		panel_1.add(tf_subject);
		
		JLabel lblMessage = new JLabel("Message : ");
		lblMessage.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMessage.setBounds(10, 233, 149, 24);
		panel_1.add(lblMessage);
		
		tf_message = new JTextField();
		tf_message.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_message.setColumns(10);
		tf_message.setBounds(182, 233, 310, 25);
		panel_1.add(tf_message);
		
		tf_password = new JPasswordField();
		tf_password.setBounds(182, 88, 310, 25);
		panel_1.add(tf_password);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdateAction();
			}
		});
		btnUpdate.setBounds(211, 341, 116, 34);
		panel_1.add(btnUpdate);
		
		JLabel lblDoYouWant = new JLabel("Do you want to send Data backup on Mail?");
		lblDoYouWant.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDoYouWant.setBounds(10, 281, 251, 24);
		panel_1.add(lblDoYouWant);
		ButtonGroup bg  = new ButtonGroup();
		
		rdbtnYes = new JRadioButton("Yes");
		rdbtnYes.setBounds(318, 282, 60, 23);
		panel_1.add(rdbtnYes);
		
		
		rdbtnNo = new JRadioButton("No");
		rdbtnNo.setBounds(394, 282, 52, 23);
		panel_1.add(rdbtnNo);
		setResizable(false);
		bg.add(rdbtnYes);
		bg.add(rdbtnNo);
		rdbtnYes.setSelected(false);
		rdbtnNo.setSelected(false);
		JPanel panel = new JPanel();
		panel.setBounds(148, 28, 804, 548);
		panel.setLayout(null);
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		setMailConfigDetails();
		
		ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
				
	}
	
	public void setMailConfigDetails(){
		MailConfigDTO mail = MailConfigurationServices.getMailConfig();
		
		tf_mailFrom.setText(mail.getMailFrom());
		tf_password.setText(mail.getPassword());
		tf_dataBkpMailID.setText(mail.getMailTo());
		tf_subject.setText(mail.getMailSubject());
		tf_message.setText(mail.getMailMessage());
		configId = mail.getConfigId();
		if("Y".equals(mail.getIsEnabled())){
			rdbtnYes.setSelected(true);
		}else{
			rdbtnNo.setSelected(true);
		}
	}

	private void UpdateAction() {
		if(PDFUtils.isMandatoryEntered(tf_mailFrom)&&PDFUtils.isMandatoryEntered(tf_password)
				&& PDFUtils.isMandatoryEntered(tf_dataBkpMailID)&&PDFUtils.isMandatoryEntered(tf_subject)){
			MailConfigDTO mail = new MailConfigDTO();
			mail.setMailFrom(tf_mailFrom.getText());
			mail.setPassword(tf_password.getText());
			mail.setMailTo(tf_dataBkpMailID.getText());
			mail.setMailSubject(tf_subject.getText());
			mail.setMailMessage(tf_message.getText());
			mail.setConfigId(configId);
			if(rdbtnYes.isSelected()){
				mail.setIsEnabled("Y");
			}
			if(rdbtnNo.isSelected()){
				mail.setIsEnabled("N");
			}
			StatusDTO status = MailConfigurationServices.updateMailConfig(mail);
			if(status.getStatusCode()==0){
				JOptionPane.showMessageDialog(contentPane, "Mail Configurations Updated Successfully!");
				setMailConfigDetails();
			}else{
				JOptionPane.showMessageDialog(null, "Exception occured ", "Error", JOptionPane.WARNING_MESSAGE);
			}
		}else{
			JOptionPane.showMessageDialog(null, "Please enter mandatory fields ", "Mandatory Fields", JOptionPane.WARNING_MESSAGE);
		}
	}
}
