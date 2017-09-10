package com.shopbilling.dto;

import java.util.Comparator;

public class ProductCategory {

	private int categoryCode;
	
	private String categoryName;
	
	private String categoryDescription;
	
	private double comission;
	
	private int categoryStockQty;
	
	private double categoryStockAmount;

	public int getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(int categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public double getComission() {
		return comission;
	}

	public void setComission(double comission) {
		this.comission = comission;
	}
	public enum SortParameter {
        CATEGORY_NAME_ASCENDING,STOCK_VALUE_AMT_ASC,STOCK_QUANTITY_ASC
    }
 public static Comparator<ProductCategory> getComparator(SortParameter... sortParameters) {
	        return new ProductCategoryComparator(sortParameters);
    }
	 
 public int getCategoryStockQty() {
	return categoryStockQty;
}

public void setCategoryStockQty(int categoryStockQty) {
	this.categoryStockQty = categoryStockQty;
}

public double getCategoryStockAmount() {
	return categoryStockAmount;
}

public void setCategoryStockAmount(double categoryStockAmount) {
	this.categoryStockAmount = categoryStockAmount;
}

private static class ProductCategoryComparator implements Comparator<ProductCategory> {
        private SortParameter[] parameters;

        private ProductCategoryComparator(SortParameter[] parameters) {
            this.parameters = parameters;
        }

        public int compare(ProductCategory o1, ProductCategory o2) {
            int comparison;
            for (SortParameter parameter : parameters) {
                switch (parameter) {
                    case CATEGORY_NAME_ASCENDING:
                    	comparison = o1.getCategoryName().compareTo(o2.getCategoryName());
                        if (comparison != 0) return comparison;
                        break;
                    case STOCK_VALUE_AMT_ASC:
                    	if (o1.getCategoryStockAmount() < o2.getCategoryStockAmount()) {
                	        return 1;
                	    }
                	    else if(o1.getCategoryStockAmount() > o2.getCategoryStockAmount()){
                	        return -1;
                	    }
                        break;
                    case STOCK_QUANTITY_ASC:
                    	if (o1.getCategoryStockQty() < o2.getCategoryStockQty()) {
                	        return 1;
                	    }
                	    else if(o1.getCategoryStockQty() > o2.getCategoryStockQty()){
                	        return -1;
                	    }
                        break;
                }
            }
            return 0;
        }
 }

		
}
