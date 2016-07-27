package nextus.restartallkill.pokemongo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nextus.restartallkill.pokemongo.AddImgRecyclerAdapter;
import nextus.restartallkill.pokemongo.R;
import nextus.restartallkill.pokemongo.core.lifecycle.CycleControllerActivity;
import nextus.restartallkill.pokemongo.core.view.DeclareView;
import nextus.restartallkill.pokemongo.util.MultiPartRequest;
import nextus.restartallkill.pokemongo.util.MyApplication;

public class CreateContentsActiity extends CycleControllerActivity implements View.OnClickListener {

    @DeclareView(id = R.id.addImgRecyclerView) RecyclerView myList;
    @DeclareView(id = R.id.send, click = "this") CardView sendButton;
    @DeclareView(id=R.id.contents_title) EditText title;
    @DeclareView(id=R.id.contents_info) EditText info;
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
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                if(title.getText().toString().length() == 0 || info.getText().toString().length() == 0)
                {
                    Toast.makeText(getApplicationContext(),"제목 또는 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else
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
                SharedPreferences pref = getSharedPreferences("pokemon",MODE_PRIVATE);
                params.put("user_id", pref.getString("user_id",null));
                params.put("user_nickname", pref.getString("user_nickname",null));
                params.put("board_title", title.getText().toString());
                params.put("board_info", info.getText().toString());
                params.put("date",new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(System.currentTimeMillis())));

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                if(addedImg.size()==0)
                {
                    //params.put("image", new DataPart(""+fileName.get(i)+".png", getByteImage(addedImg.get(i)), "image/png"));
                }
                else
                {
                    for(int i=0; i<addedImg.size(); i++)
                    {
                        params.put("image"+i, new DataPart(""+fileName.get(i)+".png", getByteImage(addedImg.get(i)), "image/png"));
                    }
                }
                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(multipartRequest);
    }

    public byte[] getByteImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        return imageBytes;
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

}
