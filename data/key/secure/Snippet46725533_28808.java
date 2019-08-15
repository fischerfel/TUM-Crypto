public class CryptoTest1
{
public static byte[] doEncrypt(byte[] msg, byte[] key) throws Exception {
    //prepare key
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

    //prepare cipher
    String cipherALG = "AES/CBC/PKCS5Padding"; // use your preferred algorithm 
    Cipher cipher = Cipher.getInstance(cipherALG);
    String string = cipher.getAlgorithm();


    //as iv (Initial Vector) is only required for CBC mode
    if (string.contains("CBC")) {
        //IvParameterSpec ivParameterSpec = new IvParameterSpec(iv); 
        IvParameterSpec ivParameterSpec = cipher.getParameters().getParameterSpec(IvParameterSpec.class);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
    } else {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    }

    byte[] encMessage = cipher.doFinal(msg);        
    return encMessage;
}

public static byte[] doDecrypt(byte[] encMsgtoDec, byte[] key) throws Exception {
    //prepare key
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

    //prepare cipher
    String cipherALG = "AES/CBC/PKCS5Padding"; // use your preferred algorithm 
    Cipher cipher = Cipher.getInstance(cipherALG);
    String string = cipher.getAlgorithm();

    //as iv (Initial Vector) is only required for CBC mode
    if (string.contains("CBC")) {
        //IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);   
        IvParameterSpec ivParameterSpec = cipher.getParameters().getParameterSpec(IvParameterSpec.class);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
    } else {
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
    }

    byte[] decMsg = cipher.doFinal(encMsgtoDec);        
    return decMsg;
}
}
