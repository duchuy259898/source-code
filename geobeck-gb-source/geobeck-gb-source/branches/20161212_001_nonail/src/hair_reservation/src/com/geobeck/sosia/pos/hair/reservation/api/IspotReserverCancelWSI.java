/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.reservation.api;

import java.sql.Timestamp;

/**
 *
 * @author ivs
 */
public class IspotReserverCancelWSI {
  private Integer reserve_id;
   private Integer cancel_type;
   private String cancel_date;

    public Integer getReserve_id() {
        return reserve_id;
    }

    public void setReserve_id(Integer reserve_id) {
        this.reserve_id = reserve_id;
    }

    public Integer getCancel_type() {
        return cancel_type;
    }

    public void setCancel_type(Integer cancel_type) {
        this.cancel_type = cancel_type;
    }

    public String getCancel_date() {
        return cancel_date;
    }

    public void setCancel_date(String cancel_date) {
        this.cancel_date = cancel_date;
    }
   
      
}
