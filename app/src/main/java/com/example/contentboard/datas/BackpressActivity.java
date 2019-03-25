package com.example.contentboard.datas;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.contentboard.DetailActivity;
import com.example.contentboard.MainActivity;
//뒤로가기 종료 클래스
public class BackpressActivity {
    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;

    public BackpressActivity(Activity context) {
        this.activity = context;
    }
    public void getDialog(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(activity);
        alt_bld.setMessage("끝내실거죠?").setCancelable(
                false).setPositiveButton("응",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.finish();
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
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();

            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            getDialog();

            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }


}
