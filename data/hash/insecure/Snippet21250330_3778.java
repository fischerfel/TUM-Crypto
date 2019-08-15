public void setkey() throws Exception {
    byte[] key1 = new String("abcd").getBytes("UTF-8"); // some logic will replace "abcd"
     MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
     key1 = messageDigest.digest(key1);
     key1 = Arrays.copyOf(key1,16);
     key = key1;
     //this key must be the same when encrypting and decrypting, right?
}

@Override
public String encryptField(Myclass myClass) throws Exception {

    Cipher cipher = Cipher.getInstance("AES");
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    String encryptedField = Base64.encodeBase64String(cipher.doFinal(myClass.myField.getBytes("UTF-8")));
    myClass.setMyField(encryptedField);
    save(myClass);

    return encryptedField;
    //this looks OK, and gives me 24 character string.
}


@Override
public String decryptVoucher(Myclass myClass) throws Exception {

    String skey = key.toString();
    Cipher cipher = Cipher.getInstance("AES");
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
    byte[] decryptedField = cipher.doFinal(Base64.decodeBase64(yClass.myField.getBytes("UTF-8")));
    // decryptedField.toString() is not as same as original data...
    return decryptedField.toString();
}
