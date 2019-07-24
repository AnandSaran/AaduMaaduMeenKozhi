package com.aadu_maadu_kozhi.gregantech.adapter.listener;


import com.aadu_maadu_kozhi.gregantech.model.pojo.Like;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;
import com.aadu_maadu_kozhi.gregantech.model.pojo.UserPost;

public interface UserPostRecyclerAdapterListener<T> extends BaseRecyclerAdapterListener<T> {

    void onLikeClicked(UserPost userPost, Like data, boolean isToAdd, User user);
    void onCommentClicked(UserPost userPost, User user);
    void onChatClicked(UserPost userPost, User user);
    void onShareClicked(UserPost userPost);


    void onTrashClicked(UserPost data, User user);
}
