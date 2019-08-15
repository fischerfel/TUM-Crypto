Cipher c = Cipher.getInstance("AES/ECB/NoPadding");
Key aesKey = new SecretKeySpec(key, "AES");
c.init(Cipher.ENCRYPT_MODE, aesKey);
for (long i = 0; i < rounds; ++i) {
     data = c.doFinal(data);
}
