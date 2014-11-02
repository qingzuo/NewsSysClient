package edu.jsu.newssysclient.ui.main;

import org.androidannotations.annotations.EActivity;

import android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Acitivty基类
 * 	功能：
 * 		定义公用方法
 * @author zuo
 *
 */
public class MyBaseActivity extends Activity {
	private ProgressDialog progressDialog;
	private boolean destroyed = false;
	protected Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mContext = this;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		destroyed = true;
	}

	/**
	 * 显示等待窗口
	 */
	public void showLoadingProgressDialog() {
		this.showLoadingProgressDialog("请等待...");
	}

	/**
	 * 显示等待窗口
	 * @param message	自定义提示信息
	 */
	public void showLoadingProgressDialog(String message) {
		if (this.progressDialog == null) {
			this.progressDialog = new ProgressDialog(this);
			this.progressDialog.setIndeterminate(true);
		}

		this.progressDialog.setMessage(message);
		this.progressDialog.show();
	}

	/**
	 * 关闭等待窗口
	 */
	public void dismissProgressDialog() {
		if (this.progressDialog != null && !destroyed) {
			this.progressDialog.dismiss();
		}
	}
}
