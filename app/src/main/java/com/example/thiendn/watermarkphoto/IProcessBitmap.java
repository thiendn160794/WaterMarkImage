package com.example.thiendn.watermarkphoto;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by thiendn on 14:16 4/28/19
 */
public interface IProcessBitmap {
    void onBitmapSelected(Bitmap bitmap);
    void onBitmapsSelected(ArrayList<Bitmap> bitmaps);
}
