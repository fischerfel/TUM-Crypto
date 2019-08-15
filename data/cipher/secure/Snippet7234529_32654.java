public class RSAEncryptor {
//Get certificate from base64 string
public static X509Certificate getCertificateFromBase64String(String string) 
        throws CertificateException, javax.security.cert.CertificateException 
{
    if(string.equals("") || string == null) {
        return null;
    }

    byte[] certBytes = Base64.decode(string.getBytes(), Base64.DEFAULT);

    X509Certificate cert = X509Certificate.getInstance(certBytes);

    return cert;
}

//Get public key from base64 encoded string
public static PublicKey getPublicKeyFromEncodedCertData(String encodedCertData) 
    throws CertificateException, javax.security.cert.CertificateException 
{
    if(encodedCertData == null || encodedCertData.equals("")) return null;

    X509Certificate cert = getCertificateFromBase64String(encodedCertData);

    if(cert == null) return null;

    return cert.getPublicKey();
}

public static String rsaEncrypt(String plainText, String keyFromResources)
        throws NoSuchAlgorithmException, InvalidKeySpecException,
        IOException, NoSuchPaddingException, InvalidKeyException,
        IllegalBlockSizeException, BadPaddingException //
{
    if(plainText == null || plainText.equals("")) return null;
    if(keyFromResources == null || keyFromResources.equals("")) return null;

    byte[] encryptedBytes;

    Cipher cipher = Cipher.getInstance("RSA");
    PublicKey publicKey = null;

    try {
        publicKey = getPublicKeyFromEncodedCertData(keyFromResources);
    }
    catch(Exception ex) {
        Logger.LogError("getPublicKeyFromEncodedCertData()", ex);
    }

    cipher.init(Cipher.ENCRYPT_MODE, publicKey);

    encryptedBytes = cipher.doFinal(plainText.getBytes());
    String encrypted = new String(encryptedBytes);
    return encrypted;
}
