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

    // 初始加载|下拉刷新
    void onLoadInitialError(Runnable runnable, String errorMessage);

    // 分页加载|上拉加载
    void onLoadAfterError(Runnable runnable, String errorMessage);

    // 数据加载是否完成
    void onFinish();
}
