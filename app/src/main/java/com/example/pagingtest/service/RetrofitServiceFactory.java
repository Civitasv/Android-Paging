package com.example.pagingtest.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author 胡森
 * @description
 * @date 2020-10-27
 */
public class RetrofitServiceFactory {
    private static final RetrofitServiceFactory INSTANCE = new RetrofitServiceFactory();
    private static Retrofit retrofit;

    private RetrofitServiceFactory() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()//
                .retryOnConnectionFailure(true)
                .build();
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl("https://api.github.com/")
                .build();
    }

    public static RetrofitServiceFactory getInstance() {
        return INSTANCE;
    }

    public GithubService getGithubService() {
        return retrofit.create(GithubService.class);
    }
}
