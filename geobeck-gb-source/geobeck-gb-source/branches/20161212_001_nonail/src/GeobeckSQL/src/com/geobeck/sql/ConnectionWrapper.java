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
 * Connectionのラッパークラス
 * @author katagiri
 */
public class ConnectionWrapper
{
	/**
	 * コネクション
	 */
	protected	Connection		con			=	null;
	
	/**
	 * ドライバクラス名
	 */
	protected	String			driver		=	"";
	
	/**
	 * データベースのURL
	 */
	protected	String			url			=	"";
	
	/**
	 * ユーザーID
	 */
	protected	String			user		=	"";
	
	/**
	 * パスワード
	 */
	protected	String			password	=	"";
	
	/**
	 * トランザクション開始フラグ
	 */
	protected	boolean			beginTran	=	false;
	
	/**
	 * コンストラクタ
	 */
	public ConnectionWrapper()
	{
		this("", "", "", "");
	}
	
	/**
	 * コンストラクタ
	 * @param driver ドライバクラス名
	 */
	public ConnectionWrapper(String driver)
	{
		this(driver, "", "", "");
	}
	
	/**
	 * コンストラクタ
	 * @param driver ドライバクラス名
	 * @param url データベースのURL
	 */
	public ConnectionWrapper(String driver, String url)
	{
		this(driver, url, "", "");
	}

	/**
	 * コンストラクタ
	 * @param driver ドライバクラス名
	 * @param url データベースのURL
	 * @param user ユーザーID
	 * @param password パスワード
	 */
	public ConnectionWrapper(String driver, String url, String user, String password)
	{
		this.setDriver(driver);
		this.setUrl(url);
		this.setUser(user);
		this.setPassword(password);
	}
	
	/**
	 * ファイナライザ
	 * @throws java.sql.SQLException 
	 */
	public void finalize() throws SQLException
	{
		this.close();
	}
	
	
	/**
	 * ドライバクラス名を取得する。
	 * @return ドライバクラス名
	 */
	public String getDriver()
	{
		return driver;
	}

	/**
	 * ドライバクラス名を設定する。
	 * @param driver ドライバクラス名
	 */
	public void setDriver(String driver)
	{
		this.driver = driver;
	}
	
	/**
	 * データベースのURLを取得する。
	 * @return データベースのURL
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * データベースのURLを設定する。
	 * @param url データベースのURL
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}

	/**
	 * ユーザーIDを取得する。
	 * @return ユーザーID
	 */
	public String getUser()
	{
		return user;
	}

	/**
	 * ユーザーIDを設定する。
	 * @param user ユーザーID
	 */
	public void setUser(String user)
	{
		this.user = user;
	}

	/**
	 * パスワードを取得する。
	 * @return パスワード
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * パスワードを設定する。
	 * @param password パスワード
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	/**
	 * Connectionを取得する。
	 * @return Connection
	 */
	public Connection getConnection()
	{
		return	con;
	}

	/**
	 * トランザクションが開始されているかを取得する。
	 * @return true - トランザクションが開始されている。
	 */
	public boolean isBeginTran()
	{
		return beginTran;
	}

	/**
	 * トランザクションが開始されているかを設定する。
	 * @param beginTran true - トランザクションが開始されている。
	 */
	public void setBeginTran(boolean beginTran)
	{
		this.beginTran = beginTran;
	}
	
	/**
	 * コネクションオープン
	 * @return true - 成功
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
	 * トランザクション開始
	 * @throws java.sql.SQLException 
	 */
	public void begin() throws SQLException
	{
		con.setAutoCommit(false);
		this.setBeginTran(true);
	}
	
	/**
	 * 選択クエリを実行して、ResultSetWrapperを返す。
	 * @param sql 実行するクエリ
	 * @throws java.sql.SQLException 
	 * @return 結果が格納されたResultSetWrapper
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
	 * クエリを実行する。
	 * @param sql 実行するクエリ
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean execute(String sql) throws SQLException
	{
		Statement	st	=	con.createStatement();
		
		return	st.execute(sql);
	}
	
	/**
	 * 更新クエリを実行する。
	 * @param sql 実行するクエリ
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
	 * コネクションを閉じる。
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
