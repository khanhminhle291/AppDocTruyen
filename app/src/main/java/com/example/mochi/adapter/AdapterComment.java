package com.example.mochi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.mochi.databinding.CellCommentBinding;
import com.example.mochi.model.UserComment;

import java.util.ArrayList;

public class AdapterComment extends BaseAdapter {
    private final Context context;
    public ArrayList<UserComment> comments;

    private CellCommentBinding binding;

    public AdapterComment(Context context, ArrayList<UserComment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }


    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            binding = CellCommentBinding.inflate(LayoutInflater.from(context));
            convertView = binding.getRoot();
        }
        UserComment data = (UserComment) getItem(position);
        binding.usernameTV.setText(data.getUsername());
        binding.commentTV.setText(data.getComment());
        return convertView;
    }
}
