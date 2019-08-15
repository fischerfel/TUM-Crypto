public DesEncrypter(String passPhrase) {
    try {
        // Create the key
        KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance(
            "PBEWithMD5AndDES").generateSecret(keySpec);
        ecipher = Cipher.getInstance(key.getAlgorithm());
        dcipher = Cipher.getInstance(key.getAlgorithm());

        // Prepare the parameter to the ciphers
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

        // Create the ciphers
        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
    } catch (java.security.InvalidAlgorithmParameterException e) {
    } catch (java.security.spec.InvalidKeySpecException e) {
    } catch (javax.crypto.NoSuchPaddingException e) {
    } catch (java.security.NoSuchAlgorithmException e) {
    } catch (java.security.InvalidKeyException e) {
    }
}
