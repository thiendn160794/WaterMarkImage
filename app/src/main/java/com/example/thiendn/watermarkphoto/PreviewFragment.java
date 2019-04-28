package com.example.thiendn.watermarkphoto;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewFragment extends Fragment{

    public static final String TAG = "PreviewFragment";
    public static final String KEY_BITMAP = "key_bitmap";

    @BindView(R.id.iv_preview)
    ImageView ivPreview;

    Bitmap bitmap;

    public static PreviewFragment newInstance(Bitmap bitmap) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_BITMAP, bitmap);
        PreviewFragment fragment = new PreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bitmap = getArguments().getParcelable(KEY_BITMAP);
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
        ivPreview.setImageBitmap(bitmap);
    }
}
