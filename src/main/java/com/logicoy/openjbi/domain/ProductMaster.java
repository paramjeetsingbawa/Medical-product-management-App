/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logicoy.openjbi.domain;

import java.io.Serializable;
/**
 *
 * @author param
 */
public class ProductMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer productId;
    private String productName;
    private String company;
    private String formula;
    private String batchNo;
    private String manufactureDate;
    private String expireDate;
    private String quantityInShop;
    private String amountPerUnit;
    private String createdAt;
    private String updatedAt;

    public ProductMaster() {
    }

    public ProductMaster(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getQuantityInShop() {
        return quantityInShop;
    }

    public void setQuantityInShop(String quantityInShop) {
        this.quantityInShop = quantityInShop;
    }

    public String getAmountPerUnit() {
        return amountPerUnit;
    }

    public void setAmountPerUnit(String amountPerUnit) {
        this.amountPerUnit = amountPerUnit;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productId != null ? productId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductMaster)) {
            return false;
        }
        ProductMaster other = (ProductMaster) object;
        if ((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.logicoy.openjbi.domain.ProductMaster[ productName=" + productName + " ]";
    }
    
}
