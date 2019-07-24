package com.aadu_maadu_kozhi.gregantech.adapter.listener;


import com.aadu_maadu_kozhi.gregantech.model.pojo.Like;
import com.aadu_maadu_kozhi.gregantech.model.pojo.SuccessStory;
import com.aadu_maadu_kozhi.gregantech.model.pojo.UserPost;

public interface SuccessStoryRecyclerAdapterListener<T> extends BaseRecyclerAdapterListener<T> {

    void onLikeClicked(SuccessStory successStory, Like data, boolean isToAdd);
    void onShareClicked(SuccessStory successStory);


}
