package com.project.ruddle.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.ruddle.HomeActivity;
import com.project.ruddle.R;
import com.project.ruddle.constants.References;
import com.project.ruddle.handlers.ListsHandler;
import com.project.ruddle.handlers.PostLists;
import com.project.ruddle.verification.LoginActivity;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.profile_layout, container, false);

        TextView welcomeTextView = (TextView) rootView.findViewById(R.id.username);
        SharedPreferences settings = getActivity().getSharedPreferences(References.USER, MODE_PRIVATE);
        String name = settings.getString("name", "user");
        welcomeTextView.setText(name);


        Button logOut = (Button) rootView.findViewById(R.id.log_out);
        logOut.setOnClickListener(v -> {
            SharedPreferences.Editor editor = settings.edit();

            editor.putBoolean("hasLoggedIn", false);

            editor.apply();

            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });


        final PostLists lists = new ListsHandler((HomeActivity)getActivity());

        Button inProgressList = (Button) rootView.findViewById(R.id.inprogress);
        inProgressList.setOnClickListener(v -> lists.getInProgressPosts());

        Button solvedList = (Button) rootView.findViewById(R.id.solved);
        solvedList.setOnClickListener(v -> lists.getSolvedPosts());

        Button createdList = (Button) rootView.findViewById(R.id.created);
        createdList.setOnClickListener(v -> lists.getCreatedPosts());

        Button changeProfileIcon = (Button) rootView.findViewById(R.id.change_profile_picture);
        changeProfileIcon.setOnClickListener(v -> showFileChooser());

        return rootView;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), References.PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == References.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                ImageView imageView = (ImageView) getActivity().findViewById(R.id.profile_icon);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
