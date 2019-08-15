private String encrypt(String user) throws Exception
{
    Cipher deCipher;
    Cipher enCipher;
    SecretKeySpec key;
    IvParameterSpec ivSpec;
    String plainKey = "6JxI1HOSg7KQj4fJ1Xb3L1T6AVdLZLBAPFSqOjh2UoA=";
    String salt = "FPSJxiSMpAavjKqyGvVe1A==";
    String result = "";
    ivSpec = new IvParameterSpec(salt.getBytes());
    key = new SecretKeySpec(plainkey.getBytes(), "AES");
    enCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    byte[] input = convertToByteArray(user);
    enCipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

    return new String(enCipher.doFinal(input).toString());
}
