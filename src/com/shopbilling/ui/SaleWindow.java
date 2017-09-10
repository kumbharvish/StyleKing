package com.shopbilling.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.shopbilling.dto.MyStoreDetails;
import com.shopbilling.dto.UserDetails;
import com.shopbilling.services.DBBackupService;
import com.shopbilling.services.MyStoreServices;

public class SaleWindow extends JFrame{

	private JPanel contentPane;
	private UserDetails userDetails;
	private JPanel containerPanel;
	private POS pos;
	private QuickStockCorrection quickStockCorrection;
	private SaleReport saleReport;
	private ManageAccount manageAccount;
	private AboutUs aboutUs;
	private JMenuBar menuBar;
	private JMenuItem mntmLogOut;
	private JMenu mnWelcomeUser;
	private JMenuItem mntmManageAccount;
	private SaleWindow parentFrame;
	private CustomerFrame customer;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				UserDetails userDetails = new UserDetails();
				userDetails.setFirstName("Vishal");
				userDetails.setLastName("Kumbhar");
					SaleWindow frame = new SaleWindow(userDetails);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the frame.
	 */
	public SaleWindow(final UserDetails userDetails) {
		setTitle("MY STORE                                				           Copyright@Kumbharvish@gmail.com                  					             Contact Number : 8149880299");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setExtendedState(MAXIMIZED_BOTH);
		setIconImage(Toolkit.getDefaultToolkit().getImage("shop32X32.png"));
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		parentFrame = this;
		mnWelcomeUser = new JMenu("Welcome,"+userDetails.getFirstName()+" "+userDetails.getLastName()+" ");
		menuBar.add(mnWelcomeUser);
		
		mntmLogOut = new JMenuItem("Log Out");
		mntmLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Log Off
				DBBackupService.createDBDump();
				System.exit(0);
			}
		});
		
		//Window Listner
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	int dialogResult =JOptionPane.showConfirmDialog(parentFrame, 
			            "Are you sure to close?", "Close",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
		        if (dialogResult == JOptionPane.YES_OPTION){
		        	DBBackupService.createDBDump();
		            System.exit(0);
		        }
		    }
		});
				
		mntmManageAccount = new JMenuItem("Manage Account");
		mntmManageAccount.setIcon(new ImageIcon(MainWindow.class.getResource("/images/user_male.png")));
		mnWelcomeUser.add(mntmManageAccount);
		
		mntmManageAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				manageAccount = new ManageAccount(userDetails);
				containerPanel.add(manageAccount);
			}
		});
		
		mntmLogOut.setIcon(new ImageIcon(MainWindow.class.getResource("/images/exit.png")));
		mnWelcomeUser.add(mntmLogOut);
		
		JMenu mnMenu = new JMenu("Menu");
		mnMenu.setIcon(new ImageIcon(SaleWindow.class.getResource("/images/about_menu.png")));
		menuBar.add(mnMenu);
		
		JMenuItem mntmPosSale = new JMenuItem("POS");
		mntmPosSale.setIcon(new ImageIcon(SaleWindow.class.getResource("/images/shopping_cart_red.png")));
		mnMenu.add(mntmPosSale);
		
		mntmPosSale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				pos = new POS();
				containerPanel.add(pos);
			}
		});
		
		JMenuItem mntmReport = new JMenuItem("Report");
		mntmReport.setIcon(new ImageIcon(SaleWindow.class.getResource("/images/report (1).png")));
		mnMenu.add(mntmReport);
		
		JMenuItem mntmQuickStockCorrection = new JMenuItem("Quick Stock Correction");
		mntmQuickStockCorrection.setIcon(new ImageIcon(SaleWindow.class.getResource("/images/stock (1).png")));
		mnMenu.add(mntmQuickStockCorrection);
		
		JMenuItem mntmManageCustomer = new JMenuItem("Manage Customer");
		mntmManageCustomer.setIcon(new ImageIcon(SaleWindow.class.getResource("/images/customers (1).png")));
		mnMenu.add(mntmManageCustomer);
		mntmManageCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				customer = new CustomerFrame();
				containerPanel.add(customer);
			}
		});
		
		mntmQuickStockCorrection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quickStockCorrection = new QuickStockCorrection(parentFrame);
				quickStockCorrection.setVisible(true);
			}
		});
		
		mntmReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				saleReport = new SaleReport();
				containerPanel.add(saleReport);
			}
		});
		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setIcon(new ImageIcon(MainWindow.class.getResource("/images/help_browser.png")));
		menuBar.add(mnHelp);
		
		JMenuItem mntmAboutUs = new JMenuItem("About Us");
		mntmAboutUs.setIcon(new ImageIcon(MainWindow.class.getResource("/images/get_info.png")));
		mnHelp.add(mntmAboutUs);
		
		mntmAboutUs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				aboutUs = new AboutUs();
				containerPanel.add(aboutUs);
			}
		});
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		UIDefaults defaults = UIManager.getLookAndFeelDefaults();
		if (defaults.get("Table.alternateRowColor") == null)
		    defaults.put("Table.alternateRowColor", new Color(224, 255, 255));
		Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
		
		containerPanel = new JPanel();
		containerPanel.setBounds(10, 23, 1337, 645);
		containerPanel.setLayout(new CardLayout(0, 0));
		contentPane.setLayout(null);
		contentPane.add(containerPanel);
		MyStoreDetails myStoreDetails  = MyStoreServices.getMyStoreDetails();
		JLabel myStoreNamelbl = new JLabel(myStoreDetails.getStoreName().toUpperCase()+" , "+myStoreDetails.getCity().toUpperCase());
		myStoreNamelbl.setForeground(new Color(255, 140, 0));
		myStoreNamelbl.setFont(new Font("Tahoma", Font.BOLD, 15));
		myStoreNamelbl.setBounds(1000, 0, 347, 20);
		contentPane.add(myStoreNamelbl);
		//Create Database Backup
		DBBackupService.createDBDump();
	}
	
	protected void closeAllInternalFrames() {
		if(manageAccount!=null){
			containerPanel.remove(manageAccount);
		}
		if(aboutUs!=null){
			containerPanel.remove(aboutUs);
		}
		if(pos!=null){
			containerPanel.remove(pos);
		}
		if(saleReport!=null){
			containerPanel.remove(saleReport);
		}
		if(customer!=null){
			containerPanel.remove(customer);
		}
		
	}
}
