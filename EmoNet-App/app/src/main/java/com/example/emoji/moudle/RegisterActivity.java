package com.example.emoji.moudle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.emoji.MainActivity;
import com.example.emoji.R;
import com.example.emoji.dao.UserDao;
import com.example.emoji.model.User;
/*
*   注册界面
*
* */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private UserDao userDao = new UserDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        MainActivity.activityList.add(this);

        // 返回按钮
        Button back_button = findViewById(R.id.back_btn_2);
        back_button.setOnClickListener(this);
        // 注册按钮
        Button register_button = findViewById(R.id.register_btn_2);
        register_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.register_btn_2) { // 完成用户注册
            // 获取输入框中的内容
            Log.w("RegisterActivity", "run");
            TextView username = findViewById(R.id.register_username);
            TextView password = findViewById(R.id.register_password);
            TextView phone = findViewById(R.id.register_phone);
            String username_str = username.getText().toString();
            String password_str = password.getText().toString();
            String phone_str = phone.getText().toString();

            // 校验输入
            if (!validateInput(username_str, password_str, phone_str)) {
                return;
            }

            new Thread(new Runnable() {
                public void run() {
                    User user = new User(username_str, password_str, phone_str, 1);
                    userDao.addUser(user);

                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            // 在主线程中启动新的Activity
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }).start();
        } else if (id == R.id.back_btn_2) { // 返回注册界面
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    // 校验输入的方法
    private boolean validateInput(String username, String password, String phone) {
        // 完整性检查
        if (username.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (username.length() < 6 || username.length() > 14) {
            Toast.makeText(this, "账号必须在6-14位之间", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phone.length() != 11) {
            Toast.makeText(this, "手机号必须为11位", Toast.LENGTH_SHORT).show();
            return false;
        }



        if (password.length() < 6 || password.length() > 14) {
            Toast.makeText(this, "密码必须在6-14位之间", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
