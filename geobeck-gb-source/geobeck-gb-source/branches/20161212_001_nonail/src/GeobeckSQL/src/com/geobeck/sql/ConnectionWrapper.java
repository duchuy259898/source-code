/*
 * ConnectionWrapper.java
 *
 * Created on 2006/04/19, 15:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sql;

import java.sql.*;
import java.util.*;

/**
 * Connection�̃��b�p�[�N���X
 * @author katagiri
 */
public class ConnectionWrapper
{
	/**
	 * �R�l�N�V����
	 */
	protected	Connection		con			=	null;
	
	/**
	 * �h���C�o�N���X��
	 */
	protected	String			driver		=	"";
	
	/**
	 * �f�[�^�x�[�X��URL
	 */
	protected	String			url			=	"";
	
	/**
	 * ���[�U�[ID
	 */
	protected	String			user		=	"";
	
	/**
	 * �p�X���[�h
	 */
	protected	String			password	=	"";
	
	/**
	 * �g�����U�N�V�����J�n�t���O
	 */
	protected	boolean			beginTran	=	false;
	
	/**
	 * �R���X�g���N�^
	 */
	public ConnectionWrapper()
	{
		this("", "", "", "");
	}
	
	/**
	 * �R���X�g���N�^
	 * @param driver �h���C�o�N���X��
	 */
	public ConnectionWrapper(String driver)
	{
		this(driver, "", "", "");
	}
	
	/**
	 * �R���X�g���N�^
	 * @param driver �h���C�o�N���X��
	 * @param url �f�[�^�x�[�X��URL
	 */
	public ConnectionWrapper(String driver, String url)
	{
		this(driver, url, "", "");
	}

	/**
	 * �R���X�g���N�^
	 * @param driver �h���C�o�N���X��
	 * @param url �f�[�^�x�[�X��URL
	 * @param user ���[�U�[ID
	 * @param password �p�X���[�h
	 */
	public ConnectionWrapper(String driver, String url, String user, String password)
	{
		this.setDriver(driver);
		this.setUrl(url);
		this.setUser(user);
		this.setPassword(password);
	}
	
	/**
	 * �t�@�C�i���C�U
	 * @throws java.sql.SQLException 
	 */
	public void finalize() throws SQLException
	{
		this.close();
	}
	
	
	/**
	 * �h���C�o�N���X�����擾����B
	 * @return �h���C�o�N���X��
	 */
	public String getDriver()
	{
		return driver;
	}

	/**
	 * �h���C�o�N���X����ݒ肷��B
	 * @param driver �h���C�o�N���X��
	 */
	public void setDriver(String driver)
	{
		this.driver = driver;
	}
	
	/**
	 * �f�[�^�x�[�X��URL���擾����B
	 * @return �f�[�^�x�[�X��URL
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * �f�[�^�x�[�X��URL��ݒ肷��B
	 * @param url �f�[�^�x�[�X��URL
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}

	/**
	 * ���[�U�[ID���擾����B
	 * @return ���[�U�[ID
	 */
	public String getUser()
	{
		return user;
	}

	/**
	 * ���[�U�[ID��ݒ肷��B
	 * @param user ���[�U�[ID
	 */
	public void setUser(String user)
	{
		this.user = user;
	}

	/**
	 * �p�X���[�h���擾����B
	 * @return �p�X���[�h
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * �p�X���[�h��ݒ肷��B
	 * @param password �p�X���[�h
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	/**
	 * Connection���擾����B
	 * @return Connection
	 */
	public Connection getConnection()
	{
		return	con;
	}

	/**
	 * �g�����U�N�V�������J�n����Ă��邩���擾����B
	 * @return true - �g�����U�N�V�������J�n����Ă���B
	 */
	public boolean isBeginTran()
	{
		return beginTran;
	}

	/**
	 * �g�����U�N�V�������J�n����Ă��邩��ݒ肷��B
	 * @param beginTran true - �g�����U�N�V�������J�n����Ă���B
	 */
	public void setBeginTran(boolean beginTran)
	{
		this.beginTran = beginTran;
	}
	
	/**
	 * �R�l�N�V�����I�[�v��
	 * @return true - ����
	 */
	public boolean open()
	{
		try
		{
			Class.forName(this.getDriver());
		}
		catch(ClassNotFoundException e)
		{
			return	false;
		}
		
		try
		{
                    //-- 2013/05/02 GB Start
                    // 12hour = 43200sec
                    DriverManager.setLoginTimeout(43200);
                    //-- 2013/05/02 GB End


                    if(this.getUser().equals(""))
			{
				con	=	DriverManager.getConnection(this.getUrl());
			}
			else
			{
				con	=	DriverManager.getConnection(this.getUrl(),
													  this.getUser(),
													  this.getPassword());
			}
		}
		catch(SQLException e)
		{
			return	false;
		}
		
		return	true;
	}
	
	/**
	 * �g�����U�N�V�����J�n
	 * @throws java.sql.SQLException 
	 */
	public void begin() throws SQLException
	{
		con.setAutoCommit(false);
		this.setBeginTran(true);
	}
	
	/**
	 * �I���N�G�������s���āAResultSetWrapper��Ԃ��B
	 * @param sql ���s����N�G��
	 * @throws java.sql.SQLException 
	 * @return ���ʂ��i�[���ꂽResultSetWrapper
	 */
	public ResultSetWrapper executeQuery(String sql) throws SQLException
	{
		ResultSetWrapper	rs	=	new ResultSetWrapper();
		
                try {
			rs.executeQuery(con, sql);
		} catch (SQLException e) {
			throw new SQLException("sql=" + sql, e);
		}
		
		return	rs;
	}
	
	/**
	 * �N�G�������s����B
	 * @param sql ���s����N�G��
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean execute(String sql) throws SQLException
	{
		Statement	st	=	con.createStatement();
		
		return	st.execute(sql);
	}
	
	/**
	 * �X�V�N�G�������s����B
	 * @param sql ���s����N�G��
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public int executeUpdate(String sql) throws SQLException
	{
		Statement	st	=	con.createStatement();
		
		return	st.executeUpdate(sql);
	}
	
	public void clearWarnings() throws SQLException
	{
		con.clearWarnings();
	}
	
	/**
	 * �R�l�N�V���������B
	 * @throws java.sql.SQLException 
	 */
	public void close() throws SQLException
	{
		if(con != null && !con.isClosed())
		{
			if(this.isBeginTran())	this.rollback();
			
			con.close();
		}
	}

	public void commit() throws SQLException
	{
		con.commit();
		this.setAutoCommit(true);
		this.setBeginTran(false);
	}

	public Statement createStatement() throws SQLException
	{
		return	con.createStatement();
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException
	{
		return	con.createStatement(resultSetType, resultSetConcurrency);
	}

	public Statement createStatement(int resultSetType,
										int resultSetConcurrency,
										int resultSetHoldability) throws SQLException
	{
		return	con.createStatement(resultSetType,
										resultSetConcurrency,
										resultSetHoldability);
	}

	public boolean getAutoCommit() throws SQLException
	{
		return	con.getAutoCommit();
	}

	public String getCatalog() throws SQLException
	{
		return	con.getCatalog();
	}
	public int getHoldability() throws SQLException
	{
		return	con.getHoldability();
	}

	public DatabaseMetaData getMetaData() throws SQLException
	{
		return	con.getMetaData();
	}

	public int getTransactionIsolation() throws SQLException
	{
		return	con.getTransactionIsolation();
	}

	public Map<String,Class<?>> getTypeMap() throws SQLException
	{
		return	con.getTypeMap();
	}

	public SQLWarning getWarnings() throws SQLException
	{
		return	con.getWarnings();
	}

	public boolean isClosed() throws SQLException
	{
		return	con.isClosed();
	}

	public boolean isReadOnly() throws SQLException
	{
		return	con.isReadOnly();
	}

	public String nativeSQL(String sql) throws SQLException
	{
		return	con.nativeSQL(sql);
	}

	public CallableStatement prepareCall(String sql) throws SQLException
	{
		return	con.prepareCall(sql);
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
	{
		return	con.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
	{
		return	con.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException
	{
		return	con.prepareStatement(sql);
	}
	
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException
	{
		return	con.prepareStatement(sql, autoGeneratedKeys);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException
	{
		return	con.prepareStatement(sql, columnIndexes);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
	{
		return	con.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
	{
		return	con.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException
	{
		return	con.prepareStatement(sql, columnNames);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException
	{
		con.releaseSavepoint(savepoint);
	}

	public void rollback() throws SQLException
	{
		con.rollback();
		this.setAutoCommit(true);
		this.setBeginTran(false);
	}

	public void rollback(Savepoint savepoint) throws SQLException
	{
		con.rollback(savepoint);
		this.setAutoCommit(true);
		this.setBeginTran(false);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException
	{
		con.setAutoCommit(autoCommit);
	}

	public void setCatalog(String catalog) throws SQLException
	{
		con.setCatalog(catalog);
	}

	public void setHoldability(int holdability) throws SQLException
	{
		con.setHoldability(holdability);
	}
	
	public void setReadOnly(boolean readOnly) throws SQLException
	{
		con.setReadOnly(readOnly);
	}

	public Savepoint setSavepoint() throws SQLException
	{
		return	con.setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException
	{
		return	con.setSavepoint(name);
	}

	public void setTransactionIsolation(int level) throws SQLException
	{
		con.setTransactionIsolation(level);
	}

	public void setTypeMap(Map<String,Class<?>> map) throws SQLException
	{
		con.setTypeMap(map);
	}
}
