package edu.jsu.newssysclient.adapter;

import java.io.InputStream;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;

import edu.jsu.newssysclient.MyApplication;
import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.bean.Item;
import edu.jsu.newssysclient.ui.menu.AboutActivity_;
import edu.jsu.newssysclient.ui.util.ImageViewUtil;
import edu.jsu.newssysclient.util.local.IntentUtil;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 头部图片滚动适配器
 * @author zuo
 *
 */
public class MyGalleryAdapter extends BaseAdapter {
//	public String[] itemImages= new String[] { "http://img0.bdstatic.com/img/image/shouye/sylmaxgh.jpg",
//		"http://img0.bdstatic.com/img/image/shouye/gxxhz-11578209641.jpg", "http://img0.bdstatic.com/img/image/shouye/mnwm-9474280561.jpg"};
//	public String[] itemDescr = new String[] {"浪漫光绘摄影", "奇葩童年照！你也这么拍过吗？", "唯美"};
	public String[] itemImages= new String[] { "http://img0.bdstatic.com/img/image/shouye/sylmaxgh.jpg"};
		public String[] itemDescr = new String[] {"浪漫光绘摄影"};

	private List<Item> mDatas;
	private Context mContext;
	private int width;
	private int height;

	public MyGalleryAdapter(Context context, List<Item> datas) {
		this.mContext = context;
		this.mDatas = datas;
		
		Display mDisplay= ((Activity) mContext).getWindowManager().getDefaultDisplay();
		width= mDisplay.getWidth();
		height = dip2px(220);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size()<=1? 1:Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int i) {
		// TODO Auto-generated method stub
		return i;
	}

	@Override
	public long getItemId(int i) {
		// TODO Auto-generated method stub
		return i;
	}

	@Override
	public View getView(final int i, View view, ViewGroup viewgroup) {
		if (view == null) {
			view = View.inflate(mContext, R.layout.newslist_top_gallery_item_layout, null);
			view.setLayoutParams(new Gallery.LayoutParams(width,height));
		}
		
		ImageView imageView = ViewHolder.get(view, R.id.newslist_top_image);//(ImageView) view.findViewById(R.id.newslist_top_image);

		TextView descr = ViewHolder.get(view, R.id.newslist_top_image_descr);//(TextView) view.findViewById(R.id.newslist_top_image_descr);
		TextView title = ViewHolder.get(view, R.id.newslist_top_image_title);//(TextView) view.findViewById(R.id.newslist_top_image_title);
		TextView position = ViewHolder.get(view, R.id.newslist_top_image_position);//(TextView) view.findViewById(R.id.newslist_top_image_position);
		
		if (mDatas.size() <= 0) {	// 显示默认的一张图片
//			title.setText(itemDescr[i%itemImages.length]);
			title.setText("关于我们");
			position.setText((i%itemImages.length+1)+"/"+itemImages.length);
			ImageLoader.getInstance().displayImage(itemImages[i%itemImages.length], imageView);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					IntentUtil.go2Activity(mContext, AboutActivity_.class);
				}
			});
		} else if (mDatas.size() == 1) {	// 显示一个新闻
			int length = mDatas.size();
			Item curItem = mDatas.get(i%length);
			title.setText(curItem.getTitle());
			position.setText((i%length+1) + "/" + length);
			ImageLoader.getInstance().displayImage(curItem.getImageurl(), imageView);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					IntentUtil.go2ShowNewsActivity(mContext, mDatas.get(i%mDatas.size()).getId());
				}
			});
		} else {	// 显示多张图片
			int length = mDatas.size();
			Item curItem = mDatas.get(i%length);
			title.setText(curItem.getTitle());
			position.setText((i%length+1) + "/" + length);
			ImageLoader.getInstance().displayImage(curItem.getImageurl(), imageView);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					IntentUtil.go2ShowNewsActivity(mContext, mDatas.get(i%mDatas.size()).getId());
				}
			});
		}
		
		return view;
	}

	// 读取一个资源图片
	public Bitmap readBitmap(Context context, int resId) {
		BitmapFactory.Options options = new Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, options);
	}
	
	// dip 转 px
	public int dip2px(float dipValue){
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int)(dipValue * scale + 0.5f);
	}

}
