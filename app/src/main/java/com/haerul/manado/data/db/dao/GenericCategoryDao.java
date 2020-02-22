package com.haerul.manado.data.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.haerul.manado.data.entity.GenericCategory;

@Dao
public interface GenericCategoryDao {
    
    @Query("select * from generic_category where category_name=:name")
    GenericCategory getCategoryByName(String name);
    
}
