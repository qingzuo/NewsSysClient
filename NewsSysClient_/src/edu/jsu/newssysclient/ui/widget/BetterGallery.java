package edu.jsu.newssysclient.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * 自定义控件，用于显示图片集合滚动
 * @author zuo
 *
 */
public class BetterGallery extends Gallery {
	private boolean scrollingHorizontally = false;

	public BetterGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BetterGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BetterGallery(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		super.onInterceptTouchEvent(ev);
		return scrollingHorizontally;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		scrollingHorizontally = true;
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			scrollingHorizontally = false;
		}

		return super.onTouchEvent(event);
	}
}