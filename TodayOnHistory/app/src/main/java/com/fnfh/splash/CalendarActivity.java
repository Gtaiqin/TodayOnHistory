package com.fnfh.splash;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.fnfh.R;
import com.fnfh.splash.bean.DateBean;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarActivity extends AppCompatActivity {

    @BindView(R.id.calendar_ToolBar)
    Toolbar calendarToolBar;
    @BindView(R.id.mCalendarView)
    CalendarView mCalendarView;
    @BindView(R.id.CalendarQuery_bt)
    Button CalendarQueryBt;
    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);
        getDate();
        calendarToolBar.setNavigationIcon(R.mipmap.back);
        calendarToolBar.setTitle("当前日期");
        calendarToolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(calendarToolBar);
        calendarToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                Toast.makeText(CalendarActivity.this, year + "年" + (month + 1) + "月" + day + "日", Toast.LENGTH_SHORT).show();
                mYear = year;
                mMonth = month+1;
                mDay = day;
            }
        });

    }
    //获取系统当前日期信息
    private void getDate() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mMonth = mMonth + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        CalendarQueryBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new DateBean(mYear, mMonth, mDay));
                finish();
            }
        });
    }
}
