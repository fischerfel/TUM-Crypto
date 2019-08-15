KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), byteSalt, NB_ITER_RFC, SIZE_KEY);
SecretKey temp = factory.generateSecret(spec);
Cipher c = Cipher.getInstance(DES_EDE_PKCS5);
IvParameterSpec ivParam = new IvParameterSpec(bytesIv);
c.init(Cipher.ENCRYPT_MODE, temp, ivParam);
byte[] encrypted = c.doFinal(texteAChiffrer.getBytes("UTF-8"));
mdp = Base64.encodeToString(encrypted, Base64.DEFAULT);
