package com.zhiqing.loadingviewstatemachine.loadstateview;

import android.annotation.SuppressLint;

import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.zhiqing.loadingviewstatemachine.loadstateview.StateMachine.State;
import com.zhiqing.loadingviewstatemachine.loadstateview.StateMachine.StateMachine;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

class LoadStateMachine extends StateMachine {
    private final static String TAG = LoadStateContants.MODULE_NAME;

    private static final int EVENT_STATE_IDLE = 1;
    private static final int EVENT_STATE_LOADING = 2;

    private static final int EVENT_STATE_LOADED_SUCCESS = 3;
    private static final int EVENT_STATE_UPDATE_EMPTY = 30;

    private static final int EVENT_STATE_LOADED_EMPTY = 4;

    private static final int EVENT_STATE_LOADED_FAIL = 5;
    private static final int EVENT_SHOW_LOADED_FAIL = 6;

    private final DefaultState mDefaultState = new DefaultState();
    private final StartupState mStartupState = new StartupState();
    private final IdleState mIdleState = new IdleState();

    private final LoadedEmptyState mLoadedEmptyState = new LoadedEmptyState();
    private final LoadedFailState mLoadedFailState = new LoadedFailState();
    private final LoadedSuccessState mLoadedSuccessState = new LoadedSuccessState();
    private final LoadedState mLoadedState = new LoadedState();
    private final LoadingState mLoadingState = new LoadingState();
    private final LoadState mLoadState = new LoadState();
    private LoadStateViewWrapperInternal mLoadStateViewWrapperInternal;
    private MutableLiveData<LoadStateCategory> loadStateObserver = new MutableLiveData<>();
    //for debug
    EmptyCategory emptyCategory;

    private boolean isStrictMode = false;

    LoadStateMachine(String name,
                     Looper looper,
                     EmptyCategory defEmptyCategory,
                     LoadStateView view) {
        this(name, looper, view);
        mLoadStateViewWrapperInternal.setEmptyCategory(defEmptyCategory);
    }

    LoadStateMachine(String name, Looper looper, LoadStateView view) {
        super(name, looper);
        mLoadStateViewWrapperInternal = new LoadStateViewWrapperInternal(view);
        addState(mDefaultState);
        addState(mStartupState, mDefaultState);
        addState(mIdleState, mDefaultState);
        addState(mLoadState, mDefaultState);
        addState(mLoadingState, mLoadState);
        addState(mLoadedState, mLoadState);
        addState(mLoadedSuccessState, mLoadedState);
        addState(mLoadedFailState, mLoadedState);
        addState(mLoadedEmptyState, mLoadedSuccessState);
        setInitialState(mStartupState);
    }

    void setEmptyCategory(EmptyCategory category) {
        emptyCategory = category;
        mLoadStateViewWrapperInternal.setEmptyCategory(category);
    }

    void enableStrictMode(boolean isStrictMode) {
        this.isStrictMode = isStrictMode;
    }

    void showLoadingView() {
        sendMessage(EVENT_STATE_LOADING, true);
    }

    void resetStateView() {
        sendMessage(EVENT_STATE_IDLE);
    }

    void updateEmptyView(int dataCount) {
        sendMessage(EVENT_STATE_UPDATE_EMPTY, dataCount);
    }

    void loadedSuccess() {
        sendMessage(EVENT_STATE_LOADED_SUCCESS, Integer.MAX_VALUE);
    }

    void loadedSuccess(int dataCount) {
        sendMessage(EVENT_STATE_LOADED_SUCCESS, dataCount);
    }

    void loadedFail(LoadFailCategory category) {
        sendMessage(EVENT_STATE_LOADED_FAIL, category);
    }

    void startLoading(boolean isShowLoading) {
        sendMessage(EVENT_STATE_LOADING, isShowLoading);
    }

    void registerEmptyStateClickHandler(LoadStateView.EmptyStateHandler handler) {
        mLoadStateViewWrapperInternal.registerEmptyStateClickHandler(handler);
    }

    void unRegisterEmptyStateClickHandler() {
        mLoadStateViewWrapperInternal.unRegisterEmptyStateClickHandler();
    }

    void registerLoadedFailStateClickHandler(LoadStateView.LoadedFailHandler handler) {
        mLoadStateViewWrapperInternal.registerLoadedFailStateClickHandler(handler);
    }

    void unRegisterLoadedFailStateClickHandler() {
        mLoadStateViewWrapperInternal.unRegisterLoadedFailStateClickHandler();
    }

    void registerStateChangeListener(@NonNull LifecycleOwner owner,
                                     @NonNull Observer<LoadStateCategory> observer) {
        loadStateObserver.observe(owner, observer);
    }

    void unRegisterStateChangeListener(@NonNull Observer<LoadStateCategory> observer) {
        loadStateObserver.removeObserver(observer);
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
            case EVENT_STATE_LOADED_FAIL:
                builder.append("what= EVENT_STATE_LOADED_FAIL");
                break;

            case EVENT_SHOW_LOADED_FAIL:
                builder.append("what= EVENT_SHOW_LOADED_FAIL");
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

    private class LoadedEmptyState extends BaseState {
        @Override
        public void enter() {
            Log.d(TAG, emptyCategory + " LoadedEmptyState enter: ");
            super.enter();
            mLoadStateViewWrapperInternal.showEmptyView();
            loadStateObserver.postValue(LoadStateCategory.LOADED_SUCCESS_EMPTY);
        }

        @Override
        public void exit() {
            Log.d(TAG, emptyCategory + " LoadedEmptyState exit: ");
            super.exit();
            mLoadStateViewWrapperInternal.hideListStateView();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.d(TAG, emptyCategory + " LoadedEmptyState processMessage: msg=" + dumpMessage(msg));
            switch (msg.what) {
                case EVENT_STATE_LOADED_EMPTY:
                    Log.d(TAG, emptyCategory
                        + " LoadStateMachine.java.processMessage: LoadedEmptyState  already empty state ");
                    return true;
                default: {
                    if (!isStrictMode) {
                        return super.processMessage(msg);
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    private class LoadedFailState extends BaseState {
        @Override
        public void enter() {
            Log.d(TAG, emptyCategory + " LoadedFailState enter: ");
            super.enter();
        }

        @Override
        public void exit() {
            Log.d(TAG, emptyCategory + " LoadedFailState exit: ");
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.d(TAG, emptyCategory + " LoadedFailState processMessage: msg=" + dumpMessage(msg));
            switch (msg.what) {
                case EVENT_STATE_IDLE:
                    transitionTo(mIdleState);
                    return true;
                case EVENT_SHOW_LOADED_FAIL: {
                    LoadFailCategory category = (LoadFailCategory) msg.obj;
                    mLoadStateViewWrapperInternal.showLoadedFailView(category);
                    return true;
                }
                default: {
                    if (!isStrictMode) {
                        return super.processMessage(msg);
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    private class LoadedSuccessState extends BaseState {
        @Override
        public void enter() {
            Log.d(TAG, emptyCategory + " LoadedSuccessState enter: ");
            super.enter();
            mLoadStateViewWrapperInternal.hideListStateView();
            loadStateObserver.postValue(LoadStateCategory.LOADED_SUCCESS);
        }

        @Override
        public void exit() {
            Log.d(TAG, emptyCategory + " LoadedSuccessState exit: ");
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.d(TAG,
                emptyCategory + " LoadedSuccessState processMessage: msg=" + dumpMessage(msg));
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
                default: {
                    if (!isStrictMode) {
                        return super.processMessage(msg);
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    private class LoadedState extends BaseState {
        @Override
        public void enter() {
            Log.d(TAG, emptyCategory + " LoadedState enter: ");
            super.enter();
        }

        @Override
        public void exit() {
            Log.d(TAG, emptyCategory + " LoadedState exit: ");
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.d(TAG, emptyCategory + " LoadedState processMessage: msg=" + dumpMessage(msg));
            switch (msg.what) {
                case EVENT_STATE_LOADING:
                    transitionTo(mLoadingState);
                    deferMessage(msg);
                    return true;
                default: {
                    if (isStrictMode) {
                        return super.processMessage(msg);
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    private class LoadingState extends BaseState {
        @Override
        public void enter() {
            Log.d(TAG, emptyCategory + " LoadingState enter: ");
            super.enter();
            loadStateObserver.postValue(LoadStateCategory.LOADING);
        }

        @Override
        public void exit() {
            Log.d(TAG, emptyCategory + " LoadingState exit: ");
            super.exit();
            mLoadStateViewWrapperInternal.hideListStateView();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.d(TAG, emptyCategory + " LoadingState .processMessage:  msg= " + dumpMessage(msg));
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
                case EVENT_STATE_LOADED_FAIL: {
                    transitionTo(mLoadedFailState);
                    Message message = Message.obtain(msg);
                    message.what = EVENT_SHOW_LOADED_FAIL;
                    sendMessage(message);
                    return true;
                }

                case EVENT_STATE_LOADING:
                    try {
                        boolean isShowLoading = (boolean) msg.obj;
                        if (isShowLoading) {
                            mLoadStateViewWrapperInternal.showLoadingView();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, emptyCategory + " LoadingState processMessage: ", e);
                    }
                    return true;

                default: {
                    if (!isStrictMode) {
                        return super.processMessage(msg);
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    private class LoadState extends BaseState {
        @Override
        public void enter() {
            Log.d(TAG, emptyCategory + " LoadState enter: ");
            super.enter();
        }

        @Override
        public void exit() {
            Log.d(TAG, emptyCategory + " LoadState exit: ");
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.d(TAG, emptyCategory + " LoadState processMessage: msg=" + dumpMessage(msg));
            return false;//super.processMessage(msg);
        }
    }

    private class IdleState extends BaseState {
        @Override
        public void enter() {
            Log.d(TAG, emptyCategory + " IdleState enter: ");
            super.enter();
            mLoadStateViewWrapperInternal.hideListStateView();
            loadStateObserver.postValue(LoadStateCategory.IDLE);
        }

        @Override
        public void exit() {
            Log.d(TAG, emptyCategory + " IdleState exit: ");
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.d(TAG, emptyCategory + " IdleState processMessage: msg=" + dumpMessage(msg));
            switch (msg.what) {
                case EVENT_STATE_IDLE:
                    return true;
                case EVENT_STATE_LOADING:
                    transitionTo(mLoadingState);
                    deferMessage(msg);
                    return true;

                default: {
                    if (!isStrictMode) {
                        return super.processMessage(msg);
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    private class StartupState extends BaseState {
        boolean loadConfigSuccess = false;

        @SuppressLint("CheckResult")
        @Override
        public void enter() {
            Log.d(TAG, emptyCategory + " StartupState enter: ");
            super.enter();
            loadStateObserver.postValue(LoadStateCategory.STARTUP);
            Flowable.just("").observeOn(Schedulers.io()).map(new Function<String, Boolean>() {
                @Override
                public Boolean apply(String s) throws Exception {
                    return mLoadStateViewWrapperInternal.loadViewConfig();
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean isLoadSuccess) throws Exception {
                    loadConfigSuccess = isLoadSuccess;
                    if (isLoadSuccess) {
                        sendMessage(EVENT_STATE_IDLE);
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    Log.e(TAG, emptyCategory + " StartupState accept: ", throwable);
                }
            });
        }

        @Override
        public void exit() {
            Log.d(TAG, emptyCategory + " StartupState exit: ");
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.d(TAG, emptyCategory + " StartupState processMessage: msg=" + dumpMessage(msg));
            switch (msg.what) {
                case EVENT_STATE_IDLE:
                    if (loadConfigSuccess) {
                        transitionTo(mIdleState);
                    } else {
                        deferMessage(msg);
                    }
                    return true;
                case EVENT_STATE_LOADING:
                case EVENT_STATE_LOADED_SUCCESS:
                case EVENT_STATE_UPDATE_EMPTY:
                case EVENT_STATE_LOADED_EMPTY:
                case EVENT_STATE_LOADED_FAIL:
                case EVENT_SHOW_LOADED_FAIL:
                    deferMessage(msg);
                    return true;
                default:
                    return false;
            }
        }
    }

    private class DefaultState extends BaseState {
        @Override
        public void enter() {
            Log.d(TAG, emptyCategory + " DefaultState enter: ");
            super.enter();
        }

        @Override
        public void exit() {
            Log.d(TAG, emptyCategory + " DefaultState exit: ");
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {

            Log.d(TAG,
                emptyCategory + " DefaultState processMessage: unhandle msg=" + dumpMessage(msg));
            return false;//super.processMessage(msg);
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
            if (isStrictMode) {
                return false;
            }

            switch (msg.what) {
                case EVENT_STATE_IDLE:
                    transitionTo(mIdleState);
                    return true;
                case EVENT_STATE_LOADING:
                    transitionTo(mLoadingState);
                    deferMessage(msg);
                    return true;

                case EVENT_STATE_LOADED_FAIL: {
                    transitionTo(mLoadedFailState);
                    Message message = Message.obtain(msg);
                    message.what = EVENT_SHOW_LOADED_FAIL;
                    sendMessage(message);
                    return true;
                }
                case EVENT_STATE_LOADED_EMPTY:
                    transitionTo(mLoadedEmptyState);
                    return true;
                case EVENT_STATE_UPDATE_EMPTY:
                case EVENT_STATE_LOADED_SUCCESS: {
                    int loadedDataCount = msg.arg1;
                    if (loadedDataCount > 0) {
                        transitionTo(mLoadedSuccessState);
                    } else {
                        transitionTo(mLoadedEmptyState);
                    }
                    return true;
                }

                default:
                    return false;

            }
        }
    }
}

