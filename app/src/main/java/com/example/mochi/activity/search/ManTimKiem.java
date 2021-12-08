package com.example.mochi.activity.search;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mochi.R;
import com.example.mochi.activity.content.Content;
import com.example.mochi.adapter.AdapterTruyen;
import com.example.mochi.database.databasedoctruyen;
import com.example.mochi.define.Define;
import com.example.mochi.model.Truyen;

import java.util.ArrayList;

public class ManTimKiem extends AppCompatActivity {
    ListView listView;

    EditText edt;

    ArrayList<Truyen> TruyenArrayList;

    ArrayList<Truyen> arrayList;

    AdapterTruyen adapterTruyen;

    databasedoctruyen databasedoctruyen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_tim_kiem);

        listView = findViewById(R.id.listviewTimKiem);
        edt = findViewById(R.id.timkiem);

        intList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ManTimKiem.this, Content.class);
                String name = arrayList.get(position).getTenTruyen();
                String content = arrayList.get(position).getNoiDung();
                intent.putExtra(Define.KEY_NAME, name);
                intent.putExtra(Define.KEY_CONTENT, content);
                startActivity(intent);
            }
        });
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }

    //tìm kiếm
    private void filter(String text) {
        // xóa dữ liệu mảng
        arrayList.clear();

        ArrayList<Truyen> filterdList = new ArrayList<>();

        for (Truyen item : TruyenArrayList) {
            int checkTest = item.getTenTruyen().toLowerCase().indexOf(text.toLowerCase());
            if (checkTest != -1) {

                //thêm item  vào filterdList
                filterdList.add(item);

                //thêm vào mảng
                arrayList.add(item);

            }
        }
        adapterTruyen.filterList(filterdList);
    }

    private void intList() {
        TruyenArrayList = new ArrayList<>();

        arrayList = new ArrayList<>();

        databasedoctruyen = new databasedoctruyen(this);

        Cursor cursor = databasedoctruyen.getData2();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String tentruyen = cursor.getString(1);
            String noidung = cursor.getString(2);
            String anh = cursor.getString(3);
            int id_tk = cursor.getInt(4);

            TruyenArrayList.add(new Truyen(id, tentruyen, noidung, anh, id_tk));

            arrayList.add(new Truyen(id, tentruyen, noidung, anh, id_tk));

            adapterTruyen = new AdapterTruyen(getApplicationContext(), TruyenArrayList);

            listView.setAdapter(adapterTruyen);

        }
        cursor.moveToFirst();
        cursor.close();

    }
}
