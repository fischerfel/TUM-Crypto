import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import com.Ostermiller.util.Base64;
import com.Ostermiller.util.MD5;
import java.net.URLEncoder;
public class EnDecryptor {

    public static void addProvider()
    {
        if (Security.getProvider("IBMJCE") == null) {
            // IBMJCE is not installed, install it.
            try 
            {
                Security.addProvider
                ((Provider)Class.forName("com.ibm.crypto.provider.IBMJCE").newInstance());
            }
            catch (Exception ex) {
                AuthLogger.logFatal("EnDecryptor:addProvider():Cannot install provider: " + ex.getMessage());
            }
        }
    }

    public static String encrypt(String word)
    {
        addProvider();
        String encWord="";
        byte[] encryptedWordbytes=null;
        try
        {
            if (null == word)
            {
                throw new NullPointerException("EnDecryptor:encrypt(): No string to be encrypted provided!");
            }

            AuthLogger.logDebug("EnDecryptor:encrypt():Generating an encryption key...");
            encryptedWordbytes = MD5.getHash(word);
            AuthLogger.logDebug("EnDecryptor:encrypt():MD5 HASH length:"+encryptedWordbytes.length);
            AuthLogger.logDebug("EnDecryptor:encrypt():MD5 HASH :"+new String(encryptedWordbytes));
            // Create a Rijndael key
            SecretKeySpec KeySpec = new SecretKeySpec(encryptedWordbytes, "AES");

            AuthLogger.logDebug("EnDecryptor:encrypt():Done generating the key...");
            Cipher cipherKey =Cipher.getInstance("AES/CBC/PKCS5Padding", "IBMJCE");
            byte[] iv = new byte[16];
            IvParameterSpec spec = null;
            for(int i=0;i<16;i++)
            {
                iv[i]=(byte)i;
            }
            spec = new IvParameterSpec(iv);
            cipherKey.init(Cipher.ENCRYPT_MODE, KeySpec, spec);
            byte[] encryptedDataBytes = cipherKey.doFinal(word.getBytes());
            String base64data = new String(Base64.encode(encryptedDataBytes));
            AuthLogger.logDebug("EnDecryptor:encrypt():BASE64DATA="+base64data);
            byte[] encryptedKeyBytes = MD5.getHash(encryptedDataBytes);
            SecretKeySpec KeySpec2 =  new SecretKeySpec(encryptedKeyBytes, "AES");
            Cipher cipherKey2 = Cipher.getInstance("AES/CBC/PKCS5Padding", "IBMJCE");//Cipher.getInstance(DataEncryptDecrypt.AlgEnc);
            cipherKey2.init(Cipher.ENCRYPT_MODE, KeySpec2, spec);
            byte[] encryptedkey = cipherKey2.doFinal(encryptedWordbytes);
            String base64Key = new String(Base64.encode(encryptedkey));
            AuthLogger.logDebug("EnDecryptor:encrypt():BASE64Key="+base64Key);

            String parm1 = "Data=" + URLEncoder.encode(base64data, "UTF-8") ;//$$$ encode(base64data);
            String parm2 = "A=" + URLEncoder.encode(base64Key, "UTF-8") ;//$$$ encode(base64Key);
            //encWord="Data="+parm1+"&A="+parm2;
            encWord=parm1+"&"+parm2;

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return encWord;
    }

    public static String decrypt(String encData, String encKey)
    {
        addProvider();
        String decryptedData="";
        byte[] abKeysKey=null;
        try
        {
            byte[] abEncryptedKeys=(Base64.decode(encKey.getBytes()));
            if (null == encData)
            {
                throw new NullPointerException("EnDecryptor:decrypt(): No data to be decryopted provided!");
            }

            AuthLogger.logDebug("EnDecryptor:decrypt():Generating a the HASH of the data...");
            abKeysKey = MD5.getHash(Base64.decode(encData.getBytes()));
            // Create a Rijndael key
            SecretKeySpec KeySpec = new SecretKeySpec(abKeysKey, "AES");

            Cipher cipherKey = Cipher.getInstance("AES/CBC/PKCS5Padding", "IBMJCE");//Cipher.getInstance(DataEncryptDecrypt.AlgEnc);
            IvParameterSpec spec = null;
            byte[] iv = new byte[16];
            for(int i=0;i<16;i++)
            {   
                iv[i]=(byte)i;
            }
            spec = new IvParameterSpec(iv);
            cipherKey.init(Cipher.DECRYPT_MODE, KeySpec, spec);

            byte[] abKeys = cipherKey.doFinal(abEncryptedKeys);
            String base64key = new String(Base64.encode(abKeys));
            AuthLogger.logDebug("EnDecryptor:decrypt():BASE64 DECODED KEY="+base64key);
            //byte[] encryptedKeyBytes = MD5.getHash(encryptedDataBytes);
            SecretKeySpec KeySpec2 =  new SecretKeySpec(abKeys, "AES");
            Cipher cipherKey2 = Cipher.getInstance("AES/CBC/PKCS5Padding", "IBMJCE");//Cipher.getInstance(DataEncryptDecrypt.AlgEnc);
            cipherKey2.init(Cipher.DECRYPT_MODE, KeySpec2, spec);

            byte[] decodedData = cipherKey2.doFinal(Base64.decode(encData.getBytes()));
            decryptedData= new String(decodedData);
            AuthLogger.logDebug("EnDecryptor:decrypt():decoded data="+decryptedData);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return decryptedData;
    }
}
