package com.rose.stockmanagement.customecontrol;

import com.rose.stockmanagement.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class CustomDialogYes extends Dialog{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();    
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.8f;
		getWindow().setAttributes(lpWindow);
		
		setContentView(R.layout.custom_dialog_yes);
		
		setLayout();
		setTitle(mTitle);
		setContent(mContent);
		setClickListener(mLeftClickListener , mRightClickListener);
	}
	
	public CustomDialogYes(Context context) {
		//
		super(context , android.R.style.Theme_Translucent_NoTitleBar);
	}
	
	public CustomDialogYes(Context context , String title , 
			View.OnClickListener singleListener) {
		super(context , android.R.style.Theme_Translucent_NoTitleBar);
		this.mTitle = title;
		this.mLeftClickListener = singleListener;
	}
	
	public CustomDialogYes(Context context , String title , String content , 
			View.OnClickListener leftListener ,	View.OnClickListener rightListener) {
		super(context , android.R.style.Theme_Translucent_NoTitleBar);
		this.mTitle = title;
		this.mContent = content;
		this.mLeftClickListener = leftListener;
		this.mRightClickListener = rightListener;
	}
	
	private void setTitle(String title){
		mTitleView.setText(title);
	}
	
	private void setContent(String content){
		mContentView.setText(content);
	}
	
	private void setClickListener(View.OnClickListener left , View.OnClickListener right){
		if(left!=null && right!=null){
			mLeftButton.setOnClickListener(left);
			mRightButton.setOnClickListener(right);
		}else if(left!=null && right==null){
			mLeftButton.setOnClickListener(left);
		}else {
			
		}
	}
	
	/*
	 * Layout
	 */
	private TextView mTitleView;
	private TextView mContentView;
	private Button mLeftButton;
	private Button mRightButton;
	private String mTitle;
	private String mContent;
	
	
	private View.OnClickListener mLeftClickListener;
	private View.OnClickListener mRightClickListener;
	
	/*
	 * Layout
	 */
	private void setLayout(){
		mTitleView = (TextView) findViewById(R.id.tv_title);
		mContentView = (TextView) findViewById(R.id.tv_content);
		mLeftButton = (Button) findViewById(R.id.bt_left);
		mRightButton = (Button) findViewById(R.id.bt_right);
	}
	
}









