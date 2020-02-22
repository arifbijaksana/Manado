package com.haerul.manado.data.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.haerul.manado.data.db.dao.Base64DataDao;
import com.haerul.manado.data.db.dao.GenericCategoryDao;
import com.haerul.manado.data.db.dao.GenericReferencesDao;
import com.haerul.manado.data.db.dao.RolesDao;
import com.haerul.manado.data.db.dao.UserDao;
import com.haerul.manado.data.entity.Base64Data;
import com.haerul.manado.data.entity.GenericCategory;
import com.haerul.manado.data.entity.GenericReferences;
import com.haerul.manado.data.entity.User;
import com.haerul.manado.data.entity.UserRoles;
import com.haerul.manado.utils.Constants;

@Database(entities = {
        User.class, 
        UserRoles.class,
        GenericCategory.class,
        GenericReferences.class,
        Base64Data.class
}, version = 1, exportSchema = false)
public abstract class MasterDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract RolesDao rolesDao();
    public abstract GenericCategoryDao genericCategoryDao();
    public abstract GenericReferencesDao genericReferencesDao();
    public abstract Base64DataDao base64DataDao();
    
    private static MasterDatabase INSTANCE;
    public  static MasterDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (MasterDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, MasterDatabase.class, Constants.MASTER_DB)
                            .allowMainThreadQueries()
                            .setJournalMode(JournalMode.TRUNCATE)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
