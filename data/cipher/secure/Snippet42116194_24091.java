

public String encryptData(String requestData, byte[] sessionKey,
        String messageRefNo) throws Exception {

    SecretKey secKey = new SecretKeySpec(sessionKey, "AES");
    Cipher cipher = Cipher.getInstance(symmetricKeyAlgorithm);
    IvParameterSpec ivSpec = new IvParameterSpec(messageRefNo.getBytes("UTF-8"));
    System.out.println("Seckey: "+secKey);
    cipher.init(Cipher.ENCRYPT_MODE, secKey, ivSpec);
    byte[] newData = cipher.doFinal(requestData.getBytes());

    return Base64.encodeBase64String(newData);
}
