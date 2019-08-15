String value = "test@example.com"
String encrypedValue = EncrypterService get().encrypt(value.getBytes())
String decryptedValue = EncrypterService get().decrypt(encrypedValue .getBytes())



public final class EncrypterService {

    private static Key keySpec;
    private static Cipher encryptCipher;
    private static Cipher decryptCipher;
    private static String passphrase = "IU1ZaypiTiVYc3AtPXMxNWNMYGUmVUF8YUAtUSMuKVI=";
    private static final String KEY_ALGORIGHT = "HmacSHA256";
    private static final String CIPHER_ALGORITHM = "AES";
    private static final String MD5_ALGORITH = "MD5";
    private static EncrypterService service;

    private EncrypterService(){

    }

    private static synchronized void initialize() {
        if (service == null) {
            service = new EncrypterService();
            service.init();
        }
    }

    public static EncrypterService get() {
        initialize();
        return service;
    }


    public String encrypt (byte[] plaintext){
        //returns byte array encrypted with key
        try {

            byte[] encode = encryptCipher.doFinal(plaintext);
            return new String(encode);
        }catch(Exception e){
            throw new RuntimeException("Unable to decrypt data" + e);
        }
    }

    public  String decrypt (byte[] ciphertext) {
        //returns byte array decrypted with key
        try {
            byte[] decode = decryptCipher.doFinal(ciphertext);
            return new String(decode);
        }catch(Exception e){
            throw new RuntimeException("Unable to decrypt data" + e);
        }
    }

    private static void init(){
       try {
           if (encryptCipher == null && decryptCipher == null) {
               byte[] bytesOfMessage = Base64.decode(passphrase, Base64.NO_WRAP);
               MessageDigest md = MessageDigest.getInstance(MD5_ALGORITH);
               byte[] thedigest = md.digest(bytesOfMessage);
               keySpec = new SecretKeySpec(thedigest, KEY_ALGORIGHT);
               encryptCipher = Cipher.getInstance(CIPHER_ALGORITHM);
               encryptCipher.init(Cipher.ENCRYPT_MODE, keySpec);
               decryptCipher = Cipher.getInstance(CIPHER_ALGORITHM);
               decryptCipher.init(Cipher.DECRYPT_MODE, keySpec);
           }
       }catch(Exception e){
           throw new RuntimeException("Unable to initialise encryption", e);
       }
    }

}
