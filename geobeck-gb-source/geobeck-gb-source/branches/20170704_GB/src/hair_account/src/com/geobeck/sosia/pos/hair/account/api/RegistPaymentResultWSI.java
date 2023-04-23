/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.account.api;

import java.util.ArrayList;

/**
 *
 * @author lvut
 */
public class RegistPaymentResultWSI {

    private String api_version;
    private String return_code;
    private String response_datetime;
    private ArrayList<String> message;

    public String getApi_version() {
        return api_version;
    }

    public void setApi_version(String api_version) {
        this.api_version = api_version;
    }

    public ArrayList<String>getMessage() {
        return message;
    }

    public void setMessage(ArrayList<String> message) {
        this.message = message;
    }

    public String getResponse_datetime() {
        return response_datetime;
    }

    public void setResponse_datetime(String response_datetime) {
        this.response_datetime = response_datetime;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public RegistPaymentResultWSI(String api_version, String return_code, String response_datetime, ArrayList<String> message) {
        this.api_version = api_version;
        this.return_code = return_code;
        this.response_datetime = response_datetime;
        this.message = message;
    }
    
   
   
    
}
