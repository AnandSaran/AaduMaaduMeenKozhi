package com.aadu_maadu_kozhi.gregantech.view.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.TypeListCheckBoxAdapter;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.databinding.FragmentProfileBinding;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ThagavalKalanchiyam;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;
import com.aadu_maadu_kozhi.gregantech.util.FireStoreKey;
import com.aadu_maadu_kozhi.gregantech.util.KeyboardUtil;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;
import com.aadu_maadu_kozhi.gregantech.view.activity.DoctorLoginActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_THAGAVAL_KALANCHIYAM;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_USER;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_AREA;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_DOCTOR_DOCUMENT_URL;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_DOCTOR_LICENCE_NUMBER;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_FARM_ANIMAL_TYPE;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_IS_DOCTOR;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_IS_DOCTOR_APPROVED;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_IS_FARM_OWNER;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_IS_FARM_OWNER_APPROVED;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_IS_SELLER;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_IS_SELLER_APPROVED;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_MOBILE_NUMBER;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_SELLING_ANIMAL_TYPE;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_THAGAVAL_KALANCHIYAM_POST_CREATED_DATE_AND_TIME;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_USER_EMAIL_ID;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_USER_IMAGE_URL;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_USER_NAME;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_PROFILE_IMAGE_PATH;

/**
 */
public class ProfileFragment extends BaseFragment implements TextView.OnEditorActionListener, BaseRecyclerAdapterListener<ThagavalKalanchiyam> {

    private FragmentProfileBinding binding;
    private View rootView;
    private StorageReference mStorageRef;
    private List<ThagavalKalanchiyam> thagavalKalanchiyamList = new ArrayList<>();
    private TypeListCheckBoxAdapter typeListCheckBoxAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    private void getThagavalKalachiyamFromFireStore() {
        getFireStoreDb().collection(FS_COL_THAGAVAL_KALANCHIYAM).orderBy(FS_KEY_THAGAVAL_KALANCHIYAM_POST_CREATED_DATE_AND_TIME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    thagavalKalanchiyamList = task.getResult().toObjects(ThagavalKalanchiyam.class);
                    ThagavalKalanchiyam thagavalKalanchiyam = new ThagavalKalanchiyam();
                    thagavalKalanchiyam.setTitle("மற்றவை");
                    thagavalKalanchiyamList.add(thagavalKalanchiyam);
                } else {

                }

            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        if (SharedPref.getInstance().getStringValue(getActivity(), FS_KEY_MOBILE_NUMBER) != null &&
                (!SharedPref.getInstance().isSeller(getActivity()) || !SharedPref.getInstance().isFarmOwner(getActivity()))) {
            getThagavalKalachiyamFromFireStore();
        }
        binding.edtName.setOnEditorActionListener(this);
        binding.edtEmail.setOnEditorActionListener(this);
        binding.edtName.setText(SharedPref.getInstance().getUserName(getActivity()));
        binding.edtEmail.setText(SharedPref.getInstance().getUserEmailId(getActivity()));
        Log.e(TAG, "" + binding.edtEmail.getText().toString());
        if (SharedPref.getInstance().getUserProfileUrl(getActivity()) != null)
            setProfileImage(SharedPref.getInstance().getUserProfileUrl(getActivity()));
        setDoctorAndSellerAndFarmOwnerData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {

            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
            rootView = binding.getRoot();
            rootView.setTag(binding);
        } else {
            binding = (FragmentProfileBinding) rootView.getTag();
        }
        binding.setHandlers(this); // fragment's context

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserProfile();

    }

    private void getUserProfile() {
        getFireStoreDb().collection(FireStoreKey.FS_COL_USER).document(SharedPref.getInstance().getUserId(getActivity())).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        user.setUser_id(documentSnapshot.getId());
                        saveUserProfileInSharedPreference(user);
                        setDoctorAndSellerAndFarmOwnerData();
                        return;
                    }
                });


    }

    private void setDoctorAndSellerAndFarmOwnerData() {
        if (SharedPref.getInstance().isDoctor(getActivity()) && SharedPref.getInstance().isDoctorApproved(getActivity())) {
            binding.btnLoginAsDoctor.setEnabled(false);
            binding.btnLoginAsDoctor.setText("DOCTOR");
        } else if (SharedPref.getInstance().isDoctor(getActivity()) && !SharedPref.getInstance().isDoctorApproved(getActivity())) {
            binding.btnLoginAsDoctor.setEnabled(false);
            binding.btnLoginAsDoctor.setText("DOCTOR - Waiting for approval");
        }
        if (SharedPref.getInstance().isFarmOwner(getActivity()) && SharedPref.getInstance().isFarmOwnerApproved(getActivity())) {
            binding.btnLoginAsFarmOwners.setEnabled(false);
            binding.btnLoginAsFarmOwners.setText("FARM OWNER");
        } else if (SharedPref.getInstance().isFarmOwner(getActivity()) && !SharedPref.getInstance().isFarmOwnerApproved(getActivity())) {
            binding.btnLoginAsFarmOwners.setEnabled(false);
            binding.btnLoginAsFarmOwners.setText("FARM OWNER - Waiting for approval");
        }
        if (SharedPref.getInstance().isSeller(getActivity()) && SharedPref.getInstance().isSellerApproved(getActivity())) {
            binding.btnLoginAsSellers.setEnabled(false);
            binding.btnLoginAsSellers.setText("SELLER");
        } else if (SharedPref.getInstance().isSeller(getActivity()) && !SharedPref.getInstance().isSellerApproved(getActivity())) {
            binding.btnLoginAsSellers.setEnabled(false);
            binding.btnLoginAsSellers.setText("SELLER - Waiting for approval");
        }
    }

    private void saveUserProfileInSharedPreference(User user) {
        SharedPref.getInstance().setSharedValue(getActivity(), getString(R.string.user_id), user.getUser_id());
        SharedPref.getInstance().setSharedValue(getActivity(), getString(R.string.user_name), user.getName());
        SharedPref.getInstance().setSharedValue(getActivity(), getString(R.string.user_email_id), user.getEmail());
        SharedPref.getInstance().setSharedValue(getActivity(), getString(R.string.user_profile_url), user.getImage_url());
        SharedPref.getInstance().setSharedValue(getActivity(), FS_KEY_IS_DOCTOR, user.isIs_doctor());
        SharedPref.getInstance().setSharedValue(getActivity(), FS_KEY_MOBILE_NUMBER, user.getMobile_number());
        SharedPref.getInstance().setSharedValue(getActivity(), FS_KEY_IS_DOCTOR_APPROVED, user.isIs_doctor_approved());
        SharedPref.getInstance().setSharedValue(getActivity(), FS_KEY_DOCTOR_LICENCE_NUMBER, user.getDoctor_licence_number());
        SharedPref.getInstance().setSharedValue(getActivity(), FS_KEY_AREA, user.getArea());
        SharedPref.getInstance().setSharedValue(getActivity(), FS_KEY_DOCTOR_DOCUMENT_URL, user.getDoctor_document_url());
        SharedPref.getInstance().setSharedValue(getActivity(), FS_KEY_IS_FARM_OWNER, user.isIs_farm_owner());
        SharedPref.getInstance().setSharedValue(getActivity(), FS_KEY_IS_FARM_OWNER_APPROVED, user.isIs_farm_owner_approved());
        SharedPref.getInstance().setSharedValue(getActivity(), FS_KEY_IS_SELLER, user.isIs_seller());
        SharedPref.getInstance().setSharedValue(getActivity(), FS_KEY_IS_SELLER_APPROVED, user.isIs_seller_approved());

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            com.aadu_maadu_kozhi.gregantech.library.Log.d(TAG, " is NOT on screen");
        } else {
            getUserProfile();

        }
    }

    private void setProfileImage(String url) {
        Glide.with(this)
                .asBitmap()

                .load(url)
                .into(new BitmapImageViewTarget(binding.ivProfile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        // Do bitmap magic here
                        super.setResource(resource);
                        binding.ivProfile.setImageBitmap(resource);
                        binding.background.setImageBitmap(resource);

                    }
                });

       /* Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        binding.ivProfile.setImageBitmap(resource);
                        binding.background.setImageBitmap(resource);

                        resource.recycle();
                    }
                });*/
    }


    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        Log.e(TAG, "onEditorAction");

        switch (textView.getId()) {
            case R.id.edtName:
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.ivEditTickName.performClick();
                }
                break;
            case R.id.edtEmail:
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.ivEditTickEmailId.performClick();
                    return true;
                }
                break;
        }
        return false;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivEditTickName:
                if (binding.edtName.isEnabled()) {
                    binding.edtName.setEnabled(false);
                    ((ImageView) view).setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_edit_black_24dp));
                    updateProfile(FS_KEY_USER_NAME, binding.edtName.getText().toString());

                } else {
                    ((ImageView) view).setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_done_black_24dp));


                    binding.edtName.setEnabled(true);
                    binding.edtName.setSelection(binding.edtName.getText().toString().length());
                    binding.edtName.requestFocus();
                    KeyboardUtil.forceShowSoftKeypad(binding.edtName);

                }
                break;
            case R.id.ivEditTickEmailId:
                if (binding.edtEmail.isEnabled()) {
                    binding.edtEmail.setEnabled(false);

                    ((ImageView) view).setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_edit_black_24dp));
                    updateProfile(FS_KEY_USER_EMAIL_ID, binding.edtEmail.getText().toString());


                } else {
                    ((ImageView) view).setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_done_black_24dp));


                    binding.edtEmail.setEnabled(true);
                    binding.edtEmail.setSelection(binding.edtEmail.getText().toString().length());
                    binding.edtEmail.requestFocus();
                    KeyboardUtil.forceShowSoftKeypad(binding.edtEmail);
                }
                break;
            case R.id.iv_profile:
                CropImage.activity()
                        .start(getContext(), this);
                break;
            case R.id.btnLoginAsDoctor:
                Intent iDoctorLogin = new Intent(getActivity(), DoctorLoginActivity.class);
                iDoctorLogin.putExtra(Constants.BundleKey.ForDoctorLogin, true);
                startActivity(iDoctorLogin);
                break;
            case R.id.btnLoginAsFarmOwners:
                if (SharedPref.getInstance().getStringValue(getActivity(), FS_KEY_AREA) == null || SharedPref.getInstance().getStringValue(getActivity(), FS_KEY_MOBILE_NUMBER) == null) {
                    Intent iFarmOwnersLogin = new Intent(getActivity(), DoctorLoginActivity.class);
                    iFarmOwnersLogin.putExtra(Constants.BundleKey.ForFarmOwnersLogin, true);

                    startActivity(iFarmOwnersLogin);
                } else {
                    if (thagavalKalanchiyamList.size() > 0)
                        showSelectTypeDialog(view);
                }
                break;
            case R.id.btnLoginAsSellers:
                if (SharedPref.getInstance().getStringValue(getActivity(), FS_KEY_AREA) == null || SharedPref.getInstance().getStringValue(getActivity(), FS_KEY_MOBILE_NUMBER) == null) {
                    Intent iSellersLogin = new Intent(getActivity(), DoctorLoginActivity.class);
                    iSellersLogin.putExtra(Constants.BundleKey.ForSellersLogin, true);

                    startActivity(iSellersLogin);
                } else {
                    if (thagavalKalanchiyamList.size() > 0)
                        showSelectTypeDialog(view);

                }
                break;
        }
    }

    private void submitSellerDetails(String type) {
        Map<String, Object> map = new HashMap<>();
        map.put(FS_KEY_IS_SELLER, true);
        map.put(FS_KEY_IS_SELLER_APPROVED, true);
        map.put(FS_KEY_SELLING_ANIMAL_TYPE, type);

        getFireStoreDb().collection(FS_COL_USER).document(SharedPref.getInstance().getUserId(getActivity())).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dismissProgressbar();
                if (task.isSuccessful()) {
                    showInfoMessage("Form submitted");
                    SharedPref.getInstance().setSharedValue(getActivity(), FS_KEY_IS_SELLER, true);
                    SharedPref.getInstance().setSharedValue(getActivity(), FS_KEY_IS_SELLER_APPROVED, true);
                    setDoctorAndSellerAndFarmOwnerData();
                } else {
                    showErrorMessage("Something went wrong");

                }
            }
        });
    }

    private void submitFarmOwnerDetails(String type) {
        Map<String, Object> map = new HashMap<>();
        map.put(FS_KEY_IS_FARM_OWNER, true);
        map.put(FS_KEY_IS_FARM_OWNER_APPROVED, true);
        map.put(FS_KEY_FARM_ANIMAL_TYPE, type);

        getFireStoreDb().collection(FS_COL_USER).document(SharedPref.getInstance().getUserId(getActivity())).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dismissProgressbar();
                if (task.isSuccessful()) {
                    showInfoMessage("Form submitted");
                    SharedPref.getInstance().setSharedValue(getActivity(), FS_KEY_IS_FARM_OWNER, true);
                    SharedPref.getInstance().setSharedValue(getActivity(), FS_KEY_IS_FARM_OWNER_APPROVED, true);
                    setDoctorAndSellerAndFarmOwnerData();
                } else {
                    showErrorMessage("Something went wrong");

                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult");
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                Glide.with(this)
                        .load(resultUri)
                        .into(binding.ivProfile);
                Glide.with(this)
                        .load(resultUri)
                        .into(binding.background);
                uploadProfileImage(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void uploadProfileImage(Uri file) {
        binding.progressbar.setVisibility(View.VISIBLE);
        binding.ivProfile.setAlpha(0.5f);
        // Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        String path = FS_PROFILE_IMAGE_PATH + "UserId:" + SharedPref.getInstance().getUserId(getActivity()) + " Date:" + new Date() + ".jpg";

        final StorageReference riversRef = mStorageRef.child(path);

        UploadTask uploadTask = riversRef.putFile(file);

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
                    Uri downloadUri = task.getResult();
                    updateProfile(FS_KEY_USER_IMAGE_URL, downloadUri.toString());
                    Glide.with(getActivity())
                            .load(downloadUri.toString());
                    //setProfileImage(downloadUri.toString());
                    binding.progressbar.setVisibility(View.GONE);
                    binding.ivProfile.setAlpha(1f);

                } else {
                    binding.progressbar.setVisibility(View.GONE);
                    binding.ivProfile.setAlpha(1f);

                    showMessage("Something went wrong");

                    // Handle failures
                    // ...
                }
            }
        });

    }

    private void updateProfile(final String key, final String value) {
        DocumentReference profile = getFireStoreDb().collection(FS_COL_USER).document(/*FS_DOC_PROFILE + */SharedPref.getInstance().getUserId(getActivity()));
        if (profile != null) {
            profile.update(key, value)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Updated Successfully",
                                    Toast.LENGTH_SHORT).show();
                            switch (key) {
                                case FS_KEY_USER_NAME:
                                    SharedPref.getInstance().setSharedValue(getContext(), getString(R.string.user_name), value);

                                    break;
                                case FS_KEY_USER_EMAIL_ID:
                                    SharedPref.getInstance().setSharedValue(getContext(), getString(R.string.user_email_id), value);

                                    break;
                                case FS_KEY_USER_IMAGE_URL:
                                    SharedPref.getInstance().setSharedValue(getContext(), getString(R.string.user_profile_url), value);

                                    break;
                            }
                        }
                    });
        }
    }


    private void showSelectTypeDialog(final View view) {


        typeListCheckBoxAdapter = new TypeListCheckBoxAdapter(thagavalKalanchiyamList, this);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Select type");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String type = "";
                for (ThagavalKalanchiyam languages : thagavalKalanchiyamList
                        ) {
                    if (languages.isSelected()) {
                        type = type + languages.getTitle() + ", ";
                    }
                }
                if (type.length() > 0) {
                    type = type.substring(0, type.trim().length() - 1) + ".";
                } else {
                    showErrorMessage("Select atleast one");
                    return;
                }
                switch (view.getId()) {
                    case R.id.btnLoginAsFarmOwners:
                        submitFarmOwnerDetails(type);

                        break;
                    case R.id.btnLoginAsSellers:
                        submitSellerDetails(type);

                        break;
                }

                // languageAdapter.getSelectedItem();
            }
        });

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_type_list, null);
        dialogBuilder.setView(dialogView);
        RecyclerView mRecyclerView = (RecyclerView) dialogView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(typeListCheckBoxAdapter);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClickItem(ThagavalKalanchiyam data) {

    }

    @Override
    public void onClickItem(View itemView, ThagavalKalanchiyam data) {

    }
}
