package com.haerul.manado.utils;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.haerul.manado.R;
import com.haerul.manado.data.api.ApiInterface;
import com.haerul.manado.data.db.MasterDatabase;
import com.haerul.manado.data.db.repository.MasterRepository;
import com.haerul.manado.data.entity.Base64Data;
import com.haerul.manado.data.entity.GenericReferences;
import com.haerul.manado.data.entity.UserRoles;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static com.haerul.manado.BuildConfig.BASE_URL;

public final class Util {
    
    public static String getStringPreference(Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString(key, null);
    }

    public static int getIntegerPreference(Context context, int key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getInt(String.valueOf(key), 0);
    }

    public static long getLongPreference(Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getLong(key, 0);
    }

    public static int getIntegerPreference(Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getInt(key, 0);
    }

    public static boolean getBooleanPreference(Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getBoolean(key, false);
    }

    public static void putPreference(Context context, String key, String value) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(key, value);
        editor.apply();
        editor.commit();
    }

    public static void putPreference(Context context, String key, float value) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putFloat(key, value);
        editor.apply();
        editor.commit();
    }

    public static void putPreference(Context context, String key, long value) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putLong(key, value);
        editor.apply();
        editor.commit();
    }

    public static void putPreference(Context context, String key, boolean value) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
        editor.commit();
    }

    public static void putPreference(Context context, String key, int value) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(key, value);
        editor.apply();
        editor.commit();
    }

    public static String dateFormatter(String Date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        java.util.Date value = null;
        try {
            value = formatter.parse(Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        return dateFormatter.format(value);
    }

    public static String dateFormatterDateOnly(String Date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        java.util.Date value = null;
        try {
            value = formatter.parse(Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM yyyy");
        return dateFormatter.format(value);
    }

    public static String dateFormatter(String Date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        java.util.Date value = null;
        try {
            value = formatter.parse(Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
        return dateFormatter.format(value);
    }

    public static String dateFormatter2(String Date) {
        if (Date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date value = null;
            try {
                value = formatter.parse(Date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            return dateFormatter.format(value);
        }
        return "-";
    }

    public static String dateFormatter2(String Date, String format) {
        if (Date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date value = null;
            try {
                value = formatter.parse(Date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
            return dateFormatter.format(value);
        }
        return "-";
    }

    public static void scrollToView(View view, NestedScrollView nestedScroll) {
        int[] viewLocation = {0, 0};
        view.getLocationOnScreen(viewLocation);
        int[] scrollLocation = {0, 0};
        nestedScroll.getLocationInWindow(scrollLocation);
        int y = nestedScroll.getScrollY() - (nestedScroll.getHeight() / 2);
        new Handler().post(() -> nestedScroll.smoothScrollTo(0, y + viewLocation[1] - scrollLocation[1]));
    }

    /*public static void scrollToView(View view, StickyScrollView nestedScroll) {
        int[] viewLocation = {0, 0};
        view.getLocationOnScreen(viewLocation);
        int[] scrollLocation = {0, 0};
        nestedScroll.getLocationInWindow(scrollLocation);
        int y = nestedScroll.getScrollY() - (nestedScroll.getHeight() / 2);
        new Handler().post(() -> nestedScroll.smoothScrollTo(0, y + viewLocation[1] - scrollLocation[1]));
    }*/

    public static void setErrorSipnner(Spinner mySpinner, String errorMessage) {
        TextView errorText = (TextView) mySpinner.getSelectedView();
        errorText.setError("");
        errorText.setTextColor(Color.RED);//just to highlight that this is an error
        errorText.setText(errorMessage);//changes the selected item text to this
    }

    public static boolean checkFileDatabase(Context context, String databaseName) {
        boolean isAvailable = false;
        File databases = new File(context.getApplicationInfo().dataDir + "/databases");
        File db = new File(databases, databaseName);
        if (db.exists()) {
            System.out.println("Database ada");
            File journal = new File(databases, databaseName + "-journal");
            if (journal.exists()) {
                System.out.println("Database journal ada");
                File shm = new File(databases, databaseName + "-shm");
                if (shm.exists()) {
                    System.out.println("Database journal ada");
                    File wal = new File(databases, databaseName + "-wal");
                    if (wal.exists()) {
                        System.out.println("Database journal ada");
                        isAvailable = true;
                    } else {
                        isAvailable = false;
                    }
                } else {
                    isAvailable = false;
                }
            } else {
                isAvailable = false;
            }
        } else {
            isAvailable = false;
        }
        return isAvailable;
    }

    public static void deleteDatabaseFile(Context context) {
        String databaseName = Constants.MASTER_DB;
        File databases = new File(context.getApplicationInfo().dataDir + "/databases");
        File db = new File(databases, databaseName);
        if (db.delete()) {
            System.out.println("Database deleted");
        } else {
            System.out.println("Failed to delete database");
        }

        File journal = new File(databases, databaseName + "-journal");
        if (journal.exists()) {
            if (journal.delete()) {
                System.out.println("Database journal deleted");
            } else {
                System.out.println("Failed to delete database journal");
            }
        }

        File shm = new File(databases, databaseName + "-shm");
        if (shm.exists()) {
            if (shm.delete()) {
                System.out.println("Database journal deleted");
            } else {
                System.out.println("Failed to delete database shm");
            }
        }

        File wal = new File(databases, databaseName + "-wal");
        if (wal.exists()) {
            if (wal.delete()) {
                System.out.println("Database journal deleted");
            } else {
                System.out.println("Failed to delete database wal");
            }
        }
    }

    public static boolean importDb(Context context, String addName) throws IOException {
        if (!addName.equals("")) {
            File res = new File(Constants.PATH);
            if (!res.exists()) {
                res.mkdirs();
            }
            File file = new File(Constants.PATH, Constants.MASTER_DB + addName);
            if (!file.exists()) {
                file.createNewFile();
            }
        }
        InputStream externalDbStream = new FileInputStream(Constants.PATH + Constants.MASTER_DB + addName); // Environment.getExternalStorageDirectory() + "/Sadix-CRM/"; MASTER_DB.db
        String outFileName = String.format("//data//data//%s//databases//", context.getPackageName()) + Constants.MASTER_DB + addName; // //data//data//%s//databases//", packageName MASTER_DB.db
        OutputStream localDbStream = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }
        Log.w("TAG", "database imported");
        localDbStream.close();
        externalDbStream.close();
        return true;
    }


    public static void deleteMasterData(Context context) {
        File file = new File(Constants.PATH + Constants.MASTER_DB);
        if (file.exists()) {
            file.delete();
            Log.w("TAG", "database deleted");
        }
        file = null;
    }


    public static String getTimestampNow() {
        return new SimpleDateFormat(Constants.TIMESTAMP_FORMAT, Locale.US).format(new Date());
    }

    public static String getFileNameFromURL(String url) {
        if (url == null) {
            return "";
        }
        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                // handle ...example.com
                return "";
            }
        } catch (MalformedURLException e) {
            return "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        // find end index for ?
        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        // find end index for #
        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        // calculate the end index
        int endIndex = Math.min(lastQMPos, lastHashPos);
        return url.substring(startIndex, endIndex);
    }

    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void expandFast(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration(300);
        v.startAnimation(a);
    }

    public static void collapseFast(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration(300);
        v.startAnimation(a);
    }

    public static String replaceComma(String data) {
        return data.replace(".", ",");
    }

    public static Bitmap setImageBase64(String imageBase64) {
        if (imageBase64 != null) {
            byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        return null;
    }

    public static boolean isValidEmail(String string) {
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public static boolean isNullOrEmpty(String string) {
        return TextUtils.isEmpty(string);
    }

    public static String formatDecimal(double number) {
        DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
        formatter.setMaximumFractionDigits(4);
        formatter.setMinimumFractionDigits(4);
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        return formatter.format(number);
    }

    public static String formatDecimal2(double number) {
        DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        return formatter.format(number);
    }

    public static boolean isNumeric(String string) {
        return TextUtils.isDigitsOnly(string);
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String encoder(File originalFile) {
        String encodedBase64 = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
            byte[] bytes = new byte[(int) originalFile.length()];
            fileInputStreamReader.read(bytes);
            encodedBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedBase64;
    }

    public static void saveImage(final Context context, final String base64String, final String ext) {
        String dir = Constants.PATH_DOWNLOAD;
        String filename = "/download." + ext;
        File dirs = new File(dir);
        File file = new File(dirs + filename);
        if (!file.exists()) {
            try {
                dirs.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(Base64.decode(base64String, Base64.DEFAULT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
            //open file

            if (file.exists()) {
                MimeTypeMap map = MimeTypeMap.getSingleton();
                String extension = MimeTypeMap.getFileExtensionFromUrl(file.getName());
                String type = map.getMimeTypeFromExtension(extension);

                if (type == null)
                    type = "*/*";

                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.fromFile(file);

                intent.setDataAndType(data, type);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);

                Intent chooser = Intent.createChooser(intent, "Open with : ");
                try {
                    context.startActivity(chooser);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "Failed to open file", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveImage(final Context context, final String base64String, final String name, final String ext) {
        String dir = Constants.PATH_DOWNLOAD;
        String filename = "/" + name + ext;
        File dirs = new File(dir);
        File file = new File(dirs + filename);
        if (!file.exists()) {
            try {
                dirs.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(Base64.decode(base64String, Base64.DEFAULT));
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static String getRealPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static void showDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogRoundStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    public static void Toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void confirmDialog(Context context, final String title, final String message, final ConfirmListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context, R.style.DialogRoundStyle).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                if (listener != null) {
                    listener.onDialogCompleted(true);
                }
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CLOSE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                if (listener != null) {
                    listener.onDialogCompleted(false);
                }
            }
        });
        dialog.show();
    }

    public static void confirmDialogGangguanHapus(Context context, final String title, final String message, final ConfirmListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context, R.style.DialogRoundStyle).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "HAPUS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                if (listener != null) {
                    listener.onDialogCompleted(true);
                }
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "PERTAHANKAN", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                if (listener != null) {
                    listener.onDialogCompleted(false);
                }
            }
        });
        dialog.show();
    }

    public static void okDialog(Context context, final String title, final String message, final OkListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context, R.style.DialogRoundStyle).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                if (listener != null) {
                    listener.onOk();
                }
            }
        });
        dialog.show();
    }

    public static void info(Context context, String info) {
        showDialog(context, "INFO", info);
    }

    public static void error(Context context, String error) {
        showDialog(context, "ERROR", error);
    }

    public static ContentValues JSONtoContentValues(JSONObject json) throws Exception {
        ContentValues cv = new ContentValues();
        Iterator keys = json.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            cv.put(key, json.getString(key));
        }
        return cv;
    }

    public static String getUserSID(Context baseActivity) {
        return Util.getStringPreference(baseActivity, Constants.USER_SID);
    }

    public static String getToken(Context baseActivity) {
        return Util.getStringPreference(baseActivity, Constants.TOKEN_AUTH);
    }

    public static boolean isLevelWilayah(Context context) {
        for (GenericReferences genericReferences : Util.getRole(context)) {
            if (genericReferences.ref_value == 3 || Util.getUlp(context).ref_value == 2 || Util.getUlp(context).ref_value == 1) {
                return true;
            }
        }
        return false;
    }

    public interface LocationListener {
        void location(String alamat);
    }

    public interface ConfirmListener {
        void onDialogCompleted(boolean answer);
    }

    public interface OkListener {
        void onOk();
    }

    public static boolean isServiceRunning(Context context, String className) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (className.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static byte[] readFile(String file) throws IOException {
        return readFile(new File(file));
    }

    public static byte[] readFile(File file) throws IOException {
        ByteArrayOutputStream ous = null;
        FileInputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException e) {
            }

            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }
        return ous.toByteArray();
    }

    public static String getCurrentLocationViaJSON(double lat, double lng, String key) {
        JSONObject jsonObj = getLocationInfo(lat, lng, key);
        String currentLocation = null;
        try {
            String status = jsonObj.getString("status");
            if (status.equalsIgnoreCase("OK")) {
                JSONArray results = jsonObj.getJSONArray("results");
                if (results.length() > 0) {
                    JSONObject json = results.getJSONObject(0);
                    currentLocation = json.getString("formatted_address");
                }
                return currentLocation;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getLocationInfo(double lat, double lng, String key) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String dataUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&sensor=true&language=id&key=" + key;
            Log.i("dataUrl", dataUrl);
            URL url = new URL(dataUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream stream = connection.getInputStream();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public static void saveFile(File file, String text) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            fos.flush();
            fos.close();
            fos = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getUriRealPath(Context ctx, Uri uri) {
        String ret = "";

        if (isAboveKitKat()) {
            // Android OS above sdk version 19.
            ret = getUriRealPathAboveKitkat(ctx, uri);
        } else {
            // Android OS below sdk version 19
            ret = getImageRealPath(ctx.getContentResolver(), uri, null);
        }

        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String getUriRealPathAboveKitkat(Context ctx, Uri uri) {
        String ret = "";

        if (ctx != null && uri != null) {

            if (isContentUri(uri)) {
                if (isGooglePhotoDoc(uri.getAuthority())) {
                    ret = uri.getLastPathSegment();
                } else {
                    ret = getImageRealPath(ctx.getContentResolver(), uri, null);
                }
            } else if (isFileUri(uri)) {
                ret = uri.getPath();
            } else if (isDocumentUri(ctx, uri)) {

                // Get uri related document id.
                String documentId = DocumentsContract.getDocumentId(uri);

                // Get uri authority.
                String uriAuthority = uri.getAuthority();

                if (isMediaDoc(uriAuthority)) {
                    String[] idArr = documentId.split(":");
                    if (idArr.length == 2) {
                        // First item is document type.
                        String docType = idArr[0];

                        // Second item is document real id.
                        String realDocId = idArr[1];

                        // Get content uri by document type.
                        Uri mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        if ("image".equals(docType)) {
                            mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else if ("video".equals(docType)) {
                            mediaContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } else if ("audio".equals(docType)) {
                            mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        // Get where clause with real document id.
                        String whereClause = MediaStore.Images.Media._ID + " = " + realDocId;

                        ret = getImageRealPath(ctx.getContentResolver(), mediaContentUri, whereClause);
                    }

                } else if (isDownloadDoc(uriAuthority)) {
                    // Build download uri.
                    Uri downloadUri = Uri.parse("content://downloads/public_downloads");

                    // Append download document id at uri end.
                    Uri downloadUriAppendId = ContentUris.withAppendedId(downloadUri, Long.valueOf(documentId));

                    ret = getImageRealPath(ctx.getContentResolver(), downloadUriAppendId, null);

                } else if (isExternalStoreDoc(uriAuthority)) {
                    String[] idArr = documentId.split(":");
                    if (idArr.length == 2) {
                        String type = idArr[0];
                        String realDocId = idArr[1];

                        if ("primary".equalsIgnoreCase(type)) {
                            ret = Environment.getExternalStorageDirectory() + "/" + realDocId;
                        }
                    }
                }
            }
        }

        return ret;
    }

    /* Check whether current android os version is bigger than kitkat or not. */
    private static boolean isAboveKitKat() {
        boolean ret = false;
        ret = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        return ret;
    }

    /* Check whether this uri represent a document or not. */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static boolean isDocumentUri(Context ctx, Uri uri) {
        boolean ret = false;
        if (ctx != null && uri != null) {
            ret = DocumentsContract.isDocumentUri(ctx, uri);
        }
        return ret;
    }

    /* Check whether this uri is a content uri or not.
     *  content uri like content://media/external/images/media/1302716
     *  */
    private static boolean isContentUri(Uri uri) {
        boolean ret = false;
        if (uri != null) {
            String uriSchema = uri.getScheme();
            if ("content".equalsIgnoreCase(uriSchema)) {
                ret = true;
            }
        }
        return ret;
    }

    /* Check whether this uri is a file uri or not.
     *  file uri like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
     * */
    private static boolean isFileUri(Uri uri) {
        boolean ret = false;
        if (uri != null) {
            String uriSchema = uri.getScheme();
            if ("file".equalsIgnoreCase(uriSchema)) {
                ret = true;
            }
        }
        return ret;
    }


    /* Check whether this document is provided by ExternalStorageProvider. */
    private static boolean isExternalStoreDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.android.externalstorage.documents".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by DownloadsProvider. */
    private static boolean isDownloadDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.android.providers.downloads.documents".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by MediaProvider. */
    private static boolean isMediaDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.android.providers.media.documents".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by google photos. */
    private static boolean isGooglePhotoDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.google.android.apps.photos.content".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /* Return uri represented document file real local path.*/
    private static String getImageRealPath(ContentResolver contentResolver, Uri uri, String whereClause) {
        String ret = "";

        // Query the uri with condition.
        Cursor cursor = contentResolver.query(uri, null, whereClause, null, null);

        if (cursor != null) {
            boolean moveToFirst = cursor.moveToFirst();
            if (moveToFirst) {

                // Get columns name by uri type.
                String columnName = MediaStore.Images.Media.DATA;

                if (uri == MediaStore.Images.Media.EXTERNAL_CONTENT_URI || uri == MediaStore.Images.Media.INTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Images.Media.DATA;
                } else if (uri == MediaStore.Audio.Media.EXTERNAL_CONTENT_URI || uri == MediaStore.Audio.Media.INTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Audio.Media.DATA;
                } else if (uri == MediaStore.Video.Media.EXTERNAL_CONTENT_URI || uri == MediaStore.Video.Media.INTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Video.Media.DATA;
                }

                // Get column index.
                int imageColumnIndex = cursor.getColumnIndex(columnName);

                // Get column value which is the uri related file local path.
                ret = cursor.getString(imageColumnIndex);
            }
        }

        return ret;
    }

    public static String getTag(Context context) {
        return context.getPackageName() + " --> ";
    }

    public static String dateToTimeFormat(String oldstringDate) {
        PrettyTime p = new PrettyTime(new Locale("en-US"));
        String isTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.ENGLISH);
            Date date = sdf.parse(oldstringDate);
            isTime = p.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return isTime;
    }

    public static String toJson(Object object) {
        Gson gson = new GsonBuilder().create();
        gson.toJson(object);
        return gson.toString();
    }

    public static String setRupiahFormat(String value) {
        DecimalFormat format = new DecimalFormat("#,###,###,###");
        if (!value.equals("") && value != null) {
            double numberDouble = Double.parseDouble(value);
            return format.format(numberDouble);
        }
        return "";
    }

    public static String setRupiahFormat(double value) {
        DecimalFormat format = new DecimalFormat("#,###,###,###");
        if (value != 0) {
            return format.format(value);
        }
        return "";
    }

    public static String setRupiahFormatWithComma(double value) {
        DecimalFormat format = new DecimalFormat("#,###,###,###");
        if (value != 0) {
            return format.format(value).replaceAll(".", ",");
        }
        return "";
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static String createSID() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    public static Base64Data insertBase64(MasterRepository repository, String parentSid, String base64Data, String path, String postBy) {
        String sid = createSID();
        Base64Data data = new Base64Data();
        data.data_sid = sid;
        data.parent_sid = parentSid;
        data.data = base64Data;
        data.data_path = path;
        data.date_created = getTimestampNow();
        data.post_by = postBy;
        repository.insertBase64Data(data);
        return data;
    }

    public static byte[] createFileFromBitmap(Bitmap bmp, String path, String filename) throws IOException {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File f = new File(path, filename);
        f.createNewFile();

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return bitmapdata;
    }

    public static String getMd5(String input) {
        try {

            // Static getInstance method is called with hashing MD5 
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest 
            //  of an input digest() return array of byte 
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value 
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setImageBase64(@NonNull Base64Data dataImage, ImageView imageView) {
        byte[] decodedString = Base64.decode(dataImage.data, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);
    }

    public static String setImageBase64Preview(@NonNull Base64Data dataImage, ImageView imageView) {
        final String[] base64encoded = {null};
        if (dataImage == null) {
            byte[] decodedString = Base64.decode(dataImage.data, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
            imageView.setOnClickListener(view -> {
                openImage(imageView.getContext(), decodedString);
            });
            Log.w("base64 image", "is available!");
            base64encoded[0] = dataImage.data;
        }
        else if (dataImage.data != null) {
            byte[] decodedString = Base64.decode(dataImage.data, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
            imageView.setOnClickListener(view -> {
                openImage(imageView.getContext(), decodedString);
            });
            Log.w("base64 image", "is available!");
            base64encoded[0] = dataImage.data;
        } else {
            if (dataImage.data_path != null) {
                Log.w("file image", dataImage.data_path);
                File file = new File(dataImage.data_path);
                if (file.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                    imageView.setOnClickListener(view -> {
                        openImagePath(imageView.getContext(), dataImage.data_path);
                    });
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    base64encoded[0] = Base64.encodeToString(byteArray, Base64.DEFAULT);
                } else {
                    Log.w("file image", "not exist! " + dataImage.data_sid);
                    if (dataImage.data_sid != null) {
                        getApi().getBase64Data(Util.getToken(imageView.getContext()), dataImage.data_sid)
                            .enqueue(new Callback<Base64Data>() {
                                @Override
                                public void onResponse(Call<Base64Data> call, Response<Base64Data> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        if (response.body().data != null) {
                                            Log.w("response", "" + response.message() + " " + response.body().data_sid);
                                            MasterRepository repository = MasterRepository.getInstance(MasterDatabase.getDatabase(imageView.getContext()));
                                            repository.updateBase64Data(response.body());
                                            byte[] decodedString = Base64.decode(response.body().data, Base64.DEFAULT);
                                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                            imageView.setImageBitmap(decodedByte);
                                            imageView.setOnClickListener(view -> {
                                                openImage(imageView.getContext(), decodedString);
                                            });
                                            base64encoded[0] = Base64.encodeToString(decodedString, Base64.DEFAULT);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Base64Data> call, Throwable t) {
                                }
                            });
                    }
                }
            } else {
                Log.w("file image", "not available!");
            }
        }
        return base64encoded[0];
    }

    public static String downloadImageBase64Preview(@NonNull String dataImageSid, ImageView imageView) {
        Log.d("TAG =>", "has download");
        final String[] base64download = {null};
        getApi().getBase64Data(Util.getToken(imageView.getContext()), dataImageSid)
            .enqueue(new Callback<Base64Data>() {
                @Override
                public void onResponse(Call<Base64Data> call, Response<Base64Data> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().data != null) {
                            Log.w("response", "" + response.message() + " " + response.body().data_sid);
                            MasterRepository repository = MasterRepository.getInstance(MasterDatabase.getDatabase(imageView.getContext()));
                            repository.insertBase64Data(response.body());
                            base64download[0] = response.body().data;
                            byte[] decodedString = Base64.decode(response.body().data, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imageView.setImageBitmap(decodedByte);
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    openImage(imageView.getContext(), decodedString);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure(Call<Base64Data> call, Throwable t) {}
            });
        return base64download[0];
    }

    public static void downloadImageBase64Preview2(@NonNull String dataImageSid, ImageView imageView, ImageView imageView2) {
        getApi().getBase64Data(Util.getToken(imageView.getContext()), dataImageSid)
                .enqueue(new Callback<Base64Data>() {
                    @Override
                    public void onResponse(Call<Base64Data> call, Response<Base64Data> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().data != null) {
                                Log.w("response", "" + response.message() + " " + response.body().data_sid);
                                MasterRepository repository = MasterRepository.getInstance(MasterDatabase.getDatabase(imageView.getContext()));
                                repository.insertBase64Data(response.body());
                                byte[] decodedString = Base64.decode(response.body().data, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                imageView.setImageBitmap(decodedByte);
                                imageView2.setImageBitmap(decodedByte);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Base64Data> call, Throwable t) {}
                });
    }

    public static void setFadeBackgroundError(View screen) {
        ObjectAnimator colorFade = ObjectAnimator.ofObject(screen, "backgroundColor",
                new ArgbEvaluator(), Color.argb(255, 255, 0, 0), 0x00000000);
        colorFade.setDuration(1000);
        colorFade.start();
    }

    public static String dateFormatteryyyyMMdd_HHmm(String Date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.util.Date value = null;
        try {
            value = formatter.parse(Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
        return dateFormatter.format(value);
    }

    public static ApiInterface getApi() {
        return getAPIClient().create(ApiInterface.class);
    }

    public static Retrofit getAPIClient() {
        return new Retrofit.Builder().baseUrl(BASE_URL)
                .client(provideOkHttp())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    static Interceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor localHttpLoggingInterceptor = new HttpLoggingInterceptor();
        localHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return localHttpLoggingInterceptor;
    }

    static OkHttpClient provideOkHttp() {
        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.MINUTES)
                .writeTimeout(60, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.MINUTES)
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
    
    public static void deleteFileByPath(String path) {
        File fileExisting = new File(path);
        if (fileExisting.exists()) {
            fileExisting.delete();
            Log.w("delete file", "1");
        } else {
            Log.w("delete file", "0");
        }
    }
    
    public static void loadMapView(WebView mapView, String lat, String lon) {
        String html = "<iframe id=\"map\"width=\"100%\"height=\"100%\"frameborder=\"0\"scrolling=\"no\"marginheight=\"0\"marginwidth=\"0\"src=\"https://maps.google.com/maps?q=" + 
                lat + "," + lon + 
                "&hl=id&t=k&z=15&output=embed\"></iframe>";
        mapView.setWebChromeClient(new WebChromeClient());
        mapView.setWebViewClient(new WebViewClient());
        mapView.getSettings().setJavaScriptEnabled(true);
        mapView.loadData(html, "text/html", "UTF-8");
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (!isNullOrEmpty(lon) && !isNullOrEmpty(lat)) {
                        openMap(mapView.getContext(), lat, lon);
                    }
                }
                return true;
            }
        });
    }
    
    public static void openMap(Context context, String lat, String lon) {
        Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?q=loc:" + lat + "," + lon);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(((Activity)context).getPackageManager()) != null) {
            ((Activity)context).startActivity(mapIntent);
        } else {
            Toast.makeText(context, "Failed to open map apps!", Toast.LENGTH_SHORT).show();
        }
    }

    public static String previewCapturedImage(ImageView imageView, String uri) {
        String image = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            final Bitmap bitmap = BitmapFactory.decodeFile(Uri.parse(uri).getPath(), options);
            image = encodeImage(bitmap);
            imageView.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return image;
    }

    private static String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    
    public static void openImage(Context context, byte[] decodedString) {
        if (decodedString != null) {
            Intent intent = new Intent(context, PhotoViewerActivity.class);
            intent.putExtra("data", decodedString);
            ((Activity)context).startActivity(intent);
        }
    }

    public static void openImagePath(Context context, String path) {
        if (path != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(path),"image/*");
            intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
            ((Activity)context).startActivity(intent);
        }
    }
    
    public static GenericReferences getUlp(Context context) {
        String ulp = Util.getStringPreference(context, Constants.USER_UNIT);
        return MasterRepository.getInstance(MasterDatabase.getDatabase(context)).getRefBySID(ulp);
    }

    public static GenericReferences getUP3(Context context) {
        return MasterRepository.getInstance(MasterDatabase.getDatabase(context)).getRefBySID(getUlp(context).parent_sid);
    }

    public static List<GenericReferences> getRole(Context context) {
        List<GenericReferences> genericReferences = new ArrayList<>();
        for (UserRoles role : MasterRepository.getInstance(MasterDatabase.getDatabase(context)).getUserRoles(Util.getUserSID(context))) {
            GenericReferences ref = MasterRepository.getInstance(MasterDatabase.getDatabase(context)).getRefBySID(role.ur_role_sid);
            genericReferences.add(ref);
        }
        return genericReferences;
    }

    public static void startJobService(Context context, String tag) {
        FirebaseJobDispatcher dispatcher = FirebaseSingleton.getInstance(context).getDispatcher();
        Job myJob = dispatcher.newJobBuilder()
                .setService(FirebaseJobService.class) // the JobService that will be called
                .setReplaceCurrent(true)
                .setRecurring(true)
                .setTag(tag)
                .setTrigger(Trigger.executionWindow(30,50))
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setLifetime(Lifetime.FOREVER)
                .setTag(Util.getUserSID(context))  // uniquely identifies the job
                .build();
        dispatcher.mustSchedule(myJob);
    }
    
    public static void stopJobService(Context context, String tag) {
        FirebaseJobDispatcher dispatcher = FirebaseSingleton.getInstance(context).getDispatcher();
        dispatcher.cancel(tag);
    }

//    up3_alias = repository.getRefBySID(up3).ref_description;
}
