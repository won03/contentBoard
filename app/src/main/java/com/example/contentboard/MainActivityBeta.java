package com.example.contentboard;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.contentboard.PostDATA.CheckActivity;
import com.example.contentboard.adapter.MyAdapter;
import com.example.contentboard.adapter.RecycleAdapter;
import com.example.contentboard.datas.BackpressActivity;
import com.example.contentboard.datas.Token;
import com.example.contentboard.datas.contentData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/*여기는 리스트 페이지*/
public class MainActivityBeta extends CheckActivity implements View.OnClickListener {
    Button writeBtn, findBtn;
    ImageView serachBtn, menuside;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerAdapter;
    LinearLayoutManager linearLayoutManager;
    RecycleAdapter recycleAdapter;
    TextView showTv;
    ArrayList<contentData> dataArr;//contentData 데이터를 담고있는 배열
    ProgressBar progressBar;
    EditText serachET;
    DrawerLayout drawer;
    public BackpressActivity backPressCloseHandler;

    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        writeBtn = findViewById(R.id.writin_btn);
        serachBtn = findViewById(R.id.search_btn);
        findBtn = findViewById(R.id.find_btn);
        recyclerView = findViewById(R.id.list_v);
        serachET = findViewById(R.id.search_id);

        showTv = findViewById(R.id.tv_view);
        menuside = findViewById(R.id.menu_btn);
        linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        dataArr = new ArrayList<>();
        progressBar = findViewById(R.id.progressbar);
        recyclerAdapter = new RecycleAdapter(this, dataArr, pos);
        recyclerView.setAdapter(recyclerAdapter);
        findBtn.setOnClickListener(this);
        drawer = findViewById(R.id.draw_layout);
        progressBar.setVisibility(View.GONE);
        backPressCloseHandler = new BackpressActivity(this);
        getServer();
        final Intent intent = new Intent(MainActivityBeta.this, WritingActivity.class);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        serachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTv.setVisibility(View.INVISIBLE);
                writeBtn.setVisibility(View.INVISIBLE);
                serachET.setVisibility(View.VISIBLE);
                findBtn.setVisibility(View.VISIBLE);
            }
        });
        menuside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.find_btn) {
            showTv.setVisibility(View.VISIBLE);
            writeBtn.setVisibility(View.VISIBLE);
            serachET.setVisibility(View.INVISIBLE);
            findBtn.setVisibility(View.INVISIBLE);
            findContent();


        }
    }

    int posidx = -1;
    String idxPos = String.valueOf(posidx);
    public void getServer() {
        params.clear();
        dataArr.clear();
        params.put("token", Token.token);
        params.put("idx", idxPos);
        Log.d("won", "리스뷰아이디!!!-->" + posidx);
        request("test_board_list.php", this);


    }

//사용자 아이디를 입력하면 사용자가 쓴 내용만 보여주는 서버통신 메소드
    public void findContent() {
        String userId = serachET.getText().toString();
        String id = dataArr.get(pos).idx;
        params.clear();
        dataArr.clear();
        params.put("idx", id);
        params.put("userid", userId);
        request("test_member_board_list.php", this);
        Log.d("won", "click id-->");
    }




    @Override
    public void onResponse(String s) {

        super.onResponse(s);
        Log.d("won", "리스트:" + s);
        try {

            JSONObject object = new JSONObject(s);
            JSONArray jsonArray = object.getJSONArray("list");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                String title = temp.getString("title");
                String content = temp.getString("content");
                String writer = temp.getString("writer");
                String id = temp.getString("idx");
                idxPos = temp.getString("idx");
                contentData contentData = new contentData(title, content, writer, id);
                dataArr.add(contentData);
                Log.d("won", "list:" + id);
                recyclerAdapter.notifyDataSetChanged();

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }


}
