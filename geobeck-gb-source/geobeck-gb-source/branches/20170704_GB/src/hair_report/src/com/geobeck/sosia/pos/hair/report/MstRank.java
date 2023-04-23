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
public class MstRank
{
    private Integer rankId          = null;
    private String rankName         = null;
    private String color            = null;

    public Integer getRankId() {
        return rankId;
    }

    public void setRankId(Integer rankId) {
        this.rankId = rankId;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    /**
     * ResultSetWrapper����f�[�^���Z�b�g����
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException
    {
            this.setRankId(rs.getInt("rank_id"));
            this.setRankName(rs.getString("rank_name"));
            this.setColor(rs.getString("color"));
    }

    /**
     * ���̐ݒ�.
     * @return true - ����
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
     * ���̐ݒ�
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    public boolean delete(ConnectionWrapper con) throws SQLException
    {
            String	sql		=	"";

            if(isExists(con))
            {
                    sql	=	this.getDeleteSQL();
            }
            else
            {
                    return	false;
            }

            if(con.executeUpdate(sql) != 1)
            {
                    return	false;
            }

            return	true;
    }

    /**
     * ���̐ݒ�
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ���݂���
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException
    {
            if(this.getRankId() == null || this.getRankId()< 1)	return	false;

            if(con == null)	return	false;

            ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

            if(rs.next())	return	true;
            else	return	false;
    }

    /**
     * Select sql by rankId�B
     * @return Select��
     */
    private String getSelectSQL()
    {
            return	"select *\n"
                    +	"from mst_rank\n"
                    +	"where delete_date is null\n"
                    +	"and rank_id = " + SQLUtil.convertForSQL(this.getRankId()) + "\n";
    }

    /**
     * Insert���̐ݒ�B
     * @return Insert��
     */
    private String	getInsertSQL()
    {
            return	"insert into mst_rank\n" +
                            "(rank_id, rank_name, color,\n" +
                            "insert_date, update_date, delete_date)\n" +
                            "values( \n" +
                            "(select\n" +
                            "coalesce(max(rank_id), 0) + 1\n" +
                            "from mst_rank\n), " +
                            SQLUtil.convertForSQL(this.getRankName()) + ",\n" +
                            SQLUtil.convertForSQL(this.getColor()) + ",\n" +
                            "current_timestamp, current_timestamp, null )\n";
    }

    /**
     * Update ���̐ݒ�B
     * @return Update��
     */
    private String	getUpdateSQL()
    {
            return	"update mst_rank\n" +
                            "set\n" +
                            "rank_name = " + SQLUtil.convertForSQL(this.getRankName()) + ",\n" +
                            "color = " + SQLUtil.convertForSQL(this.getColor()) + ",\n" +
                            "update_date = current_timestamp\n" +
                            "where	rank_id = " + SQLUtil.convertForSQL(this.getRankId()) + "\n" +
                            "and delete_date is null";
    }

    /**
     * �폜�pUpdate�����擾����B
     * @return �폜�pUpdate��
     */
    private String	getDeleteSQL()
    {
            return	"update mst_rank\n"
                    +	"set\n"
                    +	"delete_date = current_timestamp\n"
                    +	"where	rank_id = " + SQLUtil.convertForSQL(this.getRankId()) + "\n";
    }
    
    public String toString() {
        
        StringBuffer sb = new StringBuffer();
        
        sb.append(this.rankName);
        
        return sb.toString();
    }
}
