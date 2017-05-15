package com.example.opencvdetectprototype.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.opencvdetectprototype.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;


public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        initToolbar();
        initControls();


    }

    private void initToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tutorial");
    }

    private void initControls(){
        WebView tutorialView = (WebView)findViewById(R.id.webView);
//        String htmlFilename = "bio10.html";
//        AssetManager mgr = getBaseContext().getAssets();
//        try {
//            InputStream in = mgr.open(htmlFilename, AssetManager.ACCESS_BUFFER);
//            String htmlContentInStringFormat = StreamToString(in);
//            in.close();
//            tutorialView.loadDataWithBaseURL(null, htmlContentInStringFormat, "text/html", "utf-8", null);
            tutorialView.setWebViewClient(new WebViewClient());
            tutorialView.loadUrl("file:///android_asset/sample.html");
//        }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
        return new Intent(context, TutorialActivity.class);
    }

    public static String StreamToString(InputStream in) throws IOException {
        if(in == null) {
            return "";
        }
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
        }
        return writer.toString();
    }
}
