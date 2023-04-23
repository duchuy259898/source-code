/*
 * ResultSetWrapper.java
 *
 * Created on 2006/04/19, 15:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sql;

import java.io.*;
import java.math.*;
import java.net.*;
import java.sql.*;
import java.util.*;

/**
 * StatementとResultSetのラッパークラス
 * @author katagiri
 */
public class ResultSetWrapper
{
	protected	Statement	st		=	null;
	
	protected	ResultSet	rs		=	null;
	
	/** Creates a new instance of ResultSetWrapper */
	public ResultSetWrapper()
	{
	}
	
	public void finalize() throws SQLException
	{
		this.close();
	}
	
	public void executeQuery(Connection con, String sql) throws SQLException
	{
		st	=	con.createStatement();
		rs	=	st.executeQuery(sql);
	}
	
	public boolean absolute(int row) throws SQLException
	{
		return	rs.absolute(row);
	}

	public void afterLast() throws SQLException
	{
		rs.afterLast();
	}

	public void beforeFirst() throws SQLException
	{
		rs.beforeFirst();
	}

	public void cancelRowUpdates() throws SQLException
	{
		rs.cancelRowUpdates();
	}

	public void clearWarnings() throws SQLException
	{
		rs.clearWarnings();
	}

	public void close() throws SQLException
	{
		if(rs != null)
		{
			rs.close();
		}
		if(st != null)
		{
			st.close();
		}
	}

	public void deleteRow() throws SQLException
	{
		rs.deleteRow();
	}

	public int findColumn(String columnName) throws SQLException
	{
		return	rs.findColumn(columnName);
	}

	public boolean first() throws SQLException
	{
		return	rs.first();
	}

	public Array getArray(int i) throws SQLException
	{
		return	rs.getArray(i);
	}

	public Array getArray(String colName) throws SQLException
	{
		return	rs.getArray(colName);
	}

	public InputStream getAsciiStream(int columnIndex) throws SQLException
	{
		return	rs.getAsciiStream(columnIndex);
	}

	public InputStream getAsciiStream(String columnName) throws SQLException
	{
		return	rs.getAsciiStream(columnName);
	}

	public BigDecimal getBigDecimal(int columnIndex) throws SQLException
	{
		return	rs.getBigDecimal(columnIndex);
	}
	
	public BigDecimal getBigDecimal(String columnName) throws SQLException
	{
		return	rs.getBigDecimal(columnName);
	}

	public InputStream getBinaryStream(int columnIndex) throws SQLException
	{
		return	rs.getBinaryStream(columnIndex);
	}

	public InputStream getBinaryStream(String columnName) throws SQLException
	{
		return	rs.getBinaryStream(columnName);
	}

	public Blob getBlob(int i) throws SQLException
	{
		return	rs.getBlob(i);
	}

	public Blob getBlob(String colName) throws SQLException
	{
		return	rs.getBlob(colName);
	}

	public boolean getBoolean(int columnIndex) throws SQLException
	{
		return	rs.getBoolean(columnIndex);
	}

	public boolean getBoolean(String columnName) throws SQLException
	{
		return	rs.getBoolean(columnName);
	}

	public byte getByte(int columnIndex) throws SQLException
	{
		return	rs.getByte(columnIndex);
	}

	public byte getByte(String columnName) throws SQLException
	{
		return	rs.getByte(columnName);
	}

	public byte[] getBytes(int columnIndex) throws SQLException
	{
		return	rs.getBytes(columnIndex);
	}

	public byte[] getBytes(String columnName) throws SQLException
	{
		return	rs.getBytes(columnName);
	}

	public Reader getCharacterStream(int columnIndex) throws SQLException
	{
		return	rs.getCharacterStream(columnIndex);
	}

	public Reader getCharacterStream(String columnName) throws SQLException
	{
		return	rs.getCharacterStream(columnName);
	}

	public Clob getClob(int i) throws SQLException
	{
		return	rs.getClob(i);
	}

	public Clob getClob(String colName) throws SQLException
	{
		return	rs.getClob(colName);
	}

	public int getConcurrency() throws SQLException
	{
		return	rs.getConcurrency();
	}

	public String getCursorName() throws SQLException
	{
		return	rs.getCursorName();
	}

	public java.sql.Date getDate(int columnIndex) throws SQLException
	{
		return	rs.getDate(columnIndex);
	}

	public java.sql.Date getDate(int columnIndex, Calendar cal) throws SQLException
	{
		return	rs.getDate(columnIndex, cal);
	}

	public java.sql.Date getDate(String columnName) throws SQLException
	{
		return	rs.getDate(columnName);
	}

	public java.sql.Date getDate(String columnName, Calendar cal) throws SQLException
	{
		return	rs.getDate(columnName, cal);
	}

	public double getDouble(int columnIndex) throws SQLException
	{
		return	rs.getDouble(columnIndex);
	}

	public double getDouble(String columnName) throws SQLException
	{
		return	rs.getDouble(columnName);
	}

	public int getFetchDirection() throws SQLException
	{
		return	rs.getFetchDirection();
	}

	public int getFetchSize() throws SQLException
	{
		return	rs.getFetchSize();
	}

	public float getFloat(int columnIndex) throws SQLException
	{
		return	rs.getFloat(columnIndex);
	}

	public float getFloat(String columnName) throws SQLException
	{
		return	rs.getFloat(columnName);
	}
	
	public GregorianCalendar getGregorianCalendar(int columnIndex) throws SQLException
	{
		java.sql.Timestamp	temp	=	this.getTimestamp(columnIndex);
		
		if(temp != null)
		{
			GregorianCalendar	cal	=	new GregorianCalendar();
			cal.setTime(temp);
			return	cal;
		}
		
		return	null;
	}
	
	public GregorianCalendar getGregorianCalendar(String columnName) throws SQLException
	{
		java.sql.Timestamp	temp	=	this.getTimestamp(columnName);
		
		if(temp != null)
		{
			GregorianCalendar	cal	=	new GregorianCalendar();
			cal.setTime(temp);
			return	cal;
		}
		
		return	null;
	}

	public int getInt(int columnIndex) throws SQLException
	{
		return	rs.getInt(columnIndex);
	}

	public int getInt(String columnName) throws SQLException
	{
		return	rs.getInt(columnName);
	}

	public long getLong(int columnIndex) throws SQLException
	{
		return	rs.getLong(columnIndex);
	}

	public long getLong(String columnName) throws SQLException
	{
		return	rs.getLong(columnName);
	}

	public ResultSetMetaData getMetaData() throws SQLException
	{
		return	rs.getMetaData();
	}

	public Object getObject(int columnIndex) throws SQLException
	{
		return	rs.getObject(columnIndex);
	}

	public Object getObject(int i, Map<String,Class<?>> map) throws SQLException
	{
		return	rs.getObject(i, map);
	}

	public Object getObject(String columnName) throws SQLException
	{
		return	rs.getObject(columnName);
	}

	public Object getObject(String colName, Map<String,Class<?>> map) throws SQLException
	{
		return	rs.getObject(colName, map);
	}

	public Ref getRef(int i) throws SQLException
	{
		return	rs.getRef(i);
	}

	public Ref getRef(String colName) throws SQLException
	{
		return	rs.getRef(colName);
	}

	public int getRow() throws SQLException
	{
		return	rs.getRow();
	}

	public short getShort(int columnIndex) throws SQLException
	{
		return	rs.getShort(columnIndex);
	}

	public short getShort(String columnName) throws SQLException
	{
		return	rs.getShort(columnName);
	}

	public Statement getStatement() throws SQLException
	{
		return	rs.getStatement();
	}

	public String getString(int columnIndex) throws SQLException
	{
		return	rs.getString(columnIndex);
	}

	public String getString(String columnName) throws SQLException
	{
		return	rs.getString(columnName);
	}

	public Time getTime(int columnIndex) throws SQLException
	{
		return	rs.getTime(columnIndex);
	}

	public Time getTime(int columnIndex, Calendar cal) throws SQLException
	{
		return	rs.getTime(columnIndex, cal);
	}

	public Time getTime(String columnName) throws SQLException
	{
		return	rs.getTime(columnName);
	}

	public Time getTime(String columnName, Calendar cal) throws SQLException
	{
		return	rs.getTime(columnName, cal);
	}

	public Timestamp getTimestamp(int columnIndex) throws SQLException
	{
		return	rs.getTimestamp(columnIndex);
	}

	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException
	{
		return	rs.getTimestamp(columnIndex, cal);
	}

	public Timestamp getTimestamp(String columnName) throws SQLException
	{
		return	rs.getTimestamp(columnName);
	}

	public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException
	{
		return	rs.getTimestamp(columnName, cal);
	}

	public int getType() throws SQLException
	{
		return	rs.getType();
	}

	public URL getURL(int columnIndex) throws SQLException
	{
		return	rs.getURL(columnIndex);
	}

	public URL getURL(String columnName) throws SQLException
	{
		return	rs.getURL(columnName);
	}

	public SQLWarning getWarnings() throws SQLException
	{
		return	rs.getWarnings();
	}

	public void insertRow() throws SQLException
	{
		rs.insertRow();
	}

	public boolean isAfterLast() throws SQLException
	{
		return	rs.isAfterLast();
	}

	public boolean isBeforeFirst() throws SQLException
	{
		return	rs.isBeforeFirst();
	}

	public boolean isFirst() throws SQLException
	{
		return	rs.isFirst();
	}

	public boolean isLast() throws SQLException
	{
		return	rs.isLast();
	}

	public boolean last() throws SQLException
	{
		return	rs.last();
	}

	public void moveToCurrentRow() throws SQLException
	{
		rs.moveToCurrentRow();
	}

	public void moveToInsertRow() throws SQLException
	{
		rs.moveToInsertRow();
	}

	public boolean next() throws SQLException
	{
		return	rs.next();
	}

	public boolean previous() throws SQLException
	{
		return	rs.previous();
	}

	public void refreshRow() throws SQLException
	{
		rs.refreshRow();
	}

	public boolean relative(int rows) throws SQLException
	{
		return	rs.relative(rows);
	}

	public boolean rowDeleted() throws SQLException
	{
		return	rs.rowDeleted();
	}

	public boolean rowInserted() throws SQLException
	{
		return	rs.rowInserted();
	}

	public boolean rowUpdated() throws SQLException
	{
		return	rs.rowUpdated();
	}

	public void setFetchDirection(int direction) throws SQLException
	{
		rs.setFetchDirection(direction);
	}

	public void setFetchSize(int rows) throws SQLException
	{
		rs.setFetchSize(rows);
	}

	public void updateArray(int columnIndex, Array x) throws SQLException
	{
		rs.updateArray(columnIndex, x);
	}

	public void updateArray(String columnName, Array x) throws SQLException
	{
		rs.updateArray(columnName, x);
	}

	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException
	{
		rs.updateAsciiStream(columnIndex, x, length);
	}

	public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException
	{
		rs.updateAsciiStream(columnName, x, length);
	}

	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException
	{
		rs.updateBigDecimal(columnIndex, x);
	}

	public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException
	{
		rs.updateBigDecimal(columnName, x);
	}

	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException
	{
		rs.updateBinaryStream(columnIndex, x, length);
	}

	public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException
	{
		rs.updateBinaryStream(columnName, x, length);
	}

	public void updateBlob(int columnIndex, Blob x) throws SQLException
	{
		rs.updateBlob(columnIndex, x);
	}

	public void updateBlob(String columnName, Blob x) throws SQLException
	{
		rs.updateBlob(columnName, x);
	}

	public void updateBoolean(int columnIndex, boolean x) throws SQLException
	{
		rs.updateBoolean(columnIndex, x);
	}

	public void updateBoolean(String columnName, boolean x) throws SQLException
	{
		rs.updateBoolean(columnName, x);
	}

	public void updateByte(int columnIndex, byte x) throws SQLException
	{
		rs.updateByte(columnIndex, x);
	}

	public void updateByte(String columnName, byte x) throws SQLException
	{
		rs.updateByte(columnName, x);
	}

	public void updateBytes(int columnIndex, byte[] x) throws SQLException
	{
		rs.updateBytes(columnIndex, x);
	}

	public void updateBytes(String columnName, byte[] x) throws SQLException
	{
		rs.updateBytes(columnName, x);
	}

	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException
	{
		rs.updateCharacterStream(columnIndex, x, length);
	}

	public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException
	{
		rs.updateCharacterStream(columnName, reader, length);
	}

	public void updateClob(int columnIndex, Clob x) throws SQLException
	{
		rs.updateClob(columnIndex, x);
	}

	public void updateClob(String columnName, Clob x) throws SQLException
	{
		rs.updateClob(columnName, x);
	}

	public void updateDate(int columnIndex, java.sql.Date x) throws SQLException
	{
		rs.updateDate(columnIndex, x);
	}

	public void updateDate(String columnName, java.sql.Date x) throws SQLException
	{
		rs.updateDate(columnName, x);
	}

	public void updateDouble(int columnIndex, double x) throws SQLException
	{
		rs.updateDouble(columnIndex, x);
	}

	public void updateDouble(String columnName, double x) throws SQLException
	{
		rs.updateDouble(columnName, x);
	}

	public void updateFloat(int columnIndex, float x) throws SQLException
	{
		rs.updateFloat(columnIndex, x);
	}

	public void updateFloat(String columnName, float x) throws SQLException
	{
		rs.updateFloat(columnName, x);
	}

	public void updateInt(int columnIndex, int x) throws SQLException
	{
		rs.updateInt(columnIndex, x);
	}

	public void updateInt(String columnName, int x) throws SQLException
	{
		rs.updateInt(columnName, x);
	}

	public void updateLong(int columnIndex, long x) throws SQLException
	{
		rs.updateLong(columnIndex, x);
	}

	public void updateLong(String columnName, long x) throws SQLException
	{
		rs.updateLong(columnName, x);
	}

	public void updateNull(int columnIndex) throws SQLException
	{
		rs.updateNull(columnIndex);
	}

	public void updateNull(String columnName) throws SQLException
	{
		rs.updateNull(columnName);
	}

	public void updateObject(int columnIndex, Object x) throws SQLException
	{
		rs.updateObject(columnIndex, x);
	}

	public void updateObject(int columnIndex, Object x, int scale) throws SQLException
	{
		rs.updateObject(columnIndex, x);
	}

	public void updateObject(String columnName, Object x) throws SQLException
	{
		rs.updateObject(columnName, x);
	}

	public void updateObject(String columnName, Object x, int scale) throws SQLException
	{
		rs.updateObject(columnName, x);
	}

	public void updateRef(int columnIndex, Ref x) throws SQLException
	{
		rs.updateRef(columnIndex, x);
	}

	public void updateRef(String columnName, Ref x) throws SQLException
	{
		rs.updateRef(columnName, x);
	}

	public void updateRow() throws SQLException
	{
		rs.updateRow();
	}

	public void updateShort(int columnIndex, short x) throws SQLException
	{
		rs.updateShort(columnIndex, x);
	}

	public void updateShort(String columnName, short x) throws SQLException
	{
		rs.updateShort(columnName, x);
	}

	public void updateString(int columnIndex, String x) throws SQLException
	{
		rs.updateString(columnIndex, x);
	}

	public void updateString(String columnName, String x) throws SQLException
	{
		rs.updateString(columnName, x);
	}

	public void updateTime(int columnIndex, Time x) throws SQLException
	{
		rs.updateTime(columnIndex, x);
	}

	public void updateTime(String columnName, Time x) throws SQLException
	{
		rs.updateTime(columnName, x);
	}

	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException
	{
		rs.updateTimestamp(columnIndex, x);
	}

	public void updateTimestamp(String columnName, Timestamp x) throws SQLException
	{
		rs.updateTimestamp(columnName, x);
	}

	public boolean wasNull() throws SQLException
	{
		return	rs.wasNull();
	}
}
