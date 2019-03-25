package com.example.contentboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.contentboard.PostDATA.CheckActivity;
import com.example.contentboard.adapter.MyAdapter;
import com.example.contentboard.datas.BackpressActivity;
import com.example.contentboard.datas.Token;
import com.example.contentboard.datas.contentData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/*사용하지않음*/
public class MainActivity extends CheckActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    Button writeBtn, findBtn;
    ImageView serachBtn, menuside;
    ListView listView;  //리스트뷰
    MyAdapter listAdapter;
    TextView showTv;
    ArrayList<contentData> dataArr;//contentData 데이터를 담고있는 배열
    ProgressBar progressBar;
    EditText serachET;
    DrawerLayout drawer;
    private boolean lastItemVisibleFlag = false;//리스트 스크롤이 마지막 셀로이동했는지 체크
    int page = 0;
    private boolean mLockListView = true;
    public BackpressActivity backPressCloseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        writeBtn = findViewById(R.id.writin_btn);
        serachBtn = findViewById(R.id.search_btn);
        findBtn = findViewById(R.id.find_btn);
        listView = findViewById(R.id.list_v);
        serachET = findViewById(R.id.search_id);
        showTv = findViewById(R.id.tv_view);
        menuside = findViewById(R.id.menu_btn);
        mLockListView = true;
        dataArr = new ArrayList<>();
        progressBar = findViewById(R.id.progressbar);
        listAdapter = new MyAdapter(this, dataArr);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
        findBtn.setOnClickListener(this);
        drawer = findViewById(R.id.draw_layout);
        progressBar.setVisibility(View.GONE);
        getServer();
        backPressCloseHandler = new BackpressActivity(this);
        final Intent intent = new Intent(MainActivity.this, WritingActivity.class);
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

    int pos;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        pos = position;
        String title = dataArr.get(position).title;
        String content = dataArr.get(position).content;
        String writer = dataArr.get(position).token;
        String idx = dataArr.get(position).idx;
        Intent intent = new Intent(this, com.example.contentboard.DetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("writer", writer);
        intent.putExtra("idx", idx);
        startActivity(intent);
        Log.d("won", "onitemClicklist id-->" + position);
    }


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
    protected void onResume() {
        super.onResume();
        getServer();

    }


    @Override
    public void onResponse(String s) {

        super.onResponse(s);
        Log.d("won", "리스트:" + s);
        try {

            JSONObject object = new JSONObject(s);
            mLockListView = true;
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
                listAdapter.notifyDataSetChanged();

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;

                progressBar.setVisibility(View.GONE);
                mLockListView = false;
            }
        }, 1000);
    }




    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }


}
