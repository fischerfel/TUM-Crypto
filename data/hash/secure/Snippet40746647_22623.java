public class CryptoClass {
private static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5PADDING";
private static final int KEY_SIZE = 256;
public String DecryptText(String input) throws NoSuchAlgorithmException
    {
        input = input.replace("\0", "");
        byte[] bytesToBeDecrypted = input.getBytes();
        byte[] passwordBytes = Config.ServerKey.getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        passwordBytes = md.digest(passwordBytes);
        byte[] bytesDecrypted = null;
    try {
        bytesDecrypted = AES_Decrypt(bytesToBeDecrypted, passwordBytes);
    } catch (IllegalBlockSizeException ex) {
        Logger.getLogger(CryptoClass.class.getName()).log(Level.SEVERE, null, ex);
    } catch (BadPaddingException ex) {
        Logger.getLogger(CryptoClass.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidKeyException ex) {
        Logger.getLogger(CryptoClass.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidAlgorithmParameterException ex) {
        Logger.getLogger(CryptoClass.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchPaddingException ex) {
        Logger.getLogger(CryptoClass.class.getName()).log(Level.SEVERE, null, ex);
    }
        return new String(bytesDecrypted);
    }
    public String EncryptText(String input) throws NoSuchAlgorithmException
    {
        byte[] bytesToBeEncrypted = input.getBytes();
        byte[] passwordBytes = Config.ServerKey.getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        passwordBytes = md.digest(passwordBytes);
        byte[] bytesEncrypted = null;
    try {
        bytesEncrypted = AES_Encrypt(bytesToBeEncrypted, passwordBytes);
    } catch (NoSuchPaddingException ex) {
        Logger.getLogger(CryptoClass.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidAlgorithmParameterException ex) {
        Logger.getLogger(CryptoClass.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidKeyException ex) {
        Logger.getLogger(CryptoClass.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalBlockSizeException ex) {
        Logger.getLogger(CryptoClass.class.getName()).log(Level.SEVERE, null, ex);
    } catch (BadPaddingException ex) {
        Logger.getLogger(CryptoClass.class.getName()).log(Level.SEVERE, null, ex);
    }
        return Base64.getEncoder().encodeToString(bytesEncrypted);
    }
    public byte[] AES_Decrypt(byte[] bytesToBeDecrypted, byte[] passwordBytes) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException
    {
        byte[] decryptedBytes = null;
        byte[] saltBytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
        final Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
        final byte[] keyData = Arrays.copyOf(passwordBytes, KEY_SIZE
                / Byte.SIZE);
        final byte[] ivBytes = Arrays.copyOf(keyData, cipher.getBlockSize());
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyData, "AES"),
                new IvParameterSpec(ivBytes));
        decryptedBytes = cipher.doFinal(bytesToBeDecrypted);
        return decryptedBytes;
    }
    public byte[] AES_Encrypt(byte[] bytesToBeEncrypted, byte[] passwordBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        byte[] encryptedBytes = null;
        byte[] saltBytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
        final Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
        final byte[] keyData = Arrays.copyOf(passwordBytes, KEY_SIZE
                / Byte.SIZE);
        final byte[] ivBytes = Arrays.copyOf(keyData, cipher.getBlockSize());
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyData, "AES"),
                new IvParameterSpec(ivBytes));
        encryptedBytes = cipher.doFinal(bytesToBeEncrypted);
        return encryptedBytes;
    }
