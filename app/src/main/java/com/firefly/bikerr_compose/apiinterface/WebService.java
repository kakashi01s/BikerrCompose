package com.firefly.bikerr_compose.apiinterface;

import com.firefly.bikerr_compose.model.User;

import io.getstream.chat.android.client.models.Command;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WebService {

    @Headers("Content-Type: application/json")
    @POST("/api/users")
    Call<User> addSession(@Query("email") String email, @Query("password") String password);

//    @GET("/api/devices")
//    Call<List<Device>> getDevices();
//
//    @GET("/api/commandtypes")
//    Call<List<CommandType>> getCommandTypes(@Query("deviceId") long deviceId);
//
//    @POST("/api/commands")
//    Call<Command> sendCommand(@Body Command command);
}