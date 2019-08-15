private final static String keyString = "123456789012345678901234";
private final static String ivString = "abcdefgh";


public static String encrypt(String data) throws Exception {


    KeySpec keySpec = new DESedeKeySpec(keyString.getBytes());
    SecretKey key = SecretKeyFactory.getInstance("DESede").generateSecret(keySpec);
    IvParameterSpec iv = new IvParameterSpec(ivString.getBytes());
    Cipher ecipher = Cipher.getInstance("DESede/CFB8/NoPadding");
    ecipher.init(Cipher.ENCRYPT_MODE, key, iv);

    byte[] valeur = data.getBytes("UTF-8");
    byte[] enc = ecipher.doFinal(valeur);

    return new String(Base64.encode(enc, Base64.DEFAULT), "UTF-8");
}
