public class EncProj1 {

    // Class constants 
    public static final String ORIG_FPATH = "test.jpg";
    public static final String ENC_FPATH = "enc.jpg";
    public static final String DECRYP_FPATH = "decryp.jpg";


    public static void main(String[] args) throws NoSuchAlgorithmException, InstantiationException, IllegalAccessException, IOException {
        //Define enc key
        SecretKeySpec secretKey;
        byte[] key;
        String myKey = "ThisIsAStrongPasswordForEncryptionAndDecryption";

        //Hash the key
        //Define hash algo
        MessageDigest sha = null;
        //Convert key to bytes
        key = myKey.getBytes("UTF-8");
        System.out.println("Orig key len:-> " + key.length); //==> Debug <==
        //Dedine hash algo
        sha = MessageDigest.getInstance("SHA-1");
        //Hash the key
        key = sha.digest(key);
        //Pad the key... use only first 128 bit (16)
        key = Arrays.copyOf(key, 16); 
        System.out.println("Padded key len:-> " + key.length); //==> Debug <==
        System.out.println("Hashed key:-> " + new String(key, "UTF-8")); //==> Debug <==
        //Define the key
        secretKey = new SecretKeySpec(key, "AES");

        //Get the file for encryption
        byte[] fForEnc = getFileBytes(ORIG_FPATH);
        System.out.println("File for enc:-> " + fForEnc); //==> Debug <==

        //Encrypt file
        byte[] encrypted = encryptFile(secretKey, fForEnc);
        System.out.println("Encrypted file:-> " + encrypted); //==> Debug <==

        //Save encrypted file
        saveFile(encrypted, ENC_FPATH);
        System.out.println("Encrypted file saved!");

        //Get enc file
        byte[] encFile = getFileBytes(ENC_FPATH);
        System.out.println("Enc File:-> " + encFile); //==> Debug <==

        //decrypt the file
        byte[] decrypted = decryptFile(secretKey, encFile);
        System.out.println("Decryp File:-> " + decrypted); //==> Debug <==

        //Save decryp file
        saveFile(decrypted, DECRYP_FPATH);
        System.out.println("Done");


    }

    public static byte[] getFileBytes(String fPath) {

              File f = new File(fPath);
              InputStream is = null;
              try {
                  is = new FileInputStream(f);
              } catch (FileNotFoundException e2) {
                  e2.printStackTrace();
              }
              byte[] content = null;
              try {
                  content = new byte[is.available()];
              } catch (IOException e1) {
                  e1.printStackTrace();
              }
              try {
                  is.read(content);
              } catch (IOException e) {
                  e.printStackTrace();
              }

              return content;
          }

    public static byte[] encryptFile(SecretKey secretKey, byte[] content) {
              Cipher cipher;
              byte[] encrypted = null;
              try {
                  //Define AES cipher
                  cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

                  cipher.init(Cipher.ENCRYPT_MODE, secretKey);

                  //Encrypt a base64 version of the file bytes
                  encrypted = Base64.encodeBase64(cipher.doFinal(content));

              } catch (Exception e) {

                  System.out.println("Error while encrypting: " + e.toString());
              }
              return encrypted;

          }

    public static void saveFile(byte[] bytes, String fPath) throws IOException {

              FileOutputStream fos = new FileOutputStream(fPath);
              fos.write(bytes);
              fos.close();

          }

    public static byte[] decryptFile(SecretKey secretKey, byte[] textCryp) {
              Cipher cipher;
              byte[] decrypted = null;
              try {
                  cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");

                  cipher.init(Cipher.DECRYPT_MODE, secretKey);
                  decrypted = cipher.doFinal(Base64.decodeBase64(textCryp));

              } catch (Exception e) {

                  System.out.println("Error while decrypting: " + e.toString());
              }
              return decrypted;
          }

}
