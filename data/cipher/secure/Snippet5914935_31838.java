SecretKeyFactory sf = SecretKeyFactory.getInstance("PBEWITHSHAAND256BITAES-CBC-BC");
KeySpec ks = new PBEKeySpec(masterPassword.toCharArray(),k1,1320,256);
secKey = sf.generateSecret(ks);
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, secKey, generateIV(cipher));
