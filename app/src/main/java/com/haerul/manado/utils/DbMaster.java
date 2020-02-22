package com.haerul.manado.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import net.lingala.zip4j.model.LocalFileHeader;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DbMaster extends SQLiteOpenHelper {

    private final String PATH_MASTER_DATA = Constants.PATH + Constants.MASTER_DB;
    public static String DB_PATH;
    public static String DB_NAME = Constants.MASTER_DB;
    public SQLiteDatabase database;
    public final Context context;

    public SQLiteDatabase getDb() {
        return database;
    }

    public DbMaster(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        String packageName = context.getPackageName();
        DB_PATH = String.format("//data//data//%s//databases//", packageName);
        //Log.i("DbMaster",DB_PATH+DB_NAME);
    }

    public boolean isFileMasterData() {
        File file = new File(PATH_MASTER_DATA);
        boolean cek = file.exists();
        file = null;
        return cek;
    }

    public void deleteMasterData() {
        File file = new File(PATH_MASTER_DATA);
        if (file.exists()) {
            file.delete();
        }
        file = null;
    }


    public void deleteMasterDataZip() {
        File file = new File(Constants.PATH+Constants.MASTER_DB_ZIP);
        if (file.exists()) {
            file.delete();
        }
        file = null;
    }
    

    public void createDataBase() {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //Log.i(this.getClass().toString(), "Database already exists");
        }
    }

    public void importDataBase() {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
                deleteMasterData();
                deleteMasterDataZip();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
                deleteMasterData();
                deleteMasterDataZip();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkDataBase() {
        SQLiteDatabase checkDb = null;
        try {
            String path = DB_PATH + DB_NAME;
            // TODO : check if exists, klo ada baru openDatabase
            checkDb = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (checkDb != null) {
            checkDb.close();
        }
        return checkDb != null;
    }

    
    public boolean unpackZip(String path, String zipname, String dbname)
    {
        InputStream is;
        ZipInputStream zis;
        try
        {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + dbname);

                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Log.wtf("ERROR UNZIP", e.getLocalizedMessage());
            return false;
        }

        return true;
    }

    private void copyDataBase() throws IOException {
        InputStream externalDbStream = new FileInputStream(PATH_MASTER_DATA); // Environment.getExternalStorageDirectory() + "/Sadix-CRM/"; MASTER_DB.db
        String outFileName = DB_PATH + DB_NAME; // //data//data//%s//databases//", packageName MASTER_DB.db
        OutputStream localDbStream = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }
        localDbStream.close();
        externalDbStream.close();
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        String path = DB_PATH + DB_NAME;
        if (database == null) {
            createDataBase();
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS |
                    SQLiteDatabase.OPEN_READWRITE);
        }
        return database;
    }
    
    public boolean deteleDatabase() {
        return context.deleteDatabase(DB_NAME);
    }

    public void deleteDatabaseFile(String databaseName) {
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
            }
            else {
                System.out.println("Failed to delete database journal");
            }
        }

        File shm = new File(databases, databaseName + "-journal");
        if (journal.exists()) {
            if (journal.delete()) {
                System.out.println("Database journal deleted");
            }
            else {
                System.out.println("Failed to delete database journal");
            }
        }
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
            database = null;
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor query(String sql) {
        Cursor cursor = null;
        cursor = openDataBase().rawQuery(sql, null);
        return cursor;
    }

    public List<JSONObject> queryJSON(String sql) {
        List<JSONObject> list = new ArrayList<JSONObject>();
        try {
            Cursor cursor = query(sql);
            while (cursor.moveToNext()) {
                JSONObject json = new JSONObject();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    if (cursor.getType(i) == Cursor.FIELD_TYPE_NULL) {
                        json.put(cursor.getColumnName(i), null);
                    } else if (cursor.getType(i) == Cursor.FIELD_TYPE_STRING) {
                        json.put(cursor.getColumnName(i), cursor.getString(i));
                    } else if (cursor.getType(i) == Cursor.FIELD_TYPE_INTEGER) {
                        json.put(cursor.getColumnName(i), cursor.getInt(i));
                    } else if (cursor.getType(i) == Cursor.FIELD_TYPE_FLOAT) {
                        json.put(cursor.getColumnName(i), cursor.getFloat(i));
                    } else if (cursor.getType(i) == Cursor.FIELD_TYPE_BLOB) {
                    }
                }
                list.add(json);
            }
            cursor.close();
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public JSONObject getJSON(String sql) {
        JSONObject json = null;
        try {
            Cursor cursor = query(sql);
            if (cursor.moveToNext()) {
                json = new JSONObject();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    if (cursor.getType(i) == Cursor.FIELD_TYPE_NULL) {
                        json.put(cursor.getColumnName(i), null);
                    } else if (cursor.getType(i) == Cursor.FIELD_TYPE_STRING) {
                        json.put(cursor.getColumnName(i), cursor.getString(i));
                    } else if (cursor.getType(i) == Cursor.FIELD_TYPE_INTEGER) {
                        json.put(cursor.getColumnName(i), cursor.getInt(i));
                    } else if (cursor.getType(i) == Cursor.FIELD_TYPE_FLOAT) {
                        json.put(cursor.getColumnName(i), cursor.getFloat(i));
                    } else if (cursor.getType(i) == Cursor.FIELD_TYPE_BLOB) {
                    }
                }
            }
            cursor.close();
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public boolean save(ContentValues cv, String table) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.insert(table, null, cv);
            db.close();
            return true;
        } catch (Exception e) {
//			e.printStackTrace();
            return false;
        }
    }

    public boolean update(ContentValues cv, JSONObject id, String table) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String pk = null;
            String[] pks = new String[id.length()];
            Iterator keys = id.keys();
            int i = 0;
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (pk == null) {
                    pk = key + "=?";
                } else {
                    pk += " and " + key + "=?";
                }
                pks[i] = id.getString(key);
                i++;
            }
            db.update(table, cv, pk, pks);
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean execSQL(String sql) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(sql);
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void extractWithZipInputStream(File zipFile, char[] password) throws IOException {
        LocalFileHeader localFileHeader;
        int readLen;
        byte[] readBuffer = new byte[4096];

        try (FileInputStream fileInputStream = new FileInputStream(zipFile);
             net.lingala.zip4j.io.inputstream.ZipInputStream zipInputStream = new net.lingala.zip4j.io.inputstream.ZipInputStream(new BufferedInputStream(fileInputStream), password)) {
            while ((localFileHeader = zipInputStream.getNextEntry()) != null) {
                File extractedFile = new File(Constants.PATH + Constants.MASTER_DB);
                try (OutputStream outputStream = new FileOutputStream(extractedFile)) {
                    while ((readLen = zipInputStream.read(readBuffer)) != -1) {
                        outputStream.write(readBuffer, 0, readLen);
                    }
                }
            }
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            db.disableWriteAheadLogging();
        }
        super.onOpen(db);
    }
}
