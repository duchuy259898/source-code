/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.logic;

/**
 * カスタム帳票
 * @author IVS
 */
public abstract class ReportCustomize {

    final int LIMIT_STAFF   = 15;
    final int LIMIT_SHOP    = 30;
    //年
    protected Integer year;
    //月
    protected Integer month;
    
    public abstract boolean report();
    
    public abstract String getReportSQL();
}
