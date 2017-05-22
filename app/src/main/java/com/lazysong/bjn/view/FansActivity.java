package com.lazysong.bjn.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lazysong.bjn.R;
import com.lazysong.bjn.utils.ImgLoadTask;
import com.lazysong.bjn.vo.Page;
import com.lazysong.bjn.vo.UserVo;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

public class FansActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtTitle;
    private TextView txtFansInfo;
    private RecyclerView recyclerViewStars;

    private MyStarTask fansTask;
    private ImgLoadTask headTask;
    private Page<UserVo> userVoPage;
    private List<UserVo> userVos;

    private String userId;
    private String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);

        txtTitle = (TextView) findViewById(R.id.titleToolbar);
        txtTitle.setText("我的粉丝");
        txtFansInfo = (TextView) findViewById(R.id.txtFansInfo);

        toolbar = (Toolbar) findViewById(R.id.toolbar_base);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);//设置导航栏图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FansActivity.this.finish();
            }
        });
        recyclerViewStars = (RecyclerView) findViewById(R.id.recycleview_fans);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userKey = intent.getStringExtra("userKey");
        fansTask = new MyStarTask(userId, "1", userKey);
        fansTask.execute();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class MyStarTask extends AsyncTask<Void, Void, String> {
        private final String BASE_URL = "http://bijiniu.com/tomcat/bijiniu";

        private final String userId;
        private final String pageId;
        private final String userKey;
        private final OkHttpClient client = new OkHttpClient();

        MyStarTask(String userId, String pageId, String userKey) {
            this.userId = userId;
            this.pageId = pageId;
            this.userKey = userKey;
        }

        @Override
        protected String doInBackground(Void... params) {
            String urlStr = "http://bijiniu.com/tomcat/bijiniu/user/" + userId + "/followers?page=" + pageId;
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
            fansTask = null;
            Gson gson = new Gson();
            userVoPage = gson.fromJson(result, new TypeToken<Page<UserVo>>(){}.getType());
            userVos = userVoPage.getResult();
            recyclerViewStars.setAdapter(new UserInfoAdapter(FansActivity.this));
            recyclerViewStars.setLayoutManager(new LinearLayoutManager(FansActivity.this));
        }
    }

    class ViewCache extends RecyclerView.ViewHolder {
        public ImageView imgUserHead;
        public TextView txtNickname;
        public TextView txtStars;
        public TextView txtFans;
        public TextView txtMaterials;
        public Button btnUnstar;

        public ViewCache(View itemView) {
            super(itemView);
            imgUserHead = (ImageView) itemView.findViewById(R.id.imgHeadStar);
            txtNickname = (TextView) itemView.findViewById(R.id.txtNicknameStar);
            txtStars = (TextView) itemView.findViewById(R.id.txtStarsStar);
            txtFans = (TextView) itemView.findViewById(R.id.txtFansStar);
            txtMaterials = (TextView) itemView.findViewById(R.id.txtMaterialsStar);
            btnUnstar = (Button) itemView.findViewById(R.id.btnUnstar);
        }
    }
    class UserInfoAdapter extends RecyclerView.Adapter<ViewCache> {
        private Context context;
        public UserInfoAdapter(Context context) {
            this.context = context;
        }

        @Override
        public ViewCache onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.listview_item_userinfo, parent, false);
            ViewCache viewCache = new ViewCache(itemView);
            return viewCache;
        }

        @Override
        public void onBindViewHolder(final ViewCache holder, int position) {
            final UserVo userVo = userVos.get(position);
            holder.txtNickname.setText(userVo.getNickName());
            holder.txtFans.setText(userVo.getFans() + "");
            holder.txtStars.setText(userVo.getStars() + "");
            holder.txtMaterials.setText(userVo.getMaterials() + "");
            final Button btnUnstarAndStar = holder.btnUnstar;
            btnUnstarAndStar.setTag(R.id.tag_watched, true);
            btnUnstarAndStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((boolean)(btnUnstarAndStar.getTag(R.id.tag_watched))) {
                        //取消关注某用户
                        unstar();
                        btnUnstarAndStar.setTag(R.id.tag_watched, false);
                        btnUnstarAndStar.setText("关注");
                    }
                    else {
                        star();
                        btnUnstarAndStar.setTag(R.id.tag_watched, true);
                        btnUnstarAndStar.setText("取消关注");
                    }
                }
            });
            //设置用户头像
            headTask = new ImgLoadTask(userVo.getHeadUrl(), holder.imgUserHead);
            headTask.execute();
        }

        @Override
        public int getItemCount() {
            return userVos.size();
        }
    }

    private void star() {
    }

    private void unstar() {

    }
}
