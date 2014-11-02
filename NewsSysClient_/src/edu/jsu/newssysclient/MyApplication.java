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
 * Application������Ӧ�ó���ȫ�ֱ���
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
        
        // ͼƬ��������
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), " NewsSys/Cache");
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory()
        .cacheOnDisc()
        .build();
        
        // ͼƬ��������
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .defaultDisplayImageOptions(defaultOptions)
        .memoryCacheExtraOptions(480, 800)
        .memoryCacheSize(10 * 1024 * 1024)    
        .discCacheSize(100 * 1024 * 1024)    
        .threadPoolSize(5)//�̳߳��ڼ��ص�����  
        .discCacheFileCount(300) //������ļ�����
        .memoryCache(new WeakMemoryCache())
        .imageDownloader(new BaseImageDownloader(globalContext, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)��ʱʱ��   
        .discCache(new UnlimitedDiscCache(cacheDir))//�Զ��建��·��
        .build();
        ImageLoader.getInstance().init(config); // Do it on Application start
    }
    
    public static MyApplication getInstance() {
        return globalContext;
    }
}
