package com.rose.stockmanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class StockReport extends Activity {

	final Context context = this;
	
	private Spinner cb_Storage;
	private Spinner cb_Category1;
	private Spinner cb_Category2;
	private Spinner cb_Category3;
	private Spinner cb_Team;
	
	private EditText tb_Barcode;
	
	static final int CODE_REQUEST = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_report);
		
		SetDefault();
		InitControlEvent();
	}
	
	String _strBarcode = "";
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		char pressKey = (char)event.getUnicodeChar();
		
		if(pressKey == '\n'){
			ShowBarCodeSelect(_strBarcode);
			return false;
		}
		
		_strBarcode = _strBarcode + pressKey;
		
		
		
		return false;
		//return super.onKeyDown(keyCode, event);
	}
	
	private void ShowBarCodeSelect(String strBarCode)
	{
		List listProductInfo = Login.dbHelper.ConvertResultSetToList(Login.dbHelper.GetProductInfo(strBarCode));
		if(listProductInfo.size() > 1)
		{
			Intent intent = new Intent(this, StockInventoryProc.class);
        	
        	Bundle b = new Bundle();
        	b.putString("selCode", strBarCode);
        	
        	intent.putExtras(b);
        	
        	startActivityForResult(intent, CODE_REQUEST);
		}
		else if(listProductInfo.size() == 1)
		{
			EditText txtBarCode = (EditText)findViewById(R.id.edit_stockreport_barcode);
			HashMap<String, Object> h = (HashMap<String, Object>)listProductInfo.get(0);
			txtBarCode.setText(h.get("ID").toString());
		}
		else
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder
		    .setMessage("상품 정보가 없습니다.")
		    .setIcon(android.R.drawable.ic_dialog_alert)
		    .setNegativeButton("확인", null).show();  
		}
		
		_strBarcode = "";
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == CODE_REQUEST) 
	    {
	        // Make sure the request was successful
	        if (resultCode == Activity.RESULT_OK) {
	            
	        	String strCode = data.getStringExtra("SelectID");
	        	
	        	EditText txtBarCode = (EditText)findViewById(R.id.edit_stockreport_barcode);
	        	txtBarCode.setText(strCode);
	        }
	    }
	}
		
	private void InitControlEvent()
	{
		Button btnSearch = (Button)findViewById(R.id.btn_stockreport_search);
		
		// 재고이동에서 목록버튼을 클릭하였을떄...
		btnSearch.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
			 	String categoryid = "";
	            String storeid = "";
	            String teamid = "";
	            
	            if(cb_Storage.getSelectedItem().toString().length() > 0)
	            	storeid = Login.dbHelper.GetStoreOrWarehouseID(cb_Storage.getSelectedItem().toString());
	            
	            if(cb_Team.getSelectedItem().toString().length() > 0)
	            	teamid = cb_Team.getSelectedItem().toString().substring(0, 2);
	            
	            if (teamid.equals("LG")) teamid = "WH0201";
	            else if (teamid.equals("SK")) teamid = "WH0401";
	            else if (teamid.equals("HE")) teamid = "WH0301";
	            else if (teamid.equals("HA")) teamid = "WH0801";
	            
	            if(cb_Category3.getSelectedItem() != null && cb_Category3.getSelectedItem().toString() != "") categoryid = Login.dbHelper.GetCategoryCode(cb_Category3.getSelectedItem().toString());
	            else if(cb_Category2.getSelectedItem() != null && cb_Category2.getSelectedItem().toString() != "") categoryid = Login.dbHelper.GetCategoryCode(cb_Category2.getSelectedItem().toString());
	            else if(cb_Category1.getSelectedItem() != null && cb_Category1.getSelectedItem().toString() != "") categoryid = Login.dbHelper.GetCategoryCode(cb_Category1.getSelectedItem().toString());
		            
				Intent intent = new Intent(context, StockReportList.class);
				
				Bundle b = new Bundle();
				b.putString("categoryid", categoryid);
				b.putString("storeid", storeid);
				b.putString("teamid", teamid);
				b.putString("barcode", tb_Barcode.getText().toString());
				
				intent.putExtras(b);
				
				startActivity(intent);
				
			}
    	});
		
		
		Button btnFormat = (Button)findViewById(R.id.btn_stockreport_init);
		
		// 재고이동에서 목록버튼을 클릭하였을떄...
		btnFormat.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				cb_Storage.setSelection(0);
				cb_Category1.setSelection(0);
				cb_Category2.setSelection(0);
				cb_Category3.setSelection(0);
				cb_Team.setSelection(0);
				
				tb_Barcode.setText("");
			}
    	});
		
		Button btnClose = (Button)findViewById(R.id.btn_stockreport_close);
		
		// 재고이동에서 목록버튼을 클릭하였을떄...
		btnClose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				finish();
			}
    	});
	}
	
	private void SetDefault()
	{
		tb_Barcode = (EditText)findViewById(R.id.edit_stockreport_barcode);
		
		cb_Category1 = (Spinner)findViewById(R.id.cb_stockreport_category1);
    	cb_Category2 = (Spinner)findViewById(R.id.cb_stockreport_category2);
    	cb_Category3 = (Spinner)findViewById(R.id.cb_stockreport_category3);
    	
    	cb_Category1.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				cb_Category1_Changed();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});
    	
    	cb_Category2.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				cb_Category2_Changed();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});

    	
		EditText tb_Barcode = (EditText)findViewById(R.id.edit_stockreport_barcode);
		tb_Barcode.setText("");
		
		GetStoreComboBox();
        GetCategoryComboBox(cb_Category1, "L", "");
        GetTeamComboBox();
	}
	
	private void GetTeamComboBox()
    {
        try
        {
        	cb_Team = (Spinner)findViewById(R.id.cb_stockreport_team);
        	
        	List resultCategory = Login.dbHelper.ConvertResultSetToList(Login.dbHelper.GetTeamList());

        	List<String> listStore = new ArrayList<String>();
        	listStore.add("");
			
    		for(Object h:resultCategory)
    		{
    			HashMap hash = (HashMap)h;
    			listStore.add(hash.get("NAME").toString());
    		}
    		
    		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
    				context, android.R.layout.simple_spinner_item,
    				listStore);
    		
    		dataAdapter
    				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		
    		cb_Team.setAdapter(dataAdapter);
        }
        catch (Exception ex)
        {
        }

    }
	
	private void GetCategoryComboBox(Spinner cmb, String type, String code)
    {
        try
        {
            //중, 소분류는 상위 코드 정보가 있어야 함.
            if ((type == "M" || type == "S") && code.length() == 0)
                return;

            List resultCategory = Login.dbHelper.ConvertResultSetToList(Login.dbHelper.GetCategoryList(type, code));
            
            List<String> listStore = new ArrayList<String>();
            
            listStore.add("");
			
    		for(Object h:resultCategory)
    		{
    			HashMap hash = (HashMap)h;
    			listStore.add(hash.get("NAME").toString());
    		}
    		
    		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
    				context, android.R.layout.simple_spinner_item,
    				listStore);
    		
    		dataAdapter
    				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		
    		cmb.setAdapter(dataAdapter);
        }
        catch (Exception ex)
        {
        	
        }
    }
	
	private void cb_Category1_Changed()
	{
		GetCategoryComboBox(cb_Category2, "M", Login.dbHelper.GetCategoryCode(cb_Category1.getSelectedItem().toString()));
		cb_Category3.setAdapter(null);
	}
	
	private void cb_Category2_Changed()
	{
		GetCategoryComboBox(cb_Category3, "S", Login.dbHelper.GetCategoryCode(cb_Category2.getSelectedItem().toString()));
	}
	
	//콤보박스 매장/창고
    private void GetStoreComboBox()
    {
        try
        {
        	cb_Storage = (Spinner)findViewById(R.id.cb_stockreport_storage);
            
            //창고정보 가져오고
            List resultWarehouse = Login.dbHelper.ConvertResultSetToList(Login.dbHelper.GetWarehouseList(""));
            List resultStockCheck = Login.dbHelper.ConvertResultSetToList(Login.dbHelper.GetStoreList());
            
            
            List<String> listStore = new ArrayList<String>();
            listStore.add("");
			
    		for(Object h:resultWarehouse)
    		{
    			HashMap hash = (HashMap)h;
    			listStore.add(hash.get("NAME").toString());
    		}
    		
    		for(Object h:resultStockCheck)
    		{
    			HashMap hash = (HashMap)h;
    			listStore.add(hash.get("NAME").toString());
    		}
    		
    		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
    				context, android.R.layout.simple_spinner_item,
    				listStore);
    		
    		dataAdapter
    				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		
    		cb_Storage.setAdapter(dataAdapter);
        }
        catch (Exception ex)
        {
        }
    }
}
