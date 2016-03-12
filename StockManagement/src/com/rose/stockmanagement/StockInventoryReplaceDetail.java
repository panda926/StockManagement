package com.rose.stockmanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.codegineer.datagrid.DataGrid;
import com.codegineer.datatable.DataTable;
import com.codegineer.datatable.DataTable.DataRow;
import com.rose.stockmanagement.inter.OnGetDataCompleted;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;

public class StockInventoryReplaceDetail extends Activity{

	String stReturn_Barcode = "";
    String stReturn_GoodsName = "";
    String stReturn_Count = "";

    String SelectID = "";
    String SelectWarehouseID = "";
    
    private EditText editStore;
    private DataGrid _dg;
    
    static final int CODE_REQUEST = 1;
    static final int CODE_REQUEST_BARCODE = 2;
    
    private final Context context = this;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_inventory_replace_detail);
		
		Bundle bundle = this.getIntent().getExtras();
		
		SelectID = getIntent().getStringExtra("SelectID");
		SelectWarehouseID = getIntent().getStringExtra("SelectWarehouseID");
		
		editStore = (EditText)findViewById(R.id.edit_stockreplacedetail_store);
		
		InitControlsEvent();
		InitGridView();
		SetDafault();
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
		if(listProductInfo.size() > 0)
		{
			Intent intent = new Intent(this, BarcodeSelect.class);
        	
        	Bundle b = new Bundle();
        	b.putString("selCode", strBarCode);
        	
        	intent.putExtras(b);
        	
        	startActivityForResult(intent, CODE_REQUEST_BARCODE);
		}
//		else if(listProductInfo.size() == 1)
//		{
//			EditText txtBarCode = (EditText)findViewById(R.id.edit_stockreport_barcode);
//			HashMap<String, Object> h = (HashMap<String, Object>)listProductInfo.get(0);
//			txtBarCode.setTag(h.get("ID"));
//		}
		else
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder
		    .setMessage("항목 등록이 실패하였습니다.")
		    .setIcon(android.R.drawable.ic_dialog_alert)
		    .setNegativeButton("확인", null).show();  
		}
		
		_strBarcode = "";
	}
	
	private void InitControlsEvent()
	{
//		Button btnTest1 = (Button)findViewById(R.id.btn_replacedetail_test1);
//		
//		btnTest1.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {	
//				StockProc("HE121MKK8000");
//			}
//    	});
		
		Button btnDelete = (Button)findViewById(R.id.btn_stockreplacedetail_del);
		
		btnDelete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				if(_dg.GetDataGridAdapter().GetCheckedCount() > 0)
				{					        
					if(MessageBox.getYesNoWithExecutionStop("확인", "선택한 항목을 삭제하시겠습니까?", context))
					{
						deleteStockCheckingItem();
						LoadStockCheckingItem();
					}
				}
			}
    	});
		
		Button btnNext = (Button)findViewById(R.id.btn_stockreplacedetail_next);
		
		btnNext.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				Intent intent = new Intent(context, StockInventoryReplaceList.class);
				startActivity(intent);
				
				finish();
			}
    	});
		
		Button btnClose = (Button)findViewById(R.id.btn_stockreplacedetail_cancel);
		
		btnClose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				finish();
			}
    	});
	}
	
	private void deleteStockCheckingItem()
	{		
		if(!Login.dbHelper.deleteStockCheckingForReplaceItem(_dg, SelectID))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
		    builder
		    .setMessage("재고조사 항목 삭제가 실패하였습니다.")
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
		
		for(int i = 0; i < _dg.GetDataGridAdapter().getCount(); i++)
		{
			if(_dg.GetDataGridAdapter().getIsSelected().get(i))
			{						
				_dg.GetDataSource().remove(i);
				_dg.refresh();
			}
		}
	}
	
	int iExistIdx = -1;
	DataRow drItem;
	HashMap<String, Object> h;
	
	private void StockProc(String barcode)
	{
		//수량은 기본 1
        String qty = "1";
        String orgprice = "";
        String note = "";
        String convertBarcode = "";
        String SelectNowQty = "0";
        
        //리스트에 이미 있는지 검사
        
        
        int LogisticA = 0;
        int LogisticB = 0;
        int LogisticC = 0;
        
        List list = Login.dbHelper.ConvertResultSetToList(Login.dbHelper.GetProductInfo(barcode));
        if(list.size() == 0)
        {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder
		    .setMessage("상품정보가 없습니다.")
		    .setIcon(android.R.drawable.ic_dialog_alert)
		    .setNegativeButton("확인", null).show();  
		    
		    return;
        }
        
        h = (HashMap<String, Object>)list.get(0);
        
        for( int i = 0; i < _dg.GetDataGridAdapter().getCount(); i++)
        {
        	DataRow dr = _dg.GetDataSource().getRow(i);
        	
        	if(dr.get("col2").equals(h.get("ID").toString()))
        	{
        		iExistIdx = i;
        		drItem = dr;
        		SelectNowQty = dr.get("col3").toString();
        	}
        }
        
        LogisticA = Integer.parseInt(h.get("LOGISTICSUNITA").toString());
        LogisticB = Integer.parseInt(h.get("LOGISTICSUNITB").toString());
        LogisticC = Integer.parseInt(h.get("LOGISTICSUNITC").toString());

        //물류박스 바코드 혹은 자사코드인지 확인 시작. 박스단위가 아니라면 단품갯수로 처리.
        if (h.get("LOGISTICSCODEA1").toString().equals(barcode))
            qty = h.get("LOGISTICSUNITA").toString();
        else if (h.get("LOGISTICSCODEA2").toString().equals(barcode))
        	qty = h.get("LOGISTICSUNITA").toString();
        //물류박스B = 물류박스A X 물류박스B (물류박스B가 0이라면 A의 수량만 넘겨준다.)
        else if (h.get("LOGISTICSCODEB1").toString().equals(barcode))
        {
            if (h.get("LOGISTICSUNITB").toString().equals("0"))
            {            	
            	if(MessageBox.getYesNoWithExecutionStop("통보", "바코드 스캔 결과 물류박스B의 수량이 없습니다. \n물류박스A의 수량으로 입력하시겠습니까?", this))
            	{
            		qty = String.valueOf(LogisticA);
            	}
            	else
            		return;
            }
            else
                qty = String.valueOf(LogisticA * LogisticB);
        }
        else if (h.get("LOGISTICSCODEB2").toString().equals(barcode))
        {

            if (h.get("LOGISTICSUNITB").toString().equals("0"))
            {
            	if(MessageBox.getYesNoWithExecutionStop("통보", "바코드 스캔 결과 물류박스B의 수량이 없습니다. \n물류박스A의 수량으로 입력하시겠습니까?", this))
            	{
            		qty = String.valueOf(LogisticA);
            	}
            	else
            		return;
            }
            else
            	qty = String.valueOf(LogisticA * LogisticB);
        }
        //물류박스C = 물류박스A X 물류박스B X 물류박스C (물류박스B와C가 0이라면 A의 수량만.)
        else if (h.get("LOGISTICSCODEC1").toString().equals(barcode))
        {
            if (h.get("LOGISTICSUNITC").toString().equals("0"))
            {
                if (h.get("LOGISTICSUNITB").toString().equals("0"))
                {
                    {
                    	if(MessageBox.getYesNoWithExecutionStop("통보", "바코드 스캔 결과 물류박스B와C의 수량이 없습니다. \n물류박스A의 수량으로 입력하시겠습니까?", this))
                    	{
                    		qty = String.valueOf(LogisticA);
                    	}
                    	else
                    		return;
                    }
                }
                else
                {
                	if(MessageBox.getYesNoWithExecutionStop("통보", "바코드 스캔 결과 물류박스C의 수량이 없습니다. \n물류박스AxB의 수량으로 입력하시겠습니까?", this))
                	{
                		qty = String.valueOf(LogisticA * LogisticB);
                	}
                	else
                		return;
                }
            }
            else
            {
                qty = String.valueOf(LogisticA * LogisticB * LogisticC);
            }
        }
        else if (h.get("LOGISTICSCODEC2").toString().equals(barcode))
        {
            if (h.get("LOGISTICSUNITC").toString().equals("0"))
            {
                if (h.get("LOGISTICSUNITB").toString().equals("0"))
                {
                    {
                    	if(MessageBox.getYesNoWithExecutionStop("통보", "바코드 스캔 결과 물류박스B와C의 수량이 없습니다. \n물류박스A의 수량으로 입력하시겠습니까?", this))
                    	{
                    		qty = String.valueOf(LogisticA);
                    	}
                    	else
                    		return;
                    }
                }
                else
                {
                	if(MessageBox.getYesNoWithExecutionStop("통보", "바코드 스캔 결과 물류박스C의 수량이 없습니다. \n물류박스AxB의 수량으로 입력하시겠습니까", this))
                	{
                		qty = String.valueOf(LogisticA * LogisticB);
                	}
                	else
                		return;
                }
            }
            else
            {
                qty = String.valueOf(LogisticA * LogisticB * LogisticC);
            }
        }
        else
            qty = "1";
        
        if(list.size() > 0)
        {
        	Intent intent = new Intent(this, StockInventoryProc.class);
        	
        	Bundle b = new Bundle();
        	b.putString("barcode", barcode);
        	b.putString("qty", qty);
        	b.putString("SelectNowQty", SelectNowQty);
        	
        	intent.putExtras(b);
        	
        	startActivityForResult(intent, CODE_REQUEST);
        }
        else
        {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder
		    .setMessage("상품 정보가 없습니다.")
		    .setIcon(android.R.drawable.ic_dialog_alert)
		    .setNegativeButton("확인", null).show();  
        }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == CODE_REQUEST) 
	    {
	        // Make sure the request was successful
	        if (resultCode == Activity.RESULT_OK) {
	            
	        	String strQty = "";
	        	String strBarcode = "";
	        	
	        	strQty = data.getStringExtra("qty");
	        	strBarcode = data.getStringExtra("barcode");
	        	
	        	if (iExistIdx != -1)
	        	{
	        		String strValue = String.valueOf(Integer.parseInt(drItem.get("col3").toString()) + Integer.parseInt(strQty));
	        		drItem.set("col3", strValue);
	        		drItem.set("col2", strBarcode);
	        		
	        		if(Login.dbHelper.UpdateStockReplaceCheckingItem(drItem, SelectID))
	        		{
	        			_dg.GetDataSource().getRow(iExistIdx).set("col3", strValue);
	        			_dg.refresh();
	        		}
	        		
	        		iExistIdx = -1;
	        	}
	        	else
	        	{
	        		if (!InsertStockItem(strBarcode, strQty, h.get("NAME").toString()))
                    {
	        			AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    		    builder
		    		    .setMessage("상품 정보가 없습니다.")
		    		    .setIcon(android.R.drawable.ic_dialog_alert)
		    		    .setNegativeButton("확인", null).show();  
                    }
	        	}
	        }
	    }
	    else if (requestCode == CODE_REQUEST_BARCODE) 
	    {
	        // Make sure the request was successful
	        if (resultCode == Activity.RESULT_OK) {
	            
	        	String strCode = data.getStringExtra("SelectID");
	        	this.StockProc(strCode);
	        }
	    }
	}
	
	public Boolean InsertStockItem(String barcode, String qty, String productname)
	{
		Boolean bRet = false;
		
		try
        {
			String workdate = (String) DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date());
			EditText tbStore = (EditText)findViewById(R.id.edit_stockreplacedetail_store);
			
			List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
			HashMap<String,Object> row = new HashMap<String, Object>(1);
			row.put("ID", SelectID);
			row.put("WAREHOUSEID", tbStore.getText().toString());
			row.put("PRODUCTID", barcode);
			row.put("QTY", qty);
			row.put("WORKDATE", workdate);
			row.put("WORKERID", Common.IDX_LOGIN);
			row.put("PRODUCTNAME", productname);
			
			list.add(row);
			
			if(Login.dbHelper.InsertStockReplaceItem(list))
			{
				bRet = true;
				
				DataRow drRow = _dg.GetDataSource().newRow();
				
				drRow.set(0, "false");
	        	drRow.set(1, row.get("PRODUCTID").toString());
	        	drRow.set(2, row.get("QTY").toString());
	        	drRow.set(3, row.get("WORKDATE").toString());
	        	drRow.set(4, row.get("WORKERID").toString());        	        
	        	
	        	_dg.GetDataSource().add(drRow);
	        	_dg.refresh();
			}
			
        }
        catch (Exception ex)
        {
        }
		
		return bRet;
	}
	
	private void InitGridView()
	{
		_dg = (DataGrid)findViewById(R.id.datagrid_stockreplacedetail_detail);
		_dg.addColumnStyles(new DataGrid.ColumnStyle[]{
        		new DataGrid.ColumnStyle("", "co1", 60),
        		new DataGrid.ColumnStyle("자사코드", "col2", 330),
        		new DataGrid.ColumnStyle("수량", "col3", 330),
        		new DataGrid.ColumnStyle("작업일시", "col4", 330),
        		new DataGrid.ColumnStyle("작업자", "col5", 330)
        });
	}
	
	private void SetDafault()
    {

        stReturn_Barcode = "";
        stReturn_GoodsName = "";
        stReturn_Count = "";
       
        editStore.setText(SelectWarehouseID);
        LoadStockCheckingItem();
    }
	
	private void LoadStockCheckingItem()
	{
		InitDataGrid();
		GetStockChecking(SelectID);
	}
	
	private void GetStockChecking(String strID)
	{
		List listResult = Login.dbHelper.ConvertResultSetToList(Login.dbHelper.GetStockReplaceItem(SelectID));
		DataTable dtDataSource = new DataTable();
		dtDataSource.addAllColumns(new String[]{"co1", "col2","col3", "col4", "col5"});
        
		List resultStock = listResult;
		if(resultStock == null)
			return;
		
		for(HashMap<String, Object> h : (List<HashMap<String, Object>>)resultStock)
		{
	        DataTable.DataRow drRow;
	        
        	drRow = dtDataSource.newRow();
        	
        	drRow.set(0, "false");
        	drRow.set(1, h.get("PRODUCTID").toString());
        	drRow.set(2, h.get("QTY").toString());
        	drRow.set(3, h.get("WORKDATE").toString());
        	drRow.set(4, h.get("WORKERID").toString());        	        
        	
        	dtDataSource.add(drRow);	
		}
		
		_dg.setDataSource(dtDataSource);
        _dg.refresh();
	}
	
	private void InitDataGrid()
	{
		DataTable dtDataSource = new DataTable();
        dtDataSource.addAllColumns(new String[]{"co1", "col2","col3", "col4", "col5"});
        
		_dg.setDataSource(dtDataSource);
        _dg.refresh();
	}
}
