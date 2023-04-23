/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.master.company;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lvtu
 */
public class MstAPI extends ArrayList<MstAPIDetail>{
    
    final int           TYPE_STAFF               = 1;
    final int           TYPE_BED                 = 2;
    final int           TYPE_SHIFT               = 3;
    final int           TYPE_MENU                = 4;
    //nhtvinh start add 20161021 New request #54239
    final int           TYPE_RESERVATION         = 5;
    final int           TYPE_USERMEDIA           = 6; //nami add 20170828 #24239
    String              API_RESERVATION_INFO       = "";
    Integer             salonStatus;
    //nhtvinh end add 20161021 New request #54239
    String              API_USERMEDIA_INFO  = "";  //nami add 20170828 #24239
    String              API_STAFF_INFO      = "";
    String              API_BED_INFO        = "";
    String              API_SHIFT_INFO      = "";
    String              API_MENU_INFO       = "";
    Integer             apiID               = SystemInfo.getUserAPI();
    String              apiName             = "";
    String              apiUrl              = "";
    String              apiAuthCode         = "";
    Integer             salonID             = SystemInfo.getApiSalonID();
    
    APIStaffs           listStaff           = new APIStaffs();
    
    APIBeds             listBed             = new APIBeds();
    
    APIShifts           listShift           = new APIShifts();
    
    APIMenus            listMenu            = new APIMenus();
    
    APIUserMedias       listUserMedia       = new APIUserMedias(); //nami add 20170828 #24239
    

    public APIMenus getListMenu() {
        return listMenu;
    }

    public void setListMenu(APIMenus listMenu) {
        this.listMenu = listMenu;
    }

    public APIShifts getListShift() {
        return listShift;
    }

    public void setListShift(APIShifts listShift) {
        this.listShift = listShift;
    }

    public APIBeds getListBed() {
        return listBed;
    }

    public void setListBed(APIBeds listBed) {
        this.listBed = listBed;
    }

    public Integer getSalonID() {
        return salonID;
    }

    public void setSalonID(Integer salonID) {
        this.salonID = salonID;
    }

    public APIStaffs getListStaff() {
        return listStaff;
    }

    public void setListStaff(APIStaffs listStaff) {
        this.listStaff = listStaff;
    }
    
    /**
     * 20170828 add #24239
     * @return 
     */
    public APIUserMedias getListUserMedia () {
        return listUserMedia;
    }
    
    /**
     * 20170828 add #24239
     * @param listUserMedia 
     */
    public void setListUserMedia (APIUserMedias listUserMedia) {
        this.listUserMedia = listUserMedia;
    }

    public MstAPI() {
        //set path
        API_STAFF_INFO          = "/salon/" + this.salonID + "/staff";
        API_BED_INFO            = "/salon/" + this.salonID + "/facility";
        API_SHIFT_INFO          = "/salon/" + this.salonID + "/work-pattern";
        API_MENU_INFO           = "/salon/" + this.salonID + "/menu";
        //nhtvinh start add 20161021 New request #54239
        API_RESERVATION_INFO    = "/salon/" + this.salonID;
        //nhtvinh start end 20161021 New request #54239
        //nami start add 20170828 #24239
        API_USERMEDIA_INFO      = "/salon/" + this.salonID + "/user-media";
        //nami end add 20170828 #24239
        
        ConnectionWrapper con = SystemInfo.getBaseConnection();
        try {
            this.load(con);
        } catch (SQLException ex) {
            Logger.getLogger(MstAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public MstAPI(int apiID) {
        
        this.apiID = apiID;
        ConnectionWrapper con = SystemInfo.getBaseConnection();
        try {
            this.load(con);
        } catch (SQLException ex) {
            Logger.getLogger(MstAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    
    public Integer getApiID() {
        return apiID;
    }

    public void setApiID(Integer apiID) {
        this.apiID = apiID;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
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
    
    //nhtvinh start add 20161021 New request #54239
    public void loadSalonStatus() {
        String url = "";
        
        if(this.apiUrl.equals("") || this.apiAuthCode.equals("")) {
            return;
        }
        
        url = this.apiUrl + this.API_RESERVATION_INFO;
        getDataListAPI( url , this.TYPE_RESERVATION);
    }
    
    public Integer getSalonStatus(){
        return salonStatus;
    }
    //nhtvinh end add 20161021 New request #54239
    
    public APIStaffs getStaffListAPI() {
        String url = "";
        
        if(this.apiUrl.equals("") || this.apiAuthCode.equals("")) {
            return null;
        }
        
        url = this.apiUrl + this.API_STAFF_INFO;
        getDataListAPI( url , this.TYPE_STAFF);
        
        return listStaff;
    } 
    
    public APIBeds getBedListAPI() {
        String url = "";
        
        if(this.apiUrl.equals("") || this.apiAuthCode.equals("")) {
            return null;
        }
        
        url = this.apiUrl + this.API_BED_INFO;
        getDataListAPI( url, this.TYPE_BED );
        
        return listBed;
    } 
    
    public APIShifts getShiftListAPI() {
        String url = "";

        if(this.apiUrl.equals("") || this.apiAuthCode.equals("")) {
            return null;
        }

        url = this.apiUrl + this.API_SHIFT_INFO;
        getDataListAPI( url, this.TYPE_SHIFT );

        return listShift;
    } 
    
    public APIMenus getMenuListAPI() {
        String url = "";

        if(this.apiUrl.equals("") || this.apiAuthCode.equals("")) {
            return null;
        }

        url = this.apiUrl + this.API_MENU_INFO;
        getDataListAPI( url, this.TYPE_MENU );

        return listMenu;
    }

    /**
     * nami add 20170828 #24239
     * @return 
     */
    public APIUserMedias getUserMediaAPI () {
        String url = "";

        if(this.apiUrl.equals("") || this.apiAuthCode.equals("")) {
            return null;
        }

        url = this.apiUrl + this.API_USERMEDIA_INFO;
        getDataListAPI( url, this.TYPE_USERMEDIA );

        return listUserMedia;
    }
    
    public void getDataListAPI(String url, int type) {
        try {
            BufferedReader reader = null;
            StringBuilder stringBuilder;

            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", this.apiAuthCode);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            
            // read the output from the server
            int status = conn.getResponseCode();
            if(status == 404) {
                return;
            }
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
              stringBuilder.append(line + "\n");
            }
            //System.out.println("json : " + stringBuilder);
            
            
            Gson gson = new Gson();
             
            if(type == this.TYPE_STAFF) {
                listStaff = gson.fromJson(stringBuilder.toString(), APIStaffs.class);
            }else if(type == this.TYPE_BED) {
                listBed = gson.fromJson(stringBuilder.toString(), APIBeds.class);
            }else if(type == this.TYPE_SHIFT) {
                listShift = gson.fromJson(stringBuilder.toString(), APIShifts.class);
            }else if(type == this.TYPE_MENU) {
                listMenu = gson.fromJson(stringBuilder.toString(), APIMenus.class);
            //nhtvinh start add 20161021 New request #54239
            }else if(type == this.TYPE_RESERVATION) {
                salonStatus = getSalonStatusResponse(stringBuilder.toString());   
            }
            //nhtvinh end add 20161021 New request #54239
            //nami start add 20170828 #24239
            else if (type == this.TYPE_USERMEDIA) {
               //setUserMedias(stringBuilder.toString());
                listUserMedia = gson.fromJson(stringBuilder.toString(), APIUserMedias.class);
            }
            //nami end add 20170828 #24239

        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
    
     //nhtvinh start add 20161021 New request #54239
    /**
     * get status from api response
     * parser json response to object, then get status field
     * @param jsonResponse
     * @return 
     */
    private Integer getSalonStatusResponse(String jsonResponse){
        try{
            JsonParser parser = new JsonParser();
            JsonObject objectResponse = parser.parse(jsonResponse).getAsJsonObject();
            if(null != objectResponse){
                JsonElement statusElement = objectResponse.get("status");
                String status = statusElement.getAsString();
                return Integer.parseInt(status);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
     //nhtvinh start end 20161021 New request #54239
    
   
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.apiID = rs.getInt("api_id");
        this.apiName = rs.getString("api_name");
        this.apiUrl = rs.getString("api_url");
        this.apiAuthCode = rs.getString("api_auth_code");
    }


    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getApiID()== null) {
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

    private void load(ConnectionWrapper con) throws SQLException
    {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
        
        if(rs.next()) {
            this.setData(rs);
        }
        rs.close();
    }
    
    public void loadDetail(ConnectionWrapper con) throws SQLException
    {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(this.getSelectDetailSQL());
        
        if(rs.next()) {
            this.setData(rs);
        }
        
        rs.beforeFirst();
        while (rs.next()) {
            MstAPIDetail api = new MstAPIDetail();
            api.setData(rs);
            this.add(api);
        }

        rs.close();
    }

    private String getSelectSQL() {
        return "select * \n"
                + "from mst_api \n"
                + "where api_id = " + SQLUtil.convertForSQL(this.apiID) + "\n";
    }
    
    private String getSelectDetailSQL() {
        return "select * \n"
                + "from mst_api ma\n"
                + " inner join mst_api_detail mad on mad.api_id = ma.api_id \n"
                + " where ma.api_id = " + SQLUtil.convertForSQL(this.apiID) + "\n"
                + " order by display_seq  \n";
        
    }
    
    //IVS_NHTVINH start add 2016/09/07 New request #54487
        /**
         * send loginId,reservation date to reservation_all API to register
         * then check response , if success return true, else return false
         * @return 
         */
    public Boolean sendRegistReservationAllApi(String loginId, Date reservationDate){
        try{
            MstAPI mstApi = new MstAPI(0);
            String apiUrl = mstApi.getApiUrl();
            String url = apiUrl + "/s/send/reservation_all.php";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
            con.setRequestProperty( "charset", "utf-8");

            Format formatter = new SimpleDateFormat("yyyy/MM/dd");
            String dateString = formatter.format(reservationDate);
            //set parameter
            Map mapParam = new LinkedHashMap();
            mapParam.put("login_id", loginId);
            mapParam.put("reservation_datetime", dateString);
            Gson gson = new Gson(); 
            String jsonParam = gson.toJson(mapParam); 
            String urlParameters = "param=" + jsonParam;

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            //get response
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            in.close();
            if(response.toString().contains("\"code\":200")){
                return true;
            }

        }catch(Exception e){
            return false;
        }
        return false;
    }
    
}
