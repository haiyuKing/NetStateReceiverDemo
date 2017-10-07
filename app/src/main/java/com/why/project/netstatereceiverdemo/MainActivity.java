package com.why.project.netstatereceiverdemo;

import android.os.Bundle;

import com.why.project.netstatereceiverdemo.utils.netstatus.NetUtils;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onNetworkConnected(NetUtils.NetType netType) {

	}

	@Override
	protected void onNetworkDisConnected() {

	}
}
