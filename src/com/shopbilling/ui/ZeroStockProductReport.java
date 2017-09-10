package com.shopbilling.ui;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
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
import com.shopbilling.dto.Product;
import com.shopbilling.services.ExcelServices;
import com.shopbilling.services.JasperServices;
import com.shopbilling.services.ProductServices;
import com.shopbilling.utils.JasperUtils;
import com.shopbilling.utils.PDFUtils;

public class ZeroStockProductReport extends JInternalFrame {
	private JTable table;
	private DefaultTableModel reportModel;
	private JTextField tf_totalProducts;
	private int totalProducts=0;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
	/**
	 * Create the frame.
	 */
	public ZeroStockProductReport() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Zero Stock Products Report");
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
					 false, false, false, false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
		 reportModel.setColumnIdentifiers(new String[] {
				 "Product Code", "Product Name","Product Category","Quantity"}
	       );
		table.setModel(reportModel);
		scrollPane.setViewportView(table);
		table.getColumnModel().getColumn(1).setPreferredWidth(170);
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
		
		JButton btnXLS = new JButton("");
		btnXLS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportExcel();
			}
		});
		btnXLS.setBackground(SystemColor.menu);
		btnXLS.setToolTipText("Excel");
		btnXLS.setBorder(BorderFactory.createEmptyBorder());
		btnXLS.setIcon(new ImageIcon(ProductProfitReportUI.class.getResource("/images/excel.png")));
		btnXLS.setBounds(988, 10, 48, 48);
		getContentPane().add(btnXLS);
		PDFUtils.setTableRowHeight(table);
		 ButtonGroup bg = new ButtonGroup();
		 
		 JPanel panel = new JPanel();
		 panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Total Products", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		 panel.setBounds(968, 511, 202, 80);
		 getContentPane().add(panel);
		 panel.setLayout(null);
		 
		 tf_totalProducts = new JTextField();
		 tf_totalProducts.setColumns(10);
		 tf_totalProducts.setBounds(10, 38, 185, 25);
		 tf_totalProducts.setFont(new Font("Dialog", Font.BOLD, 20));
		 tf_totalProducts.setHorizontalAlignment(SwingConstants.RIGHT);
		 tf_totalProducts.setEditable(false);
		 panel.add(tf_totalProducts);
		 fillReportTable();
		 ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
	protected void exportExcel() {
		List<Product> productList= ProductServices.getZeroStockProducts();
		boolean isSucess=ExcelServices.writeZeroStockProductsExcel(productList);
		if(isSucess){
			JOptionPane.showMessageDialog(getContentPane(), "Report Exported Sucessfully! ");
		}else{
			JOptionPane.showMessageDialog(getContentPane(),"Error Occured! ","Error",JOptionPane.WARNING_MESSAGE);
		}
		
	}
	private void fillReportTable() {
		 totalProducts=0;
		List<Product> productList= ProductServices.getZeroStockProducts();
			reportModel.setRowCount(0);
			totalProducts = productList.size();
			if(productList.isEmpty()){
				JOptionPane.showMessageDialog(getContentPane(), "No Products found!");
			}else{
				for(Product p : productList){
					reportModel.addRow(new Object[]{p.getProductCode(), p.getProductName(),p.getProductCategory(),p.getQuanity()});
				}
				setTotalFieldValues();
			}
	}
	
	private void setTotalFieldValues(){
		tf_totalProducts.setText(String.valueOf(totalProducts));
	}
	
	protected void exportPDF() {
		List<Product> productList= ProductServices.getZeroStockProducts();
		List<Map<String,?>> dataSourceMap = JasperServices.createDataForZeroStockProductsReport(productList);
		boolean isSucess=JasperUtils.createPDF(dataSourceMap,AppConstants.ZERO_STOCK_PRODUCT_REPORT_JRXML,"Zero_Stock_Products_Report");
		if(isSucess){
			JOptionPane.showMessageDialog(getContentPane(), "Report Exported Sucessfully! ");
		}else{
			JOptionPane.showMessageDialog(getContentPane(),"Error Occured! ","Error",JOptionPane.WARNING_MESSAGE);
		}
	}
}
