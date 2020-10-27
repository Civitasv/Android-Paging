package com.example.pagingtest.datasource.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.pagingtest.datasource.GithubDataSource;
import com.example.pagingtest.model.GithubRes;
import com.example.pagingtest.service.RetrofitServiceFactory;

/**
 * @author 胡森
 * @description 数据源工厂
 * @date 2020-10-27
 */
public class GithubDataSourceFactory extends DataSource.Factory<Integer, GithubRes.Item> {
    private MutableLiveData<GithubDataSource> mGithubDataSource = new MutableLiveData<>();
    private String query;
    private String sort;

    public GithubDataSourceFactory(String query, String sort) {
        this.query = query;
        this.sort = sort;
    }

    @NonNull
    @Override
    public DataSource<Integer, GithubRes.Item> create() {
        GithubDataSource githubDataSource = new GithubDataSource(RetrofitServiceFactory.getInstance().getGithubService(),query,sort);
        mGithubDataSource.postValue(githubDataSource);
        return githubDataSource;
    }

    public MutableLiveData<GithubDataSource> getmGithubDataSource() {
        return mGithubDataSource;
    }
}
