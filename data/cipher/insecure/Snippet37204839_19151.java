public class ata {
public static byte[] a(byte[] bArr, String str) {
    try {
        return ata.a(bArr, str.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        return null;
    }
}

public static byte[] a(byte[] bArr, byte[] bArr2) {
    try {
        Cipher instance = Cipher.getInstance("RC4");
        instance.init(2, new SecretKeySpec(bArr2, "RC4"));
        return instance.update(bArr);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        return null;
    } catch (NoSuchPaddingException e2) {
        e2.printStackTrace();
        return null;
    } catch (InvalidKeyException e3) {
        e3.printStackTrace();
        return null;
    }
  }
}
