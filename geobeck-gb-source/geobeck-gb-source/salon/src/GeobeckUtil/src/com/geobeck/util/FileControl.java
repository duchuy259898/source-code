/*
 * FileControl.java
 *
 * Created on 2007/10/26, 15:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.util;

import java.awt.*;
import java.io.*;
import java.lang.*;
import java.util.*;

/**
 * ファイルコントロールクラス
 * @author kanemoto
 */
public class FileControl {
	private FileReader            fr    = null;
	private BufferedReader        br    = null;
	private ByteArrayOutputStream bout  = null;
	private DataInputStream       spin  = null;
	private DataOutputStream      spout = null;
	private FileInputStream       fis   = null;
	
	public boolean endReadToByteArray() {
		try {
			if( spin != null ) spin.close();
			return true;
		} catch( Exception e ) {
			return false;
		} finally {
			spin = null;
		}
	}
	
	public boolean openReadToByteArray( String fName ) {
		try {
			endWriteToByteArray();
			// FileInputStreamオブジェクトを作成する
			spin = new DataInputStream( new FileInputStream( fName ) );
			return true;
		}
		catch( Exception e ) {
			System.out.println( "openFileException : " + fName );
		}
		return false;
	}
	
	
	/**
	 * ファイル名を指定してライン読み込みとしてファイルを開く
	 */
	public void openReadLine( String fName ) {
		try {
			// MS932で読み込むとき
			// new BufferdInputReader( new InputStreamReader( new IOStream( "filename" ), "MS932" ) ) ;
			fr = new FileReader( fName );
			br = new BufferedReader( fr );
		} catch( Exception e ) {
			System.out.println( "openReadLineException : " + e );
		}
	}
	
	/**
	 * １ライン分のデータを取得します
	 * @return 1ラインデータ
	 */
	// 1ライン分のデータを読み込む
	public String readLine() {
		// 該当データ列の取得
		try {
			return br.readLine();
		} catch( Exception e ) {
			return "";
		}
	}
	
	/**
	 * ライン読み込みファイルを閉じる
	 */
	public void closeReadLine() {
		try {
			fr.close();
		} catch( Exception e ) {
			System.out.println( "closeReadLineException : " + e );
		}
	}
	
	/**
	 * 書き込みストリームを開く
	 * @return ストリーム展開成功時にTrueを返す
	 */
	public boolean openWriteToByteArray() {
		try {
			endWrite();
			bout = new ByteArrayOutputStream();
			spout = new DataOutputStream( bout );
			return true;
		} catch( Exception e ) {
			return false;
		}
	}
	
	public int size() {
		try {
			return spout.size();
		}
		catch( Exception e ) {
			return 0;
		}
	}
	
	/**
	 * 書き込みストリームのデータを取得する
	 * @return ストリームデータ
	 */
	public byte[] endWriteToByteArray() {
		byte[] out = new byte[ 0 ];
		try {
			out = bout.toByteArray();
			endWrite();
		} catch( Exception e ) {
			out = new byte[ 0 ];
		}
		return out;
	}
	
	/**
	 * 書き込みを終了する
	 * @return ストリームを正常に閉じれた場合にTrueを返す
	 */
	public boolean endWrite() {
		try {
			if( spout != null ) spout.close();
			if( bout  != null ) bout.close();
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			spout = null;
			bout = null;
		}
	}
	
	/**
	 * 指定ファイルに指定のデータ配列を保存する
	 * @param outFileName 格納するファイル名
	 * @param otuDat 格納ファイルデータ
	 */
	public void writeFile( String outFileName, byte[] outDat ) {
		FileOutputStream     fout = null;
		BufferedOutputStream bos = null;
		
		try {
			fout = new FileOutputStream( outFileName );
			bos  = new BufferedOutputStream( fout );
			bos.write( outDat );
			bos.close();
			fout.close();
		} catch( Exception e ) {
			ErrorMsg( "writeFile : " + e );
		}
	}
	
	/**
	 * エラーメッセージを表示する
	 * @param msg 表示メッセージ
	 */
	public void ErrorMsg( String msg ) {
		System.out.println( msg );
	}
	
	//---------------------------------------------------------
	// バイト配列 の読み書き
	//---------------------------------------------------------
	
	public boolean read( byte[] b ) {
		try {
			spin.read( b );
			return true;
		}
		catch( Exception e ) {
			return false;
		}
	}
	
	/**
	 * バイト配列をストリームに書き出す
	 * @param b byte[] 書き出し要求倍と配列
	 * @return 書き出し成功時にTrueを返す
	 */
	public boolean write( byte[] b ) {
		try {
			spout.write( b );
			return true;
		}
		catch( Exception e ) {
			return false;
		}
	}
	//---------------------------------------------------------
	// １バイト の読み書き
	//---------------------------------------------------------
	public int readByte() {
		int b;
		try {
			b = spin.readByte();
			return b;
		}
		catch( Exception e ) {
			return 0;
		}
	}
	public int readUByte() {
		int b;
		try {
			b = spin.readUnsignedByte();
			return b;
		}
		catch( Exception e ) {
			return 0;
		}
	}
	/**
	 * 1バイトデータをストリームに書き出す
	 * @param b 書き出し要求値
	 * @return 書き出し成功時にTrueを返す
	 */
	public boolean writeByte(int b) {
		try {
			spout.writeByte(b);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	//---------------------------------------------------------
	// 2バイト の読み書き
	//---------------------------------------------------------
	public int readShort() {
		try {
			int i = spin.readShort();
			return i;
		}
		catch( Exception e ) {
			return 0;
		}
	}
	public int readUShort() {
		try {
			int i = spin.readUnsignedShort();
			return i;
		}
		catch( Exception e ) {
			return 0;
		}
	}
	/**
	 * 2バイトデータをストリームに書き出す
	 * @param c 書き出し要求値
	 * @return 書き出し成功時にTrueを返す
	 */
	public boolean writeShort( int c ) {
		try {
			spout.writeShort( c );
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	//---------------------------------------------------------
	// 4バイト の読み書き
	//---------------------------------------------------------
	public int readInt() {
		try {
			int i = spin.readInt();
			return i;
		}
		catch (Exception e) {
			return 0;
		}
	}
	/**
	 * 4バイトデータをストリームに書き出す
	 * @param i 書き出し要求値
	 * @return 書き出し成功時にTrueを返す
	 */
	public boolean writeInt( int i ) {
		try {
			spout.writeInt(i);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	//---------------------------------------------------------
	// 文字列 の読み書き
	//---------------------------------------------------------
	public String readString() {
		int strLen = readUByte();
		
		if( 0 < strLen ) {
			try {
				byte[] strBytes = new byte[ strLen ];
				read( strBytes );
				return new String( strBytes );
			}
			catch( Exception e ) {
			}
		}
		return null;
	}
	public String readString( int size ) {
		if( 0 < size ) {
			try {
				byte[] strBytes = new byte[ size ];
				read( strBytes );
				return new String( strBytes );
			}
			catch( Exception e ) {}
		}
		return null;
	}
	
	/**
	 * 指定文字列の文字列長をストリームに書き出す
	 * @param str 文字列長参照文字列
	 * @return 書き出し成功時にTrueを返す
	 */
	public boolean writeStringSize( String str ) {
		try {
			spout.writeByte( ( str.getBytes() ).length );
			return true;
		}
		catch( Exception e ) {
			return false;
		}
	}
	
	/**
	 * 文字列データをストリームに書き出す
	 * @param str 書き出し文字列
	 * @return 書き出し成功時にTrueを返す
	 */
	public boolean write( String str ) {
		try {
			spout.write( str.getBytes() );
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 指定ファイルが存在するかを調べる
	 */
	public boolean exists( String fName ) {
		File file = new File( fName );
		
		return file.exists();
	}
	
	/**
	 * 指定ファイル名のデータを取得する
	 * @param fName データ取得要求ファイル名
	 * @return 取得データ配列
	 */
	public byte[] readFile( String fName ) {
		byte[] readFileData;
		File   file = new File( fName );
		int    readFileSize = (int)file.length();
		readFileData = new byte[ readFileSize ];
		// ファイルデータを読み込む
		try {
			FileInputStream fis = new FileInputStream( fName );
			fis.read( readFileData );
			fis.close();
		}
		catch( Exception e ) {
			ErrorMsg( "Exception : " + e );
			ErrorMsg( "読み込みファイル名 : " + fName );
		}
		return ( readFileData );
	}
	
	/**
	 * 指定ファイル名のカレントパスを取得する
	 * @param fileName 取得カレントパス参照ファイル名
	 * @return カレントパス
	 */
	public String getCurrentName( String fileName ) {
		File file = new File( fileName );
		return file.getParent();
	}
	
	
	public void writeFile( String fileName ) {
		File file = new File( fileName );
		String currentName;
		byte[] readFileData;
		
		currentName = file.getParent();
		
		readFileData = readFile( currentName + "\\" + fileName );
		writeInt( readFileData.length );
		write( readFileData );
	}
	
	/**
	 * 指定ディレクトリを作成する
	 * @param makeDirName パスを含むディレクトリ名
	 */
	public void mkdir( String makeDirName ) {
		File file = new File( makeDirName );
		
		file.mkdir();
	}
	
	/**
	 * 指定ファイルを削除する
	 * @param fileName 削除ファイル名
	 */
	public void deleteFile( String fileName ) {
		File file = new File( fileName );
		
		// ファイルかどうかを判定する
		if( file.isFile() ) {
			file.delete();
		}
	}
	
	/**
	 * 指定パスをサブディレクトリを含めて削除する
	 */
	public void delTree( String deletePath ) {
		String[] fList;
		File     file = new File( deletePath );
		
		// 指定パスがディレクトリ
		if( file.isDirectory() ) {
			fList = file.list();
			for( int i = 0;i < fList.length; i++ ) {
				delTree( deletePath + "/" + fList[ i ] );
			}
			file.delete();
		}
		// 指定パスがファイル
		else if( file.isFile() ) {
			deleteFile( deletePath );
		}
	}
	
	/**
	 * 指定パスに存在するディレクトリ一覧を取得する
	 */
	public String[] getDirList( String path ) {
		ArrayList<String>	dirList;
		String[]			fList = null;
		File				file  = new File( path );
		
		// 指定パスがディレクトリかを確認する
		if( file.isDirectory() ) {
			dirList = new ArrayList<String>();
			fList = file.list();
			for( int i = 0;i < fList.length; i++ ) {
				if( ( new File( path + "/" + fList[ i ] ) ).isDirectory() ) dirList.add( fList[ i ] );
			}
			fList = new String[ dirList.size() ];
			dirList.toArray( fList );
		}
		return fList;
	}
	
	/**
	 * 指定パスに存在するファイル一覧を取得する
	 */
	public String[] getFileList( String path ) {
		ArrayList<String>	fileList;
		String[]			fList = null;
		File				 file  = new File( path );
		
		// 指定パスがディレクトリかを確認する
		if( file.isDirectory() ) {
			fileList = new ArrayList<String>();
			fList = file.list();
			for( int i = 0;i < fList.length; i++ ) {
				if( ( new File( path + "/" + fList[ i ] ) ).isFile() ) fileList.add( fList[ i ] );
			}
			fList = new String[ fileList.size() ];
			fileList.toArray( fList );
		}
		return fList;
	}
	
	/**
	 * ユニークな名前を作成する
	 */
	public String getUniqueName( int length ) {
		String       useCode    = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
		StringBuffer uniqueName = new StringBuffer();
		Random       rnd = new Random( System.currentTimeMillis() );
		while( uniqueName.length() < length ) {
			uniqueName.append( useCode.charAt( Math.abs( rnd.nextInt() ) % useCode.length() ) );
		}
		return uniqueName.toString();
	}
	
	/**
	 * 指定パスに存在しないユニークな名前を作成する
	 */
	public String getUniqueName( String path ) {
		String   uniqueName = null;
		File     file       = new File( path );
		String[] fList;
		
		// 指定パスがディレクトリかを確認する
		if( file.isDirectory() ) {
			fList = file.list();
			
			while( uniqueName == null ) {
				uniqueName = getUniqueName( 5 );
				for( int i = 0; i < fList.length; i++ ) {
					if( uniqueName.equals( fList[ i ] ) ) {
						uniqueName = null;
						break;
					}
				}
			}
		}
		return uniqueName;
	}
	
	/**
	 * ファイル名の拡張子を除いたファイル名を返す
	 */
	public String getSubExtFileName( String fName ) {
		int extPos = fName.lastIndexOf( "." );
		
		if( extPos == -1 ) extPos = fName.length();
		return fName.substring( 0, extPos );
	}
	
	/**
	 * 指定ファイルの絶対パスを取得する
	 */
	public String getAbsolutePath( String fName ) {
		File file = new File( fName );
		return file.getAbsolutePath();
	}
}
