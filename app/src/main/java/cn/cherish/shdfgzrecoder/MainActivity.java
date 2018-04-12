package cn.cherish.shdfgzrecoder;
import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.cherish.shdfgzrecoder.landscapevideocapture.configuration.CaptureConfiguration;
import cn.cherish.shdfgzrecoder.landscapevideocapture.configuration.PredefinedCaptureConfigurations.CaptureResolution;
import cn.cherish.shdfgzrecoder.landscapevideocapture.configuration.PredefinedCaptureConfigurations.CaptureQuality;
import cn.cherish.shdfgzrecoder.okhttp.utils.CaseRecordUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String STREAM_URL = "rtmp://pili-publish.push.expresscourts.com/expresscourt/test1";
    final static int PERMISSIONS_REQUEST_CODE = 12;

    private final static int REQUEST_CODE_AUDIO = 101;
    private final static int REQUEST_CODE_VIDEO = 102;
    private final static int REQUEST_CODE_PLAN = 103;

    private Button record_btn;
    private Button recorder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CODE);
        }
        setContentView(R.layout.activity_main);
        record_btn = (Button) findViewById(R.id.record_btn);
        recorder= (Button) findViewById(R.id.recorder);
        record_btn.setOnClickListener(this);
        recorder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_btn:
                startActivity(RecordActivity.makeIntent(STREAM_URL));
                break;
            case R.id.recorder:
                SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
                AppContext.setBackCamera(true);
                Intent intent3 = new Intent(this, VideoCaptureActivity.class);
                intent3.putExtra(VideoCaptureActivity.EXTRA_CAPTURE_CONFIGURATION,
                        new CaptureConfiguration(CaptureResolution.RES_720P,
                                CaptureQuality.MEDIUM));
                intent3.putExtra(
                        VideoCaptureActivity.EXTRA_OUTPUT_FILENAME,
                        CaseRecordUtils.VIDEO_DIR
                                +"公证"+ft.format(new Date()) + ".mp4");
                startActivityForResult(intent3, REQUEST_CODE_VIDEO);
                break;
            default:
                break;
        }
    }

}
