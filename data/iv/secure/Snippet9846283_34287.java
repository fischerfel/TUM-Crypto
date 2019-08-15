private static String decrypt(String inputStr, String keyStr, String ivStr) throws Exception {

    IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());
    SecretKeySpec key = new SecretKeySpec(keyStr.getBytes(), "DESede");
    inputStr = hexToString(inputStr, 2);

    Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, key, iv);
    byte[] decrypted = cipher.doFinal(inputStr.getBytes());

    return new String(decrypted);
}

private static String hexToString(String input, int groupLength) {
    StringBuilder sb = new StringBuilder(input.length() / groupLength);
    for (int i = 0; i < input.length() - groupLength + 1; i += groupLength) {
        String hex = input.substring(i, i + groupLength);
        sb.append((char) Integer.parseInt(hex, 16));
    }
    return sb.toString();
}

public static void main(String[] args) throws Exception {
    String decryptSignature = decrypt("c37551bb77f741d0bcdc16497b4f97b1", "123456781234567812345678", "12345678");
    System.out.println(decryptSignature);
}
