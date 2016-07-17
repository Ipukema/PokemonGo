package nextus.restartallkill.pokemongo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.HashMap;
import java.util.Map;

import nextus.restartallkill.pokemongo.BlogItem;
import nextus.restartallkill.pokemongo.BoardItem;
import nextus.restartallkill.pokemongo.R;
import nextus.restartallkill.pokemongo.core.lifecycle.CycleControllerActivity;
import nextus.restartallkill.pokemongo.core.view.DeclareView;
import nextus.restartallkill.pokemongo.util.CustomRequest;
import nextus.restartallkill.pokemongo.util.MyApplication;

public class MainActivity extends CycleControllerActivity implements View.OnClickListener{

    @DeclareView(id= R.id.adView) AdView adView;
    @DeclareView(id=R.id.glossary, click="this") CardView glossary;
    @DeclareView(id=R.id.community, click="this") CardView community;
    @DeclareView(id=R.id.term, click="this") CardView term;
    @DeclareView(id=R.id.developer, click="this") CardView developer;
    @DeclareView(id=R.id.pokedex, click="this") CardView pokedex;
    @DeclareView(id=R.id.inven, click="this") CardView inven;
    @DeclareView(id=R.id.bestLocation, click="this") CardView bestLocation;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main,true);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        dialog = ProgressDialog.show(this, "","Loading..Wait.." , true);
        dialog.show();

        getData();
        getBlogData();
        /*
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //your code here
                //  getData();


            }
        }, 6000);
*/

    }

    public void getData()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("id", "sef");

        String url = "http://125.209.193.163/pokemongo/getBoardData.jsp";

        final CustomRequest<BoardItem> jsonObjReq = new CustomRequest<BoardItem>(Request.Method.POST, url, param,
                BoardItem.class, //Not null.
                new Response.Listener<BoardItem>() {
                    @Override
                    public void onResponse(BoardItem response) {
                        try {
                            MyApplication.getInstance().boardItem = response;
                            Log.e("Test:",response.getBoardData().get(0).getBoard_title());
                            //MySingletonOld.dinosaursBasicData.getData().addAll(response.getData());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    public void getBlogData()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("id", "sef");

        String url = "http://125.209.193.163/pokemongo/getBestLocation.jsp";

        final CustomRequest<BlogItem> jsonObjReq = new CustomRequest<>(Request.Method.POST, url, param,
                BlogItem.class, //Not null.
                new Response.Listener<BlogItem>() {
                    @Override
                    public void onResponse(BlogItem response) {
                        try {
                            MyApplication.getInstance().blogItem = response;
                            Log.e("Test:",response.getBlogData().get(0).getBl_img());
                            dialog.dismiss();

                            //MySingletonOld.dinosaursBasicData.getData().addAll(response.getData());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
        Intent intent;
        switch (view.getId())
        {
            case R.id.pokedex:
                Toast.makeText(this,"죄송합니다. 준비중입니다.",Toast.LENGTH_SHORT).show();
                break;
            case R.id.glossary:
                intent = new Intent(this, GlossaryActivity.class);
                startActivity(intent);
            break;
            case R.id.community:
                //intent = new Intent(this, BoardActivity.class);
                //startActivity(intent);
                Toast.makeText(this,"죄송합니다. 준비중입니다. 7/23 업데이트 예정",Toast.LENGTH_SHORT).show();
                break;
            case R.id.inven:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pokemongo.inven.co.kr/"));
                startActivity(intent);
                break;
            case R.id.term:
                intent = new Intent(this, TermActivity.class);
                startActivity(intent);
                break;
            case R.id.developer:
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;

            case R.id.bestLocation:
                intent = new Intent(this, BestLocationActivity.class);
                startActivity(intent);
                break;
        }
    }
}
