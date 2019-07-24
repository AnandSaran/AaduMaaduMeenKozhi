package com.aadu_maadu_kozhi.gregantech.presenter.ipresenter;

import android.net.Uri;

/**
 * Created by Anand on 8/27/2017.
 */

public interface IAccessoriesListFragmentPresenter extends IPresenter {

    void getAccessories();

    void showAddAccessoriesDialog();

    void uploadFile(Uri filePath);

    void checkAccessoriesApproved();
}
