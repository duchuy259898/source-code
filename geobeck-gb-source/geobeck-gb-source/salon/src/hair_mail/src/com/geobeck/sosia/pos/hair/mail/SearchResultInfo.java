/*
 * SearchResultInfo.java
 *
 * Created on 2008/11/07, 14:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.mail;

import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sql.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class SearchResultInfo extends MstCustomer {

    private String lastShopName;
    private String lastVisitDate;
    private String lastStaffName;
    private String introducerName = "";
    private String introducerNo = "";
    private String introduceNo = "";
    private String introduceName = "";
    private String free1;
    private String free2;
    private String free3;
    private String free4;
    private Boolean designatedFlag;
    private String age;
    private String free;
    private String firstComingMotiveName;
    private String firstComingMotiveNote;
    private Long sumTotal;
    private String nextReserveDate;
    private String nextReserveType;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
    private ArrayList<String> arrFreeHeading = new ArrayList<String>(); 

     public ArrayList<String> getArraryFree() {
            return arrFreeHeading;
    }
   public void setArraryFree(ArrayList<String> arrFreeHeading){
        this.arrFreeHeading = arrFreeHeading;
   }

    public void setData(ResultSetWrapper rs, ConnectionWrapper con) throws SQLException {
        this.setData(rs, con, false);
    }

    public void setData(ResultSetWrapper rs, ConnectionWrapper con, boolean isGroup) throws SQLException {
        super.setData(rs);

        super.setVisitCount(rs.getLong("visit_num"));

        if (isGroup) {
            // 本部選択の場合は最終来店店舗の情報を取得する
            MstShop shop = new MstShop();
            shop.setShopID(rs.getInt("last_shop_id"));
            try {
                shop.load(con);
            } catch (Exception e) {
            }
            this.setShop(shop);
        }

        this.setLastShopName(rs.getString("shop_name") != null ? rs.getString("shop_name") : "");
        this.setLastVisitDate(rs.getDate("sales_date") != null ? sdf.format((rs.getDate("sales_date"))) : "");
        this.setLastStaffName(rs.getString("staff_name") != null ? rs.getString("staff_name") : "");
        this.setIntroducerNo(rs.getString("introducer_no") != null ? rs.getString("introducer_no") : "");
        this.setIntroducerName(rs.getString("introducer_name") != null ? rs.getString("introducer_name") : "");
        try {
            this.setFree1(rs.getString("free_heading_name1") != null && !rs.getString("free_heading_name1").equals("") ? rs.getString("free_heading_name1") : "");
        } catch (Exception e) {
        }
        try {
            this.setFree2(rs.getString("free_heading_name2") != null && !rs.getString("free_heading_name2").equals("") ? rs.getString("free_heading_name2") : "");
        } catch (Exception e) {
        }
        try {
            this.setFree3(rs.getString("free_heading_name3") != null && !rs.getString("free_heading_name3").equals("") ? rs.getString("free_heading_name3") : "");
        } catch (Exception e) {
        }
        try {
            this.setFree4(rs.getString("free_heading_name4") != null && !rs.getString("free_heading_name4").equals("") ? rs.getString("free_heading_name4") : "");
        } catch (Exception e) {
        }
        this.setAge(rs.getString("age") != null ? rs.getString("age") : "");
        this.setDesignatedFlag(rs.getBoolean("designated_flag"));
        this.setFirstComingMotiveName(rs.getString("first_coming_motive_name") != null ? rs.getString("first_coming_motive_name") : "");
        this.setFirstComingMotiveNote(rs.getString("first_coming_motive_note") != null ? rs.getString("first_coming_motive_note") : "");
        this.setSumTotal(rs.getLong("sumTotal"));
        this.setNextReserveDate(rs.getString("next_reserve_date") != null && !rs.getString("next_reserve_date").equals("") ? rs.getString("next_reserve_date") : "");
        this.setNextReserveType(rs.getString("next_reserve_type") != null && !rs.getString("next_reserve_type").equals("") ? rs.getString("next_reserve_type") : "");
        //IVS_LVTu start edit 2015/07/16 Bug #40581
        //this.setFirstVisitDate(rs.getDate("first_visit_date"));
        if ( rs.getDate("first_visit_date") == null && rs.getDate("date_sales") != null) {
            try {
                this.setFirstVisitDate(rs.getDate("date_sales"));
            }catch(SQLException e) {
                
            }
        }else {
            this.setFirstVisitDate(rs.getDate("first_visit_date"));
        }
        //IVS_LVTu end edit 2015/07/16 Bug #40581

        getJob().setJobName(rs.getString("job_name"));

        if (designatedFlag) {
            free = "指名";
        } else {
            free = "フリー";
        }

        // 紹介した人
        if (rs.getString("introducer_no_array") != null) {
            this.introduceNo = rs.getString("introducer_no_array").replace("{", "").replace("}", "").replace("\"", "");
        }
        if (rs.getString("introducer_name_array") != null) {
            this.introduceName = rs.getString("introducer_name_array").replace("{", "").replace("}", "").replace("\"", "");
        }

    }
    
    
      public void setData(ResultSetWrapper rs, ConnectionWrapper con, boolean isGroup  , int isFreeHeadingSize) throws SQLException {
        super.setData(rs);

        super.setVisitCount(rs.getLong("visit_num"));

        if (isGroup) {
            // 本部選択の場合は最終来店店舗の情報を取得する
            MstShop shop = new MstShop();
            shop.setShopID(rs.getInt("last_shop_id"));
            try {
                shop.load(con);
            } catch (Exception e) {
            }
            this.setShop(shop);
        }

        this.setLastShopName(rs.getString("shop_name") != null ? rs.getString("shop_name") : "");
        this.setLastVisitDate(rs.getDate("sales_date") != null ? sdf.format((rs.getDate("sales_date"))) : "");
        this.setLastStaffName(rs.getString("staff_name") != null ? rs.getString("staff_name") : "");
        this.setIntroducerNo(rs.getString("introducer_no") != null ? rs.getString("introducer_no") : "");
        this.setIntroducerName(rs.getString("introducer_name") != null ? rs.getString("introducer_name") : "");
        // IVS change 20140925 Bug #30953
//        try {
//            this.setFree1(rs.getString("free_heading_name1") != null && !rs.getString("free_heading_name1").equals("") ? rs.getString("free_heading_name1") : "");
//        } catch (Exception e) {
//        }
//        try {
//            this.setFree2(rs.getString("free_heading_name2") != null && !rs.getString("free_heading_name2").equals("") ? rs.getString("free_heading_name2") : "");
//        } catch (Exception e) {
//        }
//        try {
//            this.setFree3(rs.getString("free_heading_name3") != null && !rs.getString("free_heading_name3").equals("") ? rs.getString("free_heading_name3") : "");
//        } catch (Exception e) {
//        }
//        try {
//            this.setFree4(rs.getString("free_heading_name4") != null && !rs.getString("free_heading_name4").equals("") ? rs.getString("free_heading_name4") : "");
//        } catch (Exception e) {
//        }
        
         for (int i = 1; i <= isFreeHeadingSize; i++) {
            String free_heading_name = rs.getString("free_heading_name" + i) != null && !rs.getString("free_heading_name" + i).equals("") ? rs.getString("free_heading_name" + i) : "";
             arrFreeHeading.add(i -1,free_heading_name);
        }
         // IVS change 20140925 Bug #30953
        //IVS_TMTrong start edit 2015/10/19 New request #43511 
        String ageTemp  = rs.getString("age");
         if( ageTemp!= null && !ageTemp.equals("0")){
            this.setAge(rs.getString("age"));
        }else{
            this.setAge("");
        }
        //IVS_TMTrong end edit 2015/10/19 New request #43511 
        this.setDesignatedFlag(rs.getBoolean("designated_flag"));
        this.setFirstComingMotiveName(rs.getString("first_coming_motive_name") != null ? rs.getString("first_coming_motive_name") : "");
        this.setFirstComingMotiveNote(rs.getString("first_coming_motive_note") != null ? rs.getString("first_coming_motive_note") : "");
        this.setSumTotal(rs.getLong("sumTotal"));
        this.setNextReserveDate(rs.getString("next_reserve_date") != null && !rs.getString("next_reserve_date").equals("") ? rs.getString("next_reserve_date") : "");
        this.setNextReserveType(rs.getString("next_reserve_type") != null && !rs.getString("next_reserve_type").equals("") ? rs.getString("next_reserve_type") : "");
        //IVS_LVTu start edit 2015/07/16 Bug #40581
        if ( rs.getDate("first_visit_date") == null && rs.getDate("date_sales")  != null) {
            try {
                this.setFirstVisitDate(rs.getDate("date_sales"));
            }catch(SQLException e) {
                
            }
        }else {
            this.setFirstVisitDate(rs.getDate("first_visit_date"));
        }
        //IVS_LVTu end edit 2015/07/16 Bug #40581

        getJob().setJobName(rs.getString("job_name"));

        if (designatedFlag) {
            free = "指名";
        } else {
            free = "フリー";
        }

        // 紹介した人
        if (rs.getString("introducer_no_array") != null) {
            this.introduceNo = rs.getString("introducer_no_array").replace("{", "").replace("}", "").replace("\"", "");
        }
        if (rs.getString("introducer_name_array") != null) {
            this.introduceName = rs.getString("introducer_name_array").replace("{", "").replace("}", "").replace("\"", "");
        }

    }

    public String getPcMailAddress() {
        return super.getPCMailAddress() != null ? super.getPCMailAddress() : "";
    }

    public String getCellularMailAddress() {
        return super.getCellularMailAddress() != null ? super.getCellularMailAddress() : "";
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAge() {
        return this.age != null ? this.age : "";
    }

    public String getBirthdayString() {
        return super.getBirthday() != null && super.getBirthday().getTime() != null ? sdf.format(super.getBirthday().getTime()) : "";
    }

    public String getJobName() {
        return getJob().getJobName() != null ? getJob().getJobName() : "";
    }

    public String getNote() {
        return super.getNote() != null ? super.getNote() : "";
    }

    public String getAddress1() {
        return super.getAddress()[0] != null ? super.getAddress()[0] : "";
    }

    public String getAddress2() {
        return super.getAddress()[1] != null ? super.getAddress()[1] : "";
    }

    public String getAddress3() {
        return super.getAddress()[2] != null ? super.getAddress()[2] : "";
    }

    public String getAddress4() {
        return super.getAddress()[3] != null ? super.getAddress()[3] : "";
    }

    public String getPostalCode() {
        String s = super.getPostalCode();
        if (s == null) {
            return "";
        } else if (s.startsWith("0")) {
            return s + "　";
        } else {
            return s;
        }
    }

    public String getPhoneNumber() {
        String s = super.getPhoneNumber();
        if (s == null) {
            return "";
        } else if (s.startsWith("0")) {
            return s + "　";
        } else {
            return s;
        }
    }

    public String getCellularNumber() {
        String s = super.getCellularNumber();
        if (s == null) {
            return "";
        } else if (s.startsWith("0")) {
            return s + "　";
        } else {
            return s;
        }
    }

    public String getFaxNumber() {
        String s = super.getFaxNumber();
        if (s == null) {
            return "";
        } else if (s.startsWith("0")) {
            return s + "　";
        } else {
            return s;
        }
    }

    public void setIntroducerNo(String number) {
        introducerNo = number;
    }

    public String getCustomerNo() {
        String s = super.getCustomerNo();
        if (s == null) {
            return "";
        } else if (s.length() > 1 && s.startsWith("0")) {
            return s + "　";
        } else {
            return s;
        }
    }

    public String getIntroducerNo() {
        String s = introducerNo;
        if (s == null) {
            return "";
        } else if (s.length() > 1 && s.startsWith("0")) {
            return s + "　";
        } else {
            return s;
        }
    }

    public void setDesignatedFlag(Boolean b) {
        this.designatedFlag = b;
    }

    public Boolean isDesignatedFlag() {
        return this.designatedFlag;
    }

    public void setIntroducerName(String name) {
        this.introducerName = name;
    }

    public String getFree() {
        return free;
    }

    public String getIsMember() {
        return free;
    }

    public String getIntroducerName() {
        return this.introducerName;
    }

    public String getLastShopName() {
        return lastShopName;
    }

    public void setLastShopName(String lastShopName) {
        this.lastShopName = lastShopName;
    }

    public String getLastVisitDate() {
        return lastVisitDate;
    }

    public void setLastVisitDate(String lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    public String getLastStaffName() {
        return lastStaffName;
    }

    public void setLastStaffName(String lastStaffName) {
        this.lastStaffName = lastStaffName;
    }

    public String getFree1() {
        return free1;
    }

    public void setFree1(String free1) {
        this.free1 = free1;
    }

    public String getFree2() {
        return free2;
    }

    public void setFree2(String free2) {
        this.free2 = free2;
    }

    public String getFree3() {
        return free3;
    }

    public void setFree3(String free3) {
        this.free3 = free3;
    }

    public String getFree4() {
        return free4;
    }

    public void setFree4(String free4) {
        this.free4 = free4;
    }

    public String getIntroduceNo() {
        return introduceNo;
    }

    public void setIntroduceNo(String introduceNo) {
        this.introduceNo = introduceNo;
    }

    public String getIntroduceName() {
        return introduceName;
    }

    public void setIntroduceName(String introduceName) {
        this.introduceName = introduceName;
    }

    public String getFirstComingMotiveName() {
        return firstComingMotiveName;
    }

    public void setFirstComingMotiveName(String firstComingMotive) {
        this.firstComingMotiveName = firstComingMotive;
    }

    public String getFirstComingMotiveNote() {
        return firstComingMotiveNote;
    }

    public void setFirstComingMotiveNote(String firstComingMotiveNote) {
        this.firstComingMotiveNote = firstComingMotiveNote;
    }

    public Long getSumTotal() {
        return sumTotal;
    }

    public void setSumTotal(Long sumTotal) {
        this.sumTotal = sumTotal;
    }

    public String getNextReserveDate() {
        return nextReserveDate;
    }

    public void setNextReserveDate(String nextReserveDate) {
        this.nextReserveDate = nextReserveDate;
    }

    public String getNextReserveType() {
        return nextReserveType;
    }

    public void setNextReserveType(String nextReserveType) {
        this.nextReserveType = nextReserveType;
    }
}
