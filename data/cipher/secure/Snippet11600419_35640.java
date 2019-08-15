public static byte[] encrypt(byte[] Data) throws Exception {
    Log.i("Debug", "initial data is" + java.util.Arrays.toString(Data));

    Key key = generateKey();
    Cipher c = Cipher.getInstance(ALGO);
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encVal = c.doFinal(Data);

    Log.i("Debug", "encrypted data is" + java.util.Arrays.toString(encVal));
     ;
    return Base64.encode(encVal,0);
   }
