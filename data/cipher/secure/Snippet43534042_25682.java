public static String PUBLIC_KEY = "YOUR PUBLIC KEY";

static String enccriptData(String txt)
{
  String encoded = "";
  byte[] encrypted = null;
    try {
        byte[] publicBytes = Base64.decode(PUBLIC_KEY, Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING"); //or try with "RSA"
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        encrypted = cipher.doFinal(txt.getBytes());
        encoded = Base64.encodeToString(encrypted, Base64.DEFAULT);
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    return encoded;
}
