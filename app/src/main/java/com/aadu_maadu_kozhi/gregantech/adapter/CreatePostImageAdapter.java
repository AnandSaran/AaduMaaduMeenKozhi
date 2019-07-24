package com.aadu_maadu_kozhi.gregantech.adapter;

import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.adapter.viewholder.CreatePostImageViewHolder;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateCreatePostImageBinding;

import java.util.List;


/**
 * Created by Anand on 8/27/2017.
 */

public class CreatePostImageAdapter extends BaseRecyclerAdapter<String, CreatePostImageViewHolder> {
    private BaseRecyclerAdapterListener<String> listener;

    public CreatePostImageAdapter(List<String> data, BaseRecyclerAdapterListener<String> listener) {
        super(data);
        this.listener = listener;

    }

    @Override
    public CreatePostImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InflateCreatePostImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.inflate_create_post_image, parent, false);
        return new CreatePostImageViewHolder(binding, listener);

    }
}
