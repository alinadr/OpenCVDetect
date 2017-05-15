package com.example.opencvdetectprototype.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.opencvdetectprototype.data.AppPreferences;
import com.example.opencvdetectprototype.data.AppPreferencesImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Leo on 11.05.2017.
 */

@Module
public class AppModule {

    private final Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    SharedPreferences provideSharedPreferences() {
        return mApplication.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    @Singleton
    @Provides
    AppPreferences provideAppPreferences(SharedPreferences sharedPreferences) {
        return new AppPreferencesImpl(sharedPreferences);
    }
}
