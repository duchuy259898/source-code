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
 * 来店カルテ設定
 * @author GB
 * 
 */
public class VisitKarteSetting extends ArrayList<MstVisitKarte>{
    
    public final String[] VISIT_KARTE_SETTING_PANEL_FIELDS = 
    {"お名前", "ふりがな", "生年月日", "性別", "郵便番号", "都道府県", "市区町村", "町域・番地", "マンション名等"
     , "電話番号", "FAX番号", "携帯番号", "PCメールアドレス", "携帯メールアドレス"
     , "メール配信可否", "電話連絡可否", "DM送付可否", "職業", "初回来店動機"};
    
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
     * コンストラクタ
     */
     public VisitKarteSetting() {
        this.load();
        this.setSchemaName();
    }
    

    /**
     * ロード
     * @return true:成功 false:失敗
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
      * データを格納
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
        
        // 顧客コード自動採番
        this.setAutoNumbering(rs.getInt("auto_numbering"));
        //QR有効期限
        this.setOnetimeSetting(rs.getInt("onetime_setting"));
    }
    
    
     /**
      * データを格納（mst_karte_settingテーブルにデータがない場合の処理）
      */
     public void setData() {

        for(int i = 0; i < VISIT_KARTE_SETTING_TABLE_FIELDS.length; i++) {
            
            MstVisitKarte   mvk    =    new MstVisitKarte();
            
            if(i == 0) {
                // 名前の項目は表示＆必須チェック、入力画面表示名を設定しておく
                mvk.setFlag(2);
                mvk.setName(VISIT_KARTE_SETTING_PANEL_FIELDS[i]);
            }else {
                mvk.setFlag(0);
                mvk.setName("");
            }

            this.add(mvk);
        }
        
        // 顧客コード自動採番
        // 店舗設定の自動採番のありなしと合わせておく
        this.setAutoNumbering(SystemInfo.getCurrentShop().getAutoNumber());
        
        // QR有効期限
        this.setOnetimeSetting(30);
    }
     
     
    
    /**
     * mst_karte_settingへの登録
     * @return true:成功 false:失敗
     */
    public boolean regist() {
        
            boolean		result	=	true;
            
	    try {
		    ConnectionWrapper con	=	SystemInfo.getConnection();

		    con.begin();

		    

		    try {
                            if(isExists()) {
                                    // 更新
                                    if(con.executeUpdate(this.getUpdateSQL()) != 1) {
                                            result	=	false;
                                    }
                            }else {
                                    // 新規登録
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
     * data_karte_qrにデータ登録＆karte_qr_idを返す
     * @param customerID
     * @return karte_qr_id(失敗時は-1を返す)
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
     * 生成されたシーケンスIDを取得
     * @return karte_qr_id(失敗時は-1を返す)
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
     * データが存在するか
     * @return true:存在する false:存在しない
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
     * 顧客No自動採番設定取得
     * @return 0:しない 1:する
     */
    public Integer getAutoNumbering() {
        return this.autoNumbering;
    }
    
    /**
     * 顧客No自動採番設定
     * @param autoNumbering 
     */
    public void setAutoNumbering(Integer autoNumbering) {
        this.autoNumbering = autoNumbering;
    }
    
    /**
     * QRコード有効期限取得
     * @return QRコード有効期限（分）
     */
    public Integer getOnetimeSetting() {
        return this.onetimeSetting;
    }
    
    /**
     * QRコード有効期限設定
     * @param onetimeSetting 
     */
    public void setOnetimeSetting(Integer onetimeSetting) {
        this.onetimeSetting = onetimeSetting;
    }
    
    /**
     * スキーマ名(pos_hair_xxxのxxx部分)取得
     * @return スキーマ名
     */
    public String getSchemaName() {
        return this.schemaName;
    }
    
    /**
     * スキーマ名(pos_hair_xxxのxxx部分)設定
     */
    private void setSchemaName() {
        this.schemaName = generateSchemaName();
    }

    /**
     * スキーマ名(pos_hair_xxxのxxx部分)生成
     * @return スキーマ名
     */
    public String generateSchemaName() {
        String tmpSchemaName =  SystemInfo.getDatabase().substring(DATABASE_NAME.length());
        return tmpSchemaName;
    }
    
    /**
     * 有効期限取得(印刷用)
     * @param karteQrID
     * @return 既存顧客【有効期限：yyyy/MM/dd HH:mm】  新規【有効期限：なし】
     */
    public String getExprirationTime(Integer karteQrID) {
        
        String exprirationTime = "有効期限：";
        
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
                    exprirationTime = exprirationTime + "なし";
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
               // フリー項目区分
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
               // フリー項目区分
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
               // フリー項目区分
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
               // フリー項目区分
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
       // sql.append(" returning(karte_qr_id)"); Postgresql 8.2以降でしか使えない
       
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
    * フリー項目区分のindex取得
    * @param row:テーブルの行数
    * @return フリー項目区分のindex
    */
   public Integer getFreeHeadingClassIndex(Integer row) {
       Integer freeHeadingIndex = row - 19;

       return freeHeadingIndex;
   }
   

}
