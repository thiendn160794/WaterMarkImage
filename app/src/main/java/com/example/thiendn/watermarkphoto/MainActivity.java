package com.example.thiendn.watermarkphoto;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements IProcessBitmap{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SelectImageFragment selectImageFragment = SelectImageFragment.newInstance();
        selectImageFragment.setmProcessBitmap(this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fr_main, selectImageFragment, SelectImageFragment.TAG)
                .addToBackStack(SelectImageFragment.TAG)
                .commit();
    }

    @Override
    public void onBitmapSelected(HashMap<String, Bitmap> bitmap) {
        try {
            Bitmap bitmap2 = Utils.convertUriToBitmap(Uri.parse(SharePref.getWaterMarkUrl(this)), this);
            for (String key : bitmap.keySet()) {
                Bitmap value = bitmap.get(key);
                bitmap.put(key, combineBitmaps(value, bitmap2));
            }
            PreviewFragment previewFragment = PreviewFragment.newInstance(bitmap);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fr_main, previewFragment, PreviewFragment.TAG)
                    .addToBackStack(PreviewFragment.TAG)
                    .commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap combineBitmaps(Bitmap firstBitmap, Bitmap secondBitmap) {
        Bitmap bitmapWaterMark = getResizedBitmap(secondBitmap, firstBitmap.getWidth(), firstBitmap.getHeight());
        Bitmap bmOverlay = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(), firstBitmap.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(firstBitmap, new Matrix(), null);
        canvas.drawBitmap(bitmapWaterMark, 0, 0, null);
        return bmOverlay;
    }

    private Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
