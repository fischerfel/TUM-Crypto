    try {

         byte[] inputBytes = "abcdefghijklmnopqrst".getBytes("UTF-8");

         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");   
         SecretKey sk = getCastedActivity().getSecretKey();

         SecretKeySpec skeySpec = new SecretKeySpec(sk.getEncoded(), "AES");

         //Confirmed 20 bytes input 
         Log.i(TAG, "InputBytes length " + inputBytes.length);

         cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(MyEncrypt.generateIv()));
         byte[] encryptedBytes = encryptedBytes = cipher.doFinal(inputBytes);

         //As expected 32 byte encrypted size.
         Log.i(TAG, "OutputBytes length " + encryptedBytes.length);

     } catch (Exception e) {
         Log.e(TAG, "encryption exception", e);
     }
