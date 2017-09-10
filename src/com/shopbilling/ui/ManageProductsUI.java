package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.LabelAlignment;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.Product;
import com.shopbilling.dto.ProductCategory;
import com.shopbilling.dto.StatusDTO;
import com.shopbilling.dto.UserDetails;
import com.shopbilling.services.ProductCategoryServices;
import com.shopbilling.services.ProductHistoryServices;
import com.shopbilling.services.ProductServices;
import com.shopbilling.utils.PDFUtils;

public class ManageProductsUI extends JInternalFrame {

	private JPanel contentPane;
	private UserDetails userDetails;
	JTextField productCode ;
	JTextField productName;
	JTextField productDescription; 
	JTextField quantity ;
	JComboBox<String> productCategory;
	JComboBox<String> measure;
	JTextField purchasePrice ;
	JTextField purchaseRate ;
	JTextField productBarCode ;
	JTextField productTax ;
	JTextField sellingPrice;
	//JTextField discount; 
	JTextField lastUpdate;
	JTextField enterBy;
	JTextField entryDate;
	DefaultTableModel productmodel;
	private JTextField tf_searchProduct;
	private JTable productTable;
	private HashMap<String,Integer> productCategoryMap;
	private JFrame frame;

	public ManageProductsUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Manage Products");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setResizable(false);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(15, 16, 1150, 587);
		panel.setLayout(null);
		frame = (JFrame)this.getTopLevelAncestor();;
		JPanel paneProductEntry = new JPanel();
		paneProductEntry.setBorder(new TitledBorder(null, "New Product Entry", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		paneProductEntry.setBounds(10, 42, 400, 524);
		panel.add(paneProductEntry);
		
		JPanel panelProductDetails = new JPanel();
		panelProductDetails.setBorder(new TitledBorder(null, "Product Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelProductDetails.setBounds(420, 12, 720, 554);
		panel.add(panelProductDetails);
		
		JLabel label = new JLabel("Search Product Below : ");
		label.setBounds(379, 18, 131, 17);
		label.setFont(new Font("Tahoma", Font.BOLD, 11));
		JScrollPane prodcutScrollPane = new JScrollPane();
		prodcutScrollPane.setBounds(10, 50, 700, 493);
		tf_searchProduct = new JTextField();
		tf_searchProduct.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_searchProduct.setBounds(514, 16, 196, 20);
		tf_searchProduct.setColumns(10);
		panelProductDetails.setLayout(null);
		panelProductDetails.add(prodcutScrollPane);
		panelProductDetails.add(label);
		panelProductDetails.add(tf_searchProduct);
		
		//Search text field listner
		tf_searchProduct.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				searchProductTableData(productmodel,tf_searchProduct.getText());
			}
		});
		
		contentPane.add(panel);
		DesignGridLayout productEntryLayout = new DesignGridLayout(paneProductEntry);
		productEntryLayout.labelAlignment(LabelAlignment.RIGHT);
		 productCode = new JTextField();
		 productCode.setEditable(false);
		 productCode.setFont(new Font("Tahoma", Font.BOLD, 12));
		 productName = new JTextField(20);
		 productName.setFont(new Font("Tahoma", Font.BOLD, 12));
		 productDescription = new JTextField(20);
		 productDescription.setFont(new Font("Tahoma", Font.BOLD, 12));
		 quantity = new JTextField(10);
		 quantity.setFont(new Font("Tahoma", Font.BOLD, 12));
		 quantity.setDisabledTextColor(Color.BLACK);
		 productCategory = new JComboBox<String>();
		 //Fill category in dropdown
		 populateCategories(productCategory);
		 measure = new JComboBox<String>();
		 measure.setRenderer(new DefaultListCellRenderer() {
		        @Override
		        public void paint(Graphics g) {
		            setForeground(Color.BLACK);
		            super.paint(g);
		        }
		    });
		 populateUnits(measure);
		 measure.setEnabled(false);
		 purchasePrice = new JTextField();
		 purchasePrice.setFont(new Font("Tahoma", Font.BOLD, 12));
		 purchasePrice.setEnabled(false);
		 purchasePrice.setDisabledTextColor(Color.BLUE);
		 purchaseRate = new JTextField();
		 purchaseRate.setFont(new Font("Tahoma", Font.BOLD, 12));
		 productBarCode = new JTextField();
		 productBarCode.setFont(new Font("Tahoma", Font.BOLD, 12));
		 productTax = new JTextField();
		 productTax.setFont(new Font("Tahoma", Font.BOLD, 12));
		 sellingPrice = new JTextField();
		 sellingPrice.setFont(new Font("Tahoma", Font.BOLD, 12));
		// discount = new JTextField();
		 lastUpdate = new JTextField();
		 lastUpdate.setFont(new Font("Tahoma", Font.BOLD, 12));
		 lastUpdate.setEditable(false);
		 enterBy = new JTextField();
		 enterBy.setFont(new Font("Tahoma", Font.BOLD, 12));
		 enterBy.setEditable(false);
		 entryDate = new JTextField();
		 entryDate.setFont(new Font("Tahoma", Font.BOLD, 12));
		 entryDate.setEditable(false);
 		JButton btnProductDelete = new JButton("Delete");
 		JButton btnProductUpdate = new JButton("Update");
 		JButton btnProductSave = new JButton("Add");
 		JButton btnProductReset = new JButton("Reset");
 		productEntryLayout.row().grid(new JLabel("Product Code :"))	.add(productCode);
 		productEntryLayout.emptyRow();
 		productEntryLayout.row().grid(new JLabel("Product Category *:"))	.add(productCategory);
 		productEntryLayout.emptyRow();
 		productEntryLayout.row().grid(new JLabel("Product Name *:"))	.add(productName);
 		productEntryLayout.emptyRow();
 		productEntryLayout.row().grid(new JLabel("Unit *:"))	.add(measure);
 		productEntryLayout.emptyRow();
 		productEntryLayout.row().grid(new JLabel("Quantity *:"))	.add(quantity);
 		productEntryLayout.emptyRow();
 		productEntryLayout.row().grid(new JLabel("Purchase Rate *:"))	.add(purchaseRate);
 		productEntryLayout.emptyRow();
 		productEntryLayout.row().grid(new JLabel("Product Tax(%) *:"))	.add(productTax);
 		productEntryLayout.emptyRow();
 		productEntryLayout.row().grid(new JLabel("Purchase Price :"))	.add(purchasePrice);
 		productEntryLayout.emptyRow();
 		productEntryLayout.row().grid(new JLabel("Sell Price *:"))	.add(sellingPrice);
 		productEntryLayout.emptyRow();
 		//productEntryLayout.row().grid(new JLabel("Last Update :"))	.add(lastUpdate);
 		productEntryLayout.row().grid(new JLabel("Bar Code :"))	.add(productBarCode);
 		productEntryLayout.emptyRow();
 		productEntryLayout.row().grid(new JLabel("Entered By :"))	.add(enterBy);
 		productEntryLayout.emptyRow();
 		productEntryLayout.row().grid(new JLabel("Entry Date :"))	.add(entryDate);
 		productEntryLayout.emptyRow();
 		productEntryLayout.row().grid(new JLabel("Description :"))	.add(productDescription);
 		productEntryLayout.emptyRow();
 		productEntryLayout.row().center().add(btnProductSave).add(btnProductUpdate).add(btnProductDelete).add(btnProductReset);
 		
		productTable = new JTable();
		productmodel = new DefaultTableModel(){
			 boolean[] columnEditables = new boolean[] {
					 false, false, false, false,false,false, false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
		 productmodel.setColumnIdentifiers(new String[] {
    		   "Product Code", "Product Name", "Category", "Quantity", "Purchase Price", "Sale Price", "Discount"}
       );
		 
		 //Table Row Height 
		 PDFUtils.setTableRowHeight(productTable);
		 
		productTable.setModel(productmodel);
		productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		prodcutScrollPane.setViewportView(productTable);
		
		JLabel label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon(ManageProductsUI.class.getResource("/images/search.png")));
		label_1.setBounds(344, 11, 34, 30);
		panelProductDetails.add(label_1);
		
		JButton button = new JButton("");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!PDFUtils.isMandatoryEntered(productCode)){
					JOptionPane.showMessageDialog(contentPane, "Please select Product!");
				}else{
					Product product = new Product();
					product.setProductCode(Integer.valueOf(productCode.getText()));
					product.setProductName(productName.getText());
					
					ViewProductPurPriceHistUI view = new ViewProductPurPriceHistUI(product, frame);
					view.setVisible(true);
				}
			}
		});
		button.setIcon(new ImageIcon(ManageProductsUI.class.getResource("/images/history_clear.png")));
		button.setToolTipText("Purchase Price History");
		button.setBounds(371, 9, 27, 27);
		panel.add(button);
		
		JButton button_1 = new JButton("");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!PDFUtils.isMandatoryEntered(productCode)){
					JOptionPane.showMessageDialog(contentPane, "Please select Product!");
				}else{
				Product product = new Product();
				product.setProductCode(Integer.valueOf(productCode.getText()));
				product.setProductName(productName.getText());
				product.setQuanity(Integer.valueOf(quantity.getText()));
				
				ViewStockLedgerUI viewLedger = new ViewStockLedgerUI(product, frame);
				viewLedger.setVisible(true);
			}
			}
		});
		button_1.setIcon(new ImageIcon(ManageProductsUI.class.getResource("/images/shopping.png")));
		button_1.setToolTipText("View Stock Ledger");
		button_1.setBounds(326, 9, 27, 27);
		panel.add(button_1);
			
		fillProductTableData(productmodel);
		
		productTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = productTable.getSelectedRow();
				int productCode= Integer.valueOf(productTable.getModel().getValueAt(row, 0).toString());
	        	getProductDetails(productCode);
	        	quantity.setEnabled(false);
			}
		});
		
		productTable.getColumnModel().getColumn(6).setMinWidth(0);
		productTable.getColumnModel().getColumn(6).setMaxWidth(0);
		productTable.getColumnModel().getColumn(6).setWidth(0);
		productTable.getColumnModel().getColumn(0).setMinWidth(90);
		productTable.getColumnModel().getColumn(0).setMaxWidth(90);
		productTable.getColumnModel().getColumn(0).setWidth(90);
		productTable.getColumnModel().getColumn(1).setMinWidth(200);
		productTable.getColumnModel().getColumn(1).setMaxWidth(200);
		productTable.getColumnModel().getColumn(1).setWidth(200);
		productTable.getColumnModel().getColumn(3).setMinWidth(70);
		productTable.getColumnModel().getColumn(3).setMaxWidth(70);
		productTable.getColumnModel().getColumn(3).setWidth(70);
		
	//Reset Product Button Action
	btnProductReset.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			resetProductTextFields();
		}
	});
	
	//Save Product Button Action
	btnProductSave.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			saveProductAction();
		}
	});
	
	//delete Product Button
	btnProductDelete.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			deleteProductAction();
		}
	});
	
	//update Product Button
	btnProductUpdate.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			updateProductAction();
		}
	});
	
	quantity.addKeyListener(new KeyAdapter() {
		   public void keyTyped(KeyEvent e) {
		      char c = e.getKeyChar();
		      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
		         e.consume();  // ignore event
		      }
		   }
		});
	purchaseRate.addKeyListener(new KeyAdapter() {
		   public void keyTyped(KeyEvent e) {
		      char c = e.getKeyChar();
		      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)&&(c != KeyEvent.VK_PERIOD)) {
		         e.consume();  // ignore event
		      }
		   }
		   @Override
			public void keyReleased(KeyEvent e) {
			   setPurchasePrice();
			}
		});
	productTax.addKeyListener(new KeyAdapter() {
		   public void keyTyped(KeyEvent e) {
		      char c = e.getKeyChar();
		      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)&&(c != KeyEvent.VK_PERIOD)) {
		         e.consume();  // ignore event
		      }
		   }
		   @Override
			public void keyReleased(KeyEvent e) {
			   setPurchasePrice();
			}
		});
	sellingPrice.addKeyListener(new KeyAdapter() {
		   public void keyTyped(KeyEvent e) {
		      char c = e.getKeyChar();
		      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)&&(c != KeyEvent.VK_PERIOD)) {
		         e.consume();  // ignore event
		      }
		   }
		});
	/*discount.addKeyListener(new KeyAdapter() {
		   public void keyTyped(KeyEvent e) {
		      char c = e.getKeyChar();
		      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)&&(c != KeyEvent.VK_PERIOD)) {
		         e.consume();  // ignore event
		      }
		   }
		});*/
	ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
				
	}
	
	public void fillProductTableData(DefaultTableModel model){
		List<Product> productList= ProductServices.getAllProducts();
		model.setRowCount(0);
		if(productList.isEmpty()){
			JOptionPane.showMessageDialog(contentPane, "No Product found!");
		}else{
			for(Product p : productList){
				 model.addRow(new Object[]{p.getProductCode(), p.getProductName(), p.getProductCategory(),p.getQuanity(),PDFUtils.getDecimalFormat(p.getPurcasePrice()),PDFUtils.getDecimalFormat(p.getSellPrice()),p.getDiscount()});
			}
		}
	}
	
	public void resetProductTextFields(){
		productCode.setText("");
		productName.setText("");
		productDescription.setText("");
		quantity.setText("");
		purchasePrice.setText("");
		purchaseRate.setText("");
		productTax.setText("");
		sellingPrice.setText("");
		//discount.setText("");
		lastUpdate.setText("");
		enterBy.setText("");
		entryDate.setText("");
		productCategory.setSelectedIndex(0);
		productBarCode.setText("");
		quantity.setEnabled(true);
	}
	//Populate categories into dropdown
	public void populateCategories(JComboBox<String> productCategory){
		productCategoryMap = new HashMap<String,Integer>();
		List<ProductCategory> categorylist = ProductCategoryServices.getAllCategories();
		productCategory.addItem("--- SELECT CATEGORY---");
		for (ProductCategory category : categorylist){
			productCategory.addItem(category.getCategoryName());
			productCategoryMap.put(category.getCategoryName(),category.getCategoryCode());
		}
		
	}
	//populate units in dropdown
	public void populateUnits(JComboBox<String> units){
		/*//List<String> unitsList = AppUtils.getAppDataValues("MEASURES");
		
		for (String unit : unitsList){
			units.addItem(unit);
		}*/
		units.addItem("Quantity");
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}
	
	protected void setPurchasePrice() {
		double purRateFinal=0;
		double purchaseRateTemp=0;
		if(!productTax.getText().equals("") && !purchaseRate.getText().equals("")){
			Double tax = Double.parseDouble(productTax.getText());
			purchaseRateTemp = Double.parseDouble(purchaseRate.getText());
			double tempPurRate = purchaseRateTemp;
			tempPurRate= tempPurRate+(purchaseRateTemp/100)*tax;
			purRateFinal = tempPurRate;
			purchasePrice.setText(PDFUtils.getDecimalFormat(purRateFinal));
		}else{
			if(!purchaseRate.getText().equals("")){
				purchaseRateTemp = Double.parseDouble(purchaseRate.getText());
				purchasePrice.setText(PDFUtils.getDecimalFormat(purchaseRateTemp));
			}else{
				purRateFinal = purchaseRateTemp;
				purchasePrice.setText(PDFUtils.getDecimalFormat(purRateFinal));
			}
			
		}
	}

	private void updateProductAction() {
		if(productCode.getText().equals("")){
			JOptionPane.showMessageDialog(contentPane, "Please select product!");
		}else{
			if(PDFUtils.isMandatoryEntered(productName) && PDFUtils.isMandatoryEntered(quantity)
					&& PDFUtils.isMandatoryEntered(purchaseRate)&&PDFUtils.isMandatoryEntered(productTax)  
					&& PDFUtils.isMandatoryEntered(sellingPrice) && PDFUtils.isMandatorySelected(productCategory)){
					Product productToUpdate = new Product();
					productToUpdate.setProductCode(Integer.valueOf(productCode.getText()));
					productToUpdate.setProductName(productName.getText());
					productToUpdate.setDescription(productDescription.getText());
					productToUpdate.setMeasure((String)measure.getSelectedItem());
					productToUpdate.setQuanity(Integer.valueOf(quantity.getText()));
					//productToUpdate.setProductCategory((String)productCategory.getSelectedItem());
					productToUpdate.setCategoryCode(productCategoryMap.get((String)productCategory.getSelectedItem()));
					productToUpdate.setDiscount(0);
					/*if(discount.getText().equals("")){
						productToUpdate.setDiscount(0);
					}else{
						productToUpdate.setDiscount(Double.parseDouble(discount.getText()));
					}*/
					if(purchaseRate.getText().equals("")){
						productToUpdate.setPurcaseRate(0);
					}else{
						productToUpdate.setPurcaseRate(Double.parseDouble(purchaseRate.getText()));
					}
					if(productTax.getText().equals("")){
						productToUpdate.setProductTax(0);
					}else{
						productToUpdate.setProductTax(Double.parseDouble(productTax.getText()));
					}
					productToUpdate.setPurcasePrice(Double.parseDouble(purchasePrice.getText()));
					productToUpdate.setSellPrice(Double.parseDouble(sellingPrice.getText()));
					productToUpdate.setEnterBy(userDetails.getFirstName()+" "+userDetails.getLastName());
					productToUpdate.setLastUpdateDate(new java.sql.Date(System.currentTimeMillis()));
					if(productBarCode.getText().equals("")){
						productToUpdate.setProductBarCode(Long.valueOf(0));
					}else{
						productToUpdate.setProductBarCode(Long.valueOf(productBarCode.getText()));
					}
					if(ProductServices.getProductBarCodeMap().containsKey(productToUpdate.getProductBarCode()) && 
							ProductServices.getProductBarCodeMap().get(productToUpdate.getProductBarCode()).getProductCode()!=productToUpdate.getProductCode()){
						JOptionPane.showMessageDialog(null, "Entered Product Barcode already exists.", "Error", JOptionPane.WARNING_MESSAGE);
					}else{
						StatusDTO status = ProductServices.updateProduct(productToUpdate);
						
						if(status.getStatusCode()==0){
							JOptionPane.showMessageDialog(contentPane, "Product Updated!");
							fillProductTableData(productmodel);
							resetProductTextFields();
						}else{
							if(status.getException().contains("Duplicate entry")){
								JOptionPane.showMessageDialog(null, "Entered Product Name already exists.", "Error", JOptionPane.WARNING_MESSAGE);
							}else{
								JOptionPane.showMessageDialog(null, "Exception occured ", "Error", JOptionPane.WARNING_MESSAGE);
							}
						}
					}
					
				}else{
					JOptionPane.showMessageDialog(contentPane, "Please Enter Mandatory fields!");
				}
		}
	}

	private void deleteProductAction() {
		if(productCode.getText().equals("")){
			JOptionPane.showMessageDialog(contentPane, "Please select Product!");
		}else{
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure?","Warning",dialogButton);
			if(dialogResult == JOptionPane.YES_OPTION){
				ProductServices.deleteProduct(Integer.parseInt(productCode.getText()));
				resetProductTextFields();
				fillProductTableData(productmodel);
			}else{
				resetProductTextFields();
			}
			
		}
	}

	private void saveProductAction() {
		if(productCode.getText().equals("")){
			if(PDFUtils.isMandatoryEntered(productName) && PDFUtils.isMandatoryEntered(quantity)
				&& PDFUtils.isMandatoryEntered(purchasePrice) 
				&& PDFUtils.isMandatoryEntered(sellingPrice)&& PDFUtils.isMandatorySelected(productCategory)){
				Product productToSave = new Product();
				productToSave.setProductCode(PDFUtils.getRandomCode());
				productToSave.setProductName(productName.getText());
				productToSave.setDescription(productDescription.getText());
				productToSave.setMeasure((String)measure.getSelectedItem());
				productToSave.setQuanity(Integer.valueOf(quantity.getText()));
				//productToSave.setProductCategory((String)productCategory.getSelectedItem());
				productToSave.setCategoryCode(productCategoryMap.get((String)productCategory.getSelectedItem()));
				productToSave.setDiscount(0);
				/*if(discount.getText().equals("")){
					productToSave.setDiscount(0);
				}else{
					productToSave.setDiscount(Double.parseDouble(discount.getText()));
				}*/
				if(purchaseRate.getText().equals("")){
					productToSave.setPurcaseRate(0);
				}else{
					productToSave.setPurcaseRate(Double.parseDouble(purchaseRate.getText()));
				}
				if(productTax.getText().equals("")){
					productToSave.setProductTax(0);
				}else{
					productToSave.setProductTax(Double.parseDouble(productTax.getText()));
				}
				productToSave.setPurcasePrice(Double.parseDouble(purchasePrice.getText()));
				productToSave.setSellPrice(Double.parseDouble(sellingPrice.getText()));
				productToSave.setEnterBy(userDetails.getFirstName()+" "+userDetails.getLastName());
				productToSave.setEntryDate(new java.sql.Date(System.currentTimeMillis()));
				productToSave.setLastUpdateDate(new java.sql.Date(System.currentTimeMillis()));
				if(productBarCode.getText().equals("")){
					productToSave.setProductBarCode(Long.valueOf(0));
				}else{
					productToSave.setProductBarCode(Long.valueOf(productBarCode.getText()));
				}
				
				if(ProductServices.getProductBarCodeMap().containsKey(productToSave.getProductBarCode())){
					JOptionPane.showMessageDialog(null, "Entered Product Barcode already exists.", "Error", JOptionPane.WARNING_MESSAGE);
				}else{
					StatusDTO status = ProductServices.addProduct(productToSave);
					if(status.getStatusCode()==0){
						List<Product> list = new ArrayList<Product>();
						productToSave.setDescription("Add Product Opening Stock");
						list.add(productToSave);
						//Update Stock Ledger
						ProductHistoryServices.addProductStockLedger(list, AppConstants.STOCK_IN, AppConstants.ADD_PRODUCT);
						//Add Product Purchase price history
						productToSave.setDescription(AppConstants.ADD_PRODUCT);
						productToSave.setSupplierId(001);
						ProductHistoryServices.addProductPurchasePriceHistory(list);
						JOptionPane.showMessageDialog(contentPane, "Product Added!");
						resetProductTextFields();
						fillProductTableData(productmodel);
					}else{
						if(status.getException().contains("Duplicate entry")){
							JOptionPane.showMessageDialog(null, "Entered Product Name already exists.", "Error", JOptionPane.WARNING_MESSAGE);
						}else{
							JOptionPane.showMessageDialog(null, "Exception occured ", "Error", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
				
			}else{
				JOptionPane.showMessageDialog(contentPane, "Please Enter Mandatory fields!");
			}
		}else{
			JOptionPane.showMessageDialog(contentPane, "Please reset fields!");
		}
	}
	
	private void getProductDetails(int productCodeLocal) {
		Product product = ProductServices.getProduct(productCodeLocal);
		productCode.setText(String.valueOf(product.getProductCode()));
		productName.setText(product.getProductName());
		productDescription.setText(product.getDescription());
		quantity.setText(String.valueOf(product.getQuanity()));
		purchaseRate.setText(PDFUtils.getDecimalFormat(product.getPurcaseRate()));
		productTax.setText(PDFUtils.getDecimalFormat(product.getProductTax()));
		purchasePrice.setText(PDFUtils.getDecimalFormat(product.getPurcasePrice()));
		sellingPrice.setText(PDFUtils.getDecimalFormat(product.getSellPrice()));
		//discount.setText(String.valueOf(product.getDiscount()));
		lastUpdate.setText(product.getLastUpdateDate().toString());
		productBarCode.setText(product.getProductBarCode()==0?"":String.valueOf(product.getProductBarCode()));
		enterBy.setText(product.getEnterBy());
		entryDate.setText(product.getEntryDate().toString());
		productCategory.setSelectedItem(product.getProductCategory());
		
	}
	
	public void searchProductTableData(DefaultTableModel model,String searchString){
		List<Product> productList = null;
		if(searchString.isEmpty()){
			productList= ProductServices.getAllProducts();
		}else{
			productList= ProductServices.searchProduct(searchString);
		}
		
		model.setRowCount(0);
		if(productList.isEmpty()){
			//JOptionPane.showMessageDialog(contentPane, "No Product with matching criteria!");
		}else{
			for(Product p : productList){
				model.addRow(new Object[]{p.getProductCode(), p.getProductName(), p.getProductCategory(),p.getQuanity(),PDFUtils.getDecimalFormat(p.getPurcasePrice()),PDFUtils.getDecimalFormat(p.getSellPrice()),p.getDiscount()});
			}
		}
	}
}
