package com.example.contentboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {
    Button getBtn,logBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getBtn=findViewById(R.id.getID_btn);
        logBtn=findViewById(R.id.getLog_btn);
        getBtn.setOnClickListener(this);
        logBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.getID_btn){
            startActivity(new Intent(IntroActivity.this,GetID.class));
            finish();
        }
        if (v.getId()==R.id.getLog_btn){
            startActivity(new Intent(IntroActivity.this,LogIn.class));
            finish();
        }
    }

}
