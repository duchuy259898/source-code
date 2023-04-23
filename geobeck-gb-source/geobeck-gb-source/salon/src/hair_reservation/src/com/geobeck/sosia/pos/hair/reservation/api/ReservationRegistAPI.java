/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.reservation.api;

import com.geobeck.sosia.pos.hair.data.reservation.DataReservation;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lvtu
 */
public class ReservationRegistAPI {

    final String        CUSTOMER_FAMILY_KANA        = "ソシア";
    final String        CUSTOMER_FAMILY_NAME        = "SOSIA";
    final String        CUSTOMER_GIVEN_KANA         = "ヨヤク";
    final String        CUSTOMER_GIVEN_NAME         = "予約";
    
    final int           FLAG_INSERT_UPDATE          = 1;
    final int           FLAG_INSERT                 = 1;
    final int           FLAG_UPDATE                 = 2;
    final int           FLAG_DELETE                 = 3;

    Integer             apiID                       = SystemInfo.getUserAPI();
    Integer             salonID                     = SystemInfo.getApiSalonID();
    Integer             shopID                      = null;
    Integer             reservationID               = null;
    Integer             reservationNo               = null;
    PayLoad             payload                     = null;
    String              apiUrl                      = "";
    String              apiAuthCode                 = "";
    ResponseReservation reponseData                 = null;
    DataReservation     reservation                 = new DataReservation();
    String              startTime                   = "";
    String              endTime                     = "";

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public DataReservation getReservation() {
        return reservation;
    }

    public void setReservation(DataReservation reservation) {
        this.reservation = reservation;
    }

    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

    public Integer getReservationNo() {
        return reservationNo;
    }

    public void setReservationNo(Integer reservationNo) {
        this.reservationNo = reservationNo;
    }

    public Integer getApiID() {
        return apiID;
    }

    public void setApiID(Integer apiID) {
        this.apiID = apiID;
    }

    public Integer getSalonID() {
        return salonID;
    }

    public void setSalonID(Integer salonID) {
        this.salonID = salonID;
    }

    public Integer getReservationID() {
        return reservationID;
    }

    public void setReservationID(Integer reservationID) {
        this.reservationID = reservationID;
    }

    public PayLoad getPayload() {
        return payload;
    }

    public void setPayload(PayLoad payload) {
        this.payload = payload;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiAuthCode() {
        return apiAuthCode;
    }

    public void setApiAuthCode(String apiAuthCode) {
        this.apiAuthCode = apiAuthCode;
    }

    public ReservationRegistAPI() {

        try {
            ConnectionWrapper con = SystemInfo.getBaseConnection();
            load(con);
        } catch (SQLException ex) {
            Logger.getLogger(ReservationRegistAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean registReservationAPI(ConnectionWrapper connect, int flag) {
        boolean result = false;
        try {
            String url = "";
            switch (flag) {
                case FLAG_INSERT:
                    url = this.apiUrl + "/salon/" + this.salonID + "/reservation";
                    break;
                case FLAG_UPDATE:
                    url = this.apiUrl + "/salon/" + this.salonID + "/reservation/" + this.reservationID;
                    break;
                case FLAG_DELETE:
                    url = this.apiUrl + "/salon/" + this.salonID + "/reservation/" + this.reservationID;
                    break;
                default:
                    break;
            }

            if (url.equals("")) {
                return result;
            }

            URL object = new URL(url);
            Gson gson = new Gson();

            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/vnd.reservation+json");
            con.setRequestProperty("Authorization", this.apiAuthCode);
            con.setRequestProperty("charset", "utf-8");
            con.setDoOutput(true);
            con.setDoInput(true);

            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(gson.toJson(this.payload));
            writer.flush();
            writer.close();
            os.close();

            System.out.println("payload: " + gson.toJson(this.payload));

            //display what returns the POST request
            StringBuilder sb = new StringBuilder();
            int HttpResult = con.getResponseCode();
            if (HttpResult == 200 || HttpResult == 202) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                System.out.println("Response: " + sb.toString());
                result = true;
                if (flag == this.FLAG_INSERT) {
                    ResponseReservation responseReservation = gson.fromJson(sb.toString(), ResponseReservation.class);
                    result = this.getUpdateReservationSQL(connect, responseReservation.id);
                }
            }
            System.out.println(con.getResponseMessage());

        } catch (Exception ex) {
            Logger.getLogger(ReservationRegistAPI.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            return result;
        }   
    }

    private void load(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        if (rs.next()) {
            this.apiUrl = rs.getString("api_url");
            this.apiAuthCode = rs.getString("api_auth_code");
        }
        rs.close();
    }

    private String getSelectSQL() {
        return "select * \n"
                + "from mst_api \n"
                + "where api_id = " + SQLUtil.convertForSQL(this.apiID) + "\n";
    }

    private boolean getUpdateReservationSQL(ConnectionWrapper con, int allianceReserveID) throws SQLException {
        String sql = "update data_reservation\n"
                + "set\n"
                + "alliance_reserve_id = " + SQLUtil.convertForSQL(allianceReserveID) + "\n"
                + "where shop_id = " + SQLUtil.convertForSQL(shopID) + "\n"
                + "and reservation_no = " + SQLUtil.convertForSQL(reservationNo) + "\n";

        if (con.executeUpdate(sql) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean executeAPI(ConnectionWrapper con, int flag) throws SQLException {

        boolean result = false;
        LinkedHashMap<Integer, Integer> mapBed = loadBedShopAll(con, this.reservation.getShop().getShopID());
        LinkedHashMap<Integer, Integer> mapStaff = loadStaffShopAll(con, this.reservation.getShop().getShopID());
        this.shopID         = this.reservation.getShop().getShopID();
        this.reservationNo  = this.reservation.getReservationNo();
        
        PayLoad pay = new PayLoad();
        pay.setCustomer_family_kana(this.CUSTOMER_FAMILY_KANA);
        pay.setCustomer_family_name(this.CUSTOMER_FAMILY_NAME);
        pay.setCustomer_given_kana(this.CUSTOMER_GIVEN_KANA);
        pay.setCustomer_given_name(this.CUSTOMER_GIVEN_NAME);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String reservationDate = dateFormat.format(this.reservation.getReserveDate());
        pay.setDate(reservationDate);
        pay.setEnd_time(this.endTime);
        pay.setFacility_times(this.reservation, mapBed);
        pay.setMedia_id(this.reservation.getReservationNo().toString());
        pay.setStaff_times(this.reservation, mapStaff);
        pay.setStart_time(this.startTime);
        if (flag == this.FLAG_INSERT || flag == this.FLAG_UPDATE) {
            pay.setStatus(1);
        } else if (flag == this.FLAG_DELETE) {
            pay.setStatus(8);
        }

        this.setPayload(pay);

        this.setShopID(this.reservation.getShop().getShopID());
        this.setReservationNo(this.reservation.getReservationNo());
        this.setReservationID(this.getAllianceReserve(con ));
        // case when insert(1)
        if (flag == this.FLAG_INSERT_UPDATE) {
            if (this.getReservationID() == null) {
                result = this.registReservationAPI(con, this.FLAG_INSERT);
            } else {
                result = this.registReservationAPI(con, this.FLAG_UPDATE);
            }
        } else if (flag == this.FLAG_DELETE) {
            result = this.registReservationAPI(con, this.FLAG_DELETE);
        }

        return result;
    }

    public LinkedHashMap<Integer, Integer> loadBedShopAll(ConnectionWrapper con, int shopID) throws SQLException {

        LinkedHashMap<Integer, Integer> linkBed = new LinkedHashMap<Integer, Integer>();

        ResultSetWrapper rs = con.executeQuery(this.getSelectBedSQL(shopID));

        while (rs.next()) {
            linkBed.put(rs.getInt("spos_bed_id"), rs.getInt("bed_id"));
        }
        rs.close();

        return linkBed;
    }

    private String getSelectBedSQL(int shopID) {
        return "select * \n"
                + "from mst_kanzashi_bed \n"
                + "where	shop_id = " + SQLUtil.convertForSQL(shopID) + "\n"
                + "and delete_date is null \n";
    }

    public LinkedHashMap<Integer, Integer> loadStaffShopAll(ConnectionWrapper con, int shopID) throws SQLException {

        LinkedHashMap<Integer, Integer> linkStaff = new LinkedHashMap<Integer, Integer>();

        ResultSetWrapper rs = con.executeQuery(this.getSelectStaffSQL(shopID));

        while (rs.next()) {
            linkStaff.put(rs.getInt("spos_staff_id"), rs.getInt("staff_id"));
        }
        rs.close();

        return linkStaff;
    }

    private String getSelectStaffSQL(int shopID) {
        return "select * \n"
                + "from mst_kanzashi_staff \n"
                + "where	shop_id = " + SQLUtil.convertForSQL(shopID) + "\n"
                + "and delete_date is null \n";
    }

    public Integer getAllianceReserve(ConnectionWrapper con) throws SQLException {
        if (reservationNo == null || reservationNo < 1) {
            return null;
        }

        if (con == null) {
            return null;
        }

        String sql = "select coalesce(alliance_reserve_id, 0) as alliance_reserve_id \n"
                + "from data_reservation\n"
                + "where	shop_id = " + SQLUtil.convertForSQL(shopID) + "\n"
                + "and	reservation_no = " + SQLUtil.convertForSQL(reservationNo) + "\n";

        ResultSetWrapper rs = con.executeQuery(sql);

        if (rs.next()) {
            if (rs.getInt("alliance_reserve_id") != 0) {
                return rs.getInt("alliance_reserve_id");
            } else {
                return null;
            }
        }
        return null;
    }
    

    class ResponseReservation {

        Integer id = null;
        String date = "";
        String end_time = "";
        String start_time = "";
        Integer status = null;
        Integer salon_id = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getSalon_id() {
            return salon_id;
        }

        public void setSalon_id(Integer salon_id) {
            this.salon_id = salon_id;
        }
    }
}
