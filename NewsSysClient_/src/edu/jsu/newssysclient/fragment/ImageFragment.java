package edu.jsu.newssysclient.fragment;

import com.nostra13.universalimageloader.core.ImageLoader;

import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.bean.ImageNews;
import edu.jsu.newssysclient.dao.cache.Cache;
import edu.jsu.newssysclient.ui.util.ImageViewUtil;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 显示一张图片界面
 * @author zuo
 *
 */
public class ImageFragment extends Fragment{
	private ImageNews mImageNews;
	private ImageView imageView;
	private Activity mActivity;
	
	public static ImageFragment newInstance(ImageNews imageNews) {
		ImageFragment fragment = new ImageFragment();
		fragment.mImageNews = imageNews;

		return fragment;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.mActivity = activity;
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = View.inflate(getActivity(), R.layout.image_fragment_layout, null);
		
		imageView = (ImageView) v.findViewById(R.id.imageView);
		
		ImageViewUtil.loadImage(imageView, mImageNews.getImageNewsUrl());
//		ImageLoader.getInstance().displayImage(mImageNews.getImageUrl(), imageView);
		
		return v;
	}
}
