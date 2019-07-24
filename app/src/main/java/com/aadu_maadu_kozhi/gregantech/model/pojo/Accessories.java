package com.aadu_maadu_kozhi.gregantech.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.List;

public class Accessories implements Parcelable {
    @SuppressWarnings("unused")
    public static final Creator<Accessories> CREATOR = new Creator<Accessories>() {
        @Override
        public Accessories createFromParcel(Parcel in) {
            return new Accessories(in);
        }

        @Override
        public Accessories[] newArray(int size) {
            return new Accessories[size];
        }
    };
    private String imageUrl;
    private String title;
    private String content;
    private String whoCreated;
    @ServerTimestamp
    private Timestamp accessories_post_created_date_and_time = null;
    private String id;
    private List<String> post_liked_list = new ArrayList<>();

    protected Accessories(Parcel in) {
        imageUrl = in.readString();
        title = in.readString();
        content = in.readString();
        whoCreated = in.readString();
        accessories_post_created_date_and_time = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
        id = in.readString();
        if (in.readByte() == 0x01) {
            post_liked_list = new ArrayList<String>();
            in.readList(post_liked_list, String.class.getClassLoader());
        } else {
            post_liked_list = null;
        }
    }

    public Accessories() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(whoCreated);
        dest.writeValue(accessories_post_created_date_and_time);
        dest.writeString(id);
        if (post_liked_list == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(post_liked_list);
        }
    }

    public List<String> getPost_liked_list() {
        return post_liked_list;
    }

    public void setPost_liked_list(List<String> post_liked_list) {
        this.post_liked_list = post_liked_list;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWhoCreated() {
        return whoCreated;
    }

    public void setWhoCreated(String whoCreated) {
        this.whoCreated = whoCreated;
    }

    public Timestamp getAccessories_post_created_date_and_time() {
        return accessories_post_created_date_and_time;
    }

    public void setAccessories_post_created_date_and_time(Timestamp accessories_post_created_date_and_time) {
        this.accessories_post_created_date_and_time = accessories_post_created_date_and_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}