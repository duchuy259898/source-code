/*
 * StaffRanking.java
 *
 * Created on 2008/07/20, 16:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 * �X�^�b�t�����L���O�f�[�^
 * @author saito
 */
public class StaffRanking
{
	/**
	 * �X�^�b�t
	 */
	protected	MstStaff		staff               =	new MstStaff();
	/**
	 * �ݐГX�ܖ�
	 */
	protected	String			shopName            =	"";
        /**
	 * ���q��
	 */
	protected	Long			totalCount           =	0l;
        /**
	 * �Z�p�q��
	 */
	protected	Long			techCount           =	0l;
        /**
	 * �Z�p����
	 */
	protected	Long			techSales           =	0l;
        /**
	 * �w���q��
	 */
	protected	Long			designatedCount     =	0l;
        /**
	 * �w������
	 */
	protected	Long			designatedSales     =	0l;
	/**
	 * ���i����
	 */
	protected	Long			itemSales	    =	0l;
        /**
	 * �Z�p�V�K�q��
	 */
	protected	Long			newCount     =	0l;
        /**
	 * �Z�p�V�K�Љ�q��
	 */
	protected	Long			introduceCount     =	0l;
        /**
	 * �A�v���[�`�q��
	 */
	private	Long                            approachedCount    =	0l;
        /**
	 * �A�v���[�`����
	 */
	private	Long                            approachedSales    =	0l;
        //nhanvt start add 20150309 New request #35223
	private Long staffId ;
        protected Long	    repeatRate		=   0l;

        //Luc start add 20150625 #38408
        /**
	 * ��N�̑��q��
	 */
        protected	Long			totalCountLast           =	0l;
        /**
	 * ��N�̋Z�p�q��
	 */
	protected	Long			techCountLast           =	0l;
        /**
	 * ��N�̋Z�p����
	 */
	protected	Long			techSalesLast           =	0l;
        /**
	 * ��N�̎w���q��
	 */
	protected	Long			designatedCountLast     =	0l;
        /**
	 * ��N�̎w������
	 */
	protected	Long			designatedSalesLast     =	0l;
	/**
	 * ��N�̏��i����
	 */
	protected	Long			itemSalesLast	    =	0l;
        /**
	 * ��N�̋Z�p�V�K�q��
	 */
	protected	Long			newCountLast     =	0l;
        /**
	 * ��N�̋Z�p�V�K�Љ�q��
	 */
	protected	Long			introduceCountLast     =	0l;
        /**
	 * ��N�̃A�v���[�`�q��
	 */
	private	Long                            approachedCountLast    =	0l;
        /**
	 * ��N�̃A�v���[�`����
	 */
	private	Long                            approachedSalesLast    =	0l;

        //IVS_TMTrong start edit 2015-07-20 Bug #40595
        //protected Long	    repeatRateLast		=   0l;
        //IVS_TMTrong end edit 2015-07-20 Bug #40595
        private boolean  isLastYear;
        
        //IVS_TMTrong start add 2015-07-20 Bug #40595
        private Long repeat_count = 0l;
        private Long repeat_total= 0l;
        private Long repeat_count_last=0l;
        private Long repeat_total_last=0l;
        //IVS_TMTrong end add 2015-07-20 Bug #40595
        
        //LVTu start edit 2016/01/19 New request #46728
        protected	Long			courseCount                 =	0l;
        protected	Long			courseSales                 =	0l;
        protected	Long			courseDigestionSales        =	0l;
        
        protected	Long			courseCountLast              =	0l;
        protected	Long			courseSalesLast              =	0l;
        protected	Long			courseDigestionSalesLast     =	0l;
        
        //LVTu start edit 2016/02/05 New request #46728
        private boolean courseFlag = false;

        public boolean isCourseFlag() {
            return courseFlag;
        }

        public void setCourseFlag(boolean courseFlag) {
            this.courseFlag = courseFlag;
        }
        //LVTu end edit 2016/02/05 New request #46728

        public Long getCourseCount() {
            return courseCount;
        }

        public void setCourseCount(Long courseCount) {
            this.courseCount = courseCount;
        }

        public Long getCourseSales() {
            return courseSales;
        }

        public void setCourseSales(Long courseSales) {
            this.courseSales = courseSales;
        }

        public Long getCourseDigestionSales() {
            return courseDigestionSales;
        }

        public void setCourseDigestionSales(Long courseDigestionSales) {
            this.courseDigestionSales = courseDigestionSales;
        }
        
         public Long getCourseCountLast() {
            return courseCountLast;
        }

        public void setCourseCountLast(Long courseCountLast) {
            this.courseCountLast = courseCountLast;
        }

        public Long getCourseSalesLast() {
            return courseSalesLast;
        }

        public void setCourseSalesLast(Long courseSalesLast) {
            this.courseSalesLast = courseSalesLast;
        }

        public Long getCourseDigestionSalesLast() {
            return courseDigestionSalesLast;
        }

        public void setCourseDigestionSalesLast(Long courseDigestionSalesLast) {
            this.courseDigestionSalesLast = courseDigestionSalesLast;
        }

        public boolean isIsLastYear() {
            return isLastYear;
        }

        private boolean pic;

        public boolean isPic() {
            return pic;
        }

        public void setPic(boolean pic) {
            this.pic = pic;
        }

        
        
        public void setIsLastYear(boolean isLastYear) {
            this.isLastYear = isLastYear;
        }
        
        //Luc end add 20150625 #38408
        public Long getStaffId() {
            return staffId;
        }

        public void setStaffId(Long staffId) {
            this.staffId = staffId;
        }
        
        //IVS_TMTrong start edit 2015-07-20 Bug #40595
//        public Long getRepeatRate() {
//            return repeatRate;
//        }
//
//        public void setRepeatRate(Long repeatRate) {
//            this.repeatRate = repeatRate;
//        }
        //IVS_TMTrong end edit 2015-07-20 Bug #40595
       //nhanvt end add 20150309 New request #35223
        
	/**
        * Creates a new instance of StaffRanking
        */
	public StaffRanking()
	{
	}
	
	/**
	 * ResultSetWrapper����f�[�^���擾����B
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
        
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		getStaff().setStaffName(0, rs.getString("staff_name1"));
		getStaff().setStaffName(1, rs.getString("staff_name2"));
		setShopName(rs.getString("shop_name"));
		setTotalCount(rs.getLong("totalCount"));
		setTechCount(rs.getLong("techCount"));
		setTechSales(rs.getLong("techSales"));
		setDesignatedCount(rs.getLong("designatedCount"));
		setDesignatedSales(rs.getLong("designatedSales"));
		setItemSales(rs.getLong("itemSales"));
		setNewCount(rs.getLong("newCount"));
		setIntroduceCount(rs.getLong("introduceCount"));
		setApproachedCount(rs.getLong("approachedCount"));
		setApproachedSales(rs.getLong("approachedSales"));
                //nhanvt start add 20150309 New request #35223
                setStaffId(rs.getLong("staff_id"));
                //IVS_TMTrong start add 2015-07-20 Bug #40595
                //setRepeatRate(rs.getLong("repeat_rate"));
                //IVS_TMTrong end add 2015-07-20 Bug #40595
                //nhanvt end add 20150309 New request #35223
                
               //Luc start add 20150625 #38408
                
                //IVS_TMTrong start add 2015-07-20 Bug #40595
                setCourseCount(rs.getLong("coursecount"));
                Double t = rs.getDouble("courseSales");
                setCourseSales(t.longValue());
                setCourseDigestionSales(((Double) rs.getDouble("course_digestionSales")).longValue());
                try {
                setRepeat_count(rs.getLong("repeat_count"));
                setRepeat_total(rs.getLong("repeat_total"));
                setRepeat_count_last(rs.getLong("repeat_count_last"));
                setRepeat_total_last(rs.getLong("repeat_total_last"));
                }catch(Exception e) {}
                //IVS_TMTrong end add 2015-07-20 Bug #40595
                
                //�S���҂�I������B
                if(pic) {
                    //��N
                    if(isIsLastYear()) {
                    setTotalCountLast(rs.getLong("totalCount_last"));
                    setTechCountLast(rs.getLong("techCount_last"));
                    setTechSalesLast(rs.getLong("techSales_last"));
                    setDesignatedCountLast(rs.getLong("designatedCount_last"));
                    setDesignatedSalesLast(rs.getLong("designatedSales_last"));
                    setItemSalesLast(rs.getLong("itemSales_last"));
                    setNewCountLast(rs.getLong("newCount_last"));
                    setIntroduceCountLast(rs.getLong("introduceCount_last"));
                    setApproachedCountLast(rs.getLong("approachedCount_last"));
                    setApproachedSalesLast(rs.getLong("approachedSales_last"));
                    //IVS_TMTrong start edit 2015-07-20 Bug #40595
                    //setRepeatRateLast(rs.getLong("repeat_rate_last"));
                    //IVS_TMTrong end edit 2015-07-20 Bug #40595
                    setCourseCountLast(rs.getLong("courseCount_last"));
                    setCourseSalesLast(rs.getLong("courseSales_last"));
                    setCourseDigestionSalesLast(((Double) rs.getDouble("course_digestionSales_last")).longValue());
                    }else {
                      //�ڕW
                        setTotalCountLast(rs.getLong("totalCount_target"));
                        setTechCountLast(rs.getLong("techCount_target"));
                        setTechSalesLast(rs.getLong("techSales_target"));
                        setDesignatedCountLast(rs.getLong("designatedCount_target"));
                        setDesignatedSalesLast(rs.getLong("designatedSales_target"));
                        setItemSalesLast(rs.getLong("itemSales_target"));
                        setNewCountLast(rs.getLong("newCount_target"));
                        setIntroduceCountLast(rs.getLong("introduceCount_target"));
                        setApproachedCountLast(rs.getLong("approachedCount_target"));
                        setApproachedSalesLast(rs.getLong("approachedSales_target"));
                        //IVS_TMTrong start edit 2015-07-20 Bug #40595
                        //setRepeatRateLast(rs.getLong("repeat_rate_target"));
                        setCourseSalesLast(rs.getLong("courseSales_target"));
                        setCourseDigestionSalesLast(((Double) rs.getDouble("courseDigestionSales_target")).longValue());
                        try {
                        setRepeat_count_last(rs.getLong("repeat_count_last"));
                        }catch(Exception e) {}
                        //IVS_TMTrong end edit 2015-07-20 Bug #40595
                    }
                   //Luc end add 20150625 #38408
                }
                
	}
        
        //LVTu end edit 2016/01/19 New request #46728

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
	 * �X�^�b�t�����擾����B
	 * @return �X�^�b�t��
	 */
	public String getStaffName()
	{
		if(this.getStaff() != null)
		{
			return	this.getStaff().getFullStaffName();
		}
		
		return	null;
	}

	/**
	 * �ݐГX�ܖ����擾����B
	 * @return �ݐГX�ܖ�
	 */
	public String getShopName()
	{
		return shopName;
	}

	/**
	 * �ݐГX�ܖ����Z�b�g����B
	 * @param shopName �ݐГX�ܖ�
	 */
	public void setShopName(String shopName)
	{
		this.shopName = shopName;
	}

        /**
	 * ���q�����擾����B
	 * @return ���q��
	 */
	public Long getTotalCount()
	{
		return totalCount;
	}
        
        /**
	 * ���q�����Z�b�g����B
	 * @param totalCount ���q��
	 */
	public void setTotalCount(Long totalCount)
	{
		this.totalCount = totalCount;
	}
        
        /**
	 * �Z�p�q�����擾����B
	 * @return �Z�p�q��
	 */
	public Long getTechCount()
	{
		return techCount;
	}
        
        /**
	 * �Z�p�q�����Z�b�g����B
	 * @param techCount �Z�p�q��
	 */
	public void setTechCount(Long techCount)
	{
		this.techCount = techCount;
	}
        
	/**
	 * �Z�p������擾����B
	 * @return �Z�p����
	 */
	public Long getTechSales()
	{
		return techSales;
	}

	/**
	 * �Z�p������Z�b�g����B
	 * @param techSales �Z�p����
	 */
	public void setTechSales(Long techSales)
	{
		this.techSales = techSales;
	}
	
        /**
	 * �w���q�����擾����B
	 * @return �w���q��
	 */
	public Long getDesignatedCount()
	{
		return designatedCount;
	}

	/**
	 * �w���q�����Z�b�g����B
	 * @param designatedCount �w���q��
	 */
	public void setDesignatedCount(Long designatedCount)
	{
		this.designatedCount = designatedCount;
	}
	
	/**
	 * �w��������擾����B
	 * @return �w������
	 */
	public Long getDesignatedSales()
	{
		return designatedSales;
	}

	/**
	 * �w��������Z�b�g����B
	 * @param designatedSales �w������
	 */
	public void setDesignatedSales(Long designatedSales)
	{
		this.designatedSales = designatedSales;
	}
	
	/**
	 * ���i������擾����B
	 * @return ���i����
	 */
	public Long getItemSales()
	{
		return itemSales;
	}

	/**
	 * ���i������Z�b�g����B
	 * @param itemSales ���i����
	 */
	public void setItemSales(Long itemSales)
	{
		this.itemSales = itemSales;
	}
	
        /**
	 * �V�K�q�����擾����B
	 * @return �V�K�q��
	 */
	public Long getNewCount()
	{
		return newCount;
	}
        
        /**
	 * �V�K�q�����Z�b�g����B
	 * @param newCount �V�K�q��
	 */
	public void setNewCount(Long newCount)
	{
		this.newCount = newCount;
	}
        
        /**
	 * �Z�p�V�K�Љ�q�����擾����B
	 * @return �Z�p�V�K�Љ�q��
	 */
	public Long getIntroduceCount()
	{
		return introduceCount;
	}
        
        /**
	 * �Z�p�V�K�Љ�q�����Z�b�g����B
	 * @param introduceCount �Z�p�V�K�Љ�q��
	 */
	public void setIntroduceCount(Long introduceCount)
	{
		this.introduceCount = introduceCount;
	}
        
	/**
	 * ��������擾����B
	 * @return ������
	 */
        //LVTu start edit 2016/02/05 New request #46728
	public Long getTotalSales()
	{
            if (courseFlag) {
                return techSales + itemSales + courseSales;
            }
		return techSales + itemSales;
	}

	/**
	 * ��������擾����B
	 * @return ������
	 */
	public Long getTotalSalesLast()
	{
            if (courseFlag) {
                return techSalesLast + itemSalesLast + courseSalesLast;
            }
		return techSalesLast + itemSalesLast;
	}
        //LVTu end edit 2016/02/05 New request #46728
	/**
	 * �q�P�����擾����B
	 * @return �q�P��
	 */
	public Long getUnitValue()
	{
	    if (getTotalCount() > 0)
	    {
		return getTotalSales() / getTotalCount();
	    }
	    else
	    {
		return 0l;
	    }
	}

        public Long getUnitValueLast()
	{
	    if (getTotalCountLast() > 0)
	    {
		return getTotalSalesLast() / getTotalCountLast();
	    }
	    else
	    {
		return 0l;
	    }
	}
	/**
	 * �Z�p�q�P�����擾����B
	 * @return �Z�p�q�P��
	 */
	public Long getTechUnitValue()
	{
	    if (getTechCount() > 0)
	    {
		return getTechSales() / getTechCount();
	    }
	    else
	    {
		return 0l;
	    }
	}
        /**
	 * �Z�p�q�P�����擾����B
	 * @return �Z�p�q�P��
	 */
	public Long getTechUnitValueLast()
	{
	    if (getTechCountLast() > 0)
	    {
		return getTechSalesLast() / getTechCountLast();
	    }
	    else
	    {
		return 0l;
	    }
	}


        /**
	 * �A�v���[�`�q�����擾����B
	 * @return �A�v���[�`�q��
	 */
        public Long getApproachedCount() {
            return approachedCount;
        }

        /**
	 * �A�v���[�`�q�����Z�b�g����B
	 * @param techCount �A�v���[�`�q��
	 */
        public void setApproachedCount(Long approachedCount) {
            this.approachedCount = approachedCount;
        }

	/**
	 * �A�v���[�`������擾����B
	 * @return �A�v���[�`����
	 */
        public Long getApproachedSales() {
            return approachedSales;
        }

	/**
	 * �A�v���[�`������Z�b�g����B
	 * @param techSales �A�v���[�`����
	 */
        public void setApproachedSales(Long approachedSales) {
            this.approachedSales = approachedSales;
        }

        //Luc start add 20150625 #38408
        
        public Long getTotalCountLast() {
            return totalCountLast;
        }

        public void setTotalCountLast(Long totalCountLast) {
            this.totalCountLast = totalCountLast;
        }

        public Long getTechCountLast() {
            return techCountLast;
        }

        public void setTechCountLast(Long techCountLast) {
            this.techCountLast = techCountLast;
        }

        public Long getTechSalesLast() {
            return techSalesLast;
        }

        public void setTechSalesLast(Long techSalesLast) {
            this.techSalesLast = techSalesLast;
        }

        public Long getDesignatedCountLast() {
            return designatedCountLast;
        }

        public void setDesignatedCountLast(Long designatedCountLast) {
            this.designatedCountLast = designatedCountLast;
        }

        public Long getDesignatedSalesLast() {
            return designatedSalesLast;
        }

        public void setDesignatedSalesLast(Long designatedSalesLast) {
            this.designatedSalesLast = designatedSalesLast;
        }

        public Long getItemSalesLast() {
            return itemSalesLast;
        }

        public void setItemSalesLast(Long itemSalesLast) {
            this.itemSalesLast = itemSalesLast;
        }

        public Long getNewCountLast() {
            return newCountLast;
        }

        public void setNewCountLast(Long newCountLast) {
            this.newCountLast = newCountLast;
        }

        public Long getIntroduceCountLast() {
            return introduceCountLast;
        }

        public void setIntroduceCountLast(Long introduceCountLast) {
            this.introduceCountLast = introduceCountLast;
        }

        public Long getApproachedCountLast() {
            return approachedCountLast;
        }

        public void setApproachedCountLast(Long approachedCountLast) {
            this.approachedCountLast = approachedCountLast;
        }

        public Long getApproachedSalesLast() {
            return approachedSalesLast;
        }

        public void setApproachedSalesLast(Long approachedSalesLast) {
            this.approachedSalesLast = approachedSalesLast;
        }
        
        //IVS_TMTrong start edit 2015-07-20 Bug #40595
//        public Long getRepeatRateLast() {
//            return repeatRateLast;
//        }
//
//        public void setRepeatRateLast(Long repeatRateLast) {
//            this.repeatRateLast = repeatRateLast;
//        }
        //IVS_TMTrong end edit 2015-07-20 Bug #40595
        //Luc end add 20150625 #38408

    /**
     * @return the repeat_count
     */
    public Long getRepeat_count() {
        return repeat_count;
    }

    /**
     * @param repeat_count the repeat_count to set
     */
    public void setRepeat_count(Long repeat_count) {
        this.repeat_count = repeat_count;
    }

    /**
     * @return the repeat_total
     */
    public Long getRepeat_total() {
        return repeat_total;
    }

    /**
     * @param repeat_total the repeat_total to set
     */
    public void setRepeat_total(Long repeat_total) {
        this.repeat_total = repeat_total;
    }

    /**
     * @return the repeat_count_last
     */
    public Long getRepeat_count_last() {
        return repeat_count_last;
    }

    /**
     * @param repeat_count_last the repeat_count_last to set
     */
    public void setRepeat_count_last(Long repeat_count_last) {
        this.repeat_count_last = repeat_count_last;
    }

    /**
     * @return the repeat_total_last
     */
    public Long getRepeat_total_last() {
        return repeat_total_last;
    }

    /**
     * @param repeat_total_last the repeat_total_last to set
     */
    public void setRepeat_total_last(Long repeat_total_last) {
        this.repeat_total_last = repeat_total_last;
    }
}
