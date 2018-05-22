package com.example.shengxi.eie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shengxi.eie.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by ShengXi on 2018-05-12.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Holder> {

    private Context mContext;

    public SearchAdapter(Context context){

        this.mContext = context;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_category_list,null);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

//        Picasso.with(mContext)
//                .load()
//                .placeholder(R.mipmap.ic_launcher)
//                .into(holder.imageView);
//        holder.title.setText();

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class Holder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_cate_imageview)
        ImageView imageView;

        @BindView(R.id.item_cate_title)
        TextView title;

        @BindView(R.id.item_cate_teacher)
        TextView teacher;

        @BindView(R.id.item_cate_count)
        TextView viewer;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(mContext,itemView);

        }
    }
}
