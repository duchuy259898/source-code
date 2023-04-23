/*
 * SalesRankingStoreBean.java
 *
 * Created on 2008/09/19, 17:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

/**
 *
 * @author trino
 */
public class SalesRankingStoreBean
{
    /** �X�� */
    private String store;
    /** ���� */
    private Integer rank;
    /** ���� */
    private Long sales;
    /** �X�^�b�t�� */
    private Long staffCount;
    /** �P�l������ */
    private Long salesPerPerson;
    
    /** �R���X�g���N�^ */
    public SalesRankingStoreBean(String store){
        this.store = store;
    }
    
    /** �X�����擾���� */
    public String getStore(){return store;}
    
    /** ���ʂ��擾���� */
    public Integer getRank(){return rank;}
    
    /** ������擾���� */
    public Long getSales(){return sales;}
    
    /** �X�^�b�t�����擾���� */
    public Long getStaffCount() {return staffCount;}

    /** �P�l��������擾���� */
    public Long getSalesPerPerson(){return salesPerPerson;}
    
    /** �X����ݒ肷�� */
    public void setStore(String name){this.store = name;}
    
    /** ���ʂ�ݒ肷�� */
    public void setRank(Integer pos){this.rank = pos;}
    
    /** �����ݒ肷�� */
    public void setSales(Long amount){this.sales = amount;}

    /** �X�^�b�t����ݒ肷�� */
    public void setStaffCount(Long staffCount) {this.staffCount = staffCount;}

    /** �P�l�������ݒ�ł��܂��� */
    public void setSalesPerPerson(Long salesPerPerson){
        this.salesPerPerson = salesPerPerson;
    }
}
