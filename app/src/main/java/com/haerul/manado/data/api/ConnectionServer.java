package com.haerul.manado.data.api;

import com.google.gson.JsonObject;
import com.haerul.manado.utils.Constants;

import javax.inject.Inject;

import retrofit2.Call;

public class ConnectionServer {

    @Inject ApiInterface apiInterface;
    
    public ConnectionServer(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }
    
    public Call<JsonObject> loginCall(JsonObject body) {
        return apiInterface.login(body);
    }

    public Call<JsonObject> getUser(String head, String id) {
        return apiInterface.getUser(
                head, id
        );
    }

    public Call<JsonObject> getLinkDb(JsonObject head, JsonObject body) {
        return apiInterface.initDB(
                head.get(Constants.SECURITY_KEY).getAsString(),
                body
        );
    }

    public Call<JsonObject> postInspeksi(String head, JsonObject body) {
        return apiInterface.postInspeksi(head, body);
    }
    
    public Call<JsonObject> postBase64Data(String head,JsonObject body) {
        return apiInterface.postBase64Data(head, body);
    }

    public Call<JsonObject> putInspeksi(String head, JsonObject body) {
        return apiInterface.putInspeksi(head, body);
    }

    public Call<JsonObject> putGangguan(String head, JsonObject body) {
        return apiInterface.putGangguan(head, body);
    }

    public Call<JsonObject> checkDistance(String head, String lat, String lon, String jTemuan) {
        return apiInterface.checkDistance(head, lat, lon, jTemuan);
    }

    public Call<JsonObject> updateProfile(String head, JsonObject object, String id) {
        return apiInterface.putUser(head, object, id);
    }
}
