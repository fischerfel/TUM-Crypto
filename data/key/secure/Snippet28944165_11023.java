package cat.copernic.simetriccd;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;


class symetric{

    public static String byteArrayToHexString(byte[] b){ 
        return DatatypeConverter.printHexBinary(b);
    }   

    public static byte[] hexToByteArray(String b){
        return DatatypeConverter.parseHexBinary(b);
    }

    static SecretKey genKey(String text) {    
        SecretKey sKey = null;  
        int keySize = 256;      
        try {
            byte[] data = text.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(data);
            byte[] key = Arrays.copyOf(hash, keySize/8);
            sKey = new SecretKeySpec(key, "AES"); 
        } catch (Exception ex) {
            System.err.println("Error generant la clau");  
        }

        return sKey;   
    }

    static String encryptData(String password, String data) {
        byte[] encryptedData = null;    
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, genKey(password));
            encryptedData =  cipher.doFinal(data.getBytes());
        } catch (Exception  ex) {  
            System.err.println("Error xifrant les dades");          
        } 

        return byteArrayToHexString(encryptedData);
    }

    static String decryptData(String password, String data) throws UnsupportedEncodingException {
        byte[] decryptedData = null;    
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, genKey(password));
            decryptedData =  cipher.doFinal(hexToByteArray(data));
        } catch (Exception  ex) {  
            System.err.println("Error desxifrant les dades");          
        } 

        return new String(decryptedData, "UTF-8");
    }

}

public class Main { 

    public static void main(String[] args) throws UnsupportedEncodingException {

        //controlar numero de parametros que se pasan
        if(args.length <= 1 || args.length > 3 || args.length == 2){
            System.out.println("java Main -mode=encrypt|decrypt <message> <key>");
            return;
        }

        try {

            //sacamos los valores de los argumentos
            String mode = args[0].split("=")[1].toLowerCase();
            String mensage = args[1];
            String key = args[2];

            if(mode.equals("encrypt") || mode.equals("decrypt")){
                switch (mode) {
                case "encrypt":
                    //System.out.println("encrypt");
                    System.out.println("mensaje encriptado= " + symetric.encryptData(key, mensage));
                    break;

                case "decrypt":
                    //System.out.println("decrypt");
                    System.out.println("mensaje desencriptado= " + symetric.decryptData(key, mensage));
                    break;

                default:
                    break;
                }
            }else {
                System.out.println("Solo coje valores encrypt/ENCRYPT o decrypt/DECRYPT");
            }

        } catch (Exception e) {
            System.out.println("Error en la definicion de los atributos");
        }
    }
}
