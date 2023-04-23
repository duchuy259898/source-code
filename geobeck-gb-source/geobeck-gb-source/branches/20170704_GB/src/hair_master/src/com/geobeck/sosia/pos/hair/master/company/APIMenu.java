/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.master.company;

import java.util.ArrayList;

/**
 *
 * @author lvtu
 */
public class APIMenu {
    String              category        = "";
    Integer             id              = null;
    boolean             is_disabled     = false;
    String              name            = "";
    String              original_media  = "";
    Integer             price           = null;
    Integer             salon_id        = null;
    String              time            = "";

    ArrayList<APIMedia> media_ids       = new ArrayList<APIMedia>();

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isIs_disabled() {
        return is_disabled;
    }

    public void setIs_disabled(boolean is_disabled) {
        this.is_disabled = is_disabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginal_media() {
        return original_media;
    }

    public void setOriginal_media(String original_media) {
        this.original_media = original_media;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSalon_id() {
        return salon_id;
    }

    public void setSalon_id(Integer salon_id) {
        this.salon_id = salon_id;
    }

    public ArrayList<APIMedia> getMedia_ids() {
        return media_ids;
    }

    public void setMedia_ids(ArrayList<APIMedia> media_ids) {
        this.media_ids = media_ids;
    }
    
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
