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
import com.lazysong.bjn.vo.RecordVo;
import com.lazysong.bjn.vo.UserVo;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

public class BuyinfoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtTitle;
    private RecyclerView recyclerViewBuyinfoCombo;

    private BuyInfoTask buyinfoTask;
    private Page<RecordVo> recordVoPage;
    private List<RecordVo> recordVos;

    private String userId;
    private String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyinfo);

        txtTitle = (TextView) findViewById(R.id.titleToolbar);
        txtTitle.setText("我的购买");

        toolbar = (Toolbar) findViewById(R.id.toolbar_base);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);//设置导航栏图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyinfoActivity.this.finish();
            }
        });
        recyclerViewBuyinfoCombo = (RecyclerView) findViewById(R.id.recycleview_buyinfo_combo);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userKey = intent.getStringExtra("userKey");
        buyinfoTask = new BuyInfoTask(userId, "1", userKey);
        buyinfoTask.execute();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class BuyInfoTask extends AsyncTask<Void, Void, String> {
        private final String BASE_URL = "http://bijiniu.com/tomcat/bijiniu";

        private final String userId;
        private final String pageId;
        private final String userKey;
        private final OkHttpClient client = new OkHttpClient();

        BuyInfoTask(String userId, String pageId, String userKey) {
            this.userId = userId;
            this.pageId = pageId;
            this.userKey = userKey;
        }

        @Override
        protected String doInBackground(Void... params) {
            String urlStr = BASE_URL + "/money/" + userId + "/1";
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
            buyinfoTask = null;
            Gson gson = new Gson();
            recordVoPage = gson.fromJson(result, new TypeToken<Page<RecordVo>>(){}.getType());
            recordVos = recordVoPage.getResult();
            recyclerViewBuyinfoCombo.setAdapter(new UserInfoAdapter(BuyinfoActivity.this));
            recyclerViewBuyinfoCombo.setLayoutManager(new LinearLayoutManager(BuyinfoActivity.this));
        }
    }

    class ViewCache extends RecyclerView.ViewHolder {
        public TextView txtNameBuyinfoCombo;
        public TextView txtPriceBuyinfoComdbo;
        public TextView txtStartBuyinfoCombo;
        public TextView txtStopBuyinfoCombo;

        public ViewCache(View itemView) {
            super(itemView);
            txtNameBuyinfoCombo = (TextView) itemView.findViewById(R.id.txtNameBuyinfoCombo);
            txtPriceBuyinfoComdbo = (TextView) itemView.findViewById(R.id.txtPriceBuyinfoCombo);
            txtStartBuyinfoCombo = (TextView) itemView.findViewById(R.id.txtStartBuyinfoCombo);
            txtStopBuyinfoCombo = (TextView) itemView.findViewById(R.id.txtStopBuyinfoCombo);
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
            View itemView = inflater.inflate(R.layout.listview_item_buyinfo_combo, parent, false);
            ViewCache viewCache = new ViewCache(itemView);
            return viewCache;
        }

        @Override
        public void onBindViewHolder(final ViewCache holder, int position) {
            final RecordVo recordVo = recordVos.get(position);
            holder.txtNameBuyinfoCombo.setText(recordVo.getDescrption());
            holder.txtPriceBuyinfoComdbo.setText(recordVo.getMoney() + "");
            holder.txtStartBuyinfoCombo.setText(recordVo.getCreateTime() + "");
            holder.txtStopBuyinfoCombo.setText(recordVo.getDueTime() + "");
        }

        @Override
        public int getItemCount() {
            return recordVos.size();
        }
    }
}
