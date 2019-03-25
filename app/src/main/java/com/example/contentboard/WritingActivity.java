package com.example.contentboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.contentboard.PostDATA.CheckActivity;
import com.example.contentboard.datas.Token;
import com.example.contentboard.datas.contentData;


import java.util.ArrayList;
/*
글작성하기
* */
public class WritingActivity extends CheckActivity implements View.OnClickListener {
    Button compeleteBtn;
    EditText titleEt,contentEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        compeleteBtn=findViewById(R.id.compelete_btn);
        titleEt=findViewById(R.id.title_et);
        contentEt=findViewById(R.id.content_et);
        compeleteBtn.setOnClickListener(this);

    }
    public void getWriting(){
        String title=titleEt.getText().toString();
        String content=contentEt.getText().toString();
        params.clear();
        params.put("token",Token.token);
        params.put("title",title);
        params.put("content",content);
        request("test_board_insert.php",this);

    }

    @Override
    public void onClick(View v) {
        String title=titleEt.getText().toString();
        String content=contentEt.getText().toString();
        if (v.getId()==R.id.compelete_btn){

            if (title.equals("")||content.equals("")){
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
                alt_bld.setMessage("빈칸은 모두체워야합니다").setCancelable(
                        false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = alt_bld.create();
                alert.show();
                return;
            }else {
                getWriting();
            }
        }
    }


    @Override
    public void onResponse(String s) {
        super.onResponse(s);
        Log.d("won", "titlevent:" + s);

        startActivity(new Intent(WritingActivity.this,MainActivityBeta.class));
        finish();

    }
}
