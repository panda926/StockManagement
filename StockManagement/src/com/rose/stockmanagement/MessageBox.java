package com.rose.stockmanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class MessageBox {
	
	private static boolean mResult;
	public static boolean getYesNoWithExecutionStop(String title, String message, Context context) {
	    // make a handler that throws a runtime exception when a message is received
	    final Handler handler = new Handler() {
	        @Override
	        public void handleMessage(Message mesg) {
	            throw new RuntimeException();
	        } 
	    };

	    // make a text input dialog and show it
	    AlertDialog.Builder alert = new AlertDialog.Builder(context);
	    alert.setTitle(title);
	    alert.setMessage(message);
	    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            mResult = true;
	            handler.sendMessage(handler.obtainMessage());
	        }
	    });
	    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            mResult = false;
	            handler.sendMessage(handler.obtainMessage());
	        }
	    });
	    alert.show();

	    // loop till a runtime exception is triggered.
	    try { Looper.loop(); }
	    catch(RuntimeException e2) {}

	    return mResult;
	}
}
