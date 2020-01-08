package com.zhiqing.loadingviewstatemachine;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.zhiqing.loadingviewstatemachine.loadstateview.LoadFailCategory;
import com.zhiqing.loadingviewstatemachine.loadstateview.LoadStateView;
import com.zhiqing.loadingviewstatemachine.loadstateview.LoadStateViewDispatcher;

import static com.zhiqing.loadingviewstatemachine.loadstateview.EmptyCategory.EMPTY_CATEGORY_FAVORITE_FILTER;
import static com.zhiqing.loadingviewstatemachine.loadstateview.EmptyCategory.EMPTY_CATEGORY_FAVORITE_NORMAL;
import static com.zhiqing.loadingviewstatemachine.loadstateview.EmptyCategory.EMPTY_CATEGORY_MY_SPACE_FILTER;
import static com.zhiqing.loadingviewstatemachine.loadstateview.EmptyCategory.EMPTY_CATEGORY_MY_SPACE_NORMAL;
import static com.zhiqing.loadingviewstatemachine.loadstateview.EmptyCategory.EMPTY_CATEGORY_OFFLINE_NORMAL;
import static com.zhiqing.loadingviewstatemachine.loadstateview.EmptyCategory.EMPTY_CATEGORY_RECENT_FILTER;
import static com.zhiqing.loadingviewstatemachine.loadstateview.EmptyCategory.EMPTY_CATEGORY_RECENT_NORMAL;
import static com.zhiqing.loadingviewstatemachine.loadstateview.EmptyCategory.EMPTY_CATEGORY_SHARE_NORMAL;
import static com.zhiqing.loadingviewstatemachine.loadstateview.EmptyCategory.EMPTY_CATEGORY_UNKNOW;

public class MainActivity extends AppCompatActivity {
    LoadStateViewDispatcher loadStateViewDispatcher;
    LoadStateView loadStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: {
                        loadStateViewDispatcher.setEmptyCategory(EMPTY_CATEGORY_RECENT_NORMAL);
                        break;
                    }
                    case 1: {
                        loadStateViewDispatcher.setEmptyCategory(EMPTY_CATEGORY_RECENT_FILTER);
                        break;
                    }
                    case 2: {
                        loadStateViewDispatcher.setEmptyCategory(EMPTY_CATEGORY_MY_SPACE_NORMAL);
                        break;
                    }
                    case 3: {
                        loadStateViewDispatcher.setEmptyCategory(EMPTY_CATEGORY_MY_SPACE_FILTER);
                        break;
                    }
                    case 4: {
                        loadStateViewDispatcher.setEmptyCategory(EMPTY_CATEGORY_SHARE_NORMAL);
                        break;
                    }
                    //case 5: {
                    //    //loadStateViewDispatcher.setEmptyCategory(EMPTY_CATEGORY_SHARE_FILTER);
                    //    break;
                    //}
                    case 6: {
                        loadStateViewDispatcher.setEmptyCategory(EMPTY_CATEGORY_FAVORITE_NORMAL);
                        break;
                    }
                    case 7: {
                        loadStateViewDispatcher.setEmptyCategory(EMPTY_CATEGORY_FAVORITE_FILTER);
                        break;
                    }
                    case 8: {
                        loadStateViewDispatcher.setEmptyCategory(EMPTY_CATEGORY_OFFLINE_NORMAL);
                        break;
                    }

                    //case 9: {
                    //    //loadStateViewDispatcher.setEmptyCategory(EMPTY_CATEGORY_OFFLINE_FILTER);
                    //    break;
                    //}
                    case 10: {
                        loadStateViewDispatcher.setEmptyCategory(EMPTY_CATEGORY_UNKNOW);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        loadStateView = (LoadStateView)

            findViewById(R.id.list_state_view_root);

        loadStateViewDispatcher =
            new LoadStateViewDispatcher(loadStateView, EMPTY_CATEGORY_RECENT_NORMAL, Looper.getMainLooper());
        loadStateViewDispatcher.registerEmptyStateClickHandler(new LoadStateView.EmptyStateHandler() {

            @Override
            public void onMasterButtonTouchEvent() {

            }

            @Override
            public void onSlaveButtonTouchEvent() {

            }
        });
        loadStateViewDispatcher.init();
    }

    public void showLoadingView(View view) {
        Toast.makeText(this, "已点击", Toast.LENGTH_SHORT).show();
        loadStateViewDispatcher.showLoadingView();
    }

    public void loadedSuccessHasData(View view) {
        Toast.makeText(this, "已点击", Toast.LENGTH_SHORT).show();

        loadStateViewDispatcher.loadedSuccess(10);
    }

    public void loadedSuccessNoData(View view) {
        Toast.makeText(this, "已点击", Toast.LENGTH_SHORT).show();

        loadStateViewDispatcher.loadedSuccess();
    }

    public void loadedFailNoNetwork(View view) {
        Toast.makeText(this, "已点击", Toast.LENGTH_SHORT).show();

        loadStateViewDispatcher.loadedFail(LoadFailCategory.CATEGORY_NO_NETWORK);
    }

    public void loadedFailUnknow(View view) {
        Toast.makeText(this, "已点击", Toast.LENGTH_SHORT).show();

        loadStateViewDispatcher.loadedFail(LoadFailCategory.CATEGORY_UN_KNOW);
    }

    public void updateEmptyViewHasData(View view) {
        Toast.makeText(this, "已点击", Toast.LENGTH_SHORT).show();

        loadStateViewDispatcher.updateEmptyView(100);
    }

    public void updateEmptyViewNoData(View view) {
        Toast.makeText(this, "已点击", Toast.LENGTH_SHORT).show();

        loadStateViewDispatcher.updateEmptyView(0);
    }

    public void idle(View view) {
        Toast.makeText(this, "已点击", Toast.LENGTH_SHORT).show();

        loadStateViewDispatcher.hideStateView();
    }
}
