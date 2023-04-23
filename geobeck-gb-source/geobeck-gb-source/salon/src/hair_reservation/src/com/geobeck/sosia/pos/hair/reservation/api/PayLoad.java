/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.reservation.api;

import com.geobeck.sosia.pos.hair.data.reservation.DataReservation;
import com.geobeck.sosia.pos.hair.data.reservation.DataReservationDetail;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import javax.swing.JComboBox;

/**
 *
 * @author lvtu
 */
public class PayLoad {
    String customer_family_kana                 = "";
    String customer_family_name                 = "";
    String customer_given_kana                  = "";
    String customer_given_name                  = "";
    String date                                 = "";
    String end_time                             = "";
    ArrayList<FacilityTimes> facility_times     = null;
    String media_id                             = "";
    ArrayList<StaffTimes> staff_times            = null;
    String start_time                           = "";
    Integer status                              = 1;

    public PayLoad() {
        facility_times     = new ArrayList<FacilityTimes>();
        staff_times            = new ArrayList<StaffTimes>();
        
        FacilityTimes ft = new FacilityTimes();
        StaffTimes st = new StaffTimes();
        
        facility_times.add(ft);
        staff_times.add(st);
    }
    
    public String getCustomer_family_kana() {
        return customer_family_kana;
    }

    public void setCustomer_family_kana(String customer_family_kana) {
        this.customer_family_kana = customer_family_kana;
    }

    public String getCustomer_family_name() {
        return customer_family_name;
    }

    public void setCustomer_family_name(String customer_family_name) {
        this.customer_family_name = customer_family_name;
    }

    public String getCustomer_given_kana() {
        return customer_given_kana;
    }

    public void setCustomer_given_kana(String customer_given_kana) {
        this.customer_given_kana = customer_given_kana;
    }

    public String getCustomer_given_name() {
        return customer_given_name;
    }

    public void setCustomer_given_name(String customer_given_name) {
        this.customer_given_name = customer_given_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public ArrayList<FacilityTimes> getFacility_times() {
        return facility_times;
    }

    public void setFacility_times(DataReservation reservation, LinkedHashMap<Integer, Integer> mapBed) {
        ArrayList<FacilityTimes> arrFacility = new ArrayList<FacilityTimes>();
        DateFormat df = new SimpleDateFormat("HH:mm");
        
        for (DataReservationDetail detail : reservation) {
            if(detail.getBed() == null || detail.getBed().getBedID() == null || detail.getBed().getBedID() == 0
                    || mapBed.get((detail.getBed().getBedID() == null || detail.getBed().getBedID() == 0) ? -1 : detail.getBed().getBedID()) == null) {
                continue;
            }
            int operationTime = 0 ;
            if(detail.getTechnic() != null && detail.getTechnic().getTechnicID() != null) {
                operationTime = detail.getTechnic().getOperationTime();
            }else if(detail.getCourse() != null && detail.getCourse().getCourseId() != null) {
                operationTime = detail.getCourse().getOperationTime();
            }else if(detail.getConsumptionCourse() != null && detail.getConsumptionCourse().getCourseId() != null) {
                operationTime = detail.getConsumptionCourse().getOperationTime();
            }
            
            operationTime = operationTime + detail.getReservationDatetime().getTime().getMinutes();
            GregorianCalendar cal = (GregorianCalendar) detail.getReservationDatetime().clone();
            cal.set(Calendar.MINUTE, operationTime);
            
            FacilityTimes factility = new FacilityTimes();
            factility.start_time = df.format(detail.getReservationDatetime().getTime());
            factility.end_time = df.format(cal.getTime());
            factility.facility_id = mapBed.get((detail.getBed().getBedID() == null || detail.getBed().getBedID() == 0) ? -1 : detail.getBed().getBedID());
            
            System.err.println("start_time : " + factility.start_time);
            System.err.println("end_time : " + factility.end_time);
            
            arrFacility.add(factility);
        }
        if(arrFacility.isEmpty()) {
            //arrFacility.add(new FacilityTimes());
        }
                
        this.facility_times = arrFacility;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public ArrayList<StaffTimes> getStaff_times() {
        return staff_times;
    }

    public void setStaff_times(DataReservation reservation, LinkedHashMap<Integer, Integer> mapStaff) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        ArrayList<StaffTimes> arrstaffTime = new ArrayList<StaffTimes>();
        for (DataReservationDetail detail : reservation) {
            if(detail.getStaff() == null || detail.getStaff().getStaffID() == null || detail.getStaff().getStaffID() == 0
                    || mapStaff.get((detail.getStaff().getStaffID() == null || detail.getStaff().getStaffID() == 0) ? -1 : detail.getStaff().getStaffID()) == null) {
                continue;
            }
            
            int operationTime = 0 ;
            if(detail.getTechnic() != null && detail.getTechnic().getTechnicID() != null) {
                operationTime = detail.getTechnic().getOperationTime();
            }else if(detail.getCourse() != null && detail.getCourse().getCourseId() != null) {
                operationTime = detail.getCourse().getOperationTime();
            }else if(detail.getConsumptionCourse() != null && detail.getConsumptionCourse().getCourseId() != null) {
                operationTime = detail.getConsumptionCourse().getOperationTime();
            }
            
            operationTime = operationTime + detail.getReservationDatetime().getTime().getMinutes();
            
            GregorianCalendar cal = (GregorianCalendar) detail.getReservationDatetime().clone();
            cal.set(Calendar.MINUTE, operationTime);
            
            StaffTimes staffTime = new StaffTimes();
            staffTime.start_time = df.format(detail.getReservationDatetime().getTime());
            staffTime.end_time = df.format(cal.getTime());
            staffTime.is_appointed = detail.getDesignated();
            staffTime.staff_id = mapStaff.get((detail.getStaff().getStaffID() == null || detail.getStaff().getStaffID() == 0) ? -1 : detail.getStaff().getStaffID());
            
            System.err.println("start_time : " + staffTime.start_time);
            System.err.println("end_time : " + staffTime.end_time);
            
            arrstaffTime.add(staffTime);
        }
        if(arrstaffTime.isEmpty()) {
            //arrstaffTime.add(new StaffTimes());
        }
        
        this.staff_times = arrstaffTime;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    
    class FacilityTimes {
        String end_time         = "";
        Integer facility_id     = null;
        String start_time       = "";
        
         public FacilityTimes (){
        }
    }
    
    class StaffTimes {
        String end_time         = "";
        Boolean is_appointed    = false; 
        Integer staff_id        = null;
        String start_time       = "";
        
        public StaffTimes () {
        }
    }
}
