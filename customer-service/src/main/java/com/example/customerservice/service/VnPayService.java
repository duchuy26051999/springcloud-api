package com.example.customerservice.service;

import com.example.customerservice.component.VNPayConfig;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VnPayService {
    private static final String VND = "VND";

    public String vnpay(HttpServletRequest req) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo = "";
        String orderType = "Phần mềm";
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(req);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        int amount = 100;
        Map<String, Object> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", VND);
        String bank_code = req.getParameter("bankcode");
        if (bank_code != null && !bank_code.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bank_code);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = req.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        //            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        LocalDateTime createDate = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(createDate);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        //            cld.add(Calendar.MINUTE, 15);
        LocalDateTime expireDate = createDate.plusMinutes(15);
        String vnp_ExpireDate = formatter.format(expireDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        //Add Params of 2.1.0 Version
        vnp_Params.put("vnp_Inv_Phone", req.getParameter("txt_inv_mobile"));
        vnp_Params.put("vnp_Inv_Email", req.getParameter("txt_inv_email"));
        vnp_Params.put("vnp_Inv_Customer", req.getParameter("txt_inv_customer"));
        vnp_Params.put("vnp_Inv_Address", req.getParameter("txt_inv_addr1"));
        vnp_Params.put("vnp_Inv_Company", req.getParameter("txt_inv_company"));
        vnp_Params.put("vnp_Inv_Taxcode", req.getParameter("txt_inv_taxcode"));
        vnp_Params.put("vnp_Inv_Type", req.getParameter("cbo_inv_type"));
        //Build data to hash and querystring
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return VNPayConfig.vnp_PayUrl + "?" + queryUrl;
    }

    private Map<String, String> getVnpSecureHash(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName;
            String fieldValue;
            fieldName = URLEncoder.encode(params.nextElement(), StandardCharsets.US_ASCII);
            fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                fields.put(fieldName, fieldValue);
            }
        }
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");
        return fields;
    }

    public String paymentVnPay(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        int returnLogin = 0; // trả về trang login
        int waring = 2; // cảnh báo thanh toán lỗi
        int fail = 1; // thất bại
        int success = 3; // thành công
        try {
            if (getVnpSecureHash(request).isEmpty()) {
                return "false";
            }
            String vnpSecureHash = request.getParameter("vnp_SecureHash");
            String vnpTxnRef = request.getParameter("vnp_TxnRef");
            String signValue = VNPayConfig.hashAllFields(getVnpSecureHash(request));
            String vnpBankCode = request.getParameter("vnp_BankCode");
            String vnpCardType = request.getParameter("vnp_CardType");
            String vnpResponseCode = request.getParameter("vnp_ResponseCode");
            if (!signValue.equals(vnpSecureHash)) {
                return "Chữ ký không hợp lệ";
            }
            if (vnpResponseCode != null && vnpResponseCode.equals("24")) {
                return "Giao dịch đã bị hủy!";
            }
            if (!"00".equals(vnpResponseCode)) {
                return "Thanh toán thất bại";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
