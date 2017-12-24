package com.example.shengxi.eie.beans;

import java.util.List;

/**
 * Created by ShengXi on 2017/4/25.
 */

public class CommentsBean {

    public List<Comments> comments;
    public static class Comments{
        public String commentsId;
        public String commentsName;
        public String mainText;
        public String commentsDate;
        public String commentsImg;
    }
}
