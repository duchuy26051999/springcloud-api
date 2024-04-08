//package com.example.customerservice.utils;
//
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.pdf.codec.Base64;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.security.*;
//import java.security.cert.Certificate;
//import java.security.cert.CertificateException;
//import java.security.cert.CertificateFactory;
//import java.security.cert.X509Certificate;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class KySoNEAC {
//    @Value("${sign.spId}")
//    private String spId;
//
//    @Value("${sign.spPassword}")
//    private String spPassword;
//
//    private final ChuKySoRepository chuKySoRepository;
//
//    private final ConfigUrlChuKySoRepository configUrlChuKySoRepository;
//
//    public byte[] KySo(JSONObject cert, InputStream fileIn, String currentName, int index, String chucDanh) throws JSONException, GeneralSecurityException, IOException, DocumentException {
//        ChuKySo chuKySo = chuKySoRepository.getChuKySoByUsername(currentName);
//        if (chuKySo == null) {
//            throw new NullPointerException();
//        }
//        String stringgetcert = "{\n" + "\t\"sp_id\": \"" + spId + "\",\n" + "\t\"sp_password\": \"" + spPassword + "\",\n" + "\t\"user_id\": \"" + chuKySo.getUserId() + "\",\n" + "\t\"ca_name\": \"" + chuKySo.getCaName() + "\",\n" + "\t\"serial_number\": \"" + cert.getJSONObject("data").getJSONArray("user_certificates").getJSONObject(0).getString("serial_number") + "\"\n" + "}";
//        JSONObject jsongetCert = new JSONObject(stringgetcert);
//        String certdata = cert.getJSONObject("data").getJSONArray("user_certificates").getJSONObject(0).getString("cert_data");
//        Certificate[] certificate = certb64toCertificate(certdata);
//        String name = ((X509Certificate) certificate[0]).getSubjectDN().getName();
//        Map<String, String> dic = new HashMap<String, String>();
//        dic.put("chucdanh", chucDanh);
//        dic.put("name", name);
//
//        dic.put("noidung", "Nhật ký thi công");
//
////        dic.put("image", "C:\\Users\\huynd.UNITEK\\Downloads\\Capture.PNG");
//        int[] arrToaDo = arr(index);
//        byte[] dataky = NEAC.sign_no_image(jsongetCert, fileIn, String.valueOf(System.currentTimeMillis()), certificate, "https://esign.neac.gov.vn/sign_v2", arrToaDo[0], arrToaDo[1], arrToaDo[2], arrToaDo[3], 1, dic);
//
//        fileIn.close();
//        return dataky;
//    }
//
//    private int[] arr(int index) {
//        int width = 115;
//        if (index <= 5) {
//            int n = index - 1;
//            return new int[]{n * width, 1000, (n + 1) * width, 480};
//        }
//        int n = index - 6;
//        return new int[]{n * width, 1000, (n + 1) * width, 210};
//    }
//
//
//    public Certificate[] certb64toCertificate(String cert) throws CertificateException {
//        cert = cert.replace("\n", "");
//        cert = cert.replace("\r", "");
//        byte encodedCert[] = Base64.decode(cert);
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedCert);
//        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
//        Certificate certificate = certFactory.generateCertificate(inputStream);
//        Certificate[] chain = new Certificate[1];
//        chain[0] = certificate;
//        return chain;
//    }
//
//    public JSONObject LayChuKy(String name) throws JSONException, UnrecoverableKeyException, KeyManagementException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
//        List<ConfigUrlChuKySo> list = configUrlChuKySoRepository.findAll();
//        if (list.isEmpty()) {
//            throw new NullPointerException();
//        }
//        ChuKySo chuKySo = chuKySoRepository.getChuKySoByUsername(name);
//        if (chuKySo == null) {
//            return null;
//        }
//        ConfigUrlChuKySo configUrlChuKySo = list.get(0);
//        String stringgetcert = "{\n" + "\t\"sp_id\": \"" + spId + "\",\n" + "\t\"sp_password\": \"" + spPassword + "\",\n" + "\t\"user_id\": \"" + chuKySo.getUserId() + "\",\n" + "\t\"ca_name\": \"" + chuKySo.getCaName() + "\",\n" + "\t\"serial_number\": \"\"\n" + "}";
//        JSONObject jsongetCert = new JSONObject(stringgetcert);
//        return NEAC.getCert(jsongetCert, configUrlChuKySo.getUrl(), configUrlChuKySo.getPart());
//    }
//
//    public JSONObject layChuKy(String userId, String password) throws JSONException, UnrecoverableKeyException, KeyManagementException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
//        String stringgetcert = "{\n" + "\t\"sp_id\": \"" + spId + "\",\n" + "\t\"sp_password\": \"" + spPassword + "\",\n" + "\t\"user_id\": \"" + userId + "\",\n" + "\t\"ca_name\": \"" + password + "\",\n" + "\t\"serial_number\": \"\"\n" + "}";
//        JSONObject jsongetCert = new JSONObject(stringgetcert);
//        List<ConfigUrlChuKySo> list = configUrlChuKySoRepository.findAll();
//        if (list.isEmpty()) {
//            throw new NullPointerException();
//        }
//        ConfigUrlChuKySo configUrlChuKySo = list.get(0);
//        return NEAC.getCert(jsongetCert, configUrlChuKySo.getUrl(), configUrlChuKySo.getPart());
//    }
//
//    public JSONObject layChuKy(String url, String part, String userId, String password) throws JSONException, UnrecoverableKeyException, KeyManagementException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
//        String stringgetcert = "{\n" + "\t\"sp_id\": \"" + spId + "\",\n" + "\t\"sp_password\": \"" + spPassword + "\",\n" + "\t\"user_id\": \"" + userId + "\",\n" + "\t\"ca_name\": \"" + password + "\",\n" + "\t\"serial_number\": \"\"\n" + "}";
//        JSONObject jsongetCert = new JSONObject(stringgetcert);
//        return NEAC.getCert(jsongetCert, url, part);
//    }
//}
