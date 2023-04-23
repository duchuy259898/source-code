/*
 * MessageUtil.java
 *
 * Created on 2006/04/28, 14:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.util;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;

import com.geobeck.sql.*;

/**
 * ���b�Z�[�W���[�e�B���e�B
 * @author katagiri
 */
public class MessageUtil
{
	/**
	 * ���b�Z�[�W�t�@�C����
	 */
	public static final	String	MESSAGE_FILE_NAME		=	"message.xml";
	
	/**
	 * �o�^�m�F���b�Z�[�W�h�c
	 */
	public static final Integer CONFIRM_REGIST			=	101;
	/**
	 * �폜�m�F���b�Z�[�W�h�c
	 */
	public static final Integer CONFIRM_DELETE			=	102;
// 2017/01/27 �ڋq���� ADD START
        /**
	 * �폜�m�F�i�폜���܂����H�j���b�Z�[�W�h�c
	 */
	public static final Integer CONFIRM_DELETE_FIXED		=	106;
// 2017/01/27 �ڋq���� ADD END
	
	/**
	 * �o�^�������b�Z�[�W�h�c
	 */
	public static final Integer INFO_REGIST_SUCCESS		=	201;
	/**
	 * �폜�������b�Z�[�W�h�c
	 */
	public static final Integer INFO_DELETE_SUCCESS		=	202;
	
	/**
	 * �f�[�^�x�[�X�ڑ��G���[���b�Z�[�W�h�c
	 */
	public static final Integer	ERROR_CONNECT_FAILED	=	1000;
	/**
	 * �o�^���s���b�Z�[�W�h�c
	 */
	public static final Integer	ERROR_REGIST_FAILED		=	1001;
	/**
	 * �폜���s���b�Z�[�W�h�c
	 */
	public static final Integer	ERROR_DELETE_FAILED		=	1002;
	
	/**
	 * �����̓G���[���b�Z�[�W�h�c
	 */
	public static final Integer	ERROR_INPUT_EMPTY		=	1100;
	/**
	 * ���̓~�X�G���[���b�Z�[�W�h�c
	 */
	public static final Integer	ERROR_INPUT_WRONG		=	1101;
	/**
	 * ���I���G���[���b�Z�[�W�h�c
	 */
	public static final Integer	ERROR_NOT_SELECTED		=	1102;
	/**
	 * ���l�ȊO�G���[���b�Z�[�W�h�c
	 */
	public static final Integer	ERROR_NOT_NUMERIC		=	1103;
	/**
	 * ���݃`�F�b�N�G���[���b�Z�[�W�h�c
	 */
	public static final Integer	ERROR_INPUT_NOT_EXIST	=	1110;
	/**
	 * ����No.���݃G���[���b�Z�[�W�h�c
	 */
	public static final Integer	ERROR_SAME_NO_EXIST		=	1111;
	/**
	 * �Ώۃf�[�^�����݂��Ȃ��G���[���b�Z�[�W�h�c
	 */
	public static final Integer	ERROR_NO_DATA			=	1112;
        
        // IVS_LeTheHieu Start add
        public static final Integer	REGIST_WARNING_MESSAGE		=	1114;
        // IVS_LeTheHieu End   end
	
        // IVS_TTMLoan Start add 20140805
        public static final Integer	ERROR_REGIST_MESSAGE		=	1115;
        // IVS_TTMLoan End  add 20140805
        
	/**
	 * ���b�Z�[�W���X�g
	 */
	protected static HashMap<Integer, String>	messages	=	new HashMap<Integer, String>();
	
	/** Creates a new instance of MessageUtil */
	public MessageUtil()
	{
	}
	
	
	/**
	 * mst_message���烁�b�Z�[�W���擾����
	 * @param messageID ���b�Z�[�W�h�c
	 * @param items ���b�Z�[�W�ɖ��ߍ��ޕ�����̏��
	 * Integer�^ �| mst_message_item����A�Ή����镶������擾�����ߍ��ށB
	 * String�^ �| �n���ꂽ����������̂܂ܖ��ߍ��ށB
	 * @return ���b�Z�[�W
	 */
	public static String getMessage(int messageID, Object... items)
	{
		return	MessageUtil.createMessage(messageID, items);
	}
	
	
	/**
	 * ���b�Z�[�W�t�@�C����ǂݍ��ށB
	 * @return true - ����
	 */
	public static boolean readMassageFile() throws Exception
	{
		messages.clear();
		
		//�h�L�������g�r���_�[�t�@�N�g���𐶐�
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		//�h�L�������g�r���_�[�𐶐�
		DocumentBuilder builder = dbfactory.newDocumentBuilder();
		//�p�[�X�����s����Document�I�u�W�F�N�g���擾
		Document doc = builder.parse(MessageUtil.class.getClassLoader().getResourceAsStream(MESSAGE_FILE_NAME));
		//���[�g�v�f���擾�i�^�O���Fsite�j
		Element root = doc.getDocumentElement();
		//message�v�f�̃��X�g���擾
		NodeList list = root.getElementsByTagName("message");

		//message�v�f�̐��������[�v
		for(int i = 0; i < list.getLength() ; i ++)
		{
			//message�v�f���擾
			Element element = (Element)list.item(i);
			//id�����̒l���擾
			Integer id = Integer.parseInt(element.getAttribute("id"));
			//text�v�f�̃��X�g���擾
			NodeList textList = element.getElementsByTagName("text");
			//text�v�f���擾
			Element textElement = (Element)textList.item(0);
			//text�v�f�̍ŏ��̎q�m�[�h�i�e�L�X�g�m�[�h�j�̒l���擾
			String text = textElement.getFirstChild().getNodeValue();

			text	=	text.replaceAll("\\\\n", "\n");

			messages.put(id, text);
		}
		
		return	true;
	}
	
	/**
	 * ���b�Z�[�W���쐬����B
	 * @param messageID ���b�Z�[�W�h�c
	 * @param items ���b�Z�[�W����
	 * @return �쐬���ꂽ���b�Z�[�W
	 */
	private static String createMessage(int messageID, Object[] items)
	{
		String	message	=	messages.get(messageID);
		//���b�Z�[�W�Ɉ����𖄂ߍ���
		for(Integer i = 0; i < items.length; i ++)
		{
			message	=	message.replaceAll("\\{" + i.toString() + "\\}", (String)items[i]);
		}

		return	message;
	}
}
