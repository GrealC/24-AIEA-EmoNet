package com.example.emoji.moudle;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emoji.R;


/*
*   隐私政策界面
* */
public class PrivacyActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_privacy);

        // 返回按钮
        Button back = findViewById(R.id.back_btn_4);
        back.setOnClickListener(this);

        TextView content = findViewById(R.id.privacy_text_2);
        content.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if ( id == R.id.back_btn_4) {
            this.finish();
        }
    }
}