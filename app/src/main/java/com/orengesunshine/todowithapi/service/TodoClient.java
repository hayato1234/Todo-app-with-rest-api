package com.orengesunshine.todowithapi.service;

import com.orengesunshine.todowithapi.model.ResponseMessage;
import com.orengesunshine.todowithapi.model.Task;
import com.orengesunshine.todowithapi.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TodoClient {

    String baseUrl = "your_address";

    @POST("register")
    Call<ResponseMessage> registerUser(@Query("name") String name, @Query("email") String email, @Query("password") String password);

    @POST("login")
    Call<User> loginUser(@Query("email") String email, @Query("password") String password);

    @POST("tasks")
    Call<ResponseMessage> createTask(@Header("Authorization")String api,@Query("task") String task);

    @GET("tasks")
    Call<List<Task>> loadAllTask(@Header("Authorization")String api);
}
