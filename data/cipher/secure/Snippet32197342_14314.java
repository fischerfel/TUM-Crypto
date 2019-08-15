import javax.crypto.Cipher;

String payload = "something";
public static void main(String[] args) throws Exception {
    String dig = makeDigest(payload, "SHA-1");
    Key k = getKey();
    String signature = encrypt(dig, "RSA/ECB/PKCS1Padding", k);
    System.out.print(signature);
}

public static String makeDigest(String payload, String digestAlgo) {
    String strDigest = "";
    try {
        MessageDigest md = MessageDigest.getInstance(digestAlgo);
        byte[] p_b = payload.getBytes("UTF-8");
        md.update(p_b);
        byte[] digest = md.digest();
        byte[] encoded = Hex.encode(digest);
        strDigest = new String(encoded);
    }
    catch (Exception ex) {
        ex.printStackTrace();
    }
    return strDigest;
}

public static String encrypt(String sha, String encryptAlgo, Key k) {
    String strEncrypted = "";
    try {
        Cipher cipher = Cipher.getInstance(encryptAlgo);
        cipher.init(Cipher.ENCRYPT_MODE, k);
        byte[] encrypted = cipher.doFinal(sha.getBytes("UTF-8"));
        byte[] encoded = Hex.encode(encrypted);
        strEncrypted = new String(encoded);
    }
    catch (Exception ex) {
        ex.printStackTrace();
    }
    return strEncrypted;
}
