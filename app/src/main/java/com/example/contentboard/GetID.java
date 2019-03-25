package com.example.contentboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.contentboard.PostDATA.CheckActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class GetID extends CheckActivity implements View.OnClickListener {
    CheckBox agreeCB;
    EditText idET, pwdET;
    Button completeBtn, chekingBtn;
    boolean ischecked = false;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_id);

        idET = findViewById(R.id.id_ET);
        pwdET = findViewById(R.id.pwd_ET);
        completeBtn = findViewById(R.id.btn_getlog);
        chekingBtn = findViewById(R.id.checkBtn);
        agreeCB = findViewById(R.id.check_bx);
        completeBtn.setOnClickListener(this);
        requestQueue=Volley.newRequestQueue(this);


        chekingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkId();

            }
        });


    }
    //중복체크 클릭이벤트
    public void checkId() {
        String id = idET.getText().toString();

        if (id.equals("")) {
            Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
        } else {
            //중복체크 메소드
            getcheck();

        }
    }

/*--------------------------SHA키-----------------------------------------------------------*/
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

    //회원가입 서버로 던지는 메소드
    public void getId() {
        params.clear();
        params.put("name", idET.getText().toString());
        params.put("pass", passwordSHA(pwdET.getText().toString()));
        request("test_join.php", this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_getlog) {

            String id = idET.getText().toString();
            String pass = pwdET.getText().toString();

            //빈칸이었을때
            if (id.equals("") || pass.equals("")) {
                Toast.makeText(getApplicationContext(), "빈칸 입력하세요", Toast.LENGTH_SHORT).show();
            //중복확이잇되었는지 확인하기
            } else if (!ischecked) {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
                alt_bld.setMessage("중복확인부터").setCancelable(
                        false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = alt_bld.create();
                alert.show();
                return;
                //모든게 다 충족하면 getID()로넘어감
            } else if (agreeCB.isChecked() == true) {
                getId();

                //체크확인
            } else if (agreeCB.isChecked() == false) {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
                alt_bld.setMessage("정보수입에 동의해주세요").setCancelable(
                        false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = alt_bld.create();
                alert.show();
            }
        }
    }

    @Override
    public void onResponse(String s) {
        super.onResponse(s);
        Log.d("won", "get id-->" + s);
        try {
            JSONObject object = new JSONObject(s);
            String name = object.getString("result");
            String success="OK";
            String fail="NK";
            if (name.equals(success)) {
                Toast.makeText(this,"회원가입성공",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(GetID.this, IntroActivity.class));
                finish();

            } else if (name.equals("NK")) {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
                alt_bld.setMessage("오류!").setCancelable(
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

    }//respons 끝-----------------------------------------------------------------------------------------------]
/*-------------------------------------------------------------중복학인 respons------------------------------------------------------*/
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.d("won", "onErrorResponse: " + volleyError);
        }
    };
    private void getcheck() {
        final String id = idET.getText().toString();
        String URL = "https://wavenbreakwater.com/temp/test_chk_member.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, sucess, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.clear();
                params.put("name", id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        requestQueue.add(stringRequest);
    }

    Response.Listener<String> sucess = new Response.Listener<String>() {

        public void onResponse(String s) {
            try {

                JSONObject object = new JSONObject(s);
                String name = object.getString("result");
                String msg = object.getString("msg");
                if (name.equals("OK")) {
                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(GetID.this);
                    alt_bld.setMessage(msg).setCancelable(
                            false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ischecked = true;
                                }
                            });
                    AlertDialog alert = alt_bld.create();
                    alert.show();


                } else if (name.equals("NK")) {
                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(GetID.this);
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
            Log.d("won", "id:" + s);
        }

    };




}