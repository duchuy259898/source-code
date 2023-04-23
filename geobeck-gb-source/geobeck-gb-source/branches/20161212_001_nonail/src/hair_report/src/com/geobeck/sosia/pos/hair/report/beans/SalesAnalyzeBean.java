/* SalesAnalyzeBean.java
 * Created on 2008/10/08, 16:17
 */
package com.geobeck.sosia.pos.hair.report.beans;
/** 
 * 売上分析グラフ用Bean
 * @author trino
 */
public class SalesAnalyzeBean {
    /** 月 */
    private String month;
    /** 実績 */
    private Double result = null;
    /** 12ヶ月合計 */
    private Double sum = Double.NaN;
    /** 移動値 */
    private Double ave = null;
    /** 前年度実績 */
    private Double previusResult = Double.NaN;
    
    // <editor-fold desc="コンストラクタ">    
    /** コンストラクタ */
    public SalesAnalyzeBean() {}

    /** コンストラクタ(月のみ指定)
     * @param month 月
     */
    public SalesAnalyzeBean(String month) {this.month = month;}

    /** コンストラクタ(月と前年度実績を指定)
     * @param month 月
     * @param previusResult 前年度実績
     */
    public SalesAnalyzeBean(String month, Double previusResult) {
        this.month = month;
        this.previusResult = previusResult;
    }
    // </editor-fold>

    // <editor-fold desc="セッタとゲッタ">       
    /** 月を取得する */
    public String getMonth() {return month;}
    
    /** 月を設定する */
    public void setMonth(String month) {this.month = month;}
    
    /** 実績を取得する */
    public Double getResult() {return result;}
    
    /** 実績を設定する */
    public void setResult(Double result) {this.result = result;}
    
    /** 12ヶ月合計を取得する */
    public Double getSum() {return sum;}
    
    /** 12ヶ月合計を設定する */
    public void setSum(Double sum) {this.sum = sum;}
    
    /** 移動値を取得する */
    public Double getAve() {return ave;}
    
    /** 移動値を設定する */
    public void setAve(Double ave) {this.ave = ave;}

    /** 前年度実績を取得する */
    public Double getPreviusResult() {return previusResult;}
    
    /** 前年度実績を設定する */
    public void setPreviusResult(Double result) {this.previusResult = result;}
    // </editor-fold>  
}