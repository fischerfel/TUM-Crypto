    public class AesEncrDec
{

public static String encrypt(String Data)
{
    byte[] byteCipherText = null;
    try {
        String plainData=Data,cipherText,decryptedText;
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecureRandom rnd = new SecureRandom();
        SecretKey secretKey = keyGen.generateKey();
        IvParameterSpec iv;
        iv = new IvParameterSpec(rnd.generateSeed(16));
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE,secretKey,iv); 
        byte[] byteDataToEncrypt = plainData.getBytes();
        byteCipherText = aesCipher.doFinal(byteDataToEncrypt);
        cipherText = new BASE64Encoder().encode(byteCipherText);
        return new String(byteCipherText);
    } catch (InvalidKeyException ex) {
        Logger.getLogger(AesEncrDec.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(AesEncrDec.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchPaddingException ex) {
        Logger.getLogger(AesEncrDec.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalBlockSizeException ex) {
        Logger.getLogger(AesEncrDec.class.getName()).log(Level.SEVERE, null, ex);
    } catch (BadPaddingException ex) {
        Logger.getLogger(AesEncrDec.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidAlgorithmParameterException ex) {
        Logger.getLogger(AesEncrDec.class.getName()).log(Level.SEVERE, null, ex);
    }
    return new String(byteCipherText);
}

public static String dencrypt(String Data)
{
    byte[] byteDecryptedText = null;
    try {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        IvParameterSpec iv;
        SecureRandom rnd = new SecureRandom();
        iv = new IvParameterSpec(rnd.generateSeed(16));
        Cipher aesCipher = Cipher.getInstance("AES");
        SecretKey secretKey = keyGen.generateKey();
        aesCipher.init(Cipher.ENCRYPT_MODE,secretKey,iv);
        byteDecryptedText = aesCipher.doFinal(Data.getBytes());
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(AesEncrDec.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchPaddingException ex) {
        Logger.getLogger(AesEncrDec.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidKeyException ex) {
        Logger.getLogger(AesEncrDec.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidAlgorithmParameterException ex) {
        Logger.getLogger(AesEncrDec.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalBlockSizeException ex) {
        Logger.getLogger(AesEncrDec.class.getName()).log(Level.SEVERE, null, ex);
    } catch (BadPaddingException ex) {
        Logger.getLogger(AesEncrDec.class.getName()).log(Level.SEVERE, null, ex);
    }
    return new String(byteDecryptedText);
}
}
