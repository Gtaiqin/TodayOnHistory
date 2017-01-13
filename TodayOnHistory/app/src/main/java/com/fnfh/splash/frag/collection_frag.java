package com.fnfh.splash.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fnfh.R;
import com.fnfh.splash.DetailsActivity;
import com.fnfh.splash.bean.SQLBean;
import com.fnfh.splash.helper.ItemTouchHelperAdapter;
import com.fnfh.splash.helper.ItemTouchHelperViewHolder;
import com.fnfh.splash.helper.OnStartDragListener;
import com.fnfh.splash.helper.SimpleItemTouchHelperCallback;
import com.fnfh.splash.util.MyDatabase;
import com.fnfh.splash.view.RecyclerViewClickListener;
import com.fnfh.splash.view.SpaceItemDecoration;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 李泰亲 on 2016/12/15.
 * 收藏页面
 */

public class collection_frag extends Fragment implements OnStartDragListener {

    @BindView(R.id.collection_rv)
    RecyclerView collectionRv;
    private View view;
    private List<SQLBean> select;
    private MyDatabase database;
    private MycollectionRvAdapter adapter;
    private ItemTouchHelper itemTouchHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_collection, container, false);
        ButterKnife.bind(this, view);
        //设置布局管理器
        collectionRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        //创建数据库对象
        database = new MyDatabase(getActivity());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //查询数据库数据并展示
        select = database.select();
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        //设置recyclerview条目之间的间距
        collectionRv.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        adapter = new MycollectionRvAdapter();
        collectionRv.setHasFixedSize(true);
        collectionRv.setAdapter(adapter);
        //设置条目点击事件，点击跳转对应的收藏条目的详情
        collectionRv.addOnItemTouchListener(new RecyclerViewClickListener(getActivity(), collectionRv,
                new RecyclerViewClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        intent.putExtra("id", select.get(position).getData_id() + "");
                        Toast.makeText(getActivity(), select.get(position).getData_id() + "", Toast.LENGTH_SHORT).show();
                        intent.putExtra("title", select.get(position).getTitle() + "");
                        intent.putExtra("date", select.get(position).getData() + "");
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
        //给recyclerview设置左右滑动删除
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(collectionRv);

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    class MycollectionRvAdapter extends RecyclerView.Adapter<MycollectionRvAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MycollectionRvAdapter.MyViewHolder holder = new MycollectionRvAdapter.MyViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.collection_rvitem, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.collection_title.setText(select.get(position).getData());
            holder.collection_context.setText(select.get(position).getTitle());
            Glide.with(collection_frag.this).load(select.get(position).getUrl())
                    .into(holder.collection_img);
        }

        @Override
        public int getItemCount() {
            return select.size();
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            Collections.swap(select, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onItemDismiss(int position) {
            database.delete(select.get(position).getData_id());
            select.remove(position);
            notifyItemRemoved(position);
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
            TextView collection_title, collection_context;
            ImageView collection_img;

            public MyViewHolder(View itemView) {
                super(itemView);
                collection_title = (TextView) itemView.findViewById(R.id.collection_title);
                collection_context = (TextView) itemView.findViewById(R.id.collection_context);
                collection_img = (ImageView) itemView.findViewById(R.id.collection_img);
            }

            @Override
            public void onItemSelected() {

            }

            @Override
            public void onItemClear() {

            }
        }
    }
}
