package com.aadu_maadu_kozhi.gregantech.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.SuccessStoryRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.adapter.viewholder.SuccessStoryViewHolder;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateSuccessStoryBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.SuccessStory;

import java.util.List;

import androidx.databinding.DataBindingUtil;


/**
 * Created by Anand on 8/27/2017.
 */

public class SuccessStoryAdapter extends BaseRecyclerAdapter<SuccessStory, SuccessStoryViewHolder> {
    private SuccessStoryRecyclerAdapterListener<SuccessStory> listener;

    public SuccessStoryAdapter(List<SuccessStory> data, SuccessStoryRecyclerAdapterListener<SuccessStory> listener) {
        super(data);
        this.listener = listener;

    }

    @Override
    public SuccessStoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InflateSuccessStoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.inflate_success_story, parent, false);
        return new SuccessStoryViewHolder(binding, listener);

    }
}
