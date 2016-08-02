package nextus.restartallkill.pokemongo.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nextus.restartallkill.pokemongo.R;
import nextus.restartallkill.pokemongo.core.lifecycle.CycleControllerActivity;
import nextus.restartallkill.pokemongo.core.view.DeclareView;
import nextus.restartallkill.pokemongo.util.MyApplication;

public class BoardItemViewActivity extends CycleControllerActivity {

    @DeclareView(id=R.id.view_title) TextView view_title;
    @DeclareView(id=R.id.view_info) TextView view_info;
    @DeclareView(id=R.id.view_date) TextView view_date;
    @DeclareView(id=R.id.view_img) NetworkImageView view_img;
    @DeclareView(id=R.id.adView) AdView adView;

    int pos = 0;

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
    }

    public void setData()
    {
        view_title.setText(MyApplication.boardItem.getBoardData().get(pos).getBoard_title());
        view_info.setText(MyApplication.boardItem.getBoardData().get(pos).getBoard_info());
        view_img.setImageUrl(MyApplication.boardItem.getBoardData().get(pos).getBoard_img(), MyApplication.getInstance().getImageLoader());

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

}
