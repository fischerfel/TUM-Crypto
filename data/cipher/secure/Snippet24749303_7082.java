private String iv = "MYKEYHERE";//Dummy iv (CHANGE IT!)
private String SecretKey = "MYKEYHERE";//Dummy secretKey (CHANGE IT!)

private byte[] decrypt(String code)
{
    byte[] decrypted = null;
    try {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
        SecretKeySpec keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");
        if(code == null || code.length() == 0)
            throw new Exception("Empty string");

        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

        decrypted = cipher.doFinal(hexToBytes(code));
    } catch (Exception e) {
        e.printStackTrace();
    }
    return decrypted;
}

private static byte[] hexToBytes(String str) {
    if (str==null) {
        return null;
    } else if (str.length() < 2) {
        return null;
    } else {
        int len = str.length() / 2;
        byte[] buffer = new byte[len];
        for (int i=0; i<len; i++) {
            try {
                buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return buffer;
    }
}
