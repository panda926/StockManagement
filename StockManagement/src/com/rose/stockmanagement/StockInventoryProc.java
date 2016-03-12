package com.rose.stockmanagement;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class StockInventoryProc extends Activity {

	private String barcode = "";
    private String qty = "";
    private String nowqty = "";
    
    private final Context context = this;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_inventory_proc);
		
		barcode = getIntent().getStringExtra("barcode");
		qty = getIntent().getStringExtra("qty");
		nowqty = getIntent().getStringExtra("SelectNowQty");
		
		InitControlsEvent();
		SetDefault();
	}
	
	private void InitControlsEvent()
	{
		Button btnOK = (Button)findViewById(R.id.inventory_proc_btnOK);
		
		// 확인버튼을 클릭하였을때...
		btnOK.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				EditText tbQty = (EditText)findViewById(R.id.tbQty);
				EditText tbBarcode = (EditText)findViewById(R.id.tbBarcode);
				
				if(tbQty.getText().toString().length() == 0)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    		    builder
	    		    .setMessage("수량을 입력하세요.")
	    		    .setIcon(android.R.drawable.ic_dialog_alert)
	    		    .setNegativeButton("확인", null).show();  
	    		    
					return;
				}
				
				if(MessageBox.getYesNoWithExecutionStop("통보", "저장하시겠습니까?", context))
            	{
					Intent result = new Intent();
					
					Bundle b = new Bundle();
					b.putString("qty", tbQty.getText().toString());
					b.putString("barcode", tbBarcode.getText().toString());
					
					result.putExtras(b);
					
					setResult(Activity.RESULT_OK, result);
					finish();
            	}
			}
    	});
		
		Button btnClose = (Button)findViewById(R.id.inventory_proc_btnClose);
		
		// 닫기버튼을 클릭하였을때...
		btnClose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				
				if(MessageBox.getYesNoWithExecutionStop("통보", "등록을 취소하시겠습니까?", context))
				{
					finish();
				}
			}
    	});
	}
	
	private Boolean SetDefault()
    {
		Boolean bRet = false;

        try
        {
        	List list = Login.dbHelper.ConvertResultSetToList(Login.dbHelper.GetProductInfo(barcode));
        	
        	if(list.size() > 0)
        	{
        		HashMap<String, Object> h = (HashMap<String, Object>)list.get(0);
        		
        		String strPrice = (NumberFormat.getNumberInstance(Locale.KOREA).format(Double.parseDouble(h.get("PRICE").toString())));
        		
        		EditText tbBarcode = (EditText)findViewById(R.id.tbBarcode); tbBarcode.setText(h.get("ID").toString());
        		EditText tbProductName = (EditText)findViewById(R.id.tbProductName); tbProductName.setText(h.get("NAME").toString());
        		EditText tbModel = (EditText)findViewById(R.id.tbModel); tbModel.setText(h.get("MODEL").toString());
        		//EditText tbPrice = (EditText)findViewById(R.id.tbPrice); tbPrice.setText(h.get("PRICE").toString());
        		EditText tbPrice = (EditText)findViewById(R.id.tbPrice); tbPrice.setText(strPrice);
        		EditText tbColor = (EditText)findViewById(R.id.tbColor); tbColor.setText(h.get("COLOR").toString());
        		EditText tbMaterial = (EditText)findViewById(R.id.tbMaterial); tbMaterial.setText(h.get("MATERIAL").toString());
        		EditText tbMemo = (EditText)findViewById(R.id.tbMemo); tbMemo.setText(h.get("MEMO").toString());
        		EditText tbNowQty = (EditText)findViewById(R.id.tbNowQty); tbNowQty.setText(nowqty);
        		EditText tbQty = (EditText)findViewById(R.id.tbQty); tbQty.setText(qty);
        		
        		bRet = true;
        	}
        	else
        	{
        		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		    builder
    		    .setMessage("상품정보가 없습니다.")
    		    .setIcon(android.R.drawable.ic_dialog_alert)
    		    .setNegativeButton("확인", null).show();  
        	}
        }
        catch (Exception ex)
        {            
        }

        return bRet;
    }
}
