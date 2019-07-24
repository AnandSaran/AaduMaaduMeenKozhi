package com.aadu_maadu_kozhi.gregantech.view.fragment;


import android.app.Activity;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.databinding.FragmentThagavalKalanchiyamBinding;
import com.aadu_maadu_kozhi.gregantech.library.ExceptionTracker;
import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.presenter.ThagavalKalanchiyamPresenter;
import com.aadu_maadu_kozhi.gregantech.presenter.ipresenter.IThagavalKalanchiyamFragmentPresenter;
import com.aadu_maadu_kozhi.gregantech.util.CommonUtils;
import com.aadu_maadu_kozhi.gregantech.util.permission.IPermissionHandler;
import com.aadu_maadu_kozhi.gregantech.util.permission.PermissionProducer;
import com.aadu_maadu_kozhi.gregantech.util.permission.RequestPermission;
import com.aadu_maadu_kozhi.gregantech.view.iview.IThagavalKalanchiyamFragmentView;


/**
 */
public class ThagavalKalanchiyamFragment extends BaseFragment implements IThagavalKalanchiyamFragmentView, PermissionProducer {

    public static final int PICK_GALLERY_IMAGE_REQUEST_CODE = 200;
    private FragmentThagavalKalanchiyamBinding binding;
    private View rootView;
    private IThagavalKalanchiyamFragmentPresenter iThagavalKalanchiyamFragmentPresenter;
    private IPermissionHandler iPermissionHandler;

    public ThagavalKalanchiyamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_thagaval_kalanchiyam, container, false);
            rootView = binding.getRoot();
            rootView.setTag(binding);
        } else {
            binding = (FragmentThagavalKalanchiyamBinding) rootView.getTag();
        }
        iPermissionHandler = RequestPermission.newInstance(this);

        binding.setHandlers(this); // fragment's context

        iThagavalKalanchiyamFragmentPresenter = new ThagavalKalanchiyamPresenter(this, binding);
        iThagavalKalanchiyamFragmentPresenter.onCreatePresenter(getArguments());

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iThagavalKalanchiyamFragmentPresenter.getThagavalKalanchiyamHeading();
        iThagavalKalanchiyamFragmentPresenter.checkThagavalKalanchiyamAdmin();

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabAdd:
                iThagavalKalanchiyamFragmentPresenter.showAddThagavalKalanchiyamHeadingDialog();
                break;
        }
    }

    @Override
    public void choiceAvatarFromGallery() {
        if (CommonUtils.getInstance().isAboveMarshmallow()) {
            iPermissionHandler.callStoragePermissionHandlerForFragment(this);
        } else {
            callGalleryPic();
        }
    }

    @Override
    public void setVisibleAddFab() {
        binding.fabAdd.setVisibility(View.VISIBLE);
    }


    private void callGalleryPic() {
        Intent intent;
        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_GALLERY_IMAGE_REQUEST_CODE);
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


                        iThagavalKalanchiyamFragmentPresenter.uploadFile(filePath);

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


}
