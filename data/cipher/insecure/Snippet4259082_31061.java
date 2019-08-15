  final byte[] rawKey = hexStringToByteArray("9c361fec3ac1ebe7b540487c9c25e24e");
  final SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
  // Instantiate the cipher
  final Cipher cipher = Cipher.getInstance("AES");
  cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

  final byte[] encrypted = cipher.doFinal(plainText.getBytes());
