/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.message;

import com.geobeck.sql.ResultSetWrapper;
import java.sql.SQLException;
import java.util.Calendar;

/**
 *
 * @author ivs
 */
public class DataMessage {

    private Integer msgNo;//int4 NOT NULL,
    private String msgText1;// text,
    private String msgText2;// text,
    private java.util.Date viewableDate;//timestamp NOT NULL,
    private Calendar insertDate;//timestamp NOT NULL,
    private Calendar updateDate;//timestamp NOT NULL,
    private Calendar deleteDate;//timestamp,
    private Integer viewFlg;// int4 DEFAULT 0,

    public Integer getMsgNo() {
        return msgNo;
    }

    public void setMsgNo(Integer msgNo) {
        this.msgNo = msgNo;
    }

    public String getMsgText1() {
        return msgText1;
    }

    public void setMsgText1(String msgText1) {
        this.msgText1 = msgText1;
    }

    public String getMsgText2() {
        return msgText2;
    }

    public void setMsgText2(String msgText2) {
        this.msgText2 = msgText2;
    }

    public java.util.Date getViewableDate() {
        return viewableDate;
    }

    public void setViewableDate(java.util.Date viewableDate) {
        this.viewableDate = viewableDate;
    }

    public Calendar getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Calendar insertDate) {
        this.insertDate = insertDate;
    }

    public Calendar getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Calendar updateDate) {
        this.updateDate = updateDate;
    }

    public Calendar getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Calendar deleteDate) {
        this.deleteDate = deleteDate;
    }

    public Integer getViewFlg() {
        return viewFlg;
    }

    public void setViewFlg(Integer viewFlg) {
        this.viewFlg = viewFlg;
    }
     public void setData(ResultSetWrapper rs) throws SQLException
     {
         this.setMsgNo(rs.getInt("msg_no"));
         this.setMsgText1(rs.getString("msg_text1"));
         this.setMsgText2(rs.getString("msg_text2"));
         this.setViewableDate(rs.getTimestamp("viewable_date"));
         this.setViewFlg(rs.getInt("view_flg"));
     }
   
}
