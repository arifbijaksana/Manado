package com.haerul.manado.di.module;

import com.haerul.manado.App;
import com.haerul.manado.BuildConfig;
import com.haerul.manado.data.api.ApiInterface;
import com.haerul.manado.data.api.ConnectionServer;
import com.haerul.manado.utils.Constants;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Provides @Singleton
    ConnectionServer provideConnectionServer(ApiInterface apiInterface) {
        return new ConnectionServer(apiInterface);
    }

    @Provides @Singleton
    Interceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor localHttpLoggingInterceptor = new HttpLoggingInterceptor();
        localHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return localHttpLoggingInterceptor;
    }

    @Provides @Singleton
    OkHttpClient provideOkHttp(App application) {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder builder = chain.request().newBuilder();
                    builder.addHeader(Constants.CONTENT_TYPE, Constants.APP_JSON);
                    builder.method(original.method(), original.body());
                    return chain.proceed(builder.build());
                })
                .addNetworkInterceptor(provideLoggingInterceptor())
                .build();
    }

    @Provides @Singleton
    Retrofit provideRetrofitClient(App application) {
        return new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .client(provideOkHttp(application))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides @Singleton ApiInterface provideApiInterface(Retrofit retrofit) {
        return retrofit.create(ApiInterface.class);
    }
}