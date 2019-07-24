package com.aadu_maadu_kozhi.gregantech.adapter.viewholder;

import android.view.View;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateChatMessageReceiverBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ChatMessage;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;
import com.aadu_maadu_kozhi.gregantech.util.DateTimeUtils;
import com.aadu_maadu_kozhi.gregantech.util.FireStoreKey;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;

import javax.annotation.Nullable;

/**
 * Created by Anand on 8/27/2017.
 */

public class ChatMessageReceiverViewHolder extends BaseViewHolder<ChatMessage> implements View.OnClickListener {
    private BaseRecyclerAdapterListener<ChatMessage> listener;
    private InflateChatMessageReceiverBinding binding;

    public ChatMessageReceiverViewHolder(InflateChatMessageReceiverBinding itemView, BaseRecyclerAdapterListener<ChatMessage> listener) {
        super(itemView.getRoot());
        this.listener = listener;
        binding = itemView;
        binding.setHandlers(this);
        bindHolder();
    }

    private void bindHolder() {


    }

    @Override
    void populateData(ChatMessage data) {
        binding.tvComments.setText(data.getMessage());
        if (data.getChat_message_date_and_time() != null) {
            Date date = data.getChat_message_date_and_time().toDate();
            binding.tvChatMessageTime.setText(DateTimeUtils.getInstance().getTimeAgo(date.getTime()));
        } else {
            binding.tvChatMessageTime.setText("Just now");
        }
        FirebaseFirestore.getInstance().collection(FireStoreKey.FS_COL_USER).document(data.getSenderId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot == null) {
                    return;
                } else {
                    try {

                        User user = documentSnapshot.toObject(User.class);
                        if (user.isIs_online()) {
                            binding.vOnline.setVisibility(View.VISIBLE);
                        } else {
                            binding.vOnline.setVisibility(View.GONE);

                        }

                        String url = user.getImage_url() == null ? itemView.getContext().getString(R.string.default_user_profile_img_url) : user.getImage_url();
                        Glide.with(itemView.getContext()).load(url)
                                .apply(RequestOptions.placeholderOf(R.drawable.profile_pic).error(R.drawable.profile_pic).circleCropTransform())
                                .into(binding.ivUserProfile);

                        String name = user.getName() == null ? "New User" : user.getName();
                        binding.tvUserName.setText(name);
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {

    }
}
