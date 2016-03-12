package com.rose.stockmanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.rose.stockmanagement.database.UpdateData;
import com.rose.stockmanagement.inter.OnUpdateDataCompleted;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class StockInventoryReplaceNew extends Activity {

	final Context context = this;
	private Spinner spinerStore;
	
	private EditText _editMemo;
	private EditText _editToStore;
	
	private String SelectID = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_inventory_replace_new);
		
		_editMemo = (EditText) this.findViewById(R.id.edit_inventoryreplacenew_memo);
		_editToStore = (EditText) this.findViewById(R.id.edit_inventoryreplacenew_tostore);
		
		InitStoreComboBox();
		InitControlsEvent();
	}
	
	private void InitStoreComboBox()
	{
		spinerStore = (Spinner) this
				.findViewById(R.id.cb_inventoryreplacenew_fromstore);
		
		List<String> listStore = new ArrayList<String>();
		
		listStore.add("");
		listStore.add("본사창고");
		listStore.add("본사불량창고");
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				context, android.R.layout.simple_spinner_item,
				listStore);
		
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinerStore.setAdapter(dataAdapter);
	}
	
	private void InitControlsEvent()
	{
		Button btnGetReplaceList = (Button)findViewById(R.id.btn_inventoryreplacenew_list);
		
		// 재고이동에서 목록버튼을 클릭하였을떄...
		btnGetReplaceList.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				Intent intent = new Intent(context, StockInventoryReplaceList.class);
				startActivity(intent);
				
			}
    	});
		
		Button btnOK = (Button)findViewById(R.id.btn_inventoryreplacenew_ok);
		
		// 재고이동에서 확인버튼을 클릭하였을떄...
		btnOK.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				String strSelectedText = (String)spinerStore.getSelectedItem();
				if(strSelectedText == null || strSelectedText.length() == 0)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
				    builder
				    .setMessage("창고/매장을 선택하세요.")
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
				}
				else
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
				    builder
				    .setTitle("확인")
				    .setMessage("재고 조사 항목을 생성합니다.")
				    .setIcon(android.R.drawable.ic_dialog_alert)
				    .setCancelable(false)				    
				    .setPositiveButton("Yes", new DialogInterface.OnClickListener() 
				    {
				        public void onClick(DialogInterface dialog, int which) 
				        {       	
				        	NewStockProc();
				        	Intent intent = new Intent(context, StockInventoryReplaceDetail.class);
				        	
				        	Bundle b = new Bundle();
							b.putString("SelectID", SelectID);
							b.putString("SelectWarehouseID", spinerStore.getSelectedItem().toString());
							intent.putExtras(b);
							
							startActivity(intent);
				        }
				    });   
				    
				    builder.setNegativeButton("No", new DialogInterface.OnClickListener() 
				    {
				        public void onClick(DialogInterface dialog, int which) 
				        {   
				        	dialog.dismiss();     
				        }
				    });        
				    
					AlertDialog alert = builder.create();
					        alert.show();
				}
			}
    	});
		
		Button btnMemeo = (Button)findViewById(R.id.btn_replace_memo);
		
		// 재고이동에서 메모버튼을 클릭하였을때...
		btnMemeo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				EditText editView = (EditText)findViewById(R.id.edit_inventoryreplacenew_memo);
				if(editView.isEnabled())
				{
					editView.setEnabled(false);
				}
				else
				{
					editView.setEnabled(true);			
					editView.requestFocus();
				}
			}
    	});		
		
		Button btnExit = (Button)findViewById(R.id.btn_inventoryreplacenew_exit);
		
		// 재고이동에서 닫기버튼을 클릭하였을때...
		btnExit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				finish();
			}
    	});		
				
		spinerStore.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				if(spinerStore.getSelectedItem().toString().equals("본사창고"))
				{
					_editToStore.setText("본사불량창고");
				}
				else if(spinerStore.getSelectedItem().toString().equals("본사불량창고"))
				{
					_editToStore.setText("본사창고");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public Boolean NewStockProc()
	{
		Boolean bRet = false;
		
		String strID = (String) DateFormat.format("yyMMddHHmmss", new Date());
		String strFromWarehouseID = spinerStore.getSelectedItem().toString();
		String strToWarehouseID = _editToStore.getText().toString();
		String strTotalProductQty = "0";
		String strTotalQty = "0";
		String strMemo = _editMemo.getText().toString();
		String strWorkDate = (String) DateFormat.format("yyyy-MM-dd", new Date());
		String strInsertStaff = Common.IDX_LOGIN;
				
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		HashMap<String,Object> row = new HashMap<String, Object>(1);
		row.put("ID", strID);
		row.put("FROMWAREHOUSEID", strFromWarehouseID);
		row.put("TOWAREHOUSEID", strToWarehouseID);
		row.put("WORKDATE", strWorkDate);
		row.put("TOTALPRODUCTQTY", strTotalProductQty);
		row.put("TOTALQTY", strTotalQty);
		row.put("MEMO", strMemo);
		row.put("STAFFID", strInsertStaff);
		
		list.add(row);
		
		SelectID = strID;
		
		if(Login.dbHelper.InsertStockReplace(list))		
			bRet = true;
		
		return bRet;
	}
}
