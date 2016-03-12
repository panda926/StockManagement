package com.rose.stockmanagement.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class CommonUtils {
	
	private static Boolean yesNoMessageResult = false;
	
	public static void ShowMessage(String strMessage, String strTitle, Context context)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    builder
	    .setTitle(strTitle)
	    .setMessage(strMessage)
	    .setIcon(android.R.drawable.ic_dialog_alert);
//	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() 
//	    {
//	        public void onClick(DialogInterface dialog, int which) 
//	        {       
//	               //do some thing here which you need
//	    }
//	    });   
	    
	    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int which) 
	        {   
	        	dialog.dismiss();           
	        }
	    });        
	    
		AlertDialog alert = builder.create();
		        alert.show();
	}
	
	public static Boolean ShowYesNoMessage(String strMessage, String strTitle, Context context)
	{	
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    builder
	    .setTitle(strTitle)
	    .setMessage(strMessage)
	    .setIcon(android.R.drawable.ic_dialog_alert)
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int which) 
	        {       
	        	yesNoMessageResult = true;
	        }
	    });   
	    
	    builder.setNegativeButton("No", new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int which) 
	        {   
	        	dialog.dismiss();     
	        	yesNoMessageResult = false;
	        }
	    });        
	    
		AlertDialog alert = builder.create();
		        alert.show();
		        
		return yesNoMessageResult;
	}
}
