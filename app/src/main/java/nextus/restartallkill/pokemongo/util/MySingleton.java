package nextus.restartallkill.pokemongo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import nextus.restartallkill.pokemongo.BoardItem;

/**
 * Created by chosw_000 on 2015-07-15.
 */
public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;
    private static BoardItem boardItem;

    private MySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
        getData();
    }

    public BoardItem getBoardItem()
    {
        return boardItem;
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
                            boardItem = response;
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

    }

    public static synchronized MySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
