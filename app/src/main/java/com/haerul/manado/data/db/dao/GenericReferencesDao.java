package com.haerul.manado.data.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.haerul.manado.data.entity.GenericReferences;

import java.util.List;

@Dao
public interface GenericReferencesDao {
    
    @Query("select generic_references.* from generic_references " +
            "left join generic_category on generic_references.category_sid = generic_category.category_sid " +
            "where generic_category.category_name=:name order by ref_value asc")
    List<GenericReferences> getGenericRefByCategory(String name);

    @Query("select generic_references.* from generic_references " +
            "left join generic_category on generic_references.category_sid = generic_category.category_sid " +
            "where generic_category.category_name=:name and generic_references.ref_description=:where order by ref_value asc")
    List<GenericReferences> getGenericRefByCategory(String name, String where);

    @Query("select generic_references.* from generic_references " +
            "left join generic_category on generic_references.category_sid = generic_category.category_sid " +
            "where generic_category.category_name=:name and generic_references.parent_sid=:where order by ref_value asc")
    List<GenericReferences> getGenericRefByCategoryAndParent(String name, String where);

    @Query("select generic_references.* from generic_references " +
            "left join generic_category on generic_references.category_sid = generic_category.category_sid " +
            "where generic_references.ref_sid=:sid")
    GenericReferences getRefBySID(String sid);

    @Query("select generic_references.* from generic_references " +
            "left join generic_category on generic_references.category_sid = generic_category.category_sid " +
            "where generic_references.ref_value=:val and generic_category.category_name=:cat")
    GenericReferences getRefByValue(String cat, int val);
    
    @Query("select * from generic_references where ref_sid=:sid")
    GenericReferences getGenericRefBySID(String sid);
    
}
