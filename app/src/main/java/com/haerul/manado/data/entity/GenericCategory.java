package com.haerul.manado.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "generic_category")
public class GenericCategory {
    @PrimaryKey @NonNull
    public String category_sid;
    public String category_name;
    public String category_desc;
    public int is_config_two_level;
    public String parent_sid;
    public String date_created;
    public String date_modified;
}
