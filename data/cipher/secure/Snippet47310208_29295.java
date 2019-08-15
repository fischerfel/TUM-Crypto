public class DESedeCrypto implements SymmetricCryptoManager {

/**
 * Constant for the algorithm being used for Triple DES (CBC)
 */
public static final String DESEDE_CBC_TRANSFORMATION_ALG = "DESede/CBC/NoPadding";

@Override
public DESedeEncryptionResult encrypt(byte[] srcData, String cryptoAlgorithm) throws Exception {
    DESedeEncryptionResult result = null;
    byte[] encryptedBytes = null;
    byte[] initializationVector = null;

    if (srcData == null || srcData.length == 0) {
        throw new InvalidEncryptionTargetException();
    }

    KeyGenerator keyGen = KeyGenerator.getInstance("DESede");
    SecretKey secretKey = keyGen.generateKey();

    Cipher cipher = Cipher.getInstance(cryptoAlgorithm);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    result = new DESedeEncryptionResult();

    if (cryptoAlgorithm.equals(DESEDE_CBC_TRANSFORMATION_ALG)) {
        IvParameterSpec spec = cipher.getParameters().getParameterSpec(IvParameterSpec.class);
        initializationVector = spec.getIV();
        result.setInitializationVector(initializationVector);
    }

    encryptedBytes = cipher.doFinal(srcData);

    result.setResultBytes(encryptedBytes);
    result.setKeyBytes(secretKey.getEncoded());

    return result;
}

}
