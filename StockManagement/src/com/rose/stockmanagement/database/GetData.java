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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.rose.stockmanagement.database.*;
import com.rose.stockmanagement.inter.*;

public class GetData extends AsyncTask<String, String, List> {

	private OnGetDataCompleted listener;

	private Context m_Context;
	private ProgressDialog m_ProgressDlg;
	
	private String m_strQueryType;
	
	public static String _strMessage = "잠시만 기다려주세요...";
	
	public GetData(OnGetDataCompleted listener, String strQueryType, Context context) {
		this.listener = listener;
		m_Context = context;
		m_strQueryType = strQueryType;
	}
	@Override
	protected List doInBackground(String... params) {
		// TODO Auto-generated method stub
		ResultSet resultSet = null;
		Connection DBConnection = null;
		
		try{
			Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
			String connectionUrl = "jdbc:jtds:sqlserver://" + DBConstant._DBServerIP + ";databaseName=" + DBConstant._DBName + ";";
			DBConnection = DriverManager.getConnection(connectionUrl, DBConstant._DBAccount, DBConstant._DBPassword);			
			Statement state = DBConnection.createStatement();

			String query = params[0];			
			state.setQueryTimeout(180);
			resultSet = state.executeQuery(query);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		// 수행이 끝나고 리턴하는 값은 다음에 수행될 onProgressUpdate 의 파라미터가 된다
		return ConvertResultSetToList(resultSet);
	}

	@Override
	protected void onPreExecute() {
		m_ProgressDlg = new ProgressDialog(m_Context);
		m_ProgressDlg.setMessage(_strMessage);
		//m_ProgressDlg.setCancelable(false);
		m_ProgressDlg.setCanceledOnTouchOutside(false);
		//m_ProgressDlg.setIndeterminate(false);  
		m_ProgressDlg.show();
		super.onPreExecute();
	}
	
	protected void onPostExecute(List result) {
		m_ProgressDlg.dismiss();		
		this.listener.onGetDataCompleted(result, m_strQueryType, this.m_Context);
	}
	
	public List<HashMap<String, Object>> ConvertResultSetToList(ResultSet resultSet)
	{
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		
	    try {
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
}
