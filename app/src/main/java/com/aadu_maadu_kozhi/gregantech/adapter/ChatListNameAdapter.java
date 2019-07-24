package com.aadu_maadu_kozhi.gregantech.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.ChatItemRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.adapter.viewholder.ChatListNameViewHolder;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateChatListNameBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ChatMessage;

import java.util.List;

import androidx.databinding.DataBindingUtil;


/**
 * Created by Anand on 8/27/2017.
 */

public class ChatListNameAdapter extends BaseRecyclerAdapter<ChatMessage, ChatListNameViewHolder> {
    private ChatItemRecyclerAdapterListener<ChatMessage> listener;

    public ChatListNameAdapter(List<ChatMessage> data, ChatItemRecyclerAdapterListener<ChatMessage> listener) {
        super(data);
        this.listener = listener;

    }

    @Override
    public ChatListNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InflateChatListNameBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.inflate_chat_list_name, parent, false);
        return new ChatListNameViewHolder(binding, listener);

    }
}
