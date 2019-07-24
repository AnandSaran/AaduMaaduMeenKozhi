package com.aadu_maadu_kozhi.gregantech.model.pojo;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class UserCreatePostFCM {
private String UserPost;
private String UserName;
private String forr;
private List<String> tokens;

    public String getUserPost() {
        return UserPost;
    }

    public void setUserPost(String userPost) {
        UserPost = userPost;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getForr() {
        return forr;
    }

    public void setForr(String forr) {
        this.forr = forr;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }
}
