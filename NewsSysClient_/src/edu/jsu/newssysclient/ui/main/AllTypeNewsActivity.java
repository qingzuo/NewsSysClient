package edu.jsu.newssysclient.ui.main;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.viewpagerindicator.TabPageIndicator;

import de.greenrobot.event.EventBus;
import edu.jsu.newssysclient.MyApplication;
import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.R.drawable;
import edu.jsu.newssysclient.R.id;
import edu.jsu.newssysclient.R.layout;
import edu.jsu.newssysclient.URLHelper;
import edu.jsu.newssysclient.adapter.MyCustomGrideViewAdapter;
import edu.jsu.newssysclient.bean.NewDescrListFetchEvent;
import edu.jsu.newssysclient.bean.TypeList;
import edu.jsu.newssysclient.bean.UserInfo;
import edu.jsu.newssysclient.dao.cache.Cache;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.fragment.NewsListFragment;
import edu.jsu.newssysclient.ui.menu.AboutActivity_;
import edu.jsu.newssysclient.ui.menu.CollectActivity_;
import edu.jsu.newssysclient.ui.menu.CustomNewsTypesActivity;
import edu.jsu.newssysclient.ui.menu.HelpActivity_;
import edu.jsu.newssysclient.ui.menu.LoginActivity_;
import edu.jsu.newssysclient.ui.menu.RegisterActivity_;
import edu.jsu.newssysclient.ui.menu.SettingActivity;
import edu.jsu.newssysclient.ui.menu.ShowUserInfoActivity_;
import edu.jsu.newssysclient.ui.menu.SuggestActivity_;
import edu.jsu.newssysclient.ui.util.ImageViewUtil;
import edu.jsu.newssysclient.ui.widget.ViewFlow;
import edu.jsu.newssysclient.util.local.UserInfoManager;
import edu.jsu.newssysclient.util.net.UserHelper;

/**
 * �����棬չʾ���ŷ��࣬�в�߲˵�
 * @author zuo
 *
 */
public class AllTypeNewsActivity extends MenuBaseActivity {
    private static final String[] NEWSTYPES1 = new String[] { "����", "�Ƽ�", "����", "����", "�ƾ�", "�Ƽ�", "ʱ��", "����", "����", "��ʷ", "��Ӱ"};
    private static final String[] NEWSTYPES2 = new String[] { "����", "�Ƽ�", "����", "����", "�ƾ�", "�Ƽ�"};

    private static final String STATE_NEWSTYPE = "net.simonvt.menudrawer.samples.LeftDrawerSample.contentText";
    public static final String NEEDOPENMENU = "edu.jsu.newssysclient.ui.main.AllTypeNewsActivity.needopenmenu";
    private boolean needOpenMenuFlag = false;

	protected static final String TAG = "edu.jsu.newssysclient.ui.main.AllTypeNewsActivity";

	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	private TabPageIndicator indicator;	// ����tab
	public ViewPager mViewPager;		// ������������
    private int mPagerOffsetPixels;	//	
    private int mPagerPosition;		// ��ǰλ��

	private String mCurrentFragmentTag;
	
    private String mContentText;
    
    private UserInfo userInfo;
    private Cache cache = new Cache();
	private List<String> showTypes = new ArrayList<String>();
	private String showNewsTypesKey = "CurrentNewsTypesCacheKey";
    
    /**
     * ������˵�ͷ��ͼƬ�ĵ���¼�
     * @param v
     */
    public void MoreUserInfo(View v){
    	if (userInfo != null) {
    		Intent intent = new Intent(this, ShowUserInfoActivity_.class);
        	startActivity(intent);
    	} else {
    		Toast.makeText(this, "���¼~", 0).show();
    	}
    }
    
    @Override
    protected void onStart() {
    	AppLogger.i("�����棬 onStart");
    	// TODO Auto-generated method stub
    	super.onStart();
    	//����û��Ƿ��¼
    	userInfo = UserInfoManager.getUserInfo();
    	if (userInfo != null) {
    		userName.setText(userInfo.getUserName());
    		
    		ImageViewUtil.loadImage(userHead, URLHelper.SERVERURL+userInfo.getPtoShowPath());
    	}
    	Intent intent = getIntent();
    	if (intent != null) {
    		needOpenMenuFlag = getIntent().getBooleanExtra(NEEDOPENMENU, false) ;
    	}
    	if (needOpenMenuFlag) {
    		mMenuDrawer.openMenu();
    	}
    }

    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);
    	AppLogger.i("�����棬 onCreate");

        // ��߲˵�����
        mMenuDrawer.setContentView(R.layout.alltype_news_layout);
        mMenuDrawer.setSlideDrawable(R.drawable.ic_drawer);
        mMenuDrawer.setDrawerIndicatorEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
        mMenuDrawer.setOnInterceptMoveEventListener(new MenuDrawer.OnInterceptMoveEventListener() {
            @Override
            public boolean isViewDraggable(View v, int dx, int x, int y) {
            	// ����false�򿪲�߲˵���true����
            	// �����Ե�⻬�룬��ʾ���صĲ˵�
            	if (x < 80) {
            		return false;
            	}
            	// ��viewpager���滬�������ڵ�һ��ѡ���򿪲�����˵�������ѡ�����
            	if (v == mViewPager) {
                    return !(mPagerPosition == 0 && mPagerOffsetPixels == 0) || dx < 0;
                }
            	// ��ͷ��ѡ�indeicator���滬�������򿪲�����˵�
            	if (v == indicator){
            		return true;
            	}
            	// ��ͷ��ѡ�indeicator���滬�������򿪲�����˵�
            	if (v instanceof ViewFlow){
            		AppLogger.v("******this is viewflow click.********");
            		return true;
            	}

                return false;
            }
        });

		// ��ȡ��ǰ��ʾ��������������
		TypeList currentTypeslList = cache
				.get(showNewsTypesKey, TypeList.class);
		if (currentTypeslList != null && currentTypeslList.getTypes() != null
				&& currentTypeslList.getTypes().size() != 0) { // �л���
			for (String t : currentTypeslList.getTypes()) {
				showTypes.add(t);
			}
		} else { // ����Ĭ�ϵ�
			for (int i = 0; i < CustomNewsTypesActivity.NEWSTYPESDEFUALTSHOW.length; i++) {
				showTypes.add(CustomNewsTypesActivity.NEWSTYPESDEFUALTSHOW[i]);
			}
		}

		// ����������
        FragmentPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager(), showTypes);

        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(adapter);

        // ������
        indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPagerPosition = position;
                mPagerOffsetPixels = positionOffsetPixels;
            }
            
        });
        AppLogger.w("recreate");
    }
    
    /**
     * �ҳ��������
     * @author zuo
     *
     */
    class MyViewPagerAdapter extends FragmentPagerAdapter {
//    	private String[] CONTENT;
    	private List<String> types;
    	
        public MyViewPagerAdapter(FragmentManager fm, List<String> types) {
            super(fm);
            this.types = types;
        }

        @Override
        public Fragment getItem(int position) {
//            return NewsListFragment.newInstance(CONTENT[position % CONTENT.length].trim());
        	// �����б�ҳ�棬��������id����һ�����Ϊ1
//        	return NewsListFragment.newInstance(""+(position+1));
        	
        	int typeId = CustomNewsTypesActivity.ALLNEWSTYPE.get(showTypes.get(position));
        	//AppLogger.i("�����棬�����������ͣ�" + showTypes.get(position));
        	//AppLogger.i("�����棬��������ID��" + typeId);
        	return NewsListFragment.newInstance(Integer.toString(typeId));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return " " + types.get(position % types.size()) + " ";
        }

        @Override
        public int getCount() {
            return types.size();
        }
    }

    @Override
    protected int getDragMode() {
        return MenuDrawer.MENU_DRAG_CONTENT;
    }

    @Override
    protected Position getDrawerPosition() {
        return Position.START;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_NEWSTYPE, mContentText);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mMenuDrawer.toggleMenu();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        final int drawerState = mMenuDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mMenuDrawer.closeMenu();
            return;
        }

        super.onBackPressed();
        //finish();
    }

    /**
     * ��߲˵�����¼�
     */
	@Override
	protected void onMenuItemClicked(int position,
			edu.jsu.newssysclient.adapter.MenuItem item) {
		// ����д�˵�������¼�
    	Intent intent = null;
    	
    	// {"��½", "ע��", "�ղ�", "����", "����", "����", "����"};
    	switch (item.getMId()) {
		case 0:		// ��¼
			intent = new Intent(this, LoginActivity_.class);
			break;
		case 1:		// ע��
			intent = new Intent(this, RegisterActivity_.class);
			break;
		case 2:		//�ղ�
			if (userInfo != null) {
				intent = new Intent(this, CollectActivity_.class);
			} else {
				Toast.makeText(this, "���¼~", 0).show();
				return ;
			}
			break;
		case 3:		//����
			intent = new Intent(this, SettingActivity.class);
			break;
		case 7:		//����
			intent = new Intent(this, CustomNewsTypesActivity.class);
			break;
		case 4:		//����
			intent = new Intent(this, SuggestActivity_.class);
			break;
		case 5:		//����
			intent = new Intent(this, HelpActivity_.class);
			break;
		case 6:		//����
			intent = new Intent(this, AboutActivity_.class);
			break;
		case 10:	// ��Ƶ
			intent = new Intent(this, AllTypeVideoActivity.class);
			break;
		case 11:	//����
			intent = new Intent(this, SubscriptionActivity.class);
			break;

		default:
			intent = new Intent(this, ShowNewsActivity.class);
			break;
		}
    	
    	startActivity(intent);
        //mMenuDrawer.closeMenu();
	}
}
