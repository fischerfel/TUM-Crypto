public class AES_encryption {
private static SecretKey skey;
public static Cipher cipher;

public static void main(String[] args) throws Exception{
    String init_vector = "RndInitVecforCBC"; 
    String message = "Encrypt this?!()";
    String ciphertext = null;

    //Generate Key
    skey = generateKey();

    //Create IV necessary for CBC
    IvParameterSpec iv = new IvParameterSpec(init_vector.getBytes());

    //Set cipher to AES/CBC/
    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    try{
        ciphertext = encrypt(skey, iv, message);
    }
    catch(Exception ex){
        System.err.println("Exception caught at encrypt method!" + ex);
    }
    System.out.println("Original Message: " + message + "\nCipher Text: " + ciphertext);

    try{
        message = decrypt(skey, iv, message);
    }
    catch(Exception ex){
        System.err.println("Exception caught at decrypt method! " + ex);
    }

    System.out.println("Original Decrypted Message: " + message);


}

private static SecretKey generateKey(){
    try {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(128);
        skey = keygen.generateKey();
    }
    catch(NoSuchAlgorithmException ex){ 
        System.err.println(ex);
    }
    return skey;
}

private static String encrypt(SecretKey skey, IvParameterSpec iv, String plaintext) throws Exception{
    //Encodes plaintext into a sequence of bytes using the given charset
    byte[] ptbytes = plaintext.getBytes(StandardCharsets.UTF_8);

    //Init cipher for AES/CBC encryption 
    cipher.init(Cipher.ENCRYPT_MODE, skey, iv);

    //Encryption of plaintext and enconding to Base64 String so it can be printed out
    byte[] ctbytes = cipher.doFinal(ptbytes);
    Base64.Encoder encoder64 = Base64.getEncoder();
    String ciphertext = new String(encoder64.encode(ctbytes), "UTF-8");

    return ciphertext;
}

private static String decrypt(SecretKey skey, IvParameterSpec iv, String ciphertext) throws Exception{
    //Decoding ciphertext from Base64 to bytes[]
    Base64.Decoder decoder64 = Base64.getDecoder();
    byte[] ctbytes = decoder64.decode(ciphertext);

    //Init cipher for AES/CBC decryption 
    cipher.init(Cipher.DECRYPT_MODE, skey, iv);

    //Decryption of ciphertext 
    byte[] ptbytes = cipher.doFinal(ctbytes);
    String plaintext = new String(ptbytes);

    return plaintext;
}
