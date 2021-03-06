package com.example.pagingtest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pagingtest.adapter.GithubPagedAdapter;
import com.example.pagingtest.listener.NetworkState;
import com.example.pagingtest.vm.MainViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;
    private GithubPagedAdapter mGithubPagedAdapter;
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        EditText editText = findViewById(R.id.search_repo);
        Button button = findViewById(R.id.search);
        ProgressBar progressBar = findViewById(R.id.progress_circular);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        SmartRefreshLayout refresh = findViewById(R.id.refreshLayout);
        refresh.setRefreshHeader(new ClassicsHeader(this));
        refresh.setRefreshFooter(new ClassicsFooter(this));
        refresh.setOnRefreshListener(refreshLayout -> mainViewModel.invalidate());
        refresh.setEnableLoadMoreWhenContentNotFull(false);
        mainViewModel.setNetworkState(new NetworkState() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    if (refresh.getState() == RefreshState.Refreshing)
                        refresh.finishRefresh(true);
                    if (refresh.getState() == RefreshState.Loading) {
                        refresh.finishLoadMore(true);
                        refresh.setOnLoadMoreListener(null);
                    }
                });
            }

            @Override
            public void onLoading() {
                runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
            }

            @Override
            public void onLoadAfterError(Runnable runnable, String errorMessage) {
                runOnUiThread(() -> {
                    if (refresh.getState() == RefreshState.Loading)
                        refresh.finishLoadMore(false);
                });
                refresh.setOnLoadMoreListener(refreshLayout -> executorService.submit(runnable));
            }

            @Override
            public void onFinish() {
                runOnUiThread(refresh::finishLoadMoreWithNoMoreData);
            }

            @Override
            public void onLoadInitialError(Runnable runnable, String errorMessage) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    if (refresh.getState() == RefreshState.Refreshing)
                        refresh.finishRefresh(false);
                });
                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG)
                        .setAction("RETRY", view -> executorService.submit(runnable)).show();
            }
        });

        button.setOnClickListener(v -> {
            mainViewModel.setItemPagedList(editText.getText().toString(), "stars");
            setRecyclerView(recyclerView);
        });

    }

    private void setRecyclerView(RecyclerView recyclerView) {
        mGithubPagedAdapter = new GithubPagedAdapter(this);
        mainViewModel.getItemPagedList().observe(this, items -> mGithubPagedAdapter.submitList(items));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mGithubPagedAdapter);
    }
}