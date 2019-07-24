package com.aadu_maadu_kozhi.gregantech.model.pojo;

public class DoctorDetails {
    private String mobile_number;
    private boolean is_doctor=false;
    private boolean is_doctor_approved =false;
    private String doctor_licence_number ;
    private String area ;
    private String doctor_document_url ;

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
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
}
