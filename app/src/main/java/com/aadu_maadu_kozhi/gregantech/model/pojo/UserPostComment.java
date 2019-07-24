package com.aadu_maadu_kozhi.gregantech.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonIgnore;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

import androidx.room.Ignore;

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class UserPostComment implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserPostComment> CREATOR = new Parcelable.Creator<UserPostComment>() {
        @Override
        public UserPostComment createFromParcel(Parcel in) {
            return new UserPostComment(in);
        }

        @Override
        public UserPostComment[] newArray(int size) {
            return new UserPostComment[size];
        }
    };
    private String id;
    private String commented_user_id;
    private String post_id;
    private String commented_user_profile_url;
    private String commented_user_name;
    private String comments;
    @JsonIgnore
    @ServerTimestamp
    private Timestamp post_commented_date_and_time = null;
    private Date post_commented__local_date_and_time = null;

    protected UserPostComment(Parcel in) {
        id = in.readString();
        commented_user_id = in.readString();
        post_id = in.readString();
        commented_user_profile_url = in.readString();
        commented_user_name = in.readString();
        comments = in.readString();
        post_commented_date_and_time = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
        long tmpPost_commented__local_date_and_time = in.readLong();
        post_commented__local_date_and_time = tmpPost_commented__local_date_and_time != -1 ? new Date(tmpPost_commented__local_date_and_time) : null;
    }

    public UserPostComment() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(commented_user_id);
        dest.writeString(post_id);
        dest.writeString(commented_user_profile_url);
        dest.writeString(commented_user_name);
        dest.writeString(comments);
        dest.writeValue(post_commented_date_and_time);
        dest.writeLong(post_commented__local_date_and_time != null ? post_commented__local_date_and_time.getTime() : -1L);
    }

    public Date getPost_commented__local_date_and_time() {
        return post_commented__local_date_and_time;
    }

    public void setPost_commented__local_date_and_time(Date post_commented__local_date_and_time) {
        this.post_commented__local_date_and_time = post_commented__local_date_and_time;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommented_user_id() {
        return commented_user_id;
    }

    public void setCommented_user_id(String commented_user_id) {
        this.commented_user_id = commented_user_id;
    }

    public String getCommented_user_profile_url() {
        return commented_user_profile_url;
    }

    public void setCommented_user_profile_url(String commented_user_profile_url) {
        this.commented_user_profile_url = commented_user_profile_url;
    }

    public String getCommented_user_name() {
        return commented_user_name;
    }

    public void setCommented_user_name(String commented_user_name) {
        this.commented_user_name = commented_user_name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Timestamp getPost_commented_date_and_time() {
        return post_commented_date_and_time;
    }

    public void setPost_commented_date_and_time(Timestamp post_commented_date_and_time) {
        this.post_commented_date_and_time = post_commented_date_and_time;
    }
}
