package com.example.contentboard;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.contentboard.PostDATA.CheckActivity;
import com.example.contentboard.adapter.CommentAdapter;
import com.example.contentboard.datas.Token;
import com.example.contentboard.datas.contentData;
import com.example.contentboard.datas.userData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DetailActivity extends CheckActivity implements View.OnClickListener {
    TextView titleTv, contentTv, writerTv;
    public EditText  commentEt;
   public Button  admitBtn,updateCombtn;
    Intent intent;
    ImageView backImg;
    ListView commentList;
   public static String posId;
    ArrayList<userData> arrUser;
    CommentAdapter commentAdapter;
    android.support.v7.app.ActionBar actionBar;
    String upTitle;
    String upContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        actionBar=getSupportActionBar();
        backImg = findViewById(R.id.back_press);
        titleTv = findViewById(R.id.title_data);
        writerTv = findViewById(R.id.writer_tv);
        contentTv = findViewById(R.id.content_data);
        updateCombtn=findViewById(R.id.update_admit_btn);
        commentEt = findViewById(R.id.comment_et);
        admitBtn = findViewById(R.id.admit_btn);
        commentList = findViewById(R.id.comment_list);
        intent = getIntent();
        titleTv.setText(intent.getStringExtra("title"));
        contentTv.setText(intent.getStringExtra("content"));
        writerTv.setText(intent.getStringExtra("writer"));
        posId = intent.getStringExtra("idx");
        arrUser = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, arrUser,posId,admitBtn,commentEt,updateCombtn);
        commentList.setAdapter(commentAdapter);
        upTitle=titleTv.getText().toString();
        upContent=contentTv.getText().toString();

        admitBtn.setOnClickListener(this);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.back_press) {
                    onBackPressed();
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.add){
            Intent intent2=new Intent(DetailActivity.this,UpdateActivity.class);
            intent2.putExtra("upTitle",upTitle);
            intent2.putExtra("upContent",upContent);
            intent2.putExtra("ids",posId);
            startActivityForResult(intent2,10);
        }
        if (item.getItemId()==R.id.delete){
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
            alt_bld.setMessage("정말삭제?").setCancelable(
                    false).setPositiveButton("응",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getDeletServer();
                            startActivity(new Intent(DetailActivity.this, MainActivityBeta.class));
                        }
                    }).setNegativeButton("아니",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            return;
                        }
                    });
            AlertDialog alert = alt_bld.create();
            alert.show();

        }

       return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==10&&requestCode==RESULT_OK){
            Intent intent2=getIntent();
            titleTv.setText(intent2.getStringExtra("t"));
            contentTv.setText(intent2.getStringExtra("c"));

        }
    }
    //
    public void getServer() {

        params.clear();
        params.put("token", Token.token);
        params.put("idx",posId );
        request("test_reply_list.php", this);
        Log.d("won", "list id-->" + posId);
    }
    //상제내용을 삭제하는메소
    public void getDeletServer() {
        params.clear();
        params.put("idx", posId);
        params.put("token", Token.token);
        request("test_board_delete.php", this);
        Log.d("won", "delete id-->" + posId);

    }
    //댓글 입력 하는 메소드
    public void getComment() {
        params.clear();
        arrUser.clear();
        params.put("idx", posId);
        params.put("token", Token.token);
        params.put("content", commentEt.getText().toString());
        request("test_reply_insert.php", this);
        Log.d("won", "comment:" + commentEt.getText().toString());

    }






    @Override
    public void onClick(View v) {

//        if (v.getId() == R.id.delete) {
//            AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
//            alt_bld.setMessage("정말삭제?").setCancelable(
//                    false).setPositiveButton("응",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            getDeletServer();
//                            startActivity(new Intent(DetailActivity.this, MainActivityBeta.class));
//                        }
//                    }).setNegativeButton("아니",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            return;
//                        }
//                    });
//            AlertDialog alert = alt_bld.create();
//            alert.show();
//
//        }

        if (v.getId() == R.id.admit_btn) {

            getComment();
            commentEt.setText(null);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(commentEt.getWindowToken(), 0);

        }
//        if (v.getId() == R.id.edit_btn) {
//            editOkBtn.setVisibility(View.VISIBLE);
//            editBtn.setVisibility(View.INVISIBLE);
//            titleEt.setVisibility(View.VISIBLE);
//            contentEt.setVisibility(View.VISIBLE);
//            titleTv.setVisibility(View.INVISIBLE);
//            contentTv.setVisibility(View.INVISIBLE);
//        } else if (v.getId() == R.id.edit_ok) {
//            getedit();
//            editOkBtn.setVisibility(View.INVISIBLE);
//            titleEt.setVisibility(View.INVISIBLE);
//            contentEt.setVisibility(View.INVISIBLE);
//
//
//
//        }

    }
//        public void getedit(){
//            params.clear();
//            params.put("token", Token.token);
//            params.put("idx", posId);
//            params.put("title", titleEt.getText().toString());
//            params.put("content", contentEt.getText().toString());
//            Log.d("won", "idx:" + posId);
//            request("test_board_update.php", this);
//        }
    @Override
    protected void onResume() {
        super.onResume();
        getServer();

    }
    //JSON파싱 댓글 어레이 리스트받아오기
    @Override
    public void onResponse(String s) {
        super.onResponse(s);
        Log.d("won", "리스폰스:" + s);
        try {
            JSONObject object = new JSONObject(s);
            JSONArray jsonArray = object.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                String content = temp.getString("content");
                String writer = temp.getString("writer");
                String id = temp.getString("idx");
                userData useData = new userData(content, writer, id);
                arrUser.add(useData);
                Log.d("won", "list:" + id);

            }
            commentAdapter.notifyDataSetChanged();

            String result = object.getString("result");
            String msg = object.getString("msg");
            String fail="NK";
            if (result.equals(fail)) {

                AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
                alt_bld.setMessage("다른 사용자입니다").setCancelable(
                        false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                AlertDialog alert = alt_bld.create();
                alert.show();

            } else if (result.equals("OK")) {

                finish();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
