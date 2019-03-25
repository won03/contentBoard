package com.example.contentboard.adapter;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.contentboard.R;
import com.example.contentboard.datas.contentData;


import java.util.ArrayList;
import java.util.List;
/*사용하지안음*/
public class MyAdapter extends ArrayAdapter {

    public class RowDataViewHolder {
        public TextView titleTv;
        public TextView writerTv;

    }
    LayoutInflater lnf;
    Context context;
    ArrayList<contentData> arr;

    public MyAdapter(Context context, ArrayList<contentData>arr) {
        super(context, R.layout.list_item,arr);
        this.context=context;
        this.arr=arr;

        lnf = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    String idid;
    public View getView(final int position, View convertView, ViewGroup parent) {
        RowDataViewHolder viewHolder;
        if (convertView == null) {
            convertView = lnf.inflate(R.layout.list_item, parent, false);
            viewHolder = new RowDataViewHolder();
            viewHolder.titleTv = (TextView) convertView.findViewById(R.id.title_tv);
            viewHolder.writerTv=(TextView)convertView.findViewById(R.id.witer_tv);
            convertView.setTag(viewHolder);
            idid = arr.get(position).idx;
        } else {
            viewHolder = (RowDataViewHolder) convertView.getTag();
        }

        viewHolder.titleTv.setText("제목:"+arr.get(position).title);
        viewHolder.writerTv.setText("작성자:"+arr.get(position).token);
        return convertView;
    }


}
