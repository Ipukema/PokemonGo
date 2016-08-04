package nextus.restartallkill.pokemongo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import nextus.restartallkill.pokemongo.R;
import nextus.restartallkill.pokemongo.core.lifecycle.CycleControllerActivity;
import nextus.restartallkill.pokemongo.core.view.DeclareView;
import nextus.restartallkill.pokemongo.data.BoardItem;
import nextus.restartallkill.pokemongo.util.CustomRequest;
import nextus.restartallkill.pokemongo.util.MyApplication;

public class BoardItemViewActivity extends CycleControllerActivity implements View.OnClickListener {

    @DeclareView(id=R.id.view_title) TextView view_title;
    @DeclareView(id=R.id.view_info) TextView view_info;
    @DeclareView(id=R.id.view_date) TextView view_date;
    @DeclareView(id=R.id.view_count) TextView view_count;
    @DeclareView(id=R.id.like_count) TextView like_count;
    @DeclareView(id=R.id.comment_count) TextView comment_count;
    @DeclareView(id=R.id.view_img) NetworkImageView view_img;
    @DeclareView(id=R.id.adView) AdView adView;
    @DeclareView(id=R.id.like_button, click = "this") LinearLayout like_button;
    @DeclareView(id=R.id.create_comments, click = "this") LinearLayout create_comments;
    @DeclareView(id=R.id.like_img) AwesomeTextView like_img;
    @DeclareView(id=R.id.like_text) TextView like_text;

    int pos = 0;
    boolean alreadyLike = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_view_layout, true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pos = getIntent().getIntExtra("position",0);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        setData();
        updateViewCount();
        getLikeData();
    }

    public void updateViewCount()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("id", ""+MyApplication.boardItem.getBoardData().get(pos).getBoard_id());

        String url = "http://125.209.193.163/pokemongo/updateViewCount.jsp";

        final CustomRequest<BoardItem> jsonObjReq = new CustomRequest<BoardItem>(Request.Method.POST, url, param,
                BoardItem.class, //Not null.
                new Response.Listener<BoardItem>() {
                    @Override
                    public void onResponse(BoardItem response) {

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });
        MyApplication.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void setData()
    {
        view_title.setText(MyApplication.boardItem.getBoardData().get(pos).getBoard_title());
        view_info.setText(MyApplication.boardItem.getBoardData().get(pos).getBoard_info());
        view_img.setImageUrl(MyApplication.boardItem.getBoardData().get(pos).getBoard_img(), MyApplication.getInstance().getImageLoader());
        view_count.setText(""+MyApplication.boardItem.getBoardData().get(pos).getView_count());
        like_count.setText(""+MyApplication.boardItem.getBoardData().get(pos).getLike_count());
        comment_count.setText(""+MyApplication.boardItem.getBoardData().get(pos).getComment_count());

        String date = MyApplication.boardItem.getBoardData().get(pos).getDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.KOREA);

        Date current = new Date(System.currentTimeMillis());

        try{
            Date d = format.parse(date) ;
            long time_long = current.getTime() - d.getTime();
            int time = (int)time_long/1000;

            Log.e("Time", ""+time);
            if( time < 60 )
            {
                view_date.setText("방금");
            }
            else if( time >=60 && time < 3600 )
            {
                time = time/60;
                view_date.setText(""+time+"분 전");
            }
            else if( time >=3600 && time < 86400 )
            {
                time = time/3600;
                view_date.setText(""+time+"시간 전");
            }
            else
            {
                view_date.setText(date);
            }
        }catch (Exception e)
        {
           e.printStackTrace();
        }

    }

    public void increasingLike()
    {
        if( alreadyLike ) // 이미 좋아요를 누른 상태이면
        {

        }
        else  // 좋아요를 누르지 않은 상태 -> 색상 변경 및 db값 변경
        {
            like_img.setTextColor(Color.BLUE);
            like_text.setTextColor(Color.BLUE);
            sendLikeData();
        }

    }

    public void getLikeData()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", MyApplication.result.getId());
        param.put("board_id", ""+MyApplication.boardItem.getBoardData().get(pos).getBoard_id());

        JSONObject jsonObject = new JSONObject(param);
        String url = "http://125.209.193.163/pokemongo/findLike.jsp";

        final CustomRequest<JSONObject> jsonObjReq = new CustomRequest<>(Request.Method.POST, url, param,
                JSONObject.class, //Not null.
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJSON(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });


        MyApplication.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void setLike(int result)
    {
        if(result == 1)
        {
            like_img.setTextColor(Color.BLUE);
            like_text.setTextColor(Color.BLUE);
            alreadyLike = true;
        }
        else alreadyLike = false;
    }

    private String parseJSON(JSONObject json) {
        String mText = "";
        try {
            //JSONObject value = json.getJSONObject("result_array");
            //Log.e("Value", value.getString("result"));
            JSONArray items = json.getJSONArray("result_array");
            JSONObject item = items.getJSONObject(0);
            mText = item.getString("result");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mText;
    }

    public void sendLikeData()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", MyApplication.result.getId());
        param.put("board_id", ""+MyApplication.boardItem.getBoardData().get(pos).getBoard_id());

        String url = "http://125.209.193.163/pokemongo/createLike.jsp";

        final CustomRequest<BoardItem> jsonObjReq = new CustomRequest<BoardItem>(Request.Method.POST, url, param,
                BoardItem.class, //Not null.
                new Response.Listener<BoardItem>() {
                    @Override
                    public void onResponse(BoardItem response) {

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });
        MyApplication.getInstance().addToRequestQueue(jsonObjReq);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.like_button:
                increasingLike();
                break;
        }
    }
}
