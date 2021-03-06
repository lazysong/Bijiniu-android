package com.lazysong.bjn.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.lazysong.bjn.R;

public class AboutActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        txtTitle = (TextView) findViewById(R.id.titleToolbar);
        txtTitle.setText("关于我们");

        toolbar = (Toolbar) findViewById(R.id.toolbar_base);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);//设置导航栏图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.this.finish();
            }
        });
    }
}
