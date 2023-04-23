/*
 * ReappearanceReportBean.java
 *
 * Created on 2008/07/21, 21:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.report.bean;

/**
 *
 * @author Administrator
 */
public class ReappearanceReportBean {
    
    private String age;
    private String technic;
    
    private int targetCount1;
    private int reappearanceCount1;
    
    private int total;
    private int reappearance;
    private int new_total;
    private int new_reappearance;
    private int sub_fixed_total;
    private int sub_fixed_reappearance;
    private int fixed_total;
    private int fixed_reappearance;   
    private String response_name;
    private int response_id;
    private int sex;

    private int targetCount2;
    private int reappearanceCount2;

    private int targetCount3;
    private int reappearanceCount3;
    
    public void setAge(String a){
        age = a;
    }
    
    public String getAge(){
        return age;
    }
    
    public void setTechnic(String t){
        this.technic = t;
    }
    
    public String getTechnic(){
        return this.technic;
    }
    
    public void setTargetCount1(int c){
        targetCount1 = c;
    }
    
    public int getTargetCount1(){
        return targetCount1;
    }
    
    public void setTargetCount2(int c){
        targetCount2 = c;
    }
    
    public int getTargetCount2(){
        return targetCount2;
    }
        
   public void setTargetCount3(int c){
        targetCount3 = c;
    }
    
    public int getTargetCount3(){
        return targetCount3;
    }
        
    public void setReappearanceCount1(int c){
        reappearanceCount1 = c;
    }
    
    public int getReappearanceCount1(){
        return reappearanceCount1;
    }
    
            
    public void setReappearanceCount2(int c){
        reappearanceCount2 = c;
    }
    
    public int getReappearanceCount2(){
        return reappearanceCount2;
    }
    
    public void setReappearanceCount3(int c){
        reappearanceCount3 = c;
    }
    
    public int getReappearanceCount3(){
        return reappearanceCount3;
    }
    public int getTotal() {
        return total;
    }

    public void setTotal(int c) {
        this.total = c;
    }

    public int getReappearance() {
        return reappearance;
    }

    public void setReappearance(int c) {
        this.reappearance = c;
    }

    public int getNew_total() {
        return new_total;
    }

    public void setNew_total(int c) {
        this.new_total = c;
    }

    public int getNew_reappearance() {
        return new_reappearance;
    }

    public void setNew_reappearance(int c) {
        this.new_reappearance = c;
    }

    public int getSub_fixed_total() {
        return sub_fixed_total;
    }

    public void setSub_fixed_total(int c) {
        this.sub_fixed_total = c;
    }

    public int getSub_fixed_reappearance() {
        return sub_fixed_reappearance;
    }

    public void setSub_fixed_reappearance(int c) {
        this.sub_fixed_reappearance = c;
    }

    public int getFixed_total() {
        return fixed_total;
    }

    public void setFixed_total(int c) {
        this.fixed_total = c;
    }

    public int getFixed_reappearance() {
        return fixed_reappearance;
    }

    public void setFixed_reappearance(int c) {
        this.fixed_reappearance = c;
    }
    public String getResponse_name() {
        return response_name;
    }

    public void setResponse_name(String r) {
        this.response_name = r;
    }
     public int getResponse_id() {
        return response_id;
    }

    public void setResponse_id(int response_id) {
        this.response_id = response_id;
    }
    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
