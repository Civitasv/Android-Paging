package com.example.pagingtest.datasource;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.pagingtest.listener.NetworkState;
import com.example.pagingtest.model.GithubRes;
import com.example.pagingtest.service.GithubService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author 胡森
 * @description
 * @date 2020-10-27
 */
public class GithubDataSource extends PageKeyedDataSource<Integer, GithubRes.Item> {
    private static final String TAG = "GithubDataSource";
    private static final int FIRST_PAGE = 1;
    public static final int PAGE_SIZE = 10;
    private GithubService githubService;
    private String query;
    private String sort;
    private NetworkState networkState;

    public GithubDataSource(GithubService githubService, String query, String sort, NetworkState networkState) {
        this.githubService = githubService;
        this.query = query;
        this.sort = sort;
        this.networkState = networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, GithubRes.Item> callback) {
        networkState.onLoading();
        Call<GithubRes> call = githubService.query(query, sort, FIRST_PAGE, PAGE_SIZE);
        try {
            Response<GithubRes> resResponse = call.execute();
            GithubRes res = resResponse.body();

            if (res != null) {
                networkState.onSuccess();
                callback.onResult(res.items, null, FIRST_PAGE + 1);
            } else
                networkState.onRefreshError(() -> loadInitial(params, callback), "访问错误！");
        } catch (IOException e) {
            e.printStackTrace();
            networkState.onRefreshError(() -> loadInitial(params, callback), e.getMessage());
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, GithubRes.Item> callback) {
        Call<GithubRes> call = githubService.query(query, sort, params.key, PAGE_SIZE);
        try {
            Integer key = (params.key > 1) ? params.key - 1 : null;
            GithubRes res = call.execute().body();
            if (res != null) {
                networkState.onSuccess();
                callback.onResult(res.items, key);
            } else networkState.onLoadMoreError(() -> loadBefore(params, callback), "访问错误！");
        } catch (IOException e) {
            e.printStackTrace();
            networkState.onLoadMoreError(() -> loadBefore(params, callback), e.getMessage());
        }
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, GithubRes.Item> callback) {
        Call<GithubRes> call = githubService.query(query, sort, params.key, PAGE_SIZE);
        try {
            GithubRes res = call.execute().body();
            if (res != null) {
                networkState.onSuccess();
                if (!res.complete)
                    callback.onResult(res.items, params.key + 1);
                else callback.onResult(res.items, null);
            } else
                networkState.onLoadMoreError(() -> loadAfter(params, callback), "访问错误！");
        } catch (IOException e) {
            e.printStackTrace();
            // 回调
            networkState.onLoadMoreError(() -> loadAfter(params, callback), e.getMessage());
        }
    }
}
