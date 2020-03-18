package com.zhiqing.loadingviewstatemachine.connection;

/**
 * Created by wangxin.sidney on 01/12/2017.
 */

public enum NetworkType {

    NETWORK_WIFI("WiFi"),
    NETWORK_4G("4G"),
    NETWORK_3G("3G"),
    NETWORK_2G("2G"),
    NETWORK_MOBILE("MOBILE"),
    NETWORK_UNKNOWN("Unknown"),
    NETWORK_NO("No network");

    private String desc;
    NetworkType(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}

