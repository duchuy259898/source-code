/*
 * MaterialRateSubBean_TOM.java
 *
 * Created on 2008/10/18, 11:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product.beans;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author takeda
 */
public class MaterialRateAvgSubBean_TOM {

    private JRBeanCollectionDataSource subReport;
    private JRBeanCollectionDataSource subRankReport;

    public JRBeanCollectionDataSource getSubReport() {
        return subReport;
    }

    public void setSubReport(JRBeanCollectionDataSource subReport) {
        this.subReport = subReport;
    }

    public JRBeanCollectionDataSource getSubRankReport() {
        return subRankReport;
    }

    public void setSubRankReport(JRBeanCollectionDataSource subRankReport) {
        this.subRankReport = subRankReport;
    }

    
}
