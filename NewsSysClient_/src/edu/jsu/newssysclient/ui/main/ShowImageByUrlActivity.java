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
 * 显示一张图片界面
 * @author zuo
 *
 */
public class ShowImageByUrlActivity extends MyBaseActivity {
	public static final String IMAGEURL = "edu.jsu.newssysclient.ui.main.ShowImageByUrlActivity.IMAGEURL";
	private GalleryViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showimageurl);
		
		// 获取url数据
		String url = getIntent().getStringExtra(ShowImageByUrlActivity.IMAGEURL);
		AppLogger.i("image url：" + url);
		if (url == null) {
			return ;
		}
		
		// 配置参数
		List<String> items = new ArrayList<String>();
		items.add(url);
		
		// 创建适配器
		UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items);

		// 绑定数据
		mViewPager = (GalleryViewPager) findViewById(R.id.viewer);
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setAdapter(pagerAdapter);

	}
	
}