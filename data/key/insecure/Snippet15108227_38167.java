public String encryptStringWithAES(String Message) throws Exception {

    String key = "123456789abcdefg";

    String iv = "1234567890123456";

    String padding = "ZeroBytePadding";


    SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF8"), "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/" + padding);



    IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());


    cipher.init(Cipher.ENCRYPT_MODE, spec, ivspec);
    byte[] array = cipher.doFinal(Message.getBytes());

    System.out.println("encrypted ARRAY LENGHT:" + array.length);

    String encoded_string = android.util.Base64.encodeToString(array, 0,
    array.length, Base64.NO_PADDING);

    System.out.println("Requested encoded string: " + encoded_string);
    return encoded_string;

}
