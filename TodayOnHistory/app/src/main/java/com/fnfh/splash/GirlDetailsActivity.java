package com.fnfh.splash;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fnfh.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GirlDetailsActivity extends AppCompatActivity {

    @BindView(R.id.girl_details_bar)
    Toolbar girlDetailsBar;
    @BindView(R.id.girl_details_img)
    ImageView girlDetailsImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girl_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String girl_url = intent.getStringExtra("girl_url");
        girlDetailsBar.setTitle("妹纸");
        girlDetailsBar.setTitleTextColor(Color.WHITE);
        girlDetailsBar.setNavigationIcon(R.mipmap.back);
        girlDetailsBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Glide.with(GirlDetailsActivity.this).load(girl_url)
                .into(girlDetailsImg);
    }
}
