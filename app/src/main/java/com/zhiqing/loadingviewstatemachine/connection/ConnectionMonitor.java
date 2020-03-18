package com.zhiqing.loadingviewstatemachine.connection;

import android.content.Context;

public class ConnectionMonitor {
    private Context mContext;

    public void init(Context context) {
        mContext = context;
        NetStateChangeReceiver.registerReceiver(mContext);
    }

    public void destroy() {
        NetStateChangeReceiver.unregisterReceiver(mContext);
    }

    public NetworkDetector getNetworkDetector() {
        return new NetworkDetector(mContext);
    }
}
