package com.example.mochi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.mochi.R;
import com.example.mochi.databinding.CellChuyenMucBinding;
import com.example.mochi.model.Chuyenmuc;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterChuyenMuc extends BaseAdapter {
    private final Context context;
    private final List<Chuyenmuc> chuyenmucList;
    private CellChuyenMucBinding binding;

    public AdapterChuyenMuc(Context context, List<Chuyenmuc> chuyenmucList) {
        this.context = context;
        this.chuyenmucList = chuyenmucList;
    }

    @Override
    public int getCount() {
        return chuyenmucList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            binding = CellChuyenMucBinding.inflate(LayoutInflater.from(context));
            convertView = binding.getRoot();
        }
        Chuyenmuc data = chuyenmucList.get(position);
        binding.nameTV.setText(data.getTenchuyenmuc());
        Picasso.get().load(data.getHinhanhchuyenmuc()).placeholder(R.drawable.ic_action_load)
                .error(R.drawable.images).into(binding.image);
        return convertView;
    }
}
