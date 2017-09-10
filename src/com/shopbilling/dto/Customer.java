package com.shopbilling.dto;

import java.sql.Timestamp;
import java.util.Comparator;

public class Customer{

	private long custMobileNumber;
	private String custName;
	private double balanceAmt;
	private String custCity;
	private String custEmail;
	private double amount;
	private String historyFlag;
	private Timestamp entryDate;
	private Timestamp lastUpdateDate;
	private String narration;
	
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public long getCustMobileNumber() {
		return custMobileNumber;
	}
	public void setCustMobileNumber(long custMobileNumber) {
		this.custMobileNumber = custMobileNumber;
	}
	public double getBalanceAmt() {
		return balanceAmt;
	}
	public void setBalanceAmt(double balanceAmt) {
		this.balanceAmt = balanceAmt;
	}
	public String getCustCity() {
		return custCity;
	}
	public void setCustCity(String custCity) {
		this.custCity = custCity;
	}
	public String getCustEmail() {
		return custEmail;
	}
	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getHistoryFlag() {
		return historyFlag;
	}
	public void setHistoryFlag(String historyFlag) {
		this.historyFlag = historyFlag;
	}
	public Timestamp getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Timestamp entryDate) {
		this.entryDate = entryDate;
	}
	public Timestamp getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Timestamp lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	public static Comparator<Customer> getComparator(SortParameter... sortParameters) {
        return new CustomerComparator(sortParameters);
    }
	public String getNarration() {
		return narration;
	}
	public void setNarration(String narration) {
		this.narration = narration;
	}
	public enum SortParameter {
        CUSTOMER_NAME_ASCENDING,CUST_BALANCE_ASCENDING
    }
	private static class CustomerComparator implements Comparator<Customer> {
        private SortParameter[] parameters;

        private CustomerComparator(SortParameter[] parameters) {
            this.parameters = parameters;
        }

        public int compare(Customer o1, Customer o2) {
            int comparison;
            for (SortParameter parameter : parameters) {
                switch (parameter) {
                    case CUSTOMER_NAME_ASCENDING:
                    	comparison = o1.getCustName().compareTo(o2.getCustName());
                        if (comparison != 0) return comparison;
                        break;
                    case CUST_BALANCE_ASCENDING:
                    	if (o1.getBalanceAmt() < o2.getBalanceAmt()) {
                	        return 1;
                	    }
                	    else if(o1.getBalanceAmt() > o2.getBalanceAmt()){
                	        return -1;
                	    }
                        break;
                }
            }
            return 0;
        }
 }
	
}
