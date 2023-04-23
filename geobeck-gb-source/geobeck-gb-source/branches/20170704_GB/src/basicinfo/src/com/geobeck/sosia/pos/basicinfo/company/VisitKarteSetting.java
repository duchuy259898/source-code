/*
 * VisitKarteSetting.java
 *
 * Created on 2017/03/29, 12:00
 */

package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sosia.pos.hair.master.product.MstFreeHeadingClasses;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;

/***
 * ���X�J���e�ݒ�
 * @author GB
 * 
 */
public class VisitKarteSetting extends ArrayList<MstVisitKarte>{
    
    public final String[] VISIT_KARTE_SETTING_PANEL_FIELDS = 
    {"�����O", "�ӂ肪��", "���N����", "����", "�X�֔ԍ�", "�s���{��", "�s�撬��", "����E�Ԓn", "�}���V��������"
     , "�d�b�ԍ�", "FAX�ԍ�", "�g�єԍ�", "PC���[���A�h���X", "�g�у��[���A�h���X"
     , "���[���z�M��", "�d�b�A����", "DM���t��", "�E��", "���񗈓X���@"};
    
    private static final String[] VISIT_KARTE_SETTING_TABLE_FIELDS = 
    {"customer_name", "customer_kana", "birthday", "sex"
     , "postal_code", "address1", "address2", "address3", "address4", "phone_number", "fax_number", "cellular_number"
     , "pc_mail_address", "cellular_mail_address", "send_mail", "call_flag", "send_dm", "job_id"
     , "first_coming_motive", "free01", "free02", "free03", "free04", "free05", "free06", "free07", "free08", "free09", "free10"};
    
     private static final String DATABASE_NAME   = "pos_hair_";
    
     private    Integer     autoNumbering       =   0;
     private    Integer     onetimeSetting      =   0;
     private    String      schemaName          =   "";
     
     private    MstFreeHeadingClasses mfhc      =   new MstFreeHeadingClasses();
    
    
    
    /**
     * �R���X�g���N�^
     */
     public VisitKarteSetting() {
        this.load();
        this.setSchemaName();
    }
    

    /**
     * ���[�h
     * @return true:���� false:���s
     */
     private boolean load() {
        
        try{

            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(this.getSelectSQL());

            if(rs.next()) {
                    this.setData(rs);
            }else {
                    this.setData();
            }
        
        }catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	}
        
        return true;
    }

    
     
     /**
      * �f�[�^���i�[
      * @param rs
      * @throws SQLException 
      */
     public void setData(ResultSetWrapper rs) throws SQLException {
        
        for(int i = 0; i < VISIT_KARTE_SETTING_TABLE_FIELDS.length; i++) {
            String      colName    =    VISIT_KARTE_SETTING_TABLE_FIELDS[i];
            String      flag       =    colName;
            String      name       =    "field_" + colName;
            
            MstVisitKarte   mvk    =    new MstVisitKarte();
            
            mvk.setData(rs, flag, name, i);
            this.add(mvk);
        }
        
        // �ڋq�R�[�h�����̔�
        this.setAutoNumbering(rs.getInt("auto_numbering"));
        //QR�L������
        this.setOnetimeSetting(rs.getInt("onetime_setting"));
    }
    
    
     /**
      * �f�[�^���i�[�imst_karte_setting�e�[�u���Ƀf�[�^���Ȃ��ꍇ�̏����j
      */
     public void setData() {

        for(int i = 0; i < VISIT_KARTE_SETTING_TABLE_FIELDS.length; i++) {
            
            MstVisitKarte   mvk    =    new MstVisitKarte();
            
            if(i == 0) {
                // ���O�̍��ڂ͕\�����K�{�`�F�b�N�A���͉�ʕ\������ݒ肵�Ă���
                mvk.setFlag(2);
                mvk.setName(VISIT_KARTE_SETTING_PANEL_FIELDS[i]);
            }else {
                mvk.setFlag(0);
                mvk.setName("");
            }

            this.add(mvk);
        }
        
        // �ڋq�R�[�h�����̔�
        // �X�ܐݒ�̎����̔Ԃ̂���Ȃ��ƍ��킹�Ă���
        this.setAutoNumbering(SystemInfo.getCurrentShop().getAutoNumber());
        
        // QR�L������
        this.setOnetimeSetting(30);
    }
     
     
    
    /**
     * mst_karte_setting�ւ̓o�^
     * @return true:���� false:���s
     */
    public boolean regist() {
        
            boolean		result	=	true;
            
	    try {
		    ConnectionWrapper con	=	SystemInfo.getConnection();

		    con.begin();

		    

		    try {
                            if(isExists()) {
                                    // �X�V
                                    if(con.executeUpdate(this.getUpdateSQL()) != 1) {
                                            result	=	false;
                                    }
                            }else {
                                    // �V�K�o�^
                                    if(con.executeUpdate(this.getInsertSQL()) != 1) {
                                            result	=	false;
                                    }
                            }    
                        
		    }catch(SQLException e) {
                            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			    result	=	false;
		    }

		    if(result) {
			    con.commit();
		    }else {
			    con.rollback();
		    }
                    
	    }catch(SQLException e) {
                    result	=	false;
		    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }

	    return	result;
    }
    
    
    
    /**
     * data_karte_qr�Ƀf�[�^�o�^��karte_qr_id��Ԃ�
     * @param customerID
     * @return karte_qr_id(���s����-1��Ԃ�)
     */
    public Integer registDataKarteQR(Integer customerID) {
        
            Integer		result	=	-1;
            
       	    try {
		    ConnectionWrapper con	=	SystemInfo.getConnection();

		    con.begin();

                    
		    try {
                        
                            if(con.executeUpdate(this.getDataKarteQrInsertSQL(customerID)) == 1) {
                                
                                result = this.getSequenceID(con);
                                
                            }
                            
		    }catch(SQLException e) {
			    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		    }

		    if(result > 0) {
			    con.commit();
		    }else {
			    con.rollback();
		    }
                    
	    }catch(SQLException e) {
		    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }

	    return	result;
    }
    
    
    /**
     * �������ꂽ�V�[�P���XID���擾
     * @return karte_qr_id(���s����-1��Ԃ�)
     */
    private Integer getSequenceID(ConnectionWrapper con) throws SQLException {
            
            Integer seqID = -1;
        
            if(con == null) {
                return	seqID;
            }
            
            try{
                ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSeqIdSQL());

                if(rs.next()) {
                    seqID = rs.getInt("currval");
                }

                rs.close();
                
            }catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            return	seqID;
    }
    

    /**
     * �f�[�^�����݂��邩
     * @return true:���݂��� false:���݂��Ȃ�
     */
    public boolean isExists()
   {
           try{
               ConnectionWrapper con = SystemInfo.getConnection();
               
               if(con == null) {
                    return	false;
                }

                ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

                if(!rs.next()) {
                    return	false;
                }
               
           }catch(SQLException e) {
               SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
           }

           return	true;
   }
    
    
   
    /**
     * �ڋqNo�����̔Ԑݒ�擾
     * @return 0:���Ȃ� 1:����
     */
    public Integer getAutoNumbering() {
        return this.autoNumbering;
    }
    
    /**
     * �ڋqNo�����̔Ԑݒ�
     * @param autoNumbering 
     */
    public void setAutoNumbering(Integer autoNumbering) {
        this.autoNumbering = autoNumbering;
    }
    
    /**
     * QR�R�[�h�L�������擾
     * @return QR�R�[�h�L�������i���j
     */
    public Integer getOnetimeSetting() {
        return this.onetimeSetting;
    }
    
    /**
     * QR�R�[�h�L�������ݒ�
     * @param onetimeSetting 
     */
    public void setOnetimeSetting(Integer onetimeSetting) {
        this.onetimeSetting = onetimeSetting;
    }
    
    /**
     * �X�L�[�}��(pos_hair_xxx��xxx����)�擾
     * @return �X�L�[�}��
     */
    public String getSchemaName() {
        return this.schemaName;
    }
    
    /**
     * �X�L�[�}��(pos_hair_xxx��xxx����)�ݒ�
     */
    private void setSchemaName() {
        this.schemaName = generateSchemaName();
    }

    /**
     * �X�L�[�}��(pos_hair_xxx��xxx����)����
     * @return �X�L�[�}��
     */
    public String generateSchemaName() {
        String tmpSchemaName =  SystemInfo.getDatabase().substring(DATABASE_NAME.length());
        return tmpSchemaName;
    }
    
    /**
     * �L�������擾(����p)
     * @param karteQrID
     * @return �����ڋq�y�L�������Fyyyy/MM/dd HH:mm�z  �V�K�y�L�������F�Ȃ��z
     */
    public String getExprirationTime(Integer karteQrID) {
        
        String exprirationTime = "�L�������F";
        
        try{
            ConnectionWrapper con	=	SystemInfo.getConnection();
            
            if(con == null) {
               return	exprirationTime;
           }
           
           StringBuilder sql = new StringBuilder();
           sql.append("select customer_id, insert_date from data_karte_qr where karte_qr_id = ");
           sql.append(SQLUtil.convertForSQL(karteQrID));
           
           ResultSetWrapper	rs	=	con.executeQuery(sql.toString());
           
           if(rs.next()) {
               if(rs.getInt("customer_id") != 0) {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");

                    cal.setTime(rs.getTimestamp("insert_date"));
                    cal.add(Calendar.MINUTE, this.getOnetimeSetting());

                    exprirationTime = exprirationTime + format.format(cal.getTime()).toString();
               }else {
                    exprirationTime = exprirationTime + "�Ȃ�";
               }
           }

        }catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        return exprirationTime;
    }
   
   private String getSelectSQL() {
        Integer     shopID   =  SystemInfo.getCurrentShop().getShopID();
        StringBuilder   sql    =    new StringBuilder();
        
        sql.append("select * from mst_karte_setting ");
        sql.append("where delete_date is null ");
        sql.append("and shop_id = ").append(SQLUtil.convertForSQL(shopID));

        return sql.toString();
    }
   
   private String getUpdateSQL() {
       StringBuilder   sql    =    new StringBuilder();
       
       sql.append("update mst_karte_setting set ");
       sql.append(" onetime_setting = ").append(SQLUtil.convertForSQL(this.getOnetimeSetting()));
       sql.append(" , auto_numbering  = ").append(SQLUtil.convertForSQL(this.getAutoNumbering()));
       
       for(int i = 0; i < VISIT_KARTE_SETTING_TABLE_FIELDS.length; i++) {
           if(i >= 19) {
               // �t���[���ڋ敪
               Integer index = getFreeHeadingClassIndex(i);
               if(mfhc.get(index).getUseFlg()) {
                   sql.append(", ").append(VISIT_KARTE_SETTING_TABLE_FIELDS[i]).append(" = ").append(SQLUtil.convertForSQL(this.get(i).getFlag()));
               }else {
                   sql.append(", ").append(VISIT_KARTE_SETTING_TABLE_FIELDS[i]).append(" = 0 ");
               }
           }else {
               sql.append(", ").append(VISIT_KARTE_SETTING_TABLE_FIELDS[i]).append(" = ").append(SQLUtil.convertForSQL(this.get(i).getFlag()));
           }
       }
       
       for(int i = 0; i < VISIT_KARTE_SETTING_TABLE_FIELDS.length; i++) {
           String colName = "field_" + VISIT_KARTE_SETTING_TABLE_FIELDS[i];
           if(i >= 19) {
               // �t���[���ڋ敪
               Integer index = getFreeHeadingClassIndex(i);
               if(mfhc.get(index).getUseFlg()) {
                   sql.append(", ").append(colName).append(" = ").append(SQLUtil.convertForSQL(this.get(i).getName()));
               }else {
                   sql.append(", ").append(colName).append(" = '' ");
               }
           }else {
               sql.append(", ").append(colName).append(" = ").append(SQLUtil.convertForSQL(this.get(i).getName()));
           }
       }
       
       sql.append(" , update_date = current_timestamp ");
       sql.append("where delete_date is null ");
       sql.append("     and shop_id = ").append(SQLUtil.convertForSQL(SystemInfo.getCurrentShop().getShopID()));

       
       return sql.toString();
   }
   
   private String getInsertSQL() {
       StringBuilder    sql      =   new StringBuilder();
       
       sql.append("insert into mst_karte_setting ( ");
       sql.append("shop_id, onetime_setting, auto_numbering ");
       for(String colName : VISIT_KARTE_SETTING_TABLE_FIELDS) {
           sql.append(", ").append(colName);
       }
       for(String colName : VISIT_KARTE_SETTING_TABLE_FIELDS) {
           colName = "field_" + colName;
           sql.append(", ").append(colName);
       }
       sql.append(", insert_date, update_date, delete_date ) ");
       sql.append("values( ");
       
       sql.append(SQLUtil.convertForSQL(SystemInfo.getCurrentShop().getShopID()));
       sql.append(", ").append(SQLUtil.convertForSQL(this.getOnetimeSetting()));
       sql.append(", ").append(SQLUtil.convertForSQL(this.getAutoNumbering()));
       for(int i = 0; i < VISIT_KARTE_SETTING_TABLE_FIELDS.length; i++) {
           if(i >= 19) {
               // �t���[���ڋ敪
               Integer index = getFreeHeadingClassIndex(i);
               if(mfhc.get(index).getUseFlg()) {
                   sql.append(", ").append(SQLUtil.convertForSQL(this.get(i).getFlag()));
               }else {
                   sql.append(", 0");
               }
           }else {
               sql.append(", ").append(SQLUtil.convertForSQL(this.get(i).getFlag()));
           }
       }
       for(int i = 0; i < VISIT_KARTE_SETTING_TABLE_FIELDS.length; i++) {
           if(i >= 19) {
               // �t���[���ڋ敪
               Integer index = getFreeHeadingClassIndex(i);
               if(mfhc.get(index).getUseFlg()) {
                   sql.append(", ").append(SQLUtil.convertForSQL(this.get(i).getName()));
               }else {
                   sql.append(", ''");
               }
           }else {
               sql.append(", ").append(SQLUtil.convertForSQL(this.get(i).getName()));
           }
       }
       sql.append(", current_timestamp, current_timestamp, null ) ");
      
       return sql.toString();
   }
   
   private String getDataKarteQrInsertSQL(Integer customerID) {
       StringBuilder sql = new StringBuilder();
       
       sql.append("insert into data_karte_qr (shop_id, customer_id, issued_flg, insert_date, update_date, delete_date )");
       sql.append("values( ").append(SystemInfo.getCurrentShop().getShopID());
       sql.append(", ").append(customerID).append(", false, current_timestamp, current_timestamp, null )");
       // sql.append(" returning(karte_qr_id)"); Postgresql 8.2�ȍ~�ł����g���Ȃ�
       
       return sql.toString();
   }
   
   private String getSelectSeqIdSQL() {
       String sql = "SELECT currval('data_karte_qr_karte_qr_id_seq')";
       return sql;
   }
   
   private String getShopAutoNumberingSettingSQL() {
       String sql = "select auto_number from mst_shop where shop_id = ";
       sql += SQLUtil.convertForSQL(SystemInfo.getCurrentShop().getShopID());
       
       return sql;
   }
   
   /**
    * �t���[���ڋ敪��index�擾
    * @param row:�e�[�u���̍s��
    * @return �t���[���ڋ敪��index
    */
   public Integer getFreeHeadingClassIndex(Integer row) {
       Integer freeHeadingIndex = row - 19;

       return freeHeadingIndex;
   }
   

}
