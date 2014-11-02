/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package edu.jsu.newssysclient.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import ru.truba.touchgallery.GalleryWidget.BasePagerAdapter.OnItemChangeListener;
import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;
import ru.truba.touchgallery.GalleryWidget.UrlPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.androidannotations.annotations.ViewById;

import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.bean.ImageNews;
import edu.jsu.newssysclient.bean.ItemImages;
import edu.jsu.newssysclient.dao.cache.Cache;
import edu.jsu.newssysclient.debug.AppLogger;

/**
 * 图片新闻activity
 * @author zuo
 *
 */
public class GalleryUrlActivity extends MyBaseActivity {
	private List<ImageNews> mImageNewsList = new ArrayList<ImageNews>();	// 图片列表
	private String mNewsId;				// 新闻id
	private Cache cache = new Cache();	// 缓存
	
	TextView imageNewsTitle;	// 新闻标题
	TextView imageNewsPosition;	// 新闻位置导航
	TextView imageNewsContent;	// 新闻描述

	private GalleryViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_galleryurl);
		imageNewsTitle = (TextView) findViewById(R.id.imageNewsTitle);
		imageNewsPosition = (TextView) findViewById(R.id.imageNewsPosition);
		imageNewsContent = (TextView) findViewById(R.id.imageNewsContent);
		
		// 获取数据
		mNewsId = getIntent().getStringExtra(ShowNewsActivity.SHOWNEWSACTIVITYNEWSID);
		AppLogger.i("新闻id：" + mNewsId);
		if (mNewsId == null) {
			return ;
		}
		// 获取缓存数据
		ItemImages itemImages = cache.get(Cache.NEWSIMAGENEWSLISTBYNEWSID+mNewsId, ItemImages.class);
		if (itemImages != null && itemImages.getImages() != null) {
			AppLogger.i("图片新闻类，图片大小：" + itemImages.getImages().size());
			mImageNewsList = itemImages.getImages();
		} else {
			return ;
		}
		
		// 填充数据
		List<String> items = new ArrayList<String>();
		for (ImageNews i : mImageNewsList) {
			items.add(i.getImageNewsUrl());
		}
		
		imageNewsTitle.setText(itemImages.getTitle());

		// 适配界面
		final int max  = mImageNewsList.size();
		UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items);
		pagerAdapter.setOnItemChangeListener(new OnItemChangeListener() {
			@Override
			public void onItemChange(int currentPosition) {
				// 改变序号
				imageNewsPosition.setText( (currentPosition+1) + "/" + max);
				// 改变描述
				imageNewsContent.setText(mImageNewsList.get(currentPosition).getImageNewsDescr());
			}
		});

		// 设置图片视频器
		mViewPager = (GalleryViewPager) findViewById(R.id.viewer);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(pagerAdapter);

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