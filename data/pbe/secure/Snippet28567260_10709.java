PBEKeySpec pbeKeySpec = new PBEKeySpec(PASSWORD.toCharArray(), byteSalt, NB_ITER_RFC, SIZE_KEY);
byte[] key2 = PBEParametersGenerator.PKCS12PasswordToBytes(pbeKeySpec.getPassword());
SecretKey temp2 = factory.generateSecret(pbeKeySpec);
Cipher c2 = Cipher.getInstance(DES_EDE_PKCS5);
c2.init(Cipher.ENCRYPT_MODE, temp2, ivParam);
byte[] encrypted2 = c2.doFinal(texteAChiffrer.getBytes("UTF-8"));       
mdp = Base64.encodeToString(encrypted2, Base64.DEFAULT);
