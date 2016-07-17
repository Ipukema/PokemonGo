package nextus.restartallkill.pokemongo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import nextus.restartallkill.pokemongo.BlogRecyclerAdapter;
import nextus.restartallkill.pokemongo.R;
import nextus.restartallkill.pokemongo.core.lifecycle.CycleControllerActivity;
import nextus.restartallkill.pokemongo.core.view.DeclareView;
import nextus.restartallkill.pokemongo.util.MyApplication;

public class BestLocationActivity extends CycleControllerActivity {

    @DeclareView(id=R.id.bestLocation_recyclerview) RecyclerView recyclerView;
    @DeclareView(id= R.id.adView) AdView adView;

    StaggeredGridLayoutManager staggeredGridLayoutManager;
    BlogRecyclerAdapter recyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_location, true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        recyclerViewAdapter = new BlogRecyclerAdapter(this, 1);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClickListener(onItemClickListener);

    }

    BlogRecyclerAdapter.OnItemClickListener onItemClickListener = new BlogRecyclerAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MyApplication.getInstance().blogItem.getBlogData().get(position).getBl_url()));
            startActivity(intent);
            //  intent.putExtra("position", MySingleton.current_group_list.get(position));
            //startActivity(intent);
            Log.e("Position", ""+MyApplication.getInstance().blogItem.getBlogData().get(position).getBl_url());

        }
    };

}
