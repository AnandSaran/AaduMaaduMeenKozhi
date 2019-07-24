package com.aadu_maadu_kozhi.gregantech.model.webservice;



import com.aadu_maadu_kozhi.gregantech.library.Log;
import com.aadu_maadu_kozhi.gregantech.model.dto.response.BaseResponse;
import com.aadu_maadu_kozhi.gregantech.model.dto.response.BaseResponseList;
import com.aadu_maadu_kozhi.gregantech.retrofit.ResponseListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public  class BaseModel {
    protected String TAG = this.getClass().getSimpleName();
    protected long mCurrentTaskId = -1;
    ResponseListener IResponseListener;

    public BaseModel(ResponseListener listener) {
        IResponseListener = listener;
    }

   /* protected void enQueueTask(long taskId, Call<T> tCall) {
        this.mCurrentTaskId = taskId;
        tCall.enqueue(baseModelCallBackListener);
    }
*/

    protected <T> void validateBaseResponseList(Call<BaseResponseList<T>> mNativeRegisterResponseCall) {
        mNativeRegisterResponseCall.enqueue(new Callback<BaseResponseList<T>>() {
            @Override
            public void onResponse(Call<BaseResponseList<T>> call, Response<BaseResponseList<T>> response) {
                Log.e(TAG, "response.code(): " + response.code());
                Log.e(TAG, "response.code(): " + response.raw().code());


                switch (response.code()) {
                    case 200://success response
                       if (response.body().getSuccess()) {

                            IResponseListener.onSuccess(response.body(), mCurrentTaskId);
                      }else {
                            IResponseListener.showErrorDialog(response.body(),mCurrentTaskId);

                        }
                     break;

                    default://Request error

                        IResponseListener.showErrorDialog(response.body(),mCurrentTaskId);


                        break;
                  /*  case 401://ERROR
                        IResponseListener.showDialog(response.body().getMessage());

                        break;*/

                }
            }

            @Override
            public void onFailure(Call<BaseResponseList<T>> call, Throwable t) {
                IResponseListener.onFailureApi(t);

            }
        });


    }

    protected <T> void validateBaseResponse(Call<BaseResponse<T>> mNativeRegisterResponseCall) {
        mNativeRegisterResponseCall.enqueue(new Callback<BaseResponse<T>>() {
            @Override
            public void onResponse(Call<BaseResponse<T>> call, Response<BaseResponse<T>> response) {
                Log.e(TAG, "response.code(): " + response.code());
                Log.e(TAG, "response.code(): " + response.raw().code());


                switch (response.code()) {
                    case 200://success response
/*                        if (response.body().getSuccess()) {*/

                            IResponseListener.onSuccess(response.body(), mCurrentTaskId);
                      /*  }else {
                            IResponseListener.showErrorDialog(response.body(),mCurrentTaskId);

                        }
                      */  break;

                    default://Request error

                        IResponseListener.showErrorDialog(response.body(),mCurrentTaskId);


                        break;
                  /*  case 401://ERROR
                        IResponseListener.showDialog(response.body().getMessage());

                        break;*/

                }
            }

            @Override
            public void onFailure(Call<BaseResponse<T>> call, Throwable t) {
                IResponseListener.onFailureApi(t);

            }
        });


    }

}
