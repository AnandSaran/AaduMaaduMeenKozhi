package com.aadu_maadu_kozhi.gregantech.retrofit;


/**
 * Created by anand_android on 9/28/2016.
 */
public interface ResponseListener<T> {
    void onSuccess(T mResponse, long flag);
    void onFailureApi(Throwable mThrowable);
   // void showDialog(T mResponse, long flag);
    void showErrorDialog(T mResponse, long flag);
   // void showInternalServerErrorDialog(T mResponse, long flag);
    //void logOut(long flag);

}
