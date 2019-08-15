import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AdvancedCrypto implements ICrypto {

        public static final String PROVIDER = "BC";
        public static final int SALT_LENGTH = 20;
        public static final int IV_LENGTH = 16;
        public static final int PBE_ITERATION_COUNT = 100;

        private static final String RANDOM_ALGORITHM = "SHA1PRNG";
        private static final String HASH_ALGORITHM = "SHA-512";
        private static final String PBE_ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";
        private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
        private static final String SECRET_KEY_ALGORITHM = "AES";

        public String encrypt(SecretKey secret, String cleartext) throws CryptoException {
                try {

                        byte[] iv = generateIv();
                        String ivHex = HexEncoder.toHex(iv);
                        IvParameterSpec ivspec = new IvParameterSpec(iv);

                        Cipher encryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM, PROVIDER);
                        encryptionCipher.init(Cipher.ENCRYPT_MODE, secret, ivspec);
                        byte[] encryptedText = encryptionCipher.doFinal(cleartext.getBytes("UTF-8"));
                        String encryptedHex = HexEncoder.toHex(encryptedText);

                        return ivHex + encryptedHex;

                } catch (Exception e) {
                        throw new CryptoException("Unable to encrypt", e);
                }
        }

        public String decrypt(SecretKey secret, String encrypted) throws CryptoException {
                try {
                        Cipher decryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM, PROVIDER);
                        String ivHex = encrypted.substring(0, IV_LENGTH * 2);
                        String encryptedHex = encrypted.substring(IV_LENGTH * 2);
                        IvParameterSpec ivspec = new IvParameterSpec(HexEncoder.toByte(ivHex));
                        decryptionCipher.init(Cipher.DECRYPT_MODE, secret, ivspec);
                        byte[] decryptedText = decryptionCipher.doFinal(HexEncoder.toByte(encryptedHex));
                        String decrypted = new String(decryptedText, "UTF-8");
                        return decrypted;
                } catch (Exception e) {
                        throw new CryptoException("Unable to decrypt", e);
                }
        }

        public SecretKey getSecretKey(String password, String salt) throws CryptoException {
                try {
                        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), HexEncoder.toByte(salt), PBE_ITERATION_COUNT, 256);
                        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBE_ALGORITHM, PROVIDER);
                        SecretKey tmp = factory.generateSecret(pbeKeySpec);
                        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), SECRET_KEY_ALGORITHM);
                        return secret;
                } catch (Exception e) {
                        throw new CryptoException("Unable to get secret key", e);
                }
        }

        public String getHash(String password, String salt) throws CryptoException {
                try {
                        String input = password + salt;
                        MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM, PROVIDER);
                        byte[] out = md.digest(input.getBytes("UTF-8"));
                        return HexEncoder.toHex(out);
                } catch (Exception e) {
                        throw new CryptoException("Unable to get hash", e);
                }
        }

        public String generateSalt() throws CryptoException {
                try {
                        SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
                        byte[] salt = new byte[SALT_LENGTH];
                        random.nextBytes(salt);
                        String saltHex = HexEncoder.toHex(salt);
                        return saltHex;
                } catch (Exception e) {
                        throw new CryptoException("Unable to generate salt", e);
                }
        }

        private byte[] generateIv() throws NoSuchAlgorithmException, NoSuchProviderException {
                SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
                byte[] iv = new byte[IV_LENGTH];
                random.nextBytes(iv);
                return iv;
        }

}
