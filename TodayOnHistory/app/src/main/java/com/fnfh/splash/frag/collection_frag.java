package com.fnfh.splash.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fnfh.R;
import com.fnfh.splash.bean.SqlBean;
import com.fnfh.splash.util.DbUtils;
import com.fnfh.splash.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by w9072 on 2016/12/15.
 */

public class collection_frag extends Fragment {

    @BindView(R.id.collection_rv)
    RecyclerView collectionRv;
    private View view;
    private ArrayList<String> id_list;
    private List<SqlBean> select;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_collection, container, false);
        ButterKnife.bind(this, view);
        collectionRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        DbUtils util = new DbUtils(getActivity());
        select = util.select();
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        collectionRv.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        collectionRv.setAdapter(new MycollectionRvAdapter());
        return view;
    }

    class MycollectionRvAdapter extends RecyclerView.Adapter<MycollectionRvAdapter.MyViewHolder> {
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
        }

        @Override
        public int getItemCount() {
            return select.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView collection_title, collection_context;
            ImageView collection_img;

            public MyViewHolder(View itemView) {
                super(itemView);
                collection_title = (TextView) itemView.findViewById(R.id.collection_title);
                collection_context = (TextView) itemView.findViewById(R.id.collection_context);
                collection_img = (ImageView) itemView.findViewById(R.id.collection_img);
            }
        }
    }
}
