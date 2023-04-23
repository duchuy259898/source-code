/*
 * SalesRankingSubReportBean.java
 *
 * Created on 2008/09/19, 16:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;


// include files for JRBeanCollection
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
/**
 *
 * @author gloridel
 */
public class SalesRankingSubReportBean
{
    private JRBeanCollectionDataSource subStaffRank;
    private JRBeanCollectionDataSource subStoreRank;
    
    /** Creates a new instance of SalesRankingSubReportBean */
    public SalesRankingSubReportBean()
    {
    }
    
    public JRBeanCollectionDataSource getSubStaffRank(){
        return this.subStaffRank;
    }
    
    public JRBeanCollectionDataSource getSubStoreRank(){
        return this.subStoreRank;
    }
    
    public void setSubStaffRank(JRBeanCollectionDataSource rank){
        this.subStaffRank = rank;
    }
    
    public void setSubStoreRank(JRBeanCollectionDataSource rank){
        this.subStoreRank = rank;
    }
}
