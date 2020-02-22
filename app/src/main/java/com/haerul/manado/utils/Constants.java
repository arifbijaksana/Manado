package com.haerul.manado.utils;

import android.os.Environment;

public final class Constants {
   
    public static final String MASTER_DB = "slw.db";
    public static final String LOG_DB = "log_slw.db";
    public static final String MASTER_DB_ZIP = "slw.zip";
    public static final String TEMP_DB = "slw_temp.db";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APP_JSON = "application/json";
    public static final String MASTER_DB_PATH = String.format("//data//data//%s//databases//", "com.haerul.manado");
    public static final String PATH = Environment.getExternalStorageDirectory() + "/SEULAWAH/";
    public static final String PATH_DATA_EXPORT = PATH + "file_exported/";
    public static final String PATH_DOWNLOAD = PATH + "file_downloaded/";
    public static final String PATH_IMG_HIDDEN = PATH + ".temp_img/";
    public static final String PATH_IMG = PATH + "img/";
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_ONLY_FORMAT = "yyyy-MM-dd";
    public static final String DATE_ONLY_FORMAT_NORMAL = "dd-MM-yyyy";
    public static final String TIME_ONLY_FORMAT = "HH:mm";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SECURITY_KEY = "Authorization";
    public static final String STATUS = "status";
    public static final String MESSAGE = "message";
    public static final String TOKEN_EXPIRED = "Unauthorized Access!";
    public static final String DB_NAME = "db_name";
    public static final String DB_VERSION = "db_version";
    public static final String DB_PATH = "db_path";
    public static final String DB_URL = "db_url";
    public static final String DATA = "data";
    public static final String UID = "uid";
    public static final String IS_LOGIN = "is_login";
    public static final String USER_SID = "user_sid";
    public static final String USER_UID = "user_uid";
    public static final String USER_NAME = "user_name";
    public static final String USER_PHONE = "user_phone";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_UNIT = "user_unit";
    public static final String USER_LOGIN_NAME = "user_login_name";
    public static final String USER_PASSWORD = "user_password";
    public static final String IS_ACTIVE = "is_active";
    public static final String USER_ROLE_SID = "user_role_sid";
    public static final String DATE_CREATED = "date_created";
    public static final String DATE_MODIFIED = "date_modified";
    public static final String TOKEN_AUTH = "token_auth";
    public static final String USER_BANNER = "banner";
    public static final String LAST_CONNECT = "last_connect";
    public static final String LAT_CONNECT = "lat_connect";
    public static final String LON_CONNECT = "lon_connect";
    public static final String KONDISI_TINGKAT_EMERGENCY = "KONDISI";
    public static final String PEMADAMAN = "PEMADAMAN";
    public static final String ULP = "ULP";
    public static final String PENYULANG = "PENYULANG BNA";
    public static final String JENIS_TEMUAN = "TEMUAN";
    public static final String INDIKASI = "INDIKASI";
    public static final String KELOMPOK = "KELOMPOK GANGGUAN";
    public static final String STATUS_GANGGUAN = "STATUS GANGGUAN";
    public static final String JENIS_WO = "JENIS WO";
    public static final String STATUS_TL = "STATUS TL";
    public static final String EXTRA_DATA = "data";
    public static final String TAB_TITLE = "tab_title";
    public static final String TAB_POSITION = "tab_position";
    public static final String UP3 = "UP3";

    public static final int USER_TL_SPV_TRANS_ENERGI = 14;
    public static final int USER_TL_SPV_PEMBANGKIT = 13;
    public static final int USER_TL_SPV_ADM_UMUM = 12;
    public static final int USER_TL_GANGGUAN_YANTEK = 11;
    public static final int USER_TL_HARDIST_PDKB = 10;
    public static final int USER_TL_HARDIST_YANTEK = 9;
    public static final int USER_TL_HARDIST_VANDOR = 8;
    public static final int USER_TL_RINTIS_YANTEK = 7;
    public static final int USER_TL_RINTIS_VENDOR = 6;
    public static final int USER_INSPEKSI = 5;
    public static final int USER_C4A = 4;
    public static final int USER_C4A_WILAYAH = 3;
    public static final int ADMIN_ULP = 2;
    public static final int ADMIN_UP3 = 1;
    public static final int ADMIN_WILAYAH = 0;
}