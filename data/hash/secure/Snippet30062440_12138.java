import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


import org.apache.commons.codec.binary.Base64;

public class TestEncryptionEncode2 {

    private String keyString = "asdfgh";

    public static void main(String[] args) throws Exception {
         TestEncryptionEncode2 api = new TestEncryptionEncode2();

         String input = "abcdwer.comq1234";

        try {       
            String[] encrypted = api.encryptObject(input);  
            // url may differ.. based upon project initial context
            System.out.println("http://localhost:8080/view?d="+encrypted[0]+"&v="+encrypted[1]);

                        Object obj = api.decryptObject(encrypted[0], encrypted[1]);
                        System.out.println("Object Decrypted: "+obj.toString());

        }catch(Exception e) {
            //logger.debug("Unable to encrypt view id: "+id, e);
                    e.printStackTrace();
        }   

                System.out.println("DONEE ");

     }

    private String[] encryptObject(Object obj) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(stream);
        try {
            // Serialize the object         
            out.writeObject(obj);       
            byte[] serialized = stream.toByteArray();

                        System.out.println("serialized "+serialized[0]);

            // Setup the cipher and Init Vector
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] iv = new byte[cipher.getBlockSize()];

                System.out.println("cipher.getBlockSize() "+cipher.getBlockSize());
                System.out.println("iv.length "+iv.length);


            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // Hash the key with SHA-256 and trim the output to 128-bit for the key
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(keyString.getBytes());
            byte[] key = new byte[16];
            System.arraycopy(digest.digest(), 0, key, 0, key.length);
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

            // encrypt
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            // Encrypt & Encode the input
            byte[] encrypted = cipher.doFinal(serialized);       
            byte[] base64Encoded = Base64.encodeBase64(encrypted);
            String base64String = new String(base64Encoded);
            String urlEncodedData = URLEncoder.encode(base64String,"UTF-8");

            // Encode the Init Vector
            byte[] base64IV = Base64.encodeBase64(iv);
            String base64IVString = new String(base64IV);
            String urlEncodedIV = URLEncoder.encode(base64IVString, "UTF-8");

                System.out.println("urlEncodedData.length "+urlEncodedData.length());
                System.out.println("urlEncodedIV.length "+urlEncodedIV.length());


            return new String[] {urlEncodedData, urlEncodedIV};
        }finally {
            stream.close();
            out.close();
        }
    }

    /**
     * Decrypts the String and serializes the object
     * @param base64Data
     * @param base64IV
     * @return
     * @throws Exception
     */
    public Object decryptObject(String base64Data, String base64IV) throws Exception {

            System.out.println("decryptObject "+base64Data);
            System.out.println("decryptObject "+base64IV);

        // Decode the data
        byte[] encryptedData = Base64.decodeBase64(base64Data.getBytes()); 

        // Decode the Init Vector
        byte[] rawIV = Base64.decodeBase64(base64IV.getBytes()); 
        System.out.println("rawIV "+rawIV.length);
                for (int i=0;i < rawIV.length;i++ ){
                    System.out.println("---------"+rawIV[i]);
                }
        // Configure the Cipher
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivSpec = new IvParameterSpec(rawIV); 
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(keyString.getBytes());
        byte[] key = new byte[16];
        System.arraycopy(digest.digest(), 0, key, 0, key.length);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec); //////////////////////////////This is the error line

        // Decrypt the data..
        byte[] decrypted = cipher.doFinal(encryptedData);

        // Deserialize the object       
        ByteArrayInputStream stream = new ByteArrayInputStream(decrypted);
        ObjectInput in = new ObjectInputStream(stream);
        Object obj = null;
        try {
            obj = in.readObject(); 
                        System.out.println("objobj "+obj);

        }
                catch(Exception e) {
            //logger.debug("Unable to encrypt view id: "+id, e);
                    e.printStackTrace();
        }finally {
            stream.close();
            in.close();
        }
        return obj;
    }


}
