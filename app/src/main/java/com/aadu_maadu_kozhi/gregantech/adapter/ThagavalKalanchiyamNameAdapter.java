package com.aadu_maadu_kozhi.gregantech.adapter;

import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.adapter.viewholder.ThagavalKalanchiyamNameViewHolder;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateThagavalKalanchiyamNameBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ThagavalKalanchiyam;

import java.util.List;


/**
 * Created by Anand on 8/27/2017.
 */

public class ThagavalKalanchiyamNameAdapter extends BaseRecyclerAdapter<ThagavalKalanchiyam, ThagavalKalanchiyamNameViewHolder> {
    private BaseRecyclerAdapterListener<ThagavalKalanchiyam> listener;

    public ThagavalKalanchiyamNameAdapter(List<ThagavalKalanchiyam> data, BaseRecyclerAdapterListener<ThagavalKalanchiyam> listener) {
        super(data);
        this.listener = listener;

    }

    @Override
    public ThagavalKalanchiyamNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InflateThagavalKalanchiyamNameBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.inflate_thagaval_kalanchiyam_name, parent, false);
        return new ThagavalKalanchiyamNameViewHolder(binding, listener);

    }
}
