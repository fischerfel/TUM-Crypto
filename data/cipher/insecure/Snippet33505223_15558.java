public byte[] createMac(byte[] fileBytes, String nick){
    byte[] mac = null;
    byte[] digest;
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(fileBytes);
        digest = md.digest();

        KeyFactory keyFactory = KeyFactory.getInstance("DH");

        byte[] myPrivateKey = Files.readAllBytes(Paths.get(dirMyKeys+"//dhPrivateKey.txt"));
        PrivateKey dhPrivKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(myPrivateKey));


        byte[] myPublicKey = Files.readAllBytes(Paths.get(dirKeys+"/"+nick+"DhPublicKey.txt"));
        System.out.println(dirKeys+"/"+nick+"DhPublicKey.txt");
        PublicKey dhPubKey = keyFactory.generatePublic(new X509EncodedKeySpec(myPublicKey));

        SecretKey secretKey = combine(dhPrivKey, dhPubKey);

        Cipher cipher = Cipher.getInstance("AES");
        System.out.println( "\nStart decryption" );
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        mac = cipher.doFinal(digest);
        System.out.println( "Finish decryption: " );

        return mac;

    } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException ex) {
        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
    }
    return mac;
}

private static SecretKey combine(PrivateKey private1, PublicKey public1) throws NoSuchAlgorithmException, InvalidKeyException  {
    KeyAgreement ka = KeyAgreement.getInstance("DH");
    ka.init(private1);
    //error on this line
    ka.doPhase(public1, true);
    SecretKey secretKey = ka.generateSecret("DES");
    return secretKey;
}
