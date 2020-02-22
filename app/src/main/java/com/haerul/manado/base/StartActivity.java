package com.haerul.manado.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;

@SuppressLint("StaticFieldLeak")
public class StartActivity extends AsyncTask<Void, Void, Void> {

    private Intent intent;
    private Context context;
    private View progressBar;

    public StartActivity(Context context, View progressBar, Intent intent) {
        this.context = context;
        this.progressBar = progressBar;
        this.intent = intent;
    }

    public void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setClickable(true);
        progressBar.setFocusable(true);
    }

    public Void doInBackground(Void... unused) {
        context.startActivity(intent);
        return null;
    }

    public void onPostExecute(Void unused) {
        new Handler().postDelayed(() -> progressBar.setVisibility(View.INVISIBLE), 1000);
    }
}
