package com.zhiqing.loadingviewstatemachine;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.zhiqing.loadingviewstatemachine.listloadstate.EmptyCategory;
import com.zhiqing.loadingviewstatemachine.listloadstate.LoadFailCategory;
import com.zhiqing.loadingviewstatemachine.listloadstate.LoadStateView;
import com.zhiqing.loadingviewstatemachine.listloadstate.LoadStateViewDispatcher;

public class MainActivity extends AppCompatActivity {
    LoadStateViewDispatcher loadStateViewDispatcher;
    LoadStateView loadStateView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadStateView=(LoadStateView) findViewById(R.id.list_state_view_root);
        loadStateViewDispatcher = new LoadStateViewDispatcher(loadStateView,
            EmptyCategory.EMPTY_CATEGORY_OFFLINE, Looper.getMainLooper());
        loadStateViewDispatcher.registerEmptyStateClickHandler(new LoadStateView.EmptyStateHandler() {
            @Override
            public void masterButtonTouchEvent() {

            }

            @Override
            public void slaveButtonTouchEvent() {

            }
        });
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
