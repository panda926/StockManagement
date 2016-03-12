package com.rose.stockmanagement.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import android.os.AsyncTask;

public class UpdateDataSync{

	public Boolean UpdateData(String strQuery) {
		Connection DBConnection = null;
		
		try{
			Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
			String connectionUrl = "jdbc:jtds:sqlserver://" + DBConstant._DBServerIP + ";databaseName=" + DBConstant._DBName + ";";
			DBConnection = DriverManager.getConnection(connectionUrl, DBConstant._DBAccount, DBConstant._DBPassword);			
			Statement state = DBConnection.createStatement();

			String query = strQuery;	
			state.setQueryTimeout(180);
			int nResult = state.executeUpdate(query);
			
			if(nResult > 0)
				return true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}

}
