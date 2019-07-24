package com.aadu_maadu_kozhi.gregantech.adapter;

import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.adapter.viewholder.ThagavalKalanchiyamViewHolder;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateThagavalKalanchiyamBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ThagavalKalanchiyam;

import java.util.List;


/**
 * Created by Anand on 8/27/2017.
 */

public class ThagavalKalanchiyamAdapter extends BaseRecyclerAdapter<ThagavalKalanchiyam, ThagavalKalanchiyamViewHolder> {
    private BaseRecyclerAdapterListener<ThagavalKalanchiyam> listener;

    public ThagavalKalanchiyamAdapter(List<ThagavalKalanchiyam> data, BaseRecyclerAdapterListener<ThagavalKalanchiyam> listener) {
        super(data);
        this.listener = listener;

    }

    @Override
    public ThagavalKalanchiyamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InflateThagavalKalanchiyamBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.inflate_thagaval_kalanchiyam, parent, false);
        return new ThagavalKalanchiyamViewHolder(binding, listener);

    }
}
