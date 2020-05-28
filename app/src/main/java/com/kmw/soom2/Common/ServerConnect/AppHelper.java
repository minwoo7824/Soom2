package com.kmw.soom2.Common.ServerConnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by kmw on 2017. 10. 18..
 */

public class AppHelper {

    private static String TAG = "AppHelper";

    public static byte[] getFileDataFromDrawable(Context context, int id) {
        Drawable drawable = ContextCompat.getDrawable(context, id);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Turn drawable into byte array.
     *
     * @param drawable data
     * @return byte array
     */
    public static byte[] getFileDataFromDrawable(Context context, Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        Log.i(TAG," = " + byteArrayOutputStream.toByteArray());
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] getFileDataFromDrawable(Context context, Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        Log.i(TAG," = " + byteArrayOutputStream.toByteArray());
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] getByteFromMP3(Context context, File file) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(byteArrayOutputStream);
            oos.writeObject(file);
            byteArrayOutputStream.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG," = " + byteArrayOutputStream.toByteArray());
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] convertVideoToBytes(Context context, Uri uri) {
        byte[] videoBytes = null;
        try {//from   w  ww  .  j av a2s  . com
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            FileInputStream fis = new FileInputStream(new File(String.valueOf(uri)));
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);

            videoBytes = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoBytes;
    }

    public static byte[] convertVideoToBytes(Context context, String path) {
        byte[] theByte = null;

        return path.getBytes();
    }
}
