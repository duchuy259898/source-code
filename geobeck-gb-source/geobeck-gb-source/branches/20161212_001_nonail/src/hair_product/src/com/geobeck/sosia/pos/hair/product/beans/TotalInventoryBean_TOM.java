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
public class TotalInventoryBean_TOM
{
    private JRBeanCollectionDataSource subReportSupplier;
    
    /** Creates a new instance of TotalInventoryBean */
    public TotalInventoryBean_TOM()
    {
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
