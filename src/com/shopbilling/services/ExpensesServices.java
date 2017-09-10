package com.shopbilling.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComboBox;

import com.shopbilling.dto.Expense;
import com.shopbilling.dto.ExpenseType;
import com.shopbilling.dto.StatusDTO;
import com.shopbilling.utils.PDFUtils;

public class ExpensesServices {

	private static final String GET_EXPENSES_DETAILS = "SELECT * FROM EXPENSE_DETAILS WHERE DATE(DATE) BETWEEN ? AND ? ";
	
	private static final String GET_EXPENSE = "SELECT * FROM EXPENSE_DETAILS WHERE ID=?";
	
	private static final String ADD_EXPENSE = "INSERT INTO EXPENSE_DETAILS " 
												+ "(CATEGORY,DESCRIPTION,AMOUNT,DATE)" 
												+ " VALUES(?,?,?,?)";
	
	private static final String DELETE_EXPENSE = "DELETE FROM EXPENSE_DETAILS WHERE ID=?";
	
	private static final String UPDATE_EXPENSE = "UPDATE EXPENSE_DETAILS SET CATEGORY=?," 
												+"DESCRIPTION=?, AMOUNT=?, DATE=?"
												+" WHERE ID=?";
	
	private static final String GET_EXPENSE_TYPES = "SELECT * FROM APP_EXPENSE_TYPES";


	public static List<Expense> getExpenses(Date fromDate,Date toDate,String expenseCategory) {
		Connection conn = null;
		PreparedStatement stmt = null;
		Expense pc = null;
		List<Expense> expenseList = new ArrayList<Expense>();
		StringBuilder query1 = new StringBuilder(GET_EXPENSES_DETAILS);
		String ORDER_BY_CLAUSE = "ORDER BY DATE ASC";
		String CATEGORY = " AND CATEGORY = ? ";
		try {
			if(fromDate==null){
				fromDate = new Date(1947/01/01);
			}
			if(toDate==null){
				toDate = new Date(System.currentTimeMillis());
			}
			if(expenseCategory!=null){
				query1.append(CATEGORY);
			}
			query1.append(ORDER_BY_CLAUSE);
			
			conn = PDFUtils.getConnection();
			stmt = conn.prepareStatement(query1.toString());
			stmt.setDate(1,fromDate);
			stmt.setDate(2,toDate);
			if(expenseCategory!=null){
				stmt.setString(3, expenseCategory);
			}
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				pc = new Expense();
				pc.setId(rs.getInt("ID"));
				pc.setCategory(rs.getString("CATEGORY"));
				pc.setAmount(Double.parseDouble(rs.getString("AMOUNT")));
				pc.setDescription(rs.getString("DESCRIPTION"));
				pc.setDate(rs.getDate("DATE"));
				
				expenseList.add(pc);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		return expenseList;
	}
	
	public static StatusDTO addExpense(Expense expense) {
		Connection conn = null;
		PreparedStatement stmt = null;
		StatusDTO status = new StatusDTO();
		try {
			if(expense!=null){
				conn = PDFUtils.getConnection();
				stmt = conn.prepareStatement(ADD_EXPENSE);
				stmt.setString(1,expense.getCategory());
				stmt.setString(2,expense.getDescription());
				stmt.setDouble(3,expense.getAmount());
				stmt.setDate(4,expense.getDate());
				
				int i = stmt.executeUpdate();
				if(i>0){
					status.setStatusCode(0);
				}
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
	
	public static boolean deleteExpense(int expenseId) {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean flag=false;
		try {
				conn = PDFUtils.getConnection();
				stmt = conn.prepareStatement(DELETE_EXPENSE);
				stmt.setInt(1,expenseId);
				
				int i = stmt.executeUpdate();
				if(i>0){
					flag=true;
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		return flag;
	}
	
	public static StatusDTO updateExpense(Expense expense) {
		Connection conn = null;
		PreparedStatement stmt = null;
		StatusDTO status = new StatusDTO();
		try {
			if(expense!=null){
				conn = PDFUtils.getConnection();
				stmt = conn.prepareStatement(UPDATE_EXPENSE);
				stmt.setString(1,expense.getCategory());
				stmt.setString(2,expense.getDescription());
				stmt.setDouble(3, expense.getAmount());
				stmt.setDate(4, expense.getDate());
				stmt.setInt(5, expense.getId());
				
				int i = stmt.executeUpdate();
				if(i>0){
					status.setStatusCode(0);
				}
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
	
	public static Expense getExpenseDetails(Integer id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		Expense pc = null;
		try {
			conn = PDFUtils.getConnection();
			stmt = conn.prepareStatement(GET_EXPENSE);
			stmt.setInt(1,id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				pc = new Expense();
				pc.setId(rs.getInt("ID"));
				pc.setCategory(rs.getString("CATEGORY"));
				pc.setAmount(Double.parseDouble(rs.getString("AMOUNT")));
				pc.setDescription(rs.getString("DESCRIPTION"));
				pc.setDate(rs.getDate("DATE"));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		return pc;
	}
	
	// This method returns expense types configured
		public static List<ExpenseType> getExpenseTypes() {

			List<ExpenseType> dataList = new LinkedList<ExpenseType>();
			Connection conn = null;
			PreparedStatement stmt = null;
			try {
				conn = PDFUtils.getConnection();
				stmt = conn.prepareStatement(GET_EXPENSE_TYPES);
				ResultSet rs = stmt.executeQuery();

				while (rs.next()) {
					ExpenseType e = new ExpenseType();
					e.setId(rs.getInt("ID"));
					e.setName(rs.getString("NAME"));
					e.setType(rs.getString("TYPE"));
					e.setIsEnabled(rs.getString("IS_ENABLED"));
					dataList.add(e);
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				PDFUtils.closeConnectionAndStatment(conn, stmt);
			}
			return dataList;

		}

		public static void populateDropdown(JComboBox<String> combobox){
			for(ExpenseType s : getExpenseTypes()){
				combobox.addItem(s.getName());
			}
		}
}
