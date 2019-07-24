package com.aadu_maadu_kozhi.gregantech.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonIgnore;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.List;

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class UserPost implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserPost> CREATOR = new Parcelable.Creator<UserPost>() {
        @Override
        public UserPost createFromParcel(Parcel in) {
            return new UserPost(in);
        }

        @Override
        public UserPost[] newArray(int size) {
            return new UserPost[size];
        }
    };
    private String id;
    private String post_content;
    private String user_id;
    private String area_name;
    private String animal_type;
    private List<String> post_image_path;
    private List<String> post_liked_list = new ArrayList<>();
    @JsonIgnore
    @ServerTimestamp
    private Timestamp post_created_date_and_time = null;

    protected UserPost(Parcel in) {
        id = in.readString();
        post_content = in.readString();
        user_id = in.readString();
        area_name = in.readString();
        animal_type = in.readString();
        if (in.readByte() == 0x01) {
            post_image_path = new ArrayList<String>();
            in.readList(post_image_path, String.class.getClassLoader());
        } else {
            post_image_path = null;
        }
        if (in.readByte() == 0x01) {
            post_liked_list = new ArrayList<String>();
            in.readList(post_liked_list, String.class.getClassLoader());
        } else {
            post_liked_list = null;
        }
        post_created_date_and_time = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
    }

    public UserPost() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(post_content);
        dest.writeString(user_id);
        dest.writeString(area_name);
        dest.writeString(animal_type);
        if (post_image_path == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(post_image_path);
        }
        if (post_liked_list == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(post_liked_list);
        }
        dest.writeValue(post_created_date_and_time);
    }

    public String getAnimal_type() {
        return animal_type;
    }

    public void setAnimal_type(String animal_type) {
        this.animal_type = animal_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public List<String> getPost_image_path() {
        return post_image_path;
    }

    public void setPost_image_path(List<String> post_image_path) {
        this.post_image_path = post_image_path;
    }

    public Timestamp getPost_created_date_and_time() {
        return post_created_date_and_time;
    }

    public void setPost_created_date_and_time(Timestamp post_created_date_and_time) {
        this.post_created_date_and_time = post_created_date_and_time;
    }

    public List<String> getPost_liked_list() {
        return post_liked_list;
    }

    public void setPost_liked_list(List<String> post_liked_list) {
        this.post_liked_list = post_liked_list;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }
}
