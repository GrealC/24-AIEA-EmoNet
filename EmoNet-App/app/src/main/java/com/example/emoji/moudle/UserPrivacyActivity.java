package com.example.emoji.moudle;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.emoji.R;

/*
*   用户协议界面:
*   1. 用户可以查看相关的用户协议
*   2. 点击返回按钮退出
* */
public class UserPrivacyActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_privacy);

        // 返回按钮
        Button back = findViewById(R.id.back_btn_5);
        back.setOnClickListener(this);

        TextView content = findViewById(R.id.privacy_text_3);
        content.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if ( id == R.id.back_btn_5) {
            this.finish();
        }
    }
}