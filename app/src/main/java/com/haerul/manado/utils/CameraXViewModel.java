package com.haerul.manado.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.haerul.manado.base.BaseViewModel;

public class CameraXViewModel extends BaseViewModel {
    
    public CameraXViewModel(Context context) {
        super(context);
    }
    
    public static class ModelFactory implements ViewModelProvider.Factory {
        private Context context;
        public ModelFactory(Context context) {
            this.context = context;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new CameraXViewModel(context);
        }
    }
}
