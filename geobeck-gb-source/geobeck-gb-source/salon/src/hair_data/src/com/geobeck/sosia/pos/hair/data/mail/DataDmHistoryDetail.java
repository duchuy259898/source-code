/*
 * DataDmHistoryDetail.java
 *
 * Created on 2010/01/25, 14:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.mail;

import java.util.*;
import com.geobeck.sosia.pos.master.customer.MstCustomer;

/**
 * DMçÏê¨óöóñæç◊ÉfÅ[É^
 * @author geobeck
 */
public class DataDmHistoryDetail extends MstCustomer
{
    private Integer     shopID      = null;
    private Integer     dmType      = null;
    private Date        makeDate    = null;
    private String      mailAddress = null;
    private String      mailTitle   = null;
    private String      mailBody    = null;
    private String title = null;
    
    public Integer getShopID() {
        return shopID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

    public Integer getDmType() {
        return dmType;
    }

    public void setDmType(Integer dmType) {
        this.dmType = dmType;
    }

    public Date getMakeDate() {
        return makeDate;
    }

    public void setMakeDate(Date makeDate) {
        this.makeDate = makeDate;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }
    
    public String getMailTitle() {
        return mailTitle;
    }

    public void setMailTitle(String mailTitle) {
        this.mailTitle = mailTitle;
    }

    public String getMailBody() {
        return mailBody;
    }

    public void setMailBody(String mailBody) {
        this.mailBody = mailBody;
    }
}
