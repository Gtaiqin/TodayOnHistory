package com.fnfh.splash.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fnfh.R;
import com.fnfh.splash.GirlDetailsActivity;
import com.fnfh.splash.bean.Girl_data;
import com.fnfh.splash.util.OkHttp;
import com.fnfh.splash.view.RecyclerViewClickListener;
import com.fnfh.splash.view.SpaceItemDecoration;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by w9072 on 2016/12/15.
 */

public class girl_frag extends Fragment {

    @BindView(R.id.girl_refresh)
    SwipeRefreshLayout girlRefresh;
    @BindView(R.id.frag_gile_rv)
    RecyclerView fragGileRv;
    private View view;
    private String Url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/20/";
    private int pager_num = 2;
    private List<Girl_data.ResultsBean> results;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_girl, container, false);
        ButterKnife.bind(this, view);
        GridLayoutManager sgm = new GridLayoutManager(getActivity(), 2);
        fragGileRv.setLayoutManager(sgm);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        fragGileRv.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        getHpptData();
        fragGileRv.addOnItemTouchListener(new RecyclerViewClickListener(getActivity(), fragGileRv,
                new RecyclerViewClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), GirlDetailsActivity.class);
                        intent.putExtra("girl_url", results.get(position).getUrl());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
        girlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHpptData();
            }
        });
        return view;
    }

    public void getHpptData() {
        OkHttp.getAsync(Url + pager_num, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {

            }

            @Override
            public void requestSuccess(String result) throws Exception {
                girlRefresh.setRefreshing(false);
                Gson gson = new Gson();
                Girl_data girl_data = gson.fromJson(result, Girl_data.class);
                results = girl_data.getResults();
                fragGileRv.setAdapter(new StaggeredGridLayoutAdapter(results));
            }
        });
    }

    class StaggeredGridLayoutAdapter extends RecyclerView.Adapter<StaggeredGridLayoutAdapter.MyViewHolder> {
        private List<Girl_data.ResultsBean> results;

        public StaggeredGridLayoutAdapter(List<Girl_data.ResultsBean> results) {
            this.results = results;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.staggered_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Glide.with(girl_frag.this)
                    .load(results.get(position).getUrl())
                    .into(holder.staggered_img);
        }

        @Override
        public int getItemCount() {
            return results.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView staggered_img;

            public MyViewHolder(View itemView) {
                super(itemView);
                staggered_img = (ImageView) itemView.findViewById(R.id.staggered_img);
            }
        }
    }
}
