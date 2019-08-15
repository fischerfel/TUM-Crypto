static final String HEXES = "0123456789ABCDEF"; 
byte[] buf = new byte[1024];      

public void saveToFile(String fileName, BigInteger mod, BigInteger exp) throws IOException {
    ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
      try {
            oout.writeObject(mod);
            oout.writeObject(exp);
      } catch (Exception e) {
      throw new IOException("Unexpected error", e);
      } finally {
        oout.close();
      }
}

public static void main(String[] args) throws Exception {       
            MyEncrypt myEncrypt = new MyEncrypt();
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(2048);
    KeyPair kp = kpg.genKeyPair();
    RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();
    KeyFactory fact = KeyFactory.getInstance("RSA");        
    RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
    RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);

    myEncrypt.saveToFile("public.key", pub.getModulus(), pub.getPublicExponent());
    myEncrypt.saveToFile("private.key", priv.getModulus(), priv.getPrivateExponent());
    String encString = myEncrypt.rsaEncrypt("pritesh");
    System.out.println("encrypted : " + encString);
    String decString = myEncrypt.rsaDecrypt(encString);
    System.out.println("decrypted : " + decString);

    String main_file_path = "resume.doc";                
    String main_encrypt_file_path = "encrypt.doc";
    String main_decrypt_file_path = "decrypt.doc";

    myEncrypt.rsaEncrypt(new FileInputStream(main_file_path),new FileOutputStream(main_encrypt_file_path));
            // Decrypt
    myEncrypt.rsaDecrypt(new FileInputStream(main_encrypt_file_path),new FileOutputStream(main_decrypt_file_path));
}   

PublicKey readKeyFromFile(String keyFileName) throws Exception {
  InputStream in = new FileInputStream(keyFileName);
  ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
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

PrivateKey readPrivateKeyFromFile(String keyFileName) throws Exception {
  InputStream in = new FileInputStream(keyFileName);
  ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
  try {
    BigInteger m = (BigInteger) oin.readObject();
    BigInteger e = (BigInteger) oin.readObject();
    RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    PrivateKey pubKey = fact.generatePrivate(keySpec);
    return pubKey;
  } catch (Exception e) {
    throw new RuntimeException("Spurious serialisation error", e);
  } finally {
    oin.close();
  }
}

public String rsaEncrypt(String plaintext) throws Exception {      
  PublicKey pubKey = this.readKeyFromFile("public.key");
  Cipher cipher = Cipher.getInstance("RSA");
  cipher.init(Cipher.ENCRYPT_MODE, pubKey);
  byte[] ciphertext = cipher.doFinal(plaintext.getBytes("UTF-8"));
  return this.byteToHex(ciphertext);
}

public void rsaEncrypt(InputStream in, OutputStream out) throws Exception {
    try {                    
        PublicKey pubKey = this.readKeyFromFile("public.key");
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey); 
        // Bytes written to out will be encrypted
        out = new CipherOutputStream(out, cipher);

        // Read in the cleartext bytes and write to out to encrypt
        int numRead = 0;
        while ((numRead = in.read(buf)) >= 0){
            out.write(buf, 0, numRead);
        }
        out.close();
    }
    catch (java.io.IOException e){
        e.printStackTrace();
    }
}

public void rsaDecrypt(InputStream in, OutputStream out) throws Exception {
    try {                 
        PrivateKey pubKey = this.readPrivateKeyFromFile("private.key");
        Cipher dcipher = Cipher.getInstance("RSA");
        dcipher.init(Cipher.DECRYPT_MODE, pubKey);
        // Bytes read from in will be decrypted
        in = new CipherInputStream(in, dcipher);

        // Read in the decrypted bytes and write the cleartext to out
        int numRead = 0;
        while ((numRead = in.read(buf)) >= 0) {
            out.write(buf, 0, numRead);
        }
        out.close();
    } catch (java.io.IOException e) {
         e.printStackTrace();
    }
}

public String rsaDecrypt(String hexCipherText) throws Exception {      
  PrivateKey pubKey = this.readPrivateKeyFromFile("private.key");
  Cipher cipher = Cipher.getInstance("RSA");
  cipher.init(Cipher.DECRYPT_MODE, pubKey);      
  String plaintext = new String(cipher.doFinal(this.hexToByte(hexCipherText)), "UTF-8");
  return plaintext;
}

public static String byteToHex( byte [] raw ) {
    if ( raw == null ) {
      return null;
    }
    final StringBuilder hex = new StringBuilder( 2 * raw.length );
    for ( final byte b : raw ) {
      hex.append(HEXES.charAt((b & 0xF0) >> 4))
         .append(HEXES.charAt((b & 0x0F)));
    }
    return hex.toString();
}

public static byte[] hexToByte( String hexString){
    int len = hexString.length();
    byte[] ba = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        ba[i/2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i+1), 16));
    }
    return ba;
}
