private static final byte[] ENCRYPTION_KEY = Hex.decode("448D3F076D8304036A55A3D7E0055A78");
private static final byte[] PLAINTEXT = Hex.decode("1234567890ABCDEFFEDCBA0987654321");

public static void main(String[] args) throws Exception {
    SecretKey desABAKey = createDES_ABAKey(ENCRYPTION_KEY);
    Cipher desEDE = Cipher.getInstance("DESede/CBC/NoPadding");
    IvParameterSpec zeroIV = new IvParameterSpec(new byte[desEDE.getBlockSize()]);
    desEDE.init(Cipher.ENCRYPT_MODE, desABAKey, zeroIV);
    byte[] ciphertext = desEDE.doFinal(PLAINTEXT);
    System.out.println(Hex.toHexString(ciphertext));
}
