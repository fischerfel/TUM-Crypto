public static String encode(String chave, final String value)
        throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
        InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

    final Key keySpec = new SecretKeySpec(chave.getBytes(), "AES");

    final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    System.out.println(Hex.encodeHex(new byte[16]));


    cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(new byte[16]));

    final byte[] message = cipher.doFinal(value.getBytes());

    return new String(Hex.encodeHex(message));
}
