public static void main(String[] args) throws Exception {
    //Generate the keys

    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(1024);
    KeyPair kp = kpg.genKeyPair();
    Key publicKey = kp.getPublic();
    Key privateKey = kp.getPrivate();

    KeyFactory fact = KeyFactory.getInstance("RSA");
    cip = Cipher.getInstance("RSA/ECB/NoPadding");

    // Store Public Key.

    X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
            publicKey.getEncoded());
    FileOutputStream fos = new FileOutputStream("public.key");
    fos.write(x509EncodedKeySpec.getEncoded());
    fos.close();

    // Store Private Key.
    PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
            privateKey.getEncoded());
    fos = new FileOutputStream("private.key");
    fos.write(pkcs8EncodedKeySpec.getEncoded());
    fos.close();

    //Get the public and private keys out of their files
    getPubAndPrivateKey();

    //Check if the keys gotten out of the files are the same as the generated files (this returns truetrue)
    System.out.print(publicKey.equals(pubKey));
    System.out.print(privateKey.equals(privKey));


    byte[] text = "This is my super secret secret".getBytes();
    encryptToFile("encrypted.txt", text );
    decryptToFile("encrypted.txt", "decrypted.txt");

}
