package com.shopbilling.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

import com.shopbilling.dto.BillDetails;
import com.shopbilling.dto.ItemDetails;
import com.shopbilling.dto.StatusDTO;
import com.shopbilling.utils.PDFUtils;

public class BillingServices {
	
	private static final String UPDATE_BILL_DETAILS = "UPDATE CUSTOMER_BILL_DETAILS SET CUST_MOB_NO=?,CUST_NAME=?,BILL_TAX=?,BILL_DISCOUNT=?,BILL_DISC_AMOUNT =?," +
			"PAYMENT_MODE=?,GRAND_TOTAL=?,NET_SALES_AMOUNT=? WHERE BILL_NUMBER=?";
	
	private static final String DELETE_BILL_DETAILS = "DELETE FROM CUSTOMER_BILL_DETAILS WHERE BILL_NUMBER=?";
	
	private static final String DELETE_BILL_ITEM_DETAILS = "DELETE FROM BILL_ITEM_DETAILS WHERE BILL_NUMBER=?";
	
	private static final String UPDATE_PRODUCT_STOCK ="UPDATE PRODUCT_DETAILS SET QUANTITY=QUANTITY+? WHERE PRODUCT_ID=?";
	
	private static final String INS_OPENING_CASH = "INSERT INTO CASH_COUNTER " 
			+ "(DATE,AMOUNT)" 
			+ " VALUES(?,?)";
	
	private static final String UPDATE_OPENING_CASH = "UPDATE CASH_COUNTER SET AMOUNT=?" 
			+" WHERE DATE=?";
	
	//Modify Bill Details
	public static StatusDTO modifyBillDetails(BillDetails bill) {
		Connection conn = null;
		PreparedStatement stmt = null;
		StatusDTO staus = new StatusDTO();
		try {
			if(bill!=null){
				conn = PDFUtils.getConnection();
				stmt = conn.prepareStatement(UPDATE_BILL_DETAILS);
				stmt.setLong(1, bill.getCustomerMobileNo());
				stmt.setString(2, bill.getCustomerName());
				stmt.setDouble(3, bill.getTax());
				stmt.setDouble(4, bill.getDiscount());
				stmt.setDouble(5, bill.getDiscountAmt());
				stmt.setString(6, bill.getPaymentMode());
				stmt.setDouble(7, bill.getGrandTotal());
				stmt.setDouble(8, bill.getNetSalesAmt());
				stmt.setInt(9, bill.getBillNumber());
				int i = stmt.executeUpdate();
				if(i>0){
					staus.setStatusCode(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			staus.setStatusCode(-1);
			staus.setException(e.getMessage());
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		return staus;
	}
	//Delete Bill Details including Items
	public static StatusDTO deleteBillDetails(int billNumber) {
		Connection conn = null;
		PreparedStatement stmt = null;
		StatusDTO status = new StatusDTO();
		try {
				conn = PDFUtils.getConnection();
				stmt = conn.prepareStatement(DELETE_BILL_DETAILS);
				stmt.setInt(1,billNumber);
				
				int i = stmt.executeUpdate();
				if(i>0){
					status.setStatusCode(0);
					status = deleteBillItemDetails(billNumber);
				}
		} catch (Exception e) {
			e.printStackTrace();
			status.setException(e.getMessage());
			status.setStatusCode(-1);
			return status;
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		return status;
	}
	
	//Delete bill only Bill Item Details
	public static StatusDTO deleteBillItemDetails(int billNumber) {
		Connection conn = null;
		PreparedStatement stmt = null;
		StatusDTO status = new StatusDTO();
		try {
				conn = PDFUtils.getConnection();
				stmt = conn.prepareStatement(DELETE_BILL_ITEM_DETAILS);
				stmt.setInt(1,billNumber);
				
				int i = stmt.executeUpdate();
				if(i>0){
					status.setStatusCode(0);
				}
		} catch (Exception e) {
			e.printStackTrace();
			status.setException(e.getMessage());
			status.setStatusCode(-1);
			return status;
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		return status;
	}

	//Update Product Stock
	public static StatusDTO updateDeletedBillProductStock(List <ItemDetails> itemList) {
		Connection conn = null;
		PreparedStatement stmt = null;
		StatusDTO status = new StatusDTO();		
		try {
			if(!itemList.isEmpty()){
				conn = PDFUtils.getConnection();
				conn.setAutoCommit(false);
				stmt = conn.prepareStatement(UPDATE_PRODUCT_STOCK);
				for(ItemDetails item : itemList){
					stmt.setInt(2, item.getItemNo());
					stmt.setInt(1, item.getQuantity());
					stmt.addBatch();
				}
				int batch[] = stmt.executeBatch();
				conn.commit();
				if(batch.length == itemList.size()){
					status.setStatusCode(0);
					System.out.println("Product Stock  updated");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status.setStatusCode(-1);
			status.setException(e.getMessage());
			return status;
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		return status;
	}
	
	
	//Add Opening Cash
	
	public static StatusDTO addOpeningCash(double amount) {
		Connection conn = null;
		PreparedStatement stmt = null;
		StatusDTO status = new StatusDTO();
		try {
				conn = PDFUtils.getConnection();
				stmt = conn.prepareStatement(INS_OPENING_CASH);
				stmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
				stmt.setDouble(2, amount);
				
				int i = stmt.executeUpdate();
				if(i>0){
					status.setStatusCode(0);
				}
		} catch (Exception e) {
			e.printStackTrace();
			status.setException(e.getMessage());
			status.setStatusCode(-1);
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		return status;
	}
	
	//Update Opening Cash
	
	public static StatusDTO updateOpeningCash(double amount,Date date) {
		Connection conn = null;
		PreparedStatement stmt = null;
		StatusDTO status = new StatusDTO();
		try {
				conn = PDFUtils.getConnection();
				stmt = conn.prepareStatement(UPDATE_OPENING_CASH);
				stmt.setDouble(1, amount);
				stmt.setDate(2, date);
				
				int i = stmt.executeUpdate();
				if(i>0){
					status.setStatusCode(0);
				}
		} catch (Exception e) {
			e.printStackTrace();
			status.setException(e.getMessage());
			status.setStatusCode(-1);
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		return status;
	}
}
