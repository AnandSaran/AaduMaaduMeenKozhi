package com.aadu_maadu_kozhi.gregantech.adapter.viewholder;

import android.view.View;

import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateAreaBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.Area;

/**
 * Created by Anand on 8/27/2017.
 */

public class AreaBottomSheetViewHolder extends BaseViewHolder<Area> implements View.OnClickListener {
    private BaseRecyclerAdapterListener<Area> listener;
    private InflateAreaBinding binding;

    public AreaBottomSheetViewHolder(InflateAreaBinding itemView, BaseRecyclerAdapterListener<Area> listener) {
        super(itemView.getRoot());
        this.listener = listener;
        binding = itemView;
        binding.setHandlers(this);
        // binding.lbUSerPostLike.setOnClickListener(this);
        bindHolder();
    }

    private void bindHolder() {


    }

    @Override
    void populateData(Area data) {
        binding.rbArea.setText(data.getArea_name());

    }


    @Override
    public void onClick(View v) {
        listener.onClickItem(data);

    }
}
