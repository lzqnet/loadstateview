package com.zhiqing.loadingviewstatemachine.listloadstate;

public enum LoadFailCategory {
    CATEGORY_NO_NETWORK("nonewwork", 200), CATEGORY_UN_KNOW("unknow", 201);

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
