String presharedKey = getKey();
// f8250b0d5960444e4de6ecc3a78900bb941246a1dece7848fc72b90092ab3ecd0c1c8e36fddba501ef92e72c95b47e07f98f7fd9cb63da75c008a3201124ea5d

String deviceId = getDeviceId();
// 1605788742789230

SyncKey syncKey = generateSyncKey();
// 824C1EE9EF507B52EA28362C71BD4AD512A5F82ACFAE80DEF531F73AC124CA814BA30CE805A157D6ADB9EC04FC99AAE6FDC4238FCD76B87CE22BC2FE33B2E5C9

String concat = syncKey.hexString();
// 824C1EE9EF507B52EA28362C71BD4AD512A5F82ACFAE80DEF531F73AC124CA814BA30CE805A157D6ADB9EC04FC99AAE6FDC4238FCD76B87CE22BC2FE33B2E5C9

String ALGORITHM = "HmacSHA256";
String hash = null;
try {
    SecretKeySpec keySpec = new SecretKeySpec(
        presharedKey.getBytes(),
        ALGORITHM);
    Mac mac = Mac.getInstance(ALGORITHM);
    mac.init(keySpec);
    byte[] result = mac.doFinal(concat.getBytes());
    hash = Base64.encodeToString(result, Base64.DEFAULT);
    // FpDE2JLmCBr+/rW+n/jBHH13F8AV80sUM2fQAY2IpRs=
} catch (NoSuchAlgorithmException x) {
} catch (InvalidKeyException x) {
}

String encKey = presharedKey.substring(0, presharedKey.length() / 2);
// f8250b0d5960444e4de6ecc3a78900bb941246a1dece7848fc72b90092ab3ecd

int len = encKey.length();
byte[] encKeyBytes = new byte[len / 2];
for (int i = 0; i < len; i += 2) {
    encKeyBytes[i / 2] = (byte) ((Character.digit(encKey.charAt(i), 16) << 4)
            + Character.digit(encKey.charAt(i+1), 16));
}

String encryptedSyncKey = null;
try {
    byte[] iv = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
    AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
    SecretKeySpec encKeySpec = new SecretKeySpec(encKeyBytes, "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, encKeySpec, ivSpec);
    byte[] encryptedSyncKeyBytes = cipher.doFinal(syncKey.hexString().getBytes());
    encryptedSyncKey = Base64.encodeToString(encryptedSyncKeyBytes, Base64.DEFAULT);
    /*
        Yrl0/SuTUUTC6oJ8o4TCOy65EwO0JzoXfEi9kLq0AOlf6rH+nN7+BEc0s5uE7TIo1UlJb/DvR2Ca
        ACmQVXXhgpZUTB4sQ0eSo+t32lg0EEb9xKI5CZ4l9QO5raw0xBn7r/tfIdVm8AIFkN9QCcthS0DF
        KH3oWhpwNS+tfEuibLPgGqP/zGTozmido9U9lb4n
    */
} catch (InvalidAlgorithmParameterException e) {
} catch (NoSuchAlgorithmException e) {
} catch (NoSuchPaddingException e) {
} catch (InvalidKeyException e) {
} catch (IllegalBlockSizeException e) {
} catch (BadPaddingException e) {
}

sendStuffToWeb(encryptedSyncKey, deviceId, hash);
