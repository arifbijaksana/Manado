package com.haerul.manado.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "generic_references")
public class GenericReferences {
    @PrimaryKey
    @NonNull
    public String ref_sid;
    public String category_sid;
    public String parent_sid;
    public String ref_name;
    public int ref_value;
    public String ref_description;
    public String date_created;
    public String date_modified;
    
    @Ignore
    public GenericReferences(@NonNull String ref_sid, String ref_name) {
        this.ref_sid = ref_sid;
        this.ref_name = ref_name;
    }
    
    public GenericReferences() {
    }
}
