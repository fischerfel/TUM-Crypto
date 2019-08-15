enter code here public class AESEncryption {

public static String algo = "AES";
public static String transformation = "AES/ECB/NoPadding";
public static String key = "2b7e151628aed2a6abf7158809cf4f3c";
public static String path = "AESencrypt_1.jpg";

public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
    //encrypt();
    decrypt();
    bytes();
}

    private static void encrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException{
   Cipher aesCipher = Cipher.getInstance(transformation);
   byte[] b = key.getBytes();
   SecretKeySpec key = new SecretKeySpec(b,"AES");
           aesCipher.init(Cipher.ENCRYPT_MODE, key);

    FileInputStream is = new FileInputStream(path);
    CipherOutputStream os = new CipherOutputStream(new FileOutputStream("ecrypted.jpg"), aesCipher);

    copy(is, os);
    os.close();
