Cipher c = Cipher.getInstance("ECIES","SC");
c.init(Cipher.DECRYPT_MODE,privateKeyFromFile);
encodeBytes = hexStringToByteArray(my_encrypted_string);
decodeBytes = c.doFinal(encodeBytes);
String deCrypt = new String(decodeBytes,"UTF-8");

//////////////////And the function//////////////

public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                + Character.digit(s.charAt(i+1), 16));
    }
    return data;
}
