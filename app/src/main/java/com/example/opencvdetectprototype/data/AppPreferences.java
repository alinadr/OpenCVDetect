package com.example.opencvdetectprototype.data;

/**
 * Created by Leo on 11.05.2017.
 */

public interface AppPreferences {

    int getThreshold();

    void setThreshold(int value);

    boolean isFaceRecognitionEnabled();

    void setFaceRecognitionEnabled(boolean value);

    boolean isSaveEnabled();

    void setSaveEnabled(boolean value);

    int getFaceSize();

    void setFaceSize(int value);

}
