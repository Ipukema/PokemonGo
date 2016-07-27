package nextus.restartallkill.pokemongo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

import nextus.restartallkill.pokemongo.AddImgRecyclerAdapter;
import nextus.restartallkill.pokemongo.R;
import nextus.restartallkill.pokemongo.core.lifecycle.CycleControllerActivity;
import nextus.restartallkill.pokemongo.core.view.DeclareView;
import nextus.restartallkill.pokemongo.util.MultiPartRequest;
import nextus.restartallkill.pokemongo.util.MyApplication;

public class CreateContentsActiity extends CycleControllerActivity implements View.OnClickListener {

    @DeclareView(id = R.id.addImgRecyclerView) RecyclerView myList;
    @DeclareView(id = R.id.send, click = "this") CardView sendButton;
    @DeclareView(id = R.id.adView) AdView adView;

    private int PICK_IMAGE_REQUEST = 1;

    private ArrayList<Bitmap> addedImg = new ArrayList<>();
    private ArrayList<String> fileName = new ArrayList<>();
    LinearLayoutManager layoutManager;
    AddImgRecyclerAdapter adapter;

    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contents_actiity, true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("글쓰기");

        Log.e("UserID",""+MyApplication.result.getId());

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        myList.setLayoutManager(layoutManager);

        adapter = new AddImgRecyclerAdapter(getApplicationContext(), addedImg);
        adapter.setOnItemClickListener(onItemClickListener);
        adapter.setOnClickListener(onClickListener);

        myList.setAdapter(adapter);

    }

    AddImgRecyclerAdapter.OnItemClickListener onItemClickListener = new AddImgRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (position == addedImg.size()) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            } else if (view.getId() == R.id.cancel_button) {
                addedImg.remove(position);
                adapter.notifyDataSetChanged();
                Log.e("Position_number", "" + position);
            }


        }
    };

    AddImgRecyclerAdapter.OnClickListener onClickListener = new AddImgRecyclerAdapter.OnClickListener() {
        @Override
        public void onClick(View view, int position) {
            if (view.getId() == R.id.cancel_button) {
                addedImg.remove(position);
                adapter.notifyDataSetChanged();
            }

            Log.e("Position_number", "" + position + " view_id = " + view.getId());
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                upload();
                //uploadImage();
                //String image = getStringImage(addedImg.get(0));
                //decodeImg(image);
                break;
        }
    }

    public void upload()
    {
        String UPLOAD_URL = "http://restartallkill.nextus.co.kr/pokemongo/multipart.jsp";

        MultiPartRequest multipartRequest = new MultiPartRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Toast.makeText(getApplicationContext(), ""+addedImg.size()+"개의 이미지와 함께 업로드가 완료되었습니다." , Toast.LENGTH_LONG).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", ""+MyApplication.result.getId());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                for(int i=0; i<addedImg.size(); i++)
                {
                    params.put("test"+i, new DataPart(""+fileName.get(i)+".png", getByteImage(addedImg.get(i)), "image/png"));
                }


                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(multipartRequest);
    }

    private void uploadImage(){
        //Showing the progress dialog
        String UPLOAD_URL = "http://restartallkill.nextus.co.kr/pokemongo/uploadImage.jsp";

        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(getApplicationContext(), s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(addedImg.get(0));

                //Getting Image Name
                //String name = editTextName.getText().toString().trim();

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("test", ""+image);
                Log.e("Image STring",""+image);

                //returning parameters
                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(stringRequest);
    }

    public byte[] getByteImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        return imageBytes;
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
      //  Log.e("EncodedImage",""+encodedImage);

        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri filePath = data.getData();
            file = new File(filePath.getPath());
            fileName.add(file.getName());
            try {
                //Getting the Bitmap from Gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                addedImg.add(bitmap);
                adapter.notifyDataSetChanged();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void decodeImg(String base64)
    {
        //String base64 = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEB%20AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEB%20AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCAusFMADASIA%20AhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA%20AAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3%20ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm%20p6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEA%20AwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx%20BhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK%20U1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3%20uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD8Y/D+%20qRanp89zPc20OH+xeUrmHe+bhJXkRDHJJx+9/fRRQfv26nNF4ZNPtjBd2S2s0bh47lJvtUaRuHS2%20huQh/wBVlfKwDF/qgTk9asVno2lRw2sEbprW5W+0yOUhMbovlvscn/WyD9/6eWeuCWo38VxcTpNd%20RzzTPsjeaZ5IftWyRs74c7/KGI+pj/cTHJN+Dubt7Scuq5Vf/wAGO/4dfPXV35cHBQotLa6srbWc%2018/hv5czV3Ztxv8A2dZ2Qm80pCl3Ha/x73fz5wlnChY/vbkPJNNNL/z2OckBq1vs/laZFNYWSTO8%20KweSkiCaZMfI7o//ADy8zOZf3HPfPNPZHqDme42XUTyRulkiAQf6xo45ngmizHLKc+T+9m5x/pRG%205hZtLbUYbWG/kIdY/MgaJpkuY008XEiSSTOn+sl9c+Ufs5u+SRzTlvtzW5Wr7aza1t1utLaa6u2u%20qjzU5K9r7PvrUWqvp976LV3Kmm6p9iuJpLnZ9r2Mv2Z5HbZ+8YBJnc/64xxpNDCcnBuASCCSr2cl%20vqsF5P50zpNL8jmGVH+0o7wo+z/VxfP/AKmL/l3IuiScGtOGzstQiPkW6JeTIMPNvT/RhI2+R+Uj%20/d+VFiGXMBJPJ+9VGz82TUh58aPcQIblLaPYiJ5PnfZZN/8AzyjjjM00+embXOTzHt9JPk+FX+Lf%20W38vz/QilG3tNficnttzOK79OW/z8ghuy000otwiRfJ/z8TSRrG7s6DHlxyxb/33f98Pspyc0yW2%20srZ4IPL8xYsTQpHNxcz3Ak3zImf3nmxAZ/em4HA6jJvQi2lglluvO83e7SwvlEmcMzom/wAtJI5U%20/wBdP34/0UkjNJD5VwDBFHPczJDMkK2u/wC0mfdMZXZznnHlf66LrKOcsWPBT5XWl7u0u/Xn5b7d%203e3m1e12KcXJzUdbuOl7W5Zu72d7u110Wt9WFhby3EjTPb+c7oXhd/kkh/eP/osaOZMfu44v9d5Q%20545Dbsa5ii126nv9Qkmh/s8iyS2hdEh3hjshREaP97k+VAYvNIts/auQxPU21xrP9i332Kz/ANRc%20iGSa5P76d2DQ3ML745f3XliX9/27kkEnH8u4smj321zNbP8A6YjwCRkTdud0RM/aPkzFF+656C6z%20kvVVVbmd95uP3uTv+O34q5vFcsVG97K1/ncLWRJJWdbj57aGOHSt3/HukjFvNREU/vJYvM4mPNvc%20FhdHJ5j/AH8cbPLny9kscaWzr8iFHCTP5h/eeZvPneV6nnO81nSXlv5sYS3TR97JHCm8u6HY+/vL%20++kDy/64+xPzVvTR3Ecy+dGn2beGiDyfJJ975NmU8zzcCWDv9o3DJxuPPKfNpZbJXesna+7sr79u%20/cyq6Ke2kW9t9JrXvt91l0u8q3kXEEd3bedswHhkz9lRHlnSF3dGlkjll8kGbmbv1JZq0ryOOfUN%20S1IRTxxQaZHa5tVklR4Xt3SbyI8lI4vlH+p8o/ul5wWJs3NhFCZZLqN1G3ztybHRDlo+PJMcckvO%20QJTN1I4yWLLjT728tmZzsCQjfILkFHhlVnkh2eZmKXyx7/e6g/LWTV7baNNfK/8AwPveumqw0n+8%20SlZpJNRTi1q076u+z8tZa2Q9NUkkghgg8tbD/R4D50BWS5geNBCkGW8znHnCbk24zyMZNRbby7uS%20blE2RJDMu+YQu/n7E2J+7+1SRrFF5/Jwx54OXDzPMtETL2NrbAOiIkMKRusyJsdzJ5csXlD/AFUf%20kfN/pV0a1RZxSedNHGnyIHhR5n5+ZBM/zny/N+z/AOvmiPWY85BL6LSE+vM0vS0ua/nf/g67AtL+%20aS+53KkFtHDcTzR3D7NjIF8nYJ44d+xB+9/1o8z9xz/y2I5zk10kt4eW2QYzHAzjZsRDvSfydgk5%208sn9753/AE9XXDZr6kNQ2Ojy/Zpt8f30TyYYN537xJ+7lljx/qc5twRkkg1FFHJcRJvjfZsHyBCj%20/JJM/Rz5kssbv/yyl+0c55JqSZJck/8AC/wdS3fsvw101uQyyWGo3dl5bkPDG6ujqkMPmpJ5u9HP%20/LOMf89fXnJOWfZBc2d1vCJdwvHMS8L7H8uRj+7aSQ5lSPMXkzH7RkjJzxSNImZoRH+8g+ypH5ju%20iv8AvHATYSU+/wDus/8AHwSQeMEGhNI0moGBpJIYrby5nSDyd/ySs77sGX/VgiWfMX/LY4BBZqLJ%20N26uKf3ySvrpayv/ANvb21wgvdi/5pRb3+xOUV1fe7fTs7mw/mYaSbY8Ny8Ebvz/AKM6Kzv9pk82%20MxxR+ZmHrjyuTnk0JPL3RXMfnO6TSfuYPkgWOENH5jzeYY5ZbiMiWHn8SS1ICbp5V8yZPPZ/PkmY%20+XDM8jyR70jJ/wBZhvJ86In5rnnJNNd4IZJoI7cw29k8b2s8Es6TTXPzTFHjcN5pkjHrn7RMOQcE%207NWpLzcX8kpa/j/VzpekJd3bT+7eeu/bWz1e1272t6deS/uJ5/ueSHd3uX+SOOabP3DIf3sm/wDf%20eWZ/3p5xuByLiOSRX+x7Ibi5Q+Xv8yGbfHK75XZJ+98z9z58HmfaM7Rk81to+NPmg825+0wPA80f%20nK00ke2T5FSby3PSHzvNjP2c55AJBzbkXEEHl7ngkVEmcHJgh8mcpCiNhMS7/K+0eUYRwvOSSZeq%20UdeZRUbW396Ur36Kzv19dbj5l7N69OZaPWN7320017/MvR28sUjzahG8257dE3o67z5Tb0P/AFzj%20H7jn0GThs1preOSW9ubq22XcNtJ9yf8A49Q6MkEKR58v96BEe/7jF1kkZK6peSactrc2tu+oTTJI%20myN0e1h82U70k3k+ZlMeRBnmfObcVW057i4hP2ryd+xhKkyJveN2f+DzTJjy/K8797MeT9lxjm6O%20nN/cg5eulR28vXX0OfDKzlO/Rq3p717/ANd/I0rG4ik06KC+t45QmI7aO2kkWNHlL4m3ky/aDJ+8%20ng56kjJOWOcbWwt9aN5iaJvmhbe/kwv50kzyQyK8vmDp+587/lvGBkkkmcRC2zaxeZAJiEZhCVBd%201b5E/dp9nH7mKaEyxxEHI5IJNFdOW3uFRZGeKN7iaU3IDnfGzu8M2wntCuZ8Z5GckEnaknCbd+Z6%20fHeV/i1jbXbe19ObVm84c8eW7dmnfvrL4dHd2310utdWae6D7JLNN5yW7";

        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap base64Bitmap = BitmapFactory.decodeByteArray(decodedString, 0,
                decodedString.length);
        addedImg.add(base64Bitmap);
        adapter.notifyDataSetChanged();
    }
}
