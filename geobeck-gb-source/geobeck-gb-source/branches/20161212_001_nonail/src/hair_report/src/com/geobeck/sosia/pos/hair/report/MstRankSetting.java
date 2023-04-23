/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;

/**
 *
 * @author lvtu
 */
public class MstRankSetting {
    private Integer shopCategoryId          = null;
    private Integer x5y5                    = null;
    private Integer x5y4                    = null;
    private Integer x5y3                    = null;
    private Integer x5y2                    = null;
    private Integer x5y1                    = null;
    private Integer x4y5                    = null;
    private Integer x4y4                    = null;
    private Integer x4y3                    = null;
    private Integer x4y2                    = null;
    private Integer x4y1                    = null;
    private Integer x3y5                    = null;
    private Integer x3y4                    = null;
    private Integer x3y3                    = null;
    private Integer x3y2                    = null;
    private Integer x3y1                    = null;
    private Integer x2y5                    = null;
    private Integer x2y4                    = null;
    private Integer x2y3                    = null;
    private Integer x2y2                    = null;
    private Integer x2y1                    = null;
    private Integer x1y5                    = null;
    private Integer x1y4                    = null;
    private Integer x1y3                    = null;
    private Integer x1y2                    = null;
    private Integer x1y1                    = null;
    private Integer item_x5y2               = null;
    private Integer item_x5y1               = null;
    private Integer item_x4y2               = null;
    private Integer item_x4y1               = null;
    private Integer item_x3y2               = null;
    private Integer item_x3y1               = null;
    private Integer item_x2y2               = null;
    private Integer item_x2y1               = null;
    private Integer item_x1y2               = null;
    private Integer item_x1y1               = null;

    public Integer getShopCategoryId() {
        return shopCategoryId;
    }

    public void setShopCategoryId(Integer shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }

    public Integer getX5y5() {
        return x5y5;
    }

    public void setX5y5(Integer x5y5) {
        this.x5y5 = x5y5;
    }

    public Integer getX5y4() {
        return x5y4;
    }

    public void setX5y4(Integer x5y4) {
        this.x5y4 = x5y4;
    }

    public Integer getX5y3() {
        return x5y3;
    }

    public void setX5y3(Integer x5y3) {
        this.x5y3 = x5y3;
    }

    public Integer getX5y2() {
        return x5y2;
    }

    public void setX5y2(Integer x5y2) {
        this.x5y2 = x5y2;
    }

    public Integer getX5y1() {
        return x5y1;
    }

    public void setX5y1(Integer x5y1) {
        this.x5y1 = x5y1;
    }

    public Integer getX4y5() {
        return x4y5;
    }

    public void setX4y5(Integer x4y5) {
        this.x4y5 = x4y5;
    }

    public Integer getX4y4() {
        return x4y4;
    }

    public void setX4y4(Integer x4y4) {
        this.x4y4 = x4y4;
    }

    public Integer getX4y3() {
        return x4y3;
    }

    public void setX4y3(Integer x4y3) {
        this.x4y3 = x4y3;
    }

    public Integer getX4y2() {
        return x4y2;
    }

    public void setX4y2(Integer x4y2) {
        this.x4y2 = x4y2;
    }

    public Integer getX4y1() {
        return x4y1;
    }

    public void setX4y1(Integer x4y1) {
        this.x4y1 = x4y1;
    }

    public Integer getX3y5() {
        return x3y5;
    }

    public void setX3y5(Integer x3y5) {
        this.x3y5 = x3y5;
    }

    public Integer getX3y4() {
        return x3y4;
    }

    public void setX3y4(Integer x3y4) {
        this.x3y4 = x3y4;
    }

    public Integer getX3y3() {
        return x3y3;
    }

    public void setX3y3(Integer x3y3) {
        this.x3y3 = x3y3;
    }

    public Integer getX3y2() {
        return x3y2;
    }

    public void setX3y2(Integer x3y2) {
        this.x3y2 = x3y2;
    }

    public Integer getX3y1() {
        return x3y1;
    }

    public void setX3y1(Integer x3y1) {
        this.x3y1 = x3y1;
    }

    public Integer getX2y5() {
        return x2y5;
    }

    public void setX2y5(Integer x2y5) {
        this.x2y5 = x2y5;
    }

    public Integer getX2y4() {
        return x2y4;
    }

    public void setX2y4(Integer x2y4) {
        this.x2y4 = x2y4;
    }

    public Integer getX2y3() {
        return x2y3;
    }

    public void setX2y3(Integer x2y3) {
        this.x2y3 = x2y3;
    }

    public Integer getX2y2() {
        return x2y2;
    }

    public void setX2y2(Integer x2y2) {
        this.x2y2 = x2y2;
    }

    public Integer getX2y1() {
        return x2y1;
    }

    public void setX2y1(Integer x2y1) {
        this.x2y1 = x2y1;
    }

    public Integer getX1y5() {
        return x1y5;
    }

    public void setX1y5(Integer x1y5) {
        this.x1y5 = x1y5;
    }

    public Integer getX1y4() {
        return x1y4;
    }

    public void setX1y4(Integer x1y4) {
        this.x1y4 = x1y4;
    }

    public Integer getX1y3() {
        return x1y3;
    }

    public void setX1y3(Integer x1y3) {
        this.x1y3 = x1y3;
    }

    public Integer getX1y2() {
        return x1y2;
    }

    public void setX1y2(Integer x1y2) {
        this.x1y2 = x1y2;
    }

    public Integer getX1y1() {
        return x1y1;
    }

    public void setX1y1(Integer x1y1) {
        this.x1y1 = x1y1;
    }

    public Integer getItem_x5y2() {
        return item_x5y2;
    }

    public void setItem_x5y2(Integer item_x5y2) {
        this.item_x5y2 = item_x5y2;
    }

    public Integer getItem_x5y1() {
        return item_x5y1;
    }

    public void setItem_x5y1(Integer item_x5y1) {
        this.item_x5y1 = item_x5y1;
    }

    public Integer getItem_x4y2() {
        return item_x4y2;
    }

    public void setItem_x4y2(Integer item_x4y2) {
        this.item_x4y2 = item_x4y2;
    }

    public Integer getItem_x4y1() {
        return item_x4y1;
    }

    public void setItem_x4y1(Integer item_x4y1) {
        this.item_x4y1 = item_x4y1;
    }

    public Integer getItem_x3y2() {
        return item_x3y2;
    }

    public void setItem_x3y2(Integer item_x3y2) {
        this.item_x3y2 = item_x3y2;
    }

    public Integer getItem_x3y1() {
        return item_x3y1;
    }

    public void setItem_x3y1(Integer item_x3y1) {
        this.item_x3y1 = item_x3y1;
    }

    public Integer getItem_x2y2() {
        return item_x2y2;
    }

    public void setItem_x2y2(Integer item_x2y2) {
        this.item_x2y2 = item_x2y2;
    }

    public Integer getItem_x2y1() {
        return item_x2y1;
    }

    public void setItem_x2y1(Integer item_x2y1) {
        this.item_x2y1 = item_x2y1;
    }

    public Integer getItem_x1y2() {
        return item_x1y2;
    }

    public void setItem_x1y2(Integer item_x1y2) {
        this.item_x1y2 = item_x1y2;
    }

    public Integer getItem_x1y1() {
        return item_x1y1;
    }

    public void setItem_x1y1(Integer item_x1y1) {
        this.item_x1y1 = item_x1y1;
    }

     /**
     * ResultSetWrapperからデータをセットする
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException
    {
        this.setShopCategoryId(rs.getInt("shop_category_id"));
        this.setItem_x1y1(rs.getInt("item_x1y1"));
        this.setItem_x1y2(rs.getInt("item_x1y2"));
        this.setItem_x2y1(rs.getInt("item_x2y1"));
        this.setItem_x2y2(rs.getInt("item_x2y2"));
        this.setItem_x3y1(rs.getInt("item_x3y1"));
        this.setItem_x3y2(rs.getInt("item_x3y2"));
        this.setItem_x4y1(rs.getInt("item_x4y1"));
        this.setItem_x4y2(rs.getInt("item_x4y2"));
        this.setItem_x5y1(rs.getInt("item_x5y1"));
        this.setItem_x5y2(rs.getInt("item_x5y2"));
        
        this.setX1y1(rs.getInt("x1y1"));
        this.setX1y2(rs.getInt("x1y2"));
        this.setX1y3(rs.getInt("x1y3"));
        this.setX1y4(rs.getInt("x1y4"));
        this.setX1y5(rs.getInt("x1y5"));
        this.setX2y1(rs.getInt("x2y1"));
        this.setX2y2(rs.getInt("x2y2"));
        this.setX2y3(rs.getInt("x2y3"));
        this.setX2y4(rs.getInt("x2y4"));
        this.setX2y5(rs.getInt("x2y5"));
        this.setX3y1(rs.getInt("x3y1"));
        this.setX3y2(rs.getInt("x3y2"));
        this.setX3y3(rs.getInt("x3y3"));
        this.setX3y4(rs.getInt("x3y4"));
        this.setX3y5(rs.getInt("x3y5"));
        this.setX4y1(rs.getInt("x4y1"));
        this.setX4y2(rs.getInt("x4y2"));
        this.setX4y3(rs.getInt("x4y3"));
        this.setX4y4(rs.getInt("x4y4"));
        this.setX4y5(rs.getInt("x4y5"));
        this.setX5y1(rs.getInt("x5y1"));
        this.setX5y2(rs.getInt("x5y2"));
        this.setX5y3(rs.getInt("x5y3"));
        this.setX5y4(rs.getInt("x5y4"));
        this.setX5y5(rs.getInt("x5y5"));
    }

    /**
     * ランク設定。
     * @return true - 成功
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     */
    public boolean regist(ConnectionWrapper con) throws SQLException
    {
            if(isExists(con))
            {
                if(con.executeUpdate(this.getUpdateSQL()) != 1)
                {
                        return	false;
                }
            }
            else
            {
                if(con.executeUpdate(this.getInsertSQL()) != 1)
                {
                        return	false;
                }
            }

            return	true;
    }

    /**
     * ランク設定.
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException
    {
            if(this.getShopCategoryId()== null)	return	false;

            if(con == null)	return	false;

            ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

            if(rs.next())	return	true;
            else	return	false;
    }

    public boolean LoadByShopCategoryId(ConnectionWrapper con) throws SQLException
    {
            if(this.getShopCategoryId()== null)	return	false;

            if(con == null)	return	false;

            ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

            if(rs.next())
            {
                setData(rs);
                return	true;
            }
            else	return	false;
    }
    /**
     * Select sql by shop_category_id。
     * @return Select文
     */
    private String getSelectSQL()
    {
            return	"select *\n"
                    +	"from mst_rank_setting\n"
                    +	"where delete_date is null\n"
                    +	"and shop_category_id = " + SQLUtil.convertForSQL(this.getShopCategoryId()) + "\n";
    }

    /**
     * Insertランク設定。
     * @return Insert文
     */
    private String	getInsertSQL()
    {
            return	"insert into mst_rank_setting\n" +
                            "(shop_category_id,\n" +
                            "x1y1, x1y2, x1y3, x1y4, x1y5,\n" +
                            "x2y1, x2y2, x2y3, x2y4, x2y5,\n" +
                            "x3y1, x3y2, x3y3, x3y4, x3y5,\n" +
                            "x4y1, x4y2, x4y3, x4y4, x4y5,\n" +
                            "x5y1, x5y2, x5y3, x5y4, x5y5,\n" +
                            
                            "item_x5y1, item_x5y2, item_x4y1, item_x4y2, item_x3y1,\n" +
                            "item_x3y2, item_x2y1, item_x2y2, item_x1y1, item_x1y2,\n" +
                            "insert_date, update_date, delete_date)\n" +
                            "values( \n" +
                            SQLUtil.convertForSQL(this.getShopCategoryId()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX1y1()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX1y2()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX1y3()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX1y4()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX1y5()) + ",\n" +
                    
                            SQLUtil.convertForSQL(this.getX2y1()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX2y2()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX2y3()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX2y4()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX2y5()) + ",\n" +
                    
                            SQLUtil.convertForSQL(this.getX3y1()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX3y2()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX3y3()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX3y4()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX3y5()) + ",\n" +
                    
                            SQLUtil.convertForSQL(this.getX4y1()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX4y2()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX4y3()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX4y4()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX4y5()) + ",\n" +
                    
                            SQLUtil.convertForSQL(this.getX5y1()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX5y2()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX5y3()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX5y4()) + ",\n" +
                            SQLUtil.convertForSQL(this.getX5y5()) + ",\n" +
                    
                            SQLUtil.convertForSQL(this.getItem_x5y1()) + ",\n" +
                            SQLUtil.convertForSQL(this.getItem_x5y2()) + ",\n" +
                            SQLUtil.convertForSQL(this.getItem_x4y1()) + ",\n" +
                            SQLUtil.convertForSQL(this.getItem_x4y2()) + ",\n" +
                            SQLUtil.convertForSQL(this.getItem_x3y1()) + ",\n" +
                            SQLUtil.convertForSQL(this.getItem_x3y2()) + ",\n" +
                            SQLUtil.convertForSQL(this.getItem_x2y1()) + ",\n" +
                            SQLUtil.convertForSQL(this.getItem_x2y2()) + ",\n" +
                            SQLUtil.convertForSQL(this.getItem_x1y1()) + ",\n" +
                            SQLUtil.convertForSQL(this.getItem_x1y2()) + ",\n" +
                            
                            "current_timestamp, current_timestamp, null )\n";
    }

    /**
     * Updateランク設定。
     * @return Update文
     */
    private String	getUpdateSQL()
    {
            return	"update mst_rank_setting\n" +
                            "set\n" +
                            "x1y1 =" + SQLUtil.convertForSQL(this.getX1y1()) + ",\n" +
                            "x1y2 =" + SQLUtil.convertForSQL(this.getX1y2()) + ",\n" +
                            "x1y3 =" + SQLUtil.convertForSQL(this.getX1y3()) + ",\n" +
                            "x1y4 =" + SQLUtil.convertForSQL(this.getX1y4()) + ",\n" +
                            "x1y5 =" + SQLUtil.convertForSQL(this.getX1y5()) + ",\n" +
                            "x2y1 =" + SQLUtil.convertForSQL(this.getX2y1()) + ",\n" +
                            "x2y2 =" + SQLUtil.convertForSQL(this.getX2y2()) + ",\n" +
                            "x2y3 =" + SQLUtil.convertForSQL(this.getX2y3()) + ",\n" +
                            "x2y4 =" + SQLUtil.convertForSQL(this.getX2y4()) + ",\n" +
                            "x2y5 =" + SQLUtil.convertForSQL(this.getX2y5()) + ",\n" +
                            "x3y1 =" + SQLUtil.convertForSQL(this.getX3y1()) + ",\n" +
                            "x3y2 =" + SQLUtil.convertForSQL(this.getX3y2()) + ",\n" +
                            "x3y3 =" + SQLUtil.convertForSQL(this.getX3y3()) + ",\n" +
                            "x3y4 =" + SQLUtil.convertForSQL(this.getX3y4()) + ",\n" +
                            "x3y5 =" + SQLUtil.convertForSQL(this.getX3y5()) + ",\n" +
                            "x4y1 =" + SQLUtil.convertForSQL(this.getX4y1()) + ",\n" +
                            "x4y2 =" + SQLUtil.convertForSQL(this.getX4y2()) + ",\n" +
                            "x4y3 =" + SQLUtil.convertForSQL(this.getX4y3()) + ",\n" +
                            "x4y4 =" + SQLUtil.convertForSQL(this.getX4y4()) + ",\n" +
                            "x4y5 =" + SQLUtil.convertForSQL(this.getX4y5()) + ",\n" +
                            "x5y1 =" + SQLUtil.convertForSQL(this.getX5y1()) + ",\n" +
                            "x5y2 =" + SQLUtil.convertForSQL(this.getX5y2()) + ",\n" +
                            "x5y3 =" + SQLUtil.convertForSQL(this.getX5y3()) + ",\n" +
                            "x5y4 =" + SQLUtil.convertForSQL(this.getX5y4()) + ",\n" +
                            "x5y5 =" + SQLUtil.convertForSQL(this.getX5y5()) + ",\n" +
                    
                            "item_x5y1 =" + SQLUtil.convertForSQL(this.getItem_x5y1()) + ",\n" +
                            "item_x5y2 =" + SQLUtil.convertForSQL(this.getItem_x5y2()) + ",\n" +
                            "item_x4y1 =" + SQLUtil.convertForSQL(this.getItem_x4y1()) + ",\n" +
                            "item_x4y2 =" + SQLUtil.convertForSQL(this.getItem_x4y2()) + ",\n" +
                            "item_x3y1 =" + SQLUtil.convertForSQL(this.getItem_x3y1()) + ",\n" +
                            "item_x3y2 =" + SQLUtil.convertForSQL(this.getItem_x3y2()) + ",\n" +
                            "item_x2y1 =" + SQLUtil.convertForSQL(this.getItem_x2y1()) + ",\n" +
                            "item_x2y2 =" + SQLUtil.convertForSQL(this.getItem_x2y2()) + ",\n" +
                            "item_x1y1 =" + SQLUtil.convertForSQL(this.getItem_x1y1()) + ",\n" +
                            "item_x1y2 =" + SQLUtil.convertForSQL(this.getItem_x1y2()) + ",\n" +
                            "update_date = current_timestamp\n" +
                            "where	shop_category_id = " + SQLUtil.convertForSQL(this.getShopCategoryId()) + "\n" +
                            "and delete_date is null";
    }

    /**
     * cover object to array.
     * @param row
     * @param col
     * @return Array.
     */
    public Integer[][] coverToArray(int row, int col)
    {
        Integer [][] arr = new Integer[row][col];
        if(row == 5)
        {
            arr[0][0] =	this.x5y5;
            arr[0][1] =	this.x4y5;
            arr[0][2] =	this.x3y5;
            arr[0][3] =	this.x2y5;
            arr[0][4] =	this.x1y5;
            arr[1][0] =	this.x5y4;
            arr[1][1] =	this.x4y4;
            arr[1][2] =	this.x3y4;
            arr[1][3] =	this.x2y4;
            arr[1][4] =	this.x1y4;
            arr[2][0] =	this.x5y3;
            arr[2][1] =	this.x4y3;
            arr[2][2] =	this.x3y3;
            arr[2][3] =	this.x2y3;
            arr[2][4] =	this.x1y3;
            arr[3][0] =	this.x5y2;
            arr[3][1] =	this.x4y2;
            arr[3][2] =	this.x3y2;
            arr[3][3] =	this.x2y2;
            arr[3][4] =	this.x1y2;
            arr[4][0] =	this.x5y1;
            arr[4][1] =	this.x4y1;
            arr[4][2] =	this.x3y1;
            arr[4][3] =	this.x2y1;
            arr[4][4] =	this.x1y1;
        }
        else
        {
            arr[0][0] = this.item_x5y2;
            arr[0][1] = this.item_x4y2;
            arr[0][2] = this.item_x3y2;
            arr[0][3] = this.item_x2y2;
            arr[0][4] = this.item_x1y2;
                        
            arr[1][0] = this.item_x5y1;
            arr[1][1] = this.item_x4y1;
            arr[1][2] = this.item_x3y1;
            arr[1][3] = this.item_x2y1;
            arr[1][4] = this.item_x1y1;
        }
                
        return arr;
    }
}
