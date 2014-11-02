package edu.jsu.newssysclient;

import java.io.File;

import org.androidannotations.annotations.EApplication;
import org.apache.commons.httpclient.HttpClient;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import de.greenrobot.event.EventBus;
import edu.jsu.newssysclient.bean.UserInfo;
import android.app.Application;

/**
 * Application，定义应用程序全局变量
 * @author zuo
 *
 */
public class MyApplication extends Application {
	public final static EventBus BUS = new EventBus();
	private static HttpClient client = null;

	//singleton
    private static MyApplication globalContext = null;
    
    public static HttpClient getHttpClient() {
    	if (client == null) {
    		client = new HttpClient();
    		client.getHttpConnectionManager().getParams()
			.setConnectionTimeout(5000);
    	}
    	return client;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        globalContext = this;
        
        // 图片缓存设置
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), " NewsSys/Cache");
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory()
        .cacheOnDisc()
        .build();
        
        // 图片加载配置
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .defaultDisplayImageOptions(defaultOptions)
        .memoryCacheExtraOptions(480, 800)
        .memoryCacheSize(10 * 1024 * 1024)    
        .discCacheSize(100 * 1024 * 1024)    
        .threadPoolSize(5)//线程池内加载的数量  
        .discCacheFileCount(300) //缓存的文件数量
        .memoryCache(new WeakMemoryCache())
        .imageDownloader(new BaseImageDownloader(globalContext, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间   
        .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
        .build();
        ImageLoader.getInstance().init(config); // Do it on Application start
    }
    
    public static MyApplication getInstance() {
        return globalContext;
    }
}
