/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logicoy.openjbi.rest;

/**
 *
 * @author Param
 */
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import com.logicoy.openjbi.connection.DbConnection;
import com.logicoy.openjbi.dao.MonitoringDao;
import com.logicoy.openjbi.domain.BillMaster;
import com.logicoy.openjbi.domain.CustomerBillingDetails;
import com.logicoy.openjbi.domain.ProductMaster;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/BpelManagementService")
public class BpelManagementService {

    private static Logger logger = Logger.getLogger(BpelManagementService.class.getCanonicalName());
    private static DbConnection dbconnection;
    private static MonitoringDao monitordao;

    static {
        try {
            dbconnection = new DbConnection();
            monitordao = new MonitoringDao();
            monitordao.setDataSource(dbconnection.getDataSource("jdbc/bpelseNonXA"));
            //ApplicationContext ctx = new ClassPathXmlApplicationContext("META-INF/application-context.xml");
        } catch (Exception ex) {
            Logger.getLogger(BpelManagementService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @GET
    @Path("/getProductDetail")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ProductMaster> getProductDetail(
            @QueryParam("productId") String productId,
            @QueryParam("productName") String productName,
            @QueryParam("company") String company, @QueryParam("formula") String formula,
            @QueryParam("batchNo") String batchNo, @QueryParam("sortColumn") String sortColumn,
            @QueryParam("sortFilter") String sortFilter) {

        List<ProductMaster> productList = monitordao.getProductDetail(productId, productName, company, formula, batchNo, sortColumn, sortFilter);
        return productList;
    }

    @GET
    @Path("/getCustomerBillingDetail")
    @Produces({MediaType.APPLICATION_JSON})
    public List<CustomerBillingDetails> getCustomerBillingDetail(
            @QueryParam("maxrecord") Integer maxrecord, @QueryParam("custNameFilter") String custName,
            @QueryParam("custMobileFilter") String custMobile) {

        List<CustomerBillingDetails> custBillList = monitordao.getCustomerBillingDetail(maxrecord, custName, custMobile);
        return custBillList;
    }

    @GET
    @Path("/getBillingDetailWithProducts")
    @Produces({MediaType.APPLICATION_JSON})
    public List<BillMaster> getBillingDetailWithProducts(
            @QueryParam("maxrecord") Integer maxrecord, @QueryParam("billNo") String billNo) {

        List<BillMaster> custBillList = monitordao.getBillingDetailWithProducts(maxrecord, billNo);
        return custBillList;
    }

    @GET
    @Path("/addProductDetail")
    @Produces({MediaType.TEXT_PLAIN})
    public String addProductDetail(
            @QueryParam("productName") String productName,
            @QueryParam("company") String company, @QueryParam("formula") String formula,
            @QueryParam("batchNo") String batchNo, @QueryParam("manfactureDate") String manfactureDate,
            @QueryParam("expiryDate") String expiryDate, @QueryParam("quantity") String quantity,
            @QueryParam("amount") String amount) {

        String productList = monitordao.addProductDetail(productName, company, formula, batchNo,
                expiryDate, manfactureDate, quantity, amount);
        return productList;
    }

    @GET
    @Path("/addBillingDetail")
    @Produces({MediaType.TEXT_PLAIN})
    public String addBillingDetail(
            @QueryParam("customerName") String customerName,
            @QueryParam("customerMobileNumber") String customerMobileNumber, @QueryParam("billNo") String billNo,
            @QueryParam("billDate") String billDate, @QueryParam("totalAmount") String totalAmount,
            @QueryParam("productIds") String productIds, @QueryParam("quantityPurchased") String quantityPurchased,
            @QueryParam("amount") String amount) {

        String productList = monitordao.addCustomerBillDetail(customerName, customerMobileNumber, billNo, billDate, totalAmount);

        String[] productOrdered = productIds.split(",");

        String[] quantityOrdered = quantityPurchased.split(",");

        String[] amountOrdered = amount.split(",");

        for (int i = 0; i < productOrdered.length; i++) {

            String returnString = monitordao.addProductBillDetail(billNo, productOrdered[i], quantityOrdered[i], amountOrdered[i]);

            returnString = monitordao.updateProductQuantity(productOrdered[i], quantityOrdered[i]);

            if (!returnString.equalsIgnoreCase("true")) {
                return returnString;
            }

        }


        return "true";
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream) {

        String result = null;
        result = writeToFile(uploadedInputStream);
        

        if (result.equalsIgnoreCase("true")) {
            return Response.status(200).entity("<font color='Green' style='font-weight:700' size='5'>Congratulation!!! <br /><br />Product list added in Avon medical master database successfully...<br /> <br />Restart your application to see your new product list.. <br /> <hr /> Regards, <br />Param").build();
        } else {
            return Response.status(200).entity("<font color='Red' style='font-weight:700' size='5'>ERROR : Product list can not be added in Avon medical master database, ERROR MESSAGE : " + result + " <br /> Check your file once again for format issue otherwise, contact Param [9986881500] <br /> <hr /> Thanks, <br />Param").build();
        }

    }
    
    //@Context org.jboss.resteasy.spi.HttpResponse response;
    @Path("/json")
    @Produces("application/json")
    @GET
    public Response getJSONData() {
 
        Map<String, String> data1 = new HashMap<String,String>();
        data1.put( "key", "Computers");
        data1.put( "value","114");
 
        Map<String, Object> data2 = new HashMap<String, Object>();
        data2.put( "key", "Electronics");
        data2.put( "value","214");
 
        Map<String, Object> data3 = new HashMap<String, Object>();
        data3.put( "key", "Mechanical");
        data3.put( "value","514");
 
        JSONObject json1 = new JSONObject(data1);
        JSONObject json2 = new JSONObject(data2);
        JSONObject json3 = new JSONObject(data3);
 
        JSONArray array = new JSONArray();
        array.put(json1);
        array.put(json2);
        array.put(json3);
 
        JSONObject finalObject = new JSONObject();
        finalObject.put("student_data", array);
 
        //response.getOutputHeaders().putSingle("Access-Control-Allow-Origin", "*");
        return Response.status(200).entity(finalObject.toString()).build();
    }

// save uploaded file to new location
    private String writeToFile(InputStream uploadedInputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(uploadedInputStream));
        String str = null;
        List<ProductMaster> productList = new ArrayList<ProductMaster>();
        boolean skipHeader = false;
        try {
            while ((str = reader.readLine()) != null) {
                
                if( skipHeader == false){
                    skipHeader = true;
                    System.out.println(str + "..header skipped");
                    continue;
                }
                
                System.out.println(str);
                String[] cols = str.split(",");
                ProductMaster prod = new ProductMaster();
                //for(int i=0;i<cols.length;i++){
                try {
                    prod.setProductName(cols[0]);
                } catch (Exception e) {
                    System.out.println("Product name can not be set as, File is not having it..skipped");
                }

                try {
                    prod.setCompany(cols[1]);
                } catch (Exception e) {
                    System.out.println("Company name can not be set as, File is not having it..skipped");
                }


                try {
                    prod.setFormula(cols[2]);
                } catch (Exception e) {
                    System.out.println("Formula name can not be set as, File is not having it..skipped");
                }


                try {
                    prod.setBatchNo(cols[3]);
                } catch (Exception e) {
                    System.out.println("Batch No can not be set as, File is not having it..skipped");
                }


                try {
                    prod.setManufactureDate(cols[4]);
                } catch (Exception e) {
                    System.out.println("ManufactureDate can not be set as, File is not having it..skipped");
                }


                try {
                    prod.setExpireDate(cols[5]);
                } catch (Exception e) {
                    System.out.println("setExpireDate can not be set as, File is not having it..skipped");
                }


                try {
                    prod.setQuantityInShop(cols[6]);
                } catch (Exception e) {
                    System.out.println("setQuantityInShop can not be set as, File is not having it..skipped");
                }


                try {
                    prod.setAmountPerUnit(cols[7]);
                } catch (Exception e) {
                    System.out.println("setAmountPerUnit can not be set as, File is not having it..skipped");
                }

                //}

                productList.add(prod);


            }
        } catch (Exception e) {
            if (productList.size() > 0) {
            }
        }

        String result = this.monitordao.addProductsFromFile(productList);

        return result;

    }
}
