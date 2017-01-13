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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fnfh.R;
import com.fnfh.splash.GirlDetailsActivity;
import com.fnfh.splash.bean.Girl_data;
import com.fnfh.splash.util.OkHttp;
import com.fnfh.splash.view.RecyclerViewClickListener;
import com.fnfh.splash.view.SpaceItemDecoration;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by 李泰亲 on 2016/12/15.
 * 妹子页面
 */

public class girl_frag extends Fragment {
    @BindView(R.id.girl_refresh)
    SwipeRefreshLayout girlRefresh;
    @BindView(R.id.frag_gile_rv)
    RecyclerView fragGileRv;
    private View view;
    private String Url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/20/";
    private int pager_num = 2;
    private int lastVisibleItem;
    private List<Girl_data.ResultsBean> results;
    private StaggeredGridLayoutAdapter recyclerAdapter;
    private List<Girl_data.ResultsBean> mList;
    private GridLayoutManager sgm;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_girl, container, false);
        ButterKnife.bind(this, view);
        //设置recyclerview
        setRecycler();
        mList = new ArrayList<>();
        //请求数据
        getHpptData();
        //设置监听事件
        setListener();
        return view;
    }

    private void setListener() {
        //添加recyclerview点击事件，调转妹子详情
        fragGileRv.addOnItemTouchListener(new RecyclerViewClickListener(getActivity(), fragGileRv,
                new RecyclerViewClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), GirlDetailsActivity.class);
                        intent.putExtra("girl_url", mList.get(position).getUrl());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
        //设置刷新事件监听
        girlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHpptData();
            }
        });
        //设置recyclerview滑动监听，滑动到底部加载下一页。
        fragGileRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                /*if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == mList.size()) {
                    pager_num++;
                    getHpptData();
                    recyclerAdapter.notifyDataSetChanged();
                }*/
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // lastVisibleItem = sgm.findLastVisibleItemPosition();
                int visible = sgm.getChildCount();
                int total = sgm.getItemCount();
                int past = sgm.findFirstCompletelyVisibleItemPosition();
                if ((visible + past) >= total) {
                    Toast.makeText(getActivity(), "慢点滑呦！", Toast.LENGTH_SHORT).show();
                    pager_num++;
                    getHpptData();
                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setRecycler() {
        //设置recyclerview布局管理器
        sgm = new GridLayoutManager(getActivity(), 2);
        fragGileRv.setLayoutManager(sgm);
        //设置recyclerview子条目之间的间距
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        fragGileRv.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
    }

    //网络请求妹子图片信息，并将请求的数据添加到集合，使用此集合让recyclerview展示数据
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
                for (int i = 0; i < results.size(); i++) {
                    mList.add(results.get(i));
                }
                recyclerAdapter = new StaggeredGridLayoutAdapter(mList);
                if (pager_num == 2) {
                    fragGileRv.setAdapter(recyclerAdapter);
                } else {
                    recyclerAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    class StaggeredGridLayoutAdapter extends RecyclerView.Adapter<StaggeredGridLayoutAdapter.MyViewHolder> {
        private List<Girl_data.ResultsBean> list;

        public StaggeredGridLayoutAdapter(List<Girl_data.ResultsBean> list) {
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.staggered_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Glide.with(girl_frag.this)
                    .load(list.get(position).getUrl())
                    .into(holder.staggered_img);
        }

        @Override
        public int getItemCount() {
            return list.size();
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
