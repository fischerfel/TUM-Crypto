import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.*;

public class Criptografia {
    byte[] chave = "chave de 16bytes".getBytes();

    public String encriptaAES(String chaveCriptografada)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        try {

            Cipher cipher = Cipher.getInstance("AES");
            byte[] mensagem = chaveCriptografada.getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(chave, "AES"));
            chaveCriptografada = cipher.doFinal(mensagem).toString();



            chaveCriptografada  =Base64.getUrlEncoder().encodeToString(chaveCriptografada.getBytes("utf-8"));


        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {

            e.printStackTrace();
        }

        return chaveCriptografada;

    }


    public String descriptografaAES(String chaveCriptografada) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
        Cipher cipher = Cipher.getInstance("AES");

        byte[] base64decodedBytes = Base64.getUrlDecoder().decode(chaveCriptografada);

        chaveCriptografada= base64decodedBytes.toString();


         try {
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(this.chave, "AES"));
             byte[] decrypted = cipher.doFinal(chaveCriptografada.getBytes("UTF-8"));
             chaveCriptografada=decrypted.toString();

        } catch (InvalidKeyException e) {

            e.printStackTrace();
        }



        return chaveCriptografada;


    }   

}

Exception in thread "main" javax.crypto.IllegalBlockSizeException: Input length must be multiple of 16 when decrypting with padded cipher
    at com.sun.crypto.provider.CipherCore.doFinal(CipherCore.java:922)
    at com.sun.crypto.provider.CipherCore.doFinal(CipherCore.java:833)
    at com.sun.crypto.provider.AESCipher.engineDoFinal(AESCipher.java:446)
    at javax.crypto.Cipher.doFinal(Cipher.java:2165)
    at Criptografia.descriptografaAES(Criptografia.java:47)
    at Run.main(Run.java:15)
