/*
 * PointCardLayoutBean.java
 *
 * Created on 2008/09/01, 17:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.pointcard;

import java.util.Date;

/**
 *
 * @author shiera.delusa
 */
public class PointCardLayoutBean
{
    private int shopId;
    private int templateId;
    private String templateTitle;
    private Date fromDate;
    private Date toDate;
    private String comment1;
    private String comment2;
    private String comment3;
    private String comment4;
    private String comment5;
    private String comment6;
    private String comment7;
    private String comment8;
    private String comment9;
    private int displaySeq;
    private Date insertDate;
    private Date updateDate;
    private Date deleteDate;
    
    /** Creates a new instance of PointCardLayoutBean */
    public PointCardLayoutBean()
    {
    }

    public int getTemplateId()
    {
        return templateId;
    }

    public void setTemplateId(int templateId)
    {
        this.templateId = templateId;
    }

    public String getTemplateTitle()
    {
        return templateTitle;
    }

    public void setTemplateTitle(String templateTitle)
    {
        this.templateTitle = templateTitle;
    }

    public Date getFromDate()
    {
        return fromDate;
    }

    public void setFromDate(Date fromDate)
    {
        this.fromDate = fromDate;
    }

    public Date getToDate()
    {
        return toDate;
    }

    public void setToDate(Date toDate)
    {
        this.toDate = toDate;
    }

    public String getComment1()
    {
        return comment1;
    }

    public void setComment1(String comment1)
    {
        this.comment1 = comment1;
    }

    public String getComment2()
    {
        return comment2;
    }

    public void setComment2(String comment2)
    {
        this.comment2 = comment2;
    }

    public String getComment3()
    {
        return comment3;
    }

    public void setComment3(String comment3)
    {
        this.comment3 = comment3;
    }

    public String getComment4()
    {
        return comment4;
    }

    public void setComment4(String comment4)
    {
        this.comment4 = comment4;
    }

    public String getComment5()
    {
        return comment5;
    }

    public void setComment5(String comment5)
    {
        this.comment5 = comment5;
    }

    public String getComment6()
    {
        return comment6;
    }

    public void setComment6(String comment6)
    {
        this.comment6 = comment6;
    }

    public String getComment7()
    {
        return comment7;
    }

    public void setComment7(String comment7)
    {
        this.comment7 = comment7;
    }

    public String getComment8()
    {
        return comment8;
    }

    public void setComment8(String comment8)
    {
        this.comment8 = comment8;
    }

    public String getComment9()
    {
        return comment9;
    }

    public void setComment9(String comment9)
    {
        this.comment9 = comment9;
    }

    public int getDisplaySeq()
    {
        return displaySeq;
    }

    public void setDisplaySeq(int displaySeq)
    {
        this.displaySeq = displaySeq;
    }
    
    public Date getInsertDate() 
    {
        return insertDate;
    }
    
    public void setInsertDate( Date newDate )
    {
        insertDate = newDate;
    }
    
    public Date getUpdateDate()
    {
        return updateDate;
    }
    
    public void setUpdateDate( Date newDate )
    {
        updateDate = newDate;
    }
    
    public Date getDeleteDate()
    {
        return deleteDate;
    }
    
    public void setDeleteDate( Date newDate )
    {
        deleteDate = newDate;
    }    

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }
}
