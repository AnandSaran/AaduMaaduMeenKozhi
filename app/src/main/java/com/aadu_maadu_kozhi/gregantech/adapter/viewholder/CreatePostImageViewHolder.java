package com.aadu_maadu_kozhi.gregantech.adapter.viewholder;

import android.view.View;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateCreatePostImageBinding;
import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by Anand on 8/27/2017.
 */

public class CreatePostImageViewHolder extends BaseViewHolder<String> implements View.OnClickListener {
    private BaseRecyclerAdapterListener<String> listener;
    private InflateCreatePostImageBinding binding;

    public CreatePostImageViewHolder(InflateCreatePostImageBinding itemView, BaseRecyclerAdapterListener<String> listener) {
        super(itemView.getRoot());
        this.listener = listener;
        binding = itemView;
        binding.setHandlers(this);
        bindHolder();
    }

    private void bindHolder() {


    }

    @Override
    void populateData(String data) {
        if (data.contains("http")) {
            Glide.with(itemView.getContext()).load(data).into(binding.ivSelectedImage);
            binding.ivDelete.setVisibility(View.GONE);

        } else {
            Glide.with(itemView.getContext()).load(new File(data)).into(binding.ivSelectedImage);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivDelete:
                listener.onClickItem(data);

                break;

        }
    }
}
