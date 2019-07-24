package com.aadu_maadu_kozhi.gregantech.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.common.Constants;
import com.aadu_maadu_kozhi.gregantech.customview.GRadioGroup;
import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.model.pojo.Area;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ChatMessage;
import com.aadu_maadu_kozhi.gregantech.model.pojo.User;
import com.aadu_maadu_kozhi.gregantech.presenter.HomeActivityPresenter;
import com.aadu_maadu_kozhi.gregantech.presenter.ipresenter.IHomeActivityPresenter;
import com.aadu_maadu_kozhi.gregantech.util.CommonUtils;
import com.aadu_maadu_kozhi.gregantech.util.FireStoreKey;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;
import com.aadu_maadu_kozhi.gregantech.view.fragment.AccessoriesListFragment;
import com.aadu_maadu_kozhi.gregantech.view.fragment.ChatBottomSheet;
import com.aadu_maadu_kozhi.gregantech.view.fragment.ChatFragment;
import com.aadu_maadu_kozhi.gregantech.view.fragment.DoctorsListFragment;
import com.aadu_maadu_kozhi.gregantech.view.fragment.FarmOwnerListFragment;
import com.aadu_maadu_kozhi.gregantech.view.fragment.HomeFragment;
import com.aadu_maadu_kozhi.gregantech.view.fragment.ProfileFragment;
import com.aadu_maadu_kozhi.gregantech.view.fragment.SellerListFragment;
import com.aadu_maadu_kozhi.gregantech.view.fragment.SuccessStoryFragment;
import com.aadu_maadu_kozhi.gregantech.view.fragment.ThagavalKalanchiyamFragment;
import com.aadu_maadu_kozhi.gregantech.view.iview.IHomeActivityView;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_COL_USER;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_APP_VERSION;
import static com.aadu_maadu_kozhi.gregantech.util.FireStoreKey.FS_KEY_TOKEN;

public class HomeActivity extends BaseActivity
        implements IHomeActivityView, View.OnClickListener, GRadioGroup.INavigationRadioButtonClick {
    private IHomeActivityPresenter iHomeActivityPresenter;
    private FloatingActionButton fab;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private ThagavalKalanchiyamFragment thagavalKalanchiyamFragment;
    private DoctorsListFragment doctorsListFragment;
    private ChatFragment chatFragment;
    private SuccessStoryFragment successStoryFragment;
    private FarmOwnerListFragment farmOwnerListFragment;
    private SellerListFragment sellerListFragment;
    private AccessoriesListFragment accessoriesListFragment;
    private Spinner sprArea;
    private int lastPosition = 0;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private TextView tvTitle;
    private RadioButton rbThagavalKalanchiyam, rbSuccessStory, rbNewsFeed, rbProfile, rbChat, rbLogout, rbDoctor, rbFertilizer, rbFarm, rbSeller;
    private RelativeLayout rlThagavalKalanchiyamParent, rlNewsFeedParent, rlSuccessStoryParent, rlProfileParent, rlChatParent, rlLogoutParent, rlDoctorParent, rlSellerParent, rlFertilizerParent, rlFarmParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_home);
        rbThagavalKalanchiyam = findViewById(R.id.rbThagavalKalanchiyam);
        rbSuccessStory = findViewById(R.id.rbSuccessStory);
        rbNewsFeed = findViewById(R.id.rbNewsFeed);
        rbProfile = findViewById(R.id.rbProfile);
        rbChat = findViewById(R.id.rbChat);
        rbLogout = findViewById(R.id.rbLogout);
        rbDoctor = findViewById(R.id.rbDoctor);
        rbFertilizer = findViewById(R.id.rbAccessories);
        rbSeller = findViewById(R.id.rbSeller);
        rbFarm = findViewById(R.id.rbFarm);
        rlThagavalKalanchiyamParent = findViewById(R.id.rlThagavalKalanchiyamParent);
        rlDoctorParent = findViewById(R.id.rlDoctorParent);
        rlProfileParent = findViewById(R.id.rlProfileParent);
        rlNewsFeedParent = findViewById(R.id.rlNewsFeedParent);
        rlChatParent = findViewById(R.id.rlChatParent);
        rlSuccessStoryParent = findViewById(R.id.rlSuccessStoryParent);
        rlLogoutParent = findViewById(R.id.rlLogoutParent);
        rlFertilizerParent = findViewById(R.id.rlAccessoriesParent);
        rlFarmParent = findViewById(R.id.rlFarmParent);
        rlSellerParent = findViewById(R.id.rlSellerParent);
        rbNewsFeed.setOnClickListener(this);
        rlThagavalKalanchiyamParent.setOnClickListener(this);
        rlDoctorParent.setOnClickListener(this);
        rlSuccessStoryParent.setOnClickListener(this);
        rlProfileParent.setOnClickListener(this);
        rlLogoutParent.setOnClickListener(this);
        rlChatParent.setOnClickListener(this);
        rlFertilizerParent.setOnClickListener(this);
        rlFarmParent.setOnClickListener(this);
        rlSellerParent.setOnClickListener(this);


        GRadioGroup gr = new GRadioGroup(this, rbThagavalKalanchiyam, rbSuccessStory, rbNewsFeed, rbProfile, rbChat, rbLogout, rbDoctor, rbFertilizer, rbFarm, rbSeller);


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()/*.addTestDevice("FBE6FDE1A7E96B4E13769234C95DBFD5")*/.build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // showMessage("Add loaded");
                mAdView.setVisibility(View.VISIBLE);
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.INTERSTITIAL_CREATE_POST_SUCCESS));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sprArea = findViewById(R.id.spr_Area);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        tvTitle = findViewById(R.id.tvTitle);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iHomeActivityPresenter.picImage();
            }
        });

       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
        iHomeActivityPresenter = new HomeActivityPresenter(this);
        iHomeActivityPresenter.onCreatePresenter(getIntent().getExtras());
        iHomeActivityPresenter.getAreaFromFireStore();
        rbNewsFeed.performClick();
        /*tvTitle.setText(getString(R.string.thagaval_kalanchiyam));
        showThagavalKalanchiyamFragment();*/
        checkUpdate();
        updateFcmToken();
        handleIntent(getIntent());

    }


    private void updateFcmToken() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();
                Log.e("Token", mToken);
                getFireStoreDb().collection(FS_COL_USER).document(SharedPref.getInstance().getUserId(getActivity())).update(FS_KEY_TOKEN,
                        mToken);

            }
        });


    }

    private void checkUpdate() {
        getFireStoreDb().collection(FireStoreKey.FS_COL_APP_VERSION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshots :
                            task.getResult()) {
                        if (!CommonUtils.getInstance().checkAppVersion((int) (long) queryDocumentSnapshots.get(FS_KEY_APP_VERSION), HomeActivity.this)) {

                            CommonUtils.getInstance().showAppUpdateDialog(HomeActivity.this);
                        }
                        break;

                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

   /* @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            tvTitle.setText(getString(R.string.home));
            showHomeFragment();
        } else if (id == R.id.nav_profile) {
            tvTitle.setText(getString(R.string.profile));

            showProfileFragment();

        } else if (id == R.id.nav_thagaval_kalanchiyam) {
            tvTitle.setText(getString(R.string.thagaval_kalanchiyam));

            showThagavalKalanchiyamFragment();

        } else if (id == R.id.nav_chat) {
            tvTitle.setText(getString(R.string.chat));

            showChatFragment();

        } else if (id == R.id.nav_log_out) {
            AlertDialog.Builder alert = new AlertDialog.Builder(
                    HomeActivity.this);
            alert.setTitle("Logout");
            alert.setMessage("நிச்சயமாக வெளியேற விரும்புகிறீர்களா?");
            alert.setPositiveButton("ஆம்", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logout();


                }
            });
            alert.setNegativeButton("இல்லை", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            alert.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, Constants.RequestCodes.REQUEST_CODE_PIC_IMAGE, Constants.Common.NUMBER_OF_IMAGES_TO_SELECT);
                } else {
                    Toast.makeText(HomeActivity.this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    private void showAccessoriesListFragment() {
        hideAreaSpinner();

        if (accessoriesListFragment != null) {

            hideAllFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .show(accessoriesListFragment).commit();
            /*   FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, thagavalKalanchiyamFragment);
            fragmentTransaction.commit();*/
        } else {
            accessoriesListFragment = new AccessoriesListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, accessoriesListFragment, accessoriesListFragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }
    }

    private void showSellersListFragment() {
        hideAreaSpinner();

        if (sellerListFragment != null) {

            hideAllFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .show(sellerListFragment).commit();
            /*   FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, thagavalKalanchiyamFragment);
            fragmentTransaction.commit();*/
        } else {
            sellerListFragment = new SellerListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, sellerListFragment, sellerListFragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }
    }

    private void showFarmHoldersListFragment() {
        hideAreaSpinner();

        if (farmOwnerListFragment != null) {

            hideAllFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .show(farmOwnerListFragment).commit();
            /*   FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, thagavalKalanchiyamFragment);
            fragmentTransaction.commit();*/
        } else {
            farmOwnerListFragment = new FarmOwnerListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, farmOwnerListFragment, farmOwnerListFragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }

    }

    private void showSuccessStoryFragment() {
        hideAreaSpinner();

        if (successStoryFragment != null) {

            hideAllFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .show(successStoryFragment).commit();
            /*   FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, thagavalKalanchiyamFragment);
            fragmentTransaction.commit();*/
        } else {
            successStoryFragment = new SuccessStoryFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, successStoryFragment, successStoryFragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }
    }

    private void showChatFragment() {
        hideAreaSpinner();

        if (chatFragment != null) {

            hideAllFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .show(chatFragment).commit();
            /*   FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, thagavalKalanchiyamFragment);
            fragmentTransaction.commit();*/
        } else {
            chatFragment = new ChatFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, chatFragment, chatFragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }
    }

    private void showDoctorsListFragment() {
        hideAreaSpinner();

        if (doctorsListFragment != null) {

            hideAllFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .show(doctorsListFragment).commit();
            /*   FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, thagavalKalanchiyamFragment);
            fragmentTransaction.commit();*/
        } else {
            doctorsListFragment = new DoctorsListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, doctorsListFragment, doctorsListFragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }
    }

    private void showThagavalKalanchiyamFragment() {
        hideAreaSpinner();

        if (thagavalKalanchiyamFragment != null) {

            hideAllFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .show(thagavalKalanchiyamFragment).commit();
            /*   FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, thagavalKalanchiyamFragment);
            fragmentTransaction.commit();*/
        } else {
            thagavalKalanchiyamFragment = new ThagavalKalanchiyamFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, thagavalKalanchiyamFragment, thagavalKalanchiyamFragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }
    }

    private void showProfileFragment() {
        hideAreaSpinner();

        if (profileFragment != null) {
            hideAllFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .show(profileFragment).commit();
            /*
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, profileFragment);
            fragmentTransaction.commit();*/
        } else {
            profileFragment = new ProfileFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, profileFragment, profileFragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }
    }


    private void showHomeFragment() {
        showAreaSpinner();

        if (homeFragment != null) {

            hideAllFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .show(homeFragment).commit();
            /* FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, homeFragment);
            fragmentTransaction.commit();*/
        } else {
            homeFragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, homeFragment, homeFragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }
    }

    private void hideAllFragment() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
        }
    }

    private void showAreaSpinner() {
        sprArea.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);

    }

    private void hideAreaSpinner() {
        sprArea.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);

    }

    @Override
    public void setAreaSpinnerAdapter(ArrayAdapter<Area> areaSpinnerAdapter) {
        //   sprArea.setVisibility(View.VISIBLE);
        sprArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (lastPosition != position) {
                    lastPosition = position;
                    refreshUserPost(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        sprArea.setAdapter(areaSpinnerAdapter);

    }

    @Override
    public void refreshUserPost(int position) {

        if (homeFragment != null && homeFragment.getFireStoreDb() != null) {
            homeFragment.getPostByAreaName(iHomeActivityPresenter.getAreaName(position));
        }
    }

    @Override
    public int getAreaSpinnerSelectedPostion() {
        return sprArea.getSelectedItemPosition();
    }

    @Override
    public void showInterstitialAdds() {
        if (mInterstitialAd.isLoaded()) {
        //    mInterstitialAd.show();
        } else {
            Log.d(TAG, "The interstitial wasn't loaded yet.");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void picImage() {
        iHomeActivityPresenter.picImage();

    }

    @Override
    public void onBackPressed() {

        if (!(tvTitle.getText().toString().equalsIgnoreCase(getString(R.string.home)))) {
            rbNewsFeed.performClick();
            ((HorizontalScrollView) findViewById(R.id.hsvMainMenu)).smoothScrollTo(0, 0);
        } else {
            showExitDialogue();
        }

    }

    private void logout() {
        iHomeActivityPresenter.updateUserOnline(false);
        SharedPref.getInstance().clearAll(HomeActivity.this);
        startActivity(new Intent(HomeActivity.this, SplashActivity.class));
        finish();
    }

    private void showExitDialogue() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlThagavalKalanchiyamParent:
                rlThagavalKalanchiyamParent.getChildAt(0).performClick();
                break;
            case R.id.rlNewsFeedParent:
                rlNewsFeedParent.getChildAt(0).performClick();

                break;
            case R.id.rlDoctorParent:
                rlDoctorParent.getChildAt(0).performClick();

                break;
            case R.id.rlSuccessStoryParent:
                rlSuccessStoryParent.getChildAt(0).performClick();

                break;
            case R.id.rlProfileParent:
                rlProfileParent.getChildAt(0).performClick();

                break;
            case R.id.rlChatParent:
                rlChatParent.getChildAt(0).performClick();

                break;
            case R.id.rlLogoutParent:
                rlLogoutParent.getChildAt(0).performClick();

                break;
            case R.id.rlAccessoriesParent:
                rlFertilizerParent.getChildAt(0).performClick();

                break;
            case R.id.rlFarmParent:
                rlFarmParent.getChildAt(0).performClick();

                break;
            case R.id.rlSellerParent:
                rlSellerParent.getChildAt(0).performClick();

                break;
        }
    }

    @Override
    public void onRadioButtonSelect(int id) {
        switch (id) {
            case R.id.rbThagavalKalanchiyam:
                tvTitle.setText(getString(R.string.thagaval_kalanchiyam));

                showThagavalKalanchiyamFragment();

                break;
            case R.id.rbNewsFeed:
                tvTitle.setText(getString(R.string.home));
                showHomeFragment();
                break;
            case R.id.rbSuccessStory:
                tvTitle.setText(getString(R.string.succss_stories));

                showSuccessStoryFragment();
                break;
            case R.id.rbProfile:
                tvTitle.setText(getString(R.string.profile));

                showProfileFragment();
                break;
            case R.id.rbChat:
                tvTitle.setText(getString(R.string.chat));

                showChatFragment();
                break;
            case R.id.rbLogout:
                showLogoutDialog();
                break;
            case R.id.rbDoctor:
                tvTitle.setText(getString(R.string.doctors));

                showDoctorsListFragment();
                break;
            case R.id.rbAccessories:
                tvTitle.setText(getString(R.string.accessories));

                showAccessoriesListFragment();
                break;
            case R.id.rbFarm:
                tvTitle.setText(getString(R.string.farm_holders));

                showFarmHoldersListFragment();
                break;
            case R.id.rbSeller:
                tvTitle.setText(getString(R.string.sellers));

                showSellersListFragment();
                break;

        }
    }




    private void showLogoutDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                HomeActivity.this);
        alert.setTitle("Logout");
        alert.setMessage("நிச்சயமாக வெளியேற விரும்புகிறீர்களா?");
        alert.setPositiveButton("ஆம்", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();


            }
        });
        alert.setNegativeButton("இல்லை", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alert.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey(Constants.BundleKey.For)) {
            Log.e(TAG, extras.getString(Constants.BundleKey.For));
            switch (extras.getString(Constants.BundleKey.For)) {

                case Constants.BundleKey.UserChat:
                    rlChatParent.performClick();
                    showChat((ChatMessage) intent.getParcelableExtra(Constants.BundleKey.UserChat), (User) intent.getParcelableExtra(Constants.BundleKey.User));
                    break;

            }
        }
    }

    private void showChat(ChatMessage chatMessage, User user) {
        ChatBottomSheet chatBottomSheet = new ChatBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BundleKey.FIRST_USER_ID, chatMessage.getChat_users().get(0));
        bundle.putString(Constants.BundleKey.SECOND_USER_ID, chatMessage.getChat_users().get(1));
        bundle.putParcelable(Constants.BundleKey.USER, user);

        chatBottomSheet.setArguments(bundle);
        chatBottomSheet.show(getActivity().getSupportFragmentManager(), chatBottomSheet.getTag());
    }
    @Override
    public void sendCreatePostNotification() {

    }

}
