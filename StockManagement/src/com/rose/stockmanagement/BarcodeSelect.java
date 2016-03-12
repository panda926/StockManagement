package com.rose.stockmanagement;

import java.util.HashMap;
import java.util.List;

import com.codegineer.datagrid.DataGrid;
import com.codegineer.datagrid.Item;
import com.codegineer.datatable.DataTable;
import com.codegineer.datatable.DataTable.DataRow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class BarcodeSelect extends Activity {

	String selCode = "";
	List listProductInfo;
	
	private DataGrid _dg;	
	private int nSelectedIndex = -1;
	
	private final Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barcode_select);
						
		_dg = (DataGrid)findViewById(R.id.barcodeset_datagrid);
		
		selCode = getIntent().getStringExtra("selCode");
		listProductInfo = Login.dbHelper.ConvertResultSetToList(Login.dbHelper.GetProductInfo(selCode));
		
//		TextView lblName = (TextView)findViewById(R.id.lbl_name);
//		lblName.setTextSize(convertFromDp(24));
		
		InitGridView();
		SetData();
		InitControlEvent();
		
		if(listProductInfo.size() == 1)
		{			
			ListView mList = this._dg.GetListView();
			mList.performItemClick(
				    mList.getAdapter().getView(0, null, null),
				    0,
				    mList.getAdapter().getItemId(0));
			
			Button btnOK = (Button)findViewById(R.id.barcodeset_btnOK);
			btnOK.performClick();
		}
	}
	
	public float convertFromDp(int input) {
	    final float scale = getResources().getDisplayMetrics().density;
	    return ((input - 0.5f) / scale);
	}
	
	private void InitControlEvent()
	{
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
		
		Button btnSearch = (Button)findViewById(R.id.barcodeset_btnSearch);
		
		btnSearch.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				EditText txtMakerCode = (EditText)findViewById(R.id.barcodeset_txtMakercode);
				EditText txtProduct = (EditText)findViewById(R.id.barcodeset_txtProduct);
				
				listProductInfo = Login.dbHelper.ConvertResultSetToList(Login.dbHelper.GetProductInfo(txtMakerCode.getText().toString(), txtProduct.getText().toString()));
				SetData();
			}
    	});
		
		Button btnOK = (Button)findViewById(R.id.barcodeset_btnOK);
        
        btnOK.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				if(nSelectedIndex != -1)
				{
					DataRow dr = _dg.GetDataSource().getRow(nSelectedIndex);
					
					String strSelectID = dr.get("col2");
					
					Intent result = new Intent();
					
					Bundle b = new Bundle();
					b.putString("SelectID", strSelectID);
					
					result.putExtras(b);
					
					setResult(Activity.RESULT_OK, result);
					finish();
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
			}
    	});
        
        Button btnClose = (Button)findViewById(R.id.barcodeset_btnClose);
        
        btnClose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				finish();
			}
    	});
	}
	
	private void SetData()
	{
		if(listProductInfo.size() > 0)
		{
			HashMap<String, Object> h0 = (HashMap<String, Object>)listProductInfo.get(0);
			
			EditText txtMakerCode = (EditText)findViewById(R.id.barcodeset_txtMakercode);
			EditText txtProduct = (EditText)findViewById(R.id.barcodeset_txtProduct);
			
			txtMakerCode.setText(h0.get("MAKERCODE").toString());
			txtProduct.setText(h0.get("NAME").toString());
		}
		
		InitDataGrid();
		
		DataTable dtDataSource = new DataTable();
		dtDataSource.addAllColumns(new String[]{"co1", "col2","col3", "col4"});
		
		for(HashMap<String, Object> h : (List<HashMap<String, Object>>)listProductInfo)
		{
	        DataTable.DataRow drRow;
	        
        	drRow = dtDataSource.newRow();
        	
        	drRow.set(0, "false");
        	drRow.set(1, h.get("ID").toString());
        	drRow.set(2, h.get("MODEL").toString());
        	drRow.set(3, "");
        	        	
        	dtDataSource.add(drRow);	
		}
		
		_dg.setDataSource(dtDataSource);
        _dg.refresh();
	}
	
	private void InitDataGrid()
	{
		DataTable dtDataSource = new DataTable();
        dtDataSource.addAllColumns(new String[]{"co1", "col2","col3", "col4"});
        
		_dg.setDataSource(dtDataSource);
        _dg.refresh();
	}
	
	private void InitGridView()
	{
		_dg.addColumnStyles(new DataGrid.ColumnStyle[]{
        		new DataGrid.ColumnStyle("", "co1", 60),
        		new DataGrid.ColumnStyle("바코드", "col2", 400),
        		new DataGrid.ColumnStyle("모델명", "col3", 400),
        		new DataGrid.ColumnStyle("", "col4", 1000)
        });
	}
}
