/*
 * TechnicKarteReference.java
 *
 * Created on 2008/09/21, 13:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.customer;

import com.geobeck.sosia.pos.customer.TechnicKarte;
import java.sql.*;

import com.geobeck.sql.*;

/**
 * �Z�p�J���e�Q�ƃf�[�^
 * @author saito
 */
public class TechnicKarteReference<T>
{
	/**
	 * �Z�p�J���e
	 */
	protected	TechnicKarte	technicKarte	=	new TechnicKarte();
	/**
	 * �J���e�Q�Ƃh�c
	 */
	protected Integer		karteReferenceId		=	null;
	/**
	 * �J���e�Q�Ɩ�
	 */
	protected	String		karteReferenceName         =	null;
	/**
	 * �J���e�ڍׂh�c
	 */
	protected Integer		karteDetailId		=	null;
	/**
	 * �\����
	 */
	protected Integer		displaySeq              =	null;
	/**
	 * �\�[�X�I�u�W�F�N�g
	 */
        protected  T sourceObject = null;

	/**
	 * �\�[�X�I�u�W�F�N�g���擾����B
	 * @return �\�[�X�I�u�W�F�N�g
	 */
        public T getSourceObject() {
            return sourceObject;
        }

	/**
	 * �\�[�X�I�u�W�F�N�g��ݒ肷��B
	 * @param sourceObject �\�[�X�I�u�W�F�N�g
	 */
        public void setSourceObject(T sourceObject) {
            this.sourceObject = sourceObject;
        }
	
	/**
	 * �R���X�g���N�^
	 */
	public TechnicKarteReference()
	{
	}

	/**
	 * �Z�p�J���e���擾����B
	 * @return �Z�p�J���e
	 */
	public TechnicKarte getTechnicKarte()
	{
		return technicKarte;
	}

	/**
	 * �Z�p�J���e��ݒ肷��B
	 * @param technicKarte �Z�p�J���e
	 */
	public void setTechnicKarte(TechnicKarte technicKarte)
	{
		this.technicKarte = technicKarte;
	}
	
	/**
	 * ������ɕϊ�����B�i�J���e�Q�Ɩ��j
	 * @return �J���e�Q�Ɩ�
	 */
	public String toString()
	{
		return karteReferenceName;
	}

	/**
	 * �J���e�Q�Ƃh�c���擾����B
	 * @return �J���e�Q�Ƃh�c
	 */
	public Integer getKarteReferenceId()
	{
		return karteReferenceId;
	}

	/**
	* �J���e�Q�Ƃh�c���Z�b�g����B
	* @param karteReferenceId �J���e�Q�Ƃh�c
	*/
	public void setKarteReferenceId(Integer karteReferenceId)
	{
		this.karteReferenceId = karteReferenceId;
	}

	/**
	 * �J���e�Q�Ɩ����擾����B
	 * @return �J���e�Q�Ɩ�
	 */
	public String getKarteReferenceName()
	{
		return karteReferenceName;
	}

	/**
	* �J���e�Q�Ɩ����Z�b�g����B
	* @param karteReferenceName �J���e�Q�Ɩ�
	*/
	public void setKarteReferenceName(String karteReferenceName)
	{
                this.karteReferenceName	=	karteReferenceName;
	}

	/**
	 * �J���e�ڍׂh�c���擾����B
	 * @return �J���e�ڍׂh�c
	 */
	public Integer getKarteDetailId()
	{
		return karteDetailId;
	}

	/**
	* �J���e�ڍׂh�c���Z�b�g����B
	* @param karteDetailId �J���e�ڍׂh�c
	*/
	public void setKarteDetailId(Integer karteDetailId)
	{
		this.karteDetailId = karteDetailId;
	}

	/**
	 * �\�������擾����B
	 * @return �\����
	 */
	public Integer getDisplaySeq()
	{
		return displaySeq;
	}

	/**
	 * �\�������Z�b�g����B
	 * @param displaySeq �\����
	 */
	public void setDisplaySeq(Integer displaySeq)
	{
                this.displaySeq	=	null;
	}
	
	/**
	 * �f�[�^��ݒ肷��B
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setKarteReferenceId(rs.getInt("karte_reference_id"));
		this.setKarteReferenceName(rs.getString("karte_reference_name"));
		this.setKarteDetailId(rs.getInt("karte_detail_id"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
}
