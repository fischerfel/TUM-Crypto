byte[] inputByteArray = Base64.decode(val);
Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(byKey, "DESede"), new IvParameterSpec(iv, 0, 8));

byte[] decryptedBytes;
for (int i = 0, j = 0; i < inputByteArray.length; i++) {
    if ((decryptedBytes = c.update(inputByteArray, i, 1)) == null)
        continue;
    else {
        System.out.println(new String(decryptedBytes));
        j += decryptedBytes.length;
    }          
}
