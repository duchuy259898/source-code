/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.company;

/**
 *
 * @author nami
 * 20170828 #24239
 */
public class APIUserMedia {
    
    String  created_at     = "";
    Boolean is_suspended   = false;
    String  media          = "";
    Integer salon_id       = 0;
    String  store_id       = "";
    String  updated_at     = "";
    String  user_id        = "";
    
    public String getCreated_at () {
        return this.created_at;
    }
    
    public void setCreated_at (String created_at) {
        this.created_at = created_at;
    }
    
    public Boolean getIs_suspended () {
        return this.is_suspended;
    }
    
    public void setIs_suspended (Boolean is_suspended) {
        this.is_suspended = is_suspended;
    }
    
    public String getMedia () {
        return this.media;
    }
    
    public void setMedia (String media) {
        this.media = media;
    }
    
    public Integer getSalon_id () {
        return this.salon_id;
    }
    
    public void setSalon_id (Integer salon_id) {
        this.salon_id = salon_id;
    }
    
    public String getStore_id () {
        return this.store_id;
    }
    
    public void setStore_id (String store_id) {
        this.store_id = store_id;
    }
    
    public String getUpdated_at () {
        return this.updated_at;
    }
    
    public void setUpdated_at (String updated_at) {
        this.updated_at = updated_at;
    }
    
    public String getUser_id () {
        return user_id;
    }
    
    public void setUser_id (String user_id) {
        this.user_id = user_id;
    }
}
