try {
  Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

  SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

  // Here you need to accurately and correctly read your file into a byte
  // array. Either Google for a decent solution (there are many out there)
  // or use an existing implementation, such as Apache Commons commons-io.
  // Your existing effort is buggy and doesn't close its resources.      
  byte[] cipherText = FileUtils.readFileToByteArray(new File(savedScoresFileName));


  cipher.init(Cipher.DECRYPT_MODE, key);

  // Just one call to doFinal
  byte[] plainText = cipher.doFinal(cipherText);

  // Note: don't do this. If you create a string from a byte array,
  // PLEASE pass a charset otherwise your result is platform dependent.
  return new String(plainText);
} catch (Exception e) {
  e.printStackTrace();
}
