package edu.jsu.newssysclient.util.net;

import edu.jsu.newssysclient.MyApplication;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {

	/**
	 * 检查是否连接网络
	 * @return true,已连接网络；false,未连接网络
	 */
	public static boolean canConnectNet() {
		ConnectivityManager connectivityManager = (ConnectivityManager)MyApplication.getInstance().getSystemService(MyApplication.getInstance().CONNECTIVITY_SERVICE);  
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();  
        if(networkInfo == null || !networkInfo.isAvailable())  
        {  
            //当前有可用网络 
            return false; 
        }  
        else   
        {  
            //当前无可用网络 
            return true; 
        }
	}
}
