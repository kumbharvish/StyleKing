package com.shopbilling.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
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
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.MyStoreDetails;
import com.shopbilling.dto.UserDetails;
import com.shopbilling.services.DBBackupService;
import com.shopbilling.services.MyStoreServices;
import com.shopbilling.services.ReportServices;
import java.awt.GridLayout;

public class MainWindow extends JFrame{

	private JPanel contentPane;
	private UserDetails userDetails;
	private JPanel containerPanel;
	private JButton btnProducts;
	private JButton btnNewBill;
	private JButton btnSellReport;
	private JButton btnStockEntry;
	//private ProductFrame productFrame;
	private ManageProductsUI productFrame;
	private CashCounterUI cashCounterUI;
	private NewBill newBill;
	private SellReport sellReport;
	//private StockEntry stockEntry;
	private StockPurchaseEntryUI stockEntry;
	//private CustomerFrame customer;
	private ManageCustomersUI customer;
	private SalesReturnUI salesReturnUI;
	private BillWiseProfitUI billWiseProfit;
	private StockEntryWiseProfitUI stockEntryWiseProfit;
	private CustomerWiseProfitUI customerWiseProfit;
	private ProductWiseSalesAnalysis productWiseSalesAnalysis;
	private ProductWiseProfitUI productWiseProfit;
	private ProductProfitReportUI productProfitReport;
	private SalesStockReportUI salesStockReport;
	private CustomersReportUI customersReport;
	private MonthlyReportUI monthlyReportUI;
	private ProfitLossStatementUI profitLossStatementUI; 
	private ManageAccount manageAccount;
	private PaymentModeWiseCollectionGR paymentModeWiseCol;
	private DailySalesAmtProfitGR dailySalesProfit;
	private MonthlySalesAmtProfitGR monthlySalesAmtProfit;
	private ZeroStockProductReport zeroStockProductReport;
	private GenerateBarCodeUI generateBarCode; 
	private PrintBarcodeSheetUI printBarcodeSheet;
	private AboutUs aboutUs;
	private QuickStockCorrection quickStockCorrection;
	private MyStoreDetailsUI myStoreDetails;
	private CategoryWiseStockReportUI categoryWiseStockReportUI;
	private SalesReportExportUI salesReportExportUI;
	private SalesReturnReportExportUI salesReturnReportExportUI;
	//Product Management
	private ProductCategoryUI productCategoryUI; 
	private ManageProductsUI manageProductsUI;
	//Stock Management
	private ManageSupplierUI manageSupplierUI;
	private StockPurchaseEntryUI stockPurchaseEntryUI;
	private StockPurchaseHistoryUI stockPurchaseHistoryUI;
	//Customer Management
	private ManageCustomersUI manageCustomersUI;
	private CustomerPurchaseHistoryUI customerPurchaseHistoryUI;
	private CustomerPaymentHistoryUI customerPaymentHistoryUI;
	//Sales Return
	private SalesReturnUI newSalesReturn;
	private SalesReturnReportUI salesReturnReportUI;
	//Settings
	private DataBackupMailConfigUI dataBackupMailConfigUI;
	//private AppConfigurationsUI appConfigurationsUI;
	//Expense
	private AddExpenseUI addExpenseUI;
	private ViewExpensesUI viewExpensesUI;
	
	
	private JButton btnDataBackup;
	private JButton btnClose;
	private JButton btnCustomer;
	private JButton btnSalesReturn;
	private JButton btnCounter;
	private JMenuBar menuBar;
	private JMenu mnBusinessAnalysis;
	private JMenuItem mntmLogOut;
	private JMenu mnWelcomeUser;
	private JMenuItem mntmManageAccount;
	private JMenuItem mntmDailySalesCollection;
	private JFrame parentFrame;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				UserDetails userDetails = new UserDetails();
				userDetails.setFirstName("Vishal");
				userDetails.setLastName("Kumbhar");
					MainWindow frame = new MainWindow(userDetails);
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
	public MainWindow(final UserDetails userDetails) {
		setTitle(AppConstants.APP_TITLE);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setExtendedState(MAXIMIZED_BOTH);
		setIconImage(Toolkit.getDefaultToolkit().getImage("shop32X32.png"));
		parentFrame = this;
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		
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
		
		JMenuItem mntmMyStoreDetails = new JMenuItem("My Store Details");
		mntmMyStoreDetails.setIcon(new ImageIcon(MainWindow.class.getResource("/images/shop16.png")));
		mnWelcomeUser.add(mntmMyStoreDetails);
		
		mntmMyStoreDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				myStoreDetails = new MyStoreDetailsUI();
				containerPanel.add(myStoreDetails);
			}
		});
		
		mntmLogOut.setIcon(new ImageIcon(MainWindow.class.getResource("/images/exit.png")));
		mnWelcomeUser.add(mntmLogOut);
		
		JMenu mnMenu = new JMenu("Menu");
		mnMenu.setIcon(new ImageIcon(MainWindow.class.getResource("/images/about_menu.png")));
		menuBar.add(mnMenu);
		
		JMenu mnProductManagement = new JMenu("Product Management");
		mnProductManagement.setIcon(new ImageIcon(MainWindow.class.getResource("/images/briefcase.png")));
		mnMenu.add(mnProductManagement);
		
		JMenuItem mntmManageProductCategory = new JMenuItem("Manage Product Category");
		mnProductManagement.add(mntmManageProductCategory);
		
		mntmManageProductCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				productCategoryUI = new ProductCategoryUI();
				productCategoryUI.setUserDetails(userDetails);
				containerPanel.add(productCategoryUI);
			}
		});
		
		JMenuItem mntmManageProducts = new JMenuItem("Manage Products");
		mnProductManagement.add(mntmManageProducts);
		
		mntmManageProducts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				manageProductsUI = new ManageProductsUI();
				manageProductsUI.setUserDetails(userDetails);
				containerPanel.add(manageProductsUI);
			}
		});
		
		JMenuItem mntmQuickStockCorrection = new JMenuItem("Quick Stock Correction");
		mnProductManagement.add(mntmQuickStockCorrection);
		mntmQuickStockCorrection.setIcon(null);
		
		JMenu mnBarcodeSetup = new JMenu("Barcode Management");
		mnMenu.add(mnBarcodeSetup);
		mnBarcodeSetup.setIcon(new ImageIcon(MainWindow.class.getResource("/images/barcode (2).png")));
		
		JMenuItem mntmCreateForProducts = new JMenuItem("Generate Barcode For Product");
		mntmCreateForProducts.setIcon(null);
		mnBarcodeSetup.add(mntmCreateForProducts);
		
		mntmCreateForProducts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				generateBarCode = new GenerateBarCodeUI();
				containerPanel.add(generateBarCode);
			}
		});
		
		JMenuItem mntmPrintBarcodeFor = new JMenuItem("Print Barcode Label");
		mntmPrintBarcodeFor.setIcon(null);
		mnBarcodeSetup.add(mntmPrintBarcodeFor);
		
		JMenu mnStockManagement = new JMenu("Stock Management");
		mnStockManagement.setIcon(new ImageIcon(MainWindow.class.getResource("/images/shopping_cart_full.png")));
		mnMenu.add(mnStockManagement);
		
		JMenuItem mntmManageSuppliers = new JMenuItem("Manage Suppliers");
		mnStockManagement.add(mntmManageSuppliers);
		
		mntmManageSuppliers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				manageSupplierUI = new ManageSupplierUI();
				containerPanel.add(manageSupplierUI);
			}
		});
		
		JMenuItem mntmStockPurchaseEntry = new JMenuItem("Stock Purchase Entry");
		mnStockManagement.add(mntmStockPurchaseEntry);
		
		mntmStockPurchaseEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				stockPurchaseEntryUI = new StockPurchaseEntryUI();
				containerPanel.add(stockPurchaseEntryUI);
			}
		});

		JMenuItem mntmStockPurchaseHistory = new JMenuItem("Stock Purchase History");
		mnStockManagement.add(mntmStockPurchaseHistory);
		
		mntmStockPurchaseHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				stockPurchaseHistoryUI = new StockPurchaseHistoryUI();
				containerPanel.add(stockPurchaseHistoryUI);
			}
		});
		
		JMenu mnSalesReturn = new JMenu("Sales Return");
		mnSalesReturn.setIcon(new ImageIcon(MainWindow.class.getResource("/images/shopcartdown.png")));
		mnMenu.add(mnSalesReturn);
		
		JMenuItem mntmNewReturn = new JMenuItem("New Return");
		mnSalesReturn.add(mntmNewReturn);
		
		mntmNewReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				newSalesReturn = new SalesReturnUI();
				containerPanel.add(newSalesReturn);
			}
		});
		
		JMenuItem mntmSalesReturnReport = new JMenuItem("Sales Return Report");
		mnSalesReturn.add(mntmSalesReturnReport);
		
		mntmSalesReturnReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				salesReturnReportUI = new SalesReturnReportUI();
				containerPanel.add(salesReturnReportUI);
			}
		});
		
		JMenu mnCustomerManagement = new JMenu("Customer Management");
		mnCustomerManagement.setIcon(new ImageIcon(MainWindow.class.getResource("/images/user.png")));
		mnMenu.add(mnCustomerManagement);
		
		JMenuItem mntmManageCustomers = new JMenuItem("Manage Customer Details");
		mnCustomerManagement.add(mntmManageCustomers);
		
		mntmManageCustomers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				manageCustomersUI = new ManageCustomersUI();
				manageCustomersUI.setUserDetails(userDetails);
				containerPanel.add(manageCustomersUI);
			}
		});
		
		JMenuItem mntmCustomerPaymentHistory = new JMenuItem("Customer Payment History");
		mnCustomerManagement.add(mntmCustomerPaymentHistory);
		
		mntmCustomerPaymentHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				customerPaymentHistoryUI = new CustomerPaymentHistoryUI();
				containerPanel.add(customerPaymentHistoryUI);
			}
		});
		
		JMenuItem mntmCustomerPurchaseHistory = new JMenuItem("Customer Purchase History");
		mnCustomerManagement.add(mntmCustomerPurchaseHistory);
		
		mntmCustomerPurchaseHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				customerPurchaseHistoryUI = new CustomerPurchaseHistoryUI();
				containerPanel.add(customerPurchaseHistoryUI);
			}
		});
		
		JMenu mnExpenses = new JMenu("Expenses");
		mnExpenses.setIcon(new ImageIcon(MainWindow.class.getResource("/images/banknote.png")));
		mnMenu.add(mnExpenses);
		
		JMenuItem mntmAddExpense = new JMenuItem("Add Expense");
		mnExpenses.add(mntmAddExpense);
		
		JMenuItem mntmViewExpenses = new JMenuItem("View Expenses");
		mnExpenses.add(mntmViewExpenses);
		
		mntmViewExpenses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				viewExpensesUI = new ViewExpensesUI();
				containerPanel.add(viewExpensesUI);
			}
		});
		
		mntmAddExpense.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				addExpenseUI = new AddExpenseUI();
				containerPanel.add(addExpenseUI);
			}
		});
		
		JMenu mnSettings = new JMenu("Settings");
		mnSettings.setIcon(new ImageIcon(MainWindow.class.getResource("/images/advancedsettings (1).png")));
		mnMenu.add(mnSettings);
		
		JMenuItem mntmDataBackupMail = new JMenuItem("Data Backup Mail Settings");
		mnSettings.add(mntmDataBackupMail);
		
		mntmDataBackupMail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				dataBackupMailConfigUI = new DataBackupMailConfigUI();
				containerPanel.add(dataBackupMailConfigUI);
			}
		});
		
		mntmPrintBarcodeFor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				printBarcodeSheet = new PrintBarcodeSheetUI();
				containerPanel.add(printBarcodeSheet);
			}
		});
		
		mntmQuickStockCorrection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quickStockCorrection = new QuickStockCorrection(parentFrame);
				quickStockCorrection.setVisible(true);
			}
		});
		
		mnBusinessAnalysis = new JMenu("Business Analysis");
		mnBusinessAnalysis.setIcon(new ImageIcon(MainWindow.class.getResource("/images/research.png")));
		menuBar.add(mnBusinessAnalysis);
		
		JMenuItem mntmProductWise = new JMenuItem("Product Wise Profit");
		mntmProductWise.setIcon(new ImageIcon(MainWindow.class.getResource("/images/product_193.png")));
		mnBusinessAnalysis.add(mntmProductWise);
		
		mntmProductWise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				productWiseProfit = new ProductWiseProfitUI();
				containerPanel.add(productWiseProfit);
			}
		});
		
		JMenuItem mntmBillWiseReports = new JMenuItem("Bill Wise Profit");
		mntmBillWiseReports.setIcon(new ImageIcon(MainWindow.class.getResource("/images/invoice.png")));
		mnBusinessAnalysis.add(mntmBillWiseReports);
		mntmBillWiseReports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				billWiseProfit = new BillWiseProfitUI();
				containerPanel.add(billWiseProfit);
			}
		});
		JMenuItem mntmPurchaseEntryWise = new JMenuItem("Stock Entry Wise Profit");
		mntmPurchaseEntryWise.setIcon(new ImageIcon(MainWindow.class.getResource("/images/stock_draw_circle_pie.png")));
		mnBusinessAnalysis.add(mntmPurchaseEntryWise);
		mntmPurchaseEntryWise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				stockEntryWiseProfit = new StockEntryWiseProfitUI();
				containerPanel.add(stockEntryWiseProfit);
			}
		});
		
		JMenuItem mntmCustomerWiseProfit = new JMenuItem("Customer Wise Profit");
		mntmCustomerWiseProfit.setIcon(new ImageIcon(MainWindow.class.getResource("/images/customers (1).png")));
		mnBusinessAnalysis.add(mntmCustomerWiseProfit);
		
		JMenuItem mntmProductWiseSales = new JMenuItem("Product Wise Sales");
		mntmProductWiseSales.setIcon(new ImageIcon(MainWindow.class.getResource("/images/product_193 (2).png")));
		mnBusinessAnalysis.add(mntmProductWiseSales);
		
		mntmProductWiseSales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				productWiseSalesAnalysis = new ProductWiseSalesAnalysis();
				containerPanel.add(productWiseSalesAnalysis);
			}
		});
		
		mntmCustomerWiseProfit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				customerWiseProfit = new CustomerWiseProfitUI();
				containerPanel.add(customerWiseProfit);
			}
		});
		
		JMenu mnReports = new JMenu("Reports");
		mnReports.setIcon(new ImageIcon(MainWindow.class.getResource("/images/report_check.png")));
		menuBar.add(mnReports);
		
		JMenuItem mntmProductProfitReport = new JMenuItem("Product Profit Report");
		mntmProductProfitReport.setIcon(new ImageIcon(MainWindow.class.getResource("/images/product_193 (1).png")));
		mnReports.add(mntmProductProfitReport);
		
		mntmProductProfitReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				productProfitReport = new ProductProfitReportUI();
				containerPanel.add(productProfitReport);
			}
		});
		
		JMenuItem mntmSalesStockValue = new JMenuItem("Sales Stock Value Report");
		mntmSalesStockValue.setIcon(new ImageIcon(MainWindow.class.getResource("/images/stock_edit_bookmark.png")));
		mnReports.add(mntmSalesStockValue);
		
		mntmSalesStockValue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				salesStockReport = new SalesStockReportUI();
				containerPanel.add(salesStockReport);
			}
		});
		
		JMenuItem mntmCustomersReport = new JMenuItem("Customers Report");
		mntmCustomersReport.setIcon(new ImageIcon(MainWindow.class.getResource("/images/customers (2).png")));
		mnReports.add(mntmCustomersReport);
		
		JMenuItem mntmZeroStockProducts = new JMenuItem("Zero Stock Products Report");
		mntmZeroStockProducts.setIcon(new ImageIcon(MainWindow.class.getResource("/images/trashcan_empty.png")));
		mnReports.add(mntmZeroStockProducts);
		
		JMenuItem mntmProductCategoryWise = new JMenuItem("Product Category Wise Stock Report");
		mntmProductCategoryWise.setIcon(new ImageIcon(MainWindow.class.getResource("/images/category.png")));
		mnReports.add(mntmProductCategoryWise);
		
		JMenuItem mntmSalesReport = new JMenuItem("Sales Report");
		mntmSalesReport.setIcon(new ImageIcon(MainWindow.class.getResource("/images/report_check.png")));
		mnReports.add(mntmSalesReport);
		
		JMenuItem mntmSalesReturnReportExport = new JMenuItem("Sales Return Report");
		mntmSalesReturnReportExport.setIcon(new ImageIcon(MainWindow.class.getResource("/images/shopcartdown (1).png")));
		mnReports.add(mntmSalesReturnReportExport);
		
		JMenuItem mntmMonthlyReport = new JMenuItem("Monthly Report");
		mntmMonthlyReport.setIcon(new ImageIcon(MainWindow.class.getResource("/images/stats.png")));
		mnReports.add(mntmMonthlyReport);
		
		JMenuItem mntmProfitLossStatement = new JMenuItem("Profit Loss Report");
		mntmProfitLossStatement.setIcon(new ImageIcon(MainWindow.class.getResource("/images/chart_up.png")));
		mnReports.add(mntmProfitLossStatement);
		
		mntmProfitLossStatement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				profitLossStatementUI = new ProfitLossStatementUI();
				containerPanel.add(profitLossStatementUI);
			}
		});
		
		mntmMonthlyReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				monthlyReportUI = new MonthlyReportUI();
				containerPanel.add(monthlyReportUI);
			}
		});
		
		mntmSalesReturnReportExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				salesReturnReportExportUI = new SalesReturnReportExportUI();
				containerPanel.add(salesReturnReportExportUI);
			}
		});
		
		mntmSalesReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				salesReportExportUI = new SalesReportExportUI();
				containerPanel.add(salesReportExportUI);
			}
		});
		
		mntmProductCategoryWise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				categoryWiseStockReportUI = new CategoryWiseStockReportUI();
				containerPanel.add(categoryWiseStockReportUI);
			}
		});
		
		mntmZeroStockProducts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				zeroStockProductReport = new ZeroStockProductReport();
				containerPanel.add(zeroStockProductReport);
			}
		});
		
		JMenu mnGraphicalReports = new JMenu("Graphical Reports");
		mnGraphicalReports.setIcon(new ImageIcon(MainWindow.class.getResource("/images/chart_02.png")));
		menuBar.add(mnGraphicalReports);
		
		JMenuItem mntmPaymentModeWise = new JMenuItem("Payment Mode Wise Sales Amount");
		mntmPaymentModeWise.setIcon(new ImageIcon(MainWindow.class.getResource("/images/pie_chart_red.png")));
		mnGraphicalReports.add(mntmPaymentModeWise);
		
		mntmDailySalesCollection = new JMenuItem("Daily Sales Amount & Profit");
		mntmDailySalesCollection.setIcon(new ImageIcon(MainWindow.class.getResource("/images/chart_bar.png")));
		mnGraphicalReports.add(mntmDailySalesCollection);
		
		JMenuItem mntmMonthlySalesAmount = new JMenuItem("Monthly Sales Amount & Profit");
		mntmMonthlySalesAmount.setIcon(new ImageIcon(MainWindow.class.getResource("/images/chart_bar.png")));
		mnGraphicalReports.add(mntmMonthlySalesAmount);
		
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
		
		mntmMonthlySalesAmount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				monthlySalesAmtProfit = new MonthlySalesAmtProfitGR();
				containerPanel.add(monthlySalesAmtProfit);
				//Code To Hide Title Bar of Internal Frame
				((javax.swing.plaf.basic.BasicInternalFrameUI)   
						monthlySalesAmtProfit.getUI()).setNorthPane(null);
			}
		});
		
		mntmDailySalesCollection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				dailySalesProfit = new DailySalesAmtProfitGR();
				containerPanel.add(dailySalesProfit);
				//Code To Hide Title Bar of Internal Frame
				((javax.swing.plaf.basic.BasicInternalFrameUI)   
						dailySalesProfit.getUI()).setNorthPane(null);
			}
		});
		
		mntmPaymentModeWise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				paymentModeWiseCol = new PaymentModeWiseCollectionGR();
				containerPanel.add(paymentModeWiseCol);
			}
		});
		
		mntmCustomersReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
				customersReport = new CustomersReportUI();
				containerPanel.add(customersReport);
			}
		});
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		UIDefaults defaults = UIManager.getLookAndFeelDefaults();
		if (defaults.get("Table.alternateRowColor") == null)
		    defaults.put("Table.alternateRowColor", new Color(224, 255, 255));
		btnProducts = new JButton("Products");
		btnProducts.setIcon(new ImageIcon(MainWindow.class.getResource("/images/Products.png")));
		btnProducts.setBackground(UIManager.getColor("Button.background"));
		btnProducts.setFont(new Font("Tahoma", Font.BOLD, 12));
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBounds(0, 0, 147, (int)height-100);
		buttonsPanel.setBackground(Color.LIGHT_GRAY);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
		buttonsPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 2), "Quick Menu", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		containerPanel = new JPanel();
		//SET CONTAINTER PANEL SIZE
		containerPanel.setBounds(157, 23, (int)width-176, (int)height-123);
		containerPanel.setLayout(new CardLayout(0, 0));
		//Button Product Action
		btnProducts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				productFrame = new ManageProductsUI();
				//productFrame.setVisible(true);
				productFrame.setUserDetails(userDetails);
				closeAllInternalFrames();
				containerPanel.add(productFrame);
				disableEnableButtons(btnProducts,productFrame,btnNewBill,newBill,btnSellReport,sellReport,btnStockEntry,stockEntry,btnCustomer,customer,btnSalesReturn,salesReturnUI,btnCounter,cashCounterUI);				
			}

		});
		
		btnSellReport = new JButton("Report");
		btnSellReport.setIcon(new ImageIcon(MainWindow.class.getResource("/images/Report.png")));
		btnSellReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sellReport = new SellReport();
				sellReport.setVisible(true);
				//sellReport.setUserDetails(userDetails);
				closeAllInternalFrames();
				containerPanel.add(sellReport);
				disableEnableButtons(btnSellReport,sellReport,btnNewBill,newBill,btnProducts,productFrame,btnStockEntry,stockEntry,btnCustomer,customer,btnSalesReturn,salesReturnUI,btnCounter,cashCounterUI);
			}
		});
		btnSellReport.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnSellReport.setBackground(UIManager.getColor("Button.background"));
		
		btnStockEntry = new JButton("Purchase");
		btnStockEntry.setIcon(new ImageIcon(MainWindow.class.getResource("/images/Stock.png")));
		btnStockEntry.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnStockEntry.setBackground(SystemColor.menu);
		btnStockEntry.setBackground(UIManager.getColor("Button.background"));
		
		btnStockEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stockEntry = new StockPurchaseEntryUI();
				stockEntry.setVisible(true);
				//sellReport.setUserDetails(userDetails);
				closeAllInternalFrames();
				containerPanel.add(stockEntry);
				disableEnableButtons(btnStockEntry,stockEntry,btnSellReport,sellReport,btnNewBill,newBill,btnProducts,productFrame,btnCustomer,customer,btnSalesReturn,salesReturnUI,btnCounter,cashCounterUI);
			}
		});
		
		btnClose = new JButton("Close");
		btnClose.setIcon(new ImageIcon(MainWindow.class.getResource("/images/Close (2).png")));
		btnClose.setBackground(UIManager.getColor("Button.background"));
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllInternalFrames();
			}
		});
		btnClose.setFont(new Font("Tahoma", Font.BOLD, 12));
		buttonsPanel.setLayout(new GridLayout(0, 1, 5, 15));
		buttonsPanel.add(btnProducts);
		
		 btnNewBill = new JButton("POS");
		 btnNewBill.setIcon(new ImageIcon(MainWindow.class.getResource("/images/newBill2.png")));
		 btnNewBill.setBackground(UIManager.getColor("Button.background"));
		 btnNewBill.setFont(new Font("Tahoma", Font.BOLD, 12));
		 btnNewBill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newBill = new NewBill();
				newBill.setVisible(true);
				newBill.setUserDetails(userDetails);
				closeAllInternalFrames();
				containerPanel.add(newBill);
				disableEnableButtons(btnNewBill,newBill,btnProducts,productFrame,btnSellReport,sellReport,btnStockEntry,stockEntry,btnCustomer,customer,btnSalesReturn,salesReturnUI,btnCounter,cashCounterUI);
			}
		});
		 buttonsPanel.add(btnNewBill);
		buttonsPanel.add(btnSellReport);
		buttonsPanel.add(btnStockEntry);
		
		btnDataBackup = new JButton("Backup");
		btnDataBackup.setIcon(new ImageIcon(MainWindow.class.getResource("/images/backup2.png")));
		btnDataBackup.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnDataBackup.setBackground(SystemColor.menu);
		btnDataBackup.setBackground(UIManager.getColor("Button.background"));
		
		btnDataBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Data Backup Successfully Started !", "Data Backup", JOptionPane.INFORMATION_MESSAGE);
				DBBackupService.createDBDumpSendOnMail();
			}
		});
		
		btnCounter = new JButton("Counter");
		btnCounter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cashCounterUI = new CashCounterUI();
				cashCounterUI.setVisible(true);
				closeAllInternalFrames();
				containerPanel.add(cashCounterUI);
				disableEnableButtons(btnCounter,cashCounterUI,btnSalesReturn,salesReturnUI,btnCustomer,customer,btnSellReport,sellReport,btnNewBill,newBill,btnProducts,productFrame,btnStockEntry,stockEntry);
			}
		});
		
		btnSalesReturn = new JButton("Returns");
		btnSalesReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salesReturnUI = new SalesReturnUI();
				salesReturnUI.setVisible(true);
				closeAllInternalFrames();
				containerPanel.add(salesReturnUI);
				disableEnableButtons(btnSalesReturn,salesReturnUI,btnCustomer,customer,btnSellReport,sellReport,btnNewBill,newBill,btnProducts,productFrame,btnStockEntry,stockEntry,btnCounter,cashCounterUI);
			}
		});
		
		btnCustomer = new JButton("Customer");
		btnCustomer.setIcon(new ImageIcon(MainWindow.class.getResource("/images/customers.png")));
		btnCustomer.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCustomer.setBackground(SystemColor.menu);
		btnCustomer.setBackground(UIManager.getColor("Button.background"));
		
		btnCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				customer = new ManageCustomersUI();
				customer.setUserDetails(userDetails);
				customer.setVisible(true);
				closeAllInternalFrames();
				containerPanel.add(customer);
				disableEnableButtons(btnCustomer,customer,btnSellReport,sellReport,btnNewBill,newBill,btnProducts,productFrame,btnStockEntry,stockEntry,btnSalesReturn,salesReturnUI,btnCounter,cashCounterUI);
			}
		});
		buttonsPanel.add(btnCustomer);
		btnSalesReturn.setIcon(new ImageIcon(MainWindow.class.getResource("/images/cart_remove.png")));
		btnSalesReturn.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnSalesReturn.setBackground(UIManager.getColor("Button.background"));
		buttonsPanel.add(btnSalesReturn);
		btnCounter.setIcon(new ImageIcon(MainWindow.class.getResource("/images/currency_sign_rupee_arrow_up.png")));
		btnCounter.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCounter.setBackground(SystemColor.menu);
		btnCounter.setBackground(UIManager.getColor("Button.background"));
		buttonsPanel.add(btnCounter);
		buttonsPanel.add(btnDataBackup);
		buttonsPanel.add(btnClose);
		contentPane.setLayout(null);
		contentPane.add(buttonsPanel);
		contentPane.add(containerPanel);
		MyStoreDetails myStoreDetails  = MyStoreServices.getMyStoreDetails();
		JLabel myStoreNamelbl = new JLabel(myStoreDetails.getStoreName().toUpperCase()+" , "+myStoreDetails.getCity().toUpperCase());
		myStoreNamelbl.setForeground(new Color(255, 140, 0));
		myStoreNamelbl.setFont(new Font("Tahoma", Font.BOLD, 15));
		myStoreNamelbl.setBounds((int)width-366, 2, 347, 20);
		contentPane.add(myStoreNamelbl);
		
		//Create Database Backup
		DBBackupService.createDBDump();
		// Add Opening Stock Value Amount Entry
		//ReportServices.doRecurrsiveInsertOpeningAmount();
		
		//ScalableLayoutRegistry scalerRegistry = new DefaultScalableLayoutRegistry();
		//ScalableLayoutUtils.installScalableLayout(scalerRegistry, contentPane);
		//ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
		System.out.println("width " +width);
		System.out.println("height "+height);
	}
	
	protected void closeAllInternalFrames() {
		if(newBill != null){
			if(!btnNewBill.isEnabled())
				btnNewBill.setEnabled(true);
			containerPanel.remove(newBill);
		}
		if(productFrame !=null){
			if(!btnProducts.isEnabled())
				btnProducts.setEnabled(true);
			containerPanel.remove(productFrame);
		} 
		if(stockEntry !=null){
			if(!btnStockEntry.isEnabled())
				btnStockEntry.setEnabled(true);
			containerPanel.remove(stockEntry);
		} 
		if(sellReport !=null){
			if(!btnSellReport.isEnabled())
				btnSellReport.setEnabled(true);
			containerPanel.remove(sellReport);
		}
		if(customer !=null){
			if(!btnCustomer.isEnabled())
				btnCustomer.setEnabled(true);
			containerPanel.remove(customer);
		}
		if(salesReturnUI !=null){
			if(!btnSalesReturn.isEnabled())
				btnSalesReturn.setEnabled(true);
			containerPanel.remove(salesReturnUI);
		}
		if(cashCounterUI !=null){
			if(!btnCounter.isEnabled())
				btnCounter.setEnabled(true);
			containerPanel.remove(cashCounterUI);
		}
		//Menus
		if(billWiseProfit !=null){
			containerPanel.remove(billWiseProfit);
		}
		if(stockEntryWiseProfit !=null){
			containerPanel.remove(stockEntryWiseProfit);
		}
		if(customerWiseProfit!=null){
			containerPanel.remove(customerWiseProfit);
		}
		if(productWiseProfit!=null){
			containerPanel.remove(productWiseProfit);
		}
		if(productProfitReport!=null){
			containerPanel.remove(productProfitReport);
		}
		if(salesStockReport!=null){
			containerPanel.remove(salesStockReport);
		}
		if(customersReport!=null){
			containerPanel.remove(customersReport);
		}
		if(manageAccount!=null){
			containerPanel.remove(manageAccount);
		}
		if(paymentModeWiseCol!=null){
			containerPanel.remove(paymentModeWiseCol);
		}
		if(dailySalesProfit!=null){
			containerPanel.remove(dailySalesProfit);
		}
		if(monthlySalesAmtProfit!=null){
			containerPanel.remove(monthlySalesAmtProfit);
		}
		if(zeroStockProductReport!=null){
			containerPanel.remove(zeroStockProductReport);
		}
		if(productWiseSalesAnalysis!=null){
			containerPanel.remove(productWiseSalesAnalysis);
		}
		if(generateBarCode!=null){
			containerPanel.remove(generateBarCode);
		}
		if(printBarcodeSheet!=null){
			containerPanel.remove(printBarcodeSheet);
		}
		if(aboutUs!=null){
			containerPanel.remove(aboutUs);
		}
		if(myStoreDetails!=null){
			containerPanel.remove(myStoreDetails);
		}
		if(productCategoryUI!=null){
			containerPanel.remove(productCategoryUI);
		}
		if(manageProductsUI!=null){
			containerPanel.remove(manageProductsUI);
		}
		if(manageSupplierUI!=null){
			containerPanel.remove(manageSupplierUI);
		}
		if(stockPurchaseEntryUI!=null){
			containerPanel.remove(stockPurchaseEntryUI);
		}
		if(stockPurchaseHistoryUI!=null){
			containerPanel.remove(stockPurchaseHistoryUI);
		}
		if(manageCustomersUI!=null){
			containerPanel.remove(manageCustomersUI);
		}
		if(customerPurchaseHistoryUI!=null){
			containerPanel.remove(customerPurchaseHistoryUI);
		}
		if(dataBackupMailConfigUI!=null){
			containerPanel.remove(dataBackupMailConfigUI);
		}
		/*if(appConfigurationsUI!=null){
			containerPanel.remove(appConfigurationsUI);
		}*/
		if(categoryWiseStockReportUI!=null){
			containerPanel.remove(categoryWiseStockReportUI);
		}
		if(customerPaymentHistoryUI!=null){
			containerPanel.remove(customerPaymentHistoryUI);
		}
		if(newSalesReturn!=null){
			containerPanel.remove(newSalesReturn);
		}
		if(salesReturnReportUI!=null){
			containerPanel.remove(salesReturnReportUI);
		}
		if(salesReportExportUI!=null){
			containerPanel.remove(salesReportExportUI);
		}
		if(salesReturnReportExportUI!=null){
			containerPanel.remove(salesReturnReportExportUI);
		}
		if(addExpenseUI!=null){
			containerPanel.remove(addExpenseUI);
		}
		if(viewExpensesUI!=null){
			containerPanel.remove(viewExpensesUI);
		}
		if(monthlyReportUI!=null){
			containerPanel.remove(monthlyReportUI);
		}
		if(profitLossStatementUI!=null){
			containerPanel.remove(profitLossStatementUI);
		}
	}

	private void disableEnableButtons(JButton btnToDisable,JInternalFrame frameToVisible,JButton btnToEnable_1,JInternalFrame frameToDisable_1,
			JButton btnToEnable_2,JInternalFrame frameToDisable_2,JButton btnToEnable_3,JInternalFrame frameToDisable_3,
			JButton btnToEnable_4,JInternalFrame frameToDisable_4,JButton btnToEnable_5,JInternalFrame frameToDisable_5,
			JButton btnToEnable_6,JInternalFrame frameToDisable_6) {
		btnToDisable.setEnabled(false);
		frameToVisible.setVisible(true);
		btnToEnable_1.setEnabled(true);
		if(frameToDisable_1 !=null){
			frameToDisable_1.dispose();
		}
		btnToEnable_2.setEnabled(true);
		if(frameToDisable_2 !=null){
			frameToDisable_2.dispose();
		}
		btnToEnable_3.setEnabled(true);
		if(frameToDisable_3 !=null){
			frameToDisable_3.dispose();
		}
		btnToEnable_4.setEnabled(true);
		if(frameToDisable_4 !=null){
			frameToDisable_4.dispose();
		}
		btnToEnable_5.setEnabled(true);
		if(frameToDisable_5 !=null){
			frameToDisable_5.dispose();
		}
		btnToEnable_6.setEnabled(true);
		if(frameToDisable_6 !=null){
			frameToDisable_6.dispose();
		}
	}
}
