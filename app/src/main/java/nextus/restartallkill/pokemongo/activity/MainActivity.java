package nextus.restartallkill.pokemongo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import nextus.restartallkill.pokemongo.R;
import nextus.restartallkill.pokemongo.core.lifecycle.CycleControllerActivity;
import nextus.restartallkill.pokemongo.core.view.DeclareView;
import nextus.restartallkill.pokemongo.util.MyApplication;

public class MainActivity extends CycleControllerActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    @DeclareView(id= R.id.adView) AdView adView;
    @DeclareView(id=R.id.glossary, click="this") CardView glossary;
    @DeclareView(id=R.id.community, click="this") CardView community;
    @DeclareView(id=R.id.term, click="this") CardView term;
    @DeclareView(id=R.id.developer, click="this") CardView developer;
    @DeclareView(id=R.id.pokedex, click="this") CardView pokedex;
    @DeclareView(id=R.id.inven, click="this") CardView inven;
    @DeclareView(id=R.id.bestLocation, click="this") CardView bestLocation;

    private static final String TAG = "MainActivity";

    ProgressDialog mProgressDialog;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main,true);

        mGoogleApiClient = MyApplication.getInstance().getGoogleApiClient(this, this);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            Log.e("new_user","TEST");
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("TEST", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            MyApplication.result = result.getSignInAccount();


            Log.e("UserID", "userID:" + acct.getId() + " userEmail:"+acct.getEmail());
            String idToken = acct.getIdToken();

            Log.e("IDtoken",""+idToken);
            // mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //  updateUI(true);
        } else {
            Log.e("IDtoken","null");
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
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
                intent = new Intent(this, BoardActivity.class);
                startActivity(intent);
                //Toast.makeText(this,"죄송합니다. 준비중입니다. 7/23 업데이트 예정",Toast.LENGTH_SHORT).show();
                break;
            case R.id.inven:
                //intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pokemongo.inven.co.kr/"));
                //startActivity(intent);
                if(mGoogleApiClient.isConnected())
                {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    Toast.makeText(getApplicationContext(),"Log-Out", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.term:
                intent = new Intent(this, TermActivity.class);
                startActivity(intent);
                break;
            case R.id.developer:
                intent = new Intent(this, SendMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.bestLocation:
                intent = new Intent(this, BestLocationActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
