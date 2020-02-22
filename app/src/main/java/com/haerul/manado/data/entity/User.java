package com.haerul.manado.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
   @PrimaryKey @NonNull
   public String user_sid;
   public String user_uid;
   public String user_no_induk;
   public String user_name;
   public String user_phone;
   public String user_email;
   public String user_jabatan;
   public String user_bagian;
   public String user_unit;
   public String user_login_name;
   public String user_password;
   public String user_role_sid;
   public String last_login;
   public String lat_login;
   public String lon_login;
   public String last_connect;
   public String lat_connect;
   public String lon_connect;
   public String device_info;
   public int is_active;
   public String date_created;
   public String date_modified;
}
