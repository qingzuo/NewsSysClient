package edu.jsu.newssysclient.util.net;

import edu.jsu.newssysclient.MyApplication;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {

	/**
	 * ����Ƿ���������
	 * @return true,���������磻false,δ��������
	 */
	public static boolean canConnectNet() {
		ConnectivityManager connectivityManager = (ConnectivityManager)MyApplication.getInstance().getSystemService(MyApplication.getInstance().CONNECTIVITY_SERVICE);  
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();  
        if(networkInfo == null || !networkInfo.isAvailable())  
        {  
            //��ǰ�п������� 
            return false; 
        }  
        else   
        {  
            //��ǰ�޿������� 
            return true; 
        }
	}
}
