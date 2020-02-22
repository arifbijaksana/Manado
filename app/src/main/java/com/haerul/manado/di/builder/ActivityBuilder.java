package com.haerul.manado.di.builder;

import com.haerul.manado.ui.MainActivity;
import com.haerul.manado.ui.login.LoginActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {
    
    @ContributesAndroidInjector
    abstract LoginActivity loginActivity();
    
    @ContributesAndroidInjector
    abstract MainActivity mainActivity();
    
}
