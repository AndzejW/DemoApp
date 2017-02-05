package com.andrzej.demoapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Andrzej on 04.02.2017.
 */

public interface MyWebService {
    String URL = "https://api.github.com";

    @GET("/users/google/repos")
    Call<List<Element>> getElement();
}
