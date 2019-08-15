KeyPairGenerator kpg;

KeyPair kp;

static PublicKey publicKey;

static PrivateKey privateKey;

byte [] encryptedBytes,decryptedBytes;

Cipher cipher;

public String GetEncryptValue(String value) 
throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, 
       IllegalBlockSizeException, BadPaddingException 
{
    InputStream is = null;
    javax.security.cert.X509Certificate x5092 = null;
    try {
        is = getAssets().open("publicCer.cer");
         x5092 = javax.security.cert.X509Certificate.getInstance(is);
    } catch (javax.security.cert.CertificateException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return RSAEncrypt("Gergova", x5092);
}

public String RSAEncrypt (
    final String plain, javax.security.cert.X509Certificate x5092) 
        throws NoSuchAlgorithmException, NoSuchPaddingException, 
           InvalidKeyException, IllegalBlockSizeException, 
           BadPaddingException 
{
    kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(1024);
    kp = kpg.genKeyPair();
    publicKey = x5092.getPublicKey();
    privateKey = kp.getPrivate();

    cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    encryptedBytes = cipher.doFinal(stringToBytesASCII(plain));
    String str = null;
    try {
        str = new String(encryptedBytes, "ISO-8859-1");
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return str;
}

public static byte[] stringToBytesASCII(String str) {

     byte[] b = new byte[str.length()];
     for (int i = 0; i < b.length; i++) {
      b[i] = (byte) str.charAt(i);
     }
     return b;
}
