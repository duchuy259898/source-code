/*
 * SearchCostomer.java
 *
 * Created on 2011/07/25, 16:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.search.account;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import com.geobeck.sql.*;
import com.geobeck.sosia.pos.master.account.*;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.util.SQLUtil;

/**
 * �ڋq��������
 * @author katagiri
 */
public class SearchCostomer extends ArrayList<CustomerData>
{
         /**
    	 * ����
	 */
	protected	boolean			name                    = false;

         /**
    	 * �X�֔ԍ�
	 */
	protected	boolean			post                    = false;

         /**
    	 * �Z��
	 */
	protected	boolean			address                 = false;

         /**
    	 * �d�b�ԍ�
	 */
	protected	boolean			tel1                    = false;

         /**
    	 * �g�єԍ�
	 */
	protected	boolean			tel2                    = false;

         /**
    	 * ���N����
	 */
	protected	boolean			birthday                = false;

	/**
	 * �x���敪�̃��X�g
	 */
	protected	MstPaymentClasses	paymentClasses	=	new MstPaymentClasses();

        /**
         * �ڋq
         */
        protected       MstCustomer             targetCustomer = null;
        
        /**
         * ����
         */
        private       boolean                 chkNotMember = false;

	/**
	 * �R���X�g���N�^
	 */
	public SearchCostomer()
	{
	}

        /**
	 * ����
	 * @param name ����
	 */
	public void setName(boolean name)
	{
		this.name = name;
	}

        /**
	 * �X�֔ԍ�
	 * @param post �X�֔ԍ�
	 */
	public void setPost(boolean post)
	{
		this.post = post;
	}

        /**
	 * �Z��
	 * @param address �Z��
	 */
	public void setAddress(boolean address)
	{
		this.address = address;
	}

        /**
	 * �d�b�ԍ�
	 * @param tel1 �d�b�ԍ�
	 */
	public void setTel1(boolean tel1)
	{
		this.tel1 = tel1;
	}

        /**
	 * �g�єԍ�
	 * @param tel2 �g�єԍ�
	 */
	public void setTel2(boolean tel2)
	{
		this.tel2 = tel2;
	}

         /**
	 * ���N����
	 * @param birthday ���N����
	 */
	public void setBirthday(boolean birthday)
	{
		this.birthday = birthday;
	}

        /**
         * @return the customer
         */
        public MstCustomer getTargetCustomer() {
            return targetCustomer;
        }

        /**
         * @param customer the customer to set
         */
        public void setTargetCustomer(MstCustomer customer) {
            this.targetCustomer = customer;
        }

        /**
         * @return the chkNotMember
         */
        public boolean isChkNotMember() {
            return chkNotMember;
        }

        /**
         * @param chkNotMember the chkNotMember to set
         */
        public void setChkNotMember(boolean chkNotMember) {
            this.chkNotMember = chkNotMember;
        }

	/**
	 * �������������s���B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void init(ConnectionWrapper con) throws SQLException
	{
		paymentClasses.loadClasses(con);
		
		//getSearchPC().clear();
		
		//getSearchPC().add(new Boolean(true));
		
		for(MstPaymentClass	mpc : paymentClasses)
		{
			//getSearchPC().add(new Boolean(true));
		}
	}
	
	/**
	 * �ڋq�f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		this.clear();

		ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
                
		MstAccountSetting ma = new MstAccountSetting();
		ma.load( SystemInfo.getConnection());

		//double tax = SystemInfo.getTaxRate(SystemInfo.getSystemDate());
		
		while(rs.next())
		{
                    CustomerData cd = new CustomerData();
                    cd.setData(rs);
                    this.add(cd);
		}
		
		rs.close();
	}
	
	/**
	 * �ڋq�f�[�^���擾����r�p�k�����擾����B
	 * @return �ڋq�f�[�^���擾����r�p�k��
	 */
	private String getSelectSQL()
	{
                String selectSql = "";
                String selectSubSql = "";
                String fromSubSql = "";
                String whereSubSql = "";
                String groupSubSql = "";
                String subAllSql = "";

                String fromSub2Sql = "";
                String sql = "";

                selectSql = "SELECT ";
                selectSql += "M2.customer_id,";
                selectSql += "M2.customer_no,";
                selectSql += "M2.target_number,";
                
                selectSubSql = "SELECT ";
                selectSubSql += "COUNT(1) AS target_number,";
                selectSubSql += "MIN(customer_no) AS customer_no,";
                selectSubSql += "MIN(customer_id) AS customer_id,";

                whereSubSql = " WHERE ";
                groupSubSql = " GROUP BY ";

                fromSub2Sql = "(SELECT DISTINCT ";
                
                //�������`�F�b�N����Ă���ꍇ
                if(name == true)
                {
                    selectSql += "M2.customer_name,";
                    selectSubSql += "COALESCE(T1.customer_name1,'') || COALESCE(T1.customer_name2,'') AS customer_name,";

                    whereSubSql += " COALESCE(T1.customer_name1,'') || COALESCE(T1.customer_name2,'') = T2.customer_name AND";
                    whereSubSql += " COALESCE(T1.customer_name1,'') || COALESCE(T1.customer_name2,'') <> '' AND";
                    whereSubSql += " COALESCE(T2.customer_name, '') <> '' AND";

                    groupSubSql += "COALESCE(T1.customer_name1,'') || COALESCE(T1.customer_name2,''),";

                    fromSub2Sql += "(COALESCE(customer_name1,'') || COALESCE(customer_name2,'')) AS customer_name,";
                }
                else
                {
                    selectSql += "COALESCE(M1.customer_name1,'') || COALESCE(M1.customer_name2,'') AS customer_name,";
                }
                //�X�֔ԍ����`�F�b�N����Ă���ꍇ
                if(post == true)
                {
                    selectSql += "M2.postal_code,";
                    selectSubSql += "T1.postal_code,";

                    whereSubSql += " T1.postal_code = T2.postal_code AND";
                    whereSubSql += " COALESCE(T1.postal_code, '') <> '' AND";
                    whereSubSql += " COALESCE(T2.postal_code, '') <> '' AND";

                    groupSubSql += "T1.postal_code,";

                    fromSub2Sql += "postal_code,";
                }
                else
                {
                    selectSql += "M1.postal_code,";
                }
                //�Z�����`�F�b�N����Ă���ꍇ
                if(address == true)
                {
                    selectSql += "M2.address,";
                    //selectSubSql += "COALESCE(T1.address1,'') || COALESCE(T1.address2,'') || COALESCE(T1.address3,'') || COALESCE(T1.address4,'') AS address,";
                    selectSubSql += "COALESCE(T1.address1,'') || COALESCE(T1.address2,'') AS address,";

                    whereSubSql += " COALESCE(T1.address1,'') || COALESCE(T1.address2,'') = T2.address AND";
                    whereSubSql += " COALESCE(T1.address1,'') || COALESCE(T1.address2,'') <> '' AND";
                    whereSubSql += " COALESCE(T2.address, '') <> '' AND";

                    groupSubSql += "COALESCE(T1.address1,'') || COALESCE(T1.address2,''),";

                    fromSub2Sql += "COALESCE(address1,'') || COALESCE(address2,'') AS address,";
                }
                else
                {
                    //selectSql += "COALESCE(M1.address1,'') || COALESCE(M1.address2,'') || COALESCE(M1.address3,'') || COALESCE(M1.address4,'') AS address,";
                    selectSql += "COALESCE(M1.address1,'') || COALESCE(M1.address2,'') AS address,";
                }
                //�d�b�ԍ����`�F�b�N����Ă���ꍇ
                if(tel1 == true)
                {
                    selectSql += "M2.phone_number,";
                    selectSubSql += "T1.phone_number,";

                    whereSubSql += " T1.phone_number = T2.phone_number AND";
                    whereSubSql += " COALESCE(T1.phone_number, '') <> '' AND";
                    whereSubSql += " COALESCE(T2.phone_number, '') <> '' AND";
                    
                    groupSubSql += "T1.phone_number,";

                    fromSub2Sql += "phone_number,";
                }
                else
                {
                    selectSql += "M1.phone_number,";
                }
                //�g�єԍ����`�F�b�N����Ă���ꍇ
                if(tel2 == true)
                {
                    selectSql += "M2.cellular_number,";
                    selectSubSql += "T1.cellular_number,";

                    whereSubSql += " T1.cellular_number = T2.cellular_number AND";
                    whereSubSql += " COALESCE(T1.cellular_number, '') <> '' AND";
                    whereSubSql += " COALESCE(T2.cellular_number, '') <> '' AND";

                    groupSubSql += "T1.cellular_number,";

                    fromSub2Sql += "cellular_number,";
                }
                else
                {
                    selectSql += "M1.cellular_number,";
                }
                //���N�������`�F�b�N����Ă���ꍇ
                if(birthday == true)
                {
                    selectSql += "M2.birthday,";
                    selectSubSql += "T1.birthday,";

                    whereSubSql += " T1.birthday = T2.birthday AND";
                    whereSubSql += " T1.birthday is not null AND";
                    whereSubSql += " T2.birthday is not null AND";

                    groupSubSql += "T1.birthday,";

                    fromSub2Sql += "birthday,";
                }
                else
                {
                    selectSql += "M1.birthday,";
                }

                selectSql = selectSql.substring(0, selectSql.length()-1);
                selectSubSql = selectSubSql.substring(0, selectSubSql.length()-1);

                fromSub2Sql = fromSub2Sql.substring(0, fromSub2Sql.length()-1);
                fromSub2Sql += " FROM mst_customer";
                //�ڋq���I������Ă���ꍇ
                if (targetCustomer != null && targetCustomer.getCustomerID() != null) {
                    fromSub2Sql += " WHERE customer_id = " + SQLUtil.convertForSQL(targetCustomer.getCustomerID());
                }
                fromSub2Sql += " ) T2 ";

                fromSubSql = " FROM mst_customer T1, ";
                fromSubSql += fromSub2Sql;

                whereSubSql = whereSubSql.substring(0, whereSubSql.length()-3);
                whereSubSql += " AND T1.delete_date is null";
                if (! isChkNotMember()) {
                    whereSubSql += " AND T1.customer_no <> '0'";
                }

                groupSubSql = groupSubSql.substring(0, groupSubSql.length()-1);

                subAllSql = selectSubSql + fromSubSql + whereSubSql + groupSubSql;

                sql = selectSql + " FROM mst_customer M1, (" + subAllSql + ") M2 WHERE M1.customer_id = M2.customer_id";
//---- 2013/04/24 GB MOD START
//                sql += " HAVING M2.target_number > 1";
                sql += " AND M2.target_number > 1";
//---- 2013/04/24 GB MOD END
                sql += " ORDER BY LPAD(M2.customer_no,15,'0')";

		return	sql;
	}

        	/**
	 * �ڋq�f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void loadCustomerDetail(ConnectionWrapper con, Integer customerId) throws SQLException
	{
		this.clear();

		ResultSetWrapper rs = con.executeQuery(this.getCustomerDetailSQL(customerId));

		MstAccountSetting ma = new MstAccountSetting();
		ma.load( SystemInfo.getConnection());

		while(rs.next())
		{
                    CustomerData cd = new CustomerData();
                    cd.setCustomerDetailData(rs);
                    this.add(cd);
		}

		rs.close();
	}

        /**
	 * �ڋq�f�[�^�̏ڍ׏����擾����r�p�k�����擾����B
	 * @return �ڋq�f�[�^�̏ڍ׏����擾����r�p�k��
	 */
	private String getCustomerDetailSQL(Integer customerId)
	{
                String selectSql = "";
                String selectSubSql = "";
                String fromSubSql = "";
                String whereSubSql = "";
                String whereSql = "";
                String subAllSql = "";

                String fromSub2Sql = "";
                String sql = "";

                selectSql = "SELECT ";
                selectSql += "M1.customer_id,";
                selectSql += "M1.customer_no,";
                selectSql += "M1.customer_kana1 || '�@' || M1.customer_kana2 AS customer_kana,";
                selectSql += "M1.pc_mail_address,";
                selectSql += "M1.cellular_mail_address,";
                selectSql += "CASE WHEN M1.sex = 1 THEN '�j' ELSE '��' END AS sex ,";
                selectSql += "M4.job_name,";
                selectSql += "M1.note,";

                selectSubSql = "SELECT ";
                selectSubSql += "customer_no,";
                selectSubSql += "customer_id,";

                whereSql = " WHERE ";
                whereSubSql = " WHERE ";

                fromSub2Sql = "(SELECT DISTINCT ";

                //�������`�F�b�N����Ă���ꍇ
                if(name == true)
                {
                    selectSql += "COALESCE(M1.customer_name1,'') || '�@' || COALESCE(M1.customer_name2,'') AS customer_name,";
                    selectSubSql += "COALESCE(T1.customer_name1,'') || COALESCE(T1.customer_name2,'') AS customer_name,";

                    whereSubSql += " COALESCE(T1.customer_name1,'') || COALESCE(T1.customer_name2,'') = T2.customer_name AND";
                    whereSubSql += " COALESCE(T1.customer_name1,'') || COALESCE(T1.customer_name2,'') <> '' AND";
                    whereSubSql += " COALESCE(T2.customer_name, '') <> '' AND";

                    whereSql += " COALESCE(M1.customer_name1,'') || COALESCE(M1.customer_name2,'') = M2.customer_name AND";
                    
                    fromSub2Sql += "(COALESCE(customer_name1,'') || COALESCE(customer_name2,'')) AS customer_name,";
                }
                else
                {
                    selectSql += "COALESCE(M1.customer_name1,'') || '�@' || COALESCE(M1.customer_name2,'') AS customer_name,";
                }
                //�X�֔ԍ����`�F�b�N����Ă���ꍇ
                if(post == true)
                {
                    selectSql += "M1.postal_code,";
                    selectSubSql += "T1.postal_code,";

                    whereSubSql += " T1.postal_code = T2.postal_code AND";
                    whereSubSql += " COALESCE(T1.postal_code, '') <> '' AND";
                    whereSubSql += " COALESCE(T2.postal_code, '') <> '' AND";

                    whereSql += " M1.postal_code = M2.postal_code AND";
                    
                    fromSub2Sql += "postal_code,";
                }
                else
                {
                    selectSql += "M1.postal_code,";
                }
                //�Z�����`�F�b�N����Ă���ꍇ
                if(address == true)
                {
                    selectSql += "M1.address1,";
                    selectSql += "M1.address2,";
                    selectSql += "M1.address3,";
                    selectSql += "M1.address4,";
                    selectSubSql += "COALESCE(T1.address1,'') || COALESCE(T1.address2,'') AS address,";

                    whereSubSql += " COALESCE(T1.address1,'') || COALESCE(T1.address2,'') = T2.address AND";
                    whereSubSql += " COALESCE(T1.address1,'') || COALESCE(T1.address2,'') <> '' AND";
                    whereSubSql += " COALESCE(T2.address, '') <> '' AND";

                    whereSql += " COALESCE(M1.address1,'') || COALESCE(M1.address2,'') = M2.address AND";
                    
                    fromSub2Sql += "COALESCE(address1,'') || COALESCE(address2,'') AS address,";
                }
                else
                {
                    //selectSql += "COALESCE(M1.address1,'') || COALESCE(M1.address2,'') || COALESCE(M1.address3,'') || COALESCE(M1.address4,'') AS address,";
                    selectSql += "M1.address1,";
                    selectSql += "M1.address2,";
                    selectSql += "M1.address3,";
                    selectSql += "M1.address4,";
                }
                //�d�b�ԍ����`�F�b�N����Ă���ꍇ
                if(tel1 == true)
                {
                    selectSql += "M1.phone_number,";
                    selectSubSql += "T1.phone_number,";

                    whereSubSql += " T1.phone_number = T2.phone_number AND";
                    whereSubSql += " COALESCE(T1.phone_number, '') <> '' AND";
                    whereSubSql += " COALESCE(T2.phone_number, '') <> '' AND";

                    whereSql += " M1.phone_number = M2.phone_number AND";

                    fromSub2Sql += "phone_number,";
                }
                else
                {
                    selectSql += "M1.phone_number,";
                }
                //�g�єԍ����`�F�b�N����Ă���ꍇ
                if(tel2 == true)
                {
                    selectSql += "M1.cellular_number,";
                    selectSubSql += "T1.cellular_number,";

                    whereSubSql += " T1.cellular_number = T2.cellular_number AND";
                    whereSubSql += " COALESCE(T1.cellular_number, '') <> '' AND";
                    whereSubSql += " COALESCE(T2.cellular_number, '') <> '' AND";

                    whereSql += " M1.cellular_number = M2.cellular_number AND";

                    fromSub2Sql += "cellular_number,";
                }
                else
                {
                    selectSql += "M1.cellular_number,";
                }
                //���N�������`�F�b�N����Ă���ꍇ
                if(birthday == true)
                {
                    selectSql += "M1.birthday,";
                    selectSubSql += "T1.birthday,";

                    whereSubSql += " T1.birthday = T2.birthday AND";
                    whereSubSql += " T1.birthday is not null AND";
                    whereSubSql += " T2.birthday is not null AND";

                    whereSql += " M1.birthday = M2.birthday AND";

                    fromSub2Sql += "birthday,";
                }
                else
                {
                    selectSql += "M1.birthday,";
                }

                selectSql += "COALESCE(M3.visit_num,0) AS visit_num,";

                selectSql = selectSql.substring(0, selectSql.length()-1);
                selectSubSql = selectSubSql.substring(0, selectSubSql.length()-1);

                fromSub2Sql = fromSub2Sql.substring(0, fromSub2Sql.length()-1);
                fromSub2Sql += " FROM mst_customer) T2 ";

                fromSubSql = " FROM mst_customer T1, ";
                fromSubSql += fromSub2Sql;

                //whereSubSql = whereSubSql.substring(0, whereSubSql.length()-3);
                whereSql = whereSql.substring(0, whereSql.length()-3);

                whereSubSql += " customer_id = '" + customerId + "'";

                subAllSql = selectSubSql + fromSubSql + whereSubSql;

                sql = selectSql + " FROM mst_customer M1 LEFT OUTER JOIN";
                sql += " (SELECT customer_id, MAX(visit_num) AS visit_num FROM data_sales GROUP BY customer_id) M3 ON M1.customer_id = M3.customer_id";

                sql += " LEFT OUTER JOIN mst_job M4 ON M1.job_id = M4.job_id, ";
                sql += "(" + subAllSql + ") M2 " + whereSql + " AND M1.delete_date is null";
                if (! isChkNotMember()) {
                    sql += " AND M1.customer_no <> '0'";
                }
                sql += " ORDER BY LPAD(M1.customer_no,15,'0')";

		return	sql;
	}

}
