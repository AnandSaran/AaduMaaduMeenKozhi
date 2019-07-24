package com.aadu_maadu_kozhi.gregantech.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class ThagavalKalanchiyam implements  Parcelable {
    private String imageUrl;
    private String title;
    private String content;
    private String whoCreated;
    @ServerTimestamp
    private Timestamp thagaval_kalanchiyam_post_created_date_and_time = null;
    private String id;
    private boolean selected;

    protected ThagavalKalanchiyam(Parcel in) {
        imageUrl = in.readString();
        title = in.readString();
        content = in.readString();
        whoCreated = in.readString();
        thagaval_kalanchiyam_post_created_date_and_time = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
        id = in.readString();
        selected = in.readByte() != 0x00;
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
        dest.writeValue(thagaval_kalanchiyam_post_created_date_and_time);
        dest.writeString(id);
        dest.writeByte((byte) (selected ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ThagavalKalanchiyam> CREATOR = new Parcelable.Creator<ThagavalKalanchiyam>() {
        @Override
        public ThagavalKalanchiyam createFromParcel(Parcel in) {
            return new ThagavalKalanchiyam(in);
        }

        @Override
        public ThagavalKalanchiyam[] newArray(int size) {
            return new ThagavalKalanchiyam[size];
        }
    };
    public String getId() {
        return id;
    }

    public ThagavalKalanchiyam() {
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setId(String id) {
        this.id = id;
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

    public Timestamp getThagaval_kalanchiyam_post_created_date_and_time() {
        return thagaval_kalanchiyam_post_created_date_and_time;
    }

    public void setThagaval_kalanchiyam_post_created_date_and_time(Timestamp thagaval_kalanchiyam_post_created_date_and_time) {
        this.thagaval_kalanchiyam_post_created_date_and_time = thagaval_kalanchiyam_post_created_date_and_time;
    }
}
