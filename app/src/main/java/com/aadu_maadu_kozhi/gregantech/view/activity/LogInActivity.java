package com.aadu_maadu_kozhi.gregantech.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;
import com.aadu_maadu_kozhi.gregantech.util.Constants;
import com.aadu_maadu_kozhi.gregantech.util.FireStoreKey;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_AREA;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_DOCTOR_DOCUMENT_URL;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_DOCTOR_LICENCE_NUMBER;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_FB_ID;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_GMAIL_ID;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_IS_DOCTOR;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_IS_DOCTOR_APPROVED;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_IS_FARM_OWNER;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_IS_FARM_OWNER_APPROVED;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_IS_SELLER;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_IS_SELLER_APPROVED;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_MOBILE_NUMBER;

public class LogInActivity extends BaseActivity implements View.OnClickListener {
    private static final String EMAIL = "email";
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1001;
    private User user = new User();
    private ImageView ivBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPref.getInstance().getStringValue(getActivity(), getString(R.string.user_id)) != null) {

            startHomeScreen();
            finish();
        }
        setContentView(R.layout.activity_log_in);
        ivBackground = (ImageView) findViewById(R.id.background);
        loadBackgroundImage();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.rlFbLogin).setOnClickListener(this);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                showProgressbar();
                // App code
                getUserDetails(loginResult);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    private void loadBackgroundImage() {
        if (Build.VERSION.SDK_INT >= 16) {
            Drawable drawable = (ContextCompat.getDrawable(this, R.drawable.bg));
            ((ViewGroup) ivBackground.getParent()).setBackground(new BitmapDrawable(getResources(), Constants.fastblur(Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), 50, 50, true))));// ));
        }
        Glide.with(getActivity())
                .load(R.drawable.bg)
                .into(ivBackground);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    protected void getUserDetails(LoginResult loginResult) {
        Bundle params = new Bundle();
        params.putString("fields", "id,email,name,gender,cover,picture.type(large)");
        new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            JSONObject data = response.getJSONObject();
                            Log.e(TAG, data.toString());
                            try {
                                if (data.has("picture")) {
                                    String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url") == null
                                            ? getString(R.string.default_user_profile_img_url)
                                            : data.getJSONObject("picture").getJSONObject("data").getString("url");
                                    user.setImage_url(profilePicUrl);
                                }
                                user.setEmail(data.optString("email"));
                                user.setName(data.optString("name"));
                                user.setFb_id(data.optString("id"));
                                checkUserAlreadyExit(FS_KEY_FB_ID, user.getFb_id());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                                    /*URL fb_url = new URL(profilePicUrl);
                                    Bitmap profilePic = BitmapFactory.decodeStream(fb_url.openConnection().getInputStream());
                                    ImageView mImageView = findViewById(R.id.iv);

                                    mImageView.setImageBitmap(profilePic);*/


                        }
                    }
                }).executeAsync();
        Bundle permission_param = new Bundle();


    }


    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.rlFbLogin:
                loginButton.performClick();
                break;
            // ...
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            showProgressbar();
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.e(TAG, "Name: " + account.getDisplayName() + " image: " + account.getPhotoUrl());
            user.setImage_url(account.getPhotoUrl() != null
                    ? String.valueOf(account.getPhotoUrl())
                    : getString(R.string.default_user_profile_img_url));

            user.setEmail(account.getEmail());
            user.setName(account.getDisplayName());
            user.setGmail_id(account.getId());
            checkUserAlreadyExit(FS_KEY_GMAIL_ID, user.getGmail_id());

        } catch (ApiException e) {
            dismissProgressbar();
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }

    private void checkUserAlreadyExit(String key, String value) {
        db.collection(FireStoreKey.FS_COL_USER).whereEqualTo(key, value).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments() == null || queryDocumentSnapshots.getDocuments().size() == 0) {
                    fsLogin();
                    return;
                }
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    user = document.toObject(User.class);
                    user.setUser_id(document.getId());
                    saveUserProfileInSharedPreference(document.getId());
                    showSnackBar("Log-in successful");
                    startHomeScreen();
                    return;
                }
            }
        });
    }

    private void saveUserProfileInSharedPreference(String id) {
        SharedPref.getInstance().setSharedValue(getActivity(), getString(R.string.user_id), id);
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

    private void fsLogin() {
        db.collection(FireStoreKey.FS_COL_USER).add(user).addOnSuccessListener(getActivity(), new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                saveUserProfileInSharedPreference(documentReference.getId());
                showSnackBar("Sign-up successful");
                startHomeScreen();

            }
        });

    }

    private void signOut() {
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });
            LoginManager.getInstance().logOut();
        }
    }

    private void startHomeScreen() {
        dismissProgressbar();
        signOut();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LogInActivity.this, HomeActivity.class));
                finish();
            }
        }, 400);

    }
}
