package nextus.restartallkill.pokemongo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import nextus.restartallkill.pokemongo.core.lifecycle.CycleControllerActivity;
import nextus.restartallkill.pokemongo.core.view.DeclareView;

public class MainActivity extends CycleControllerActivity implements View.OnClickListener{

    @DeclareView(id=R.id.adView) AdView adView;
    @DeclareView(id=R.id.glossary, click="this") CardView glossary;
    @DeclareView(id=R.id.community, click="this") CardView community;
    @DeclareView(id=R.id.term, click="this") CardView term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main,true);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId())
        {
            case R.id.glossary:
                intent = new Intent(this, GlossaryActivity.class);
                startActivity(intent);
            break;

            case R.id.community:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pokemongo.inven.co.kr/"));
                startActivity(intent);
                break;

            case R.id.term:
                intent = new Intent(this, TermActivity.class);
                startActivity(intent);
                break;
        }
    }
}
