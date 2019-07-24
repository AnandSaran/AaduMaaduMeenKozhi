package com.aadu_maadu_kozhi.gregantech.adapter.listener;


import com.aadu_maadu_kozhi.gregantech.model.pojo.ChatMessage;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;

public interface ChatItemRecyclerAdapterListener<T> extends BaseRecyclerAdapterListener<T> {

    void onChatClicked(ChatMessage userPost, User user);


}
