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
public class BillMaster implements Serializable {
    
    private String billSerialNumber;
    private String productId;
    private String quantityPurchased;
    private String amount;
    String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public BillMaster() {
    }

    public BillMaster(String billSerialNumber) {
        this.billSerialNumber = billSerialNumber;
    }

    public String getBillSerialNumber() {
        return billSerialNumber;
    }

    public void setBillSerialNumber(String billSerialNumber) {
        this.billSerialNumber = billSerialNumber;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuantityPurchased() {
        return quantityPurchased;
    }

    public void setQuantityPurchased(String quantityPurchased) {
        this.quantityPurchased = quantityPurchased;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billSerialNumber != null ? billSerialNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BillMaster)) {
            return false;
        }
        BillMaster other = (BillMaster) object;
        if ((this.billSerialNumber == null && other.billSerialNumber != null) || (this.billSerialNumber != null && !this.billSerialNumber.equals(other.billSerialNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.logicoy.openjbi.domain.BillMaster[ billSerialNumber=" + billSerialNumber + " ]";
    }
    
}
