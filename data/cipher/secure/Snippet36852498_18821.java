import java.util.Base64;

//Decrypt the userpassword
byte[] plainText = Base64.getDecoder().decode(encryptedData);
inputLine = decryptData(plainText);

private String decryptData(byte[] cipherText) throws UnsupportedEncodingException{  
    // decrypt the ciphertext using the private key
    Cipher cipher;
    byte[] newPlainText = null;
    try {
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        newPlainText = cipher.doFinal(cipherText);
        //System.out.println( "Finish decryption: " );
        //System.out.println( new String(newPlainText, "UTF8") );
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchPaddingException ex) {
        Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidKeyException ex) {
        Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalBlockSizeException ex) {
        Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
    } catch (BadPaddingException ex) {
        Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
    }
    return new String(newPlainText, "UTF8");
}  
