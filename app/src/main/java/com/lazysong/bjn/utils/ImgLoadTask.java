package com.lazysong.bjn.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lazysong on 2017/5/22.
 */
public class ImgLoadTask extends AsyncTask<Void, Void, Bitmap> {
    private final String BASE_URL = "http://bijiniu.com/tomcat/bijiniu";
    private final String headUrl;
    private ImageView imgview;
    private final OkHttpClient client = new OkHttpClient();

    public ImgLoadTask(String headUrl, ImageView imgview) {
        this.headUrl = headUrl;
        this.imgview = imgview;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Request request = new Request.Builder().url(BASE_URL + headUrl).build();
        Response response;
        Bitmap bitmap = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                InputStream is = response.body().byteStream();
                bitmap = BitmapFactory.decodeStream(is);
            }
            else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(final Bitmap bitmap) {
        imgview.setImageBitmap(BitmapProcesser.getRoundedCornerBitmap(bitmap));
    }
}
