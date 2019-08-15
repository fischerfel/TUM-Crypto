public class CryptoUtils {

    private  final String TRANSFORMATION = "AES";
    private  final String encodekey = "1234543444555666";
    public  String encrypt(String inputFile)
            throws CryptoException {
        return doEncrypt(encodekey, inputFile);
    }


    public  String decrypt(String input)
            throws CryptoException {
    // return  doCrypto(Cipher.DECRYPT_MODE, key, inputFile);
    return doDecrypt(encodekey,input);
    }

    private  String doEncrypt(String encodekey, String inputStr)   throws CryptoException {
        try {

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            byte[] key = encodekey.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit

            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] inputBytes = inputStr.getBytes();     
            byte[] outputBytes = cipher.doFinal(inputBytes);

            return Base64Utils.encodeToString(outputBytes);

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
       }
     }


    public  String doDecrypt(String encodekey,String encrptedStr) { 
          try {     

              Cipher dcipher = Cipher.getInstance(TRANSFORMATION);
              dcipher = Cipher.getInstance("AES");
              byte[] key = encodekey.getBytes("UTF-8");
              MessageDigest sha = MessageDigest.getInstance("SHA-1");
              key = sha.digest(key);
              key = Arrays.copyOf(key, 16); // use only first 128 bit

              SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

              dcipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            // decode with base64 to get bytes

              byte[] dec = Base64Utils.decode(encrptedStr.getBytes());  
              byte[] utf8 = dcipher.doFinal(dec);

              // create new string based on the specified charset
              return new String(utf8, "UTF8");

          } catch (Exception e) {

            e.printStackTrace();

          }
      return null;
      }
 }
