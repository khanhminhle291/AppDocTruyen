package com.example.mochi.activity.comment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mochi.adapter.AdapterComment;
import com.example.mochi.database.UserCommentFirebase;
import com.example.mochi.database.UserSingleton;
import com.example.mochi.databinding.ActivityCommentBinding;
import com.example.mochi.define.Define;
import com.example.mochi.model.User;
import com.example.mochi.model.UserComment;

import java.util.ArrayList;

public class Comment extends AppCompatActivity {

    private ActivityCommentBinding binding;
    private AdapterComment adapterComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra(Define.KEY_NAME);
        User user = UserSingleton.getInstance().getUser();
        ArrayList<UserComment> comments = new ArrayList<>();

        UserCommentFirebase.getComments(name, commentsData -> {
            Log.d("DEBUG", "comment data = " + commentsData);
            comments.addAll(commentsData);
            adapterComment = new AdapterComment(this, comments);
            binding.commentsLV.setAdapter(adapterComment);
        });

        binding.sendBT.setOnClickListener(v -> {
            binding.sendBT.setEnabled(false);

            String comment = binding.commentTV.getText().toString();
            if (comment.isEmpty()) {
                binding.sendBT.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Vui lòng nhập nội dung cho bình luận", Toast.LENGTH_SHORT).show();
            } else {
                comments.add(new UserComment(user.getUsername(), comment));
                UserCommentFirebase.updateComment(name, comments);
                adapterComment.notifyDataSetChanged();
                binding.commentTV.setText(null);
                binding.sendBT.setEnabled(true);
            }
        });
    }

}
