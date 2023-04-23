/*
 * MstVisitKarte.java
 *
 * Created on 2017/03/29, 12:00
 */

package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sql.ResultSetWrapper;
import java.sql.SQLException;

/***
 * 来店カルテ設定画面用のデータ格納クラス
 * @author GB
 * 
 */
public class MstVisitKarte {
    
    private     Integer     flag               =   0;      // 表示・必須フラグ
    private     String      name               =   "";     // 入力画面表示名
    
    public MstVisitKarte() {}
    
    public Integer getFlag() {
        return flag;
    }
    
    public void setFlag(Integer flag) {
        this.flag = flag;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setData(ResultSetWrapper rs, String flag, String name, Integer i) throws SQLException {
	    this.setFlag(rs.getInt(flag));
	    this.setName(rs.getString(name));
    }
    
    
}
