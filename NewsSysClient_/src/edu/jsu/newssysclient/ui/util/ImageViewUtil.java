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
 * ͼƬ������
 * @author zuo
 *
 */
public class ImageViewUtil {


    /**
     * ����ͷ��
     * @param image ImageView
     * @param url	String ͷ���ַ
     */
	public static void loadImage(final ImageView image, String url) {
		if (image == null) {
			return ;
		}
		// ����Ĭ��ͼƬ
		//image.setImageResource(R.drawable.ic_default_avatar);
		ImageSize targetSize = new ImageSize(image.getWidth(), image.getHeight()); // result Bitmap will be fit to this size
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)//����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ  
		.showImageOnFail(R.drawable.ic_launcher)  //����ͼƬ����/��������д���ʱ����ʾ��ͼƬ
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//����ͼƬ����εı��뷽ʽ��ʾ
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();//�������
		
		AppLogger.i("����ͼƬ����ַ��" + url);
		ImageLoader.getInstance().loadImage(url, targetSize, options, new SimpleImageLoadingListener() {
		    @Override
		    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		    	image.setImageBitmap(loadedImage);
		    }
		});
	}
}
