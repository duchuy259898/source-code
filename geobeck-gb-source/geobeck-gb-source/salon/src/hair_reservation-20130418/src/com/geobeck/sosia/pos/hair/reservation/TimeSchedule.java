/*
 * TimeSchedule.java
 *
 * Created on 2006/06/16, 17:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.reservation;

import com.geobeck.sosia.pos.basicinfo.company.DataRecess;
import com.geobeck.sosia.pos.hair.data.reservation.*;
import com.geobeck.sosia.pos.hair.master.product.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.customer.*;
import com.geobeck.sosia.pos.basicinfo.company.DataSchedule;
import com.geobeck.sosia.pos.basicinfo.company.DataSchedules;

/**
 * �^�C���X�P�W���[������
 * @author katagiri
 */
public class TimeSchedule
{
	/**
	 * �X��
	 */
	protected   MstShop shop = new MstShop();
	
	/**
	 * ���t
	 */
        protected   java.util.Date  fromDate;
        protected   java.util.Date  toDate;
	
	/**
	 * �w�b�_�f�[�^���X�g
	 */
	protected   ArrayList<ReservationHeader> staffs = new ArrayList<ReservationHeader>();
	
	/**
	 * �w�b�_�f�[�^���X�g
	 */
	protected   ArrayList<ReservationHeader> beds = new ArrayList<ReservationHeader>();

	/**
	 *���o�C���\��t���O
         *1:���\���@2:�\����
	 */        
        protected   int mobileFlag = 1;
        protected   int mobileCancelFlag = 3;
	
        protected MstStaffClass msc = new MstStaffClass();
        protected ArrayList<MstStaffClass> mscList;
        
        private     MstShifts     shifts      = new MstShifts();
        private     DataSchedules staffShifts = new DataSchedules();

        private     Integer	term;           // ���ԒP��
        private     Integer	openHour;       // �J�X���ԁi���j
        private     Integer	openMinute;     // �J�X���ԁi���j
        private     Integer	closeHour;      // �X���ԁi���j
        private     Integer	closeMinute;    // �X���ԁi���j
        
	/**
	 * �R���X�g���N�^
	 */
	public TimeSchedule()
	{
            this.setShop(SystemInfo.getCurrentShop());

            try {
                mscList = msc.load(SystemInfo.getConnection());
            } catch (Exception e) {
            }
	}

 	/**
	 *���o�C���\��t���O���擾����B
         *@return �t���O
         */        
        public int getMobileFlag(){
            return mobileFlag ;
        }

 	/**
	 *���o�C���\��L�����Z���t���O���擾����B
         *@return �t���O
         */
        public int getMobileCancelFlag(){
            return mobileCancelFlag ;
        }

	/**
	 * �X�܂��擾����B
	 * @return �X��
	 */
	public MstShop getShop()
	{
            return shop;
	}

	/**
	 * �X�܂�ݒ肷��B
	 * @param shop �X��
	 */
	public void setShop(MstShop shop)
	{
            this.shop = shop;
	}

	/**
	 * �w�b�_�f�[�^���X�g���擾����B
	 * @return �w�b�_�f�[�^���X�g
	 */
	public ArrayList<ReservationHeader> getStaffs()
	{
            return staffs;
	}

	/**
	 * �w�b�_�f�[�^���X�g���Z�b�g����B
	 * @param headers �w�b�_�f�[�^���X�g
	 */
	public void setstaffs(ArrayList<ReservationHeader> staffs)
	{
            this.staffs = staffs;
	}

	/**
	 * �w�b�_�f�[�^���X�g���擾����B
	 * @return �w�b�_�f�[�^���X�g
	 */
	public ArrayList<ReservationHeader> getBeds()
	{
            return beds;
	}

	/**
	 * �w�b�_�f�[�^���X�g���Z�b�g����B
	 * @param headers �w�b�_�f�[�^���X�g
	 */
	public void setBeds(ArrayList<ReservationHeader> beds)
	{
            this.beds = beds;
	}
	
        private void calcLoadDate()
        {
            GregorianCalendar date = new GregorianCalendar();
            date.setTime(fromDate);
            date.set(date.HOUR_OF_DAY, this.getShop().getOpenHour());
            date.set(date.MINUTE, this.getShop().getOpenMinute());
            fromDate = date.getTime();

            if (toDate != null) {
                date.setTime(toDate);
                date.set(date.HOUR_OF_DAY, this.getShop().getOpenHour());
                date.set(date.MINUTE, this.getShop().getOpenMinute());
                toDate = date.getTime();
            }
        }

	public boolean load(java.util.Date loadDate)
	{
            this.fromDate = loadDate;
            this.toDate   = null;
            this.calcLoadDate();
            
            // �X�^�b�t�̃V�t�g����ǂݍ���
            try {
                staffShifts.setShop(shop);
                staffShifts.setScheduleDate(loadDate);
                staffShifts.load(SystemInfo.getConnection(), false);

                shifts.setShopId(shop.getShopID());
                shifts.loadAll(SystemInfo.getConnection());
            } catch (SQLException e) {
                // ���ɋΖ��X�P�W���[�����ǂ߂Ȃ��Ă��A�����͑�����
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            
            // �X�^�b�t
            if (!this.load(staffs, true)) {
                return false;
            }
            if (shop.getBed() == 1) {
                // �{�p��
                if (!this.load(beds, false)) {
                    return false;
                }
            }


            return true;
	}
	
	/**
	 * �f�[�^��ǂݍ��ށB
	 * @param isStaff �^�u�̏�ԁitrue - �X�^�b�t�j
	 * @param loadDate ���t
	 * @return true - ����
	 */
	public boolean load(ArrayList<ReservationHeader> headers, boolean isStaff)
	{
		headers.clear();
		
		if (this.getShop().getShopID() == 0) return false;
		
		ConnectionWrapper con = SystemInfo.getConnection();
		
		try
		{
			ResultSetWrapper rs = con.executeQuery(this.getSelectSQL(isStaff));
			
			ReservationHeader rh = null;
			
                        ReservationJTextField rtf = null;

                        DataReservation dr = null;

        		GregorianCalendar nextReservationDatetime = new GregorianCalendar();

			while(rs.next())
			{
                                // �V�t�g�A�����Ă���ꍇ�A�x�݂̃X�^�b�t�͕\�����Ȃ��B
                                // �������\�񂪂���X�^�b�t�̓V�t�g���x�݂ł��\������B
                                if (isStaff) {
                                    if ( !this.getShop().isReservationStaffHoliday() && rs.getString("shop_id") == null) {
                                        DataSchedule ds = staffShifts.getSchedule(rs.getInt("staff_id"));
                                        if (ds == null || ds.getShiftName() == null || ds.getShiftName().equals("�x")) {
                                            continue;
                                        }
                                    }
                                }

                                //�\��ڍ׃f�[�^�̎擾
                                DataReservationDetail drd = new DataReservationDetail();

				drd.setReservationDetailNo(rs.getInt("reservation_detail_no"));
				drd.getReservationDatetime().setTime(rs.getTimestamp("reservation_datetime"));
				
				MstTechnicClass	mtc	=	new MstTechnicClass();
				mtc.setTechnicClassID(rs.getInt("technic_class_id"));
				mtc.setTechnicClassName(rs.getString("technic_class_name"));
				mtc.setTechnicClassContractedName(rs.getString("technic_class_contracted_name"));
				MstTechnic		mt	=	new MstTechnic();
				mt.setTechnicClass(mtc);
				mt.setTechnicID(rs.getInt("technic_id"));
				mt.setTechnicNo(rs.getString("technic_no"));
				mt.setTechnicName(rs.getString("technic_name"));
				mt.setOperationTime(rs.getInt("operation_time"));
				drd.setTechnic(mt);
				
				drd.getBed().setBedID(rs.getInt("bed_id"));
				drd.getBed().setBedName(rs.getString("bed_name"));
				drd.getStaff().setStaffID(rs.getInt("staff_id"));
				drd.getStaff().setStaffNo(rs.getString("staff_no"));
				drd.getStaff().setStaffName(0, rs.getString("staff_name1"));
				drd.getStaff().setStaffName(1, rs.getString("staff_name2"));
				
                                //�ȉ��̂����ꂩ�̏����ɓ��Ă͂܂�ꍇ
                                //�P�D�\��w�b�_�f�[�^���Ȃ��A�܂��͗\��w�b�_�f�[�^���قȂ�ꍇ
                                //�Q�D�w�b�_�f�[�^���Ȃ��A�܂��̓w�b�_�f�[�^���قȂ�ꍇ
                                //�R�D�\��������A�����Ă��Ȃ��ꍇ
				if(dr == null ||
                                    !(dr.getShop().getShopID() == rs.getInt("shop_id") &&
                                     dr.getReservationNo() == rs.getInt("reservation_no")
                                     ) ||
                                    (rh == null ||
						(rh.getId() == null && rs.getString((isStaff ? "staff_id" : "bed_id")) != null) ||
						(rh.getId() != null && !(isStaff ?
								rh.getId().toString().equals(rs.getString("staff_id")) :
                                                    (Integer)rh.getId() == rs.getInt("bed_id")))) ||
                                    nextReservationDatetime.compareTo(drd.getReservationDatetime()) != 0
                                   )
				{
                                    //�\��w�b�_�f�[�^�������Ă���ꍇ�A�w�b�_�ɒǉ�
                                    if (dr != null) {
                                        rtf.setReservationDate(fromDate);
                                        rtf.setReservation(dr);
                                        rtf.setHeader(rh);

                                        rh.getReservations().add(rtf);
                                    }
				
                                    //�\��w�b�_�f�[�^�̎擾
                                    rtf	=	new ReservationJTextField();
                                    dr	=	new DataReservation();
				
				MstShop		ms	=	new MstShop();
				ms.setShopID(rs.getInt("shop_id"));
				dr.setShop(ms);
				
				dr.setReservationNo(rs.getInt("reservation_no"));
				dr.setDesignated(rs.getBoolean("designated_flag"));
				
				MstStaff	mst	=	new MstStaff();
				mst.setStaffID(rs.getInt("charge_staff_id"));
				mst.setStaffNo(rs.getString("charge_staff_no"));
				mst.setStaffName(0, rs.getString("charge_staff_name1"));
				mst.setStaffName(1, rs.getString("charge_staff_name2"));
				dr.setStaff(mst);
				
				MstCustomer		mc	=	new MstCustomer();
				mc.setCustomerID(rs.getInt("customer_id"));
				mc.setCustomerNo(rs.getString("customer_no"));
				mc.setCustomerKana(0, rs.getString("customer_kana1"));
				mc.setCustomerKana(1, rs.getString("customer_kana2"));
				mc.setCustomerName(0, rs.getString("customer_name1"));
				mc.setCustomerName(1, rs.getString("customer_name2"));
                                mc.setVisitCount(rs.getLong("visit_count"));
                                mc.setBirthdayDate(rs.getDate("birthday"));
				dr.setCustomer(mc);
				
				dr.setStatus(rs.getInt("status"));
                                dr.setMobileFlag(rs.getInt("mobile_flag"));
				dr.setComment(rs.getString("comment"));
				}
				
                                //�\��ڍ׃f�[�^��\��w�b�_�f�[�^�ɒǉ�
                                dr.add(drd);
				
                                //�\��������A�����Ă��邩�ǂ����̔��f�p�Ƃ��Đݒ�
                                nextReservationDatetime.setTime(drd.getReservationDatetime().getTime());
                                nextReservationDatetime.add(nextReservationDatetime.MINUTE, drd.getTechnic().getOperationTime());

				//�w�b�_�������A�܂��̓w�b�_��񂪈قȂ�ꍇ
				if(rh == null ||
						(rh.getId() == null && rs.getString((isStaff ? "staff_id" : "bed_id")) != null) ||
						(rh.getId() != null && !(isStaff ?
								rh.getId().toString().equals(rs.getString("staff_id")) :
								(Integer)rh.getId() == rs.getInt("bed_id"))))
				{
					//�w�b�_�Ƀf�[�^�������Ă���ꍇ�A���X�g�ɒǉ�
					if(rh != null)
					{
						headers.add(rh);
					}
				
					rh	=	new ReservationHeader();
				
					rh.setShopId((isStaff ? rs.getString("staff_shop_id") : null));
					rh.setId((isStaff ? rs.getString("staff_id") : rs.getInt("bed_id")));
					rh.setClassName(isStaff ? rs.getString("staff_class_contracted_name") : "");
					rh.setName(rs.getString((isStaff ? "staff_name" : "bed_name")));
				}
                                
                                // �X�^�b�t�p�f�[�^�́A�ғ����Ԃ�ݒ肷��
                                if( isStaff ){
                                    if( rh.getId() == null ){
                                        // �X�^�b�t���o�^�̃f�[�^�́A�X�܊J�X���Ԃŕ\��
                                        rh.setOpenTime(this.getOpenTime());
                                    }else{
                                        // �X�^�b�t�̊J�X���Ԃ�\��
                                        rh.setOpenTime(this.getOpenTime(Integer.parseInt((String)rh.getId()), rh.getShopId()));
                                    }
                                }else{
                                    rh.setOpenTime(this.getOpenTime());
                                }
			}

                        //�\��w�b�_�f�[�^�������Ă���ꍇ�A�w�b�_�ɒǉ�
                        if(dr != null)
                        {
                                rtf.setReservationDate(fromDate);
                                rtf.setReservation(dr);
				                rtf.setHeader(rh);
				
				rh.getReservations().add(rtf);
                                
                                // �X�^�b�t�p�f�[�^�́A�ғ����Ԃ�ݒ肷��
                                if( isStaff ){
                                    if( rh.getId() == null ){
                                        // �X�^�b�t���o�^�̃f�[�^�́A�X�܊J�X���Ԃŕ\��
                                        rh.setOpenTime(this.getOpenTime());
                                    }else{
                                        // �X�^�b�t�̊J�X���Ԃ�\��
                                        rh.setOpenTime(this.getOpenTime(Integer.parseInt((String)rh.getId()), rh.getShopId()));
                                    }
                                }else{
                                    rh.setOpenTime(this.getOpenTime());
                                }

			}
			//�w�b�_�Ƀf�[�^�������Ă���ꍇ�A���X�g�ɒǉ�
			if(rh != null)
			{
				headers.add(rh);
			}
			
			rs.close();
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return	false;
		}
		
		return	true;
	}
	
	
	/**
	 * ���Ԃ��X�V����B
	 * @param rtf �^�C���X�P�W���[����ʗpJTextField
	 * @return true - ����
	 */
	public boolean updateTime(ReservationJTextField rtf)
	{
		boolean	result	=	false;
		
		ConnectionWrapper	con	=	SystemInfo.getConnection();
		
		try
		{
			con.begin();
			
                        for(DataReservationDetail drd : rtf.getReservation())
                        {
                            if(drd.updateReservationDatetime(con))
			{
				con.commit();
				result	=	true;
			}
			else
			{
				con.rollback();
			}
		}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
	/**
	 * �f�[�^�ǂݍ��ݗp�r�p�k�����擾����B
	 * @return �f�[�^�ǂݍ��ݗp�r�p�k��
	 */
	private String getSelectSQL(boolean isStaff)
	{
		//�����̊J�X���Ԃ��擾
		GregorianCalendar nextDate = new GregorianCalendar();
		nextDate.setTime(fromDate);
		nextDate.add(nextDate.DAY_OF_MONTH, 1);
		
                StringBuilder reservationSql = new StringBuilder(1000);
                reservationSql.append(" select");
                reservationSql.append("      dr.shop_id");
                reservationSql.append("     ,dr.reservation_no");
                reservationSql.append("     ,dr.designated_flag");
                reservationSql.append("     ,dr.staff_id as charge_staff_id");
                reservationSql.append("     ,cms.staff_no as charge_staff_no");
                reservationSql.append("     ,cms.staff_name1 as charge_staff_name1");
                reservationSql.append("     ,cms.staff_name2 as charge_staff_name2");
                reservationSql.append("     ,drd.reservation_detail_no");
                reservationSql.append("     ,drd.reservation_datetime");
                reservationSql.append("     ,case when drd.staff_id = 0 then null else drd.staff_id end as staff_id");
                reservationSql.append("     ,ms.staff_no");
                reservationSql.append("     ,ms.staff_name1 || ms.staff_name2 as staff_name");
                reservationSql.append("     ,(select staff_class_contracted_name from mst_staff_class where staff_class_id = ms.staff_class_id) as staff_class_contracted_name");
                reservationSql.append("     ,ms.staff_name1");
                reservationSql.append("     ,ms.staff_name2");
                reservationSql.append("     ,case when drd.bed_id = 0 then null else drd.bed_id end as bed_id");
                reservationSql.append("     ,mb.bed_name");
                reservationSql.append("     ,dr.customer_id");
                reservationSql.append("     ,mc.customer_no");
                reservationSql.append("     ,mc.customer_kana1");
                reservationSql.append("     ,mc.customer_kana2");
                reservationSql.append("     ,mc.customer_name1");
                reservationSql.append("     ,mc.customer_name2");
                reservationSql.append("     ,mc.birthday");
                reservationSql.append("     ,get_visit_count(mc.customer_id, " + SQLUtil.convertForSQL(this.getShop().getShopID()) + ") as visit_count");
                reservationSql.append("     ,drd.technic_id");
                reservationSql.append("     ,mt.technic_class_id");
                reservationSql.append("     ,mtc.technic_class_name");
                reservationSql.append("     ,mtc.technic_class_contracted_name");
                reservationSql.append("     ,mt.technic_no");
                reservationSql.append("     ,mt.technic_name");
                reservationSql.append("     ,coalesce(drd.operation_time, mt.operation_time) as operation_time");
                reservationSql.append("     ,dr.status");
                reservationSql.append("     ,dr.comment");
                reservationSql.append("     ,dr.mobile_flag");
                reservationSql.append("     ,ms.display_seq");
                reservationSql.append("     ,mb.display_seq as bed_display_seq");
                reservationSql.append("     ,case when drd.staff_id = 0 then null else ms.shop_id end as staff_shop_id");
                reservationSql.append("     ,null as course_flg");
                reservationSql.append(" from");
                reservationSql.append("     data_reservation dr");
                reservationSql.append("         inner join data_reservation_detail drd");
                reservationSql.append("                 on drd.shop_id = dr.shop_id");
                reservationSql.append("                and drd.reservation_no = dr.reservation_no");
                reservationSql.append("                and drd.delete_date is null");
                reservationSql.append("         left outer join mst_staff cms");
                reservationSql.append("                      on cms.staff_id = dr.staff_id");
                reservationSql.append("         left outer join mst_staff ms");
                reservationSql.append("                      on ms.staff_id = drd.staff_id");
                reservationSql.append("         left outer join mst_customer mc");
                reservationSql.append("                      on mc.customer_id = dr.customer_id");
                reservationSql.append("         left outer join mst_bed mb");
                reservationSql.append("                      on mb.bed_id = drd.bed_id");
                reservationSql.append("         left outer join mst_technic mt");
                reservationSql.append("                      on mt.technic_id = drd.technic_id");
                reservationSql.append("         left outer join mst_technic_class mtc");
                reservationSql.append("                      on mtc.technic_class_id = mt.technic_class_id");
                reservationSql.append(" where");
                reservationSql.append("         dr.delete_date is null");
                reservationSql.append("     and dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
                reservationSql.append("     and drd.reservation_datetime >= " + SQLUtil.convertForSQL(fromDate));
                reservationSql.append("     and drd.reservation_datetime < " + SQLUtil.convertForSQL(nextDate));
                reservationSql.append("     and drd.course_flg is null");

                // �\�񖾍א����P�܂��͎{�p���Ԃ��O���ȏ�̃f�[�^���o�͂���
                reservationSql.append(" and (");
                reservationSql.append("     exists");
                reservationSql.append("     (");
                reservationSql.append("         select");
                reservationSql.append("             1");
                reservationSql.append("         from");
                reservationSql.append("             data_reservation_detail");
                reservationSql.append("         where");
                reservationSql.append("                 delete_date is null");
                reservationSql.append("             and shop_id = dr.shop_id");
                reservationSql.append("             and reservation_no = dr.reservation_no");
                reservationSql.append("         having");
                reservationSql.append("             count(*) = 1");
                reservationSql.append("     )");
                reservationSql.append("     or");
                reservationSql.append("     exists");
                reservationSql.append("     (");
                reservationSql.append("         select");
                reservationSql.append("             1");
                reservationSql.append("         from");
                reservationSql.append("             data_reservation_detail a");
                reservationSql.append("                 join mst_technic b");
                reservationSql.append("                     using (technic_id)");
                reservationSql.append("         where");
                reservationSql.append("                 a.delete_date is null");
                reservationSql.append("             and a.shop_id = dr.shop_id");
                reservationSql.append("             and a.reservation_no = dr.reservation_no");
                reservationSql.append("             and a.reservation_detail_no = drd.reservation_detail_no");

                // Deleted 2012-07-07
                //reservationSql.append("             and coalesce(a.operation_time, b.operation_time) <> 0");

                reservationSql.append("     )");
                reservationSql.append(" )");

                //�R�[�X�_�� ��������------------------------------
                reservationSql.append(" union all");
                reservationSql.append(" select");
                reservationSql.append("      dr.shop_id");
                reservationSql.append("     ,dr.reservation_no");
                reservationSql.append("     ,dr.designated_flag");
                reservationSql.append("     ,dr.staff_id as charge_staff_id");
                reservationSql.append("     ,cms.staff_no as charge_staff_no");
                reservationSql.append("     ,cms.staff_name1 as charge_staff_name1");
                reservationSql.append("     ,cms.staff_name2 as charge_staff_name2");
                reservationSql.append("     ,drd.reservation_detail_no");
                reservationSql.append("     ,drd.reservation_datetime");
                reservationSql.append("     ,case when drd.staff_id = 0 then null else drd.staff_id end as staff_id");
                reservationSql.append("     ,ms.staff_no");
                reservationSql.append("     ,ms.staff_name1 || ms.staff_name2 as staff_name");
                reservationSql.append("     ,(select staff_class_contracted_name from mst_staff_class where staff_class_id = ms.staff_class_id) as staff_class_contracted_name");
                reservationSql.append("     ,ms.staff_name1");
                reservationSql.append("     ,ms.staff_name2");
                reservationSql.append("     ,case when drd.bed_id = 0 then null else drd.bed_id end as bed_id");
                reservationSql.append("     ,mb.bed_name");
                reservationSql.append("     ,dr.customer_id");
                reservationSql.append("     ,mc.customer_no");
                reservationSql.append("     ,mc.customer_kana1");
                reservationSql.append("     ,mc.customer_kana2");
                reservationSql.append("     ,mc.customer_name1");
                reservationSql.append("     ,mc.customer_name2");
                reservationSql.append("     ,mc.birthday");
                reservationSql.append("     ,get_visit_count(mc.customer_id, " + SQLUtil.convertForSQL(this.getShop().getShopID()) + ") as visit_count");
                reservationSql.append("     ,drd.technic_id");
                reservationSql.append("     ,c.course_class_id as technic_class_id");
                reservationSql.append("     ,cc.course_class_name as technic_class_name");
                reservationSql.append("     ,cc.course_class_contracted_name as technic_class_contracted_name");
                reservationSql.append("     ,null as technic_no");
                reservationSql.append("     ,c.course_name as technic_name");
                reservationSql.append("     ,coalesce(drd.operation_time, c.operation_time) as operation_time");
                reservationSql.append("     ,dr.status");
                reservationSql.append("     ,dr.comment");
                reservationSql.append("     ,dr.mobile_flag");
                reservationSql.append("     ,ms.display_seq");
                reservationSql.append("     ,mb.display_seq as bed_display_seq");
                reservationSql.append("     ,case when drd.staff_id = 0 then null else ms.shop_id end as staff_shop_id");
                reservationSql.append("     ,1 as course_flg");
                reservationSql.append(" from");
                reservationSql.append("     data_reservation dr");
                reservationSql.append("         inner join data_reservation_detail drd");
                reservationSql.append("                 on drd.shop_id = dr.shop_id");
                reservationSql.append("                and drd.reservation_no = dr.reservation_no");
                reservationSql.append("                and drd.delete_date is null");
                reservationSql.append("         left outer join mst_staff cms");
                reservationSql.append("                      on cms.staff_id = dr.staff_id");
                reservationSql.append("         left outer join mst_staff ms");
                reservationSql.append("                      on ms.staff_id = drd.staff_id");
                reservationSql.append("         left outer join mst_customer mc");
                reservationSql.append("                      on mc.customer_id = dr.customer_id");
                reservationSql.append("         left outer join mst_bed mb");
                reservationSql.append("                      on mb.bed_id = drd.bed_id");
                reservationSql.append("         left outer join mst_course c");
                reservationSql.append("                      on c.course_id = drd.technic_id");
                reservationSql.append("         left outer join mst_course_class cc");
                reservationSql.append("                      on cc.course_class_id = c.course_class_id");
                reservationSql.append(" where");
                reservationSql.append("         dr.delete_date is null");
                reservationSql.append("     and dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
                reservationSql.append("     and drd.reservation_datetime >= " + SQLUtil.convertForSQL(fromDate));
                reservationSql.append("     and drd.reservation_datetime < " + SQLUtil.convertForSQL(nextDate));
                reservationSql.append("     and drd.course_flg = 1");

                // �\�񖾍א����P�܂��͎{�p���Ԃ��O���ȏ�̃f�[�^���o�͂���
                reservationSql.append(" and (");
                reservationSql.append("     exists");
                reservationSql.append("     (");
                reservationSql.append("         select");
                reservationSql.append("             1");
                reservationSql.append("         from");
                reservationSql.append("             data_reservation_detail");
                reservationSql.append("         where");
                reservationSql.append("                 delete_date is null");
                reservationSql.append("             and shop_id = dr.shop_id");
                reservationSql.append("             and reservation_no = dr.reservation_no");
                reservationSql.append("         having");
                reservationSql.append("             count(*) = 1");
                reservationSql.append("     )");
                reservationSql.append("     or");
                reservationSql.append("     exists");
                reservationSql.append("     (");
                reservationSql.append("         select");
                reservationSql.append("             1");
                reservationSql.append("         from");
                reservationSql.append("             data_reservation_detail a");
                reservationSql.append("                 join mst_technic b");
                reservationSql.append("                     using (technic_id)");
                reservationSql.append("         where");
                reservationSql.append("                 a.delete_date is null");
                reservationSql.append("             and a.shop_id = dr.shop_id");
                reservationSql.append("             and a.reservation_no = dr.reservation_no");
                reservationSql.append("             and a.reservation_detail_no = drd.reservation_detail_no");
                reservationSql.append("             and coalesce(a.operation_time, b.operation_time) <> 0");
                reservationSql.append("     )");
                reservationSql.append(" )");
                //�R�[�X�_�� �����܂�------------------------------




                //�����R�[�X ��������------------------------------
                reservationSql.append(" union all");
                reservationSql.append(" select distinct");
                reservationSql.append("      dr.shop_id");
                reservationSql.append("     ,dr.reservation_no");
                reservationSql.append("     ,dr.designated_flag");
                reservationSql.append("     ,dr.staff_id as charge_staff_id");
                reservationSql.append("     ,cms.staff_no as charge_staff_no");
                reservationSql.append("     ,cms.staff_name1 as charge_staff_name1");
                reservationSql.append("     ,cms.staff_name2 as charge_staff_name2");
                reservationSql.append("     ,drd.reservation_detail_no");
                reservationSql.append("     ,drd.reservation_datetime");
                reservationSql.append("     ,case when drd.staff_id = 0 then null else drd.staff_id end as staff_id");
                reservationSql.append("     ,ms.staff_no");
                reservationSql.append("     ,ms.staff_name1 || ms.staff_name2 as staff_name");
                reservationSql.append("     ,(select staff_class_contracted_name from mst_staff_class where staff_class_id = ms.staff_class_id) as staff_class_contracted_name");
                reservationSql.append("     ,ms.staff_name1");
                reservationSql.append("     ,ms.staff_name2");
                reservationSql.append("     ,case when drd.bed_id = 0 then null else drd.bed_id end as bed_id");
                reservationSql.append("     ,mb.bed_name");
                reservationSql.append("     ,dr.customer_id");
                reservationSql.append("     ,mc.customer_no");
                reservationSql.append("     ,mc.customer_kana1");
                reservationSql.append("     ,mc.customer_kana2");
                reservationSql.append("     ,mc.customer_name1");
                reservationSql.append("     ,mc.customer_name2");
                reservationSql.append("     ,mc.birthday");
                reservationSql.append("     ,get_visit_count(mc.customer_id, " + SQLUtil.convertForSQL(this.getShop().getShopID()) + ") as visit_count");
                reservationSql.append("     ,drd.technic_id");
                reservationSql.append("     ,c.course_class_id as technic_class_id");
                reservationSql.append("     ,cc.course_class_name as technic_class_name");
                reservationSql.append("     ,cc.course_class_contracted_name as technic_class_contracted_name");
                reservationSql.append("     ,null as technic_no");
                reservationSql.append("     ,c.course_name as technic_name");
                reservationSql.append("     ,coalesce(drd.operation_time, c.operation_time) as operation_time");
                reservationSql.append("     ,dr.status");
                reservationSql.append("     ,dr.comment");
                reservationSql.append("     ,dr.mobile_flag");
                reservationSql.append("     ,ms.display_seq");
                reservationSql.append("     ,mb.display_seq as bed_display_seq");
                reservationSql.append("     ,case when drd.staff_id = 0 then null else ms.shop_id end as staff_shop_id");
                reservationSql.append("     ,2 as course_flg");
                reservationSql.append(" from");
                reservationSql.append("     data_reservation dr");
                reservationSql.append("         inner join data_reservation_detail drd");
                reservationSql.append("                 on drd.shop_id = dr.shop_id");
                reservationSql.append("                and drd.reservation_no = dr.reservation_no");
                reservationSql.append("                and drd.delete_date is null");
                reservationSql.append("         left outer join mst_staff cms");
                reservationSql.append("                      on cms.staff_id = dr.staff_id");
                reservationSql.append("         left outer join mst_staff ms");
                reservationSql.append("                      on ms.staff_id = drd.staff_id");
                reservationSql.append("         left outer join mst_customer mc");
                reservationSql.append("                      on mc.customer_id = dr.customer_id");
                reservationSql.append("         left outer join mst_bed mb");
                reservationSql.append("                      on mb.bed_id = drd.bed_id");

                //2013/04/13 delete�@start okubo
                //reservationSql.append("         left outer join data_contract_digestion dcd");
                //reservationSql.append("                      on drd.shop_id = dcd.shop_id and drd.contract_no = dcd.contract_no and drd.contract_detail_no = dcd.contract_detail_no");
                //2013/04/13 delete�@end

                reservationSql.append("         left outer join mst_course c");
                reservationSql.append("                      on c.course_id = drd.technic_id");
                reservationSql.append("         left outer join mst_course_class cc");
                reservationSql.append("                      on cc.course_class_id = c.course_class_id");
                reservationSql.append(" where");
                reservationSql.append("         dr.delete_date is null");
                reservationSql.append("     and dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
                reservationSql.append("     and drd.reservation_datetime >= " + SQLUtil.convertForSQL(fromDate));
                reservationSql.append("     and drd.reservation_datetime < " + SQLUtil.convertForSQL(nextDate));
                reservationSql.append("     and drd.course_flg = 2");

                // �\�񖾍א����P�܂��͎{�p���Ԃ��O���ȏ�̃f�[�^���o�͂���
                reservationSql.append(" and (");
                reservationSql.append("     exists");
                reservationSql.append("     (");
                reservationSql.append("         select");
                reservationSql.append("             1");
                reservationSql.append("         from");
                reservationSql.append("             data_reservation_detail");
                reservationSql.append("         where");
                reservationSql.append("                 delete_date is null");
                reservationSql.append("             and shop_id = dr.shop_id");
                reservationSql.append("             and reservation_no = dr.reservation_no");
                reservationSql.append("         having");
                reservationSql.append("             count(*) = 1");
                reservationSql.append("     )");
                reservationSql.append("     or");
                reservationSql.append("     exists");
                reservationSql.append("     (");
                reservationSql.append("         select");
                reservationSql.append("             1");
                reservationSql.append("         from");
                reservationSql.append("             data_reservation_detail a");
                reservationSql.append("                 join mst_technic b");
                reservationSql.append("                     using (technic_id)");
                reservationSql.append("         where");
                reservationSql.append("                 a.delete_date is null");
                reservationSql.append("             and a.shop_id = dr.shop_id");
                reservationSql.append("             and a.reservation_no = dr.reservation_no");
                reservationSql.append("             and a.reservation_detail_no = drd.reservation_detail_no");
                reservationSql.append("             and coalesce(a.operation_time, b.operation_time) <> 0");
                reservationSql.append("     )");
                reservationSql.append(" )");
                //�����R�[�X �����܂�------------------------------

                StringBuilder sql = new StringBuilder(1000);
                sql.append(" select * from");
                sql.append(" (" + reservationSql.toString());
                
                // �\��\�\���̃X�^�b�t�敪������ꍇ
                if (isStaff && getStaffClassIdList().length() > 0) {
                    
                    sql.append(" union all");
                    sql.append(" select");
                    sql.append("      null as shop_id");
                    sql.append("     ,null as reservation_no");
                    sql.append("     ,null as designated_flag");
                    sql.append("     ,null as charge_staff_id");
                    sql.append("     ,null as charge_staff_no");
                    sql.append("     ,null as charge_staff_name1");
                    sql.append("     ,null as charge_staff_name2");
                    sql.append("     ,null as reservation_detail_no");
                    sql.append("     ,'1900-01-01'::date as reservation_datetime");
                    sql.append("     ,ms.staff_id");
                    sql.append("     ,ms.staff_no");
                    sql.append("     ,ms.staff_name1 || ms.staff_name2 as staff_name");
                    sql.append("     ,(select staff_class_contracted_name from mst_staff_class where staff_class_id = ms.staff_class_id) as staff_class_contracted_name");
                    sql.append("     ,ms.staff_name1");
                    sql.append("     ,ms.staff_name2");
                    sql.append("     ,null as bed_id");
                    sql.append("     ,null as bed_name");
                    sql.append("     ,null as customer_id");
                    sql.append("     ,null as customer_no");
                    sql.append("     ,null as customer_kana1");
                    sql.append("     ,null as customer_kana2");
                    sql.append("     ,null as customer_name1");
                    sql.append("     ,null as customer_name2");
                    sql.append("     ,null as birthday");
                    sql.append("     ,null as visit_count");
                    sql.append("     ,null as technic_id");
                    sql.append("     ,null as technic_class_id");
                    sql.append("     ,null as technic_class_name");
                    sql.append("     ,null as technic_class_contracted_name");
                    sql.append("     ,null as technic_no");
                    sql.append("     ,null as technic_name");
                    sql.append("     ,null as operation_time");
                    sql.append("     ,null as status");
                    sql.append("     ,null as comment");
                    sql.append("     ,null as mobile_flag");
                    sql.append("     ,ms.display_seq");
                    sql.append("     ,0 as bed_display_seq");
                    sql.append("     ,null as staff_shop_id");
                    sql.append("     ,null as course_flg");
                    sql.append(" from");
                    sql.append("     mst_staff ms");
                    sql.append(" where");
                    sql.append("         ms.delete_date is null");
                    sql.append("     and ms.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));

                    // ���X�܂ւ̃w���v�X�^�b�t�͏��O����
                    sql.append("     and ms.staff_id not in");
                    sql.append("         (");
                    sql.append("             select");
                    sql.append("                 a.staff_id");
                    sql.append("             from");
                    sql.append("                 mst_staff a");
                    sql.append("                     join data_staff_work_time b");
                    sql.append("                     using (staff_id)");
                    sql.append("             where");
                    sql.append("                     a.delete_date is null");
                    sql.append("                 and b.delete_date is null");
                    sql.append("                 and b.shop_id <> " + SQLUtil.convertForSQL(this.getShop().getShopID()));
                    sql.append("                 and b.working_date = " + SQLUtil.convertForSQLDateOnly(fromDate));
                    sql.append("         )");
                    
                    // �\��\�\���̃X�^�b�t�敪�ɏ�������X�^�b�t��\������
                    sql.append("     and ms.staff_id in");
                    sql.append("         (");
                    sql.append("             select");
                    sql.append("                 staff_id");
                    sql.append("             from");
                    sql.append("                 mst_staff");
                    sql.append("             where");
                    sql.append("                     delete_date is null");
                    sql.append("                 and staff_class_id in (" + getStaffClassIdList() + ")");
                    sql.append("         )");
                    
                    // �\��o�^�ς݂̃X�^�b�t�͏��O����
                    sql.append("     and ms.staff_id not in (select staff_id from (" + reservationSql.toString() + ") t where staff_id is not null)");

                    // �w���v�X�^�b�t���܂߂�
                    sql.append(" union all");
                    sql.append(" select");
                    sql.append("      null as shop_id");
                    sql.append("     ,null as reservation_no");
                    sql.append("     ,null as designated_flag");
                    sql.append("     ,null as charge_staff_id");
                    sql.append("     ,null as charge_staff_no");
                    sql.append("     ,null as charge_staff_name1");
                    sql.append("     ,null as charge_staff_name2");
                    sql.append("     ,null as reservation_detail_no");
                    sql.append("     ,'1900-01-01'::date as reservation_datetime");
                    sql.append("     ,ms.staff_id");
                    sql.append("     ,ms.staff_no");
                    sql.append("     ,ms.staff_name1 || ms.staff_name2 as staff_name");
                    sql.append("     ,(select staff_class_contracted_name from mst_staff_class where staff_class_id = ms.staff_class_id) as staff_class_contracted_name");
                    sql.append("     ,ms.staff_name1");
                    sql.append("     ,ms.staff_name2");
                    sql.append("     ,null as bed_id");
                    sql.append("     ,null as bed_name");
                    sql.append("     ,null as customer_id");
                    sql.append("     ,null as customer_no");
                    sql.append("     ,null as customer_kana1");
                    sql.append("     ,null as customer_kana2");
                    sql.append("     ,null as customer_name1");
                    sql.append("     ,null as customer_name2");
                    sql.append("     ,null as birthday");
                    sql.append("     ,null as visit_count");
                    sql.append("     ,null as technic_id");
                    sql.append("     ,null as technic_class_id");
                    sql.append("     ,null as technic_class_name");
                    sql.append("     ,null as technic_class_contracted_name");
                    sql.append("     ,null as technic_no");
                    sql.append("     ,null as technic_name");
                    sql.append("     ,null as operation_time");
                    sql.append("     ,null as status");
                    sql.append("     ,null as comment");
                    sql.append("     ,null as mobile_flag");
                    sql.append("     ,ms.display_seq");
                    sql.append("     ,0 as bed_display_seq");
                    sql.append("     ,ms.shop_id as staff_shop_id");
                    sql.append("     ,null as course_flg");
                    sql.append(" from");
                    sql.append("     mst_staff ms");
                    sql.append(" where");
                    sql.append("         ms.delete_date is null");
                    sql.append("     and ms.staff_id in");
                    sql.append("         (");
                    sql.append("             select");
                    sql.append("                 a.staff_id");
                    sql.append("             from");
                    sql.append("                 mst_staff a");
                    sql.append("                     join data_staff_work_time b");
                    sql.append("                     using (staff_id)");
                    sql.append("             where");
                    sql.append("                     a.delete_date is null");
                    sql.append("                 and b.delete_date is null");
                    sql.append("                 and b.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
                    sql.append("                 and b.working_date = " + SQLUtil.convertForSQLDateOnly(fromDate));
                    sql.append("         )");

                } else {
                    
                    sql.append(" union all");
                    sql.append(" select");
                    sql.append("      null as shop_id");
                    sql.append("     ,null as reservation_no");
                    sql.append("     ,null as designated_flag");
                    sql.append("     ,null as charge_staff_id");
                    sql.append("     ,null as charge_staff_no");
                    sql.append("     ,null as charge_staff_name1");
                    sql.append("     ,null as charge_staff_name2");
                    sql.append("     ,null as reservation_detail_no");
                    sql.append("     ,'1900-01-01'::date as reservation_datetime");
                    sql.append("     ,null as staff_id");
                    sql.append("     ,null as staff_no");
                    sql.append("     ,null as staff_name");
                    sql.append("     ,null as staff_class_contracted_name");
                    sql.append("     ,null as staff_name1");
                    sql.append("     ,null as staff_name2");
                    sql.append("     ,mb.bed_id");
                    sql.append("     ,mb.bed_name");
                    sql.append("     ,null as customer_id");
                    sql.append("     ,null as customer_no");
                    sql.append("     ,null as customer_kana1");
                    sql.append("     ,null as customer_kana2");
                    sql.append("     ,null as customer_name1");
                    sql.append("     ,null as customer_name2");
                    sql.append("     ,null as birthday");
                    sql.append("     ,null as visit_count");
                    sql.append("     ,null as technic_id");
                    sql.append("     ,null as technic_class_id");
                    sql.append("     ,null as technic_class_name");
                    sql.append("     ,null as technic_class_contracted_name");
                    sql.append("     ,null as technic_no");
                    sql.append("     ,null as technic_name");
                    sql.append("     ,null as operation_time");
                    sql.append("     ,null as status");
                    sql.append("     ,null as comment");
                    sql.append("     ,null as mobile_flag");
                    sql.append("     ,9999999999 as display_seq");
                    sql.append("     ,mb.display_seq as bed_display_seq");
                    sql.append("     ,null as staff_shop_id");
                    sql.append("     ,null as course_flg");
                    sql.append(" from");
                    sql.append("     mst_bed mb");
                    sql.append(" where");
                    sql.append("         mb.delete_date is null");
                    sql.append("     and mb.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
                    
                    // �\��o�^�ς݂̎{�p��͏��O����
                    sql.append("     and mb.bed_id not in");
                    sql.append("         (");
                    sql.append("             select");
                    sql.append("                 bed_id");
                    sql.append("             from");
                    sql.append("                 (" + reservationSql.toString() + ") t");
                    sql.append("             where");
                    sql.append("                 bed_id is not null");
                    sql.append("         )");

                }
                
                sql.append(" ) t");

                sql.append(" order by");
                
                if (isStaff) {

                    sql.append("  display_seq");
                    sql.append(" ,lpad(staff_no, 10, '0')");
                    sql.append(" ,staff_id");
                    
                } else {

                    sql.append("  bed_display_seq");
                    sql.append(" ,bed_id");
                }

                // �\��g���J�n���ԏ��Ɍ��ԂȂ����񂷂�悤�ɕ��я���ݒ�
                sql.append("     ,to_char(");
                sql.append("         coalesce(");
                sql.append("              (select visit_time from data_reservation where shop_id = t.shop_id and reservation_no = t.reservation_no)");
                sql.append("             ,(select min(reservation_datetime) from data_reservation_detail where shop_id = t.shop_id and reservation_no = t.reservation_no)");
                sql.append("         ), 'yyyy/mm/dd hh24:mi:ss'");
                sql.append("      ) || '_' || t.reservation_no");
                
                sql.append("     ,reservation_no");
                sql.append("     ,reservation_datetime");
                sql.append("     ,reservation_detail_no");

                return sql.toString();
	}
        
        private String getStaffClassIdList() {
            
            String result = "";
            
            for (MstStaffClass msc : mscList) {
                if (msc.isDisplayReservation()) {
                    if (result.length() > 0) result += ",";
                    result += msc.getStaffClassID().toString();
                }
            }
            
            return result;
        }
 
        /**
         * �ғ����ԗp�z��̓X�܃f�B�t�H���g�l���擾
         * @return �X�܂̉c�Ǝ��Ԕz��
         */
        public	Vector<Integer> getTimeTable() {

            Vector<Integer> time = new Vector<Integer>();

            if(getOpenHour() == null || getCloseHour() == null || getTerm() == null ) return time;

            for(int h = getOpenHour(); h <= getCloseHour(); h ++) {
                for(int m = 0; m < 60; m += getTerm()) {
                    //�J�X���Ԃ��O�̏ꍇ
                    if(h == getOpenHour() && m < getOpenMinute())
                        continue;
                    //�X���Ԃ���̏ꍇ
                    if(h == getCloseHour() && getCloseMinute() <= m)
                        break;

                    time.add(m);
                }
            }

            return time;
        }

        /**
         * �ғ����ԗp�z��̓X�܃f�B�t�H���g�l���擾
         * @return �X�܂̉c�Ǝ��Ԕz��
         */
        private	Vector<Boolean> getOpenTime() {
            Vector<Boolean>	openTime		=	new Vector<Boolean>();

            if(getOpenHour() == null || getCloseHour() == null || getTerm() == null )	return	openTime;

            for(int h = getOpenHour(); h <= getCloseHour(); h ++) {
                for(int m = 0; m < 60; m += getTerm()) {
                    //�J�X���Ԃ��O�̏ꍇ
                    if(h == getOpenHour() && m < getOpenMinute())
                        continue;
                    //�X���Ԃ���̏ꍇ
                    if(h == getCloseHour() && getCloseMinute() <= m)
                        break;

                    openTime.add(true);
                }
            }

            return	openTime;
        }

        /*
         * �X�^�b�t�̉ғ����Ԃ��擾
         */
        private Vector<Boolean> getOpenTime(Integer staffId) {
            return getOpenTime(staffId, null);
        }
        private Vector<Boolean> getOpenTime(Integer staffId, Object staffShopId) {

            Vector openTime = this.getOpenTime();

            if( staffShifts == null ) return openTime;

            // ��x�A�S�̈��false�ɐݒ�
            for(int ii = 0; ii < openTime.size(); ii++){
                openTime.set(ii, false);
            }

            // �ΏۃX�^�b�t�̋Ζ������擾
            DataSchedule sched = staffShifts.getSchedule(staffId);
            if( sched == null ) return openTime;

            // �Ζ����ԈȊO��false��ݒ�
            MstShift shift = null;
            if (staffShopId != null) {
                shift = shifts.getShift(sched.getShiftId(), Integer.valueOf(staffShopId.toString()));
            } else {
                shift = shifts.getShift(sched.getShiftId());
            }
            if( shift == null ) return openTime;

            boolean opend;
            int ii = 0;
            for(int h = getOpenHour(); h <= getCloseHour(); h ++) {
                opend = false;
                for(int m = 0; m < 60; m += getTerm()) {
                    //�J�X���Ԃ��O�̏ꍇ
                    if(h == getOpenHour() && m < getOpenMinute())
                        continue;
                    //�X���Ԃ���̏ꍇ
                    if(h == getCloseHour() && getCloseMinute() <= m)
                        break;

                    String targetTime = String.format("%02d%02d", h, m);
                    if( shift.inRange(targetTime) ){
                        List result = sched.getRecesses().inRangeEx(targetTime);
                        if ( ! ((Boolean)result.get(0)) ){
                            openTime.set(ii, true);
                        } else {
                            // �x�e���o�^����Ă���ꍇ��DataRecess���Z�b�g����
                            openTime.set(ii, (DataRecess)result.get(1));
                        }
                    }
                    ++ii;
                }
            }

            return openTime;
        }

        /**
         * �I������Ă���X�܂�ID���擾����B
         * @return �I������Ă���X�܂�ID
         */
        private Integer getSelectedShopID() {
            MstShop	ms	=	this.getShop();

            if(ms != null)
                return	ms.getShopID();
            else
                return	0;
        }

        /**
         * �w��X�܂̊J�X�X���Ԃ��擾����
         */
        public void setOpenCloseTimeByReservation(GregorianCalendar	currentDate) {
            ConnectionWrapper	con	=	SystemInfo.getConnection();
            this.fromDate = currentDate.getTime();
            try {
                ResultSetWrapper	rs	=	con.executeQuery(this.getOpenCloseTimeSQL());

                if(rs.next()) {
                    setOpenHour(rs.getInt("open_hour"));
                    setOpenMinute(rs.getInt("open_minute"));
                    setCloseHour(rs.getInt("close_hour"));
                    setCloseMinute(rs.getInt("close_minute"));
                }

                rs.close();
            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }

        void setOpenCloseTimeByReservation(GregorianCalendar currentDate, GregorianCalendar toDate) {
            ConnectionWrapper	con	=	SystemInfo.getConnection();
            this.fromDate = currentDate.getTime();
            this.toDate = toDate.getTime();
            try {
                ResultSetWrapper	rs	=	con.executeQuery(this.getOpenCloseTimeSQL());

                if(rs.next()) {
                    setOpenHour(rs.getInt("open_hour"));
                    setOpenMinute(rs.getInt("open_minute"));
                    setCloseHour(rs.getInt("close_hour"));
                    setCloseMinute(rs.getInt("close_minute"));
                }

                rs.close();
            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }

        private String getOpenCloseTimeSQL() {

            StringBuilder sb = new StringBuilder();

            String dateStr     = DateUtil.format(fromDate, "yyyy/MM/dd");
            String toDateStr;
            if( toDate == null ){
                //�����̊J�X���Ԃ��擾
                GregorianCalendar	nextDate	=	new GregorianCalendar();
                nextDate.setTime(fromDate);
                nextDate.add(nextDate.DAY_OF_MONTH, 1);
                toDateStr   = DateUtil.format(nextDate.getTime(), "yyyy/MM/dd");
            }else{
                toDateStr   = DateUtil.format(toDate, "yyyy/MM/dd");
            }

            sb.append(" SELECT");
            sb.append("     date_part('hour', open_time) as open_hour");
            sb.append("    ,date_part('minute', open_time) as open_minute");
            sb.append("    ,date_part('hour', close_time) +");
            sb.append("            case when date_part('day', open_time) != date_part('day', close_time) then");
            sb.append("                24 else 0 ");
            sb.append("            end as close_hour");
            sb.append("    ,date_part('minute', close_time) as close_minute");
            sb.append(" FROM");
            sb.append("    (");
            sb.append("        SELECT");
            sb.append("             max(case when drd.open_time < ms.open_time then drd.open_time else ms.open_time end) as open_time");
            sb.append("            ,max(case when drd.close_time > ms.close_time then drd.close_time else ms.close_time end) as close_time");
            sb.append("        FROM");
            sb.append("            (");
            sb.append("                SELECT");
            sb.append("                     ms.shop_id");
            sb.append("                    ,ms.term");
            sb.append("                    ," + SQLUtil.convertForSQL(dateStr) + "::timestamp +");
            sb.append("                            cast(ms.open_hour || ' hours' as interval) +");
            sb.append("                            cast(ms.open_minute || ' minutes' as interval) as open_time");
            sb.append("                    ," + SQLUtil.convertForSQL(dateStr) + "::timestamp +");
            //sb.append("                            case when ms.close_hour < 24 then");
            //sb.append("                                cast('0 days' as interval) ");
            //sb.append("                            else");
            //sb.append("                                cast('1 days' as interval) ");
            //sb.append("                            end +");
            sb.append("                            cast(ms.close_hour || ' hours' as interval) +");
            sb.append("                            cast(ms.close_minute || ' minutes' as interval) as close_time");
            sb.append("                FROM");
            sb.append("                    mst_shop ms");
            sb.append("                WHERE");
            sb.append("                    ms.shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID()));
            sb.append("            ) ms");
            sb.append("            left outer join");
            sb.append("            (");
            sb.append("                select");
            sb.append("                    drd.shop_id ");
            sb.append("                    ,min(drd.reservation_datetime ");
            sb.append("                     - date_trunc('day', ( drd.reservation_datetime +");
            sb.append("                            cast((coalesce(drd.operation_time, mt.operation_time) || ' minutes') as interval) -");
            sb.append("                            ( " + SQLUtil.convertForSQL(dateStr) + "::timestamp +");
            sb.append("                              cast(ms.open_hour || ' hours' as interval) + ");
            sb.append("                              cast(ms.open_minute || ' minutes' as interval) ) ");
            sb.append("                        ) ) )");
            sb.append("                    as open_time ");
            sb.append("                    ,max(drd.reservation_datetime + cast((coalesce(drd.operation_time, mt.operation_time) || ' minutes') as interval) ");
            sb.append("                     - date_trunc('day', ( drd.reservation_datetime +");
            sb.append("                            cast((coalesce(drd.operation_time, mt.operation_time) || ' minutes') as interval) -");
            sb.append("                            ( " + SQLUtil.convertForSQL(dateStr) + "::timestamp + ");
            sb.append("                              cast(ms.open_hour || ' hours' as interval) + ");
            sb.append("                              cast(ms.open_minute || ' minutes' as interval) ) ");
            sb.append("                        ) ) )");
            sb.append("                    as close_time ");
            sb.append("                FROM ");
            sb.append("                    mst_shop ms, ");
            sb.append("                    data_reservation_detail drd ");
            sb.append("                        inner join mst_technic mt ");
            sb.append("                        on mt.technic_id = drd.technic_id ");
            sb.append("                WHERE ");
            sb.append("                    ms.shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID()));
            sb.append("                AND drd.delete_date is null ");
            sb.append("                AND drd.shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID()));
            sb.append("                AND drd.reservation_datetime + cast((coalesce(drd.operation_time, mt.operation_time) || ' minutes') as interval) between");
            sb.append("                        ( ");
            sb.append("                            SELECT  " + SQLUtil.convertForSQL(dateStr) + "::timestamp +");
            sb.append("                                            cast(ms.open_hour   || ' hours'   as interval) +");
            sb.append("                                            cast(ms.open_minute || ' minutes' as interval) ");
            sb.append("                            FROM  mst_shop ms ");
            sb.append("                            WHERE ms.shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID()));
            sb.append("                        ) ");
            sb.append("                        and ");
            sb.append("                        ( ");
            sb.append("                            SELECT  " + SQLUtil.convertForSQL(toDateStr) + "::timestamp +");
            sb.append("                                            cast(ms.open_hour   || ' hours'   as interval) +");
            sb.append("                                            cast(ms.open_minute || ' minutes' as interval) ");
            sb.append("                            FROM   mst_shop ms ");
            sb.append("                            WHERE  ms.shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID()));
            sb.append("                        ) ");
            sb.append("                group by ");
            sb.append("                    drd.shop_id ");
            sb.append("            ) drd");
            sb.append("            on drd.shop_id = ms.shop_id");
            sb.append("    ) ms");

            return sb.toString();

            /*
            String sql = "";
            sql += " SELECT";
            sql += "      date_part('hour', open_time) as open_hour";
            sql += "     ,date_part('minute', open_time) as open_minute";
            sql += "     ,date_part('hour', close_time) + case when date_part('day', open_time) != date_part('day', close_time) then 24 else 0 end as close_hour";
            sql += "     ,date_part('minute', close_time) as close_minute";
            sql += " FROM";
            sql += "     (";
            sql += "         SELECT";
            sql += "              max(case when drd.open_time < ms.open_time then drd.open_time else ms.open_time end) as open_time";
            sql += "             ,max(case when drd.close_time > ms.close_time then drd.close_time else ms.close_time end) as close_time";
            sql += "         FROM";
            sql += "             (";
            sql += "                 SELECT";
            sql += "                      ms.shop_id";
            sql += "                     ,ms.term";
            sql += "                     ,to_timestamp(" + SQLUtil.convertForSQLDateOnly(fromDate) + ", 'YYYY/MM/DD') + cast(ms.open_hour || ' hours' as interval) + cast(ms.open_minute || ' minutes' as interval) as open_time";
            sql += "                     ,to_timestamp(" + SQLUtil.convertForSQLDateOnly(fromDate) + ", 'YYYY/MM/DD') + case when ms.close_hour < 24 then cast('0 days' as interval) else cast('1 days' as interval) end + cast(ms.close_hour || ' hours' as interval) + cast(ms.close_minute || ' minutes' as interval) as close_time";
            sql += "                 FROM";
            sql += "                     mst_shop ms";
            sql += "                 WHERE";
            sql += "                     ms.shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID());
            sql += "             ) ms";
            sql += "             left outer join";
            sql += "                 (";
            sql += "                     SELECT";
            sql += "                          drd.shop_id";
            sql += "                         ,drd.reservation_datetime as open_time";
            sql += "                         ,drd.reservation_datetime + cast((mt.operation_time || ' minutes') as interval) as close_time";
            sql += "                     FROM";
            sql += "                         data_reservation_detail drd";
            sql += "                             inner join mst_technic mt";
            sql += "                                     on mt.technic_id = drd.technic_id";
            sql += "                     WHERE";
            sql += "                             drd.delete_date is null";
            sql += "                         AND drd.shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID());
            sql += "                         AND drd.reservation_datetime + cast((mt.operation_time || ' minutes') as interval) between";
            sql += "                                 (";
            sql += "                                     SELECT to_timestamp(" + SQLUtil.convertForSQLDateOnly(fromDate) + ", 'YYYY/MM/DD') + cast(ms.open_hour || ' hours' as interval) + cast(ms.open_minute || ' minutes' as interval) as open_time";
            sql += "                                       FROM mst_shop ms";
            sql += "                                      WHERE ms.shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID());
            sql += "                                 )";
            sql += "                                 and";
            sql += "                                 (";
            sql += "                                     SELECT to_timestamp(" + SQLUtil.convertForSQLDateOnly(fromDate) + ", 'YYYY/MM/DD') + cast(ms.open_hour || ' hours' as interval) + cast(ms.open_minute || ' minutes' as interval) + '1 days' as open_time";
            sql += "                                       FROM mst_shop ms";
            sql += "                                      WHERE ms.shop_id = " + SQLUtil.convertForSQL(this.getSelectedShopID());
            sql += "                                 )";
            sql += "                 ) drd";
            sql += "                 on drd.shop_id = ms.shop_id";
            sql += "                and drd.close_time between ms.open_time and (ms.open_time + '1 days')";
            sql += "     ) ms";

            return sql;
             */
        }    
        
        public Integer getTerm() {
            return term;
        }

        public void setTerm(Integer term) {
            this.term = term;
        }

        public Integer getOpenHour() {
            return openHour;
        }

        public void setOpenHour(Integer openHour) {
            this.openHour = openHour;
        }

        public Integer getOpenMinute() {
            return openMinute;
        }

        public void setOpenMinute(Integer openMinute) {
            this.openMinute = openMinute;
        }

        public Integer getCloseHour() {
            return closeHour;
        }

        public void setCloseHour(Integer closeHour) {
            this.closeHour = closeHour;
        }

        public Integer getCloseMinute() {
            return closeMinute;
        }

        public void setCloseMinute(Integer closeMinute) {
            this.closeMinute = closeMinute;
        }
}