package com.example.opencvdetectprototype;

import android.app.Application;
import android.content.Context;

import com.example.opencvdetectprototype.di.AppComponent;
import com.example.opencvdetectprototype.di.AppModule;
import com.example.opencvdetectprototype.di.DaggerAppComponent;


public class OpenCVApplication extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public static AppComponent getAppComponent(Context context) {
        return ((OpenCVApplication) context.getApplicationContext()).mAppComponent;
    }
}
