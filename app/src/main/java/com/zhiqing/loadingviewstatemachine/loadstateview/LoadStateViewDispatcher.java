package com.zhiqing.loadingviewstatemachine.loadstateview;


import android.content.Context;
import android.os.Looper;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import com.zhiqing.loadingviewstatemachine.connection.NetworkCheck;
import com.zhiqing.loadingviewstatemachine.connection.NetworkType;
import java.net.SocketTimeoutException;

public class LoadStateViewDispatcher {
    private final static String TAG = LoadStateContants.MODULE_NAME;

    private LoadStateMachine stateMachine;
    private Context mContext;

    public LoadStateViewDispatcher(LoadStateView stateView, EmptyCategory emptyCategory, Looper looper) {
        this(stateView, looper);
        stateMachine.setEmptyCategory(emptyCategory);
        mContext = stateView.getContext();
    }

    public LoadStateViewDispatcher(LoadStateView stateView, Looper looper) {
        if (stateView == null) {
            throw new RuntimeException("LoadStateView is invalid !!!");
        }
        stateMachine = new LoadStateMachine(TAG, looper, stateView);
    }

    public void init() {
        stateMachine.start();
    }

    public void init(boolean isStrictMode){
        init();
        stateMachine.enableStrictMode(isStrictMode);

    }

    public void setEmptyCategory(EmptyCategory category) {
        Log.d(TAG, "LoadStateViewDispatcher.setEmptyCategory: category=" + category.toString());
        stateMachine.setEmptyCategory(category);
    }

    public void showLoadingView() {
        Log.d(TAG, "LoadStateViewDispatcher.showLoadingView: ");
        stateMachine.showLoadingView();
    }

    public void startLoading(boolean isShowLoading){
        stateMachine.startLoading(isShowLoading);
    }

    public void hideStateView() {
        Log.d(TAG, "LoadStateViewDispatcher.resetStateView: ");
        stateMachine.resetStateView();
    }

    public void updateEmptyView(int dataCount) {
        Log.d(TAG, "LoadStateViewDispatcher.java.updateEmptyView: dataCount= " + dataCount);
        stateMachine.updateEmptyView(dataCount);
    }

    public void loadedSuccess() {
        Log.d(TAG, "LoadStateViewDispatcher.java.loadedSuccess: 58 ");
        stateMachine.loadedSuccess();
    }

    public void loadedSuccess(int dataCount) {
        Log.d(TAG, "LoadStateViewDispatcher.java.loadedSuccess: dataCount= "+dataCount);
        stateMachine.loadedSuccess(dataCount);
    }

    public void loadedSuccessButEmpty() {
        Log.d(TAG, "LoadStateViewDispatcher.java.loadedSuccessButEmpty: 68 ");
        stateMachine.loadedSuccess(0);
    }

    public void loadedFail(LoadFailCategory category) {
        Log.d(TAG, "LoadStateViewDispatcher.java.loadedFail: category= " + category);
        stateMachine.loadedFail(category);
    }

    public void loadedFail(Throwable throwable) {
        Log.d(TAG, "LoadStateViewDispatcher.java.loadedFail: throwable= " + throwable.getMessage());
        if (NetworkCheck.getNetworkState(mContext) == NetworkType.NETWORK_NO) {
            loadedFail(LoadFailCategory.CATEGORY_NO_NETWORK);
        } else if (isTimeout(throwable)) {
            loadedFail(LoadFailCategory.CATEGORY_OVERTIME);
        } else {
            loadedFail(LoadFailCategory.CATEGORY_UN_KNOW);
        }
    }

    private boolean isTimeout(Throwable throwable) {
        //if (throwable instanceof CloudReqFun.OkHttpReqException) {
        //    CloudReqFun.OkHttpReqException exception = (CloudReqFun.OkHttpReqException) throwable;
        //    return exception.exception.equals(SocketTimeoutException.class.getName());
        //}
        return throwable instanceof SocketTimeoutException;
    }

    public void registerEmptyStateClickHandler(LoadStateView.EmptyStateHandler handler) {
        stateMachine.registerEmptyStateClickHandler(handler);
    }

    public void unRegisterEmptyStateClickHandler() {
        stateMachine.unRegisterEmptyStateClickHandler();
    }

    public void registerLoadedFailStateClickHandler(LoadStateView.LoadedFailHandler handler) {
        stateMachine.registerLoadedFailStateClickHandler(handler);
    }

    public void unRegisterLoadedFailStateClickHandler() {
        stateMachine.unRegisterLoadedFailStateClickHandler();
    }

    public void registerStateChangeListener(@NonNull LifecycleOwner owner,
                                            @NonNull Observer<LoadStateCategory> observer) {
        stateMachine.registerStateChangeListener(owner, observer);
    }

    public void unRegisterStateChangeListener(@NonNull Observer<LoadStateCategory> observer) {
        stateMachine.unRegisterStateChangeListener(observer);
    }
}
