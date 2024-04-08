package com.example.customerservice.utils;

import java.io.Serializable;
import java.util.List;

public class Sign_file implements Serializable {
    private String sp_id;
    private String sp_password;
    private String user_id;
    private String ca_name;
    private List<Sign_files> sign_files;
    private String transaction_id;
    private String serial_number;
    private String time_stamp;

    public Sign_file() {
    }

    public String getSp_id() {
        return this.sp_id;
    }

    public void setSp_id(String sp_id) {
        this.sp_id = sp_id;
    }

    public String getSp_password() {
        return this.sp_password;
    }

    public void setSp_password(String sp_password) {
        this.sp_password = sp_password;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCa_name() {
        return this.ca_name;
    }

    public void setCa_name(String ca_name) {
        this.ca_name = ca_name;
    }

    public List<Sign_files> getSign_files() {
        return this.sign_files;
    }

    public void setSign_files(List<Sign_files> sign_files) {
        this.sign_files = sign_files;
    }

    public String getTransaction_id() {
        return this.transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getSerial_number() {
        return this.serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getTime_stamp() {
        return this.time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }
}
