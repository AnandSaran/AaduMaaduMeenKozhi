package com.aadu_maadu_kozhi.gregantech.adapter;

import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.adapter.viewholder.UserPostCommentViewHolder;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateUserPostCommentBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.UserPostComment;

import java.util.List;


/**
 * Created by Anand on 8/27/2017.
 */

public class UserPostCommentAdapter extends BaseRecyclerAdapter<UserPostComment, UserPostCommentViewHolder> {
    private BaseRecyclerAdapterListener<UserPostComment> listener;

    public UserPostCommentAdapter(List<UserPostComment> data, BaseRecyclerAdapterListener<UserPostComment> listener) {
        super(data);
        this.listener = listener;

    }

    @Override
    public UserPostCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InflateUserPostCommentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.inflate_user_post_comment, parent, false);
        return new UserPostCommentViewHolder(binding, listener);

    }
}
