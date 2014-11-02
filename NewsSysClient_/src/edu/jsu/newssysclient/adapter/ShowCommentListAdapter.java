package edu.jsu.newssysclient.adapter;

import java.util.List;
import java.util.Random;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import edu.jsu.newssysclient.MyApplication;
import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.URLHelper;
import edu.jsu.newssysclient.bean.Follow;
import edu.jsu.newssysclient.bean.Item;
import edu.jsu.newssysclient.bean.NewsImage;
import edu.jsu.newssysclient.bean.NewsInfo;
import edu.jsu.newssysclient.util.local.StringUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 为新闻列表填充数据
 * @author zuo
 *
 */
public class ShowCommentListAdapter extends BaseAdapter {
	private List<Follow> datas;
	private ImageLoader imageLoader;
	DisplayImageOptions options;  
	
	public ImageLoader getImageLoader() {
		return imageLoader;
	}
	
	public ShowCommentListAdapter(List<Follow> datas){
		this.datas = datas;
		imageLoader = ImageLoader.getInstance();
		
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片  
		.showImageOnFail(R.drawable.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
		.displayer(new RoundedBitmapDisplayer(30))
		.cacheInMemory()
		.cacheOnDisc()
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();//构建完成
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
	        convertView = LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.show_comment_listitem, null);
	    }
		// 获取控件
		ImageView head = ViewHolder.get(convertView, R.id.iv_comment_item_head);
		TextView name = (TextView) ViewHolder.get(convertView, R.id.tv_comment_item_username);
		TextView time = (TextView) ViewHolder.get(convertView, R.id.tv_comment_item_time);
		TextView content = (TextView) ViewHolder.get(convertView, R.id.tv_comment_item_content);
		
		// 设置值
		Follow f = datas.get(position);
		name.setText(f.getFollowuser());
		/*if (StringUtil.isEmpty(f.getFollowtime())) {
			time.setText("不久前");
		} else {
			time.setText(f.getFollowtime());
		}*/
		time.setText(f.getFollowtime());
		content.setText(f.getFollowcontent());
		
		// 异步加载图片
		head.setImageResource(R.drawable.ic_launcher);
		String uri = f.getFollowuserhead();
		if (!StringUtil.isEmpty(uri)) imageLoader.displayImage(URLHelper.SERVERURL+uri, head, options);
		
		return convertView;
	}

}
