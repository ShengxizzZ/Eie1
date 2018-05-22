package com.example.shengxi.eie.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shengxi.eie.R;
import com.example.shengxi.eie.activity.VideoTeachActivity;
import com.example.shengxi.eie.beans.ClassMenuBean;
import com.example.shengxi.eie.utils.DataUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.vov.vitamio.ThumbnailUtils;
import io.vov.vitamio.provider.MediaStore;


/**
 *
 * Created by ShengXi on 2017/4/20.
 */

public class ClassMenuAdapter extends RecyclerView.Adapter<ClassMenuAdapter.Holder> {

    private ClassMenuBean data;
    private Context context;

    public ClassMenuAdapter(Context context, ClassMenuBean data) {
        if (data != null) {
            this.context = context;
            this.data = data;
        }
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_classmenu, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        if (data!=null){
            holder.index.setText(data.classes.get(position).id);
            holder.teacher.setText(data.classes.get(position).teacher);
            holder.viewer.setText(String.valueOf(data.classes.get(position).viewer));
            setImage(holder, position);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(context, VideoTeachActivity.class);
                    String[] arr = {data.classes.get(position).teacher, data.classes.get(position).title, data.classes.get(position).summary, data.classes.get(position).url, data.classes.get(position).id};
                    in.putExtra("data", arr);
                    context.startActivity(in);

                }
            });
        }

    }

    private void setImage(final Holder holder, final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //设置缩略图,Vitamio提供的工具类。
                final Bitmap videoThumbnail = ThumbnailUtils.createVideoThumbnail(
                        context, DataUtils.baseUrl + data.classes.get(position).url
                        , MediaStore.Video.Thumbnails.MINI_KIND);
                if (videoThumbnail != null) {
                    holder.img.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.img.setDrawingCacheQuality(ImageView.DRAWING_CACHE_QUALITY_LOW);
                            holder.img.setImageBitmap(videoThumbnail);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.classes.size();
        }
        return 0;
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_live_cover)
        ImageView img;

        @BindView(R.id.item_live_title)
        TextView title;

        @BindView(R.id.item_live_user)
        TextView teacher;

        @BindView(R.id.item_live_count)
        TextView viewer;

        @BindView(R.id.gv_index)
        TextView index;

        @BindView(R.id.classMenu_cardView)
        CardView cardView;

        Holder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
