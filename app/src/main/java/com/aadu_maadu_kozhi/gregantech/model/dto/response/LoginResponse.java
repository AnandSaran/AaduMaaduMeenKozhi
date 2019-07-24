package com.aadu_maadu_kozhi.gregantech.model.dto.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Anand on 8/28/2017.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)

public class LoginResponse  {
    private Boolean is_profile_created;

    private String app_version;

    private String user_id;



    public Boolean getIs_profile_created ()
    {
        return is_profile_created;
    }

    public void setIs_profile_created (Boolean is_profile_created)
    {
        this.is_profile_created = is_profile_created;
    }

    public String getApp_version ()
    {
        return app_version;
    }

    public void setApp_version (String app_version)
    {
        this.app_version = app_version;
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [is_profile_created = "+is_profile_created+", app_version = "+app_version+", user_id = "+user_id+"]";
    }
}
