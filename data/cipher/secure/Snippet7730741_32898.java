    static final String PassPhrase = "IhDyHz6bgQyS0Ff1/1s=";   
    static final String SaltValue = "0A0Qvv09OXd3GsYHVrA=";    
    static final String HashAlgorithm = "SHA1";               
    static final int PasswordIterations = 3;                   
    static final String InitVector = "GjrlRZ6INgNckBqv";       
    static final int KeySize = 256;

public static String encrypt(String plainText)
{
    char[] password = PassPhrase.toCharArray();
    byte[] salt = SaltValue.getBytes();
    byte[] iv = InitVector.getBytes();
    byte[] ciphertext = new byte[0];

    SecretKeyFactory factory;
    try {
        factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        KeySpec spec = new PBEKeySpec(password, salt, PasswordIterations, 256);
        SecretKey tmp;

        tmp = factory.generateSecret(spec);

        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        //iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        ciphertext = cipher.doFinal(plainText.getBytes("UTF-8"));

    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeySpecException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } 
    //catch (InvalidParameterSpecException e) {
    //  // TODO Auto-generated catch block
    //  e.printStackTrace();
    //} 
    catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return Base64.encode(new String(ciphertext));
}
