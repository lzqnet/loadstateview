package com.zhiqing.loadingviewstatemachine.loadstateview;

public enum EmptyCategory {
    EMPTY_CATEGORY_RECENT_NORMAL("recent", Mode.NORMAL),

    EMPTY_CATEGORY_RECENT_FILTER("recent", Mode.FILTER),

    EMPTY_CATEGORY_MY_SPACE_NORMAL("myspace", Mode.NORMAL),

    EMPTY_CATEGORY_MY_SPACE_FILTER("myspace", Mode.FILTER),

    EMPTY_CATEGORY_SHARE_NORMAL("share", Mode.NORMAL),

    EMPTY_CATEGORY_FAVORITE_NORMAL("favorite", Mode.NORMAL),

    EMPTY_CATEGORY_FAVORITE_FILTER("favorite", Mode.FILTER),

    EMPTY_CATEGORY_OFFLINE_NORMAL("offline", Mode.NORMAL),

    EMPTY_CATEGORY_FOLDER_NORMAL("folder", Mode.NORMAL),

    EMPTY_CATEGORY_WIKI_NORMAL("wiki", Mode.NORMAL),

    EMPTY_CATEGORY_UNKNOW("unknow", Mode.NORMAL);

    private final String mCategoryName;
    private Mode mode;

    EmptyCategory(String mCategoryName, Mode mode) {
        this.mCategoryName = mCategoryName;
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }

    public enum Mode {
        NORMAL("normal"),

        FILTER("filter");

        private String modeName;

        Mode(String modeName) {
            this.modeName = modeName;
        }

        public String getModeName() {
            return modeName;
        }

        public void setModeName(String modeName) {
            this.modeName = modeName;
        }
    }
}

