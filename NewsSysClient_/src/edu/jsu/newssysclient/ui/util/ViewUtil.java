package edu.jsu.newssysclient.ui.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import de.greenrobot.event.EventBus;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcel;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.LeadingMarginSpan.Standard;
import android.text.style.URLSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import edu.jsu.newssysclient.MyApplication;
import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.bean.Follow;
import edu.jsu.newssysclient.bean.NewsImage;
import edu.jsu.newssysclient.bean.NewsLead;
import edu.jsu.newssysclient.bean.NewsLink;
import edu.jsu.newssysclient.bean.NewsText;
import edu.jsu.newssysclient.bean.NewsTimeSource;
import edu.jsu.newssysclient.bean.NewsTitle;
import edu.jsu.newssysclient.bean.NewsVideo;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.ui.main.ShowImageByUrlActivity;
import edu.jsu.newssysclient.ui.main.ShowNewsActivity;
import edu.jsu.newssysclient.ui.main.WebViewActivity;
import edu.jsu.newssysclient.util.local.StringUtil;
import edu.jsu.newssysclient.util.net.IOStream;

/**
 * View生产
 * @author zuo
 *
 */
public class ViewUtil {
	
	private Context context;
	
	public ViewUtil(Context context) {
		this.context = context;
	}
	
	/**
	 * 添加标题
	 */
	public View addView(NewsTitle nt){
		TextView tv = (TextView) View.inflate(context, R.layout.view_title_layout, null);
		
		tv.setText(nt.getText());
		
		return tv;
	}

	/**
	 * 添加“时间和来源”
	 */
	public View addView(NewsTimeSource nt){
		TextView tv = (TextView) View.inflate(context, R.layout.view_timesource_layout, null);
		
		tv.setText(nt.getText());
		
		return tv;
	}
	
	public int dip2px(float dipValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dipValue * scale + 0.5f);
	}
	
	public int px2dip(Context context, float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(pxValue / scale + 0.5f);
	}
	
	/**
	 * 添加链接
	 */
	public View addView(NewsLink nt){
		TextView tv = new TextView(context);
		
//System.out.println("ViewUtil: "+nt.getText());
		Toast.makeText(context, ""+nt.getText().compareTo("<a href=\"http://student.csdn.net/?232885\"><u>我的CSDN博客 </u></a>"), 0).show(); 
		tv.setText(Html.fromHtml(nt.getText()));
		tv.setTextSize(nt.getSize());
		tv.setTextColor(nt.getColor());
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
		lp.setMargins(0, nt.getPaddingtop(), 0, 0);  
		tv.setLayoutParams(lp);
		tv.setMovementMethod(LinkMovementMethod.getInstance());  
        CharSequence text = tv.getText();   
        if(text instanceof Spannable){   
            int end = text.length();   
            Spannable sp = (Spannable)tv.getText();   
            URLSpan[] urls=sp.getSpans(0, end, URLSpan.class);    
            SpannableStringBuilder style=new SpannableStringBuilder(text);   
            style.clearSpans();//should clear old spans   
            for(URLSpan url : urls){   
                MyURLSpan myURLSpan = new MyURLSpan(url.getURL(), context);   
                style.setSpan(myURLSpan,sp.getSpanStart(url),sp.getSpanEnd(url),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);   
          }  
        }
	    
		return tv;
	}
	
	 class MyURLSpan extends ClickableSpan{   
        
        private String mUrl;   
        Context context;
        MyURLSpan(String url, Context context) {   
            mUrl =url;  
            this.context = context;
        }   
        @Override
        public void onClick(View widget) {
//            Intent intent = new Intent(context, WebViewActivity.class);
//            intent.putExtra(WebViewActivity.WEBVIEWURL, mUrl);
//            context.startActivity(intent);
        	Toast.makeText(context, "haah"+mUrl,Toast.LENGTH_LONG).show();
        }   
    }
	
	/**
	 * 添加新闻导语
	 */
	public View addView(NewsLead nt){
		View v = View.inflate(context, R.layout.view_lead_layout, null);
		
		TextView tv = (TextView) v.findViewById(R.id.text);
//		tv.setText(Html.fromHtml(nt.getText()));
		tv.setText(addRetract(nt.getText()));
		tv.setLineSpacing(0f, 1.2f);
//		tv.setPadding(10, 10, 10, 10);
		tv.setMovementMethod(LinkMovementMethod.getInstance());  
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
		lp.setMargins(0, 30, 0, 0);  
		v.setLayoutParams(lp);
		
		return v;
	}
	
	/**
	 * 添加新闻内容
	 */
	public View addView(NewsText nt){
		if (nt == null) return null;
		
		TextView tv = (TextView) View.inflate(context, R.layout.view_content_layout, null);
		
		if (!StringUtil.isEmpty(nt.getText())) {
			tv.setText(Html.fromHtml(nt.getText()));
//			tv.setText(addRetract(nt.getText()));
			AppLogger.i("添加新闻内容："+nt.getText());
		}
		tv.setLineSpacing(0f, 1.2f);
		/*LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
		lp.setMargins(0, nt.getPaddingtop(), 0, 0);  
		tv.setLayoutParams(lp);*/
		tv.setMovementMethod(LinkMovementMethod.getInstance()); 
		
		return tv;
	}
	
	/**
	 * 添加新闻图片
	 */
	public View addView(final NewsImage ni){
		LinearLayout ll = (LinearLayout) View.inflate(context, R.layout.view_image_layout, null);
		
		final ImageView image =  (ImageView) ll.findViewById(R.id.view_image);
		TextView tv = (TextView) ll.findViewById(R.id.view_note);
		if (!StringUtil.isEmpty(ni.getNote())) {
			tv.setText(ni.getNote());
		} else {
			tv.setVisibility(View.GONE);
		}

		int t = context.getResources().getDisplayMetrics().widthPixels-dip2px(36);
		AppLogger.i("新闻详细界面显示图片，图片宽最大值：" + t);
//		image.setMaxHeight(px2dip(context, 380));
		image.setMaxWidth(px2dip(context, t));
		int h = 300;
		/*if (ni.getHeight() != 0 && ni.getHeight() != 0) {
			 h = t * ni.getHeight()/ni.getWidth();
		}*/
		int w = t;
		if (!StringUtil.isEmpty(ni.getSource())) {
	    	//image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			ImageLoader imageLoader = ImageLoader.getInstance();
			ImageSize targetSize = new ImageSize(w, h); // result Bitmap will be fit to this size
			DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片  
			.showImageOnFail(R.drawable.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();//构建完成
			imageLoader.loadImage(ni.getSource(), targetSize, options, new SimpleImageLoadingListener() {
			    @Override
			    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			    	AppLogger.i("新闻详细界面显示图片，图片高度" + loadedImage.getHeight());
			    	AppLogger.i("新闻详细界面显示图片，图片宽度" + loadedImage.getWidth());
			    	image.setImageBitmap(loadedImage);
			    	/*ShowNewsActivity activity = (ShowNewsActivity)context;
			    	activity.show();*/
			    }
			});
		}
		
		image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ShowImageByUrlActivity.class); 
				intent.putExtra(ShowImageByUrlActivity.IMAGEURL, ni.getSource());
				context.startActivity(intent);
			}
		});
		
		return ll;
	}
	/*public View addView(NewsImage ni){
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
		lp.setMargins(0, 0, 0, 0);  
		ll.setLayoutParams(lp);
		
		final ImageView image = new ImageView(context);
		TextView tv = new TextView(context);
		
		h = 400;
		w = 400;
		image.setImageResource(R.drawable.news_img);
		if (!StringUtil.isEmpty(ni.getSource())) {
			ImageLoader imageLoader = ImageLoader.getInstance();
			ImageSize targetSize = new ImageSize(120, 80); // result Bitmap will be fit to this size
			DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片  
			.showImageOnFail(R.drawable.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
			.bitmapConfig(Bitmap.Config.ARGB_8888)
			.build();//构建完成
			imageLoader.loadImage(ni.getSource(), targetSize, options, new SimpleImageLoadingListener() {
			    @Override
			    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			    	image.setImageBitmap(loadedImage);
			    }
			});
		}
		wpx = (context.getResources().getDisplayMetrics().widthPixels-36) * h/w;
		image.setScaleType(ScaleType.FIT_CENTER);
		image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,  
				wpx));
		image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "See Image", 0).show();
				Intent intent = new Intent(); 
				intent.setAction(android.content.Intent.ACTION_VIEW);
				
//				intent.setDataAndType(Uri., "image/png");
				//context.startActivity(intent);
			}
		});
		
		tv.setText(ni.getNote());
		tv.setPadding(0, 5, 0, 0);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		
		ll.addView(image);
		ll.addView(tv);
		
		return ll;
	}*/
	
	/**
	 * 添加新闻视频
	 */
	public View addView(final NewsVideo nv) {
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		ImageView image = new ImageView(context);
		TextView tv = new TextView(context);
		
		image.setImageDrawable(context.getResources().getDrawable(R.drawable.news_video));
		image.setScaleType(ScaleType.FIT_CENTER);
		image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,  
                400));
		image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 检查手机是否 安装了播放视频的应用
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
			        String type = "video/* ";
			        Uri uri = Uri.parse(nv.getSource());
			        intent.setDataAndType(uri, type);
			        context.startActivity(intent);
				} catch (Exception e) {
					Toast.makeText(context, "未安装观看视频的应用", 0).show();
				}
			}
		});
		
		tv.setText(nv.getNote());
		tv.setPadding(0, 5, 0, 0);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
		lp.setMargins(0, 40, 0, 0);  
		ll.setLayoutParams(lp);
		ll.addView(image);
		ll.addView(tv);
		
		return ll;
	}

	/**
	 * 给文本添加缩进
	 * @param str
	 * @return
	 */
	private Spannable addRetract(String str) {
		if (str == null) {
			str = "null";
		}
		Spanned s = Html.fromHtml(str);
//		Spannable spannable = new SpannableString(s.toString());
		Spannable spannable = (Spannable) s;
		Parcel p = Parcel.obtain();
		p.writeInt(70);
		p.writeInt(0);
		p.setDataPosition(0);
		Standard lms = new Standard(p);
		spannable.setSpan(lms, 0, 0, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		
		return spannable;
	}
}
