package edu.jsu.newssysclient.ui.widget;

import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.ui.main.AllTypeNewsActivity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自定义控件，用于新闻列表头部图片展示
 * @author zuo
 *
 */
public class MyViewFlow extends ViewFlow {
	ViewGroup parent;
	
	public void setParent(ViewGroup parent) {
		this.parent = parent;
	}
	
	public MyViewFlow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyViewFlow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyViewFlow(Context context, int sideBuffer) {
		super(context, sideBuffer);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		/*switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			parent.requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_UP:
			parent.requestDisallowInterceptTouchEvent(false);
			break;
		case MotionEvent.ACTION_CANCEL:
			parent.requestDisallowInterceptTouchEvent(false);
			break;
		case MotionEvent.ACTION_MOVE:
			parent.requestDisallowInterceptTouchEvent(true);
			break;
		}*/
		super.onTouchEvent(ev);
		return true;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		AppLogger.d("***MyViewFlow***onInterceptTouchEvent*****");
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			parent.requestDisallowInterceptTouchEvent(true);
			/*if (getContext() instanceof AllTypeNewsActivity) {
				AppLogger.w("on MyViewFlow Action Down");
				AllTypeNewsActivity m = (AllTypeNewsActivity) getContext();
				m.mViewPager.requestDisallowInterceptTouchEvent(true);
				AppLogger.w("on MyViewFlow Action Down");
			}*/
			break;
		case MotionEvent.ACTION_UP:
			parent.requestDisallowInterceptTouchEvent(false);
//			if (getContext() instanceof AllTypeNewsActivity) {
//				AllTypeNewsActivity m = (AllTypeNewsActivity) getContext();
//				m.mViewPager.requestDisallowInterceptTouchEvent(false);
//			}
			break;
		case MotionEvent.ACTION_CANCEL:
			parent.requestDisallowInterceptTouchEvent(false);
//			if (getContext() instanceof AllTypeNewsActivity) {
//				AllTypeNewsActivity m = (AllTypeNewsActivity) getContext();
//				m.mViewPager.requestDisallowInterceptTouchEvent(false);
//			}
			break;
		case MotionEvent.ACTION_MOVE:
			parent.requestDisallowInterceptTouchEvent(true);
//			if (getContext() instanceof AllTypeNewsActivity) {
//				AllTypeNewsActivity m = (AllTypeNewsActivity) getContext();
//				m.mViewPager.requestDisallowInterceptTouchEvent(true);
//			}
			break;
		}
		return true;
	}
}
