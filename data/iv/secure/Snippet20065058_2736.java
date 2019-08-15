  AlgorithmID blowfish=new AlgorithmID("1.3.6.1.4.1.3029.1.2","BLOWFISH_CBC","Blowfish/CBC/PKCS5Padding");
  byte[] iv = new byte[8];
    random.nextBytes(iv);
  try{
        KeyGenerator keyGen = KeyGenerator.getInstance("Blowfish", "IAIK");
        secretKey = keyGen.generateKey();

        AlgorithmParameterSpec params = new IvParameterSpec(iv);

        keyGen.init(128);

        secretKey = keyGen.generateKey();
        iaik.pkcs.pkcs7.EncryptedContentInfoStream eci = new iaik.pkcs.pkcs7.EncryptedContentInfoStream(ObjectID.pkcs7_data, is);
        eci.setupCipher(blowfish, secretKey, params);
        return eci;
  }catch(Exception e){

  }
