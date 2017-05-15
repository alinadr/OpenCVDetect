package com.example.opencvdetectprototype.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.opencvdetectprototype.OpenCVApplication;
import com.example.opencvdetectprototype.R;
import com.example.opencvdetectprototype.data.AppPreferences;

import javax.inject.Inject;



public class SettingsActivity extends AppCompatActivity {

    @Inject
    AppPreferences mAppPreferences;
    Spinner mFaceSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        OpenCVApplication.getAppComponent(this).inject(this);

        initToolbar();
        initControls();
    }

    private void initToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");
    }

    private void initControls() {
        Switch faceDetectionSwitch = (Switch) findViewById(R.id.face_recognition_switch);
        faceDetectionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAppPreferences.setFaceRecognitionEnabled(isChecked);
            }
        });
        faceDetectionSwitch.setChecked(mAppPreferences.isFaceRecognitionEnabled());

        Switch saveSwitch = (Switch) findViewById(R.id.save_switch);
        saveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAppPreferences.setSaveEnabled(isChecked);
            }
        });
        saveSwitch.setChecked(mAppPreferences.isSaveEnabled());

        mFaceSpinner = (Spinner) findViewById(R.id.face_size_spinner);
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.face_size, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFaceSpinner.setAdapter(adapter);
        mFaceSpinner.setSelection(3);

        mFaceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] choose = getResources().getStringArray(R.array.face_size);
                mAppPreferences.setFaceSize(Integer.parseInt(choose[i]));
                mFaceSpinner.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mAppPreferences.setFaceSize(20);
                mFaceSpinner.setSelection(3);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent createLaunchIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }
}
