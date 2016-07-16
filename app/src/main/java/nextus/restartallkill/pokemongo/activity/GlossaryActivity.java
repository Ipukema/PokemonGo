package nextus.restartallkill.pokemongo.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import nextus.restartallkill.pokemongo.R;
import nextus.restartallkill.pokemongo.RecyclerViewAdapter;
import nextus.restartallkill.pokemongo.core.lifecycle.CycleControllerActivity;
import nextus.restartallkill.pokemongo.core.view.DeclareView;

public class GlossaryActivity extends CycleControllerActivity {

    @DeclareView(id= R.id.recyclerView) RecyclerView recyclerView;
    @DeclareView(id=R.id.adView) AdView adView;

    RecyclerViewAdapter recyclerViewAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glossary, true);


        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);


        recyclerViewAdapter = new RecyclerViewAdapter(this, 0);
        recyclerView.setAdapter(recyclerViewAdapter);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
