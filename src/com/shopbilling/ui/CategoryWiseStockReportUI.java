package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.ProductCategory;
import com.shopbilling.services.ExcelServices;
import com.shopbilling.services.JasperServices;
import com.shopbilling.services.ProductCategoryServices;
import com.shopbilling.utils.JasperUtils;
import com.shopbilling.utils.PDFUtils;

public class CategoryWiseStockReportUI extends JInternalFrame {
	private JTable table;
	private DefaultTableModel reportModel;
	private JRadioButton rdbtnStockValueAmt;
	private JRadioButton rdbtnCategoryName;
	private JRadioButton rdbtnStockQuantity;
	private JTextField tf_totalQty;
	private JTextField tf_totalStockAmount;
	private int totalQty=0;
	private double totalStockValueAmt=0;
	/**
	 * Create the frame.
	 */
	public CategoryWiseStockReportUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Product Category Wise Stock Report");
		getContentPane().setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Report", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(21, 62, 937, 529);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 26, 909, 492);
		panel_1.add(scrollPane);
		
		table = new JTable();
		reportModel = new DefaultTableModel(){
			 boolean[] columnEditables = new boolean[] {
					 false, false, false, false,false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
		 reportModel.setColumnIdentifiers(new String[] {
				 "Category Code", "Category Name","Stock Quantity","Stock Value Amount"}
	       );
		table.setModel(reportModel);
		scrollPane.setViewportView(table);
		table.getColumnModel().getColumn(1).setPreferredWidth(290);
		JLabel lblExportAs = new JLabel("Export As :");
		lblExportAs.setBounds(842, 22, 65, 21);
		getContentPane().add(lblExportAs);
		
		JButton btnPDF = new JButton("");
		btnPDF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportPDF();
			}
		});
		btnPDF.setBackground(SystemColor.menu);
		btnPDF.setBorder(BorderFactory.createEmptyBorder());
		btnPDF.setToolTipText("PDF");
		btnPDF.setIcon(new ImageIcon(ProductProfitReportUI.class.getResource("/images/pdf.png")));
		btnPDF.setBounds(915, 10, 48, 48);
		getContentPane().add(btnPDF);
		
		JButton btnXSLS = new JButton("");
		btnXSLS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportExcel();
			}
		});
		btnXSLS.setBackground(SystemColor.menu);
		btnXSLS.setToolTipText("Excel");
		btnXSLS.setBorder(BorderFactory.createEmptyBorder());
		btnXSLS.setIcon(new ImageIcon(ProductProfitReportUI.class.getResource("/images/excel.png")));
		btnXSLS.setBounds(988, 10, 48, 48);
		getContentPane().add(btnXSLS);
		
		JLabel lblSortBy = new JLabel("Sort by :");
		lblSortBy.setBounds(233, 22, 53, 21);
		getContentPane().add(lblSortBy);
		
		rdbtnStockValueAmt = new JRadioButton("Stock Value Amount");
		rdbtnStockValueAmt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chageSortParam();
			}
		});
		rdbtnStockValueAmt.setBounds(292, 21, 160, 23);
		getContentPane().add(rdbtnStockValueAmt);
		
		rdbtnCategoryName = new JRadioButton("Category Name");
		rdbtnCategoryName.setBounds(454, 21, 140, 23);
		
		rdbtnCategoryName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chageSortParam();
			}
		});
		
		
		getContentPane().add(rdbtnCategoryName);
		PDFUtils.setTableRowHeight(table);
		 
		 rdbtnStockQuantity = new JRadioButton("Stock Quantity");
		 rdbtnStockQuantity.setSelected(false);
		 rdbtnStockQuantity.setBounds(596, 21, 109, 23);
		 rdbtnStockQuantity.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chageSortParam();
				}
			});
		 ButtonGroup bg = new ButtonGroup();
			bg.add(rdbtnCategoryName);
			bg.add(rdbtnStockValueAmt);
			bg.add(rdbtnStockQuantity);
			rdbtnStockValueAmt.setSelected(true);
			 rdbtnCategoryName.setSelected(false);
			 rdbtnStockQuantity.setSelected(false);
		 getContentPane().add(rdbtnStockQuantity);
		 
		 JPanel panel = new JPanel();
		 panel.setBorder(new TitledBorder(null, "Total Stock", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		 panel.setBounds(968, 433, 202, 158);
		 getContentPane().add(panel);
		 panel.setLayout(null);
		 
		 JLabel lblTotal = new JLabel("Total Quantity");
		 lblTotal.setBounds(10, 30, 134, 25);
		 panel.add(lblTotal);
		 
		 tf_totalQty = new JTextField();
		 tf_totalQty.setColumns(10);
		 tf_totalQty.setBounds(10, 53, 185, 25);
		 tf_totalQty.setFont(new Font("Dialog", Font.BOLD, 20));
		 tf_totalQty.setHorizontalAlignment(SwingConstants.RIGHT);
		 tf_totalQty.setEditable(false);
		 panel.add(tf_totalQty);
		 
		 JLabel lblTotalStockValue = new JLabel("Total Stock Value Amount");
		 lblTotalStockValue.setBounds(10, 89, 150, 25);
		 panel.add(lblTotalStockValue);
		 
		 tf_totalStockAmount = new JTextField();
		 tf_totalStockAmount.setColumns(10);
		 tf_totalStockAmount.setBounds(45, 116, 150, 30);
		 tf_totalStockAmount.setBackground(Color.GRAY);
		 tf_totalStockAmount.setForeground(Color.WHITE);
		 tf_totalStockAmount.setEditable(false);
		 tf_totalStockAmount.setFont(new Font("Dialog", Font.BOLD, 20));
		 tf_totalStockAmount.setHorizontalAlignment(SwingConstants.RIGHT);
		 panel.add(tf_totalStockAmount);
		 
		 JLabel label = new JLabel("New label");
		 label.setIcon(new ImageIcon(SalesStockReportUI.class.getResource("/images/currency_sign_rupee.png")));
		 label.setBounds(10, 116, 29, 30);
		 panel.add(label);
		 fillReportTable(ProductCategory.SortParameter.STOCK_VALUE_AMT_ASC);
		 ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
	private void fillReportTable(ProductCategory.SortParameter key) {
		totalQty=0;
		totalStockValueAmt=0;
		List<ProductCategory> productCategoryList= ProductCategoryServices.getCategoryWiseStockReprot();
		 Comparator<ProductCategory> cp = ProductCategory.getComparator(key); 
			Collections.sort(productCategoryList,cp);
			reportModel.setRowCount(0);
			if(productCategoryList.isEmpty()){
				JOptionPane.showMessageDialog(getContentPane(), "No Records found!");
			}else{
				for(ProductCategory p : productCategoryList){
					reportModel.addRow(new Object[]{p.getCategoryCode(), p.getCategoryName(), p.getCategoryStockQty(),PDFUtils.getDecimalFormat(p.getCategoryStockAmount())});
					totalQty += p.getCategoryStockQty();
					totalStockValueAmt += p.getCategoryStockAmount();
				}
				setTotalFieldValues();
			}
	}
	
	private void chageSortParam(){
		if(rdbtnStockValueAmt.isSelected()){
			fillReportTable(ProductCategory.SortParameter.STOCK_VALUE_AMT_ASC);
		}
		if(rdbtnCategoryName.isSelected()){
			fillReportTable(ProductCategory.SortParameter.CATEGORY_NAME_ASCENDING);
		}
		if(rdbtnStockQuantity.isSelected()){
			fillReportTable(ProductCategory.SortParameter.STOCK_QUANTITY_ASC);
		}
	}
	
	private void setTotalFieldValues(){
		tf_totalQty.setText(String.valueOf(totalQty));
		tf_totalStockAmount.setText(PDFUtils.getDecimalFormat(totalStockValueAmt));
	}
	
	protected void exportExcel() {
		List<ProductCategory> productCategoryList= ProductCategoryServices.getCategoryWiseStockReprot();
		if(rdbtnStockValueAmt.isSelected()){
			 Comparator<ProductCategory> cp = ProductCategory.getComparator(ProductCategory.SortParameter.STOCK_VALUE_AMT_ASC); 
				Collections.sort(productCategoryList,cp);
		}
		if(rdbtnCategoryName.isSelected()){
			 Comparator<ProductCategory> cp = ProductCategory.getComparator(ProductCategory.SortParameter.CATEGORY_NAME_ASCENDING); 
				Collections.sort(productCategoryList,cp);
		}
		if(rdbtnStockQuantity.isSelected()){
			 Comparator<ProductCategory> cp = ProductCategory.getComparator(ProductCategory.SortParameter.STOCK_QUANTITY_ASC); 
				Collections.sort(productCategoryList,cp);
		}
		boolean isSucess=ExcelServices.writeCategoryWiseStockExcel(productCategoryList);
		if(isSucess){
			JOptionPane.showMessageDialog(getContentPane(), "Report Exported Sucessfully! ");
		}else{
			JOptionPane.showMessageDialog(getContentPane(),"Error Occured! ","Error",JOptionPane.WARNING_MESSAGE);
		}
			
		
	}
	
	protected void exportPDF() {
		List<ProductCategory> productCategoryList= ProductCategoryServices.getCategoryWiseStockReprot();
		if(rdbtnStockValueAmt.isSelected()){
			 Comparator<ProductCategory> cp = ProductCategory.getComparator(ProductCategory.SortParameter.STOCK_VALUE_AMT_ASC); 
				Collections.sort(productCategoryList,cp);
		}
		if(rdbtnCategoryName.isSelected()){
			 Comparator<ProductCategory> cp = ProductCategory.getComparator(ProductCategory.SortParameter.CATEGORY_NAME_ASCENDING); 
				Collections.sort(productCategoryList,cp);
		}
		if(rdbtnStockQuantity.isSelected()){
			 Comparator<ProductCategory> cp = ProductCategory.getComparator(ProductCategory.SortParameter.STOCK_QUANTITY_ASC); 
				Collections.sort(productCategoryList,cp);
		}
		List<Map<String,?>> dataSourceMap = JasperServices.createDataForCategoryWiseStockReport(productCategoryList);
		boolean isSucess=JasperUtils.createPDF(dataSourceMap,AppConstants.CATEGORY_WISE_STOCK_REPORT,"Category_Wise_Stock_Report");
		if(isSucess){
			JOptionPane.showMessageDialog(getContentPane(), "Report Exported Sucessfully! ");
		}else{
			JOptionPane.showMessageDialog(getContentPane(),"Error Occured! ","Error",JOptionPane.WARNING_MESSAGE);
		}
	}
}
