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
public class APIShift  implements Comparable<APIShift>{

    Integer         id              = null;
    String          name            = "";
    String          short_name      = "";
    String          created_at      = "";
    String          start_time      = "";
    String          end_time        = "";
    boolean         is_deleted      = false;
    boolean         non_work_day    = false;
    String          original_media  = "";
    Integer         salon_id        = null;
    boolean         work_all_day    = false;
    
    ArrayList<APIMedia> media_ids   = new ArrayList<APIMedia>();

    public ArrayList<APIMedia> getMedia_ids() {
        return media_ids;
    }

    public void setMedia_ids(ArrayList<APIMedia> media_ids) {
        this.media_ids = media_ids;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public boolean isNon_work_day() {
        return non_work_day;
    }

    public void setNon_work_day(boolean non_work_day) {
        this.non_work_day = non_work_day;
    }

    public String getOriginal_media() {
        return original_media;
    }

    public void setOriginal_media(String original_media) {
        this.original_media = original_media;
    }

    public Integer getSalon_id() {
        return salon_id;
    }

    public void setSalon_id(Integer salon_id) {
        this.salon_id = salon_id;
    }

    public boolean isWork_all_day() {
        return work_all_day;
    }

    public void setWork_all_day(boolean work_all_day) {
        this.work_all_day = work_all_day;
    }
    
    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(APIShift t) {
        return t.isIs_deleted() == false ? 1 : -1;
    }

}
