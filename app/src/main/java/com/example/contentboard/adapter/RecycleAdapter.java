package com.example.contentboard.adapter;

import android.app.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.contentboard.DetailActivity;
import com.example.contentboard.R;
import com.example.contentboard.datas.contentData;

import java.util.ArrayList;
/*리사이클러뷰 어뎁터 클라스*/
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder>{
    public Activity activity;
    public ArrayList<contentData> dataArr;
    public int pos;
    public RecycleAdapter (Activity activity,ArrayList<contentData>dataArr,int pos){
        this.pos=pos;
        this.activity=activity;
        this.dataArr=dataArr;
    }
    @Override
    public int getItemCount() {
        return dataArr.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView titleTv;
        public TextView writerTv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv=itemView.findViewById(R.id.title_tv);
            writerTv=itemView.findViewById(R.id.witer_tv);

        }


    }
    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,viewGroup,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);

        return myViewHolder;
    }
    String idid;
    @Override
    public void onBindViewHolder(MyViewHolder myviewHolder, final int position) {
        pos=position;
        myviewHolder.titleTv.setText(dataArr.get(position).title);
        myviewHolder.writerTv.setText(dataArr.get(position).content);
        myviewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idid=dataArr.get(position).idx;
                String title=dataArr.get(position).title;
                String content=dataArr.get(position).content;
                String writer=dataArr.get(position).token;
                Intent intent=new Intent(v.getContext(),DetailActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("content",content);
                intent.putExtra("idx",idid);
                intent.putExtra("writer",writer);
                v.getContext().startActivity(intent);

            }
        });
    }
}
