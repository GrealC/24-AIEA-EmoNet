package com.example.emoji.moudle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emoji.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/*
*    结果页
*   1. 存放服务器传回来的结果
*   2. 显示图片
*   3. 按钮点击退出
* */
public class ResultActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);

        Button backBtn = findViewById(R.id.back_btn_6);
        backBtn.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) { // activity与fragment之间传递数据,图片数据太大，故传递路径
            ImageView imageView = findViewById(R.id.result_image);
            String filePath = intent.getStringExtra("filePath");
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            imageView.setImageBitmap(bitmap);

        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.back_btn_6) {
            this.finish();
        }
    }
}