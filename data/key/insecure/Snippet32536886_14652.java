private String encryptString(String origString) {
    String encryptedString = "";
    Cipher cipher = null;
    byte[] encoded = null;
    byte[] rawEnc =null;
    try {
        //Code which is working
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(INITIALIZATIO_VECTOR.getBytes("UTF-8")));
        rawEnc = cipher.doFinal(origString.getBytes("UTF-8"));
        encoded = Base64.encodeBase64(rawEnc);
        encryptedString = new String(encoded, "UTF-8");

    } catch (NoSuchAlgorithmException e) {
        System.out.println("No Such Algorithm Exception:" + e.getMessage());
    } catch (NoSuchProviderException e) {
        System.out.println("No Such Provider Exception:" + e.getMessage());
    } catch (NoSuchPaddingException e) {
        System.out.println("No Such Padding Exception:" + e.getMessage());
    } catch (InvalidKeyException | InvalidAlgorithmParameterException
             | UnsupportedEncodingException e) {
        System.out.println("Exception:" + e.getMessage());
    } catch (Exception e) {
        System.out.println("Exception:" + e.getMessage());
    }
    return encryptedString;
}
private static final String SECRET_KEY = "secret_key";
private static final String INITIALIZATIO_VECTOR = "123456";

}
