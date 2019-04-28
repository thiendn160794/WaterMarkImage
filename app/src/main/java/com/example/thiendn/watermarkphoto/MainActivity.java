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
    public void onBitmapSelected(Bitmap bitmap) {
        try {
            Bitmap bitmap2 = Utils.convertUriToBitmap(Uri.parse(SharePref.getWaterMarkUrl(this)), this);
            PreviewFragment previewFragment = PreviewFragment.newInstance(combineBitmaps(bitmap, bitmap2));
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fr_main, previewFragment, PreviewFragment.TAG)
                    .addToBackStack(PreviewFragment.TAG)
                    .commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBitmapsSelected(ArrayList<Bitmap> bitmaps) {

    }

    private Bitmap combineBitmaps(Bitmap firstBitmap, Bitmap secondBitmap) {
        Bitmap bmOverlay = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(), firstBitmap.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(firstBitmap, new Matrix(), null);
        canvas.drawBitmap(secondBitmap, 0, 0, null);
        return bmOverlay;
    }
}
