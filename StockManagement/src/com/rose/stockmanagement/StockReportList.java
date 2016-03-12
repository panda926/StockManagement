package com.rose.stockmanagement;

import java.util.HashMap;
import java.util.List;

import com.codegineer.datagrid.DataGrid;
import com.codegineer.datatable.DataTable;
import com.rose.stockmanagement.database.GetData;
import com.rose.stockmanagement.inter.OnGetDataCompleted;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StockReportList extends Activity {

	String strBarcode = "";
    String strStoreid = "";
    String strCategoryid = "";
    String strTeamid = "";
    
    private DataGrid _dg;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_report_list);
				
		strBarcode = getIntent().getStringExtra("barcode");
		strStoreid = getIntent().getStringExtra("storeid");
		strCategoryid = getIntent().getStringExtra("categoryid");
		strTeamid = getIntent().getStringExtra("teamid");
		
		InitControlEvent();
		InitGridView();
		SetDefault();		
	}
	
	private void InitControlEvent()
	{
		Button btnClose = (Button)findViewById(R.id.btn_stockreportlist_close);
		
		// 재고이동에서 목록버튼을 클릭하였을떄...
		btnClose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				finish();
			}
    	});
	}
	
	private void InitGridView()
	{
		_dg = (DataGrid)findViewById(R.id.datagrid_stockreport);
		_dg.addColumnStyles(new DataGrid.ColumnStyle[]{
				new DataGrid.ColumnStyle("", "col1", 60),
        		new DataGrid.ColumnStyle("매장/창고", "col2", 330),
        		//new DataGrid.ColumnStyle("분류", "col3", 330),
        		new DataGrid.ColumnStyle("상품명", "col3", 330),
        		new DataGrid.ColumnStyle("사이즈", "col4", 330),
        		new DataGrid.ColumnStyle("재고량", "col5", 330),
        		new DataGrid.ColumnStyle("상품코드", "col6", 330)
        });
	}
	
	private void InitDataGrid()
	{
		DataTable dtDataSource = new DataTable();
        //dtDataSource.addAllColumns(new String[]{"co1", "col2","col3", "col4", "col5", "col6"});
		dtDataSource.addAllColumns(new String[]{"co1", "col2", "col3", "col4", "col5", "col6"});
        
		_dg.setDataSource(dtDataSource);
        _dg.refresh();
	}
	
	private void SetDefault()
	{
		InitDataGrid();
		
		String strQuery = "";
		String strTeamId = "";
		if (strTeamid.length() > 0)
			strTeamId = strTeamid;
        else if (strStoreid.length() > 0)
        	strTeamId = strStoreid;
		
		strQuery = String.format("dbo.GetAllStock '%s', '%s', '%s';", strCategoryid, strBarcode, strTeamId);
		
		com.rose.stockmanagement.database.GetData._strMessage = "잠시만 기다려주세요.";
		com.rose.stockmanagement.database.GetData getData = new GetData(new OnGetDataCompleted()
		{

			@Override
			public void onGetDataCompleted(List listResult,
					String strQueryType, Context context) {
				// TODO Auto-generated method stub
				DataTable dtDataSource = new DataTable();
				//dtDataSource.addAllColumns(new String[]{"co1", "col2","col3", "col4", "col5", "col6"});
				dtDataSource.addAllColumns(new String[]{"co1", "col2", "col3", "col4", "col5", "col6"});
		        
				List resultStock = listResult;
				if(resultStock == null)
					return;
				
				for(HashMap<String, Object> h : (List<HashMap<String, Object>>)resultStock)
				{
			        DataTable.DataRow drRow;
			        
		        	drRow = dtDataSource.newRow();
		        	
//		        	drRow.set(0, "");
//		        	//drRow.set(1, h.get("warehouseorstoreid").toString());
//		        	drRow.set(1, Login.dbHelper.GetWarehouseName(h.get("warehouseorstoreid").toString()));
//		        	drRow.set(2, Login.dbHelper.GetStoreName(h.get("categoryid").toString()));
//		        	drRow.set(3, h.get("productname").toString());
//		        	drRow.set(4, h.get("stockquantity").toString());
//		        	drRow.set(5, h.get("productid").toString());    	       
		        	
		        	int nStockQuantity = Integer.parseInt(h.get("stockquantity").toString());
		        	
		        	if(nStockQuantity > 0)
		        	{
			        	drRow.set(0, "");
			        	//drRow.set(1, h.get("warehouseorstoreid").toString());
			        	if(Login.dbHelper.GetWarehouseName(h.get("warehouseorstoreid").toString().trim()) == "")
			        	{
			        		String strVal = h.get("warehouseorstoreid").toString();
			        	}
			        	
			        	drRow.set(1, Login.dbHelper.GetWarehouseName(h.get("warehouseorstoreid").toString().trim()));		        	
			        	drRow.set(2, h.get("productname").toString());
			        	drRow.set(3, h.get("specsizeid").toString());
			        	drRow.set(4, h.get("stockquantity").toString());
			        	drRow.set(5, h.get("productid").toString());    
			        	
			        	dtDataSource.add(drRow);
		        	}
				}
				
				_dg.setDataSource(dtDataSource);
		        _dg.refresh();
			}
			
		}, "", this);
		
		getData.execute(strQuery);
	}
}
