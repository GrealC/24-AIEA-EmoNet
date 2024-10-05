package com.example.emoji.moudle;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.emoji.R;

import java.util.Objects;

/*
*  个人中心
*
* */
public class PersonFragment extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        // 获取 SharedPreferences 中存储的数据
        SharedPreferences sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = sp.getString("username", "默认用户名");
        String phone = sp.getString("phone", "默认手机号");
        int uid = sp.getInt("uid", -1);

        // 找到布局中的 TextView 控件
        TextView usernameTextView = view.findViewById(R.id.username_value);
        TextView phoneTextView = view.findViewById(R.id.phone_value);
        TextView uidTextView = view.findViewById(R.id.uid_value);

        // 设置 TextView 的内容
        usernameTextView.setText(String.format("%s", username));
        phoneTextView.setText(String.format("%s", phone));
        uidTextView.setText(""+uid);

        view.findViewById(R.id.privacy_btn).setOnClickListener(this);
        view.findViewById(R.id.userPolicy_btn).setOnClickListener(this);
        view.findViewById(R.id.back_btn_3).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.privacy_btn) {
            Intent intent = new Intent(getActivity(), PrivacyActivity.class);
            startActivity(intent);
        } else if (id == R.id.userPolicy_btn){
            Intent intent = new Intent(getActivity(), UserPrivacyActivity.class);
            startActivity(intent);
        } else if (id == R.id.back_btn_3) {
            requireActivity().finish();
        }
    }
}
