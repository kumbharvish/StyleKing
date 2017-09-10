package com.shopbilling.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.shopbilling.dto.Product;
import com.shopbilling.dto.ProductCategory;
import com.shopbilling.dto.StatusDTO;
import com.shopbilling.utils.PDFUtils;

public class TempService {
	private static HashMap<String,Integer> productCategoryMap;
	private static final String SAVE_BARCODE ="UPDATE PRODUCT_DETAILS SET CATEGORY_ID=? WHERE PRODUCT_ID=?";
	
	private static final String GET_ALL_PRODUCTS = "SELECT * FROM PRODUCT_DETAILS;";
	
	private final static Logger logger = Logger.getLogger(TempService.class);
	
	public static StatusDTO updateCategory(int category_id,int productCode) {
		Connection conn = null;
		PreparedStatement stmt = null;
		StatusDTO status = new StatusDTO();
		try {
				conn = PDFUtils.getConnection();
				stmt = conn.prepareStatement(SAVE_BARCODE);
				stmt.setInt(1, category_id);
				stmt.setInt(2, productCode);
				
				int i = stmt.executeUpdate();
				if(i>0){
					status.setStatusCode(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			status.setStatusCode(-1);
			status.setException(e.getMessage());
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		return status;
	}
	
	public static List<Product> getAllProducts() {
		Connection conn = null;
		PreparedStatement stmt = null;
		Product pc = null;
		List<Product> productList = new ArrayList<Product>();
		try {
			conn = PDFUtils.getConnection();
			stmt = conn.prepareStatement(GET_ALL_PRODUCTS);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				pc = new Product();
				pc.setProductCode(rs.getInt("PRODUCT_ID"));
				pc.setProductName(rs.getString("PRODUCT_NAME"));
				pc.setProductCategory(rs.getString("PRODUCT_CATEGORY"));
				productList.add(pc);
				Comparator<Product> cp = Product.getComparator(Product.SortParameter.CATEGORY_NAME_ASCENDING); 
				Collections.sort(productList,cp);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		
		return productList;
	}
	
		public static void populateCategories(){
			productCategoryMap = new HashMap<String,Integer>();
			List<ProductCategory> categorylist = ProductCategoryServices.getAllCategories();
			for (ProductCategory category : categorylist){
				productCategoryMap.put(category.getCategoryName(),category.getCategoryCode());
			}
			
		}
		
		public static void main(String[] args) {
			JOptionPane.showMessageDialog(null,"Category Updated Started !");
			PropertyConfigurator.configure(TempService.class.getClassLoader().getResourceAsStream("resources/log4j.properties"));
			populateCategories();
			List<Product> list = getAllProducts();
			logger.error(list);
			for(Product p : list){
				System.out.println(p.getProductCategory());
				int categoryid= productCategoryMap.get(p.getProductCategory());
			logger.error(categoryid);
				updateCategory(categoryid,p.getProductCode());
				logger.error("Update Category For : "+p.getProductName());
			}
			
			JOptionPane.showMessageDialog(null,"Category Updated Completed sucessfully !");
		}

}
