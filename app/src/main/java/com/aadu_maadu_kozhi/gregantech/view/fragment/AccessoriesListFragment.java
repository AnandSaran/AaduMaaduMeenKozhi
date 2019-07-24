package com.aadu_maadu_kozhi.gregantech.view.fragment;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.databinding.FragmentAccessoriesListBinding;
import com.aadu_maadu_kozhi.gregantech.library.ExceptionTracker;
import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.presenter.AccessoriesListFragmentPresenter;
import com.aadu_maadu_kozhi.gregantech.presenter.ipresenter.IAccessoriesListFragmentPresenter;
import com.aadu_maadu_kozhi.gregantech.util.CommonUtils;
import com.aadu_maadu_kozhi.gregantech.util.permission.IPermissionHandler;
import com.aadu_maadu_kozhi.gregantech.util.permission.PermissionProducer;
import com.aadu_maadu_kozhi.gregantech.util.permission.RequestPermission;
import com.aadu_maadu_kozhi.gregantech.view.iview.IAccessoriesListFragmentView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccessoriesListFragment extends BaseFragment implements IAccessoriesListFragmentView, PermissionProducer {
private FragmentAccessoriesListBinding binding;
    public static final int PICK_GALLERY_IMAGE_REQUEST_CODE = 200;
    private View rootView;
    private IAccessoriesListFragmentPresenter iAccessoriesListFragmentPresenter;
    private IPermissionHandler iPermissionHandler;


    public AccessoriesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (rootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_accessories_list, container, false);
            rootView = binding.getRoot();
            rootView.setTag(binding);
        } else {
            binding = (FragmentAccessoriesListBinding) rootView.getTag();
        }
        iPermissionHandler = RequestPermission.newInstance(this);

        binding.setHandlers(this); // fragment's context

        iAccessoriesListFragmentPresenter = new AccessoriesListFragmentPresenter(this, binding);
        iAccessoriesListFragmentPresenter.onCreatePresenter(getArguments());

        return rootView;
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabAdd:
                iAccessoriesListFragmentPresenter.showAddAccessoriesDialog();

                break;
        }
    } @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iAccessoriesListFragmentPresenter.getAccessories();
        iAccessoriesListFragmentPresenter.checkAccessoriesApproved();

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


                        iAccessoriesListFragmentPresenter.uploadFile(filePath);

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
