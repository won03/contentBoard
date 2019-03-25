package com.example.contentboard;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.contentboard.PostDATA.CheckActivity;
import com.example.contentboard.datas.Token;

import org.json.JSONException;
import org.json.JSONObject;
/*수정하기*/
public class UpdateActivity extends CheckActivity implements View.OnClickListener {
        EditText updateTitle,updateContent;
        Button updateBtn,cancelBtn;
        String posId;
        Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        updateTitle=findViewById(R.id.update_title_et);
        updateContent=findViewById(R.id.update_content_et);
        updateBtn=findViewById(R.id.update_comp_btn);
        cancelBtn=findViewById(R.id.update_cancel_btn);
        cancelBtn.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        intent=getIntent();
        updateTitle.setText(intent.getStringExtra("upTitle"));
        updateContent.setText(intent.getStringExtra("upContent"));
        posId = intent.getStringExtra("ids");
        }

    @Override
    public void onClick(View v) {
            if (v.getId()==R.id.update_comp_btn){
                getedit();
            }
            if (v.getId()==R.id.update_cancel_btn){

                intent=new Intent(UpdateActivity.this,MainActivityBeta.class);
                    startActivity(intent);
                return;
            }

    }
        public void getedit(){
            params.clear();
            params.put("token", Token.token);
            params.put("idx", DetailActivity.posId);
            params.put("title", updateTitle.getText().toString());
            params.put("content", updateContent.getText().toString());
            Log.d("won", "idx:" + posId);
            request("test_board_update.php", this);
        }

    @Override
    public void onResponse(String s) {
        super.onResponse(s);
        try {
            JSONObject object=new JSONObject(s);
            String result=object.getString("result");
            String sucess="OK";
            String fail="NK";
            if (result.equals(sucess)){
                Toast.makeText(this,"수정완료",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateActivity.this,MainActivityBeta.class));
                finish();
            }
            if (result.equals(fail)){
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
                alt_bld.setMessage("수정실패").setCancelable(
                        false).setPositiveButton("응",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                return;

                            }
                        });
                AlertDialog alert = alt_bld.create();
                alert.show();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
