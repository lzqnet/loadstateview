package com.zhiqing.loadingviewstatemachine.connection;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;

/**
 * Observe the network change and notify its observers.
 * Created by wangxin.sidney on 01/12/2017.
 */

public class NetworkDetector extends LiveData<NetworkType> {
    private static final String TAG = "NetworkDetector";

    private Context mContext;

    public NetworkDetector(Context context) {
        mContext = context.getApplicationContext();
    }

    private NetStateChangeObserver mObserver = new NetStateChangeObserver() {
        @Override
        public void onNetDisconnected() {
            Log.d(TAG, "network is disconnected.");
            postValue(NetworkType.NETWORK_NO);
        }

        @Override
        public void onNetConnected(NetworkType networkType) {
            Log.d(TAG, "network is connected. netWorkType =" +
                    (networkType == null ? null : networkType.name()));
            postValue(networkType);
        }
    };

    @Override
    protected void onActive() {
        super.onActive();
        setValue(NetworkCheck.getNetworkState(mContext));
        Log.d(TAG, "register observer in active state.");
        NetStateChangeReceiver.registerObserver(mObserver);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        Log.d(TAG, "unregister observer in inactive state.");
        NetStateChangeReceiver.unregisterObserver(mObserver);
    }
}
