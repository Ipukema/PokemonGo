package nextus.restartallkill.pokemongo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.util.HashMap;
import java.util.Map;

import nextus.restartallkill.pokemongo.BlogItem;
import nextus.restartallkill.pokemongo.BoardItem;
import nextus.restartallkill.pokemongo.R;
import nextus.restartallkill.pokemongo.core.lifecycle.CycleControllerActivity;
import nextus.restartallkill.pokemongo.core.view.DeclareView;
import nextus.restartallkill.pokemongo.util.CustomRequest;
import nextus.restartallkill.pokemongo.util.MyApplication;

public class MainActivity extends CycleControllerActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    @DeclareView(id= R.id.adView) AdView adView;
    @DeclareView(id=R.id.glossary, click="this") CardView glossary;
    @DeclareView(id=R.id.community, click="this") CardView community;
    @DeclareView(id=R.id.term, click="this") CardView term;
    @DeclareView(id=R.id.developer, click="this") CardView developer;
    @DeclareView(id=R.id.pokedex, click="this") CardView pokedex;
    @DeclareView(id=R.id.inven, click="this") CardView inven;
    @DeclareView(id=R.id.bestLocation, click="this") CardView bestLocation;

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "MainActivity";

    GoogleApiClient mGoogleApiClient;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main,true);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        getData();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
              //  .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                //.addConnectionCallbacks(this)
                //.addOnConnectionFailedListener(this)
               // .addApi(Plus.API)
                //.addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        findViewById(R.id.sign_in_button).setOnClickListener(this);
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
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pokemongo.inven.co.kr/"));
                startActivity(intent);
                break;
            case R.id.term:
                intent = new Intent(this, TermActivity.class);
                startActivity(intent);
                break;
            case R.id.developer:
                intent = new Intent(this, CreateContentsActiity.class);
                startActivity(intent);
                break;
            case R.id.bestLocation:
                intent = new Intent(this, BestLocationActivity.class);
                startActivity(intent);
                break;
            case R.id.sign_in_button:
                //mGoogleApiClient.connect();
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("onActivityResult","true");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("TEST", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "연결 에러 " + connectionResult);

        if (connectionResult.hasResolution()) {

            Log.e(TAG,
                    String.format(
                            "Connection to Play Services Failed, error: %d, reason: %s",
                            connectionResult.getErrorCode(),
                            connectionResult.toString()));
            try {
                //이게 핵심?
                connectionResult.startResolutionForResult(this, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, e.toString(), e);
            }
        }else{
            Toast.makeText(getApplicationContext(), "이미 로그인 중", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "구글 플레이 연결이 되었습니다.");

        if (!mGoogleApiClient.isConnected() || Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) == null) {

            Log.d(TAG, "onConnected 연결 실패");

        } else {
            Log.d(TAG, "onConnected 연결 성공");

            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            if (currentPerson.hasImage()) {

                Log.d(TAG, "이미지 경로는 : " + currentPerson.getImage().getUrl());

               /* Glide.with(MainActivity.this)
                        .load(currentPerson.getImage().getUrl())
                        .into(userphoto);*/

            }
            if (currentPerson.hasDisplayName()) {
                Log.d(TAG,"디스플레이 이름 : "+ currentPerson.getDisplayName());
                Log.d(TAG, "디스플레이 아이디는 : " + currentPerson.getId());
            //    userName.setText(currentPerson.getDisplayName());
            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

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
}
