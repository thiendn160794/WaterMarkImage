package com.example.thiendn.watermarkphoto;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class SelectImageFragment extends Fragment {

    public static final String TAG = "SelectImageFragment";

    private static final int OPEN_IMAGE_CODE = 2;
    private static final int OPEN_CAMERA_CODE = 3;
    private static final int OPEN_WATER_MARK_CODE = 4;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 101;

    private IProcessBitmap mProcessBitmap;

    public static SelectImageFragment newInstance(){
        return new SelectImageFragment();
    }

    public void setmProcessBitmap(IProcessBitmap mProcessBitmap) {
        this.mProcessBitmap = mProcessBitmap;
    }

    @BindView(R.id.iv_watermark)
    ImageView ivWaterMark;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_images, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            ivWaterMark.setImageURI(Uri.parse(SharePref.getWaterMarkUrl(getContext())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_open_camera, R.id.btn_select_image, R.id.btn_pick_water_mark})
    public void onClick(View view){
        if (view.getId() == R.id.btn_pick_water_mark) {
            openGallery(OPEN_WATER_MARK_CODE);
            return;
        }
        if (Utils.isURIValid(getContext(), SharePref.getWaterMarkUrl(getContext()))){
            if (view.getId() == R.id.btn_open_camera){
                requestPermission();
            } else if (view.getId() == R.id.btn_select_image){
                openGallery(OPEN_IMAGE_CODE);
            }
        } else {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.error)
                    .setMessage(R.string.watermark_has_not_been_initialize)
                    .setNegativeButton(getString(R.string.ok), (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        }
    }

    private void openGallery(int code){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, code);
    }

    private void openCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, OPEN_CAMERA_CODE);
        }
    }

    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(getContext(), "Only work if camera available!", Toast.LENGTH_LONG).show();
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                // No explanation needed; request the permission
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_CAMERA is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Camera permission granted!", Toast.LENGTH_SHORT).show();
                    openCamera();
                } else {
                    Toast.makeText(getContext(), "Camera permission denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == OPEN_IMAGE_CODE && resultCode == RESULT_OK) {
            if (resultData != null) {
                // this is the image selected by the user
                Uri imageUri = resultData.getData();
                if (imageUri != null) {
                    try {
                        Date dNow = new Date();
                        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
                        String fileName = ft.format(dNow) + ".png";
                        HashMap<String, Bitmap> maps = new HashMap<>();
                        maps.put(fileName, Utils.convertUriToBitmap(imageUri, getContext()));
                        mProcessBitmap.onBitmapSelected(maps);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (requestCode == OPEN_CAMERA_CODE && resultCode == RESULT_OK){
            if (resultData != null){
                Date dNow = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
                String fileName = ft.format(dNow) + ".png";
                HashMap<String, Bitmap> maps = new HashMap<>();
                Bitmap image = (Bitmap) resultData.getExtras().get("data");
                maps.put(fileName, image);
                mProcessBitmap.onBitmapSelected(maps);
            }
        } else if (requestCode == OPEN_WATER_MARK_CODE && resultCode == RESULT_OK) {
            if (resultData != null && resultData.getData() != null) {
                Uri waterMarkUri = resultData.getData();
                ivWaterMark.setImageURI(waterMarkUri);
                SharePref.saveWaterMarkUrl(waterMarkUri.toString(), getContext().getApplicationContext());
            }
        }
    }
}
