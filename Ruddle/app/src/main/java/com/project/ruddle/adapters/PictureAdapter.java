package com.project.ruddle.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.project.ruddle.R;

import java.io.IOException;

public class PictureAdapter extends AppCompatActivity implements View.OnClickListener {


    //Declaring views
    private Button changeProfilePicture;
    private ImageView imageView;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //Bitmap to get image from gallery
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        //Initializing views
        changeProfilePicture = (Button) findViewById(R.id.change_profile_picture);
        imageView = (ImageView) findViewById(R.id.profile_icon);

        //Setting clicklistener
        changeProfilePicture.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        showFileChooser();
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
