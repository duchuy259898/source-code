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
public class APIBed  implements Comparable<APIBed>{
    Integer     acceptable_count    = null;
    String      created_at          = "";
    Integer     id                  = null;
    boolean     is_disabled         = false;    
    String      name                = "";
    Integer     order               = null;
    String     original_media       = "";
    Integer     priority            = null;
    Integer     salon_id            = null;
    ArrayList<APIMedia> media_ids   = new ArrayList<APIMedia>();

    public Integer getAcceptable_count() {
        return acceptable_count;
    }

    public void setAcceptable_count(Integer acceptable_count) {
        this.acceptable_count = acceptable_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getOriginal_media() {
        return original_media;
    }

    public void setOriginal_media(String original_media) {
        this.original_media = original_media;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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
    
    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(APIBed t) {
        return t.isIs_disabled() == false ? 1 : -1;
    }
}
