package com.haerul.manado.data.db.repository;

import android.content.Context;
import android.database.Cursor;

import androidx.lifecycle.LiveData;

import com.haerul.manado.data.db.MasterDatabase;
import com.haerul.manado.data.entity.Base64Data;
import com.haerul.manado.data.entity.GenericReferences;
import com.haerul.manado.data.entity.User;
import com.haerul.manado.data.entity.UserRoles;

import java.util.List;

public class MasterRepository {
    private final MasterDatabase database;

    public MasterRepository(MasterDatabase database) {
        this.database = database;
    }

    public MasterRepository(Context context) {
        this.database =  MasterDatabase.getDatabase(context);
    }

    public static MasterRepository getInstance(MasterDatabase database) {
        return new MasterRepository(database);
    }
    
    //reff generic
    public List<GenericReferences> getRefByCategory(String category) {
        return database.genericReferencesDao().getGenericRefByCategory(category);
    }
    public List<GenericReferences> getRefByCategory(String category, String where) {
        return database.genericReferencesDao().getGenericRefByCategory(category, where);
    }
    public List<GenericReferences> getRefByCategoryAndParent(String category, String where) {
        return database.genericReferencesDao().getGenericRefByCategoryAndParent(category, where);
    }
    public GenericReferences getRefBySID(String sid) {
        return database.genericReferencesDao().getRefBySID(sid);
    }

    public GenericReferences getRefByValue(String cat, int val) {
        return database.genericReferencesDao().getRefByValue(cat, val);
    }
    
    
    // user
    public User getUserBySID(String sid) {
        return database.userDao().getUserBySID(sid);
    }
    
    public List<UserRoles> getUserRoles(String sid) {
        return database.rolesDao().getUserRoles(sid);
    }
    
    
    // base64data
    public void insertBase64Data(Base64Data data) {
        database.base64DataDao().insertBase64Data(data);
    }
    
    public void updateStatus(String sid, boolean status) {
        database.base64DataDao().updateStatus(sid, status);
    }

    public Base64Data getBase64Data(String sid) {
        return database.base64DataDao().getBase64Data(sid);
    }
    
    public void updateBase64Data(Base64Data data) {
        database.base64DataDao().updateData(data);
    }
    
    public void updateUser(String phone, String email, String userSID) {
        database.userDao().updateUser(phone, email, userSID);
    }
}
