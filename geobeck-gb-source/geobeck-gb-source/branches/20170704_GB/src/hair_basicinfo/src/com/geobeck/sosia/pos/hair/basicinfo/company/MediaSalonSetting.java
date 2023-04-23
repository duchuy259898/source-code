/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.basicinfo.company;

import com.geobeck.sosia.pos.hair.master.company.MstAPI;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import org.apache.commons.collections.map.LinkedMap;

/**
 *
 * @author lvtu
 */
public class MediaSalonSetting {
    // Varialble HttpResponse 0
    private final int CONSTANT_HTTP_RESPONSE = 0;
    // https://relax-api-stg.pp-dev.org/salon
    String              url         = ""; 
    //header 'Authorization: Basic c29zaWE6dTJVTG9YVW90SFJwd1hQcVYzSTVwNnBySnM='
    String              apiAuthCode         = "";
    //{SalonID}ÇÕmst_user.api_salon_id ÇégópÇ∑ÇÈÅB
    Integer             salonID             = SystemInfo.getApiSalonID();
    //éÊìæåãâ ÇîªíË
    Integer     status      = null;
    
    MstAPI      api         = new MstAPI();

    public MediaSalonSetting() {
        this.url = this.api.getApiUrl() + "/salon/" + this.salonID;
        this.apiAuthCode = this.api.getApiAuthCode();
    }
    
    public Integer getSalonID() {
        return salonID;
    }

    public void setSalonID(Integer salonID) {
        this.salonID = salonID;
    }
    
    public String getApiAuthCode() {
        return apiAuthCode;
    }

    public void setApiAuthCode(String apiAuthCode) {
        this.apiAuthCode = apiAuthCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    /**
     * get data from API and response data API
     */
    public void getSalonAPI(){
        ResponseStatus response = new ResponseStatus();
        Gson gson = new Gson();
        BufferedReader reader = null;
        StringBuilder stringBuilder;
        SystemInfo.getLogger().log(java.util.logging.Level.INFO, "start method: getSalonAPI class: MediaSalonSetting");
        try {
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", this.apiAuthCode);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
              stringBuilder.append(line + "\n");
            }

            response = gson.fromJson(stringBuilder.toString(), ResponseStatus.class);
            this.status = response.getStatus();
            SystemInfo.getLogger().log(java.util.logging.Level.INFO, conn.getResponseMessage());
        } catch (MalformedURLException ex) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, ex.getMessage());
        } catch (IOException ex) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, ex.getMessage());
        } catch (Exception ex) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, ex.getMessage());
        }finally {
            SystemInfo.getLogger().log(java.util.logging.Level.INFO, "end method: getSalonAPI class: MediaSalonSetting");
        }
    } 
    
    /**
     * push data API
     * @param status
     * @return code
     */
    public int pushSalonAPI(Integer status) {
        
        int HttpResult = CONSTANT_HTTP_RESPONSE;
        Gson gson = new Gson();
        SystemInfo.getLogger().log(java.util.logging.Level.INFO, "start method: pushSalonAPI class: MediaSalonSetting");
        try {
            URL object = new URL(url);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/vnd.reservation+json");
            con.setRequestProperty("Authorization", this.getApiAuthCode());
            con.setRequestProperty("charset", "utf-8");
            con.setDoOutput(true);
            con.setDoInput(true);

            Map mapParam = new LinkedMap();
            mapParam.put("status", status);

            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(gson.toJson(mapParam));
            writer.flush();

            HttpResult = con.getResponseCode();
           SystemInfo.getLogger().log(java.util.logging.Level.INFO, con.getResponseMessage());
        } catch (MalformedURLException ex) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, ex.getMessage());
        } catch (IOException ex) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, ex.getMessage());
        } catch (Exception ex) {
            SystemInfo.getLogger().log(java.util.logging.Level.SEVERE, ex.getMessage());
        }finally {
            SystemInfo.getLogger().log(java.util.logging.Level.INFO, "end method: pushSalonAPI class: MediaSalonSetting");
        }
        
        return HttpResult;
    }
}

class ResponseStatus {

    private Integer status;

    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
}
