package com.example.pagingtest.service;

import com.example.pagingtest.model.GithubRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author 胡森
 * @description
 * @date 2020-10-27
 */
public interface GithubService {

    @GET("search/repositories")
    Call<GithubRes> query(@Query("q") String query, @Query("sort") String sort,
                                @Query("page") int page, @Query("per_page") int size);
}
