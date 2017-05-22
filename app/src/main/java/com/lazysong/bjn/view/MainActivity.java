package com.lazysong.bjn.view;

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.lazysong.bjn.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener,
        HomeFragment.OnFragmentInteractionListener, UploadFragment.OnFragmentInteractionListener, AccountFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener {
    private BottomNavigationBar bottomNavigationBar;
    private android.support.v4.app.Fragment currentFragment;
    private List<Fragment> fragmentList;
    private boolean isLogin;
    private String userId;
    private String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(0, new HomeFragment());
        fragmentList.add(1, new UploadFragment());
        checkLoginState();
        if (isLogin) {
            fragmentList.add(2, AccountFragment.newInstance(true, userId, userKey));
        }
        else {
            fragmentList.add(2, LoginFragment.newInstance(null, null));
        }
    }

    private void checkLoginState() {
        // 从本地 sp 文件中读取登录状态
        SharedPreferences sp = this.getSharedPreferences("loginpref", android.app.Activity.MODE_PRIVATE);
        isLogin = sp.getBoolean("logined", false);
        userId =sp.getString("userId", "unknown");
        userKey = sp.getString("userKey", "-1");
        sp = null;
    }

    private void initViews() {
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_explore_black_24dp, "发现"))
                .addItem(new BottomNavigationItem(R.drawable.ic_cloud_upload_black_24dp, "上传"))
                .addItem(new BottomNavigationItem(R.drawable.ic_perm_identity_black_24dp, "我的"))
                .setActiveColor(R.color.colorPrimary)
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(int position) {
        if (position == 2) {
            checkLoginState();
            fragmentList.remove(2);
            if (isLogin) {
                fragmentList.add(2, AccountFragment.newInstance(true, userId, userKey));
            }
            else {
                fragmentList.add(2, LoginFragment.newInstance(null, null));
            }
        }
        if(findViewById(R.id.main_container) != null) {
            currentFragment = fragmentList.get(position);
            /*Bundle args = new Bundle();
            args.putInt("position", position);
            currentFragment.setArguments(args);*/
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_container, currentFragment).commit();
        }
    }

    @Override
    public void onTabUnselected(int position) {
        currentFragment = fragmentList.get(position);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(currentFragment).commit();
    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void changeToLoginedFragment(boolean isLogin, String userId, String userKey) {
        if (isLogin) {
            fragmentList.remove(2);
            fragmentList.add(AccountFragment.newInstance(isLogin, userId, userKey));
        }
        currentFragment = fragmentList.get(2);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, currentFragment).commit();
        transaction = null;
    }
}
