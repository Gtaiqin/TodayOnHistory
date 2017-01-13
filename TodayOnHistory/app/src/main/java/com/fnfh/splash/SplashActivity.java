package com.fnfh.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.fnfh.R;

public class SplashActivity extends AppCompatActivity {
    // 声明控件对象
    private TextView timedown_tv;
    private int count = 3;
    private Animation animation;

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                timedown_tv.setText(getCount() + "秒");
                handler.sendEmptyMessageDelayed(0, 1000);
                animation.reset();
                timedown_tv.startAnimation(animation);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置去掉手机的顶部时间、日期等栏目
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        //获取控件ID
        timedown_tv = (TextView) findViewById(R.id.timeDown_tv);
        //设置动画
        animation = AnimationUtils.loadAnimation(this, R.anim.time_down);
        //设置倒计时
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    private int getCount() {
        count--;
        if (count == 0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return count;
    }
}
