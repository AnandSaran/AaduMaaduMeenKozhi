package com.aadu_maadu_kozhi.gregantech.view.iview;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.view.View;

import com.aadu_maadu_kozhi.gregantech.presenter.ipresenter.IPresenter;
import com.aadu_maadu_kozhi.gregantech.util.CodeSnippet;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;


public interface IView {

    void showMessage(String message);
    void showInfoMessage(String message);

    void showMessage(int resId);
    void showErrorMessage(String message);


    void showProgressbar();

    void dismissProgressbar();

    FragmentActivity getActivity();

    void showSnackBar(String message);

    void showSnackBar(@NonNull View view, String message);

    void showNetworkMessage();

    CodeSnippet getCodeSnippet();

    void bindPresenter(IPresenter iPresenter);

    boolean checkNetWork();


    FirebaseFirestore getFireStoreDb();
    String sendPushNotification(String to, Map<String, Object> newCall);
    String sendMultiplePushNotification(List<String> to, Map<String, Object> newCall);

}
