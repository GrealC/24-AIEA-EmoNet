package com.example.emoji;

import static java.lang.Thread.sleep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emoji.moudle.LoginActivity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/*
*  项目启动页，启动项目由此进入
* */
public class MainActivity extends AppCompatActivity {

    // 存放所有的Activity，当点击退出按钮的时候,finish所有Activity
    public static List<Activity> activityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 将当前Activity加入到集合中
        activityList.add(this);
        // 使用计数器进行跳转, 进入程序后首先进入开屏界面，2秒后跳转首页
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        };

        timer.schedule(task, 2000);

    }
}