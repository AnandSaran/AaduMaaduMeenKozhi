package com.aadu_maadu_kozhi.gregantech.model.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.type.LatLng;

public class Area implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Area> CREATOR = new Parcelable.Creator<Area>() {
        @Override
        public Area createFromParcel(Parcel in) {
            return new Area(in);
        }

        @Override
        public Area[] newArray(int size) {
            return new Area[size];
        }
    };
    private String area_name;
    private LatLng latLng;
    @ServerTimestamp
    private Timestamp created_date_and_time = null;

    protected Area(Parcel in) {
        area_name = in.readString();
        latLng = (LatLng) in.readValue(LatLng.class.getClassLoader());
        created_date_and_time = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
    }

    public Area(String area_name) {
        this.area_name = area_name;
    }

    public Area() {
    }

    public Area(String area_name, LatLng latLng) {
        this.area_name = area_name;
        this.latLng = latLng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(area_name);
        dest.writeValue(latLng);
        dest.writeValue(created_date_and_time);
    }

    public Timestamp getCreated_date_and_time() {
        return created_date_and_time;
    }

    public void setCreated_date_and_time(Timestamp created_date_and_time) {
        this.created_date_and_time = created_date_and_time;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }


    @Override
    public String toString() {
        return area_name;
    }
}