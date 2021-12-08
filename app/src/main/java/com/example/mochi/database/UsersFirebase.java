package com.example.mochi.database;

import androidx.annotation.NonNull;

import com.example.mochi.define.Define;
import com.example.mochi.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class UsersFirebase {

    public interface OnListener {
        void onData(boolean exist);
    }

    public interface OnListenerData {
        void onData(User user);
    }

    static public void addUser(User user) {
        FirebaseDatabase.getInstance().getReference(Define.KEY_USERS).child(user.getUsername()).setValue(user);
    }

    static public void updateUser(String username, String key, String value) {
        FirebaseDatabase.getInstance().getReference(Define.KEY_USERS).child(username).child(key).setValue(value);
    }

    static public void updateUser(String username, String key, ArrayList<String> value) {
        FirebaseDatabase.getInstance().getReference(Define.KEY_USERS).child(username).child(key).setValue(value);
    }

    static public void checkUser(String key, String value, OnListener listener) {
        FirebaseDatabase.getInstance().getReference(Define.KEY_USERS)
                .orderByChild(key).equalTo(value).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listener.onData(snapshot.exists());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    static public void getUser(String key, String value, OnListenerData listener) {
        FirebaseDatabase.getInstance().getReference(Define.KEY_USERS)
                .orderByChild(key).equalTo(value).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            try {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(snapshot.getValue()));
                                Iterator<String> keys = jsonObject.keys();
                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    try {
                                        JSONObject user = jsonObject.getJSONObject(key);
                                        ArrayList<String> likes = new ArrayList<>();
                                        if (user.has(Define.KEY_LIKES)) {
                                            JSONArray array = user.getJSONArray(Define.KEY_LIKES);
                                            for (int i = 0; i < array.length(); i++) {
                                                likes.add(array.getString(i));
                                            }
                                        }

                                        listener.onData(new User(
                                                user.getString(Define.KEY_FULL_NAME),
                                                user.getString(Define.KEY_USERNAME),
                                                user.getString(Define.KEY_EMAIL),
                                                user.getString(Define.KEY_PHONE_NUMBER),
                                                user.getString(Define.KEY_PASSWORD), likes));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        listener.onData(null);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.onData(null);
                            }
                        } else {
                            listener.onData(null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }
}
