package edu.jsu.newssysclient.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import edu.jsu.newssysclient.MyApplication;
import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.bean.ImageNews;
import edu.jsu.newssysclient.bean.Item;
import edu.jsu.newssysclient.bean.ItemImages;
import edu.jsu.newssysclient.bean.NewsImage;
import edu.jsu.newssysclient.bean.NewsInfo;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.ui.main.ShowNewsActivity;
import edu.jsu.newssysclient.ui.widget.BetterGallery;
import edu.jsu.newssysclient.ui.widget.CircleFlowIndicator;
import edu.jsu.newssysclient.ui.widget.ImageAdapter;
import edu.jsu.newssysclient.ui.widget.ViewFlow;
import edu.jsu.newssysclient.util.local.IntentUtil;
import edu.jsu.newssysclient.util.local.StringUtil;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 为新闻列表填充数据
 * @author zuo
 *
 */
public class ShowNewsListAdapter extends BaseAdapter {
	private Context mContext ;
	private List<Item> datas;
	private List<Item> galleryList;
	private boolean mBusy = false;
	private ImageLoader imageLoader;
	DisplayImageOptions options;  
	
	public ImageLoader getImageLoader() {
		return imageLoader;
	}
	
	public ShowNewsListAdapter(Context context, List<Item> datas, List<Item> galleryList){
		mContext = context;
		this.datas = datas;
		this.galleryList = galleryList;
		
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片  
		.showImageOnFail(R.drawable.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
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
		boolean flag = isCommonNews(datas.get(position));
		if (convertView == null) {
			if (flag) {	// 普通新闻
	        	convertView = LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.show_news_list_item_layout, null);
	        } else {	// 图片新闻
	        	convertView = LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.image_news_list_item_layout, null);
	        }
	    }
		
		if (flag) {	// 普通新闻
			// 如果原来的布局是图片新闻布局，需重新布局
			if (ViewHolder.get(convertView, R.id.iv_news_logo) == null) {
				convertView = LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.show_news_list_item_layout, null);
			}
			ImageView logo = (ImageView) ViewHolder.get(convertView, R.id.iv_news_logo);
			TextView title = (TextView) ViewHolder.get(convertView, R.id.tv_news_title);
			TextView intro = (TextView) ViewHolder.get(convertView, R.id.tv_news_intro);
			TextView followup = (TextView) ViewHolder.get(convertView, R.id.tv_news_followup);
			
			Item descr = datas.get(position);
			
			title.setText(descr.getTitle());
			// 根据描述内容截取
			int maxLength = 35;
			String mDescr = descr.getDescr();
			if (mDescr!=null && mDescr.length() < maxLength) {
				maxLength = mDescr.length();
			}
			if (mDescr != null) {
				intro.setText(mDescr.subSequence(0, maxLength));
			}
			int t = descr.getCommenttime();
			if (t != 0) {
				followup.setText(""+t+"条评论");
			} else{ 
				t = descr.getBrowsetime();
				followup.setText(""+t+"次浏览");
			}
			//followup.setVisibility(View.GONE);
			
			//logo.setImageResource(DataInfo.Images[position%DataInfo.Images.length]);
			// 异步加载图片
			logo.setImageResource(R.drawable.ic_launcher);
			ImageSize targetSize = new ImageSize(120, 80);
			if (StringUtil.isEmpty(descr.getImageurl())) {
				logo.setVisibility(View.GONE);
			} else {
				logo.setVisibility(View.VISIBLE);
				imageLoader.displayImage(descr.getImageurl(), logo);
			}
		} else {	// 图片新闻
			if (ViewHolder.get(convertView, R.id.tv_image_news_title) == null) {
				convertView = LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.image_news_list_item_layout, null);
			}
			ItemImages itemImages = (ItemImages) datas.get(position);
			
			TextView title = (TextView) ViewHolder.get(convertView, R.id.tv_image_news_title);
			TextView follow = (TextView) ViewHolder.get(convertView, R.id.tv_image_news_follow);
			ImageView image01 = (ImageView) ViewHolder.get(convertView, R.id.image_news_image01);
			ImageView image02 = (ImageView) ViewHolder.get(convertView, R.id.image_news_image02);
			ImageView image03 = (ImageView) ViewHolder.get(convertView, R.id.image_news_image03);
			
			title.setText(itemImages.getTitle());
			int t = itemImages.getCommenttime();
			if (t != 0) {
				follow.setText(""+t+"条评论");
			} else{ 
				t = itemImages.getBrowsetime();
				follow.setText(""+t+"次浏览");
			}
			
			// 图片
			List<ImageNews> imageNewsList= itemImages.getImages();
			if (imageNewsList != null) {
				imageLoader.displayImage(imageNewsList.get(0).getImageNewsUrl(), image01);
				imageLoader.displayImage(imageNewsList.get(1).getImageNewsUrl(), image02);
				imageLoader.displayImage(imageNewsList.get(2).getImageNewsUrl(), image03);
			}
		}
		
		return convertView;
	}

	/**
	 * 判断是普通新闻还是图片新闻
	 * @param item 
	 * @return
	 */
	private boolean isCommonNews(Item item) {
		if (item instanceof ItemImages) {
			return false;
		}
		return true;
	}

}
