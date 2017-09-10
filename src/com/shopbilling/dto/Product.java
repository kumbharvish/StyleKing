package com.shopbilling.dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Comparator;

public class Product {

	private int productCode;
	
	private Long productBarCode;
	
	private String productName;
	
	private String measure;
	
	private int quanity;
	
	private double purcaseRate;

	private double productTax;
	
	private double purcasePrice;
	
	private double sellPrice;
	
	private double productMRP;
	
	private double discount;
	
	private Date entryDate;
	
	private Date lastUpdateDate;
	
	private String Description;

	private String enterBy;
	
	private String productCategory;
	
	private int sellQuantity;
	
	private double stockValueAmount;
	
	private double profit;
	
	private double stockPurchaseAmount;
	
	private int categoryCode;
	
	private String supplierName;
	
	private int supplierId;
	
	private Timestamp timeStamp;

	public int getProductCode() {
		return productCode;
	}

	public void setProductCode(int productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getMeasure() {
		return measure;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}

	public int getQuanity() {
		return quanity;
	}

	public void setQuanity(int quanity) {
		this.quanity = quanity;
	}

	public double getPurcasePrice() {
		return purcasePrice;
	}

	public void setPurcasePrice(double purcasePrice) {
		this.purcasePrice = purcasePrice;
	}

	public double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public double getProductMRP() {
		return productMRP;
	}

	public void setProductMRP(double productMRP) {
		this.productMRP = productMRP;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getEnterBy() {
		return enterBy;
	}

	public void setEnterBy(String enterBy) {
		this.enterBy = enterBy;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public int getSellQuantity() {
		return sellQuantity;
	}

	public void setSellQuantity(int sellQuantity) {
		this.sellQuantity = sellQuantity;
	}
	
	public double getItemPurchasePrice(){
		
		return sellQuantity*purcasePrice;
	}

	public double getPurcaseRate() {
		return purcaseRate;
	}

	public void setPurcaseRate(double purcaseRate) {
		this.purcaseRate = purcaseRate;
	}

	public double getProductTax() {
		return productTax;
	}

	public void setProductTax(double productTax) {
		this.productTax = productTax;
	}

	public double getProfit() {
		return productMRP - purcasePrice;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	 public static Comparator<Product> getComparator(SortParameter... sortParameters) {
	        return new ProductComparator(sortParameters);
	    }

	 public double getStockValueAmount() {
		return productMRP * quanity;
	}

	public void setStockValueAmount(double stockValueAmount) {
		this.stockValueAmount = stockValueAmount;
	}

	public Long getProductBarCode() {
		return productBarCode;
	}

	public void setProductBarCode(Long productBarCode) {
		this.productBarCode = productBarCode;
	}

	public double getStockPurchaseAmount() {
		return purcasePrice * quanity;
	}

	public void setStockPurchaseAmount(double stockPurchaseAmount) {
		this.stockPurchaseAmount = stockPurchaseAmount;
	}

	public int getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(int categoryCode) {
		this.categoryCode = categoryCode;
	}

	public enum SortParameter {
	        CATEGORY_NAME_ASCENDING,PROFIT_ASCENDING,PRODUCT_NAME_ASCENDING,STOCK_VALUE_AMT_ASC,STOCK_QUANTITY_ASC
	    }

	 private static class ProductComparator implements Comparator<Product> {
	        private SortParameter[] parameters;

	        private ProductComparator(SortParameter[] parameters) {
	            this.parameters = parameters;
	        }

	        public int compare(Product o1, Product o2) {
	            int comparison;
	            for (SortParameter parameter : parameters) {
	                switch (parameter) {
	                    case CATEGORY_NAME_ASCENDING:
	                    	comparison = o1.getProductCategory().compareTo(o2.getProductCategory());
	                        if (comparison != 0) return comparison;
	                        break;
	                    case PROFIT_ASCENDING:
	                    	if (o1.getProfit() < o2.getProfit()) {
	                	        return 1;
	                	    }
	                	    else if(o1.getProfit() > o2.getProfit()){
	                	        return -1;
	                	    }
	                        break;
	                    case PRODUCT_NAME_ASCENDING:
	                    	comparison = o1.getProductName().compareTo(o2.getProductName());
	                        if (comparison != 0) return comparison;
	                        break;
	                    case STOCK_VALUE_AMT_ASC:
	                    	if (o1.getStockValueAmount() < o2.getStockValueAmount()) {
	                	        return 1;
	                	    }
	                	    else if(o1.getStockValueAmount() > o2.getStockValueAmount()){
	                	        return -1;
	                	    }
	                        break;
	                    case STOCK_QUANTITY_ASC:
	                    	if (o1.getQuanity() < o2.getQuanity()) {
	                	        return 1;
	                	    }
	                	    else if(o1.getQuanity() > o2.getQuanity()){
	                	        return -1;
	                	    }
	                        break;
	                }
	            }
	            return 0;
	        }
	 }

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return "Product [productCode=" + productCode + ", productBarCode="
				+ productBarCode + ", productName=" + productName
				+ ", measure=" + measure + ", quanity=" + quanity
				+ ", purcaseRate=" + purcaseRate + ", productTax=" + productTax
				+ ", purcasePrice=" + purcasePrice + ", sellPrice=" + sellPrice
				+ ", productMRP=" + productMRP + ", discount=" + discount
				+ ", entryDate=" + entryDate + ", lastUpdateDate="
				+ lastUpdateDate + ", Description=" + Description
				+ ", enterBy=" + enterBy + ", productCategory="
				+ productCategory + ", sellQuantity=" + sellQuantity
				+ ", stockValueAmount=" + stockValueAmount + ", profit="
				+ profit + ", stockPurchaseAmount=" + stockPurchaseAmount
				+ ", categoryCode=" + categoryCode + ", supplierName="
				+ supplierName + ", supplierId=" + supplierId + ", timeStamp="
				+ timeStamp + "]";
	}
	 
}
