package com.example.opencvdetectprototype.data;

import android.content.SharedPreferences;


public class AppPreferencesImpl implements AppPreferences {

    private static final String KEY_THRESHOLD = "threshold";
    private static final String KEY_FACE_ENABLED = "face";
    private static final String KEY_SAVE_ENABLED = "save";
    private static final String KEY_FACE_SIZE = "face_size";

    private final SharedPreferences mSharedPreferences;

    public AppPreferencesImpl(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public int getThreshold() {
        return mSharedPreferences.getInt(KEY_THRESHOLD, 100);
    }

    @Override
    public void setThreshold(int value) {
        mSharedPreferences.edit().putInt(KEY_THRESHOLD, value).apply();
    }

    @Override
    public boolean isFaceRecognitionEnabled() {
        return mSharedPreferences.getBoolean(KEY_FACE_ENABLED, false);
    }

    @Override
    public void setFaceRecognitionEnabled(boolean value) {
        mSharedPreferences.edit().putBoolean(KEY_FACE_ENABLED, value).apply();
    }

    @Override
    public boolean isSaveEnabled() {
        return mSharedPreferences.getBoolean(KEY_SAVE_ENABLED, false);
    }

    @Override
    public void setSaveEnabled(boolean value) {
        mSharedPreferences.edit().putBoolean(KEY_SAVE_ENABLED, value).apply();
    }

    @Override
    public int getFaceSize() {
        return mSharedPreferences.getInt(KEY_FACE_SIZE, 20);
    }

    @Override
    public void setFaceSize(int value) {
        mSharedPreferences.edit().putInt(KEY_FACE_SIZE, value).apply();
    }


}
