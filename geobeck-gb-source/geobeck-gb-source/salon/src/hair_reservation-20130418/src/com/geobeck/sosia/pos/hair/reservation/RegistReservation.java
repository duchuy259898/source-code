/*
 * RegistReservation.java
 *
 * Created on 2006/06/21, 11:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.reservation;

import com.geobeck.sosia.pos.hair.data.account.ConsumptionCourse;
import com.geobeck.sosia.pos.hair.data.account.ConsumptionCourseClass;
import com.geobeck.sosia.pos.hair.data.account.Course;
import com.geobeck.sosia.pos.hair.data.account.CourseClass;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.hair.data.reservation.*;
import com.geobeck.sosia.pos.hair.master.company.MstBed;
import com.geobeck.sosia.pos.hair.master.product.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.hair.data.product.*;

/**
 * �\��o�^����
 * @author katagiri
 */
public class RegistReservation
{
	/**
	 * �X��
	 */
	protected	MstShop				shop		=	new MstShop();
	/**
	 * �S�X�^�b�t
	 */
	protected	Vector<MstStaff>	staffs		=	new Vector<MstStaff>();
	/**
	 * �S�x�b�h
	 */
	protected	Vector<MstBed>		beds		=	new Vector<MstBed>();
	
	/**
	 * �ڋq
	 */
	protected	MstCustomer			customer	=	new MstCustomer();
	/**
	 * �\���
	 */
	protected	GregorianCalendar	date		=	new	GregorianCalendar();
	/**
	 * �\��
	 */
	protected	DataReservation		reservation	=	new DataReservation();
	
	/** Creates a new instance of RegistReservation */
	public RegistReservation()
	{
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
		reservation.setShop(this.shop);
	}

	/**
	 * �S�X�^�b�t���擾����B
	 * @return �S�X�^�b�t
	 */
	public Vector<MstStaff> getStaffs()
	{
		return staffs;
	}

	/**
	 * �S�X�^�b�t��ݒ肷��B
	 * @param staffs �S�X�^�b�t
	 */
	public void setStaffs(Vector<MstStaff> staffs)
	{
		this.staffs = staffs;
	}

	/**
	 * �S�x�b�h���擾����B
	 * @return �S�x�b�h
	 */
	public Vector<MstBed> getBeds()
	{
		return beds;
	}

	/**
	 * �S�x�b�h��ݒ肷��B
	 * @param beds �S�x�b�h
	 */
	public void setBeds(Vector<MstBed> beds)
	{
		this.beds = beds;
	}

	/**
	 * �ڋq���擾����B
	 * @return �ڋq
	 */
	public MstCustomer getCustomer()
	{
		return customer;
	}

	/**
	 * �ڋq��ݒ肷��B
	 * @param customer �ڋq
	 */
	public void setCustomer(MstCustomer customer)
	{
		this.customer = customer;
		
		reservation.setCustomer(customer);
	}

	/**
	 * �\������擾����B
	 * @return �\���
	 */
	public GregorianCalendar getDate()
	{
		return date;
	}

	/**
	 * �\�����ݒ肷��B
	 * @param date �\���
	 */
	public void setDate(GregorianCalendar date)
	{
		this.date = date;
	}

	/**
	 * �\����擾����B
	 * @return �\��
	 */
	public DataReservation getReservation()
	{
		return reservation;
	}

	/**
	 * �\��̃��X�g��ݒ肷��B
	 * @param reservations �\��̃��X�g
	 */
	public void setReservation(DataReservation reservation)
	{
		this.reservation = reservation;
	}
	
	/**
	 * �������������s���B
	 */
	public void init()
	{
		ConnectionWrapper	con	=	SystemInfo.getConnection();

		this.loadStaffs(con);
		this.loadBeds(con);
		
		this.clear();
	}
	
	/**
	 * �S�X�^�b�t���f�[�^�x�[�X����ǂݍ��ށB
	 * @param con ConnectionWrapper
	 */
	private void loadStaffs(ConnectionWrapper con)
	{
		staffs.clear();
		
		try
		{
			ResultSetWrapper	rs	=	con.executeQuery(this.getLoadStaffsSQL());

			while(rs.next())
			{
				MstStaff	staff	=	new MstStaff();
				staff.setData(rs);
				staffs.add(staff);
			}

			rs.close();
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * �S�x�b�h���f�[�^�x�[�X����ǂݍ��ށB
	 * @param con ConnectionWrapper
	 */
	private void loadBeds(ConnectionWrapper con)
	{
		beds.clear();
		
		try
		{
			ResultSetWrapper	rs	=	con.executeQuery(this.getLoadBedsSQL());

			while(rs.next())
			{
				MstBed	bed	=	new MstBed();
				bed.setData(rs);
				beds.add(bed);
			}

			rs.close();
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * �f�[�^���N���A����B
	 */
	public void clear()
	{
//		customer	=	new MstCustomer();
		reservation	=	new DataReservation();
		reservation.setShop(shop);
	}
	
	/**
	 * �폜�������s���B
	 * @param index �Z�p�̃C���f�b�N�X
	 */
	public void deleteTechnic(int index)
	{
		if(0 <= index && index < reservation.size())
		{
                        if (reservation.get(index).getCourse() != null) {
        			reservation.setTotalTime(reservation.getTotalTime() - reservation.get(index).getCourse().getOperationTime());
                        } else if (reservation.get(index).getConsumptionCourse() != null) {
        			reservation.setTotalTime(reservation.getTotalTime() - reservation.get(index).getConsumptionCourse().getOperationTime());
                        } else {
        			reservation.setTotalTime(reservation.getTotalTime() - reservation.get(index).getTechnic().getOperationTime());
                        }
			reservation.remove(index);
		}
	}
	
	public boolean delete()
	{
		boolean	result	=	false;
		
		if(this.getReservation() != null)
		{
			if(this.getReservation().getReservationNo() != null)
			{
				ConnectionWrapper	con	=	SystemInfo.getConnection();

				try
				{
					con.begin();
					
					if(this.getReservation().delete(con))
					{
						con.commit();
						result	=	true;
					}
					else
					{
						con.rollback();
					}
				}
				catch(SQLException e)
				{
					SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
		}
		
		return	result;
	}
	
	
	/**
	 * �\��f�[�^��ǂݍ��ށB
	 * @param reservationNo �\��ԍ��̔z��
	 * @return true - �ǂݍ��ݐ���
	 */
	public boolean loadReservation(Integer reservationNo)
	{
		MstStaff staff = null;
		
		this.clear();
		
		ConnectionWrapper con = SystemInfo.getConnection();
		
		try
		{
			ResultSetWrapper rs = con.executeQuery(this.getLoadReservationSQL(reservationNo));
			
			while(rs.next())
			{
				//�P���ڂ̃f�[�^�̏ꍇ
				if(reservation.size() == 0)
				{
					reservation		=	new DataReservation();
					reservation.setDesignated( rs.getBoolean( "charge_staff_designated_flag" ) );
					staff = new MstStaff();
					staff.setStaffID(rs.getInt("charge_staff_id"));
					staff.setStaffNo(rs.getString("charge_staff_no"));
					staff.setStaffName(0, rs.getString("charge_staff_name1") );
					staff.setStaffName(1, rs.getString("charge_staff_name2") );
					reservation.setStaff(staff);
					reservation.setShop(shop);
					reservation.setReservationNo(rs.getInt("reservation_no"));
					customer.setCustomerID(rs.getInt("customer_id"));
					customer.setCustomerNo(rs.getString("customer_no"));
					customer.setCustomerName(0, rs.getString("customer_name1"));
					customer.setCustomerName(1, rs.getString("customer_name2"));
					reservation.setCustomer(customer);
					reservation.setStatus(rs.getInt("status"));
					reservation.setSubStatus(rs.getInt("sub_status"));
					reservation.setComment(rs.getString("comment"));
					reservation.setNextFlag(rs.getInt("next_flag"));
					reservation.setPreorderFlag(rs.getInt("preorder_flag"));

					staff = new MstStaff();
					staff.setStaffID(rs.getInt("reg_staff_id"));
					staff.setStaffNo(rs.getString("reg_staff_no"));
					staff.setStaffName(0, rs.getString("reg_staff_name1") );
					staff.setStaffName(1, rs.getString("reg_staff_name2") );
					reservation.setRegStaff(staff);

					reservation.setInsertDate(rs.getTimestamp("insert_date"));
					reservation.setUpdateDate(rs.getTimestamp("update_date"));
					date.setTime(rs.getDate("reservation_datetime"));
				}
				
				DataReservationDetail drd = new DataReservationDetail();
				
				drd.setReservation(reservation);
				drd.setReservationDetailNo(rs.getInt("reservation_detail_no"));
				drd.getReservationDatetime().setTime(rs.getTimestamp("reservation_datetime"));
				drd.setDesignated( rs.getBoolean( "menu_staff_designated_flag" ) );
                                Object courseFlg = rs.getObject("course_flg");

                                if (courseFlg == null) {
                                    //�Z�p�̏ꍇ
                                    drd.setCourseFlg(null);
                                    
                                    MstTechnicClass	technicClass	=	new MstTechnicClass();
                                    technicClass.setTechnicClassID(rs.getInt("technic_class_id"));
                                    technicClass.setTechnicClassName(rs.getString("technic_class_name"));

                                    MstTechnic technic = new MstTechnic();
                                    technic.setTechnicClass(technicClass);
                                    technic.setTechnicID(rs.getInt("technic_id"));
                                    technic.setTechnicNo(rs.getString("technic_no"));
                                    technic.setTechnicName(rs.getString("technic_name"));
                                    technic.setOperationTime(rs.getInt("operation_time"));
                                    drd.setTechnic(technic);
                                } else if ((Integer)courseFlg == 1) {
                                    //�R�[�X�_��̏ꍇ
                                    drd.setCourseFlg((Integer)courseFlg);
                                    
                                    CourseClass courseClass = new CourseClass();
                                    courseClass.setCourseClassId(rs.getInt("technic_class_id"));
                                    courseClass.setCourseClassName(rs.getString("technic_class_name"));

                                    Course course = new Course();
                                    course.setCourseClass(courseClass);
                                    course.setCourseId(rs.getInt("technic_id"));
                                    course.setCourseName(rs.getString("technic_name"));
                                    course.setOperationTime(rs.getInt("operation_time"));
                                    drd.setCourse(course);
                                } else if ((Integer)courseFlg == 2) {
                                    //�����R�[�X�̏ꍇ
                                    drd.setCourseFlg((Integer)courseFlg);

                                    ConsumptionCourseClass consumptionCourseClass = new ConsumptionCourseClass();
                                    consumptionCourseClass.setCourseClassId(rs.getInt("technic_class_id"));
                                    consumptionCourseClass.setCourseClassName(rs.getString("technic_class_name"));

                                    ConsumptionCourse consumptionCourse = new ConsumptionCourse();
                                    consumptionCourse.setConsumptionCourseClass(consumptionCourseClass);
                                    consumptionCourse.setCourseId(rs.getInt("technic_id"));
                                    consumptionCourse.setCourseName(rs.getString("technic_name"));
                                    consumptionCourse.setOperationTime(rs.getInt("operation_time"));
                                    consumptionCourse.setContractNo(rs.getInt("contract_no"));
                                    consumptionCourse.setContractDetailNo(rs.getInt("contract_detail_no"));
                                    drd.setConsumptionCourse(consumptionCourse);
                                }
				
				staff = new MstStaff();
				staff.setStaffID(rs.getInt("staff_id"));
				staff.setStaffNo(rs.getString("staff_no"));
				staff.setStaffName(0, "staff_name1");
				staff.setStaffName(1, "staff_name2");
				drd.setStaff(staff);
				
				MstBed bed = new MstBed();
				bed.setBedID(rs.getInt("bed_id"));
				bed.setBedName(rs.getString("bed_name"));
				drd.setBed(bed);
				
				// ���f�[�^���擾����
				loadReservationProportionally( drd );
				
				reservation.add(drd);
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	true;
	}
	
	/**
	 * ���\��f�[�^���擾����
	 */
	private boolean loadReservationProportionally( DataReservationDetail drd )
	{
		// �\��ڍ׃f�[�^���̈��f�[�^���N���A����
		drd.clear();
		
		ConnectionWrapper	con	=	SystemInfo.getConnection();
		
		try
		{
			ResultSetWrapper	rs	=	con.executeQuery(
					this.loadReservationProportionallySQL( drd.getReservationNo(), drd.getReservationDetailNo(), drd.getTechnic().getTechnicID() ) );
			
			while(rs.next())
			{
				DataReservationProportionally drp = new DataReservationProportionally();
				drp.setReservationDetail( drd );
				DataProportionally proportionally = new DataProportionally();
				proportionally.setDataProportionallyID( rs.getInt( "data_proportionally_id" ) );
				proportionally.setTechnic( drd.getTechnic() );
				MstProportionally mp = new MstProportionally();
				mp.setProportionallyID(    rs.getInt("proportionally_id") );
				mp.setProportionallyName(  rs.getString("proportionally_name") );
				mp.setProportionallyPoint( rs.getInt("proportionally_point") );
				mp.setDisplaySeq(          rs.getInt("display_seq") );
				proportionally.setProportionally( mp );
				proportionally.setRatio( rs.getInt( "proportionally_ratio" ) );
				drp.setProportionally( proportionally );
				drp.setDesignated( rs.getBoolean( "designated_flag" ) );
				MstStaff	staff			=	new MstStaff();
				staff.setStaffID(rs.getInt("staff_id"));
				staff.setStaffNo(rs.getString("staff_no"));
				staff.setStaffName(0, "staff_name1");
				staff.setStaffName(1, "staff_name2");
				drp.setStaff( staff );
				drd.add( drp );
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return true;
	}
	
	/**
	 * �\�񏈗����s���B
	 * @return true - ����
	 */
	public boolean reserve()
	{
		try
		{
			reservation.setStatus(1);
			// �{�p�J�n�\�񎞊Ԃ�o�^����
			
			
			ConnectionWrapper con = SystemInfo.getConnection();
			
			try
			{
				con.begin();
				
//				if(!this.registCommon(con, false))
				if(!this.registCommon(con))
				{
					return	false;
				}

				con.commit();
			}
			catch(SQLException e)
			{
				con.rollback();
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
				return	false;
			}
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	true;
	}
	
	/**
	 * ��t�������s���B
	 * @return true - ����
	 */
	public boolean receipt()
	{
		try
		{
                        if (reservation.getStatus() < 3) {
                            reservation.setStatus(2);
                            reservation.setSubStatus(1);
                        }
			
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			try
			{
				con.begin();
				
//				if(!this.registCommon(con, true))
				if(!this.registCommon(con))
				{
					con.rollback();
					return	false;
				}
				
				con.commit();
			}
			catch(SQLException e)
			{
				con.rollback();
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
				return	false;
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	true;
	}
	
//	private boolean registCommon(ConnectionWrapper con, boolean isStay) throws SQLException
	private boolean registCommon(ConnectionWrapper con) throws SQLException
	{
		if(customer.getCustomerNo().equals("0") || customer.getCustomerID() == null)
		{
			if(SystemInfo.getSetteing().isShareCustomer())
			{
				customer.setShop(new MstShop());
				customer.getShop().setShopID(0);
			}
			else
			{
				customer.setShop(shop);
			}

//			customer.setCustomerID(null);

                        // ���Ȗ��������͂̏ꍇ�͖��O�Ɠ������̂��Z�b�g����
                        if (customer.getCustomerKana(0).length() == 0) customer.setCustomerKana(0, customer.getCustomerName(0));
                        if (customer.getCustomerKana(1).length() == 0) customer.setCustomerKana(1, customer.getCustomerName(1));

			if(!customer.regist(con))
			{
				con.rollback();
				return	false;
			}

//			customer.setCustomerID(MstCustomer.getMaxCustomerID(con));

			if(customer.getCustomerID() == null)
			{
				con.rollback();
				return	false;
			}
		}

		reservation.setShop(shop);
		reservation.setCustomer(customer);

//		if(!reservation.regist(con, isStay))
		if(!reservation.regist(con))
		{
			con.rollback();
			return	false;
		}

		if(reservation.getReservationNo() == null)
		{
			Integer		maxNo	=	this.getMaxReservationNo(con);

			if(maxNo == null)
			{
				con.rollback();
				return	false;
			}

			reservation.setReservationNo(maxNo);
		}

		if(!reservation.deleteDetail(con))
		{
			con.rollback();
			return	false;
		}
		// ���f�[�^���폜����
		if( !reservation.deleteProportionally( con ) )
		{
			con.rollback();
			return false;
		}

		for(DataReservationDetail drd : reservation)
		{
			drd.setReservation(reservation);

			if(!drd.regist(con))
			{
				con.rollback();
				return	false;
			}
		}
		
		return	true;
	}
	
	
	/**
	 * �\��ԍ��̍ő�l�i�o�^�����\��ԍ��j���擾����B
	 * @param con ConnectionWrapper
	 * @return �\��ԍ��̍ő�l�i�o�^�����\��ԍ��j
	 * @throws java.sql.SQLException SQLException
	 */
	private Integer	getMaxReservationNo(ConnectionWrapper con) throws SQLException
	{
		Integer		maxNo	=	null;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getMaxReservationNoSQL());
		
		if(rs.next())
		{
			maxNo	=	rs.getInt("max_no");
		}
		
		return	maxNo;
	}
	
	/**
	 * �S�X�^�b�t���擾����r�p�k�����擾����B
	 * @return �S�X�^�b�t���擾����r�p�k��
	 */
	private String getLoadStaffsSQL()
	{
		return	"select *\n" +
			"from mst_staff ms\n" +
			"where ms.delete_date is null\n" +
			"order by ms.staff_id\n";
	}
	
	/**
	 * �S�x�b�h���擾����r�p�k�����擾����B
	 * @return �S�x�b�h���擾����r�p�k��
	 */
	private String getLoadBedsSQL()
	{
		return	"select *\n" +
			"from mst_bed mb\n" +
			"where mb.delete_date is null\n" +
			"and mb.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
			"order by mb.display_seq\n";
	}
	
	/**
	 * �\��f�[�^���擾����r�p�k�����擾����B
	 * @param reservationNo �\��ԍ��̔z��
	 * @return �\��f�[�^���擾����r�p�k��
	 */
	private String getLoadReservationSQL(Integer reservationNo)
	{
		return	"select dr.shop_id,\n" +
				"dr.reservation_no,\n" +
				"dr.designated_flag as charge_staff_designated_flag,\n" +
				"cms.staff_id as charge_staff_id,\n" +
				"cms.staff_no as charge_staff_no,\n" +
				"cms.staff_name1 as charge_staff_name1,\n" +
				"cms.staff_name2 as charge_staff_name2,\n" +
				"drd.reservation_detail_no,\n" +
				"drd.reservation_datetime,\n" +
				"drd.designated_flag as menu_staff_designated_flag,\n" +
				"dr.customer_id,\n" +
				"mc.customer_no,\n" +
				"mc.customer_name1,\n" +
				"mc.customer_name2,\n" +
				"mt.technic_class_id,\n" +
				"mtc.technic_class_name,\n" +
				"drd.technic_id,\n" +
				"mt.technic_no,\n" +
				"mt.technic_name || case when mt.technic_no like 'mo-rsv-%' then '�i�\��p�j' else '' end as technic_name,\n" +
				"coalesce(drd.operation_time, mt.operation_time) as operation_time,\n" +
				"drd.staff_id,\n" +
				"ms.staff_no,\n" +
				"ms.staff_name1,\n" +
				"ms.staff_name2,\n" +
				"drd.bed_id,\n" +
				"mb.bed_name,\n" +
				"dr.status,\n" +
				"dr.sub_status,\n" +
				"dr.comment,\n" +
				"dr.next_flag,\n" +
				"dr.preorder_flag,\n" +
				"rms.staff_id as reg_staff_id,\n" +
				"rms.staff_no as reg_staff_no,\n" +
				"rms.staff_name1 as reg_staff_name1,\n" +
				"rms.staff_name2 as reg_staff_name2,\n" +
				"dr.insert_date,\n" +
				"dr.update_date,\n" +
                                "drd.contract_no ,\n" +
                                "drd.contract_detail_no ,\n" +
                                "drd.course_flg\n" +
				"from data_reservation dr\n" +
				"left outer join mst_staff as cms on\n" +
				"dr.staff_id = cms.staff_id\n" +
				"left outer join mst_staff as rms on\n" +
				"dr.reg_staff_id = rms.staff_id\n" +
				"inner join data_reservation_detail drd\n" +
				"on drd.shop_id = dr.shop_id \n" +
				"and drd.reservation_no = dr.reservation_no\n" +
				"and (dr.mobile_flag = 3 or drd.delete_date is null)\n" +
				"left outer join mst_customer mc\n" +
				"on mc.customer_id = dr.customer_id\n" +
				"left outer join mst_Technic mt\n" +
				"on mt.technic_id = drd.technic_id\n" +
				"left outer join mst_technic_class mtc\n" +
				"on mtc.technic_class_id = mt.technic_class_id\n" +
				"left outer join mst_staff ms\n" +
				"on ms.staff_id = drd.staff_id\n" +
				"left outer join mst_bed mb\n" +
				"on mb.shop_id = dr.shop_id\n" +
				"and mb.bed_id = drd.bed_id\n" +
				"where dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and dr.reservation_no = " + reservationNo.toString() + "\n" +
                                "and drd.course_flg is null\n" +

                                //�R�[�X�_��
                                "union all\n" +
                                "select dr.shop_id,\n" +
				"dr.reservation_no,\n" +
				"dr.designated_flag as charge_staff_designated_flag,\n" +
				"cms.staff_id as charge_staff_id,\n" +
				"cms.staff_no as charge_staff_no,\n" +
				"cms.staff_name1 as charge_staff_name1,\n" +
				"cms.staff_name2 as charge_staff_name2,\n" +
				"drd.reservation_detail_no,\n" +
				"drd.reservation_datetime,\n" +
				"drd.designated_flag as menu_staff_designated_flag,\n" +
				"dr.customer_id,\n" +
				"mc.customer_no,\n" +
				"mc.customer_name1,\n" +
				"mc.customer_name2,\n" +
				"c.course_class_id as technic_class_id,\n" +
				"cc.course_class_name as technic_class_name,\n" +
				"drd.technic_id,\n" +
				"'' as technic_no,\n" +
				"c.course_name as technic_name,\n" +
				"coalesce(drd.operation_time, c.operation_time) as operation_time,\n" +
				"drd.staff_id,\n" +
				"ms.staff_no,\n" +
				"ms.staff_name1,\n" +
				"ms.staff_name2,\n" +
				"drd.bed_id,\n" +
				"mb.bed_name,\n" +
				"dr.status,\n" +
				"dr.sub_status,\n" +
				"dr.comment,\n" +
				"dr.next_flag,\n" +
				"dr.preorder_flag,\n" +
				"rms.staff_id as reg_staff_id,\n" +
				"rms.staff_no as reg_staff_no,\n" +
				"rms.staff_name1 as reg_staff_name1,\n" +
				"rms.staff_name2 as reg_staff_name2,\n" +
				"dr.insert_date,\n" +
				"dr.update_date,\n" +
                                "drd.contract_no ,\n" +
                                "drd.contract_detail_no ,\n" +
                                "drd.course_flg\n" +
				"from data_reservation dr\n" +
				"left outer join mst_staff as cms on\n" +
				"dr.staff_id = cms.staff_id\n" +
				"left outer join mst_staff as rms on\n" +
				"dr.reg_staff_id = rms.staff_id\n" +
				"inner join data_reservation_detail drd\n" +
				"on drd.shop_id = dr.shop_id \n" +
				"and drd.reservation_no = dr.reservation_no\n" +
				"and (dr.mobile_flag = 3 or drd.delete_date is null)\n" +
				"left outer join mst_customer mc\n" +
				"on mc.customer_id = dr.customer_id\n" +
				"left outer join mst_course c\n" +
				"on c.course_id = drd.technic_id\n" +
				"left outer join mst_course_class cc\n" +
				"on cc.course_class_id = c.course_class_id\n" +
				"left outer join mst_staff ms\n" +
				"on ms.staff_id = drd.staff_id\n" +
				"left outer join mst_bed mb\n" +
				"on mb.shop_id = dr.shop_id\n" +
				"and mb.bed_id = drd.bed_id\n" +
				"where dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and dr.reservation_no = " + reservationNo.toString() + "\n" +
                                "and drd.course_flg = 1\n" +

                                //�����R�[�X
                                "union all\n" +
                                "select distinct dr.shop_id,\n" +
				"dr.reservation_no,\n" +
				"dr.designated_flag as charge_staff_designated_flag,\n" +
				"cms.staff_id as charge_staff_id,\n" +
				"cms.staff_no as charge_staff_no,\n" +
				"cms.staff_name1 as charge_staff_name1,\n" +
				"cms.staff_name2 as charge_staff_name2,\n" +
				"drd.reservation_detail_no,\n" +
				"drd.reservation_datetime,\n" +
				"drd.designated_flag as menu_staff_designated_flag,\n" +
				"dr.customer_id,\n" +
				"mc.customer_no,\n" +
				"mc.customer_name1,\n" +
				"mc.customer_name2,\n" +
				"c.course_class_id as technic_class_id,\n" +
				"cc.course_class_name as technic_class_name,\n" +
				"drd.technic_id,\n" +
				"'' as technic_no,\n" +
				"c.course_name as technic_name,\n" +
				"coalesce(drd.operation_time, c.operation_time) as operation_time,\n" +
				"drd.staff_id,\n" +
				"ms.staff_no,\n" +
				"ms.staff_name1,\n" +
				"ms.staff_name2,\n" +
				"drd.bed_id,\n" +
				"mb.bed_name,\n" +
				"dr.status,\n" +
				"dr.sub_status,\n" +
				"dr.comment,\n" +
				"dr.next_flag,\n" +
				"dr.preorder_flag,\n" +
				"rms.staff_id as reg_staff_id,\n" +
				"rms.staff_no as reg_staff_no,\n" +
				"rms.staff_name1 as reg_staff_name1,\n" +
				"rms.staff_name2 as reg_staff_name2,\n" +
				"dr.insert_date,\n" +
				"dr.update_date,\n" +
                                "drd.contract_no ,\n" +
                                "drd.contract_detail_no ,\n" +
                                "drd.course_flg\n" +
				"from data_reservation dr\n" +
				"left outer join mst_staff as cms on\n" +
				"dr.staff_id = cms.staff_id\n" +
				"left outer join mst_staff as rms on\n" +
				"dr.reg_staff_id = rms.staff_id\n" +
				"inner join data_reservation_detail drd\n" +
				"on drd.shop_id = dr.shop_id \n" +
				"and drd.reservation_no = dr.reservation_no\n" +
				"and (dr.mobile_flag = 3 or drd.delete_date is null)\n" +
				"left outer join mst_customer mc\n" +
				"on mc.customer_id = dr.customer_id\n" +
                                "left outer join data_contract_digestion dcd\n" +
                                "on dcd.shop_id = drd.shop_id and dcd.contract_no = drd.contract_no and dcd.contract_detail_no = drd.contract_detail_no\n" +
                                "left outer join mst_course c\n" +
                                "on c.course_id = drd.technic_id\n" +
				"left outer join mst_course_class cc\n" +
				"on cc.course_class_id = c.course_class_id\n" +
				"left outer join mst_staff ms\n" +
				"on ms.staff_id = drd.staff_id\n" +
				"left outer join mst_bed mb\n" +
				"on mb.shop_id = dr.shop_id\n" +
				"and mb.bed_id = drd.bed_id\n" +
				"where dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and dr.reservation_no = " + reservationNo.toString() + "\n" +
                                "and drd.course_flg = 2\n" +

				"order by reservation_datetime, reservation_detail_no\n";
	}
	
	private String loadReservationProportionallySQL( Integer reservationNo, Integer reservationDetailNo, Integer technicID )
	{
		return
			"select\n" +
			"mp.*,\n" +
			"ms.staff_id,\n" +
			"ms.staff_no,\n" +
			"ms.staff_name1,\n" +
			"ms.staff_name2,\n" +
			"dp.*,\n" +
			"drp.designated_flag\n" +
			"from\n" +
			"data_proportionally as dp\n" +
			"inner join mst_proportionally as mp on\n" +
			"mp.proportionally_id = dp.proportionally_id\n" +
			"left outer join data_reservation_proportionally as drp on\n" +
			"drp.delete_date is null\n" +
			"and drp.reservation_no = " + SQLUtil.convertForSQL( reservationNo ) + "\n" +
			"and drp.reservation_detail_no = " + SQLUtil.convertForSQL( reservationDetailNo ) + "\n" +
			"and drp.data_proportionally_id = dp.data_proportionally_id\n" +
			"left outer join mst_staff as ms on\n" +
			"ms.staff_id = drp.staff_id\n" +
			"and drp.data_proportionally_id = dp.data_proportionally_id\n" +
			"where\n" +
			"dp.delete_date is null\n" +
			"and dp.technic_id = " + SQLUtil.convertForSQL( technicID ) + "\n" +
			"order by\n" +
			"mp.display_seq, dp.data_proportionally_id\n" +
			";\n";
	}
	
	/**
	 * �\��ԍ��̍ő�l�i�o�^�����\��ԍ��j���擾����r�p�k�����擾����B
	 * @return �\��ԍ��̍ő�l�i�o�^�����\��ԍ��j���擾����r�p�k��
	 */
	private String getMaxReservationNoSQL()
	{
		return	"select max(dr.reservation_no) as max_no\n" +
				"from data_reservation dr\n" +
				"where dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n";
	}
}
