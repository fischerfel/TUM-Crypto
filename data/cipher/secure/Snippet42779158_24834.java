public static String encrypt(String paramString)throws Exception
{
    return Base64Utils.encode(RSAUtils.encryptByPrivateKey(paramString.getBytes(), "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIrrUGxh+yvNNI1c9hUg1rH+EtipI0nPk3zRm2Cj4mLDWLJ6DaTzdJTXTF3BYZaancWeG3QtBL+fITUi72InwBP7zaNG8uv/guwuhWT6V/YO7AaTrOFeTkg9NXuaFbn3hWVtZxQm2tIlaVa8snoNj3VGnPqIjXmGcxk4axuYd7sTAgMBAAECgYA43YhnRVh2nqJzd2k4Tt/zrmhyjhHm5fSetIKg9ZT3DrXhITsymYHQZ61X95AGATayLT1Zug/mjLIgOTO6f0ENkRQtjVCmKd8Yf/BeDEc5kRLUYDfSqoEydHK0+rCw5tJMgrAnQc5lHc+FVdGe2bOxKTEtZoss9VQ2jYuQ+Z5fUQJBANnvDOcI2OYSksX3PpHzO9F272xkmqYBRGkMc/a5RuOv1CY6FqMIkkloTf6nVl9y6XYV8gnHfbbI/wj4Q4UnPYsCQQCjLxyRYaOeEb/qOzSmFXytgMuCM9sr4eY9jpjzDgNWhpbtaVaf1QvSTXqN0zaUu4Se2tmWGX7zXw9p/dFf8DmZAkEAzl1o0FU2XhZ0WXVYEIhMunpvGSrirhNBHmAmZxjmoa/bqh8TVGpHa6+TO3JlfZioraL2QIBg8Ha/2VSNS0bvJQJALfCLaFpGh6+TicuVLNSLvwStRkB3CUmVWesVIAfn5KoLP1cSbfi6VUA+qkK18PVBhr8x1lHjLXyriDlOgmXMsQJAW9vD/IoBs4QJF87xF7tZvu/b1KRVgLM1edqOgVwMNbIQHBAXghjVjrpuln5w6z1dJ2cEjRP98OxKC0hqEIwIuQ=="));
}

public static byte[] encryptByPrivateKey(byte[] paramArrayOfByte, String paramString)throws Exception
{ 
    paramString = new PKCS8EncodedKeySpec(Base64Utils.decode(paramString));
    paramString = KeyFactory.getInstance("RSA").generatePrivate(paramString);
    Cipher localCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");//like this padding
    localCipher.init(1, paramString);
    int k = paramArrayOfByte.length;
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    int j = 0;
    int i = 0;
    if (k - j > 0)
    {
      if (k - j > 117) {}
      for (paramString = localCipher.doFinal(paramArrayOfByte, j, 117);; paramString = localCipher.doFinal(paramArrayOfByte, j, k - j))
      {
        localByteArrayOutputStream.write(paramString, 0, paramString.length);
        i += 1;
        j = i * 117;
        break;
      }
    }
    paramArrayOfByte = localByteArrayOutputStream.toByteArray();
    localByteArrayOutputStream.close();
    return paramArrayOfByte;
}
