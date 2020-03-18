package com.zhiqing.loadingviewstatemachine.connection;

/**
 * 网络状态变化观察者
 */
public interface NetStateChangeObserver {

    void onNetDisconnected();

    void onNetConnected(NetworkType networkType);
}

