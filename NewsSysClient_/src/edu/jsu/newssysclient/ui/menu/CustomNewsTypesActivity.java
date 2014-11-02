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
 * �����������ͽ���
 * @author zuo
 *
 */
public class CustomNewsTypesActivity extends MyBaseActivity implements
		MyCustomGrideViewAdapter.DeleteCustomeItem {
	// �����������ͼ���Ӧ��id
	public static Map<String, Integer> ALLNEWSTYPE = new HashMap<String, Integer>();
	static {
		ALLNEWSTYPE.put("ͷ��", 1);
		ALLNEWSTYPE.put("���籭", 2);
		ALLNEWSTYPE.put("�Ƽ�", 3);
		ALLNEWSTYPE.put("����", 4);
		ALLNEWSTYPE.put("����", 5);
		ALLNEWSTYPE.put("�ƾ�", 6);
		ALLNEWSTYPE.put("�Ƽ�", 7);
		ALLNEWSTYPE.put("ͼƬ", 8);
		ALLNEWSTYPE.put("����һ��", 9);
		ALLNEWSTYPE.put("����", 10);
		ALLNEWSTYPE.put("ʱ��", 11);
		ALLNEWSTYPE.put("�żҽ�", 12);
		ALLNEWSTYPE.put("����", 13);
		ALLNEWSTYPE.put("����", 14);
		ALLNEWSTYPE.put("��Ϸ��ѡ", 15);
		ALLNEWSTYPE.put("��̨", 16);
		ALLNEWSTYPE.put("�ۺ��Ķ�", 17);
		ALLNEWSTYPE.put("��̳", 18);
		ALLNEWSTYPE.put("����", 19);
		ALLNEWSTYPE.put("���", 20);
		ALLNEWSTYPE.put("��Ӱ", 21);
		ALLNEWSTYPE.put("��Ʊ", 22);
		ALLNEWSTYPE.put("NBA", 23);
		ALLNEWSTYPE.put("�г�", 24);
		ALLNEWSTYPE.put("��������", 25);
		ALLNEWSTYPE.put("CBA", 26);
		ALLNEWSTYPE.put("�ֻ�", 27);
		ALLNEWSTYPE.put("����", 28);
		ALLNEWSTYPE.put("�ƶ�����", 29);
		ALLNEWSTYPE.put("ԭ��", 30);
		ALLNEWSTYPE.put("�Ҿ�����", 31);
		ALLNEWSTYPE.put("����", 32);
		ALLNEWSTYPE.put("����", 33);
		ALLNEWSTYPE.put("��ѩ��Ϸ", 34);
		ALLNEWSTYPE.put("����", 35);
		ALLNEWSTYPE.put("����", 36);
		ALLNEWSTYPE.put("���Ѿ�", 37);
		ALLNEWSTYPE.put("���", 38);
		ALLNEWSTYPE.put("����", 39);
		ALLNEWSTYPE.put("����", 40);
	}
	// Ĭ����ʾ����������
	public static final String[] NEWSTYPESDEFUALTSHOW = new String[] { "ͷ��","����", "����","����","����","�ƾ�","�Ƽ�","ʱ��"};
	// Ĭ�ϲ���ʾ����������
	public static final String[] NEWSTYPESDEFUALTHIDE = new String[] { "����","���", "��̳","�Ƽ�","����","��Ӱ","NBA","�г�","��������","CBA","�ֻ�","����","ԭ��","�Ҿ�����","����","���" };

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
		// ��ȡ��ǰ��ʾ��������������
		TypeList currentTypeslList = cache
				.get(showNewsTypesKey, TypeList.class);
		if (currentTypeslList != null && currentTypeslList.getTypes() != null
				&& currentTypeslList.getTypes().size() != 0) { // �л���
			for (String t : currentTypeslList.getTypes()) {
				showTypes.add(t);
			}
		} else { // ����Ĭ�ϵ�
			for (int i = 0; i < NEWSTYPESDEFUALTSHOW.length; i++) {
				showTypes.add(NEWSTYPESDEFUALTSHOW[i]);
			}
		}
		AppLogger.i("��ʼ����showTypes��" + showTypes.toString());
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
				AppLogger.i("�ƶ���" + showTypes.toString());
				// ���»���
				//updateNewsTypeListCache(showNewsTypesKey, showTypes);

				customAdapter.notifyDataSetChanged();

			}
		});

		final GridView addGridView = (GridView) findViewById(R.id.gv_custome_newstype_add);
		// ��ȡ��ǰ���ص�������������
		TypeList hideTypeslList = cache.get(hideNewsTypesKey, TypeList.class);
		if (hideTypeslList != null && hideTypeslList.getTypes() != null
				&& hideTypeslList.getTypes().size() != 0) { // �л���
			for (String t : hideTypeslList.getTypes()) {
				hideTypes.add(t);
			}
		} else { // ����Ĭ�ϵ�
			for (int i = 0; i < NEWSTYPESDEFUALTHIDE.length; i++) {
				hideTypes.add(NEWSTYPESDEFUALTHIDE[i]);
			}
		}
		AppLogger.i("��ʼ����hideTypes��" + hideTypes.toString());
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
					Toast.makeText(getApplicationContext(), "���ֻ�����15��", 0)
							.show();
					return;
				}
				// add
				showTypes.add((String) dataSourceList2.get(position).get(
						"item_text"));
				AppLogger.i("��ӣ�showTypes��" + showTypes.toString());
				// ���»���
				//updateNewsTypeListCache(showNewsTypesKey, showTypes);
				customAdapter.notifyDataSetChanged();

				// delete
				hideTypes.remove(position);
				AppLogger.i("��ӣ�hidetypes��" + hideTypes.toString());
				// ���»���
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
		AppLogger.i("ɾ����hidetypes��" + hideTypes.toString());
		// ���»���
		//updateNewsTypeListCache(hideNewsTypesKey, hideTypes);
		dataSourceList2.add(itemHashMap);
		mSimpleAdapter2.notifyDataSetChanged();

		showTypes.remove(position);
		AppLogger.i("ɾ��, showTypes��" + showTypes.toString());
		// ���»���
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
