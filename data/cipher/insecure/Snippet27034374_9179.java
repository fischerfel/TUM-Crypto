public class Main {

public static void main(String[] args) {
    try {
        System.out.println(encrypt("12345678", "abc", "12345678"));
        //System.out.println(encrypt("12345678", "ABC", "12345678"));


        System.out.println(decrypt("12345678", "9YR6ZPdZufM=", "12345678"));
        //System.out.println(decrypt("12345678", "6rtTnrF34mPkJ5SO3RiaaQ==", "12345678"));

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public static String encrypt(String key, String str, String ivString) throws Exception {
    DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    Key secretKey = keyFactory.generateSecret(desKeySpec);

    IvParameterSpec iv = new IvParameterSpec(ivString.getBytes());
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
    byte[] bytes = cipher.doFinal(str.getBytes());
    dumpHex(bytes);

    return Base64.encode(bytes);
}

public static void dumpHex(byte[] bytes) {
    for (byte b : bytes) {
        System.out.println(String.format("%02x",b&0xff));
    }
    System.out.println(bytesToHex(bytes));
}

final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for ( int j = 0; j < bytes.length; j++ ) {
        int v = bytes[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
}

public static String decrypt(String key, String str, String ivString) throws Exception {
    byte[] data = Base64.decode(str);
    dumpHex(data);
    DESKeySpec dks = new DESKeySpec(key.getBytes());
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    Key secretKey = keyFactory.generateSecret(dks);
    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    IvParameterSpec iv = new IvParameterSpec(ivString.getBytes());
    cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
    byte[] decryptedBytes = cipher.doFinal(data);
    return new String(decryptedBytes, "gb2312");
}
