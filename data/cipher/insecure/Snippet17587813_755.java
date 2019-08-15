    package security.symmatric;

    import javax.crypto.Cipher;
    import javax.crypto.spec.SecretKeySpec;

    import org.apache.commons.codec.binary.Base64;

    public class AES {
        public static String symmetricEncrypt(String text, String secretKey) {
            byte[] raw;
            String encryptedString;
            SecretKeySpec skeySpec;
            byte[] encryptText = text.getBytes();
            Cipher cipher;
            try {
                raw = Base64.decodeBase64(secretKey);
                skeySpec = new SecretKeySpec(raw, "AES");
                cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
                encryptedString = Base64.encodeBase64String(cipher.doFinal(encryptText));
            } 
            catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
            return encryptedString;
        }

        public static String symmetricDecrypt(String text, String secretKey) {
            Cipher cipher;
            String encryptedString;
            byte[] encryptText = null;
            byte[] raw;
            SecretKeySpec skeySpec;
            try {
                raw = Base64.decodeBase64(secretKey);
                skeySpec = new SecretKeySpec(raw, "AES");
                encryptText = Base64.decodeBase64(text);
                cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
                encryptedString = new String(cipher.doFinal(encryptText));
            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
            return encryptedString;
        }

        public static void main(String[] args) {
            String secretKey = "XMzDdG4D03CKm2IxIWQw7g==";
            String value1= "ABCD";
            String enctypedValue1= "3uweh4pzoVyH1uODQmVNJA==";
            String enctypedValue2= "37PTC20w4DMZYjG3f+GWepSvAbEJUccMXwS/lXilLav1qM/PrCTdontw5/82OdC1zzyhDEsFVRGo rV6gXAQcm+Zai15hliiUQ8l8KRMtUl4=";
            String value4= "20000";

            /**  Ecnryption and decryption of value1 **/
            String encryptedValue1= symmetricEncrypt(value1, secretKey);
            String decryptedValue1 = symmetricDecrypt(encryptedValue1, secretKey);
            System.out.println(decryptedValue1);

            /**  Decryption of  enctypedValue1 **/
            String decryptedValue2 = symmetricDecrypt(enctypedValue1, secretKey);
            System.out.println(decryptedValue2);

            /**  Decryption of  enctypedValue2 **/
            String decryptedValue3 = symmetricDecrypt(enctypedValue2, secretKey);
            System.out.println(decryptedValue3);

            /**  Ecnryption and decryption of value4 **/
            String encryptedValue4= symmetricEncrypt(value4, secretKey);
            String decryptedValue4 = symmetricDecrypt(encryptedValue4, secretKey);
            System.out.println(decryptedValue4);
        }
    }
