public class RSACrypto implements AsymmetricCryptoManager {

/**
 * Tranformation algorithm for OAEP.
 */
private static final String OAEP_RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding";

/**
 * Constructor of the class.
 */
public RSACrypto() {
}

@Override
public byte[] encryptWithCertificate(InputStream inputStream, byte[] targetBytes) throws Exception {
    byte[] encryptedBytes = null;

    if (targetBytes == null || targetBytes.length == 0) {
        throw new InvalidEncryptionTargetException();
    }

    if (inputStream == null) {
        throw new InvalidCertificatePathException("Resource specified for operation is not valid");
    }

    X509Certificate certificate = CryptoUtils.readCertificateFromInputStream(inputStream);
    encryptedBytes = getEncryptedBytes(certificate, targetBytes);

    return encryptedBytes;
}

private byte[] getEncryptedBytes(X509Certificate certificate, byte[] targetBytes) throws Exception {
    byte[] encryptedBytes = null;

    PublicKey publicKey = certificate.getPublicKey();

    Cipher cipher = Cipher.getInstance(OAEP_RSA_TRANSFORMATION);
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    encryptedBytes = cipher.doFinal(targetBytes);

    return encryptedBytes;
}

}
