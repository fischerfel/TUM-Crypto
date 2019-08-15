private String byte[] decodeBase64(String dataToDecode)
{
    byte[] dataDecoded = Base64.decode(dataToDecode, Base64.DEFAULT);
    return dataDecoded;
}
public static void decrypt(String s) throws Exception
{

    String modulus = "your modulus here";
    String dD = "your D here"; 

    byte[] modBytes = decodeBase64(modulus.trim());
    byte[] dBytes = decodeBase64(dD.trim());

    BigInteger modules = new BigInteger(1, modBytes);
    BigInteger d = new BigInteger(1, dBytes);

    KeyFactory factory = KeyFactory.getInstance("RSA");
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

    RSAPrivateKeySpec privSpec = new RSAPrivateKeySpec(modules, d);
    PrivateKey privKey = factory.generatePrivate(privSpec);
    cipher.init(Cipher.DECRYPT_MODE, privKey);
    byte[] decrypted = cipher.doFinal(decodeBase64(s));
    return new String(decrypted);
}
