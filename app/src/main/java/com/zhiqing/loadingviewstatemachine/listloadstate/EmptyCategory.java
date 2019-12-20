package com.zhiqing.loadingviewstatemachine.listloadstate;

public enum EmptyCategory {
    EMPTY_CATEGORY_RECENT("recent", 100), EMPTY_CATEGORY_MY_SPACE("myspace", 101), EMPTY_CATEGORY_SHARE("share",
        102), EMPTY_CATEGORY_FAVORITE("favorite", 103), EMPTY_CATEGORY_OFFLINE("offline", 104), EMPTY_CATEGORY_UNKNOW(
        "unknow", 105);

    private final String mCategoryName;
    private final int mId;

    EmptyCategory(String mCategoryName, int mId) {
        this.mCategoryName = mCategoryName;
        this.mId = mId;
    }

    public static EmptyCategory getListModleByName(String name) {
        switch (name) {
            case "recent":
                return EMPTY_CATEGORY_RECENT;
            case "myspace":
                return EMPTY_CATEGORY_MY_SPACE;
            case "share":
                return EMPTY_CATEGORY_SHARE;
            case "favorite":
                return EMPTY_CATEGORY_FAVORITE;
            case "offline":
                return EMPTY_CATEGORY_OFFLINE;
            default:
                return EMPTY_CATEGORY_UNKNOW;
        }
    }

    public String getmCategoryName() {
        return mCategoryName;
    }

    public int getmId() {
        return mId;
    }
}

