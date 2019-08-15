public boolean generateKey(String plainText, String password, String salt)
{
    try
    {
        iv = null;
        cipherText = null;
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWITHSHA256AND256BITAES-CBC-BC");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        /* Encrypt the message. */
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        try
        {
            iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        } catch (InvalidParameterSpecException e) {
            Log.i("InvalidParameterSpecException", "InvalidParameterSpecException: "+e.toString());
            e.printStackTrace();
            return false;
        }
        try {
            cipherText = cipher.doFinal(plainText.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Log.i("UnsupportedEncodingException", "UnsupportedEncodingException: "+e.toString());
            e.printStackTrace();
            return false;
        }
    } catch (InvalidKeyException e) {
        e.printStackTrace();
        return false;
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        Log.i("NoSuchAlgorithmException", "NoSuchAlgorithmException: "+e.toString());
        e.printStackTrace();
        return false;
    } catch (InvalidKeySpecException e) {
        Log.i("InvalidKeySpecException", "InvalidKeySpecException: "+e.toString());
        e.printStackTrace();
        return false;
    } catch (NoSuchPaddingException e) {
        Log.i("NoSuchPaddingException", "NoSuchPaddingException: "+e.toString());
        e.printStackTrace();
        return false;
    } catch (IllegalBlockSizeException e) {
        Log.i("IllegalBlockSizeException", "IllegalBlockSizeException: "+e.toString());
        e.printStackTrace();
        return false;
    } catch (BadPaddingException e) {
        Log.i("BadPaddingException", "BadPaddingException: "+e.toString());
        e.printStackTrace();
        return false;
    }
    return true;
}
