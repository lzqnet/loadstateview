package com.zhiqing.loadingviewstatemachine.loadstateview;

public enum LoadFailCategory {
    CATEGORY_NO_NETWORK("nonetwork", 200),

    CATEGORY_OVERTIME("overtime", 201),

    CATEGORY_UN_KNOW("unknow", 202);

    private final String mCategoryName;
    private final int mId;

    LoadFailCategory(String mCategoryName, int mId) {
        this.mCategoryName = mCategoryName;
        this.mId = mId;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }

    public int getmId() {
        return mId;
    }
}
