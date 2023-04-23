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
public class APIStaff  implements Comparable<APIStaff>{
    Integer     acceptable_count    = null;
    String      created_at          = "";
    String      gender              = "";
    Integer     id                  = null;
    boolean     is_disabled         = false;
    String      kana                = "";
    String      name                = "";
    Integer     order               = null;
    Integer     reservation_fee     = null;
    Integer     salon_id            = null;
    ArrayList<APIMedia> media_ids   = new ArrayList<APIMedia>();

    public ArrayList<APIMedia> getMedia_ids() {
        return media_ids;
    }

    public void setMedia_ids(ArrayList<APIMedia> media_ids) {
        this.media_ids = media_ids;
    }

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getKana() {
        return kana;
    }

    public void setKana(String kana) {
        this.kana = kana;
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

    public Integer getReservation_fee() {
        return reservation_fee;
    }

    public void setReservation_fee(Integer reservation_fee) {
        this.reservation_fee = reservation_fee;
    }

    public Integer getSalon_id() {
        return salon_id;
    }

    public void setSalon_id(Integer salon_id) {
        this.salon_id = salon_id;
    }
    
    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(APIStaff t) {
        return t.isIs_disabled() == false ? 1 : -1;
    }
}
