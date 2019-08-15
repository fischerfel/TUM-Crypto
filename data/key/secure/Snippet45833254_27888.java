package com.security;
import java.util.Map;
import javax.xml.bind.DatatypeConverter;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

public class Signature {
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String SECRET_KEY = "LF8np6Lk0PvQ8yQvOnsqvC5WOnRbLOEadpZOAYd22QNdXnSKI08VXfo6Kl0yV5wx";

    public static String sign(Map<String,String> params) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        return sign(buildDataToSign(params), SECRET_KEY).trim();
    }

    private static String sign(String data, String secretKey) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), HMAC_SHA256);
        Mac mac = Mac.getInstance(HMAC_SHA256);
        mac.init(secretKeySpec);
        byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));
        return DatatypeConverter.printBase64Binary(rawHmac).replace("\n", "").trim();
    }

    private static String buildDataToSign(Map<String,String> params) {
        String[] signedFieldNames = String.valueOf(params.get("signed_fields")).split(",");
        ArrayList<String> dataToSign = new ArrayList<String>();
        for (String signedFieldName : signedFieldNames) {
            dataToSign.add(signedFieldName + "=" + String.valueOf(params.get(signedFieldName)).trim());
        }
        return commaSeparate(dataToSign);
    }

    private static String commaSeparate(ArrayList<String> dataToSign) {
        StringBuilder csv = new StringBuilder();
        for (Iterator<String> it = dataToSign.iterator(); it.hasNext(); ) {
            csv.append(it.next());
            if (it.hasNext()) {
                csv.append(",");
            }
        }
        return csv.toString();
    }
}
