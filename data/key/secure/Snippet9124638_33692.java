public static final int AES_Key_Size = 128;

Cipher pkCipher, aesCipher;
byte[] aesKey;
SecretKeySpec aeskeySpec;

public static void main(String[] args) throws Exception {
    //MyEncrypt.createRSAKeys(); //call to generated RSA keys
    MyEncrypt secure = new MyEncrypt();
    // to encrypt a file
    secure.makeKey();
    secure.saveKey(new File("keys/ase.key"), "keys/public.key");
    secure.encrypt(new File("test/sample.pdf"), new File("test/encrypt_sample.pdf"));

    // to decrypt it again
    secure.loadKey(new File("keys/ase.key"), "keys/private.key");
    secure.decrypt(new File("test/encrypt_sample.pdf"), new File("test/decrypted_sample.pdf"));
}

/**
* Constructor: creates ciphers
*/
public MyEncrypt() throws GeneralSecurityException {
    // create RSA public key cipher
    pkCipher = Cipher.getInstance("RSA");
    // create AES shared key cipher
    aesCipher = Cipher.getInstance("AES");
}

public static void createRSAKeys() throws Exception {
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(512);
    KeyPair kp = kpg.genKeyPair();
    RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();
    KeyFactory fact = KeyFactory.getInstance("RSA");        
    RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
    RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);
    saveToFile("keys/public.key", pub.getModulus(), pub.getPublicExponent());
    saveToFile("keys/private.key", priv.getModulus(), priv.getPrivateExponent());
    System.out.println("RSA public & private keys are generated");
}

private static void saveToFile(String fileName, BigInteger mod, BigInteger exp) throws IOException {
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

/**
* Creates a new AES key
*/
public void makeKey() throws NoSuchAlgorithmException {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    kgen.init(AES_Key_Size);
    SecretKey key = kgen.generateKey();
    aesKey = key.getEncoded();
    aeskeySpec = new SecretKeySpec(aesKey, "AES");
}

/**
* Encrypts the AES key to a file using an RSA public key
*/
public void saveKey(File out, String publicKeyFile) throws IOException, GeneralSecurityException {
        try {
            // read public key to be used to encrypt the AES key
            byte[] encodedKey = new byte[(int)publicKeyFile.length()];
            new FileInputStream(publicKeyFile).read(encodedKey);

            // create public key
            //X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
            //KeyFactory kf = KeyFactory.getInstance("RSA");
            //PublicKey pk = kf.generatePublic(publicKeySpec);
            PublicKey pk = this.readPublicKeyFromFile(publicKeyFile);

            // write AES key
            pkCipher.init(Cipher.ENCRYPT_MODE, pk);
            CipherOutputStream os = new CipherOutputStream(new FileOutputStream(out), pkCipher);
            os.write(aesKey);
            os.close();    
        } catch (Exception e) {
            //throw new Exception("Saving key exception", e);    
        }

}

private PublicKey readPublicKeyFromFile(String keyFileName) throws Exception {
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

private PrivateKey readPrivateKeyFromFile(String keyFileName) throws Exception {
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

/**
* Decrypts an AES key from a file using an RSA private key
*/
public void loadKey(File in, String privateKeyFile) throws GeneralSecurityException, IOException {
            try {
                // read private key to be used to decrypt the AES key
               byte[] encodedKey = new byte[(int)privateKeyFile.length()];
               new FileInputStream(privateKeyFile).read(encodedKey);

               // create private key
               //PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedKey);
               //KeyFactory kf = KeyFactory.getInstance("RSA");
               //PrivateKey pk = kf.generatePrivate(privateKeySpec);
               PrivateKey pk = this.readPrivateKeyFromFile(privateKeyFile);

               // read AES key
               pkCipher.init(Cipher.DECRYPT_MODE, pk);
               aesKey = new byte[AES_Key_Size/8];
               CipherInputStream is = new CipherInputStream(new FileInputStream(in), pkCipher);
               is.read(aesKey);
               aeskeySpec = new SecretKeySpec(aesKey, "AES");     
            } catch (Exception e) {

            }

   }

/**
 * Encrypts and then copies the contents of a given file.
 */
public void encrypt(File in, File out) throws IOException, InvalidKeyException {
    aesCipher.init(Cipher.ENCRYPT_MODE, aeskeySpec);

    FileInputStream is = new FileInputStream(in);
    CipherOutputStream os = new CipherOutputStream(new FileOutputStream(out), aesCipher);

    copy(is, os);

    os.close();
}

/**
 * Decrypts and then copies the contents of a given file.
 */
public void decrypt(File in, File out) throws IOException, InvalidKeyException {
    aesCipher.init(Cipher.DECRYPT_MODE, aeskeySpec);

    CipherInputStream is = new CipherInputStream(new FileInputStream(in), aesCipher);
    FileOutputStream os = new FileOutputStream(out);

    copy(is, os);

    is.close();
    os.close();
}

/**
 * Copies a stream.
 */
private void copy(InputStream is, OutputStream os) throws IOException {
    int i;
    byte[] b = new byte[1024];
    while((i=is.read(b))!=-1) {
        os.write(b, 0, i);
    }
}
