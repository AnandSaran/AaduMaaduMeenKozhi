package com.aadu_maadu_kozhi.gregantech.util.permission;


import com.aadu_maadu_kozhi.gregantech.view.iview.IView;

public interface PermissionProducer extends IView {
    void onReceivedPermissionStatus(int code, boolean isGrated);
}
