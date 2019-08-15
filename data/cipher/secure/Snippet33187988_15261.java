public byte[] encryptPublic(PublicKey key, byte[] array){
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, key);
byte[] encrypted = cipher.doFinal(array);
return encrypted;
}
