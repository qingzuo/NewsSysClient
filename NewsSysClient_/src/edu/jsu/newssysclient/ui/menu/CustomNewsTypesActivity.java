package edu.jsu.newssysclient.ui.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.adapter.MyCustomGrideViewAdapter;
import edu.jsu.newssysclient.bean.TypeList;
import edu.jsu.newssysclient.dao.cache.Cache;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.ui.main.MyBaseActivity;
import edu.jsu.newssysclient.ui.widget.DragGridView;
import edu.jsu.newssysclient.ui.widget.DragGridView.OnChanageListener;
import edu.jsu.newssysclient.util.local.IntentUtil;

/**
 * 定制新闻类型界面
 * @author zuo
 *
 */
public class CustomNewsTypesActivity extends MyBaseActivity implements
		MyCustomGrideViewAdapter.DeleteCustomeItem {
	// 所以新闻类型及对应的id
	public static Map<String, Integer> ALLNEWSTYPE = new HashMap<String, Integer>();
	static {
		ALLNEWSTYPE.put("头条", 1);
		ALLNEWSTYPE.put("世界杯", 2);
		ALLNEWSTYPE.put("推荐", 3);
		ALLNEWSTYPE.put("娱乐", 4);
		ALLNEWSTYPE.put("体育", 5);
		ALLNEWSTYPE.put("财经", 6);
		ALLNEWSTYPE.put("科技", 7);
		ALLNEWSTYPE.put("图片", 8);
		ALLNEWSTYPE.put("轻松一刻", 9);
		ALLNEWSTYPE.put("汽车", 10);
		ALLNEWSTYPE.put("时尚", 11);
		ALLNEWSTYPE.put("张家界", 12);
		ALLNEWSTYPE.put("军事", 13);
		ALLNEWSTYPE.put("房产", 14);
		ALLNEWSTYPE.put("游戏精选", 15);
		ALLNEWSTYPE.put("电台", 16);
		ALLNEWSTYPE.put("聚合阅读", 17);
		ALLNEWSTYPE.put("论坛", 18);
		ALLNEWSTYPE.put("博客", 19);
		ALLNEWSTYPE.put("社会", 20);
		ALLNEWSTYPE.put("电影", 21);
		ALLNEWSTYPE.put("彩票", 22);
		ALLNEWSTYPE.put("NBA", 23);
		ALLNEWSTYPE.put("中超", 24);
		ALLNEWSTYPE.put("国际足球", 25);
		ALLNEWSTYPE.put("CBA", 26);
		ALLNEWSTYPE.put("手机", 27);
		ALLNEWSTYPE.put("数码", 28);
		ALLNEWSTYPE.put("移动互联", 29);
		ALLNEWSTYPE.put("原创", 30);
		ALLNEWSTYPE.put("家居旅游", 31);
		ALLNEWSTYPE.put("教育", 32);
		ALLNEWSTYPE.put("酒香", 33);
		ALLNEWSTYPE.put("暴雪游戏", 34);
		ALLNEWSTYPE.put("亲子", 35);
		ALLNEWSTYPE.put("读书", 36);
		ALLNEWSTYPE.put("葡萄酒", 37);
		ALLNEWSTYPE.put("情感", 38);
		ALLNEWSTYPE.put("国内", 39);
		ALLNEWSTYPE.put("国际", 40);
	}
	// 默认显示的新闻类型
	public static final String[] NEWSTYPESDEFUALTSHOW = new String[] { "头条","国内", "国际","娱乐","体育","财经","科技","时尚"};
	// 默认不显示的新闻类型
	public static final String[] NEWSTYPESDEFUALTHIDE = new String[] { "军事","社会", "论坛","推荐","博客","电影","NBA","中超","国际足球","CBA","手机","数码","原创","家居旅游","教育","情感" };

	private List<HashMap<String, Object>> dataSourceList = new ArrayList<HashMap<String, Object>>();
	private List<HashMap<String, Object>> dataSourceList2 = new ArrayList<HashMap<String, Object>>();
	private List<String> showTypes = new ArrayList<String>();
	private List<String> hideTypes = new ArrayList<String>();
	private DragGridView mDragGridView;
	private MyCustomGrideViewAdapter customAdapter;
	private SimpleAdapter mSimpleAdapter2;
	private Cache cache = new Cache();
	private String showNewsTypesKey = "CurrentNewsTypesCacheKey";
	private String hideNewsTypesKey = "OtherNewsTypesCacheKey";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_newstype_layout);

		mDragGridView = (DragGridView) findViewById(R.id.dragGridView);
		// 获取当前显示的所有新闻类型
		TypeList currentTypeslList = cache
				.get(showNewsTypesKey, TypeList.class);
		if (currentTypeslList != null && currentTypeslList.getTypes() != null
				&& currentTypeslList.getTypes().size() != 0) { // 有缓存
			for (String t : currentTypeslList.getTypes()) {
				showTypes.add(t);
			}
		} else { // 加载默认的
			for (int i = 0; i < NEWSTYPESDEFUALTSHOW.length; i++) {
				showTypes.add(NEWSTYPESDEFUALTSHOW[i]);
			}
		}
		AppLogger.i("初始化，showTypes：" + showTypes.toString());
		/*
		 * for (int i = 0; i < NEWSTYPESDEFUALTSHOW.length; i++) {
		 * showTypes.add(NEWSTYPESDEFUALTSHOW[i]); }
		 */

		customAdapter = new MyCustomGrideViewAdapter(this, showTypes);

		mDragGridView.setAdapter(customAdapter);

		mDragGridView.setOnChangeListener(new OnChanageListener() {

			@Override
			public void onChange(int from, int to) {
				String temp = showTypes.get(from);
				if (from < to) {
					for (int i = from; i < to; i++) {
						Collections.swap(showTypes, i, i + 1);
					}
				} else if (from > to) {
					for (int i = from; i > to; i--) {
						Collections.swap(showTypes, i, i - 1);
					}
				}
				showTypes.set(to, temp);
				AppLogger.i("移动：" + showTypes.toString());
				// 更新缓存
				//updateNewsTypeListCache(showNewsTypesKey, showTypes);

				customAdapter.notifyDataSetChanged();

			}
		});

		final GridView addGridView = (GridView) findViewById(R.id.gv_custome_newstype_add);
		// 获取当前隐藏的所有新闻类型
		TypeList hideTypeslList = cache.get(hideNewsTypesKey, TypeList.class);
		if (hideTypeslList != null && hideTypeslList.getTypes() != null
				&& hideTypeslList.getTypes().size() != 0) { // 有缓存
			for (String t : hideTypeslList.getTypes()) {
				hideTypes.add(t);
			}
		} else { // 加载默认的
			for (int i = 0; i < NEWSTYPESDEFUALTHIDE.length; i++) {
				hideTypes.add(NEWSTYPESDEFUALTHIDE[i]);
			}
		}
		AppLogger.i("初始化，hideTypes：" + hideTypes.toString());
		/*
		 * for (int i = 0; i < NEWSTYPESDEFUALTHIDE.length; i++) {
		 * hideTypes.add(NEWSTYPESDEFUALTHIDE[i]); }
		 */

		for (int i = 0; i < hideTypes.size(); i++) {
			HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
			itemHashMap.put("item_image",
					R.drawable.com_tencent_open_notice_msg_icon_big);
			itemHashMap.put("item_text", hideTypes.get(i));
			dataSourceList2.add(itemHashMap);
		}
		mSimpleAdapter2 = new SimpleAdapter(this, dataSourceList2,
				R.layout.grid_normal_item, new String[] { "item_image",
						"item_text" }, new int[] { R.id.item_image,
						R.id.item_text });
		addGridView.setAdapter(mSimpleAdapter2);
		addGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (showTypes.size() >= 15) {
					Toast.makeText(getApplicationContext(), "最多只能添加15个", 0)
							.show();
					return;
				}
				// add
				showTypes.add((String) dataSourceList2.get(position).get(
						"item_text"));
				AppLogger.i("添加，showTypes：" + showTypes.toString());
				// 更新缓存
				//updateNewsTypeListCache(showNewsTypesKey, showTypes);
				customAdapter.notifyDataSetChanged();

				// delete
				hideTypes.remove(position);
				AppLogger.i("添加，hidetypes：" + hideTypes.toString());
				// 更新缓存
				//updateNewsTypeListCache(hideNewsTypesKey, hideTypes);
				dataSourceList2.remove(position);
				mSimpleAdapter2.notifyDataSetChanged();
			}
		});
	}

	public void updateNewsTypeListCache(String key, List<String> types) {
		cache.put(key, new TypeList(types));
	}

	@Override
	public void deleteItem(int position) {
		HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
		itemHashMap.put("item_image",
				R.drawable.com_tencent_open_notice_msg_icon_big);
		itemHashMap.put("item_text", showTypes.get(position));
		hideTypes.add(showTypes.get(position));
		AppLogger.i("删除，hidetypes：" + hideTypes.toString());
		// 更新缓存
		//updateNewsTypeListCache(hideNewsTypesKey, hideTypes);
		dataSourceList2.add(itemHashMap);
		mSimpleAdapter2.notifyDataSetChanged();

		showTypes.remove(position);
		AppLogger.i("删除, showTypes：" + showTypes.toString());
		// 更新缓存
		//updateNewsTypeListCache(showNewsTypesKey, showTypes);
		customAdapter.notifyDataSetChanged();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.custome_newstype_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_sure:
			updateNewsTypeListCache(showNewsTypesKey, showTypes);
			updateNewsTypeListCache(hideNewsTypesKey, hideTypes);
			IntentUtil.go2AllTypeNews(this);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
