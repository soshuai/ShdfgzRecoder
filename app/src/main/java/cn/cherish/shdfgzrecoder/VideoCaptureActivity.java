package cn.cherish.shdfgzrecoder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import cn.cherish.shdfgzrecoder.landscapevideocapture.CLog;
import cn.cherish.shdfgzrecoder.landscapevideocapture.VideoFile;
import cn.cherish.shdfgzrecoder.landscapevideocapture.camera.CameraWrapper;
import cn.cherish.shdfgzrecoder.landscapevideocapture.camera.NativeCamera;
import cn.cherish.shdfgzrecoder.landscapevideocapture.configuration.CaptureConfiguration;
import cn.cherish.shdfgzrecoder.landscapevideocapture.recorder.AlreadyUsedException;
import cn.cherish.shdfgzrecoder.landscapevideocapture.recorder.VideoRecorder;
import cn.cherish.shdfgzrecoder.landscapevideocapture.recorder.VideoRecorderInterface;
import cn.cherish.shdfgzrecoder.landscapevideocapture.view.RecordingButtonInterface;
import cn.cherish.shdfgzrecoder.landscapevideocapture.view.VideoCaptureView;
import cn.cherish.shdfgzrecoder.utils.BluetoothController;
import cn.cherish.shdfgzrecoder.utils.IoUtils;
import cn.cherish.shdfgzrecoder.utils.SpfUtils;

public class VideoCaptureActivity extends Activity implements RecordingButtonInterface, VideoRecorderInterface, View.OnClickListener {

    public static final int RESULT_ERROR = 753245;

    public static final String EXTRA_OUTPUT_FILENAME = "com.jmolsmobile.extraoutputfilename";
    public static final String EXTRA_CAPTURE_CONFIGURATION = "com.jmolsmobile.extracaptureconfiguration";
    public static final String EXTRA_ERROR_MESSAGE = "com.jmolsmobile.extraerrormessage";

    private static final String SAVED_RECORDED_BOOLEAN = "com.jmolsmobile.savedrecordedboolean";
    protected static final String SAVED_OUTPUT_FILENAME = "com.jmolsmobile.savedoutputfilename";

    private boolean mVideoRecorded = false;
    VideoFile mVideoFile = null;
    private CaptureConfiguration mCaptureConfiguration;

    private VideoCaptureView mVideoCaptureView;
    private VideoRecorder mVideoRecorder;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CLog.toggleLogging(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_capture);

        initializeCaptureConfiguration(savedInstanceState);

        SpfUtils.saveInt(VideoCaptureActivity.this, "cameraState", 0);//内置外置摄像头标志符
        AppContext.setCameraState(0);

        mVideoCaptureView = (VideoCaptureView) findViewById(R.id.videocapture_videocaptureview_vcv);
        if (mVideoCaptureView == null)
            return; // Wrong orientation
        initializeRecordingUI(0);
    }

    private void initializeCaptureConfiguration(final Bundle savedInstanceState) {
        mCaptureConfiguration = generateCaptureConfiguration();
        mVideoRecorded = generateVideoRecorded(savedInstanceState);
        mVideoFile = generateOutputFile(savedInstanceState);
    }

    private void initializeRecordingUI(int style) {
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        mVideoRecorder = new VideoRecorder(this, mCaptureConfiguration, mVideoFile, new CameraWrapper(
                new NativeCamera(), display.getRotation()), mVideoCaptureView.getPreviewSurfaceHolder());
        mVideoCaptureView.setRecordingButtonInterface(this);

        if (mVideoRecorded) {
            mVideoCaptureView.updateUIRecordingFinished(getVideoThumbnail());
        } else {
            mVideoCaptureView.updateUINotRecording();
        }
    }

    @Override
    protected void onPause() {
        if (mVideoRecorder != null) {
            mVideoRecorder.stopRecording(null);
        }
        releaseAllResources();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        finishCancelled();
    }

    @Override
    public void onRecordButtonClicked() {
//        setToast("onRecordButtonClicked");
        try {
            mVideoRecorder.toggleRecording();
        } catch (AlreadyUsedException e) {
            CLog.d(CLog.ACTIVITY, "Cannot toggle recording after cleaning up all resources");
        }
    }

    @Override
    public void onAcceptButtonClicked() {
//        setToast("onRecordButtonClicked");
        finishCompleted();
    }

    @Override
    public void onDeclineButtonClicked() {
//        setToast("onRecordButtonClicked");
        finishCancelled();
    }

    @Override
    public void onRecordingStarted() {
        mVideoCaptureView.updateUIRecordingOngoing();
    }

    @Override
    public void onRecordingStopped(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

        mVideoCaptureView.updateUIRecordingFinished(getVideoThumbnail());
        releaseAllResources();
    }

    @Override
    public void onRecordingSuccess() {
        mVideoRecorded = true;
    }

    @Override
    public void onRecordingFailed(String message) {
        finishError(message);
    }

    private void finishCompleted() {
        final Intent result = new Intent();
        result.putExtra(EXTRA_OUTPUT_FILENAME, mVideoFile.getFullPath());
        this.setResult(RESULT_OK, result);
        finish();
    }

    private void finishCancelled() {
        this.setResult(RESULT_CANCELED);
        IoUtils.deleteFileWithPath(mVideoFile.getFullPath());
        finish();
    }

    private void finishError(final String message) {
//        Toast.makeText(getApplicationContext(), "Can't capture video: " + message, Toast.LENGTH_LONG).show();

        final Intent result = new Intent();
        result.putExtra(EXTRA_ERROR_MESSAGE, message);
        this.setResult(RESULT_ERROR, result);
        finish();
    }

    private void releaseAllResources() {
        if (mVideoRecorder != null) {
            mVideoRecorder.releaseAllResources();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(SAVED_RECORDED_BOOLEAN, mVideoRecorded);
        savedInstanceState.putString(SAVED_OUTPUT_FILENAME, mVideoFile.getFullPath());
        super.onSaveInstanceState(savedInstanceState);
    }

    protected CaptureConfiguration generateCaptureConfiguration() {
        CaptureConfiguration returnConfiguration = this.getIntent().getParcelableExtra(EXTRA_CAPTURE_CONFIGURATION);
        if (returnConfiguration == null) {
            returnConfiguration = new CaptureConfiguration();
            CLog.d(CLog.ACTIVITY, "No captureconfiguration passed - using default configuration");
        }
        return returnConfiguration;
    }

    private boolean generateVideoRecorded(final Bundle savedInstanceState) {
        if (savedInstanceState == null)
            return false;
        return savedInstanceState.getBoolean(SAVED_RECORDED_BOOLEAN, false);
    }

    protected VideoFile generateOutputFile(Bundle savedInstanceState) {
        VideoFile returnFile;
        if (savedInstanceState != null) {
            returnFile = new VideoFile(savedInstanceState.getString(SAVED_OUTPUT_FILENAME));
        } else {
            returnFile = new VideoFile(this.getIntent().getStringExtra(EXTRA_OUTPUT_FILENAME));
        }
        // TODO: add checks to see if outputfile is writeable
        return returnFile;
    }

    public Bitmap getVideoThumbnail() {
        final Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(mVideoFile.getFullPath(),
                MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        if (thumbnail == null) {
            CLog.d(CLog.ACTIVITY, "Failed to generate video preview");
        }
        return thumbnail;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("ssss", "onKeyDown");
        int ckey = BluetoothController.getControllerKey(keyCode);
        if (ckey == BluetoothController.A_BTN) {
            if (super.findViewById(R.id.videocapture_rotate_iv) == null) {
                if (mVideoRecorder.isRecording()) {
                    onRecordButtonClicked();
                    this.finishCompleted();
                } else {
                    finishCancelled();
                }
            } else {
                finishCancelled();
            }
        } else if (ckey == BluetoothController.X_BTN) {
            if (super.findViewById(R.id.videocapture_rotate_iv) == null) {
                if (!mVideoRecorder.isRecording()) {
                    onRecordButtonClicked();
                }
            }
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View v) {

    }

}

