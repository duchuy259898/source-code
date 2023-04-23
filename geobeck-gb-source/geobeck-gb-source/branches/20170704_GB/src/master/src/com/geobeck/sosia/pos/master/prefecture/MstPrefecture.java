/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.master.prefecture;
import java.sql.*;
import java.util.*;
import com.geobeck.sql.*;

/**
 *
 * @author ivs
 */
public class MstPrefecture {
    private String prefecture_code  =  "";
    private String prefecture_name  = "";
     
    public MstPrefecture()
    {
    }
    public void setPrefectCode(String prefecture_code)
    {
	this.prefecture_code = prefecture_code;
    }

    public void setPrefectName(String prefecture_name)
    {
	this.prefecture_name = prefecture_name;
    }
    
    public String getPrefectCode()
    {
	return this.prefecture_code;
    }

    public String getPrefectName()
    {
	return this.prefecture_name ;
    }
    
    public void setData(ResultSetWrapper rs) throws SQLException
    {
        this.setPrefectCode(rs.getString("prefecture_code"));
         this.setPrefectName(rs.getString("prefecture_name"));
    }
    
    /**
	 * Select文を取得する。
	 * @return Select文
	 */
    public String getSelectSQL()
    {   
        String result = new String();
        result = " select * from mst_prefecture ";
        return	result;
    }
    
    /**
	 * データをクリアする。
	 */
	public void clear()
	{
            this.setPrefectCode("");
            this.setPrefectName("");
	}
}
