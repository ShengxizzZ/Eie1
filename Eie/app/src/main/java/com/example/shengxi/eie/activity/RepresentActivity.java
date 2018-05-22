package com.example.shengxi.eie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shengxi.eie.R;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 *
 * Created by ShengXi on 2017/5/8.
 */

public class RepresentActivity extends AppCompatActivity{

    @BindView(R.id.repreinfo_title)
    TextView title;
    @BindView(R.id.repreinfo_text)
    TextView text;


    private String[] temp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_represent_info);
        ButterKnife.bind(this);
        Intent in = getIntent();
        temp = in.getStringArrayExtra("data");
        initView();
        setData();
    }

    private void setData() {
        title.setText(temp[0]);
        text.setText(temp[1]);

    }

    private void initView() {

        //title = (TextView) findViewById(R.id.repreinfo_title);
        //text = (TextView) findViewById(R.id.repreinfo_text);
        ImageView repreBack = (ImageView) findViewById(R.id.repre_back);
        repreBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
