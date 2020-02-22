package com.haerul.manado.base;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;


import com.haerul.manado.data.api.ConnectionServer;
import com.haerul.manado.data.db.repository.MasterRepository;

import java.lang.ref.WeakReference;

public abstract class BaseViewModel<N> extends ViewModel {

    private final ObservableBoolean isLoading = new ObservableBoolean(false);
    private final ObservableBoolean isConnecting = new ObservableBoolean(false);


    @SuppressLint("StaticFieldLeak")
    private Context context;
    private WeakReference<N> navigator;
   
    private ConnectionServer connectionServer;
    private MasterRepository repository;

    public BaseViewModel(Context context, ConnectionServer connectionServer) {
        this.context = context;
        this.connectionServer = connectionServer;
    }

    public BaseViewModel(Context context, ConnectionServer connectionServer, MasterRepository repository) {
        this.context = context;
        this.connectionServer = connectionServer;
        this.repository = repository;
    }

    public BaseViewModel(Context context) {
        this.context = context;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public ObservableBoolean getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading.set(isLoading);
    }

    public N getNavigator() {
        return navigator.get();
    }

    public void setNavigator(N navigator) {
        this.navigator = new WeakReference<>(navigator);
    }

    public ObservableBoolean getIsConnecting() {
        return isConnecting;
    }

    public void setIsConnecting(boolean isConnecting) {
        this.isConnecting.set(isConnecting);
    }

    public Context getContext() {
        return context;
    }

    public ConnectionServer getConnectionServer() {
        return connectionServer;
    }

    public MasterRepository getRepository() {
        return repository;
    }
    
}
