package com.aadu_maadu_kozhi.gregantech.view.iview;

import android.widget.ArrayAdapter;

import com.aadu_maadu_kozhi.gregantech.model.pojo.Area;

/**
 * Created by Anand on 8/27/2017.
 */

public interface IHomeActivityView extends IView {


    void setAreaSpinnerAdapter(ArrayAdapter<Area> areaSpinnerAdapter);

    void refreshUserPost(int postion);

    int getAreaSpinnerSelectedPostion();

    void showInterstitialAdds();

    void sendCreatePostNotification();
}
