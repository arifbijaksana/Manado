package com.haerul.manado.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class DownloadDataSqlite extends AsyncTask<String, Integer, Void> {
    private Context context;
    private ProgressBar progressBar;
    private TextView textProgress;
    private TextView textProgressPercent;
    private DbMaster sqLiteImporter;
    private int progressPercent = 0;
    public static String error;
    public static boolean isSuccess = false;
    
    public DownloadDataSqlite(Context contextf, ProgressBar progressBar, TextView textProgress, TextView textProgressPercent) {
        context = contextf;
        this.progressBar = progressBar;
        this.textProgress = textProgress;
        this.textProgressPercent = textProgressPercent;
        this.textProgress.setVisibility(View.VISIBLE);

        sqLiteImporter = new DbMaster(context);
    }

    public abstract void finishDownload(boolean isSuccess, String message);

    //TODO NANTI INI DIUBAH DOWNLOAD ZIP LAGI YA
    @Override
    protected Void doInBackground(String... arg0) {
        try {
            String patUrl = arg0[0];
            String db = Constants.MASTER_DB;
            URL url = new URL(patUrl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.connect();
            String PATH = Constants.PATH;
            File file = new File(PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            File outputFile = new File(file, db);
            if (outputFile.exists()) {
                outputFile.delete();
            }
            FileOutputStream fos = new FileOutputStream(outputFile, true);
//           InputStream is = c.getInputStream();
            InputStream is = new BufferedInputStream(c.getInputStream(), 1024);
            final int size = c.getContentLength() / (1024);

            publishProgress(0, size);
            int downloaded = 0;

            byte[] buffer = new byte[1024 * 4];
            int len1;
            while ((len1 = is.read(buffer)) != -1) {
                progressPercent += 1;
                downloaded += len1;
                publishProgress(downloaded / (1024));
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();

            sqLiteImporter.importDataBase();
            sqLiteImporter.deleteMasterData();
            isSuccess = true;
            error = "Data has been imported!";


        } catch (Exception e) {
            e.printStackTrace();
            error = "Error: " + e.getMessage();
            isSuccess = false;
        }
        return null;
    }

    /*@Override
    protected Void doInBackground(String... arg0) {
        try {
            String patUrl = arg0[0];
            String db = Constants.MASTER_DB_ZIP;
            URL url = new URL(patUrl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.connect();
            String PATH = Constants.PATH;
            File file = new File(PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            File outputFile = new File(file, db);
            if (outputFile.exists()) {
                outputFile.delete();
            }
            FileOutputStream fos = new FileOutputStream(outputFile, true);
//           InputStream is = c.getInputStream();
            InputStream is = new BufferedInputStream(c.getInputStream(), 1024);
            final int size = c.getContentLength() / (1024);

            publishProgress(0, size);
            int downloaded = 0;

            byte[] buffer = new byte[1024 * 4];
            int len1;
            while ((len1 = is.read(buffer)) != -1) {
                progressPercent += 1;
                downloaded += len1;
                publishProgress(downloaded / (1024));
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();

            File fileZip = new File(Constants.PATH, Constants.MASTER_DB_ZIP);
            sqLiteImporter.extractWithZipInputStream(fileZip,
                    Util.getMd5(Util.getStringPreference(context, Constants.USER_SID))
                            .toCharArray());
            
            sqLiteImporter.importDataBase();
            sqLiteImporter.deleteMasterData();
            isSuccess = true;
            error = "Data has been imported!";

            
        } catch (Exception e) {
            e.printStackTrace();
            error = "Error: " + e.getMessage();
            isSuccess = false;
        }
        return null;
    }*/

    @Override
    protected void onPostExecute(Void respon) {
        finishDownload(isSuccess, error);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (values.length > 1) {
            progressBar.setMax(values[1]);
        }
        progressBar.setProgress(values[0]);
        textProgress.setText(values[0] + "" + "/" + progressBar.getMax() + " KB");
        textProgressPercent.setText(progressPercent + " %");
    }
}