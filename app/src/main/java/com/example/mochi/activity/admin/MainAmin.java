package com.example.mochi.activity.admin;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mochi.R;
import com.example.mochi.activity.post.MainDangBai;
import com.example.mochi.adapter.AdapterTruyen;
import com.example.mochi.database.databasedoctruyen;
import com.example.mochi.model.Truyen;

import java.util.ArrayList;

public class MainAmin extends AppCompatActivity {

    ListView listView;
    Button buttonThem;

    ArrayList<Truyen> TruyenArrayList;
    AdapterTruyen adapterTruyen;
    databasedoctruyen databasedoctruyen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_amin);

        listView = findViewById(R.id.listviewAdmin);
        buttonThem = findViewById(R.id.buttonThemTruyen);

        initList();

        buttonThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten1 = getIntent();
                int id = inten1.getIntExtra("Id", 0);

                Intent intent = new Intent(MainAmin.this, MainDangBai.class);
                intent.putExtra("Id", id);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DialogDelete(position);
                return false;
            }
        });
    }

    private void DialogDelete(final int position) {
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialogdelete);

        dialog.setCanceledOnTouchOutside(false);


        Button btnYes = dialog.findViewById(R.id.butnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idtruyen = TruyenArrayList.get(position).getID();

                databasedoctruyen.Delete(idtruyen);

                Intent intent = new Intent(MainAmin.this, MainAmin.class);
                finish();
                startActivity(intent);

                Toast.makeText(MainAmin.this, "Xóa truyện thành công", Toast.LENGTH_SHORT).show();

            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

    private void initList() {
        TruyenArrayList = new ArrayList<>();

        databasedoctruyen = new databasedoctruyen(this);
        Cursor cursor1 = databasedoctruyen.getData2();

        while (cursor1.moveToNext()) {
            int id = cursor1.getInt(0);
            String tentruyen = cursor1.getString(1);
            String noidung = cursor1.getString(2);
            String anh = cursor1.getString(3);
            int id_tk = cursor1.getInt(4);

            TruyenArrayList.add(new Truyen(id, tentruyen, noidung, anh, id_tk));

            adapterTruyen = new AdapterTruyen(getApplicationContext(), TruyenArrayList);

            listView.setAdapter(adapterTruyen);
        }

        cursor1.moveToFirst();
        cursor1.close();
    }
}
