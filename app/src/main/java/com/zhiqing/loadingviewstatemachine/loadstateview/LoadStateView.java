package com.zhiqing.loadingviewstatemachine.loadstateview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zhiqing.loadingviewstatemachine.BearLottieView;
import com.zhiqing.loadingviewstatemachine.R;

public class LoadStateView extends FrameLayout {
    private final static String TAG = LoadStateContants.MODULE_NAME;

    LoadingViewHolder loadingViewHolder;
    EmptyViewHolder emptyViewHolder;
    LoadedFailViewHolder loadedFailViewHolder;
    EmptyViewAttrEntity currentEmptyViewAttr;
    LoadedFailViewAttrEntity currentLoadedFailEntity;
    CurrentViewType currentViewType = CurrentViewType.NONE;
    ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            if (currentViewType != CurrentViewType.NONE && getVisibility() == VISIBLE) {
                updateLoadStateView();
            }
            return true;
        }
    };

    public LoadStateView(Context context) {
        super(context);
        init();
    }

    public LoadStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.load_state_layout, this);
        loadingViewHolder = new LoadingViewHolder();
        emptyViewHolder = new EmptyViewHolder();
        loadedFailViewHolder = new LoadedFailViewHolder();
        getViewTreeObserver().addOnPreDrawListener(preDrawListener);
    }

    private void updateLoadStateView() {
        switch (currentViewType) {
            case NONE:
                break;
            case EMPTY:
                showEmptyView(currentEmptyViewAttr);
                break;
            case LOADED_FAIL:
                showLoadedFailView(currentLoadedFailEntity);
                break;
            case LOADING:
                showLoadingView();
                break;
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == INVISIBLE || visibility == GONE) {
            currentViewType = CurrentViewType.NONE;
            loadingViewHolder.hideLoading();
            loadedFailViewHolder.hideLoadedFailView();
            emptyViewHolder.hideEmptyView();
        }
    }

    private void showAndAdjustLayoutPosition(IViewHolder holder) {
        if (holder == null) {
            return;
        }

        if (getVisibility() == VISIBLE) {
            Rect rect = new Rect();
            if (getGlobalVisibleRect(rect) && rect.height() > 0) {
                try {
                    View view = holder.getRootView();
                    int viewMeasuredHeight = view.getMeasuredHeight();
                    int marginTop = (rect.height() - viewMeasuredHeight) / 2;
                    if (marginTop > 0 && Math.abs(marginTop - holder.getCurrentMarginTop()) > 5) {
                        holder.setCurrentMarginTop(marginTop);
                        LayoutParams params = (LayoutParams) view.getLayoutParams();
                        params.setMargins(0, marginTop, 0, 0);
                        view.setLayoutParams(params);
                    }
                    if (view.getVisibility() != VISIBLE) {
                        view.setVisibility(VISIBLE);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "showAndAdjustLayoutPosition: ", e);
                }
            }
        }
    }

    void registerEmptyStateClickHandler(final EmptyStateHandler handler) {
        if (handler == null) {
            Log.e(TAG, "registerEmptyStateClickHandler: EmptyStateHandler is invalid");
            return;
        }
        emptyViewHolder.masterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.onMasterButtonTouchEvent();
            }
        });

        emptyViewHolder.slaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.onSlaveButtonTouchEvent();
            }
        });
    }

    void unRegisterEmptyStateClickHandler() {
        emptyViewHolder.masterBtn.setOnClickListener(null);
        emptyViewHolder.slaveBtn.setOnClickListener(null);
    }

    void registerLoadedFailStateClickHandler(final LoadStateView.LoadedFailHandler handler) {
        if (handler == null) {
            Log.w(TAG, "registerLoadedFailStateClickHandler: LoadedFailHandler is invalid");
            return;
        }
        loadedFailViewHolder.loadedFailViewRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.onTouchEvent();
            }
        });
    }

    void unRegisterLoadedFailStateClickHandler() {
        loadedFailViewHolder.loadedFailViewRoot.setOnClickListener(null);
    }

    void showLoadedFailView(LoadedFailViewAttrEntity attr) {
        if (attr == null) {
            Log.w(TAG, "ListStateView.java.showLoadedFailView: 46 attr is null");
            return;
        }
        currentViewType = CurrentViewType.LOADED_FAIL;
        currentLoadedFailEntity = attr;
        setVisibility(VISIBLE);
        loadingViewHolder.hideLoading();
        emptyViewHolder.hideEmptyView();
        loadedFailViewHolder.showLoadedFailView(attr);
    }

    void showLoadingView() {
        currentViewType = CurrentViewType.LOADING;
        setVisibility(VISIBLE);
        emptyViewHolder.hideEmptyView();
        loadedFailViewHolder.hideLoadedFailView();
        loadingViewHolder.showLoading();
    }

    void showEmptyView(EmptyViewAttrEntity attr) {
        if (attr == null) {
            Log.w(TAG, "ListStateView.java.showEmptyView: currentEmptyViewAttr="
                + currentEmptyViewAttr
                + " attr="
                + attr);
            return;
        }
        currentViewType = CurrentViewType.EMPTY;
        currentEmptyViewAttr = attr;
        setVisibility(VISIBLE);
        loadingViewHolder.hideLoading();
        loadedFailViewHolder.hideLoadedFailView();
        emptyViewHolder.showEmptyView(attr);
    }

    public interface EmptyStateHandler {
        void onMasterButtonTouchEvent();

        void onSlaveButtonTouchEvent();
    }

    public interface LoadedFailHandler {
        void onTouchEvent();
    }

    public static class EmptyViewAttrEntity {
        private EmptyCategory emptyCategory;
        private String imageRes;
        private String masterTip;
        private String slaveTip;
        private String masterBtnName;
        private String slaveBtnName;

        public EmptyCategory getEmptyCategory() {
            return emptyCategory;
        }

        public void setEmptyCategory(EmptyCategory emptyCategory) {
            this.emptyCategory = emptyCategory;
        }

        public String getImageRes() {
            return imageRes;
        }

        public void setImageRes(String imageRes) {
            this.imageRes = imageRes;
        }

        public String getMasterTip() {
            return masterTip;
        }

        public void setMasterTip(String masterTip) {
            this.masterTip = masterTip;
        }

        public String getSlaveTip() {
            return slaveTip;
        }

        public void setSlaveTip(String slaveTip) {
            this.slaveTip = slaveTip;
        }

        public String getMasterBtnName() {
            return masterBtnName;
        }

        public void setMasterBtnName(String masterBtnName) {
            this.masterBtnName = masterBtnName;
        }

        public String getSlaveBtnName() {
            return slaveBtnName;
        }

        public void setSlaveBtnName(String slaveBtnName) {
            this.slaveBtnName = slaveBtnName;
        }
    }

    public static class LoadedFailViewAttrEntity {
        private LoadFailCategory category;
        private String imageRes;
        private String masterTip;
        private String slaveTip;
        private String masterBtnName;
        private String slaveBtnName;

        public LoadFailCategory getCategory() {
            return category;
        }

        public void setCategory(LoadFailCategory category) {
            this.category = category;
        }

        public String getImageRes() {
            return imageRes;
        }

        public void setImageRes(String imageRes) {
            this.imageRes = imageRes;
        }

        public String getMasterTip() {
            return masterTip;
        }

        public void setMasterTip(String masterTip) {
            this.masterTip = masterTip;
        }

        public String getSlaveTip() {
            return slaveTip;
        }

        public void setSlaveTip(String slaveTip) {
            this.slaveTip = slaveTip;
        }

        public String getMasterBtnName() {
            return masterBtnName;
        }

        public void setMasterBtnName(String masterBtnName) {
            this.masterBtnName = masterBtnName;
        }

        public String getSlaveBtnName() {
            return slaveBtnName;
        }

        public void setSlaveBtnName(String slaveBtnName) {
            this.slaveBtnName = slaveBtnName;
        }
    }

    private class LoadingViewHolder implements IViewHolder {
        RelativeLayout loadingViewRoot;
        BearLottieView loadingLottieView;
        int currentMarginTop;

        private LoadingViewHolder() {
            init();
        }

        private void init() {
            loadingViewRoot = findViewById(R.id.list_state_loading_root);
            loadingLottieView = findViewById(R.id.list_state_loading_Lottie_view);
        }

        @Override
        public int getCurrentMarginTop() {
            return currentMarginTop;
        }

        @Override
        public void setCurrentMarginTop(int value) {
            currentMarginTop = value;
        }

        private void showLoading() {
            showAndAdjustLayoutPosition(this);
            if (loadingLottieView != null && !loadingLottieView.isAnimating()) {
                loadingLottieView.playAnimation();
            }
        }

        @Override
        public View getRootView() {
            return loadingViewRoot;
        }

        private void hideLoading() {
            currentMarginTop = 0;
            loadingViewRoot.setVisibility(GONE);
            if (loadingLottieView != null && loadingLottieView.isAnimating()) {
                loadingLottieView.cancelAnimation();
            }
        }
    }

    private class EmptyViewHolder implements IViewHolder {
        LinearLayout emptyViewRoot;
        ImageView imageIcon;
        TextView masterTip;
        TextView slaveTip;
        Button masterBtn;
        Button slaveBtn;
        int currentMarginTop;

        private EmptyViewHolder() {
            init();
        }

        private void init() {
            emptyViewRoot = findViewById(R.id.list_state_empty_root);
            imageIcon = findViewById(R.id.list_state_empty_image);
            masterTip = findViewById(R.id.list_state_empty_master_tip);
            slaveTip = findViewById(R.id.list_state_empty_slave_tip);
            masterBtn = findViewById(R.id.list_state_empty_master_btn);
            slaveBtn = findViewById(R.id.list_state_empty_slave_btn);
        }

        private void setEmptyViewAttr(EmptyViewAttrEntity attr) {
            if (attr == null) {
                Log.w(TAG, "ListStateView.java.setEmptyViewAttr: currentEmptyViewAttr="
                    + currentEmptyViewAttr
                    + " attr="
                    + attr);
                return;
            }

            Resources resources = getResources();
            try {
                imageIcon.setImageResource(resources.getIdentifier(attr.imageRes, "drawable",
                    getContext().getPackageName()));
            }catch (Exception e){
                Log.e(TAG, "setEmptyViewAttr: attr.imageRes="+attr.imageRes );
            }
            if (!TextUtils.isEmpty(attr.masterTip)) {
                masterTip.setVisibility(VISIBLE);
                try {
                    masterTip.setText(resources.getIdentifier(attr.masterTip, "string",
                        getContext().getPackageName()));
                } catch (Exception e) {
                    Log.e(TAG,
                        "setEmptyViewAttr: fetch resource fail:attr.masterTip=" + attr.masterTip);
                }
            } else {
                masterTip.setVisibility(GONE);
            }

            if (!TextUtils.isEmpty(attr.slaveTip)) {
                slaveTip.setVisibility(VISIBLE);
                try {
                    slaveTip.setText(resources.getIdentifier(attr.slaveTip, "string",
                        getContext().getPackageName()));
                } catch (Exception e) {
                    Log.e(TAG,
                        "setEmptyViewAttr: fetch resource fail: attr.slaveTip=" + attr.slaveTip);
                }
            } else {
                slaveTip.setVisibility(GONE);
            }

            if (!TextUtils.isEmpty(attr.masterBtnName)) {
                masterBtn.setVisibility(VISIBLE);
                try {
                    masterBtn.setText(resources.getIdentifier(attr.masterBtnName, "string",
                        getContext().getPackageName()));
                } catch (Exception e) {
                    Log.e(TAG, "setEmptyViewAttr: fetch resource fail:  attr.masterBtnName="
                        + attr.masterBtnName);
                }
            } else {
                masterBtn.setVisibility(GONE);
            }

            if (!TextUtils.isEmpty(attr.slaveBtnName)) {
                slaveBtn.setVisibility(VISIBLE);
                try {
                    slaveBtn.setText(resources.getIdentifier(attr.slaveBtnName, "string",
                        getContext().getPackageName()));
                } catch (Exception e) {
                    Log.e(TAG, "setEmptyViewAttr: fetch resource fail: attr.slaveBtnName="
                        + attr.slaveBtnName);
                }
            } else {
                slaveBtn.setVisibility(GONE);
            }
        }

        @Override
        public int getCurrentMarginTop() {
            return currentMarginTop;
        }

        @Override
        public void setCurrentMarginTop(int value) {
            currentMarginTop = value;
        }

        @Override
        public View getRootView() {
            return emptyViewRoot;
        }

        private void showEmptyView(EmptyViewAttrEntity attr) {
            setEmptyViewAttr(attr);
            showAndAdjustLayoutPosition(this);
        }

        private void hideEmptyView() {
            currentMarginTop = 0;
            emptyViewRoot.setVisibility(GONE);
        }
    }

    private class LoadedFailViewHolder implements IViewHolder {
        LinearLayout loadedFailViewRoot;
        ImageView imageIcon;
        TextView masterTip;
        TextView slaveTip;
        Button masterBtn;
        Button slaveBtn;
        int currentMarginTop;

        private LoadedFailViewHolder() {
            init();
        }

        private void init() {
            loadedFailViewRoot = findViewById(R.id.list_state_loaded_fail_root);
            imageIcon = findViewById(R.id.list_state_loaded_fail_image);
            masterTip = findViewById(R.id.list_state_loaded_fail_master_tip);
            slaveTip = findViewById(R.id.list_state_loaded_fail_slave_tip);
            masterBtn = findViewById(R.id.list_state_loaded_fail_master_btn);
            slaveBtn = findViewById(R.id.list_state_loaded_fail_slave_btn);
        }

        private void setLoadedFailViewAttr(LoadedFailViewAttrEntity attr) {
            if (attr == null) {
                Log.w(TAG, "ListStateView.java.setLoadedFailViewAttr: attr="
                    + attr
                    + " currentLoadedFailEntity="
                    + currentLoadedFailEntity);
                return;
            }
            Resources resources = getResources();
            try {
                imageIcon.setImageResource(resources.getIdentifier(attr.imageRes, "drawable",
                    getContext().getPackageName()));
            }catch (Exception e){
                Log.e(TAG, "setLoadedFailViewAttr: attr.imageRes="+attr.imageRes );
            }
            if (!TextUtils.isEmpty(attr.masterTip)) {
                masterTip.setVisibility(VISIBLE);
                try {
                    masterTip.setText(resources.getIdentifier(attr.masterTip, "string",
                        getContext().getPackageName()));
                } catch (Exception e) {
                    Log.e(TAG, "setLoadedFailViewAttr: fetch resource fail:attr.masterTip="
                        + attr.masterTip);
                }
            } else {
                masterTip.setVisibility(GONE);
            }

            if (!TextUtils.isEmpty(attr.slaveTip)) {
                slaveTip.setVisibility(VISIBLE);
                try {
                    slaveTip.setText(resources.getIdentifier(attr.slaveTip, "string",
                        getContext().getPackageName()));
                } catch (Exception e) {
                    Log.e(TAG, "setLoadedFailViewAttr: fetch resource fail: attr.slaveTip="
                        + attr.slaveTip);
                }
            } else {
                slaveTip.setVisibility(GONE);
            }

            if (!TextUtils.isEmpty(attr.masterBtnName)) {
                masterBtn.setVisibility(VISIBLE);
                try {
                    masterBtn.setText(resources.getIdentifier(attr.masterBtnName, "string",
                        getContext().getPackageName()));
                } catch (Exception e) {
                    Log.e(TAG, "setLoadedFailViewAttr: fetch resource fail:  attr.masterBtnName="
                        + attr.masterBtnName);
                }
            } else {
                masterBtn.setVisibility(GONE);
            }

            if (!TextUtils.isEmpty(attr.slaveBtnName)) {
                slaveBtn.setVisibility(VISIBLE);
                try {
                    slaveBtn.setText(resources.getIdentifier(attr.slaveBtnName, "string",
                        getContext().getPackageName()));
                } catch (Exception e) {
                    Log.e(TAG, "setLoadedFailViewAttr: fetch resource fail: attr.slaveBtnName="
                        + attr.slaveBtnName);
                }
            } else {
                slaveBtn.setVisibility(GONE);
            }
        }

        @Override
        public int getCurrentMarginTop() {
            return currentMarginTop;
        }

        @Override
        public void setCurrentMarginTop(int value) {
            currentMarginTop = value;
        }

        @Override
        public View getRootView() {
            return loadedFailViewRoot;
        }

        private void showLoadedFailView(LoadedFailViewAttrEntity attr) {
            setLoadedFailViewAttr(attr);
            showAndAdjustLayoutPosition(this);
        }

        private void hideLoadedFailView() {
            currentMarginTop = 0;
            loadedFailViewRoot.setVisibility(GONE);
        }
    }

    private interface IViewHolder {
        int getCurrentMarginTop();

        void setCurrentMarginTop(int value);

        View getRootView();
    }

    private enum CurrentViewType {
        NONE, LOADING, LOADED_FAIL, EMPTY
    }
}
