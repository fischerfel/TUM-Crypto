package com.ucsc.raji;

import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class DualSignatureSample {

    public static byte[] encrypt(String original, Key privateKey) {
        if (original != null && privateKey != null) {
            byte[] bs = original.getBytes();
            byte[] encData = convert(bs, privateKey, Cipher.ENCRYPT_MODE);
            return encData;
        }
        return null;
    }

    public static byte[] decrypt(byte[] encrypted, Key publicKey) {
        if (encrypted != null && publicKey != null) {
            byte[] decData = convert(encrypted, publicKey, Cipher.DECRYPT_MODE);
            return decData;
        }
        return null;
    }

    private static byte[] convert(byte[] data, Key key, int mode) {
        try {
            //Cipher cipher = Cipher.getInstance("RSA");
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

            cipher.init(mode, key);
            byte[] newData = cipher.doFinal(data);
            byte[] datax = org.apache.commons.codec.binary.Base64.encodeBase64(newData);
            return datax;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static KeyPair getPrivateKey() throws Exception {
        FileInputStream is = new FileInputStream("C:" + File.separator
                + "Users" + File.separator + "rsatkunam" + File.separator
                + "Documents" + File.separator + "Rajeenthini" + File.separator
                + "MCS" + File.separator + "keystore.jks");

        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, "password".toCharArray());
        PublicKey publicKey = null;
        String alias = "rajeenthini";

        Key key = keystore.getKey(alias, "2015mcs070".toCharArray());
        if (key instanceof PrivateKey) {
            // Get certificate of public key
            Certificate cert = keystore.getCertificate(alias);

            // Get public key
            publicKey = cert.getPublicKey();

            // Return a key pair

        }
        return new KeyPair(publicKey, (PrivateKey) key);
    }

    public static PublicKey getPublicKey() throws Exception {
        FileInputStream is = new FileInputStream("C:" + File.separator
                + "Users" + File.separator + "rsatkunam" + File.separator
                + "Documents" + File.separator + "Rajeenthini" + File.separator
                + "MCS" + File.separator + "keystore.jks");

        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, "password".toCharArray());
        PublicKey publicKey = null;
        String alias = "rajeenthini";

        Key key = keystore.getKey(alias, "password".toCharArray());
        // Get certificate of public key
        Certificate cert = keystore.getCertificate(alias);
        // Get public key
        publicKey = cert.getPublicKey();
        return publicKey;
    }

    public static String getSha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static byte[] fromHexString(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    public static boolean verifyDualByMerchant(String ds, String oi, String hpi, PublicKey pubKey) throws NoSuchAlgorithmException{
        boolean isVerified = false;
        // String hOI = hash(oi);
        // String hoihpi = hoi + hpi;

        // String originalHash = decrypt(ds, pubKey);

        // if (hoihpi.equals(originalHash)) {
        //  isVerified = true;
        // }
        System.out.println(ds.length());

        byte[] ostr = decrypt(ds.getBytes(), pubKey);
        System.out.println(new String(ostr).toString());

        String xtr = new String(ostr);
        System.out.println(xtr);

        String x = getSha1(oi);
        String cx = x + hpi;

        String cxx = getSha1(cx);
        System.out.println(cxx);


        return isVerified;
    }
    public static void main(String args[]) {
        String OI = "One Laptop Computer";
        String PI = "4465-5342-2344-1009";

        try {
            /*System.out.println("---------1"+getPrivateKey().getPrivate());
            byte[] cipherTxt = encrypt(OI, getPrivateKey().getPrivate());
            System.out.println("---------2"+cipherTxt.toString());

            System.out.println("---------3"+getPublicKey());
            byte[] plainTxt = decrypt(cipherTxt, getPublicKey());
            String s = new String(plainTxt);
            System.out.println("---------4"+s);*/

            String hashOI = getSha1(OI);
            System.out.println("---------"+hashOI);

            String hashPI = getSha1(PI);
            System.out.println("---------"+hashPI);


            String concatOIPI = hashOI + hashPI;
            System.out.println("---------"+concatOIPI);

            String hashconcatOIPI = getSha1(concatOIPI);
            System.out.println("---------"+hashconcatOIPI);


            byte[] dualSignature = encrypt(hashconcatOIPI, getPrivateKey().getPrivate());
            String s = new String(dualSignature);   

            PublicKey pubKey = getPublicKey();
            verifyDualByMerchant(s, OI, hashPI, pubKey);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
