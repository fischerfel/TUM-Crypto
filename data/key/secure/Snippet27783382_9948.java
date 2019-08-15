    import java.security.MessageDigest;
    import javax.crypto.spec.SecretKeySpec;
    import javax.crypto.spec.IvParameterSpec;
    import javax.crypto.Cipher;
    import java.util.Base64;
    import javax.xml.bind.DatatypeConverter;

    public class AESExample {
        private static byte[] iv = "0000000000000000".getBytes();
        private static String decrypt(String encrypted, String seed)
                throws Exception {
            byte[] keyb = seed.getBytes("utf-8");
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] thedigest = md.digest(keyb);
            SecretKeySpec skey = new SecretKeySpec(thedigest, "AES");
            Cipher dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            dcipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(seed.getBytes("UTF-8"), "AES"), new IvParameterSpec(iv));
            byte[] clearbyte = dcipher.doFinal(DatatypeConverter
                    .parseHexBinary(encrypted));
            return new String(clearbyte);
        }
        public static String encrypt(String content, String key) throws Exception {
            byte[] input = content.getBytes("utf-8");
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] thedigest = md.digest(key.getBytes("utf-8"));
            SecretKeySpec skc = new SecretKeySpec(thedigest, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("UTF-8"), "AES"), new IvParameterSpec(iv));
            byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
            int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
            ctLength += cecrtKeyByteipher.doFinal(cipherText, ctLength);
            return DatatypeConverter.printHexBinary(cipherText);
        }

public static String encrypt128(String content, String key) throws Exception {
        byte[] input = content.getBytes("utf-8");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(DatatypeConverter.parseHexBinary(key), "AES"), new IvParameterSpec(iv));
         byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));
        return DatatypeConverter.printHexBinary(encrypted);
    }

        public static void main(String[] args) throws Exception {
            String data = "Here is my string";
            String key = "1234567891123456";
            String cipher = AESExample.encrypt(data, key);
            String decipher = AESExample.decrypt(cipher, key);
            System.out.println(cipher);
            System.out.println(decipher);
            System.out.println(AESExample.encrypt(data, "1234567891123456"));
            System.out.println(AESExample.encrypt128(data, "d7900701209d3fbac4e214dfeb5f230f"));
        }
    }
