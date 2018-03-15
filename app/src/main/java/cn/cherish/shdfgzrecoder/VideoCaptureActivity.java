package cn.cherish.shdfgzrecoder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

public class VideoCaptureActivity extends Activity implements RecordingButtonInterface, VideoRecorderInterface {

    public static final int RESULT_ERROR = 753245;

    public static final String EXTRA_OUTPUT_FILENAME = "com.jmolsmobile.extraoutputfilename";
    public static final String EXTRA_CAPTURE_CONFIGURATION = "com.jmolsmobile.extracaptureconfiguration";
    public static final String EXTRA_ERROR_MESSAGE = "com.jmolsmobile.extraerrormessage";

    private static final String SAVED_RECORDED_BOOLEAN = "com.jmolsmobile.savedrecordedboolean";
    protected static final String SAVED_OUTPUT_FILENAME = "com.jmolsmobile.savedoutputfilename";
    private final static String LOG_TAG = "ssss";
    private boolean mVideoRecorded = false;
    VideoFile mVideoFile = null;
    private CaptureConfiguration mCaptureConfiguration;
    private VideoCaptureView mVideoCaptureView;
    private VideoRecorder mVideoRecorder;
    private boolean clicked = false;
    final int RECORD_LENGTH = 0;
    long startTime = 0;
    boolean recording = false;
    volatile boolean runAudioThread = true;
    Frame[] images;
    long[] timestamps;
    ShortBuffer[] samples;
    int imagesIndex, samplesIndex;
    private String ffmpeg_link = "";
    private FFmpegFrameRecorder recorder;
    private boolean isPreviewOn = false;
    private int sampleAudioRateInHz = 44100;
    private int imageWidth = 320;
    private int imageHeight = 240;
    private int frameRate = 30;
    private AudioRecord audioRecord;
    private AudioRecordRunnable audioRecordRunnable;
    private Thread audioThread;
    private Frame yuvImage = null;
    private Camera camera;
    private NativeCamera nativeCamera;
    private CameraView cameraView;

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

        nativeCamera = new NativeCamera();
        camera = nativeCamera.camera;
        cameraView = new CameraView(this, camera);
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
                nativeCamera, display.getRotation()), mVideoCaptureView.getPreviewSurfaceHolder());
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
        try {
            mVideoRecorder.toggleRecording();
        } catch (AlreadyUsedException e) {
            CLog.d(CLog.ACTIVITY, "Cannot toggle recording after cleaning up all resources");
        }

        if (!clicked) {
            clicked = true;
            startRecording();
        } else {
            clicked = false;
            stopRecording();
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

    public void startRecording() {

        initRecorder();

        try {
            recorder.start();
            startTime = System.currentTimeMillis();
            recording = true;
            audioThread.start();

        } catch (FFmpegFrameRecorder.Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {

        runAudioThread = false;
        try {
            audioThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        audioRecordRunnable = null;
        audioThread = null;

        if (recorder != null && recording) {
            if (RECORD_LENGTH > 0) {
                Log.v(LOG_TAG, "Writing frames");
                try {
                    int firstIndex = imagesIndex % samples.length;
                    int lastIndex = (imagesIndex - 1) % images.length;
                    if (imagesIndex <= images.length) {
                        firstIndex = 0;
                        lastIndex = imagesIndex - 1;
                    }
                    if ((startTime = timestamps[lastIndex] - RECORD_LENGTH * 1000000L) < 0) {
                        startTime = 0;
                    }
                    if (lastIndex < firstIndex) {
                        lastIndex += images.length;
                    }
                    for (int i = firstIndex; i <= lastIndex; i++) {
                        long t = timestamps[i % timestamps.length] - startTime;
                        if (t >= 0) {
                            if (t > recorder.getTimestamp()) {
                                recorder.setTimestamp(t);
                            }
                            recorder.record(images[i % images.length]);
                        }
                    }

                    firstIndex = samplesIndex % samples.length;
                    lastIndex = (samplesIndex - 1) % samples.length;
                    if (samplesIndex <= samples.length) {
                        firstIndex = 0;
                        lastIndex = samplesIndex - 1;
                    }
                    if (lastIndex < firstIndex) {
                        lastIndex += samples.length;
                    }
                    for (int i = firstIndex; i <= lastIndex; i++) {
                        recorder.recordSamples(samples[i % samples.length]);
                    }
                } catch (FFmpegFrameRecorder.Exception e) {
                    Log.v(LOG_TAG, e.getMessage());
                    e.printStackTrace();
                }
            }

            recording = false;
            Log.v(LOG_TAG, "Finishing recording, calling stop and release on recorder");
            try {
                recorder.stop();
                recorder.release();
            } catch (FFmpegFrameRecorder.Exception e) {
                e.printStackTrace();
            }
            recorder = null;

        }
    }

    //---------------------------------------
    // initialize ffmpeg_recorder
    //---------------------------------------
    private void initRecorder() {

        Log.w(LOG_TAG, "init recorder");

        if (RECORD_LENGTH > 0) {
            imagesIndex = 0;
            images = new Frame[RECORD_LENGTH * frameRate];
            timestamps = new long[images.length];
            for (int i = 0; i < images.length; i++) {
                images[i] = new Frame(imageWidth, imageHeight, Frame.DEPTH_UBYTE, 2);
                timestamps[i] = -1;
            }
        } else if (yuvImage == null) {
            yuvImage = new Frame(imageWidth, imageHeight, Frame.DEPTH_UBYTE, 2);
            Log.i(LOG_TAG, "create yuvImage");
        }

        Log.i(LOG_TAG, "ffmpeg_url: " + ffmpeg_link);
        recorder = new FFmpegFrameRecorder(ffmpeg_link, imageWidth, imageHeight, 1);
        recorder.setVideoCodec(28);
        recorder.setFormat("flv");
        recorder.setSampleRate(sampleAudioRateInHz);
        // Set in the surface changed method
        recorder.setFrameRate(frameRate);

        Log.i(LOG_TAG, "recorder initialize success");

        audioRecordRunnable = new AudioRecordRunnable();
        audioThread = new Thread(audioRecordRunnable);
        runAudioThread = true;
    }


    //---------------------------------------------
    // audio thread, gets and encodes audio data
    //---------------------------------------------
    class AudioRecordRunnable implements Runnable {

        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

            // Audio
            int bufferSize;
            ShortBuffer audioData;
            int bufferReadResult;

            bufferSize = AudioRecord.getMinBufferSize(sampleAudioRateInHz,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleAudioRateInHz,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

            if (RECORD_LENGTH > 0) {
                samplesIndex = 0;
                samples = new ShortBuffer[RECORD_LENGTH * sampleAudioRateInHz * 2 / bufferSize + 1];
                for (int i = 0; i < samples.length; i++) {
                    samples[i] = ShortBuffer.allocate(bufferSize);
                }
            } else {
                audioData = ShortBuffer.allocate(bufferSize);
            }

            Log.d(LOG_TAG, "audioRecord.startRecording()");
            audioRecord.startRecording();

            /* ffmpeg_audio encoding loop */
            while (runAudioThread) {
                if (RECORD_LENGTH > 0) {
                    audioData = samples[samplesIndex++ % samples.length];
                    audioData.position(0).limit(0);
                }
                //Log.v(LOG_TAG,"recording? " + recording);
                bufferReadResult = audioRecord.read(audioData.array(), 0, audioData.capacity());
                audioData.limit(bufferReadResult);
                if (bufferReadResult > 0) {
                    Log.v(LOG_TAG, "bufferReadResult: " + bufferReadResult);
                    // If "recording" isn't true when start this thread, it never get's set according to this if statement...!!!
                    // Why?  Good question...
                    if (recording) {
                        if (RECORD_LENGTH <= 0) try {
                            recorder.recordSamples(audioData);
                            //Log.v(LOG_TAG,"recording " + 1024*i + " to " + 1024*i+1024);
                        } catch (FFmpegFrameRecorder.Exception e) {
                            Log.v(LOG_TAG, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
            Log.v(LOG_TAG, "AudioThread Finished, release audioRecord");

            /* encoding finish, release recorder */
            if (audioRecord != null) {
                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;
                Log.v(LOG_TAG, "audioRecord released");
            }
        }
    }

    //---------------------------------------------
    // camera thread, gets and encodes video data
    //---------------------------------------------
    public class CameraView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

        private SurfaceHolder mHolder;
        private Camera mCamera;

        public CameraView(Context context, Camera camera) {
            super(context);
            Log.w("camera", "camera view");
            mCamera = camera;
            mHolder = getHolder();
            mHolder.addCallback(CameraView.this);
//            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//            mCamera.setPreviewCallback(CameraView.this);
//            mCamera.setDisplayOrientation(90);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
//            try {
//                stopPreview();
//                mCamera.setPreviewDisplay(holder);
//            } catch (IOException exception) {
//                mCamera.release();
//                mCamera = null;
//            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            stopPreview();

            Camera.Parameters camParams = mCamera.getParameters();
            List<Camera.Size> sizes = camParams.getSupportedPreviewSizes();
            // Sort the list in ascending order
            Collections.sort(sizes, new Comparator<Camera.Size>() {

                public int compare(final Camera.Size a, final Camera.Size b) {
                    return a.width * a.height - b.width * b.height;
                }
            });

            // Pick the first preview size that is equal or bigger, or pick the last (biggest) option if we cannot
            // reach the initial settings of imageWidth/imageHeight.
            for (int i = 0; i < sizes.size(); i++) {
                if ((sizes.get(i).width >= imageWidth && sizes.get(i).height >= imageHeight) || i == sizes.size() - 1) {
                    imageWidth = sizes.get(i).width;
                    imageHeight = sizes.get(i).height;
                    Log.v(LOG_TAG, "Changed to supported resolution: " + imageWidth + "x" + imageHeight);
                    break;
                }
            }
            camParams.setPreviewSize(imageWidth, imageHeight);

            Log.v(LOG_TAG, "Setting imageWidth: " + imageWidth + " imageHeight: " + imageHeight + " frameRate: " + frameRate);

            camParams.setPreviewFrameRate(frameRate);
            Log.v(LOG_TAG, "Preview Framerate: " + camParams.getPreviewFrameRate());

            mCamera.setParameters(camParams);

            // Set the holder (which might have changed) again
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.setPreviewCallback(CameraView.this);
                startPreview();
            } catch (Exception e) {
                Log.e(LOG_TAG, "Could not set preview display in surfaceChanged");
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            try {
                mHolder.addCallback(null);
                mCamera.setPreviewCallback(null);
            } catch (RuntimeException e) {
                // The camera has probably just been released, ignore.
            }
        }

        public void startPreview() {
            if (!isPreviewOn && mCamera != null) {
                isPreviewOn = true;
                mCamera.startPreview();
            }
        }

        public void stopPreview() {
            if (isPreviewOn && mCamera != null) {
                isPreviewOn = false;
                mCamera.stopPreview();
            }
        }

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (audioRecord == null || audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                startTime = System.currentTimeMillis();
                return;
            }
            if (RECORD_LENGTH > 0) {
                int i = imagesIndex++ % images.length;
                yuvImage = images[i];
                timestamps[i] = 1000 * (System.currentTimeMillis() - startTime);
            }
            /* get video data */
            if (yuvImage != null && recording) {
                ((ByteBuffer) yuvImage.image[0].position(0)).put(data);

                if (RECORD_LENGTH <= 0)
                    try {
                        Log.v(LOG_TAG, "Writing Frame");
                        long t = 1000 * (System.currentTimeMillis() - startTime);
                        if (t > recorder.getTimestamp()) {
                            recorder.setTimestamp(t);
                        }
                        recorder.record(yuvImage);
                    } catch (FFmpegFrameRecorder.Exception e) {
                        Log.v(LOG_TAG, e.getMessage());
                        e.printStackTrace();
                    }
            }
        }
    }
}

