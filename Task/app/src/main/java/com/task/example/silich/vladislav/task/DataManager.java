package com.task.example.silich.vladislav.task;

import com.task.example.silich.vladislav.task.network.RestService;
import com.task.example.silich.vladislav.task.network.ServiceGenerator;
import com.task.example.silich.vladislav.task.network.responce.ResponceSearchPlaces;

import retrofit2.Call;

/**
 * Created by Lenovo on 18.07.2017.
 */

public class DataManager {
    private static DataManager INSTANCE = null;
    private RestService service;

    public DataManager(){
        this.service = ServiceGenerator.createService(RestService.class);
    }

    public static DataManager getInstnce (){
        if (INSTANCE == null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }


    public Call<ResponceSearchPlaces> getPlaceReference (String location, int radius, String apiKey){
        return service.getPlace(location,radius,apiKey);
    }

}
