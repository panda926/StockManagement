package com.rose.stockmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StockActivity extends Activity {

	final Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock);
		
		Button btnGetStock = (Button)findViewById(R.id.btn_getstock); 
		btnGetStock.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				Intent intent = new Intent(context, StockInventory.class);
				startActivity(intent);
				
			}
    	});
		
		Button btnMoveStock = (Button)findViewById(R.id.btn_movestock); 
		btnMoveStock.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				Intent intent = new Intent(context, StockInventoryReplaceNew.class);
				startActivity(intent);
				
			}
    	});
		
		Button btnStockReport = (Button)findViewById(R.id.btn_stock); 
		btnStockReport.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				Intent intent = new Intent(context, StockReport.class);
				startActivity(intent);
				
			}
    	});
		
		Button btnClose = (Button)findViewById(R.id.btn_stock_cancel); 
		btnClose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				finish();
			}
    	});
	}
}
