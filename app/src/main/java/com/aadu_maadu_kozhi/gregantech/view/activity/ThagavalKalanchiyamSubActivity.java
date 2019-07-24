package com.aadu_maadu_kozhi.gregantech.view.activity;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.databinding.ActivityThagavalKalanchiyamSubBinding;
import com.aadu_maadu_kozhi.gregantech.library.ExceptionTracker;
import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.presenter.ThagavalKalanchiyamSubActivityPresenter;
import com.aadu_maadu_kozhi.gregantech.presenter.ipresenter.IThagavalKalanchiyamSubActivitytPresenter;
import com.aadu_maadu_kozhi.gregantech.util.CommonUtils;
import com.aadu_maadu_kozhi.gregantech.util.permission.IPermissionHandler;
import com.aadu_maadu_kozhi.gregantech.util.permission.PermissionProducer;
import com.aadu_maadu_kozhi.gregantech.util.permission.RequestPermission;
import com.aadu_maadu_kozhi.gregantech.view.iview.IThagavalKalanchiyamSubActivityView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;


import static com.aadu_maadu_kozhi.gregantech.view.fragment.ThagavalKalanchiyamFragment.PICK_GALLERY_IMAGE_REQUEST_CODE;

public class ThagavalKalanchiyamSubActivity extends BaseActivity implements IThagavalKalanchiyamSubActivityView,PermissionProducer {
    private ActivityThagavalKalanchiyamSubBinding binding;
    private IThagavalKalanchiyamSubActivitytPresenter iThagavalKalanchiyamSubActivitytPresenter;
    private IPermissionHandler iPermissionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_thagaval_kalanchiyam_sub);
        binding.setHandlers(this);
        setUpAdd();

        iPermissionHandler = RequestPermission.newInstance(this);

        iThagavalKalanchiyamSubActivitytPresenter = new ThagavalKalanchiyamSubActivityPresenter(this,binding);
        iThagavalKalanchiyamSubActivitytPresenter.onCreatePresenter(getIntent().getExtras());


        iThagavalKalanchiyamSubActivitytPresenter. getThagavalKalanchiyamSubHeading();
        iThagavalKalanchiyamSubActivitytPresenter. checkThagavalKalanchiyamAdmin();

    }
    private void setUpAdd() {

        AdRequest adRequest = new AdRequest.Builder()
                //   .addTestDevice("821E8BF14772CBA5E715CA4AE95A189F")
                .build();
        binding.adView.loadAd(adRequest);
        binding.adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                binding.adView.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    public void setVisibleAddFab() {
        binding.fabAdd.setVisibility(View.VISIBLE);
    }

    @Override
    public void choiceAvatarFromGallery() {
        if (CommonUtils.getInstance().isAboveMarshmallow()) {
            iPermissionHandler.callStoragePermissionHandler();
        } else {
            callGalleryPic();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabAdd:
                iThagavalKalanchiyamSubActivitytPresenter.showAddThagavalKalanchiyamSubHeadingDialog();
                break;
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult");
        switch (requestCode) {


            case PICK_GALLERY_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    Log.e(TAG, "File Path: " + data.getData());
                    Uri filePath = data.getData();
                    try {


                        iThagavalKalanchiyamSubActivitytPresenter.uploadFile(filePath);

                    } catch (Exception e) {
                        ExceptionTracker.track(e);
                    }
                } else {
                    showErrorMessage("Sorry! Failed to get image");
                }

                break;

        }
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
    private void callGalleryPic() {
        Intent intent;
        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_GALLERY_IMAGE_REQUEST_CODE);
    }


}
