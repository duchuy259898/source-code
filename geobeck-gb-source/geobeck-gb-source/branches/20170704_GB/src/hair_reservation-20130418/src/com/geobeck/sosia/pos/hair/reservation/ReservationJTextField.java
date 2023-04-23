/*
 * ReservationJTextField.java
 *
 * Created on 2006/06/16, 19:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.reservation;

import com.geobeck.sosia.pos.hair.data.reservation.*;
import com.geobeck.sosia.pos.hair.master.product.MstCustomerFreeHeading;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.DateUtil;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import javax.swing.*;

/**
 * �^�C���X�P�W���[����ʗpJTextField
 * @author katagiri
 */
public class ReservationJTextField extends JLabel
{
	/**
	 * �^�C���X�P�W���[����ʗp�\��w�b�_�f�[�^
	 */
	protected	ReservationHeader header = null;
	/**
	 * �\��f�[�^
	 */
	protected	DataReservation	reservation = new DataReservation();

        /**
         * �\���
         */
        protected       java.util.Date reservationDate = null;

	/**
	 * �R���X�g���N�^
	 */
	public ReservationJTextField()
	{
            super();
            this.setOpaque(true);
	}

	/**
	 * �R���X�g���N�^
	 * @param text ����
	 */
	public ReservationJTextField(String text)
	{
		super(text);
	}

	/**
	 * �^�C���X�P�W���[����ʗp�\��w�b�_�f�[�^���擾����B
	 * @return �^�C���X�P�W���[����ʗp�\��w�b�_�f�[�^
	 */
	public ReservationHeader getHeader()
	{
		return header;
	}

	/**
	 * �^�C���X�P�W���[����ʗp�\��w�b�_�f�[�^���Z�b�g����B
	 * @param header �^�C���X�P�W���[����ʗp�\��w�b�_�f�[�^
	 */
	public void setHeader(ReservationHeader header)
	{
		this.header = header;
	}

	/**
	 * �\��f�[�^���擾����B
	 * @return �\��f�[�^
	 */
	public DataReservation getReservation()
	{
		return reservation;
	}

	/**
	 * �\��f�[�^���Z�b�g����B
	 * @param reservation �\��f�[�^
	 */
	public void setReservation(DataReservation reservation)
	{
		this.reservation = reservation;
	}
	
        public java.util.Date getReservationDate() {
            return reservationDate;
        }

        public void setReservationDate(java.util.Date reservationDate) {
            this.reservationDate = reservationDate;
        }
	
	/**
	 * �T�C�Y��ݒ肷��B
	 * @param term �\���P��
	 * @param colWidth ��̕�
	 * @param rowHeight �s�̍���
	 */
	public void setSize(int term, int colWidth, int rowHeight)
	{
		//�{�p���Ԃ��ݒ肳��Ă��Ȃ��ꍇ�P�񕪂̕��ɂ���
		if(reservation.getTotalTime() == null)
		{
			this.setSize(colWidth - 1, rowHeight);
		}
		else
		{
			//�񕝂��i�{�p���ԁ��\���P�ʁj�̐؂�グ�Ŏ擾����
			int	cols	=	reservation.getTotalTime() / term;
			if(0 < reservation.getTotalTime() % term)
					cols ++;
			if(cols == 0)	cols = 1;
			this.setSize(colWidth * cols - 1, rowHeight);
		}
	}
	
	/**
	 * ���Ԃ��擾����B
	 * @param currentDate ��ƂȂ���t
	 * @return ����
	 */
	public int getHour(Date currentDate)
	{
		GregorianCalendar	cd	=	new GregorianCalendar();
		cd.setTime(currentDate);
		
		if(reservation.getDRDStartReservationDatetime() == null)
		{
			return	0;
		}
		else
		{
			int hour = reservation.getDRDStartReservationDatetime().get(Calendar.HOUR_OF_DAY);
			
			if(cd.get(cd.DAY_OF_MONTH) != reservation.getDRDStartReservationDatetime().get(Calendar.DAY_OF_MONTH))
					hour += 24;
			
			return hour;
		}
	}
	
	/**
	 * �����擾����B
	 * @return ��
	 */
	public int getMinute()
	{
		if(reservation.getDRDStartReservationDatetime() == null)
		{
			return	0;
		}
		else
		{
			return	reservation.getDRDStartReservationDatetime().get(Calendar.MINUTE);
		}
	}
	
	public String getTermString()
	{
		GregorianCalendar endTime = new GregorianCalendar();
		endTime.setTime(reservation.getDRDStartReservationDatetime().getTime());
		endTime.add(endTime.MINUTE, reservation.getTotalTime());
		return getFormatTime(reservation.getDRDStartReservationDatetime()) + "�`" + getFormatTime(endTime);
	}
	
        private String getFormatTime(Calendar cal) {

                int h = cal.get(Calendar.HOUR_OF_DAY);
                int m = cal.get(Calendar.MINUTE);
                if (Integer.parseInt(DateUtil.format(reservationDate, "dd")) + 1 == cal.get(Calendar.DAY_OF_MONTH)) {
                    h += 24;
                }

                return String.format("%1$02d", h) + ":" + String.format("%1$02d", m);
        }
	
	/**
	 * Text��ToolTipText���Z�b�g����B
	 */
	public void setTexts(MstShop shop, boolean isOutStaff)
	{
            String s =
                "<html>&nbsp;" + reservation.getTechnicClassContractedName() +
                (isOutStaff ? "�i" + reservation.getDRDStaffName() + "�j" : "") +
                "<br>&nbsp;" + (reservation.getDesignated() ? "[�w]" : "[F]") +
                (reservation.getComment().length() > 0 ? "[����]" : "") +
                reservation.getCustomer().getFullCustomerName() + "</html>";

            this.setText(s);

            StringBuffer stringbuffer = new StringBuffer();
            stringbuffer.append("<html><table>");
            stringbuffer.append("<tr><th>�ڋqNo.</th><td>" + reservation.getCustomer().getCustomerNo() + "</td></tr>");
            stringbuffer.append("<tr><th>�ڋq��</th><td>" + reservation.getCustomer().getFullCustomerName() + "�i" + reservation.getCustomer().getFullCustomerKana() + "�j</td></tr>");

            String birth = "";
            if (reservation.getCustomer().getBirthday() != null) {
                Integer ageTemp = DateUtil.calcAge(
                    new com.ibm.icu.util.GregorianCalendar(),
                    reservation.getCustomer().getBirthday());
                birth = reservation.getCustomer().getBirthdayString("yyyy�NM��d��") + " (" + ageTemp + "��)";
            }
            stringbuffer.append("<tr><th>�a����</th><td>" + birth + "</td></tr>");

            stringbuffer.append("<tr><th>�X�^�b�t</th><td>" + reservation.getDRDFullStaffName() + "</td></tr>");
            if (shop != null && shop.isBed()) {
                //�{�p��L
                stringbuffer.append("<tr><th>�{�p��</th><td>" + reservation.getDRDBedName() + "</td></tr>");
            }
            stringbuffer.append("<tr><th>�Z�p</th><td>" + reservation.getDRDTechnicName() + "</td></tr>");
            stringbuffer.append("<tr><th>����</th><td>" + this.getTermString() + "</td></tr>");
            stringbuffer.append("<tr><th valign='top'>�\�񃁃�</th><td>" + ReservationStatusPanel.formatHTMLString(ReservationStatusPanel.insertNewLineString(reservation.getComment())) + "</td></tr>");

/**
            // �t���[����
            ArrayList<MstCustomerFreeHeading> mcfhs = new ArrayList<MstCustomerFreeHeading>();
	    try {
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(this.getFreeHeadingSelectSQL());
		while (rs.next()) {
                    MstCustomerFreeHeading mt = new MstCustomerFreeHeading();
                    mt.setData(rs);
                    mcfhs.add(mt);
		}
		rs.close();
	    } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }

            for ( MstCustomerFreeHeading mcfh : mcfhs ) {
                String freeHeadingClassName = mcfh.getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassName();
                String freeHeadingName = mcfh.getMstFreeHeading().getFreeHeadingName();
                if (freeHeadingName != null) {
                    stringbuffer.append("<tr><th colspan='2' align='left' valign='top'>" + freeHeadingClassName + "</th></tr>");
                    stringbuffer.append("<tr><td colspan='2' align='left'>" + freeHeadingName + "</td></tr>");
                }
            }
**/
            stringbuffer.append("</table></html>");
            this.setToolTipText(stringbuffer.toString());
	}

	/**
	 * �t���[���ڎ擾
	 * @return Select��
	 */
	private String getFreeHeadingSelectSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      mfhc.*");
            sql.append("     ,mc.*");
            sql.append("     ,mfh.*");
            sql.append("     ,coalesce( mfh.free_heading_id, -1 )");
            sql.append("     ,mfh.free_heading_name");
            sql.append("     ,mfh.display_seq");
            sql.append(" from");
            sql.append("     mst_free_heading_class as mfhc");
            sql.append("         left join mst_customer_free_heading as mcfh");
            sql.append("                on mcfh.delete_date is null");
            sql.append("               and mcfh.customer_id = " + SQLUtil.convertForSQL(reservation.getCustomer().getCustomerID()));
            sql.append("               and mcfh.free_heading_class_id = mfhc.free_heading_class_id");
            sql.append("         left join mst_customer as mc");
            sql.append("                on mc.delete_date is null");
            sql.append("               and mc.customer_id = " + SQLUtil.convertForSQL(reservation.getCustomer().getCustomerID()));
            sql.append("         left join mst_free_heading as mfh");
            sql.append("                on mfh.delete_date is null");
            sql.append("               and mfh.free_heading_class_id = mcfh.free_heading_class_id");
            sql.append("               and mfh.free_heading_id = mcfh.free_heading_id");
            sql.append(" where");
            sql.append("         mfhc.delete_date is null");
            sql.append("     and mfhc.use_type = 't'");
            sql.append(" order by");
            sql.append("     mfhc.free_heading_class_id");

            return sql.toString();
	}

}
