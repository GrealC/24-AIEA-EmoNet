package com.example.emoji.moudle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.emoji.R;
import com.example.emoji.dao.PictureDao;
import com.example.emoji.model.Picture;
import com.example.emoji.receiver.AddReceiver;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
*    拍照识别
*   1. 拍照
*   2. 上传
*   3. 识别
*   4. 保存
*   5. 广播
* */
public class RecognitionFragment extends Fragment implements View.OnClickListener{

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PERMISSION = 100;
    private String currentPhotoPath;
    private PictureDao pictureDAO ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recognition, container, false);

        //拍照识别按钮
        ImageButton photo_btn = view.findViewById(R.id.photo_btn);
        photo_btn.setOnClickListener(this);

        pictureDAO = new PictureDao(getContext());

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if ( id == R.id.photo_btn) { // 点击进行拍照识别
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { // 权限检查
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            } else {
                dispatchTakePicture(); // 拍照
            }
        }
    }

    private void dispatchTakePicture() {
        //拍照识别
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(), "com.example.android.fileprovider1", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // 创建图片文件
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // 拍照后上传图片
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            File file = new File(currentPhotoPath);
            if (file.exists()) {
                uploadImage(file);
            }
        }
    }

    // 上传图片与服务器交互
    private void uploadImage(File file) {
        OkHttpClient client = new OkHttpClient();
        // 构建请求体
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("image/jpeg"), file))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.215.60:5001/upload")
                .post(requestBody)
                .build();

        // 异步请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    requireActivity().runOnUiThread(() -> {
                        Log.w("上传成功", bitmap.toString());
                        String filePath = saveBitmapToFile(bitmap);

                        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                        int uid = sharedPreferences.getInt("uid", -1);

                        // 获取文件名
                        String fileName = new File(filePath).getName();

                        // 创建Picture对象并保存到数据库
                        Picture picture = new Picture(uid, filePath, fileName);
                        pictureDAO.insertPicture(picture);

                        Intent intent = new Intent(getContext(), ResultActivity.class);
                        intent.putExtra("filePath", filePath);
                        startActivity(intent);
                    });
                } else {
                    requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private String saveBitmapToFile(Bitmap bitmap) {
        String savedImagePath;
        String imageFileName = "JPEG_" + System.currentTimeMillis() + ".jpg";
        File storageDir = new File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/PhotoTest");

        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
            Log.w("DirCreate", "创建状态：" + success);
        }

        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();

            try (OutputStream fOut = new FileOutputStream(imageFile)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            } catch (Exception e) {
                Log.e("ImageSave", "存储失败", e);
            }
            galleryAddPic(savedImagePath);
            return savedImagePath;
        } else {
            return null;
        }
    }

    // 发送广播，通知系统图库更新, 通知个人图库更新
    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Intent intent = new Intent(AddReceiver.ACTION_ADD_COMPLETED);
        requireActivity().sendBroadcast(intent);

        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        requireActivity().sendBroadcast(mediaScanIntent);
    }
}