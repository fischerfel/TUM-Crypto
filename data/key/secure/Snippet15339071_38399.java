private static Key generateKey() throws Exception 
{
    byte[] keyValue;
    keyValue = new BASE64Decoder().decodeBuffer(passKey);
    Key key = new SecretKeySpec(keyValue, ALGORITHM);

    return key;
}
