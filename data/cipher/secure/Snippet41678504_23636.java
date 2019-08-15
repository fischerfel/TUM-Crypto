public static String encrypt(byte[] key, byte[] initVector, String value) {
    try {
        IvParameterSpec iv = new IvParameterSpec(initVector);
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(completeBlocks(value));
        return Base64.encodeBase64String(encrypted);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
        System.out.println("Error: " + ex);
    }

    return null;
}

/**
 * Completes 16 lenght blocks
 *
 * @param message
 *
 *
 */
static byte[] completeBlocks(String message) {
    try {

        int bytesLenght = message.getBytes("UTF-8").length;
        if (bytesLenght % 16 != 0) {
            byte[] newArray = new byte[bytesLenght + (16 - (bytesLenght % 16))];
            System.arraycopy(message.getBytes(), 0, newArray, 0, bytesLenght);
            return newArray;
        }

        return message.getBytes("UTF-8");

    } catch (UnsupportedEncodingException ex) {
        System.out.println("" + ex);
    }
    return null;
}

public static void main(String[] args) {

    String key = "253D3FB468A0E24677C28A624BE0F939";
    String strToEncrypt = "My Secret text";
    final byte[] initVector = new byte[16];
    String resultado = encrypt(new BigInteger(key, 16).toByteArray(), initVector, strToEncrypt.trim());
    System.out.println("ENCRYPTED:");
    System.out.println(resultado);
}
