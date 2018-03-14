package cn.cherish.shdfgzrecoder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///////////////////////////////////////
        //沉浸式
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ///////////////////////////////////////

//        ButterKnife.bind(this);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_in_right);
    }

    public void androidToast(String ss) {
        Toast.makeText(this, ""+ss, Toast.LENGTH_SHORT).show();
    }


}
