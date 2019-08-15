public class Encrypter {
    private String iv           = "qj839.SkW@a#pPsX";
    private String SecretKey    = "!D&@DKmq81-CClo";
    String keyphrase = "SomeWords";

    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;

    public Encrypter() {
        SecretKey = Hash.getMD5(keyphrase).substring(4, 20);
        ivspec = new IvParameterSpec(iv.getBytes());
        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");
        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public byte[] decrypt(String code) throws Exception {
        if (code == null || code.length() == 0) {
            return null;
        }

        code = code.replaceAll("-", "").toLowerCase();
        byte[] decrypted = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            decrypted = cipher.doFinal(hexToBytes(code));
            //Remove trailing zeroes
            if (decrypted.length > 0) {
                int trim = 0;
                for (int i = decrypted.length - 1; i >= 0; i--) {
                    if (decrypted[i] == 0) {
                        trim++;
                    }
                }

                if (trim > 0) {
                    byte[] newArray = new byte[decrypted.length - trim];
                    int length = decrypted.length - trim;
                    int srcPos = 0;
                    int destPos = 0;
                    while (length > 0) {
                        newArray[destPos] = decrypted[srcPos];
                        srcPos++;
                        destPos++;
                        length--;
                    }
                    decrypted = newArray;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return decrypted;
    }

    public String decryptString(String text) throws Exception {
        byte[] temp = decrypt(text);
        if (temp == null) return null;
        return new String(temp);
    }

    public byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }
}
