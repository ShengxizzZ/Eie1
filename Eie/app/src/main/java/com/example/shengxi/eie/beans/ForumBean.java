package com.example.shengxi.eie.beans;

import java.util.List;

/**
 * Created by ShengXi on 2017/4/23.
 */

public class ForumBean {

    public List<Forum> forum;
    public static class Forum{
        public String articleId;
        public String articleTitle;
        public String articleTime;
        public String articleUpId;
        public String articleUpName;
        public String articleSummary;
        public int ariticleComments;
        public int ariticleDiggs;
        public String articleMain;
        public String articleUpImg;


    }
}
