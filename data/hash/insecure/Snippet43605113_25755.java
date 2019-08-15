import com.pplive.common.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import sun.misc.BASE64Encoder;
    public static void main(String[] args) {
            // key: 8d3fj5T7lPMjMfMTyzdbl6Aq95zSUrmuChBFf/ex3lF23jHweq9ABJmEbd7+4z5DmjmhvTVyXWoeoFWCv6xCPFw7CAx7n9RZ9x9fOVrY3Cy+Nm1TAThsjgOjjK+M1S3XIim39NiRp55ai/dm2+E+NzRGPN0wW1bc9Q
            // b: a165f267f74aba5ded7563ebae2c5ac0
            // c: 6131363566323637663734616261356465643735363365626165326335616330
            // d: 636f6e74656e74446f63756d656e7473
            //ss: YzYv1oDPuHMyvtt8dgHUOdKaTev3rDtDXu9O%2BBlnCt4X118BroqK7nDjc%2Bo6cb6aoik6KAvIhZwENjkmrMvF26xhhWATh0TrlK0ZWPotNWI9mXWW1FgZtRrFkQGn6%2F4BWY8D%2BAfLxePup4rAw%2BekHjpkFsSZcY6rlL37uUKU%2F6OTdN4rrjG3FOEmN8yDZ9sMNAXu%2FdmAqgFRJ77gvWjgKZEXvhqYqaH1Ukb9eZYMRAE%3D

            //int end = html.indexOf("\")", start);
            //String plaintext = html.substring(start + "encodeURIComponent(get(\"".length(), end);
            String plaintext = "8d3fj5T7lPMjMfMTyzdbl6Aq95zSUrmuChBFf/ex3lF23jHweq9ABJmEbd7+4z5DmjmhvTVyXWoeoFWCv6xCPFw7CAx7n9RZ9x9fOVrY3Cy+Nm1TAThsjgOjjK+M1S3XIim39NiRp55ai/dm2+E+NzRGPN0wW1bc9Q";
            System.out.println("plaintext: " + plaintext);

            try {
                String b = EncoderByMd5("contentWindowHig");
                System.out.println("b: " + b);

                String c = byteToHexString(b.getBytes("utf-8"));
                System.out.println("c: " + c);

                String d = byteToHexString("contentDocuments".getBytes("utf-8"));
                System.out.println("d: " + d);

                String enc_data = AES_CBC_Encrypt_nopadding(
                        plaintext.getBytes("utf-8"),
                        b.getBytes(),
                        "contentDocuments".getBytes());
                System.out.println("enc_data: " + enc_data);

                String encoded_str = URLEncoder.encode(enc_data, "utf-8");
                System.out.println("encoded_str: " + encoded_str);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
}
        public static String AES_CBC_Encrypt_nopadding(byte[] content, byte[] keyBytes, byte[] iv){

                try{
                    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                    keyGenerator.init(128, new SecureRandom(keyBytes));
                    SecretKey key = keyGenerator.generateKey();
                    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
                    int blockSize = cipher.getBlockSize();
                    // process plain text
                    int plaintextLength = content.length;
                    if (plaintextLength % blockSize != 0) {
                        plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
                    }
                    byte[] plaintext = new byte[plaintextLength];
                    System.arraycopy(content, 0, plaintext, 0, content.length);
                    for (int i=content.length;i<plaintextLength;i++) {
                        plaintext[i] = (byte)0;
                    }
                    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
                    byte[] encrypted = cipher.doFinal(plaintext);
                    BASE64Encoder base64en = new BASE64Encoder();
                    return base64en.encode(encrypted);
                }catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("exception:"+e.toString());
                }
                return null;
            }
    public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
            MessageDigest md5=MessageDigest.getInstance("MD5");
            md5.update(str.getBytes());
            return new BigInteger(1, md5.digest()).toString(16);
        }

    public static String AES_CBC_Decrypt(byte[] content, byte[] keyBytes, byte[] iv){

            try{
                KeyGenerator keyGenerator=KeyGenerator.getInstance("AES");
                keyGenerator.init(128, new SecureRandom(keyBytes));//key长可设为128，192，256位，这里只能设为128
                SecretKey key=keyGenerator.generateKey();
                Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
                byte[] result = cipher.doFinal(content);
                BASE64Encoder base64en = new BASE64Encoder();
                return base64en.encode(result);
            }catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println("exception:"+e.toString());
            }
            return null;
        }

    //Converting a string of hex character to bytes
        public static byte[] hexStringToByteArray(String s) {
            int len = s.length();
            byte[] data = new byte[len / 2];
            for (int i = 0; i < len; i += 2){
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                        + Character.digit(s.charAt(i+1), 16));
            }
            return data;
        }

        public static String byteToHexString(byte[] bytes) {
            StringBuffer sb = new StringBuffer(bytes.length);
            String sTemp;
            for (int i = 0; i < bytes.length; i++) {
                sTemp = Integer.toHexString(0xFF & bytes[i]);
                if (sTemp.length() < 2)
                    sb.append(0);
                sb.append(sTemp.toUpperCase());
            }
            return sb.toString();
        }
