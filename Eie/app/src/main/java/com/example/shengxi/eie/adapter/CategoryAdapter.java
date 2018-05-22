package com.example.shengxi.eie.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.shengxi.eie.R;
import com.example.shengxi.eie.activity.CategoryActivity;
import com.example.shengxi.eie.activity.VideoTeachActivity;
import com.example.shengxi.eie.beans.ClassMenuBean;
import com.example.shengxi.eie.utils.DataUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by ShengXi on 2017/5/8.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {
    private ClassMenuBean data;
    private Context context;

    public CategoryAdapter(Context context, ClassMenuBean data) {

        Log.e("data", data.toString());
        this.data = data;
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_list,null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder,  int position) {
        holder.title.setText(data.classes.get(position).title);
        holder.teacher.setText(data.classes.get(position).teacher);
        holder.viewer.setText(String.valueOf(data.classes.get(position).viewer));
        Picasso.with(context)
                .load(DataUtils.baseUrl+data.classes.get(position).img)
                .placeholder(R.mipmap.ico_user_default)
                .error(R.mipmap.ico_user_default)
                .into(holder.img);

        final int i = position;
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String []arr = {data.classes.get(i-1).teacher,data.classes.get(i-1).title,data.classes.get(i-1).summary,data.classes.get(i-1).url,data.classes.get(i-1).id};
                Intent in = new Intent(context, VideoTeachActivity.class);
                in.putExtra("data",arr);
                context.startActivity(in);
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return data.classes.size();
    }

//    @Override
//    public View getView(final int i, View view, ViewGroup viewGroup) {
//        final Item item;
//        if (view == null){
//            view = inflater.inflate(R.layout.item_category_list,null);
//            item = new Item();
//            item.img = (ImageView) view.findViewById(R.id.item_cate_imageview);
//            item.title = (TextView) view.findViewById(R.id.item_cate_title);
//            item.teacher = (TextView) view.findViewById(R.id.item_cate_teacher);
//            item.viewer = (TextView) view.findViewById(R.id.item_cate_count);
//            item.cardView = (CardView) view.findViewById(R.id.category_list_card);
//            view.setTag(item);
//
//        }else {
//            item = (Item) view.getTag();
//        }
//        item.title.setText(data.classes.get(i).title);
//        item.teacher.setText(data.classes.get(i).teacher);
//        item.viewer.setText(String.valueOf(data.classes.get(i).viewer));
//        Picasso.with(context)
//                .load(DataUtils.baseUrl+data.classes.get(i).img)
//                .placeholder(R.mipmap.ico_user_default)
//                .error(R.mipmap.ico_user_default)
//                .into(item.img);
//
//        item.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String []arr = {data.classes.get(i-1).teacher,data.classes.get(i-1).title,data.classes.get(i-1).summary,data.classes.get(i-1).url,data.classes.get(i-1).id};
//                Intent in = new Intent(context, VideoTeachActivity.class);
//                in.putExtra("data",arr);
//                context.startActivity(in);
//            }
//        });
//        return  view;
//    }


    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_cate_imageview)
        ImageView img;
        @BindView(R.id.item_cate_title)
        TextView title;
        @BindView(R.id.item_cate_teacher)
        TextView teacher;
        @BindView(R.id.item_cate_count)
        TextView viewer;

        @BindView(R.id.category_list_card)
        CardView cardView;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
