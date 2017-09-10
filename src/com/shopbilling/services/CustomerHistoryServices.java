package com.shopbilling.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.shopbilling.dto.BillDetails;
import com.shopbilling.dto.Customer;
import com.shopbilling.dto.CustomerPaymentHistory;
import com.shopbilling.dto.CustomerProfit;
import com.shopbilling.utils.PDFUtils;

public class CustomerHistoryServices {

	
	private static final String GET_ALL_CUSTOMERS_HISTORY = "SELECT CPH.*,CD.CUST_NAME AS CUSTOMER_NAME FROM CUSTOMER_PAYMENT_HISTORY CPH,CUSTOMER_DETAILS CD WHERE CPH.CUST_MOB_NO=? AND CPH.CUST_MOB_NO=CD.CUST_MOB_NO ORDER BY TIMESTAMP DESC";
	
	private static final String GET_ALL_CUST_BILLS = "SELECT CBD.*,CD.CUST_NAME AS CUSTOMER_NAME FROM CUSTOMER_BILL_DETAILS CBD,CUSTOMER_DETAILS CD WHERE CBD.CUST_MOB_NO=? AND CBD.CUST_MOB_NO=CD.CUST_MOB_NO ORDER BY BILL_DATE_TIME DESC";
	
	private static final String CUSTOMER_WISE_PROFIT = "SELECT CUST_MOB_NO, SUM(NET_SALES_AMOUNT) AS SUM_BILL_AMT ,SUM(BILL_PURCHASE_AMT)AS SUM_BILL_PUR_AMT,SUM(NO_OF_ITEMS) AS TOTAL_NO_OF_ITEMS,SUM(BILL_QUANTITY)  AS TOTAL_QUANTITY FROM CUSTOMER_BILL_DETAILS WHERE DATE(BILL_DATE_TIME) BETWEEN ? AND ? GROUP BY CUST_MOB_NO";
		//Get All Customers Payment History
		public static List<CustomerPaymentHistory> getAllCustomersPayHistory(Long customerMobile) {
			Connection conn = null;
			PreparedStatement stmt = null;
			CustomerPaymentHistory customer = null;
			List<CustomerPaymentHistory> customerList = new ArrayList<CustomerPaymentHistory>();
			try {
				conn = PDFUtils.getConnection();
				stmt = conn.prepareStatement(GET_ALL_CUSTOMERS_HISTORY);
				System.out.println("GET_ALL_CUSTOMERS_HISTORY" +GET_ALL_CUSTOMERS_HISTORY);
				stmt.setLong(1, customerMobile);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					customer = new CustomerPaymentHistory();
					customer.setCustMobNo(rs.getLong("CUST_MOB_NO"));
					customer.setCustName(rs.getString("CUSTOMER_NAME"));
					customer.setClosingBlanace(rs.getDouble("AMOUNT"));
					customer.setEntryDate(rs.getTimestamp("TIMESTAMP"));
					customer.setStatus(rs.getString("STATUS"));
					customer.setNarration(rs.getString("NARRATION"));
					customer.setCredit(rs.getDouble("CREDIT"));
					customer.setDebit(rs.getDouble("DEBIT"));
					
					customerList.add(customer);
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				PDFUtils.closeConnectionAndStatment(conn, stmt);
			}
			return customerList;
		}
		
			//Get Bill Details
				public static List<BillDetails> getBillDetails(Long customerMobile) {
					Connection conn = null;
					PreparedStatement stmt = null;
					BillDetails billDetails=null;
					List<BillDetails> billDetailsList = new ArrayList<BillDetails>();
					try {
						if(customerMobile !=null){
							conn = PDFUtils.getConnection();
							stmt = conn.prepareStatement(GET_ALL_CUST_BILLS);
							System.out.println("GET_ALL_CUST_BILLS "+GET_ALL_CUST_BILLS);
							stmt.setLong(1,customerMobile);
							ResultSet rs = stmt.executeQuery();
							
							while (rs.next()) {
								billDetails = new BillDetails();
								billDetails.setBillNumber(rs.getInt("BILL_NUMBER"));
								billDetails.setTimestamp(rs.getTimestamp("BILL_DATE_TIME"));
								billDetails.setCustomerMobileNo(rs.getLong("CUST_MOB_NO"));
								billDetails.setCustomerName(rs.getString("CUSTOMER_NAME"));
								billDetails.setNoOfItems(rs.getInt("NO_OF_ITEMS"));
								billDetails.setTotalQuanity(rs.getInt("BILL_QUANTITY"));
								billDetails.setTotalAmount(rs.getDouble("TOTAL_AMOUNT"));
								billDetails.setTax(rs.getDouble("BILL_TAX"));
								billDetails.setGrandTotal(rs.getDouble("GRAND_TOTAL"));
								billDetails.setPaymentMode(rs.getString("PAYMENT_MODE"));
								billDetails.setDiscount(rs.getDouble("BILL_DISCOUNT"));
								billDetails.setDiscountAmt(rs.getDouble("BILL_DISC_AMOUNT"));
								billDetails.setNetSalesAmt(rs.getDouble("NET_SALES_AMOUNT"));
								billDetails.setPurchaseAmt(rs.getDouble("BILL_PURCHASE_AMT"));
								
								billDetailsList.add(billDetails);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						PDFUtils.closeConnectionAndStatment(conn, stmt);
					}
					return billDetailsList;
				}
		
		//Customer wise profit report
		public static List<CustomerProfit> getCustomerWiseProfit(Date fromDate,Date toDate) {
			Connection conn = null;
			PreparedStatement stmt = null;
			CustomerProfit customer = null;
			List<CustomerProfit> customerList = new ArrayList<CustomerProfit>();
			try {
				if(fromDate==null){
					fromDate = new Date(1947/01/01);
				}
				if(toDate==null){
					toDate = new Date(System.currentTimeMillis());
				}
				conn = PDFUtils.getConnection();
				stmt = conn.prepareStatement(CUSTOMER_WISE_PROFIT);
				stmt.setDate(1, fromDate);
				stmt.setDate(2, toDate);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					customer = new CustomerProfit();
					customer.setCustMobileNumber(rs.getLong("CUST_MOB_NO"));
					customer.setSumOfBillAmt(rs.getDouble("SUM_BILL_AMT"));
					customer.setSumOfBillPurAmt(rs.getDouble("SUM_BILL_PUR_AMT"));
					customer.setTotalNoOfItems(rs.getInt("TOTAL_NO_OF_ITEMS"));
					customer.setTotalQty(rs.getInt("TOTAL_QUANTITY"));
					
					customerList.add(customer);
					Collections.sort(customerList);
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				PDFUtils.closeConnectionAndStatment(conn, stmt);
			}
			return customerList;
		}
		
		public static HashMap<Long,Customer> getCustomerMap(){
			HashMap<Long,Customer> customerMap= new HashMap<Long,Customer>();
			for(Customer cust : UserServices.getAllCustomers()){
				customerMap.put(cust.getCustMobileNumber(), cust);
			}
			return customerMap;
			
			
		}
}
