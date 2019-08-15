private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
private static final byte[] KEY = "StBet9834#$10BCy".getBytes();

private String encryptCreditCard(String ccNumber) {
    // do some encryption
    if (ccNumber == null || ccNumber.length() == 0) {
        return "";
    }
    Key key = new SecretKeySpec(KEY, "AES");
    try {
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        //return Base64.encodeBytes(c.doFinal(ccNumber.getBytes()));
        byte[] ccNumberBytes = ccNumber.getBytes();
        byte[] encCCNumber = c.doFinal(ccNumberBytes);
        return new String(Base64.encodeBase64(encCCNumber));
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
