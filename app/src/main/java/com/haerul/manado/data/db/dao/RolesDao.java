package com.haerul.manado.data.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.haerul.manado.data.entity.UserRoles;

import java.util.List;

@Dao
public interface RolesDao {

    @Query("select * from users_roles where ur_user_sid=:sid order by ur_role_sid")
    List<UserRoles> getUserRoles(String sid);
}
