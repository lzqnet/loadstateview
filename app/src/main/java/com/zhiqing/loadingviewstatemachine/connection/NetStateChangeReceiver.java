package com.zhiqing.loadingviewstatemachine.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.List;

/**
 * 监听网络状态变化的BroadcastReceiver
 */
public class NetStateChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "NetStateChangeReceiver";
    private static final String CONNECTIVITY_ACTION = "com.bytedance.ee.bear.conn.CONNECTIVITY_CHANGE";

    private static class InstanceHolder {
        private static final NetStateChangeReceiver INSTANCE = new NetStateChangeReceiver();
    }

    private List<NetStateChangeObserver> mObservers = new ArrayList<>();

    public NetStateChangeReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive()... intent = " + intent);
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())
                || CONNECTIVITY_ACTION.equals(intent.getAction())) {
            NetworkType networkType = NetworkCheck.getNetworkState(context);
            notifyObservers(networkType);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static class NetworkCallback23 extends ConnectivityManager.NetworkCallback {
        private Context mContext;

        NetworkCallback23(Context context) {
            mContext = context;
        }

        @Override
        public void onAvailable(Network network) {
            Log.d(TAG, "onAvailable: " + network);
            Intent intent = new Intent();
            intent.setAction(CONNECTIVITY_ACTION);
            intent.putExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            mContext.sendBroadcast(intent);
        }

        @Override
        public void onLost(Network network) {
            Log.d(TAG, "onLost: " + network);
            Intent intent = new Intent();
            intent.setAction(CONNECTIVITY_ACTION);
            intent.putExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, true);
            mContext.sendBroadcast(intent);
        }
    }

    /**
     * 注册网络监听
     */
    public static void registerReceiver(@NonNull final Context context) {
        if (Build.VERSION.SDK_INT > 22) {
            Log.d(TAG, "registerReceiver() for api higher than 22 ... context = " + context);
            final ConnectivityManager
                connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkRequest.Builder builder = new NetworkRequest.Builder();
                connectivityManager.registerNetworkCallback(builder.build(), new NetworkCallback23(context));
            }

            IntentFilter intentFilter = new IntentFilter(CONNECTIVITY_ACTION);
            context.registerReceiver(InstanceHolder.INSTANCE, intentFilter);
        } else {
            Log.d(TAG, "registerReceiver()... context = " + context);
            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(InstanceHolder.INSTANCE, intentFilter);
        }
    }

    /**
     * 取消网络监听
     */
    public static void unregisterReceiver(@NonNull Context context) {
        Log.d(TAG, "unregisterReceiver()... context = " + context);
        context.unregisterReceiver(InstanceHolder.INSTANCE);
    }

    /**
     * 注册网络变化Observer
     */
    public static void registerObserver(NetStateChangeObserver observer) {
        Log.d(TAG, "registerObserver()... observer =" + observer);
        if (observer == null) {
            return;
        }
        if (!InstanceHolder.INSTANCE.mObservers.contains(observer)) {
            InstanceHolder.INSTANCE.mObservers.add(observer);
        }
    }

    /**
     * 取消网络变化Observer的注册
     */
    public static void unregisterObserver(NetStateChangeObserver observer) {
        Log.d(TAG, "unregisterObserver()... observer = " + observer);
        if (observer == null) {
            return;
        }
        if (InstanceHolder.INSTANCE.mObservers == null)
            return;
        InstanceHolder.INSTANCE.mObservers.remove(observer);
    }

    /**
     * 通知所有的Observer网络状态变化
     */
    private void notifyObservers(NetworkType networkType) {
        Log.e(TAG, "notifyObservers().... netWorkType = " +
                (networkType == null ? null : networkType.name())
                + ", observer.size =" + mObservers.size());
        if (networkType == NetworkType.NETWORK_NO) {
            for (NetStateChangeObserver observer : mObservers) {
                Log.d(TAG, "notifyObservers()1111... observer = " + observer);
                observer.onNetDisconnected();
            }
        } else {
            for (NetStateChangeObserver observer : mObservers) {
                Log.d(TAG, "notifyObservers()2222... observer = " + observer);
                observer.onNetConnected(networkType);
            }
        }
    }
}

