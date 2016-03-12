package com.rose.stockmanagement.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.AsyncTask;

public class GetDataSync{
	
	public List GetData(String strQuery)
	{
		ResultSet result = null;
		
		Connection DBConnection = null;
		
		try{
			Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
			String connectionUrl = "jdbc:jtds:sqlserver://" + DBConstant._DBServerIP + ";databaseName=" + DBConstant._DBName + ";";
			DBConnection = DriverManager.getConnection(connectionUrl, DBConstant._DBAccount, DBConstant._DBPassword);			
			Statement state = DBConnection.createStatement();

			String query = strQuery;			
			state.setQueryTimeout(180);
			result = state.executeQuery(query);						
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return ConvertResultSetToList(result);
	}
	
	public List<HashMap<String, Object>> ConvertResultSetToList(ResultSet resultSet)
	{
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		
	    try {
	    	if(resultSet == null)
	    		return list;
	    	
	    	ResultSetMetaData md = resultSet.getMetaData();
		    int columns = md.getColumnCount();
		    
			while (resultSet.next()) {
			    HashMap<String,Object> row = new HashMap<String, Object>(columns);
			    for(int i=1; i<=columns; ++i) {
			        row.put(md.getColumnName(i),resultSet.getObject(i));
			    }
			    list.add(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    return list;
	}
//	
//	public boolean UpdateData(String strQuery)
//	{
//		Connection DBConnection = null;
//		
//		try{
//			Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
//			String connectionUrl = "jdbc:jtds:sqlserver://" + DBConstant._DBServerIP + ";databaseName=" + DBConstant._DBName + ";";
//			DBConnection = DriverManager.getConnection(connectionUrl, DBConstant._DBAccount, DBConstant._DBPassword);			
//			Statement state = DBConnection.createStatement();
//
//			String query = strQuery;			
//			state.setQueryTimeout(180);
//			int nResult = state.executeUpdate(query);
//			
//			if(nResult > 0)
//				return true;
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		return false;
//	}

}
