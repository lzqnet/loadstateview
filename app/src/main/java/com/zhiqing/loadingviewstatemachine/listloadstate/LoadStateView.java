package com.zhiqing.loadingviewstatemachine.listloadstate;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhiqing.loadingviewstatemachine.BearLottieView;
import com.zhiqing.loadingviewstatemachine.R;

public class LoadStateView extends FrameLayout {
    private final static String TAG = "load_state_view_module";

    LoadingViewHolder loadingViewHolder;
    EmptyViewHolder emptyViewHolder;
    LoadedFailViewHolder loadedFailViewHolder;
    EmptyViewAttrEntity currentEmptyViewAttr;

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
    }

    void registerEmptyStateClickHandler(final EmptyStateHandler handler) {
        if (handler == null) {
            return;
        }
        emptyViewHolder.masterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.masterButtonTouchEvent();
            }
        });

        emptyViewHolder.slaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.slaveButtonTouchEvent();
            }
        });
    }

    void unRegisterEmptyStateClickHandler() {
        emptyViewHolder.masterBtn.setOnClickListener(null);
        emptyViewHolder.slaveBtn.setOnClickListener(null);
    }

    void showLoadedFailView(LoadedFailViewAttrEntity attr) {
        if (attr == null) {
            Log.e(TAG, "ListStateView.java.showLoadedFailView: 46 attr is null");
            return;
        }
        setVisibility(VISIBLE);
        loadedFailViewHolder.showLoadedFailView(attr);
        loadingViewHolder.hideLoading();
        emptyViewHolder.hideEmptyView();
    }

    void showLoadingView() {
        setVisibility(VISIBLE);
        loadingViewHolder.showLoading();
        emptyViewHolder.hideEmptyView();
        loadedFailViewHolder.hideLoadedFailView();
    }

    void showEmptyView(EmptyViewAttrEntity attr) {
        setVisibility(VISIBLE);
        if (currentEmptyViewAttr != attr) {
            currentEmptyViewAttr = attr;
            emptyViewHolder.setEmptyViewAttr(attr);
        }
        loadingViewHolder.hideLoading();
        emptyViewHolder.showEmptyView();
        loadedFailViewHolder.hideLoadedFailView();
    }

    public interface EmptyStateHandler {
        void masterButtonTouchEvent();

        void slaveButtonTouchEvent();
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

    private class LoadingViewHolder {
        LinearLayout loadingViewRoot;
        BearLottieView loadingLottieView;

        private LoadingViewHolder() {
            init();
        }

        private void init() {
            loadingViewRoot = findViewById(R.id.list_state_loading_root);
            loadingLottieView = findViewById(R.id.list_state_loading_Lottie_view);
        }

        private void showLoading() {
            loadingViewRoot.setVisibility(VISIBLE);
            if (loadingLottieView != null) {
                loadingLottieView.playAnimation();
            }
        }

        private void hideLoading() {
            loadingViewRoot.setVisibility(GONE);
            if (loadingLottieView != null) {
                loadingLottieView.cancelAnimation();
            }
        }
    }

    private class EmptyViewHolder {
        LinearLayout emptyViewRoot;
        ImageView imageIcon;
        TextView masterTip;
        TextView slaveTip;
        Button masterBtn;
        Button slaveBtn;

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
                Log.e(TAG, "ListStateView.java.setEmptyViewAttr: 83");
                return;
            }
            Resources resources = getResources();
            imageIcon.setImageResource(
                resources.getIdentifier(attr.imageRes, "drawable", getContext().getPackageName()));
            if (!TextUtils.isEmpty(attr.masterTip)) {
                masterTip.setVisibility(VISIBLE);
                masterTip.setText(resources.getIdentifier(attr.masterTip, "string", getContext().getPackageName()));
            } else {
                masterTip.setVisibility(GONE);
            }

            if (!TextUtils.isEmpty(attr.slaveTip)) {
                slaveTip.setVisibility(VISIBLE);
                slaveTip.setText(resources.getIdentifier(attr.slaveTip, "string", getContext().getPackageName()));
            } else {
                slaveTip.setVisibility(GONE);
            }

            if (!TextUtils.isEmpty(attr.masterBtnName)) {
                masterBtn.setVisibility(VISIBLE);
                masterBtn.setText(resources.getIdentifier(attr.masterBtnName, "string", getContext().getPackageName()));
            } else {
                masterBtn.setVisibility(GONE);
            }

            if (!TextUtils.isEmpty(attr.slaveBtnName)) {
                slaveBtn.setVisibility(VISIBLE);
                slaveBtn.setText(resources.getIdentifier(attr.slaveBtnName, "string", getContext().getPackageName()));
            } else {
                slaveBtn.setVisibility(GONE);
            }
        }

        private void showEmptyView() {
            emptyViewRoot.setVisibility(VISIBLE);
        }

        private void hideEmptyView() {
            emptyViewRoot.setVisibility(GONE);
        }
    }

    private class LoadedFailViewHolder {
        LinearLayout loadedFailViewRoot;
        ImageView imageIcon;
        TextView masterTip;
        TextView slaveTip;
        Button masterBtn;
        Button slaveBtn;

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
                Log.e(TAG, "ListStateView.java.setLoadedFailViewAttr: 83");
                return;
            }
            Resources resources = getResources();
            imageIcon.setImageResource(
                resources.getIdentifier(attr.imageRes, "drawable", getContext().getPackageName()));
            if (!TextUtils.isEmpty(attr.masterTip)) {
                masterTip.setVisibility(VISIBLE);
                masterTip.setText(resources.getIdentifier(attr.masterTip, "string", getContext().getPackageName()));
            } else {
                masterTip.setVisibility(GONE);
            }

            if (!TextUtils.isEmpty(attr.slaveTip)) {
                slaveTip.setVisibility(VISIBLE);
                slaveTip.setText(resources.getIdentifier(attr.slaveTip, "string", getContext().getPackageName()));
            } else {
                slaveTip.setVisibility(GONE);
            }

            if (!TextUtils.isEmpty(attr.masterBtnName)) {
                masterBtn.setVisibility(VISIBLE);
                masterBtn.setText(resources.getIdentifier(attr.masterBtnName, "string", getContext().getPackageName()));
            } else {
                masterBtn.setVisibility(GONE);
            }

            if (!TextUtils.isEmpty(attr.slaveBtnName)) {
                slaveBtn.setVisibility(VISIBLE);
                slaveBtn.setText(resources.getIdentifier(attr.slaveBtnName, "string", getContext().getPackageName()));
            } else {
                slaveBtn.setVisibility(GONE);
            }
        }

        private void showLoadedFailView(LoadedFailViewAttrEntity attr) {
            setLoadedFailViewAttr(attr);
            loadedFailViewRoot.setVisibility(VISIBLE);
        }

        private void hideLoadedFailView() {
            loadedFailViewRoot.setVisibility(GONE);
        }
    }
}
