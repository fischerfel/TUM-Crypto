public class AESEncrtptor {
    String strKey = "1234567890123456";
    byte[] byteKey;
    byte[] byteVector = new byte[] { 59, 12, (byte) 129, 77, 39, 119, 82, 6,
            23, 1, 55, 24, 12, (byte) 154, (byte) 224, 14 };

    public AESEncrtptor() {
        try {
            byteKey = strKey.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public enum EnumCipherMode {
        CBC, ECB
    }

    public String Encrypt(String strdata, EnumCipherMode CipherMode) {
        try {
            String strAlgorithm = "AES/CBC/PKCS7Padding";
            switch (CipherMode) {
            case CBC:
                strAlgorithm = "AES/CBC/PKCS7Padding";
                break;
            case ECB:
                strAlgorithm = "AES/ECB/PKCS7Padding";
                break;
            }
            Cipher c = Cipher.getInstance(strAlgorithm);
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, strAlgorithm);
            switch (CipherMode) {
            case CBC:
                c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(
                        byteVector));
                break;
            case ECB:
                c.init(Cipher.ENCRYPT_MODE, keySpec);
                break;
            }
            byte[] data = strdata.getBytes();

            byte[] encrypted = c.doFinal(data);
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            return "";
        }
    }

    public String Decrypt(String strdata, EnumCipherMode CipherMode) {
        try {
            String strAlgorithm = "AES/CBC/PKCS7Padding";
            switch (CipherMode) {
            case CBC:
                strAlgorithm = "AES/CBC/PKCS7Padding";
                break;
            case ECB:
                strAlgorithm = "AES/ECB/PKCS7Padding";
                break;
            }
            Cipher d_c = Cipher.getInstance(strAlgorithm);
            SecretKeySpec d_keySpec = new SecretKeySpec(byteKey, strAlgorithm);
            switch (CipherMode) {
            case CBC:
                d_c.init(Cipher.DECRYPT_MODE, d_keySpec, new IvParameterSpec(
                        byteVector));
                break;
            case ECB:
                d_c.init(Cipher.DECRYPT_MODE, d_keySpec);
                break;
            }

            byte[] decrypted = d_c.doFinal(Base64.decode(strdata,
                    Base64.DEFAULT));
            String decryptedStr = "";
            for (int i = 0; i < decrypted.length; i++)
                decryptedStr += (char) decrypted[i];
            return decryptedStr;
        } catch (Exception e) {
            return "";
        }
    }
}
