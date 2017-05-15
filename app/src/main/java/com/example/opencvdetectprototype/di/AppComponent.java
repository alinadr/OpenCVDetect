package com.example.opencvdetectprototype.di;

import com.example.opencvdetectprototype.ui.activities.CameraActivity;
import com.example.opencvdetectprototype.ui.activities.SettingsActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Leo on 11.05.2017.
 */

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(SettingsActivity activity);
    void inject(CameraActivity activity);
}
