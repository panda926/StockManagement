package com.rose.stockmanagement.updatemgr;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.rose.stockmanagement.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class DownloadApkAsync extends AsyncTask<String, Integer, String> {

	private ProgressDialog mDlg;
	private Context mContext;
	final int maxTag = 1;
	final int progressTag = 2;
	
	public DownloadApkAsync(Context context) {
		mContext = context;
	}
	
	@Override
	protected void onPreExecute() {
		mDlg = new ProgressDialog(mContext);
		mDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mDlg.setCanceledOnTouchOutside(false);
		mDlg.setTitle(R.string.msg_downloading);
		mDlg.show();

		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		int count = 0;
		File file = null;
		String filePath = null;
		
		try {
			String sdCard = Environment.getExternalStorageState();
			
			if(!sdCard.equals(Environment.MEDIA_MOUNTED)){
				//SD카드가 마운트되여있지 않음
				file = Environment.getRootDirectory();
			}
			else{
				file = Environment.getExternalStorageDirectory();
			}
			
			String dir = file.getAbsolutePath() + "/BB";
			file = new File(dir);
			if(!file.exists()){
				file.mkdirs();
			}
			
			URL url = new URL(arg0[0].toString());
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(30000);
			connection.connect();

			int lenghtOfFile = connection.getContentLength();
			Log.d("ANDRO_ASYNC", "Length of file: " + lenghtOfFile);
			publishProgress(maxTag, lenghtOfFile / 1000);
			
			InputStream input = new BufferedInputStream(url.openStream());
			filePath = dir + "/" + com.rose.stockmanagement.Common._ApkName;
			OutputStream output = new FileOutputStream(filePath);

			byte data[] = new byte[1024];
			long total = 0;
			while ((count = input.read(data)) != -1) {
				output.write(data, 0, count);
				total += count;
				publishProgress(progressTag, (int)total/1000);
			}

			output.flush();
			output.close();
			input.close();
			
		} catch (Exception e) {
			//e.printStackTrace();
		} 
		return filePath;
	}

	@Override
	protected void onPostExecute(String filePath) {
		mDlg.dismiss();
		UpdateMgr.instance.UpdateApk(filePath);
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		if(progress[0] == maxTag){
			mDlg.setMax(progress[1]);
		}
		else{
			mDlg.setProgress(progress[1]);
		}
		
		super.onProgressUpdate(progress);
	}
}
