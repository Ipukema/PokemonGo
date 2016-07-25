package nextus.restartallkill.pokemongo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;

import nextus.restartallkill.pokemongo.AddImgRecyclerAdapter;
import nextus.restartallkill.pokemongo.R;
import nextus.restartallkill.pokemongo.core.lifecycle.CycleControllerActivity;
import nextus.restartallkill.pokemongo.core.view.DeclareView;

public class CreateContentsActiity extends CycleControllerActivity implements View.OnClickListener {

    @DeclareView(id=R.id.addImgRecyclerView) RecyclerView myList;

    private int PICK_IMAGE_REQUEST = 1;

    private ArrayList<Bitmap> addedImg = new ArrayList<>();
    LinearLayoutManager layoutManager;
    AddImgRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contents_actiity, true);


        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        myList.setLayoutManager(layoutManager);

        adapter = new AddImgRecyclerAdapter(getApplicationContext(), addedImg);
        adapter.setOnItemClickListener(onItemClickListener);

        myList.setAdapter(adapter);

    }

    AddImgRecyclerAdapter.OnItemClickListener onItemClickListener = new AddImgRecyclerAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View view, int position) {
            if(position == addedImg.size())
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }

        }
    };

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
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
