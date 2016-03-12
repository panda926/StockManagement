package com.rose.stockmanagement;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rose.stockmanagement.updatemgr.*;
import com.rose.stockmanagement.inter.*;
import com.rose.stockmanagement.database.*;


public class Login extends Activity implements OnTouchListener, OnGetDataCompleted{
	
	private InputMethodManager m_InputMethodManager;
	private LinearLayout m_Container;
	
	public static Login Instance = null;
	public static DBAdapter dbHelper;
	
	private EditText etID;
	private EditText etPassword;
	
	private ProgressDialog prgDlg;
	private Context context = this;
	
	private int nPage = 0;
    private int nTotalPage = 1;
    
    private com.rose.stockmanagement.database.GetDataSync getProductItem;
    
    private UpdateMgr updateMgr = null;
    	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		dbHelper = new DBAdapter(this); 
		dbHelper.open(); 
		
		setKeyboard();
		initUI();
		enableUI();
		
		Instance = this;
		updateMgr = new UpdateMgr(this);
        updateMgr.init();
	}
	
	private void setKeyboard(){
    	m_Container = (LinearLayout)findViewById(R.id.container);
    	m_InputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    	m_Container.setOnTouchListener(this);
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(v.equals(m_Container)){
			//화면의 빈령역을 터치하면 떠있던 키보드가 사라지ㄷ록 설정한다.
			m_InputMethodManager.hideSoftInputFromWindow(m_Container.getWindowToken(), 0);
		}
		return true;
	}
	
	private void initUI(){
    	
		if(dbHelper.GetLoginCount() > 0)
		{
	    	SharedPreferences sharedPrefer = getSharedPreferences(getResources().getString(R.string.information_string), Context.MODE_PRIVATE);
	       	
	    	String id = sharedPrefer.getString(getResources().getString(R.string.account_id), "");	       	  	       
	       	
	    	etID = (EditText)findViewById(R.id.account);			etID.setText(id);
	    	etPassword = (EditText)findViewById(R.id.pass);	    	    		    	
		}
    }
	
	private void enableUI(){				
		
    	etID = (EditText)findViewById(R.id.account); etID.setEnabled(true);
    	etPassword = (EditText)findViewById(R.id.pass); etPassword.setEnabled(true);
    	Button btLogin = (Button)findViewById(R.id.btn_login); btLogin.setEnabled(true);
    	btLogin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				OnLoginClick(v);
			}
    	});
    	
    	Button btnExit = (Button)findViewById(R.id.btn_login_exit); 
    	btnExit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(1);
			}
    	});
    }
	
	// Login버튼을 클릭할때 
	@SuppressLint("NewApi") 
	public void OnLoginClick(View view){
		
		CheckBox checkOffLine = (CheckBox)findViewById(R.id.chk_offline);
				
		String UserId = etID.getText().toString();
		String UserPassword = etPassword.getText().toString();
		
		if(UserId.isEmpty()){
			String strID = getString(R.string.msg_account);
			ShowMsg(strID);
			return;
		}
		if(UserPassword.isEmpty()){
			String strPassword = getString(R.string.msg_password);
			ShowMsg(strPassword);
			return;
		}	
		
		// 오프라인
		if(checkOffLine.isChecked())
		{
			if(dbHelper.GetLoginCount() == 0)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
			    builder
			    .setMessage("최초 온라인 로그인 정보가 없습니다. 온라인 로그인 후 사용해주세요.")
			    .setIcon(android.R.drawable.ic_dialog_alert)
			    .setNegativeButton("확인", new DialogInterface.OnClickListener() 
			    {
			        public void onClick(DialogInterface dialog, int which) 
			        {   
			        	dialog.dismiss();     
			        }
			    });        
			    
				AlertDialog alert = builder.create();
				alert.show();
				
				return; 
			}
			else
			{
				Cursor curUserInfo = dbHelper.GetUserInfo();
				if(dbHelper.LoginProcess(UserId, UserPassword))
				{
					if(curUserInfo != null)
					{
						do{
							if(etID.getText().toString().equals(curUserInfo.getString(curUserInfo.getColumnIndex("id"))))
							{
								Common.IDX_LOGIN = curUserInfo.getString(curUserInfo.getColumnIndex("id"));
								Common.IDX_NAME = curUserInfo.getString(curUserInfo.getColumnIndex("name"));
								
								SharedPreferences sharedPrefer = getSharedPreferences(getResources().getString(R.string.information_string), Context.MODE_PRIVATE);
						    	SharedPreferences.Editor sharedEditor = sharedPrefer.edit();
						    	sharedEditor.putString(getResources().getString(R.string.account_id), etID.getText().toString());
						    	sharedEditor.commit();
						    	
						    	ShowStockInventoryActivity();
						    	break;
							}
						}while(curUserInfo.moveToNext());
					}
				}
			}
		}
		// 온라인
		else
		{
			String strQuery = "SELECT name, GETDATE() dbdatetime, id, GETDATE() sysDateTime from Staff WHERE ";
			strQuery += String.format("erpid = '%s'", UserId);
			strQuery += String.format(" and passwd = '%s'", UserPassword);
			strQuery += " and useflag = 'Y' and retirementdate = '1900-01-01'";
			
			com.rose.stockmanagement.database.GetData._strMessage = "로그인중입니다...";
			new com.rose.stockmanagement.database.GetData(this, "Login", this).execute(strQuery);						
		}
	}
	
	private void ShowMsg(String Msg){
		Toast.makeText(this, Msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onGetDataCompleted(final List listResult, String strQueryType, Context context) {
		// TODO Auto-generated method stub
		if(this == context)
		{			
			
			if(strQueryType.equals("Login")){
				
				if(listResult.isEmpty())
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
				    builder
				    .setMessage("아이디나 패스워드가 일치하지 않습니다.")
				    .setIcon(android.R.drawable.ic_dialog_alert)
				    .setNegativeButton("확인", new DialogInterface.OnClickListener() 
				    {
				        public void onClick(DialogInterface dialog, int which) 
				        {   
				    		etID.setText("");
				    		etPassword.setText("");
				        	dialog.dismiss();     
				        }
				    });        
				    
					AlertDialog alert = builder.create();
					        alert.show();
				}
				else
				{
					dbHelper.InsertUserInfo(etID.getText().toString(), etPassword.getText().toString(), listResult);
					if(listResult.size() > 0)
					{
						Object obj = listResult.get(0);
						HashMap<String, Object> hash = (HashMap)obj;
						
						Common.serverTime = hash.get("sysDateTime").toString();
						Common.IDX_LOGIN = hash.get("id").toString();
						Common.IDX_NAME = hash.get("name").toString();
						
						SharedPreferences sharedPrefer = getSharedPreferences(getResources().getString(R.string.information_string), Context.MODE_PRIVATE);
				    	SharedPreferences.Editor sharedEditor = sharedPrefer.edit();
				    	sharedEditor.putString(getResources().getString(R.string.latestlogin), Common.serverTime);
				    	sharedEditor.putString(getResources().getString(R.string.account_id), etID.getText().toString());
				    	sharedEditor.commit();
				    	
				    	Cursor curVersion = dbHelper.GetVersion();
				    	if(curVersion.getCount() > 0)
				    	{
				    		StartDownloadSequence_CATEGORY();
				    		//StartDownloadSequence_ITEM();
				    	}
					}
				}
			}
			
			if(strQueryType.equals("GetCategory")){
				if(!listResult.isEmpty()){
					
					prgDlg = new ProgressDialog(this);
					prgDlg.setMessage("Category정보 보관중...");
					prgDlg.setCanceledOnTouchOutside(false);
					prgDlg.show();
					
					Thread thread = new Thread(new Runnable()
					{

						@Override
						public void run() {
							// TODO Auto-generated method stub
							dbHelper.InsertCategory(listResult);
							Message msg = Message.obtain();
							msg.obj = "CategorySave";
							handler.sendMessage(msg);
						}
						
					});
					
					thread.start();
										
				}
				else
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
				    builder
				    .setMessage("Category정보 불러오는중 오류가 발생하였습니다.")
				    .setIcon(android.R.drawable.ic_dialog_alert)
				    .setNegativeButton("확인", new DialogInterface.OnClickListener() 
				    {
				        public void onClick(DialogInterface dialog, int which) 
				        {   
				    		etID.setText("");
				    		etPassword.setText("");
				        	dialog.dismiss();     
				        } 
				    });        
				    
					AlertDialog alert = builder.create();
					        alert.show();
				}
			}
			
			if(strQueryType.equals("GetCompany")){
				if(!listResult.isEmpty()){
					
					prgDlg = new ProgressDialog(this);
					prgDlg.setMessage("Company정보 보관중...");
					prgDlg.setCanceledOnTouchOutside(false);
					prgDlg.show();
					
					Thread thread = new Thread(new Runnable()
					{

						@Override
						public void run() {
							// TODO Auto-generated method stub
							dbHelper.InsertCompany(listResult);
							Message msg = Message.obtain();
							msg.obj = "CompanySave";
							handler.sendMessage(msg);
						}
						
					});
					
					thread.start();
					
				}
				else
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
				    builder
				    .setMessage("Company정보 불러오는중 오류가 발생하였습니다.")
				    .setIcon(android.R.drawable.ic_dialog_alert)
				    .setNegativeButton("확인", new DialogInterface.OnClickListener() 
				    {
				        public void onClick(DialogInterface dialog, int which) 
				        {   
				    		etID.setText("");
				    		etPassword.setText("");
				        	dialog.dismiss();     
				        }
				    });        
				    
					AlertDialog alert = builder.create();
					        alert.show();
				}
			}
			
			if(strQueryType.equals("GetProductItem")){
				
				if(!listResult.isEmpty()){
					
					prgDlg = new ProgressDialog(this);
					prgDlg.setMessage("상품정보 보관중...");
					prgDlg.setCanceledOnTouchOutside(false);
					prgDlg.show();
					
					Thread thread = new Thread(new Runnable()
					{

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(dbHelper.InsertItem(listResult))
							{
								SharedPreferences sharedPrefer = getSharedPreferences(getResources().getString(R.string.information_string), Context.MODE_PRIVATE);
						    	SharedPreferences.Editor sharedEditor = sharedPrefer.edit();
						    	sharedEditor.putString(getResources().getString(R.string.masterversion), Common.serverTime);						    	
						    	sharedEditor.commit();
						    	
								Message msg = Message.obtain();
								msg.obj = "ProductItemSave";
								handler.sendMessage(msg);
							}
						}
						
					});
					
					thread.start();
					
				}
				else
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
				    builder
				    .setMessage("상품정보 불러오는중 오류가 발생하였습니다.")
				    .setIcon(android.R.drawable.ic_dialog_alert)
				    .setNegativeButton("확인", new DialogInterface.OnClickListener() 
				    {
				        public void onClick(DialogInterface dialog, int which) 
				        {   
				    		etID.setText("");
				    		etPassword.setText("");
				        	dialog.dismiss();     
				        }
				    });        
				    
					AlertDialog alert = builder.create();
					        alert.show();
				}
			}
		}
	}
	
	private void StartDownloadSequence_CATEGORY()
	{
		String strQuery = "SELECT *, CAST(symbol as VARCHAR(255)) as realsymbol FROM Category WHERE id NOT LIKE 'PM%'";
		com.rose.stockmanagement.database.GetData._strMessage = "Category정보 불러오는중...";
		new com.rose.stockmanagement.database.GetData(this, "GetCategory", this).execute(strQuery);
	}
	
	private void StartDownloadSequence_COMPANY()
	{
		String strQuery = "SELECT * FROM CompanyView as Company";
		com.rose.stockmanagement.database.GetData._strMessage = "Company정보 불러오는중...";
		new com.rose.stockmanagement.database.GetData(this, "GetCompany", this).execute(strQuery);
	}
	
	public void StartDownloadSequence_ITEM()
	{
		getProductItem = new com.rose.stockmanagement.database.GetDataSync();
		
		prgDlg = new ProgressDialog(this);
		prgDlg.setCancelable(false);
		prgDlg.setCanceledOnTouchOutside(false);
		prgDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		prgDlg.setMax(0);
		prgDlg.setProgress(0);
		prgDlg.setMessage("상품정보 불러오는중...");
		prgDlg.show();
		
		SharedPreferences sharedPrefer = getSharedPreferences(getResources().getString(R.string.information_string), Context.MODE_PRIVATE);
		
		String strVersion = sharedPrefer.getString(getResources().getString(R.string.masterversion), "");		
		String strVersionTo = Common.serverTime;
		
		if(!strVersion.equals(strVersionTo))
		{
			Thread thGetProductItem = new Thread(new Runnable(){
	
				@Override
				public void run() {
					// TODO Auto-generated method stub
					GetProductItem();
				}
				
			});
					
			
			thGetProductItem.start();
		}
		else
		{
			CloseProgressDialog();
		}
	}
		
	private void GetProductItem()
	{
		SharedPreferences sharedPrefer = getSharedPreferences(getResources().getString(R.string.information_string), Context.MODE_PRIVATE);
		
		String strVersion = sharedPrefer.getString(getResources().getString(R.string.masterversion), "");		
		String strVersionTo = Common.serverTime;
		
		if(strVersion.isEmpty())
			strVersion = "2012-04-21 00:00:00";
		
		if(nPage + 1 > nTotalPage)
		{
			CloseProgressDialog();
			return;
		}
		
		String strQuery = String.format("WITH Props AS (SELECT Product.*, ROW_NUMBER() OVER (ORDER BY productcode) AS RowNumber FROM ProductView as Product WHERE ");		
		strQuery += String.format("updatedate > '%s' and updatedate <= '%s' and useflag = 'Y')", strVersion, strVersionTo);
		strQuery += String.format("	SELECT *, (SELECT MAX(RowNumber) FROM Props) as MaxRow FROM Props WHERE RowNumber BETWEEN %d AND %d", nPage * Common.nRowCountPerPage + 1, (nPage + 1) * Common.nRowCountPerPage);
		
		List listResult = null;
		listResult = getProductItem.GetData(strQuery);
		
		if(listResult == null)
		{
			CloseProgressDialog();
			return;
		}
		
		nPage++;
		
		if(!listResult.isEmpty()){
			
			if(dbHelper.InsertItem(listResult))
			{				
				Object obj = listResult.get(0);
				HashMap<String, Object> hash = (HashMap)obj;
				
				int nMaxRow = Integer.parseInt(hash.get("MaxRow").toString());
				if (nMaxRow > Common.nRowCountPerPage)
	            {
	                if (nMaxRow % Common.nRowCountPerPage > 0)
	                    this.nTotalPage = nMaxRow / Common.nRowCountPerPage + 1;
	                else
	                    this.nTotalPage = nMaxRow / Common.nRowCountPerPage;
	                
	                this.runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							prgDlg.setMax(nTotalPage);
							prgDlg.setProgress(nPage);
						}
						
					});

	                GetProductItem();
	            }
	            else
	            	CloseProgressDialog();
			}			
		}	
		else
			CloseProgressDialog();
	}
	
	private void CloseProgressDialog()
	{
		prgDlg.dismiss();
		
		SharedPreferences sharedPrefer = getSharedPreferences(getResources().getString(R.string.information_string), Context.MODE_PRIVATE);
    	SharedPreferences.Editor sharedEditor = sharedPrefer.edit();
    	sharedEditor.putString(getResources().getString(R.string.masterversion), Common.serverTime);						    	
    	sharedEditor.commit();
    	
    	ShowStockInventoryActivity();
	}
	
	private void ShowStockInventoryActivity()
	{
		Intent intent = new Intent(context, StockActivity.class);
		startActivity(intent);
			
//		Instance = null;
//		System.gc();
//		finish();
	}
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) 
		{
			String strMessage = msg.obj.toString();
			if(prgDlg.isShowing())
				prgDlg.dismiss();
			
			switch(strMessage)
			{
				case "CategorySave":
				{		
					StartDownloadSequence_COMPANY();
					break;
				}
				
				case "CompanySave":
				{
					StartDownloadSequence_ITEM();
					break;
				}
			}
		}
	};
}
