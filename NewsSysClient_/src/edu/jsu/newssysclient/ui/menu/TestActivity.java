package edu.jsu.newssysclient.ui.menu;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.ui.widget.CircleFlowIndicator;
import edu.jsu.newssysclient.ui.widget.ImageAdapter;
import edu.jsu.newssysclient.ui.widget.MyViewFlow;
import edu.jsu.newssysclient.ui.widget.ViewFlow;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * 测试界面，无功能
 * @author zuo
 *
 */
public class TestActivity extends Activity {
	ViewFlow viewFlow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		PullToRefreshListView mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_refresh_all_new_listview);
		
		View head = View.inflate(this, R.layout.newslist_top_gallery_layout, null);
		MyViewFlow viewFlow = (MyViewFlow) head.findViewById(R.id.viewflow);
		viewFlow.setAdapter(new ImageAdapter(this), 0);
        viewFlow.setmSideBuffer(4); //  
		CircleFlowIndicator indic = (CircleFlowIndicator) head.findViewById(R.id.viewflowindic);
		viewFlow.setFlowIndicator(indic);
		viewFlow.setTimeSpan(4500);
		viewFlow.setSelection(3 * 1000); // 设置初始位置
		viewFlow.startAutoFlowTimer(); // 启动自动播放
		
		
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		// 添加到ListView头部
		actualListView.addHeaderView(head);
		actualListView.setAdapter(new BaseAdapter() {
			String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "2", "3", "4", "5", "6", "7", "8", "9", "2", "3", "4", "5", "6", "7", "8", "9", "2", "3", "4", "5", "6", "7", "8", "9", "2", "3", "4", "5", "6", "7", "8", "9", "2", "3", "4", "5", "6", "7", "8", "9"};
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView t = new TextView(TestActivity.this);
				t.setText(arr[position]);
				return t;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return arr.length;
			}
		});
	}
}
