package com.example.mochi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.mochi.R;
import com.example.mochi.databinding.NewtruyenBinding;
import com.example.mochi.model.Truyen;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterTruyen extends BaseAdapter {
    private final Context context;
    public ArrayList<Truyen> listTruyen;

    private NewtruyenBinding binding;

    public AdapterTruyen(Context context, ArrayList<Truyen> listTruyen) {
        this.context = context;
        this.listTruyen = listTruyen;
    }

    @Override
    public int getCount() {
        return listTruyen.size();
    }


    @Override
    public Object getItem(int position) {
        return listTruyen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void filterList(ArrayList<Truyen> filterdList) {
        System.out.print(filterdList.size());
        listTruyen = filterdList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            binding = NewtruyenBinding.inflate(LayoutInflater.from(context));
            convertView = binding.getRoot();
        }
        Truyen truyen = (Truyen) getItem(position);
        binding.nameTV.setText(truyen.getTenTruyen());
        Picasso.get().load(truyen.getAnh()).placeholder(R.drawable.ic_action_load).error(R.drawable.images).into(binding.image);
        return convertView;
    }
}
