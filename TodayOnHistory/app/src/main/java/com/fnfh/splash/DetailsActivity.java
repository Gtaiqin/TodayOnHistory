package com.fnfh.splash;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fnfh.R;
import com.fnfh.splash.bean.History_context;
import com.fnfh.splash.util.MyDatabase;
import com.fnfh.splash.util.OkHttp;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.details_fab)
    FloatingActionButton detailsFab;
    @BindView(R.id.tv_details)
    TextView tvDetails;
    @BindView(R.id.details_bg_img)
    ImageView detailsBgImg;
    private boolean if_collection;
    private String content;
    private List<History_context.ResultBean> result1;
    //private DbUtils historyUtil;
    private String url;
    private static String id;
    private String title;
    private String date;
    private MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //得到传递的历史信息ID
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        date = intent.getStringExtra("date");
        //创建数据库对象
        database = new MyDatabase(this);
        //根据传过来的ID查询数据库，按是否有收藏的该条数据
        int idread = database.idread(id);
        if (idread > 0) {
            if_collection = true;
            detailsFab.setImageResource(R.mipmap.collection_true);
        } else {
            if_collection = false;
            detailsFab.setImageResource(R.mipmap.collection_false);
        }
        //根据ID获取数据设置折叠区的图片
        getHttpData(id);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.mipmap.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle(title);
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.BLACK);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
        //对收藏按钮进行监听
        detailsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (if_collection) {
                    detailsFab.setImageResource(R.mipmap.collection_false);
                    boolean delete = database.delete(id);
                    if (delete == true) {
                        Toast.makeText(DetailsActivity.this, "取消收藏!", Toast.LENGTH_SHORT).show();
                    }
                    if_collection = false;
                } else {
                    detailsFab.setImageResource(R.mipmap.collection_true);
                    boolean insert = database.insert(id, date, title, url);
                    if (insert == true) {
                        Toast.makeText(DetailsActivity.this, "添加收藏成功!", Toast.LENGTH_SHORT).show();
                    }
                    if_collection = true;
                }
            }
        });
    }

    public void getHttpData(String id) {
        OkHttp.getAsync("http://v.juhe.cn/todayOnhistory/queryDetail.php?key=69a7eeba7869f8bdcdee7b2bc3bb5aa2&e_id=" + id, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {

            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson = new Gson();
                History_context history_context = gson.fromJson(result, History_context.class);
                result1 = history_context.getResult();
                content = result1.get(0).getContent();
                url = result1.get(0).getPicUrl().get(0).getUrl();
                Glide.with(DetailsActivity.this).load(url)
                        .into(detailsBgImg);
                tvDetails.setText(content);
            }
        });
    }
}
