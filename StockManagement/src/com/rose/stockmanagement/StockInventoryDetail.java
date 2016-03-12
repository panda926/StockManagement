package com.rose.stockmanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.codegineer.datagrid.DataGrid;
import com.codegineer.datagrid.Item;
import com.codegineer.datatable.DataTable;
import com.codegineer.datatable.DataTable.DataRow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class StockInventoryDetail extends Activity {

	String stReturn_Barcode = "";
    String stReturn_GoodsName = "";
    String stReturn_Count = "";

    String SelectID = "";
    String SelectWarehouseID = "";
    
    private DataGrid _dg;	
    
    static final int CODE_REQUEST = 1;
    static final int CODE_REQUEST_BARCODE = 2;
    
    private final Context context = this;  
    
    TextView txtTotalCount;    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_inventory_detail);
		
		SelectID = getIntent().getStringExtra("SelectID");
		SelectWarehouseID = getIntent().getStringExtra("SelectWarehouseID");
		
		txtTotalCount = (TextView)findViewById(R.id.lblInventoryDetail_totalcount);
		txtTotalCount.setText("�Ѽ���: 0");
		
		InitGridView();			
		SetDafault();
		InitControlsEvent();	
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
		if(listProductInfo.size() >= 0)
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
		    .setMessage("��ǰ ������ �����ϴ�.")
		    .setIcon(android.R.drawable.ic_dialog_alert)
		    .setNegativeButton("Ȯ��", null).show();  
		}
		
		_strBarcode = "";
	}
	
	private void InitControlsEvent()
	{
//		Button btnTest1 = (Button)findViewById(R.id.btn_stockinventorydetail_test1);
//		
//		// ������翡�� ��Ϲ�ư�� Ŭ���Ͽ�����...
//		btnTest1.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {	
//				//StockProc("8806153715141");
//				//StockProc("1234567890123");
//				ShowBarCodeSelect("LT141HDA200L");
//			}
//    	});
		
		_dg.setLvOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				int nRow = arg2;
				DataRow dr = _dg.GetDataSource().getRow(nRow);
				StockProc(dr.get("col2"));
			}
			
		});
		
		Button btnDelete = (Button)findViewById(R.id.btn_stockinventorydetail_del);
		
		btnDelete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				if(_dg.GetDataGridAdapter().GetCheckedCount() > 0)
				{					        
					if(MessageBox.getYesNoWithExecutionStop("Ȯ��", "������ �׸��� �����Ͻðڽ��ϱ�?", context))
					{
						deleteStockCheckingItem();
						LoadStockCheckingItem();
					}
				}
			}
    	});
		
		Button btnNext = (Button)findViewById(R.id.btn_stockinventorydetail_next);
		
		btnNext.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				Intent intent = new Intent(context, StockInventoryList.class);
				
				Bundle b = new Bundle();
				b.putString("selectID", SelectID);
				intent.putExtras(b);
				
				startActivity(intent);
				
				finish();
			}
    	});
		
		Button btnSearch = (Button)findViewById(R.id.btn_stockinventorydetail_search);
		
		btnSearch.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				ShowBarCodeSelect("");
			}
    	});
		
		Button btnClose = (Button)findViewById(R.id.btn_stockinventorydetail_cancel);
		
		btnClose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				finish();
			}
    	});				
	}
	
	private void deleteStockCheckingItem()
	{		
		if(!Login.dbHelper.deleteStockCheckingItem(_dg, SelectID))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
		    builder
		    .setMessage("������� �׸� ������ �����Ͽ����ϴ�.")
		    .setIcon(android.R.drawable.ic_dialog_alert)
		    .setNegativeButton("Ȯ��", new DialogInterface.OnClickListener() 
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
		//������ �⺻ 1
        String qty = "1";
        String convertBarcode = "";
        String SelectNowQty = "0";
        
        //����Ʈ�� �̹� �ִ��� �˻�
        
        
        int LogisticA = 0;
        int LogisticB = 0;
        int LogisticC = 0;
        
        List list = Login.dbHelper.ConvertResultSetToList(Login.dbHelper.GetProductInfo(barcode));
        if(list.size() == 0)
        {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder
		    .setMessage("��ǰ������ �����ϴ�.")
		    .setIcon(android.R.drawable.ic_dialog_alert)
		    .setNegativeButton("Ȯ��", null).show();  
		    
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
        		SelectNowQty = dr.get("col4").toString();
        	}
        }
        
        LogisticA = Integer.parseInt(h.get("LOGISTICSUNITA").toString());
        LogisticB = Integer.parseInt(h.get("LOGISTICSUNITB").toString());
        LogisticC = Integer.parseInt(h.get("LOGISTICSUNITC").toString());

        //�����ڽ� ���ڵ� Ȥ�� �ڻ��ڵ����� Ȯ�� ����. �ڽ������� �ƴ϶�� ��ǰ������ ó��.
        if (h.get("LOGISTICSCODEA1").toString().equals(barcode))
            qty = h.get("LOGISTICSUNITA").toString();
        else if (h.get("LOGISTICSCODEA2").toString().equals(barcode))
        	qty = h.get("LOGISTICSUNITA").toString();
        //�����ڽ�B = �����ڽ�A X �����ڽ�B (�����ڽ�B�� 0�̶�� A�� ������ �Ѱ��ش�.)
        else if (h.get("LOGISTICSCODEB1").toString().equals(barcode))
        {
            if (h.get("LOGISTICSUNITB").toString().equals("0"))
            {            	
            	if(MessageBox.getYesNoWithExecutionStop("�뺸", "���ڵ� ��ĵ ��� �����ڽ�B�� ������ �����ϴ�. \n�����ڽ�A�� �������� �Է��Ͻðڽ��ϱ�?", this))
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
            	if(MessageBox.getYesNoWithExecutionStop("�뺸", "���ڵ� ��ĵ ��� �����ڽ�B�� ������ �����ϴ�. \n�����ڽ�A�� �������� �Է��Ͻðڽ��ϱ�?", this))
            	{
            		qty = String.valueOf(LogisticA);
            	}
            	else
            		return;
            }
            else
            	qty = String.valueOf(LogisticA * LogisticB);
        }
        //�����ڽ�C = �����ڽ�A X �����ڽ�B X �����ڽ�C (�����ڽ�B��C�� 0�̶�� A�� ������.)
        else if (h.get("LOGISTICSCODEC1").toString().equals(barcode))
        {
            if (h.get("LOGISTICSUNITC").toString().equals("0"))
            {
                if (h.get("LOGISTICSUNITB").toString().equals("0"))
                {
                    {
                    	if(MessageBox.getYesNoWithExecutionStop("�뺸", "���ڵ� ��ĵ ��� �����ڽ�B��C�� ������ �����ϴ�. \n�����ڽ�A�� �������� �Է��Ͻðڽ��ϱ�?", this))
                    	{
                    		qty = String.valueOf(LogisticA);
                    	}
                    	else
                    		return;
                    }
                }
                else
                {
                	if(MessageBox.getYesNoWithExecutionStop("�뺸", "���ڵ� ��ĵ ��� �����ڽ�C�� ������ �����ϴ�. \n�����ڽ�AxB�� �������� �Է��Ͻðڽ��ϱ�?", this))
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
                    	if(MessageBox.getYesNoWithExecutionStop("�뺸", "���ڵ� ��ĵ ��� �����ڽ�B��C�� ������ �����ϴ�. \n�����ڽ�A�� �������� �Է��Ͻðڽ��ϱ�?", this))
                    	{
                    		qty = String.valueOf(LogisticA);
                    	}
                    	else
                    		return;
                    }
                }
                else
                {
                	if(MessageBox.getYesNoWithExecutionStop("�뺸", "���ڵ� ��ĵ ��� �����ڽ�C�� ������ �����ϴ�. \n�����ڽ�AxB�� �������� �Է��Ͻðڽ��ϱ�", this))
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
		    .setMessage("��ǰ ������ �����ϴ�.")
		    .setIcon(android.R.drawable.ic_dialog_alert)
		    .setNegativeButton("Ȯ��", null).show();  
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
	        		String strValue = String.valueOf(Integer.parseInt(drItem.get("col4").toString()) + Integer.parseInt(strQty));
	        		drItem.set("col4", strValue);
	        		drItem.set("col2", strBarcode);
	        		
	        		if(Login.dbHelper.UpdateStockCheckingItem(drItem, SelectID))
	        		{
	        			_dg.GetDataSource().getRow(iExistIdx).set("col4", strValue);
	        			_dg.refresh();
	        		}
	        		
	        		iExistIdx = -1;
	        		
	        		ShowTotalCount();
	        	}
	        	else
	        	{
	        		if (!InsertStockItem(strBarcode, strQty, h.get("NAME").toString()))
                    {
	        			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		    		    builder1
		    		    .setMessage("�׸� ����� �����Ͽ����ϴ�.")
		    		    .setIcon(android.R.drawable.ic_dialog_alert)
		    		    .setNegativeButton("Ȯ��", null).show();  
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
			EditText tbStore = (EditText)findViewById(R.id.edit_stockinventorydetail_store);
			
			List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
			HashMap<String,Object> row = new HashMap<String, Object>(1);
			row.put("ID", SelectID);
			row.put("WAREHOUSEID", tbStore.getText().toString());
			row.put("PRODUCTID", barcode);
			row.put("QTY", qty);
			row.put("WORKDATE", workdate);
			row.put("WORKERID", Common.IDX_LOGIN);
			//row.put("WORKERID", Common.IDX_NAME);
			row.put("PRODUCTNAME", productname);
			
//			int nTotalCount = Integer.parseInt(qty);
//			this.txtTotalCount.setText("�Ѽ���: " + nTotalCount);
			
			list.add(row);
					    
			if(Login.dbHelper.InsertStockCheckingItem(list))
			{
				bRet = true;
				
				DataRow drRow = _dg.GetDataSource().newRow();
				
				drRow.set(0, "false");
	        	drRow.set(1, row.get("PRODUCTID").toString());
	        	drRow.set(2, productname);
	        	drRow.set(3, row.get("QTY").toString());
	        	drRow.set(4, row.get("WORKDATE").toString());
	        	//drRow.set(5, row.get("WORKERID").toString());
	        	drRow.set(5, Common.IDX_NAME);
	        	
	        	_dg.GetDataSource().add(drRow);
	        	_dg.refresh();
			}
			
			ShowTotalCount();
        }
        catch (Exception ex)
        {
        	String strErroMsg = ex.toString();
        }
		
		return bRet;
	}
	
	private void ShowTotalCount()
	{
		int nTotalCount = 0;
		for( int i = 0; i < this._dg.GetDataGridAdapter().getCount(); i++)
		{
			DataRow dr = _dg.GetDataSource().getRow(i);
			nTotalCount += Integer.parseInt(dr.get("col4"));
		}
		
		this.txtTotalCount.setText("�Ѽ���: " + nTotalCount);
	}
	
	private void InitGridView()
	{
		_dg = (DataGrid)findViewById(R.id.datagrid_stockinventory_detail);
		_dg.addColumnStyles(new DataGrid.ColumnStyle[]{
        		new DataGrid.ColumnStyle("", "co1", 60),
        		new DataGrid.ColumnStyle("�ڻ��ڵ�", "col2", 300),
        		new DataGrid.ColumnStyle("��ǰ��", "col3", 300),
        		new DataGrid.ColumnStyle("����", "col4", 300),
        		new DataGrid.ColumnStyle("�۾��Ͻ�", "col5", 300),
        		new DataGrid.ColumnStyle("�۾���", "col6", 300)
        });
	}
	
	private void SetDafault()
    {

        stReturn_Barcode = "";
        stReturn_GoodsName = "";
        stReturn_Count = "";

        EditText tbStore = (EditText)findViewById(R.id.edit_stockinventorydetail_store);
        tbStore.setText(SelectWarehouseID);

        LoadStockCheckingItem();        
    }
	
	private void LoadStockCheckingItem()
	{
		InitDataGrid();
		GetStockCheckingItem();
	}
	
	private void GetStockCheckingItem()
	{
		List listResult = Login.dbHelper.ConvertResultSetToList(Login.dbHelper.GetStockCheckingItem(SelectID));
		DataTable dtDataSource = new DataTable();
		dtDataSource.addAllColumns(new String[]{"co1", "col2","col3", "col4", "col5", "col6"});
        
		List resultStock = listResult;
		if(resultStock == null)
			return;			
		
		int nTotalCount = 0;
		
		for(HashMap<String, Object> h : (List<HashMap<String, Object>>)resultStock)
		{
	        DataTable.DataRow drRow;
	        
        	drRow = dtDataSource.newRow();
        	
        	drRow.set(0, "false");
        	drRow.set(1, h.get("PRODUCTID").toString());
        	drRow.set(2, h.get("PRODUCTNAME").toString());
        	drRow.set(3, h.get("QTY").toString());
        	drRow.set(4, h.get("WORKDATE").toString());
        	drRow.set(5, Login.dbHelper.GetUserName(h.get("WORKERID").toString()));   	        
        	
        	dtDataSource.add(drRow);	
        	
        	nTotalCount += Integer.parseInt(h.get("QTY").toString());
		}
		
		this.txtTotalCount.setText("�Ѽ���: " + nTotalCount);
		
		_dg.setDataSource(dtDataSource);
        _dg.refresh();
	}
	
	private void InitDataGrid()
	{
		DataTable dtDataSource = new DataTable();
        dtDataSource.addAllColumns(new String[]{"co1", "col2","col3", "col4", "col5", "col6"});
        
		_dg.setDataSource(dtDataSource);
        _dg.refresh();
	}
}
