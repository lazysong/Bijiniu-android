package com.lazysong.bjn.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lazysong.bjn.R;
import com.lazysong.bjn.vo.Page;
import com.lazysong.bjn.vo.RecordVo;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class RewardActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtTitle;
    private TextView txtWithdrawReward;
    private Button btnWidthraw;

    private String userId;
    private String userKey;

    private double reward;

    private GetRewardTask getRewardTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        txtTitle = (TextView) findViewById(R.id.titleToolbar);
        txtTitle.setText("我的奖励");
        txtWithdrawReward = (TextView) findViewById(R.id.txtWithdrawReward);
        btnWidthraw = (Button) findViewById(R.id.btnWidthraw);
        btnWidthraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提现至微信
                Toast.makeText(RewardActivity.this, "提现成功", Toast.LENGTH_SHORT).show();
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar_base);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);//设置导航栏图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewardActivity.this.finish();
            }
        });

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userKey = intent.getStringExtra("userKey");
        getRewardTask = new GetRewardTask(userId, userKey);
        getRewardTask.execute();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class GetRewardTask extends AsyncTask<Void, Void, String> {
        private final String BASE_URL = "http://bijiniu.com/tomcat/bijiniu";

        private final String userId;
        private final String userKey;
        private final OkHttpClient client = new OkHttpClient();

        GetRewardTask(String userId, String userKey) {
            this.userId = userId;
            this.userKey = userKey;
        }

        @Override
        protected String doInBackground(Void... params) {
            String urlStr = BASE_URL + "/money/getWithdraw/" + userId;
            Request request = new Request.Builder()
                    .header("key", userKey)
                    .url(urlStr + userId).build();
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
            getRewardTask = null;
            /*Gson gson = new Gson();
            reward = gson.fromJson(result, Integer.class);*/
            reward = Double.parseDouble(result);
            txtWithdrawReward.setText(reward + "");
        }
    }
}
