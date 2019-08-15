public static void main(String[] args) throws Exception {
    String iv = "0102030405060708";
    String key = "1882051051AgVfZUKJLInUbWvOPsAP6LM6nBwLn14140722186";

    byte[] aaa = AES_cbc_decrypt("hv208Otx0FZL32GUuErHDLlZzC3zVEGRt56f8lviQpk=", key, iv);
    System.out.println(new String(aaa));
}

private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

public static byte[] AES_cbc_decrypt(String content,String key,String iv) throws Exception 
{
    byte[] contentBytes = Base64.decode(content);
    byte[] keyBytes = key.substring(0, 16).getBytes();
    byte[] ivBytes = iv.getBytes();

    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(ivBytes));
    byte[] decbbdt = cipher.doFinal(contentBytes);
    return decbbdt;
}
