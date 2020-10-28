package com.example.pagingtest.listener;

/**
 * @author 胡森
 * @description
 * @date 2020-10-28
 */
public interface NetworkState {
    // 加载成功
    void onSuccess();

    // 加载中
    void onLoading();

    // 分页加载失败
    void onLoadMoreError(Runnable runnable, String errorMessage);

    // 刷新失败
    void onRefreshError(Runnable runnable, String errorMessage);
}
