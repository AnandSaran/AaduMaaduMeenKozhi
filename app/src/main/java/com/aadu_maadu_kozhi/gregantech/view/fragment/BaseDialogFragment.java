package com.aadu_maadu_kozhi.gregantech.view.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.View;

import com.aadu_maadu_kozhi.gregantech.library.ExceptionTracker;
import com.aadu_maadu_kozhi.gregantech.presenter.ipresenter.IPresenter;
import com.aadu_maadu_kozhi.gregantech.util.CodeSnippet;
import com.aadu_maadu_kozhi.gregantech.view.iview.IView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;


public abstract class BaseDialogFragment extends DialogFragment implements IView {

    protected String TAG = getClass().getSimpleName();


    @Override
    public void bindPresenter(IPresenter iPresenter) {
        // nothing to implement here!
    }

    @Override
    public void showMessage(String message) {
        try {
            ((IView) getActivity()).showMessage(message);
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }

    @Override
    public void showMessage(int resId) {
        ((IView) getActivity()).showMessage(resId);
    }

    @Override
    public void showInfoMessage(String message) {
        ((IView) getActivity()).showInfoMessage(message);
    }


    @Override
    public void showProgressbar() {
        ((IView) getActivity()).showProgressbar();
    }

    @Override
    public void dismissProgressbar() {
        try {


            ((IView) getActivity()).dismissProgressbar();
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }

    @Override
    public void showSnackBar(String message) {
        ((IView) getActivity()).showSnackBar(message);
    }

    @Override
    public void showNetworkMessage() {
        ((IView) getActivity()).showNetworkMessage();
    }

    @Override
    public CodeSnippet getCodeSnippet() {
        return ((IView) getActivity()).getCodeSnippet();
    }

    @Override
    public void showSnackBar(@NonNull View view, String message) {
        ((IView) getActivity()).showSnackBar(view, message);
    }

    @Override
    public boolean checkNetWork() {
        return ((IView) getActivity()).checkNetWork();

    }

    @Override
    public void showErrorMessage(String message) {
        ((IView) getActivity()).showErrorMessage(message);

    }

    @Override
    public FirebaseFirestore getFireStoreDb() {
        return ((IView) getActivity()).getFireStoreDb();
    }

    @Override
    public String sendPushNotification(String to, Map<String, Object> newCall) {

        return ((IView) getActivity()).sendPushNotification( to, newCall);
    }
    @Override
    public String sendMultiplePushNotification(List<String> to, Map<String, Object> newCall) {

        return ((IView) getActivity()).sendMultiplePushNotification( to, newCall);
    }
}
