package com.aadu_maadu_kozhi.gregantech.adapter.viewholder;

import android.view.View;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateThagavalKalanchiyamBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ThagavalKalanchiyam;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ThagavalKalanchiyamViewHolder extends BaseViewHolder<ThagavalKalanchiyam> implements View.OnClickListener {
    private BaseRecyclerAdapterListener<ThagavalKalanchiyam> listener;
    private InflateThagavalKalanchiyamBinding binding;

    public ThagavalKalanchiyamViewHolder(InflateThagavalKalanchiyamBinding itemView, BaseRecyclerAdapterListener<ThagavalKalanchiyam> listener) {
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
    void populateData(ThagavalKalanchiyam data) {
        String url = data.getImageUrl();
        Glide.with(itemView.getContext()).load(url)
                .apply(RequestOptions.placeholderOf(R.drawable.bg).error(R.drawable.logo))
                .into(binding.ivThagaval);

        binding.tvName.setText(data.getTitle());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvParent:
                listener.onClickItem(v,data);
                break;
        }

    }
}
