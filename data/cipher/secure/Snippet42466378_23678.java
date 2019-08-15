import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.json.*;
import javax.xml.bind.DatatypeConverter;

public class CryptographicTools 
{
    /**
     * This method encrypts a message
     * @param message String message to be encrypted
     * @return a JSONObject 
     */
    public JSONObject encryptMessage(String message)
    {
        JSONObject output = new JSONObject(); // instantiate JSONObject

        try
        {
            //read in public key
            byte[] publicKeyBytes = readKeyFromFile("public.der");//pem convert to der

            //turn bytes into public key
            X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicKeyBytes); //encodes the bytes
            KeyFactory keyFactory = KeyFactory.getInstance("RSA"); //make the key a RSA instance

            //initialize RSA object and public key
            Cipher RSAObject = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding"); //with OAEP   
            RSAObject.init(Cipher.ENCRYPT_MODE, keyFactory.generatePublic(publicSpec)); //create RSA encryption cipher with a generated public key

            //generate 256-bit AES key
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");//generate AES Key
            keyGen.init(256); //generate a key with 256 bits
            SecretKey AESKey = keyGen.generateKey(); //generate AES key with 256 bits

            //Create AES IV
            SecureRandom randomByteGenerator = new SecureRandom();//secure generator to generate random byes for  IV
            byte[] AESKeyIVArray = new byte[16];
            randomByteGenerator.nextBytes(AESKeyIVArray);//get random bytes for iv
            IvParameterSpec AES_IV = new IvParameterSpec(AESKeyIVArray); //iv object for AES object

            //initialize AES object
            Cipher AESObject = Cipher.getInstance("AES/CBC/PKCS5Padding");
            AESObject.init(Cipher.ENCRYPT_MODE, AESKey, AES_IV); //tell the AES object to encrypt

            //encrypt message with AES
            byte[] AESciphertext = AESObject.doFinal(message.getBytes());

            //generate 256-bit HMAC key
            byte[] SHA256KeyArray = new byte[32];//256 bits
            randomByteGenerator.nextBytes(SHA256KeyArray);//generate random bits for key
            SecretKeySpec HMACKeySpec = new SecretKeySpec (SHA256KeyArray,"HmacSHA256"); //make the key
            Mac HMAC = Mac.getInstance("HmacSHA256"); //initialize HMAC
            HMAC.init(HMACKeySpec);//put key in cipher
            byte [] HMACTag = HMAC.doFinal(AESciphertext);//generate HMAC tag

            //concatenate AES and HMAC keys
            byte[] AESKeyByte = AESKey.getEncoded();///turn AESKey to byte array
            byte[] HMACKeySpecByte = HMACKeySpec.getEncoded();///turn HMAXKey to byte array
            byte[] concatenatedKeys = new byte[AESKeyByte.length + HMACKeySpecByte.length];//new array for concatenated keys

            //combine keys in new array
            System.arraycopy(AESKeyByte, 0, concatenatedKeys, 0, AESKeyByte.length);
            System.arraycopy(HMACKeySpecByte, 0, concatenatedKeys, AESKeyByte.length, HMACKeySpecByte.length);

            //encrypt keys with RSA object
            byte[] RSAciphertext = RSAObject.doFinal(concatenatedKeys);

            //put RSA ciphertext, AES ciphertext, AES_IV and HMAC tag in JSon
            //save byte[] as Strings in hex
            output.put("RSAciphertext", DatatypeConverter.printHexBinary(RSAciphertext));
            output.put("AESciphertext", DatatypeConverter.printHexBinary(AESciphertext));
            output.put("AES_IV", DatatypeConverter.printHexBinary(AES_IV.getIV()));
            output.put("HMACTag", DatatypeConverter.printHexBinary(HMACTag));
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.toString() +e.getMessage()); //error message
        }

        return output; //return as JSON Object
    }

    /**
     * This method decrypts a message
     * @param jsonObjectEncrypted
     * @return message as string
     */
    public String decrypt (JSONObject jsonObjectEncrypted)
    {
        String message="";
        try
        {
            //recover RSA ciphertext from JSON
            String RSACiphertextString=jsonObjectEncrypted.getString("RSAciphertext");
            byte[] recoveredRSAciphertext = DatatypeConverter.parseHexBinary(RSACiphertextString); //convert hex string to byte array

            //recover AES ciphertext from JSON
            String AESCiphertextString=jsonObjectEncrypted.getString("AESciphertext");
            byte[] recoveredAESciphertext = DatatypeConverter.parseHexBinary(AESCiphertextString); //convert hex string to byte array

            //recover AES IV from JSON
            String AES_IVString=jsonObjectEncrypted.get("AES_IV").toString();
            byte[] recoveredAES_IV = DatatypeConverter.parseHexBinary(AES_IVString); //convert hex string to byte array
            //recover HMACTag from JSON
            String HMACTagString=jsonObjectEncrypted.getString("HMACTag");
            byte[] recoveredHMACTag = DatatypeConverter.parseHexBinary(HMACTagString); //convert hex string to byte array

            //read in private key
            byte[] privateKeyBytes = readKeyFromFile("private.der");//pem convert to der

            //turn bytes into private key
            PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            //initialize RSA object and private key
            Cipher RSAObject = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding"); //with OAEP   
            RSAObject.init(Cipher.DECRYPT_MODE, keyFactory.generatePrivate(privateSpec)); //create RSA encryption cipher with a generated private key

            //Decrypt concatenated keys with RSA object
            byte[] concatenatedKeys = RSAObject.doFinal(recoveredRSAciphertext);

            //split the concatenated keys
            byte[] AESKey = new byte[concatenatedKeys.length/2];
            byte[] HMACKey = new byte[concatenatedKeys.length/2];
            System.arraycopy(concatenatedKeys, 0,AESKey,0,AESKey.length); //Copy half into AESKey
            System.arraycopy(concatenatedKeys, AESKey.length,HMACKey,0,HMACKey.length); //Copy Other half into HMACKey

            //generate HMACTag
            SecretKeySpec HMACKeySpec = new SecretKeySpec (HMACKey,"HmacSHA256"); //make the key
            Mac HMAC = Mac.getInstance("HmacSHA256");
            HMAC.init(HMACKeySpec);//initialize with HMAC Key
            byte [] newHMACTag = HMAC.doFinal(recoveredAESciphertext); //generate HMACTag with AES Ciphertext

            if(recoveredHMACTag.equals(newHMACTag)) //encrypt message if tags are equal
            {
                //initialize AES object
                Cipher AESObject = Cipher.getInstance("AES/CBC/PKCS5Padding");
                AESObject.init(Cipher.DECRYPT_MODE, new SecretKeySpec (AESKey,"AES"), new IvParameterSpec(recoveredAES_IV)); //tell the AES object to encrypt
                message = new String (AESObject.doFinal(recoveredAESciphertext), "US-ASCII");//encrypt AES ciphertext and save as string

            }
            else
            {
                System.out.println("Message cannot be decrypted.");
            }


        }
        catch (Exception e)
        {
            System.out.println("Error: "+e.toString()+": "+e.getMessage()); //error message
        }

        return message; //return plaintext
    }

    /**
     * This method reads bytes of a key from a file  into a byte array
     * @param fileName type of key
     * @return byte array
     * @throws IOException
     */
    public byte[] readKeyFromFile(String fileName) throws IOException
    {
        return Files.readAllBytes(Paths.get(fileName));
    }

}
