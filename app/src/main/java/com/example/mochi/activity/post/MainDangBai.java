package com.example.mochi.activity.post;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mochi.R;
import com.example.mochi.activity.admin.MainAmin;
import com.example.mochi.database.databasedoctruyen;
import com.example.mochi.model.Truyen;

public class MainDangBai extends AppCompatActivity {

    EditText edtTenTruyen, edtNoiDung, edtAnh;
    Button btnDangBai;
    databasedoctruyen databasedoctruyen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dang_bai);
        edtAnh = findViewById(R.id.dbImg);
        edtTenTruyen = findViewById(R.id.dbTenTruyen);
        edtNoiDung = findViewById(R.id.dbNoiDung);
        btnDangBai = findViewById(R.id.dbdangbai);


        databasedoctruyen = new databasedoctruyen(this);

        btnDangBai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tentruyen = edtTenTruyen.getText().toString();
                String noidung = edtNoiDung.getText().toString();
                String anh = edtAnh.getText().toString();

                Truyen truyen = CreateTruyen();

                if (tentruyen.equals("") || noidung.equals("") || anh.equals("")) {
                    Toast.makeText(MainDangBai.this, "Yêu cầu bạn nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    Log.e("ERR :", "Nhập đầy đủ thông tin");
                } else {
                    databasedoctruyen.AddTruyen(truyen);

                    Intent intent = new Intent(MainDangBai.this, MainAmin.class);
                    finish();

                    startActivity(intent);
                }
            }
        });

    }

    private Truyen CreateTruyen() {
        String tentruyen = edtTenTruyen.getText().toString();
        String noidung = edtNoiDung.getText().toString();
        String anh = edtAnh.getText().toString();

        Intent intent = getIntent();
        int id = intent.getIntExtra("Id", 0);
        Truyen truyen = new Truyen(tentruyen, noidung, anh, id);
        return truyen;


    }
}
