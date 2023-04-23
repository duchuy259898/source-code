/*
 * MstStaffClass.java
 *
 * Created on 2006/04/25, 17:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.basicinfo.company;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * スタッフ区分データ
 *
 * @author nakhoa
 */
public class DataTargetOX {

    private Integer shopID = null;
    private Integer year = null;
    private Integer month = null;
    private Integer technicID = null;
    private Integer item = null;
    private Integer cota = null;
    private Integer cutNum = null;
    private Integer cutSale = null;
    private Integer permNum = null;
    private Integer permSale = null;
    private Integer colNum = null;
    private Integer colSale = null;
    //private Integer culSale = null;
    private Integer spaNum = null;
    private Integer spaSale = null;
    private Integer treatNum = null;
    private Integer treatSale = null;

    private Integer totalNum = null;
    private Integer itemNum = null;
    private Integer yNum = null;
    private Integer oNum = null;
    private Integer mNum = null;
    private Integer unNum = null;
    private Integer newNum = null;
    private Integer usallyNum =  null;
    private Integer newRepertNum = null;
    private Integer introNum = null;
    private Integer nominatNum = null;
    private Integer openDay = null;
    private Integer eStylist = null;
    private Integer eAssistant = null;
    private Integer arStylist = null;
    private Integer arAssistant = null;
    private Integer arOther = null;

    public Integer getTotalNum() {
        return totalNum;
    }

    public String getTotalNumStr(){
        return String.valueOf(this.totalNum);
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }
    
    public Integer getShopID() {
        return shopID;
    }

    public String getShopIdStr(){
        if(this.shopID != null){
            return this.shopID.toString();
        }
        return "";
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getTechnicID() {
        return technicID;
    }

    public String getTechnicIdStr(){
        if(this.technicID != null){
            return this.technicID.toString();
        }
        return "";
    }

    public void setTechnicID(Integer technicID) {
        this.technicID = technicID;
    }

    public Integer getItem() {
        return item;
    }

    public String getItemStr(){
        if(this.item != null){
            return this.item.toString();
        }
        return "";
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Integer getCota() {
        return cota;
    }

    public String getCotaStr(){
        if(this.cota != null){
            return this.cota.toString();
        }
        return "";
    }

    public void setCota(Integer cota) {
        this.cota = cota;
    }

    public Integer getCutNum() {
        return cutNum;
    }

    public String getCutNumStr(){
        if(this.cutNum != null){
            return this.cutNum.toString();
        }
        return "";
    }

    public void setCutNum(Integer cutNum) {
        this.cutNum = cutNum;
    }

    public Integer getCutSale() {
        return cutSale;
    }

    public String getCutSaleStr(){
        if(this.cutSale != null){
            return this.cutSale.toString();
        }
        return "";
    }

    public void setCutSale(Integer cutSale) {
        this.cutSale = cutSale;
    }

    public Integer getPermNum() {
        return permNum;
    }

    public String getPermNumStr(){
        if(this.permNum != null){
            return this.permNum.toString();
        }
        return "";
    }

    public void setPermNum(Integer permNum) {
        this.permNum = permNum;
    }

    public Integer getPermSale() {
        return permSale;
    }

    public String getPermSaleStr(){
        if(this.permSale != null){
            return this.permSale.toString();
        }
        return "";
    }

    public void setPermSale(Integer permSale) {
        this.permSale = permSale;
    }

    public Integer getColNum() {
        return colNum;
    }

    public String getColNumStr(){
        if(this.colNum != null){
            return this.colNum.toString();
        }
        return "";
    }

    public void setColNum(Integer colNum) {
        this.colNum = colNum;
    }

    public void setColSale(Integer colSale){
        this.colSale = colSale;
    }

    public String getColSaleStr(){
        if(this.colSale != null){
            return this.colSale.toString();
        }
        return "";
    }

    public Integer getColSale(){
        return this.colSale;
    }

//    public Integer getCulSale() {
//        return culSale;
//    }
//
//    public String getCulSaleStr(){
//        if(this.culSale != null){
//            return this.culSale.toString();
//        }
//        return "";
//    }
//
//    public void setCulSale(Integer culSale) {
//        this.culSale = culSale;
//    }

    public Integer getSpaNum() {
        return spaNum;
    }

    public String getSpaNumStr(){
        if(this.spaNum != null){
            return this.spaNum.toString();
        }
        return "";
    }

    public void setSpaNum(Integer spaNum) {
        this.spaNum = spaNum;
    }

    public Integer getSpaSale() {
        return spaSale;
    }

    public String getSpaSaleStr(){
        if(this.spaSale != null){
            return this.spaSale.toString();
        }
        return "";
    }

    public void setSpaSale(Integer spaSale) {
        this.spaSale = spaSale;
    }

    public Integer getTreatNum() {
        return treatNum;
    }

    public String getTreatNumStr(){
        if(this.treatNum != null){
            return this.treatNum.toString();
        }
        return "";
    }

    public void setTreatNum(Integer treatNum) {
        this.treatNum = treatNum;
    }

    public Integer getTreatSale() {
        return treatSale;
    }

    public String getTreatSaleStr(){
        if(this.treatSale != null){
            return this.treatSale.toString();
        }
        return "";
    }

    public void setTreatSale(Integer treatSale) {
        this.treatSale = treatSale;
    }

    public Integer getItemNum() {
        return itemNum;
    }

    public String getitemNumStr(){
        if(this.itemNum != null){
            return this.itemNum.toString();
        }
        return "";
    }

    public void setItemNum(Integer itemNum) {
        this.itemNum = itemNum;
    }

    public Integer getyNum() {
        return yNum;
    }

    public String getyNumStr(){
        if(this.yNum != null){
            return this.yNum.toString();
        }
        return "";
    }

    public void setyNum(Integer yNum) {
        this.yNum = yNum;
    }

    public Integer getoNum() {
        return oNum;
    }

    public String getoNumStr(){
        if(this.oNum != null){
            return this.oNum.toString();
        }
        return "";
    }

    public void setoNum(Integer oNum) {
        this.oNum = oNum;
    }

    public Integer getmNum() {
        return mNum;
    }

    public String getmNumStr(){
        if(this.mNum != null){
            return this.mNum.toString();
        }
        return "";
    }

    public void setmNum(Integer mNum) {
        this.mNum = mNum;
    }

    public Integer getUnNum() {
        return unNum;
    }

    public String getUnNumStr(){
        return String.valueOf(this.unNum);
    }

    public void setUnNum(Integer unNum) {
        this.unNum = unNum;
    }

    public Integer getNewNum() {
        return newNum;
    }

    public String getNewNumStr(){
        if(this.newNum != null){
            return this.newNum.toString();
        }
        return "";
    }

    public void setNewNum(Integer newNum) {
        this.newNum = newNum;
    }

    public Integer getUsallyNum() {
        return usallyNum;
    }

    public String getUsallyNumStr(){
        return String.valueOf(this.usallyNum);
    }

    public void setUsallyNum(Integer usallyNum) {
        this.usallyNum = usallyNum;
    }

    public Integer getNewRepertNum() {
        return newRepertNum;
    }

    public String getNewRepertNumStr(){
        if(this.newRepertNum != null){
            return this.newRepertNum.toString();
        }
        return "";
    }

    public void setNewRepertNum(Integer newRepertNum) {
        this.newRepertNum = newRepertNum;
    }

    public Integer getIntroNum() {
        return introNum;
    }

    public String getIntroNumStr(){
        if(this.introNum != null){
            return this.introNum.toString();
        }
        return "";
    }

    public void setIntroNum(Integer introNum) {
        this.introNum = introNum;
    }

    public Integer getNominatNum() {
        return nominatNum;
    }

    public String getnominatNumStr(){
        if(this.nominatNum != null){
            return this.nominatNum.toString();
        }
        return "";
    }

    public void setNominatNum(Integer nominatNum) {
        this.nominatNum = nominatNum;
    }

    public Integer getOpenDay() {
        return openDay;
    }

    public String getopenDayStr(){
        if(this.openDay != null){
            return this.openDay.toString();
        }
        return "";
    }

    public void setOpenDay(Integer openDay) {
        this.openDay = openDay;
    }

    public Integer geteStylist() {
        return eStylist;
    }

    public String geteStylistStr(){
        if(this.eStylist != null){
            return this.eStylist.toString();
        }
        return "";
    }

    public void seteStylist(Integer eStylist) {
        this.eStylist = eStylist;
    }

    public Integer geteAssistant() {
        return eAssistant;
    }

    public String geteAssistantStr(){
        if(this.eAssistant != null){
            return this.eAssistant.toString();
        }
        return "";
    }

    public void seteAssistant(Integer eAssistant) {
        this.eAssistant = eAssistant;
    }

    public Integer getArStylist() {
        return arStylist;
    }

    public String getArStylistStr(){
        if(this.arStylist != null){
            return this.arStylist.toString();
        }
        return "";
    }

    public void setArStylist(Integer arStylist) {
        this.arStylist = arStylist;
    }

    public Integer getArAssistant() {
        return arAssistant;
    }

    public String getArAssistantStr(){
        if(this.arAssistant != null){
            return this.arAssistant.toString();
        }
        return "";
    }

    public void setArAssistant(Integer arAssistant) {
        this.arAssistant = arAssistant;
    }

    public Integer getArOther() {
        return arOther;
    }

    public String getArOtherStr(){
        if(this.arOther != null){
            return this.arOther.toString();
        }
        return "";
    }

    public void setArOther(Integer arOther) {
        this.arOther = arOther;
    }
    private Timestamp insertDate;
    private Timestamp updateDate;
   
    /**
     * コンストラクタ
     */
    public DataTargetOX() {
        
    }

    

    public Timestamp getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Timestamp insertDate) {
        this.insertDate = insertDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

     /**
     * データをクリアする。
     */
    public void clear() {
        this.setTechnicID(0);
        this.setItem(0);
        this.setCota(0);
        this.setCutNum(0);
        this.setCutSale(0);
        this.setPermNum(0);
        this.setPermSale(0);
        this.setColNum(0);
        this.setColSale(0);
        this.setSpaNum(0);
        this.setSpaSale(0);
        this.setTreatNum(0);
        this.setTreatSale(0);
        this.setItemNum(0);
        this.setoNum(0);
        this.setyNum(0);
        this.setmNum(0);
        this.setNewNum(0);
        this.setNewRepertNum(0);
        this.setTotalNum(0);
        this.setIntroNum(0);
        this.setNominatNum(0);
        this.setOpenDay(0);
        this.seteAssistant(0);
        this.seteStylist(0);
        this.setArAssistant(0);
        this.setArOther(0);
        this.setArStylist(0);
        this.setMonth(0);
        this.setShopID(0);
        this.setYear(0);
        this.setUnNum(0);
        this.setUsallyNum(0);
        this.setInsertDate(null);
        this.setUpdateDate(null);
    }

    /**
     * スタッフ区分マスタデータからデータをセットする。
     *
     * @param msc スタッフ区分マスタデータ
     */
    public void setData(DataTargetOX dt) {
        this.setArAssistant(dt.getArAssistant());
        this.setArOther(dt.getArOther());
        this.setArStylist(dt.getArStylist());
        this.setColNum(dt.getColNum());
        this.setCota(dt.getCota());
        this.setColSale(dt.getColSale());
        this.setCutNum(dt.getCutNum());
        this.setCutSale(dt.getCutSale());
        this.setInsertDate(dt.getInsertDate());
        this.setIntroNum(dt.getIntroNum());
        this.setItem(dt.getItem());
        this.setItemNum(dt.getItemNum());
        this.setMonth(dt.getMonth());
        this.setNewNum(dt.getNewNum());
        this.setNewRepertNum(dt.getNewRepertNum());
        this.setNominatNum(dt.getNominatNum());
        this.setOpenDay(dt.getOpenDay());
        this.setPermNum(dt.getPermNum());
        this.setPermSale(dt.getPermSale());
        this.setShopID(dt.getShopID());
        this.setSpaNum(dt.getSpaNum());
        this.setSpaSale(dt.getSpaSale());
        this.setTechnicID(dt.getTechnicID());
        this.setTreatNum(dt.getTreatNum());
        this.setTreatSale(dt.getTreatSale());
        this.setUpdateDate(dt.getUpdateDate());
        this.setYear(dt.getYear());
        this.setyNum(dt.getyNum());
        this.seteAssistant(dt.geteAssistant());
        this.seteStylist(dt.geteStylist());
        this.setoNum(dt.getoNum());
        this.setmNum(dt.getmNum());
        this.setUnNum(dt.getUnNum());
        this.setUsallyNum(dt.getUsallyNum());
        this.setTotalNum(dt.getTotalNum());
        
    }

    /**
     * ResultSetWrapperのデータを読み込む。
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setTechnicID(rs.getInt("technic"));
        this.setItem(rs.getInt("item"));
        this.setCota(rs.getInt("cota"));
        this.setCutNum(rs.getInt("cut_num"));
        this.setCutSale(rs.getInt("cut_sale"));
        this.setPermNum(rs.getInt("perm_num"));
        this.setPermSale(rs.getInt("perm_sale"));
        this.setColNum(rs.getInt("col_num"));
        this.setColSale(rs.getInt("col_sale"));
        this.setSpaNum(rs.getInt("spa_num"));
        this.setSpaSale(rs.getInt("spa_sale"));
        this.setTreatNum(rs.getInt("treat_num"));
        this.setTreatSale(rs.getInt("treat_sale"));
        this.setItemNum(rs.getInt("item_num"));
        this.setyNum(rs.getInt("y_num"));
        this.setoNum(rs.getInt("o_num"));
        this.setmNum(rs.getInt("m_num"));
        this.setUnNum(rs.getInt("un_num"));
        this.setNewNum(rs.getInt("new_num"));
        this.setUsallyNum(rs.getInt("usally_num"));
        this.setNewRepertNum(rs.getInt("new_repert_num"));
        this.setIntroNum(rs.getInt("intro_num"));
        this.setNominatNum(rs.getInt("nominat_num"));
    }

    public void setDataOX(ResultSetWrapper rs) throws SQLException {
        this.setTechnicID(rs.getInt("technic"));
        this.setItem(rs.getInt("item"));
        this.setCota(rs.getInt("cota"));
        this.setCutNum(rs.getInt("cut_num"));
        this.setCutSale(rs.getInt("cut_sale"));
        this.setPermNum(rs.getInt("perm_num"));
        this.setPermSale(rs.getInt("perm_sale"));
        this.setColNum(rs.getInt("col_num"));
        this.setColSale(rs.getInt("col_sale"));
        this.setSpaNum(rs.getInt("spa_num"));
        this.setSpaSale(rs.getInt("spa_sale"));
        this.setTreatNum(rs.getInt("treat_num"));
        this.setTreatSale(rs.getInt("treat_sale"));
        this.setItemNum(rs.getInt("item_num"));
        this.setyNum(rs.getInt("y_num"));
        this.setoNum(rs.getInt("o_num"));
        this.setmNum(0);
        this.setUnNum(0);
        this.setNewNum(rs.getInt("new_num"));
        this.setUsallyNum(0);
        this.setNewRepertNum(rs.getInt("new_repert_num"));
        this.setTotalNum(rs.getInt("total_num"));
        this.setIntroNum(rs.getInt("intro_num"));
        this.setNominatNum(rs.getInt("nominat_num"));
        // set open_day
        this.setOpenDay(rs.getInt("open_day"));
        // set e_stylist
        this.seteStylist(rs.getInt("e_stylist"));
        // set e_assistant
        this.seteAssistant(rs.getInt("e_assistant"));
        // set ar_stylist
        this.setArStylist(rs.getInt("ar_stylist"));
        // set ar_assistant
        this.setArAssistant(rs.getInt("ar_assistant"));
        // set ar_othe
        this.setArOther(rs.getInt("ar_other"));
    }

    /**
     * スタッフ区分マスタデータをArrayListに読み込む。
     */
    public ArrayList<DataTargetOX> loadAllDate(ConnectionWrapper con) throws SQLException {
        ArrayList<DataTargetOX> list = new ArrayList<DataTargetOX>();
        ResultSetWrapper rs = con.executeQuery(getSelectAllSQL());
        while (rs.next()) {
            DataTargetOX msc = new DataTargetOX();
            msc.setData(rs);
            list.add(msc);
        }
        rs.close();

        return list;
    }
    
    /**
     * 
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getShopID() == null || this.getShopID() < 1
                || this.getMonth() == null || this.getYear() == null ) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean insertData(ConnectionWrapper con)throws SQLException{
        if(con == null){
            return false;
        }
        if(con.executeUpdate(this.getInsertSQL()) != 1){
            return false;
        }else{
            return true;
        }
    }

    public boolean UpdatetData(ConnectionWrapper con)throws SQLException{
        if(con == null){
            return false;
        }
        if(con.executeUpdate(this.getUpdateSQL()) != 1){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Select文を取得する。
     *
     * @return Select文
     */
    private String getSelectAllSQL() {
        return " select * \n"
                + "from data_target_ox \n"
                + "where shopID =  " + SQLUtil.convertForSQL(this.getShopID()) + "\n"
                + "      and year = " + SQLUtil.convertForSQL(this.getYear());

    }
    
    
    private String getSelectSQL() {
        return " select * \n"
                + "from data_target_ox \n"
                + "where shop_id =  " + SQLUtil.convertForSQL(this.getShopID()) + "\n"
                + "      and year = " + SQLUtil.convertForSQL(this.getYear())
                + "      and month = " + SQLUtil.convertForSQL(this.getMonth());

    }
    
    /**
     * Insert文を取得する。
     *
     * @return Insert文
     */
    private String getInsertSQL() {
        
        return " insert into data_target_ox "
                + "     (shop_id, year,month,technic,item, \n"
                + "     cota, cut_num , cut_sale,  perm_num, perm_sale, col_num, \n"
                + "     col_sale, spa_num , spa_sale , treat_num , treat_sale , \n "
                + "     item_num, y_num ,  o_num , new_num, new_repert_num, total_num, \n"
                + "     intro_num, nominat_num, open_day, e_stylist, e_assistant, \n "
                + "     ar_stylist, ar_assistant, ar_other, insert_date, update_date \n)"
                + "values ( "
                + SQLUtil.convertForSQL(this.getShopID()) +", "
                + SQLUtil.convertForSQL(this.getYear()) + ", "
                + SQLUtil.convertForSQL(this.getMonth()) + ", "
                + SQLUtil.convertForSQL(this.getTechnicID()) +", "
                +SQLUtil.convertForSQL(this.getItem()) + ", \n "
                + SQLUtil.convertForSQL(this.getCota()) + ", "
                +SQLUtil.convertForSQL(this.getCutNum()) + ", "
                +SQLUtil.convertForSQL(this.getCutSale()) + ", "
                +SQLUtil.convertForSQL(this.getPermNum()) + ", "
                +SQLUtil.convertForSQL(this.getPermSale()) + ", "
                +SQLUtil.convertForSQL(this.getColNum()) + ", \n"
                +SQLUtil.convertForSQL(this.getColSale()) + ","
                +SQLUtil.convertForSQL(this.getSpaNum()) + ","
                +SQLUtil.convertForSQL(this.getSpaSale()) + ","
                +SQLUtil.convertForSQL(this.getTreatNum()) + ","
                +SQLUtil.convertForSQL(this.getTreatSale()) + ", \n"
                +SQLUtil.convertForSQL(this.getItemNum()) + ","
                +SQLUtil.convertForSQL(this.getyNum()) + ", "
                +SQLUtil.convertForSQL(this.getoNum()) + ", "
                +SQLUtil.convertForSQL(this.getNewNum()) + ", "
                +SQLUtil.convertForSQL(this.getNewRepertNum()) + ", "
                +SQLUtil.convertForSQL(this.getTotalNum()) + ", \n"
                +SQLUtil.convertForSQL(this.getIntroNum()) + ", "
                +SQLUtil.convertForSQL(this.getNominatNum()) + ", "
                +SQLUtil.convertForSQL(this.getOpenDay()) + ", "
                +SQLUtil.convertForSQL(this.geteStylist()) + ", "
                +SQLUtil.convertForSQL(this.geteAssistant()) + ", \n"
                +SQLUtil.convertForSQL(this.getArStylist()) + ","
                +SQLUtil.convertForSQL(this.getArAssistant()) + ", "
                +SQLUtil.convertForSQL(this.getArOther()) + ", "
                + "current_timestamp,current_timestamp\n ) " ;
       
    }

    /**
     * Update文を取得する。
     *
     * @return Update文
     */
    private String getUpdateSQL() {
        return "update data_target_ox \n"
                + "set\n"
                + "technic = " + SQLUtil.convertForSQL(this.getTechnicID()) + ", \n"
                + "item = " + SQLUtil.convertForSQL(this.getItem()) + ", \n"
                + "cota = " + SQLUtil.convertForSQL(this.getCota()) + ", \n"
                + "cut_num = " + SQLUtil.convertForSQL(this.getCutNum()) + ", \n"
                + "cut_sale = " + SQLUtil.convertForSQL(this.getCutSale()) + ", \n"
                + "perm_num = " + SQLUtil.convertForSQL(this.getPermNum()) + ", \n"
                + "perm_sale = " + SQLUtil.convertForSQL(this.getPermSale()) + ", \n"
                + "col_num = " + SQLUtil.convertForSQL(this.getColNum()) + ", \n"
                + "col_sale = " + SQLUtil.convertForSQL(this.getColSale()) + ", \n"
                + "spa_num = " + SQLUtil.convertForSQL(this.getSpaNum()) + ", \n"
                + "spa_sale = " + SQLUtil.convertForSQL(this.getSpaSale()) + ", \n"
                + "treat_num = " + SQLUtil.convertForSQL(this.getTreatNum()) + ", \n"
                + "treat_sale = " + SQLUtil.convertForSQL(this.getTreatSale()) + ", \n"
                + "item_num = " + SQLUtil.convertForSQL(this.getItemNum()) + ", \n"
                + "y_num = " + SQLUtil.convertForSQL(this.getyNum()) + ", \n"
                + "o_num = " + SQLUtil.convertForSQL(this.getoNum()) + ", \n"
                + "new_num = " + SQLUtil.convertForSQL(this.getNewNum()) + ", \n"
                + "new_repert_num = " + SQLUtil.convertForSQL(this.getNewRepertNum()) + ", \n"
                + "total_num = " + SQLUtil.convertForSQL(this.getTotalNum()) + ", \n"
                + "intro_num = " + SQLUtil.convertForSQL(this.getIntroNum()) + ", \n"
                + "nominat_num = " + SQLUtil.convertForSQL(this.getNominatNum()) + ", \n"
                + "open_day = " + SQLUtil.convertForSQL(this.getOpenDay()) + ", \n"
                + "e_stylist = " + SQLUtil.convertForSQL(this.geteStylist()) + ", \n"
                + "e_assistant = " + SQLUtil.convertForSQL(this.geteAssistant()) + ", \n"
                + "ar_stylist = " + SQLUtil.convertForSQL(this.getArStylist()) + ", \n"
                + "ar_assistant = " + SQLUtil.convertForSQL(this.getArAssistant()) + ", \n"
                + "ar_other = " + SQLUtil.convertForSQL(this.getArOther()) + ", \n"                
                + "update_date = current_timestamp \n"
                + " where shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n"
                + " and  year = " + SQLUtil.convertForSQL(this.getYear()) +" \n"
                + " and month = " + SQLUtil.convertForSQL(this.getMonth()) ;           
    }
}
