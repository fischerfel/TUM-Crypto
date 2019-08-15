public boolean licenseValid() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
    java.io.File file = new java.io.File(Environment.getExternalStorageDirectory().toString() ,
            "/folder/file.lic");
    byte[] fileBArray = new byte[(int)file.length()];
    FileInputStream fis = new FileInputStream(file);

    // Read in the bytes
    int offset = 0;
    int numRead = 0;
    while (offset < fileBArray.length
           && (numRead=fis.read(fileBArray, offset, fileBArray.length-offset)) >= 0) {
        offset += numRead;
    }

    // Ensure all the bytes have been read in
    if (offset < fileBArray.length) {
        throw new IOException("Could not completely read file "+file.getName());
    }

    fis.close();

    // Decrypt the ciphertext using the public key
PublicKey pubKey = readKeyFromFile();
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.DECRYPT_MODE, pubKey);
byte[] newPlainText = cipher.doFinal(fileBArray);

    // THE FOLLOWING TOAST PRINTS MANY <?> AND THAN THE DECRYPTED MESSAGE. THE TOTAL NUMBER OF CHARACTERS IS 255, EVEN IF I CHANGE ENCRYPTED TEXT!
toast(String.valueOf(cipher.doFinal(fileBArray).length));

    if (new String(newPlainText, "utf-8").compareTo("Hello World!") == 0)
        return true;
    else
        return false;
}

PublicKey readKeyFromFile() throws IOException {
    Resources myResources = getResources();
    //public key filename "pub.lic"
    InputStream is = myResources.openRawResource(R.raw.pub);
ObjectInputStream oin =
    new ObjectInputStream(new BufferedInputStream(is));

try {
        BigInteger m = (BigInteger) oin.readObject();
        BigInteger e = (BigInteger) oin.readObject();
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    PublicKey pubKey = fact.generatePublic(keySpec);
    return pubKey;
  } catch (Exception e) {
    throw new RuntimeException("Spurious serialisation error", e);
  } finally {
    oin.close();
   }
}
