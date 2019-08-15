String decode(String base64Text, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    byte[] inputArr = Base64.getUrlDecoder().decode(base64Text);
    SecretKeySpec skSpec = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
    int blockSize = cipher.getBlockSize();
    IvParameterSpec iv = new IvParameterSpec(Arrays.copyOf(inputArr, blockSize));
    byte[] dataToDecrypt = Arrays.copyOfRange(inputArr, blockSize, inputArr.length);
    cipher.init(Cipher.DECRYPT_MODE, skSpec, iv);
    byte[] result = cipher.doFinal(dataToDecrypt);
    return new String(result, StandardCharsets.UTF_8);
}
