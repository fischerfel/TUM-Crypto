class AESCodec extends ... {

public static final String IV_FILE="C:/keystore/iv-file"    
private static final String RANDOM_ALGORITHM = "SHA1PRNG";
private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";


static encode = { String target ->
    def cipher = getCipher(Cipher.ENCRYPT_MODE)
    return cipher.doFinal(target.bytes).encodeBase64()
}

static decode = { String target ->
    //println "target:"+target
    println "enter decode with target:"+target
    def cipher = getCipher(Cipher.DECRYPT_MODE)
    println "decode cipher:"+cipher
    return new String(cipher.doFinal(target.decodeBase64()))//<============== failing here when running decode independently
}

private String secretPassword

private String getSecretKey() {
    return "secret12"
}

private static getPassword() { new AESCodec().getSecretKey().getChars() }

private static byte[] iv

static SecretKey secretKey
/*
* Get key from Keystore where it is stored after creation
*/
private static SecretKey createKey(Integer mode) {
    println "createKey() mode values 1=encrypt,2=decrypt mode:"+mode
    if (secretKey == null) {
        if (mode== Cipher.ENCRYPT_MODE) {
            println "inside encrypt mode in createKey()"
            byte[] salt = "DYKSalt".getBytes()
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            KeySpec spec = new PBEKeySpec(getPassword(), salt, 65536, 256)
            SecretKey tmp = factory.generateSecret(spec)
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES")
            secretKey = secret
            println 'inside encrypt secretKey:'+secretKey
            //store it in keystore
            KeyStore ks = KeyStore.getInstance("JCEKS")
            ks.load(null, null);
            KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(secretKey)
            //key alias and passwd
            ks.setEntry("alias", skEntry,
              new KeyStore.PasswordProtection("fggd".toCharArray()))
            FileOutputStream fos = null
            try {
                fos = new FileOutputStream(AsymmetricCipher.KEYSTORE_DIR+"aes.keystore")
                //keystore passwd
                ks.store(fos, "fggd".toCharArray())
            } finally {

                fos?.close()

            }


        } else if (mode== Cipher.DECRYPT_MODE) {


            InputStream inputStream = getKeystoreAsStream(AsymmetricCipher.KEYSTORE_DIR+"aes.keystore")

            BufferedInputStream fis = null
            try {

                fis = new BufferedInputStream(inputStream)
                println "fis:"+fis
                //keystore passwd
                KeyStore ks = KeyStore.getInstance("JCEKS")
                //get key store
                ks.load(fis, "fggd".toCharArray())

                //get key from keystore
                Entry entry = ks.getEntry("alias", new KeyStore.PasswordProtection("fggd".toCharArray()))
                KeyStore.SecretKeyEntry secretKeystoreEntry = (KeyStore.SecretKeyEntry)entry
                secretKey = secretKeystoreEntry.getSecretKey()
                println " returned secretKey from decrypt mode"
            } finally {

                fis?.close()

            }

        }

    } else {


    }

    return secretKey
}


private SecretKey getKey() {

    return secretKey
}

private static getCipher(mode) {


  SecretKey secret = createKey(mode)
  Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
  println "secret:"+secret.getEncoded()
  println "cipher:"+cipher

  if (mode == Cipher.DECRYPT_MODE) {
      //get iv
      iv = readIvFromFile(IV_FILE)
      println "decrypt iv created from file:"+iv
      IvParameterSpec ivspec = new IvParameterSpec(iv);
      println "ivspec:"+ivspec
      cipher.init(mode, secret,ivspec)
      //cipher.init(mode, secret)
  } else {

      //save that to IV_FILE
      byte[] iv = generateIv();
      println "encrypt iv created:"+iv
      IvParameterSpec ivspec = new IvParameterSpec(iv);
      println "ivspec:"+ivspec
      cipher.init(mode, secret,ivspec)
      //iv = cipher.getIV()
      AlgorithmParameters params = cipher.getParameters()
      iv = params.getParameterSpec(IvParameterSpec.class).getIV()
      saveToFile(IV_FILE,iv)
  }


  return cipher

}

private static byte[] generateIv() throws NoSuchAlgorithmException {
    SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
    byte[] iv = new byte[16];
    random.nextBytes(iv);
    return iv;
}


public static void saveToFile(String fileName,
    byte[] iv) throws IOException {
    println "saveToFile() fileName:"+fileName
    FileOutputStream oout = 
      new FileOutputStream(fileName);

    try {
      oout.write(iv);

    } catch (Exception e) {
      throw new IOException("Unexpected error", e);
    } finally {

        oout?.flush()
        oout?.close()
    }
}

private static byte[] readIvFromFile(String keyFileName)
throws IOException {
    println "readIvFromFile() keyFileName:"+keyFileName

    InputStream inputStream = AsymmetricCipher.getFile(keyFileName)

    try {

      iv = IOUtils.toByteArray(inputStream);

      println "read iv:"+iv
      return iv;
    } catch (Exception e) {
        throw new RuntimeException("Spurious serialisation error ", e);
    } finally {
        inputStream = null
        //oin?.close();
    }

}


static void main(String[] args) {

  String message="This is just an example";

  if(args) {

      def encryptedValue =  encode(args[0])
      println "encryptedValue:"+encryptedValue //byte[]

      String encryptedValue1=(String)(encryptedValue)
      println "encryptedValue1:"+encryptedValue1



      def decryptedValue = decode(encryptedValue1)
      println "decryptedValue:"+decryptedValue
      def decryptedValueStr = (String)decryptedValue


  }

//
}
