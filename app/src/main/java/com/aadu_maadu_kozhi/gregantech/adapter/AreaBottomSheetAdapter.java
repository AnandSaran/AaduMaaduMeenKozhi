package com.aadu_maadu_kozhi.gregantech.adapter;

import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.adapter.viewholder.AreaBottomSheetViewHolder;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateAreaBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.Area;

import java.util.List;


/**
 * Created by Anand on 8/27/2017.
 */

public class AreaBottomSheetAdapter extends BaseRecyclerAdapter<Area, AreaBottomSheetViewHolder> {
    private BaseRecyclerAdapterListener<Area> listener;

    public AreaBottomSheetAdapter(List<Area> data, BaseRecyclerAdapterListener<Area> listener) {
        super(data);
        this.listener = listener;

    }

    @Override
    public AreaBottomSheetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InflateAreaBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.inflate_area, parent, false);
        return new AreaBottomSheetViewHolder(binding, listener);

    }
}
