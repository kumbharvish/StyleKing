package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.MyStoreDetails;
import com.shopbilling.dto.UserDetails;
import com.shopbilling.properties.AppProperties;
import com.shopbilling.services.AppLicenseServices;
import com.shopbilling.services.MyStoreServices;
import com.shopbilling.services.UserServices;
import com.shopbilling.utils.PDFUtils;

public class ManageStoreUI extends JFrame {

	private JPanel contentPane;
	private JTextField userName;
	private JPasswordField passwordField;

	private final static Logger logger = Logger.getLogger(ManageStoreUI.class);
	
	public ManageStoreUI() throws Exception {
		setTitle(AppConstants.APP_TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 389, 300);
		setExtendedState(MAXIMIZED_BOTH);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		setIconImage(Toolkit.getDefaultToolkit().getImage("shop32X32.png"));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		JPanel panel = new JPanel();
		panel.setBounds(401, 175, 571, 336);
		panel.setBackground(new Color(220, 220, 220));
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		JLabel lblLicenseValidUpto = new JLabel("License Valid Upto : ");
		lblLicenseValidUpto.setBounds((int)width-866, (int)height-104, 183, 25);
		lblLicenseValidUpto.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblLicenseValidUpto.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLicenseValidUpto.setForeground(Color.GRAY);
		
		JLabel lblValidDate = new JLabel("");
		lblValidDate.setBounds((int)width-677, (int)height-104, 183, 25);
		lblValidDate.setHorizontalAlignment(SwingConstants.LEFT);
		lblValidDate.setForeground(Color.GRAY);
		lblValidDate.setFont(new Font("Tahoma", Font.BOLD, 13));
		
		lblValidDate.setText(PDFUtils.dec(PDFUtils.getAppDataValues("APP_SECURE_KEY").get(0)));
		
		JLabel lblCopyrightkumbharvishgmailcom = new JLabel("Copyright@Kumbharvish@gmail.com");
		lblCopyrightkumbharvishgmailcom.setIcon(new ImageIcon(ManageStoreUI.class.getResource("/images/copyright.png")));
		lblCopyrightkumbharvishgmailcom.setBounds(15, (int)height-104, 314, 25);
		lblCopyrightkumbharvishgmailcom.setHorizontalAlignment(SwingConstants.LEFT);
		lblCopyrightkumbharvishgmailcom.setForeground(Color.BLACK);
		lblCopyrightkumbharvishgmailcom.setFont(new Font("Tahoma", Font.BOLD, 13));
		
		JLabel lblContactNo = new JLabel("Contact No : 8149880299");
		lblContactNo.setBounds((int)width-211, (int)height-104, 174, 25);
		lblContactNo.setHorizontalAlignment(SwingConstants.LEFT);
		lblContactNo.setForeground(Color.BLACK);
		lblContactNo.setFont(new Font("Tahoma", Font.BOLD, 13));
		MyStoreDetails storeDetails = MyStoreServices.getMyStoreDetails();
		JLabel lblNewLabel = new JLabel(storeDetails.getStoreName().toUpperCase()+" , "+storeDetails.getCity().toUpperCase());
		lblNewLabel.setBounds(214, 50, 926, 76);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 35));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblstoreLogo = new JLabel("");
		lblstoreLogo.setBounds(101, 24, 199, 139);
		if(storeDetails.getImage()!=null){
			lblstoreLogo.setIcon(PDFUtils.resizeImage(storeDetails.getImage(), lblstoreLogo));
		}
		
		JLabel lblUsername = new JLabel("User Name :");
		lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsername.setBounds(25, 93, 142, 26);
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 21));
		
		userName = new JTextField();
		userName.setFont(new Font("Tahoma", Font.BOLD, 15));
		userName.setBounds(194, 93, 237, 29);
		userName.setColumns(10);
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.BOLD, 15));
		passwordField.setBounds(194, 146, 237, 31);
		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setBounds(35, 151, 132, 26);
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 21));
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setIcon(null);
		btnLogin.setBounds(72, 228, 157, 62);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnLogin.setBackground(UIManager.getColor("Button.background"));
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				String user = userName.getText();
				String pwd = passwordField.getText();
				if ("".equals(user)|| "".equals(pwd)){
					JOptionPane.showMessageDialog(contentPane, "Please Enter UserName / Password !");
				}else{
					UserDetails userDetails = UserServices.validateUser(user, pwd);
					if(userDetails!=null){
						setVisible(false);
						if(AppConstants.ADMIN_USER_TYPE.equals(userDetails.getUserType())){
							//OPEN APPLICATION WINDOW
							MainWindow mw = new MainWindow(userDetails);
							mw.setVisible(true);
						}else if(AppConstants.CASHIER_USER_TYPE.equals(userDetails.getUserType())) {
							SaleWindow sw = new SaleWindow(userDetails);
							sw.setVisible(true);
						}else{
							JOptionPane.showMessageDialog(contentPane, "Invalid User Type !");
						}
					}else{
						JOptionPane.showMessageDialog(contentPane, "Login Failed!");
					}
				}
			}
		});
		
		JButton btnClose = new JButton("Close");
		btnClose.setIcon(null);
		btnClose.setBounds(352, 228, 150, 58);
		btnClose.setPreferredSize(new Dimension(57, 23));
		btnClose.setBackground(UIManager.getColor("Button.background"));
		btnClose.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		contentPane.setLayout(null);
		panel.setLayout(null);
		panel.add(lblUsername);
		panel.add(lblPassword);
		panel.add(userName);
		panel.add(passwordField);
		panel.add(btnLogin);
		panel.add(btnClose);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(ManageStoreUI.class.getResource("/images/hitchhikeguidetogalaxy3_lock.png")));
		label.setBounds(249, 0, 72, 93);
		panel.add(label);
		contentPane.add(panel);
		contentPane.add(lblCopyrightkumbharvishgmailcom);
		contentPane.add(lblLicenseValidUpto);
		contentPane.add(lblValidDate);
		contentPane.add(lblContactNo);
		contentPane.add(lblstoreLogo);
		contentPane.add(lblNewLabel);
		//Update Last Run
		AppLicenseServices.updateLastRun();
	}
	
	public static void main(String[] args) {
		PropertyConfigurator.configure(ManageStoreUI.class.getClassLoader().getResourceAsStream("resources/log4j.properties"));
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if(!AppProperties.check()){
						JOptionPane.showMessageDialog(null, AppConstants.LICENSE_ERROR_1, AppConstants.LICENSE_ERROR, JOptionPane.WARNING_MESSAGE);
					}else{
						if(AppLicenseServices.change()){
							JOptionPane.showMessageDialog(null, AppConstants.COMP_DATE_ERROR, AppConstants.COMP_DATE, JOptionPane.WARNING_MESSAGE);
						}else{
							if(!AppProperties.doCheck()){
								JOptionPane.showMessageDialog(null, AppConstants.LICENSE_ERROR_2, AppConstants.LICENSE_EXPIRED, JOptionPane.WARNING_MESSAGE);
								System.exit(0);
							}else{
								ManageStoreUI frame = new ManageStoreUI();
								frame.setVisible(true);
							}
						}
					}
					
				} catch (Exception e) {
					logger.info("$$ Exception $$ :" ,e);
					e.printStackTrace();
				}
			}
		});
	}
}
