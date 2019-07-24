package com.aadu_maadu_kozhi.gregantech.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.adapter.viewholder.ChatMessageReceiverViewHolder;
import com.aadu_maadu_kozhi.gregantech.adapter.viewholder.ChatMessageSenderViewHolder;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateChatMessageReceiverBinding;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateChatMessageSenderBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ChatMessage;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;

import java.util.List;


;

/**
 * Created by Anand on 8/27/2017.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int SENT = 0;
    private static final int RECEIVED = 1;
    protected String TAG = getClass().getSimpleName();
    private List<ChatMessage> data;
    private BaseRecyclerAdapterListener<ChatMessage> listener;
    private Context context;

    public ChatMessageAdapter(List<ChatMessage> data, BaseRecyclerAdapterListener<ChatMessage> listener, Context context) {
        this.listener = listener;
        this.context = context;
        this.data = data;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SENT) {
            InflateChatMessageSenderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.inflate_chat_message_sender, parent, false);
            return new ChatMessageSenderViewHolder(binding, listener);
        } else {
            InflateChatMessageReceiverBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.inflate_chat_message_receiver, parent, false);
            return new ChatMessageReceiverViewHolder(binding, listener);
        }


    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getSenderId().contentEquals(SharedPref.getInstance().getUserId(context))) {
            return SENT;
        } else {
            return RECEIVED;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChatMessageSenderViewHolder) {
            ChatMessageSenderViewHolder viewHolder = (ChatMessageSenderViewHolder) holder;

            viewHolder.setData(getItem(position));
        } else if (holder instanceof ChatMessageReceiverViewHolder) {
            ChatMessageReceiverViewHolder viewHolder = (ChatMessageReceiverViewHolder) holder;

            viewHolder.setData(getItem(position));
        }
    }


    ChatMessage getItem(int position) throws IndexOutOfBoundsException {
        return data.get(position);
    }

    public void addItem(ChatMessage object) {
        data.add(object);
        notifyItemInserted(data.indexOf(object));
    }

    public void removeItem(int position) throws IndexOutOfBoundsException {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void resetItems(List<ChatMessage> data) {
        if (data != null) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    public void addItems(List<ChatMessage> data) {
        if (data != null) {
            int startRange = (this.data.size() - 1) > 0 ? data.size() - 1 : 0;
            this.data.addAll(data);
            notifyItemRangeChanged(startRange, data.size());
        }
    }
}
