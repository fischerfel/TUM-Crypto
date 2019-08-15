public class AES {
 public static void main(String[] args) throws Exception {

  byte[] encKey1 = new byte[128];

  byte[] EncIV = new byte[256];
  byte[] UnEncIV = new byte[128];
  byte[] unCrypKey = new byte[128];
  byte[] unCrypText = new byte[1424];

  File f = new File("C://ftp//ciphertext.enc");
  FileInputStream fis = new FileInputStream(F);
  byte[] EncText = new byte[(int) f.length()];
  fis.read(encKey1);
  fis.read(EncIV);
  fis.read(EncText);
  EncIV = Arrays.copyOfRange(EncIV, 128, 256);
  EncText = Arrays.copyOfRange(EncText, 384, EncText.length);
  System.out.println(EncText.length);
  KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
  char[] password = "lab1StorePass".toCharArray();
  java.io.FileInputStream fos = new java.io.FileInputStream(
    "C://ftp//lab1Store");
  ks.load(fos, password);

  char[] passwordkey1 = "lab1KeyPass".toCharArray();

  PrivateKey Lab1EncKey = (PrivateKey) ks.getKey("lab1EncKeys",
    passwordkey1);

  Cipher rsaDec = Cipher.getInstance("RSA"); // set cipher to RSA decryption
  rsaDec.init(Cipher.DECRYPT_MODE, Lab1EncKey); // initalize cipher ti lab1key

  unCrypKey = rsaDec.doFinal(encKey1); // Decryps first key

  UnEncIV = rsaDec.doFinal(EncIV); //decryps encive byte array to undecrypted bytearray---- OBS! Error this is 64 BYTES big, we want 16?
  System.out.println("lab1key "+ unCrypKey +" IV " + UnEncIV);
  //-------CIPHERTEXT decryption---------
  Cipher AESDec = Cipher.getInstance("AES/CBC/PKCS5Padding");
  //---------convert decrypted bytearrays to acctual keys
  SecretKeySpec unCrypKey1 = new SecretKeySpec(unCrypKey, "AES");
  IvParameterSpec ivSpec = new IvParameterSpec(UnEncIV);

  AESDec.init(Cipher.DECRYPT_MODE, unCrypKey1, ivSpec );

  unCrypText = AESDec.doFinal(EncText);

  // Convert decrypted cipher bytearray to string
  String deCryptedString = new String(unCrypKey);
  System.out.println(deCryptedString);
 }
