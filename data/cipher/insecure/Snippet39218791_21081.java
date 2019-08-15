   public static String base64Encode(byte[] bytes)
    {
        return new BASE64Encoder().encode(bytes);
    }

   public static byte[] base64Decode(String property) throws IOException
    {
        return new BASE64Decoder().decodeBuffer(property);
    }


    public static String encrypt(String mystring) throws GeneralSecurityException, UnsupportedEncodingException
       {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(mystring.toCharArray()));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return base64Encode(pbeCipher.doFinal(mystring.getBytes("UTF-8")));
        }

    public static String decrypt(String estring) throws GeneralSecurityException, IOException
        {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(estring.toCharArray()));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return new String(pbeCipher.doFinal(base64Decode(estring)), "UTF-8");
        }
