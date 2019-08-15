@Override
public String encrypt(String dataToEncrypt, String IV) throws Exception{
    if(encryptionKey.length() < 10){
        encryptionKey = generateEncryptionKey().toString();
    }
    System.out.println("number of IV bytes is "+IV.length()+" "+IV);
    Cipher cipher = Cipher.getInstance(encrpytionAlgo);
    SecretKey key = new SecretKeySpec(encryptionKey.getBytes(Charset.forName("UTF-8")), "AES");
    cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes(Charset.forName("UTF-8"))));
    byte[] encryptedTextBytes = cipher.doFinal(dataToEncrypt.getBytes(Charset.forName("UTF-8")));
    return new Base64().encodeAsString(encryptedTextBytes);
}
