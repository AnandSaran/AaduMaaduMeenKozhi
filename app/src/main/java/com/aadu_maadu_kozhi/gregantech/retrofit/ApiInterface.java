package com.aadu_maadu_kozhi.gregantech.retrofit;


import com.aadu_maadu_kozhi.gregantech.model.dto.response.BaseResponse;
import com.aadu_maadu_kozhi.gregantech.model.pojo.UserCreatePostFCM;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by anand_android on 9/29/2016.
 */
public interface ApiInterface {
    @POST("/AMMK/user_create_post.php")
    Call<BaseResponse<String>> user_create_post(@Body UserCreatePostFCM data);
}
