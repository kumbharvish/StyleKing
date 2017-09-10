package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.LabelAlignment;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.dto.Product;
import com.shopbilling.dto.ProductCategory;
import com.shopbilling.dto.StatusDTO;
import com.shopbilling.dto.UserDetails;
import com.shopbilling.services.ProductCategoryServices;
import com.shopbilling.services.ProductServices;
import com.shopbilling.utils.PDFUtils;

public class ProductCategoryUI extends JInternalFrame {

	private JPanel contentPane;
	private UserDetails userDetails;
	private JTextField categoryCode ;
	private JTextField categoryName;
	private JTextField categoryDescription; 
	//private JTextField categoryComission ;
	private DefaultTableModel productCategoryModel;
	private JTable productCategoryTable;

	public ProductCategoryUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Manage Product Categroy");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setResizable(false);
		
		JPanel panel = new JPanel();
		panel.setBounds(148, 28, 804, 548);
		panel.setLayout(null);
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(34, 27, 739, 111);
		panel.add(panel_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(33, 156, 741, 353);
		panel.add(scrollPane);
		DesignGridLayout layout = new DesignGridLayout(panel_1);
		layout.labelAlignment(LabelAlignment.RIGHT);
		 categoryCode = new JTextField();
		 categoryCode.setEditable(false);
		 categoryName = new JTextField(20);
		 categoryName.setFont(new Font("Tahoma", Font.BOLD, 12));
		 categoryDescription = new JTextField(20);
		 categoryDescription.setFont(new Font("Tahoma", Font.BOLD, 12));
		 //categoryComission = new JTextField(10);
 		JButton deleteButton = new JButton("Delete");
 		JButton updateButton = new JButton("Update");
 		JButton saveButton = new JButton("Add");
 		JButton resetButton = new JButton("Reset");
		layout.row().grid(new JLabel("Category Name *:"))	.add(categoryName).grid(new JLabel(""));
		layout.row().grid(new JLabel("Category Description :"))	.add(categoryDescription).grid(new JLabel(""));//.grid(new JLabel("Category Commission *:"))	.add(categoryComission);
		layout.emptyRow();
 		layout.row().right().add(saveButton).add(updateButton).add(deleteButton).add(resetButton);
 		productCategoryModel = new DefaultTableModel(){
			 boolean[] columnEditables = new boolean[] {
						false, false, false, false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
		 productCategoryModel.setColumnIdentifiers(new String[] {
				"Categroy Code", "Categroy Name", "Categroy Description", "Comission"
			}
        );
		 productCategoryTable = new JTable();
		 productCategoryTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = productCategoryTable.getSelectedRow();
	        	categoryCode.setText(productCategoryTable.getModel().getValueAt(row, 0).toString());
				categoryName.setText(productCategoryTable.getModel().getValueAt(row, 1).toString());
				categoryDescription.setText(productCategoryTable.getModel().getValueAt(row, 2).toString());
				//categoryComission.setText(table.getModel().getValueAt(row, 3).toString());
			}
		});
		 //Table Row Height 
		 PDFUtils.setTableRowHeight(productCategoryTable);
		 
		 productCategoryTable.setModel(productCategoryModel);
		scrollPane.setViewportView(productCategoryTable);
		//Hide Category Code Column
		productCategoryTable.getColumnModel().getColumn(0).setMinWidth(0);
		productCategoryTable.getColumnModel().getColumn(0).setMaxWidth(0);
		productCategoryTable.getColumnModel().getColumn(0).setWidth(0);
		//Hide Category Comission
		productCategoryTable.getColumnModel().getColumn(3).setMinWidth(0);
		productCategoryTable.getColumnModel().getColumn(3).setMaxWidth(0);
		productCategoryTable.getColumnModel().getColumn(3).setWidth(0);
		contentPane.setLayout(null);
		contentPane.add(panel);
		fillCategoryTableData(productCategoryModel);
		 
		//Reset Button Action
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetCategoryTextFields();
			}
		});
		
		//Save Button Action
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveCategoryAction();
			}
		});
		//Update Button Action
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCategoryAction();
			}
		});
		//Delete Button Action
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteCategoryAction();
			}
		});
			
		ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
	
	private void deleteCategoryAction() {
		if(categoryCode.getText().equals("")){
			JOptionPane.showMessageDialog(contentPane, "Please select Category!");
		}else{
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure?","Warning",dialogButton);
			if(dialogResult == JOptionPane.YES_OPTION){
				
				List<Product> productList = ProductCategoryServices.getAllProductsForCategory(Integer.parseInt(categoryCode.getText()));
				if(productList.size()>0){
					JOptionPane.showMessageDialog(getContentPane(), "Total "+productList.size()+" Products under this category. Please delete the products first in order to delete the category.", "Product Exists", JOptionPane.WARNING_MESSAGE);
				}else{
					ProductCategoryServices.deleteCategory(Integer.parseInt(categoryCode.getText()));
					JOptionPane.showMessageDialog(contentPane, "Category deleted Sucessfully!");
					resetCategoryTextFields();
					fillCategoryTableData(productCategoryModel);
				}
			}else{
				resetCategoryTextFields();
			}
			
		}
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
	public void resetCategoryTextFields(){
		categoryCode.setText("");
		categoryName.setText("");
		categoryDescription.setText("");
		//categoryComission.setText("");
	}
	
	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	private void updateCategoryAction() {
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
				fillCategoryTableData(productCategoryModel);
				JOptionPane.showMessageDialog(contentPane, "Product Category Updated!");
			}else{
				if(status.getException().contains("Duplicate entry")){
					JOptionPane.showMessageDialog(getContentPane(), "Entered Product Category Name already exists.", "Error", JOptionPane.WARNING_MESSAGE);
				}else{
					JOptionPane.showMessageDialog(getContentPane(), "Exception occured ", "Error", JOptionPane.WARNING_MESSAGE);
				}
			}
			}else{
				JOptionPane.showMessageDialog(contentPane, "Please Enter Mandatory fields!");
			}
		}
	}

	private void saveCategoryAction() {
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
					fillCategoryTableData(productCategoryModel);
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
}
