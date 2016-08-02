package nextus.restartallkill.pokemongo.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
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
import java.util.Hashtable;
import java.util.Map;

import nextus.restartallkill.pokemongo.R;
import nextus.restartallkill.pokemongo.TempBoardAdapter;
import nextus.restartallkill.pokemongo.adapter.BoardAdapter;
import nextus.restartallkill.pokemongo.adapter.GenericRecylerAdapter;
import nextus.restartallkill.pokemongo.core.lifecycle.CycleControllerActivity;
import nextus.restartallkill.pokemongo.core.view.DeclareView;
import nextus.restartallkill.pokemongo.data.BoardItem;
import nextus.restartallkill.pokemongo.util.CustomRequest;
import nextus.restartallkill.pokemongo.util.MyApplication;

public class BoardActivity extends CycleControllerActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    @DeclareView(id= R.id.boardRecycler) RecyclerView recyclerView;
    @DeclareView(id=R.id.adView) AdView adView;
    @DeclareView(id=R.id.fab, click="this") FloatingActionButton fab;

    StaggeredGridLayoutManager staggeredGridLayoutManager;
    LinearLayoutManager linearLayoutManager;

    EditText nickname;

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "MainActivity";

    BoardAdapter adapter;
    TempBoardAdapter adapter2;

    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;
    ProgressDialog mProgressDialog;
    SignInButton signInButton;
    Dialog dialog;

    Boolean login = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board, true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.menu04));

        gso = MyApplication.getInstance().getGoogleSignInOptions();
        mGoogleApiClient = MyApplication.getInstance().getGoogleApiClient(this, this);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        //staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
       // recyclerView.setHasFixedSize(true);

        //recyclerViewAdapter = new BoardRecyclerAdapter(this, 1);
        //recyclerView.setAdapter(recyclerViewAdapter);

        adapter = new BoardAdapter();
        adapter.setClickListener(new GenericRecylerAdapter.OnViewHolderClick() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getApplicationContext(),"Position :"+position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), BoardItemViewActivity.class);
                intent.putExtra("position",position);


                startActivity(intent);
            }
        });

        adapter2 = new TempBoardAdapter(getApplicationContext());
        //recyclerView.setAdapter(adapter);
        recyclerView.setAdapter(adapter);


        SharedPreferences pref = getSharedPreferences("pokemon", MODE_PRIVATE);
        if(pref.getString("user_nickname",null) != null )
        {
            Log.e("USER_NICKNAME",""+pref.getString("user_nickname",null));
        }


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int previousTotal = 0;
            private boolean loading = true;
            private int visibleThreshold = 5;
            int visibleItemCount, totalItemCount;
            int[] firstVisibleItems, firstVisibleItem = null;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = staggeredGridLayoutManager.getChildCount();
                totalItemCount = staggeredGridLayoutManager.getItemCount();
                firstVisibleItem = staggeredGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.fab:
                if(!login)
                {
                    showDialog();
                }
                else
                {
                    Intent intent = new Intent(this, CreateContentsActiity.class);
                    startActivity(intent);
                }
                break;

            case R.id.sign_in_button:
                signIn();
                break;

            case R.id.nickname_submit:
                if(nickname.getText().toString().length() == 0)
                {
                    Toast.makeText(getApplicationContext(),"닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    setUserData();
                }
                break;
        }
    }

    public void showDialog()
    {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.login_dialog);
        dialog.setTitle("로그인");
        dialog.show();

        signInButton = (SignInButton) dialog.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(this);
    }


    private void signIn() {
        dialog.dismiss();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            makeNickName();
        }
    }

    private void setUserData()
    {
        SharedPreferences pref = getSharedPreferences("pokemon", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user_id", MyApplication.result.getId());
        editor.putString("user_email",MyApplication.result.getEmail());
        editor.putString("user_nickname",nickname.getText().toString());
        editor.apply();

        addUserInfo();
    }

    private void makeNickName()
    {
        dialog.setContentView(R.layout.nickname_dialog);
        dialog.show();

        nickname = (EditText) dialog.findViewById(R.id.nickname_input);
        Button submit = (Button) dialog.findViewById(R.id.nickname_submit);
        submit.setOnClickListener(this);

    }

    private void addUserInfo()
    {
        String url = "http://restartallkill.nextus.co.kr/pokemongo/addUser.jsp";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("user_id", MyApplication.result.getId());
                params.put("user_email", MyApplication.result.getEmail());
                params.put("user_nickname", nickname.getText().toString());
                return params;
            }

        };

        MyApplication.getInstance().addToRequestQueue(stringRequest);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("TEST", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            MyApplication.result = result.getSignInAccount();

            login = true;
        } else {
            Log.e("IDtoken","null");
            // Signed out, show unauthenticated UI.
            login = false;
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

       // showProgressDialog();
        getData();
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
                            adapter.setList(MyApplication.getInstance().boardItem.getBoardData());
                            if(MyApplication.boardItem.getBoardData().size() >= 5 )
                                adapter.setItmeCout(5);
                            else
                                adapter.setItmeCout(MyApplication.boardItem.getBoardData().size());

                            adapter.notifyDataSetChanged();
                            hideProgressDialog();
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
    public void onDestroy()
    {
        super.onDestroy();
        if(dialog != null) dialog.dismiss();
        hideProgressDialog();
    }

}
