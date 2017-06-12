package com.example.opencvdetectprototype.di;

import com.example.opencvdetectprototype.ui.activities.CameraActivity;
import com.example.opencvdetectprototype.ui.activities.SettingsActivity;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(SettingsActivity activity);
    void inject(CameraActivity activity);
}
