package com.haerul.manado.data.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.haerul.manado.data.entity.User;

@Dao
public interface UserDao {
    
    @Query("select * from users where user_sid=:sid")
    User getUserBySID(String sid);

    @Query("update users set user_phone=:phone, user_email=:email where user_sid=:userSID")
    void updateUser(String phone, String email, String userSID);
}
