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
public class MstRankItemGroup {
    
    private Integer itemGroupId         = null;
    private String  itemGroupName       = "";

    public Integer getItemGroupId() {
        return itemGroupId;
    }

    public void setItemGroupId(Integer itemGroupId) {
        this.itemGroupId = itemGroupId;
    }

    public String getItemGroupName() {
        return itemGroupName;
    }

    public void setItemGroupName(String itemGroupName) {
        this.itemGroupName = itemGroupName;
    }
    
     @Override
    public String toString() {
        return itemGroupName;
    }
    /**
     * ResultSetWrapper����f�[�^���Z�b�g����
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException
    {
            this.setItemGroupId(rs.getInt("item_group_id"));
            this.setItemGroupName(rs.getString("item_group_name"));
    }

    /**
     *���i�f�ݒ�
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
     * ���i�f�ݒ�
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
     * ���i�f�ݒ�
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    public boolean deleteRankItemDetail(ConnectionWrapper con) throws SQLException
    {
            String	sql		=	"";

            if(isExistsRankItemDetail(con))
            {
                    sql	=	this.getDeleteRankRankItemDetailByIdSQL();
            }
            else
            {
                    return	true;
            }

            if(con.executeUpdate(sql) < 1)
            {
                    return	false;
            }

            return	true;
    }
    /**
     * ���i�f�ݒ�
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ���݂���
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException
    {
            if(this.getItemGroupId()== null || this.getItemGroupId()< 1)	return	false;

            if(con == null)	return	false;

            ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

            if(rs.next())	return	true;
            else	return	false;
    }
    
    /**
     * ���i�f�ݒ�
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true
     */
    public boolean isExistsRankItemDetail(ConnectionWrapper con) throws SQLException
    {
            if(this.getItemGroupId()== null || this.getItemGroupId()< 1)	return	false;

            if(con == null)	return	false;

            ResultSetWrapper	rs	=	con.executeQuery(this.getSelectRankItemDetailSQL());

            if(rs.next())	return	true;
            else	return	false;
    }
    /**
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     */
    public boolean selectDataByShopCategoryId(ConnectionWrapper con, Integer shopCategoryId) throws SQLException
    {
            if(shopCategoryId == null )	return	false;

            if(con == null)	return	false;

            ResultSetWrapper	rs	=	con.executeQuery(this.getRankAdvancedSettingSQL(shopCategoryId));

            if(rs.next())
            {
                setData(rs);
                return	true;
            }
            else	return	false;
    }
    
    /**
     * Select sql by item_group_id�B
     * @return Select��
     */
    private String getSelectSQL()
    {
            return	"select *\n"
                    +	"from mst_rank_item_group\n"
                    +	"where delete_date is null\n"
                    +	"and item_group_id = " + SQLUtil.convertForSQL(this.getItemGroupId()) + "\n";
    }

    /**
     * Select sql by item_group_id�B
     * @return Select��
     */
    private String getSelectRankItemDetailSQL()
    {
            return	"select *\n"
                    +	"from mst_rank_item_detail\n"
                    +	"where item_group_id = " + SQLUtil.convertForSQL(this.getItemGroupId()) + "\n";
    }
    /**
     * Insert���i�f�ݒ�B
     * @return Insert��
     */
    private String	getInsertSQL()
    {
            return	"insert into mst_rank_item_group\n" +
                            "(item_group_id, item_group_name,\n" +
                            "insert_date, update_date, delete_date)\n" +
                            "values( \n" +
                            "(select\n" +
                            "coalesce(max(item_group_id), 0) + 1\n" +
                            "from mst_rank_item_group\n), " +
                            SQLUtil.convertForSQL(this.getItemGroupName()) + ",\n" +
                            "current_timestamp, current_timestamp, null )\n";
    }

    /**
     * Update���i�f�ݒ�B
     * @return Update��
     */
    private String	getUpdateSQL()
    {
            return	"update mst_rank_item_group\n" +
                            "set\n" +
                            "item_group_name = " + SQLUtil.convertForSQL(this.getItemGroupName()) + ",\n" +
                            "update_date = current_timestamp\n" +
                            "where	item_group_id = " + SQLUtil.convertForSQL(this.getItemGroupId()) + "\n" +
                            "and delete_date is null";
    }

    /**
     * �폜�pUpdate���i�f�ݒ�B
     * @return �폜�pUpdate��
     */
    private String	getDeleteSQL()
    {
            return	"update mst_rank_item_group\n"
                    +	"set\n"
                    +	"delete_date = current_timestamp\n"
                    +	"where	item_group_id = " + SQLUtil.convertForSQL(this.getItemGroupId()) + "\n";
    }

    /**
     * Delete mst_rank_item_detail.
     * @return String
     */
    private String getDeleteRankRankItemDetailByIdSQL()
    {
        return	"delete from mst_rank_item_detail\n"
                    +	"where	item_group_id = " + SQLUtil.convertForSQL(this.getItemGroupId()) + "\n";
    }
    /**
     * Select���i�f�ݒ�B
     * @param shopCategoryId 
     * @return Select��
     */
    private String getRankAdvancedSettingSQL(Integer shopCategoryId) {
        return "select mrig.* from mst_rank_advanced_setting mras\n" +
                "inner join mst_rank_item_group mrig on mrig.item_group_id = mras.item_group_id\n" +
                "where mras.shop_category_id = "+shopCategoryId+"\n" +
                "and mrig.delete_date is null\n" +
                "and mras.delete_date is null";

    }
}
