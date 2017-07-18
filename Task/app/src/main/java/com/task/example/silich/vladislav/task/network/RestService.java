package com.task.example.silich.vladislav.task.network;

import com.task.example.silich.vladislav.task.network.responce.ResponceSearchPlaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Lenovo on 18.07.2017.
 */

public interface RestService {
@GET("json")
    Call<ResponceSearchPlaces> getPlace (@Query("location")String location,@Query("radius") int radius,@Query("key") String apiKey);
}
