package com.example.mochi.activity.admin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mochi.R;
import com.example.mochi.database.databasedoctruyen;

public class LoginAmin extends AppCompatActivity {

    EditText edtTaiKhoan, edtMatKhau;
    Button btnDangNhap;

    //tạo đối tượng cho databasedoctruyen

    databasedoctruyen databasedoctruyen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_amin);
        AnhXa();

        databasedoctruyen = new databasedoctruyen(this);
        //tạo sự kiện click button đăng ký sẽ chuyển qua form đăng ký

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gán các biến là các giá trị nhập từ edittext
                String tentaikhoan = edtTaiKhoan.getText().toString();
                String matkhau = edtMatKhau.getText().toString();


                //sử dụng con trỏ để lấy dữ liệu, trỏ tới getData() để lấy tất cả các tài khoản ở database
                Cursor cursor = databasedoctruyen.getData();


                //thực hiện vòng lặp để lấy dữ liệu từ curor với moviText() dy chuyển tiếp

                while (cursor.moveToNext()) {
                    //lấy dữ liệu và gắn vào biến , dữ liệu tài khoản ở ô 1 và mật khẩu ở ô 2 , ô 0 là idtai khoản,
                    //ô 3 là email,ô 4 là phân quyền

                    String datatentaikhoan = cursor.getString(1);
                    String datamatkhau = cursor.getString(2);

                    // nếu tài khoản và mật khẩu nhập vào trùng với database
                    if (datatentaikhoan.equals(tentaikhoan) && datamatkhau.equals(matkhau)) {
                        //lấy dữ liệu phân quyền và id

                        int phanquyen = cursor.getInt(4);
                        int idd = cursor.getInt(0);
                        String email = cursor.getString(3);
                        String tentk = cursor.getString(1);

                        //chuyển qua màn hình activity
                        //Intent intent = new Intent(LoginAmin.this,MainAmin.class);


                        //dăng bài


                        Intent intent = new Intent(LoginAmin.this, MainAmin.class);


                        //gửi dữ liệu
                        intent.putExtra("phanq", phanquyen);
                        intent.putExtra("idd", idd);
                        intent.putExtra("email", email);
                        intent.putExtra("tentaikhoan", tentk);

                        startActivity(intent);

                    }
                }
                //thực hiện trả curor về đâu
                cursor.moveToFirst();
                //đóng khi không dùng
                cursor.close();
            }
        });


    }

    private void AnhXa() {
        edtMatKhau = findViewById(R.id.matkhau);
        edtTaiKhoan = findViewById(R.id.taikhoan);

        btnDangNhap = findViewById(R.id.dangnhap);


    }
}
