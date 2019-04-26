package com.example.thiendn.watermarkphoto;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class SelectImageFragment extends Fragment {

    public final static String TAG = "SelectImageFragment";
    private static final int OPEN_DOCUMENT_CODE = 2;
    private static final int OPEN_CAMERA_CODE = 3;

    public static SelectImageFragment newInstance(){
        return new SelectImageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pick_images, container, false);
    }

    @OnClick({R.id.btn_open_camera, R.id.btn_select_image})
    public void onClick(View view){
        if (view.getId() == R.id.btn_open_camera){
            openCamera();
        } else if (view.getId() == R.id.btn_select_image){
            openGallery();
        }
    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_DOCUMENT_CODE);
    }

    private void openCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, OPEN_CAMERA_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == OPEN_DOCUMENT_CODE && resultCode == RESULT_OK) {
            if (resultData != null) {
                // this is the image selected by the user
                Uri imageUri = resultData.getData();
            }
        } else if (requestCode == OPEN_CAMERA_CODE && resultCode == RESULT_OK){
            if (resultData != null){

            }
        }
    }
}
