public static String cryptBC(String data, String key) throws Exception{
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    byte[] input = data.getBytes();
    byte[] keyBytes = key.getBytes() ;   
    SecretKeySpec skey = new SecretKeySpec(keyBytes, "DESede");
    Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding", "BC");

    if(input.length % 8 != 0){ 
        byte[] padded = new byte[input.length + 8 - (input.length % 8)];
        System.arraycopy(input, 0, padded, 0, input.length);
        input = padded;
    }
    System.out.println("input : " + new String(input));
    cipher.init(Cipher.ENCRYPT_MODE, skey);
    byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
    int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
    ctLength += cipher.doFinal(cipherText, ctLength);

    return new String(Base64.encodeBase64(cipherText));
}
