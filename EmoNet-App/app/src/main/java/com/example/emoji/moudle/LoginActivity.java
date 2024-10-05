package com.example.emoji.moudle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.emoji.MainActivity;
import com.example.emoji.R;
import com.example.emoji.dao.UserDao;
import com.example.emoji.model.User;
import com.example.emoji.receiver.InternetReceiver;

/*
*   登录界面，实现用户登录功能与，并且含有用户协议碎片
*   1. 用户进入登录界面
*   2. 输入用户名密码
*   3. 点击用户协议单选框，阅读并点击确认用户协议
*   4. 点击登录按钮，登录成功
* */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private boolean isNetworkConnected = false;
    private InternetReceiver internetReceiver;
    // 进行用户相关的操作
    private UserDao userDao = new UserDao();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        MainActivity.activityList.add(this);

        // 退出app按钮 -1
        Button back_btn_1 = findViewById(R.id.back_btn_1);
        back_btn_1.setOnClickListener(this);

        //用户协议按钮 -1
        RadioButton privacy_btn_1 = findViewById(R.id.privacy_btn_1);
        privacy_btn_1.setOnClickListener(this);

        // 登录按钮
        Button login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);

        // 注册按钮
        Button register_btn = findViewById(R.id.register_btn_1);
        register_btn.setOnClickListener(this);

        // 动态注册网络判断广播
        internetReceiver = new InternetReceiver();
        IntentFilter intentFilter = new IntentFilter();
        registerReceiver(internetReceiver, intentFilter);
    }


    @Override
    public void onClick(View v) {
        // 获取控件id，以判断用户操作
        int id = v.getId();

        if (id == R.id.back_btn_1) { // 退出app
            back();
        } else if (id == R.id.privacy_btn_1) { // 用户协议
            PromptFragment promptFragment = new PromptFragment();
            promptFragment.show(getSupportFragmentManager(), "prompt");
        } else if (id == R.id.login_btn) { // 登录

            //登录的时候，判断网络是否连接
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            isNetworkConnected = networkInfo != null && networkInfo.isConnected();

            if (!isNetworkConnected) {
                Toast.makeText(this, "网络未连接，请检查您的网络连接", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread(new Runnable() {
                public void run() {

                    TextView username = findViewById(R.id.editTextPhone);
                    TextView password = findViewById(R.id.editTextTextPassword);
                    RadioButton privacy_btn_1 = findViewById(R.id.privacy_btn_1);

                    String username_str = username.getText().toString();
                    String password_str = password.getText().toString();

                    User user = null;
                    try {
                        user = userDao.getUserByUsername(username_str);
                    }catch (Exception e) {
                        Log.w("Login","Internet");
                    }

                    if (!privacy_btn_1.isChecked()) { // 未勾选用户协议
                        Looper.prepare();
                        Toast.makeText(LoginActivity.this, "请阅读并同意用户协议", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else if (user == null){
                        Looper.prepare(); // 弹出提示，在子线程中需要使用Looper才能使用Toast
                        Toast.makeText(LoginActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else if (username_str.isEmpty() || password_str.isEmpty()) {
                        Looper.prepare(); // 弹出提示，在子线程中需要使用Looper才能使用Toast
                        Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    else {
                        if (password_str.equals(user.getPassword())) { // 密码正确

                            // 将登录信息保存在user.xml中
                            Context context = LoginActivity.this;
                            SharedPreferences sp = context.getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();

                            editor.putString("username", user.getUsername());
                            editor.putString("phone", user.getPhone());
                            editor.putInt("identity", user.getIdentity());
                            editor.putInt("uid", user.getUid());
                            editor.apply();

                            // Test file
                            String username_t = sp.getString("username", "default_usernmae");
                            String phone_t = sp.getString("phone", "default_phone");
                            Log.d("Login", username_t);
                            Log.w("Login", phone_t);
                            //登录成功跳转到主界面
                            final Handler handler = new Handler(Looper.getMainLooper()); //获取当前主线程的消息队列
                            handler.post(new Runnable() { //
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, UseActivity.class);
                                    startActivity(intent);
                                }
                            });

                        } else { // 密码错误等其他问题
                            Looper.prepare(); // 弹出提示，在子线程中需要使用Looper才能使用Toast
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                }
            }).start();
        } else if (id == R.id.register_btn_1) { // 注册界面跳转
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    // 退出app
    private void back() {
        for (Activity act: MainActivity.activityList) {
            act.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销BroadcastReceiver
        if (internetReceiver != null) {
            unregisterReceiver(internetReceiver);
        }
    }
}