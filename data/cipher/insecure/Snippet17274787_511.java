    // generate a key pair
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(512, Utils.createFixedRandom());

    KeyPair pair = kpg.generateKeyPair();

    for (Object obj : java.security.Security.getAlgorithms("Cipher")) {
      System.out.println(obj);
    }

    // wrapping step
    char[]              password = "hello".toCharArray();
    byte[]              salt = new byte[20];
    int                 iCount = 100;
    String              pbeAlgorithm = "PBEWITHMD5ANDTRIPLEDES";
    PBEKeySpec          pbeKeySpec = new PBEKeySpec(password, salt, iCount);
    SecretKeyFactory    secretKeyFact = SecretKeyFactory.getInstance(pbeAlgorithm/*, "BC"*/);
    Cipher              cipher = Cipher.getInstance(pbeAlgorithm/*, "BC"*/);

    cipher.init(Cipher.WRAP_MODE, secretKeyFact.generateSecret(pbeKeySpec));

    byte[]             wrappedKey = cipher.wrap(pair.getPrivate());

//    System.out.println(ASN1Dump.dumpAsString(new ASN1InputStream(cipher.getParameters().getEncoded()).readObject()));

    // create carrier   
    EncryptedPrivateKeyInfo pInfo = new EncryptedPrivateKeyInfo(cipher.getParameters(), wrappedKey);

    // unwrapping step - note we only use the password
    pbeKeySpec = new PBEKeySpec(password);

    cipher = Cipher.getInstance(pInfo.getAlgName()/*, "BC"*/);

    cipher.init(Cipher.DECRYPT_MODE, secretKeyFact.generateSecret(pbeKeySpec), pInfo.getAlgParameters());

    PKCS8EncodedKeySpec pkcs8Spec = pInfo.getKeySpec(cipher);
    KeyFactory          keyFact = KeyFactory.getInstance("RSA"/*, "BC"*/);
    PrivateKey          privKey = keyFact.generatePrivate(pkcs8Spec);


    ASN1InputStream     aIn = new ASN1InputStream(pkcs8Spec.getEncoded());
    PrivateKeyInfo      info = PrivateKeyInfo.getInstance(aIn.readObject());

    System.out.println(ASN1Dump.dumpAsString(info));        
    System.out.println(ASN1Dump.dumpAsString(info.getPrivateKey()));

    if (privKey.equals(pair.getPrivate()))
    {
        System.out.println("key recovery successful");
    }
    else
    {
        System.out.println("key recovery failed");
    }
