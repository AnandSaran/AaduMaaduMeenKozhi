package com.aadu_maadu_kozhi.gregantech.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.ChatMessageAdapter;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.customview.RecyclerViewMargin;
import com.aadu_maadu_kozhi.gregantech.databinding.BottomsheetChatBinding;
import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ChatMessage;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;
import com.aadu_maadu_kozhi.gregantech.util.FireStoreKey;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;
import com.bluelinelabs.logansquare.LoganSquare;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_USER_CHAT_SUB;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_CHAT_ID;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_CHAT_MESSAGE_DATE_AND_TIME;

public class ChatBottomSheet extends BaseDialogFragment implements BaseRecyclerAdapterListener<ChatMessage> {
    private final String TAG = ChatBottomSheet.class.getSimpleName();
    BottomsheetChatBinding binding;
    ChatMessageAdapter chatMessageAdapter;
    List<ChatMessage> chatMessages = new ArrayList<>();
    private String chatId = null;
    private List<String> chatUsers = new ArrayList<>();
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.bottomsheet_chat, container, false);
        binding.setHandlers(this);
        setAdapter();
        if (getArguments() != null) {
            user = getArguments().getParcelable(Constants.BundleKey.USER);
        }
        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialog);
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    private void setAdapter() {
        chatMessages.clear();
        binding.rvChatList.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewMargin decoration = new RecyclerViewMargin(20, 1);
        binding.rvChatList.addItemDecoration(decoration);
        binding.rvChatList.setItemAnimator(new DefaultItemAnimator());
        chatMessageAdapter = new ChatMessageAdapter(chatMessages, this, getActivity());
        binding.rvChatList.setAdapter(chatMessageAdapter);
        getChat(getArguments().getString(Constants.BundleKey.FIRST_USER_ID), getArguments().getString(Constants.BundleKey.SECOND_USER_ID));

    }

    private void getChat(String first_user_id, String second_user_id) {


        int compare = first_user_id.compareTo(second_user_id);
        if (compare < 0) {
            chatId = first_user_id + "_" + second_user_id;
            chatUsers.add(first_user_id);
            chatUsers.add(second_user_id);
        } else if (compare > 0) {
            chatId = second_user_id + "_" + first_user_id;
            chatUsers.add(second_user_id);
            chatUsers.add(first_user_id);

        } else {
            chatId = first_user_id + "_" + second_user_id;
            chatUsers.add(first_user_id);
            chatUsers.add(second_user_id);

        }
        if (chatId == null) {
            showErrorMessage("Something went wrong");
        } else {
            addChatListener();

          /*  getFireStoreDb().collection(FireStoreKey.FS_COL_USER_CHAT).
                    orderBy(FS_KEY_CHAT_MESSAGE_DATE_AND_TIME, Query.Direction.ASCENDING)
                    .whereEqualTo(FS_KEY_CHAT_ID, chatId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    chatMessages.clear();
                    if (task.isSuccessful()) {
                       chatMessages.addAll(task.getResult().toObjects(ChatMessage.class));
                        binding.tvMessage.setVisibility(View.GONE);
                        binding.rvChatList.setVisibility(View.VISIBLE);
                        chatMessageAdapter.notifyDataSetChanged();
                        addChatListener();
                    } else {
                        Log.e(TAG,task.getException().toString());
                        Log.e(TAG,task.getResult().toString());
                        showErrorMessage("Something went wrong");

                    }
                }
            });
*/





                          /*  new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.getDocuments() == null || queryDocumentSnapshots.getDocuments().size() == 0) {
                        binding.tvMessage.setText(getActivity().getString(R.string.start_chat));

                        return;
                    }

                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        chatMessages.add(document.toObject(ChatMessage.class));

                    }
                    chatMessageAdapter.notifyDataSetChanged();
                    addChatListener();
                    if (chatMessages.size() > 0) {
                        binding.tvMessage.setVisibility(View.GONE);
                        binding.rvChatList.setVisibility(View.VISIBLE);

                    } else {
                        binding.rvChatList.setVisibility(View.GONE);

                        binding.tvMessage.setVisibility(View.VISIBLE);

                        binding.tvMessage.setText(getActivity().getString(R.string.be_the_first_to_comment_this_post));
                    }
                }
            });

*/


                   /*new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        showErrorMessage("Something went wrong");

                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Log.d(TAG, "Current data: " + documentSnapshot.getData());
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });*/

         /*   get().(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                           // showInfoMessage("Creating chat");
                            //createChatRoom();

                        }
                    } else {
                        showErrorMessage("Something went wrong");

                    }
                }
            });*/


        }


    }

    private void addChatListener() {
        getFireStoreDb().collection(FireStoreKey.FS_COL_USER_CHAT).document(chatId).collection(FireStoreKey.FS_COL_USER_CHAT_SUB)
                .orderBy(FS_KEY_CHAT_MESSAGE_DATE_AND_TIME, Query.Direction.ASCENDING)

                .whereEqualTo(FS_KEY_CHAT_ID, chatId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    //   Log.e(TAG,e.getMessage());
                    showErrorMessage("Something went wrong");

                    return;
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    Log.d(TAG, "Current data: " + dc.getDocument().getData());
                    switch (dc.getType()) {
                        case ADDED:
                            chatMessages.add(dc.getDocument().toObject(ChatMessage.class));
                            chatMessageAdapter.notifyItemInserted(chatMessages.size());
                            binding.rvChatList.smoothScrollToPosition(chatMessages.size());

                            Log.d(TAG, "New city: " + dc.getDocument().getData());
                            break;
                        case MODIFIED:
                            Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                            Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                            break;
                    }
                }


                if (chatMessages.size() > 0) {
                    binding.tvMessage.setVisibility(View.GONE);
                    //  binding.rvChatList.setVisibility(View.VISIBLE);

                } else {
                    //binding.rvChatList.setVisibility(View.INVISIBLE);

                    binding.tvMessage.setVisibility(View.VISIBLE);

                    binding.tvMessage.setText(getActivity().getString(R.string.start_chat));
                }
            }
        });
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnChatSend:
                if (!binding.edtChatMessage.getText().toString().trim().isEmpty()) {

                    final ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setSenderId(SharedPref.getInstance().getUserId(getActivity()));
                    chatMessage.setChat_id(chatId);
                    chatMessage.setChat_users(chatUsers);
                    chatMessage.setMessage(binding.edtChatMessage.getText().toString());
                    chatMessage.setSenderName(SharedPref.getInstance().getUserName(getActivity()));
                    sendChatPushNotification(chatMessage,user);

                    getFireStoreDb().collection(FireStoreKey.FS_COL_USER_CHAT).document(chatId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {

                            } else {
                                getFireStoreDb().collection(FireStoreKey.FS_COL_USER_CHAT).document(chatId).set(chatMessage);

                                Log.e(TAG, "Not Exits");
                            }
                        }
                    });

                    getFireStoreDb().collection(FireStoreKey.FS_COL_USER_CHAT).document(chatId).collection(FS_COL_USER_CHAT_SUB)
                            .add(chatMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                        }
                    });
                    binding.edtChatMessage.setText(null);

                } else {
                    if (getActivity() != null) {

                        showErrorMessage(getString(R.string.enter_message));
                    }

                }
                break;

        }
    }

    private void sendChatPushNotification(final ChatMessage chatMessage, final User user) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (ChatBottomSheet.this.user != null && ChatBottomSheet.this.user.getToken() != null && !ChatBottomSheet.this.user.getUser_id().equals(SharedPref.getInstance().getUserId(getActivity()))) {
                    try {
                        String jsonString = LoganSquare.serialize(chatMessage)/*.replace(",", ",\n").replace("{", "{\n").replace("}", "}\n")*/;
                        String jsonStringUser = LoganSquare.serialize(user)/*.replace(",", ",\n").replace("{", "{\n").replace("}", "}\n")*/;

                        Map<String, Object> newCall = new HashMap<>();
                        newCall.put(Constants.NotificationKey.UserChat, jsonString);
                        newCall.put(Constants.NotificationKey.User, jsonStringUser);
                        newCall.put(Constants.NotificationKey.For, Constants.NotificationKey.UserChat);

                        sendPushNotification(ChatBottomSheet.this.user.getToken(), newCall);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }).start();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClickItem(ChatMessage data) {

    }

    @Override
    public void onClickItem(View itemView, ChatMessage data) {


    }

    public void setUserPostId(String id) {

    }
}