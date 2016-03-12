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
 * @see: Application의 업데이트를 관리하는 클라스. 현재 Application의 패키지판본과 
 * 서버에 등록되여 있는 패키지판본을 비교하여 다른 경우 업데이트를 진행한다.
 */

public class UpdateMgr {

	private Context m_Context = null;
	
	private String m_DownloadUrl = "";//클라이언트APK패키지에 대한 다운로드페지에 대한 URL
	private String m_LocalVersionName = "";//local패키지판본이름정보
	private String m_ServerVersionName = "";//Server패키지판본 이름정보
	
	private DownloadApkAsync _DownloadApkAsync = null;
	private GetApkVersionAsync _GetVersionAsync = null;
	private CustomDialogYes updateDialog = null;
	public static UpdateMgr instance = null;
	
	
	public UpdateMgr(Context context) {
		m_Context = context;
		if(instance == null)
			instance = this;
	}
	//Application의 local판본과 Server판본을 얻어서 비교한다.
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
				// 만일 업데이트하기를 수락하였다면
				updateDialog.dismiss();
				_DownloadApkAsync = new DownloadApkAsync(m_Context);
				_DownloadApkAsync.execute(m_DownloadUrl);
			}
		};
		View.OnClickListener noClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View noButton) {
				updateDialog.dismiss();
				//Login.Instance.setNetwork();//만일 업데이트하기를 거절하였다면 네트워크설정을 진행한다.
			}
		};
		
		if(m_ServerVersionName.trim().contentEquals(m_LocalVersionName)){
			//Login.Instance.setNetwork();//만일 현재 버전이 서버의 버전과 같다면 네트워크설정을 진행한다.
		}
		else{//만일 현재버전과 서버의 버전이 다르다면 업데이트를 문의하는 대화창을 띄우고 사용자의 선택에 따라 다음 업데이트를 진행하던가 취소를 진행한다.
			String title = m_Context.getString(R.string.msg_notification);
			String msg = m_Context.getString(R.string.msg_update);
			updateDialog = new CustomDialogYes(m_Context, title, msg, yesClickListener, noClickListener);
			updateDialog.show();
		}
		
	}
	//다은로드받은 파일의 경로를 설정하고 업데이트를 시작한다.
	public void UpdateApk(String path){
		
		
		try{
			
			if(path == null){//서버페지의 응답이 없는 경우 파일경로가 null이 된다.이때는 기정패키지를 실행한다.
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
