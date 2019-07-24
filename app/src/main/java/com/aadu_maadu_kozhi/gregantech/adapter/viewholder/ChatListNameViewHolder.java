package com.aadu_maadu_kozhi.gregantech.adapter.viewholder;

import android.view.View;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.ChatItemRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.databinding.InflateChatListNameBinding;
import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ChatMessage;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;
import com.aadu_maadu_kozhi.gregantech.util.FireStoreKey;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

import javax.annotation.Nullable;

import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_READ;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_SENDER_ID;

public class ChatListNameViewHolder extends BaseViewHolder<ChatMessage> implements View.OnClickListener {

    private ChatItemRecyclerAdapterListener<ChatMessage> listener;
    private InflateChatListNameBinding binding;
    private User user;

    public ChatListNameViewHolder(InflateChatListNameBinding itemView, ChatItemRecyclerAdapterListener<ChatMessage> listener) {
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
        for (String userId :
                data.getChat_users()) {
            if (!userId.equalsIgnoreCase(SharedPref.getInstance().getUserId(itemView.getContext()))) {
                FirebaseFirestore.getInstance().collection(FireStoreKey.FS_COL_USER).document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot == null) {
                            return;
                        } else {
                            try {

                                user = documentSnapshot.toObject(User.class);
                                user.setUser_id(documentSnapshot.getId());

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
            } else {


                Task firstTask = FirebaseFirestore.getInstance().collection(FireStoreKey.FS_COL_USER_CHAT).document(data.getChat_id()).collection(FireStoreKey.FS_COL_USER_CHAT_SUB)
                        .whereGreaterThan(FS_KEY_SENDER_ID, userId).whereEqualTo(FS_KEY_READ, false).get();
                Task secondTask = FirebaseFirestore.getInstance().collection(FireStoreKey.FS_COL_USER_CHAT).document(data.getChat_id()).collection(FireStoreKey.FS_COL_USER_CHAT_SUB)
                        .whereLessThan(FS_KEY_SENDER_ID, userId).whereEqualTo(FS_KEY_READ, false).get();

                Task combinedTask = Tasks.whenAllSuccess(firstTask, secondTask).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> list) {
                        //Do what you need to do with your list

                        Log.e(TAG, "Size: " + list.size());


                    }
                });
          /*.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots == null) {
                            return;
                        } else {

                        }
                    }
                });*/


            }

        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvParent:
                listener.onChatClicked(data, user);
                break;
        }

    }
}
