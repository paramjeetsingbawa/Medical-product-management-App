/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logicoy.openjbi.dao;

import com.logicoy.openjbi.connection.DbConnection;
import com.logicoy.openjbi.domain.BillMaster;
import com.logicoy.openjbi.domain.CustomerBillingDetails;
import com.logicoy.openjbi.domain.ProductMaster;
import com.logicoy.openjbi.rest.BpelManagementService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author logicoy
 */
public class MonitoringDao {

    private final static String findServiceUnit = "SELECT SUNAME, SUZIPARCHIVE FROM ServiceUnit";
    @Autowired
    private DbConnection dbCon;
    private JdbcTemplate jdbcTemplate = null;
    private DataSource datasource;

    public void setDataSource(DataSource datasource) {

        this.datasource = datasource;
        this.jdbcTemplate = new JdbcTemplate(datasource);

    }

    public String addProductDetail(String productName, String company, String formula, String batchNo, String expiryDate, String manfactureDate, String quantity, String amount) {

        String SQL = "INSERT INTO PRODUCT_MASTER(  PRODUCT_NAME  ,COMPANY  ,FORMULA  ,BATCH_NO  ,MANUFACTURE_DATE  ,EXPIRE_DATE  ,QUANTITY_IN_SHOP  ,AMOUNT_PER_UNIT)VALUES (?,?,?,?,?,?,?,?)";

        System.out.println("addProductDetail executing sql : " + SQL);
        try {
            int i = this.jdbcTemplate.update(SQL, new Object[]{productName, company, formula, batchNo, manfactureDate, expiryDate, quantity, amount});
            System.out.println("addProductDetail product detail inserted ...success");
            return "true";

        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    public String addCustomerBillDetail(String customerName, String mobile, String billNo, String billDate, String totalAmount) {

        String SQL = "INSERT INTO CUSTOMER_BILL_MASTER(CUSTOMER_NAME ,CUSTOMER_MOBILE_NUMBER,BILL_SERIAL_NUMBER,BILLING_DATE,TOTAL_AMOUNT_PAID) VALUES (?,?,?,?,?)";

        System.out.println("addProductDetail executing sql : " + SQL);
        try {
            int i = this.jdbcTemplate.update(SQL, new Object[]{customerName, mobile, billNo, billDate, totalAmount});
            System.out.println("addProductDetail product detail inserted ...success");
            return "true";

        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    public String addProductBillDetail(String billNo, String productName, String quantity, String totalAmount) {
        
        totalAmount = String.valueOf( Integer.parseInt(totalAmount.trim()) * Integer.parseInt(quantity.trim()));

        String SQL = "INSERT INTO BILL_MASTER(   BILL_SERIAL_NUMBER  ,PRODUCT_ID  ,QUANTITY_PURCHASED  ,AMOUNT) values(?,?,?,?)";

        System.out.println("addProductDetail executing sql : " + SQL);
        try {
            int i = this.jdbcTemplate.update(SQL, new Object[]{billNo, productName, quantity, totalAmount});
            System.out.println("addProductDetail product detail inserted ...success");
            return "true";

        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    public String updateProductQuantity(String productId, String quantity) {

        String getQuantity = "SELECT QUANTITY_IN_SHOP FROM PRODUCT_MASTER WHERE PRODUCT_ID=" + productId;

        String SQL = "UPDATE PRODUCT_MASTER SET QUANTITY_IN_SHOP = ? WHERE PRODUCT_ID=" + productId;

        System.out.println("updateProductQuantity executing sql : " + SQL);
        try {

            System.out.println("updateProductQuantity executing sql : " + getQuantity);
            List<String> quantityList = this.jdbcTemplate.query(getQuantity, new RowMapper() {
                public Object mapRow(ResultSet rs, int Rownum) throws SQLException {

                    String quantityOrg = rs.getString("QUANTITY_IN_SHOP");

                    return quantityOrg;
                }
            });

            for (String q : quantityList) {
                int quan = Integer.parseInt(q);
                quan = quan - Integer.parseInt(quantity.trim());
                if (quan < 0) {
                    throw new Exception("Bill can not be generated, As quantity in shop is less than ordered!!!");
                }

                System.out.println("updateProductQuantity executing sql : " + SQL);
                int i = this.jdbcTemplate.update(SQL, new Object[]{quan});
            }

            System.out.println("updateProductDetail product detail updated ...success");
            return "true";

        } catch (Exception e) {
            e.printStackTrace();
            return "false : " + e.toString();
        }

    }

    public List<ProductMaster> getProductDetail(String productId, String productName, String company,
            String formula, String batchNo, String sortColumn, String sortFilter) {

        StringBuffer sbSQL = new StringBuffer("SELECT PRODUCT_ID, PRODUCT_NAME, COMPANY, FORMULA, BATCH_NO, MANUFACTURE_DATE, EXPIRE_DATE, QUANTITY_IN_SHOP, AMOUNT_PER_UNIT, CREATED_AT, UPDATED_AT FROM PRODUCT_MASTER ");

        boolean whereExecuted = false;
        if ((productId != null && productId.trim().length() > 0)
                || (productName != null && productName.trim().length() > 0)
                || (company != null && company.trim().length() > 0)
                || (formula != null && formula.trim().length() > 0)
                || (batchNo != null && batchNo.trim().length() > 0)) {
            sbSQL.append("WHERE ");
        }

        if (productId != null && productId.trim().length() > 0) {
            sbSQL.append("PRODUCT_ID LIKE '%").append(productId).append("%' ");
            whereExecuted = true;
        }
        if (productName != null && productName.trim().length() > 0) {
            if (whereExecuted) {
                sbSQL.append(" AND ");
            }
            sbSQL.append("PRODUCT_NAME LIKE '%").append(productName).append("%' ");
            whereExecuted = true;
        }
        if (company != null && company.trim().length() > 0) {
            if (whereExecuted) {
                sbSQL.append(" AND ");
            }
            sbSQL.append("COMPANY LIKE '%").append(company).append("%' ");
            whereExecuted = true;
        }
        if (formula != null && formula.trim().length() > 0) {
            if (whereExecuted) {
                sbSQL.append(" AND ");
            }
            sbSQL.append("FORMULA LIKE '%").append(formula).append("%' ");
            whereExecuted = true;
        }
        if (batchNo != null && batchNo.trim().length() > 0) {
            if (whereExecuted) {
                sbSQL.append(" AND ");
            }
            sbSQL.append("BATCH_NO LIKE '%").append(batchNo).append("%' ");
            whereExecuted = true;
        }


        if (sortColumn != null && sortColumn.trim().length() > 0) {
            if (sortFilter != null) {
                sbSQL.append(" ORDER BY ").append(sortColumn).append(" ").append(sortFilter);
            } else {
                sbSQL.append(" ORDER BY ").append(sortColumn);
            }
        } else {
            sbSQL.append(" ORDER BY PRODUCT_NAME ASC ");
        }

        System.out.println("getProductDetail executing sql : " + sbSQL);
        ResultSet rs = null;
        SqlRowSet srs = null;
        Connection con = null;
        try {
            con = this.datasource.getConnection();
            Statement st = con.createStatement();
            rs = st.executeQuery(sbSQL.toString());

            //rs = this.jdbcTemplate.queryForRowSet(sbSQL.toString());
            List<ProductMaster> productList = new ArrayList<ProductMaster>();
            int maxLimit = 10000;
            int i = 0;
            while (rs.next()) {

                ProductMaster product = new ProductMaster();
                product.setAmountPerUnit(rs.getString("AMOUNT_PER_UNIT"));
                product.setBatchNo(rs.getString("BATCH_NO"));
                product.setFormula(rs.getString("FORMULA"));
                product.setProductId(rs.getInt("PRODUCT_ID"));
                product.setProductName(rs.getString("PRODUCT_NAME"));
                product.setCompany(rs.getString("COMPANY"));
                product.setManufactureDate(rs.getString("MANUFACTURE_DATE"));
                product.setExpireDate(rs.getString("EXPIRE_DATE"));
                product.setQuantityInShop(rs.getString("QUANTITY_IN_SHOP"));
                product.setCreatedAt(rs.getString("CREATED_AT"));
                product.setUpdatedAt(rs.getString("UPDATED_AT"));

                productList.add(product);
                i++;
                if (i > maxLimit) {
                    break;
                }
            }

            return productList;

        } catch (Exception ex) {
            Logger.getLogger(BpelManagementService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                Logger.getLogger(BpelManagementService.class.getName()).log(Level.SEVERE, null, "Returning Instance summary List by dao");
                if (con != null) {
                    con.close();
                }

                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(MonitoringDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;

    }

    public List<CustomerBillingDetails> getCustomerBillingDetail(Integer maxrecord, String custName, String custMobile) {
        StringBuffer sbSQL = new StringBuffer("SELECT CUSTOMER_NAME, CUSTOMER_MOBILE_NUMBER, BILL_SERIAL_NUMBER, BILLING_DATE, TOTAL_AMOUNT_PAID FROM CUSTOMER_BILL_MASTER ");


        boolean whereExecuted = false;
        if ((custName != null && custName.trim().length() > 0)
                || (custMobile != null && custMobile.trim().length() > 0)) {
            sbSQL.append("WHERE ");
        }
        
            

        if (custName != null && custName.trim().length() > 0) {
            sbSQL.append("CUSTOMER_NAME LIKE '%").append(custName).append("%' ");
            whereExecuted = true;
        }
        if (custMobile != null && custMobile.trim().length() > 0) {
            if (whereExecuted) {
                sbSQL.append(" AND ");
            }
            sbSQL.append("CUSTOMER_MOBILE_NUMBER LIKE '%").append(custMobile).append("%' ");
            whereExecuted = true;
        }
        sbSQL.append("ORDER BY CUSTOMER_NAME ASC ");

        System.out.println("getCustomerBillingDetail executing sql : " + sbSQL);
        ResultSet rs = null;
        SqlRowSet srs = null;
        Connection con = null;
        try {
            con = this.datasource.getConnection();
            Statement st = con.createStatement();
            rs = st.executeQuery(sbSQL.toString());

            //rs = this.jdbcTemplate.queryForRowSet(sbSQL.toString());
            List<CustomerBillingDetails> productList = new ArrayList<CustomerBillingDetails>();
            int maxLimit = 10000;
            int i = 0;
            while (rs.next()) {

                CustomerBillingDetails custBill = new CustomerBillingDetails();
                
                custBill.setBillDate(rs.getString("BILLING_DATE"));
                custBill.setBillNo(rs.getString("BILL_SERIAL_NUMBER"));
                custBill.setCusMobile(rs.getString("CUSTOMER_MOBILE_NUMBER"));
                custBill.setCustName(rs.getString("CUSTOMER_NAME"));
                custBill.setTotalAmountPaid(rs.getString("TOTAL_AMOUNT_PAID"));
                

                productList.add(custBill);
                i++;
                if (i > maxLimit) {
                    break;
                }
            }

            return productList;

        } catch (Exception ex) {
            Logger.getLogger(BpelManagementService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                Logger.getLogger(BpelManagementService.class.getName()).log(Level.SEVERE, null, "Returning Instance summary List by dao");
                if (con != null) {
                    con.close();
                }

                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(MonitoringDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public List<BillMaster> getBillingDetailWithProducts(Integer maxrecord, String billNo) {
        StringBuffer sbSQL = new StringBuffer("SELECT BILL_SERIAL_NUMBER, a.PRODUCT_ID as PRODUCT_ID, b.PRODUCT_NAME as PRODUCT_NAME, QUANTITY_PURCHASED, AMOUNT FROM param.bill_master a, product_master b where a.PRODUCT_ID = b.PRODUCT_ID and a.bill_serial_number='" + billNo + "'");

        System.out.println("getBillingDetailWithProducts executing sql : " + sbSQL);
        ResultSet rs = null;
        SqlRowSet srs = null;
        Connection con = null;
        try {
            con = this.datasource.getConnection();
            Statement st = con.createStatement();
            rs = st.executeQuery(sbSQL.toString());

            //rs = this.jdbcTemplate.queryForRowSet(sbSQL.toString());
            List<BillMaster> productList = new ArrayList<BillMaster>();
            int maxLimit = 10000;
            int i = 0;
            while (rs.next()) {

                BillMaster custBill = new BillMaster();
                
                custBill.setAmount(rs.getString("AMOUNT"));
                custBill.setBillSerialNumber(rs.getString("BILL_SERIAL_NUMBER"));
                custBill.setProductId(rs.getString("PRODUCT_ID"));
                custBill.setProductName(rs.getString("PRODUCT_NAME"));;
                custBill.setQuantityPurchased(rs.getString("QUANTITY_PURCHASED"));
                
                productList.add(custBill);
                i++;
                if (i > maxLimit) {
                    break;
                }
            }

            return productList;

        } catch (Exception ex) {
            Logger.getLogger(BpelManagementService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                Logger.getLogger(BpelManagementService.class.getName()).log(Level.SEVERE, null, "Returning Instance summary List by dao");
                if (con != null) {
                    con.close();
                }

                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(MonitoringDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public String addProductsFromFile(final List<ProductMaster> productList) {
        
        String query = "INSERT INTO PRODUCT_MASTER(PRODUCT_NAME,COMPANY,FORMULA,BATCH_NO,MANUFACTURE_DATE,EXPIRE_DATE,QUANTITY_IN_SHOP,AMOUNT_PER_UNIT,CREATED_AT) VALUES (?,?,?,?,?,?,?,?,?)";
        
        try {
            System.out.println("Executing query : " + query);
            this.jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ProductMaster data = productList.get(i);
                    ps.setString(1, data.getProductName());
                    ps.setString(2, data.getCompany());
                    ps.setString(3, data.getFormula());
                    ps.setString(4, data.getBatchNo());
                    ps.setString(5, data.getManufactureDate());
                    ps.setString(6, data.getExpireDate());
                    ps.setString(7, data.getQuantityInShop());
                    ps.setString(8, data.getAmountPerUnit());   
                    ps.setString(9,new Date().toString());

                }

                @Override
                public int getBatchSize() {
                    return productList.size();
                }
                
            });

            return "true";
        } catch (Exception e) {
            System.out.println("Batch insert to batchUpdateCertifyInvoiceData failed!!! " + e.toString());
            return e.toString();
        }
    }
}
