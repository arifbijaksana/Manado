package com.haerul.manado.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users_roles")
public class UserRoles {
    @PrimaryKey @NonNull
    public String ur_sid;
    public String ur_user_sid;
    public String ur_role_sid;
}
