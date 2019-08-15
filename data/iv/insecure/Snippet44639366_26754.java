public static class ApiCrypter {

    private String iv = "0000000000000000";
    private byte[] secretkey;     //="mysecretkeyyyyyy";
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;

    public ApiCrypter(byte[] key)
    {
        secretkey=key;
        ivspec = new IvParameterSpec(iv.getBytes());
        keyspec = new SecretKeySpec(secretkey, "AES");

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public byte[] encrypt(String text) throws Exception
    {
        if(text == null || text.length() == 0) {
            throw new Exception("Empty string");
        }
        byte[] encrypted = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        }
        catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }
        return encrypted;
    }

    public byte[] decrypt(String code) throws Exception
    {
        if(code == null || code.length() == 0) {
            throw new Exception("Empty string");
        }
        byte[] decrypted = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            decrypted = cipher.doFinal(hexToBytes(code));
        }
        catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return decrypted;
    }

    public static String bytesToHex(byte[] data)
    {
        if (data==null) {
            return null;
        }
        int len = data.length;
        String str = "";
        for (int i=0; i<len; i++) {
            if ((data[i]&0xFF)<16) {
                str = str + "0" + java.lang.Integer.toHexString(data[i]&0xFF);
            }
            else {
                str = str + java.lang.Integer.toHexString(data[i]&0xFF);
            }
        }
        return str;
    }

    public static byte[] hexToBytes(String str) {
        if (str==null) {
            return null;
        }
        else if (str.length() < 2) {
            return null;
        }
        else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i=0; i<len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
            }
            return buffer;
        }
    }
}
