package com.aadu_maadu_kozhi.gregantech.model.webservice;


import com.aadu_maadu_kozhi.gregantech.model.pojo.UserCreatePostFCM;
import com.aadu_maadu_kozhi.gregantech.retrofit.ApiClient;
import com.aadu_maadu_kozhi.gregantech.retrofit.ApiInterface;
import com.aadu_maadu_kozhi.gregantech.retrofit.ResponseListener;

/**
 * Created by Anand on 3/11/2017.
 */

public class FcmServerModel extends BaseModel{

    public FcmServerModel(ResponseListener iResponseListener) {
        super(iResponseListener);

    }
    public void sendUserCreatePost(long taskId, UserCreatePostFCM request) {
        this.mCurrentTaskId = taskId;
     validateBaseResponse(ApiClient.getClient().create(ApiInterface.class).user_create_post(request));
    }


   /* @Override
    public void onSuccess(TrackHeadData mResponse, long flag) {
        iResponseListener.onSuccess( mResponse,flag);

    }

    @Override
    public void onFailureApi(Throwable mThrowable) {

    }

    @Override
    public void showDialog(TrackHeadData mResponse, long flag) {

    }

    @Override
    public void showErrorDialog(TrackHeadData mResponse, long flag) {

    }

    @Override
    public void showInternalServerErrorDialog(TrackHeadData mResponse, long flag) {

    }

    @Override
    public void logOut(long flag) {

    }*/
}
