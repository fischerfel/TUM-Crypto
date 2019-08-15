function encrypt(String pin){ 
  String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiOnM5t6w2ZD6dpA4/MzSTAOt0IYpnsmGSAIfIVgGntI+fI4wbvUvMIhaLN3fHrjyuNGFdYw+yuoXYkapajt6VTZJniaatSiq6bwQ7R0UAop6haFSAwjnmReqexJvcKyqUsTfcfFypPpsYRewh/48/jmc/6ND+ugxDd52prkPUrbj+nnO0z3DBoUCpgDMRvW2hWXv6kZ654gp+wIAQnxbdwRMy6FZbrHjkA3tc6U0CHK+KjxAfzWAK+yI+ofskM4qk50J7y9hUZ7lLikqWZWKiqh8xiDk1kgu+FIjVh+fylKpa3gWmPPn0fSpBJjuenc1OQVmZ718a3388DjzFlYOLwIDAQAB";
  byte[] sigBytes2 = Base64.decode(key, Base64.DEFAULT);
  Log.d("WS", "new key is: " + key);
  try {
    PublicKey publicKey = KeyFactory.getInstance("RSA")
        .generatePublic(new X509EncodedKeySpec(sigBytes2));
    encryptedBase64PIN = encode(publicKey, pin);
    Log.d("WSA", "encoded key is: " + encryptedBase64PIN);
    // getSecToken();
  } catch (Exception e) {
    e.printStackTrace();
  }
}

public static String encode(Key publicKey, String data)
    throws NoSuchAlgorithmException, NoSuchPaddingException,
    InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

  byte[] byteData = data.getBytes(); // convert string to byte array

  Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");       
  cipher.init(Cipher.ENCRYPT_MODE, publicKey);      
  byte[] encryptedByteData = cipher.doFinal(byteData);

  String s = Base64.encodeToString(encryptedByteData, Base64.NO_WRAP);
  return s; // convert encrypted byte array to string and return it
}
