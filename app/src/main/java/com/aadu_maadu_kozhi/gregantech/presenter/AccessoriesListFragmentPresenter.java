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
import com.aadu_maadu_kozhi.gregantech.adapter.AccessoryAdapter;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.AccessoriesRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.databinding.FragmentAccessoriesListBinding;
import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.model.pojo.Accessories;
import com.aadu_maadu_kozhi.gregantech.model.pojo.Like;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;
import com.aadu_maadu_kozhi.gregantech.model.pojo.UserPost;
import com.aadu_maadu_kozhi.gregantech.presenter.ipresenter.IAccessoriesListFragmentPresenter;
import com.aadu_maadu_kozhi.gregantech.util.CommonUtils;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;
import com.aadu_maadu_kozhi.gregantech.view.activity.ThagavalKalanchiyamDetailActivity;
import com.aadu_maadu_kozhi.gregantech.view.fragment.ChatBottomSheet;
import com.aadu_maadu_kozhi.gregantech.view.iview.IAccessoriesListFragmentView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_ACCESSORIES_IMAGE_PATH;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_ACCESSORIES;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_ACCESSORIES_LIKES;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_USER;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_ACCESSORIES_POST_CREATED_DATE_AND_TIME;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_POST_LIKED_LIST;
import static com.facebook.FacebookSdk.getApplicationContext;

public class AccessoriesListFragmentPresenter extends BasePresenter implements IAccessoriesListFragmentPresenter, View.OnClickListener, BaseRecyclerAdapterListener<Accessories>, AccessoriesRecyclerAdapterListener<Accessories> {
    private IAccessoriesListFragmentView iAccessoriesListFragmentView;
    private FragmentAccessoriesListBinding binding;
    private ImageView ivAdd;
    private ProgressBar pgProgressBar;
    private TextView tvAddOfferText;
    private ImageView ivDone;
    private String strImageUrl;
    private EditText edtTitle;
    private EditText edtContent;
    private AlertDialog dlgAddThagavalKalanchiyamHeading;
    private StorageReference storageReference;
    private AccessoryAdapter accessoryAdapter;
    private ChatBottomSheet chatBottomSheet;

    public AccessoriesListFragmentPresenter(IAccessoriesListFragmentView iAccessoriesListFragmentView, FragmentAccessoriesListBinding binding) {
        super(iAccessoriesListFragmentView);
        this.iAccessoriesListFragmentView = iAccessoriesListFragmentView;
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
                getAccessories();
            }
        });
    }

    @Override
    public void onCreatePresenter(Bundle bundle) {

    }

    @Override
    public void getAccessories() {
        binding.swipeRefreshLayout.setRefreshing(true);

        iAccessoriesListFragmentView.getFireStoreDb().collection(FS_COL_ACCESSORIES).orderBy(FS_KEY_ACCESSORIES_POST_CREATED_DATE_AND_TIME, Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                binding.swipeRefreshLayout.setRefreshing(false);

                if (task.isSuccessful()) {

                    List<Accessories> accessoriesArrayList = new ArrayList<>();

                    for (QueryDocumentSnapshot queryDocumentSnapshot :
                            task.getResult()) {
                        Accessories accessories = queryDocumentSnapshot.toObject(Accessories.class);
                        accessories.setId(queryDocumentSnapshot.getId());
                        accessoriesArrayList.add(accessories);

                    }
                    if (accessoryAdapter == null) {
                        accessoryAdapter = new AccessoryAdapter(accessoriesArrayList, AccessoriesListFragmentPresenter.this);

                        binding.rvAccessoriesList.setLayoutManager(new LinearLayoutManager(iAccessoriesListFragmentView.getActivity()));
                        binding.rvAccessoriesList.setItemAnimator(new DefaultItemAnimator());
                        binding.rvAccessoriesList.setAdapter(accessoryAdapter);


                    } else {
                        accessoryAdapter.resetItems(accessoriesArrayList);
                    }
                    binding.rvAccessoriesList.smoothScrollToPosition(0);

                } else {
                    Log.e(TAG, task.getException().toString());
                    iAccessoriesListFragmentView.showSnackBar(iAccessoriesListFragmentView.getActivity().getString(R.string.something_went_wrong));

                }

            }
        });

    }

    @Override
    public void showAddAccessoriesDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(iAccessoriesListFragmentView.getActivity());
        LayoutInflater inflater = iAccessoriesListFragmentView.getActivity().getLayoutInflater();
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
        String path = FS_ACCESSORIES_IMAGE_PATH + "UserId:" + SharedPref.getInstance().getUserId(iAccessoriesListFragmentView.getActivity()) + " Date:" + new Date() + ".jpg";
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

                    iAccessoriesListFragmentView.showMessage(task.getException().getMessage());

                    // Handle failures
                    // ...
                }
            }
        });


    }

    @Override
    public void checkAccessoriesApproved() {
        iAccessoriesListFragmentView.getFireStoreDb().collection(FS_COL_USER).document(SharedPref.getInstance().getUserId(iAccessoriesListFragmentView.getActivity())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    User user = task.getResult().toObject(User.class);
                    if (user.isIs_accessories_approved()) {
                        iAccessoriesListFragmentView.setVisibleAddFab();

                    }
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
                    addAccessoriesToFs();
                    dlgAddThagavalKalanchiyamHeading.dismiss();

                }
                break;
            case R.id.dialog_add_offer_image:

                iAccessoriesListFragmentView.choiceAvatarFromGallery();
                break;
        }
    }

    private void addAccessoriesToFs() {
        Accessories accessories = new Accessories();
        accessories.setTitle(edtTitle.getText().toString());
        accessories.setContent(edtContent.getText().toString());
        accessories.setImageUrl(strImageUrl);
        accessories.setWhoCreated(SharedPref.getInstance().getUserId(iAccessoriesListFragmentView.getActivity()));
        iAccessoriesListFragmentView.getFireStoreDb().collection(FS_COL_ACCESSORIES).add(accessories).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    getAccessories();
                }
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
            iAccessoriesListFragmentView.showErrorMessage("Add image/Image uploading");
            count++;

        }
        return count == 0 ? true : false;

    }

    @Override
    public void onClickItem(Accessories data) {


    }

    @Override
    public void onClickItem(View itemView, Accessories data) {


        Intent intent = new Intent(iAccessoriesListFragmentView.getActivity(), ThagavalKalanchiyamDetailActivity.class);
        intent.putExtra(Constants.BundleKey.ACCESSORIES_DATA, data);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            Pair<View, String> pair1 = Pair.create(itemView.findViewById(R.id.ivOne), iAccessoriesListFragmentView.getActivity().getString(R.string.activity_image_trans));
            Pair<View, String> pair2 = Pair.create(itemView.findViewById(R.id.tvTitle), iAccessoriesListFragmentView.getActivity().getString(R.string.activity_text_trans));
            //Pair<View, String> pair3 = Pair.create(itemView.findViewById(R.id.parent), iHomeFragmentView.getActivity().getString(R.string.activity_parent_trans));
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(iAccessoriesListFragmentView.getActivity(), pair1);

            iAccessoriesListFragmentView.getActivity().startActivity(intent, options.toBundle());
        } else {

            iAccessoriesListFragmentView.getActivity().startActivity(intent);
        }


    }

    @Override
    public void onLikeClicked(Accessories successStory, Like like, boolean isToAdd) {
        iAccessoriesListFragmentView.getFireStoreDb().collection(FS_COL_ACCESSORIES).document(successStory.getId()).update(FS_KEY_POST_LIKED_LIST, successStory.getPost_liked_list());

        if (isToAdd) {
            iAccessoriesListFragmentView.getFireStoreDb().collection(FS_COL_ACCESSORIES_LIKES).document(successStory.getId() + "_" + like.getLiked_user_id()).set(like).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        iAccessoriesListFragmentView.showMessage("Post liked successfully");


                    } else {
                        iAccessoriesListFragmentView.showMessage("Something went wrong");
                    }
                }
            });

        } else {
            iAccessoriesListFragmentView.getFireStoreDb().collection(FS_COL_ACCESSORIES_LIKES).document(successStory.getId() + "_" + like.getLiked_user_id()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        iAccessoriesListFragmentView.showMessage("Removed Post like");


                    } else {
                        iAccessoriesListFragmentView.showMessage("Something went wrong");
                    }
                }
            });

        }
    }

    @Override
    public void onShareClicked(Accessories successStory) {
        if (successStory.getImageUrl() != null && !successStory.getImageUrl().isEmpty()) {
            DownloadImageFromCacheTask downloadImageFromCacheTask = new DownloadImageFromCacheTask(iAccessoriesListFragmentView.getActivity(), successStory.getTitle() + "\n\n" + successStory.getContent());
            List<String> imageUrlList = new ArrayList<>();
            imageUrlList.add(successStory.getImageUrl());
            downloadImageFromCacheTask.execute(imageUrlList);
        } else {
            sharePostContent(successStory.getTitle() + "\n\n" + successStory.getContent());
        }
    }

    private void sharePostContent(String post_content) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, iAccessoriesListFragmentView.getActivity().getString(R.string.app_name));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, post_content + "\n\n" + "ஆடு மாடு மீன் கோழி செயலியை பதிவிறக்கம் செய்ய: \n https://play.google.com/store/apps/details?id=com.aadu_maadu_meen_kozhi.drapjinfotech");
        iAccessoriesListFragmentView.getActivity().startActivity(Intent.createChooser(sharingIntent, iAccessoriesListFragmentView.getActivity().getString(R.string.app_name)));
    }
    @Override
    public void onChatClicked(Accessories accessories, User user) {
        chatBottomSheet = new ChatBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BundleKey.FIRST_USER_ID, accessories.getWhoCreated());
        bundle.putString(Constants.BundleKey.SECOND_USER_ID, SharedPref.getInstance().getUserId(iAccessoriesListFragmentView.getActivity()));
        bundle.putParcelable(Constants.BundleKey.USER, user);

        chatBottomSheet.setArguments(bundle);
        chatBottomSheet.show(iAccessoriesListFragmentView.getActivity().getSupportFragmentManager(), chatBottomSheet.getTag());


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
                FutureTarget<File> future = Glide.with(iAccessoriesListFragmentView.getActivity())
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
            iAccessoriesListFragmentView.getActivity().startActivity(Intent.createChooser(waIntent, iAccessoriesListFragmentView.getActivity().getString(R.string.app_name)));

        }

    }

}
