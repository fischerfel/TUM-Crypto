 public class Encrypt{


public static void main(String[] args) throws Exception {

   String FileName = "D:/ashok/normal.txt";
    String FileName1 = "D:/ashok/encrypted.txt";


    KeyGenerator KeyGen = KeyGenerator.getInstance("AES");
    KeyGen.init(128);

    SecretKey SecKey = KeyGen.generateKey();

    Cipher AesCipher = Cipher.getInstance("AES");



    byte[] cipherText = Files.readAllBytes(Paths.get(FileName));
    AesCipher.init(Cipher.ENCRYPT_MODE, SecKey);
    byte[] byteCipherText = AesCipher.doFinal(cipherText);
    Files.write(Paths.get(FileName1), byteCipherText);
 }
