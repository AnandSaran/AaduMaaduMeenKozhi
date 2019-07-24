package com.aadu_maadu_kozhi.gregantech.adapter.viewholder;

import android.view.View;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateUserPostCommentBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.UserPostComment;
import com.aadu_maadu_kozhi.gregantech.util.DateTimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Date;

/**
 * Created by Anand on 8/27/2017.
 */

public class UserPostCommentViewHolder extends BaseViewHolder<UserPostComment> implements View.OnClickListener {
    private BaseRecyclerAdapterListener<UserPostComment> listener;
    private InflateUserPostCommentBinding binding;

    public UserPostCommentViewHolder(InflateUserPostCommentBinding itemView, BaseRecyclerAdapterListener<UserPostComment> listener) {
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
    void populateData(UserPostComment data) {
        String url=data.getCommented_user_profile_url()==null?itemView.getContext().getString(R.string.default_user_profile_img_url):data.getCommented_user_profile_url();
        Glide.with(itemView.getContext()).load(url)
                .apply(RequestOptions.placeholderOf(R.drawable.profile_pic).error(R.drawable.profile_pic).circleCropTransform())
                .into(binding.ivUserProfile);

        binding.tvComments.setText(data.getComments());
       String name= data.getCommented_user_name()==null?"New User":data.getCommented_user_name();
        binding.tvUserName.setText(name);
        Date date=data.getPost_commented_date_and_time()==null
                ?data.getPost_commented__local_date_and_time():data.getPost_commented_date_and_time().toDate();
      binding.tvCommentTime.setText(DateTimeUtils.getInstance().getTimeAgo(date.getTime()));
    }


    @Override
    public void onClick(View v) {

    }
}
