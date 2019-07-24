package com.aadu_maadu_kozhi.gregantech.adapter.viewholder;

import android.view.View;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateDoctorListBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class DoctorsListViewHolder extends BaseViewHolder<User> implements View.OnClickListener {
    private BaseRecyclerAdapterListener<User> listener;
    private InflateDoctorListBinding binding;
    private boolean isToShowType;
    private String viewFor;

    public DoctorsListViewHolder(InflateDoctorListBinding itemView, BaseRecyclerAdapterListener<User> listener, boolean isToShowType, String viewFor) {
        super(itemView.getRoot());
        this.listener = listener;
        binding = itemView;
        binding.setHandlers(this);
        this.isToShowType = isToShowType;
        this.viewFor = viewFor;

        bindHolder();
    }

    private void bindHolder() {


    }

    @Override
    void populateData(User user) {
        String url = user.getImage_url();
        Glide.with(itemView.getContext()).load(url)
                .apply(RequestOptions.placeholderOf(R.drawable.bg).error(R.drawable.logo).circleCropTransform())

                .into(binding.ivUserProfile);

        binding.tvArea.setText(user.getArea());
        binding.tvMobileNumber.setText(user.getMobile_number());
        String name = user.getName() == null ? "New User" : user.getName();
        binding.tvUserName.setText(name);
        if (isToShowType) {
            switch (viewFor) {
                case Constants.BundleKey.ForFarmOwnersLogin:
                    binding.tvType.setText(user.getFarm_animal_type());

                    break;
                case Constants.BundleKey.ForSellersLogin:
                    binding.tvType.setText(user.getSelling_animal_type());

                    break;
            }
            binding.tvType.setVisibility(View.VISIBLE);

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvParent:
                listener.onClickItem(v, data);
                break;
        }

    }
}
