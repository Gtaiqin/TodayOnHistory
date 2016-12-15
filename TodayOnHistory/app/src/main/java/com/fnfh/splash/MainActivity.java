package com.fnfh.splash;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.fnfh.R;
import com.fnfh.splash.frag.about_frag;
import com.fnfh.splash.frag.collection_frag;
import com.fnfh.splash.frag.girl_frag;
import com.fnfh.splash.frag.history_frag;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolBar;
    private DrawerLayout mDrawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    history_frag history = new history_frag();
    collection_frag collection = new collection_frag();
    girl_frag girl = new girl_frag();
    about_frag about = new about_frag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();

        mToolBar = (Toolbar) findViewById(R.id.mToolBar); //ToolBar
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout); //DrawerLayout
        navigationView = (NavigationView) findViewById(R.id.nav); //NavigationView导航栏
        mToolBar.setTitle("历史上的今天");
        mToolBar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        setSupportActionBar(mToolBar);

        //设置左上角的图标响应
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolBar, 0, 0) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawer.setDrawerListener(mDrawerToggle); //设置侧滑监听

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        manager.beginTransaction().add(R.id.content_frame, history).commit();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        manager.beginTransaction().replace(R.id.content_frame, history).commit();
                        item.setChecked(true);
                        break;
                    case R.id.item2:
                        manager.beginTransaction().replace(R.id.content_frame, girl).commit();
                        item.setChecked(true);
                        break;
                    case R.id.item3:
                        manager.beginTransaction().replace(R.id.content_frame, collection).commit();
                        item.setChecked(true);
                        break;
                    case R.id.item4:
                        manager.beginTransaction().replace(R.id.content_frame, about).commit();
                        item.setChecked(true);
                        break;
                }
                mDrawer.closeDrawer(Gravity.LEFT);
                return false;
            }
        });
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                Gravity.RIGHT);
    }
}
