package com.example.opencvdetectprototype.data;


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
