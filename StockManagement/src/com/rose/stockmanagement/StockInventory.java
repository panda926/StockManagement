package com.rose.stockmanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.rose.stockmanagement.inter.OnGetDataCompleted;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

public class StockInventory extends Activity implements OnTouchListener {

	final Context context = this;
	
	private InputMethodManager m_InputMethodManager;
	private LinearLayout m_Container;
	private Spinner itemTypeSpinnerObj;
	private EditText _editMemo;
	
	private String SelectID = "";
	private List _setOfStoreList = null;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_inventory);
		
		InitControlsEvent();
		GetStoreNameList("");
		SetKeyboard();
		
		_editMemo = (EditText) this.findViewById(R.id.edit_memo);
		if(Common.OffLineFlag.equals(true))
		{
			_editMemo.setText("오프라인 작업");
		}
		else
			_editMemo.setText("");
	}
	
	private void GetStoreNameList(String strID)
	{
		List listResult = Login.dbHelper.ConvertResultSetToList(Login.dbHelper.GetWarehouseList(strID));
		_setOfStoreList = listResult;
		this.InitComboBox(listResult);
	}
	
	// 콤보박스 매장/창고
	private void InitComboBox(List setStoreInfo)
	{
		this._setOfStoreList = setStoreInfo;
		
		itemTypeSpinnerObj = (Spinner) this
				.findViewById(R.id.cbStore);
		
		List<String> listStore = new ArrayList<String>();
				
		for(Object h:setStoreInfo)
		{
			HashMap hash = (HashMap)h;
			listStore.add(hash.get("NAME").toString());
		}
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				context, android.R.layout.simple_spinner_item,
				listStore);
		
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		itemTypeSpinnerObj.setAdapter(dataAdapter);
	}
	
	private void SetKeyboard(){
		
		//this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
    	m_Container = (LinearLayout)findViewById(R.id.container_stockinventory);
    	m_InputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    	m_Container.setOnTouchListener(this);
    }
	
	private void InitControlsEvent()
	{
		Button btnGetStock = (Button)findViewById(R.id.btn_list);
		
		// 재고조사에서 목록버튼을 클릭하였을때...
		btnGetStock.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				Intent intent = new Intent(context, StockInventoryList.class);
				
				Bundle b = new Bundle();
				b.putString("selectID", SelectID);
				intent.putExtras(b);
				
				startActivity(intent);
				
			}
    	});
		
		Button btnOK = (Button)findViewById(R.id.btn_stock_inventory_ok);
		
		// 재고조사에서 확인버튼을 클릭하였을떄...
		btnOK.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				String strSelectedText = (String)itemTypeSpinnerObj.getSelectedItem();
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
				        	Intent intent = new Intent(context, StockInventoryDetail.class);
				        	
				        	Bundle b = new Bundle();
							b.putString("SelectID", SelectID);
							b.putString("SelectWarehouseID", itemTypeSpinnerObj.getSelectedItem().toString());
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
		
		Button btnMemeo = (Button)findViewById(R.id.btn_memo);
		
		// 재고조사에서 메모버튼을 클릭하였을때...
		btnMemeo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				EditText editView = (EditText)findViewById(R.id.edit_memo);
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
		
		Button btnClose = (Button)findViewById(R.id.btn_stock_inventory_exit);
		
		// 재고조사에서 메모버튼을 클릭하였을때...
		btnClose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				finish();
			}
    	});
		
		// 정상radio버튼 클릭사건
		RadioButton radioTrue = (RadioButton)findViewById(R.id.radioTrue);
		radioTrue.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GetStoreNameList("");
			}
			
		});
		
		// 불량radio버튼 클릭사건
		RadioButton radioFalse = (RadioButton)findViewById(R.id.radioFalse);
		radioFalse.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GetStoreNameList("bad");
			}
			
		});
	}
	
	public Boolean NewStockProc()
	{
		Boolean bRet = false;
		
		String strID = (String) DateFormat.format("yyMMddHHmmss", new Date());
		String strWarehouseID = GetStoreID();
		String strTotalProductQty = "0";
		String strTotalQty = "0";
		String strMemo = _editMemo.getText().toString();
		String strWorkDate = (String) DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date());
		String strInsertStaff = Common.IDX_LOGIN;
				
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		HashMap<String,Object> row = new HashMap<String, Object>(1);
		row.put("ID", strID);
		row.put("WAREHOUSEID", strWarehouseID);
		row.put("TOTALPRODUCTQTY", strTotalProductQty);
		row.put("TOTALQTY", strTotalQty);
		row.put("MEMO", strMemo);
		row.put("WORKDATE", strWorkDate);
		row.put("INSERTSTAFF", strInsertStaff);
		
		list.add(row);
		
		SelectID = strID;
		
		if(Login.dbHelper.InsertStockChecking(list))		
			bRet = true;
		
		return bRet;
	}
	
	private String GetStoreID()
	{
		String strStoreID = "";
				
		for(Object h : _setOfStoreList)
		{
			HashMap hash = (HashMap)h;
			if(hash.get("NAME").toString() == (String)itemTypeSpinnerObj.getSelectedItem())
			{
				return hash.get("ID").toString();
			}
		}
		
		return strStoreID;
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

}
