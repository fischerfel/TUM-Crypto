package com.company;
import java.util.Base64;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        System.out.println("Encryption process started...");
        //Next Line Explanation: "Bytes" is a class developed by me and for this case, its behaviour is verified.
        byte[] rawData = Bytes.fromHexString("ec5ac9830817ED48941A08F98100000004494C553B00000004539110730000003E0549828CCA27E966B301A48FECE2FCA5CF4D33F4A11EA877BA4AA573907330311C85DB234AA2640AFC4A76A735CF5B1F0FD68BD17FA181E1229AD867CC024D").toPbytes();
        System.out.println("Bytes to encrypt:");
        System.out.println(new Bytes(rawData).toString());
        final String RSA_PUBLIC_KEY = "-----BEGIN RSA PUBLIC KEY-----" +
                "MIIBCgKCAQEAwVACPi9w23mF3tBkdZz+zwrzKOaaQdr01vAbU4E1pvkfj4sqDsm6" +
                "lyDONS789sVoD/xCS9Y0hkkC3gtL1tSfTlgCMOOul9lcixlEKzwKENj1Yz/s7daS" +
                "an9tqw3bfUV/nqgbhGX81v/+7RFAEd+RwFnK7a+XYl9sluzHRyVVaTTveB2GazTw" +
                "Efzk2DWgkBluml8OREmvfraX3bkHZJTKX4EQSjBbbdJ2ZXIsRrYOXfaA+xayEGB+" +
                "8hdlLmAjbCVfaigxX0CDqWeR1yFL9kwd9P0NsZRPsmoqVwMbMu7mStFai6aIhc3n" +
                "Slv8kg9qv1m6XHVQY3PnEw+QQtqSIXklHwIDAQAB" +
                "-----END RSA PUBLIC KEY-----";
        byte[] rsaPublicKeyBytes = Base64.getDecoder().decode(RSA_PUBLIC_KEY);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKeyBytes);
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedData = cipher.doFinal(rawData);
        System.out.println(new Bytes(encryptedData).toString());
    }
}
