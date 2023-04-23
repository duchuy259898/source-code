/*
 * BusinessSubReportBean.java
 *
 * Created on 2006/05/22, 1:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.report.bean;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author k-anayama
 */
public class BusinessSubReportBean {
    
    private JRBeanCollectionDataSource subReportTechnic;
    private JRBeanCollectionDataSource subReportItem;
    
    /**
	 * Creates a new instance of BusinessSubReportBean
	 */
    public BusinessSubReportBean() {
    }

    public JRBeanCollectionDataSource getSubReportTechnic() {
	    return subReportTechnic;
    }

    public void setSubReportTechnic(JRBeanCollectionDataSource subReportTechnic) {
	    this.subReportTechnic = subReportTechnic;
    }

    public JRBeanCollectionDataSource getSubReportItem() {
	    return subReportItem;
    }

    public void setSubReportItem(JRBeanCollectionDataSource subReportItem) {
	    this.subReportItem = subReportItem;
    }
	
}
