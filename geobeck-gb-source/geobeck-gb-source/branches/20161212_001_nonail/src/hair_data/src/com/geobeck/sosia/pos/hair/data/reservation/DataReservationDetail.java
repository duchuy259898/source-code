/*
 * DataReservationDetail.java
 *
 * Created on 2007/02/15, 10:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.reservation;

import com.geobeck.sosia.pos.hair.data.account.ConsumptionCourse;
import com.geobeck.sosia.pos.hair.data.account.Course;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.hair.master.company.MstBed;
import com.geobeck.sosia.pos.hair.master.product.MstTechnic;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 *
 * @author katagiri
 */
public class DataReservationDetail extends ArrayList<DataReservationProportionally>
{
	/**
	 * �\��
	 */
	protected	DataReservation		reservation			=	null;
	/**
	 * �\��ڍ�No.
	 */
	protected	Integer				reservationDetailNo	=	null;
	/**
	 * �\�����
	 */
	protected	GregorianCalendar	reservationDatetime	=	new GregorianCalendar();
	/**
	 * �Z�p
	 */
	protected	MstTechnic			technic				=	new MstTechnic();
	/**
	 * �{�p��
	 */
	protected	MstBed				bed					=	new MstBed();
	/**
	 * �w��
	 */
	protected	boolean				designated			= false;
	/**
	 * �X�^�b�t
	 */
	protected	MstStaff			staff				=	new MstStaff();

        /**
         * �R�[�X�_��E�����t���O
         * 1:�R�[�X�_��
         * 2:�����R�[�X
         */
        protected Integer courseFlg = null;

        /**
         * �R�[�X�_��ԍ�
         */
        protected Integer contractNo = null;

        /**
         * �R�[�X�_��ڍהԍ�
         */
        protected Integer contractDetailNo = null;

        /**
         * �R�[�X�_��
         */
        protected Course course = null;

        /**
         * �����R�[�X
         */
        protected ConsumptionCourse consumptionCourse = null;
        
        // start add 20130806 nakhoa
        /**
         * 
         */
        protected Integer contractShopId = null;
        // end add 20130806 nakhoa

	/** Creates a new instance of DataReservationDetail */
	public DataReservationDetail()
	{
	}

	/**
	 * �X�܂��擾����B
	 * @return �X��
	 */
	public DataReservation getReservation()
	{
		return reservation;
	}

	/**
	 * �\����Z�b�g����B
	 * @param reservation �\��
	 */
	public void setReservation(DataReservation reservation)
	{
		this.reservation = reservation;
	}

	/**
	 * �X�܂��擾����B
	 * @return �X��
	 */
	public MstShop getShop()
	{
		if(reservation != null)
		{
			return reservation.getShop();
		}
		else
		{
			return	null;
		}
	}
	
	/**
	 * �\��No.���擾����B
	 * @return �\��No.
	 */
	public Integer getReservationNo()
	{
		if(reservation != null)
		{
			return reservation.getReservationNo();
		}
		else
		{
			return	null;
		}
	}
	
	/**
	 * �\��No.���擾����B
	 * @return �\��No.
	 */
	public Integer getReservationDetailNo()
	{
		return reservationDetailNo;
	}

	/**
	 * �\��No.���Z�b�g����B
	 * @param reservationNo �\��No.
	 */
	public void setReservationDetailNo(Integer reservationDetailNo)
	{
		this.reservationDetailNo = reservationDetailNo;
	}

	/**
	 * �\��������擾����B
	 * @return �\�����
	 */
	public GregorianCalendar getReservationDatetime()
	{
		return reservationDatetime;
	}
	
	/**
	 * �\�񎞊Ԃ��擾����B
	 * @return �\�񎞊�
	 */
	public String getReservationTime(GregorianCalendar baseDate)
	{
//		if(baseDate.get(Calendar.DAY_OF_MONTH) == reservationDatetime.get(Calendar.DAY_OF_MONTH))
//		{
//			return	String.format("%1$tH:%1$tM", reservationDatetime);
//		}
//		else
//		{
//			return	String.format("%1$02d:%2$tM",
//					reservationDatetime.get(Calendar.HOUR_OF_DAY) + 24,
//					reservationDatetime);
//		}
		return	String.format("%1$tH:%1$tM", reservationDatetime);
	}
	
	/**
	 * �J�n-�I�����Ԃ��擾����
	 */
	public String getReservationAllTime( GregorianCalendar baseDate )
	{
		String sTime, fTime;
		GregorianCalendar wTime = (GregorianCalendar)reservationDatetime.clone();
		
//		if(baseDate.get(Calendar.DAY_OF_MONTH) == wTime.get(Calendar.DAY_OF_MONTH))
//		{
//			sTime = String.format("%1$tH:%1$tM", wTime);
//		}
//		else
//		{
//			sTime = String.format("%1$02d:%2$tM",
//					wTime.get(Calendar.HOUR_OF_DAY) + 24,
//					wTime);
//		}
		sTime = String.format("%1$tH:%1$tM", wTime);
		
		wTime.add( GregorianCalendar.MINUTE, this.getTechnic().getOperationTime() );
		
//		if(baseDate.get(Calendar.DAY_OF_MONTH) == wTime.get(Calendar.DAY_OF_MONTH))
//		{
//			fTime = String.format("%1$tH:%1$tM", wTime);
//		}
//		else
//		{
//			fTime = String.format("%1$02d:%2$tM",
//					wTime.get(Calendar.HOUR_OF_DAY) + 24,
//					wTime
//					);
//		}
		fTime = String.format("%1$tH:%1$tM", wTime);
		//return sTime + " - " + fTime + " (" + this.getTechnic().getOperationTime() + ")";
		//uchiyama
		return fTime ;
	}

	/**
	 * �\��������Z�b�g����B
	 * @param reservationDatetime �\�����
	 */
	public void setReservationDatetime(GregorianCalendar reservationDatetime)
	{
		this.reservationDatetime = reservationDatetime;
	}

	/**
	 * �\������Z�b�g����B
	 * @param reservationDate �\���
	 */
	public void setReservationDate(java.util.Date reservationDate)
	{
		if(this.reservationDatetime == null)
				this.reservationDatetime	=	new GregorianCalendar();
		this.reservationDatetime.setTime(reservationDate);
	}

	/**
	 * �Z�p���擾����B
	 * @return �Z�p
	 */
	public MstTechnic getTechnic()
	{
		return technic;
	}

	/**
	 * �Z�p���Z�b�g����B
	 * @param technic �Z�p
	 */
	public void setTechnic(MstTechnic technic)
	{
		this.technic = technic;
	}

        /**
         * �R�[�X�_����擾����
         * @return
         */
        public Course getCourse()
        {
            return course;
        }

        /**
         * �R�[�X�_����Z�b�g����
         * @param course
         */
        public void setCourse(Course course)
        {
            this.course = course;
        }

        /**
         * �����R�[�X���擾����
         * @return
         */
        public ConsumptionCourse getConsumptionCourse()
        {
            return consumptionCourse;
        }

        /**
         * �����R�[�X���Z�b�g����
         * @param consumptionCourse
         */
        public void setConsumptionCourse(ConsumptionCourse consumptionCourse)
        {
            this.consumptionCourse = consumptionCourse;
        }

	/**
	 * �{�p����擾����B
	 * @return �{�p��
	 */
	public MstBed getBed()
	{
		return bed;
	}

	/**
	 * �{�p����Z�b�g����B
	 * @param bed �{�p��
	 */
	public void setBed(MstBed bed)
	{
		this.bed = bed;
	}

	/**
	 * �w���t���O���擾����
	 * @return designated �w�� true:�w�� false:�t���[
	 */
	public boolean getDesignated()
	{
		return designated;
	}
	
	/**
	 * �w���t���O���Z�b�g����
	 * @param designated �w���t���O
	 */
	public void setDesignated( boolean designated )
	{
		this.designated = designated;
	}
	
	/**
	 * �X�^�b�t���擾����B
	 * @return �X�^�b�t
	 */
	public MstStaff getStaff()
	{
		return staff;
	}

	/**
	 * �X�^�b�t���Z�b�g����B
	 * @param staff �X�^�b�t
	 */
	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}

        /**
         * �R�[�X�_��E�����t���O���擾����
         * @return
         */
        public Integer getCourseFlg()
        {
            return courseFlg;
        }

        /**
         * �R�[�X�_��E�����t���O���Z�b�g����
         * @param courseFlg
         */
        public void setCourseFlg(Integer courseFlg)
        {
            this.courseFlg = courseFlg;
        }

        /**
         * �R�[�X�_��ԍ����擾����
         * @return
         */
        public Integer getContractNo()
        {
            return contractNo;
        }

        /**
         * �R�[�X�_��ԍ����Z�b�g����
         * @param contractNo
         */
        public void setContractNo(Integer contractNo)
        {
            this.contractNo = contractNo;
        }

        /**
         * �R�[�X�_��ڍהԍ����擾����
         * @return
         */
        public Integer getContractDetailNo()
        {
            return contractDetailNo;
        }

        /**
         * �R�[�X�_��ڍהԍ����Z�b�g����
         * @param contractedDetailNo
         */
        public void setContractDetailNo(Integer contractDetailNo)
        {
            this.contractDetailNo = contractDetailNo;
        }

        // start add 20130806 nakhoa
        public Integer getContractShopId(){
            return this.contractShopId;
        }
        
        public void setContractShopId(Integer contractShopId){
            this.contractShopId = contractShopId;
        }
        // end add 20130806 nakhoa
        
	/**
	 * �\��f�[�^���Z�b�g����B
	 * @param dr �\��f�[�^
	 */
	public void set(DataReservationDetail drd)
	{
		this.setReservation(drd.getReservation());
		this.setReservationDetailNo(drd.getReservationDetailNo());
		this.setReservationDatetime(drd.getReservationDatetime());
		this.setTechnic(drd.getTechnic());
		this.setBed(drd.getBed());
		this.setStaff(drd.getStaff());
		this.setDesignated( drd.getDesignated() );
                
	}
	
	
	/**
	 * �f�[�^��o�^����B
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		String	sql		=	"";
		
		if(isExists(con))
		{
			sql	=	this.getUpdateSQL();
		}
		else
		{
			sql	=	this.getInsertSQL();
		}
		
		
		if( con.executeUpdate(sql) != 1 ) return false;
		
		Integer		maxNo	=	this.getMaxReservationDetailNo(con);

		if(maxNo == null)
		{
			return	false;
		}
		this.setReservationDetailNo( maxNo );

		// �������݂���ꍇ�ɂ͈��f�[�^��ۑ�����
		if( 0 < size() )
		{
			for( DataReservationProportionally drp : this )
			{
				drp.setReservationDetail( this );
				if( !drp.regist( con ) ) return false;
			}
		}
		return true;
	}
	
	
	/**
	 * �f�[�^���폜����B�i�_���폜�j
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
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
		
		if(con.executeUpdate(sql) == 1)
		{
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	/**
	 * �f�[�^�����݂��邩�`�F�b�N����B
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getReservation() == null ||
				this.getReservationDetailNo() == null ||
				this.getReservationDetailNo() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	
	/**
	 * �\��������X�V����B
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean updateReservationDatetime(ConnectionWrapper con) throws SQLException
	{
		if(con.executeUpdate(this.getUpdateReservationDatetimeSQL()) == 1)
		{
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	/**
	 * Select�����擾����B
	 * @return 
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from data_reservation_detail\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n" +
				"and reservation_detail_no = " + SQLUtil.convertForSQL(this.getReservationDetailNo()) + "\n" +
				"and delete_date is null\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return 
	 */
	private String	getInsertSQL()
	{
		return	"insert into data_reservation_detail\n" +
				"(shop_id, reservation_no, reservation_detail_no, reservation_datetime,\n" +
				"technic_id, bed_id, staff_id, designated_flag,\n" +
				"insert_date, update_date, delete_date, operation_time, course_flg, contract_no, contract_detail_no,contract_shop_id)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n" +
				SQLUtil.convertForSQL(this.getReservation().getReservationNo()) + ",\n" +
				"coalesce(max(reservation_detail_no), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getReservationDatetime()) + ",\n" +
				SQLUtil.convertForSQL(getProductId()) + ",\n" +
				SQLUtil.convertForSQL(this.getBed().getBedID()) + ",\n" +
				SQLUtil.convertForSQL(this.getStaff().getStaffID()) + ",\n" +
				SQLUtil.convertForSQL(this.getDesignated()) + ",\n" +
				"current_timestamp, current_timestamp, null,\n" +
				SQLUtil.convertForSQL(getOperationTime()) + ",\n" +
                                getCourseFlgStr() + ",\n" +
                                getContractNoStr() + ",\n" +
                                getContractDetailNoStr() + ",\n" +
                                // start add 20130806 nakhoa
                                SQLUtil.convertForSQL(this.getContractShopId()) + "\n" +
                                // end add 20130806 nakhoa
				"from data_reservation_detail\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return 
	 */
	private String	getUpdateSQL()
	{
		return	"update data_reservation_detail\n" +
				"set\n" +
				"reservation_datetime = " + SQLUtil.convertForSQL(this.getReservationDatetime()) + ",\n" +
				"technic_id = " + SQLUtil.convertForSQL(getProductId()) + ",\n" +
				"bed_id = " + SQLUtil.convertForSQL(this.getBed().getBedID()) + ",\n" +
				"staff_id = " + SQLUtil.convertForSQL(this.getStaff().getStaffID()) + ",\n" +
				"designated_flag = " + SQLUtil.convertForSQL(this.getDesignated()) + ",\n" +
                                "course_flg = " + getCourseFlgStr() + ",\n" +
                                "contract_no = " + getContractNoStr() + ",\n" +
                                "contract_detail_no = " + getContractDetailNoStr() + ",\n" +
                                // start add 20130806 nakhoa
                                "contract_shop_id = " + this.getContractShopId() + ",\n" +
                                // end add 20130806 nakhoa
				"update_date = current_timestamp\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n" +
				"and reservation_detail_no = " + SQLUtil.convertForSQL(this.getReservationDetailNo()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return 
	 */
	private String	getDeleteSQL()
	{
		return	"update data_reservation_detail\n" +
				"set\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = current_timestamp\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n" +
				"and reservation_detail_no = " + SQLUtil.convertForSQL(this.getReservationDetailNo()) + "\n";
	}
	
	/**
	 * �\��������X�V����r�p�k�����擾����B
	 * @return �\��������X�V����r�p�k��
	 */
	private String	getUpdateReservationDatetimeSQL()
	{
		return	"update data_reservation_detail\n" +
				"set\n" +
				"reservation_datetime = " + SQLUtil.convertForSQL(this.getReservationDatetime()) + ",\n" +
				"update_date = current_timestamp\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n" +
				"and reservation_detail_no = " + SQLUtil.convertForSQL(this.getReservationDetailNo()) + "\n";
	}

	public void setReservationDate(GregorianCalendar selectedDate) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	/**
	 * �\��ԍ��̍ő�l�i�o�^�����\��ԍ��j���擾����B
	 * @param con ConnectionWrapper
	 * @return �\��ԍ��̍ő�l�i�o�^�����\��ԍ��j
	 * @throws java.sql.SQLException SQLException
	 */
	private Integer	getMaxReservationDetailNo(ConnectionWrapper con) throws SQLException
	{
		Integer		maxNo	=	null;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getMaxReservationDetailNoSQL());
		
		if(rs.next())
		{
			maxNo	=	rs.getInt("max_no");
		}
		
		return	maxNo;
	}
	
	/**
	 * �\��ԍ��̍ő�l�i�o�^�����\��ԍ��j���擾����r�p�k�����擾����B
	 * @return �\��ԍ��̍ő�l�i�o�^�����\��ԍ��j���擾����r�p�k��
	 */
	private String getMaxReservationDetailNoSQL()
	{
		return
			"select\n" +
			"max( drd.reservation_detail_no ) as max_no\n" +
			"from\n" +
			"data_reservation_detail as drd\n" +
			"where\n" +
			"drd.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
			"and drd.reservation_no = " + SQLUtil.convertForSQL( this.getReservationNo() ) + "\n" +
			";\n";
	}

        public Integer getProductId()
        {
            if (getCourseFlg() == null) {
                //�Z�p�̏ꍇ
                return getTechnic().getTechnicID();
            } else if (getCourseFlg() == 1) {
                //�R�[�X�_��̏ꍇ
                return getCourse().getCourseId();
            } else if (getCourseFlg() == 2) {
                //�����R�[�X�̏ꍇ
                return getConsumptionCourse().getCourseId();
            }
            
            return null;
        }

        private Integer getOperationTime()
        {

            if (getCourseFlg() == null) {
                //�Z�p�̏ꍇ
                return getTechnic().getOperationTime();
            } else if (getCourseFlg() == 1) {
                //�R�[�X�_��̏ꍇ
                return getCourse().getOperationTime();
            } else if (getCourseFlg() == 2) {
                //�����R�[�X�̏ꍇ
                return getConsumptionCourse().getOperationTime();
            }

            return null;
        }

        private String getCourseFlgStr()
        {
            if (getCourseFlg() == null) {
                //�Z�p�̏ꍇ
                return "null";
            } else if (getCourseFlg() == 1) {
                //�R�[�X�_��̏ꍇ
                return courseFlg.toString();
            } else if (getCourseFlg() == 2) {
                //�����R�[�X�̏ꍇ
                return courseFlg.toString();
            }

            return null;

        }

        private String getContractNoStr()
        {
            if (getCourseFlg() == null) {
                //�Z�p�̏ꍇ
                return "null";
            } else if (getCourseFlg() == 1) {
                //�R�[�X�_��̏ꍇ
                return "null";
            } else if (getCourseFlg() == 2) {
                //�����R�[�X�̏ꍇ
                return consumptionCourse.getContractNo().toString();
            }

            return null;

        }

        private String getContractDetailNoStr()
        {
            if (getCourseFlg() == null) {
                //�Z�p�̏ꍇ
                return "null";
            } else if (getCourseFlg() == 1) {
                //�R�[�X�_��̏ꍇ
                return "null";
            } else if (getCourseFlg() == 2) {
                //�����R�[�X�̏ꍇ
                return consumptionCourse.getContractDetailNo().toString();
            }

            return null;

        }
}
