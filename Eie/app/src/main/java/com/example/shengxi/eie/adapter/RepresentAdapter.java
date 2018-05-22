package com.example.shengxi.eie.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.shengxi.eie.R;
import com.example.shengxi.eie.beans.RepresentBean;
import com.example.shengxi.eie.utils.CircleImageView;
import com.example.shengxi.eie.utils.DataUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by ShengXi on 2017/5/8.
 */

public class RepresentAdapter extends RecyclerView.Adapter<RepresentAdapter.Item> {

    private LayoutInflater inflater;
    private RepresentBean data;
    private Context context;

    public RepresentAdapter(Context context, RepresentBean data) {
        if (data != null) {
            this.inflater = LayoutInflater.from(context);
            this.data = data;
            this.context = context;
        }

    }


    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_represent_list,parent,false);
        Item item = new Item(view);
        return item;
    }

    @Override
    public void onBindViewHolder(Item holder, int position) {
        Picasso.with(context)
                .load(DataUtils.baseUrl + data.represents.get(position).representImg)
                .placeholder(R.mipmap.ico_user_default)
                .error(R.mipmap.ico_user_default)
                .into(holder.imageView);
        holder.name.setText(data.represents.get(position).representName);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return data==null ?0:data.represents.size();
    }

//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        final Item item;
//        if (view == null) {
//            view = inflater.inflate(R.layout.item_represent_list, null);
//            item = new Item();
//            item.imageView = (CircleImageView) view.findViewById(R.id.repre_imageview);
//            item.name = (TextView) view.findViewById(R.id.repre_name);
//            view.setTag(item);
//
//        } else {
//            item = (Item) view.getTag();
//        }
//        Picasso.with(context)
//                .load(DataUtils.baseUrl + data.represents.get(i).representImg)
//                .placeholder(R.mipmap.ico_user_default)
//                .error(R.mipmap.ico_user_default)
//                .into(item.imageView);
//        item.name.setText(data.represents.get(i).representName);
//        return view;
//    }

    class Item extends RecyclerView.ViewHolder {
        @BindView(R.id.repre_imageview)
        CircleImageView imageView;
        TextView name;

        public Item(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
