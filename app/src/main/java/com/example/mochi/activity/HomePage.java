package com.example.mochi.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.example.mochi.R;
import com.example.mochi.activity.admin.LoginAmin;
import com.example.mochi.activity.content.Content;
import com.example.mochi.activity.search.ManTimKiem;
import com.example.mochi.activity.userinfo.UserReLogin;
import com.example.mochi.adapter.AdapterChuyenMuc;
import com.example.mochi.adapter.AdapterTruyen;
import com.example.mochi.database.databasedoctruyen;
import com.example.mochi.databinding.ActivityHomePageBinding;
import com.example.mochi.define.Define;
import com.example.mochi.model.Chuyenmuc;
import com.example.mochi.model.Truyen;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class HomePage extends AppCompatActivity {
    databasedoctruyen databasedoctruyen;

    private ActivityHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databasedoctruyen = new databasedoctruyen(this);
        actionViewFlifer();
        actionBar();
        loadChuyenMuc();
        loadContents();
    }

    private void actionBar() {
        setSupportActionBar(binding.toolBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolBar.setNavigationIcon(R.drawable.ic_action_name);
        binding.toolBar.setNavigationOnClickListener(v -> binding.drawerlayout.openDrawer(GravityCompat.START));
    }

    private void actionViewFlifer() {
        ArrayList<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://cdn.eva.vn//upload/4-2016/images/2016-12-06/cau-chuyen-tho-va-rua-1481017140-width500height233.jpg");
        mangquangcao.add("https://truyencotich.vn/wp-content/uploads/2015/01/Deo-Luc-Lac-Cho-Meo7-660x440.jpg");
        mangquangcao.add("https://mn195tanlap.pgdtpthainguyen.edu.vn/upload/50539/20170407/grabc8b01_E1_BA_A2nh_ch_E1_BB_A5p_m_C3_A0n_h_C3_ACnh_2015_01_05_t_E1_BA_A1i_14.00.16.png");
        mangquangcao.add("https://cdn.eva.vn/upload/1-2021/images/2021-02-07/picture-9-1612659384-985-width600height319.jpg");
        for (int i = 0; i < mangquangcao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.get().load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            binding.viewfliper.addView(imageView);
        }
        binding.viewfliper.setFlipInterval(4000);
        binding.viewfliper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        binding.viewfliper.setInAnimation(animation_slide_in);
        binding.viewfliper.setInAnimation(animation_slide_out);
    }

    private void loadContents() {
        ArrayList<Truyen> contents = new ArrayList<>();
        Cursor cursor1 = databasedoctruyen.getData1();
        while (cursor1.moveToNext()) {
            int id = cursor1.getInt(0);
            String tentruyen = cursor1.getString(1);
            String noidung = cursor1.getString(2);
            String anh = cursor1.getString(3);
            int id_tk = cursor1.getInt(4);
            contents.add(new Truyen(id, tentruyen, noidung, anh, id_tk));
            binding.contentsLV.setAdapter(new AdapterTruyen(this, contents));
        }
        cursor1.moveToFirst();
        cursor1.close();

        binding.contentsLV.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(HomePage.this, Content.class);
            intent.putExtra(Define.KEY_NAME, contents.get(position).getTenTruyen());
            intent.putExtra(Define.KEY_CONTENT, contents.get(position).getNoiDung());
            startActivity(intent);
        });
    }

    private void loadChuyenMuc() {
        ArrayList<Chuyenmuc> chuyenmucArrayList = new ArrayList<>();
        chuyenmucArrayList.add(new Chuyenmuc("Đăng bài", R.drawable.ic_post));
        chuyenmucArrayList.add(new Chuyenmuc("Thông tin", R.drawable.username));
        chuyenmucArrayList.add(new Chuyenmuc("Đăng xuất", R.drawable.ic_exit));
        binding.chuyenMucLV.setAdapter(new AdapterChuyenMuc(this, chuyenmucArrayList));

        binding.chuyenMucLV.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(this, LoginAmin.class));
                    break;
                case 1:
                    startActivity(new Intent(this, UserReLogin.class));
                    break;
                case 2:
                    finish();
                    break;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.searchBT) {
            startActivity(new Intent(this, ManTimKiem.class));
        }
        return super.onOptionsItemSelected(item);
    }

}
