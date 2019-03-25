package com.example.contentboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contentboard.PostDATA.CheckActivity;
import com.example.contentboard.datas.Token;


import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LogIn extends CheckActivity implements View.OnClickListener {
        EditText inputID,inputPW;
        Button loginbtn;
        CheckBox autoLog;

        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        inputID=findViewById(R.id.Ed_id);
        inputPW=findViewById(R.id.Ed_pw);
        loginbtn=findViewById(R.id.btn_input);
        loginbtn.setOnClickListener(this);
        autoLog=findViewById(R.id.check_autoLog);

        sharedPreferences=getSharedPreferences("setting",0);
        editor=sharedPreferences.edit();
        if (sharedPreferences.getBoolean("autoLog",false)){
            inputID.setText(sharedPreferences.getString("Id",""));
            inputPW.setText(sharedPreferences.getString("Pwd",""));
            autoLog.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
            if (autoLog.isChecked()){
                Toast.makeText(this,"로그인",Toast.LENGTH_SHORT).show();
                String id = inputID.getText().toString();
                String pass=inputPW.getText().toString();
                editor.putString("Id",id);
                editor.putString("Pwd",pass);
                editor.putBoolean("autoLog",true);
                editor.commit();

            }else {
                editor.clear();
                editor.commit();

            }
            if (v.getId()==R.id.btn_input){
                String id = inputID.getText().toString();
                String pass=inputPW.getText().toString();
                if (id.equals("")||pass.equals("")) {
                    Toast.makeText(getApplicationContext(), "빈칸 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    params.clear();
                    params.put("name", inputID.getText().toString());
                    params.put("pass",passwordSHA(inputPW.getText().toString()));
                    request("test_login.php", this);

                }
            }
    }
//respons 시작JSON파싱
    @Override
    public void onResponse(String s) {
        super.onResponse(s);
        try {
            JSONObject obj=new JSONObject(s);
            String name=obj.getString("result");
            String msg=obj.getString("msg");
            if (name.equals("NK")) {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
                alt_bld.setMessage(msg).setCancelable(
                        false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                AlertDialog alert = alt_bld.create();
                alert.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject obj=new JSONObject(s);
            String result=obj.getString("result");
            Token.token=obj.getString("token");

            if (result.equals("OK")){
                startActivity(new Intent(LogIn.this,MainActivityBeta.class));
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("won", "id:" + s);
    }
//respons 끝


    /*sha-256*/
    public String passwordSHA(String str) {
        String SHA = "";
        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            SHA = sb.toString();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

            SHA = null;

        }
        return SHA;
    }
}
