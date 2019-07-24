package com.aadu_maadu_kozhi.gregantech.adapter.viewholder;

import android.view.View;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateThagavalKalanchiyamNameBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ThagavalKalanchiyam;

public class ThagavalKalanchiyamNameViewHolder extends BaseViewHolder<ThagavalKalanchiyam> implements View.OnClickListener {
    private BaseRecyclerAdapterListener<ThagavalKalanchiyam> listener;
    private InflateThagavalKalanchiyamNameBinding binding;

    public ThagavalKalanchiyamNameViewHolder(InflateThagavalKalanchiyamNameBinding itemView, BaseRecyclerAdapterListener<ThagavalKalanchiyam> listener) {
        super(itemView.getRoot());
        this.listener = listener;
        binding = itemView;
        binding.setHandlers(this);
        bindHolder();
    }

    private void bindHolder() {


    }

    @Override
    void populateData(ThagavalKalanchiyam data) {


        binding.rbName.setText(data.getTitle());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rbName:
                listener.onClickItem( data);
                break;
        }

    }
}
