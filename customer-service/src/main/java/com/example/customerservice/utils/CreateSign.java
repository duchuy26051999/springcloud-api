package com.example.customerservice.utils;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;
import com.itextpdf.text.pdf.security.ExternalBlankSignatureContainer;
import com.itextpdf.text.pdf.security.ExternalSignatureContainer;
import com.itextpdf.text.pdf.security.MakeSignature;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

public class CreateSign {
    private Certificate[] chain;
    private String fontPath = "templates/times.ttf";

    public CreateSign(Certificate[] chain) {
        this.chain = chain;
    }

    public byte[] emptySignature(String pdf, String fieldname, Certificate[] chain, int x, int y, int width, int height, int pagenumber, Map<String, String> dictionary) throws IOException, GeneralSecurityException, DocumentException {
        BaseFont bf = BaseFont.createFont(fontPath, "Identity-H", true);
        Font font = new Font(bf);
        font.setSize(6.0F);
        PdfReader reader = new PdfReader(pdf);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper stamper = PdfStamper.createSignature(reader, baos, '\u0000', null, true);
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(12, 0);
        String tt = (new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")).format(Calendar.getInstance().getTime());
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setVisibleSignature(new Rectangle((float) x, (float) y, (float) height, (float) width), pagenumber, fieldname);
        appearance.setLayer2Text("Người ký: " + dictionary.get("name") + "\n\nNgày ký: " + tt + "\n\nChức danh: " + dictionary.get("chucdanh") + "\n\nĐơn vị: " + dictionary.get("donvi") + "\n\nNội dung: " + dictionary.get("noidung"));
        appearance.setLayer2Font(font);
        appearance.setCertificate(chain[0]);
        appearance.setRenderingMode(RenderingMode.GRAPHIC_AND_DESCRIPTION);
        appearance.setSignatureGraphic(Image.getInstance(dictionary.get("image")));
        appearance.setImageScale(-1.0F);
        ExternalSignatureContainer external = new ExternalBlankSignatureContainer(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
        MakeSignature.signExternalContainer(appearance, external, 8192);
        return baos.toByteArray();
    }

    public void createSignature(JSONObject jsonCert, InputStream pdf, String dest, String fieldName, Certificate[] chain, String url) throws IOException, GeneralSecurityException {
        PdfReader reader = new PdfReader(pdf);

        try {
            FileOutputStream os = new FileOutputStream(dest);
            Throwable var9 = null;

            try {
                ExternalSignatureContainer external = new MyExternalSignatureContainer(chain, url, jsonCert);
                MakeSignature.signDeferred(reader, fieldName, os, external);
            } catch (Throwable var19) {
                var9 = var19;
                throw var19;
            } finally {
                if (os != null) {
                    if (var9 != null) {
                        try {
                            os.close();
                        } catch (Throwable var18) {
                            var9.addSuppressed(var18);
                        }
                    } else {
                        os.close();
                    }
                }

            }

        } catch (DocumentException var21) {
            throw new RuntimeException(var21);
        }
    }

    public byte[] emptySignature_byte(InputStream pdf, String fieldname, Certificate[] chain, int x, int y, int width, int height, int pagenumber, Map<String, String> dictionary) throws IOException, GeneralSecurityException, DocumentException {
        BaseFont bf = BaseFont.createFont(fontPath, "Identity-H", true);
        Font font = new Font(bf);
        font.setSize(6.0F);
        PdfReader reader = new PdfReader(pdf);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper stamper = PdfStamper.createSignature(reader, baos, '\u0000', null, true);
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(12, 0);
        String tt = (new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")).format(Calendar.getInstance().getTime());
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setVisibleSignature(new Rectangle((float) x, (float) y, (float) height, (float) width), pagenumber, fieldname);
        appearance.setLayer2Text("Người ký: " + dictionary.get("name") + "\n\nNgày ký: " + tt + "\n\nChức danh: " + dictionary.get("chucdanh") + "\n\nĐơn vị: " + dictionary.get("donvi") + "\n\nNội dung: " + dictionary.get("noidung"));
        appearance.setLayer2Font(font);
        appearance.setCertificate(chain[0]);
        appearance.setRenderingMode(RenderingMode.GRAPHIC_AND_DESCRIPTION);
        appearance.setSignatureGraphic(Image.getInstance(dictionary.get("image")));
        appearance.setImageScale(-1.0F);
        ExternalSignatureContainer external = new ExternalBlankSignatureContainer(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
        MakeSignature.signExternalContainer(appearance, external, 8192);
        return baos.toByteArray();
    }

    public byte[] createSignature_byte(JSONObject jsonCert, InputStream pdf, String fieldName, Certificate[] chain, String url) throws IOException, GeneralSecurityException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfReader reader = new PdfReader(pdf);
        ExternalSignatureContainer external = new MyExternalSignatureContainer(chain, url, jsonCert);
        MakeSignature.signDeferred(reader, fieldName, baos, external);
        return baos.toByteArray();
    }

    public byte[] emptySignature_no_image(InputStream pdf, String fieldname, Certificate[] chain, int x, int y, int width, int height, int pagenumber, Map<String, String> dictionary) throws IOException, GeneralSecurityException, DocumentException {
        BaseFont bf = BaseFont.createFont(fontPath, "Identity-H", true);
        Font font = new Font(bf);
        font.setSize(6.0F);
        PdfReader reader = new PdfReader(pdf);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper stamper = PdfStamper.createSignature(reader, baos, '\u0000', null, true);
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(12, 0);
        String tt = (new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")).format(Calendar.getInstance().getTime());
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setVisibleSignature(new Rectangle((float) x, (float) y, (float) width, (float) height), pagenumber, fieldname);
        //appearance.setLayer2Text("Người ký: " + dictionary.get("name") + "\n\nNgày ký: " + tt + "\n\nChức danh: " + dictionary.get("chucdanh") + "\n\nĐơn vị: " + dictionary.get("donvi") + "\n\nNội dung: " + dictionary.get("noidung"));
        appearance.setLayer2Text("Người ký: " + dictionary.get("name") + "\n\nNgày ký: " + tt + "\n\nVai trò: " + dictionary.get("chucdanh") + "\n\nNội dung: " + dictionary.get("noidung"));
        appearance.setLayer2Font(font);
        appearance.setCertificate(chain[0]);
        appearance.setRenderingMode(RenderingMode.DESCRIPTION);
        ExternalSignatureContainer external = new ExternalBlankSignatureContainer(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
        MakeSignature.signExternalContainer(appearance, external, 8192);
        return baos.toByteArray();
    }

    public byte[] emptySignature_image(InputStream pdf, String fieldname, Certificate[] chain, int x, int y, int width, int height, int pagenumber, String image) throws IOException, GeneralSecurityException, DocumentException {
        PdfReader reader = new PdfReader(pdf);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper stamper = PdfStamper.createSignature(reader, baos, '\u0000');
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setVisibleSignature(new Rectangle((float) x, (float) y, (float) height, (float) width), pagenumber, fieldname);
        appearance.setCertificate(chain[0]);
        appearance.setRenderingMode(RenderingMode.GRAPHIC);
        appearance.setSignatureGraphic(Image.getInstance(image));
        appearance.setImageScale(-1.0F);
        ExternalSignatureContainer external = new ExternalBlankSignatureContainer(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
        MakeSignature.signExternalContainer(appearance, external, 8192);
        return baos.toByteArray();
    }
}
