/*
 * DataReservation.java
 *
 * Created on 2006/06/16, 18:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.reservation;

import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.hair.master.company.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * �\��f�[�^
 * @author katagiri
 */
public class DataReservation extends ArrayList<DataReservationDetail> implements Cloneable
{
	/**
	 * �X��
	 */
	protected	MstShop         shop            = new MstShop();
	/**
	 * �\��No.
	 */
	protected	Integer         reservationNo   = null;
	/**
	 * �ڋq
	 */
	protected	MstCustomer     customer        = new MstCustomer();
	/**
	 * �w��
	 */
	protected	boolean        designated      = false;
	/**
	 * ��S���X�^�b�t
	 */
	protected	MstStaff        staff           = null;
	/**
	 * ���v����
	 */
	protected	Integer         totalTime       = 0;
	/**
	 * ���X����
	 */
	protected	GregorianCalendar   visitTime   = null;
	/**
	 * �J�n����
	 */
	protected	GregorianCalendar   startTime   = null;
	/**
	 * �ޓX����
	 */
	protected	GregorianCalendar   leaveTime   = null;
	/**
	 * ��ԃt���O
	 */
	protected	Integer             status      = 0;
	/**
	 * ����ԃt���O
	 */
	protected	Integer             subStatus   = 0;
	/**
	 * �`�[No.
	 */
	protected	Integer             slipNo      = 0;
        /**
	 *���o�C���\��t���O
         *1:���\���@2:�\����
	 */        
        protected       int     mobileFlag      = 0;
        /**
	 *���X������\��t���O
	 */        
        private       int     nextFlag    = 0;
        /**
	 *�d�b�E���O�\��t���O
	 */        
        private       int     preorderFlag    = 0;
         /**
	 *�\�����
	 */        
         protected      java.util.Date  reserveDate = null;  
         /**
	 *�\��R�����g
	 */ 
	 protected	String comment = "";

         /**
	 *�Z�p���ޗ��̃��X�g
	 */ 
	 protected	String technicClassContractedNameList = "";

	/**
	 * �o�^��
	 */
	protected	MstStaff        regStaff           = null;

	/**
	 * �o�^����
	 */
	protected	GregorianCalendar   insertDate   = null;

        /**
	 * �X�V����
	 */
	protected	GregorianCalendar   updateDate   = null;
        
        protected MstResponse Response1 = null;
        protected MstResponse Response2 = null;
        protected int cancelFlag;
        //IVS_LVTu start add 2015/09/17 Bug #42681
        protected java.util.Date lastVisitDate = null;
        protected java.util.Date nextReservationDate = null;
        protected String nextReservationDateStrArray = null;
        //�{�p���Ԃ̕ύX start
        /** �{�p���ԁi���j */
        protected String opeMinute = null;
        /** �{�p���ԁi�b�j */
        protected String opeSecond = null;
        
        /**
         * �{�p���ԁi���j��Ԃ�
         * @return �{�p���ԁi���j
         */
        public String getOpeMinute() {
            return opeMinute;
        }

        /**
         * �{�p���ԁi���j��ݒ�
         * @param opeMinute �{�p���ԁi���j
         */
        public void setOpeMinute(String opeMinute) {
            this.opeMinute = opeMinute;
        }
        
        /**
         * �{�p���ԁi�b�j��Ԃ�
         * @return �{�p���ԁi�b�j
         */
        public String getOpeSecond() {
            return opeSecond;
        }

        /**
         * �{�p���ԁi�b�j��ݒ�
         * @param opeMinute �{�p���ԁi�b�j
         */
        public void setOpeSecond(String opeSecond) {
            this.opeSecond = opeSecond;
        }
        //�{�p���Ԃ̕ύX end
        
        public StringBuffer getNextReservationDateStrArray() {
            String str[] ;
            StringBuffer strBuffer = new StringBuffer();
            strBuffer.append("");
            if(this.nextReservationDateStrArray != null) {
                str = this.nextReservationDateStrArray.split(",");
                
                for(int i = 0;i < str.length; i ++) {
                    if(!str[i].trim().equals("")) {
                        SimpleDateFormat	sdf	=	new SimpleDateFormat("yyyy-MM-dd");
                        java.util.Date ReservationDate;
                        try {
                            ReservationDate = sdf.parse(str[i]);
                            String strNextDate = DateUtil.format(ReservationDate, "yyyy�NM��d�� (E)");
                            strBuffer.append(strNextDate);
                        } catch (ParseException ex) {
                            Logger.getLogger(DataReservation.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if(((i+1)< str.length) && (!str[i+1].trim().equals(""))) {
                        strBuffer.append("<br>");
                    }
                }
            }
            
            return strBuffer;
        }

        public void setNextReservationDateStrArray(String nextReservationDateStrArray) {
            this.nextReservationDateStrArray = nextReservationDateStrArray;
        }
        
        public java.util.Date getLastVisitDate() {
            return lastVisitDate;
        }

        public void setLastVisitDate(java.util.Date lastVisitDate) {
            this.lastVisitDate = lastVisitDate;
        }

        public java.util.Date getNextReservationDate() {
            return nextReservationDate;
        }

        public void setNextReservationDate(java.util.Date nextReservationDate) {
            this.nextReservationDate = nextReservationDate;
        }
        //IVS_LVTu end add 2015/09/17 Bug #42681
        public MstResponse getResponse1() {
            return Response1;
        }

        public void setResponse1(MstResponse Response1) {
            this.Response1 = Response1;
        }

        public MstResponse getResponse2() {
            return Response2;
        }

        public void setResponse2(MstResponse Response2) {
            this.Response2 = Response2;
        }
	
         /**
	 *�\��ڍ׃f�[�^�̃\�[�g�p�N���X
	 */ 
	public class DataReservationDetailComparator implements Comparator {
            
            public int compare(Object object1, Object object2) {
                //�\������̏����ɕ��ёւ�
                DataReservationDetail drd1 = (DataReservationDetail) object1;
                DataReservationDetail drd2 = (DataReservationDetail) object2;
                GregorianCalendar date1 = drd1.getReservationDatetime();
                GregorianCalendar date2 = drd2.getReservationDatetime();
                return (date1.compareTo(date2));
            }
	}
	
        //Start add 20131002 lvut
       
        @Override
        public Object clone()  {
             return super.clone();
        }
        //End add 20131002 lvut
	/** Creates a new instance of DataReservation */
	public DataReservation()
	{
	}
        
        /**
	 *���o�C���\��t���O���擾����B
         *@return �t���O
         */        
        public int getMobileFlag(){
            return mobileFlag ;
        }
        public void setMobileFlag(int mobileFlag){
            this.mobileFlag = mobileFlag ;
        }

        /**
	 *���X������\��t���O���擾����B
         *@return �t���O
         */        
        public int getNextFlag() {
            return nextFlag;
        }

        public void setNextFlag(int nextFlag) {
            this.nextFlag = nextFlag;
        }
        
        /**
	 *�d�b�E���O�\��t���O���擾����B
         *@return �t���O
         */        
        public int getPreorderFlag() {
            return preorderFlag;
        }

        public void setPreorderFlag(int preorderFlag) {
            this.preorderFlag = preorderFlag;
        }
       
        /**
	 *�\����t���擾����B
         *@return �\����t
         */        
        public java.util.Date getReserveDate(){
            return reserveDate ;
        }
        public void setReserveDate(java.util.Date reserveDate){
            this.reserveDate = reserveDate ;
        }
        
        // IVS SANG START 20131103
         protected      java.util.Date  deleteDate = null; 
         public java.util.Date getDeleteDate(){
            return deleteDate ;
        }
        public void setDeleteDate(java.util.Date deleteDate){
            this.deleteDate = deleteDate ;
        }
        // IVS SANG START 20131103
        
	/**
	 * �X�܂��擾����B
	 * @return �X��
	 */
	public MstShop getShop()
	{
            return shop;
	}

	/**
	 * �X�܂��Z�b�g����B
	 * @param shop �X��
	 */
	public void setShop(MstShop shop)
	{
            this.shop = shop;
	}
	
	/**
	 * �\��No.���擾����B
	 * @return �\��No.
	 */
	public Integer getReservationNo()
	{
		return reservationNo;
	}

	/**
	 * �\��No.���Z�b�g����B
	 * @param reservationNo �\��No.
	 */
	public void setReservationNo(Integer reservationNo)
	{
		this.reservationNo = reservationNo;
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
	 * �ڋq���Z�b�g����B
	 * @param customer �ڋq
	 */
	public void setCustomer(MstCustomer customer)
	{
		this.customer = customer;
	}
	
	/**
	 * �w�����擾����B
	 * @return �w��
	 */
	public boolean getDesignated()
	{
		return designated;
	}

	/**
	 * �w�����Z�b�g����B
	 * @param designated �w��
	 */
	public void setDesignated( boolean designated )
	{
		this.designated = designated;
	}
	
	/**
	 * ��S���X�^�b�t���擾����B
	 * @return ��S���X�^�b�t
	 */
	public MstStaff getStaff()
	{
            return staff;
	}

	/**
	 * ��S���X�^�b�t���Z�b�g����B
	 * @param MstStaff ��S���X�^�b�t
	 */
	public void setStaff( MstStaff staff )
	{
            this.staff = staff;
	}
	
	/**
	 * �o�^�҂��擾����B
	 * @return �o�^��
	 */
	public MstStaff getRegStaff()
	{
            return regStaff;
	}

	/**
	 * �o�^�҂��Z�b�g����B
	 * @param MstStaff �o�^��
	 */
	public void setRegStaff( MstStaff regStaff )
	{
            this.regStaff = regStaff;
	}

	/**
	 * �o�^�������擾����
	 * @return �o�^����
	 */
	public GregorianCalendar getInsertDate()
	{
            return insertDate;
	}
	/**
	 * �o�^�������Z�b�g����
	 * @param �o�^����
	 */
	public void setInsertDate(GregorianCalendar insertDate)
	{
            this.insertDate = insertDate;
	}
	public void setInsertDate(java.util.Date insertDate)
	{
            if (this.insertDate == null) {
                this.insertDate = new GregorianCalendar();
            }

            this.insertDate.setTime(insertDate);
	}

	/**
	 * �X�V�������擾����
	 * @return �X�V����
	 */
	public GregorianCalendar getUpdateDate()
	{
            return updateDate;
	}
	/**
	 * �X�V�������Z�b�g����
	 * @param �X�V����
	 */
	public void setUpdateDate(GregorianCalendar updateDate)
	{
            this.updateDate = updateDate;
	}
	public void setUpdateDate(java.util.Date updateDate)
	{
            if (this.updateDate == null) {
                this.updateDate = new GregorianCalendar();
            }

            this.updateDate.setTime(updateDate);
	}

	/**
	 * ���v���Ԃ��擾����B
	 * @return ���v����
	 */
	public Integer getTotalTime()
	{
            totalTime = 0;

            if (this.size() > 0) {
                
                Calendar calMin = Calendar.getInstance();
                calMin.setTime(this.get(0).getReservationDatetime().getTime());
                Calendar calMax = (Calendar)calMin.clone();
                
                for (DataReservationDetail drd : this) {

                    Calendar calTmp = Calendar.getInstance();
                    calTmp.setTime(drd.getReservationDatetime().getTime());
                    
                    if (calMin.after(calTmp)) {
                        calMin.setTime(calTmp.getTime());
                    }

                    if (drd.getCourse() != null) {
                        calTmp.add(Calendar.MINUTE, drd.getCourse().getOperationTime());
                    } else if (drd.getConsumptionCourse() != null) {
                        calTmp.add(Calendar.MINUTE, drd.getConsumptionCourse().getOperationTime());
                    } else {
                        calTmp.add(Calendar.MINUTE, drd.getTechnic().getOperationTime());
                    }

                    if (calMax.before(calTmp)) {
                        calMax.setTime(calTmp.getTime());
                    }
                        
                }
                
                totalTime = Long.valueOf((calMax.getTimeInMillis() - calMin.getTimeInMillis()) / 1000 / 60).intValue();
            }
            
/*            
            for (DataReservationDetail drd : this) {
                totalTime += drd.getTechnic().getOperationTime();
            }
*/            
            return totalTime;
	}

	/**
	 * ���v���Ԃ��Z�b�g����B
	 * @param totalTime ���v����
	 */
	public void setTotalTime(Integer totalTime)
	{
            this.totalTime = totalTime;
	}
	
	/**
	 * ���X���Ԃ��擾����
	 * @return ���X����
	 */
	public GregorianCalendar getVisitTime()
	{
            return visitTime;
	}
	
	/**
	 * ���X���Ԃ��Z�b�g����
	 * @param startTime ���X����
	 */
	public void setVisitTime(GregorianCalendar visitTime)
	{
            this.visitTime = visitTime;
	}
        
	public void setVisitTime(java.util.Date visitTime)
	{
            if (this.visitTime == null) {
                this.visitTime = new GregorianCalendar();
            }

            this.visitTime.setTime(visitTime);
	}
	
	/**
	 * �{�p�J�n���Ԃ��擾����
	 * @return �{�p�J�n����
	 */
	public GregorianCalendar getStartTime()
	{
            return startTime;
	}
	
	/**
	 * �{�p�J�n���Ԃ��Z�b�g����
	 * @param startTime �{�p�J�n����
	 */
	public void setStartTime(GregorianCalendar startTime)
	{
            this.startTime = startTime;
	}
        
	public void setStartTime(java.util.Date startTime)
	{
            if (this.startTime == null) {
                this.startTime = new GregorianCalendar();
            }

            this.startTime.setTime(startTime);
	}
	
	/**
	 * �ޓX���Ԃ��擾����
	 * @return �ޓX����
	 */
	public GregorianCalendar getLeaveTime()
	{
            return leaveTime;
	}
	
	/**
	 * �ޓX���Ԃ��Z�b�g����
	 * @param startTime �ޓX����
	 */
	public void setLeaveTime(GregorianCalendar leaveTime)
	{
            this.leaveTime = leaveTime;
	}
        
	public void setLeaveTime(java.util.Date leaveTime)
	{
            if (this.leaveTime == null) {
                this.leaveTime = new GregorianCalendar();
            }

            this.leaveTime.setTime(leaveTime);
	}
	
	/**
	 * ��ԃt���O���擾����B
	 * @return ��ԃt���O
	 */
	public Integer getStatus()
	{
		return status;
	}

	/**
	 * ��ԃt���O���Z�b�g����B
	 * @param status ��ԃt���O
	 */
	public void setStatus(Integer status)
	{
		this.status = status;
	}

	/**
	 * ����ԃt���O���擾����B
	 * @return ����ԃt���O
	 */
	public Integer getSubStatus()
	{
		return subStatus;
	}

	/**
	 * ����ԃt���O���Z�b�g����B
	 * @param subStatus ����ԃt���O
	 */
	public void setSubStatus(Integer subStatus)
	{
		this.subStatus = subStatus;
	}

	/**
	 * �`�[No.���擾����B
	 * @return �`�[No.
	 */
	public Integer getSlipNo()
	{
		return slipNo;
	}

	/**
	 * �`�[No.���Z�b�g����B
	 * @param slipNo �`�[No.
	 */
	public void setSlipNo(Integer slipNo)
	{
		this.slipNo = slipNo;
	}
	
	/**
	 * �\��R�����g���擾����B
	 * @return �\��R�����g
	 */
	public String getComment()
	{
            if (comment == null) return "";

            return comment;
	}

	/**
	 * �\��R�����g���Z�b�g����B
	 * @param comment �\��R�����g
	 */
	public void setComment(String comment)
	{
            this.comment = comment;
	}
	
        // IVS START INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
        public int getCancelFlag() {
            return cancelFlag;
        }

        public void setCancelFlag(int cancelFlag) {
            this.cancelFlag = cancelFlag;
        }
        // IVS END INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
	public String getTechnicClassContractedName()
	{
		String name = "";
		
                //�\������̏����ɕ��ёւ�
		Collections.sort(this, new DataReservationDetailComparator());

		for (DataReservationDetail drd : this) {
                    if (0 < name.length()) name += "�A";

                    if (drd.getCourseFlg() == null) {
                        //�Z�p
                        if (drd.getTechnic().getTechnicNo() != null && (drd.getTechnic().getTechnicNo().startsWith("mo-rsv-")||drd.getTechnic().getMobileFlag()==1)) {
                            name += drd.getTechnic().getTechnicName();
                        } else {
                            name += drd.getTechnic().getTechnicClass().getTechnicClassContractedName();
                        }
                    } else if ((Integer)drd.getCourseFlg() == 1) {
                        //�R�[�X�_��
                        name += drd.getCourse().getCourseClass().getCourseClassName();
                    } else if ((Integer)drd.getCourseFlg() == 2) {
                        //�����R�[�X
                        name += drd.getConsumptionCourse().getConsumptionCourseClass().getCourseClassName();
                    }
		}

		return name;
	}

	public String getTechnicName()
	{
		String name = "";
		
                //�\������̏����ɕ��ёւ�
		Collections.sort(this, new DataReservationDetailComparator());

		for (DataReservationDetail drd : this) {
                    if (0 < name.length()) name += "�A";
                    name += drd.getTechnic().getTechnicName();
		}

		return name;
	}
        
	/**
	 * �\��ڍ׃f�[�^�̒S���Җ������ׂĎ擾����B
	 * @return �S���Җ�
	 */
	public String getDRDStaffName()
	{
                final String NO_DATA = "[�w���Ȃ�]";

		String name = "";

                //�\������̏����ɕ��ёւ�
		Collections.sort(this, new DataReservationDetailComparator());

                ArrayList<String> list = new ArrayList<String>();

		for(DataReservationDetail drd : this)
		{
                    if(drd.getStaff().getStaffName(0) == null || drd.getStaff().getStaffName(0).length() == 0){
                        if (list.contains(NO_DATA)) continue;
                        if(0 < name.length()) name += "�A";
                        list.add(NO_DATA);
                        name += NO_DATA;
                    } else {
                        if (list.contains(drd.getStaff().getStaffName(0))) continue;
                        if(0 < name.length()) name += "�A";
                        list.add(drd.getStaff().getStaffName(0));
                        name += drd.getStaff().getStaffName(0);
                    }
		}

		return name;
	}
	/**
	 * �\��ڍ׃f�[�^�̒S���Җ������ׂĎ擾����B
	 * @return �S���Җ�
	 */
	public String getDRDFullStaffName()
	{
                final String NO_DATA = "[�w���Ȃ�]";
                
		String name = "";
		
                //�\������̏����ɕ��ёւ�
		Collections.sort(this, new DataReservationDetailComparator());

                ArrayList<String> list = new ArrayList<String>();
                
		for(DataReservationDetail drd : this)
		{
                    if(drd.getStaff().getFullStaffName() == null || drd.getStaff().getFullStaffName().length() == 0){
                        if (list.contains(NO_DATA)) continue;
                        if(0 < name.length()) name += "�A";
                        list.add(NO_DATA);
                        name += NO_DATA;
                    } else {
                        if (list.contains(drd.getStaff().getFullStaffName())) continue;
                        if(0 < name.length()) name += "�A";
                        list.add(drd.getStaff().getFullStaffName());
                        name += drd.getStaff().getFullStaffName();
                    }
		}
		
		return name;
	}
	
	/**
	 * �\��ڍ׃f�[�^�̎{�p�䖼�����ׂĎ擾����B
	 * @return �{�p�䖼
	 */
	public String getDRDBedName()
	{
                final String NO_DATA = "[�w��Ȃ�]";
                
		String name = "";
		
                //�\������̏����ɕ��ёւ�
		Collections.sort(this, new DataReservationDetailComparator());

                ArrayList<String> list = new ArrayList<String>();
                
                for(DataReservationDetail drd : this)
		{
                    if(drd.getBed().getBedName() == null || drd.getBed().getBedName().length() == 0){
                        if (list.contains(NO_DATA)) continue;
                        if(0 < name.length()) name += "�A";
                        list.add(NO_DATA);
                        name += NO_DATA;
                    } else {
                        if (list.contains(drd.getBed().getBedName())) continue;
                        if(0 < name.length()) name += "�A";
                        list.add(drd.getBed().getBedName());
                        name += drd.getBed().getBedName();
                    }
		}
		
		return name;
	}
	
	/**
	 * �\��ڍ׃f�[�^�̋Z�p�������ׂĎ擾����B
	 * @return �Z�p��
	 */
	public String getDRDTechnicName()
	{
		String	name	=	"";
		
                //�\������̏����ɕ��ёւ�
		Collections.sort(this, new DataReservationDetailComparator());

		for(DataReservationDetail drd : this)
		{
			if(0 < name.length())	name	+=	"�A";
			name	+=	drd.getTechnic().getTechnicName() == null ? " " : drd.getTechnic().getTechnicName();
		}
		
		return	name;
	}
	
	/**
	 * �\��ڍ׃f�[�^�̗\�����(�J�n)���擾����B
	 * @return �\�����(�J�n)
	 */
	public GregorianCalendar getDRDStartReservationDatetime()
	{
		GregorianCalendar	date	=	null;
		
                //�\������̏����ɕ��ёւ�
		Collections.sort(this, new DataReservationDetailComparator());

		for(DataReservationDetail drd : this)
		{
			date	=	drd.getReservationDatetime();
                        break;
		}
		
		return	date;
	}
	
	/**
	 * �\��f�[�^���Z�b�g����B
	 * @param dr �\��f�[�^
	 */
	public void set(DataReservation dr)
	{
            this.setShop(dr.getShop());
            this.setReservationNo(dr.getReservationNo());
            this.setCustomer(dr.getCustomer());
            this.setDesignated(dr.getDesignated());
            this.setStaff(dr.getStaff());
            this.setTotalTime(dr.getTotalTime());
            this.setVisitTime(dr.getVisitTime());
            this.setStartTime(dr.getStartTime());
            this.setLeaveTime(dr.getLeaveTime());
            this.setStatus(dr.getStatus());
            this.setSubStatus(dr.getSubStatus());
            this.setSlipNo(dr.getSlipNo());
            this.setComment(dr.getComment());
            this.setNextFlag(dr.getNextFlag());
            this.setPreorderFlag(dr.getPreorderFlag());
            this.setRegStaff(dr.getRegStaff());
            this.setInsertDate(dr.getInsertDate());
            this.setUpdateDate(dr.getUpdateDate());
	}
	
	public void initTechnics()
	{
		this.clear();
		this.setTotalTime( 0 );
	}
	
	/**
	 * �f�[�^��o�^����B
	 * @param con 
	 * @param isStay �ڋq�� 1:�\�� 2;�ݓX4:�ޓX
	 * @throws java.sql.SQLException 
	 * @return 
	 */
//	public boolean regist(ConnectionWrapper con, boolean isStay) throws SQLException
	public boolean regist(ConnectionWrapper con ) throws SQLException
	{
		String	sql		=	"";
		
		if(isExists(con))
		{
//			sql	=	this.getUpdateSQL(isStay);
			sql	=	this.getUpdateSQL();
		}
		else
		{
//			sql	=	this.getInsertSQL(isStay);
			sql	=	this.getInsertSQL();
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
		
		if(con.executeUpdate(sql) != 1)
		{
			return	false;
		}
		
		if( !this.deleteDetail(con) ) return false;
		if( !this.deleteProportionally(con) ) return false;
                // IVS_Thanh start add 2014/07/07 GB_Mashu_�ƑԎ�S���ݒ�
                if( !this.deleteDataReservationMainStaffLogic(con) ) return false;
                // IVS_Thanh end add 2014/07/07 GB_Mashu_�ƑԎ�S���ݒ�
		return	true;
	}
	
	/**
	 * �f�[�^�����݂��邩�`�F�b�N����B
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
            if (this.getReservationNo() == null || this.getReservationNo() < 1) return false;
		
            if (con == null) return false;
		
            ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
            //IVS NNTUAN START EDIT 20131028
            //return rs.next();
            //if slip no has value . get value of slip_no
            if (rs.next()) {
                int slipNo = rs.getInt("slip_no");
                if (slipNo != 0) {
                    this.setSlipNo(slipNo);
                }
                return true;
            }
            return false;
            //IVS NNTUAN END EDIT 20131028
	}
	
	
	/**
	 * ��ԃt���O���X�V����B
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean updateStatus(ConnectionWrapper con) throws SQLException
	{
		if(con.executeUpdate(this.getUpdateStatusSQL()) == 1)
		{
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	
	/**
	 * ���׃f�[�^���폜����B
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean deleteDetail(ConnectionWrapper con) throws SQLException
	{
		if(0 <= con.executeUpdate(this.getDeleteDetailSQL()))
		{
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	/**
	 * ���f�[�^���폜����
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean deleteProportionally( ConnectionWrapper con ) throws SQLException
	{
		if( 0 <= con.executeUpdate( this.getDeleteProportionallySQL() ) )
		{
			return true;
		}
		return false;
	}
        
        // IVS_Thanh start add 2014/07/10 Mashu_����v�\��
        public boolean deleteDataReservationMainStaff(ConnectionWrapper con) throws SQLException {
            return 0 <= con.executeUpdate(this.getDeleteDataReservationMainStaffSQL());
        }
        
        public String getDeleteDataReservationMainStaffSQL() {
            return "delete from data_reservation_mainstaff\n"
                    + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                    + "and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
        }   
        
         public boolean deleteDataReservationMainStaffLogic(ConnectionWrapper con) throws SQLException {
            return 0 <= con.executeUpdate(this.getDeleteDataReservationMainStaffSQLLogic());
        }
        
        public String getDeleteDataReservationMainStaffSQLLogic() {
            return
			"update data_reservation_mainstaff\n" +
			"set\n" +
			"update_date = current_timestamp,\n" +
			"delete_date = current_timestamp\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
			"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
        }
        // IVS_Thanh end add 2014/07/10 Mashu_����v�\��
	
	/**
	 * Select�����擾����B
	 * @return 
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from data_reservation\n" +
				"where	shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and	reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return 
	 */
//	private String	getInsertSQL(boolean isStay)
	private String	getInsertSQL()
	{
		return
			"insert into data_reservation\n" +
			"(shop_id, reservation_no, customer_id,\n" +
			"designated_flag, staff_id, total_time, visit_time,\n" +
			"start_time, leave_time, status, sub_status, slip_no,comment,next_flag,preorder_flag,reg_staff_id,response_id1,response_id2,\n" +
			"insert_date, update_date, delete_date)\n" +
			"select\n" +
			SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n" +
			"coalesce(max(reservation_no), 0) + 1,\n" +
			SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ",\n" +
			SQLUtil.convertForSQL(this.getDesignated() ) + ",\n" +
			( this.getStaff() != null && this.getStaff().getStaffID() != null ? SQLUtil.convertForSQL(this.getStaff().getStaffID() ) : null ) + ",\n" +
			SQLUtil.convertForSQL(this.getTotalTime()) + ",\n" +
			// visit_time  1:null  2-1:������  2-2:�ێ��l  3:�ێ��l
			( this.getStatus() == 1 ? ( "null" ) : ( this.getStatus() == 2 ? ( this.getSubStatus() == 1 ? ( "current_timestamp" ) : ( SQLUtil.convertForSQL( this.getVisitTime() ) ) ) : ( SQLUtil.convertForSQL( this.getVisitTime() ) ) ) ) + ",\n" +
			// start_time  1:null  2-1:null    2-2:������  3:�ێ��l(������΍ݓX����)
			( this.getStatus() == 1 ? ( "null" ) : ( this.getStatus() == 2 ? ( this.getSubStatus() == 1 ? ( "null" ) : ( "current_timestamp" ) ) : ( this.getStartTime() == null ? ( SQLUtil.convertForSQL( this.getVisitTime() ) ) : SQLUtil.convertForSQL( this.getStartTime() ) ) ) ) + ",\n" +
			// leave_time  1:null  2-1:null    2-2:null    3:������
			//( this.getStatus() <= 2 ? ( "null" ) : ( "current_timestamp" ) ) + ",\n" +
			( this.getStatus() <= 2 ? ( "null" ) : SQLUtil.convertForSQL( this.getLeaveTime() ) ) + ",\n" +
			SQLUtil.convertForSQL(this.getStatus()) + ",\n" +
			SQLUtil.convertForSQL(this.getSubStatus()) + ",\n" +
			SQLUtil.convertForSQL(this.getSlipNo()) + ",\n" +
			SQLUtil.convertForSQL(this.getComment()) + ",\n" +
			SQLUtil.convertForSQL(this.getNextFlag()) + ",\n" +
			SQLUtil.convertForSQL(this.getPreorderFlag()) + ",\n" +
			( this.getRegStaff() != null && this.getRegStaff().getStaffID() != null ? SQLUtil.convertForSQL(this.getRegStaff().getStaffID() ) : null ) + ",\n" 
			+ (this.getResponse1() != null && this.getResponse1().getResponseID() != null ? SQLUtil.convertForSQL(this.getResponse1().getResponseID()) : null) + ",\n"
                        + (this.getResponse2() != null && this.getResponse2().getResponseID() != null ? SQLUtil.convertForSQL(this.getResponse2().getResponseID()) : null) + ",\n"
			+"current_timestamp, current_timestamp, null\n" +
			"from data_reservation\n" +
			"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return 
	 */
//	private String	getUpdateSQL(boolean isStay)
	private String	getUpdateSQL()
	{      
		return
                        
			"update data_reservation\n" +
			"set\n" +
			"customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ",\n" +
			"designated_flag = " + SQLUtil.convertForSQL(this.getDesignated() ) + ",\n" +
			"staff_id = " + ( this.getStaff() != null && this.getStaff().getStaffID() != null ? SQLUtil.convertForSQL(this.getStaff().getStaffID() ) : null ) + ",\n" +
			// IVS SANG START EDIT 20131115 [gb]���X�܂̂���v������Ǝ��g�̓X��ID���ς���Ă��܂�
                        // (this.getStatus() < 3 || SystemInfo.getCurrentShop().isReservationMenuUpdate() ? "total_time = " + SQLUtil.convertForSQL(this.getTotalTime()) + ",\n" : "") +
                        (this.getStatus() < 3 || this.getShop().isReservationMenuUpdate() ? "total_time = " + SQLUtil.convertForSQL(this.getTotalTime()) + ",\n" : "") +
			// IVS SANG END EDIT 20131115 [gb]���X�܂̂���v������Ǝ��g�̓X��ID���ς���Ă��܂�
                        // visit_time  1:null  2-1:������  2-2:�ێ��l  3:�ێ��l
			( this.getStatus() == 1 ? "visit_time = null,\n" : ( this.getStatus() == 2 ? ( this.getSubStatus() == 1 ? "visit_time = (case when visit_time is null then current_timestamp else visit_time end),\n" : "" ) : "" ) ) +
			// start_time  1:null  2-1:null    2-2:������  3:�ێ��l(������΍ݓX����)
			//( this.getStatus() == 1 ? "start_time = null,\n" : ( this.getStatus() == 2 ? ( this.getSubStatus() == 1 ? "start_time = null,\n" : "start_time = current_timestamp,\n" ) : "start_time = (select coalesce( start_time, visit_time ) from data_reservation where shop_id = 1 and reservation_no = 11),\n" ) ) +
			( this.getStatus() == 1 ? "start_time = null,\n" : ( this.getStatus() == 2 ? ( this.getSubStatus() == 1 ? "start_time = start_time,\n" : "start_time = current_timestamp,\n" ) : "start_time = (select coalesce( start_time, visit_time ) from data_reservation where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + " and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "),\n" ) ) +
			// leave_time  1:null  2-1:null    2-2:null    3:������
			"leave_time = " + ( this.getStatus() <= 2 ? ( "null" ) : ( "coalesce(leave_time, current_timestamp)" ) ) + ",\n" +
			"status = " + SQLUtil.convertForSQL(this.getStatus()) + ",\n" +
			"sub_status = " + SQLUtil.convertForSQL(this.getSubStatus()) + ",\n" +
			"slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + ",\n" +
			"comment = " + SQLUtil.convertForSQL(this.getComment()) + ",\n" +
			"next_flag = " + SQLUtil.convertForSQL(this.getNextFlag()) + ",\n" +
			"preorder_flag = " + SQLUtil.convertForSQL(this.getPreorderFlag()) + ",\n" +
			"reg_staff_id = " + ( this.getRegStaff() != null && this.getRegStaff().getStaffID() != null ? SQLUtil.convertForSQL(this.getRegStaff().getStaffID() ) : null ) + ",\n" 
			 //+ "response_id1 = " + SQLUtil.convertForSQL(this.getResponse1().getResponseID()) + ",\n"
                        + "response_id1 = " + (this.getResponse1() == null ? null : SQLUtil.convertForSQL(this.getResponse1().getResponseID())) + ",\n"
                        //+ "response_id2 = " + SQLUtil.convertForSQL(this.getResponse2().getResponseID()) + ",\n"
                        + "response_id2 = " + (this.getResponse2() == null ? null : SQLUtil.convertForSQL(this.getResponse2().getResponseID())) + ",\n"
			+"update_date = current_timestamp ,\n" +
                        "mobile_flag = case when mobile_flag in (1, 3) then 2 else mobile_flag end \n" +
			"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
			"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return 
	 */
	private String	getDeleteSQL()
	{
		return	"update data_reservation\n" +
				"set\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = current_timestamp\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
	}
	
	/**
	 * ��ԃt���O���X�V����r�p�k�����擾����B
	 * @return ��ԃt���O���X�V����r�p�k��
	 */
	private String	getUpdateStatusSQL()
	{
		return
			"update data_reservation\n" +
			"set\n" +
			"designated_flag = " + SQLUtil.convertForSQL(this.getDesignated()) + ",\n" +
			"staff_id = " + SQLUtil.convertForSQL(this.getStaff().getStaffID()) + ",\n" +
                        // IVS SANG START EDIT 20131115 [gb]���X�܂̂���v������Ǝ��g�̓X��ID���ς���Ă��܂�
			//(SystemInfo.getCurrentShop().isReservationMenuUpdate() ? "total_time = " + SQLUtil.convertForSQL(this.getTotalTime()) + ",\n" : "") +
			(this.getShop().isReservationMenuUpdate() ? "total_time = " + SQLUtil.convertForSQL(this.getTotalTime()) + ",\n" : "") +
                        // IVS SANG START EDIT 20131115 [gb]���X�܂̂���v������Ǝ��g�̓X��ID���ς���Ă��܂�
                        "status = " + SQLUtil.convertForSQL(this.getStatus()) + ",\n" +
			"sub_status = " + SQLUtil.convertForSQL(this.getSubStatus()) + ",\n" +
                        
			// visit_time  1:null  2-1:������  2-2:�ێ��l  3:�ێ��l
			( this.getStatus() == 1 ? "visit_time = null,\n" : ( this.getStatus() == 2 ? ( this.getSubStatus() == 1 ? "visit_time = (case when visit_time is null then current_timestamp else visit_time end),\n" : "" ) : "" ) ) +
			// start_time  1:null  2-1:null    2-2:������  3:�ێ��l(�Ȃ���΍ݓX����)
			//( this.getStatus() == 1 ? "start_time = null,\n" : ( this.getStatus() == 2 ? ( this.getSubStatus() == 1 ? "start_time = null,\n" : "start_time = current_timestamp,\n" ) : "start_time = (select coalesce( start_time, visit_time ) from data_reservation where shop_id = 1 and reservation_no = 11),\n " ) ) +
			( this.getStatus() == 1 ? "start_time = null,\n" : ( this.getStatus() == 2 ? ( this.getSubStatus() == 1 ? "start_time = null,\n" : "start_time = (case when start_time is null then current_timestamp else start_time end),\n" ) : "start_time = (select coalesce( start_time, visit_time ) from data_reservation where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + " and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "),\n " ) ) +
			// leave_time  1:null  2-1:null    2-2:null    3:������
			"leave_time = " + ( this.getStatus() <= 2 ? ( "null" ) : ( "coalesce(leave_time, current_timestamp)" ) ) + ",\n" +
			"slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + ",\n" +
			"customer_id = " + this.getCustomer().getCustomerID() + ",\n" +
			"reg_staff_id = " + (this.getRegStaff() != null && this.getRegStaff().getStaffID() != null ? SQLUtil.convertForSQL(this.getRegStaff().getStaffID()) : null) + ",\n" +
			"update_date = current_timestamp\n" +
			"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
			"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return 
	 */
	private String	getDeleteDetailSQL()
	{
		return	"update data_reservation_detail\n" +
				"set\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = current_timestamp\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
	}
	
	/**
	 * ���폜�pUpdate�����擾����
	 */
	private String getDeleteProportionallySQL()
	{
		return
			"update data_reservation_proportionally\n" +
			"set\n" +
			"update_date = current_timestamp,\n" +
			"delete_date = current_timestamp\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
			"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
	}
        private String getMobileFlagUpdateSQL()
        {
                return
                          "update data_reservation \n" +
                          "set\n"+
                          "mobile_flag = 2,\n" +
                          "update_date = current_timestamp\n" +
                          "where\n" +
                          "shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
                          "and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";

        }
        public  int  upDateMobileFlag(ConnectionWrapper con)throws SQLException{
                int result= 0;
 		result	=	con.executeUpdate(this.getMobileFlagUpdateSQL());
                return result;
         }          
        
        public boolean selectSlipNoStatus( ConnectionWrapper con ) throws SQLException{

        if( this.reservationNo == null || shop.getShopID() == null  )
                return false ;

                ResultSetWrapper	rs	=	con.executeQuery(this.getLoadSlipNoSql());


                while(rs.next())
                {
                        this.setStatus(rs.getInt("status"));
                        this.setSlipNo(rs.getInt("slip_no"));
                        this.setSubStatus(rs.getInt("sub_status"));
                }  

                return true;
        }
         
        public String getLoadSlipNoSql(){

              return  "select slip_no , status ,sub_status\n"+ 
                       "from data_reservation \n" + 
                       "where reservation_no = " + reservationNo + " \n" +  
                       "and delete_date is null" + " \n" +
		       "and shop_id = "  + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n";                                                            
                                 
        }

        public String getTechnicClassContractedNameList() {
            return technicClassContractedNameList;
        }

        public void setTechnicClassContractedNameList(String technicClassContractedNameList) {
            if (technicClassContractedNameList != null && technicClassContractedNameList.length() > 0) {
                this.technicClassContractedNameList = technicClassContractedNameList.replace("{", "").replace("}", "").replace(",", "�A");
            }
        }
        
        //�{�p���Ԃ̕ύX start add
	/**
	 * �{�p�J�n���ԁistart_time�j�{�p�I�����ԁileave_time�j���X�V����
	 * @param con �R�l�N�V����
	 * @throws java.sql.SQLException 
	 * @return �X�V����Ftrue / �X�V�ُ�Ffalse
	 */
	public boolean updateTime(ConnectionWrapper con) throws SQLException {
            String sql = this.getUpdateTimeSQL();
            return (con.executeUpdate(sql) == 1);
	}
        
        /**
         * �{�p�J�n���ԁE�{�p�I�����Ԃ��X�V����SQL��Ԃ�
         * @return SQL��
         */
	private String	getUpdateTimeSQL() {
            String strSql = "update data_reservation\n" ;
            strSql = strSql + "set\n" ;
            // start_time
            strSql = strSql + "start_time = (case when start_time is null then visit_time else start_time end),\n" ;
            // leave_time
            strSql = strSql + "leave_time = (case when start_time is null then visit_time + " + SQLUtil.convertForSQL( this.getOpeMinute() + "minute" ) + " + " + SQLUtil.convertForSQL( this.getOpeSecond() + "second" ) + 
                              " else start_time + " + SQLUtil.convertForSQL( this.getOpeMinute() + "minute" ) + " + " + SQLUtil.convertForSQL( this.getOpeSecond() + "second" ) + " end)" + ",\n" ;
            strSql = strSql + "update_date = current_timestamp\n" ;
            strSql = strSql + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" ;
            strSql = strSql + "and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
            return strSql;
	}
        
	/**
	 * �{�p�J�n���ԁistart_time�j�A�{�p�I�����ԁileave_time�j�A��t���ԁivisit_time�j���X�V����<br>
         * �y�X�L�b�v����v�ɂ�鐸�Z���Ɏg�p�z
	 * @param con �R�l�N�V����
	 * @throws java.sql.SQLException 
	 * @return �X�V����Ftrue / �X�V�ُ�Ffalse
	 */
	public boolean updateTimeForSkipSales(ConnectionWrapper con) throws SQLException {
            String sql = this.getUpdateTimeForSkipSalesSQL();
            return (con.executeUpdate(sql) == 1);
	}
        
        /**
         * �{�p�J�n���ԁE�{�p�I�����Ԃ��X�V����SQL��Ԃ�<br>
         * �y�X�L�b�v����v�ɂ�鐸�Z���Ɏg�p�z
         * @return SQL��
         */
	private String	getUpdateTimeForSkipSalesSQL() {
            String strSql = "update data_reservation\n" ;
            strSql = strSql + "set\n" ;
            strSql = strSql + "visit_time = current_timestamp + " + SQLUtil.convertForSQL( "- " + this.getOpeMinute() + "minute" ) + " + " + SQLUtil.convertForSQL( "- " + this.getOpeSecond() + "second" ) + " ,\n";
            strSql = strSql + "start_time = current_timestamp + " + SQLUtil.convertForSQL( "- " + this.getOpeMinute() + "minute" ) + " + " + SQLUtil.convertForSQL( "- " + this.getOpeSecond() + "second" ) + " ,\n";
            strSql = strSql + "leave_time = current_timestamp ,\n" ;
            strSql = strSql + "update_date = current_timestamp \n" ;
            strSql = strSql + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" ;
            strSql = strSql + "and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
            return strSql;
	}
        //�{�p���Ԃ̕ύX end add
}
