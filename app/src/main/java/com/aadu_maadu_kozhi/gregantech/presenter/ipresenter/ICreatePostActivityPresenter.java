package com.aadu_maadu_kozhi.gregantech.presenter.ipresenter;

import com.aadu_maadu_kozhi.gregantech.model.pojo.Area;
import com.aadu_maadu_kozhi.gregantech.model.pojo.ThagavalKalanchiyam;

/**
 * Created by Anand on 8/27/2017.
 */

public interface ICreatePostActivityPresenter extends IPresenter {

    void uploadImage(int imagePostion);

    void showAreaBottomSheet();

    void setArea(Area area);

    void setThagavalKalanchiyamData(ThagavalKalanchiyam data);

    void showThagavalKalanchiyamBottomSheet();
}
