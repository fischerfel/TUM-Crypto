public class CryptoUtil {

public static byte[] rsaEncrypt(byte[] publicKey, byte[] data) throws IOException, InvalidKeySpecException,
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKey);

    KeyFactory kf = KeyFactory.getInstance("RSA");
    PublicKey pk = kf.generatePublic(publicKeySpec);

    Cipher rsa = Cipher.getInstance("RSA");

    rsa.init(Cipher.ENCRYPT_MODE, pk);
    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

    CipherOutputStream os = new CipherOutputStream(byteOutputStream, rsa);
    os.write(data);
    os.flush();
    os.close();
    return byteOutputStream.toByteArray();

}

public static byte[] rsaDecrypt(byte[] privateKey, byte[] data) throws IOException, InvalidKeySpecException,
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKey);

    KeyFactory kf = KeyFactory.getInstance("RSA");
    PrivateKey pk = kf.generatePrivate(privateKeySpec);

    Cipher rsa = Cipher.getInstance("RSA");

    rsa.init(Cipher.DECRYPT_MODE, pk);
    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);

    InputStream is = new CipherInputStream(byteInputStream, rsa);
    byte[] message = IOUtils.toByteArray(is);
    is.close();
    return message;

}

}
