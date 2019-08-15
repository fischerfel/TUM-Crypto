   import java.io.File;
   import java.io.FileInputStream;
   import java.io.FileOutputStream;
   import java.io.InputStream;

   import javax.crypto.Cipher;
   import javax.crypto.CipherInputStream;
   import javax.crypto.spec.SecretKeySpec;

    import android.util.Log;

  public class Crypto
 {

public FileInputStream mIn;
public FileOutputStream mOut;

public Crypto(String fileIn, String fileOut)
{
    try
    {
        mIn = new FileInputStream(new File(fileIn));
        mOut = new FileOutputStream(new File(fileOut));
        decrypt(mIn, mOut);
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
}

public static void decrypt(InputStream in, FileOutputStream out) throws Exception
{
    final String string = "346a23652a46392b4d73257c67317e352e3372482177652c";
    byte[] hexAsBytes = hexStringToByteArray(string);

    SecretKeySpec keySpec = new SecretKeySpec(hexAsBytes, "AES");
    Cipher cipher = Cipher.getInstance("AES");

    cipher.init(Cipher.DECRYPT_MODE, keySpec);

    in = new CipherInputStream(in, cipher);
    byte[] buffer = new byte[24];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) != -1)
    {
        out.write(buffer, 0, bytesRead);
        String si = new String(buffer);
        Log.d("Crypto", si);
    }

}

public static byte[] hexStringToByteArray(String s)
{
    int len = s.length();
    byte[] data = new byte[len / 2];
    for(int i = 0; i < len; i += 2)
    {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
}

       }  
