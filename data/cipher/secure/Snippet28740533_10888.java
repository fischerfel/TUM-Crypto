import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.PublicKey;
import java.security.Security;

import javax.crypto.Cipher;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;

public class TestTut2 {
    @Test
    public void TestKeyPair() throws IOException {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        String privateKeyString = "-----BEGIN RSA PRIVATE KEY-----\n" + "MIICXQIBAAKBgQDKQtJAyCu5FHwDncK2LB/J5ClJhulGggyc7vwtji6TJHtSJfgD\n" + "4TLpHRIHh/cHqf3brhpQtYB9yjKlwogji/OzedY2mdTdSOP8O6suJYu3QENN2xG/\n" + "HvT8UiYK3feVLbJtukhJm7eSuwfMDsjHh4AK7g11fVs6EmY+foh3mjoKLQIDAQAB\n" + "AoGAR8N/wDaFtOx8t/fAv0xWlxaaQ5lXqYm5GfF9jlhVVCXsj5AjOJUtsCJ9ZCis\n" + "0I5TIR/b/Gj5xyf34nJsRViBxbnf6XdLGyXmzsNxWZoWbM70JaqU3iQKm605/EnD\n" + "vPgrI0AMfc/h6Kog0zLrKWKkna+wE5839yMmm7WPqgvxSc0CQQDoud5e3yZu/1e+\n" + "7piFZZl6StAecl+k10Wq5kzJeVQRffDB3JCca65H/W1EZIzEh76pUNr7SYAIIcbK\n" + "jzOdbj1vAkEA3n0AudM3mBzklLEUSHs1ZSqFkUMNP9MNIikwkZ/9Z2AlhW5gnwiv\n" + "dgeXonTqlTFux4e7uyKZoJpJcKAgmMicIwJBAIMl206TalE6y/Po+UKTUr470rSV\n" + "t5hpR/Va+wK+wMVqt3ZIGaZMeFZRVnYoQ7us06EO05iwftoWTrRvpqKdMTkCQBkE\n"
                + "QzWhy0l+TjFt69Luj6Vtb5FS0cWQbJSfvwdQzwR1qiJjs9eN+XSzC9jHfq0B3uvu\n" + "lixHirClSIayapfjTrMCQQCM8d97py4u9hCdCpsHBDt54dXkHsDA2abNzaPri/YA\n" + "pNFZGrfXKVGSLFOfsuf7Wj+yL7ew6ZVKOMYdJ+zb9Wwv\n" + "-----END RSA PRIVATE KEY-----"; // 128
                                                                                                                                                                                                                                                    // bit
                                                                                                                                                                                                                                                    // key
        String publicKeyString = "-----BEGIN PUBLIC KEY-----\n" + "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDKQtJAyCu5FHwDncK2LB/J5ClJ\n" + "hulGggyc7vwtji6TJHtSJfgD4TLpHRIHh/cHqf3brhpQtYB9yjKlwogji/OzedY2\n" + "mdTdSOP8O6suJYu3QENN2xG/HvT8UiYK3feVLbJtukhJm7eSuwfMDsjHh4AK7g11\n" + "fVs6EmY+foh3mjoKLQIDAQAB\n" + "-----END PUBLIC KEY-----";
        String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        String cypherText = "EqVFWCMJ2rSy1J0PjAkRRZKkQ24TJ7xQi%2FjKUa3E7ZJ%2FlwtFsBkUDqJ9VUb0aC53O4TM4uNKMmYQNFDTHpQSgoun95ExgoCAvC1BXz2jVzWkKavt1vWbhS1C5VKcWU0hfUOmxZgiOT4rGWpEXVXoLodKLiJnbkvVNZyjgw0LZPQ%3D";

        System.out.println("private:");

        Reader privateKeyReader = new StringReader(privateKeyString);

        PEMParser privatePemParser = new PEMParser(privateKeyReader);
        Object privateObject = privatePemParser.readObject();
        System.out.println("private: " + privateObject.getClass());
        if (privateObject instanceof PEMKeyPair) {
            PEMKeyPair pemKeyPair = (PEMKeyPair) privateObject;
            System.out.println("private: " + pemKeyPair.getPrivateKeyInfo());
            System.out.println("public: " + pemKeyPair.getPublicKeyInfo());

            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

            PublicKey publicKey = converter.getPublicKey(pemKeyPair.getPublicKeyInfo());

            String encodedURL = null;
            try {
                System.out.println("encrypting using WP publicKey and own cypherText\r\n");

                byte[] encripted = encrypt(publicKey, message);
                System.out.println("encrypted: " + new String(encripted));

                byte[] encodedURLBase64 = Base64.encode(encripted);
                System.out.println("base64: " + new String(encodedURLBase64));

                encodedURL = URLEncoder.encode(new String(encodedURLBase64));
                System.out.println("encodedURL: " + encodedURL);
            } catch (Exception erm) {
                System.out.println("erm: "+erm.getMessage());
            }
            // and back
            try {
                System.out.println("decrypting using WP publicKey and own cypherText\r\n");
                String decodedURL = URLDecoder.decode(encodedURL);
                System.out.println("decodedURL: " + decodedURL);

                byte[] decodedURLBase64 = Base64.decode(decodedURL);
                System.out.println("decodedURLBase64: " + new String(decodedURLBase64));

                String decrypted = decrypt(publicKey, decodedURLBase64);
                System.out.println("decrypted: " + new String(decrypted));
            } catch (Exception erm) {
                System.out.println("erm: "+erm.getMessage());
            }
            // using stuff from external party
            try {
                System.out.println("decrypting using WP publicKey and WP cypherText\r\n");

                String decodedURLWP = URLDecoder.decode(cypherText);
                System.out.println("decodedURLBase64WP: " + decodedURLWP);

                byte[] decodedURLBase64WP = Base64.decode(decodedURLWP);
                System.out.println("decodedURLBase64WP: " + new String(decodedURLBase64WP));

                String decryptedWP = decrypt(publicKey, decodedURLBase64WP);
                System.out.println("decryptedWP: " + new String(decryptedWP));
            } catch (Exception erm) {
                System.out.println("erm: "+erm.getMessage());
            }

        }
        privatePemParser.close();

        System.out.println("public:");
        Reader publicKeyReader = new StringReader(publicKeyString);
        PEMParser publicPemParser = new PEMParser(publicKeyReader);

        Object publicObject = publicPemParser.readObject();
        System.out.println("public: " + publicObject.getClass());
        if (publicObject instanceof SubjectPublicKeyInfo) {
            SubjectPublicKeyInfo publicSubjectPublicKeyInfo = (SubjectPublicKeyInfo) publicObject;
            // System.out.println("private: "+publicSubjectPublicKeyInfo);
            System.out.println("public: " + publicSubjectPublicKeyInfo);

        }
        publicPemParser.close();



    }

    private static byte[] encrypt(Key pubkey, String text) {
        try {
            Cipher rsa;
            rsa = Cipher.getInstance("RSA", "BC");
            rsa.init(Cipher.ENCRYPT_MODE, pubkey);
            return rsa.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String decrypt(Key decryptionKey, byte[] buffer) {
        try {
            Cipher rsa;
            rsa = Cipher.getInstance("RSA", "BC");
            rsa.init(Cipher.DECRYPT_MODE, decryptionKey);
            byte[] utf8 = rsa.doFinal(buffer);
            return new String(utf8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
