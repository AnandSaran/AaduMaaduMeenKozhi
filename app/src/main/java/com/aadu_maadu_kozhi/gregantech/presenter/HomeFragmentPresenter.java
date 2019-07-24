package com.aadu_maadu_kozhi.gregantech.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AbsListView;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.UserPostAdapter;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.UserPostRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.databinding.FragmentHomeBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.Like;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;
import com.aadu_maadu_kozhi.gregantech.model.pojo.UserPost;
import com.aadu_maadu_kozhi.gregantech.presenter.ipresenter.IHomeFragmentPresenter;
import com.aadu_maadu_kozhi.gregantech.util.FireStoreKey;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;
import com.aadu_maadu_kozhi.gregantech.view.activity.CreatePostActivity;
import com.aadu_maadu_kozhi.gregantech.view.fragment.ChatBottomSheet;
import com.aadu_maadu_kozhi.gregantech.view.fragment.UserPostCommentBottomSheet;
import com.aadu_maadu_kozhi.gregantech.view.iview.IHomeFragmentView;
import com.bluelinelabs.logansquare.LoganSquare;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_USER_POST;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_USER_POST_LIKES;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_AREA_NAME;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_POST_CREATED_DATE_AND_TIME;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_POST_LIKED_LIST;
import static com.facebook.FacebookSdk.getApplicationContext;




/**
 * Created by Anand on 8/27/2017.
 */

public class HomeFragmentPresenter extends BasePresenter implements IHomeFragmentPresenter, View.OnClickListener, UserPostRecyclerAdapterListener<UserPost> {
    List<UserPost> userPosts = new ArrayList<>();
    int UserPostPaginationLimit = 5;
    // int CurrentPagination = UserPostPaginationLimit;
    private IHomeFragmentView iHomeFragmentView;
    private FragmentHomeBinding binding;
    private UserPostAdapter userPostAdapter;
    private UserPostCommentBottomSheet userPostCommentBottomSheet;
    private ChatBottomSheet chatBottomSheet;
    private DocumentSnapshot lastVisibleDocumentSnapShot;
    private boolean isScrolling;
    private boolean isLastItemReached;
    private String area;
    private boolean mIsHiding;

    public HomeFragmentPresenter(IHomeFragmentView iHomeFragmentView, FragmentHomeBinding binding) {
        super(iHomeFragmentView);
        this.iHomeFragmentView = iHomeFragmentView;
        this.binding = binding;
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorPrimary);
        binding.swipeRefreshLayout.setRefreshing(true);

        setSwipeToRefreshListner();
    }

    private void setSwipeToRefreshListner() {
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUSerPost(area);
            }
        });
    }


    @Override
    public void onCreatePresenter(Bundle bundle) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


    @Override
    public void getUSerPost(String areaName) {
        area = areaName;
        Query query = null;
        if (areaName == null) {
            query = iHomeFragmentView.getFireStoreDb().collection(FireStoreKey.FS_COL_USER_POST).orderBy(FS_KEY_POST_CREATED_DATE_AND_TIME, Query.Direction.DESCENDING)/*.limit(UserPostPaginationLimit)*/;
        } else {
            query = iHomeFragmentView.getFireStoreDb().collection(FireStoreKey.FS_COL_USER_POST).orderBy(FS_KEY_POST_CREATED_DATE_AND_TIME, Query.Direction.DESCENDING).whereEqualTo(FS_KEY_AREA_NAME, areaName)/*.limit(UserPostPaginationLimit)*/;

        }
      /*  query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                }else {
                    Log.e(TAG,task.getException().toString());
                }
            }
        });*/
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                userPosts.clear();
                binding.swipeRefreshLayout.setRefreshing(false);
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    final UserPost userPost = document.toObject(UserPost.class);
                    userPost.setId(document.getId());


                    userPosts.add(userPost);
                }
                if (queryDocumentSnapshots.size() != 0) {
                    lastVisibleDocumentSnapShot = queryDocumentSnapshots.getDocuments()
                            .get(queryDocumentSnapshots.size() - 1);
                }
                binding.pbPagination.setVisibility(View.GONE);

                if (userPostAdapter == null) {
                    userPostAdapter = new UserPostAdapter(userPosts, HomeFragmentPresenter.this);
                    binding.rvUserPost.setLayoutManager(new LinearLayoutManager(iHomeFragmentView.getActivity()));
                    binding.rvUserPost.setItemAnimator(new DefaultItemAnimator());
                    binding.rvUserPost.setAdapter(userPostAdapter);


                } else {
                    userPostAdapter.notifyDataSetChanged();
                }
                RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                            isScrolling = true;
                        }
                      /*  if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                            // Hiding FAB
                            hideFabWithViewAnimation();
                        } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            // Showing FAB
                            showFabWithViewAnimation();
                        }*/
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                     /*   LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                        int visibleItemCount = linearLayoutManager.getChildCount();
                        int totalItemCount = linearLayoutManager.getItemCount();

                        if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
                            isScrolling = false;
                            Date date = new Date();
                            date.setHours(0);
                            date.setMinutes(0);
                            date.setSeconds(0);
                            date.setDate(14);
                            date.setMonth(9);
                            date.setYear(1018);
                            Query nextQuery = iHomeFragmentView.getFireStoreDb().collection(FireStoreKey.FS_COL_USER_POST).orderBy(FS_KEY_POST_CREATED_DATE_AND_TIME, Query.Direction.DESCENDING)
                                    .startAfter(lastVisibleDocumentSnapShot).limit(UserPostPaginationLimit);
                            nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                    if (t.isSuccessful()) {
                                        for (DocumentSnapshot d : t.getResult()) {
                                            UserPost userPost = d.toObject(UserPost.class);
                                            userPosts.add(userPost);
                                            userPostAdapter.notifyItemInserted(userPosts.size());
                                        }
                                       // userPostAdapter.notifyDataSetChanged();
                                        if (t.getResult().size()>0) {
                                            lastVisibleDocumentSnapShot = t.getResult().getDocuments().get(t.getResult().size() - 1);
                                        }
                                        if (t.getResult().size() < UserPostPaginationLimit) {
                                            isLastItemReached = true;
                                        }
                                    }
                                }
                            });
                        }*/
                    }
                };
                binding.rvUserPost.addOnScrollListener(onScrollListener);

            }
        });

       /* query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot lastVisible = documentSnapshots.getDocuments()
                            .get(documentSnapshots.size() -1);
                    for (DocumentSnapshot document : task.getResult()) {
                        final UserPost userPost = document.toObject(UserPost.class);
                        userPost.setId(document.getId());


                        userPosts.add(userPost);
                    }
                    binding.pbPagination.setVisibility(View.GONE);

                    if (userPostAdapter == null) {
                        userPostAdapter = new UserPostAdapter(userPosts, HomeFragmentPresenter.this);
                        binding.rvUserPost.setLayoutManager(new LinearLayoutManager(iHomeFragmentView.getActivity()));
                        binding.rvUserPost.setItemAnimator(new DefaultItemAnimator());
                        binding.rvUserPost.setAdapter(userPostAdapter);


                    } else {
                        userPostAdapter.notifyDataSetChanged();
                    }

                } else {
                    iHomeFragmentView.showSnackBar("Error getting data.");
                }
            }
        });*/
    }

    public void showFabWithViewAnimation() {

        if (binding.fabCreatePost.getVisibility() != View.VISIBLE || mIsHiding) {

            binding.fabCreatePost.clearAnimation();
            binding.fabCreatePost.setVisibility(View.VISIBLE);

            Animation anim = android.view.animation.AnimationUtils.loadAnimation(
                    binding.fabCreatePost.getContext(), R.anim.fab_in);
            anim.setDuration(400);
            anim.setInterpolator(new OvershootInterpolator());
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    binding.fabCreatePost.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            binding.fabCreatePost.startAnimation(anim);
        }
    }

    private void hideFabWithViewAnimation() {
        if (mIsHiding || binding.fabCreatePost.getVisibility() != View.VISIBLE) {
            return;
        }

        mIsHiding = true;
        Animation anim = AnimationUtils.loadAnimation(
                binding.fabCreatePost.getContext(), R.anim.fab_out);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(200);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIsHiding = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsHiding = false;
                binding.fabCreatePost.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        binding.fabCreatePost.startAnimation(anim);
    }


    @Override
    public void onLikeClicked(UserPost userPost, Like like, boolean isToAdd, User user) {
        iHomeFragmentView.getFireStoreDb().collection(FS_COL_USER_POST).document(userPost.getId()).update(FS_KEY_POST_LIKED_LIST, userPost.getPost_liked_list());

        if (isToAdd) {
            sendLikePushNotification(userPost, user);

            iHomeFragmentView.getFireStoreDb().collection(FS_COL_USER_POST_LIKES).document(userPost.getId() + "_" + like.getLiked_user_id()).set(like).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        iHomeFragmentView.showMessage("Post liked successfully");


                    } else {
                        iHomeFragmentView.showMessage("Something went wrong");
                    }
                }
            });

        } else {
            iHomeFragmentView.getFireStoreDb().collection(FS_COL_USER_POST_LIKES).document(userPost.getId() + "_" + like.getLiked_user_id()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        iHomeFragmentView.showMessage("Removed Post like");


                    } else {
                        iHomeFragmentView.showMessage("Something went wrong");
                    }
                }
            });

        }
    }


    @Override
    public void onCommentClicked(UserPost userPost, User user) {
        /* if (userPostCommentBottomSheet==null) {*/
        userPostCommentBottomSheet = new UserPostCommentBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BundleKey.USER_POST_ID, userPost.getId());
        bundle.putParcelable(Constants.BundleKey.USER, user);
        bundle.putParcelable(Constants.BundleKey.USER_POST, userPost);
        userPostCommentBottomSheet.setArguments(bundle);
        userPostCommentBottomSheet.show(iHomeFragmentView.getActivity().getSupportFragmentManager(), userPostCommentBottomSheet.getTag());
      /*  }else {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.BundleKey.USER_POST_ID, userPost.getId());

            userPostCommentBottomSheet.setArguments(bundle);
            userPostCommentBottomSheet.show(iHomeFragmentView.getActivity().getSupportFragmentManager(), userPostCommentBottomSheet.getTag());

        }*/
    }

    @Override
    public void onChatClicked(UserPost userPost, User user) {
        chatBottomSheet = new ChatBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BundleKey.FIRST_USER_ID, userPost.getUser_id());
        bundle.putString(Constants.BundleKey.SECOND_USER_ID, SharedPref.getInstance().getUserId(iHomeFragmentView.getActivity()));
        bundle.putParcelable(Constants.BundleKey.USER, user);

        chatBottomSheet.setArguments(bundle);
        chatBottomSheet.show(iHomeFragmentView.getActivity().getSupportFragmentManager(), chatBottomSheet.getTag());


    }

    @Override
    public void onShareClicked(UserPost userPost) {
        if (userPost.getPost_image_path() != null && userPost.getPost_image_path().size() > 0) {
            DownloadImageFromCacheTask DownloadImageFromCacheTask = new DownloadImageFromCacheTask(iHomeFragmentView.getActivity(), userPost.getPost_content());
            DownloadImageFromCacheTask.execute(userPost.getPost_image_path());
        } else {
            sharePostContent(userPost.getPost_content());
        }
    }

    @Override
    public void onTrashClicked(final UserPost userPost, User user) {

        AlertDialog.Builder alert = new AlertDialog.Builder(
                iHomeFragmentView.getActivity());
        alert.setTitle("Delete!!");
        alert.setMessage("Are you sure to delete post");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do your work here
                dialog.dismiss();
                iHomeFragmentView.getFireStoreDb().collection(FS_COL_USER_POST).document(userPost.getId()).delete();
                int index = userPosts.indexOf(userPost);
                userPostAdapter.removeItem(index);


            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alert.show();
    }


    @Override
    public void onClickItem(UserPost data) {
        ArrayList<String> imagePath = new ArrayList<>(data.getPost_image_path());
        Intent intent = new Intent(iHomeFragmentView.getActivity(), CreatePostActivity.class);
        intent.putStringArrayListExtra(Constants.BundleKey.IMAGE_PATH, imagePath);
        intent.putExtra(Constants.BundleKey.POST_CONTENT, data.getPost_content());
        iHomeFragmentView.getActivity().startActivity(intent);
    }

    @Override
    public void onClickItem(View itemView, UserPost data) {

    }

    private void sharePostContent(String post_content) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, iHomeFragmentView.getActivity().getString(R.string.app_name));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, post_content + "\n\n" + "ஆடு மாடு மீன் கோழி செயலியை பதிவிறக்கம் செய்ய: \n https://play.google.com/store/apps/details?id=com.aadu_maadu_meen_kozhi.drapjinfotech");
        iHomeFragmentView.getActivity().startActivity(Intent.createChooser(sharingIntent, iHomeFragmentView.getActivity().getString(R.string.app_name)));
    }

    private void sendLikePushNotification(final UserPost userPost, final User user) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (user != null && user.getToken() != null && !user.getUser_id().equals(SharedPref.getInstance().getUserId(iHomeFragmentView.getActivity()))) {
                    try {
                        String jsonString = LoganSquare.serialize(userPost)/*.replace(",", ",\n").replace("{", "{\n").replace("}", "}\n")*/;

                        Map<String, Object> newCall = new HashMap<>();
                        newCall.put(Constants.NotificationKey.UserPostLike, jsonString);
                        newCall.put(Constants.NotificationKey.LikedUserName, SharedPref.getInstance().getUserName(iHomeFragmentView.getActivity()));
                        newCall.put(Constants.NotificationKey.For, Constants.NotificationKey.UserPostLike);

                        iHomeFragmentView.sendPushNotification(user.getToken(), newCall);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }).start();
    }

    private class DownloadImageFromCacheTask extends AsyncTask<List<String>, Void, List<File>> {
        private final Context context;
        private final String post_content;

        public DownloadImageFromCacheTask(Context context, String post_content) {
            this.context = context;
            this.post_content = post_content;
        }

      /*  @Override
        protected File doInBackground(String... params) {

            FutureTarget<File> future = Glide.with(iHomeFragmentView.getActivity())
                    .load(params[0])
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

            File file = null;
            try {
                file = future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return file;
        }*/

        @Override
        protected List<File> doInBackground(List<String>... lists) {
            List<File> fileList = new ArrayList<>();
            List<String> imagePath = lists[0];
            for (String s :
                    imagePath) {
                FutureTarget<File> future = Glide.with(iHomeFragmentView.getActivity())
                        .load(s)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

                File file = null;
                try {
                    file = future.get();
                    fileList.add(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            return fileList;
        }

        @Override
        protected void onPostExecute(List<File> result) {
            ArrayList<Uri> uriList = new ArrayList<>();
            if (result == null) {
                return;
            }
            for (File file :
                    result) {
                uriList.add(FileProvider.getUriForFile(getApplicationContext(), "com.xxx.fileprovider", file));

            }
            share(uriList, post_content);
        }

        private void share(ArrayList<Uri> result, String post_content) {

            Intent waIntent = new Intent();
            waIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            waIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, result);
            waIntent.setType("image/*");
            waIntent.putExtra(Intent.EXTRA_TEXT, post_content + "\n\n" + "ஆடு மாடு மீன் கோழி செயலியை பதிவிறக்கம் செய்ய: \n https://play.google.com/store/apps/details?id=com.aadu_maadu_meen_kozhi.drapjinfotech");
            //   waIntent.putExtra(Intent.EXTRA_STREAM, result);
            //  waIntent.setPackage("com.whatsapp");
            //iHomeFragmentView.getActivity().startActivity(waIntent);
            iHomeFragmentView.getActivity().startActivity(Intent.createChooser(waIntent, iHomeFragmentView.getActivity().getString(R.string.app_name)));

        }

    }

}
