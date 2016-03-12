package com.rose.stockmanagement.updatemgr;

import java.io.File;

//import com.pearl.hbmsn.R;
//import com.pearl.hbmsn.ui.ALoginView;
//import com.pearl.hbmsn.ui.dialog.CustomDialogYes;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.View;

import com.rose.stockmanagement.R;
import com.rose.stockmanagement.customecontrol.*;
import com.rose.stockmanagement.*;

/**
 * @author Pearl
 * @version 1.0
 * @see: Application�� ������Ʈ�� �����ϴ� Ŭ��. ���� Application�� ��Ű���Ǻ��� 
 * ������ ��ϵǿ� �ִ� ��Ű���Ǻ��� ���Ͽ� �ٸ� ��� ������Ʈ�� �����Ѵ�.
 */

public class UpdateMgr {

	private Context m_Context = null;
	
	private String m_DownloadUrl = "";//Ŭ���̾�ƮAPK��Ű���� ���� �ٿ�ε������� ���� URL
	private String m_LocalVersionName = "";//local��Ű���Ǻ��̸�����
	private String m_ServerVersionName = "";//Server��Ű���Ǻ� �̸�����
	
	private DownloadApkAsync _DownloadApkAsync = null;
	private GetApkVersionAsync _GetVersionAsync = null;
	private CustomDialogYes updateDialog = null;
	public static UpdateMgr instance = null;
	
	
	public UpdateMgr(Context context) {
		m_Context = context;
		if(instance == null)
			instance = this;
	}
	//Application�� local�Ǻ��� Server�Ǻ��� �� ���Ѵ�.
	public void init(){
		m_LocalVersionName = getLocalVersion();
		_GetVersionAsync = new GetApkVersionAsync(m_Context);
		_GetVersionAsync.execute();
	}
	
	public void SetServerVersion(String serverVersion, String downloadUrl){
		
		m_ServerVersionName = serverVersion;
		m_DownloadUrl = downloadUrl;
		
		View.OnClickListener yesClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View yesButton) {
				// ���� ������Ʈ�ϱ⸦ �����Ͽ��ٸ�
				updateDialog.dismiss();
				_DownloadApkAsync = new DownloadApkAsync(m_Context);
				_DownloadApkAsync.execute(m_DownloadUrl);
			}
		};
		View.OnClickListener noClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View noButton) {
				updateDialog.dismiss();
				//Login.Instance.setNetwork();//���� ������Ʈ�ϱ⸦ �����Ͽ��ٸ� ��Ʈ��ũ������ �����Ѵ�.
			}
		};
		
		if(m_ServerVersionName.trim().contentEquals(m_LocalVersionName)){
			//Login.Instance.setNetwork();//���� ���� ������ ������ ������ ���ٸ� ��Ʈ��ũ������ �����Ѵ�.
		}
		else{//���� ��������� ������ ������ �ٸ��ٸ� ������Ʈ�� �����ϴ� ��ȭâ�� ���� ������� ���ÿ� ���� ���� ������Ʈ�� �����ϴ��� ��Ҹ� �����Ѵ�.
			String title = m_Context.getString(R.string.msg_notification);
			String msg = m_Context.getString(R.string.msg_update);
			updateDialog = new CustomDialogYes(m_Context, title, msg, yesClickListener, noClickListener);
			updateDialog.show();
		}
		
	}
	//�����ε���� ������ ��θ� �����ϰ� ������Ʈ�� �����Ѵ�.
	public void UpdateApk(String path){
		
		
		try{
			
			if(path == null){//���������� ������ ���� ��� ���ϰ�ΰ� null�� �ȴ�.�̶��� ������Ű���� �����Ѵ�.
				//Login.Instance.setNetwork();
			}
			else{
				File file = new File(path);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
				m_Context.startActivity(intent);
				Destory();
				Login.Instance.finish();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private String getLocalVersion(){
		String strVersionName = "";
		try {
			PackageInfo info = m_Context.getPackageManager().getPackageInfo(m_Context.getPackageName(), 0);
			strVersionName = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return strVersionName;
	}
	
	public void Destory(){
		
		if(_DownloadApkAsync != null)
			_DownloadApkAsync = null;
		if(_GetVersionAsync != null)
			_GetVersionAsync = null;
		if(instance != null)
			instance = null;
		if(updateDialog != null)
			updateDialog = null;
		
	}
}
