package com.rose.stockmanagement;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.codegineer.datagrid.DataGrid;
import com.codegineer.datagrid.DataGridAdapter;
import com.codegineer.datagrid.Item;
import com.codegineer.datatable.DataTable;
import com.codegineer.datatable.DataTable.DataRow;
import com.rose.stockmanagement.inter.OnUpdateDataCompleted;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

public class StockInventoryReplaceList extends Activity {

	final Context context = this;
	
	 String SelectID = "";
     String SelectWarehouseID = "";
     
     private DataGrid _dg;	
     private Boolean bIsDelBtnClick = false;	
     private int nSelectedIndex = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_inventory_replace_list);
		
		_dg = (DataGrid)findViewById(R.id.datagrid_replacelist);
		_dg.addColumnStyles(new DataGrid.ColumnStyle[]{
        		new DataGrid.ColumnStyle("", "co1", 60),
        		new DataGrid.ColumnStyle("현재창고", "col2", 250),
        		new DataGrid.ColumnStyle("이동창고", "col3", 250),
        		new DataGrid.ColumnStyle("총실사수량", "col4", 250),
        		new DataGrid.ColumnStyle("총상품수량", "col5", 250),
        		new DataGrid.ColumnStyle("작업일", "col6", 250),
        		new DataGrid.ColumnStyle("메모", "col7", 250),
        		new DataGrid.ColumnStyle("등록자", "col8", 250),
        		new DataGrid.ColumnStyle("아이디", "col9", 250)
        });
		
		Button btnUpload = (Button)findViewById(R.id.btn_replacelist_upload);
		if(Common.OffLineFlag.equals(true))
		{
			btnUpload.setEnabled(false);
		}
		else
			btnUpload.setEnabled(true);
		
		LoadStockChecking();
		InitControlsEvent();
	}
	
	private void InitControlsEvent()
	{
		Button btnOK = (Button)findViewById(R.id.btn_replacelist_ok);
        
        btnOK.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				if(nSelectedIndex != -1)
				{
					DataRow dr = _dg.GetDataSource().getRow(nSelectedIndex);
					
					SelectID = dr.get("col9");
					SelectWarehouseID = dr.get("col2");
				}
				else
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
				    builder
				    .setMessage("진행 할 항목을 선택하세요.")
				    .setIcon(android.R.drawable.ic_dialog_alert)
				    .setNegativeButton("확인", null); 
				    
				    AlertDialog alert = builder.create();
			        alert.show();
			        
                    return;
				}
				
				Intent intent = new Intent(context, StockInventoryReplaceDetail.class);
				
				Bundle b = new Bundle();
				b.putString("SelectID", SelectID);
				b.putString("SelectWarehouseID", SelectWarehouseID);
				intent.putExtras(b);
				
				startActivity(intent);
				
				finish();
			}
    	});
        
		final Button btnCheckAll = (Button)findViewById(R.id.btn_replacelist_checkall);
		btnCheckAll.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				if(btnCheckAll.getText().toString().equals("전체선택"))
				{
					btnCheckAll.setText("선택해제");
					
					DataGridAdapter adapter = _dg.GetDataGridAdapter();
					for (int i = 0; i < adapter.getCount(); i++) {
						adapter.getIsSelected().put(i, true);
					}
					
					_dg.refreshCheckState();
				}
				else
				{
					btnCheckAll.setText("전체선택");
					DataGridAdapter adapter = _dg.GetDataGridAdapter();
					for (int i = 0; i < adapter.getCount(); i++) {
						adapter.getIsSelected().put(i, false);
					}
					
					_dg.refreshCheckState();
				}
			}
    	});
		
		
		_dg.setLvOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				nSelectedIndex = arg2;
				
				Item item = (Item)arg1;
				for(int i = 0; i < arg0.getChildCount(); i++)
				{
					arg0.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
				}
				
				arg1.setBackgroundColor(Color.LTGRAY);
			}
			
		});
		
		Button btnUpload = (Button)findViewById(R.id.btn_replacelist_upload);
		btnUpload.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Boolean bChecked = false;
	            Boolean isFirst = true;
	            final Boolean isYes = false;
	            
	            for(int i = _dg.GetDataGridAdapter().getCount() - 1; i >= 0; i--)
	            {
	            	if(_dg.GetDataGridAdapter().getIsSelected().get(i))
	            	{
	            		bChecked = true;
	            		
	            		final DataRow dr = _dg.GetDataSource().getRow(i);
	            		if (dr.get("col4").equals("0"))
	                    {
	                        //MessageBox.Show("[" + listView1.Items[i].SubItems[1].Text + "] 실사항목이 없으므로 업로드되지 않습니다.");
	            			
	            			AlertDialog.Builder builder = new AlertDialog.Builder(context);
						    builder
						    .setMessage("[" + dr.get("col9") + "] 실사항목이 없으므로 업로드되지 않습니다.")
						    .setIcon(android.R.drawable.ic_dialog_alert)
						    .setNegativeButton("확인", null); 
						    
						    AlertDialog alert = builder.create();
					        alert.show();
					        
	                        continue;
	                    }
	            		
	            		
	            		if (isFirst)
	                    {
	                        isFirst = false;
	                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    				    builder
	    				    .setTitle("확인")
	    				    .setMessage("업로드 하시겠습니까?")
	    				    .setIcon(android.R.drawable.ic_dialog_alert)
	    				    .setCancelable(false)				    
	    				    .setPositiveButton("Yes", new DialogInterface.OnClickListener() 
	    				    {
	    				        public void onClick(DialogInterface dialog, int which) 
	    				        {       	
	    				        	StockUpload();	    				        	
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
	            		
	            		return;
	            	}
	            }
			}
			
		});
		
		Button btnDel = (Button)findViewById(R.id.btn_replacelist_dell);
		btnDel.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {	
				
				if(_dg.GetDataGridAdapter().GetCheckedCount() > 0)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
				    builder
				    .setTitle("확인")
				    .setMessage("선택한 항목을 삭제하시겠습니까?")
				    .setIcon(android.R.drawable.ic_dialog_alert)
				    .setCancelable(false)				    
				    .setPositiveButton("Yes", new DialogInterface.OnClickListener() 
				    {
				        public void onClick(DialogInterface dialog, int which) 
				        {       	
				        	deleteStockChecking();
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
		
		Button btnClose = (Button)findViewById(R.id.btn_replacelist_exit);
		btnClose.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {	
				
				finish();
			}
    	});
	}
	
	private void deleteStockChecking()
	{
		bIsDelBtnClick = true;
		
		if(!Login.dbHelper.deleteStockCheckingForReplace(_dg))
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
		
		this.LoadStockChecking();
	}
	
	private void StockUpload()
	{
		Thread thGetProductItem = new Thread(new Runnable(){
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				thGetProductItemThread();
			}
			
		});
		
        thGetProductItem.start();
        
        
	}
	
	private void thGetProductItemThread()
	{
		for(int i = _dg.GetDataGridAdapter().getCount() - 1; i >= 0; i--)
        {
        	if(_dg.GetDataGridAdapter().getIsSelected().get(i))
        	{
        		final DataRow dr = _dg.GetDataSource().getRow(i);
        		StockUploadByStepOne(dr);
        	}
        }
		
		this.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
		        LoadStockChecking();
			}
		});		
	}
	
	private void StockUploadByStepOne(final DataRow dr)
	{
		String strQuery = "";
		strQuery = "INSERT INTO ProductStockWarehouseTransfer (id,fromwarehouseid,towarehouseid,workdate,totalproductqty,totalqty,memo,pdauploadflag,insertdate,insertstaff" +
				") VALUES(";
		
		strQuery += String.format("'%s', ", dr.get("col9"));
		strQuery += String.format("'%s', ", Login.dbHelper.GetStoreOrWarehouseID(dr.get("col2")));
		strQuery += String.format("'%s', ", Login.dbHelper.GetStoreOrWarehouseID(dr.get("col3")));
		strQuery += String.format("'%s', ", dr.get("col6"));
		strQuery += String.format("'%s', ", dr.get("col4"));
		strQuery += String.format("'%s', ", dr.get("col5"));
		strQuery += String.format("'%s', ", dr.get("col7"));
		strQuery += String.format("'Y', ");
		strQuery += String.format("'%s', ", (String) DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()));
		strQuery += String.format("'%s')", Common.IDX_LOGIN);
		
		com.rose.stockmanagement.database.UpdateDataSync updateData = new com.rose.stockmanagement.database.UpdateDataSync();
		if(updateData.UpdateData(strQuery))
			StockItemUpload(dr);
	}
	
	private void StockItemUpload(DataRow dr)
	{
		String strId = dr.get("col9").toString();
		
		List listResult = Login.dbHelper.ConvertResultSetToList(Login.dbHelper.GetStockReplaceItem(strId));
		String strQuery = "";
		for(Object obj : listResult)
		{
			
			String strSubQuery = "";
			strSubQuery = "INSERT INTO ProductStockWarehouseTransferItem (id,fromwarehouseid,towarehouseid,productid,qty,workdate,workerid,insertdate,insertstaff" +
					") VALUES(";
			
			HashMap<String, Object> h = (HashMap<String, Object>)obj;
			
			strSubQuery += String.format("'%s', ", strId);
			strSubQuery += String.format("'%s', ", h.get("FROMWAREHOUSEID"));
			strSubQuery += String.format("'%s', ", h.get("TOWAREHOUSEID"));
			strSubQuery += String.format("'%s', ", h.get("PRODUCTID"));
			strSubQuery += String.format("'%s', ", h.get("QTY"));
			strSubQuery += String.format("'%s', ", (String) DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()));
			strSubQuery += String.format("'%s', ", h.get("WORKERID"));
			strSubQuery += String.format("'%s', ", (String) DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()));
			strSubQuery += String.format("'%s')\r\n", Common.IDX_LOGIN);
			
			strQuery += strSubQuery;
		}
		
		com.rose.stockmanagement.database.UpdateDataSync updateData = new com.rose.stockmanagement.database.UpdateDataSync();
		if(updateData.UpdateData(strQuery))
		{
			bIsDelBtnClick = false;
			deleteUploadList(dr);
		}
	}
	
	private void deleteUploadList(DataRow dr)
	{
		if(!Login.dbHelper.deleteUploadListForReplace(dr))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
		    builder
		    .setMessage("발주등록 항목 삭제가 실패하였습니다.")
		    .setIcon(android.R.drawable.ic_dialog_alert)
		    .setNegativeButton("확인", null); 
		    
		    AlertDialog alert = builder.create();
	        alert.show();
		}
	}
	
	private void LoadStockChecking()
	{
		InitDataGrid();
		SyncStockItemQty();
	}
	
	private void InitDataGrid()
	{
		DataTable dtDataSource = new DataTable();
        dtDataSource.addAllColumns(new String[]{"co1", "col2","col3", "col4", "col5", "col6", "col7", "col8", "col9"});
        
		_dg.setDataSource(dtDataSource);
        _dg.refresh();
	}
	
	private void SyncStockItemQty()
	{
		GetStockcheckingItemSum();
	}
	
	private void GetStockcheckingItemSum()
	{
		List dtItemSum = Login.dbHelper.ConvertResultSetToList(Login.dbHelper.GetStockReplaceItemSum());
		List dtStock = Login.dbHelper.ConvertResultSetToList(Login.dbHelper.GetStockReplaceChecking());
		
		if(!RealSyncStockItemQty(dtStock, dtItemSum))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder
		    .setMessage("조사 수량 동기화 실패")
		    .setIcon(android.R.drawable.ic_dialog_alert)
		    .setNegativeButton("확인", null); 
		    
			AlertDialog alert = builder.create();
			        alert.show();
            return;
		}
		
		GetStockChecking("");
	}
	
	private Boolean RealSyncStockItemQty(List listResult, List dtItemSum)
	{
		Boolean bRet = true;
		
		for(Object obj : listResult)
		{
			boolean bUpdate = false;
			HashMap h = (HashMap)obj;
			
			for(Object obj1 : dtItemSum)
			{
				HashMap hashResultItemSum = (HashMap)obj1;
				if(hashResultItemSum.get("ID").toString().equals(h.get("ID").toString()))
				{
					bUpdate = true;
					if(!Login.dbHelper.UpdateStockReplaceChecking(h.get("ID").toString(),
							hashResultItemSum.get("CNT").toString(),
							hashResultItemSum.get("TOTALQTY").toString()))
					{
						bRet = false;
					}
					
					break;
				}
				
				if(bUpdate == false)
				{
					if(!Login.dbHelper.UpdateStockReplaceChecking(h.get("ID").toString(),
							"0",
							"0"))
					{
						bRet = false;
					}
				}
			}
		}
		
		return bRet;
	}
	
	private void GetStockChecking(String strID)
	{		
		List listResult = Login.dbHelper.ConvertResultSetToList(Login.dbHelper.GetStockReplace());
		DataTable dtDataSource = new DataTable();
		dtDataSource.addAllColumns(new String[]{"co1", "col2","col3", "col4", "col5", "col6", "col7", "col8", "col9"});
        
		List resultStock = listResult;
		if(resultStock == null)
			return;
		
		for(HashMap<String, Object> h : (List<HashMap<String, Object>>)resultStock)
		{
	        DataTable.DataRow drRow;
	        
        	drRow = dtDataSource.newRow();
        	
        	drRow.set(0, "false");
        	drRow.set(1, h.get("FROMWAREHOUSEID").toString());
        	drRow.set(2, h.get("TOWAREHOUSEID").toString());
        	drRow.set(3, h.get("TOTALPRODUCTQTY").toString());
        	drRow.set(4, h.get("TOTALQTY").toString());
        	drRow.set(5, h.get("WORKDATE").toString());
        	drRow.set(6, h.get("MEMO").toString());
        	drRow.set(7, h.get("STAFFID").toString());
        	drRow.set(8, h.get("ID").toString());
        	
        	dtDataSource.add(drRow);	
		}
		
		_dg.setDataSource(dtDataSource);
        _dg.refresh();
	}
}
