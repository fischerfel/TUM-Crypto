class KeyWithExpiration {
    private PublicKey publicKey;
    private Date expirationDate;
}

public static void serializeEncrypted(File file, Serializable instance) {
   // With these lines, I hope to expose some of the craft that is needed to work with the API 
   PBEKeySpec keySpecObj = new PBEKeySpec(PASSWORD, SALT, ITERATIONS);
   Cipher ecipherObj = Cipher.getInstance(keyObj.getAlgorithm());
   SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
   SecretKey keyObj = secretKeyFactory.generateSecret(keySpecObj);

   SealedObject sealedObject = new SealedObject(instance, ecipherObj);

   ObjectOutputStream objOutputStream = new ObjectOutputStream(new FileOutputStream(file));
   objOutputStream.writeObject(sealedObject);
   objOutputStream.close();
}

// Generate a new KeyWithExpiration 
KeyWithExpiration key = new KeyWithExpiration(keyPair, DateUtil.future().days(365));
serializeEncrypted(new File(".key"), key);
