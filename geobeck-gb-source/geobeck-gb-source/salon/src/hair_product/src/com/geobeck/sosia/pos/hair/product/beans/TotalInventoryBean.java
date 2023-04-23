/*
 * TotalInventoryBean.java
 *
 * Created on 2008/09/25, 17:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.beans;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author trino
 */
public class TotalInventoryBean
{
    private JRBeanCollectionDataSource subReportCategory;
    private JRBeanCollectionDataSource subReportSupplier;
    
    /** Creates a new instance of TotalInventoryBean */
    public TotalInventoryBean()
    {
    }

    public JRBeanCollectionDataSource getSubReportCategory()
    {
        return subReportCategory;
    }

    public void setSubReportCategory(JRBeanCollectionDataSource subReportCategory)
    {
        this.subReportCategory = subReportCategory;
    }

    public JRBeanCollectionDataSource getSubReportSupplier()
    {
        return subReportSupplier;
    }

    public void setSubReportSupplier(JRBeanCollectionDataSource subReportSupplier)
    {
        this.subReportSupplier = subReportSupplier;
    }
    
}
