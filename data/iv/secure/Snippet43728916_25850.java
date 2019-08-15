public void decryptFile(InputStream inputStream, OutputStream outputStream)
        throws Exception {
    try {
        long totalread = 0L;
        int nread = 0;
        byte[] inbuf = new byte[128];


        mDecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        mInitVec = new byte[16];

        inputStream.read(mInitVec);
        mDecipher.init(2, loadKey(), new IvParameterSpec(mInitVec));


        CipherInputStream cin = new CipherInputStream(inputStream, mDecipher);

        while ((nread = cin.read(inbuf)) > 0) {
            totalread += nread;


            byte[] trimbuf = new byte[nread];

            for (int i = 0; i < nread; i++) {
                trimbuf[i] = inbuf[i];
            }


            outputStream.write(trimbuf);
        }

        outputStream.flush();
    } catch (Exception ex) {
        Logger.getLogger(FileEncryptor.class.getName()).log(Level.SEVERE, null, ex);
        throw ex;
    }
}


And here is the loadKey()

try
    {
      KeyStore keystore = KeyStore.getInstance("JCEKS");
      FileInputStream keystoreStream = new FileInputStream(keyFile);
      keystore.load(keystoreStream, storePassword.toCharArray());
      if (!keystore.containsAlias(keyAlias)) {
        throw new RuntimeException("Alias for key not found");
      }

      KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(keyPassword.toCharArray());

      KeyStore.SecretKeyEntry skEntry = (KeyStore.SecretKeyEntry)keystore.getEntry(keyAlias, protParam);
      SecretKey mySecretKey = skEntry.getSecretKey();
      byte[] enc = new byte['Ā'];
      enc = mySecretKey.getEncoded();
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error reading key from keystore. Please check if the keystore '" + keyFile + "' exists and is valid."); }
    SecretKey mySecretKey;
    FileInputStream keystoreStream; KeyStore keystore; return mySecretKey;
  }
