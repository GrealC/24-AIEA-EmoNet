package com.example.emoji.moudle;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.emoji.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/*
*  系统主界面
*  1. 初始化了系统中的3个碎片
*  2. 监听了底部导航栏的点击事件
*  3. 监听了返回键
* */
public class UseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private Fragment[] fragments;
    private int currentTabIndex = 2; // 默认显示个人中心
    private int lastTabIndex = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_use);

        bottomNavigationView = findViewById(R.id.main_navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        initFragment();
    }

    private void initFragment() {

        AlbumFragment albumFragment = new AlbumFragment();
        RecognitionFragment recognitionFragment = new RecognitionFragment();
        PersonFragment personFragment = new PersonFragment();

        fragments = new Fragment[]{albumFragment, recognitionFragment, personFragment};
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_page_controller, personFragment)
                .commit();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_album && lastTabIndex != 0) {
            currentTabIndex = 0;
            switchFragment(lastTabIndex, currentTabIndex);
            lastTabIndex = 0;
        } else if (id == R.id.menu_emoji && lastTabIndex != 1) {
            currentTabIndex = 1;
            switchFragment(lastTabIndex, currentTabIndex);
            lastTabIndex = 1;
        } else if (id == R.id.menu_home && lastTabIndex != 2) {
            currentTabIndex = 2;
            switchFragment(lastTabIndex, currentTabIndex);
            lastTabIndex = 2;
        }
        return true;
    }

    private void switchFragment(int last, int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragments[last]);

        if (!fragments[index].isAdded()) {
            transaction.add(R.id.main_page_controller, fragments[index]);
        }

        transaction.show(fragments[index]).commit();
    }
}