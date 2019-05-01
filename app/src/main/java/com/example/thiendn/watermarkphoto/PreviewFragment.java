package com.example.thiendn.watermarkphoto;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreviewFragment extends Fragment{

    public static final String TAG = "PreviewFragment";
    public static final String KEY_BITMAP = "key_bitmap";

    @BindView(R.id.iv_preview)
    ImageView ivPreview;

    HashMap<String, Bitmap> maps;

    public static PreviewFragment newInstance(HashMap<String, Bitmap> map) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_BITMAP, map);
        PreviewFragment fragment = new PreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.btn_save})
    public void onClick(){
        for (String key : maps.keySet()) {
            Bitmap value = maps.get(key);
            saveImage(key, value);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        maps = (HashMap<String, Bitmap>) getArguments().getSerializable(KEY_BITMAP);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (String key : maps.keySet()) {
            Bitmap value = maps.get(key);
            ivPreview.setImageBitmap(value);
        }
    }

    private void saveImage(String fileName, Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/WaterMark");
        myDir.mkdirs();

        File file = new File (myDir, fileName);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(getContext(), "Try Image Done!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(getContext(), "Save Image Done!", Toast.LENGTH_SHORT).show();
    }
}
