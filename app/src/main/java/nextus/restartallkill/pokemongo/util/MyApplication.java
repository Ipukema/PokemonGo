package nextus.restartallkill.pokemongo.util;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import nextus.restartallkill.pokemongo.data.BlogItem;
import nextus.restartallkill.pokemongo.data.BoardItem;

/**
 * Created by chosw on 2016-07-16.
 */
public class MyApplication extends Application {

    public static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication mInstance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    public static BoardItem boardItem;
    public static BlogItem blogItem;
    public static String userId ="";
    public static GoogleSignInAccount result;
    public AppCompatActivity activity;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public GoogleApiClient getmGoogleApiClient()
    {
        return mGoogleApiClient;
    }

    public GoogleSignInOptions getGoogleSignInOptions(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .build();
        return gso;
    }
    public GoogleApiClient getGoogleApiClient(AppCompatActivity activity, GoogleApiClient.OnConnectionFailedListener listener){
        this.activity = activity;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this.activity, listener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, getGoogleSignInOptions())
                .build();
        return mGoogleApiClient;
    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return this.requestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (imageLoader == null) {
            imageLoader = new ImageLoader(this.requestQueue, new LruBitmapCache(LruBitmapCache.getCacheSize(getApplicationContext())));
            return imageLoader;
        }
        else
            return this.imageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
}
