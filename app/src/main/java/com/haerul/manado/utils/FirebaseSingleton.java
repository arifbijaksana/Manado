package com.haerul.manado.utils;

import android.content.Context;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;

public class FirebaseSingleton {
    private static FirebaseSingleton mFirebaseSingleton;
    private static Context mContext;
    private static FirebaseJobDispatcher dispatcher;

    private FirebaseSingleton(Context context){
        mContext=context;
    }

    public static synchronized FirebaseSingleton getInstance(Context context) {
        if (mFirebaseSingleton == null) {
            mFirebaseSingleton = new FirebaseSingleton(context);
        }
        return mFirebaseSingleton;
    }

    public static FirebaseJobDispatcher getDispatcher() {
        if (dispatcher == null) {
            dispatcher= new FirebaseJobDispatcher(new GooglePlayDriver(mContext));
        }
        return dispatcher;
    }


}
