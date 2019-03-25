package com.example.contentboard.datas;
//글쓰기를 위한 생성자 데이터를 파라미터로 받아오는곳
public class contentData {

    public contentData(String title, String content, String token,String idx) {
        this.title = title;
        this.content = content;
        this.token = token;
        this.idx=idx;

    }

    public String title;
    public String content;
    public String token;
    public String idx;



}
