package com.haerul.manado.ui.login2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.haerul.manado.BuildConfig;
import com.haerul.manado.base.BaseViewModel;
import com.haerul.manado.data.api.ConnectionServer;
import com.haerul.manado.utils.Constants;
import com.haerul.manado.utils.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel2 extends BaseViewModel<LoginViewModel2.LoginNavigator> {

    public String lat = "", lon = "";

    public LoginViewModel2(Context context, ConnectionServer connectionServer) {
        super(context, connectionServer);
    }

    public void onLoginClick() {
        getNavigator().onLogin();
    }

    public void postLogin(JsonObject object, String username, String password) {
        setIsLoading(true);
        getNavigator().showProgress();
        try {

            getConnectionServer().loginCall(object).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                    setIsLoading(false);
                    getNavigator().hideProgress();

                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().get(Constants.STATUS).getAsBoolean()) {
                            JsonObject data = response.body().get("data").getAsJsonObject();

                            Util.putPreference(getContext(), Constants.USER_SID, data.get(Constants.USER_SID).getAsString());
                            Util.putPreference(getContext(), Constants.USER_UID , data.get(Constants.USER_UID ).getAsString());
                            Util.putPreference(getContext(), Constants.USER_NAME , data.get(Constants.USER_NAME ).getAsString());
                            Util.putPreference(getContext(), Constants.USER_PHONE , data.get(Constants.USER_PHONE ).getAsString());
                            Util.putPreference(getContext(), Constants.USER_EMAIL , data.get(Constants.USER_EMAIL ).getAsString());
                            Util.putPreference(getContext(), Constants.USER_UNIT, data.get(Constants.USER_UNIT).getAsString());
                            Util.putPreference(getContext(), Constants.LAST_CONNECT, data.get(Constants.LAST_CONNECT).getAsString());
                            Util.putPreference(getContext(), Constants.USER_BANNER, data.get(Constants.USER_BANNER).getAsString());
                            Util.putPreference(getContext(), Constants.USER_PASSWORD , password);
                            Util.putPreference(getContext(), Constants.USER_LOGIN_NAME , data.get(Constants.USER_LOGIN_NAME ).getAsString());
                            Util.putPreference(getContext(), Constants.IS_ACTIVE , data.get(Constants.IS_ACTIVE ).getAsString());
                            Util.putPreference(getContext(), Constants.USER_ROLE_SID , data.get(Constants.USER_ROLE_SID ).getAsString());
                            Util.putPreference(getContext(), Constants.DATE_CREATED , data.get(Constants.DATE_CREATED ).getAsString());
                            Util.putPreference(getContext(), Constants.DATE_MODIFIED , data.get(Constants.DATE_MODIFIED ).getAsString());
                            Util.putPreference(getContext(), Constants.TOKEN_AUTH , data.get(Constants.TOKEN_AUTH ).getAsString());

                            getNavigator().loginResult(true, response.body().get(Constants.MESSAGE).getAsString());
                        }
                        else {
                            getNavigator().loginResult(false, response.body().get(Constants.MESSAGE).getAsString());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                    t.getLocalizedMessage();
                    setIsLoading(false);
                    getNavigator().hideProgress();
                    getNavigator().loginResult(false, "Network Problem... \n" + t.getLocalizedMessage());
                }
            });

        }
        catch (Exception e) {
            setIsLoading(false);
            getNavigator().loginResult(false, "Network Problem... \n" + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public static class ModelFactory implements ViewModelProvider.Factory {
        private Context context;
        private ConnectionServer connectionServer;
        public ModelFactory(Context context, ConnectionServer connectionServer) {
            this.context = context;
            this.connectionServer = connectionServer;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new LoginViewModel2(context, connectionServer);
        }
    }

    public String getAppVersion() {
        return "App Version " + BuildConfig.VERSION_NAME;
    }

    public interface LoginNavigator {
        void onLogin();
        void loginResult(boolean status, String message);
        void showProgress();
        void hideProgress();
    }
}
