package com.example.mayoo.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by mayoo on 11/26/2016.
 */

public class ImageHelper {
        Bitmap imageBitmap_fromDB;

        // convert from bitmap to byte array
        public byte[] getBytes(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            return stream.toByteArray();
        }

        // convert from byte array to bitmap
        public Bitmap getImage(byte[] image) {
            InputStream inputStream  = new ByteArrayInputStream(image);
            imageBitmap_fromDB = BitmapFactory.decodeStream(inputStream);
            return imageBitmap_fromDB;
        }

}
