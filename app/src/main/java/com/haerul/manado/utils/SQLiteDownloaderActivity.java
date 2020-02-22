package com.haerul.manado.utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.haerul.manado.R;
import com.haerul.manado.base.BaseActivity;
import com.haerul.manado.data.api.ConnectionServer;
import com.haerul.manado.data.db.repository.MasterRepository;
import com.haerul.manado.databinding.ActivityDownloaderBinding;
import com.haerul.manado.ui.MainActivity;

import javax.inject.Inject;

public class SQLiteDownloaderActivity extends BaseActivity<ActivityDownloaderBinding, SQLiteDownloaderViewModel> 
        implements SQLiteDownloaderViewModel.DownloadNavigator {

    @Inject
    ConnectionServer connectionServer;
    @Inject
    MasterRepository repository;
    
    ActivityDownloaderBinding binding;
    SQLiteDownloaderViewModel viewModel;
    
    private boolean syncStatus;
    
    @Override
    public int getBindingVariable() {
        return com.haerul.manado.BR._all;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_downloader;
    }

    @Override
    public SQLiteDownloaderViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewModel = ViewModelProviders.of(this, new SQLiteDownloaderViewModel.ModelFactory(getApplicationContext(), connectionServer, repository)).get(SQLiteDownloaderViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.setNavigator(this);
        
        viewModel.refreshToken();
        
        binding.tryAgain.setOnClickListener(v -> {
            reCreateActivity();
        });
    }

    private void reCreateActivity() {
        Util.deleteDatabaseFile(this);
        Intent intent = new Intent(this, SQLiteDownloaderActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    @SuppressLint("StaticFieldLeak")
    private void donwloadDbMaster(@NonNull String dbUrl) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        new DownloadDataSqlite(this, binding.progressHorizontal, binding.textProgress, binding.textProgressPercent) {
            @Override
            public void finishDownload(boolean isSuccess, String message) {
                if (!isSuccess) {
                    binding.textTitle.setText("Download failed!");
                    binding.progressHorizontal.setProgress(0);
                    binding.textProgress.setText("0/0");
                    Snackbar.make(binding.cardLogin, message, Snackbar.LENGTH_SHORT).show();

                    new Handler().postDelayed(() -> {
                        binding.tryAgain.setVisibility(View.VISIBLE);
                    }, 100);
                } else {
                    binding.textTitle.setText("Download Successfully..");
                    Snackbar.make(binding.cardLogin, message, Snackbar.LENGTH_SHORT).show();

                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(SQLiteDownloaderActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }, 200);
                }
            }
        }.execute(dbUrl, Constants.MASTER_DB);
    }

    @Override
    public void downloadResult(boolean status, String message) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (status) {
            donwloadDbMaster(Util.getStringPreference(this, Constants.DB_URL));
        } 
        else {
            binding.textTitle.setText("We can't downloading some data!");
            Util.showDialog(this, "Sync failed!", message);
            
            new Handler().postDelayed(() -> {
                binding.textTitle.setText("Please try again");
                binding.tryAgain.setVisibility(View.VISIBLE);
            }, 200);
        }
    }
}
