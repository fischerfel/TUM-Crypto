public static void POCSimple()
{
    String secretMessage = "short message";
    PublicKey publicKey = null;
    PrivateKey privateKey = null;
    String encodedMessage = "";
    byte[] encodedBytes = null;
    String decodedMessage ="";
    byte[] decodedBytes = null;


    try
    {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair kp = kpg.genKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();

        Cipher c1 = Cipher.getInstance("RSA");
        c1.init(Cipher.ENCRYPT_MODE, publicKey);
        encodedBytes = c1.doFinal(secretMessage.getBytes());
        encodedMessage = Base64.encodeToString(encodedBytes, Base64.DEFAULT);

        Cipher c2 = Cipher.getInstance("RSA");
        c2.init(Cipher.DECRYPT_MODE, privateKey);
        decodedBytes = c2.doFinal(encodedBytes);
        decodedMessage = Base64.encodeToString(decodedBytes, Base64.DEFAULT);

        String mystring = "look at results";

    }
    catch (Exception e)
    {
        String status = e.toString();
    }


}
