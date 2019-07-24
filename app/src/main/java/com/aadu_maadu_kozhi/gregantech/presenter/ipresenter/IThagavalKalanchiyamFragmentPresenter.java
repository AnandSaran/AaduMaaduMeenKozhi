package com.aadu_maadu_kozhi.gregantech.presenter.ipresenter;

import android.net.Uri;

/**
 * Created by Anand on 8/27/2017.
 */

public interface IThagavalKalanchiyamFragmentPresenter extends IPresenter {

    void getThagavalKalanchiyamHeading();

    void showAddThagavalKalanchiyamHeadingDialog();

    void uploadFile(Uri filePath);

    void checkThagavalKalanchiyamAdmin();
}
