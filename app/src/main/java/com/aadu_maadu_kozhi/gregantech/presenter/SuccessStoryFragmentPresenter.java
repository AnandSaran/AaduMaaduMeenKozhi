package com.aadu_maadu_kozhi.gregantech.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.SuccessStoryAdapter;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.SuccessStoryRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.databinding.FragmentSuccessStoryBinding;
import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.model.pojo.Like;
import com.aadu_maadu_kozhi.gregantech.model.pojo.SuccessStory;
import com.aadu_maadu_kozhi.gregantech.presenter.ipresenter.ISuccessStoryFragmentPresenter;
import com.aadu_maadu_kozhi.gregantech.util.CommonUtils;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;
import com.aadu_maadu_kozhi.gregantech.view.activity.ThagavalKalanchiyamDetailActivity;
import com.aadu_maadu_kozhi.gregantech.view.iview.ISuccessStoryFragmentView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_ADMIN_SUCCESS_STORY;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_SUCCESS_STORY;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_SUCCESS_STORY_LIKES;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_USER_POST_LIKES;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_POST_LIKED_LIST;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_SUCCESS_STORY_POST_CREATED_DATE_AND_TIME;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_USER_EMAIL_ID;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_SUCCSS_STORY_IMAGE_PATH;
import static com.facebook.FacebookSdk.getApplicationContext;

public class SuccessStoryFragmentPresenter extends BasePresenter implements ISuccessStoryFragmentPresenter, View.OnClickListener, BaseRecyclerAdapterListener<SuccessStory>, SuccessStoryRecyclerAdapterListener<SuccessStory> {
    private ISuccessStoryFragmentView iSuccessStoryFragmentView;
    private FragmentSuccessStoryBinding binding;
    private ImageView ivAdd;
    private ProgressBar pgProgressBar;
    private TextView tvAddOfferText;
    private ImageView ivDone;
    private String strImageUrl;
    private EditText edtTitle;
    private EditText edtContent;
    private AlertDialog dlgAddThagavalKalanchiyamHeading;
    private StorageReference storageReference;
    private SuccessStoryAdapter successStoryAdapter;

    public SuccessStoryFragmentPresenter(ISuccessStoryFragmentView iSuccessStoryFragmentView, FragmentSuccessStoryBinding binding) {
        super(iSuccessStoryFragmentView);
        this.iSuccessStoryFragmentView = iSuccessStoryFragmentView;
        this.binding = binding;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorPrimary);
        binding.swipeRefreshLayout.setRefreshing(true);
        setSwipeToRefreshListner();


    }

    private void setSwipeToRefreshListner() {
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSuccessStory();
            }
        });
    }

    @Override
    public void onCreatePresenter(Bundle bundle) {

    }

    @Override
    public void getSuccessStory() {
        binding.swipeRefreshLayout.setRefreshing(true);
        iSuccessStoryFragmentView.getFireStoreDb().collection(FS_COL_SUCCESS_STORY).orderBy(FS_KEY_SUCCESS_STORY_POST_CREATED_DATE_AND_TIME, Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                binding.swipeRefreshLayout.setRefreshing(false);

                if (task.isSuccessful()) {

                    List<SuccessStory> successStories = new ArrayList<>();

                    for (QueryDocumentSnapshot queryDocumentSnapshot :
                            task.getResult()) {
                        SuccessStory successStory = queryDocumentSnapshot.toObject(SuccessStory.class);
                        successStory.setId(queryDocumentSnapshot.getId());
                        successStories.add(successStory);

                    }
                    if (successStoryAdapter == null) {
                        successStoryAdapter = new SuccessStoryAdapter(successStories, SuccessStoryFragmentPresenter.this);

                        binding.rvSuccessStoryList.setLayoutManager(new LinearLayoutManager(iSuccessStoryFragmentView.getActivity()));
                        binding.rvSuccessStoryList.setItemAnimator(new DefaultItemAnimator());
                        binding.rvSuccessStoryList.setAdapter(successStoryAdapter);


                    } else {
                        successStoryAdapter.resetItems(successStories);
                    }
                    binding.rvSuccessStoryList.smoothScrollToPosition(0);
                } else {
                    Log.e(TAG, task.getException().toString());
                    iSuccessStoryFragmentView.showSnackBar(iSuccessStoryFragmentView.getActivity().getString(R.string.something_went_wrong));

                }

            }
        });

    }

    @Override
    public void showAddThagavalKalanchiyamHeadingDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(iSuccessStoryFragmentView.getActivity());
        LayoutInflater inflater = iSuccessStoryFragmentView.getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_thagaval_kalanchiyam_heading, null);
        //  temp = (ImageView) dialogView.findViewById(R.id.iv_news_image);
        dialogView.findViewById(R.id.dialog_add_offer_image).setOnClickListener(this);

        dialogView.findViewById(R.id.submit).setOnClickListener(this);
        ivAdd = (ImageView) dialogView.findViewById(R.id.add);
        pgProgressBar = (ProgressBar) dialogView.findViewById(R.id.progress_bar);
        tvAddOfferText = (TextView) dialogView.findViewById(R.id.add_image_text);
        ivDone = (ImageView) dialogView.findViewById(R.id.done);
        edtContent = (EditText) dialogView.findViewById(R.id.edtContent);
        edtContent.setVisibility(View.VISIBLE);

        edtTitle = (EditText) dialogView.findViewById(R.id.edtTitle);
        dialogBuilder.setView(dialogView);
        dlgAddThagavalKalanchiyamHeading = dialogBuilder.create();
        dlgAddThagavalKalanchiyamHeading.setCanceledOnTouchOutside(false);

        dlgAddThagavalKalanchiyamHeading.show();
    }

    @Override
    public void uploadFile(Uri filePath) {
        showImageUploadProgressBar();
        String path = FS_SUCCSS_STORY_IMAGE_PATH + "UserId:" + SharedPref.getInstance().getUserId(iSuccessStoryFragmentView.getActivity()) + " Date:" + new Date() + ".jpg";
        final StorageReference riversRef = storageReference.child(path);
        UploadTask uploadTask = riversRef.putFile(filePath);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();

                }

                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    hideImageUploadProgressBar(true);

                    Uri downloadUri = task.getResult();
                    strImageUrl = downloadUri.toString();
                } else {

                    hideImageUploadProgressBar(false);

                    iSuccessStoryFragmentView.showMessage(task.getException().getMessage());

                    // Handle failures
                    // ...
                }
            }
        });


    }

    @Override
    public void checkThagavalKalanchiyamAdmin() {
        iSuccessStoryFragmentView.getFireStoreDb().collection(FS_COL_ADMIN_SUCCESS_STORY)
                .whereEqualTo(FS_KEY_USER_EMAIL_ID, SharedPref.getInstance().getUserEmailId(iSuccessStoryFragmentView.getActivity())).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null && task.getResult().size() > 0) {
                    iSuccessStoryFragmentView.setVisibleAddFab();
                }
            }
        });
    }

    private void hideImageUploadProgressBar(boolean success) {
        pgProgressBar.setVisibility(View.GONE);
        if (success) {
            tvAddOfferText.setText("Image uploaded");
            ivAdd.setVisibility(View.GONE);
            ivDone.setVisibility(View.VISIBLE);


        } else {
            tvAddOfferText.setText("Image upload failed.Try again");
            ivDone.setVisibility(View.GONE);
            ivAdd.setVisibility(View.VISIBLE);

        }

    }


    private void showImageUploadProgressBar() {
        ivAdd.setVisibility(View.GONE);
        pgProgressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (validateForm()) {
                    addSuccessStoryToFs();
                    dlgAddThagavalKalanchiyamHeading.dismiss();

                }
                break;
            case R.id.dialog_add_offer_image:

                iSuccessStoryFragmentView.choiceAvatarFromGallery();
                break;
        }
    }

    private void addSuccessStoryToFs() {
        SuccessStory successStory = new SuccessStory();
        successStory.setTitle(edtTitle.getText().toString());
        successStory.setContent(edtContent.getText().toString());
        successStory.setImageUrl(strImageUrl);
        successStory.setWhoCreated(SharedPref.getInstance().getUserId(iSuccessStoryFragmentView.getActivity()));
        iSuccessStoryFragmentView.getFireStoreDb().collection(FS_COL_SUCCESS_STORY).add(successStory).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                getSuccessStory();
            }
        });
        edtTitle.setText(null);
        strImageUrl = null;
    }

    private boolean validateForm() {
        int count = 0;

        if (!CommonUtils.getInstance().isNullCheck(edtTitle.getText().toString())) {
            edtTitle.setError("Enter title");
            count++;

        } else {
            edtTitle.setError(null);
        }
        if (strImageUrl == null) {
            iSuccessStoryFragmentView.showErrorMessage("Add image/Image uploading");
            count++;

        }
        return count == 0 ? true : false;

    }

    @Override
    public void onClickItem(SuccessStory data) {


    }

    @Override
    public void onClickItem(View itemView, SuccessStory data) {


        Intent intent = new Intent(iSuccessStoryFragmentView.getActivity(), ThagavalKalanchiyamDetailActivity.class);
        intent.putExtra(Constants.BundleKey.SUCCESS_STORY_DATA, data);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            Pair<View, String> pair1 = Pair.create(itemView.findViewById(R.id.ivOne), iSuccessStoryFragmentView.getActivity().getString(R.string.activity_image_trans));
            Pair<View, String> pair2 = Pair.create(itemView.findViewById(R.id.tvTitle), iSuccessStoryFragmentView.getActivity().getString(R.string.activity_text_trans));
            //Pair<View, String> pair3 = Pair.create(itemView.findViewById(R.id.parent), iHomeFragmentView.getActivity().getString(R.string.activity_parent_trans));
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(iSuccessStoryFragmentView.getActivity(), pair1);

            iSuccessStoryFragmentView.getActivity().startActivity(intent, options.toBundle());
        } else {

            iSuccessStoryFragmentView.getActivity().startActivity(intent);
        }


    }

    @Override
    public void onLikeClicked(SuccessStory successStory, Like like, boolean isToAdd) {
        iSuccessStoryFragmentView.getFireStoreDb().collection(FS_COL_SUCCESS_STORY).document(successStory.getId()).update(FS_KEY_POST_LIKED_LIST, successStory.getPost_liked_list());

        if (isToAdd) {
            iSuccessStoryFragmentView.getFireStoreDb().collection(FS_COL_SUCCESS_STORY_LIKES).document(successStory.getId() + "_" + like.getLiked_user_id()).set(like).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        iSuccessStoryFragmentView.showMessage("Post liked successfully");


                    } else {
                        iSuccessStoryFragmentView.showMessage("Something went wrong");
                    }
                }
            });

        } else {
            iSuccessStoryFragmentView.getFireStoreDb().collection(FS_COL_SUCCESS_STORY_LIKES).document(successStory.getId() + "_" + like.getLiked_user_id()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        iSuccessStoryFragmentView.showMessage("Removed Post like");


                    } else {
                        iSuccessStoryFragmentView.showMessage("Something went wrong");
                    }
                }
            });

        }
    }

    @Override
    public void onShareClicked(SuccessStory successStory) {
        if (successStory.getImageUrl() != null && !successStory.getImageUrl().isEmpty()) {
            DownloadImageFromCacheTask downloadImageFromCacheTask = new DownloadImageFromCacheTask(iSuccessStoryFragmentView.getActivity(), successStory.getTitle() + "\n\n" + successStory.getContent());
            List<String> imageUrlList = new ArrayList<>();
            imageUrlList.add(successStory.getImageUrl());
            downloadImageFromCacheTask.execute(imageUrlList);
        } else {
            sharePostContent(successStory.getTitle() + "\n\n" + successStory.getContent());
        }
    }

    private void sharePostContent(String post_content) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, iSuccessStoryFragmentView.getActivity().getString(R.string.app_name));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, post_content + "\n\n" + "ஆடு மாடு மீன் கோழி செயலியை பதிவிறக்கம் செய்ய: \n https://play.google.com/store/apps/details?id=com.aadu_maadu_meen_kozhi.drapjinfotech");
        iSuccessStoryFragmentView.getActivity().startActivity(Intent.createChooser(sharingIntent, iSuccessStoryFragmentView.getActivity().getString(R.string.app_name)));
    }

    private class DownloadImageFromCacheTask extends AsyncTask<List<String>, Void, List<File>> {
        private final Context context;
        private final String post_content;

        public DownloadImageFromCacheTask(Context context, String post_content) {
            this.context = context;
            this.post_content = post_content;
        }


        @Override
        protected List<File> doInBackground(List<String>... lists) {
            List<File> fileList = new ArrayList<>();
            List<String> imagePath = lists[0];
            for (String s :
                    imagePath) {
                FutureTarget<File> future = Glide.with(iSuccessStoryFragmentView.getActivity())
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
            iSuccessStoryFragmentView.getActivity().startActivity(Intent.createChooser(waIntent, iSuccessStoryFragmentView.getActivity().getString(R.string.app_name)));

        }

    }

}
