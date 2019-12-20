package com.zhiqing.loadingviewstatemachine.listloadstate;

import android.util.Log;
import android.view.View;
import com.zhiqing.loadingviewstatemachine.R;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LoadStateViewWrapperInternal {
    private final static String TAG = "load_state_view_module";
    LoadStateView mLoadStateView;
    EmptyCategory mDefaultEmptyCategory;
    private Map<String, LoadStateView.EmptyViewAttrEntity> mEmptyViewAttrMap;
    private Map<String, LoadStateView.LoadedFailViewAttrEntity> mLoadedFailViewAttrMap;

    LoadStateViewWrapperInternal(LoadStateView mLoadStateView, EmptyCategory category) {
        this.mLoadStateView = mLoadStateView;
        this.mDefaultEmptyCategory = category;
    }

    void showLoadingView() {
        mLoadStateView.showLoadingView();
    }

    void showEmptyView() {
        mLoadStateView.showEmptyView(mEmptyViewAttrMap.get(mDefaultEmptyCategory.getmCategoryName()));
    }

    void showEmptyView(EmptyCategory category) {
        mLoadStateView.showEmptyView(mEmptyViewAttrMap.get(category.getmCategoryName()));
    }

    void hideListStateView() {
        mLoadStateView.setVisibility(View.GONE);
    }

    void showLoadedFailView(LoadFailCategory category) {
        mLoadStateView.showLoadedFailView(mLoadedFailViewAttrMap.get(category.getmCategoryName()));
    }

    void registerEmptyStateClickHandler(LoadStateView.EmptyStateHandler handler) {
        mLoadStateView.registerEmptyStateClickHandler(handler);
    }

    void unRegisterEmptyStateClickHandler() {
        mLoadStateView.unRegisterEmptyStateClickHandler();
    }

    /**
     * Method that loads the config information.
     */
    boolean loadViewConfig() {
        try {
            // (module name) = (list_state_empty_image) | (list_state_empty_master_tip) | (list_state_empty_slave_tip)
            // | (list_state_empty_master_btn) | (list_state_empty_slave_btn)
            if (mEmptyViewAttrMap == null) {
                Properties emptyViewConfig = new Properties();
                emptyViewConfig.load(
                    mLoadStateView.getContext().getResources().openRawResource(R.raw.empty_view_config));

                mEmptyViewAttrMap = new HashMap<String, LoadStateView.EmptyViewAttrEntity>();
                Enumeration<Object> e = emptyViewConfig.keys();
                while (e.hasMoreElements()) {
                    String emptyCategoryName = (String) e.nextElement();
                    String data = emptyViewConfig.getProperty(emptyCategoryName);
                    String[] datas = data.split(",");
                    for (String theData : datas) {
                        String[] configData = theData.split("\\|");  //$NON-NLS-1$

                        // Create a reference of EmptyViewAttrEntity
                        LoadStateView.EmptyViewAttrEntity emptyViewAttrEntity = new LoadStateView.EmptyViewAttrEntity();
                        emptyViewAttrEntity.setImageRes(configData[0].trim());
                        emptyViewAttrEntity.setMasterTip(configData[1].trim());
                        emptyViewAttrEntity.setSlaveTip(configData[2].trim());
                        emptyViewAttrEntity.setMasterBtnName(configData[3].trim());
                        emptyViewAttrEntity.setSlaveBtnName(configData[4].trim());
                        if (mEmptyViewAttrMap.get(emptyCategoryName) == null) {
                            mEmptyViewAttrMap.put(emptyCategoryName, emptyViewAttrEntity);
                        } else {
                            Log.e(TAG,
                                "LoadStateViewWrapperInternal.java.emptyViewConfig: emptyViewConfig has same module config");
                        }
                    }
                }
            }

            if (mLoadedFailViewAttrMap == null) {
                Properties loadedFailViewConfig = new Properties();
                loadedFailViewConfig.load(
                    mLoadStateView.getContext().getResources().openRawResource(R.raw.loaded_fail_view_config));

                mLoadedFailViewAttrMap = new HashMap<>();
                Enumeration<Object> e = loadedFailViewConfig.keys();
                while (e.hasMoreElements()) {

                    String loadedFailCategoryName = (String) e.nextElement();
                    String data = loadedFailViewConfig.getProperty(loadedFailCategoryName);
                    String[] configData = data.split("\\|");  //$NON-NLS-1$

                    // Create a reference of LoadedFailViewAttrEntity
                    LoadStateView.LoadedFailViewAttrEntity loadedFailViewAttrEntity =
                        new LoadStateView.LoadedFailViewAttrEntity();
                    loadedFailViewAttrEntity.setImageRes(configData[0].trim());
                    loadedFailViewAttrEntity.setMasterTip(configData[1].trim());
                    loadedFailViewAttrEntity.setSlaveTip(configData[2].trim());
                    loadedFailViewAttrEntity.setMasterBtnName(configData[3].trim());
                    loadedFailViewAttrEntity.setSlaveBtnName(configData[4].trim());
                    if (mLoadedFailViewAttrMap.get(loadedFailCategoryName) == null) {
                        mLoadedFailViewAttrMap.put(loadedFailCategoryName, loadedFailViewAttrEntity);
                    } else {
                        Log.e(TAG,
                            "LoadStateViewWrapperInternal.java.loadMimeTypes: loadedFailViewConfig has same module config");
                    }
                }
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Fail to load empty config raw file.", e); //$NON-NLS-1$
            return false;
        }
    }
}
