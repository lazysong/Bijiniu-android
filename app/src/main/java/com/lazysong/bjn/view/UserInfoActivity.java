package com.lazysong.bjn.view;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lazysong.bjn.R;
import com.lazysong.bjn.module.PreferenceUtils;
import com.lazysong.bjn.vo.Result;
import com.lazysong.bjn.vo.UserVo;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnGetUser;
    private TextView txtUserInfo;
    private ImageView imgHead;
    private UserInfoTask userInfotask;
    private UserHeadTask userHeadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        btnGetUser = (Button) findViewById(R.id.btnGetUser);
        txtUserInfo = (TextView) findViewById(R.id.txtUserInfo);
        imgHead = (ImageView) findViewById(R.id.imgHead);
        btnGetUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnGetUser:
                // 从本地 sp 文件中读取登录状态
                SharedPreferences sp = UserInfoActivity.this.getSharedPreferences("loginpref", android.app.Activity.MODE_PRIVATE);
                boolean logined = sp.getBoolean("logined", false);
                if (!logined) {
                    txtUserInfo.setText("用户尚未登录，请登录");
                    return;

                }
                String userId =sp.getString("userId", "unknown");
                userInfotask = new UserInfoTask(userId);
                userInfotask.execute();
                break;
        }
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
//            OkHttpClient client = new OkHttpClient();
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
            }
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            userInfotask = null;
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

            String userInfoStr = "";
            userInfoStr += "昵称：" + nickname;
            userInfoStr += "\n学校：" + school;
            userInfoStr += "\n院系：" + faculty;
            userInfoStr += "\n学位：" + academic;
            userInfoStr += "\n入学年份：" + academiclevel;
            userInfoStr += "\n粉丝数量：" + fans;
            userInfoStr += "\n下载量：" + downloadNumber;
            userInfoStr += "\n浏览量：" + viewNumber;
            userInfoStr += "\n资料数量：" + materials;
            userInfoStr += "\n关注数量：" + stars;
            userInfoStr += "\n文档得分：" + noteScore;
            txtUserInfo.setText(userInfoStr);

            userHeadTask = new UserHeadTask(headUrl);
            userHeadTask.execute();
        }

    }

    public class UserHeadTask extends AsyncTask<Void, Void, Bitmap> {
        private final String BASE_URL = "http://bijiniu.com/tomcat/bijiniu";
        private final String headUrl;
        private final OkHttpClient client = new OkHttpClient();

        UserHeadTask(String headUrl) {
            this.headUrl = headUrl;
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
            userHeadTask = null;
            imgHead.setImageBitmap(bitmap);
        }
    }

}
