package com.aadu_maadu_kozhi.gregantech.util.permission;


import androidx.fragment.app.Fragment;

/**
 * Created by anand_android on 10/1/2016.
 */
public interface IPermissionHandler {
    int PERMISSIONS_REQUEST_CAMERA = 40;
    int PERMISSIONS_REQUEST_RECEVIE_SMS = 41;
    int PERMISSIONS_REQUEST_CAMERA_AND_STORAGE = 42;
    int PERMISSIONS_REQUEST_GALERY_AND_STORAGE = 43;
    int PERMISSIONS_REQUEST_LOCATION = 44;

    void callSmsPermissionHandler();

    void callCameraPermissionHandler();

    void callCameraAndStoragePermissionHandler();

    void callStoragePermissionHandler();
    void callLocationPermissionHandler();
    void callStoragePermissionHandlerForFragment(Fragment fragment);


}
