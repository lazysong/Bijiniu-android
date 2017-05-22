package com.lazysong.bjn.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lazysong.bjn.R;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnCheckLoginState;
    private TextView txtResult;
    private Button btnToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_login);

        btnCheckLoginState = (Button) findViewById(R.id.btnCheckLoginState);
        btnCheckLoginState.setOnClickListener(this);
        txtResult = (TextView) findViewById(R.id.txtResult);
        btnToLogin = (Button) findViewById(R.id.btnToLogin);
        btnToLogin.setVisibility(View.INVISIBLE);
        btnToLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnCheckLoginState:
                // 从本地 sp 文件中读取登录状态
                SharedPreferences sp = this.getSharedPreferences("loginpref", android.app.Activity.MODE_PRIVATE);
                boolean logined = sp.getBoolean("logined", false);
                String userId =sp.getString("userId", "unknown");
                String userKey = sp.getString("userKey", "-1");
                if (logined) {
                    txtResult.setText("用户已登录\n userId = " + userId + "\n userKey = " + userKey);
                }
                else {
                    txtResult.setText("用户尚未登录，请登录");
                    btnToLogin.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btnToLogin:
                Intent intent = new Intent();
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
                break;
        }

    }


}
