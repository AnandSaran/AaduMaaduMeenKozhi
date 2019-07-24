package com.aadu_maadu_kozhi.gregantech.adapter.listener;


import com.aadu_maadu_kozhi.gregantech.model.pojo.Accessories;
import com.aadu_maadu_kozhi.gregantech.model.pojo.Like;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;

public interface AccessoriesRecyclerAdapterListener<T> extends BaseRecyclerAdapterListener<T> {

    void onLikeClicked(Accessories accessories, Like data, boolean isToAdd);

    void onShareClicked(Accessories accessories);

    void onChatClicked(Accessories accessories, User user);


}
