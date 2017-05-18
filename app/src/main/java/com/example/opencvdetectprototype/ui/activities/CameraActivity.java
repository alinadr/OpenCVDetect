package com.example.opencvdetectprototype.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.opencvdetectprototype.OpenCVApplication;
import com.example.opencvdetectprototype.R;
import com.example.opencvdetectprototype.data.AppPreferences;
import com.example.opencvdetectprototype.domain.DetectionBasedTracker;
import com.example.opencvdetectprototype.ui.views.CVCameraView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;


public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2, View.OnTouchListener {

    @Inject
    AppPreferences mAppPreferences;
    boolean mIsZoomEnabled;

    private static final String TAG = "OCVSample::Activity";
    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);
    private CVCameraView mOpenCvCameraView;

    Mat mRgba;
    Mat mGray;
    private float X, Y;

    private File mCascadeFile;
    private DetectionBasedTracker mNativeDetector;
    private float mRelativeFaceSize   = 0.2f;
    private int mAbsoluteFaceSize   = 0;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");

//                    System.loadLibrary("detection_based_tracker");
//
//                    try {
//                        // load cascade file from application resources
//                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
//                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
//                        mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
//                        FileOutputStream os = new FileOutputStream(mCascadeFile);
//
//                        byte[] buffer = new byte[4096];
//                        int bytesRead;
//                        while ((bytesRead = is.read(buffer)) != -1) {
//                            os.write(buffer, 0, bytesRead);
//                        }
//                        is.close();
//                        os.close();
//
//                        mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);
//
//                        cascadeDir.delete();
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
//                    }

                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(CameraActivity.this);
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        OpenCVApplication.getAppComponent(this).inject(this);

        initToolbar();
        initControls();
    }

    private void initToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Camera");
    }

    private void initControls(){
        findViewById(R.id.gallery_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() +
                        "/Pictures/OpenCVDetect/");
                intent.setDataAndType(uri, "*/*");
                startActivity(Intent.createChooser(intent, "Open folder"));
            }
        });

        findViewById(R.id.zoom_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox) view).isChecked()) {
                    mIsZoomEnabled = true;
                    Toast.makeText(CameraActivity.this, "Zoom mode is enabled", Toast.LENGTH_LONG).show();
                } else {
                    mIsZoomEnabled = false;
                    Toast.makeText(CameraActivity.this, "Zoom mode is disabled", Toast.LENGTH_LONG).show();

                }
            }
        });

        mOpenCvCameraView = (CVCameraView) findViewById(R.id.java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        setMinFaceSize(mAppPreferences.getFaceSize());
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
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
        return new Intent(context, CameraActivity.class);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mGray = new Mat();
        X = (float)mRgba.size().height / 2 - 9 * (float)mRgba.size().height / 100;
        Y =  (float)mRgba.size().width / 2 - 9 * (float)mRgba.size().width / 100;
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        if(mIsZoomEnabled){
            Size sizeRgba = mRgba.size();

            int rows = (int) sizeRgba.height;
            int cols = (int) sizeRgba.width;

            int left = cols / 8;
            int top = rows / 8;

            int width = cols * 3 / 4;
            int height = rows * 3 / 4;

            if(X > (rows - 9 * rows / 50))
                X = rows - 9 * rows / 50;
            if(Y > (cols - 9 * cols / 50))
                Y = cols - 9 * cols / 50;

            Mat zoomCorner = mRgba.submat(0, rows / 2 - rows / 10, 0, cols / 2 - cols / 10);
            Mat mZoomWindow = mRgba.submat((int)X, (int)X + 9 * rows / 50, (int)Y, (int)Y + 9 * cols / 50);
            Imgproc.resize(mZoomWindow, zoomCorner, zoomCorner.size());
            Size wsize = mZoomWindow.size();
            Imgproc.rectangle(mZoomWindow, new Point(1, 1), new Point(wsize.width - 2, wsize.height - 2), new Scalar(255, 0, 0, 255), 2);
            zoomCorner.release();
            mZoomWindow.release();
        }

//        if(mAppPreferences.isFaceRecognitionEnabled()){
//            if (mAbsoluteFaceSize == 0) {
//                int height = mGray.rows();
//                if (Math.round(height * mRelativeFaceSize) > 0) {
//                    mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
//                }
//                mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
//            }
//
//            MatOfRect faces = new MatOfRect();
//
//            if (mNativeDetector != null)
//                mNativeDetector.detect(mGray, faces);
//
//            Rect[] facesArray = faces.toArray();
//            for (int i = 0; i < facesArray.length; i++)
//                Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);
//        }
        return mRgba; // This function must return
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        Log.i(TAG,"onTouch event");
        if(mIsZoomEnabled){
            X = event.getX();
            Y = event.getY();
            Toast.makeText(this, X + ", " + Y + " saved", Toast.LENGTH_SHORT).show();

        } else {
            if (mAppPreferences.isSaveEnabled()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                String currentDateandTime = sdf.format(new Date());
                String fileName = Environment.getExternalStorageDirectory().getPath() +
                        "/Pictures/OpenCVDetect/sample_picture_" + currentDateandTime + ".jpg";

                mOpenCvCameraView.takePicture(fileName);
                Toast.makeText(this, fileName + " saved", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    private void setMinFaceSize(int faceSize) {
        mRelativeFaceSize = faceSize/100;
        mAbsoluteFaceSize = 0;
    }
}
