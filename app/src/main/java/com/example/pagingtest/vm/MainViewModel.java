package com.example.pagingtest.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.pagingtest.datasource.GithubDataSource;
import com.example.pagingtest.datasource.factory.GithubDataSourceFactory;
import com.example.pagingtest.listener.NetworkState;
import com.example.pagingtest.model.GithubRes;

public class MainViewModel extends ViewModel {
    private LiveData<PagedList<GithubRes.Item>> itemPagedList;
    private MutableLiveData<GithubDataSource> mGithubDataSource;
    private NetworkState networkState;

    public void setItemPagedList(String query, String sort) {
        GithubDataSourceFactory factory = new GithubDataSourceFactory(query, sort, networkState);
        mGithubDataSource = factory.getmGithubDataSource();
        PagedList.Config myPagingConfig = new PagedList.Config.Builder()
                .setPageSize(GithubDataSource.PAGE_SIZE)
                .setPrefetchDistance(GithubDataSource.PAGE_SIZE * 2)
                .setEnablePlaceholders(true)
                .build();
        itemPagedList = new LivePagedListBuilder<>(factory, myPagingConfig)
                .build();
    }

    public LiveData<PagedList<GithubRes.Item>> getItemPagedList() {
        return itemPagedList;
    }

    public void setNetworkState(NetworkState networkState) {
        this.networkState = networkState;
    }

    public void invalidate() {
        if (mGithubDataSource != null && mGithubDataSource.getValue() != null)
            mGithubDataSource.getValue().invalidate();
    }
}