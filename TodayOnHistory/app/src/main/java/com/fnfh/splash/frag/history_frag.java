package com.fnfh.splash.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fnfh.R;
import com.fnfh.splash.CalendarActivity;
import com.fnfh.splash.DetailsActivity;
import com.fnfh.splash.bean.DateBean;
import com.fnfh.splash.bean.History_data;
import com.fnfh.splash.util.OkHttp;
import com.fnfh.splash.view.RecyclerViewClickListener;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by w9072 on 2016/12/15.
 */

public class history_frag extends Fragment {
    @BindView(R.id.fab_history_frag)
    FloatingActionButton fabHistoryFrag;
    @BindView(R.id.cdnt_history_frag)
    CoordinatorLayout cdntHistoryFrag;
    @BindView(R.id.rv_history_frag)
    RecyclerView rvHistoryFrag;
    @BindView(R.id.last_img)
    ImageView lastImg;
    @BindView(R.id.date_tv)
    TextView dateTv;
    @BindView(R.id.next_img)
    ImageView nextImg;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private int year;
    private int month;
    private int day;
    private List<History_data.ResultBean> result1;
    String Url = "http://v.juhe.cn/todayOnhistory/queryEvent.php?key=69a7eeba7869f8bdcdee7b2bc3bb5aa2&date=";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_history, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        //得到系统时间信息并设置
        gsDate();

        //悬浮日历按钮
        fabHistoryFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
            }
        });
        //获取网络数据
        getHttpData();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHttpData();

            }
        });
        //RecyclerView设置布局管理器
        rvHistoryFrag.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置条目点击事件
        rvHistoryFrag.addOnItemTouchListener(new RecyclerViewClickListener(getActivity(), rvHistoryFrag,
                new RecyclerViewClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(getActivity(), "点击" + position, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        intent.putExtra("id", result1.get(result1.size() - position - 1).getE_id());
                        intent.putExtra("title", result1.get(result1.size() - position - 1).getTitle());
                        intent.putExtra("date", result1.get(result1.size() - position - 1).getDate());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        //Toast.makeText(getActivity(), "长按" + position, Toast.LENGTH_SHORT).show();
                    }
                }));
        return view;
    }

    private void gsDate() {
        //取得系统日期:
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        month = month + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        dateTv.setText(year + "年" + month + "月" + day + "日");
        //Toast.makeText(getActivity(), "年:" + year + "月:" + month + "日:" + day, Toast.LENGTH_SHORT).show();
        lastImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (day >= 2) {
                    day = day - 1;
                } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                    day = 31;
                    if (month >= 2) {
                        month = month - 1;
                    } else {
                        month = 12;
                        year = year - 1;
                    }
                } else if (month == 2 || month == 4 || month == 6 || month == 9 || month == 11) {
                    day = 30;
                    if (month >= 2) {
                        month = month - 1;
                    } else {
                        month = 12;
                        year = year - 1;
                    }
                }
                dateTv.setText(year + "年" + month + "月" + day + "日");
                getHttpData();
            }
        });
        nextImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (month <= 11) {
                    if (day <= 29) {
                        day = day + 1;
                    } else {
                        day = 1;
                        month = month + 1;
                    }
                } else if (month == 12) {
                    if (day <= 29) {
                        day = day + 1;
                    } else if (day == 30) {
                        day = 1;
                        month = 1;
                        year = year + 1;
                    }
                }
                dateTv.setText(year + "年" + month + "月" + day + "日");
                getHttpData();
            }
        });
    }

    public void getHttpData() {
        OkHttp.getAsync(Url + month + "/" + day, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {

            }

            @Override
            public void requestSuccess(String result) throws Exception {
                refreshLayout.setRefreshing(false);
                //解析网络数据
                Gson gson = new Gson();
                History_data history_data = gson.fromJson(result, History_data.class);
                result1 = history_data.getResult();
                //设置适配器
                rvHistoryFrag.setAdapter(new MyRvAdapter(result1));
            }
        });
    }

    class MyRvAdapter extends RecyclerView.Adapter<MyRvAdapter.MyViewHolder> {
        List<History_data.ResultBean> result1;

        public MyRvAdapter(List<History_data.ResultBean> result1) {
            this.result1 = result1;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.history_frag_rv_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            int size = result1.size();
            holder.history_item_title.setText(result1.get(size - position - 1).getDate());
            holder.history_item_context.setText(result1.get(size - position - 1).getTitle());
        }

        @Override
        public int getItemCount() {
            return result1.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView history_item_title;
            private TextView history_item_context;

            public MyViewHolder(View itemView) {
                super(itemView);
                history_item_title = (TextView) itemView.findViewById(R.id.history_item_title);
                history_item_context = (TextView) itemView.findViewById(R.id.history_item_context);
            }
        }
    }

    @Subscribe
    public void onMessageEvent(DateBean db) {
        year = db.getYear();
        month = db.getMonth();
        day = db.getDay();
        dateTv.setText(year + "年" + month + "月" + day + "日");
        getHttpData();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
