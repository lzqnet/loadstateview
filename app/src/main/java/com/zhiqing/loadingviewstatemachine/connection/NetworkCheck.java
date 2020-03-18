package com.zhiqing.loadingviewstatemachine.connection;

/**
 * Created by wangxin.sidney on 24/11/2017.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by chengguo on 2016/3/17.
 */
public class NetworkCheck {

    private static final String TAG = "NetworkCheck";

    /**
     * 获取当前网络连接类型
     *
     * @param context
     * @return
     */
    public static NetworkType getNetworkState(Context context) {
        try {
            ConnectivityManager manager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info == null || !info.isAvailable()) {
                Log.i(TAG, "getNetworkState: info is null or not available.");
                return NetworkType.NETWORK_NO;
            }
            int type = info.getType();
            if (ConnectivityManager.TYPE_WIFI == type) {
                Log.i(TAG, "getNetworkState: type is wifi");
                return NetworkType.NETWORK_WIFI;
            } else if (ConnectivityManager.TYPE_MOBILE == type) {
                Log.i(TAG, "getNetworkState: type is mobile");
                TelephonyManager mgr =
                        (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                switch (mgr.getNetworkType()) {
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return NetworkType.NETWORK_3G;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return NetworkType.NETWORK_4G;
                    default:
                        return NetworkType.NETWORK_MOBILE;
                }
            } else {
                return NetworkType.NETWORK_MOBILE;
            }
        } catch (Throwable e) {
            Log.e("network", e.getMessage());
            return NetworkType.NETWORK_MOBILE;
        }
    }
}
