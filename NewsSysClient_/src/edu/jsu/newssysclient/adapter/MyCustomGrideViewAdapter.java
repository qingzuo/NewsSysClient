package edu.jsu.newssysclient.adapter;

import java.util.List;

import edu.jsu.newssysclient.R;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 定制新闻类型适配器
 * @author zuo
 *
 */
public class MyCustomGrideViewAdapter extends BaseAdapter {
	private List<String> mTypes;	// 所有类型
	private Context mContext;
	private MyCustomGrideViewAdapter mAdapter;

	public MyCustomGrideViewAdapter(Context context, List<String> types) {
		mTypes = types;
		mContext = context;
		mAdapter = this;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mTypes.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.grid_custom_item,
					null);
		}

		// 获取控件
		ImageView imageView = ViewHolder.get(convertView, R.id.item_image);
		TextView textView = ViewHolder.get(convertView, R.id.item_text);
		ImageButton imageButton = ViewHolder.get(convertView, R.id.item_delete);
		
		// 设置值
		imageView.setImageResource(R.drawable.com_tencent_open_notice_msg_icon_big);
		textView.setText(mTypes.get(position));
		imageButton.setImageResource(R.drawable.ic_custome_delete);
		imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 判断是否满足要求
				//Toast.makeText(mContext, position + "被点击了", 0).show();
				/*mTypes.remove(position);
				mAdapter.notifyDataSetChanged();*/
				if (mTypes.size() <= 5) {
					Toast.makeText(mContext, "至少保留5个", 0).show();
					return ;
				}
				((DeleteCustomeItem) mContext).deleteItem(position);
			}
		});

		return convertView;
	}

	/**
	 * 接口，用于删除特点条目
	 * @author zuo
	 *
	 */
	public interface DeleteCustomeItem{
		void deleteItem(int position);
	}

}
