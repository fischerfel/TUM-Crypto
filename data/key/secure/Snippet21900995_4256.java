public final class Operation {

    public static Key getKey(byte[] arr)
    {
        Key key = null;
    KeyGenerator keyGen;
        Security.addProvider(new BouncyCastleProvider());
    try
    {
            keyGen = KeyGenerator.getInstance("AES", "BC");
            if(arr == null)
                keyGen.init(192);
            else
                keyGen.init(new SecureRandom(arr));
            key = keyGen.generateKey();
    }
    catch (NoSuchAlgorithmException e)
    {
            System.err.println(e);  
    } catch (NoSuchProviderException e) {
            System.err.println(e);
        }

        return key;
    }

    public static String getKeyAsString(Key key)
    {
        return  new String(Base64.encode(key.getEncoded()));
    }

    public static Key getKeyFromString(String string)
    {
        return new SecretKeySpec(Base64.decode(string.getBytes()), "AES");
    }

}
