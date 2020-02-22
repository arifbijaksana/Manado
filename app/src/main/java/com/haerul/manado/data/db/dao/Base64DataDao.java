package com.haerul.manado.data.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.haerul.manado.data.entity.Base64Data;


@Dao
public interface Base64DataDao {
    
    @Insert
    void insertBase64Data(Base64Data data);
    
    @Query("update base64_data set post_status =:status where data_sid =:sid")
    void updateStatus(String sid, boolean status);

    @Query("select * from base64_data where data_sid=:sid")
    Base64Data getBase64Data(String sid);

    @Update
    void updateData(Base64Data data);
}
