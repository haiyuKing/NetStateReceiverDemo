package com.why.project.netstatereceiverdemo.utils.netstatus;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Locale;

/**
 * 参考资料：http://www.cnblogs.com/renzimu/p/4537936.html
 * */
public class NetUtils {
	public enum NetType {

		WIFI("WIFI",0),
		CMNET("CMNET",1),
		CMWAP("CMWAP",2),
		NONE("NONE",3);

		private String typeName = "NONE";
		private int typeValue = 3;

		private NetType(String name, int value) {
			typeName = name;
			typeValue = value;
		}

	}

	/**
	 * 获取网络类型*/
	public static NetType getAPNType(Context context) {
		NetType netType;

		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();

		if(networkInfo == null) {
			netType = NetType.NONE;
		}
		else {
			int networkInfoType = networkInfo.getType();
			if(networkInfoType == ConnectivityManager.TYPE_MOBILE) {
				//移动数据连接,不能与WiFi连接共存,如果wifi打开，则自动关闭
				if(networkInfo.getExtraInfo() == null) {
					netType = NetType.NONE;
				}
				else if(networkInfo.getExtraInfo().toLowerCase(Locale.getDefault()).equals("cmnet")) {
					netType = NetType.CMNET;//net方式
				}
				else {
					netType = NetType.CMWAP;//wap方式
				}
			}
			else if(networkInfoType == ConnectivityManager.TYPE_WIFI) {
				//wifi网络，当激活时，默认情况下，所有的数据流量将使用此连接
				netType = NetType.WIFI;
			}
			else {
				netType = NetType.NONE;
			}
		}

		return netType;
	}

	/**获取正在连接的网络类型*/
	public static int getConnectedType(Context context) {
		int netType;
		if(context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
			if(networkInfo == null) {
				netType = -1;
			}
			else if(networkInfo.isAvailable()) {
				netType = networkInfo.getType();
			}
			else {
				netType = -1;
			}
		}
		else {
			netType = -1;
		}
		return netType;
	}

	/**判断手机4G网络是否正在连接*/
	public static boolean isMobileConnected(Context context) {
		boolean MobileConnected = false;
		switch(NetUtils.getAPNType(context).typeValue) {
			case 1:
			case 2: {
				MobileConnected = true;
				break;
			}
			default: {
				MobileConnected = false;
				break;
			}
		}
		return MobileConnected;
	}

	/**判断是否有网络链接*/
	public static boolean isNetworkAvailable(Context context) {
		boolean NetworkAvailable = false;
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
		if(networkInfo == null) {
			NetworkAvailable = false;
		}else if(! networkInfo.isConnected()) {
			NetworkAvailable = false;
		}else if(networkInfo.getState() == NetworkInfo.State.CONNECTED) {
			NetworkAvailable = true;
		}
		else {
			NetworkAvailable = false;
		}

		return NetworkAvailable;
	}

	/**判断是否有网络链接*/
	public static boolean isNetworkConnected(Context context) {
		return NetUtils.isNetworkAvailable(context);
	}

	/**判断当前网络连接是否是WiFi*/
	public static boolean isWifiConnected(Context context) {
		return NetUtils.getAPNType(context) == NetType.WIFI ? true : false;
	}
}

