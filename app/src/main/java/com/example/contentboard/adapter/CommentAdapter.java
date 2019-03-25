package com.example.contentboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.contentboard.DetailActivity;
import com.example.contentboard.MainActivity;
import com.example.contentboard.PostDATA.CheckActivity;
import com.example.contentboard.R;
import com.example.contentboard.datas.Token;
import com.example.contentboard.datas.contentData;
import com.example.contentboard.datas.userData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*댓글리스트뷰*/
public class CommentAdapter extends ArrayAdapter    {
    RequestQueue requestQueue;



    public class itemDataViewHolder {
        public TextView titleTv;
        public TextView writerTv;
        public Button deleteComment;
        public Button editComment, inputBtn;


    }

    LayoutInflater inflate;
    Context context;
    List<userData> arruser;
    String idx;
    Button admitBtn;
    EditText commentEt;
    Button updateCombtn;

    public CommentAdapter(Context context, List<userData> arruser, String idx, Button admitBtn, EditText commentEt, Button updateCombtn) {
        super(context, R.layout.comment_item, arruser);
        requestQueue = Volley.newRequestQueue(getContext());
        this.arruser = arruser;
        this.context = context;
        this.idx = idx;
        this.admitBtn = admitBtn;
        this.commentEt = commentEt;
        this.updateCombtn = updateCombtn;

        inflate = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return arruser.size();
    }

    @Override
    public Object getItem(int position) {
        return arruser.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }
    String idid;
    public View getView(final int position, View convertView, ViewGroup parent) {
        final itemDataViewHolder viewHolder;


        if (convertView == null) {
            convertView = inflate.inflate(R.layout.comment_item, parent, false);

            viewHolder = new itemDataViewHolder();
            viewHolder.titleTv = (TextView) convertView.findViewById(R.id.text_comment);
            viewHolder.writerTv = (TextView) convertView.findViewById(R.id.text_writer);
            viewHolder.deleteComment = (Button) convertView.findViewById(R.id.delete_coment);
            viewHolder.inputBtn = (Button) convertView.findViewById(R.id.admit_btn);
            viewHolder.editComment = (Button) convertView.findViewById(R.id.edit_comentBtn);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (itemDataViewHolder) convertView.getTag();
        }
        viewHolder.titleTv.setText(arruser.get(position).comment);
        viewHolder.writerTv.setText(arruser.get(position).writer);
        viewHolder.deleteComment.setVisibility(View.VISIBLE);
         idid = arruser.get(position)._id;

//----------------------------버튼클릭시 댓글삭제 --------------------------------
        viewHolder.deleteComment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //------------------------------------------삭제URL-------------------------------------
                final String URL = "https://wavenbreakwater.com/temp/test_reply_delete.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, sucess, errorListener) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.clear();
                        arruser.clear();
                        params.put("board_idx", idx);
                        params.put("idx", idid);
                        params.put("token", Token.token);
                        Log.d("won", "idx: " + idid);
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
                requestQueue.add(stringRequest);


            }

        });
        //setTag 로 위치를 받아옴
        viewHolder.editComment.setTag(position);
        //----------------------------------------------------버튼 클릭지 수정하는 메소드-------------------------
        viewHolder.editComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.edit_comentBtn) {
                    int pos = Integer.parseInt(String.valueOf(v.getTag()));
                    admitBtn.setVisibility(View.INVISIBLE);
                    updateCombtn.setVisibility(View.VISIBLE);
                    String update = arruser.get(pos).comment;
                    commentEt.setText(update);
                    Log.d("won", "업뎃클릭:" + pos);
                    updateCombtn.setTag(position);
                }
            }

        });

        updateCombtn.setTag(position);
        updateCombtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int getId=Integer.parseInt(String.valueOf(v.getTag()));
                final String id_comment=arruser.get(getId)._id;
                    admitBtn.setVisibility(View.VISIBLE);
                    updateCombtn.setVisibility(View.INVISIBLE);
                    //수정 URL-----------------
                    final String URL = "https://wavenbreakwater.com/temp/test_reply_update.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, sucess, errorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.clear();
                            arruser.clear();
                            params.put("idx",id_comment );
                            Log.d("won", "id" +id_comment);
                            params.put("content", commentEt.getText().toString());
                            Log.d("won", "comment Buttopn:" + commentEt.getText().toString());
                            params.put("board_idx", idx);
                            params.put("token", Token.token);
                            Log.d("won", "idx: " + idx);

                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
                    requestQueue.add(stringRequest);




            }

        });

        return convertView;
    }

    /*-----------------------------서버에서 받아옴----------------------------------------*/

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.d("won", "에러뜸: " + volleyError);
        }
    };

    Response.Listener<String> sucess = new Response.Listener<String>() {


        @Override
        public void onResponse(String respon) {
            Log.d("won", "comment Buttopn:" + respon);
            try {


                Log.d("won", "respon: " + respon);
                JSONObject obj = new JSONObject(respon);
                String result=obj.getString("result");
                JSONArray jsonArray = obj.getJSONArray("list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject temp = jsonArray.getJSONObject(i);
                    String content = temp.getString("content");
                    String writer = temp.getString("writer");
                    String id = temp.getString("idx");
                    userData useData = new userData(content, writer, id);
                    arruser.add(useData);
                    Log.d("won", "list:" + id);

                }
                if (result.equals("OK")){
                    commentEt.getText().clear();
                }

                CommentAdapter.this.notifyDataSetChanged();


            } catch (JSONException e1) {
                e1.printStackTrace();
            }



        }
    };


}
