package com.example.thiendn.watermarkphoto;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

/**
 * Created by thiendn on 14:11 4/28/19
 */
public class Utils {

    public static Bitmap convertUriToBitmap(Uri uri, Context context) throws IOException {
        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
    }

    public static boolean isURIValid(Context context, String uri){
        if (uri == null) {
            return false;
        }
        ContentResolver cr = context.getContentResolver();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cur = cr.query(Uri.parse(uri), projection, null, null, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                String filePath = cur.getString(0);

                if (new File(filePath).exists()) {
                    cur.close();
                    return true;
                } else {
                    cur.close();
                    return false;
                }
            } else {
                cur.close();
                return false;
            }
        } else {
            return false;
        }
    }
}
