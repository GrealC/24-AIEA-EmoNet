package com.example.emoji.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.emoji.R;
import com.example.emoji.model.Picture;

import java.util.List;

/*
*    图片适配器
*   1. 展示用户个人识别的面部图片
*   2. 隐私数据本地数据库保存
* */
public class PictureAdapter extends BaseAdapter{

    private Context context;
    private List<Picture> pictures;
    private LayoutInflater inflater;

    public PictureAdapter(Context context, List<Picture> pictures) {
        this.context = context;
        this.pictures = pictures;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return pictures.size();
    }

    @Override
    public Object getItem(int position) {
        return pictures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.activity_image, parent, false);

        ImageView imageView = convertView.findViewById(R.id.image_album);
        ImageButton deleteButton = convertView.findViewById(R.id.delete_btn_1);

        Picture picture = pictures.get(position);
        Bitmap bitmap = BitmapFactory.decodeFile(picture.getPath());
        imageView.setImageBitmap(bitmap);

        deleteButton.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(position);
            }
        });

        return convertView;
    }

    private OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public void updatePictures(List<Picture> pictures)
    {
        this.pictures = pictures;
        notifyDataSetChanged();
    }
}
