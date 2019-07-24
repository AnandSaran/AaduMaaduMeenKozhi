package com.aadu_maadu_kozhi.gregantech.model.dto.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class BaseResponse<T> implements Serializable {
    private T data;
    private Boolean is_profile_created;

    private String app_version;

    private String user_id;
    private String message;
    private Boolean success;
    private String image_url;
    private String name;
    private String native_place;
    private String blood_group;

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNative_place() {
        return native_place;
    }

    public void setNative_place(String native_place) {
        this.native_place = native_place;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Boolean getIs_profile_created() {
        return is_profile_created;
    }

    public void setIs_profile_created(Boolean is_profile_created) {
        this.is_profile_created = is_profile_created;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "ClassPojo [is_profile_created = " + is_profile_created + ", app_version = " + app_version + ", user_id = " + user_id + "]";
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
