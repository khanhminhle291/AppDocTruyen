package com.example.mochi.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mochi.define.Define;
import com.example.mochi.model.UserComment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserCommentFirebase {

    public interface OnListenerData {
        void onData(ArrayList<UserComment> comments);
    }

    static public void getComments(String name, OnListenerData listener) {
        FirebaseDatabase.getInstance().getReference(Define.KEY_COMMENTS).child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<UserComment> comments = new ArrayList<>();
                if (!snapshot.exists()) {
                    listener.onData(comments);
                } else {
                    Log.d("DEBUG", "comments = " + snapshot);
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(snapshot.getValue()));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            comments.add(new UserComment(jsonObject.getString(Define.KEY_USERNAME),
                                    jsonObject.getString(Define.KEY_COMMENT)));
                        }
                        listener.onData(comments);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onData(null);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    static public void updateComment(String name, ArrayList<UserComment> comments) {
        FirebaseDatabase.getInstance().getReference(Define.KEY_COMMENTS).child(name).setValue(comments);
    }
}
