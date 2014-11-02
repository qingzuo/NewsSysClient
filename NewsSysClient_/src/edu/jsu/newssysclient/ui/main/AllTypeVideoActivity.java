package edu.jsu.newssysclient.ui.main;

import java.util.List;

import com.viewpagerindicator.TabPageIndicator;

import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.fragment.NewsListFragment;
import edu.jsu.newssysclient.fragment.VideoNewsListFragment;
import edu.jsu.newssysclient.ui.main.AllTypeNewsActivity.MyViewPagerAdapter;
import edu.jsu.newssysclient.ui.menu.CustomNewsTypesActivity;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

/**
 * 所有视频界面
 * @author zuo
 *
 */
public class AllTypeVideoActivity extends FragmentActivity {
    private static final String[] VIDEOTYPE = new String[] {"热门", "娱乐", "搞笑", "精品"};

	private TabPageIndicator indicator;	// 类型tab
	public ViewPager mViewPager;
    private int mPagerOffsetPixels;
    private int mPagerPosition;	// 当前位置

	@Override
	protected void onCreate(Bundle arg0) {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(arg0);
		setContentView(R.layout.alltype_news_layout);
		
        FragmentPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager(), VIDEOTYPE);
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(adapter);

        indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPagerPosition = position;
                mPagerOffsetPixels = positionOffsetPixels;
            }
            
        });
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
	
	/**
	 * 视频适配器
	 * @author zuo
	 *
	 */
    class MyViewPagerAdapter extends FragmentPagerAdapter {
//    	private String[] CONTENT;
    	private String[] types;
    	
        public MyViewPagerAdapter(FragmentManager fm, String[] types) {
            super(fm);
            this.types = types;
        }

        @Override
        public Fragment getItem(int position) {
        	String mType = types[position];
        	
        	return VideoNewsListFragment.newInstance(mType);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return " " + types[position % types.length] + " ";
        }

        @Override
        public int getCount() {
            return types.length;
        }
    }
}
