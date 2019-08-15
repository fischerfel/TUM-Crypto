public class Rsa {
    public static void main(String[] args) throws Throwable {
    //RSA init
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(512);
    KeyPair keyPair = keyGen.generateKeyPair();
    PrivateKey privateKey = keyPair.getPrivate();
    PublicKey publicKey = keyPair.getPublic();

    //AES init
    KeyGenerator keyGen2 = KeyGenerator.getInstance("AES");
    keyGen2.init(192);
    SecretKey secretKey = keyGen2.generateKey();
    byte[] encoded = secretKey.getEncoded(); 
    System.out.println("Original  AES key:"+ encoded);

    //Encrypting AES with RSA
    Cipher cipher  = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] encryptedMsg = cipher.doFinal(encoded);
    System.out.println("Encrypted AES Key with RSA:" + encryptedMsg);

    //Decrypting AES with RSA
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] decryptedMsg = cipher.doFinal(encryptedMsg);
    System.out.println("Decrypted AES Key:" + decryptedMsg);

}   
