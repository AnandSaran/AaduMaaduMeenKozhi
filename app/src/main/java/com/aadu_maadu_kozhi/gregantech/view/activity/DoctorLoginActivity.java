package com.aadu_maadu_kozhi.gregantech.view.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.adapter.TypeListCheckBoxAdapter;
import com.aadu_maadu_kozhi.gregantech.adapter.listener.BaseRecyclerAdapterListener;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.databinding.ActivityDoctorLoginBinding;
import com.aadu_maadu_kozhi.gregantech.library.ExceptionTracker;
import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.model.pojo.Area;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ThagavalKalanchiyam;
import com.aadu_maadu_kozhi.gregantech.util.CommonUtils;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;
import com.aadu_maadu_kozhi.gregantech.util.permission.IPermissionHandler;
import com.aadu_maadu_kozhi.gregantech.util.permission.PermissionProducer;
import com.aadu_maadu_kozhi.gregantech.util.permission.RequestPermission;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_AREA;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_THAGAVAL_KALANCHIYAM;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_USER;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_DOCTOR_DOCUMNET_IMAGE_PATH;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_AREA;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_AREA_CREATED_DATE_AND_TIME;
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
import static com.aadu_maadu_kozhi.gregantech.view.fragment.ThagavalKalanchiyamFragment.PICK_GALLERY_IMAGE_REQUEST_CODE;

public class DoctorLoginActivity extends BaseActivity implements PermissionProducer, BaseRecyclerAdapterListener<ThagavalKalanchiyam> {
    private ActivityDoctorLoginBinding binding;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private IPermissionHandler iPermissionHandler;
    private List<Area> areaList = new ArrayList<>();
    private StorageReference storageReference;
    private String strDoctorDocumentImageUrl;
    private String mMobileNumber;
    private boolean ForFarmOwnersLogin;
    private boolean ForDoctorLogin;
    private boolean ForSellersLogin;
    private List<ThagavalKalanchiyam> thagavalKalanchiyamList=new ArrayList<>();
    private TypeListCheckBoxAdapter typeListCheckBoxAdapter;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_login);
        binding.setHandlers(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        ForFarmOwnersLogin = getIntent().getBooleanExtra(Constants.BundleKey.ForFarmOwnersLogin, false);
        ForDoctorLogin = getIntent().getBooleanExtra(Constants.BundleKey.ForDoctorLogin, false);
        ForSellersLogin = getIntent().getBooleanExtra(Constants.BundleKey.ForSellersLogin, false);
        storageReference = storage.getReference();
        if (ForSellersLogin) {
            binding.tvTitle.setText("Seller login");

            getThagavalKalachiyamFromFireStore();
        } else if (ForFarmOwnersLogin) {
            binding.tvTitle.setText("Farm owner login");

            getThagavalKalachiyamFromFireStore();
        } else if (ForDoctorLogin) {
            binding.tvTitle.setText("Doctor login");
        }
        getAreaFromFireStore();
        iPermissionHandler = RequestPermission.newInstance(this);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed" + e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    showErrorMessage("Invalid number");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    showErrorMessage("Something went wrong");

                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                showVerifyOtpView();
                hideSendOtpButton();

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                //mResendToken = token;

                // ...
            }
        };
        if (SharedPref.getInstance().getStringValue(this, FS_KEY_MOBILE_NUMBER) != null) {
            hideSendOtpButton();
            showUploadDocumentLayout();
        }
        if (ForFarmOwnersLogin || ForSellersLogin) {
            binding.dialogAddOfferImage.setVisibility(View.GONE);
            binding.edtLicenceNumber.setVisibility(View.GONE);
        }

    }

    private void showUploadDocumentLayout() {
        binding.llDocumentLayout.setVisibility(View.VISIBLE);

    }

    private void showVerifyOtpView() {
        binding.llGetOtpLayout.setVisibility(View.VISIBLE);
        binding.btnVerifyOtp.setVisibility(View.VISIBLE);
    }

    private void hideVerifyOtpView() {
        binding.llGetOtpLayout.setVisibility(View.GONE);
        binding.btnVerifyOtp.setVisibility(View.GONE);
    }

    private void hideSendOtpButton() {
        binding.btnSendOtp.setVisibility(View.GONE);
        binding.llGetMobileNumberLayout.setVisibility(View.GONE);
    }


    public void sendOtp(View view) {
        if (binding.edtMobileNumber.getText().toString().isEmpty()) {
            binding.edtMobileNumber.setError("Enter number");
        } else {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    binding.countryPicker.getSelectedCountryCodeWithPlus() + binding.edtMobileNumber.getText().toString().trim(),        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks

            showVerifyOtpView();
            hideSendOtpButton();
        }
    }

    public void verifyOtp(View view) {
        if (binding.edtOtp.getText().toString().isEmpty()) {
            binding.edtOtp.setError("Enter otp");
        } else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, binding.edtOtp.getText().toString().trim());
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            Log.d(TAG, "mobile number: " + user.getPhoneNumber());
                            mMobileNumber = user.getPhoneNumber();
                            showMessage("Mobile number verified");
                            hideSendOtpButton();
                            hideVerifyOtpView();
                            showUploadDocumentLayout();

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure" + task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                showErrorMessage("Invalid OTP");
                              /*  hideSendOtpButton();
                                hideVerifyOtpView();
                                showUploadDocumentLayout();*/
                            }
                        }
                    }
                });
    }


    public void getAreaFromFireStore() {
        Area area = new Area();
        area.setArea_name(getActivity().getString(R.string.select_area));
        areaList.add(area);
        getFireStoreDb().collection(FS_COL_AREA).orderBy(FS_KEY_AREA_CREATED_DATE_AND_TIME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    areaList.addAll(task.getResult().toObjects(Area.class));

                    ArrayAdapter<Area> areaSpinnerAdapter = new ArrayAdapter<Area>(getActivity(), R.layout.inflate_spinner_area_black_text, areaList);

                    // Drop down layout style - list view with radio button
                    areaSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    binding.sprArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }

                    });
                    binding.sprArea.setAdapter(areaSpinnerAdapter);
                } else {

                }
            }
        });
    }

    public void submitDoctorDetails(View view) {
        if (ForDoctorLogin) {
            if (validDoctorForm()) {
                showProgressbar();
                Map<String, Object> map = new HashMap<>();
                map.put(FS_KEY_MOBILE_NUMBER, (mMobileNumber == null ? SharedPref.getInstance().getStringValue(this, FS_KEY_MOBILE_NUMBER) : mMobileNumber));
                map.put(FS_KEY_IS_DOCTOR, true);
                map.put(FS_KEY_IS_DOCTOR_APPROVED, false);
                map.put(FS_KEY_DOCTOR_LICENCE_NUMBER, binding.edtLicenceNumber.getText().toString().trim());
                map.put(FS_KEY_AREA, SharedPref.getInstance().getStringValue(this, FS_KEY_AREA) == null ? binding.sprArea.getSelectedItem().toString() : SharedPref.getInstance().getStringValue(this, FS_KEY_AREA));
                map.put(FS_KEY_DOCTOR_DOCUMENT_URL, strDoctorDocumentImageUrl);

                getFireStoreDb().collection(FS_COL_USER).document(SharedPref.getInstance().getUserId(this)).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dismissProgressbar();
                        if (task.isSuccessful()) {
                            showInfoMessage("Form submitted");
                            finish();
                        } else {
                            showErrorMessage("Something went wrong");

                        }
                    }
                });
            }
        } else if (ForSellersLogin || ForFarmOwnersLogin) {
            if (validForm()) {
                showSelectTypeDialog();

            }
        }


    }

    private void showSelectTypeDialog() {


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
                submitSellerFarmOwnerForm(type);


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

    private void submitSellerFarmOwnerForm(String type) {
        showProgressbar();
        Map<String, Object> map = new HashMap<>();
        map.put(FS_KEY_MOBILE_NUMBER, (mMobileNumber == null ? SharedPref.getInstance().getStringValue(this, FS_KEY_MOBILE_NUMBER) : mMobileNumber));
        if (ForSellersLogin) {
            map.put(FS_KEY_IS_SELLER, true);
            map.put(FS_KEY_IS_SELLER_APPROVED, true);
            map.put(FS_KEY_SELLING_ANIMAL_TYPE, type);

        } else if (ForFarmOwnersLogin) {
            map.put(FS_KEY_IS_FARM_OWNER, true);
            map.put(FS_KEY_IS_FARM_OWNER_APPROVED, true);
            map.put(FS_KEY_FARM_ANIMAL_TYPE, type);

        }
        map.put(FS_KEY_AREA, SharedPref.getInstance().getStringValue(this, FS_KEY_AREA) == null ? binding.sprArea.getSelectedItem().toString() : SharedPref.getInstance().getStringValue(this, FS_KEY_AREA));

        getFireStoreDb().collection(FS_COL_USER).document(SharedPref.getInstance().getUserId(this)).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dismissProgressbar();
                if (task.isSuccessful()) {
                    showInfoMessage("Form submitted");
                    finish();
                } else {
                    showErrorMessage("Something went wrong");

                }
            }
        });
    }

    private boolean validForm() {
        int count = 0;

        if (binding.sprArea.getSelectedItemPosition() == 0) {
            showErrorMessage("Select area");

            count++;
        }
        return count == 0 ? true : false;
    }

    private boolean validDoctorForm() {
        int count = 0;

        if (!CommonUtils.getInstance().isNullCheck(binding.edtLicenceNumber.getText().toString())) {
            binding.edtLicenceNumber.setError("Enter licence number");
            count++;

        } else {
            binding.edtLicenceNumber.setError(null);
        }

        if (strDoctorDocumentImageUrl == null) {
            showErrorMessage("Add image/Image uploading");
            count++;

        }
        if (binding.sprArea.getSelectedItemPosition() == 0) {
            showErrorMessage("Select area");

            count++;
        }
        return count == 0 ? true : false;
    }


    private void callGalleryPic() {
        Intent intent;
        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_GALLERY_IMAGE_REQUEST_CODE);
    }

    public void choiceFromGallery(View view) {
        if (CommonUtils.getInstance().isAboveMarshmallow()) {
            iPermissionHandler.callStoragePermissionHandler();
        } else {
            callGalleryPic();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult");
        switch (requestCode) {


            case PICK_GALLERY_IMAGE_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {

                    Log.e(TAG, "File Path: " + data.getData());
                    Uri filePath = data.getData();
                    try {


                        uploadFile(filePath);

                    } catch (Exception e) {
                        ExceptionTracker.track(e);
                    }
                } else {
                    showErrorMessage("Sorry! Failed to get image");
                }

                break;

        }
    }

    private void uploadFile(Uri filePath) {
        showImageUploadProgressBar();
        String path = FS_DOCTOR_DOCUMNET_IMAGE_PATH + "UserId:" + SharedPref.getInstance().getUserId(getActivity()) + " Date:" + new Date() + ".jpg";
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
                    strDoctorDocumentImageUrl = downloadUri.toString();
                } else {

                    hideImageUploadProgressBar(false);

                    showMessage(task.getException().getMessage());

                    // Handle failures
                    // ...
                }
            }
        });

    }

    private void hideImageUploadProgressBar(boolean success) {
        binding.progressBar.setVisibility(View.GONE);
        if (success) {
            binding.addImageText.setText("Image uploaded");
            binding.add.setVisibility(View.GONE);
            binding.done.setVisibility(View.VISIBLE);


        } else {
            binding.addImageText.setText("Image upload failed.Try again");
            binding.done.setVisibility(View.GONE);
            binding.add.setVisibility(View.VISIBLE);

        }

    }


    private void showImageUploadProgressBar() {
        binding.add.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onReceivedPermissionStatus(int code, boolean isGrated) {
        Log.e(TAG, "onReceivedPermissionStatus");

        switch (code) {

            case IPermissionHandler.PERMISSIONS_REQUEST_GALERY_AND_STORAGE:
                if (isGrated) {
                    callGalleryPic();
                }
                break;
        }

    }

    @Override
    public void onClickItem(ThagavalKalanchiyam data) {

    }

    @Override
    public void onClickItem(View itemView, ThagavalKalanchiyam data) {

    }
}
