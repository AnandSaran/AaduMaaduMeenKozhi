package com.aadu_maadu_kozhi.gregantech.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.adapter.viewholder.TypeListCheckBoxViewHolder;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ThagavalKalanchiyam;

import java.util.List;



/**
 * Created by Anand on 4/1/2017.
 */

public class TypeListCheckBoxAdapter extends BaseRecyclerAdapter<ThagavalKalanchiyam, TypeListCheckBoxViewHolder> {
    private BaseRecyclerAdapterListener<ThagavalKalanchiyam> listener;


    public TypeListCheckBoxAdapter(List<ThagavalKalanchiyam> data, BaseRecyclerAdapterListener<ThagavalKalanchiyam> listener) {
        super(data);
        this.listener = listener;
    }

    @Override
    public TypeListCheckBoxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TypeListCheckBoxViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_type_check_box, parent, false), listener);
    }


}
