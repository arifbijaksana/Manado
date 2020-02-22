package com.haerul.manado.di.module;


import androidx.room.Room;

import com.haerul.manado.App;
import com.haerul.manado.data.db.MasterDatabase;
import com.haerul.manado.data.db.repository.MasterRepository;
import com.haerul.manado.utils.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppDatabaseModule {

    @Provides @Singleton
    public MasterRepository provideRepository(App application) {
        MasterDatabase database = Room.databaseBuilder(application, MasterDatabase.class, Constants.MASTER_DB)
                .allowMainThreadQueries()
                .build();
        return MasterRepository.getInstance(database);
    }

}
