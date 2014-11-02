package edu.jsu.newssysclient.ui.main;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.bean.ImageNews;
import edu.jsu.newssysclient.bean.ItemImages;
import edu.jsu.newssysclient.bean.NewsImage;
import edu.jsu.newssysclient.dao.cache.Cache;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.fragment.ImageFragment;
import edu.jsu.newssysclient.fragment.NewsListFragment;
import edu.jsu.newssysclient.util.local.StringUtil;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 图片新闻界面,未使用
 * @author zuo
 *
 */
@EActivity(R.layout.image_news_layout)
public class ImageNewsActivity extends FragmentActivity {
	private List<ImageNews> mImageNewsList = new ArrayList<ImageNews>();
	private String mNewsId;
	private Cache cache = new Cache();

	@ViewById
	ViewPager imagePager;
	@ViewById
	TextView imageNewsTitle;
	@ViewById
	TextView imageNewsPosition;
	@ViewById
	TextView imageNewsContent;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		// 获取数据
		mNewsId = getIntent().getStringExtra(ShowNewsActivity.SHOWNEWSACTIVITYNEWSID);
		AppLogger.i("新闻id：" + mNewsId);
		if (mNewsId == null) {
			return ;
		}
		// 缓存数
		ItemImages itemImages = cache.get(Cache.NEWSIMAGENEWSLISTBYNEWSID+mNewsId, ItemImages.class);
		if (itemImages != null && itemImages.getImages() != null) {
			AppLogger.i("图片新闻类，图片大小：" + itemImages.getImages().size());
			mImageNewsList = itemImages.getImages();
		} else {
			return ;
		}
		
		imagePager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));

		imageNewsTitle.setText(mImageNewsList.get(0).getImageNewsTitle());
		imageNewsPosition.setText("1/" + mImageNewsList.size());
		imageNewsContent.setText(mImageNewsList.get(0).getImageNewsDescr());
		/**
		 * 滑动事件
		 */
		imagePager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				AppLogger.i("onPageSelected");
				imageNewsTitle.setText(mImageNewsList.get(arg0).getImageNewsTitle());
				imageNewsPosition.setText((arg0+1) +"/" + mImageNewsList.size());
				imageNewsContent.setText(mImageNewsList.get(arg0).getImageNewsDescr());
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				//AppLogger.i("onPageScrolled");
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				//AppLogger.i("onPageScrollStateChanged");
				
			}
		});
	}

	/**
	 * 视频器
	 * @author zuo
	 *
	 */
    class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
//        	return ImageFragment.newInstance(mImageNewsList.get(position));
        	return ImageFragment.newInstance(mImageNewsList.get(position));
        }

        @Override
        public int getCount() {
            return mImageNewsList.size();//mImageNewsList.size();
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_news_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_collect:	// 点击收藏
			break;
		case R.id.action_share:	// 分享
			break;

		default:
			break;
		}
		
		return true;
	}
}
