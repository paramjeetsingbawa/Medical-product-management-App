/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logicoy.openjbi.domain;

/**
 *
 * @author param
 */
public class CustomerBillingDetails {
    
    String custName;
    String cusMobile;
    String billNo;
    String billDate;
    
    String totalAmountPaid;

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCusMobile() {
        return cusMobile;
    }

    public void setCusMobile(String cusMobile) {
        this.cusMobile = cusMobile;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(String totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }
    
    
    
}
