**public class SimpleCrypto {
public static String decrypt(String seed, String encrypted) throws Exception {
    byte[] keyb = seed.getBytes("UTF-8");
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] thedigest = md.digest(keyb);
    SecretKeySpec skey = new SecretKeySpec(thedigest, "AES/ECB/PKCS7Padding");
    Cipher dcipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
    dcipher.init(Cipher.DECRYPT_MODE, skey);
    byte[] clearbyte = dcipher.doFinal(toByte(encrypted));
    return new String(clearbyte);
}
public static byte[] toByte(String hexString) {
    int len = hexString.length()/2;
    byte[] result = new byte[len];
    for (int i = 0; i < len; i++)
        result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
    return result;
}
}**
