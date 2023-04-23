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
 * メッセージユーティリティ
 * @author katagiri
 */
public class MessageUtil
{
	/**
	 * メッセージファイル名
	 */
	public static final	String	MESSAGE_FILE_NAME		=	"message.xml";
	
	/**
	 * 登録確認メッセージＩＤ
	 */
	public static final Integer CONFIRM_REGIST			=	101;
	/**
	 * 削除確認メッセージＩＤ
	 */
	public static final Integer CONFIRM_DELETE			=	102;
// 2017/01/27 顧客メモ ADD START
        /**
	 * 削除確認（削除しますか？）メッセージＩＤ
	 */
	public static final Integer CONFIRM_DELETE_FIXED		=	106;
// 2017/01/27 顧客メモ ADD END
	
	/**
	 * 登録完了メッセージＩＤ
	 */
	public static final Integer INFO_REGIST_SUCCESS		=	201;
	/**
	 * 削除完了メッセージＩＤ
	 */
	public static final Integer INFO_DELETE_SUCCESS		=	202;
	
	/**
	 * データベース接続エラーメッセージＩＤ
	 */
	public static final Integer	ERROR_CONNECT_FAILED	=	1000;
	/**
	 * 登録失敗メッセージＩＤ
	 */
	public static final Integer	ERROR_REGIST_FAILED		=	1001;
	/**
	 * 削除失敗メッセージＩＤ
	 */
	public static final Integer	ERROR_DELETE_FAILED		=	1002;
	
	/**
	 * 未入力エラーメッセージＩＤ
	 */
	public static final Integer	ERROR_INPUT_EMPTY		=	1100;
	/**
	 * 入力ミスエラーメッセージＩＤ
	 */
	public static final Integer	ERROR_INPUT_WRONG		=	1101;
	/**
	 * 未選択エラーメッセージＩＤ
	 */
	public static final Integer	ERROR_NOT_SELECTED		=	1102;
	/**
	 * 数値以外エラーメッセージＩＤ
	 */
	public static final Integer	ERROR_NOT_NUMERIC		=	1103;
	/**
	 * 存在チェックエラーメッセージＩＤ
	 */
	public static final Integer	ERROR_INPUT_NOT_EXIST	=	1110;
	/**
	 * 同一No.存在エラーメッセージＩＤ
	 */
	public static final Integer	ERROR_SAME_NO_EXIST		=	1111;
	/**
	 * 対象データが存在しないエラーメッセージＩＤ
	 */
	public static final Integer	ERROR_NO_DATA			=	1112;
        
        // IVS_LeTheHieu Start add
        public static final Integer	REGIST_WARNING_MESSAGE		=	1114;
        // IVS_LeTheHieu End   end
	
        // IVS_TTMLoan Start add 20140805
        public static final Integer	ERROR_REGIST_MESSAGE		=	1115;
        // IVS_TTMLoan End  add 20140805
        
	/**
	 * メッセージリスト
	 */
	protected static HashMap<Integer, String>	messages	=	new HashMap<Integer, String>();
	
	/** Creates a new instance of MessageUtil */
	public MessageUtil()
	{
	}
	
	
	/**
	 * mst_messageからメッセージを取得する
	 * @param messageID メッセージＩＤ
	 * @param items メッセージに埋め込む文字列の情報
	 * Integer型 － mst_message_itemから、対応する文字列を取得し埋め込む。
	 * String型 － 渡された文字列をそのまま埋め込む。
	 * @return メッセージ
	 */
	public static String getMessage(int messageID, Object... items)
	{
		return	MessageUtil.createMessage(messageID, items);
	}
	
	
	/**
	 * メッセージファイルを読み込む。
	 * @return true - 成功
	 */
	public static boolean readMassageFile() throws Exception
	{
		messages.clear();
		
		//ドキュメントビルダーファクトリを生成
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		//ドキュメントビルダーを生成
		DocumentBuilder builder = dbfactory.newDocumentBuilder();
		//パースを実行してDocumentオブジェクトを取得
		Document doc = builder.parse(MessageUtil.class.getClassLoader().getResourceAsStream(MESSAGE_FILE_NAME));
		//ルート要素を取得（タグ名：site）
		Element root = doc.getDocumentElement();
		//message要素のリストを取得
		NodeList list = root.getElementsByTagName("message");

		//message要素の数だけループ
		for(int i = 0; i < list.getLength() ; i ++)
		{
			//message要素を取得
			Element element = (Element)list.item(i);
			//id属性の値を取得
			Integer id = Integer.parseInt(element.getAttribute("id"));
			//text要素のリストを取得
			NodeList textList = element.getElementsByTagName("text");
			//text要素を取得
			Element textElement = (Element)textList.item(0);
			//text要素の最初の子ノード（テキストノード）の値を取得
			String text = textElement.getFirstChild().getNodeValue();

			text	=	text.replaceAll("\\\\n", "\n");

			messages.put(id, text);
		}
		
		return	true;
	}
	
	/**
	 * メッセージを作成する。
	 * @param messageID メッセージＩＤ
	 * @param items メッセージ引数
	 * @return 作成されたメッセージ
	 */
	private static String createMessage(int messageID, Object[] items)
	{
		String	message	=	messages.get(messageID);
		//メッセージに引数を埋め込む
		for(Integer i = 0; i < items.length; i ++)
		{
			message	=	message.replaceAll("\\{" + i.toString() + "\\}", (String)items[i]);
		}

		return	message;
	}
}
