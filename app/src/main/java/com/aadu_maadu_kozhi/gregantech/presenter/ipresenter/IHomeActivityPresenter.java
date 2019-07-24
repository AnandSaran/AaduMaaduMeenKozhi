package com.aadu_maadu_kozhi.gregantech.presenter.ipresenter;

/**
 * Created by Anand on 8/27/2017.
 */

public interface IHomeActivityPresenter extends IPresenter {

    void picImage();
    void getAreaFromFireStore();

    String getAreaName(int position);

    void updateUserOnline(boolean isOnline);
}
