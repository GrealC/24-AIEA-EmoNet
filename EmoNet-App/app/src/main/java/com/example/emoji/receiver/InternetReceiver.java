package com.example.emoji.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/*
*  网络状态接收器
*
* */
public class InternetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isAvailable()) {
            Toast.makeText(context, "网络已重新连接，可以放心的使用了！", Toast.LENGTH_SHORT).show();
            Log.d("NetWork", "网络已连接");
        } else {
            Toast.makeText(context, "网络已断开，无法再继续工作了哦！", Toast.LENGTH_SHORT).show();
            Log.d("NetWork", "网络已断开");
        }
    }

}
