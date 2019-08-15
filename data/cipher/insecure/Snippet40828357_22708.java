static final String KEY_STRING = "MOGO_APP";

public static byte[] decrypt(byte[] encrypt)
{
    byte[] bArr = null;
    Key key = new SecretKeySpec(KEY_STRING.getBytes(), "DES");
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(2, key);
    bArr = cipher.doFinal(encrypt);
    return bArr;
}
