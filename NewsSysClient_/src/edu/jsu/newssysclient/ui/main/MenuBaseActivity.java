package edu.jsu.newssysclient.ui.main;


import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.R.drawable;
import edu.jsu.newssysclient.R.id;
import edu.jsu.newssysclient.R.layout;
import edu.jsu.newssysclient.adapter.MenuItem;
import edu.jsu.newssysclient.adapter.MenuAdapter;
import edu.jsu.newssysclient.util.local.UserInfoManager;

/**
 * 基础类，由其他activity继承
 * @author zuo
 *
 */
public abstract class MenuBaseActivity extends FragmentActivity implements MenuAdapter.MenuListener {

    private static final String STATE_ACTIVE_POSITION =
            "net.simonvt.menudrawer.samples.LeftDrawerSample.activePosition";

    protected MenuDrawer mMenuDrawer;

    protected MenuAdapter mAdapter;
    protected ListView mList;
    protected TextView userName;
    protected ImageView userHead;

    private int mActivePosition = 0;
    
    //侧边栏菜单列表
    List<Object> items = new ArrayList<Object>();
    // "登陆", "注册", "收藏", "视频", "订阅", "设置", "定制", "建议", "帮助", "关于"
    //  0		1	  2		10		11	  3		7		4		5	6
    String[] menuItemName = {"登陆", "注册", "视频", "设置", "定制"};
    int[] menuItemIcon = {R.drawable.ic_menu01_login, R.drawable.ic_menu02_register, R.drawable.ic_action_video, R.drawable.ic_menu04_setting, 
    		 R.drawable.ic_menu07_custom};
    int[] menuItemID = {0, 1, 10, 3, 7};

    String[] loginMenuItemName = {"收藏", "视频", "设置", "定制"};
    int[] loginMenuItemIcon = {R.drawable.ic_menu03_collect, R.drawable.ic_action_video, R.drawable.ic_menu04_setting, R.drawable.ic_menu07_custom};
    int[] loginMenuItemID = {2, 10, 3, 7};

    @Override
    protected void onStart() {
    	super.onStart();

    	items.clear();
    	//	0		1	  2		3		4	  5		6
    	// {"登陆", "注册", "收藏", "设置", "建议", "帮助", "关于"};
        if (UserInfoManager.getUserInfo() == null) {	// 未登录
	        for (int i = 0; i<menuItemName.length; i++){
		        items.add(new MenuItem(menuItemName[i], menuItemIcon[i], menuItemID[i]));
	        }
        } else {	// 已经登录
        	for (int i = 0; i<loginMenuItemName.length; i++){
		        items.add(new MenuItem(loginMenuItemName[i], loginMenuItemIcon[i], loginMenuItemID[i]));
	        }
        }
        mAdapter.notifyDataSetChanged();
    }
    
    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);

        if (inState != null) {
            mActivePosition = inState.getInt(STATE_ACTIVE_POSITION);
        }

        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, getDrawerPosition(), getDragMode());

        LinearLayout v = (LinearLayout) View.inflate(this, R.layout.left_menu_layout, null);
        userHead = (ImageView) v.findViewById(R.id.iv_menu_top_head);
        userName = (TextView) v.findViewById(R.id.tv_menu_top_name);
        mList = (ListView) v.findViewById(R.id.lv_menu_list);
//        mList = new ListView(this);

        mAdapter = new MenuAdapter(this, items);
        mAdapter.setListener(this);
        mAdapter.setActivePosition(mActivePosition);

        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(mItemClickListener);

        mMenuDrawer.setMenuView(v);
    }

    /**
     * 菜单条目点击事件
     * @param position
     * @param item
     */
    protected abstract void onMenuItemClicked(int position, MenuItem item);

    protected abstract int getDragMode();

    protected abstract Position getDrawerPosition();

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mActivePosition = position;
            mMenuDrawer.setActiveView(view, position);
            mAdapter.setActivePosition(position);
            onMenuItemClicked(position, (MenuItem) mAdapter.getItem(position));
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_ACTIVE_POSITION, mActivePosition);
    }

    @Override
    public void onActiveViewChanged(View v) {
        mMenuDrawer.setActiveView(v, mActivePosition);
    }
    
}
