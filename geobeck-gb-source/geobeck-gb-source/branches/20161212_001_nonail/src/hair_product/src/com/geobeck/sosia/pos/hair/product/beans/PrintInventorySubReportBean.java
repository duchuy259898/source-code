/*
 * PrintInventorySubReportBean.java
 *
 * Created on 2008/09/25, 12:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.beans;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
/**
 *
 * @author trino
 */
public class PrintInventorySubReportBean
{
    private JRBeanCollectionDataSource subCategory;
    private JRBeanCollectionDataSource subProduct;
    
    /** Creates a new instance of PrintInventorySubReportBean */
    public PrintInventorySubReportBean()
    {
    }
    
    public JRBeanCollectionDataSource getSubCategory(){
        return this.subCategory;
    }
    
    public void setSubCategory(JRBeanCollectionDataSource report){
        this.subCategory = report;
    }
    
    public JRBeanCollectionDataSource getSubProduct(){
        return this.subProduct;
    }
    
    public void setSubProduct(JRBeanCollectionDataSource report){
        this.subProduct = report;
    }
}
