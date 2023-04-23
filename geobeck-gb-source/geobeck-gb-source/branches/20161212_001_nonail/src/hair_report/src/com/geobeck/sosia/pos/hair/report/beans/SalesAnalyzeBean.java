/* SalesAnalyzeBean.java
 * Created on 2008/10/08, 16:17
 */
package com.geobeck.sosia.pos.hair.report.beans;
/** 
 * ���㕪�̓O���t�pBean
 * @author trino
 */
public class SalesAnalyzeBean {
    /** �� */
    private String month;
    /** ���� */
    private Double result = null;
    /** 12�������v */
    private Double sum = Double.NaN;
    /** �ړ��l */
    private Double ave = null;
    /** �O�N�x���� */
    private Double previusResult = Double.NaN;
    
    // <editor-fold desc="�R���X�g���N�^">    
    /** �R���X�g���N�^ */
    public SalesAnalyzeBean() {}

    /** �R���X�g���N�^(���̂ݎw��)
     * @param month ��
     */
    public SalesAnalyzeBean(String month) {this.month = month;}

    /** �R���X�g���N�^(���ƑO�N�x���т��w��)
     * @param month ��
     * @param previusResult �O�N�x����
     */
    public SalesAnalyzeBean(String month, Double previusResult) {
        this.month = month;
        this.previusResult = previusResult;
    }
    // </editor-fold>

    // <editor-fold desc="�Z�b�^�ƃQ�b�^">       
    /** �����擾���� */
    public String getMonth() {return month;}
    
    /** ����ݒ肷�� */
    public void setMonth(String month) {this.month = month;}
    
    /** ���т��擾���� */
    public Double getResult() {return result;}
    
    /** ���т�ݒ肷�� */
    public void setResult(Double result) {this.result = result;}
    
    /** 12�������v���擾���� */
    public Double getSum() {return sum;}
    
    /** 12�������v��ݒ肷�� */
    public void setSum(Double sum) {this.sum = sum;}
    
    /** �ړ��l���擾���� */
    public Double getAve() {return ave;}
    
    /** �ړ��l��ݒ肷�� */
    public void setAve(Double ave) {this.ave = ave;}

    /** �O�N�x���т��擾���� */
    public Double getPreviusResult() {return previusResult;}
    
    /** �O�N�x���т�ݒ肷�� */
    public void setPreviusResult(Double result) {this.previusResult = result;}
    // </editor-fold>  
}