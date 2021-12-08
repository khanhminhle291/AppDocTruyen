package com.example.mochi.activity.content;


import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mochi.activity.comment.Comment;
import com.example.mochi.database.UserSingleton;
import com.example.mochi.database.UsersFirebase;
import com.example.mochi.databinding.ActivityManContentBinding;
import com.example.mochi.define.Define;
import com.example.mochi.model.User;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;


public class Content extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.mochi.databinding.ActivityManContentBinding binding =
                ActivityManContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String name = intent.getStringExtra(Define.KEY_NAME);
        String content = intent.getStringExtra(Define.KEY_CONTENT);

        User user = UserSingleton.getInstance().getUser();

        ArrayList<String> likes = user.getLikes();

        binding.nameTV.setText(name);
        binding.content.setText(content);
        binding.content.setMovementMethod(new ScrollingMovementMethod());

        binding.likeBT.setLiked(user.getLikes().contains(name));

        binding.likeBT.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if (!likes.contains(name)) {
                    likes.add(name);
                    user.setLikes(likes);
                    UserSingleton.getInstance().setUser(user);
                    UsersFirebase.updateUser(user.getUsername(), Define.KEY_LIKES, likes);
                }
                Toast.makeText(Content.this, "Yêu thích truyện thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if (likes.contains(name)) {
                    likes.remove(name);
                    user.setLikes(likes);
                    UserSingleton.getInstance().setUser(user);
                    UsersFirebase.updateUser(user.getUsername(), Define.KEY_LIKES, likes);
                }
                Toast.makeText(Content.this, "Bỏ yêu thích truyện thành công", Toast.LENGTH_SHORT).show();
            }
        });

        binding.comment.setOnClickListener(v -> {
                    Intent newIntent = new Intent(this, Comment.class);
                    newIntent.putExtra(Define.KEY_NAME, name);
                    startActivity(newIntent);
                }
        );
    }
}
