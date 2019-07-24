package com.aadu_maadu_kozhi.gregantech.presenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.ThagavalKalanchiyamAdapter;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.databinding.ActivityThagavalKalanchiyamSubBinding;
import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ThagavalKalanchiyam;
import com.aadu_maadu_kozhi.gregantech.presenter.ipresenter.IThagavalKalanchiyamSubActivitytPresenter;
import com.aadu_maadu_kozhi.gregantech.util.CommonUtils;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;
import com.aadu_maadu_kozhi.gregantech.view.activity.ThagavalKalanchiyamDetailActivity;
import com.aadu_maadu_kozhi.gregantech.view.iview.IThagavalKalanchiyamSubActivityView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;


import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_ADMIN_THAGAVAL_KALANCHIYAM;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_THAGAVAL_KALANCHIYAM;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_THAGAVAL_KALANCHIYAM_SUB;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_THAGAVAL_KALANCHIYAM_POST_CREATED_DATE_AND_TIME;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_USER_EMAIL_ID;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_THAGAVAL_KALANCHIYAM_SUB_HEADING_IMAGE_PATH;

public class ThagavalKalanchiyamSubActivityPresenter extends BasePresenter implements IThagavalKalanchiyamSubActivitytPresenter, View.OnClickListener, BaseRecyclerAdapterListener<ThagavalKalanchiyam> {
    private IThagavalKalanchiyamSubActivityView iThagavalKalanchiyamSubActivityView;
    private ActivityThagavalKalanchiyamSubBinding binding;
    private ImageView ivAdd;
    private ProgressBar pgProgressBar;
    private TextView tvAddOfferText;
    private ImageView ivDone;
    private String strImageUrl;
    private EditText edtTitle;
    private EditText edtContent;
    private AlertDialog dlgAddThagavalKalanchiyamHeading;
    private StorageReference storageReference;
    private ThagavalKalanchiyamAdapter thagavalKalanchiyamAdapter;
    private ThagavalKalanchiyam thagavalKalanchiyamHeading;

    public ThagavalKalanchiyamSubActivityPresenter(IThagavalKalanchiyamSubActivityView iThagavalKalanchiyamSubActivityView, ActivityThagavalKalanchiyamSubBinding binding) {
        super(iThagavalKalanchiyamSubActivityView);
        this.iThagavalKalanchiyamSubActivityView = iThagavalKalanchiyamSubActivityView;
        this.binding = binding;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


    }

    @Override
    public void onCreatePresenter(Bundle bundle) {
        thagavalKalanchiyamHeading = bundle.getParcelable(Constants.BundleKey.THAGAVALKALANCHIYAM_DATA);
        binding.tvTitle.setText(thagavalKalanchiyamHeading.getTitle());
    }

    @Override
    public void getThagavalKalanchiyamSubHeading() {
        iThagavalKalanchiyamSubActivityView.getFireStoreDb().collection(FS_COL_THAGAVAL_KALANCHIYAM).document(thagavalKalanchiyamHeading.getId()).collection(FS_COL_THAGAVAL_KALANCHIYAM_SUB).orderBy(FS_KEY_THAGAVAL_KALANCHIYAM_POST_CREATED_DATE_AND_TIME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    if (thagavalKalanchiyamAdapter == null) {
                        thagavalKalanchiyamAdapter = new ThagavalKalanchiyamAdapter(task.getResult().toObjects(ThagavalKalanchiyam.class), ThagavalKalanchiyamSubActivityPresenter.this);

                        binding.rvThagavalList.setLayoutManager(new GridLayoutManager(iThagavalKalanchiyamSubActivityView.getActivity(), 2));
                        binding.rvThagavalList.setItemAnimator(new DefaultItemAnimator());
                        binding.rvThagavalList.setAdapter(thagavalKalanchiyamAdapter);


                    } else {
                        thagavalKalanchiyamAdapter.resetItems(task.getResult().toObjects(ThagavalKalanchiyam.class));
                    }
                } else {
                    Log.e(TAG, task.getException().toString());
                    iThagavalKalanchiyamSubActivityView.showSnackBar(iThagavalKalanchiyamSubActivityView.getActivity().getString(R.string.something_went_wrong));

                }

            }
        });

    }

    @Override
    public void showAddThagavalKalanchiyamSubHeadingDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(iThagavalKalanchiyamSubActivityView.getActivity());
        LayoutInflater inflater = iThagavalKalanchiyamSubActivityView.getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_thagaval_kalanchiyam_heading, null);
        //  temp = (ImageView) dialogView.findViewById(R.id.iv_news_image);
        dialogView.findViewById(R.id.dialog_add_offer_image).setOnClickListener(this);
        dialogView.findViewById(R.id.submit).setOnClickListener(this);
        ivAdd = (ImageView) dialogView.findViewById(R.id.add);
        pgProgressBar = (ProgressBar) dialogView.findViewById(R.id.progress_bar);
        tvAddOfferText = (TextView) dialogView.findViewById(R.id.add_image_text);
        ivDone = (ImageView) dialogView.findViewById(R.id.done);


        edtTitle = (EditText) dialogView.findViewById(R.id.edtTitle);
        edtContent = (EditText) dialogView.findViewById(R.id.edtContent);
        edtContent.setVisibility(View.VISIBLE);
        dialogBuilder.setView(dialogView);
        dlgAddThagavalKalanchiyamHeading = dialogBuilder.create();
        dlgAddThagavalKalanchiyamHeading.setCanceledOnTouchOutside(false);

        dlgAddThagavalKalanchiyamHeading.show();
    }

    @Override
    public void uploadFile(Uri filePath) {
        showImageUploadProgressBar();
        String path = FS_THAGAVAL_KALANCHIYAM_SUB_HEADING_IMAGE_PATH + "UserId:" + SharedPref.getInstance().getUserId(iThagavalKalanchiyamSubActivityView.getActivity()) + " Date:" + new Date() + ".jpg";
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

                    iThagavalKalanchiyamSubActivityView.showMessage(task.getException().getMessage());

                    // Handle failures
                    // ...
                }
            }
        });


    }

    @Override
    public void checkThagavalKalanchiyamAdmin() {
        iThagavalKalanchiyamSubActivityView.getFireStoreDb().collection(FS_COL_ADMIN_THAGAVAL_KALANCHIYAM)
                .whereEqualTo(FS_KEY_USER_EMAIL_ID,SharedPref.getInstance().getUserEmailId(iThagavalKalanchiyamSubActivityView.getActivity())).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult()!=null && task.getResult().size()>0){
                    iThagavalKalanchiyamSubActivityView.setVisibleAddFab();
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
                    addThagavalKalanchiyamSubHeadingToFs();
                    dlgAddThagavalKalanchiyamHeading.dismiss();

                }
                break;
            case R.id.dialog_add_offer_image:

                iThagavalKalanchiyamSubActivityView.choiceAvatarFromGallery();
                break;
        }
    }

    private void addThagavalKalanchiyamSubHeadingToFs() {
        ThagavalKalanchiyam thagavalKalanchiyam = new ThagavalKalanchiyam();
        thagavalKalanchiyam.setTitle(edtTitle.getText().toString());
        thagavalKalanchiyam.setContent(edtContent.getText().toString());
        thagavalKalanchiyam.setImageUrl(strImageUrl);
        thagavalKalanchiyam.setWhoCreated(SharedPref.getInstance().getUserId(iThagavalKalanchiyamSubActivityView.getActivity()));
        iThagavalKalanchiyamSubActivityView.getFireStoreDb().collection(FS_COL_THAGAVAL_KALANCHIYAM).document(thagavalKalanchiyamHeading.getId()).collection(FS_COL_THAGAVAL_KALANCHIYAM_SUB).add(thagavalKalanchiyam);
        edtTitle.setText(null);
        edtContent.setText(null);
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
        if (!CommonUtils.getInstance().isNullCheck(edtContent.getText().toString())) {
            edtContent.setError("Enter content");
            count++;

        } else {
            edtContent.setError(null);
        }
        if (strImageUrl == null) {
            iThagavalKalanchiyamSubActivityView.showErrorMessage("Add image/Image uploading");
            count++;

        }
        return count == 0 ? true : false;

    }

    @Override
    public void onClickItem(ThagavalKalanchiyam data) {
        Intent intent = new Intent(iThagavalKalanchiyamSubActivityView.getActivity(), ThagavalKalanchiyamDetailActivity.class);
        intent.putExtra(Constants.BundleKey.THAGAVALKALANCHIYAM_DATA, data);
        iThagavalKalanchiyamSubActivityView.getActivity().startActivity(intent);


    }

    @Override
    public void onClickItem(View itemView, ThagavalKalanchiyam data) {
        Intent intent = new Intent(iThagavalKalanchiyamSubActivityView.getActivity(), ThagavalKalanchiyamDetailActivity.class);
        intent.putExtra(Constants.BundleKey.THAGAVALKALANCHIYAM_DATA, data);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            Pair<View, String> pair1 = Pair.create(itemView.findViewById(R.id.ivThagaval), iThagavalKalanchiyamSubActivityView.getActivity().getString(R.string.activity_image_trans));
            Pair<View, String> pair2 = Pair.create(itemView.findViewById(R.id.tvName), iThagavalKalanchiyamSubActivityView.getActivity().getString(R.string.activity_text_trans));
            //Pair<View, String> pair3 = Pair.create(itemView.findViewById(R.id.parent), iHomeFragmentView.getActivity().getString(R.string.activity_parent_trans));
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(iThagavalKalanchiyamSubActivityView.getActivity(), pair1);

            iThagavalKalanchiyamSubActivityView.getActivity().startActivity(intent, options.toBundle());
        } else {

            iThagavalKalanchiyamSubActivityView.getActivity().startActivity(intent);
        }

    }
}
