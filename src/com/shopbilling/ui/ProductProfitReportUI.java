package com.shopbilling.ui;

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
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.Product;
import com.shopbilling.services.ExcelServices;
import com.shopbilling.services.JasperServices;
import com.shopbilling.services.ProductServices;
import com.shopbilling.utils.JasperUtils;
import com.shopbilling.utils.PDFUtils;

public class ProductProfitReportUI extends JInternalFrame {
	private JTable table;
	private DefaultTableModel reportModel;
	private JRadioButton rdbtnProductProfit;
	private JRadioButton rdbtnProductName;
	/**
	 * Create the frame.
	 */
	public ProductProfitReportUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Product Profit Report");
		getContentPane().setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Report", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(77, 63, 937, 529);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 26, 909, 492);
		panel_1.add(scrollPane);
		
		table = new JTable();
		reportModel = new DefaultTableModel(){
			 boolean[] columnEditables = new boolean[] {
					 false, false, false, false,false,false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
		 reportModel.setColumnIdentifiers(new String[] {
				 "Product Code", "Product Name","Product Profit Amount"}
	       );
		table.setModel(reportModel);
		scrollPane.setViewportView(table);
		table.getColumnModel().getColumn(1).setPreferredWidth(290);
		JLabel lblExportAs = new JLabel("Export As :");
		lblExportAs.setBounds(842, 22, 65, 21);
		getContentPane().add(lblExportAs);
		
		JButton btnPDFButton = new JButton("");
		btnPDFButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportPDF();
			}
		});
		btnPDFButton.setBackground(SystemColor.menu);
		btnPDFButton.setBorder(BorderFactory.createEmptyBorder());
		btnPDFButton.setToolTipText("PDF");
		btnPDFButton.setIcon(new ImageIcon(ProductProfitReportUI.class.getResource("/images/pdf.png")));
		btnPDFButton.setBounds(915, 10, 48, 48);
		getContentPane().add(btnPDFButton);
		
		JButton btnXLSButton = new JButton("");
		btnXLSButton.setBackground(SystemColor.menu);
		btnXLSButton.setToolTipText("Excel");
		btnXLSButton.setBorder(BorderFactory.createEmptyBorder());
		btnXLSButton.setIcon(new ImageIcon(ProductProfitReportUI.class.getResource("/images/excel.png")));
		btnXLSButton.setBounds(988, 10, 48, 48);
		getContentPane().add(btnXLSButton);
		
		btnXLSButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportExcel();
			}
		});
		
		JLabel lblSortBy = new JLabel("Sort by :");
		lblSortBy.setBounds(346, 22, 53, 21);
		getContentPane().add(lblSortBy);
		
		rdbtnProductProfit = new JRadioButton("Product Profit");
		rdbtnProductProfit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chageSortParam();
			}
		});
		rdbtnProductProfit.setBounds(405, 22, 109, 23);
		getContentPane().add(rdbtnProductProfit);
		
		rdbtnProductName = new JRadioButton("Product Name");
		rdbtnProductName.setBounds(523, 21, 109, 23);
		
		rdbtnProductName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chageSortParam();
			}
		});
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(rdbtnProductName);
		bg.add(rdbtnProductProfit);
		getContentPane().add(rdbtnProductName);
		 PDFUtils.setTableRowHeight(table);
		 rdbtnProductProfit.setSelected(true);
		 rdbtnProductName.setSelected(false);
		 fillReportTable(Product.SortParameter.PROFIT_ASCENDING);
		 ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
	protected void exportPDF() {
		List<Product> productList= ProductServices.getAllProducts();
		if(rdbtnProductProfit.isSelected()){
			 Comparator<Product> cp = Product.getComparator(Product.SortParameter.PROFIT_ASCENDING); 
				Collections.sort(productList,cp);
		}
		if(rdbtnProductName.isSelected()){
			 Comparator<Product> cp = Product.getComparator(Product.SortParameter.PRODUCT_NAME_ASCENDING); 
				Collections.sort(productList,cp);
		}
		List<Map<String,?>> dataSourceMap = JasperServices.createDataForProductProfitReport(productList);
		boolean isSucess=JasperUtils.createPDF(dataSourceMap,AppConstants.PRODUCT_PROFIT_REPORT_JRXML,"Product_Profit_Report");
		if(isSucess){
			JOptionPane.showMessageDialog(getContentPane(), "Report Exported Sucessfully! ");
		}else{
			JOptionPane.showMessageDialog(getContentPane(),"Error Occured! ","Error",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	protected void exportExcel() {
		List<Product> productList= ProductServices.getAllProducts();
		if(rdbtnProductProfit.isSelected()){
			 Comparator<Product> cp = Product.getComparator(Product.SortParameter.PROFIT_ASCENDING); 
				Collections.sort(productList,cp);
		}
		if(rdbtnProductName.isSelected()){
			 Comparator<Product> cp = Product.getComparator(Product.SortParameter.PRODUCT_NAME_ASCENDING); 
				Collections.sort(productList,cp);
		}
		boolean isSucess=ExcelServices.writeProductProfitExcel(productList);
		if(isSucess){
			JOptionPane.showMessageDialog(getContentPane(), "Report Exported Sucessfully! ");
		}else{
			JOptionPane.showMessageDialog(getContentPane(),"Error Occured! ","Error",JOptionPane.WARNING_MESSAGE);
		}
			
		
	}
	
	private void fillReportTable(Product.SortParameter PROFIT_ASCENDING) {
		List<Product> productList= ProductServices.getAllProducts();
		 Comparator<Product> cp = Product.getComparator(PROFIT_ASCENDING); 
			Collections.sort(productList,cp);
			reportModel.setRowCount(0);
			if(productList.isEmpty()){
				JOptionPane.showMessageDialog(getContentPane(), "No Product found!");
			}else{
				for(Product p : productList){
					reportModel.addRow(new Object[]{p.getProductCode(), p.getProductName(), PDFUtils.getDecimalFormat(p.getProfit())});
				}
			}
	}
	
	private void chageSortParam(){
		if(rdbtnProductProfit.isSelected()){
			fillReportTable(Product.SortParameter.PROFIT_ASCENDING);
		}
		if(rdbtnProductName.isSelected()){
			fillReportTable(Product.SortParameter.PRODUCT_NAME_ASCENDING);
		}
	}
}
