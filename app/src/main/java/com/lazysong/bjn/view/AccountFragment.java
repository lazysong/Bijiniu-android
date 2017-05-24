package com.lazysong.bjn.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lazysong.bjn.R;
import com.lazysong.bjn.utils.BitmapProcesser;
import com.lazysong.bjn.utils.ImgLoadTask;
import com.lazysong.bjn.vo.UserVo;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String IS_LOGIN = "isLogin";
    private static final String USER_ID = "userId";
    private static final String USER_KEY = "userKey";

    // TODO: Rename and change types of parameters
    private boolean isLogin;
    private String userId;
    private String userKey;

    private OnFragmentInteractionListener mListener;

    private ImageView imgHead;
    private TextView txtNickname;
    private TextView txtEducation;
    private ListView listviewAccount;
    private ListView listviewSetting;
    private LinearLayout layoutMaterials;
    private LinearLayout layoutViewNumber;
    private LinearLayout layoutDownlaodNumber;
    private LinearLayout layoutNoteScore;
    private TextView txtMaterials;
    private TextView txtViewNumber;
    private TextView txtDownloadNumber;
    private TextView txtNoteScore;

    private Typeface iconfont;

    private UserInfoTask infoTask;
    private ImgLoadTask headTask;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param isLogin
     * @param userId unique user id
     * @param userKey token to request server
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(boolean isLogin, String userId, String userKey) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_LOGIN, isLogin);
        args.putString(USER_ID, userId);
        args.putString(USER_KEY, userKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isLogin = getArguments().getBoolean(IS_LOGIN);
            userId = getArguments().getString(USER_ID);
            userKey = getArguments().getString(USER_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        iconfont = Typeface.createFromAsset(getContext().getAssets(), "iconfont/MaterialIcons-Regular.ttf");
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        TextView textview = (TextView)(view.findViewById(R.id.medal_account));
        textview.setTypeface(iconfont);
        TextView tvUniversity = (TextView) view.findViewById(R.id.tv_account_university);
        tvUniversity.setTypeface(iconfont);

        initListView(view);
        infoTask = new UserInfoTask(userId);
        infoTask.execute();
        return view;
    }

    private void initListView(View view) {
        listviewAccount = (ListView) view.findViewById(R.id.listview_account);
        imgHead = (ImageView) view.findViewById(R.id.imgUserHead);
        txtNickname = (TextView) view.findViewById(R.id.txtNickname);
        txtEducation = (TextView) view.findViewById(R.id.txtEducation);
        layoutMaterials = (LinearLayout) view.findViewById(R.id.linearMaterials);
        layoutViewNumber = (LinearLayout) view.findViewById(R.id.linearViewNumber);
        layoutDownlaodNumber = (LinearLayout) view.findViewById(R.id.linearDownloadNumber);
        layoutNoteScore = (LinearLayout) view.findViewById(R.id.linearNoteScore);
        txtMaterials = (TextView) view.findViewById(R.id.txtMaterials);
        txtViewNumber = (TextView) view.findViewById(R.id.txtViewNumber);
        txtDownloadNumber = (TextView) view.findViewById(R.id.txtDownloadNumber);
        txtNoteScore = (TextView) view.findViewById(R.id.txtNoteScore);

        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(
                getContext(),
                R.layout.listview_item,
                R.id.tv_title_listview_item,
                getContext().getResources().getStringArray(R.array.listview_account));
        listviewAccount.setAdapter(nameAdapter);
        listviewAccount.setOnItemClickListener(this);
        listviewSetting = (ListView) view.findViewById(R.id.listview_setting);
        listviewSetting.setAdapter(new ArrayAdapter<String>(
                getContext(),
                R.layout.listview_item,
                R.id.tv_title_listview_item,
                getContext().getResources().getStringArray(R.array.listview_setting)
        ));
        listviewSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("userId", userId);
                intent.putExtra("userKey", userKey);
                switch(position) {
                    case 0:
                        intent.setClass(getContext(), ProfileEditActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(getContext(), FeedbackActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(getContext(), AboutActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("userId", userId);
        intent.putExtra("userKey", userKey);
        switch(position) {
            case 0:
                intent.setClass(getContext(), RepositoryActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent.setClass(getContext(), StarActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent.setClass(getContext(), FansActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent.setClass(getContext(), BuyinfoActivity.class);
                startActivity(intent);
                break;
            case 5:
                intent.setClass(getContext(), RewardActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                Toast.makeText(getContext(), "似乎出了点问题", Toast.LENGTH_SHORT).show();
            }
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            infoTask = null;
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

            txtNickname.setText(nickname);
            txtEducation.setText(school + " " + faculty + " " + academic);
            txtMaterials.setText(materials + "");
            txtViewNumber.setText(fans + "");
            txtDownloadNumber.setText(stars + "");
            txtNoteScore.setText(noteScore + "");

            headTask = new ImgLoadTask(headUrl, imgHead);
            headTask.execute();
        }
    }

}
