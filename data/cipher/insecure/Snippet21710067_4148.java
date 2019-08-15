    public class EncryptionClass {

public static SecretKey mainKey=null;



public static SecretKey GenerateKey() throws NoSuchAlgorithmException
{

    KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
    SecretKey myDesKey = keygenerator.generateKey();
    return myDesKey;

    }


public static String Encrypt(String plainText) {

    String encryptedText = "";

    try {
        mainKey=GenerateKey();
            Cipher desCipher;

            // Create the cipher 
            desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            // Initialize the cipher for encryption
            desCipher.init(Cipher.ENCRYPT_MODE,mainKey);

            //sensitive information
            byte[] plainTextAsBytes =plainText.getBytes();
            Log.d("Text [Byte Format] : " , plainTextAsBytes.toString());
            Log.d("Text : " ,new String(plainTextAsBytes));

           // Encrypt the text
            byte[] cipherText = desCipher.doFinal(plainTextAsBytes);

            Log.d("Text Encryted : " ,cipherText.toString());

            encryptedText=cipherText.toString();

    }catch(NoSuchAlgorithmException e){
        Log.d("NoSuchAlgorithmException :", e.toString());
    }catch(NoSuchPaddingException e){
        Log.d("NoSuchPaddingException  :", e.toString());
    }catch(InvalidKeyException e){
        Log.d("InvalidKeyException:", e.toString());
    }catch(IllegalBlockSizeException e){
        Log.d("IllegalBlockSizeException:", e.toString());
    }catch(BadPaddingException e){
        Log.d("BadPaddingException:", e.toString());
    } 
    finally{

    }

    return encryptedText;

}

public static String Decrypt(String cipherText) {

    String decryptedText = "";

    try {

            Log.d("Decrypt MAin Key:",mainKey.getEncoded().toString());
            Cipher desCipher;

            // Create the cipher 
            desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            // Initialize the cipher for encryption
            desCipher.init(Cipher.DECRYPT_MODE,mainKey);


           // Encrypt the text

            byte[] cipherTextBytes=cipherText.getBytes();
            byte[]  plainText= desCipher.doFinal(cipherTextBytes);

            Log.d("Text Decryted : " ,plainText.toString());

            decryptedText=plainText.toString();

    }catch(NoSuchAlgorithmException e){
        Log.d("NoSuchAlgorithmException :", e.toString());
    }catch(NoSuchPaddingException e){
        Log.d("NoSuchPaddingException  :", e.toString());
    }catch(InvalidKeyException e){
        Log.d("InvalidKeyException:", e.toString());
    }catch(IllegalBlockSizeException e){
        Log.d("IllegalBlockSizeException:", e.toString());
    }catch(BadPaddingException e){
        Log.d("BadPaddingException:", e.toString());
    } 
    finally{

    }


    return decryptedText;
}

}
