package edu.jsu.newssysclient.ui.util;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.URLHelper;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.util.local.StringUtil;

/**
 * 图片处理工具
 * @author zuo
 *
 */
public class ImageViewUtil {


    /**
     * 加载头像
     * @param image ImageView
     * @param url	String 头像地址
     */
	public static void loadImage(final ImageView image, String url) {
		if (image == null) {
			return ;
		}
		// 设置默认图片
		//image.setImageResource(R.drawable.ic_default_avatar);
		ImageSize targetSize = new ImageSize(image.getWidth(), image.getHeight()); // result Bitmap will be fit to this size
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片  
		.showImageOnFail(R.drawable.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();//构建完成
		
		AppLogger.i("加载图片，地址：" + url);
		ImageLoader.getInstance().loadImage(url, targetSize, options, new SimpleImageLoadingListener() {
		    @Override
		    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		    	image.setImageBitmap(loadedImage);
		    }
		});
	}
}
