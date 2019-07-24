package com.aadu_maadu_kozhi.gregantech.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.AccessoriesRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.adapter.viewholder.AccessoriesViewHolder;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateAccessoriesBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.Accessories;

import java.util.List;

import androidx.databinding.DataBindingUtil;


/**
 * Created by Anand on 8/27/2017.
 */

public class AccessoryAdapter extends BaseRecyclerAdapter<Accessories, AccessoriesViewHolder> {
    private AccessoriesRecyclerAdapterListener<Accessories> listener;

    public AccessoryAdapter(List<Accessories> data, AccessoriesRecyclerAdapterListener<Accessories> listener) {
        super(data);
        this.listener = listener;

    }

    @Override
    public AccessoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InflateAccessoriesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.inflate_accessories, parent, false);
        return new AccessoriesViewHolder(binding, listener);

    }
}
