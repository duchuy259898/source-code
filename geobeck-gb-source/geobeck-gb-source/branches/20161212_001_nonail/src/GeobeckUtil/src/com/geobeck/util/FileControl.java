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
 * �t�@�C���R���g���[���N���X
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
			// FileInputStream�I�u�W�F�N�g���쐬����
			spin = new DataInputStream( new FileInputStream( fName ) );
			return true;
		}
		catch( Exception e ) {
			System.out.println( "openFileException : " + fName );
		}
		return false;
	}
	
	
	/**
	 * �t�@�C�������w�肵�ă��C���ǂݍ��݂Ƃ��ăt�@�C�����J��
	 */
	public void openReadLine( String fName ) {
		try {
			// MS932�œǂݍ��ނƂ�
			// new BufferdInputReader( new InputStreamReader( new IOStream( "filename" ), "MS932" ) ) ;
			fr = new FileReader( fName );
			br = new BufferedReader( fr );
		} catch( Exception e ) {
			System.out.println( "openReadLineException : " + e );
		}
	}
	
	/**
	 * �P���C�����̃f�[�^���擾���܂�
	 * @return 1���C���f�[�^
	 */
	// 1���C�����̃f�[�^��ǂݍ���
	public String readLine() {
		// �Y���f�[�^��̎擾
		try {
			return br.readLine();
		} catch( Exception e ) {
			return "";
		}
	}
	
	/**
	 * ���C���ǂݍ��݃t�@�C�������
	 */
	public void closeReadLine() {
		try {
			fr.close();
		} catch( Exception e ) {
			System.out.println( "closeReadLineException : " + e );
		}
	}
	
	/**
	 * �������݃X�g���[�����J��
	 * @return �X�g���[���W�J��������True��Ԃ�
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
	 * �������݃X�g���[���̃f�[�^���擾����
	 * @return �X�g���[���f�[�^
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
	 * �������݂��I������
	 * @return �X�g���[���𐳏�ɕ��ꂽ�ꍇ��True��Ԃ�
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
	 * �w��t�@�C���Ɏw��̃f�[�^�z���ۑ�����
	 * @param outFileName �i�[����t�@�C����
	 * @param otuDat �i�[�t�@�C���f�[�^
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
	 * �G���[���b�Z�[�W��\������
	 * @param msg �\�����b�Z�[�W
	 */
	public void ErrorMsg( String msg ) {
		System.out.println( msg );
	}
	
	//---------------------------------------------------------
	// �o�C�g�z�� �̓ǂݏ���
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
	 * �o�C�g�z����X�g���[���ɏ����o��
	 * @param b byte[] �����o���v���{�Ɣz��
	 * @return �����o����������True��Ԃ�
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
	// �P�o�C�g �̓ǂݏ���
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
	 * 1�o�C�g�f�[�^���X�g���[���ɏ����o��
	 * @param b �����o���v���l
	 * @return �����o����������True��Ԃ�
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
	// 2�o�C�g �̓ǂݏ���
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
	 * 2�o�C�g�f�[�^���X�g���[���ɏ����o��
	 * @param c �����o���v���l
	 * @return �����o����������True��Ԃ�
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
	// 4�o�C�g �̓ǂݏ���
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
	 * 4�o�C�g�f�[�^���X�g���[���ɏ����o��
	 * @param i �����o���v���l
	 * @return �����o����������True��Ԃ�
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
	// ������ �̓ǂݏ���
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
	 * �w�蕶����̕����񒷂��X�g���[���ɏ����o��
	 * @param str �����񒷎Q�ƕ�����
	 * @return �����o����������True��Ԃ�
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
	 * ������f�[�^���X�g���[���ɏ����o��
	 * @param str �����o��������
	 * @return �����o����������True��Ԃ�
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
	 * �w��t�@�C�������݂��邩�𒲂ׂ�
	 */
	public boolean exists( String fName ) {
		File file = new File( fName );
		
		return file.exists();
	}
	
	/**
	 * �w��t�@�C�����̃f�[�^���擾����
	 * @param fName �f�[�^�擾�v���t�@�C����
	 * @return �擾�f�[�^�z��
	 */
	public byte[] readFile( String fName ) {
		byte[] readFileData;
		File   file = new File( fName );
		int    readFileSize = (int)file.length();
		readFileData = new byte[ readFileSize ];
		// �t�@�C���f�[�^��ǂݍ���
		try {
			FileInputStream fis = new FileInputStream( fName );
			fis.read( readFileData );
			fis.close();
		}
		catch( Exception e ) {
			ErrorMsg( "Exception : " + e );
			ErrorMsg( "�ǂݍ��݃t�@�C���� : " + fName );
		}
		return ( readFileData );
	}
	
	/**
	 * �w��t�@�C�����̃J�����g�p�X���擾����
	 * @param fileName �擾�J�����g�p�X�Q�ƃt�@�C����
	 * @return �J�����g�p�X
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
	 * �w��f�B���N�g�����쐬����
	 * @param makeDirName �p�X���܂ރf�B���N�g����
	 */
	public void mkdir( String makeDirName ) {
		File file = new File( makeDirName );
		
		file.mkdir();
	}
	
	/**
	 * �w��t�@�C�����폜����
	 * @param fileName �폜�t�@�C����
	 */
	public void deleteFile( String fileName ) {
		File file = new File( fileName );
		
		// �t�@�C�����ǂ����𔻒肷��
		if( file.isFile() ) {
			file.delete();
		}
	}
	
	/**
	 * �w��p�X���T�u�f�B���N�g�����܂߂č폜����
	 */
	public void delTree( String deletePath ) {
		String[] fList;
		File     file = new File( deletePath );
		
		// �w��p�X���f�B���N�g��
		if( file.isDirectory() ) {
			fList = file.list();
			for( int i = 0;i < fList.length; i++ ) {
				delTree( deletePath + "/" + fList[ i ] );
			}
			file.delete();
		}
		// �w��p�X���t�@�C��
		else if( file.isFile() ) {
			deleteFile( deletePath );
		}
	}
	
	/**
	 * �w��p�X�ɑ��݂���f�B���N�g���ꗗ���擾����
	 */
	public String[] getDirList( String path ) {
		ArrayList<String>	dirList;
		String[]			fList = null;
		File				file  = new File( path );
		
		// �w��p�X���f�B���N�g�������m�F����
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
	 * �w��p�X�ɑ��݂���t�@�C���ꗗ���擾����
	 */
	public String[] getFileList( String path ) {
		ArrayList<String>	fileList;
		String[]			fList = null;
		File				 file  = new File( path );
		
		// �w��p�X���f�B���N�g�������m�F����
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
	 * ���j�[�N�Ȗ��O���쐬����
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
	 * �w��p�X�ɑ��݂��Ȃ����j�[�N�Ȗ��O���쐬����
	 */
	public String getUniqueName( String path ) {
		String   uniqueName = null;
		File     file       = new File( path );
		String[] fList;
		
		// �w��p�X���f�B���N�g�������m�F����
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
	 * �t�@�C�����̊g���q���������t�@�C������Ԃ�
	 */
	public String getSubExtFileName( String fName ) {
		int extPos = fName.lastIndexOf( "." );
		
		if( extPos == -1 ) extPos = fName.length();
		return fName.substring( 0, extPos );
	}
	
	/**
	 * �w��t�@�C���̐�΃p�X���擾����
	 */
	public String getAbsolutePath( String fName ) {
		File file = new File( fName );
		return file.getAbsolutePath();
	}
}
