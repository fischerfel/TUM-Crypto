private Transfer encrypt(byte[] salt, String ticketNumber, String data) throws Exception {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(ticketNumber.toCharArray(), salt, 1000, 128);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
    String encoded = java.util.Base64.getEncoder().encodeToString(secret.getEncoded());
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    byte[] iv ={0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
    IvParameterSpec ips = new IvParameterSpec(iv);
    cipher.init(Cipher.ENCRYPT_MODE, secret, ips);
    AlgorithmParameters params = cipher.getParameters();
    Transfer myRetVal =  new Transfer();
    byte[] ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();

    //Set some decrypt information
    myRetVal.setIv(Base64.encodeBase64String(ivBytes));
    //Set the attendee data
    myRetVal.setData(Base64.encodeBase64String(cipher.doFinal(data.getBytes())));
    //Set the hashed Ticket number
    myRetVal.setTicketNumberHashed(Base64.encodeBase64String(getHash(hashIterations, ticketNumber, salt)));
    return myRetVal;
}
