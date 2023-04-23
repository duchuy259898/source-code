/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.reservation.api;

/**
 *
 * @author ivs
 */
public class SalonInfo {

   private String pos_salon_id;
   
   private String salon_name;

    public String getPosSalonId() {
        return pos_salon_id;
    }

    public void setPosSalonId(String posSalonId) {
        this.pos_salon_id = posSalonId;
    }

    public String getSalonName() {
        return salon_name;
    }
    public void setSalonName(String salonName) {
        this.salon_name = salonName;
    }

    public SalonInfo() {
         this.pos_salon_id = "";
        this.salon_name = "";
    }

    public SalonInfo(String pos_salon_id, String salon_name) {
        this.pos_salon_id = pos_salon_id;
        this.salon_name = salon_name;
    }
    

}
