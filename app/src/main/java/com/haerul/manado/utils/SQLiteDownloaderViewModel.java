package com.haerul.manado.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.haerul.manado.BuildConfig;
import com.haerul.manado.base.BaseViewModel;
import com.haerul.manado.data.api.ConnectionServer;
import com.haerul.manado.data.db.repository.MasterRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SQLiteDownloaderViewModel extends BaseViewModel<SQLiteDownloaderViewModel.DownloadNavigator> {

    public SQLiteDownloaderViewModel(Context context, ConnectionServer connectionServer, MasterRepository repository) {
        super(context, connectionServer, repository);
    }

    //get link for download sqlite
    public void getDbLink() {
        setIsLoading(true);

        JsonObject header = new JsonObject();
        header.addProperty(Constants.SECURITY_KEY, Util.getStringPreference(getContext(), Constants.TOKEN_AUTH));

        JsonObject body = new JsonObject();
        body.addProperty(Constants.USER_SID, Util.getStringPreference(getContext(), Constants.USER_SID));

        getConnectionServer().getLinkDb(header, body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                Log.v("TAG", "response" + response.toString());
                setIsLoading(false);

                if (response.isSuccessful()) {
                    if (!response.body().get(Constants.STATUS).getAsBoolean() && response.body().get(Constants.MESSAGE).getAsString().equals(Constants.TOKEN_EXPIRED)) {
                        refreshToken();
                    } 
                    else if (!response.body().get(Constants.STATUS).getAsBoolean() && response.body().get(Constants.MESSAGE).getAsString() != null) {
                        getNavigator().downloadResult(false, "Download failed! \n" + response.body().get(Constants.MESSAGE).getAsString());
                    } 
                    else {
                        JsonObject data = response.body().get("data").getAsJsonObject();
                        Util.putPreference(getContext(), Constants.DB_URL, data.get(Constants.DB_URL).getAsString());
                        Util.putPreference(getContext(), Constants.DB_NAME, data.get(Constants.DB_NAME).getAsString());
                        Util.putPreference(getContext(), Constants.DB_VERSION, data.get(Constants.DB_VERSION).getAsString());
                        Util.putPreference(getContext(), Constants.IS_LOGIN, true);

                        getNavigator().downloadResult(true, "Downloading...");
                    }
                } else {
                    getNavigator().downloadResult(false, "Message : " + response.message() + 
                            "\nResponse code : " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.v("TAG", "failure");
                t.printStackTrace();
                getNavigator().downloadResult(false, "Download filed, Network Problem! " + t.getLocalizedMessage());
                setIsLoading(false);
            }
        });
    }

    public void refreshToken() {
        try {
            JsonObject objectInfo = new JsonObject();
            objectInfo.addProperty("brand", Build.BRAND);
            objectInfo.addProperty("model", Build.MODEL);
            objectInfo.addProperty("sdk", Build.VERSION.SDK_INT);
            objectInfo.addProperty("release", Build.VERSION.RELEASE);
            objectInfo.addProperty("codename", Build.VERSION.CODENAME);
            objectInfo.addProperty("version_app", BuildConfig.VERSION_NAME);

            JsonObject object = new JsonObject();
            object.addProperty(Constants.USER_LOGIN_NAME, Util.getStringPreference(getContext(), Constants.USER_LOGIN_NAME));
            object.addProperty(Constants.USER_PASSWORD, Util.getStringPreference(getContext(), Constants.USER_PASSWORD));
            object.addProperty("lat_login", Util.getStringPreference(getContext(), Constants.LAT_CONNECT));
            object.addProperty("lon_login", Util.getStringPreference(getContext(), Constants.LON_CONNECT));
            object.addProperty("device_info", objectInfo.toString());
            
            getConnectionServer().loginCall(object).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().get(Constants.STATUS).getAsBoolean()) {

                            JsonObject data = response.body().get("data").getAsJsonObject();

                            Util.putPreference(getContext(), Constants.USER_SID, data.get(Constants.USER_SID).getAsString());
                            Util.putPreference(getContext(), Constants.USER_UID, data.get(Constants.USER_UID).getAsString());
                            Util.putPreference(getContext(), Constants.USER_NAME, data.get(Constants.USER_NAME).getAsString());
                            Util.putPreference(getContext(), Constants.USER_PHONE, data.get(Constants.USER_PHONE).getAsString());
                            Util.putPreference(getContext(), Constants.LAST_CONNECT, data.get(Constants.LAST_CONNECT).getAsString());
                            Util.putPreference(getContext(), Constants.USER_EMAIL, data.get(Constants.USER_EMAIL).getAsString());
                            Util.putPreference(getContext(), Constants.USER_BANNER, data.get(Constants.USER_BANNER).getAsString());
                            Util.putPreference(getContext(), Constants.USER_UNIT, data.get(Constants.USER_UNIT).getAsString());
                            Util.putPreference(getContext(), Constants.IS_ACTIVE, data.get(Constants.IS_ACTIVE).getAsString());
                            Util.putPreference(getContext(), Constants.DATE_CREATED, data.get(Constants.DATE_CREATED).getAsString());
                            Util.putPreference(getContext(), Constants.DATE_MODIFIED, data.get(Constants.DATE_MODIFIED).getAsString());
                            Util.putPreference(getContext(), Constants.USER_LOGIN_NAME, data.get(Constants.USER_LOGIN_NAME).getAsString());
                            Util.putPreference(getContext(), Constants.TOKEN_AUTH, data.get(Constants.TOKEN_AUTH).getAsString());

                            getDbLink();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                    t.getLocalizedMessage();
                }
            });

        } catch (Exception e) {
            setIsLoading(false);
            e.printStackTrace();
        }
    }

    public static class ModelFactory implements ViewModelProvider.Factory {
        private Context context;
        private ConnectionServer connectionServer;
        private MasterRepository repository;

        public ModelFactory(Context context, ConnectionServer connectionServer, MasterRepository repository) {
            this.context = context;
            this.connectionServer = connectionServer;
            this.repository = repository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new SQLiteDownloaderViewModel(context, connectionServer, repository);
        }
    }

    public interface DownloadNavigator {
        void downloadResult(boolean status, String message);
    }
}
