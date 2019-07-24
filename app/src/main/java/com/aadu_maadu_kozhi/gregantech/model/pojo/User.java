package com.aadu_maadu_kozhi.gregantech.model.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonIgnore;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

/**
 * Created by Anand on 8/28/2017.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class User implements Parcelable {

    private String name;
    private String mobile_number;
    private String image_url;
    private String email;
    private String fb_id;
    private String gmail_id;
    private boolean is_online = false;
    private boolean is_doctor = false;
    private boolean is_doctor_approved = false;
    private String doctor_licence_number;
    private String area;
    private String doctor_document_url;
    private String selling_animal_type;
    private String farm_animal_type;
    private boolean is_farm_owner;
    private boolean is_farm_owner_approved;
    private boolean is_seller;
    private boolean is_seller_approved;
    private boolean is_accessories_approved;
    private String user_id;
    private String token;
    @JsonIgnore
    @ServerTimestamp
    private Timestamp profile_created_date_and_time = null;

    protected User(Parcel in) {
        name = in.readString();
        mobile_number = in.readString();
        image_url = in.readString();
        email = in.readString();
        fb_id = in.readString();
        gmail_id = in.readString();
        is_online = in.readByte() != 0x00;
        is_doctor = in.readByte() != 0x00;
        is_doctor_approved = in.readByte() != 0x00;
        doctor_licence_number = in.readString();
        area = in.readString();
        doctor_document_url = in.readString();
        selling_animal_type = in.readString();
        farm_animal_type = in.readString();
        is_farm_owner = in.readByte() != 0x00;
        is_farm_owner_approved = in.readByte() != 0x00;
        is_seller = in.readByte() != 0x00;
        is_seller_approved = in.readByte() != 0x00;
        is_accessories_approved = in.readByte() != 0x00;
        user_id = in.readString();
        token = in.readString();
        profile_created_date_and_time = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(mobile_number);
        dest.writeString(image_url);
        dest.writeString(email);
        dest.writeString(fb_id);
        dest.writeString(gmail_id);
        dest.writeByte((byte) (is_online ? 0x01 : 0x00));
        dest.writeByte((byte) (is_doctor ? 0x01 : 0x00));
        dest.writeByte((byte) (is_doctor_approved ? 0x01 : 0x00));
        dest.writeString(doctor_licence_number);
        dest.writeString(area);
        dest.writeString(doctor_document_url);
        dest.writeString(selling_animal_type);
        dest.writeString(farm_animal_type);
        dest.writeByte((byte) (is_farm_owner ? 0x01 : 0x00));
        dest.writeByte((byte) (is_farm_owner_approved ? 0x01 : 0x00));
        dest.writeByte((byte) (is_seller ? 0x01 : 0x00));
        dest.writeByte((byte) (is_seller_approved ? 0x01 : 0x00));
        dest.writeByte((byte) (is_accessories_approved ? 0x01 : 0x00));
        dest.writeString(user_id);
        dest.writeString(token);
        dest.writeValue(profile_created_date_and_time);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User() {
    }

    public boolean isIs_accessories_approved() {
        return is_accessories_approved;
    }

    public void setIs_accessories_approved(boolean is_accessories_approved) {
        this.is_accessories_approved = is_accessories_approved;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSelling_animal_type() {
        return selling_animal_type;
    }

    public void setSelling_animal_type(String selling_animal_type) {
        this.selling_animal_type = selling_animal_type;
    }

    public String getFarm_animal_type() {
        return farm_animal_type;
    }

    public void setFarm_animal_type(String farm_animal_type) {
        this.farm_animal_type = farm_animal_type;
    }

    public boolean isIs_farm_owner() {
        return is_farm_owner;
    }

    public void setIs_farm_owner(boolean is_farm_owner) {
        this.is_farm_owner = is_farm_owner;
    }

    public boolean isIs_farm_owner_approved() {
        return is_farm_owner_approved;
    }

    public void setIs_farm_owner_approved(boolean is_farm_owner_approved) {
        this.is_farm_owner_approved = is_farm_owner_approved;
    }

    public boolean isIs_seller() {
        return is_seller;
    }

    public void setIs_seller(boolean is_seller) {
        this.is_seller = is_seller;
    }

    public boolean isIs_seller_approved() {
        return is_seller_approved;
    }

    public void setIs_seller_approved(boolean is_seller_approved) {
        this.is_seller_approved = is_seller_approved;
    }

    public boolean isIs_doctor() {
        return is_doctor;
    }

    public void setIs_doctor(boolean is_doctor) {
        this.is_doctor = is_doctor;
    }

    public boolean isIs_doctor_approved() {
        return is_doctor_approved;
    }

    public void setIs_doctor_approved(boolean is_doctor_approved) {
        this.is_doctor_approved = is_doctor_approved;
    }

    public String getDoctor_licence_number() {
        return doctor_licence_number;
    }

    public void setDoctor_licence_number(String doctor_licence_number) {
        this.doctor_licence_number = doctor_licence_number;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDoctor_document_url() {
        return doctor_document_url;
    }

    public void setDoctor_document_url(String doctor_document_url) {
        this.doctor_document_url = doctor_document_url;
    }

    public boolean isIs_online() {
        return is_online;
    }

    public void setIs_online(boolean is_online) {
        this.is_online = is_online;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getGmail_id() {
        return gmail_id;
    }

    public void setGmail_id(String gmail_id) {
        this.gmail_id = gmail_id;
    }

    public Timestamp getProfile_created_date_and_time() {
        return profile_created_date_and_time;
    }

    public void setProfile_created_date_and_time(Timestamp profile_created_date_and_time) {
        this.profile_created_date_and_time = profile_created_date_and_time;
    }
}
