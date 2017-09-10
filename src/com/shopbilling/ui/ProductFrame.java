package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.LabelAlignment;

import com.shopbilling.dto.Product;
import com.shopbilling.dto.ProductCategory;
import com.shopbilling.dto.StatusDTO;
import com.shopbilling.dto.UserDetails;
import com.shopbilling.services.ProductCategoryServices;
import com.shopbilling.services.ProductServices;
import com.shopbilling.utils.PDFUtils;

public class ProductFrame extends JInternalFrame {

	private JPanel contentPane;
	private UserDetails userDetails;
	//Product Category
	private JTable table;
	JTextField categoryCode ;
	JTextField categoryName;
	JTextField categoryDescription; 
	//JTextField categoryComission ;
	DefaultTableModel model;
	//Product Data members
	private JTable productTable;
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
	private JTextField searchTextField;

	public ProductFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Products");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setResizable(false);
		//fix the frame location
		//setUndecorated(true);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 1014, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(16)
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 564, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(78, Short.MAX_VALUE))
		);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Add Product Category", null, panel, null);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Product Category", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(126, 28, 804, 473);
		panel.add(panel_1);

		JPanel panel_2 = new JPanel();
		DesignGridLayout layout = new DesignGridLayout(panel_2);
		layout.labelAlignment(LabelAlignment.RIGHT);
		 categoryCode = new JTextField();
		 categoryCode.setEditable(false);
		 categoryName = new JTextField(20);
		 categoryDescription = new JTextField(20);
		 //categoryComission = new JTextField(10);
 		JButton deleteButton = new JButton("Delete");
 		JButton updateButton = new JButton("Update");
 		JButton saveButton = new JButton("Add");
 		JButton resetButton = new JButton("Reset");
		layout.row().grid(new JLabel("Category Name *:"))	.add(categoryName).grid(new JLabel(""));
		layout.row().grid(new JLabel("Category Description :"))	.add(categoryDescription).grid(new JLabel(""));//.grid(new JLabel("Category Commission *:"))	.add(categoryComission);
		layout.emptyRow();
 		layout.row().right().add(saveButton).add(updateButton).add(deleteButton).add(resetButton);
		
		JScrollPane scrollPane = new JScrollPane();
		//Form End
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap(37, Short.MAX_VALUE)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
							.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 739, GroupLayout.PREFERRED_SIZE)
							.addGap(25))
						.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 741, GroupLayout.PREFERRED_SIZE)
							.addGap(24))))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
					.addContainerGap())
		);
		 model = new DefaultTableModel(){
			 boolean[] columnEditables = new boolean[] {
						false, false, false, false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
        model.setColumnIdentifiers(new String[] {
				"Categroy Code", "Categroy Name", "Categroy Description", "Comission"
			}
        );
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
	        	categoryCode.setText(table.getModel().getValueAt(row, 0).toString());
				categoryName.setText(table.getModel().getValueAt(row, 1).toString());
				categoryDescription.setText(table.getModel().getValueAt(row, 2).toString());
				//categoryComission.setText(table.getModel().getValueAt(row, 3).toString());
			}
		});
		table.setModel(model);
		scrollPane.setViewportView(table);
		//Hide Category Code Column
		table.getColumnModel().getColumn(0).setMinWidth(0);
		 table.getColumnModel().getColumn(0).setMaxWidth(0);
		 table.getColumnModel().getColumn(0).setWidth(0);
		//Hide Category Comission
		 table.getColumnModel().getColumn(3).setMinWidth(0);
		 table.getColumnModel().getColumn(3).setMaxWidth(0);
		 table.getColumnModel().getColumn(3).setWidth(0);
		 
		panel_1.setLayout(gl_panel_1);
		contentPane.setLayout(gl_contentPane);
		fillCategoryTableData(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		 JTableHeader header = table.getTableHeader();
		 header.setFont(new Font("Dialog", Font.BOLD, 12));
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Add Product", null, panel_3, null);
		//Product Form Start
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "New Product Entry", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		DesignGridLayout productEntryLayout = new DesignGridLayout(panel_4);
		productEntryLayout.labelAlignment(LabelAlignment.RIGHT);
		 productCode = new JTextField();
		 productCode.setEditable(false);
		 productName = new JTextField(20);
		 productDescription = new JTextField(20);
		 quantity = new JTextField(10);
		 productCategory = new JComboBox<String>();
		 //Fill category in dropdown
		 populateCategories(productCategory);
		 measure = new JComboBox<String>();
		 populateUnits(measure);
		 measure.setEnabled(false);
		 purchasePrice = new JTextField();
		 purchasePrice.setEnabled(false);
		 purchasePrice.setDisabledTextColor(Color.BLUE);
		 purchasePrice.setFont(new Font("Dialog", Font.BOLD, 12));
		 purchaseRate = new JTextField();
		 productBarCode = new JTextField();
		 productTax = new JTextField();
		 sellingPrice = new JTextField();
		// discount = new JTextField();
		 lastUpdate = new JTextField();
		 lastUpdate.setEditable(false);
		 enterBy = new JTextField();
		 enterBy.setEditable(false);
		 entryDate = new JTextField();
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
 		productEntryLayout.row().right().add(btnProductSave).add(btnProductUpdate).add(btnProductDelete).add(btnProductReset);
 		//Product Form End
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "Product Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 512, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		JLabel lblSearchProductBelow = new JLabel("Search Product Below : ");
		lblSearchProductBelow.setFont(new Font("Tahoma", Font.BOLD, 11));
		searchTextField = new JTextField();
		/*searchTextField.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
			}
		});*/
		searchTextField.setColumns(10);
		JScrollPane prodcutScrollPane = new JScrollPane();
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_5.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_5.createParallelGroup(Alignment.TRAILING)
						.addComponent(prodcutScrollPane, GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
						.addGroup(gl_panel_5.createSequentialGroup()
							.addComponent(lblSearchProductBelow)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(searchTextField, GroupLayout.PREFERRED_SIZE, 196, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel_5.setVerticalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_5.createSequentialGroup()
					.addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE)
						.addComponent(searchTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSearchProductBelow, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
					.addComponent(prodcutScrollPane, GroupLayout.PREFERRED_SIZE, 428, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
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
    		   "Product Code", "Product Name", "Category", "Quantity", "Purchase Price", "Sell Price", "Discount"}
       );
		 productTable.setModel(productmodel);
		 productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		 JTableHeader headerProduct = productTable.getTableHeader();
		 headerProduct.setFont(new Font("Dialog", Font.BOLD, 12));
		prodcutScrollPane.setViewportView(productTable);
		panel_5.setLayout(gl_panel_5);
		panel_3.setLayout(gl_panel_3);
		fillProductTableData(productmodel);
		
		productTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = productTable.getSelectedRow();
				int productCode= Integer.valueOf(productTable.getModel().getValueAt(row, 0).toString());
	        	getProductDetails(productCode);
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
		//Reset Button Action
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetCategoryTextFields();
			}
		});
		
		btnProductReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetProductTextFields();
			}
		});
		
		//Save Button Action
				saveButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(categoryCode.getText().equals("")){
							if(PDFUtils.isMandatoryEntered(categoryName))// && AppUtils.isMandatoryEntered(categoryComission))
								{
								ProductCategory productCategory = new ProductCategory();
								productCategory.setCategoryName(categoryName.getText());
								productCategory.setCategoryDescription(categoryDescription.getText());
								//productCategory.setComission(Double.parseDouble(categoryComission.getText()));
								StatusDTO status = ProductCategoryServices.addCategory(productCategory);
								if(status.getStatusCode()==0){
									resetCategoryTextFields();
									fillCategoryTableData(model);
									JOptionPane.showMessageDialog(contentPane, "Product Category Added!");
								}else{
									if(status.getException().contains("Duplicate entry")){
										JOptionPane.showMessageDialog(null, "Entered Product Category Name already exists.", "Error", JOptionPane.WARNING_MESSAGE);
									}else{
										JOptionPane.showMessageDialog(null, "Exception occured ", "Error", JOptionPane.WARNING_MESSAGE);
									}
								}
							}else{
								JOptionPane.showMessageDialog(contentPane, "Please Enter Mandatory fields!");
							}
						}else{
							JOptionPane.showMessageDialog(contentPane, "Please reset fields!");
						}
						
					}
				});
				//Update Button Action
				updateButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ProductCategory productCategory = new ProductCategory();
						if(categoryCode.getText().equals("")){
							JOptionPane.showMessageDialog(contentPane, "Please select Category!");
						}else{
							if(PDFUtils.isMandatoryEntered(categoryName))// && AppUtils.isMandatoryEntered(categoryComission))
							{
							productCategory.setCategoryCode(Integer.parseInt(categoryCode.getText()));
							productCategory.setCategoryName(categoryName.getText());
							productCategory.setCategoryDescription(categoryDescription.getText());
							//productCategory.setComission(Double.parseDouble(categoryComission.getText()));
							
							StatusDTO status = ProductCategoryServices.updateCategory(productCategory);
							if(status.getStatusCode()==0){
								resetCategoryTextFields();
								fillCategoryTableData(model);
								JOptionPane.showMessageDialog(contentPane, "Product Category Updated!");
							}else{
								if(status.getException().contains("Duplicate entry")){
									JOptionPane.showMessageDialog(null, "Entered Product Category Name already exists.", "Error", JOptionPane.WARNING_MESSAGE);
								}else{
									JOptionPane.showMessageDialog(null, "Exception occured ", "Error", JOptionPane.WARNING_MESSAGE);
								}
							}
							}else{
								JOptionPane.showMessageDialog(contentPane, "Please Enter Mandatory fields!");
							}
						}
						
					}
				});
				
				deleteButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(categoryCode.getText().equals("")){
							JOptionPane.showMessageDialog(contentPane, "Please select Category!");
						}else{
							int dialogButton = JOptionPane.YES_NO_OPTION;
							int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure?","Warning",dialogButton);
							if(dialogResult == JOptionPane.YES_OPTION){
								ProductCategoryServices.deleteCategory(Integer.parseInt(categoryCode.getText()));
								resetCategoryTextFields();
								fillCategoryTableData(model);
							}else{
								resetCategoryTextFields();
							}
							
						}
						
					}
				});
				
				/*categoryComission.addKeyListener(new KeyAdapter() {
					   public void keyTyped(KeyEvent e) {
					      char c = e.getKeyChar();
					      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)&&(c != KeyEvent.VK_PERIOD)) {
					         e.consume();  // ignore event
					      }
					   }
					});*/
				
				//Search text field listner
				searchTextField.addKeyListener(new KeyAdapter() {
					/*@Override
					public void keyPressed(KeyEvent e) {
						searchProductTableData(productmodel,searchTextField.getText());
					}*/
					@Override
					public void keyTyped(KeyEvent e) {
						searchProductTableData(productmodel,searchTextField.getText());
					}
				});
				
				//Save Product Button Action
				btnProductSave.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
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
								productToSave.setProductCategory((String)productCategory.getSelectedItem());
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
				});
				
				//delete Product Button
				btnProductDelete.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
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
				});
				
				//update Product Button
				btnProductUpdate.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
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
									productToUpdate.setProductCategory((String)productCategory.getSelectedItem());
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
				
	}
	
	public void fillCategoryTableData(DefaultTableModel model){
		List<ProductCategory> categoriesList= ProductCategoryServices.getAllCategories();
		model.setRowCount(0);
		if(categoriesList.isEmpty()){
			JOptionPane.showMessageDialog(contentPane, "No Product Categories found!");
		}else{
			for(ProductCategory p : categoriesList){
				 model.addRow(new Object[]{p.getCategoryCode(), p.getCategoryName(), p.getCategoryDescription(), p.getComission()});
			}
		}
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
				 model.addRow(new Object[]{p.getProductCode(), p.getProductName(), p.getProductCategory(), p.getQuanity(),p.getPurcasePrice(),p.getSellPrice(),p.getDiscount()});
			}
		}
	}
	public void resetCategoryTextFields(){
		categoryCode.setText("");
		categoryName.setText("");
		categoryDescription.setText("");
		//categoryComission.setText("");
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
	}
	//Populate categories into dropdown
	public void populateCategories(JComboBox<String> productCategory){
		List<ProductCategory> categorylist = ProductCategoryServices.getAllCategories();
		productCategory.addItem("--- SELECT CATEGORY---");
		for (ProductCategory category : categorylist){
			productCategory.addItem(category.getCategoryName());
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
}
