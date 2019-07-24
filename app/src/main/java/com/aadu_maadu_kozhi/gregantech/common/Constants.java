package com.aadu_maadu_kozhi.gregantech.common;



public interface Constants {

    interface Common {
String FROM="from";
int NUMBER_OF_IMAGES_TO_SELECT=5;
    }

    interface InternalHttpCode{
        int SUCCESS_CODE = 200;
        int FAILURE_CODE = 0;
    }
interface AccountKitCode{
        int REQUEST_CODE = 100;
    }

    interface HttpErrorMessage{
        String INTERNAL_SERVER_ERROR = "Our server is under maintenance. We will reslove shortly!";
        String FORBIDDEN = "Seems like you haven't permitted to do this operation!";

    }

    interface NetworkType {
        String WIFI = "Wi-Fi";
        String MOBILE = "Mobile";
    }

    interface BundleKey {
        String IMAGE_PATH="image_path";
        String POST_CONTENT="post_content";
        String AREA_LIST="area_list";
        String THAGAVALKALANCHIYAM_LIST="thagavalkalanchiyam_list";
        String USER_POST_ID="user_post_id";
        String USER="user";
        String USER_POST="user_post";
        String FIRST_USER_ID="first_user_id";
        String SECOND_USER_ID="second_user_id";
        String THAGAVALKALANCHIYAM_DATA="thagavalkalanchiyam_data";
        String SUCCESS_STORY_DATA="success_story_data";
        String ACCESSORIES_DATA="accessories_data";
        String ForDoctorLogin="ForDoctorLogin";
        String ForFarmOwnersLogin="ForFarmOwnersLogin";
        String ForSellersLogin="ForSellersLogin";
        String UserPostComment="UserPostComment";
        String UserPostLike="UserPostLike";
        String UserChat="UserChat";
        String UserPostCreate="UserPostCreate";
        String User="User";
        String For="For";

    }

    interface RequestCodes {
        int KEY_REQUEST_CODE_COMPOSE_MAIL = 101;
        int REQUEST_CODE_PIC_IMAGE = 2001;
        int REQUEST_CODE_CREATE_POST = 3001;

    }

    interface BroadCastKey {

    }

    interface SharedPrefKey {

    }

    interface ApiRequestKey{

    }
    interface NotificationKey{
        String UserPostComment="UserPostComment";
        String UserChat="UserChat";
        String UserPost="UserPost";
        String UserName="UserName";
        String User="User";
        String UserPostLike="UserPostLike";
        String UserPostCreate="UserPostCreate";
        String LikedUserName="LikedUserName";
        String For="for";

    }

}
