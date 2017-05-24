package com.lazysong.bjn.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class UserManageActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private Button btnLoginCheck;
    private Button btnToLogout;
    private Button btnToUserInfo;
    private Button btnToEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);
        initView();
    }

    private void initView() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        btnLoginCheck = (Button) findViewById(R.id.btnCheckLogin);
        btnLoginCheck.setOnClickListener(this);
        btnToLogout = (Button) findViewById(R.id.btnToLogout);
        btnToLogout.setOnClickListener(this);
        btnToUserInfo = (Button) findViewById(R.id.btnToUserInfo);
        btnToUserInfo.setOnClickListener(this);
        btnToEditProfile = (Button) findViewById(R.id.btnToEditProfile);
        btnToEditProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent();
        switch (id) {
            case R.id.btnLogin:
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnCheckLogin:
                intent.setClass(this, CheckLoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnToLogout:
                PreferenceUtils.setLoginPref(this, "", "", false);
                Toast.makeText(this, "退出成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnToUserInfo:
                intent.setClass(this, UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.btnToEditProfile:
                intent.setClass(this, ProfileEditActivity.class);
                startActivity(intent);
                break;
        }
    }


}
