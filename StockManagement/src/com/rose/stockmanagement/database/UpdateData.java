package com.rose.stockmanagement.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.rose.stockmanagement.inter.OnGetDataCompleted;
import com.rose.stockmanagement.inter.OnUpdateDataCompleted;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class UpdateData extends AsyncTask<String, String, Boolean> {

	private Context m_Context;
	private ProgressDialog m_ProgressDlg;
	private Object m_objParam;
	
	private OnUpdateDataCompleted listener;
	
	public UpdateData(OnUpdateDataCompleted listener, Object objParam, Context context) {
		
		this.listener = listener;
		m_Context = context;		
		m_objParam = objParam;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		Connection DBConnection = null;
		Boolean result = false;
				
		try{
			Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
			String connectionUrl = "jdbc:jtds:sqlserver://" + DBConstant._DBServerIP + ";databaseName=" + DBConstant._DBName + ";";
			DBConnection = DriverManager.getConnection(connectionUrl, DBConstant._DBAccount, DBConstant._DBPassword);			
			Statement state = DBConnection.createStatement();

			String query = params[0];			
			state.setQueryTimeout(180);
			if(state.executeUpdate(query) >= 0) result = true;
		}
		catch(Exception e){
			e.printStackTrace();
			return result;
		}

		// 수행이 끝나고 리턴하는 값은 다음에 수행될 onProgressUpdate 의 파라미터가 된다
		return result;
	}
	
	@Override
	protected void onPreExecute() {
		m_ProgressDlg = new ProgressDialog(m_Context);
		m_ProgressDlg.setMessage("잠시만 기다려주세요...");
		m_ProgressDlg.show();
		super.onPreExecute();
	}
	
	protected void onPostExecute(Boolean result) {
		m_ProgressDlg.dismiss();
		this.listener.onUpdatedataCompleted(result, m_objParam, m_Context);
	}
}
