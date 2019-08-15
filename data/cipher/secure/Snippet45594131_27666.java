public class Encryption{
public static void main(String args){
    ByteString publicKey = client.getPublicKey(nonce).getRSAEncryptionKey();

    String strKeyPEM = "";
    BufferedReader br = new BufferedReader(new FileReader("crypt_pkcs8.pem"));
    String line;
    while ((line = br.readLine()) != null) {
        strKeyPEM += line+"\n";
    }
    br.close();
    byte[] pt;
    try {
        pt = "h".getBytes("UTF-8");
        byte[] ct =encrypt(publicKey, pt);
        byte[] response= decrypt(strKeyPEM, ct);

        assertEquals(pt, response);
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
public static byte[] encrypt(ByteString rawKey ,byte[] message){
    String strippedKey=stripKey(rawKey.toStringUtf8());

    byte[] keyBytes = Base64.getDecoder().decode(strippedKey);
    System.out.println(keyBytes.length);

    Cipher cipher_RSA;
    try {
        cipher_RSA = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        PublicKey pk = keyFactory.generatePublic(spec);
        System.out.println(pk.getAlgorithm()+" format : "+pk.getFormat());

        cipher_RSA.init(Cipher.ENCRYPT_MODE, pk); 
        return cipher_RSA.doFinal(message);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException |
            BadPaddingException | InvalidKeyException | InvalidKeySpecException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return null;
    }

}

public static byte[] decrypt(String rawKey ,byte[] message){
    String strippedKey=stripPrivateKey(rawKey);

    byte[] keyBytes = Base64.getDecoder().decode(strippedKey);
    System.out.println(keyBytes.length);

    Cipher cipher_RSA;
    try {
        cipher_RSA = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        PrivateKey pk = keyFactory.generatePrivate(spec);
        System.out.println(pk.getAlgorithm()+" format : "+pk.getFormat());

        cipher_RSA.init(Cipher.DECRYPT_MODE, pk); 
        return cipher_RSA.doFinal(message);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException |
            BadPaddingException | InvalidKeyException | InvalidKeySpecException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return null;
    }
}
public static String stripKey(String key){
    key = key.replace("-----BEGIN RSA PUBLIC KEY-----\n", "");
    key = key.replace("-----END RSA PUBLIC KEY-----", "");
    key = key.replace("\n", "");
    return key;
}

public static String stripPrivateKey(String key){
    key = key.replace("-----BEGIN PRIVATE KEY-----", "");
    key = key.replace("-----END PRIVATE KEY-----", "");
    key = key.replace("\n", "");
    return key;
}


}
