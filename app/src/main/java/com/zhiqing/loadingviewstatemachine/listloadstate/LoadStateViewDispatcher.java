package com.zhiqing.loadingviewstatemachine.listloadstate;

import android.os.Looper;

public class LoadStateViewDispatcher {
    private final static String TAG = "load_state_view_module";

    private LoadStateMachine stateMachine;

    public LoadStateViewDispatcher(LoadStateView stateView, EmptyCategory emptyCategory, Looper looper) {
        stateMachine = new LoadStateMachine(emptyCategory.getmCategoryName(), looper, emptyCategory, stateView);
        stateMachine.start();
    }

    public void showLoadingView() {
        stateMachine.showLoadingView();
    }

    public void hideStateView() {
        stateMachine.hideStateView();
    }

    public void updateEmptyView(int dataCount) {
        stateMachine.updateEmptyView(dataCount);
    }

    public void loadedSuccess() {
        stateMachine.loadedSuccess();
    }

    public void loadedSuccess(int dataCount) {
        stateMachine.loadedSuccess(dataCount);
    }

    public void loadedFail(LoadFailCategory category) {
        stateMachine.loadedFail(category);
    }

    public void registerEmptyStateClickHandler(LoadStateView.EmptyStateHandler handler) {
        stateMachine.registerEmptyStateClickHandler(handler);
    }

    public void unRegisterEmptyStateClickHandler() {
        stateMachine.unRegisterEmptyStateClickHandler();
    }
}
