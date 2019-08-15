    package cat.copernic.simetriccd;


import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

class symetric{

    public static String byteArrayToHexString(byte[] b){ 
        StringBuilder sb = new StringBuilder(b.length * 2); 
        for(int i = 0; i < b.length; i++){ 
            int v = b[i] & 0xff; if(v < 16){ 
                sb.append("0"); 
            } 
            sb.append(Integer.toHexString(v)); 
        } return sb.toString(); 
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
            System.err.println("Error generant la clau:" + ex);  
        }

        return sKey;   
    }

    static String encryptData(String password, byte[] data) {
        byte[] encryptedData = null;    
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, genKey(password));
            encryptedData =  cipher.doFinal(data);
        } catch (Exception  ex) {  
            System.err.println("Error xifrant les dades: " + ex);          
        } 

        return byteArrayToHexString(encryptedData);
    }

    static String decryptData(String password, byte[] data) {
        byte[] decryptedData = null;    
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, genKey(password));
            decryptedData =  cipher.doFinal(data);
        } catch (Exception  ex) {  
            System.err.println("Error desxifrant les dades: " + ex);          
        } 
        return decryptedData.toString();
    }

}



public class Main { 

    public static void main(String[] args) {

        //controlar numero de parametros que se pasan
        if(args.length <= 1 || args.length > 3 || args.length == 2){
            System.out.println("java Main -mode=encrypt|decrypt <message> <key>");
            return;
        }

        //sacamos los valores de los argumentos
        String mode = args[0].split("=")[1].toLowerCase();
        byte[] mensage = args[1].getBytes();
        String key = args[2];

        if(mode.equals("encrypt") || mode.equals("decrypt")){
            switch (mode) {
            case "encrypt":
                //System.out.println("encrypt");
                System.out.println(symetric.encryptData(key, mensage));
                break;

            case "decrypt":
                //System.out.println("decrypt");
                System.out.println(symetric.decryptData(key, mensage));
                break;

            default:
                break;
            }
        }else {
            System.out.println("Solo coje valores encrypt/ENCRYPT o decrypt/DECRYPT");
        }
    }
}
