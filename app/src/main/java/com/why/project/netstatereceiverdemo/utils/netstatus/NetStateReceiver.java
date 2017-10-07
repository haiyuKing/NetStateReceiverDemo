package com.why.project.netstatereceiverdemo.utils.netstatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.ArrayList;

/**
 * 参考资料：http://www.cnblogs.com/renzimu/p/4537954.html
 * */
public class NetStateReceiver extends BroadcastReceiver {
	private static final String TAG = NetStateReceiver.class.getSimpleName();

	private static final String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";//Android自带的网络改变的广播器名称
	public static final String CUSTOM_ANDROID_NET_CHANGE_ACTION = "os.net.conn.CONNECTIVITY_CHANGE";//自定义的网络改变的广播器名称

	private static boolean isNetAvailable = false;
	private static BroadcastReceiver mBroadcastReceiver;
	private static ArrayList<NetChangeObserver> mNetChangeObservers = new ArrayList();
	private static NetUtils.NetType mNetType;

	public void onReceive(Context context, Intent intent) {
		mBroadcastReceiver = this;
		if ((intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)) ||
				(intent.getAction().equalsIgnoreCase(CUSTOM_ANDROID_NET_CHANGE_ACTION))) {
			if (NetUtils.isNetworkAvailable(context)) {
				Log.i(TAG, "<--- network connected --->");
				isNetAvailable = true;
				mNetType = NetUtils.getAPNType(context);
			} else {
				Log.i(TAG, "<--- network disconnected --->");
				isNetAvailable = false;
			}
			notifyObserver();
		}
	}

	private static BroadcastReceiver getReceiver() {
		if (mBroadcastReceiver == null)
			mBroadcastReceiver = new NetStateReceiver();
		return mBroadcastReceiver;
	}

	/**
	 * 检查网络状态
	 * */
	public static void checkNetworkState(Context mContext) {
		Intent intent = new Intent();
		intent.setAction(CUSTOM_ANDROID_NET_CHANGE_ACTION);
		mContext.sendBroadcast(intent);
	}

	public static NetUtils.NetType getAPNType() {
		return mNetType;
	}

	/**
	 * 获取当前网络状态，true为网络连接成功，否则网络连接失败
	 * */
	public static boolean isNetworkAvailable() {
		return isNetAvailable;
	}

	private void notifyObserver() {
		if (!mNetChangeObservers.isEmpty()) {
			for (int i = 0; i < mNetChangeObservers.size(); i++) {
				NetChangeObserver netChangeObserver = (NetChangeObserver) mNetChangeObservers.get(i);
				if (netChangeObserver != null) {
					if (!isNetworkAvailable()) {
						netChangeObserver.onNetDisConnect();
					} else {
						netChangeObserver.onNetConnected(mNetType);
					}
				}
			}
		}
	}

	/**
	 * 注册网络状态广播
	 * */
	public static void registerNetworkStateReceiver(Context context) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(CUSTOM_ANDROID_NET_CHANGE_ACTION);
		filter.addAction(ANDROID_NET_CHANGE_ACTION);
		context.getApplicationContext().registerReceiver(getReceiver(), filter);
	}

	/**
	 * 注册网络连接观察者
	 * */
	public static void registerObserver(NetChangeObserver observer) {
		if (mNetChangeObservers == null)
			mNetChangeObservers = new ArrayList();
		mNetChangeObservers.add(observer);
	}

	/**
	 * 注销网络连接观察者
	 * */
	public static void removeRegisterObserver(NetChangeObserver observer) {
		if ((mNetChangeObservers != null) && (mNetChangeObservers.contains(observer)))
			mNetChangeObservers.remove(observer);
	}

	/** 注销网络状态广播
	 * */
	public static void unRegisterNetworkStateReceiver(Context context) {
		if (mBroadcastReceiver != null) ;
		try {
			context.getApplicationContext().unregisterReceiver(mBroadcastReceiver);
			return;
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
	}


}