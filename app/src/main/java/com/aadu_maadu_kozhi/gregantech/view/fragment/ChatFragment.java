package com.aadu_maadu_kozhi.gregantech.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.ChatListNameAdapter;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.ChatItemRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.databinding.FragmentChatBinding;
import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ChatMessage;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_USER_CHAT;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_CHAT_USERS;

public class ChatFragment extends BaseFragment implements BaseRecyclerAdapterListener<ChatMessage>, ChatItemRecyclerAdapterListener<ChatMessage> {
    private FragmentChatBinding binding;
    private View rootView;
    private ChatListNameAdapter chatListNameAdapter;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);
            rootView = binding.getRoot();
            rootView.setTag(binding);
        } else {
            binding = (FragmentChatBinding) rootView.getTag();
        }

        binding.setHandlers(this);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getChatList();


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.d(TAG, " is NOT on screen");
        } else {
            getChatList();

        }
    }

    private void getChatList() {
        getFireStoreDb().collection(FS_COL_USER_CHAT).whereArrayContains(FS_KEY_CHAT_USERS, SharedPref.getInstance().getUserId(getActivity())).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    binding.tvChatEmptyMessage.setVisibility(View.GONE);
                    setAdapter((List<ChatMessage>) task.getResult().toObjects(ChatMessage.class));

                } else {
                    binding.tvChatEmptyMessage.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void setAdapter(List<ChatMessage> chatMessages) {
        if (chatMessages == null || chatMessages.size() == 0) {
            binding.rvChat.setVisibility(View.GONE);

            binding.tvChatEmptyMessage.setVisibility(View.VISIBLE);


        } else {
            binding.tvChatEmptyMessage.setVisibility(View.GONE);
            binding.rvChat.setVisibility(View.VISIBLE);
        }
        if (chatListNameAdapter == null) {
          /*  DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);

            itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
*/
            binding.rvChat.setLayoutManager(new LinearLayoutManager(getActivity()));
            //   RecyclerViewMargin decoration = new RecyclerViewMargin(5, 1);
            //   binding.rvChat.addItemDecoration(decoration);
            binding.rvChat.setItemAnimator(new DefaultItemAnimator());
            chatListNameAdapter = new ChatListNameAdapter(chatMessages, this);
            binding.rvChat.setAdapter(chatListNameAdapter);
        } else {
            chatListNameAdapter.resetItems(chatMessages);
        }
    }

    @Override
    public void onClickItem(ChatMessage data) {


    }

    @Override
    public void onClickItem(View itemView, ChatMessage data) {

    }

    @Override
    public void onChatClicked(ChatMessage chatMessage, User user) {
        ChatBottomSheet chatBottomSheet = new ChatBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BundleKey.FIRST_USER_ID, chatMessage.getChat_users().get(0));
        bundle.putString(Constants.BundleKey.SECOND_USER_ID, chatMessage.getChat_users().get(1));
        bundle.putParcelable(Constants.BundleKey.USER, user);

        chatBottomSheet.setArguments(bundle);
        chatBottomSheet.show(getActivity().getSupportFragmentManager(), chatBottomSheet.getTag());

    }
}
