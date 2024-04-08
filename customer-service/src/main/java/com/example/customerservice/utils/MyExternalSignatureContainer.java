package com.example.customerservice.utils;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.text.pdf.security.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Collection;

public class MyExternalSignatureContainer  implements ExternalSignatureContainer {
    protected Certificate[] chain;
    protected String url;
    protected JSONObject jsonObject;

    public MyExternalSignatureContainer(Certificate[] chain, String url, JSONObject jsonObject) {
        this.chain = chain;
        this.url = url;
        this.jsonObject = jsonObject;
    }

    public byte[] sign(InputStream is) throws GeneralSecurityException {
        try {
            JSONObject x = new JSONObject();
            x.put("sp_id", this.jsonObject.getString("sp_id"));
            x.put("sp_password", this.jsonObject.getString("sp_password"));
            String hashAlgorithm = "SHA256";
            BouncyCastleDigest digest = new BouncyCastleDigest();
            PdfPKCS7 sgn = new PdfPKCS7((PrivateKey)null, this.chain, hashAlgorithm, (String)null, digest, false);
            byte[] hash = DigestAlgorithms.digest(is, digest.getMessageDigest(hashAlgorithm));
            byte[] sh = sgn.getAuthenticatedAttributeBytes(hash, (byte[])null, (Collection)null, MakeSignature.CryptoStandard.CMS);
            MessageDigest digest1 = MessageDigest.getInstance("SHA-256");
            byte[] sh1 = digest1.digest(sh);
            String s = Base64.encodeBytes(sh1);
            JSONObject jsonGetSign = Services.createJsonSignFile(s, this.jsonObject);
            JSONObject recv_signature = Services.getJSON(jsonGetSign, this.url, "/sign");
            String rs = "";
            JSONObject data_sign = recv_signature.getJSONObject("data");
            String tran_id;
            JSONObject jsonGetS;
            JSONObject res;
            JSONObject res_data;
            JSONArray jsonArray;
            JSONObject jo_signature;
            if (this.jsonObject.getString("ca_name").equals("VNPT-CA")) {
                tran_id = data_sign.getString("transaction_id");
                jsonGetS = Services.createJsonGetStatus(this.jsonObject, tran_id);
                res = Services.getJSON(jsonGetS, this.url, "/get_status");
                if (res.getString("message").equals("PENDING")) {
                    while(res.getString("message").equals("PENDING")) {
                        Thread.sleep(5000L);
                        res = Services.getJSON(jsonGetS, this.url, "/get_status");
                    }
                }

                if (res.getString("message").equals("EXPIRED")) {
                    rs = "";
                } else {
                    res_data = res.getJSONObject("data");
                    jsonArray = res_data.getJSONArray("signatures");
                    jo_signature = jsonArray.getJSONObject(0);
                    rs = jo_signature.getString("signature_value");
                }
            } else if (this.jsonObject.getString("ca_name").equals("FPT-CA")) {
                tran_id = data_sign.getString("transaction_id");
                jsonGetS = Services.createJsonGetStatus(this.jsonObject, tran_id);
                res = Services.getJSON(jsonGetS, this.url, "/get_status");
                if (res.getString("message").equals("PENDING")) {
                    while(res.getString("message").equals("PENDING")) {
                        Thread.sleep(5000L);
                        res = Services.getJSON(jsonGetS, this.url, "/get_status");
                    }
                }

                if (res.getString("message").equals("EXPIRED")) {
                    rs = "";
                } else {
                    res_data = res.getJSONObject("data");
                    jsonArray = res_data.getJSONArray("signatures");
                    jo_signature = jsonArray.getJSONObject(0);
                    rs = jo_signature.getString("signature_value");
                }
            } else if (this.jsonObject.getString("ca_name").equals("Viettel-CA")) {
                tran_id = data_sign.getString("transaction_id");
                jsonGetS = Services.createJsonGetStatus(this.jsonObject, tran_id);
                res = Services.getJSON(jsonGetS, this.url, "/get_status");
                if (res.getString("message").equals("Chờ người dùng xác nhận")) {
                    while(res.getString("message").equals("Chờ người dùng xác nhận")) {
                        Thread.sleep(5000L);
                        res = Services.getJSON(jsonGetS, this.url, "/get_status");
                    }
                }

                if (res.getString("message").equals("Quá thời gian chờ xác nhận")) {
                    rs = "";
                } else {
                    res_data = res.getJSONObject("data");
                    jsonArray = res_data.getJSONArray("signed_files");
                    jo_signature = jsonArray.getJSONObject(0);
                    rs = jo_signature.getString("signature_value");
                }
            } else if (this.jsonObject.getString("ca_name").equals("MISA-CA")) {
                tran_id = data_sign.getString("transaction_id");
                jsonGetS = Services.createJsonGetStatus(this.jsonObject, tran_id);
                res = Services.getJSON(jsonGetS, this.url, "/get_status");
                if (res.getString("message").equals("PENDING")) {
                    while(res.getString("message").equals("PENDING")) {
                        Thread.sleep(5000L);
                        res = Services.getJSON(jsonGetS, this.url, "/get_status");
                    }
                }

                if (res.getString("message").equals("FAILED")) {
                    rs = "";
                } else {
                    res_data = res.getJSONObject("data");
                    jsonArray = res_data.getJSONArray("signatures");
                    jo_signature = jsonArray.getJSONObject(0);
                    rs = jo_signature.getString("signature_value");
                }
            } else {
                jsonArray = data_sign.getJSONArray("signatures");
                jsonGetS = jsonArray.getJSONObject(0);
                rs = jsonGetS.getString("signature_value");
            }

            byte[] extSignature = Base64.decode(rs);
            sgn.setExternalDigest(extSignature, (byte[])null, "RSA");
            return sgn.getEncodedPKCS7(hash, (TSAClient)null, (byte[])null, (Collection)null, MakeSignature.CryptoStandard.CMS);
        } catch (InterruptedException | IOException var21) {
            throw new RuntimeException(var21);
        }
    }

    public void modifySigningDictionary(PdfDictionary pdfDictionary) {
    }
}
