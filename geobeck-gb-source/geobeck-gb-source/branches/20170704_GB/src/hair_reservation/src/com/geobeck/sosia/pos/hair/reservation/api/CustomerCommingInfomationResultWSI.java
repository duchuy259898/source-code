/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.reservation.api;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ivs
 */
public class CustomerCommingInfomationResultWSI {

    private String api_version;
    private String return_code;
    private String response_datetime;
    private String results_available;
    private String results_returned;
    private String results_start;
    private SalonInfo salon_info;
    private ArrayList<ComingInfo> coming_info;

    public String getApi_version() {
        return api_version;
    }

    public void setApi_version(String api_version) {
        this.api_version = api_version;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getResponse_datetime() {
        return response_datetime;
    }

    public void setResponse_datetime(String response_datetime) {
        this.response_datetime = response_datetime;
    }

    public String getResults_available() {
        return results_available;
    }

    public void setResults_available(String results_available) {
        this.results_available = results_available;
    }

    public String getResults_returned() {
        return results_returned;
    }

    public void setResults_returned(String results_returned) {
        this.results_returned = results_returned;
    }

    public String getResults_start() {
        return results_start;
    }

    public void setResults_start(String results_start) {
        this.results_start = results_start;
    }

    public SalonInfo getSalon_info() {
        return salon_info;
    }

    public void setSalon_info(SalonInfo salon_info) {
        this.salon_info = salon_info;
    }

    public ArrayList<ComingInfo> getComing_info() {
        return coming_info;
    }

    public void setComing_info(ArrayList<ComingInfo> coming_info) {
        this.coming_info = coming_info;
    }

    public CustomerCommingInfomationResultWSI(String api_version, String return_code, String response_datetime, String results_available, String results_returned, String results_start, SalonInfo salon_info, ArrayList<ComingInfo> coming_info) {
        this.api_version = api_version;
        this.return_code = return_code;
        this.response_datetime = response_datetime;
        this.results_available = results_available;
        this.results_returned = results_returned;
        this.results_start = results_start;
        this.salon_info = salon_info;
        this.coming_info = coming_info;
    }

    public CustomerCommingInfomationResultWSI() {
        this.api_version = "";
        this.return_code = "";
        this.response_datetime = "";
        this.results_available = "";
        this.results_returned = "";
        this.results_start = "";
        this.salon_info = new SalonInfo();
        this.coming_info = new ArrayList<ComingInfo>();
    }

   
   
    
}
