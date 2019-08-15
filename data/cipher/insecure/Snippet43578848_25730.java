    package com.example.encryption;

    import javax.crypto.Cipher;
    import javax.crypto.SecretKey;
    import javax.crypto.spec.SecretKeySpec;
    import java.util.Base64;

    public class AESOwnKey {
        private Cipher ecipher;
        private Cipher dcipher;

        AESOwnKey(SecretKey key) {
            try {
                ecipher = Cipher.getInstance("AES");
                dcipher = Cipher.getInstance("AES");
                ecipher.init(Cipher.ENCRYPT_MODE, key);
                dcipher.init(Cipher.DECRYPT_MODE, key);
            } catch (Exception e) {
                System.out.println("Failed in initialization");
            }
        }

        public byte[] encrypt(String str) {
            try {
                byte[] utf8 = str.getBytes("UTF-8");
                byte[] enc = ecipher.doFinal(utf8);

                return Base64.getEncoder().encode(enc);
            } catch (Exception e) {
                System.out.println("Failed in Encryption");
            }
            return null;
        }

        public String decrypt(byte[] bytes) {
            try {
                byte[] dec = Base64.getDecoder().decode(bytes);

                byte[] utf8 = dcipher.doFinal(dec);

                return new String(utf8, "UTF-8");
            } catch (Exception e) {
                System.out.println("Failed in Decryption");
            }
            return null;
        }

        public static void main(String[] args) {
            try {
                String mykey = "1234567891234567";
                SecretKey key = new SecretKeySpec(mykey.getBytes(), "AES");
                AESOwnKey encrypter = new AESOwnKey(key);
                String original = "Testing encryption";
                System.out.println("Before Encryption   : " + original);
                byte[] encrypted = encrypter.encrypt(original);
                System.out.println("After Encryption   : " + encrypted);
                String decrypted = encrypter.decrypt(encrypted);
                System.out.println("After Decryption   : " + decrypted);

            } catch (Exception e) {
            }

        }
    }
