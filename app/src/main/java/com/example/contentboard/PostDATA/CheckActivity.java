package com.example.contentboard.PostDATA;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.contentboard.adapter.CommentAdapter;
import com.example.contentboard.adapter.MyAdapter;

import java.util.HashMap;
import java.util.Map;

public class CheckActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<String> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    public Map<String,String>params =new HashMap<String, String>();
    public void request(String url, Response.Listener<String>listener) {
        //통신 요청
        RequestQueue stringRequest = Volley.newRequestQueue(this);
        url = "https://wavenbreakwater.com/temp/" + url;
        Log.d("won", "url: "+url);
        StringRequest myReq = new StringRequest(Request.Method.POST, url,
                listener,this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return params;
            }
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f)
        );
        stringRequest.add(myReq);
    }



    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Log.d("won", "onErrorResponse: "+volleyError);
    }

    @Override
    public void onResponse(String s) {

    }
}
