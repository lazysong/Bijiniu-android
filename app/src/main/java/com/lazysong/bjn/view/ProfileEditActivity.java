package com.lazysong.bjn.view;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.lazysong.bjn.R;
import com.lazysong.bjn.utils.ImgLoadTask;
import com.lazysong.bjn.vo.UserVo;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProfileEditActivity extends com.lazysong.bjn.utils.TakePhotoActivity {
    private Toolbar toolbar;
    private TextView txtTitle;
    private ImageView imgUserHead;

    private UserInfoTask userInfoTask;
    private ImgLoadTask getImgHead;

    private String userId;
    private String userKey;

    private static final String TAG = TakePhotoActivity.class.getName();

    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        txtTitle = (TextView) findViewById(R.id.titleToolbar);
        txtTitle.setText("编辑资料");

        toolbar = (Toolbar) findViewById(R.id.toolbar_base);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);//设置导航栏图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileEditActivity.this.finish();
            }
        });
        imgUserHead = (ImageView) findViewById(R.id.imgUserHeadEditProfile);
        imgUserHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ImageSelectorActivity.start(ProfileEditActivity.this, 1, ImageSelectorActivity.MODE_SINGLE, true,true,true)
                getTakePhoto().onPickFromGallery();
            }
        });
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userKey = intent.getStringExtra("userKey");
        userInfoTask = new UserInfoTask(userId);
        userInfoTask.execute();
    }

    @Override
    public void takeSuccess(TResult result) {
        String imgPath = result.getImage().getOriginalPath();
        Log.i(TAG,"takeSuccess：" + result.getImage().getOriginalPath());
        //修改头像
        ModifyHeadTask task = new ModifyHeadTask(userId, userKey, imgPath);
        task.execute();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.i(TAG, "takeFail:" + msg);
    }

    @Override
    public void takeCancel() {
        Log.i(TAG, getResources().getString(R.string.msg_operation_canceled));
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserInfoTask extends AsyncTask<Void, Void, String> {
        private final String BASE_URL = "http://bijiniu.com/tomcat/bijiniu";

        private final String userId;
        private final OkHttpClient client = new OkHttpClient();

        UserInfoTask(String userId) {
            this.userId = userId;
        }

        @Override
        protected String doInBackground(Void... params) {
            String urlStr = "http://bijiniu.com/tomcat/bijiniu/user/";
            Request request = new Request.Builder().url(urlStr + userId).build();
            Response response;
            String result = null;
            try {
                response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    result = response.body().string();
                }
                else {
                    throw new IOException("Unexpected code " + response);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(ProfileEditActivity.this, "似乎出了点问题", Toast.LENGTH_SHORT).show();
            }
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            userInfoTask = null;
            Gson gson = new Gson();
            UserVo userVo = gson.fromJson(result, UserVo.class);

            String nickname = userVo.getNickName();
            String headUrl = userVo.getHeadUrl();
            String school = userVo.getSchool();
            int schoolId;
            int facultyId;
            String faculty = userVo.getFaculty();//院系
            String academic = userVo.getAcademic();//学位
            String academiclevel = userVo.getAcademiclevel();//入学年份
            int fans = userVo.getFans();// 粉丝数量
            int downloadNumber = userVo.getDownloadNumber();// 下载量
            int viewNumber = userVo.getViewNumber();// 浏览量
            int materials = userVo.getMaterials();// 资料数量
            int stars = userVo.getStars();// 关注数量
            double noteScore = userVo.getNoteScore();// 文档得分

            /*txtNickname.setText(nickname);
            txtEducation.setText(school + " " + faculty + " " + academic);
            txtMaterials.setText(materials + "");
            txtViewNumber.setText(fans + "");
            txtDownloadNumber.setText(stars + "");
            txtNoteScore.setText(noteScore + "");*/

            getImgHead = new ImgLoadTask(headUrl, imgUserHead);
            getImgHead.execute();
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ModifyHeadTask extends AsyncTask<Void, Void, String> {
        private final String BASE_URL = "http://bijiniu.com/tomcat/bijiniu";

        private final String userId;
        private final String userKey;
        private final String imgPath;
        private final OkHttpClient client = new OkHttpClient();

        ModifyHeadTask(String userId, String userKey, String imgPath) {
            this.userId = userId;
            this.userKey = userKey;
            this.imgPath = imgPath;
        }

        @Override
        protected String doInBackground(Void... params) {
            String urlStr = "http://bijiniu.com/tomcat/bijiniu/user/";
            /*Request request = new Request.Builder().url(urlStr + userId).build();
            Request request = new Request.Builder()
                    .url("https://en.wikipedia.org/w/index.php")
                    .post(formBody)
                    .build();*/
            /*FileInputStream inputStream = null;
            File file = new File(imgPath);
            final MediaType MEDIA_TYPE_IMG = MediaType.parse("image/png");
            Request request = new Request.Builder()
                    .header("key", userKey)
                    .url("http://bijiniu.com/tomcat/bijiniu/user/" + userId + "/modifyHead")
                    .post(RequestBody.create(MEDIA_TYPE_IMG, file))
                    .build();*/
            File file = new File(imgPath);
            final MediaType MEDIA_TYPE_IMG = MediaType.parse("image/png");
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"file\"; filename=\"" + "mikasa.png" + "\""),
                            RequestBody.create(MEDIA_TYPE_IMG, file))
                    .build();

            Request request = new Request.Builder()
                    .header("key", userKey)
                    .url("http://bijiniu.com/tomcat/bijiniu/user/" + userId + "/modifyHead")
                    .post(requestBody)
                    .build();
            Response response;
            String result = null;
            try {
                response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    result = response.body().string();
                }
                else {
                    throw new IOException("Unexpected code " + response);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(ProfileEditActivity.this, "似乎出了点问题", Toast.LENGTH_SHORT).show();
            }
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            /*userInfoTask = null;
            Gson gson = new Gson();
            UserVo userVo = gson.fromJson(result, UserVo.class);

            String nickname = userVo.getNickName();
            String headUrl = userVo.getHeadUrl();
            String school = userVo.getSchool();
            int schoolId;
            int facultyId;
            String faculty = userVo.getFaculty();//院系
            String academic = userVo.getAcademic();//学位
            String academiclevel = userVo.getAcademiclevel();//入学年份
            int fans = userVo.getFans();// 粉丝数量
            int downloadNumber = userVo.getDownloadNumber();// 下载量
            int viewNumber = userVo.getViewNumber();// 浏览量
            int materials = userVo.getMaterials();// 资料数量
            int stars = userVo.getStars();// 关注数量
            double noteScore = userVo.getNoteScore();// 文档得分

            *//*txtNickname.setText(nickname);
            txtEducation.setText(school + " " + faculty + " " + academic);
            txtMaterials.setText(materials + "");
            txtViewNumber.setText(fans + "");
            txtDownloadNumber.setText(stars + "");
            txtNoteScore.setText(noteScore + "");*//*

            getImgHead = new ImgLoadTask(headUrl, imgUserHead);
            getImgHead.execute();*/
            Log.v(TAG, result);
        }
    }


    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
