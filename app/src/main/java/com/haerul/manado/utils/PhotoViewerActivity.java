package com.haerul.manado.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.haerul.manado.R;
import com.haerul.manado.base.BaseActivity;
import com.haerul.manado.base.BaseViewModel;
import com.haerul.manado.databinding.ActivityPhotoViewerBinding;


public class PhotoViewerActivity extends BaseActivity<ActivityPhotoViewerBinding, BaseViewModel> {

    private ActivityPhotoViewerBinding binding;

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo_viewer;
    }

    @Override
    public BaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        try {
            byte[] decodedString = getIntent().getByteArrayExtra("data");
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            binding.photo.setImageBitmap(decodedByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        binding.close.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}
