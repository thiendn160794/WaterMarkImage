package com.example.thiendn.watermarkphoto;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * Created by thiendn on 14:16 4/28/19
 */
public interface IProcessBitmap {
    void onBitmapSelected(HashMap<String, Bitmap> maps);
}
