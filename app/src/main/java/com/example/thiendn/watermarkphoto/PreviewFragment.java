package com.example.thiendn.watermarkphoto;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreviewFragment extends Fragment{

    public static final String TAG = "PreviewFragment";
    public static final String KEY_BITMAP = "key_bitmap";

    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.iv_preview)
    ImageView ivPreview;
    @BindView(R.id.progress_bar)
    ProgressBar mPb;

    HashMap<String, Bitmap> maps;
    SaveImage saveImageTask;

    public static PreviewFragment newInstance(HashMap<String, Bitmap> map) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_BITMAP, map);
        PreviewFragment fragment = new PreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.btn_save})
    public void onClick(){
        saveImage(maps);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveImageTask.setmCallback(null);
    }

    private void saveImage(HashMap<String, Bitmap> map) {
        saveImageTask = new SaveImage(getContext(), map, mPb);
        saveImageTask.setmCallback(new SaveImage.AsyncTaskListener() {
            @Override
            public void onPreExecuted() {
                mPb.setVisibility(View.VISIBLE);
                mPb.setProgress(0);
                btnSave.setEnabled(false);
            }

            @Override
            public void onProgressUpdate(int progress) {
                mPb.setProgress(progress);
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                mPb.setVisibility(View.GONE);
                btnSave.setEnabled(true);
            }
        });
        saveImageTask.execute();
    }

    public static class SaveImage extends AsyncTask<Void, Integer, Boolean>{

        WeakReference<Context> contextWeakReference;
        HashMap<String, Bitmap> map;
        AsyncTaskListener mCallback;
        int count;

        SaveImage(Context context, HashMap<String, Bitmap> map, ProgressBar progressBar) {
            contextWeakReference = new WeakReference<>(context);
            this.map = map;
        }

        public void setmCallback(AsyncTaskListener mCallback) {
            this.mCallback = mCallback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mCallback.onPreExecuted();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/WaterMark");
            myDir.mkdirs();
            for (String key : map.keySet()) {
                Bitmap bitmap = map.get(key);
                File file = new File (myDir, key);
                if (file.exists ()) file.delete ();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    mCallback.onError(e.getMessage());
                }
                mCallback.onProgressUpdate(++count * 100 / map.size());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mCallback.onSuccess();
        }

        public interface AsyncTaskListener {
            void onPreExecuted();
            void onProgressUpdate(int progress);
            void onError(String msg);
            void onSuccess();
        }
    }
}
