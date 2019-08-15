String encryptKey(String input)
{
    byte[] inBytes=input.getBytes();
    String finalString=null;
    try {
        Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] keyBytes=md.digest((KeyPart1+KeyPart2).getBytes());
        keyBytes = Arrays.copyOf(keyBytes, 16);
        SecretKey key= new SecretKeySpec(keyBytes,"AES");
        IvParameterSpec ivSpec = new IvParameterSpec(new byte[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        cipher.init(Cipher.ENCRYPT_MODE,key,ivSpec);
        byte[] outBytes = new byte[cipher.getOutputSize(inBytes.length)];
        //cipher.update(encrypted, 0, encrypted.length, decrypted, 0);
        outBytes=cipher.doFinal(inBytes);
        finalString=new String(Base64.encode(outBytes,0));
        Log.v(TAG,"Encrypted="+finalString);

    } catch (NoSuchAlgorithmException e) {
        Log.e(TAG,"No Such Algorithm",e);
    } catch (NoSuchPaddingException e) {
        Log.e(TAG,"No Such Padding",e);
    } catch (InvalidKeyException e) {
        Log.e(TAG,"Invalid Key",e);
    } catch (InvalidAlgorithmParameterException e) {
        Log.e(TAG,"Invalid Algorithm Parameter",e);
    } catch (IllegalBlockSizeException e) {
    } catch (BadPaddingException e) {}
    return finalString;
}

String decryptKey(String base64Text)
{
    byte[] encrypted=Base64.decode(base64Text,0);
    //encrypted=base64Text.getBytes();
    String decryptedString=null;
    try {
        Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] keyBytes=md.digest((KeyPart1+KeyPart2).getBytes());
        keyBytes = Arrays.copyOf(keyBytes, 16);
        SecretKey key= new SecretKeySpec(keyBytes,"AES");
        IvParameterSpec ivSpec = new IvParameterSpec(new byte[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        cipher.init(Cipher.DECRYPT_MODE,key,ivSpec);
        byte[] decrypted = new byte[cipher.getOutputSize(encrypted.length)];
        //cipher.update(encrypted, 0, encrypted.length, decrypted, 0);
        decrypted=cipher.doFinal(encrypted);
        decryptedString=new String(decrypted);
    } catch (NoSuchAlgorithmException e) {
        logStackTrace(e);
    } catch (NoSuchPaddingException e) {
        logStackTrace(e);
    } catch (InvalidKeyException e) {
        logStackTrace(e);
    } catch (InvalidAlgorithmParameterException e) {
        logStackTrace(e);
    } catch (IllegalBlockSizeException e) {
        logStackTrace(e);
    } catch (BadPaddingException e) {
        logStackTrace(e);
    }
    return decryptedString;
}
