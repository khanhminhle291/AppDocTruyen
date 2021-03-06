package com.example.mochi.activity.userinfo;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mochi.database.UserSingleton;
import com.example.mochi.database.UsersFirebase;
import com.example.mochi.databinding.ActivityUserInfoBinding;
import com.example.mochi.define.Define;
import com.example.mochi.model.User;

import java.util.Objects;


public class UserInfo extends AppCompatActivity {

    private ActivityUserInfoBinding binding;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ShowAllUserData();
        binding.updateBT.setOnClickListener(v -> update());
    }


    private void ShowAllUserData() {
        user = UserSingleton.getInstance().getUser();
        binding.fullNameTV.setText(user.getFullName());
        binding.fullNameET.setText(user.getFullName());
        binding.passwordET.setText(user.getPassword());
        Objects.requireNonNull(binding.emailET.getEditText()).setText(user.getEmail(),
                TextView.BufferType.NORMAL);
        Objects.requireNonNull(binding.phoneNumberET.getEditText()).setText(user.getPhoneNumber(),
                TextView.BufferType.NORMAL);
        Objects.requireNonNull(binding.usernameET.getEditText()).setText(user.getUsername(),
                TextView.BufferType.NORMAL);
    }

    public void update() {
        viewEnable(false);

        String email = Objects.requireNonNull(binding.emailET.getEditText()).getText().toString();
        isEmailChanged(email);

        String fullName = Objects.requireNonNull(binding.fullNameET.getText()).toString();
        isFullNameChanged(fullName);

        String phoneNumber = Objects.requireNonNull(binding.phoneNumberET.getEditText()).getText().toString();
        isPhoneChanged(phoneNumber);

        String username = Objects.requireNonNull(binding.usernameET.getEditText()).getText().toString();
        isUsernameChanged(username);

        String password = Objects.requireNonNull(binding.passwordET.getText()).toString();
        isPasswordChanged(password);
    }

    private void isEmailChanged(String email) {
        if (email.isEmpty()) {
            binding.emailET.setError("Ph???i ??i???n ?????y ????? th??ng tin");
            viewEnable(true);
            return;
        }
        String reg = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.matches(reg)) {
            binding.emailET.setError("?????a ch??? email kh??ng h???p l???");
            viewEnable(true);
            return;
        }
        if (!user.getEmail().equals(email)) {
            UsersFirebase.checkUser(Define.KEY_EMAIL, email, exist -> {
                if (exist) {
                    viewEnable(true);
                    binding.emailET.setError("?????a ch??? email thay ?????i ???? t???n t???i");
                } else {
                    user.setEmail(email);
                    UserSingleton.getInstance().setUser(user);
                    UsersFirebase.updateUser(user.getUsername(), Define.KEY_EMAIL, email);
                    binding.emailET.setError(null);
                    Objects.requireNonNull(binding.emailET.getEditText()).setText(email,
                            TextView.BufferType.NORMAL);
                    viewEnable(true);
                    Toast.makeText(getApplicationContext(), "C???p nh???t Email th??nh c??ng", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            viewEnable(true);
        }
    }


    private void isPhoneChanged(String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            binding.phoneNumberET.setError("Ph???i ??i???n ?????y ????? th??ng tin");
            viewEnable(true);
            return;
        }
        String noWhiteSpace = "\\A\\w{10,11}\\z";
        if (!phoneNumber.matches(noWhiteSpace)) {
            binding.phoneNumberET.setError("S??? ??i???n thoai kh??ng h???p l???");
            viewEnable(true);
            return;
        }
        if (!user.getPhoneNumber().equals(phoneNumber)) {
            UsersFirebase.checkUser(Define.KEY_PHONE_NUMBER, phoneNumber, exist -> {
                if (exist) {
                    binding.phoneNumberET.setError("S??? ??i???n tho???i thay ?????i ???? t???n t???i");
                    viewEnable(true);
                } else {
                    user.setPhoneNumber(phoneNumber);
                    UserSingleton.getInstance().setUser(user);
                    UsersFirebase.updateUser(user.getUsername(), Define.KEY_PHONE_NUMBER, phoneNumber);
                    binding.phoneNumberET.setError(null);
                    Objects.requireNonNull(binding.phoneNumberET.getEditText()).setText(phoneNumber,
                            TextView.BufferType.NORMAL);
                    viewEnable(true);
                    Toast.makeText(getApplicationContext(), "C???p nh???t s??? ??i???n tho???i th??nh c??ng", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            viewEnable(true);
        }
    }


    private void isFullNameChanged(String fullName) {
        if (!user.getFullName().equals(fullName)) {
            user.setFullName(fullName);
            UserSingleton.getInstance().setUser(user);
            UsersFirebase.updateUser(user.getUsername(), Define.KEY_FULL_NAME, fullName);
            binding.fullNameET.setError(null);
            binding.fullNameET.setText(fullName, TextView.BufferType.NORMAL);
            binding.fullNameTV.setText(fullName, TextView.BufferType.NORMAL);
            viewEnable(true);
            Toast.makeText(getApplicationContext(), "C???p nh???t FullName th??nh c??ng", Toast.LENGTH_SHORT).show();
        } else {
            viewEnable(true);
        }
    }

    private void isUsernameChanged(String username) {
        if (!user.getUsername().equals(username)) {
            user.setUsername(username);
            UsersFirebase.checkUser(Define.KEY_USERNAME, username, exist -> {
                if (exist) {
                    viewEnable(true);
                    binding.usernameET.setError("Username thay ?????i ???? t???n t???i");
                } else {
                    user.setUsername(username);
                    UserSingleton.getInstance().setUser(user);
                    UsersFirebase.updateUser(user.getUsername(), Define.KEY_USERNAME, username);
                    binding.usernameET.setError(null);
                    Objects.requireNonNull(binding.usernameET.getEditText()).setText(username,
                            TextView.BufferType.NORMAL);
                    viewEnable(true);
                    Toast.makeText(getApplicationContext(), "C???p nh???t username th??nh c??ng", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            viewEnable(true);
        }
    }

    private void isPasswordChanged(String password) {
        if (password.isEmpty()) {
            binding.passwordET.setError("Ph???i ??i???n ?????y ????? th??ng tin");
            viewEnable(true);
            return;
        }
        String noWhiteSpace = "\\A\\w{6,8}\\z";
        if (!password.matches(noWhiteSpace)) {
            binding.passwordET.setError("M???t kh???u kh??ng h???p l???");
            viewEnable(true);
            return;
        }
        if (!user.getPassword().equals(password)) {
            user.setPassword(password);
            UserSingleton.getInstance().setUser(user);
            UsersFirebase.updateUser(user.getUsername(), Define.KEY_PASSWORD, password);
            binding.passwordET.setError(null);
            viewEnable(true);
            Toast.makeText(getApplicationContext(), "C???p nh???t m???t kh???u th??nh c??ng", Toast.LENGTH_SHORT).show();
        } else {
            viewEnable(true);
        }
    }

    private void viewEnable(boolean enable) {
        binding.updateBT.setEnabled(enable);
        if (enable) {
            binding.progressBar.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
        }
    }


}
