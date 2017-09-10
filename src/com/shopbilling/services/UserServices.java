package com.shopbilling.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.Customer;
import com.shopbilling.dto.StatusDTO;
import com.shopbilling.dto.UserDetails;
import com.shopbilling.utils.PDFUtils;

public class UserServices {

	private static final String VALIDATE_USER_SQL = "SELECT FIRST_NAME,LAST_NAME,USER_ID,USERNAME,USER_TYPE FROM "
			+ "APP_USER_DETAILS WHERE USERNAME=? AND PASSWORD=?";
	
	private static final String GET_USER_DEATILS = "SELECT FIRST_NAME,LAST_NAME,USER_ID,USERNAME,MOBILE_NO,EMAIL FROM "
			+ "APP_USER_DETAILS WHERE USER_ID=?";
	
	private static final String UPDATE_USER_PWD = "UPDATE APP_USER_DETAILS SET PASSWORD=? WHERE USER_ID=? AND PASSWORD=?";
	
	private static final String UPDATE_USERNAME = "UPDATE APP_USER_DETAILS SET USERNAME=? WHERE USERNAME=? AND USER_ID=?";
	
	private static final String UPDATE_USER_DETAILS = "UPDATE APP_USER_DETAILS SET FIRST_NAME=?,LAST_NAME=?,EMAIL=?,MOBILE_NO=? WHERE USER_ID=?";
	
	private static final String GET_CUSTOMER = "SELECT * FROM "
			+ "CUSTOMER_DETAILS WHERE CUST_MOB_NO=?";
	
	private static final String SEARCH_CUSTOMER = "SELECT * FROM "
					+ "CUSTOMER_DETAILS WHERE CONCAT(CUST_MOB_NO,CUST_NAME) LIKE ?";
	
	private static final String GET_ALL_CUSTOMERS = "SELECT * FROM CUSTOMER_DETAILS";
	
	private static final String INS_CUSTOMER = "INSERT INTO CUSTOMER_DETAILS (CUST_MOB_NO,CUST_NAME,CUST_EMAIL,CUST_CITY,ENTRY_DATE,LAST_UPDATE) "
			+ "VALUES(?,?,?,?,?,?)";
	
	private static final String INS_CUSTOMER_PAY_HISTORY = "INSERT INTO CUSTOMER_PAYMENT_HISTORY (CUST_MOB_NO,TIMESTAMP,AMOUNT,STATUS,NARRATION,CREDIT,DEBIT) "
			+ "VALUES(?,?,?,?,?,?,?)";
	
	private static final String UPDATE_CUST_BALANCE = "UPDATE CUSTOMER_DETAILS SET BALANCE_AMOUNT=BALANCE_AMOUNT+? WHERE CUST_MOB_NO=? ";

	private static final String SETTLEUP_CUST_BALANCE = "UPDATE CUSTOMER_DETAILS SET BALANCE_AMOUNT=BALANCE_AMOUNT-? WHERE CUST_MOB_NO=? ";
	
	private static final String DELETE_CUSTOMER = "DELETE FROM  CUSTOMER_DETAILS WHERE CUST_MOB_NO=? ";
	
	private static final String UPDATE_CUSTOMER = "UPDATE CUSTOMER_DETAILS SET CUST_NAME=?,CUST_EMAIL=?,CUST_CITY=?,LAST_UPDATE=? WHERE " +
													"CUST_MOB_NO=?";
	
	public static UserDetails validateUser(String userName, String password) {
		Connection conn = null;
		PreparedStatement stmt = null;
		UserDetails userDetails = null;
		try {
			conn = PDFUtils.getConnection();
			stmt = conn.prepareStatement(VALIDATE_USER_SQL);
			stmt.setString(1, userName);
			stmt.setString(2, PDFUtils.enc(password));
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				userDetails = new UserDetails();
				userDetails.setFirstName(rs.getString("FIRST_NAME"));
				userDetails.setLastName(rs.getString("LAST_NAME"));
				userDetails.setUserId(rs.getInt("USER_ID"));
				userDetails.setUserName(rs.getString("USERNAME"));
				userDetails.setUserType(rs.getString("USER_TYPE"));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		return userDetails;
	}
	
	public static UserDetails getUserDetails(UserDetails userDtls) {
		Connection conn = null;
		PreparedStatement stmt = null;
		UserDetails userDetails = null;
		try {
			conn = PDFUtils.getConnection();
			stmt = conn.prepareStatement(GET_USER_DEATILS);
			stmt.setInt(1, userDtls.getUserId());
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				userDetails = new UserDetails();
				userDetails.setFirstName(rs.getString("FIRST_NAME"));
				userDetails.setLastName(rs.getString("LAST_NAME"));
				userDetails.setUserId(rs.getInt("USER_ID"));
				userDetails.setUserName(rs.getString("USERNAME"));
				userDetails.setEmail(rs.getString("EMAIL"));
				userDetails.setMobileNo(rs.getLong("MOBILE_NO"));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		return userDetails;
	}
	
	//Update username
	public static StatusDTO changeUserName(UserDetails userDetails,String newUserName) {
		Connection conn = null;
		PreparedStatement stmt = null;
		StatusDTO status = new StatusDTO();
		try {
			conn = PDFUtils.getConnection();
			stmt = conn.prepareStatement(UPDATE_USERNAME);
			stmt.setString(1, newUserName);
			stmt.setString(2, userDetails.getUserName());
			stmt.setInt(3,userDetails.getUserId());
			int records = stmt.executeUpdate();
			if (records>0) {
				status.setStatusCode(0);
			}else{
				status.setStatusCode(-1);
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
		
	//Update password
	public static boolean changePassword(UserDetails userDetails,String existingPwd,String newPassword) {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean isPwdChanged=false;
		try {
			conn = PDFUtils.getConnection();
			stmt = conn.prepareStatement(UPDATE_USER_PWD);
			stmt.setString(1, newPassword);
			stmt.setInt(2, userDetails.getUserId());
			stmt.setString(3,existingPwd);
			int records = stmt.executeUpdate();

			if (records>0) {
				isPwdChanged=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		return isPwdChanged;
	}
		
	//Update Personal Details
	public static boolean updatePersonalDetails(UserDetails userDetails) {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean isDetailsUpdated=false;
		try {
			conn = PDFUtils.getConnection();
			stmt = conn.prepareStatement(UPDATE_USER_DETAILS);
			stmt.setString(1, userDetails.getFirstName());
			stmt.setString(2, userDetails.getLastName());
			stmt.setString(3, userDetails.getEmail());
			stmt.setLong(4, userDetails.getMobileNo());
			stmt.setInt(5, userDetails.getUserId());
			
			int records = stmt.executeUpdate();

			if (records>0) {
				isDetailsUpdated=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		return isDetailsUpdated;
	}
		
	public static Customer getCustomerDetails(long custMobileNumber) {
		Connection conn = null;
		PreparedStatement stmt = null;
		Customer customer = null;
		try {
			conn = PDFUtils.getConnection();
			stmt = conn.prepareStatement(GET_CUSTOMER);
			stmt.setLong(1, custMobileNumber);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				customer = new Customer();
				customer.setCustMobileNumber(rs.getLong("CUST_MOB_NO"));
				customer.setCustName(rs.getString("CUST_NAME"));
				customer.setBalanceAmt(rs.getDouble("BALANCE_AMOUNT"));
				customer.setCustCity(rs.getString("CUST_CITY"));
				customer.setCustEmail(rs.getString("CUST_EMAIL"));
				customer.setEntryDate(rs.getTimestamp("ENTRY_DATE"));
				customer.setLastUpdateDate(rs.getTimestamp("LAST_UPDATE"));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		return customer;
	}

	public static StatusDTO addCustomer(Customer customer) {
		Connection conn = null;
		PreparedStatement stmt = null;
		StatusDTO status = new StatusDTO();
		try {
			conn = PDFUtils.getConnection();
			stmt = conn.prepareStatement(INS_CUSTOMER);
			stmt.setLong(1, customer.getCustMobileNumber());
			stmt.setString(2,customer.getCustName());
			stmt.setString(3,customer.getCustEmail());
			stmt.setString(4,customer.getCustCity());
			stmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
			stmt.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
			
			int records = stmt.executeUpdate();

			if (records>0) {
				status.setStatusCode(0);
			}
		} catch (Exception e) {
			status.setStatusCode(-1);
			status.setException(e.getMessage());
			e.printStackTrace();
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		return status;
	}
	//Add amount to customer balance
	public static StatusDTO addPendingPaymentToCustomer(long custMobileNo,double balance) {
		Connection conn = null;
		PreparedStatement stmt = null;
		StatusDTO status = new StatusDTO();
		try {
			conn = PDFUtils.getConnection();
			stmt = conn.prepareStatement(UPDATE_CUST_BALANCE);
			stmt.setDouble(1, balance);
			stmt.setLong(2,custMobileNo);
			
			int records = stmt.executeUpdate();

			if (records>0) {
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
	
	//Settle Up Customer Balance
	public static StatusDTO settleUpCustomerBalance(long custMobileNo,double amount) {
		Connection conn = null;
		PreparedStatement stmt = null;
		StatusDTO status = new StatusDTO();		
		try {
			conn = PDFUtils.getConnection();
			stmt = conn.prepareStatement(SETTLEUP_CUST_BALANCE);
			stmt.setDouble(1, amount);
			stmt.setLong(2,custMobileNo);
			
			int records = stmt.executeUpdate();

			if (records>0) {
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
	
	//Delete Customer
		public static boolean deleteCustomer(long custMobileNo) {
			Connection conn = null;
			PreparedStatement stmt = null;
			boolean isCustDeleted=false;
			try {
				conn = PDFUtils.getConnection();
				stmt = conn.prepareStatement(DELETE_CUSTOMER);
				stmt.setLong(1,custMobileNo);
				
				int records = stmt.executeUpdate();

				if (records>0) {
					isCustDeleted=true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				PDFUtils.closeConnectionAndStatment(conn, stmt);
			}
			return isCustDeleted;
		}
		//Update Customer Details
		public static boolean updateCustomer(Customer customer) {
			Connection conn = null;
			PreparedStatement stmt = null;
			boolean isCustomerAdded=false;
			try {
				conn = PDFUtils.getConnection();
				stmt = conn.prepareStatement(UPDATE_CUSTOMER);
				stmt.setString(1,customer.getCustName());
				stmt.setString(2,customer.getCustEmail());
				stmt.setString(3,customer.getCustCity());
				stmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
				stmt.setLong(5, customer.getCustMobileNumber());
				
				int records = stmt.executeUpdate();

				if (records>0) {
					isCustomerAdded=true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				PDFUtils.closeConnectionAndStatment(conn, stmt);
			}
			return isCustomerAdded;
		}
		
		public static List<Customer> searchCustomer(String searchString) {
			Connection conn = null;
			PreparedStatement stmt = null;
			Customer customer = null;
			List<Customer> customerList = new ArrayList<Customer>();
			try {
				conn = PDFUtils.getConnection();
				stmt = conn.prepareStatement(SEARCH_CUSTOMER);
				stmt.setString(1,"%"+searchString+"%");
				ResultSet rs = stmt.executeQuery();

				while (rs.next()) {
					customer = new Customer();
					customer.setCustMobileNumber(rs.getLong("CUST_MOB_NO"));
					customer.setCustName(rs.getString("CUST_NAME"));
					customer.setBalanceAmt(rs.getDouble("BALANCE_AMOUNT"));
					customer.setCustCity(rs.getString("CUST_CITY"));
					customer.setCustEmail(rs.getString("CUST_EMAIL"));
					customer.setEntryDate(rs.getTimestamp("ENTRY_DATE"));
					customer.setLastUpdateDate(rs.getTimestamp("LAST_UPDATE"));
					
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
		//Get All Customers
		public static List<Customer> getAllCustomers() {
			Connection conn = null;
			PreparedStatement stmt = null;
			Customer customer = null;
			List<Customer> customerList = new ArrayList<Customer>();
			try {
				conn = PDFUtils.getConnection();
				stmt = conn.prepareStatement(GET_ALL_CUSTOMERS);
				ResultSet rs = stmt.executeQuery();

				while (rs.next()) {
					customer = new Customer();
					customer.setCustMobileNumber(rs.getLong("CUST_MOB_NO"));
					customer.setCustName(rs.getString("CUST_NAME"));
					customer.setBalanceAmt(rs.getDouble("BALANCE_AMOUNT"));
					customer.setCustCity(rs.getString("CUST_CITY"));
					customer.setCustEmail(rs.getString("CUST_EMAIL"));
					customer.setEntryDate(rs.getTimestamp("ENTRY_DATE"));
					customer.setLastUpdateDate(rs.getTimestamp("LAST_UPDATE"));
					
					customerList.add(customer);
					Comparator<Customer> cp = Customer.getComparator(Customer.SortParameter.CUSTOMER_NAME_ASCENDING); 
					Collections.sort(customerList,cp);
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				PDFUtils.closeConnectionAndStatment(conn, stmt);
			}
			return customerList;
		}
		
		public static StatusDTO addCustomerPaymentHistory(Long customerMobile,double creditAmount,double debitAmount,String flag,String narration) {
			Connection conn = null;
			PreparedStatement stmt = null;
			StatusDTO status = new StatusDTO();
			try {
				conn = PDFUtils.getConnection();
				stmt = conn.prepareStatement(INS_CUSTOMER_PAY_HISTORY);
				stmt.setLong(1, customerMobile);
				stmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
				Customer customer = getCustomerDetails(customerMobile);
				if(AppConstants.CREDIT.equals(flag)){
					stmt.setDouble(3,customer.getBalanceAmt()+creditAmount);
				}
				if(AppConstants.DEBIT.equals(flag)){
					stmt.setDouble(3,customer.getBalanceAmt()-debitAmount);
				}
				stmt.setString(4,flag);
				stmt.setString(5,narration);
				stmt.setDouble(6,Math.abs(creditAmount));
				stmt.setDouble(7,Math.abs(debitAmount));
				
				int records = stmt.executeUpdate();

				if (records>0) {
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
}
