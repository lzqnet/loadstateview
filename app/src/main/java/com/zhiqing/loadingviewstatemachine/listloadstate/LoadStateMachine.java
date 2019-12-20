package com.zhiqing.loadingviewstatemachine.listloadstate;

import android.annotation.SuppressLint;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.zhiqing.loadingviewstatemachine.listloadstate.StateMachine.State;
import com.zhiqing.loadingviewstatemachine.listloadstate.StateMachine.StateMachine;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

class LoadStateMachine extends StateMachine {
    private final static String TAG = "load_state_view_module";

    private static final int EVENT_STATE_IDLE = 1;
    private static final int EVENT_STATE_LOADING = 2;

    private static final int EVENT_STATE_LOADED_SUCCESS = 3;
    private static final int EVENT_STATE_UPDATE_EMPTY = 30;

    private static final int EVENT_STATE_LOADED_EMPTY = 4;

    //private static final int EVENT_STATE_LOADED_FAIL = 5;
    private static final int EVENT_STATE_LOADED_FAIL_UNKNOW = 6;
    private static final int EVENT_STATE_LOADED_FAIL_NO_NETWORK = 7;
    private final DefaultState mDefaultState = new DefaultState();
    private final StartupState mStartupState = new StartupState();
    private final IdleState mIdleState = new IdleState();
    private final LoadedNoNetworkState mLoadedNoNetworkState = new LoadedNoNetworkState();
    private final LoadedUnKnowFailState mLoadedUnKnowFailState = new LoadedUnKnowFailState();
    private final LoadedEmptyState mLoadedEmptyState = new LoadedEmptyState();
    private final LoadedFailState mLoadedFailState = new LoadedFailState();
    private final LoadedSuccessState mLoadedSuccessState = new LoadedSuccessState();
    private final LoadedState mLoadedState = new LoadedState();
    private final LoadingState mLoadingState = new LoadingState();
    private final LoadState mLoadState = new LoadState();
    private LoadStateViewWrapperInternal mLoadStateViewWrapperInternal;

    LoadStateMachine(String name, Looper looper, EmptyCategory defEmptyCategory, LoadStateView view) {
        super(name, looper);
        mLoadStateViewWrapperInternal = new LoadStateViewWrapperInternal(view, defEmptyCategory);
        addState(mDefaultState);
        addState(mStartupState, mDefaultState);
        addState(mIdleState, mDefaultState);
        addState(mLoadState, mDefaultState);
        addState(mLoadingState, mLoadState);
        addState(mLoadedState, mLoadState);
        addState(mLoadedSuccessState, mLoadedState);
        addState(mLoadedFailState, mLoadedState);
        addState(mLoadedEmptyState, mLoadedSuccessState);
        addState(mLoadedNoNetworkState, mLoadedFailState);
        addState(mLoadedUnKnowFailState, mLoadedFailState);
        setInitialState(mStartupState);
    }

    void showLoadingView() {
        sendMessage(EVENT_STATE_LOADING);
    }

    void hideStateView() {
        sendMessage(EVENT_STATE_IDLE);
    }

    void updateEmptyView(int dataCount) {
        sendMessage(EVENT_STATE_UPDATE_EMPTY, dataCount);
    }

    void loadedSuccess() {
        sendMessage(EVENT_STATE_LOADED_SUCCESS);
    }

    void loadedSuccess(int dataCount) {
        sendMessage(EVENT_STATE_LOADED_SUCCESS, dataCount);
    }

    void loadedFail(LoadFailCategory category) {
        if (category == LoadFailCategory.CATEGORY_NO_NETWORK) {
            sendMessage(EVENT_STATE_LOADED_FAIL_NO_NETWORK);
        } else if (category == LoadFailCategory.CATEGORY_UN_KNOW) {
            sendMessage(EVENT_STATE_LOADED_FAIL_UNKNOW);
        }
    }

    void registerEmptyStateClickHandler(LoadStateView.EmptyStateHandler handler) {
        mLoadStateViewWrapperInternal.registerEmptyStateClickHandler(handler);
    }

    void unRegisterEmptyStateClickHandler() {
        mLoadStateViewWrapperInternal.unRegisterEmptyStateClickHandler();
    }

    private String dumpMessage(Message msg) {
        StringBuilder builder = new StringBuilder();
        builder.append("msg={");
        switch (msg.what) {
            case EVENT_STATE_IDLE:
                builder.append("what= EVENT_STATE_IDLE");
                break;
            case EVENT_STATE_LOADING:
                builder.append("what= EVENT_STATE_LOADING");
                break;
            case EVENT_STATE_LOADED_SUCCESS:
                builder.append("what= EVENT_STATE_LOADED_SUCCESS");
                break;
            case EVENT_STATE_UPDATE_EMPTY:
                builder.append("what= EVENT_STATE_UPDATE_EMPTY");
                break;
            case EVENT_STATE_LOADED_EMPTY:
                builder.append("what= EVENT_STATE_LOADED_EMPTY");
                break;
            case EVENT_STATE_LOADED_FAIL_UNKNOW:
                builder.append("what= EVENT_STATE_LOADED_FAIL_UNKNOW");
                break;

            case EVENT_STATE_LOADED_FAIL_NO_NETWORK:
                builder.append("what= EVENT_STATE_LOADED_FAIL_NO_NETWORK");
                break;

            default:
                builder.append(msg.what);
        }

        builder.append("; arg1=" + msg.arg1).append("; arg2=" + msg.arg2);
        if (msg.obj != null) {
            builder.append("; obj=" + msg.obj.toString() + "}");
        }
        return builder.toString();
    }

    private class LoadedNoNetworkState extends BaseState {
        @Override
        public void enter() {
            super.enter();
            Log.d(TAG, "LoadedNoNetworkState enter: ");
            mLoadStateViewWrapperInternal.showLoadedFailView(LoadFailCategory.CATEGORY_NO_NETWORK);
        }

        @Override
        public void exit() {
            Log.d(TAG, "LoadedNoNetworkState exit: ");
            super.exit();
            mLoadStateViewWrapperInternal.hideListStateView();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.d(TAG, "LoadedNoNetworkState processMessage: msg=" + dumpMessage(msg));
            return super.processMessage(msg);
        }
    }

    private class LoadedUnKnowFailState extends BaseState {
        @Override
        public void enter() {
            Log.d(TAG, "LoadedUnKnowFailState enter: ");
            super.enter();
            mLoadStateViewWrapperInternal.showLoadedFailView(LoadFailCategory.CATEGORY_UN_KNOW);
        }

        @Override
        public void exit() {
            Log.d(TAG, "LoadedUnKnowFailState exit: ");
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.d(TAG, "LoadedUnKnowFailState processMessage: msg=" + dumpMessage(msg));
            return super.processMessage(msg);
        }
    }

    private class LoadedEmptyState extends BaseState {
        @Override
        public void enter() {
            Log.d(TAG, "LoadedEmptyState enter: ");
            super.enter();
            mLoadStateViewWrapperInternal.showEmptyView();
        }

        @Override
        public void exit() {
            Log.d(TAG, "LoadedEmptyState exit: ");
            super.exit();
            mLoadStateViewWrapperInternal.hideListStateView();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.d(TAG, "LoadedEmptyState processMessage: msg=" + dumpMessage(msg));
            switch (msg.what) {
                case EVENT_STATE_LOADED_EMPTY:
                    Log.d(TAG, "LoadStateMachine.java.processMessage: LoadedEmptyState  already empty state ");
                    return true;
            }
            return super.processMessage(msg);
        }
    }

    private class LoadedFailState extends BaseState {
        @Override
        public void enter() {
            Log.d(TAG, "LoadedFailState enter: ");
            super.enter();
        }

        @Override
        public void exit() {
            Log.d(TAG, "LoadedFailState exit: ");
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.d(TAG, "LoadedFailState processMessage: msg=" + dumpMessage(msg));
            return super.processMessage(msg);
        }
    }

    private class LoadedSuccessState extends BaseState {
        @Override
        public void enter() {
            Log.d(TAG, "LoadedSuccessState enter: ");
            super.enter();
            mLoadStateViewWrapperInternal.hideListStateView();
        }

        @Override
        public void exit() {
            Log.d(TAG, "LoadedSuccessState exit: ");
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.d(TAG, "LoadedSuccessState processMessage: msg=" + dumpMessage(msg));
            switch (msg.what) {
                case EVENT_STATE_LOADED_EMPTY:
                    transitionTo(mLoadedEmptyState);
                    return true;
                case EVENT_STATE_UPDATE_EMPTY: {
                    int loadedDataCount = msg.arg1;
                    if (loadedDataCount > 0) {
                        transitionTo(mLoadedSuccessState);
                    } else {
                        transitionTo(mLoadedEmptyState);
                    }
                    return true;
                }
            }
            return super.processMessage(msg);
        }
    }

    private class LoadedState extends BaseState {
        @Override
        public void enter() {
            Log.d(TAG, "LoadedState enter: ");
            super.enter();
        }

        @Override
        public void exit() {
            Log.d(TAG, "LoadedState exit: ");
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.d(TAG, "LoadedState processMessage: msg=" + dumpMessage(msg));
            switch (msg.what) {
                case EVENT_STATE_LOADING:
                    transitionTo(mLoadingState);
                    return true;
            }
            return super.processMessage(msg);
        }
    }

    private class LoadingState extends BaseState {
        @Override
        public void enter() {
            Log.d(TAG, "LoadingState enter: ");
            super.enter();
            mLoadStateViewWrapperInternal.showLoadingView();
        }

        @Override
        public void exit() {
            Log.d(TAG, "LoadingState exit: ");
            super.exit();
            mLoadStateViewWrapperInternal.hideListStateView();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.d(TAG, "LoadingState .processMessage:  msg= " + dumpMessage(msg));
            switch (msg.what) {
                case EVENT_STATE_LOADED_SUCCESS: {
                    int loadedDataCount = msg.arg1;
                    if (loadedDataCount > 0) {
                        transitionTo(mLoadedSuccessState);
                    } else {
                        transitionTo(mLoadedEmptyState);
                    }
                    return true;
                }
                case EVENT_STATE_LOADED_FAIL_NO_NETWORK: {
                    transitionTo(mLoadedNoNetworkState);
                    return true;
                }

                case EVENT_STATE_LOADED_FAIL_UNKNOW:
                    transitionTo(mLoadedUnKnowFailState);
                    break;

                case EVENT_STATE_LOADING:
                    Log.d(TAG, "LoadStateMachine.java.processMessage: LoadingState already in loading state ");
                    return true;
            }
            return super.processMessage(msg);
        }
    }

    private class LoadState extends BaseState {
        @Override
        public void enter() {
            Log.d(TAG, "LoadState enter: ");
            super.enter();
        }

        @Override
        public void exit() {
            Log.d(TAG, "LoadState exit: ");
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.d(TAG, "LoadState processMessage: msg=" + dumpMessage(msg));
            return super.processMessage(msg);
        }
    }

    private class IdleState extends BaseState {
        @Override
        public void enter() {
            Log.d(TAG, "IdleState enter: ");
            super.enter();
            mLoadStateViewWrapperInternal.hideListStateView();
        }

        @Override
        public void exit() {
            Log.d(TAG, "IdleState exit: ");
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.d(TAG, "IdleState processMessage: msg=" + dumpMessage(msg));
            switch (msg.what) {
                case EVENT_STATE_IDLE:
                    return true;
                case EVENT_STATE_LOADING:
                    transitionTo(mLoadingState);
                    return true;
            }
            return super.processMessage(msg);
        }
    }

    private class StartupState extends BaseState {
        @SuppressLint("CheckResult")
        @Override
        public void enter() {
            Log.d(TAG, "StartupState enter: ");
            super.enter();
            Flowable.just("").observeOn(Schedulers.io()).map(new Function<String, Boolean>() {
                @Override
                public Boolean apply(String s) throws Exception {
                    return mLoadStateViewWrapperInternal.loadViewConfig();
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean isLoadSuccess) throws Exception {
                    if (isLoadSuccess) {
                        sendMessage(EVENT_STATE_IDLE);
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    Log.e(TAG, "StartupState accept: ", throwable);
                }
            });
        }

        @Override
        public void exit() {
            Log.d(TAG, "StartupState exit: ");
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.d(TAG, "StartupState processMessage: msg=" + dumpMessage(msg));
            switch (msg.what) {
                case EVENT_STATE_IDLE:
                    transitionTo(mIdleState);
                    return true;
                case EVENT_STATE_LOADING:
                case EVENT_STATE_LOADED_SUCCESS:
                case EVENT_STATE_UPDATE_EMPTY:
                case EVENT_STATE_LOADED_EMPTY:
                case EVENT_STATE_LOADED_FAIL_UNKNOW:
                case EVENT_STATE_LOADED_FAIL_NO_NETWORK:
                    deferMessage(msg);
                    return true;
            }
            return super.processMessage(msg);
        }
    }

    private class DefaultState extends BaseState {
        @Override
        public void enter() {
            Log.d(TAG, "DefaultState enter: ");
            super.enter();
        }

        @Override
        public void exit() {
            Log.d(TAG, "DefaultState exit: ");
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {

            Log.d(TAG, "DefaultState processMessage: unhandle msg=" + dumpMessage(msg));
            return super.processMessage(msg);
        }
    }

    private class BaseState extends State {
        @Override
        public void enter() {
        }

        @Override
        public void exit() {
        }

        @Override
        public boolean processMessage(Message msg) {
            return super.processMessage(msg);
        }
    }
}
