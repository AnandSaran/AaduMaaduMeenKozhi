package com.aadu_maadu_kozhi.gregantech.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.UserPostCommentAdapter;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.customview.RecyclerViewMargin;
import com.aadu_maadu_kozhi.gregantech.databinding.BottomsheetUserPostCommentBinding;
import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;
import com.aadu_maadu_kozhi.gregantech.model.pojo.UserPost;
import com.aadu_maadu_kozhi.gregantech.model.pojo.UserPostComment;
import com.aadu_maadu_kozhi.gregantech.util.FireStoreKey;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;
import com.bluelinelabs.logansquare.LoganSquare;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_POST_COMMENTED_DATE_AND_TIME;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_POST_ID;

public class UserPostCommentBottomSheet extends BaseDialogFragment implements BaseRecyclerAdapterListener<UserPostComment> {
    private final String TAG = UserPostCommentBottomSheet.class.getSimpleName();
    BottomsheetUserPostCommentBinding binding;
    List<UserPostComment> userPostCommentList = new ArrayList<>();
    UserPostCommentAdapter userPostCommentAdapter;
    private User user;
    private UserPost userPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.bottomsheet_user_post_comment, container, false);
        binding.setHandlers(this);
        setAdapter();
        if (getArguments() != null) {
            user = getArguments().getParcelable(Constants.BundleKey.USER);
            userPost = getArguments().getParcelable(Constants.BundleKey.USER_POST);
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
        userPostCommentList.clear();
        binding.rvCommentList.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewMargin decoration = new RecyclerViewMargin(20, 1);
        binding.rvCommentList.addItemDecoration(decoration);
        binding.rvCommentList.setItemAnimator(new DefaultItemAnimator());
        userPostCommentAdapter = new UserPostCommentAdapter(userPostCommentList, this);
        binding.rvCommentList.setAdapter(userPostCommentAdapter);
        getUserPostComments(getArguments().getString(Constants.BundleKey.USER_POST_ID));

    }


    private void getUserPostComments(String user_post_id) {
        Log.e(TAG, user_post_id);
        getFireStoreDb().collection(FireStoreKey.FS_COL_USER_POST_COMMENTS)
                .whereEqualTo(FS_KEY_POST_ID, user_post_id).orderBy(FS_KEY_POST_COMMENTED_DATE_AND_TIME, Query.Direction.ASCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if (task.isSuccessful()) {
                    userPostCommentList.addAll(task.getResult().toObjects(UserPostComment.class));
                    if (userPostCommentList.size() > 0) {
                        binding.tvMessage.setVisibility(View.GONE);
                        //   binding.rvCommentList.setVisibility(View.VISIBLE);

                        userPostCommentAdapter.notifyDataSetChanged();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.rvCommentList.smoothScrollToPosition(userPostCommentList.size());

                            }
                        }, 500);


                    } else {
                        //  binding.rvCommentList.setVisibility(View.GONE);

                        binding.tvMessage.setVisibility(View.VISIBLE);

                        binding.tvMessage.setText(getActivity().getString(R.string.be_the_first_to_comment_this_post));
                    }
                } else {
                    binding.tvMessage.setVisibility(View.VISIBLE);
                    //  binding.rvCommentList.setVisibility(View.GONE);

                    binding.tvMessage.setText(getActivity().getString(R.string.be_the_first_to_comment_this_post));

                }
            }
        });

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnComment:
                if (!binding.edtComments.getText().toString().trim().isEmpty()) {
                    UserPostComment userPostComment = new UserPostComment();
                    userPostComment.setCommented_user_id(SharedPref.getInstance().getUserId(getActivity()));
                    userPostComment.setCommented_user_name(SharedPref.getInstance().getUserName(getActivity()));
                    userPostComment.setCommented_user_profile_url(SharedPref.getInstance().getUserProfileUrl(getActivity()));
                    userPostComment.setPost_id(getArguments().getString(Constants.BundleKey.USER_POST_ID));
                    userPostComment.setComments(binding.edtComments.getText().toString());
                    userPostComment.setPost_commented__local_date_and_time(new Date());
                    userPostCommentList.add(userPostComment);
                    userPostCommentAdapter.notifyItemInserted(userPostCommentList.size());
                    binding.rvCommentList.smoothScrollToPosition(userPostCommentList.size());
                    binding.edtComments.setText(null);
                    sendCommentPushNotification(userPostComment);
                    getFireStoreDb().collection(FireStoreKey.FS_COL_USER_POST_COMMENTS).add(userPostComment).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                if (getActivity() != null) {
                                    // showMessage(getString(R.string.successfully_commented));
                                }
                            }
                        }
                    });
                    binding.tvMessage.setVisibility(View.GONE);

                } else {
                    if (getActivity() != null) {

                        showErrorMessage(getString(R.string.enter_comments));
                    }

                }


                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClickItem(UserPostComment data) {

    }

    @Override
    public void onClickItem(View itemView, UserPostComment data) {


    }

    public void setUserPostId(String id) {

    }

    private void sendCommentPushNotification(final UserPostComment userPostComment) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (user != null && user.getToken() != null && !user.getUser_id().equals(SharedPref.getInstance().getUserId(getActivity()))) {
                    try {
                        String jsonString = LoganSquare.serialize(userPostComment)/*.replace(",", ",\n").replace("{", "{\n").replace("}", "}\n")*/;
                        String jsonStringUserPost = LoganSquare.serialize(userPost)/*.replace(",", ",\n").replace("{", "{\n").replace("}", "}\n")*/;
                        String jsonStringUser = LoganSquare.serialize(user)/*.replace(",", ",\n").replace("{", "{\n").replace("}", "}\n")*/;

                        Map<String, Object> newCall = new HashMap<>();
                        newCall.put(Constants.NotificationKey.UserPostComment, jsonString);
                        newCall.put(Constants.NotificationKey.For, Constants.NotificationKey.UserPostComment);
                        newCall.put(Constants.NotificationKey.UserPost, jsonStringUserPost);
                        newCall.put(Constants.NotificationKey.User, jsonStringUser);

                        sendPushNotification(user.getToken(), newCall);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }).start();

    }
}