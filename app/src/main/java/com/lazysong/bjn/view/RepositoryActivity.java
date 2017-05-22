package com.lazysong.bjn.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.lazysong.bjn.R;

public class RepositoryActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);

        toolbar = (Toolbar) findViewById(R.id.toolbar_base);
        toolbarTitle = (TextView) findViewById(R.id.titleToolbar);
        toolbarTitle.setText("我的仓库");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RepositoryActivity.this.finish();
            }
        });
    }
}
