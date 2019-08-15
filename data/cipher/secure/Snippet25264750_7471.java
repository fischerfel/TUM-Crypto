import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

public class MainActivity extends Activity 
{

    private byte[] mKeyModulus = {...};
    private byte[] mKeyExponent = {...};

    private String tag = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String token = "";
        try
        {
            token = readTokenFromFile("token.base64");
        }
        catch (IOException e)
        {
            Log.d(tag, "Failed to open property file");
        }

        byte[] encodedBytes = Base64.encode(token.getBytes(), 0);

        Log.d(tag, "Encrypt Token"+ onEncrypt(encodedBytes));
    }

    public String readTokenFromFile(String fileName) throws IOException  
    {  
        String token = "empty";

        try
        {  
            AssetManager assetManager = getAssets();
            InputStream iS = assetManager.open(fileName);

            byte[] buffer = new byte[iS.available()];    
            iS.read(buffer);  

            ByteArrayOutputStream oS = new ByteArrayOutputStream();  

            oS.write(buffer);  
            token = oS.toString();
            oS.close();  
            iS.close(); 

            Log.d(tag, "token ==> "+token);
        }
        catch (IOException e)
        {
            Log.d(tag, "Failed to open property file");
        }

        return token; 

    } // readTokenFromFile end 
    public String onEncrypt(byte[] token)
    {   
        Log.d(tag,"onEncrypt entry");
        String encryptedTranspherable = "";//null;
        // get the publicKey
        try
        {
            BigInteger m = new BigInteger(mKeyModulus);
            BigInteger e = new BigInteger(mKeyExponent);

            KeyFactory fact = KeyFactory.getInstance("RSA");

            Key pubKey = fact.generatePublic(new RSAPublicKeySpec(m, e));

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            byte[] encrypted = blockCipher(token,cipher);
            encryptedTranspherable = Base64.encodeToString(encrypted, Base64.DEFAULT);
        }
        catch (Exception e) 
        {
            throw new RuntimeException("serialisation error got issue here !! ==>"+ e.getMessage(), e);
        }

        return encryptedTranspherable;
    }// onEncrypt end

    private byte[] blockCipher(byte[] bytes,Cipher cipher) throws IllegalBlockSizeException, BadPaddingException
    {
        Log.d(tag,"ISecurityProvider blockCipher entry");
        Log.d(tag,"ISecurityProvider byteArray =>"+ Arrays.toString(bytes));
        byte[] scrambled = new byte[0];

        // toReturn will hold the total result
        byte[] toReturn = new byte[0];
        int length = 256; 

        // another buffer. this one will hold the bytes that have to be modified in this step
        byte[] buffer = new byte[Math.min(bytes.length, length)];//(bytes.length > length ? length : bytes.length)];
        for (int i=0; i< bytes.length; i++)
        {
            // if we filled our buffer array we have our block ready for de- or encryption
            if ((i > 0) && (i % length == 0))
            {
                Log.d(tag,"ISecurityProvider blockCipher processing block  i ="+i);
                scrambled = cipher.doFinal(buffer);
                toReturn = append(toReturn,scrambled);

                // here we calculate the length of the next buffer required
                int newlength = length;

                // if newlength would be longer than  remaining bytes in the bytes array we shorten it.
                if (i + length > bytes.length) 
                {
                    newlength = bytes.length - i;
                }
                // clean the buffer array
                buffer = new byte[newlength];
            }
            // copy byte into our buffer.
            buffer[i%length] = bytes[i];
        }

        // this step is needed if we had a trailing buffer. should only happen when encrypting.
        // example: we encrypt 110 bytes. 100 bytes per run means we "forgot" the last 10 bytes. they are in the buffer array
        scrambled = cipher.doFinal(buffer);
        // final step before we can return the modified data.
        toReturn = append(toReturn,scrambled);

        return toReturn;
    }

    private byte[] append(byte[] prefix, byte[] suffix)
    {
        byte[] toReturn = new byte[prefix.length + suffix.length];

        int prefixSize = prefix.length;
        int  suffixSize = suffix.length;

        for (int i=0; i< prefixSize; i++)
        {
            toReturn[i] = prefix[i];
        }

        for (int i=0; i< suffixSize; i++)
        {
            toReturn[i+prefixSize] = suffix[i];
        }

        return toReturn;
    }
}
