package com.haerul.manado.data.api;

import com.google.gson.JsonObject;
import com.haerul.manado.data.entity.Base64Data;
import com.haerul.manado.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {
    
    @POST("api/login")
    Call<JsonObject> login(@Body JsonObject jsonObject);

    @POST("api/init_db")
    Call<JsonObject> initDB(
            @Header(Constants.SECURITY_KEY) String auth_token,
            @Body JsonObject jsonObject);
    
    @GET("api/user/{id}")
    Call<JsonObject> getUser(
            @Header(Constants.SECURITY_KEY) String auth_token,
            @Path("id") String user_sid);
    
    @POST("api/inspeksi")
    Call<JsonObject> postInspeksi(
            @Header(Constants.SECURITY_KEY) String auth_token,
            @Body JsonObject body);

    @PUT("api/inspeksi")
    Call<JsonObject> putInspeksi(
            @Header(Constants.SECURITY_KEY) String auth_token,
            @Body JsonObject body);

    @POST("api/base64_data")
    Call<JsonObject> postBase64Data(
            @Header(Constants.SECURITY_KEY) String auth_token,
            @Body JsonObject body);

    @GET("api/base64_data/{data_sid}")
    Call<Base64Data> getBase64Data(
            @Header(Constants.SECURITY_KEY) String auth_token,
            @Path("data_sid") String dataSid);

    @PUT("api/gangguan")
    Call<JsonObject> putGangguan(
            @Header(Constants.SECURITY_KEY) String auth_token,
            @Body JsonObject body);

    @GET("api/checkdistance/{lat}/{lon}/{jtemuan}")
    Call<JsonObject> checkDistance(
            @Header(Constants.SECURITY_KEY) String auth_token,
            @Path("lat") String lat,
            @Path("lon") String lon,
            @Path("jtemuan") String jtemuan);

    @PUT("api/status")
    Call<JsonObject> putLogStataus(
            @Header(Constants.SECURITY_KEY) String auth_token,
            @Body JsonObject body);
    
    @PUT("api/user/{id}")
    Call<JsonObject> putUser(
            @Header(Constants.SECURITY_KEY) String auth_token,
            @Body JsonObject body,
            @Path("id") String id);
}
