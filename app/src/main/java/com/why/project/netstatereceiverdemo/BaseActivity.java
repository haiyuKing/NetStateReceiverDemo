package com.why.project.netstatereceiverdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.why.project.netstatereceiverdemo.utils.ToastUtil;
import com.why.project.netstatereceiverdemo.utils.netstatus.NetChangeObserver;
import com.why.project.netstatereceiverdemo.utils.netstatus.NetStateReceiver;
import com.why.project.netstatereceiverdemo.utils.netstatus.NetUtils;


/**
 * Created by HaiyuKing
 * Used Activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {

	private static final String TAG = "BaseActivity";

	/*=================网络变化的广播器=====================*/
	protected NetChangeObserver mNetChangeObserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		/*=================网络变化的广播器=====================*/
		registerNetStateChange();//注册网络变化的广播器

		super.onCreate(savedInstanceState);
	}

	protected void onDestroy() {
		super.onDestroy();
		/*=================网络变化的广播器=====================*/
		NetStateReceiver.removeRegisterObserver(this.mNetChangeObserver);
	}

	/*==========网络变化的广播器=============*/
	private void registerNetStateChange() {
		if (this.mNetChangeObserver == null) {
			this.mNetChangeObserver = new NetChangeObserver() {
				public void onNetConnected(NetUtils.NetType netType) {
					super.onNetConnected(netType);
					onNetworkConnected(netType);
					if (NetUtils.isMobileConnected(getApplicationContext())){
						ToastUtil.showShortToast("您当前正在使用运营商网络");
					}
					if(NetUtils.isWifiConnected(getApplicationContext())){
						ToastUtil.showShortToast("您当前正在使用无线网络");
					}
				}

				public void onNetDisConnect() {
					super.onNetDisConnect();
					onNetworkDisConnected();
					ToastUtil.showShortToast("网络无法连接,请检查网络设置");
				}
			};
			NetStateReceiver.registerObserver(this.mNetChangeObserver);// 声明mNetChangeObserver注册到网络状态广播接收器中
		}
	}
	protected abstract void onNetworkConnected(NetUtils.NetType netType);
	protected abstract void onNetworkDisConnected();

}
