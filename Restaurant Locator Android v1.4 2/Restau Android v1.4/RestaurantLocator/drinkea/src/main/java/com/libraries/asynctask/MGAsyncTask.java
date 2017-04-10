package com.libraries.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class MGAsyncTask extends AsyncTask<Void, Void, String> {
	public ProgressDialog dialog;
	public Activity activity;
	public Object object;
	
	public int tag = 0;
	public int listTag = 0;
	
	OnMGAsyncTaskListener mCallback;
	
	public interface OnMGAsyncTaskListener {
        public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask);
        public void onAsyncTaskProgressUpdate(MGAsyncTask asyncTask);
        public void onAsyncTaskPostExecute(MGAsyncTask asyncTask);
        public void onAsyncTaskPreExecute(MGAsyncTask asyncTask);
    }
	
	public void setMGAsyncTaskListener(OnMGAsyncTaskListener listener) {
		try {
            mCallback = (OnMGAsyncTaskListener) listener;
        } catch (ClassCastException e) {
            throw new ClassCastException(this.toString() + "Did not implement OnMGAsyncTaskListener");
        }
	}
	
	public MGAsyncTask(Activity activity) {
		this.activity = activity;
	}
	
	public void startAsyncTask() {
		this.execute();
	}
	
	@Override
	protected String doInBackground(Void... params)  {
		mCallback.onAsyncTaskDoInBackground(this);
		return "";
	}
	
	@Override
	protected void onPostExecute(String result)  {
		// execution of result of Long time consuming operation. parse json data
		dialog.dismiss();
		mCallback.onAsyncTaskPostExecute(this);
	}

	@Override
	protected void onPreExecute() {
		// Things to be done before execution of long running operation. For example showing ProgessDialog
		dialog = ProgressDialog.show(activity, "", "Loading...", true);
		mCallback.onAsyncTaskPreExecute(this);
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// Things to be done while execution of long running operation is in progress. For example updating ProgessDialog
		mCallback.onAsyncTaskProgressUpdate(this);
	}
	
	public void setMessage(String message)  {
		dialog.setMessage(message);
	}
	
	public void setTitle(String title)  {
		dialog.setTitle(title);
	}
	
	public void setIcon(int resId)  {
		dialog.setIcon(resId);
	}
		
}
