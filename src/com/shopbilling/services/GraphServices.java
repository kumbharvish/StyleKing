package com.shopbilling.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.shopbilling.dto.GraphDTO;
import com.shopbilling.utils.PDFUtils;

public class GraphServices {

	private static final String PAYMENT_MODE_AMOUNT ="SELECT PAYMENT_MODE,SUM(NET_SALES_AMOUNT) AS TOTAL_AMT FROM CUSTOMER_BILL_DETAILS WHERE " +
			"DATE(BILL_DATE_TIME) BETWEEN ? AND ?  GROUP BY PAYMENT_MODE ORDER BY SUM(NET_SALES_AMOUNT) DESC";
	
	private static final String DAILY_SALES_AMOUNT_REPORT ="SELECT DATE_FORMAT(BILL_DATE_TIME,'%d %b %y') AS BILL_DATE,SUM(NET_SALES_AMOUNT) AS TOTAL_AMT,SUM(BILL_PURCHASE_AMT) AS TOTAL_PUR_AMT FROM CUSTOMER_BILL_DETAILS " +
			"GROUP BY DATE_FORMAT(BILL_DATE_TIME,'%d %b %y') ORDER BY BILL_DATE_TIME DESC ;";
	
	private static final String MONTHLY_SALES_AMOUNT_REPORT ="SELECT DATE_FORMAT(BILL_DATE_TIME,'%b %y') AS BILL_DATE,SUM(NET_SALES_AMOUNT) AS TOTAL_AMT,SUM(BILL_PURCHASE_AMT) AS TOTAL_PUR_AMT FROM CUSTOMER_BILL_DETAILS " +
			"GROUP BY DATE_FORMAT(BILL_DATE_TIME,'%b %y') ORDER BY BILL_DATE_TIME DESC ;";
	
	//Get Payment mode wise collection
	public static List<GraphDTO> getPaymentModeAmounts(Date fromDate,Date toDate){
		List<GraphDTO> list= new ArrayList<GraphDTO>();
		Connection conn = null;
		PreparedStatement stmt = null;
		GraphDTO graph = null;
		try {
			if(fromDate==null){
				fromDate = new Date(1947/01/01);
			}
			if(toDate==null){
				toDate = new Date(System.currentTimeMillis());
			}
			
			conn = PDFUtils.getConnection();
			stmt = conn.prepareStatement(PAYMENT_MODE_AMOUNT);
			stmt.setDate(1, fromDate);
			stmt.setDate(2, toDate);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				graph = new GraphDTO();
				graph.setPaymentMode(rs.getString("PAYMENT_MODE"));
				graph.setTotalAmount(rs.getDouble("TOTAL_AMT"));
				
				list.add(graph);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		return list;
	}
	
	//Get Daily Sales Amount & Profit Amount Bar Graph
		public static List<GraphDTO> getDailySalesReport(){
			List<GraphDTO> list= new ArrayList<GraphDTO>();
			Connection conn = null;
			PreparedStatement stmt = null;
			GraphDTO graph = null;
			try {
				conn = PDFUtils.getConnection();
				stmt = conn.prepareStatement(DAILY_SALES_AMOUNT_REPORT);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					graph = new GraphDTO();
					graph.setDate(rs.getString("BILL_DATE"));
					graph.setTotalCollection((int)rs.getDouble("TOTAL_AMT"));
					graph.setTotalPurchaseAmt((int)rs.getDouble("TOTAL_PUR_AMT"));
					
					list.add(graph);
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				PDFUtils.closeConnectionAndStatment(conn, stmt);
			}
			return list;
		}
		
	//Get Monthly Sales Amount & Profit Amount Bar Graph
			public static List<GraphDTO> getMonthlySalesReport(){
				List<GraphDTO> list= new ArrayList<GraphDTO>();
				Connection conn = null;
				PreparedStatement stmt = null;
				GraphDTO graph = null;
				try {
					conn = PDFUtils.getConnection();
					stmt = conn.prepareStatement(MONTHLY_SALES_AMOUNT_REPORT);
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						graph = new GraphDTO();
						graph.setDate(rs.getString("BILL_DATE"));
						graph.setTotalCollection((int)rs.getDouble("TOTAL_AMT"));
						graph.setTotalPurchaseAmt((int)rs.getDouble("TOTAL_PUR_AMT"));
						
						list.add(graph);
					}
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					PDFUtils.closeConnectionAndStatment(conn, stmt);
				}
				return list;
			}
	

}
