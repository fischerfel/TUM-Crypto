cipherAlgorythm = "PBEWithMD5AndDES";                           
cipherTransformation = "PBEWithMD5AndDES/CBC/PKCS5Padding";     
PBEParameterSpec ps = new javax.crypto.spec.PBEParameterSpec(salt, iterations);
SecretKeyFactory kf = SecretKeyFactory.getInstance(cipherAlgorythm);
SecretKey key = kf.generateSecret(new javax.crypto.spec.PBEKeySpec(password.toCharArray()));
Cipher encryptCipher = Cipher.getInstance(cipherTransformation);   
encryptCipher.init(Cipher.ENCRYPT_MODE, key, ps);
byte[] output = encryptCipher.doFinal("This is a test string".getBytes("UTF-8"));
