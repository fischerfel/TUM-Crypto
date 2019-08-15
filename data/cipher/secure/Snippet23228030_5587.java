    String f= "engineer hussein mawzi hello world";

    KeyPairGenerator kpg;
    KeyPair kp;
    PublicKey publicKey;
    PrivateKey privateKey;
    byte [] encryptedBytes1,decryptedBytes1;

    Cipher cipher,cipher1;   

          String encrypted1,decrypted1;  

    kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(512);
        kp = kpg.genKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();

          System.out.println("th"+publicKey);
             cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);




                    encryptedBytes1= cipher.doFinal(f.getBytes());

 encrypted1 = new String(encryptedBytes1);


// here is the message that i wnt to encrypte and send !!!!
                    System.out.println("here is mu test app"+encrypted1);


             System.out.println("EEncrypted?????"+encrypted1.length());
      cipher1 = Cipher.getInstance("RSA");
    cipher1.init(Cipher.DECRYPT_MODE, privateKey);

// here i want to recover the byte array of the message i extract it and decrypte it 
 byte[] by = encrypted1.getBytes();
    System.out.println(by.length);
    decryptedBytes1 = cipher1.doFinal(by);
    decrypted1 = new String(decryptedBytes1);
    System.out.println("DDecrypted?????" + decrypted1);

   System.out.println("DDecrypted?????" + decrypted1.length());
         }
