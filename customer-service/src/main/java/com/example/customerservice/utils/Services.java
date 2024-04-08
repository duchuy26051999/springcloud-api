package com.example.customerservice.utils;

import com.itextpdf.text.pdf.codec.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Services {
    public static TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }};

    public Services() {
    }

    public static String getcertJSON(JSONObject jsonObject) {
        JSONObject data = jsonObject.getJSONObject("data");
        String cert_data = "";
        JSONArray list_user_certificates = data.getJSONArray("user_certificates");

        for(int i = 0; i < list_user_certificates.length(); ++i) {
            JSONObject certificates = list_user_certificates.getJSONObject(i);
            cert_data = (String)certificates.get("cert_data");
        }

        return cert_data;
    }

    public static JSONObject getJSON(JSONObject jsonObject, String url, String part) throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        SSLContext sslcontext = SSLContext.getInstance("TLSv1.2");
        sslcontext.init((KeyManager[])null, trustAllCerts, new SecureRandom());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        CloseableHttpClient httpClient = HttpClients.custom().setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).setSSLSocketFactory(sslsf).build();
        HttpPost request = new HttpPost(url + part);
        StringEntity params = new StringEntity(jsonObject.toString());
        request.addHeader("content-type", "application/json");
        request.setEntity(params);
        HttpResponse response = httpClient.execute(request);
        String s = EntityUtils.toString(response.getEntity());
        JSONObject rs = new JSONObject(s);
        return rs;
    }

    public static java.security.cert.Certificate[] certb64toCertificate(String cert) throws CertificateException {
        byte[] encodedCert = Base64.decode(cert);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedCert);
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        java.security.cert.Certificate certificate = certFactory.generateCertificate(inputStream);
        java.security.cert.Certificate[] chain = new Certificate[]{certificate};
        return chain;
    }

    public static JSONObject createJsonSignFile(String hash, JSONObject jsonObject) {
        Random random = new Random();
        int i = random.nextInt();
        String s = "" + i;
        Sign_file sign_file = new Sign_file();
        Sign_files sign_files = new Sign_files();
        List<Sign_files> sign_filesList = new ArrayList();
        sign_file.setSp_id(jsonObject.getString("sp_id"));
        sign_file.setSp_password(jsonObject.getString("sp_password"));
        sign_file.setUser_id(jsonObject.getString("user_id"));
        sign_file.setCa_name(jsonObject.getString("ca_name"));
        sign_file.setSign_files(sign_filesList);
        sign_file.setSerial_number(jsonObject.getString("serial_number"));
        sign_file.setTime_stamp("");
        sign_files.setSign_type("hash");
        sign_files.setFile_type("pdf");
        sign_files.setDoc_id(s);
        sign_files.setData_to_be_signed(hash);
        sign_filesList.add(sign_files);
        sign_file.setSign_files(sign_filesList);
        JSONObject jsO = new JSONObject(sign_file);
        return jsO;
    }

    public static List<String> getListCert(JSONObject jsonObject) {
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray list_user_certificates = data.getJSONArray("user_certificates");
        List<String> listSeri = new ArrayList();

        for(int i = 0; i < list_user_certificates.length(); ++i) {
            JSONObject certificates = list_user_certificates.getJSONObject(i);
            String serialnumber = certificates.get("serial_number").toString();
            listSeri.add(serialnumber);
        }

        return listSeri;
    }

    public static JSONObject createJsonGetStatus(JSONObject jsonObject, String tran_id) {
        JSONObject rs = new JSONObject();
        rs.put("sp_id", jsonObject.getString("sp_id"));
        rs.put("sp_password", jsonObject.getString("sp_password"));
        rs.put("user_id", jsonObject.getString("user_id"));
        rs.put("ca_name", jsonObject.getString("ca_name"));
        rs.put("transaction_id", tran_id);
        return rs;
    }
}
