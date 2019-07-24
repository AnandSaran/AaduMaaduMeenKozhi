package com.aadu_maadu_kozhi.gregantech.presenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.aadu_maadu_kozhi.gregantech.databinding.FragmentThagavalKalanchiyamBinding;
import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ThagavalKalanchiyam;
import com.aadu_maadu_kozhi.gregantech.presenter.ipresenter.IThagavalKalanchiyamFragmentPresenter;
import com.aadu_maadu_kozhi.gregantech.util.CommonUtils;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;
import com.aadu_maadu_kozhi.gregantech.view.activity.ThagavalKalanchiyamSubActivity;
import com.aadu_maadu_kozhi.gregantech.view.iview.IThagavalKalanchiyamFragmentView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_ADMIN_THAGAVAL_KALANCHIYAM;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_THAGAVAL_KALANCHIYAM;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_THAGAVAL_KALANCHIYAM_POST_CREATED_DATE_AND_TIME;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_USER_EMAIL_ID;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_THAGAVAL_KALANCHIYAM_HEADING_IMAGE_PATH;

public class ThagavalKalanchiyamPresenter extends BasePresenter implements IThagavalKalanchiyamFragmentPresenter, View.OnClickListener, BaseRecyclerAdapterListener<ThagavalKalanchiyam> {
    private IThagavalKalanchiyamFragmentView iThagavalKalanchiyamFragmentView;
    private FragmentThagavalKalanchiyamBinding binding;
    private ImageView ivAdd;
    private ProgressBar pgProgressBar;
    private TextView tvAddOfferText;
    private ImageView ivDone;
    private String strImageUrl;
    private EditText edtTitle;
    private AlertDialog dlgAddThagavalKalanchiyamHeading;
    private StorageReference storageReference;
    private ThagavalKalanchiyamAdapter thagavalKalanchiyamAdapter;

    public ThagavalKalanchiyamPresenter(IThagavalKalanchiyamFragmentView iThagavalKalanchiyamFragmentView, FragmentThagavalKalanchiyamBinding binding) {
        super(iThagavalKalanchiyamFragmentView);
        this.iThagavalKalanchiyamFragmentView = iThagavalKalanchiyamFragmentView;
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
                getThagavalKalanchiyamHeading();
            }
        });
    }
    @Override
    public void onCreatePresenter(Bundle bundle) {

    }

    @Override
    public void getThagavalKalanchiyamHeading() {
        iThagavalKalanchiyamFragmentView.getFireStoreDb().collection(FS_COL_THAGAVAL_KALANCHIYAM).orderBy(FS_KEY_THAGAVAL_KALANCHIYAM_POST_CREATED_DATE_AND_TIME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                binding.swipeRefreshLayout.setRefreshing(false);

                if (task.isSuccessful()) {

                    List<ThagavalKalanchiyam> thagavalKalanchiyams = new ArrayList<>();
                    if (thagavalKalanchiyamAdapter == null) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot :
                                task.getResult()) {
                            ThagavalKalanchiyam thagavalKalanchiyam = queryDocumentSnapshot.toObject(ThagavalKalanchiyam.class);
                            thagavalKalanchiyam.setId(queryDocumentSnapshot.getId());
                            thagavalKalanchiyams.add(thagavalKalanchiyam);

                        }

                        thagavalKalanchiyamAdapter = new ThagavalKalanchiyamAdapter(thagavalKalanchiyams, ThagavalKalanchiyamPresenter.this);

                        binding.rvThagavalList.setLayoutManager(new GridLayoutManager(iThagavalKalanchiyamFragmentView.getActivity(), 2));
                        binding.rvThagavalList.setItemAnimator(new DefaultItemAnimator());
                        binding.rvThagavalList.setAdapter(thagavalKalanchiyamAdapter);


                    } else {
                        thagavalKalanchiyamAdapter.resetItems(task.getResult().toObjects(ThagavalKalanchiyam.class));
                    }
                } else {
                    Log.e(TAG, task.getException().toString());
                    iThagavalKalanchiyamFragmentView.showSnackBar(iThagavalKalanchiyamFragmentView.getActivity().getString(R.string.something_went_wrong));

                }

            }
        });

    }

    @Override
    public void showAddThagavalKalanchiyamHeadingDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(iThagavalKalanchiyamFragmentView.getActivity());
        LayoutInflater inflater = iThagavalKalanchiyamFragmentView.getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_thagaval_kalanchiyam_heading, null);
        //  temp = (ImageView) dialogView.findViewById(R.id.iv_news_image);
        dialogView.findViewById(R.id.dialog_add_offer_image).setOnClickListener(this);
        dialogView.findViewById(R.id.submit).setOnClickListener(this);
        ivAdd = (ImageView) dialogView.findViewById(R.id.add);
        pgProgressBar = (ProgressBar) dialogView.findViewById(R.id.progress_bar);
        tvAddOfferText = (TextView) dialogView.findViewById(R.id.add_image_text);
        ivDone = (ImageView) dialogView.findViewById(R.id.done);


        edtTitle = (EditText) dialogView.findViewById(R.id.edtTitle);
        dialogBuilder.setView(dialogView);
        dlgAddThagavalKalanchiyamHeading = dialogBuilder.create();
        dlgAddThagavalKalanchiyamHeading.setCanceledOnTouchOutside(false);

        dlgAddThagavalKalanchiyamHeading.show();
    }

    @Override
    public void uploadFile(Uri filePath) {
        showImageUploadProgressBar();
        String path = FS_THAGAVAL_KALANCHIYAM_HEADING_IMAGE_PATH + "UserId:" + SharedPref.getInstance().getUserId(iThagavalKalanchiyamFragmentView.getActivity()) + " Date:" + new Date() + ".jpg";
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

                    iThagavalKalanchiyamFragmentView.showMessage(task.getException().getMessage());

                    // Handle failures
                    // ...
                }
            }
        });


    }

    @Override
    public void checkThagavalKalanchiyamAdmin() {
        iThagavalKalanchiyamFragmentView.getFireStoreDb().collection(FS_COL_ADMIN_THAGAVAL_KALANCHIYAM)
                .whereEqualTo(FS_KEY_USER_EMAIL_ID, SharedPref.getInstance().getUserEmailId(iThagavalKalanchiyamFragmentView.getActivity())).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null && task.getResult().size() > 0) {
                    iThagavalKalanchiyamFragmentView.setVisibleAddFab();
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
                    addThagavalKalanchiyamHeadingToFs();
                    dlgAddThagavalKalanchiyamHeading.dismiss();

                }
                break;
            case R.id.dialog_add_offer_image:

                iThagavalKalanchiyamFragmentView.choiceAvatarFromGallery();
                break;
        }
    }

    private void addThagavalKalanchiyamHeadingToFs() {
        ThagavalKalanchiyam thagavalKalanchiyam = new ThagavalKalanchiyam();
        thagavalKalanchiyam.setTitle(edtTitle.getText().toString());
        thagavalKalanchiyam.setImageUrl(strImageUrl);
        thagavalKalanchiyam.setWhoCreated(SharedPref.getInstance().getUserId(iThagavalKalanchiyamFragmentView.getActivity()));
        iThagavalKalanchiyamFragmentView.getFireStoreDb().collection(FS_COL_THAGAVAL_KALANCHIYAM).add(thagavalKalanchiyam);
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
            iThagavalKalanchiyamFragmentView.showErrorMessage("Add image/Image uploading");
            count++;

        }
        return count == 0 ? true : false;

    }

    @Override
    public void onClickItem(ThagavalKalanchiyam data) {


    }

    @Override
    public void onClickItem(View itemView, ThagavalKalanchiyam data) {
        Intent intent = new Intent(iThagavalKalanchiyamFragmentView.getActivity(), ThagavalKalanchiyamSubActivity.class);
        intent.putExtra(Constants.BundleKey.THAGAVALKALANCHIYAM_DATA, data);
        iThagavalKalanchiyamFragmentView.getActivity().startActivity(intent);

    }
}
