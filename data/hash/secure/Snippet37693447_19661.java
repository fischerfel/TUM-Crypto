package com.github.ttwd80.ming;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class PreShareUrl {

    private String bucketName;
    private String keyName;
    private String region;
    private int secondsToExpire;
    private String accessKey;
    private String secretKey;
    private String amzDate;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getSecondsToExpire() {
        return secondsToExpire;
    }

    public void setSecondsToExpire(int secondsToExpire) {
        this.secondsToExpire = secondsToExpire;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAmzDate() {
        return amzDate;
    }

    public void setAmzDate(String amzDate) {
        this.amzDate = amzDate;
    }

    public String generate() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        StringBuilder sb = new StringBuilder();
        sb.append("https://");
        sb.append(createHost());
        sb.append("/");
        sb.append(keyName);
        sb.append("?");
        sb.append("X-Amz-Algorithm=AWS4-HMAC-SHA256");
        sb.append("&");
        sb.append("X-Amz-Date=");
        sb.append(amzDate);
        sb.append("&");
        sb.append("X-Amz-SignedHeaders=host");
        sb.append("&");
        sb.append("X-Amz-Expires=");
        sb.append(secondsToExpire);
        sb.append("&");
        sb.append("X-Amz-Credential=");
        sb.append(URLEncoder.encode(createCredential(), "UTF-8"));
        sb.append("&");
        sb.append("X-Amz-Signature=");
        sb.append(calculate());
        return sb.toString();
    }

    private String createHost() {
        StringBuilder sb = new StringBuilder();
        sb.append(bucketName);
        sb.append(".s3-");
        sb.append(region);
        sb.append(".amazonaws.com");
        return sb.toString();
    }

    private String createCredential() {
        StringBuilder sb = new StringBuilder();
        sb.append(accessKey);
        sb.append("/");
        sb.append(createCredentialScope());
        return sb.toString();
    }

    private String calculate() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String canonicalRequest = createCanonicalRequest();
        System.out.println("canonicalRequest");
        System.out.println(canonicalRequest);

        String stringToSign = createStringToSign(canonicalRequest);
        System.out.println(stringToSign);

        byte[] signingKey = newSigningKey(secretKey, amzDate.substring(0, 8), region, "s3");
        System.out.println(bytesToHex(signingKey));
        byte[] signature = sign(stringToSign.getBytes(Charset.forName("UTF-8")), signingKey, "HmacSHA256");

        return bytesToHex(signature);
    }

    private String createStringToSign(String canonicalRequest) throws NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        sb.append("AWS4-HMAC-SHA256");
        sb.append("\n");
        sb.append(amzDate);
        sb.append("\n");
        sb.append(createCredentialScope());
        sb.append("\n");
        String hashedCanonicalRequest = bytesToHex(doHash(canonicalRequest));
        sb.append(hashedCanonicalRequest);
        return sb.toString();
    }

    private String createCredentialScope() {
        StringBuilder sb = new StringBuilder();
        sb.append(amzDate.substring(0, 8));
        sb.append("/");
        sb.append(region);
        sb.append("/");
        sb.append("s3");
        sb.append("/");
        sb.append("aws4_request");
        return sb.toString();
    }

    private String createCanonicalRequest() throws UnsupportedEncodingException {
        String contentSha256 = "UNSIGNED-PAYLOAD";
        StringBuilder sb = new StringBuilder();
        sb.append("GET");
        sb.append("\n");
        sb.append("/");
        sb.append(keyName);
        sb.append("\n");
        sb.append("X-Amz-Algorithm=AWS4-HMAC-SHA256");
        sb.append("&");
        sb.append("X-Amz-Credential=");
        sb.append(URLEncoder.encode(createCredential(), "UTF-8"));
        sb.append("&");
        sb.append("X-Amz-Date=");
        sb.append(amzDate);
        sb.append("&");
        sb.append("X-Amz-Expires=");
        sb.append(secondsToExpire);
        sb.append("&");
        sb.append("X-Amz-SignedHeaders=host");
        sb.append("\n");
        sb.append("host:");
        sb.append(createHost());
        sb.append("\n");
        sb.append("\n");
        sb.append("host");
        sb.append("\n");
        sb.append(contentSha256);
        return sb.toString();
    }

    private byte[] doHash(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes(Charset.forName("UTF-8")));
        return md.digest();
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private byte[] newSigningKey(String secretKey, String dateStamp, String regionName, String serviceName)
            throws InvalidKeyException, NoSuchAlgorithmException {
        byte[] kSecret = ("AWS4" + secretKey).getBytes(Charset.forName("UTF-8"));
        byte[] kDate = sign(dateStamp.getBytes(), kSecret, "HmacSHA256");
        byte[] kRegion = sign(regionName.getBytes(), kDate, "HmacSHA256");
        byte[] kService = sign(serviceName.getBytes(), kRegion, "HmacSHA256");
        return sign("aws4_request".getBytes(), kService, "HmacSHA256");
    }

    protected byte[] sign(byte[] data, byte[] key, String algorithm)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(algorithm.toString());
        mac.init(new SecretKeySpec(key, algorithm.toString()));
        return mac.doFinal(data);
    }

}
