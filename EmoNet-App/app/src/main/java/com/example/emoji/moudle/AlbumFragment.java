package com.example.emoji.moudle;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.example.emoji.R;
import com.example.emoji.adapter.PictureAdapter;
import com.example.emoji.dao.PictureDao;
import com.example.emoji.model.Picture;
import com.example.emoji.receiver.AddReceiver;
import com.example.emoji.receiver.DeleteReceiver;
import com.example.emoji.service.DeletePictureService;

import java.util.List;

/*
*  表情识别库
*   1. 记录用户图片信息
* */
public class AlbumFragment extends Fragment{

    private ListView listView;
    private PictureDao pictureDAO;
    private List<Picture> pictures;
    private PictureAdapter adapter;
    private DeleteReceiver deleteReceiver;
    private AddReceiver addReceiver;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        listView = view.findViewById(R.id.image_list);

        // 获取uid
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        int uid = sharedPreferences.getInt("uid", -1);

        // 初始化PictureDAO
        pictureDAO = new PictureDao(requireContext());

        // 获取用户图片信息
        pictures = pictureDAO.getPicturesByUid(uid);

        // 设置适配器
        adapter = new PictureAdapter(requireContext(), pictures);
        listView.setAdapter(adapter);

        // 设置删除按钮点击事件
        adapter.setOnDeleteClickListener(position -> {
            Picture picture = pictures.get(position);
            Intent intent = new Intent(requireContext(), DeletePictureService.class);
            intent.putExtra("pictureId", picture.getId()); // 传递图片id
            requireContext().startService(intent);
        });

        // 注册广播接收器
        deleteReceiver = new DeleteReceiver(pictures, adapter);
        IntentFilter filter = new IntentFilter(DeletePictureService.ACTION_DELETE_COMPLETED);
        getActivity().registerReceiver(deleteReceiver, filter, Context.RECEIVER_NOT_EXPORTED);

        addReceiver = new AddReceiver(uid, adapter);
        IntentFilter addFilter = new IntentFilter(AddReceiver.ACTION_ADD_COMPLETED);
        getActivity().registerReceiver(addReceiver, addFilter, Context.RECEIVER_NOT_EXPORTED);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pictureDAO != null) {
            pictureDAO.close();
        }
        if (deleteReceiver != null) {
            requireContext().unregisterReceiver(deleteReceiver);
        }
    }

}
