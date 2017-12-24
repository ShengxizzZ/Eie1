package com.example.shengxi.eie.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.shengxi.eie.beans.ForumBean;
import com.example.shengxi.eie.utils.CircleImageView;
import com.example.shengxi.eie.utils.DataUtils;

/**
 * Created by ShengXi on 2017/4/23.
 */

public class ForumAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private RequestQueue queue;
    private ForumBean data;

    public ForumAdapter(Context context, ForumBean data) {
        queue = Volley.newRequestQueue(context);
        if (data != null) {
            this.inflater = LayoutInflater.from(context);
            this.data = data;
        }
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.forum.size();
        } else {
            return 0;
        }

    }

    @Override
    public Object getItem(int i) {
        return null;
    }



    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Item item;
        if (view == null) {
            view = inflater.inflate(R.layout.item_forum_list, null);
            item = new Item();
            item.imageView = (CircleImageView) view.findViewById(R.id.forum_circleimageview);
            item.upName = (TextView) view.findViewById(R.id.forum_up_name);
            item.time = (TextView) view.findViewById(R.id.forum_uptime);
            item.diggs = (TextView) view.findViewById(R.id.forum_diggs);
            item.comments = (TextView) view.findViewById(R.id.forum_comments);
            item.title = (TextView) view.findViewById(R.id.forum_title);
            item.summary = (TextView) view.findViewById(R.id.forum_summary);
            view.setTag(item);
        } else {
            item = (Item) view.getTag();
        }
        ImageRequest request = new ImageRequest(DataUtils.baseUrl + data.forum.get(i).articleUpImg, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                item.imageView.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);

        item.upName.setText(data.forum.get(i).articleUpName);
        item.diggs.setText(String.valueOf(data.forum.get(i).ariticleDiggs)+"赞");
        item.comments.setText(String.valueOf(data.forum.get(i).ariticleComments)+"评论");
        Log.e("comments",String.valueOf(data.forum.get(i).ariticleComments));


        item.time.setText(data.forum.get(i).articleTime);
        item.summary.setText(data.forum.get(i).articleSummary);
        item.title.setText(data.forum.get(i).articleTitle);
        return view;
    }

    public class Item {

        public CircleImageView imageView;
        public TextView title;
        public TextView time;
        public TextView comments;
        public TextView diggs;
        public TextView upName;
        public TextView summary;
    }
}
