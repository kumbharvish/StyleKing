package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.LabelAlignment;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.dto.StatusDTO;
import com.shopbilling.dto.UserDetails;
import com.shopbilling.services.UserServices;
import com.shopbilling.utils.PDFUtils;

public class ManageAccount extends JInternalFrame {

	private JPanel contentPane;
	private UserDetails userDetails;
	private JTextField tf_FirstName;
	private JTextField tf_LastName;
	private JTextField tf_MobileNo;
	private JTextField tf_Email;
	private JPasswordField tf_ExistingPwd;
	private JPasswordField tf_NewPwd;
	private JPasswordField tf_ConfirmPwd;
	private JLabel tf_ExistingUN;
	private JTextField tf_NewUserName;
	
	private JRadioButton rb_ChangePwd;
	private JRadioButton rb_PersonalDetails;
	private JRadioButton rb_ChangeUserName;
	private JPanel panel_10;
	private JPanel panel_11;
	private JPanel panel_12;

	/**
	 * Create the frame.
	 */
	public ManageAccount(UserDetails user) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		userDetails = UserServices.getUserDetails(user);
		setBounds(200, 85, 1107, 698);
		setTitle("My Account");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setResizable(false);
		//fix the frame location
		//setUndecorated(true);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(15, 21, 1150, 564);
		
		JPanel panelManageAccts = new JPanel();
		tabbedPane.addTab("Manage Account", null, panelManageAccts, null);
		panelManageAccts.setLayout(null);
		
		JPanel panelCustDetails = new JPanel();
		panelCustDetails.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Personal Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelCustDetails.setBounds(37, 11, 507, 249);
		panelManageAccts.add(panelCustDetails);
		panelCustDetails.setLayout(null);
		
		panel_10 = new JPanel();
		DesignGridLayout layout = new DesignGridLayout(panel_10);
		layout.labelAlignment(LabelAlignment.RIGHT);
		tf_Email = new JTextField();
		tf_MobileNo = new JTextField();
		tf_Email.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_Email.setDisabledTextColor(Color.BLACK);
		tf_MobileNo.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_MobileNo.setDisabledTextColor(Color.BLACK);
		//Only Numbers allowed to enter
		tf_MobileNo.addKeyListener(new KeyAdapter() {
			   public void keyTyped(KeyEvent e) {
			      char c = e.getKeyChar();
			      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
			         e.consume();  // ignore event
			      }
			   }
			});
		tf_FirstName = new JTextField();
		tf_FirstName.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_FirstName.setDisabledTextColor(Color.BLACK);
		tf_LastName = new JTextField();
		tf_LastName.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_LastName.setDisabledTextColor(Color.BLACK);
		//Personal Details
 		JButton updateButton = new JButton("Update");
 		layout.row().grid(new JLabel("First Name *:"))	.add(tf_FirstName);
		layout.row().grid(new JLabel("Last Name *:"))	.add(tf_LastName);
		layout.row().grid(new JLabel("Mobile :"))	.add(tf_MobileNo);
		layout.row().grid(new JLabel("Email :"))	.add(tf_Email);
		layout.row().grid(new JLabel(" "));
		layout.emptyRow();
 		layout.row().right().add(updateButton);
 		layout.emptyRow();
		panel_10.setBounds(73, 25, 360, 213);
		panelCustDetails.add(panel_10);
		
		JPanel panelChangePassword = new JPanel();
		panelChangePassword.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Change Password", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelChangePassword.setBounds(37, 271, 507, 254);
		panelManageAccts.add(panelChangePassword);
		panelChangePassword.setLayout(null);
		
		panel_11 = new JPanel();
		panel_11.setBounds(73, 20, 360, 213);
		panelChangePassword.add(panel_11);
		DesignGridLayout layoutChngPwd = new DesignGridLayout(panel_11);
		layoutChngPwd.labelAlignment(LabelAlignment.RIGHT);
		tf_ExistingPwd = new JPasswordField();
		tf_NewPwd = new JPasswordField();
		tf_ConfirmPwd = new JPasswordField();
		//Change Password
		JButton changeButton = new JButton("Change Password");
 		Font noteFont = new Font("Dialog", Font.PLAIN, 12);
 		layoutChngPwd.row().grid(new JLabel("Existing Password *:"))	.add(tf_ExistingPwd);
 		layoutChngPwd.row().grid(new JLabel("New Password *:"))	.add(tf_NewPwd);
 		layoutChngPwd.row().grid(new JLabel("Confirm Password *:"))	.add(tf_ConfirmPwd);
 		layoutChngPwd.row().grid(new JLabel(" "));
 		layoutChngPwd.emptyRow();
 		layoutChngPwd.row().right().add(changeButton);
 		layoutChngPwd.emptyRow();
 		JLabel note = new JLabel("Note :");
 		JLabel noteHeading = new JLabel("Password is case sensitive ");
 		note.setFont(noteFont);
 		noteHeading.setFont(noteFont);
 		layoutChngPwd.row().grid(note).add(noteHeading);
 		
		ButtonGroup bg=new ButtonGroup();
		 
		rb_ChangePwd = new JRadioButton("");
		rb_ChangePwd.setBounds(6, 264, 21, 23);
		panelManageAccts.add(rb_ChangePwd);
		
		rb_ChangeUserName = new JRadioButton("");
		rb_ChangeUserName.setBounds(577, 11, 21, 23);
		panelManageAccts.add(rb_ChangeUserName);
		
		rb_PersonalDetails = new JRadioButton("");
		rb_PersonalDetails.setBounds(6, 6, 21, 23);
		panelManageAccts.add(rb_PersonalDetails);
		contentPane.setLayout(null);
		
		ButtonGroup topBg = new ButtonGroup();
		topBg.add(rb_PersonalDetails);
		topBg.add(rb_ChangePwd);
		topBg.add(rb_ChangeUserName);
		
		JPanel panelChngUserName = new JPanel();
		panelChngUserName.setLayout(null);
		panelChngUserName.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Change UserName", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelChngUserName.setBounds(608, 18, 507, 254);
		panelManageAccts.add(panelChngUserName);
		
		panel_12 = new JPanel();
		panel_12.setBounds(73, 20, 360, 213);
		panelChngUserName.add(panel_12);
		//Change User Name
		DesignGridLayout layoutChngUN = new DesignGridLayout(panel_12);
		layoutChngUN.labelAlignment(LabelAlignment.RIGHT);
		tf_ExistingUN = new JLabel();
		tf_NewUserName = new JTextField();
		tf_NewUserName.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_NewUserName.setDisabledTextColor(Color.BLACK);
		JButton changeUNButton = new JButton("Change UserName");
		layoutChngUN.row().grid(new JLabel("Existing UserName *:")).add(tf_ExistingUN);
		layoutChngUN.row().grid(new JLabel("New UserName *:")).add(tf_NewUserName);
		layoutChngUN.row().grid(new JLabel(" "));
		layoutChngUN.emptyRow();
		layoutChngUN.row().right().add(changeUNButton);
		layoutChngUN.emptyRow();
 		
		//Set personal Details on load
		setPersonalDetails();
				
		rb_PersonalDetails.setSelected(true);
		rb_ChangeUserName.setSelected(false);
		PDFUtils.setEnableRec(panel_11, false);
		rb_ChangePwd.setSelected(false);
		PDFUtils.setEnableRec(panel_12, false);
		
		rb_PersonalDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enableDisablePanel();	
			}
		});
		rb_ChangePwd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enableDisablePanel();
			}
		});
		rb_ChangeUserName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enableDisablePanel();
			}
		});
		
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updatePersonalDetails();
			}
		});
		changeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePassword();
			}
		});
		changeUNButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeUserName();
			}
		});
		
		
		contentPane.add(tabbedPane);
		ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
	
		
	protected void changeUserName() {
		if(!tf_ExistingUN.getText().equals("") && PDFUtils.isMandatoryEntered(tf_NewUserName)){
			UserDetails userDtls = new UserDetails();
			userDtls.setUserId(userDetails.getUserId());
			userDtls.setUserName(tf_ExistingUN.getText());
			StatusDTO status = UserServices.changeUserName(userDtls, tf_NewUserName.getText());
			
			if(status.getStatusCode()==0){
				JOptionPane.showMessageDialog(contentPane, "UserName changed Successfully ");
				resetUserName();
			}else{
				if(status.getException()!=null && status.getException().contains("Duplicate entry")){
					JOptionPane.showMessageDialog(null, "Entered Username already exists.", "Error", JOptionPane.WARNING_MESSAGE);
				}else{
					JOptionPane.showMessageDialog(null, "Error occured ", "Error", JOptionPane.WARNING_MESSAGE);
				}
				//JOptionPane.showMessageDialog(contentPane,"Existing UserName is wrong ","Error",JOptionPane.WARNING_MESSAGE);
			}
		}else{
				JOptionPane.showMessageDialog(contentPane, "Please enter mandatory fields ");
		}
	}


	protected void changePassword() {
		if(!tf_ExistingPwd.getText().equals("") && !tf_NewPwd.getText().equals("") && !tf_ConfirmPwd.getText().equals("")){
			UserDetails userDtls = new UserDetails();
			boolean isUserNameChanged= false;
			userDtls.setUserId(userDetails.getUserId());
			if(!tf_NewPwd.getText().equals(tf_ConfirmPwd.getText())){
				JOptionPane.showMessageDialog(contentPane, "New Password and Confirm Password doesn't match ");
			}else{
				try {
					isUserNameChanged = UserServices.changePassword(userDtls, PDFUtils.enc(tf_ExistingPwd.getText()), PDFUtils.enc(tf_ConfirmPwd.getText()));
					if(isUserNameChanged){
						JOptionPane.showMessageDialog(contentPane, "Password changed Successfully ");
						resetPasswordFields();
					}else{
						JOptionPane.showMessageDialog(contentPane,"Existing Password is wrong ","Error",JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
				JOptionPane.showMessageDialog(contentPane, "Please enter mandatory fields ");
		}
		
	}


	protected void updatePersonalDetails() {
		if(PDFUtils.isMandatoryEntered(tf_FirstName) && PDFUtils.isMandatoryEntered(tf_LastName)){
			UserDetails userDtls = new UserDetails();
			userDtls.setFirstName(tf_FirstName.getText());
			userDtls.setLastName(tf_LastName.getText());
			userDtls.setEmail(tf_Email.getText());
			userDtls.setMobileNo(tf_MobileNo.getText().equals("")?0:Long.valueOf(tf_MobileNo.getText()));
			userDtls.setUserId(userDetails.getUserId());
			boolean isDetailsUpded = UserServices.updatePersonalDetails(userDtls);
			if(isDetailsUpded){
				JOptionPane.showMessageDialog(contentPane, "Personal Details updated Successfully ");
			}else{
				JOptionPane.showMessageDialog(contentPane,"Error occured while updating personal details ","Error",JOptionPane.WARNING_MESSAGE);
			}
		}else{
				JOptionPane.showMessageDialog(contentPane, "Please enter mandatory fields ");
		}
		
	}

	private void setPersonalDetails(){
		tf_FirstName.setText(userDetails.getFirstName());
		tf_LastName.setText(userDetails.getLastName());
		tf_Email.setText(userDetails.getEmail());
		tf_MobileNo.setText(userDetails.getMobileNo()==0?"":String.valueOf(userDetails.getMobileNo()));
		tf_ExistingUN.setText(userDetails.getUserName());
	}
	
	private void resetPasswordFields(){
		tf_ConfirmPwd.setText("");
		tf_NewPwd.setText("");
		tf_ExistingPwd.setText("");
		
	}
	private void resetUserName(){
		tf_ExistingUN.setText(tf_NewUserName.getText());
		tf_NewUserName.setText("");
		
	}

	protected void enableDisablePanel() {
		if(rb_PersonalDetails.isSelected()){
			PDFUtils.setEnableRec(panel_10, true);
			PDFUtils.setEnableRec(panel_11, false);
			PDFUtils.setEnableRec(panel_12, false);
		}
		if(rb_ChangePwd.isSelected()){
			PDFUtils.setEnableRec(panel_11, true);
			PDFUtils.setEnableRec(panel_10, false);
			PDFUtils.setEnableRec(panel_12, false);
		}
		if(rb_ChangeUserName.isSelected()){
			PDFUtils.setEnableRec(panel_12, true);
			PDFUtils.setEnableRec(panel_10, false);
			PDFUtils.setEnableRec(panel_11, false);
		}
		
	}
	
}
