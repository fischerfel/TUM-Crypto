    MessageDigest md = MessageDigest.getInstance("MD5");
seed_key = md.digest(new String(key).getBytes());

if (seed_key.length == 16) {
    byte[] tempkey = new byte[24];
    System.arraycopy(seed_key, 0, tempkey, 0, 16);
    System.arraycopy(seed_key, 0, tempkey, 16, 8);

    seed_key = tempkey;
}
SecretKeySpec keySpec = new SecretKeySpec(seed_key, "DESede");
nCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
byte[] IVector = new byte[] { 27, 9, 45, 27, 0, 72, (byte) 171, 54 };
IvParameterSpec iv = new IvParameterSpec(IVector);
nCipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

byte[] cipherbyte = nCipher.doFinal(data.getBytes());
encodeTxt = new String(Base64.encodeBase64(cipherbyte));
