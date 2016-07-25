package nextus.restartallkill.pokemongo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.HashMap;
import java.util.Map;

import nextus.restartallkill.pokemongo.data.BlogItem;
import nextus.restartallkill.pokemongo.R;
import nextus.restartallkill.pokemongo.adapter.BlogAdapter;
import nextus.restartallkill.pokemongo.core.lifecycle.CycleControllerActivity;
import nextus.restartallkill.pokemongo.core.view.DeclareView;
import nextus.restartallkill.pokemongo.util.CustomRequest;
import nextus.restartallkill.pokemongo.adapter.GenericRecylerAdapter;
import nextus.restartallkill.pokemongo.util.MyApplication;

public class BestLocationActivity extends CycleControllerActivity {

    @DeclareView(id=R.id.bestLocation_recyclerview) RecyclerView recyclerView;
    @DeclareView(id= R.id.adView) AdView adView;

    StaggeredGridLayoutManager staggeredGridLayoutManager;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_location, true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        dialog = ProgressDialog.show(this, "","Loading..Wait.." , true);
        dialog.setIndeterminate(true);
        dialog.show();
        getBlogData();

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
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
                            dialog.dismiss();
                            Log.e("Test:",response.getBlogData().get(0).getBl_img());
                            BlogAdapter adapter = new BlogAdapter(getApplicationContext(),onItemClickListener);
                            adapter.setList(response.getBlogData());
                            recyclerView.setAdapter(adapter);
                            recyclerView.notifyAll();
                            //recyclerViewAdapter.notifyDataSetChanged();

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

    GenericRecylerAdapter.OnViewHolderClick onItemClickListener = new GenericRecylerAdapter.OnViewHolderClick(){
        @Override
        public void onClick(View view, int position) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MyApplication.getInstance().blogItem.getBlogData().get(position).getBl_url()));
            startActivity(intent);
        }
    };
}
