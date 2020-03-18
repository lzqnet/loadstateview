package com.zhiqing.loadingviewstatemachine.loadstateview;

import android.util.Log;
import android.view.View;
import com.zhiqing.loadingviewstatemachine.R;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

class LoadStateViewWrapperInternal {
    private final static String TAG = LoadStateContants.MODULE_NAME;
    private LoadStateView mLoadStateView;
    private EmptyCategory mEmptyCategory;
    private Map<String, HashMap<String, LoadStateView.EmptyViewAttrEntity>> mEmptyViewAttrMap;
    private Map<String, LoadStateView.LoadedFailViewAttrEntity> mLoadedFailViewAttrMap;

    LoadStateViewWrapperInternal(LoadStateView mLoadStateView, EmptyCategory category) {
        this(mLoadStateView);
        this.mEmptyCategory = category;
    }

    LoadStateViewWrapperInternal(LoadStateView mLoadStateView) {
        this.mLoadStateView = mLoadStateView;
    }

    void showLoadingView() {
        mLoadStateView.showLoadingView();
    }

    void showEmptyView() {
        if (mEmptyCategory != null) {
            try {
                mLoadStateView.showEmptyView(
                    mEmptyViewAttrMap.get(mEmptyCategory.getmCategoryName())
                    .get(mEmptyCategory.getMode().getModeName()));
            }catch (Exception e){
                Log.w(TAG, "showEmptyView: You should init LoadStateView first.",e);

            }
        } else {
            Log.w( TAG,new RuntimeException("You should init LoadStateView first. "));
        }
    }

    void setEmptyCategory(EmptyCategory category) {
        mEmptyCategory = category;
    }

    void showEmptyView(EmptyCategory category) {
        mEmptyCategory = category;
        try {
            mLoadStateView.showEmptyView(mEmptyViewAttrMap.get(category.getmCategoryName())
                .get(mEmptyCategory.getMode().getModeName()));
        }catch (Exception e){
            Log.w(TAG, "showEmptyView: You should init LoadStateView first.",e);
        }
    }

    void hideListStateView() {
        mLoadStateView.setVisibility(View.INVISIBLE);
    }

    void showLoadedFailView(LoadFailCategory category) {
        try {
            mLoadStateView.showLoadedFailView(mLoadedFailViewAttrMap.get(category.getmCategoryName()));
        }catch (Exception e){
            Log.w(TAG, "showLoadedFailView: You should init LoadStateView first.",e );
        }
    }

    void registerEmptyStateClickHandler(LoadStateView.EmptyStateHandler handler) {
        mLoadStateView.registerEmptyStateClickHandler(handler);
    }

    void unRegisterEmptyStateClickHandler() {
        mLoadStateView.unRegisterEmptyStateClickHandler();
    }

    void registerLoadedFailStateClickHandler(LoadStateView.LoadedFailHandler handler) {
        mLoadStateView.registerLoadedFailStateClickHandler(handler);
    }

    void unRegisterLoadedFailStateClickHandler() {
        mLoadStateView.unRegisterLoadedFailStateClickHandler();
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

                mEmptyViewAttrMap = new HashMap<>();
                Enumeration<Object> e = emptyViewConfig.keys();
                while (e.hasMoreElements()) {
                    String emptyCategoryName = (String) e.nextElement();
                    String data = emptyViewConfig.getProperty(emptyCategoryName);
                    String[] splitData = data.split(",");
                    for (String theData : splitData) {
                        String[] configData = theData.split("\\|");  //$NON-NLS-1$

                        // Create a reference of EmptyViewAttrEntity
                        LoadStateView.EmptyViewAttrEntity emptyViewAttrEntity = new LoadStateView.EmptyViewAttrEntity();
                        emptyViewAttrEntity.setImageRes(configData[1].trim());
                        emptyViewAttrEntity.setMasterTip(configData[2].trim());
                        emptyViewAttrEntity.setSlaveTip(configData[3].trim());
                        emptyViewAttrEntity.setMasterBtnName(configData[4].trim());
                        emptyViewAttrEntity.setSlaveBtnName(configData[5].trim());
                        if (mEmptyViewAttrMap.get(emptyCategoryName) == null) {
                            HashMap<String, LoadStateView.EmptyViewAttrEntity>
                                infoList = new HashMap<>();
                            infoList.put(configData[0].trim(), emptyViewAttrEntity);
                            mEmptyViewAttrMap.put(emptyCategoryName, infoList);
                        } else {
                            mEmptyViewAttrMap.get(emptyCategoryName).put(configData[0].trim(), emptyViewAttrEntity);
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
