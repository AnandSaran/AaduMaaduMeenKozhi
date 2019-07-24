package com.aadu_maadu_kozhi.gregantech.adapter;

import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.UserPostRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.adapter.viewholder.UserPostViewHolder;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateUserPostBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.UserPost;

import java.util.List;


/**
 * Created by Anand on 8/27/2017.
 */

public class UserPostAdapter extends BaseRecyclerAdapter<UserPost, UserPostViewHolder> {
    private UserPostRecyclerAdapterListener<UserPost> listener;

    public UserPostAdapter(List<UserPost> data, UserPostRecyclerAdapterListener<UserPost> listener) {
        super(data);
        this.listener = listener;

    }

    @Override
    public UserPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InflateUserPostBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.inflate_user_post, parent, false);
        return new UserPostViewHolder(binding, listener);

    }
}
