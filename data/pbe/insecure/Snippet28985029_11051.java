public class DesEncryption {
private Cipher mEcipher;
private Cipher mDcipher;

private byte[] salt = {
        (byte) 0x08, (byte) 0x90, (byte) 0xA6, (byte) 0x4B,
        (byte) 0xBB, (byte) 0x51, (byte) 0x3C, (byte) 0xDE
};

// Iteration count
int iterationCount = 19;

DesEncryption(String passPhrase) throws EncryptionException {
    try {
        // Create the key
        KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance(
                "PBEWithMD5AndDES").generateSecret(keySpec);
        mEcipher = Cipher.getInstance(key.getAlgorithm());
        mDcipher = Cipher.getInstance(key.getAlgorithm());

        // Prepare the parameter to the ciphers
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

        // Create the ciphers
        mEcipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        mDcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
    } catch (java.security.InvalidAlgorithmParameterException e) {
        throw new EncryptionException(e);
    } catch (java.security.spec.InvalidKeySpecException e) {
        throw new EncryptionException(e);
    } catch (javax.crypto.NoSuchPaddingException e) {
        throw new EncryptionException(e);
    } catch (java.security.NoSuchAlgorithmException e) {
        throw new EncryptionException(e);
    } catch (java.security.InvalidKeyException e) {
        throw new EncryptionException(e);
    }
}

public String encrypt(String str) throws EncryptionException {
    try {
        // Encode the string into bytes using utf-8
        byte[] utf8 = str.getBytes("UTF8");

        // Encrypt
        byte[] enc = mEcipher.doFinal(utf8);

        // Encode bytes to base64 to get a string
        return new sun.misc.BASE64Encoder().encode(enc);
    } catch (javax.crypto.BadPaddingException e) {
        throw new EncryptionException(e);
    } catch (IllegalBlockSizeException e) {
        throw new EncryptionException(e);
    } catch (UnsupportedEncodingException e) {
        throw new EncryptionException(e);
    } catch (java.io.IOException e) {
        throw new EncryptionException(e);
    }
}

public String decrypt(String str) throws EncryptionException {
    try {
        // Decode base64 to get bytes
        byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

        // Decrypt
        byte[] utf8 = mDcipher.doFinal(dec);

        // Decode using utf-8
        return new String(utf8, "UTF8");
    } catch (javax.crypto.BadPaddingException e) {
        throw new EncryptionException(e);
    } catch (IllegalBlockSizeException e) {
        throw new EncryptionException(e);
    } catch (UnsupportedEncodingException e) {
        throw new EncryptionException(e);
    } catch (java.io.IOException e) {
        throw new EncryptionException(e);
    }
}
}
