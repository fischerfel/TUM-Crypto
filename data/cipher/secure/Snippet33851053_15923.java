import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class StreamDemo2 {
    static String IV = "AAAAAAAAAAAAAAAA";
    static String encryptionKey = "0123456789abcdef";

    public static void main(String ad[])
    {
        StreamDemo2 st = new StreamDemo2();

        ByteArrayOutputStream baos = new ByteArrayOutputStream ();
        DataOutputStream dos = new DataOutputStream (baos);
        int arr[] = new int[16];

        for(int k = 0 ; k < 16; k++)
            arr[k] = k + 11;

        for(int k = 0 ; k < 16; k++)
            System.out.println(" USER Plain text = " + arr[k]);



        try{
            for(int i = 0; i < 16;i++)
                dos.writeInt (arr[i]);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        byte[] data = baos.toByteArray();
        // conversion to bytes ends here
        // Now follows the mehtod invoking
        byte[] c = null;

        try{
             c = st.encrypt(data, encryptionKey);
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }

        System.out.println("Cipher length = " + c.length);

        // Now follows the code to decrypt

        byte[] d = null;

       try{
            d = st.decrypt(c, encryptionKey);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        ByteArrayInputStream bais = new ByteArrayInputStream (d);
        DataInputStream dis = new DataInputStream (bais);
        int j;

        System.out.println("Original data is : ");
        try{
            for(int k =0; k < 16; k++)
                System.out.print(dis.readInt() + "\t");
            }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public static byte[] encrypt(byte[] ciph, String encryptionKey) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
    cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
    return cipher.doFinal(ciph);
  }

  public static byte[] decrypt(byte[] cipherText, String encryptionKey) throws Exception{
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
    cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
    //return new String(cipher.doFinal(cipherText),"UTF-8"); // changed since the receiveing side expects the byte[]
    return cipher.doFinal(cipherText);
  }
}
