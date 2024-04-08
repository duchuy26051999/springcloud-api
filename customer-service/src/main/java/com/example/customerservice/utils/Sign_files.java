package com.example.customerservice.utils;

public class Sign_files {
    private String data_to_be_signed;
    private String doc_id;
    private String file_type;
    private String sign_type;

    public Sign_files() {
    }

    public String getData_to_be_signed() {
        return this.data_to_be_signed;
    }

    public void setData_to_be_signed(String data_to_be_signed) {
        this.data_to_be_signed = data_to_be_signed;
    }

    public String getDoc_id() {
        return this.doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getFile_type() {
        return this.file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getSign_type() {
        return this.sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }
}
