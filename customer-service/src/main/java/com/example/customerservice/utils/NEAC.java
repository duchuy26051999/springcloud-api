package com.example.customerservice.utils;

import com.itextpdf.text.DocumentException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Map;

public class NEAC {
    private Certificate[] chain;

    public NEAC(Certificate[] chain) {
        this.chain = chain;
    }

    public static JSONObject getCert(JSONObject jsonObject, String url, String part) throws UnrecoverableKeyException, CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return Services.getJSON(jsonObject, url, part);
    }

    public static void sign(JSONObject jsonObject, String pdf, String dest, String fieldName, Certificate[] chain, String url, int x, int y, int width, int height, int pagenumber, Map<String, String> dictionary) throws GeneralSecurityException, IOException, DocumentException {
        CreateSign createSign = new CreateSign(chain);
        byte[] pdf_empty = createSign.emptySignature(pdf, fieldName, chain, x, y, width, height, pagenumber, dictionary);
        InputStream inputStream = new ByteArrayInputStream(pdf_empty);
        createSign.createSignature(jsonObject, inputStream, dest, fieldName, chain, url);
    }

    public static byte[] sign(JSONObject jsonObject, InputStream pdf, String fieldName, Certificate[] chain, String url, int x, int y, int width, int height, int pagenumber, Map<String, String> dictionary) throws GeneralSecurityException, IOException, DocumentException {
        CreateSign createSign = new CreateSign(chain);
        byte[] pdf_empty = createSign.emptySignature_byte(pdf, fieldName, chain, x, y, width, height, pagenumber, dictionary);
        InputStream inputStream = new ByteArrayInputStream(pdf_empty);
        return createSign.createSignature_byte(jsonObject, inputStream, fieldName, chain, url);
    }

    public static byte[] sign_no_image(JSONObject jsonObject, InputStream pdf, String fieldName, Certificate[] chain, String url, int x, int y, int width, int height, int pagenumber, Map<String, String> dictionary) throws GeneralSecurityException, IOException, DocumentException {
        CreateSign createSign = new CreateSign(chain);
        byte[] pdf_empty = createSign.emptySignature_no_image(pdf, fieldName, chain, x, y, width, height, pagenumber, dictionary);
        InputStream inputStream = new ByteArrayInputStream(pdf_empty);
        return createSign.createSignature_byte(jsonObject, inputStream, fieldName, chain, url); // bật xác nhận ký
//        return IOUtils.toByteArray(inputStream);
    }

    public static byte[] sign_image(JSONObject jsonObject, InputStream pdf, String fieldName, Certificate[] chain, String url, int x, int y, int width, int height, int pagenumber, String image) throws GeneralSecurityException, IOException, DocumentException {
        CreateSign createSign = new CreateSign(chain);
        byte[] pdf_empty = createSign.emptySignature_image(pdf, fieldName, chain, x, y, width, height, pagenumber, image);
        InputStream inputStream = new ByteArrayInputStream(pdf_empty);
        return createSign.createSignature_byte(jsonObject, inputStream, fieldName, chain, url);
    }
}
