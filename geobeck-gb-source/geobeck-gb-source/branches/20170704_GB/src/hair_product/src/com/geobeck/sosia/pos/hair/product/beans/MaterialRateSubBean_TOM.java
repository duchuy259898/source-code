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
public class MaterialRateSubBean_TOM {

    private JRBeanCollectionDataSource[] subReport = new JRBeanCollectionDataSource[12];
    
    public void setSubReport(int index, JRBeanCollectionDataSource subReport){
        this.subReport[index] = subReport;
    }
    
    public JRBeanCollectionDataSource getSubReport1()
    {
        return subReport[0];
    }
    public JRBeanCollectionDataSource getSubReport2()
    {
        return subReport[1];
    }
    public JRBeanCollectionDataSource getSubReport3()
    {
        return subReport[2];
    }
    public JRBeanCollectionDataSource getSubReport4()
    {
        return subReport[3];
    }
    public JRBeanCollectionDataSource getSubReport5()
    {
        return subReport[4];
    }
    public JRBeanCollectionDataSource getSubReport6()
    {
        return subReport[5];
    }
    public JRBeanCollectionDataSource getSubReport7()
    {
        return subReport[6];
    }
    public JRBeanCollectionDataSource getSubReport8()
    {
        return subReport[7];
    }
    public JRBeanCollectionDataSource getSubReport9()
    {
        return subReport[8];
    }
    public JRBeanCollectionDataSource getSubReport10()
    {
        return subReport[9];
    }
    public JRBeanCollectionDataSource getSubReport11()
    {
        return subReport[10];
    }
    public JRBeanCollectionDataSource getSubReport12()
    {
        return subReport[11];
    }

    
}
